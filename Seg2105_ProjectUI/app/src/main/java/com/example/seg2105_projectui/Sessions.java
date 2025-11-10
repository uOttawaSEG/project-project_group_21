package com.example.seg2105_projectui;

import java.io.Serializable;
import java.util.List;

public class Sessions implements Serializable {
    public String tutorUsername;
    public String date;//you guys can decide on format
    public String startTime;//you guys can decide on format

    public Sessions(String tutorUsername, String date, String startTime){
        this.tutorUsername = tutorUsername;
        this.date = date;
        this.startTime = startTime;
    }

    public String getDate() {
        return date;
    }

    public String getTutorUsername() {
        return tutorUsername;
    }

    public String getStartTime() {
        return startTime;
    }
}
