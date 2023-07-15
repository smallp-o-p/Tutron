package com.example.tutron;

import java.util.ArrayList;
import java.util.Date;

public class Request {

    private int Status;

    private String StudentID;

    private String TutorID;

    private String TopicID;

    private Date RequestedDate;

    private String TopicName;

    public String getStudentFirstName() {
        return StudentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        StudentFirstName = studentFirstName;
    }

    private String StudentFirstName;

    private String StudentLastName;

    public Request(int status, String studentID, String tutorID, String topicID, Date requestedDate, String studentFirstName, String studentLastName, String topicName) {
        Status = status;
        StudentID = studentID;
        TutorID = tutorID;
        TopicID = topicID;
        RequestedDate = requestedDate;
        StudentFirstName = studentFirstName;
        StudentLastName = studentLastName;
        TopicName = topicName;
    }

    public Request() {
    }

    public String getTutorID() {
        return TutorID;
    }

    public void setTutorID(String tutorID) {
        TutorID = tutorID;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getTopicID() {
        return TopicID;
    }

    public void setTopicID(String topicID) {
        TopicID = topicID;
    }

    public Date getRequestedDate() {
        return RequestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        RequestedDate = requestedDate;
    }

    public String getStudentLastName() {
        return StudentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        StudentLastName = studentLastName;
    }

    public String getTopicName() {
        return TopicName;
    }

    public void setTopicName(String topicName) {
        TopicName = topicName;
    }

}
