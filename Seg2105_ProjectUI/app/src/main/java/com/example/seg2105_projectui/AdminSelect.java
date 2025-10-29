package com.example.seg2105_projectui;

import android.os.Bundle;


import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

public class AdminSelect extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_select);

        TextView textSelectedaTask = findViewById(R.id.textSelectedaTask);
        textSelectedaTask.setText("Please Select a Task");


        Button buttonGoBack = findViewById(R.id.buttonGoBack);

        buttonGoBack.setOnClickListener(v -> {
            Intent intent3 = new Intent(AdminSelect.this, WelcomeActivity.class);
            startActivity(intent3);
            finish();
        });

        Button buttonViewPending = findViewById(R.id.buttonViewPending);

        buttonViewPending.setOnClickListener(v -> {
            Intent intent4 = new Intent(AdminSelect.this, AdminViewPending.class);
            startActivity(intent4);

        });

        Button buttonViewRejection = findViewById(R.id.buttonViewRejection);

        buttonViewRejection.setOnClickListener(v -> {
            Intent intent5 = new Intent(AdminSelect.this, AdminViewRejections.class);
            startActivity(intent5);

        });


    }
}