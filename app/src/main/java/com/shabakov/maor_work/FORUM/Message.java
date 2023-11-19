package com.shabakov.maor_work.FORUM;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Message {
    private String message_content;
    private String Date;
    private String Name;
    private boolean belongsToCurrentUser;
    private String uid;
    private String key;

    public Message(String message_content, String date, String name, boolean belongsToCurrentUser, String uid, String key) {
        this.message_content = message_content;
        this.Date = date;
        this.Name = name;
        this.belongsToCurrentUser = belongsToCurrentUser;
        this.uid = uid;
        this.key = key;
    }

    public Message() {

    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public void setBelongsToCurrentUser(boolean belongsToCurrentUser) {
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void delete_message(String id_Building) {

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Forum").child(id_Building);
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey().equals(getKey())) {
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}