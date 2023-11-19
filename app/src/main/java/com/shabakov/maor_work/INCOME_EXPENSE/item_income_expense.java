package com.shabakov.maor_work.INCOME_EXPENSE;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class item_income_expense {
    private String name;
    private String date;
    private String Reason;
    private Boolean ischeck;
    private String price;
    private Boolean ex_or_in;

    public item_income_expense() {
    }

    public item_income_expense(String name, String date, String reason, Boolean ischeck, String price, Boolean ex_or_in) {
        this.name = name;
        this.date = date;
        this.Reason = reason;
        this.ischeck = ischeck;
        this.price = price;
        this.ex_or_in = ex_or_in;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public void setIscheck(Boolean ischeck, String id_Building) {
        this.ischeck = ischeck;
        if (ischeck & id_Building != null) {
            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Income_expense").child(id_Building);
            ref1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.getKey().equals(getDate() + "-" + getName() + "-" + getReason())) {
                            ds.getRef().removeValue();
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void setIscheck(Boolean ischeck) {
        this.ischeck = ischeck;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Boolean getEx_or_in() {
        return ex_or_in;
    }

    public void setEx_or_in(Boolean ex_or_in) {
        this.ex_or_in = ex_or_in;
    }
}