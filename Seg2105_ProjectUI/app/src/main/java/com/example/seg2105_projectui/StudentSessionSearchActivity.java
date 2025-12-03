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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentSessionSearchActivity extends AppCompatActivity {

    private int currentSessionIndex = 0;
    private List<Sessions> courseSessions;
    private DatabaseHelper dbHelper;

    private TextView displayTutorName, displayDate, displayStartTime, displayCourse, displayRating;
    private EditText editCourseSearch;
    private Button prevButton, nextButton, backButton, courseSearchButton, requestButton;

    private String loggedInStudent, selectedCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_session_search);

        dbHelper = new DatabaseHelper(this);

        loggedInStudent = getIntent().getStringExtra("username");

        // error check
        if (loggedInStudent == null || loggedInStudent.isEmpty()) {
            Toast.makeText(this, "Error: No student data found. Returning to previous screen.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        // Initialize UI components
        initializeViews();

        // Set up button actions
        setupButtonClickListeners();

        selectedCourse = "RANDOMSTRINGOFCHARACTERSTOSEARCHWITH";
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
        displayRating = findViewById(R.id.display_rating);
        editCourseSearch = findViewById(R.id.edit_coursesearch);
        courseSearchButton = findViewById(R.id.button_search_sessions);
        requestButton = findViewById(R.id.button_request);
        prevButton = findViewById(R.id.button_previous_session);
        nextButton = findViewById(R.id.button_next_session);
        backButton = findViewById(R.id.button_back_from_search);
    }


    private void loadSessionsFromDatabase() {
            courseSessions = dbHelper.searchSession(loggedInStudent,selectedCourse);
            if (courseSessions == null || courseSessions.isEmpty()) {
                currentSessionIndex = -1;
            } else if (currentSessionIndex >= courseSessions.size()) {
                currentSessionIndex = courseSessions.size() - 1;
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
            if (currentSessionIndex < courseSessions.size() - 1) {
                currentSessionIndex++;
                updateDisplay();
            }
        });

        courseSearchButton.setOnClickListener(v -> {
            selectedCourse = editCourseSearch.getText().toString().trim();
            if (selectedCourse.isEmpty()){
                Toast.makeText(this, "Please enter a course code (e.g., SEG2105)", Toast.LENGTH_SHORT).show();
                selectedCourse = "";
                editCourseSearch.setText("");
                return;
            }
            editCourseSearch.setText("");
            loadSessionsFromDatabase();
        });

        requestButton.setOnClickListener(v -> {
            if (!(currentSessionIndex != -1 && !courseSessions.isEmpty())) {
                Toast.makeText(this, "No Sessions to Request.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (sessionTimeConflict()){
                Toast.makeText(this, "This Session Overlaps with another Request.", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.studentPending(courseSessions.get(currentSessionIndex).tutorUsername, courseSessions.get(currentSessionIndex).date,courseSessions.get(currentSessionIndex).startTime,loggedInStudent);
            loadSessionsFromDatabase();
            Toast.makeText(this, "Session Requested!", Toast.LENGTH_SHORT).show();
        });

        backButton.setOnClickListener(v -> {
            Intent backToDash = new Intent(StudentSessionSearchActivity.this, StudentDashboardActivity.class);
            backToDash.putExtra("username", loggedInStudent);
            startActivity(backToDash);
            finish();
        });
    }


    private void updateDisplay() {
        if (courseSessions == null || courseSessions.isEmpty()) {
            displayCourse.setText("");
            displayTutorName.setText("No Sessions Found.");
            displayRating.setText("Please enter a Course Code");
            displayDate.setText("");
            displayStartTime.setText("");
            currentSessionIndex = -1;
        }

        else {
            if (currentSessionIndex < 0) {
                currentSessionIndex = 0;
            }
            Sessions currentSession = courseSessions.get(currentSessionIndex);

            displayCourse.setText("Course: " + currentSession.course);
            displayTutorName.setText("Tutor: " + dbHelper.getTutor(currentSession.tutorUsername).getFirstName() + " " + dbHelper.getTutor(currentSession.tutorUsername).getLastName());
            displayRating.setText("Rating: " + dbHelper.getRating(currentSession.tutorUsername));
            displayDate.setText("Date: " + currentSession.date);
            displayStartTime.setText("Starts: " + currentSession.startTime);
        }


        boolean hasSessions = courseSessions != null && !courseSessions.isEmpty();
        prevButton.setEnabled(hasSessions && currentSessionIndex > 0);
        nextButton.setEnabled(hasSessions && currentSessionIndex < courseSessions.size() - 1);
        requestButton.setEnabled(hasSessions);
    }

    private boolean sessionTimeConflict(){

        int newStartTimeInt = Integer.parseInt(courseSessions.get(currentSessionIndex).startTime.replace(":", ""));

        int timeCalc = newStartTimeInt%100;
        int newEndTimeInt = newStartTimeInt - timeCalc; //gets just the hours
        timeCalc += 30; //add the 30 min
        if (timeCalc >= 60){
            timeCalc += 40; //If it's an hour add 100, then remove 60: 100-60 --> +40
        }
        newEndTimeInt += timeCalc;

        //REPLACE WITH DATABASE METHOD TO GET ALL SESSIONS, BOTH REQUESTED AND ACCEPTED
        List<Sessions> studentRequestedSessions = dbHelper.studentSessions(loggedInStudent, "Accepting");

        for (Sessions existingSession : studentRequestedSessions) {
            if (existingSession.date.equals(courseSessions.get(currentSessionIndex).date)) {
                int existingStartTimeInt = Integer.parseInt(existingSession.startTime.replace(":", ""));

                timeCalc = existingStartTimeInt%100;
                int existingEndTimeInt = existingStartTimeInt - timeCalc; //gets just the hours
                timeCalc += 29; //add 29 min so the final hour can overlap allowing b2b sessions
                if (timeCalc >= 60){
                    timeCalc += 40; //If it's an hour add 100, then remove 60: 100-60 --> +40
                }
                existingEndTimeInt += timeCalc;

                if (newStartTimeInt < existingEndTimeInt && newEndTimeInt > existingStartTimeInt) {
                    return true; // Overlap detected
                }
            }
        }


        return false; // No overlaps found
    }



}


