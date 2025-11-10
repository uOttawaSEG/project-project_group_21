package com.example.seg2105_projectui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TutorActivity extends AppCompatActivity {

    private Button btnManage, btnPastSessions, btnUpcomingSessions, btnLogout, btnApproveSessions;

    private Switch switchAutomaticApproval;

    private DatabaseHelper dbHelper; //for automatic approvals only

    private String tutorUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);

        dbHelper = new DatabaseHelper(this);

        btnManage = findViewById(R.id.btnManage);
        btnPastSessions = findViewById(R.id.btnPastSessions);
        btnUpcomingSessions = findViewById(R.id.btnUpcomingSessions);
        btnLogout = findViewById(R.id.btnLogout);
        btnApproveSessions = findViewById(R.id.btnApproveSessions);
        switchAutomaticApproval = findViewById(R.id.toggleAutomaticApproval);

        tutorUsername = getIntent().getStringExtra("username");

        btnLogout.setOnClickListener(v -> {
            Intent intent10 = new Intent(TutorActivity.this, MainActivity.class);
            startActivity(intent10);
            finish();
        });

        btnManage.setOnClickListener(v -> {
            Intent intent11 = new Intent(TutorActivity.this, TutorManageSessionsActivity.class);
            intent11.putExtra("username", tutorUsername);
            startActivity(intent11);

        });

        btnUpcomingSessions.setOnClickListener(v -> {

            Intent intent12 = new Intent(TutorActivity.this, TutorUpcomingSessionsActivity.class);
            intent12.putExtra("username", tutorUsername);
            startActivity(intent12);

        });

        btnPastSessions.setOnClickListener(v -> {

            Intent intent13 = new Intent(TutorActivity.this, TutorPastSessionsActivity.class);
            intent13.putExtra("username", tutorUsername);
            startActivity(intent13);

        });

        btnApproveSessions.setOnClickListener(v -> {

            Intent intent14 = new Intent(TutorActivity.this, TutorViewPending.class);
            intent14.putExtra("username", tutorUsername);
            startActivity(intent14);

        });

        switchAutomaticApproval.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnApproveSessions.setVisibility(View.GONE); //make the manual approval section inaccessible
                    automaticallyApprove();
                } else {
                    btnApproveSessions.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void automaticallyApprove(){ //for automatic approvals
        //get all sesssions
        ArrayList<Sessions> allSessions = new ArrayList<>(dbHelper.allSessions());

        //get every pending session from all sessions
        for (int i = 0; i < allSessions.size(); i++){
            Sessions currentSession = allSessions.get(i); //get a date and time slot

            //get all pending requests from the date and time slot
            ArrayList<String> currentPending = new ArrayList<>(dbHelper.getPendingStudents(tutorUsername, currentSession.getDate(), currentSession.getStartTime()));

            //approve all pending sessions if they exist
            if (currentPending != null && !currentPending.isEmpty()){
                for (int j = 0; j < currentPending.size(); j++){
                    String studentUsername = currentPending.get(j);
                    dbHelper.approveStudent(tutorUsername, currentSession.getDate(), currentSession.getStartTime(), studentUsername);
                }
            }
        }
    }
}
