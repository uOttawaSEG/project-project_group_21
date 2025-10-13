package com.example.sqlite;



import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity_sqlite extends AppCompatActivity {

    private EditText etTitle, etBody;
    private TextView tvOutput;
    private NotesDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sqlite);

        etTitle = findViewById(R.id.etTitle);
        etBody  = findViewById(R.id.etBody);
        tvOutput = findViewById(R.id.tvOutput);

        db = new NotesDb(this);

        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnShow = findViewById(R.id.btnShow);
        Button btnClear = findViewById(R.id.btnClear);

        btnAdd.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String body  = etBody.getText().toString().trim();
            if (title.isEmpty()) {
                Toast.makeText(this, "Title required", Toast.LENGTH_SHORT).show();
                return;
            }
            long id = db.insert(title, body);
            if (id > 0) {
                etTitle.setText("");
                etBody.setText("");
                Toast.makeText(this, "Inserted row id=" + id, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Insert failed", Toast.LENGTH_SHORT).show();
            }
        });

        btnShow.setOnClickListener(v -> tvOutput.setText(db.dumpAll()));

        btnClear.setOnClickListener(v -> {
            int rows = db.clear();
            Toast.makeText(this, "Deleted " + rows + " rows", Toast.LENGTH_SHORT).show();
            tvOutput.setText("(results appear here)");
        });
    }

    /** Ultra-minimal SQLite helper */
    static class NotesDb extends SQLiteOpenHelper {
        private static final String DB_NAME = "demo.db";
        private static final int DB_VERSION = 1;

        private static final String T_NOTES = "notes";
        private static final String C_ID = "_id";
        private static final String C_TITLE = "title";
        private static final String C_BODY = "body";
        private static final String C_CREATED = "created_at";



        NotesDb(MainActivity_sqlite ctx) {
            super(ctx, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            // This line disables WAL, allowing the Database Inspector to refresh in real-time.
            db.disableWriteAheadLogging();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + T_NOTES + " (" +
                    C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    C_TITLE + " TEXT NOT NULL, " +
                    C_BODY + " TEXT, " +
                    C_CREATED + " INTEGER NOT NULL)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
            db.execSQL("DROP TABLE IF EXISTS " + T_NOTES);
            onCreate(db);
        }

        long insert(String title, String body) {
            ContentValues cv = new ContentValues();
            cv.put(C_TITLE, title);
            cv.put(C_BODY, body);
            cv.put(C_CREATED, System.currentTimeMillis());
            return getWritableDatabase().insert(T_NOTES, null, cv);
        }

        String dumpAll() {
            StringBuilder sb = new StringBuilder();
            SQLiteDatabase rdb = getReadableDatabase();
            Cursor c = rdb.query(T_NOTES,
                    new String[]{C_ID, C_TITLE, C_BODY, C_CREATED},
                    null, null, null, null,
                    C_CREATED + " DESC");

            try {
                if (c.getCount() == 0) return "(no rows)";
                while (c.moveToNext()) {
                    long id = c.getLong(0);
                    String title = c.getString(1);
                    String body = c.getString(2);
                    long created = c.getLong(3);
                    sb.append("#").append(id)
                            .append(" | ").append(title)
                            .append(body == null || body.isEmpty() ? "" : " — " + body)
                            .append(" | ").append(created)
                            .append("\n");
                }
            } finally {
                c.close();
            }
            return sb.toString();
        }

        int clear() {
            return getWritableDatabase().delete(T_NOTES, null, null);
        }
    }
}

