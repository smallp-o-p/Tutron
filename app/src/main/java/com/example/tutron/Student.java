package com.example.tutron;

import java.io.Serializable;

public class Student implements Serializable {

    private String FirstName;

    private String LastName;

    private String Address;

    private String CardNumber;

    private String cvv;

    private String ExpMonth;

    private String ExpYear;

    public Student(String firstName, String lastName, String address, String cardNumber, String cvv, String expMonth, String expYear) {
        FirstName = firstName;
        LastName = lastName;
        Address = address;
        CardNumber = cardNumber;
        this.cvv = cvv;
        ExpMonth = expMonth;
        ExpYear = expYear;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpMonth() {
        return ExpMonth;
    }

    public void setExpMonth(String expMonth) {
        ExpMonth = expMonth;
    }

    public String getExpYear() {
        return ExpYear;
    }

    public void setExpYear(String expYear) {
        ExpYear = expYear;
    }
}
