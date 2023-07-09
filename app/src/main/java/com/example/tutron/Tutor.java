package com.example.tutron;

import android.media.Image;

import java.util.List;

public class Tutor {

    private String FirstName;
    private String LastName;
    private String lang;
    private String Education;
    private String description;
    private Image pfp;
    private boolean suspended;

    private String SuspendTime;

    private List<String> profile_topics;

    private List<String> offered_topics;

    public Tutor(){}

    public Tutor(String firstName, String lastName, String lang, String education, String description, Boolean suspended, String SuspendTime, List<String> profile_topics, List<String> offered_topics) {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.lang = lang;
        this.Education = education;
        this.description = description;
        this.suspended = suspended;
        this.SuspendTime = SuspendTime;
        this.profile_topics = profile_topics;
        this.offered_topics = offered_topics;
    }
    public void suspend(){
        this.suspended = true;
    }
    public void temp_suspension(String suspend_time){
        this.SuspendTime = suspend_time;
    }
    public boolean GetSuspended(){
        return suspended;
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

    public List<String> getOffered_topics() {
        return offered_topics;
    }
    public void setOffered_topics(List<String> offered_topics) {
        this.offered_topics = offered_topics;
    }
    public void addOffered_topics(String topic){
        this.offered_topics.add(topic);
    }

    public void addProfile_topics(String topic){
        this.profile_topics.add(topic);
    }

    public List<String> getProfile_topics() {
        return profile_topics;
    }

    public void setProfile_topics(List<String> profile_topics) {
        this.profile_topics = profile_topics;
    }
}
