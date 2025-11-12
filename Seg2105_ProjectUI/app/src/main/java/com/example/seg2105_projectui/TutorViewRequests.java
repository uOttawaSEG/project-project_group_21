package com.example.seg2105_projectui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TutorViewRequests extends AppCompatActivity {

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

    private String tutorUsername, date, time;

    private Button buttonBackToSelection, approveButton, rejectButton, prevButton, nextButton;

    private ArrayList<Sessions> tutorDates;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_view_pending);

        dbHelper = new DatabaseHelper(this);

        //SET UP TEXT FILES; every line is a display, this allows you to change them easily
        Spinner displayTime = findViewById(R.id.pickTime);
        displayFirstName = findViewById(R.id.display_firstname_pending);
        displayLastName = findViewById(R.id.display_lastName_pending);
        displayPhoneNumber = findViewById(R.id.display_phone_pending);
        displayUsername = findViewById(R.id.display_username_pending);

        tutorUsername = getIntent().getStringExtra("username");
        date = getIntent().getStringExtra("date");

        //set up time picker
        tutorDates = new ArrayList<>(dbHelper.getTutorDay(tutorUsername, date));
        String[] allStartTimes = new String[tutorDates.size()];

        for (int i = 0; i < tutorDates.size(); i++){
            Sessions current = tutorDates.get(i);
            allStartTimes[i] = (current.getStartTime());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allStartTimes);
        displayTime.setAdapter(adapter); //put the list of all times into the dropdown

        //for selection in the time picker
        displayTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time = (String) parent.getItemAtPosition(position);
                pendingFiles = dbHelper.getPendingStudents(tutorUsername, date, time);
                pendingFileCounter = 0;
                getNewUser(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                time = (String) parent.getItemAtPosition(0);
                pendingFiles = dbHelper.getPendingStudents(tutorUsername, date, time);
                pendingFileCounter = 0;
                getNewUser(0);
            }
        });

        //SET BACK BUTTON
        buttonBackToSelection = findViewById(R.id.buttonBackToSelection);
        buttonBackToSelection.setOnClickListener(v -> {
            Intent intent1 = new Intent(TutorViewRequests.this, TutorViewPending.class);
            intent1.putExtra("username", tutorUsername);
            startActivity(intent1);
            finish();
        });

        //ALL THE DIFFERENT BUTTONS
        approveButton = findViewById(R.id.approveButton);
        approveButton.setOnClickListener(v -> {
            if (!pendingFiles.isEmpty()){
                //Set the user to be approved
                //Set user in the database to be approved
                dbHelper.rejectStudent(tutorUsername, date, time, currentFile.getUserName());
                sendMessage(currentFile.getUserName(), "approved");
                pendingFiles.remove(pendingFileCounter);
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
                dbHelper.rejectStudent(tutorUsername, date, time, currentFile.getUserName());
                sendMessage(currentFile.getUserName(), "rejected");
                pendingFiles.remove(pendingFileCounter);
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
            //get student for currentfile
            //set currentFile to be the "new" file
            String studentName = pendingFiles.get(pendingFileCounter);
            currentFile = dbHelper.getStudent(studentName);
            updateScreen();
        }
    }

    private void updateScreen(){
        String tempText = "First Name: " + currentFile.getFirstName();
        displayFirstName.setText(tempText);
        tempText = "Last Name: " + currentFile.getLastName();
        displayLastName.setText(tempText);
        tempText = "Phone Number: " + currentFile.getPhoneNumber();
        displayPhoneNumber.setText(tempText);
        tempText = "Username: " + currentFile.getUserName();
        displayUsername.setText(tempText);

        approveButton.setVisibility(View.VISIBLE);
        rejectButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        prevButton.setVisibility(View.VISIBLE);
    }

    //set the screen to just be blank with a line saying that there's no more files
    private void updateScreenNoMoreFiles(){
        String tempText = " ";
        displayFirstName.setText(tempText);
        tempText = "No more Applications Remain. Please select a new time.";
        displayLastName.setText(tempText);
        tempText = " ";
        displayPhoneNumber.setText(tempText);
        displayUsername.setText(tempText);

        approveButton.setVisibility(View.GONE);
        rejectButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        prevButton.setVisibility(View.GONE);
    }

    private void sendMessage(String email, String decision){
        try {
            Intent sendEmail = new Intent(Intent.ACTION_SENDTO);
            sendEmail.setData(Uri.parse("mailto:"));
            sendEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            sendEmail.putExtra(Intent.EXTRA_SUBJECT, "Decision made about your application");
            sendEmail.putExtra(Intent.EXTRA_TEXT, "Your recent application to the SEG2105 tutoring and learning system has been " + decision + " by administration.");
            startActivity(sendEmail);
        } catch (Exception error){
            //String tempText = error.getMessage();
            //displayRole.setText(tempText);
            // error with sending message
        }
    }

}