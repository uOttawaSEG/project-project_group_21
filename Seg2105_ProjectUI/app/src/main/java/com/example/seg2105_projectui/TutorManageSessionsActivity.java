package com.example.seg2105_projectui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Date;
import java.util.Locale;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

public class TutorManageSessionsActivity extends AppCompatActivity {

    private String loggedInTutor;
    private List<Sessions> tutorSessions;
    private int currentSessionIndex;
    private DatabaseHelper dbHelper;

    private TextView displayDate, displayStartTime, displayEndTime;
    private EditText editDate, editStartTime, editEndTime, editCourseCode;
    private Button prevButton, nextButton, deleteButton, createButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sessions);

        dbHelper = new DatabaseHelper(this);


        loggedInTutor = getIntent().getStringExtra("username");

        // error check
        if (loggedInTutor == null || loggedInTutor.isEmpty()) {
            Toast.makeText(this, "Error: No tutor data found. Returning to previous screen.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Initialize UI components
        initializeViews();

        // Set up button actions
        setupButtonClickListeners();


    }


    @Override
    protected void onResume() {
        super.onResume();
        // Load sessions and update the display every time the activity is shown.
        loadSessionsFromDatabase();
    }


    private void initializeViews() {
        displayDate = findViewById(R.id.display_date_pending2);
        displayStartTime = findViewById(R.id.display_starttime_pending2);
        displayEndTime = findViewById(R.id.display_endtime_pending2);
        editDate = findViewById(R.id.editTextDate);
        editStartTime = findViewById(R.id.editTextStartTime);
        editEndTime = findViewById(R.id.editTextEndTime);
        editCourseCode = findViewById(R.id.editTextCourseCode);
        prevButton = findViewById(R.id.prevButton1);
        nextButton = findViewById(R.id.nextButton1);
        deleteButton = findViewById(R.id.deleteButton);
        createButton = findViewById(R.id.createSessionButton);
        backButton = findViewById(R.id.buttonBackToSelection3);


        editEndTime.setVisibility(View.GONE);
    }


    private void loadSessionsFromDatabase() {
        tutorSessions = dbHelper.getTutorSessions(loggedInTutor);

        if (tutorSessions == null || tutorSessions.isEmpty()) {
            currentSessionIndex = -1;
        } else if (currentSessionIndex >= tutorSessions.size()) {
            currentSessionIndex = tutorSessions.size() - 1;
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
            if (currentSessionIndex < tutorSessions.size() - 1) {
                currentSessionIndex++;
                updateDisplay();
            }
        });

        deleteButton.setOnClickListener(v -> {
            if (currentSessionIndex != -1 && !tutorSessions.isEmpty()) {
                Sessions sessionToDelete = tutorSessions.get(currentSessionIndex);
                if(sessionToDelete.getStatus().equals("Approved") ){
                    Toast.makeText(this, "Cannot Delete an Approved Session", Toast.LENGTH_SHORT).show();
                }
                else {
                    dbHelper.deleteSession(sessionToDelete);
                    loadSessionsFromDatabase();
                    updateDisplay();
                    //Toast.makeText(this, "Delete not implemented in DatabaseHelper.", Toast.LENGTH_SHORT).show(); //should be added
                    Toast.makeText(this, "Availability Deleted!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "No availability to delete.", Toast.LENGTH_SHORT).show();
            }
        });

        createButton.setOnClickListener(v -> {
            String dateStr = editDate.getText().toString().trim();
            String startTimeStr = editStartTime.getText().toString().trim();
            String courseStr = editCourseCode.getText().toString().trim();


            if (dateStr.isEmpty() || startTimeStr.isEmpty()) {
                Toast.makeText(this, "Please fill Date and Start Time.", Toast.LENGTH_SHORT).show();
                return;
            }


            if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}") || !startTimeStr.matches("\\d{2}:\\d{2}")) {
                Toast.makeText(this, "Invalid format. Use YYYY-MM-DD and HH:mm.", Toast.LENGTH_LONG).show();
                return;
            }


            if (isDateInPast(dateStr)) {
                Toast.makeText(this, "Cannot create availability for a past date.", Toast.LENGTH_LONG).show();
                return;
            }

            if (!isDateValid(dateStr)) {
                Toast.makeText(this, "Cannot create availability for an invalid date.", Toast.LENGTH_LONG).show();
                return;
            }

            if(isOnTheHourOrHalf(dateStr, startTimeStr)){
                Toast.makeText(this, "Time Slots must start on the hour, or on the half-hour.", Toast.LENGTH_LONG).show();
                return;
            }

            if(isValidHour(startTimeStr)){
                Toast.makeText(this, "Cannot create availability for an invalid.", Toast.LENGTH_LONG).show();
                return;
            }

            if (isSlotOverlapping(dateStr, startTimeStr)) {
                Toast.makeText(this, "This time slot overlaps with an existing session or availability.", Toast.LENGTH_LONG).show();
                return;
            }

            if (courseStr.matches("")){
                Toast.makeText(this, "Please enter a course code.", Toast.LENGTH_LONG).show();
                return;
            }

            // 3. If all validation passes, create the session
            dbHelper.createSession(loggedInTutor, dateStr, startTimeStr, courseStr, "Pending");
            loadSessionsFromDatabase();
            currentSessionIndex = tutorSessions.size() - 1;
            updateDisplay();

            editDate.setText("");
            editStartTime.setText("");
            editCourseCode.setText("");
            Toast.makeText(this, "New availability created!", Toast.LENGTH_SHORT).show();

        });


        backButton.setOnClickListener(v -> finish());
    }


    private void updateDisplay() {
        if (tutorSessions == null || tutorSessions.isEmpty()) {
            displayDate.setText("No availability available.");
            displayStartTime.setText("Create one below.");
            displayEndTime.setText("");
            currentSessionIndex = -1;
        } else {

            if (currentSessionIndex < 0) {
                currentSessionIndex = 0;
            }
            Sessions currentSession = tutorSessions.get(currentSessionIndex);
            displayDate.setText("Date: " + currentSession.date);
            displayStartTime.setText("Start Time: " + currentSession.startTime);
            displayEndTime.setText("");

        }


        boolean hasSessions = tutorSessions != null && !tutorSessions.isEmpty();
        prevButton.setEnabled(hasSessions && currentSessionIndex > 0);
        nextButton.setEnabled(hasSessions && currentSessionIndex < tutorSessions.size() - 1);
        deleteButton.setEnabled(hasSessions);
    }

    private boolean isDateInPast(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayStr = sdf.format(new Date());
        return dateStr.compareTo(todayStr) < 0;
    }

    private boolean isDateValid(String dateStr) {
        int intDate = Integer.parseInt(dateStr.replace("-", ""));
        int year = intDate/10000;
        int month = intDate%10000/100;
        int day = intDate%100;
        if (month >= 1 && month <= 12){
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
                return (day >= 1 && day <= 31);
            } else if (month == 4 || month == 6 || month == 9 || month == 11){
                return day >= 1 && day <= 30;
            } else {//if feb
                if (year%4 == 0){//leap year lol
                    return day >= 1 && day <= 29;
                }
                else {
                    return day >= 1 && day <= 28;
                }
            }
        }
        return false;
    }

    private boolean isOnTheHourOrHalf(String newDate, String newStartTime){
        int newStartTimeInt = Integer.parseInt(newStartTime.replace(":", ""));
        return !((newStartTimeInt % 100) % 30 == 0); //return if the minute is either 30 or 00
    }

    private boolean isValidHour(String newStartTime){
        int newStartTimeInt = Integer.parseInt(newStartTime.replace(":", ""));
        return ((newStartTimeInt / 100) >= 23); //return if the time is in the actual day
    }


    private boolean isSlotOverlapping(String newDate, String newStartTime) {
        int newStartTimeInt = Integer.parseInt(newStartTime.replace(":", ""));

        int timeCalc = newStartTimeInt%100;
        int newEndTimeInt = newStartTimeInt - timeCalc; //gets just the hours
        timeCalc += 30; //add the 30 min
        if (timeCalc >= 60){
            timeCalc += 40; //If it's an hour add 100, then remove 60: 100-60 --> +40
        }
        newEndTimeInt += timeCalc;


        for (Sessions existingSession : tutorSessions) {
            if (existingSession.date.equals(newDate)) {
                int existingStartTimeInt = Integer.parseInt(existingSession.startTime.replace(":", ""));

                //int existingEndTimeInt = existingStartTimeInt + 100;
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
