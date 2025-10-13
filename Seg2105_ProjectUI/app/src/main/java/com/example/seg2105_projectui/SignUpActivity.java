package com.example.seg2105_projectui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class SignUpActivity extends AppCompatActivity
{
    private EditText editFirstName, editLastName, editPassword, editPhone, editDegree, editCourse;
    private RadioGroup roleGroup;
    private LinearLayout tutorFields;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        roleGroup = findViewById(R.id.roleGroup);
        tutorFields = findViewById(R.id.tutorFields);

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editPassword = findViewById(R.id.editPassword);
        editPhone = findViewById(R.id.editPhone);
        editDegree = findViewById(R.id.editDegree);
        editCourse = findViewById(R.id.editCourse);
        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);


        roleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioTutor) {
                tutorFields.setVisibility(View.VISIBLE);
            } else {
                tutorFields.setVisibility(View.GONE);
            }
        });

        buttonCreateAccount.setOnClickListener(v -> {
            //create account logic
        });



    }

}
