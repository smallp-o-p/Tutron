package com.example.tutron;

import android.media.Image;

public class Tutor {

    private String UUID;

    private String lang;

    private String Education;

    private String description;

    private Image pfp;

    public Tutor(String UUID, String lang, String education, String description) {
        this.UUID = UUID;
        this.lang = lang;
        this.Education = education;
        this.description = description;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
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

    public String getUUID() {
        return UUID;
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
}
