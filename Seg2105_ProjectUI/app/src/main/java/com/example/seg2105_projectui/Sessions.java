package com.example.seg2105_projectui;

import java.io.Serializable;
import java.util.List;

public class Sessions implements Serializable {
    public String tutorUsername;
    public String date;//you guys can decide on format
    public String startTime;//you guys can decide on format
    public String course;
    public String status;//String bc it makes database easier



    public Sessions(String tutorUsername, String date, String startTime, String course, String status){
        this.tutorUsername = tutorUsername;
        this.date = date;
        this.startTime = startTime;
        this.course = course;//pls use this format 'SEG 2222'
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public String getCourse() {
        return course;
    }

    public String getTutorUsername() {
        return tutorUsername;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getStatus() { return status; }

    public void setApproved() {
        status = "Approved";
    }

    public void setPending() {
        status = "Pending";
    }

}
