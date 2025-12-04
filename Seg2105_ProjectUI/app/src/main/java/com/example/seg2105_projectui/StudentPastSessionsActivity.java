package com.example.seg2105_projectui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.time.LocalTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kotlin.text.UStringsKt;

public class StudentPastSessionsActivity extends AppCompatActivity {

    private int currentSessionIndex;
    private List<Sessions> passedSessions;
    private DatabaseHelper dbHelper;

    private TextView displayTutorName, displayDate, displayStartTime, displayCourse, displayYourRating;
    private EditText editYourRating;
    private Button prevButton, nextButton, backButton, submitRatingButton;

    private String loggedInStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_past_sessions);

        dbHelper = new DatabaseHelper(this);

        loggedInStudent = getIntent().getStringExtra("username");

        // error check
        if (loggedInStudent == null || loggedInStudent.isEmpty()) {
            Toast.makeText(this, "Error: No student data found. Returning to previous screen.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        passedSessions = new ArrayList<Sessions>();

        // Initialize UI components
        initializeViews();

        // Set up button actions
        setupButtonClickListeners();
        loadSessionsFromDatabase();

    }



    @Override
    protected void onResume() {
        super.onResume();
    }


    private void initializeViews() {
        displayTutorName = findViewById(R.id.display_tutor_username);
        displayDate = findViewById(R.id.display_date);
        displayStartTime = findViewById(R.id.display_time);
        displayCourse = findViewById(R.id.display_course);
        displayYourRating = findViewById(R.id.display_tutor_rating);
        editYourRating = findViewById(R.id.editRating);
        submitRatingButton = findViewById(R.id.button_update_rating);
        prevButton = findViewById(R.id.button_prev);
        nextButton = findViewById(R.id.button_next);
        backButton = findViewById(R.id.button_back);
    }


    private void loadSessionsFromDatabase() {
        //Showcasing
        dbHelper.createSession("tutortest", "2025-10-10", "12:00", "OldSEG2105", "Pending");
        dbHelper.studentPending("tutortest", "2025-10-10","12:00","studenttest");
        dbHelper.approveStudent("tutortest", "2025-10-10", "12:00", "studenttest");

            getPassedSessions(loggedInStudent);
            if (passedSessions == null || passedSessions.isEmpty()) {
                currentSessionIndex = -1;
            } else if (currentSessionIndex >= passedSessions.size()) {
                currentSessionIndex = passedSessions.size() - 1;
            }
            updateDisplay();


    }

    private void setupButtonClickListeners() {
        prevButton.setOnClickListener(v -> {
            if (currentSessionIndex > 0) {
                currentSessionIndex--;
                updateDisplay();
            }
        });

        nextButton.setOnClickListener(v -> {
            if (currentSessionIndex < passedSessions.size() - 1) {
                currentSessionIndex++;
                updateDisplay();
            }
        });

        submitRatingButton.setOnClickListener(v -> {
            if (!(currentSessionIndex != -1 && !passedSessions.isEmpty())) {
                Toast.makeText(this, "No Sessions to Rate.", Toast.LENGTH_SHORT).show();
                return;
            }
            String rating = editYourRating.getText().toString().trim();

            if (rating.isEmpty()){
                Toast.makeText(this, "Please enter a numerical rating value between 1 and 5.", Toast.LENGTH_SHORT).show();
                return;
            }
            int intRating = 0;
            try {
                intRating = Integer.parseInt(rating);
            } catch (NumberFormatException e){
                Toast.makeText(this, "Please enter a numerical rating value between 1 and 5.", Toast.LENGTH_SHORT).show();
                return;
            }
            intRating = Integer.parseInt(rating);
            if (intRating > 0 && intRating < 6){
                dbHelper.rate(loggedInStudent, passedSessions.get(currentSessionIndex).tutorUsername,intRating);
                Toast.makeText(this, "Tutor Rated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter a numerical rating value between 1 and 5.", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(v -> {
            //change TutorViewPending to whatever is supposed to connect to this page
            Intent backToDash = new Intent(StudentPastSessionsActivity.this, StudentDashboardActivity.class);
            backToDash.putExtra("username", loggedInStudent);
            startActivity(backToDash);
            finish();
        });
    }


    private void updateDisplay() {
        if (passedSessions == null || passedSessions.isEmpty()) {
            displayTutorName.setText("No Passed Sessions.");
            displayDate.setText("Please Attend a Session First.");
            displayStartTime.setText("");
            displayCourse.setText("");
            displayYourRating.setText("");
            currentSessionIndex = -1;
        } else {

            if (currentSessionIndex < 0) {
                currentSessionIndex = 0;
            }
            Sessions currentSession = passedSessions.get(currentSessionIndex);

            displayTutorName.setText("Tutor: " + dbHelper.getTutor(currentSession.tutorUsername).getFirstName() + " " + dbHelper.getTutor(currentSession.tutorUsername).getLastName());
            displayDate.setText("Date: " + currentSession.date);
            displayStartTime.setText("Start Time: " + currentSession.startTime);
            displayCourse.setText("Course: " + currentSession.course);
            displayYourRating.setText("");

        }

        boolean hasSessions = passedSessions != null && !passedSessions.isEmpty();
        prevButton.setEnabled(hasSessions && currentSessionIndex > 0);
        nextButton.setEnabled(hasSessions && currentSessionIndex < passedSessions.size() - 1);
        submitRatingButton.setEnabled(hasSessions);
    }

    private void getPassedSessions(String studentName){
        List<Sessions> allSessions = dbHelper.studentSessions(studentName,"Accepted");
        for (int i = 0; i < allSessions.size(); i++){
            if (hasSessionPast(allSessions.get(i))){
                passedSessions.add(allSessions.get(i));
            }
        }
    }

    public static boolean hasSessionPast(Sessions session) {
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

            //return true if less than 0 hours away
            return hoursDifference < 0;

        } catch (ParseException e) {
            return false;
        }
    }

}
