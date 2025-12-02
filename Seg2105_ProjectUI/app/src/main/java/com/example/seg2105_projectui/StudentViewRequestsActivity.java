package com.example.seg2105_projectui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


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

    private Button buttonBackToSelection;

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