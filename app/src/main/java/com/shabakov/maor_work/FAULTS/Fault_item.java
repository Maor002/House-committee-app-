package com.shabakov.maor_work.FAULTS;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shabakov.maor_work.R;

import java.util.Date;
import java.util.Objects;

public class Fault_item {
    private String titel;
    private String name;
    private String date;
    private int id = 0;
    private String detail;
    private Boolean ischeck;
    private ImageView image;

    public Fault_item() {

    }

    public Fault_item(String titel, String name, String detail, String date) {
        this.titel = titel;
        this.name = name;
        this.id++;
        this.detail = detail;
        this.ischeck = false;
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIscheck() {
        return ischeck;
    }

    public void setIscheck(Boolean ischeck, String id_Building) {
        this.ischeck = ischeck;
        if (ischeck & id_Building != null) {
            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Faults").child(id_Building);
            ref1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.getKey().equals(getName() + "-" + getTitel())) {
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

    public ImageView getImage() {
        return image;
    }

    public void setImage() {

        this.image.setImageResource(R.drawable.check);
    }

}