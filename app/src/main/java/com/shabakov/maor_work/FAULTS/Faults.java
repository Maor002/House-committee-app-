package com.shabakov.maor_work.FAULTS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shabakov.maor_work.R;
import com.shabakov.maor_work.MODELS.UserDetail;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.Vector;

public class Faults extends AppCompatActivity {
    private static final String TAG = "RecyclerViewAdapter";
    public static String id_Building;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore fstore;
    private static Vector<Fault_item> faults_array = new Vector<Fault_item>();
    private TextView addItem;
    private ImageButton Back, Delete;
    private Button addItem2;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faults);

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());
        date = (currentDate);

        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fstore = FirebaseFirestore.getInstance();
        addItem2 = findViewById(R.id.addfaults2);
        addItem = findViewById(R.id.addfaults);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.fault_item_pop);
        dialog.setTitle("Dialog box");
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        get_id_Building();

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView date;
                String n;
                Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());
                EditText dialog_titel = dialog.findViewById(R.id.title2);
                EditText dialog_content = dialog.findViewById(R.id.content2);
                EditText dialog_name = dialog.findViewById(R.id.name2);
                ImageButton imageButton = dialog.findViewById(R.id.fixed_button2);
                dialog.show();
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fault_item fault_item = new Fault_item(dialog_titel.getText().toString(), dialog_name.getText().toString(), dialog_content.getText().toString(), currentDate);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Faults").child(id_Building).child(fault_item.getName() + "-" + fault_item.getTitel());
                        myRef.setValue(fault_item);
                        faults_array.add(fault_item);
                        dialog.dismiss();
                    }
                });

                get_faults_list();
            }
        });
        initRecyclerView();
        addItem2.setOnClickListener(v -> addItem.callOnClick());
        Back = findViewById(R.id.Back2);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view2);
        RecylerViewAdapter_fault.RecyclerViewAdapter adapter = new RecylerViewAdapter_fault.RecyclerViewAdapter(this, faults_array);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

    public void get_faults_list() {

        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fstore = FirebaseFirestore.getInstance();

        DatabaseReference ref2 = null;
        try {
            ref2 = FirebaseDatabase.getInstance().getReference("Faults").child(id_Building);
        } catch (Exception e) {
            return;
        }
        ref2 = FirebaseDatabase.getInstance().getReference("Faults").child(id_Building);
        ref2.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                faults_array.removeAllElements();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Fault_item fault_item = new Fault_item("656", "745", "789", "44");
                    fault_item = ds.getValue(Fault_item.class);
                    faults_array.add(fault_item);
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

}