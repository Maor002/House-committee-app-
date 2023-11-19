package com.shabakov.maor_work.INCOME_EXPENSE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shabakov.maor_work.R;
import com.shabakov.maor_work.MODELS.UserDetail;

import java.util.Objects;
import java.util.Vector;

public class All_income_expense extends AppCompatActivity {
    private static final String TAG = "RecyclerViewAdapter";
    static Vector<item_income_expense> list_income_expense = new Vector<item_income_expense>();
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore fstore;
    public static String id_Building;
    private ImageButton view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_income_expense);

        view = findViewById(R.id.Back2);
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fstore = FirebaseFirestore.getInstance();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(All_income_expense.this, Income_expenses.class));
                Animatoo.animateSlideUp(All_income_expense.this);

            }
        });

        get_id_Building();
        setWindowParams();
    }

    public String get_id_Building() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fstore = FirebaseFirestore.getInstance();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Users");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userID;
                userID = Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey().equals(userID)) {
                        UserDetail userDetail002 = ds.getValue(UserDetail.class);
                        id_Building = userDetail002.getId_Building();
                        get_faults_list();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return id_Building;
    }

    public void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view_income2);
        RecylerViewAdapter_income_expense.RecyclerViewAdapter adapter = new RecylerViewAdapter_income_expense.RecyclerViewAdapter(this, Income_expenses.list_income_expense, false);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "initRecyclerView: init recyclerview.");

    }

    public void get_faults_list() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fstore = FirebaseFirestore.getInstance();
        DatabaseReference ref2 = null;
        try {
            ref2 = FirebaseDatabase.getInstance().getReference("Income_expense").child(id_Building);
        } catch (Exception e) {

        }
        ref2 = FirebaseDatabase.getInstance().getReference("Income_expense").child(id_Building);
        ref2.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_income_expense.removeAllElements();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    item_income_expense income_expense = new item_income_expense("656", "745", "789", false, "44", false);
                    income_expense = ds.getValue(item_income_expense.class);
                    list_income_expense.add(income_expense);
                }
                initRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void setWindowParams() {
        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.dimAmount = 0;
        wlp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        getWindow().setAttributes(wlp);
    }
}