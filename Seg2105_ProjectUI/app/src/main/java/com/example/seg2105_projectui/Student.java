package com.example.seg2105_projectui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class Student extends Member{
    private String program;

    public Student(String userName, String userPassword, String userLastName, String userFirstName, String userPhoneNumber, String userRole, String userProgram){
        super(userName,userPassword, userLastName, userFirstName, userPhoneNumber, userRole);
        this.program = userProgram;
    }

    public String getProgram(){
        return program;
    }

    public String registerUser(){
        //For back end to save the STUDENT user i think
        return null;
    }
    
}
