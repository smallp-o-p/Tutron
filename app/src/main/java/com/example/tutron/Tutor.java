package com.example.tutron;

import android.media.Image;

public class Tutor {

    private String FirstName;
    private String LastName;
    private String lang;
    private String Education;
    private String description;
    private Image pfp;

    public Tutor(String firstName, String lastName, String lang, String education, String description) {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.lang = lang;
        this.Education = education;
        this.description = description;
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
