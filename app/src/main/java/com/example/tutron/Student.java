package com.example.tutron;

public class Student {

    private Integer UUID;

    private String Address;

    private String CardNumber;

    private String cvv;

    private Integer ExpMonth;

    private Integer ExpYear;

    public Student(Integer UUID, String address, String cardNumber, String cvv, Integer expMonth, Integer expYear) {
        this.UUID = UUID;
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

    public Integer getExpMonth() {
        return ExpMonth;
    }

    public void setExpMonth(Integer expMonth) {
        ExpMonth = expMonth;
    }

    public Integer getExpYear() {
        return ExpYear;
    }

    public void setExpYear(Integer expYear) {
        ExpYear = expYear;
    }
}
