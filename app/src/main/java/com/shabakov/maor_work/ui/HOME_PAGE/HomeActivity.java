package com.shabakov.maor_work.ui.HOME_PAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shabakov.maor_work.FAULTS.Faults;
import com.shabakov.maor_work.FORUM.forum_chet;
import com.shabakov.maor_work.INCOME_EXPENSE.Income_expenses;
import com.shabakov.maor_work.R;
import com.shabakov.maor_work.LOGIN.SignIn;
import com.shabakov.maor_work.MODELS.UserDetail;

import java.text.DateFormat;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ViewDatabase";
    DatabaseReference mDatabaseReference;
    ImageButton btnLogout;
    private ImageButton pay, malfunction, forum, income_expenses;
    TextView userTextView, debt, date,building_text;
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String userID;
    FirebaseFirestore fstore;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());
        pay = (ImageButton) findViewById(R.id.pay);
        date = findViewById(R.id.date);
        date.setText(currentDate);
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userTextView = findViewById(R.id.loggedInTextView);
        debt = findViewById(R.id.Debt);
        income_expenses = findViewById(R.id.Income_expenses);
        forum = (ImageButton) findViewById(R.id.forum);
        building_text = findViewById(R.id.Building);
        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, forum_chet.class));
                Animatoo.animateInAndOut(HomeActivity.this);
            }
        });
        income_expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, Income_expenses.class));
                Animatoo.animateInAndOut(HomeActivity.this);
            }
        });
        malfunction = findViewById(R.id.malfunction2);
        if (mFirebaseAuth.getCurrentUser() == null)
            return;
        userID = mFirebaseAuth.getCurrentUser().getUid();
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fstore = FirebaseFirestore.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mDatabaseReference = firebaseDatabase.getReference("Users");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(userID)) {
                        UserDetail userDetail = ds.getValue(UserDetail.class);
                        debt.setText("יש לתשלום :" + userDetail.getDebt_pay().toString() + " ₪");
                        userTextView.setText("ברוך הבא  " + userDetail.getFullName().toString());
                        building_text.setText("זיהוי בניין :"+userDetail.getId_Building());
                        break;
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        btnLogout = findViewById(R.id.logout);
        btnLogout.setOnClickListener(this);
        malfunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, Faults.class));
                Animatoo.animateInAndOut(HomeActivity.this);
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.bnhp.payments.paymentsapp&hl=en_US"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == btnLogout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(HomeActivity.this, SignIn.class));
            finish();
        }
    }

}