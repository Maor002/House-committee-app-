package com.shabakov.maor_work.ui.HOME_PAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.shabakov.maor_work.Tenants_debts.admin_list_tenants;

import java.text.DateFormat;
import java.util.Calendar;

public class Admin_home_page1 extends AppCompatActivity {
    private static final String TAG = "ViewDatabase";
    private ImageButton pay, malfunction, logout, forum, income_expenses, duties_tenants, help;
    private TextView userTextView, debt, date, building_text;
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String userID;
    DatabaseReference mDatabaseReference;
    FirebaseFirestore fstore;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page1);
        pay = (ImageButton) findViewById(R.id.pay);
        malfunction = (ImageButton) findViewById(R.id.malfunction);
        logout = (ImageButton) findViewById(R.id.logout);
        forum = (ImageButton) findViewById(R.id.forum);
        income_expenses = (ImageButton) findViewById(R.id.Income_expenses);
        duties_tenants = (ImageButton) findViewById(R.id.Duties_tenants);
        help = (ImageButton) findViewById(R.id.help);

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());
        date = findViewById(R.id.date);
        date.setText(currentDate);
        firebaseDatabase = FirebaseDatabase.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userTextView = findViewById(R.id.loggedInTextView2);
        debt = findViewById(R.id.Debt2);
        building_text = findViewById(R.id.Building);
        mFirebaseAuth = FirebaseAuth.getInstance();
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
                        userTextView.setText("ברוך הבא \nועד הבית " + userDetail.getFullName().toString());
                        building_text.setText("זיהוי בניין :" + userDetail.getId_Building());
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

        duties_tenants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin_home_page1.this, admin_list_tenants.class));
                Animatoo.animateInAndOut(Admin_home_page1.this);
            }
        });
        income_expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin_home_page1.this, Income_expenses.class));
                Animatoo.animateInAndOut(Admin_home_page1.this);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(Admin_home_page1.this, SignIn.class));
                finish();
            }
        });
        malfunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin_home_page1.this, Faults.class));
                Animatoo.animateInAndOut(Admin_home_page1.this);
            }
        });
        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin_home_page1.this, forum_chet.class));
                Animatoo.animateInAndOut(Admin_home_page1.this);
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

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(Admin_home_page1.this);

                builder.setIcon(R.drawable.book);
                //Setting message manually and performing action on button click
                builder.setMessage("ברוכים הבאים לוועד הבית בקליק  " + "אפליקציה זאת הינה ראשונית ביישום שלה ולכם לא הכל מוכן אבל אל דאגה אנחנו ממשיכים לפתח את האפליקציה.  " +
                        " כמה דגשים :  " +
                        " האפליקציה אינה תומכת באפליציית תשלומים לכן יש קישור להעברה בביט. " +
                        " אחרי ביצוע התשלום, על הוועד בית לעדכן את זה ובהתאם את החוב של דייריו פעולה זאת אינה מתבצעת אוטומטי.  ")
                        .setCancelable(false)

                        .setNegativeButton("בסדר, הבנתי.", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("עזרה");
                alert.show();
            }
        });

    }

}