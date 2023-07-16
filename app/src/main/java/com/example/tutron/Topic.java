package com.example.tutron;

public class Topic {
    private String name;
    private int exp;
    private String desc;
    public boolean offered;

    public Topic(){}
    public Topic(String name, String description, int exp, boolean offered) {
        this.name = name;
        this.exp = exp;
        this.desc = description;
        this.offered = offered;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String description) {
        this.desc = description;
    }

    public boolean isOffered() {
        return offered;
    }

    public void setOffered(boolean offered) {
        this.offered = offered;
    }
}
