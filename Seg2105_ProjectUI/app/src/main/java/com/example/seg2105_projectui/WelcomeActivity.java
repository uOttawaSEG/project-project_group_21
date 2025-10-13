package com.example.seg2105_projectui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class    WelcomeActivity extends AppCompatActivity {

    // @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Intent intent = getIntent();
        Member user = (Member) getIntent().getSerializableExtra("userInfo");

        TextView welcomeMessage = findViewById(R.id.welcomeMessage);
        TextView userDetails = findViewById(R.id.userDetails);

        String welcomeText = "Welcome, " + user.getFirstName() + "!";
        String detailsText = "You are logged in as a " + user.getUserRole();

        welcomeMessage.setText(welcomeText);
        userDetails.setText(detailsText);

        //logout button functionality
        Button logoutButton = findViewById(R.id.buttonLogout);

        logoutButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent1);
            finish();
        });
    }
}
