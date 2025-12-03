package com.example.seg2105_projectui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class StudentViewRequestsActivity extends AppCompatActivity {

    //Displays so i can access them from inside methods
    private TextView displayFirstName;
    private TextView displayLastName;
    private TextView displayPhoneNumber;
    private TextView displayUsername;

    //the current file or application, it is provided from pendFiles
    private Member currentFile;
    //A list of pending users, in Rejections its a list of rejected users
    private List<String> pendingFiles = null;
    //counter/iterator to go through pendingFiles
    private int pendingFileCounter;

    private DatabaseHelper dbHelper;

    private String studentUsername, date, time;

    private Button buttonBackToSelection, buttonExporttoGC;

    private ArrayList<Sessions> approvedDates, rejectedDates, pendingDates, allSessions;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_viewrequestedsessions);

        dbHelper = new DatabaseHelper(this);

        studentUsername = getIntent().getStringExtra("username");

        //set up the list display
        approvedDates = new ArrayList<>(dbHelper.studentSessions(studentUsername, "Approved"));
        rejectedDates = new ArrayList<>(dbHelper.studentSessions(studentUsername, "Rejected"));
        pendingDates = new ArrayList<>(dbHelper.studentSessions(studentUsername, "Pending"));

        allSessions = new ArrayList<>();
        allSessions.addAll(approvedDates);
        allSessions.addAll(rejectedDates);
        allSessions.addAll(pendingDates);

        checkForRequests(allSessions); //will send you back to menu if student has no tests

        sortByDates(allSessions); //creates a large list of all possible sessions and arranges them

        //FOR PLACING INTO THE LIST VIEW
        SessionsAdapter adapter = new SessionsAdapter(this, allSessions);

        ListView listView = (ListView) findViewById(R.id.listOfSessions);
        listView.setAdapter(adapter); //will properly display everything

        //SET BACK BUTTON
        buttonBackToSelection = findViewById(R.id.buttonBackToSelection4);
        buttonBackToSelection.setOnClickListener(v -> {
            //change TutorViewPending to whatever is supposed to connect to this page
            Intent backToDash = new Intent(StudentViewRequestsActivity.this, StudentDashboardActivity.class);
            backToDash.putExtra("username", studentUsername);
            startActivity(backToDash);
            finish();
        });

        buttonExporttoGC = findViewById(R.id.buttonExporttoGC); // Make sure this ID matches your XML
        buttonExporttoGC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportApprovedSessionsToCalendar();
            }
        });
    }

    private void exportApprovedSessionsToCalendar() {
        // 1. Fetch only the "Approved" sessions again. We do this to ensure we only export approved ones.
        List<Sessions> sessionsToExport = dbHelper.studentSessions(studentUsername, "Approved");

        if (sessionsToExport.isEmpty()) {
            Toast.makeText(this, "No approved sessions available to export.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Loop through each approved session
        for (Sessions session : sessionsToExport) {
            try {
                // adjust "dd-MM-yyyy HH:mm" to match database date/time format
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
                Date startDate = dateTimeFormat.parse(session.getDate() + " " + session.getStartTime());

                long startMillis = startDate.getTime();
                // assume sessions are 1 hour long
                long endMillis = startMillis + 60 * 60 * 1000;

                // open the calendar app
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                        .putExtra(CalendarContract.Events.TITLE, "Tutoring: " + session.getCourse())
                        .putExtra(CalendarContract.Events.DESCRIPTION, "Session with tutor " + session.getTutorUsername())
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

                // user will be prompted to save the event.
                startActivity(intent);

            } catch (ParseException e) {
                // catch errors if date/time in the database is in the wrong format
                Toast.makeText(this, "Could not parse date for session: " + session.getCourse(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void sortByDates(ArrayList<Sessions> dates){
        //this is so fuckass lmao im so sorry shoutout stack overflow
        dates.sort(new Comparator<Sessions>() { //override list.sort method
            @Override
            public int compare(Sessions session1, Sessions session2) {
                //compare dates (should be fine since the strings are organized in a way where this works)
                int dateCompare = session1.getDate().compareTo(session2.getDate());

                if (dateCompare == 0) {
                    //this should also be fine bc we use 24h clock i think)
                    return session1.getStartTime().compareTo(session2.getStartTime());
                }
                return dateCompare;
            }
        });
    }

    private void checkForRequests(ArrayList<Sessions> dates){
        if (dates.isEmpty()){
            Toast.makeText(this, "You have not requested any sessions. Returning to menu.", Toast.LENGTH_SHORT).show();

            Intent intent2 = new Intent(StudentViewRequestsActivity.this, StudentDashboardActivity.class);
            intent2.putExtra("username", studentUsername);
            startActivity(intent2);
            finish();
        }
    }
}