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


public class AdminViewPending extends AppCompatActivity {

    //Displays so i can access them from inside methods
    private TextView displayRole;
    private TextView displayFirstName;
    private TextView displayLastName;
    private TextView displayPhoneNumber;
    private TextView displayUsername;
    private TextView displayCoursesANDProgram;
    private TextView displayDegreeANDBlank;

    //the current file or application, it is provided from pendFiles
    private Member currentFile;
    //A list of pending users, in Rejections its a list of rejected users
    private List<Member> pendingFiles;
    //counter/iterator to go through pendingFiles
    private int pendingFileCounter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_pending);

        //SET UP TEXT FILES; every line is a display, this allows you to change them easily
        displayRole = findViewById(R.id.display_role_pending);
        displayFirstName = findViewById(R.id.display_firstname_pending);
        displayLastName = findViewById(R.id.display_lastName_pending);
        displayPhoneNumber = findViewById(R.id.display_phone_pending);
        displayUsername = findViewById(R.id.display_username_pending);
        displayCoursesANDProgram = findViewById(R.id.display_coursesOffered_pending);
        displayDegreeANDBlank = findViewById(R.id.display_highestDegree_pending);

        //check if list isnt empty
        if (SignUpActivity.dbHelper.getUsersByStatusList(0).isEmpty()){
            //pendingFiles is a List<Member>, that is a list of all files (students and tutors) awaiting approval, we give it 0 to specify pending users
            pendingFiles = SignUpActivity.dbHelper.getUsersByStatusList(0);
            //pendingFileCounter is just a way to traverse index
            pendingFileCounter = 0;
            getNewUser(0);
        } else {
            //set the screen to have a message that there's no more files
            updateScreenNoMoreFiles();
        }


        //SET BACK BUTTON
        Button buttonBackToSelection = findViewById(R.id.buttonBackToSelection);
        buttonBackToSelection.setOnClickListener(v -> {
            Intent intent1 = new Intent(AdminViewPending.this, AdminSelect.class);
            startActivity(intent1);
            finish();
        });

        //ALL THE DIFFERENT BUTTONS
        Button approveButton = findViewById(R.id.approveButton);
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

        Button rejectButton = findViewById(R.id.rejectButton);
        rejectButton.setOnClickListener(v -> {
            //set user to be rejected
            currentFile.setAccountStatus(2);
            SignUpActivity.dbHelper.rejectUser(currentFile.getUserName());
            pendingFiles.remove(pendingFileCounter);
            pendingFileCounter -= 1;

            if (pendingFiles.isEmpty()){
                updateScreenNoMoreFiles();
            } else {
                getNewUser(1);
            }
        });

        Button prevButton = findViewById(R.id.prevButton);
        prevButton.setOnClickListener(v -> {
            //get previous file
            getNewUser(-1);
        });

        Button nextButton = findViewById(R.id.nextButton);
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