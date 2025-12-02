package com.example.seg2105_projectui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StudentCancelSessionsActivity extends AppCompatActivity {

    // Display elements
    private TextView displayTutorUsername;
    private TextView displayDate;
    private TextView displayTime;
    private TextView displayCourse;
    private TextView displayStatus;
    private TextView displayMessage;

    // Session data
    private List<Sessions> pendingSessions;
    private List<Sessions> approvedSessions;
    private Sessions currentSession;
    private int currentSessionIndex;
    private String currentStudentUsername;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_cancel_sessions);

        dbHelper = new DatabaseHelper(this);

        //get student username code, kinda forgot how to do it so gl
        currentStudentUsername = getIntent().getStringExtra("username");
        if (currentStudentUsername == null) {
            //fallback - change this if need be
            currentStudentUsername = "tester"; // For testing
        }

        initializeViews();

        loadSessions();

        setupButtonListeners();

        //display first session if available
        if (!pendingSessions.isEmpty())
        {
            displaySession(0, "pending");
        }
        else if (!approvedSessions.isEmpty())
        {
            displaySession(0, "approved");
        } else {
            updateScreenNoSessions();
        }
    }

    private void initializeViews() {
        displayTutorUsername = findViewById(R.id.display_tutor_username);
        displayDate = findViewById(R.id.display_date);
        displayTime = findViewById(R.id.display_time);
        displayCourse = findViewById(R.id.display_course);
        displayStatus = findViewById(R.id.display_status);
        displayMessage = findViewById(R.id.display_message);
    }

    private void loadSessions() {
        pendingSessions = dbHelper.studentSessions(currentStudentUsername, "Pending");

        approvedSessions = dbHelper.studentSessions(currentStudentUsername, "Approved");

        currentSessionIndex = 0;
    }

    //this will work once studentactivity is made
    private void setupButtonListeners() {
        Button btnBack = findViewById(R.id.button_back);
        btnBack.setOnClickListener(v -> {
            Intent backToDash = new Intent(StudentCancelSessionsActivity.this, StudentDashboardActivity.class);
            backToDash.putExtra("username", currentStudentUsername);
            startActivity(backToDash);
            finish();
        });

        Button btnCancel = findViewById(R.id.button_cancel);
        btnCancel.setOnClickListener(v -> cancelCurrentSession());

        Button btnNext = findViewById(R.id.button_next);
        btnNext.setOnClickListener(v -> showNextSession());

        Button btnPrev = findViewById(R.id.button_prev);
        btnPrev.setOnClickListener(v -> showPreviousSession());
    }

    private void displaySession(int index, String sessionType) {
        if (sessionType.equals("pending") && index < pendingSessions.size())
        {
            currentSession = pendingSessions.get(index);
            currentSessionIndex = index;
            updateScreen("pending");
        }
        else if (sessionType.equals("approved") && index < approvedSessions.size())
        {
            currentSession = approvedSessions.get(index);
            currentSessionIndex = index;
            updateScreen("approved");
        }
        else
        {
            updateScreenNoSessions();
        }
    }

    private void showNextSession() {
        //try pending sessions
        if (currentSessionIndex + 1 < pendingSessions.size()) {
            displaySession(currentSessionIndex + 1, "pending");
        }
        //try approved sessions
        else if (!approvedSessions.isEmpty()) {
            // If we were in pending sessions and reached the end, switch to approved
            if (currentSession != null && pendingSessions.contains(currentSession)) {
                displaySession(0, "approved");
            }
            //go to next approved session from previous approved session
            else if (currentSessionIndex + 1 < approvedSessions.size())
            {
                displaySession(currentSessionIndex + 1, "approved");
            }
        }
        else
        {
            //no more sessions :(
            Toast.makeText(this, "No more sessions", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPreviousSession() {
        //try pending sessions
        if (currentSessionIndex - 1 >= 0)
        {
            if (currentSession != null && pendingSessions.contains(currentSession))
            {
                displaySession(currentSessionIndex - 1, "pending");
            }
            else if (currentSession != null && approvedSessions.contains(currentSession))
            {
                displaySession(currentSessionIndex - 1, "approved");
            }
        }
        //if at the first of the of approved sessions, switch to last pending session
        else if (currentSessionIndex == 0 && currentSession != null && approvedSessions.contains(currentSession) && !pendingSessions.isEmpty()) {
            displaySession(pendingSessions.size() - 1, "pending");
        }
        else
        {
            Toast.makeText(this, "No previous sessions", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelCurrentSession() {
        if (currentSession == null)
        {
            Toast.makeText(this, "No session to cancel", Toast.LENGTH_SHORT).show();
            return;
        }

        String sessionType = pendingSessions.contains(currentSession) ? "pending" : "approved";

        if (sessionType.equals("pending"))
        {
            //cancel pending
            cancelPendingSession();
        }
        else
        {
            //for approved sessions, check if it's more than 24 hours away
            if (isSessionMoreThan24HoursAway(currentSession)) {
                cancelApprovedSession();
            }
            else
            {
                Toast.makeText(this, "Cannot cancel approved session within 24 hours of start time", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void cancelPendingSession() {
        // Remove student from pending list
        dbHelper.rejectStudent(
                currentSession.tutorUsername,
                currentSession.date,
                currentSession.startTime,
                currentStudentUsername
        );

        Toast.makeText(this, "Pending session cancelled successfully", Toast.LENGTH_SHORT).show();

        //refresh sessions and display
        loadSessions();
        if (!pendingSessions.isEmpty())
        {
            displaySession(0, "pending");
        }
        else if
        (!approvedSessions.isEmpty())
        {
            displaySession(0, "approved");
        }
        else
        {
            updateScreenNoSessions();
        }
    }

    private void cancelApprovedSession() {
        //move student from approved to cancelled
        dbHelper.cancelStudent(
                currentSession.tutorUsername,
                currentSession.date,
                currentSession.startTime,
                currentStudentUsername
        );

        Toast.makeText(this, "Approved session cancelled successfully", Toast.LENGTH_SHORT).show();

        //refresh sessions and display
        loadSessions();
        if (!pendingSessions.isEmpty())
        {
            displaySession(0, "pending");
        }
        else if (!approvedSessions.isEmpty())
        {
            displaySession(0, "approved");
        } else {
            updateScreenNoSessions();
        }
    }

    private boolean isSessionMoreThan24HoursAway(Sessions session) {
        try
        {
            String sessionDateTimeStr = session.date + " " + session.startTime;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date sessionDateTime = sdf.parse(sessionDateTimeStr);

            //get current date and time
            Date currentDateTime = new Date();

            //calculate difference in milliseconds
            long differenceMs = sessionDateTime.getTime() - currentDateTime.getTime();
            long hoursDifference = differenceMs / (1000 * 60 * 60);

            //return true if more than 24 hours away
            return hoursDifference > 24;

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateScreen(String sessionType) {
        if (currentSession == null) return;

        displayTutorUsername.setText("Tutor: " + currentSession.tutorUsername);
        displayDate.setText("Date: " + currentSession.date);
        displayTime.setText("Time: " + currentSession.startTime);
        displayCourse.setText("Course: " + currentSession.course);
        displayStatus.setText("Status: " + currentSession.status);

        if (sessionType.equals("pending")) {
            displayMessage.setText("You can cancel this pending session");
        } else {
            if (isSessionMoreThan24HoursAway(currentSession)) {
                displayMessage.setText("You can cancel this approved session (more than 24 hours away)");
            } else {
                displayMessage.setText("Cannot cancel - session starts within 24 hours");
            }
        }
    }

    private void updateScreenNoSessions() {
        displayTutorUsername.setText("No sessions available");
        displayDate.setText("");
        displayTime.setText("");
        displayCourse.setText("");
        displayStatus.setText("");
        displayMessage.setText("You have no sessions to cancel");
        currentSession = null;
    }
}