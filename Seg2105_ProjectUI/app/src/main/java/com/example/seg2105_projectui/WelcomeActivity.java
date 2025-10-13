package com.example.seg2105_projectui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class    WelcomeActivity extends AppCompatActivity {

    private TextView welcomeMessage, userDetails;

    // @Override
    public static void onCreate(User user) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_welcome);

//        welcomeMessage = findViewById(R.id.welcomeMessage);
//        userDetails = findViewById(R.id.userDetails);

//        Intent intent = getIntent();
        String firstName = null;
        String role = null;
        if (user != null) {
        System.out.println(user.getFirstName());
        System.out.println(user);
//            role = user.getRole();
        }
//        if (intent != null && intent.getExtras() != null) {
//            firstName = intent.getExtras().getString("USER_FIRST_NAME");
//            role = intent.getExtras().getString("USER_ROLE");
//        }
    }
}
