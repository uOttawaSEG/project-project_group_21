package com.example.seg2105_projectui;

import android.os.Bundle;


import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import java.util.Arrays;
import java.util.List;


public class AdminViewRejections extends AppCompatActivity {

    //Displays so i can access them from inside methods
    private TextView displayRole;
    private TextView displayFirstName;
    private TextView displayLastName;
    private TextView displayPhoneNumber;
    private TextView displayUsername;
    private TextView displayCoursesANDBlank;
    private TextView displayDegreeANDProgram;

    //the current file or application, it is provided from pendFiles
    private Member currentFile;
    //A list of pending users, in Rejections its a list of rejected users
    private List<Member> pendingFiles = null;
    //counter/iterator to go through pendingFiles
    private int pendingFileCounter;

    private DatabaseHelper dbHelper;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_rejections);

        dbHelper = new DatabaseHelper(this);

        //SET UP TEXT FILES; every line is a display, this allows you to change them easily
        displayRole = findViewById(R.id.display_role_rejections);
        displayFirstName = findViewById(R.id.display_firstname_rejections);
        displayLastName = findViewById(R.id.display_lastName_rejections);
        displayPhoneNumber = findViewById(R.id.display_phone_rejections);
        displayUsername = findViewById(R.id.display_username_rejections);
        displayCoursesANDBlank = findViewById(R.id.display_coursesOffered_rejections);
        displayDegreeANDProgram = findViewById(R.id.display_highestDegree_rejections);


        pendingFiles = dbHelper.getUsersByStatusList(2);
        pendingFileCounter = 0;

        if (pendingFiles.isEmpty()){
            updateScreenNoMoreFiles();
        } else {
            getNewUser(0);
        }




        //SET BACK BUTTON
        Button buttonBackToSelection = findViewById(R.id.buttonBackToSelection2);
        buttonBackToSelection.setOnClickListener(v -> {
            Intent intent1 = new Intent(AdminViewRejections.this, AdminSelect.class);
            startActivity(intent1);
            finish();
        });

        //ALL THE DIFFERENT BUTTONS
        Button approveButton = findViewById(R.id.approveButton2);
        approveButton.setOnClickListener(v -> {
            if (!pendingFiles.isEmpty()){
                //Set the user to be approved
                //Set user in the database to be approved
                dbHelper.approveUser(currentFile.getUserName());
                //Remove from the file list
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


        Button prevButton = findViewById(R.id.prevButton2);
        prevButton.setOnClickListener(v -> {
            //get previous file
            getNewUser(-1);
        });

        Button nextButton = findViewById(R.id.nextButton2);
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
            //set currentFile to be the "new" file
            currentFile = pendingFiles.get(pendingFileCounter);
            updateScreen();
        }
    }

    private void updateScreen(){
        String tempText = "Application Type: " + currentFile.getUserRole();
        displayRole.setText(tempText);
        tempText = "First Name: " + currentFile.getFirstName();
        displayFirstName.setText(tempText);
        tempText = "Last Name: " + currentFile.getLastName();
        displayLastName.setText(tempText);
        tempText = "Phone Number: " + currentFile.getPhoneNumber();
        displayPhoneNumber.setText(tempText);
        tempText = "Username: " + currentFile.getUserName();
        displayUsername.setText(tempText);
        //check if file is a student or tutor then type cast to fit the specific preferred display
        if (currentFile.getUserRole().equals("Student")) {
            Student tempUser = (Student)currentFile;
            tempText = "Program: " + tempUser.getProgram();
        } else {
            Tutor tempUser = (Tutor)currentFile;
            tempText = "Highest Degree: " + tempUser.getHighestDegree();
        }
        displayDegreeANDProgram.setText(tempText);

        if (currentFile.getUserRole().equals("Student")) {
            tempText = " ";
        } else {
            Tutor tempUser = (Tutor)currentFile;
            tempText = "Courses Offered: " + Arrays.toString(tempUser.getCoursesOffered());
        }
        displayCoursesANDBlank.setText(tempText);
    }

    //set the screen to just be blank with a line saying that there's no more files
    private void updateScreenNoMoreFiles(){
        String tempText = " ";
        displayRole.setText(tempText);
        displayFirstName.setText(tempText);
        tempText = "No more Rejected Applications Remain.";
        displayLastName.setText(tempText);
        tempText = " ";
        displayPhoneNumber.setText(tempText);
        displayUsername.setText(tempText);
        displayCoursesANDBlank.setText(tempText);
        displayDegreeANDProgram.setText(tempText);
    }
}