package com.shabakov.maor_work.MODELS;


import com.google.firebase.database.DatabaseReference;

public class UserDetail {
    private String FullName;
    private String Email;
    private String pass;
    private String id_Building;
    private String Debt_pay;
    private boolean isadmin;


    public UserDetail() {


    }


    public UserDetail(String fullName, String email, String pass, String id_Building, String Debt_pay,boolean isadmin) {
        this.FullName = fullName;
        this.Email = email;
        this.pass = pass;
        this.id_Building = id_Building;
        this.Debt_pay = Debt_pay;
        this.isadmin=isadmin;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getId_Building() {
        return id_Building;
    }

    public void setId_Building(String id_Building) {
        this.id_Building = id_Building;
    }

    public String getDebt_pay() {
        return Debt_pay;
    }

    public void setDebt_pay(String Debt_pay) {
        this.Debt_pay = Debt_pay;
    }

    public boolean isIsadmin() {
        return isadmin;
    }

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }


}
