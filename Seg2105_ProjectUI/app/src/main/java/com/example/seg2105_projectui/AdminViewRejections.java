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

    private TextView displayRole;
    private TextView displayFirstName;
    private TextView displayLastName;
    private TextView displayPhoneNumber;
    private TextView displayUsername;
    private TextView displayCoursesANDProgram;
    private TextView displayDegreeANDBlank;

    private Member currentFile;
    private List<Member> pendingFiles;
    private int pendingFileCounter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_rejections);

        //SET UP TEXT FILES
        displayRole = findViewById(R.id.display_role_rejections);
        displayFirstName = findViewById(R.id.display_firstname_rejections);
        displayLastName = findViewById(R.id.display_lastName_rejections);
        displayPhoneNumber = findViewById(R.id.display_phone_rejections);
        displayUsername = findViewById(R.id.display_username_rejections);
        displayCoursesANDProgram = findViewById(R.id.display_coursesOffered_rejections);
        displayDegreeANDBlank = findViewById(R.id.display_highestDegree_rejections);

        //check if list isnt empty //we use 2 to specify rejected users
        if (SignUpActivity.dbHelper.getUsersByStatusList(2).isEmpty()){
            //pendingFiles is a List<Member>, that is a list of all files (students and tutors) that were rejected
            pendingFiles = SignUpActivity.dbHelper.getUsersByStatusList(2);
            //pendingFileCounter is just a way to traverse index
            pendingFileCounter = 0;
            getNewUser(0);
        } else {
            //set the screen to have a message that there's no more files
            updateScreenNoMoreFiles();
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
            //Set the user to be approved
            currentFile.setAccountStatus(1);
            //Set user in the database to be approved
            SignUpActivity.dbHelper.approveUser(currentFile.getUserName());
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
            tempText = "Courses Offered: " + Arrays.toString(tempUser.getCoursesOffered());
        }
        displayCoursesANDProgram.setText(tempText);
        if (currentFile.getUserRole().equals("Student")) {
            tempText = "";
        } else {
            Tutor tempUser = (Tutor)currentFile;
            tempText = "Highest Degree: " + tempUser.getHighestDegree();
        }
        displayDegreeANDBlank.setText(tempText);
    }

    //set the screen to just be blank with a line saying that there's no more files
    private void updateScreenNoMoreFiles(){
        String tempText = " ";
        displayRole.setText(tempText);
        displayFirstName.setText(tempText);
        tempText = "No more Applications Remain.";
        displayLastName.setText(tempText);
        tempText = " ";
        displayPhoneNumber.setText(tempText);
        displayUsername.setText(tempText);
        displayCoursesANDProgram.setText(tempText);
        displayDegreeANDBlank.setText(tempText);
    }
}