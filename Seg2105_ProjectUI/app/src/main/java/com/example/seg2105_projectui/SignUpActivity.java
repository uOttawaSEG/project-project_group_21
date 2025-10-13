package com.example.seg2105_projectui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class SignUpActivity extends AppCompatActivity {
    private EditText editFirstName, editLastName, editPassword, editPhone, editDegree, editCourse, editProgram, editEmailAddress;
    private RadioGroup roleGroup;
    private LinearLayout tutorFields;

    private DatabaseHelper dbHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new DatabaseHelper(this);

        roleGroup = findViewById(R.id.roleGroup);
        tutorFields = findViewById(R.id.tutorFields);

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editPassword = findViewById(R.id.editPassword);
        editPhone = findViewById(R.id.editPhone);
        editDegree = findViewById(R.id.editDegree);
        editCourse = findViewById(R.id.editCourse);
        editEmailAddress = findViewById(R.id.editEmailAddress);
        editProgram = findViewById(R.id.editProgram);

        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);


        roleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioTutor) {
                tutorFields.setVisibility(View.VISIBLE);
            } else {
                tutorFields.setVisibility(View.GONE);
            }
        });
        buttonCreateAccount.setOnClickListener(v -> {
            createAccount();
            //create account logic
        });
    }

    private void createAccount() {

        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String emailAddress = editEmailAddress.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Determine the selected role
        int selectedRoleId = roleGroup.getCheckedRadioButtonId();
        long insertResult = -1;

        if (selectedRoleId == R.id.radioTutor) {
            // --- Logic for creating a Tutor ---
            String degree = editDegree.getText().toString().trim();
            String coursesText = editCourse.getText().toString().trim();

            if (degree.isEmpty() || coursesText.isEmpty()) {
                Toast.makeText(this, "Degree and Courses cannot be empty for a Tutor", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] coursesOffered = coursesText.split(",");
            Tutor newTutor = new Tutor(emailAddress, password, lastName, firstName, phone, "Tutor", degree, coursesOffered);

            Toast.makeText(this, "Tutor account for " + newTutor.getFirstName() + " created!", Toast.LENGTH_LONG).show();
            // newTutor.registerUser();
            dbHelper.addTutor(newTutor);

        } else if (selectedRoleId == R.id.radioStudent) {
            String program = editProgram.getText().toString().trim();
            Student newStudent = new Student(emailAddress, password, lastName, firstName, phone, "Student", program);
            // --- Logic for creating a Student ---
            // Assuming you have a Student class that also extends Member
            // Student newStudent = new Student(username, password, lastName, firstName, phone);
            Toast.makeText(this, "Student account created!", Toast.LENGTH_LONG).show();
            dbHelper.addStudent(newStudent);
        }
        else {
            Toast.makeText(this, "Please select a role (Tutor or Student)", Toast.LENGTH_SHORT).show();
        }

        // After successfully creating account, navigate to another screen
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}

