package com.example.seg2105_projectui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tutor extends Member implements Serializable {

    private String highestDegree;
    private String[] coursesOffered;
    private List<Sessions> sessions;


    public Tutor(String userName, String userPassword, String userLastName, String userFirstName, String userPhoneNumber, String userRole, String userHighestDegree, String[] userCoursesOffered){

        super(userName, userPassword, userLastName, userFirstName, userPhoneNumber, userRole);
        this.highestDegree = userHighestDegree;
        this.coursesOffered = userCoursesOffered;
        this.sessions = new ArrayList<>();
    }


    public Tutor(String userName, String userPassword, String userLastName, String userFirstName, String userPhoneNumber, String userRole, String userHighestDegree, String[] userCoursesOffered, int accountStatus) {

        super(userName, userPassword, userLastName, userFirstName, userPhoneNumber, userRole);
        this.highestDegree = userHighestDegree;
        this.coursesOffered = userCoursesOffered;
        this.sessions = new ArrayList<>();
    }

    public String getHighestDegree(){
        return highestDegree;
    }

    public String[] getCoursesOffered(){
        return coursesOffered;
    }

    public List<Sessions> getSessions() {
        return sessions;
    }

    public void setSessions(List<Sessions> sessions) {
        this.sessions = sessions;
    }

    public void createNewSession(String date, String startTime) {
        Sessions newSession = new Sessions(this.getUserName(), date, startTime);
        if (this.sessions == null) {
            this.sessions = new ArrayList<>();
        }
        this.sessions.add(newSession);
    }
}
