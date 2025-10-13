package com.example.seg2105_projectui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class User {
    
    private String username;
    private String password;

    private String firstName;
    private String lastName;
    private String phoneNumber;


    public User(String userName, String userPassword){
        this.username = userName;
        this.password = userPassword;
    }

    public String getUserName(){
        return username;
    }

    public String getUserPassword(){
        return password;
    }

    public boolean login(){
        //For front end to ask the database for user login details and such
        return false;
    }

    public void logout(){  
        //For front end to log the user out of the program
    }


    public String getLastName(){
        return lastName;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }
}
