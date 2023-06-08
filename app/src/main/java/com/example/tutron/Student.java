package com.example.tutron;

import java.io.Serializable;

public class Student implements Serializable {

    private Integer UUID;

    private String Address;

    private String CardNumber;

    private String cvv;

    private String ExpMonth;

    private String ExpYear;

    public Student(String address, String cardNumber, String cvv, String expMonth, String expYear) {

        Address = address;
        CardNumber = cardNumber;
        this.cvv = cvv;
        ExpMonth = expMonth;
        ExpYear = expYear;
    }

    public Integer getUUID() {
        return UUID;
    }

    public void setUUID(Integer UUID) {
        this.UUID = UUID;
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
