package com.example.seg2105_projectui;

import java.util.List;

public class Sessions {
    public String tutorUsername;
    public String date;//you guys can decide on format
    public String startTime;//you guys can decide on format
    public List<String> pendingStudents;
    public List<String> approvedStudents;

    public List<String> rejectedStudents;

    public Sessions(String tutorUsername, String date, String startTime, List<String> pendingStudents, List<String> approvedStudents, List<String> rejectedStudents){
        this.tutorUsername = tutorUsername;
        this.date = date;
        this.startTime = startTime;
        this.pendingStudents = pendingStudents;
        this.approvedStudents = approvedStudents;
        this.rejectedStudents = rejectedStudents;

    }


}
