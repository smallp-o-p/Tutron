package com.example.tutron;

public class Complaint{

    private boolean completed;
    private String decision;
    private String tutorFirstName;
    private String tutorLastName;
    private String complaintDesc;
    private String tutorID;

    public Complaint(){}

    public Complaint(String tutorID, String tutorFirstName, String tutorLastName, String description) {
        this.completed = false;
        this.decision = "";
        this.tutorFirstName = tutorFirstName;
        this.tutorLastName = tutorLastName;
        this.complaintDesc = description;
        this.tutorID = tutorID;
    }
    public Complaint(boolean completed, String decision, String tutorFirstName, String tutorLastName, String complaintDesc, String tutorID) {
        this.completed = completed;
        this.decision = decision;
        this.tutorFirstName = tutorFirstName;
        this.tutorLastName = tutorLastName;
        this.complaintDesc = complaintDesc;
        this.tutorID = tutorID;
    }

    public boolean getCompleted() { return completed; }
    public void setCompleted(boolean completed){ this.completed = completed; }

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public String getTutorFirstName() { return tutorFirstName;}
    public String getTutorLastName() { return tutorLastName; }

    public void setTutorFirstName(String firstName) { this.tutorFirstName = firstName; }
    public void setTutorLastName(String lastName) { this.tutorLastName = lastName; }
    public String getComplaintDesc() {
        return complaintDesc;
    }

    public void setComplaintDesc(String complaintDesc) {
        this.complaintDesc = complaintDesc;
    }

    public String getTutorID() {
        return tutorID;
    }

    public void setTutorID(String tutorID) {
        this.tutorID = tutorID;
    }
}