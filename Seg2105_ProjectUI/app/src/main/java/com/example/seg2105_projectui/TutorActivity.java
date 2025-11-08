package com.example.seg2105_projectui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class TutorActivity extends AppCompatActivity {

    private Button btnViewRequests, btnPastSessions, btnUpcomingSessions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);

        btnViewRequests = findViewById(R.id.btnViewRequests);
        btnPastSessions = findViewById(R.id.btnPastSessions);
        btnUpcomingSessions = findViewById(R.id.btnUpcomingSessions);

        btnViewRequests.setOnClickListener(new View.OnClickListener() {
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
