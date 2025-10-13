package com.example.seg2105_projectui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class Tutor extends Member {
    private String highestDegree;
    private String[] coursesOffered;

    public Tutor(String userName, String userPassword, String userLastName, String userFirstName, String userPhoneNumber, String userHighestDegree, String[] userCoursesOffered){
        super(userName,userPassword, userLastName, userFirstName, userPhoneNumber);
        highestDegree = userHighestDegree;
        coursesOffered = userCoursesOffered;
    }

    public String getHighestDegree(){
        return highestDegree;
    }

    public String[] getCoursesOffered(){
        return coursesOffered;
    }

    public String registerUser(){
        //For back end to save the TUTOR user i think
        return null;
    }
}
