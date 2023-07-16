package com.example.tutron;

import android.media.Image;

public class Tutor {

    private String FirstName;
    private String LastName;
    private String lang;
    private String Education;
    private String description;
    private Image pfp;
    private boolean Suspended;
    private String SuspendTime;
    private double rating;

    private double hourly;

    public String getUUID() {
        return UUID;
    }
    public Tutor setUUID(String UUID) {
        this.UUID = UUID;
        return this;
    }

    private String UUID;

    public Tutor(){}

    public Tutor(String firstName, String lastName, String lang, String education, String description, Boolean suspended, String SuspendTime, double rating, double hourly) {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.lang = lang;
        this.Education = education;
        this.description = description;
        this.Suspended = suspended;
        this.SuspendTime = SuspendTime;
        this.rating = rating;
        this.hourly = hourly;
    }
    public void SetSuspended(){
        this.Suspended = true;
    }
    public void temp_suspension(String suspend_time){
        this.SuspendTime = suspend_time;
    }
    public boolean GetSuspended(){
        return Suspended;
    }
    public String getSuspendTime() {
        return SuspendTime;
    }
    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }
    public void setLastName(String lastName) {
        this.LastName = lastName;
    }
    public void setLang(String lang) {
        this.lang = lang;
    }
    public void setEducation(String education) {
        Education = education;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPfp(Image pfp) {
        this.pfp = pfp;
    }
    public String getLang() {
        return lang;
    }
    public String getEducation() {
        return Education;
    }

    public String getDescription() {
        return description;
    }

    public Image getPfp() {
        return pfp;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

}
