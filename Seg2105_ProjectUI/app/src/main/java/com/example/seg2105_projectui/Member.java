package com.example.seg2105_projectui;
import java.io.*;
public class Member extends User implements Serializable {
    private String firstName;
    private String lastName;
    private String phoneNumber;

    private String userRole;


    public Member(String userName, String userPassword, String userLastName, String userFirstName, String userPhoneNumber, String userRole){
        super(userName,userPassword);
        this.firstName = userFirstName;
        this.lastName = userLastName;
        this.phoneNumber = userPhoneNumber;
        this.userRole = userRole;
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
}
