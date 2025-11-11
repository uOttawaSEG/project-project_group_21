package com.example.seg2105_projectui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TutorPastSessionsActivity extends AppCompatActivity {

    private TextView displayTutor, displayDate, displayStartTime, displayStudentName;
    private TextView titleText;

    private List<Sessions> pastSessions;
    private int sessionIndex = 0;
    private DatabaseHelper dbHelper;
    private String tutorUsername;

    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_past_sessions);

        dbHelper = new DatabaseHelper(this);
        tutorUsername = getIntent().getStringExtra("username");


        displayTutor = findViewById(R.id.display_username_past);
        displayDate = findViewById(R.id.display_date_past);
        displayStartTime = findViewById(R.id.display_starttime_past);
        displayStudentName = findViewById(R.id.display_students_past);

        titleText = findViewById(R.id.pastSessionsTitle);
        titleText.setText("Past Sessions");


        Button btnPrevPast = findViewById(R.id.btnPrevPast);
        Button btnBackPast = findViewById(R.id.btnBackPast);
        Button btnNextPast = findViewById(R.id.btnNextPast);


        List<Sessions> allSessions = dbHelper.getTutorSessions(tutorUsername);


        pastSessions = new ArrayList<>();
        Date now = new Date();
        for (Sessions s : allSessions) {
            List<String> approved = dbHelper.getApprovedStudents(s.getTutorUsername(), s.getDate(), s.getStartTime());
            try {
                Date sessionDateTime = dateTimeFormat.parse(s.getDate() + " " + s.getStartTime());
                if (sessionDateTime != null && sessionDateTime.before(now) && !approved.isEmpty()) {
                    pastSessions.add(s);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (pastSessions.isEmpty()) {
            showNoSessions();
        } else {
            sessionIndex = 0;
            displaySession(pastSessions.get(sessionIndex));
        }

        btnNextPast.setOnClickListener(v -> showNextSession());
        btnPrevPast.setOnClickListener(v -> showPreviousSession());

        btnBackPast.setOnClickListener(v -> {
            Intent intent = new Intent(TutorPastSessionsActivity.this, TutorActivity.class);
            intent.putExtra("username", tutorUsername);
            startActivity(intent);
            finish();
        });
    }

    private void showNextSession() {
        if (pastSessions.isEmpty()) return;
        sessionIndex++;
        if (sessionIndex >= pastSessions.size()) sessionIndex = 0;
        displaySession(pastSessions.get(sessionIndex));
    }

    private void showPreviousSession() {
        if (pastSessions.isEmpty()) return;
        sessionIndex--;
        if (sessionIndex < 0) sessionIndex = pastSessions.size() - 1;
        displaySession(pastSessions.get(sessionIndex));
    }

    private void displaySession(Sessions session) {
        displayTutor.setText("Tutor: " + session.getTutorUsername());
        displayDate.setText("Date: " + session.getDate());
        displayStartTime.setText("Start Time: " + session.getStartTime());

        List<String> approved = dbHelper.getApprovedStudents(session.getTutorUsername(), session.getDate(), session.getStartTime());
        displayStudentName.setText("Students: " + approved);
    }

    private void showNoSessions() {
        displayTutor.setText("");
        displayDate.setText("");
        displayStartTime.setText("No past sessions found.");
        displayStudentName.setText("");
    }
}
