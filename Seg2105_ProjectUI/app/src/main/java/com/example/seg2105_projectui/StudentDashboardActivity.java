package com.example.seg2105_projectui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StudentDashboardActivity extends AppCompatActivity {

    private Button btnSearchSessions, btnRateSessions, btnRequestedSessions, btnCancelSessions, btnLogout;


    private String studentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        btnSearchSessions = findViewById(R.id.btnSearchSessions);
        btnRateSessions = findViewById(R.id.btnRateSessions);
        btnRequestedSessions = findViewById(R.id.btnRequestedSessions);
        btnCancelSessions = findViewById(R.id.btnCancelSessions);
        btnLogout = findViewById(R.id.btnLogoutStudent);

        studentUsername = getIntent().getStringExtra("username");

        btnLogout.setOnClickListener(v -> {
            Intent StudentLogOut = new Intent(StudentDashboardActivity.this, MainActivity.class);
            startActivity(StudentLogOut);
            finish();
        });

        btnSearchSessions.setOnClickListener(v -> {
            Intent SearchSession = new Intent(StudentDashboardActivity.this, StudentSessionSearchActivity.class);
            SearchSession.putExtra("username", studentUsername);
            startActivity(SearchSession);

        });

        btnRateSessions.setOnClickListener(v -> {

            Intent RateSession = new Intent(StudentDashboardActivity.this, StudentPastSessionsActivity.class);
            RateSession.putExtra("username", studentUsername);
            startActivity(RateSession);

        });

        btnRequestedSessions.setOnClickListener(v -> {

            Intent RequestedSession = new Intent(StudentDashboardActivity.this, StudentViewRequestsActivity.class);
            RequestedSession.putExtra("username", studentUsername);
            startActivity(RequestedSession);

        });

        btnCancelSessions.setOnClickListener(v -> {

            Intent CancelSession = new Intent(StudentDashboardActivity.this, StudentCancelSessionsActivity.class);
            CancelSession.putExtra("username", studentUsername);
            startActivity(CancelSession);

        });
    }


}
