package com.shabakov.maor_work.MODELS;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Firebase {
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseFirestore fstore;
    private String file;

    Firebase(String file){
        this.file = file;
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fstore = FirebaseFirestore.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(file);
    }


    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
