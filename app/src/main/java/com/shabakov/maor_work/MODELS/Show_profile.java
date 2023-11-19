package com.shabakov.maor_work.MODELS;

public class Show_profile {
    private String fname;
    private String email;
    private String pay;
    private String ID;

    public Show_profile(String fname, String email, String pay) {
        this.fname = fname;
        this.email = email;
        this.pay = pay;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
