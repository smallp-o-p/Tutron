package com.example.tutron;

import java.util.Date;

public class Rating {
    private Double rating;
    private String desc;
    private String topicName;
    private String name;
    private Date datePosted;

    private String studentID;

    private String tutorID;

    public Rating(){}

    public Rating(Double rating, String desc, String topicName, String name, Date datePosted, String studentID, String tutorID) {
        this.rating = rating;
        this.desc = desc;
        this.topicName = topicName;
        this.name = name;
        this.datePosted = datePosted;
        this.studentID = studentID;
        this.tutorID = tutorID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getTutorID() {
        return tutorID;
    }

    public void setTutorID(String tutorID) {
        this.tutorID = tutorID;
    }



    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicID(String topicName) {
        this.topicName = topicName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }


}
