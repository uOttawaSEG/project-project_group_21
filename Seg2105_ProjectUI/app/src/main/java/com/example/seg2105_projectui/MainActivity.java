package com.example.seg2105_projectui;

import android.os.Bundle;


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
            //Add backend later
        });


        signUpLink.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}