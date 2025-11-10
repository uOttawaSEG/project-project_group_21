package com.example.seg2105_projectui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class TutorActivity extends AppCompatActivity {

    private Button btnManage, btnPastSessions, btnUpcomingSessions, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);

        btnManage = findViewById(R.id.btnManage);
        btnPastSessions = findViewById(R.id.btnPastSessions);
        btnUpcomingSessions = findViewById(R.id.btnUpcomingSessions);
        btnLogout = findViewById(R.id.btnLogout);

        String tutorUsername = getIntent().getStringExtra("username");

        btnLogout.setOnClickListener(v -> {
            Intent intent10 = new Intent(TutorActivity.this, MainActivity.class);
            startActivity(intent10);
            finish();
        });

        btnManage.setOnClickListener(v -> {
            Intent intent11 = new Intent(TutorActivity.this, TutorManageSessionsActivity.class);
            intent11.putExtra("username", tutorUsername);
            startActivity(intent11);

        });

        btnUpcomingSessions.setOnClickListener(v -> {

            Intent intent12 = new Intent(TutorActivity.this, TutorUpcomingSessionsActivity.class);
            intent12.putExtra("username", tutorUsername);
            startActivity(intent12);

        });

        btnPastSessions.setOnClickListener(v -> {

            Intent intent13 = new Intent(TutorActivity.this, TutorUpcomingSessionsActivity.class);
            intent13.putExtra("username", tutorUsername);
            startActivity(intent13);

        });
    }
}
