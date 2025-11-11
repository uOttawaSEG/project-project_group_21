package com.example.seg2105_projectui;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import java.util.Iterator;


import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.net.Uri;



public class TutorViewPending extends AppCompatActivity {

    //Displays so i can access them from inside methods
    private TextView displayStatus;

    private CalendarView displayDate;

    private DatabaseHelper dbHelper;

    private String tutorUsername;

    private Button btnSeeSession;

    private Button TEMPFakeStudentJoin;

    private String date, startTime;

    private SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_pendingrequests);

        dbHelper = new DatabaseHelper(this);

        //SET UP TEXT FILES; every line is a display, this allows you to change them easily
        displayDate = findViewById(R.id.calendarView);
        displayStatus = findViewById(R.id.displayStatus);
        btnSeeSession = findViewById(R.id.btnSeeSession);

        //button for going to the see sessions page
        btnSeeSession.setOnClickListener(v -> {
            Intent intent1 = new Intent(TutorViewPending.this, TutorViewRequests.class);
            intent1.putExtra("username", tutorUsername);
            intent1.putExtra("date", date);
            startActivity(intent1);
            finish();
        });

        //SET BACK BUTTON
        Button buttonBackToSelection = findViewById(R.id.buttonBackToSelection);
        buttonBackToSelection.setOnClickListener(v -> {
            Intent intent1 = new Intent(TutorViewPending.this, TutorActivity.class);
            intent1.putExtra("username", tutorUsername);
            startActivity(intent1);
            finish();
        });

        //get tutor username
        tutorUsername = getIntent().getStringExtra("username");

        //setup + check for CURRENT date so it doesnt crash on opening
        Date today = new Date();
        date = sdf.format(today);
        checkForTimes(date);

        //get list to iterate through based on time, date + tutor
        displayDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                //Use YYYY-MM-DD and HH:mm
                date = year + "-" + (month + 1) + "-" + dayOfMonth;

                //for testing
                System.out.println(tutorUsername);
                System.out.println(startTime);

                //check
                checkForTimes(date);
            }
        });

        //for testing
        Button TEMPFakeStudentJoin = findViewById(R.id.tempStudentJoin);
        TEMPFakeStudentJoin.setOnClickListener(v -> {
            List<Sessions> SessionList = dbHelper.sessionsNoStudents();
            for (int i = 0; i < SessionList.size(); i++){
                dbHelper.studentPending(tutorUsername, SessionList.get(i).getDate(), SessionList.get(i).getStartTime(), "Tester");
                String tempText = String.valueOf(SessionList.get(i).getStartTime());
                //displayTime.setText(tempText);
            }
        });
    }

    public void checkForTimes(String date){
        ArrayList<Sessions> tutorDates = new ArrayList<>(dbHelper.getTutorDay(tutorUsername, date));
        System.out.println("checktimes started");

        if (tutorDates.isEmpty()){
            System.out.println("checktimes no sessions");
            String tempText = "You have no sessions on this date.";
            displayStatus.setText(tempText);
            btnSeeSession.setVisibility(View.GONE);
        }
        else{
            System.out.println("checktimes has sessions");
            int numSessions = tutorDates.size();

            String temp = "You have " + numSessions + " session requests for " + date + ".";
            displayStatus.setText(temp);
            btnSeeSession.setVisibility(View.VISIBLE);
        }

    }
}
