package com.shabakov.maor_work.Tenants_debts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shabakov.maor_work.FORUM.RecylerViewAdapter;
import com.shabakov.maor_work.MODELS.Show_profile;
import com.shabakov.maor_work.MODELS.UserDetail;
import com.shabakov.maor_work.R;
import com.shabakov.maor_work.ui.HOME_PAGE.Admin_home_page1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class admin_list_tenants extends AppCompatActivity {
    private static final String TAG = "ViewDatabase";
    public static String id_Building = "0";
    // public static Button save;
    public boolean isinit = false;
    private ImageButton bake;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabaseReference;
    FirebaseDatabase firebaseDatabase;
    String userID;
    FirebaseFirestore fstore;
    public static List<UserDetail> userList = new ArrayList<UserDetail>();
    public static List<Show_profile> userList_2 = new ArrayList<Show_profile>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_tenants);
        //    save=findViewById(R.id.save);
        bake = findViewById(R.id.bake);
        bake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin_list_tenants.this, Admin_home_page1.class));
            }
        });

        prepareUser();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        RecylerViewAdapter.RecyclerViewAdapter adapter = new RecylerViewAdapter.RecyclerViewAdapter(this, userList_2);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //  adapter.notifyItemChanged(0);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        isinit = true;
    }

    private List<Show_profile> prepareUser() {

        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fstore = FirebaseFirestore.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                userList_2.clear();
                userID = Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(userID)) {
                        UserDetail userDetail002 = ds.getValue(UserDetail.class);
                        id_Building = userDetail002.getId_Building();
                        break;
                    }
                }
                int j = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserDetail userDetail = ds.getValue(UserDetail.class);
                    if (userDetail.getId_Building().equals(id_Building)) {
                        userList.add(userDetail);
                        Show_profile user = new Show_profile(userList.get(j).getFullName(), userList.get(j).getEmail(), userList.get(j).getDebt_pay());
                        user.setID(ds.getKey());
                        userList_2.add(user);
                        userList.clear();
                        Log.d(TAG, "Value is: " + userList_2.get(j).getFname());

                    }

                }

                initRecyclerView();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return userList_2;

    }
}