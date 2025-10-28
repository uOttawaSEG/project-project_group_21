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

    public static DatabaseHelper dbHelper;

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

        // FIX #4: Added emailAddress.isEmpty() to the check
        if (firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || phone.isEmpty() || emailAddress.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // FIX #1: Use the new isUserExists method to check if the email is already taken.
        // FIX #3: Use the single dbHelper instance.
        if (dbHelper.isUserExists(emailAddress)) {
            Toast.makeText(this, "An account with this email already exists.", Toast.LENGTH_SHORT).show();
            return; // Stop the process if user exists
        }

        int selectedRoleId = roleGroup.getCheckedRadioButtonId();

        if (selectedRoleId == R.id.radioTutor) {
            String degree = editDegree.getText().toString().trim();
            String coursesText = editCourse.getText().toString().trim();

            if (degree.isEmpty() || coursesText.isEmpty()) {
                Toast.makeText(this, "Degree and Courses cannot be empty for a Tutor", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] coursesOffered = coursesText.split(",");
            Tutor newTutor = new Tutor(emailAddress, password, lastName, firstName, phone, "Tutor", degree, coursesOffered);

            boolean isAdded = dbHelper.addTutor(newTutor);

            if (isAdded) {
                Toast.makeText(this, "Tutor account created! Please wait for approval.", Toast.LENGTH_LONG).show();
                // FIX #2: Navigate ONLY on success.
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
            }

        } else if (selectedRoleId == R.id.radioStudent) {
            String program = editProgram.getText().toString().trim();
            Student newStudent = new Student(emailAddress, password, lastName, firstName, phone, "Student", program);

            boolean isAdded = dbHelper.addStudent(newStudent);

            if (isAdded) {
                Toast.makeText(this, "Student account created! Please wait for approval.", Toast.LENGTH_LONG).show();
                // FIX #2: Navigate ONLY on success.
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Please select a role (Tutor or Student)", Toast.LENGTH_SHORT).show();
        }
    }



}

