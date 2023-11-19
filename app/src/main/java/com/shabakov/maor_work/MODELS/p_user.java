package com.shabakov.maor_work.MODELS;

public class p_user {
    private String ID;
    private String new_pay;

    public p_user(String id, String new_pay) {
        this.ID = id;
        this.new_pay = new_pay;
    }

    public String getNew_pay() {
        return new_pay;
    }

    public void setNew_pay(String new_pay) {
        this.new_pay = new_pay;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
