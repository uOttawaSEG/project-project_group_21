package com.example.seg2105_projectui;


import androidx.appcompat.app.AppCompatActivity;


import java.lang.reflect.Member;


import android.os.Bundle;
import android.view.View;
import android.widget.*;


public class Admin extends User{
    private Member currentFile;

    public Admin(String userName, String userPassword){
        super(userName,userPassword);
        this.currentFile = null;
    }
    //Admin will eventually have to log in


    public void approveMember(Member user){
        user.setAccountStatus(1);
    }


    public void rejectMember(Member user){
        user.setAccountStatus(2);
    }


    public void getNextRejected(){
        this.currentFile = null;
    }


    public void getNextPending(){
        this.currentFile = null;
    }
}
