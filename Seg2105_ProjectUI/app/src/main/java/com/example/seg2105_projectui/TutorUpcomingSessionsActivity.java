package com.example.seg2105_projectui;

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

public class TutorUpcomingSessionsActivity extends AppCompatActivity {

    private TextView displayTutor, displayDate, displayStartTime, displayStudentName;
    private TextView titleText;

    private List<Sessions> upcomingSessions;
    private int sessionIndex = 0;
    private DatabaseHelper dbHelper;
    private String tutorUsername;

    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_upcoming_sessions);

        dbHelper = new DatabaseHelper(this);
        tutorUsername = getIntent().getStringExtra("username");


        displayTutor = findViewById(R.id.display_username_rejections);
        displayDate = findViewById(R.id.display_firstname_rejections);
        displayStartTime = findViewById(R.id.display_lastName_rejections);
        displayStudentName = findViewById(R.id.display_highestDegree_rejections);

        titleText = findViewById(R.id.textRejectedRequests);
        titleText.setText("Upcoming Sessions");

        Button nextButton = findViewById(R.id.nextButton2);
        Button prevButton = findViewById(R.id.prevButton2);
        Button backButton = findViewById(R.id.buttonBackToSelection2);
        Button dummyButton = findViewById(R.id.approveButton2);
        dummyButton.setText("No Action");

        List<Sessions> allSessions = dbHelper.getTutorSessions(tutorUsername);


        upcomingSessions = new ArrayList<>();
        Date now = new Date();
        for (Sessions s : allSessions)
        {
            List<String> approved = dbHelper.getApprovedStudents(s.getTutorUsername(), s.getDate(), s.getStartTime());
            try
            {
                Date sessionDateTime = dateTimeFormat.parse(s.getDate() + " " + s.getStartTime());
                if (sessionDateTime != null && sessionDateTime.after(now) && !approved.isEmpty())
                {
                    upcomingSessions.add(s);
                }
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }

        if (upcomingSessions.isEmpty())
        {
            showNoSessions();
        }
        else
        {
            sessionIndex = 0;
            displaySession(upcomingSessions.get(sessionIndex));
        }

        nextButton.setOnClickListener(v -> showNextSession());
        prevButton.setOnClickListener(v -> showPreviousSession());

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(TutorUpcomingSessionsActivity.this, TutorActivity.class);
            intent.putExtra("username", tutorUsername);
            startActivity(intent);
            finish();
        });
    }

    private void showNextSession()
    {
        if (upcomingSessions.isEmpty()) return;
        sessionIndex++;
        if (sessionIndex >= upcomingSessions.size()) sessionIndex = 0;
        displaySession(upcomingSessions.get(sessionIndex));
    }

    private void showPreviousSession()
    {
        if (upcomingSessions.isEmpty()) return;
        sessionIndex--;
        if (sessionIndex < 0) sessionIndex = upcomingSessions.size() - 1;
        displaySession(upcomingSessions.get(sessionIndex));
    }

    private void displaySession(Sessions session)
    {
        displayTutor.setText("Tutor: " + session.getTutorUsername());
        displayDate.setText("Date: " + session.getDate());
        displayStartTime.setText("Start Time: " + session.getStartTime());

        List<String> approved = dbHelper.getApprovedStudents(session.getTutorUsername(), session.getDate(), session.getStartTime());
        displayStudentName.setText("Students: " + approved);
    }

    private void showNoSessions()
    {
        displayTutor.setText("");
        displayDate.setText("");
        displayStartTime.setText("No upcoming sessions found.");
        displayStudentName.setText("");
    }
}
