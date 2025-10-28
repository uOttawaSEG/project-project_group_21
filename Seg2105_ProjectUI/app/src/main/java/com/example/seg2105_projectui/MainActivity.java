package com.example.seg2105_projectui;

import android.os.Bundle;


import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText emailInput = findViewById(R.id.editTextEmail);
        EditText passwordInput = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.buttonLogin);
        TextView signUpLink = findViewById(R.id.textSignUp);


        loginButton.setOnClickListener(v -> {

            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();

            }
            else {
                //connect to database
                DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                Member user = db.checkUser(email, password);
                //check if user exists in database
                if (user == null) {
                    Toast.makeText(MainActivity.this, "User does not exist. pls sign up", Toast.LENGTH_SHORT).show();
                } else {
                    if(user.getAccountStatus() == 1 || user.getUserName().equals("admin"))
                    {
                        Intent intent1 = new Intent(MainActivity.this, WelcomeActivity.class);
                        intent1.putExtra("userInfo", user);
                        startActivity(intent1);
                    }
                    else if(user.getAccountStatus() == 0){
                        Toast.makeText(MainActivity.this, "Account approval pending, please wait for the account to be processed. ", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Account Rejected, please contact support at 111-222-3333. ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            //Add backend later
        });


        signUpLink.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);

            startActivity(intent);
        });
    }
}