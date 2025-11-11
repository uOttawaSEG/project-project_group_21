package com.example.seg2105_projectui;

import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import java.util.Arrays;
import java.util.List;

import android.net.Uri;



public class TutorViewPending extends AppCompatActivity {

    //Displays so i can access them from inside methods
    private EditText displayTime;
    private TextView displayFirstName;
    private TextView displayLastName;
    private TextView displayPhoneNumber;

    private CalendarView displayDate;

    //the current file or application, it is provided from pendFiles
    private Member currentFile;
    //A list of pending users, in Rejections its a list of rejected users
    private List<String> pendingFiles = null;
    //counter/iterator to go through pendingFiles
    private int pendingFileCounter;

    private DatabaseHelper dbHelper;

    private String tutorUsername;

    private Button approveButton, rejectButton, prevButton, nextButton;

    private Button TEMPFakeStudentJoin;

    String date, startTime;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_pendingrequests);

        dbHelper = new DatabaseHelper(this);

        //SET UP TEXT FILES; every line is a display, this allows you to change them easily
        displayTime = findViewById(R.id.timeSelector);
        displayDate = findViewById(R.id.calendarView);
        displayFirstName = findViewById(R.id.displayFirstName);
        displayLastName = findViewById(R.id.displayLastName);
        displayPhoneNumber = findViewById(R.id.displayPhone);
        date = "";
        startTime ="";

        //ALL THE DIFFERENT BUTTONS -> moved up to prevent null crash when temp is called
        approveButton = findViewById(R.id.approveButton);
        approveButton.setOnClickListener(v -> {
            if (!pendingFiles.isEmpty()){
                //Set the user to be approved
                //Set user in the database to be approved
                dbHelper.approveStudent(tutorUsername, date, startTime, currentFile.getUserName());
                sendMessage(currentFile.getUserName(), "approved");
                //Removal taken care of in function

                pendingFileCounter -= 1;
                //If there's not more remaining files show a message
                if (pendingFiles.isEmpty()){
                    updateScreenNoMoreFiles();
                } else {
                    //else get the next file
                    getNewUser(1);
                }
            }
        });

        rejectButton = findViewById(R.id.rejectButton);
        rejectButton.setOnClickListener(v -> {
            if (!pendingFiles.isEmpty()){
                //Set the user to be approved
                //Set user in the database to be approved
                dbHelper.rejectStudent(tutorUsername, date, startTime, currentFile.getUserName());
                sendMessage(currentFile.getUserName(), "rejected");

                //If there's not more remaining files show a message
                if (pendingFiles == null || pendingFiles.isEmpty()){
                    updateScreenNoMoreFiles();
                } else {
                    //else get the next file
                    getNewUser(1);
                }
            }
        });

        prevButton = findViewById(R.id.prevButton);
        prevButton.setOnClickListener(v -> {
            //get previous file
            getNewUser(-1);
        });

        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> {
            //get next file
            getNewUser(1);
        });

        //get tutor username
        tutorUsername = getIntent().getStringExtra("username");

        //TEMP JUST TO DISPLAY NOTHING
        updateScreenNoMoreFiles();

        //set up files
        pendingFiles = dbHelper.getPendingStudents(tutorUsername, date, startTime);
        pendingFileCounter = 0;

        //get list to iterate through based on time, date + tutor
        displayDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //redundant
                //if (pendingFiles.isEmpty()){
                //    updateScreenNoMoreFiles();
                //}


                if (startTime.isEmpty() && !pendingFiles.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a start time", Toast.LENGTH_SHORT).show();
                }
                //Use YYYY-MM-DD and HH:mm
                date = year + "--" + (month + 1) + "--" + dayOfMonth;
                startTime = displayTime.getText().toString().trim();

                //re-call to access db
                pendingFiles = dbHelper.getPendingStudents(tutorUsername, date, startTime);

                System.out.println(date);
                System.out.println(startTime);


                if (pendingFiles.isEmpty()){
                    updateScreenNoMoreFiles();
                } else {
                    getNewUser(0);
                }
            }
        });


        //SET BACK BUTTON
        Button buttonBackToSelection = findViewById(R.id.buttonBackToSelection);
        buttonBackToSelection.setOnClickListener(v -> {
            Intent intent1 = new Intent(TutorViewPending.this, TutorActivity.class);
            intent1.putExtra("username", tutorUsername);
            startActivity(intent1);
            finish();
        });


        Button TEMPFakeStudentJoin = findViewById(R.id.tempStudentJoin);
        TEMPFakeStudentJoin.setOnClickListener(v -> {
            List<Sessions> SessionList = dbHelper.sessionsNoStudents();
            for (int i = 0; i < SessionList.size(); i++){
                dbHelper.studentPending(tutorUsername, SessionList.get(i).getDate(), SessionList.get(i).getStartTime(), "Tester");
                String tempText = String.valueOf(SessionList.get(i).getStartTime());
                //displayTime.setText(tempText);
            }
            pendingFiles = dbHelper.getPendingStudents(tutorUsername, date, startTime);
            pendingFileCounter = 0;
            getNewUser(0);

        });


    }

    private void getNewUser(int direction){
        //1 forward
        //-1 backward
        pendingFileCounter += direction;

        if (pendingFiles.isEmpty()){
            updateScreenNoMoreFiles();
        } else {
            //bounds checking
            if (pendingFileCounter >= pendingFiles.size()){
                pendingFileCounter = (pendingFiles.size()-1);
            }
            if (pendingFileCounter < 0) {
                pendingFileCounter = 0;
            }
            //set currentFile to be the "new" file
            currentFile = dbHelper.getStudent((pendingFiles.get(pendingFileCounter)));
            updateScreen();
        }
    }

    private void updateScreen(){
        String temp = "First Name: " + currentFile.getFirstName();
        displayFirstName.setText(temp);
        temp = "Last Name: " + currentFile.getLastName();
        displayLastName.setText(temp);
        temp = "Phone Number: " + currentFile.getPhoneNumber();
        displayPhoneNumber.setText(temp);

        approveButton.setVisibility(View.VISIBLE);
        rejectButton.setVisibility(View.VISIBLE);
        prevButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
    }

    //set the screen to just be blank with a line saying that there's no more files
    private void updateScreenNoMoreFiles(){
        String tempText = " ";
        displayFirstName.setText(tempText);
        tempText = "No more Session Applications Remain. Please enter a valid date and time above.";
        displayLastName.setText(tempText);
        tempText = " ";
        displayPhoneNumber.setText(tempText);

        approveButton.setVisibility(View.GONE);
        rejectButton.setVisibility(View.GONE);
        prevButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
    }

    private void sendMessage(String email, String decision){
        try {
            Intent sendEmail = new Intent(Intent.ACTION_SENDTO);
            sendEmail.setData(Uri.parse("mailto:"));
            sendEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            sendEmail.putExtra(Intent.EXTRA_SUBJECT, "Decision made about your application");
            sendEmail.putExtra(Intent.EXTRA_TEXT, "Your recent application to the SEG2105 tutoring and learning system has been " + decision + " by your tutor.");
            startActivity(sendEmail);
        } catch (Exception error){
            //String tempText = error.getMessage();
            //displayRole.setText(tempText);
            // error with sending message
        }
    }
}
