package com.example.seg2105_projectui;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;


public class Member extends User{
    private String firstName;
    private String lastName;
    private String phoneNumber;


    private String userRole;


    //0 = pending
    //1 = approved
    //2 = rejected
    private int accountStatus;


    public Member(String userName, String userPassword, String userLastName, String userFirstName, String userPhoneNumber, String userRole){
        super(userName,userPassword);
        this.firstName = userFirstName;
        this.lastName = userLastName;
        this.phoneNumber = userPhoneNumber;
        this.userRole = userRole;
        this.accountStatus = 0;
    }


    public String getFirstName(){
        return firstName;
    }


    public String getLastName(){
        return lastName;
    }


    public String getPhoneNumber(){
        return phoneNumber;
    }


    public String getUserRole() { return userRole; }
   
    public int getAccountStatus() {
        return accountStatus;
    }


    public void setAccountStatus(int status){
        this.accountStatus = status;
    }
}
