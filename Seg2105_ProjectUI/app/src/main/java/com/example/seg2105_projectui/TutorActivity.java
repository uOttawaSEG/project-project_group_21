package com.example.seg2105_projectui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        btnLogout.setOnClickListener(v -> {
            Intent intent3 = new Intent(TutorActivity.this, MainActivity.class);
            startActivity(intent3);
            finish();
        });

        btnManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //requests page

            }
        });

        btnPastSessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //past sessions page

            }
        });

        btnUpcomingSessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //upcoming sessions page

            }
        });
    }
}
