package com.shabakov.maor_work.LOGIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shabakov.maor_work.MODELS.CustomToast;
import com.shabakov.maor_work.R;
import com.shabakov.maor_work.MODELS.UserDetail;
import com.shabakov.maor_work.ui.HOME_PAGE.Admin_home_page1;
import com.shabakov.maor_work.ui.HOME_PAGE.HomeActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
    private ImageButton Login;
    private Button Register;
    private EditText fullName;
    private EditText email;
    private EditText password;
    private EditText re_password;
    private EditText Id_Building;
    private ProgressBar progressbar;
    private TextView login2;
    private Spinner option_Id_Building;
    public static final String TAG = "TAG";
    public List<UserDetail> listUsers = new ArrayList<UserDetail>();
    public List<String> items = new ArrayList<String>();
    private boolean isadmin;
    FirebaseFirestore fstore;
    String userID;
    CheckBox checkBox;
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Login = (ImageButton) findViewById(R.id.Login);
        checkBox = findViewById(R.id.checkBox);
        Register = findViewById(R.id.register);
        fullName = findViewById(R.id.et_name);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.password);
        re_password = findViewById(R.id.re_password);
        Id_Building = findViewById(R.id.Building_ID);
        option_Id_Building = findViewById(R.id.option_Building_ID);
        progressbar = findViewById(R.id.progressBar);
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fstore = FirebaseFirestore.getInstance();


        getBuilding_spinner(items);
        option_Id_Building.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedValue = items.get(position);
                Id_Building.setText(selectedValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isadmin = checkBox.isChecked();
                if (Id_Building.isShown()) {
                    Id_Building.setVisibility(View.INVISIBLE);
                    option_Id_Building.setVisibility(VISIBLE);
                } else {
                    Id_Building.setVisibility(VISIBLE);
                    option_Id_Building.setVisibility(View.INVISIBLE);
                    getBuilding_spinner(items);

                }
            }

        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String FullName = fullName.getText().toString();
                final String Email = email.getText().toString();
                final String pass = password.getText().toString();
                final String re_pass = re_password.getText().toString();
                final String id_Build = Id_Building.getText().toString().trim().replaceAll("[ ]{2,}", " ");
                String debt_pay = "0";

                if (TextUtils.isEmpty(Email)) {
                    email.setError("האימייל לא הוזן");
                    return;
                }
                if (!Email.contains("@")) {
                    email.setError("האימייל לא תקין");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    password.setError("הסיסמה לא הוזנה");
                    return;
                }
                if (pass.length() < 6) {
                    password.setError("הסיסמה חייבת להיות לפחות 6 ספרות");
                    return;
                }
                if (!pass.equals(re_pass)) {
                    password.setError("הסיסמאות אינם זהות");
                    return;
                }
                if (isadmin && Id_Building.length() == 0) {
                    Id_Building.setError("זיהוי בניין ריק");
                    return;

                }
                if (isadmin && !Id_Building.getText().toString().contains(",")) {
                    Id_Building.setError("זיהוי בניין לא תקין");
                    return;
                }
                if (!isadmin && Id_Building.getText().toString().equals("תבחר כתובת בניין")) {
                    Id_Building.setError("בבקשה תבחר בניין");
                    return;

                }


                if (isadmin) {
                    getUserDetail(listUsers);
                    for (int i = 0; i < listUsers.size(); i++) {
                        UserDetail userDetail = listUsers.get(i);
                        if (Id_Building.getText().toString().equals(userDetail.getId_Building())) {
                            Id_Building.setError("זיהוי בניין תפוס");
                            listUsers.clear();
                            return;
                        }
                    }
                }

                mFirebaseAuth.createUserWithEmailAndPassword(Email, pass)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    CustomToast.createToast(MainActivity.this,
                                            "ההרשמה לא הצליחה ", true);
                                } else {
                                    progressbar.setVisibility(VISIBLE);
                                    UserDetail userDetail = new UserDetail(FullName, Email, pass, Id_Building.getText().toString(), debt_pay, checkBox.isChecked());
                                    String uid = task.getResult().getUser().getUid();
                                    firebaseDatabase.getReference("Users").child(uid).setValue(userDetail)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Intent intent = new Intent(MainActivity.this,
                                                            HomeActivity.class);
                                                    Intent intent_admin = new Intent(MainActivity.this,
                                                            Admin_home_page1.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    intent.putExtra("name", FullName);
                                                    userID = mFirebaseAuth.getCurrentUser().getUid();
                                                    DocumentReference documentReference = fstore.collection("users").document(userID);
                                                    Map<String, Object> user = new HashMap<>();
                                                    user.put("fName", FullName);
                                                    user.put("email", Email);
                                                    user.put("pass", pass);
                                                    user.put("Id_Build", Id_Building.getText().toString().replaceAll("\\s+", " "));
                                                    user.put("pay", debt_pay);
                                                    user.put("isadmin", checkBox.isChecked());

                                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                                        }
                                                    });
                                                    progressbar.setVisibility(View.INVISIBLE);
                                                    if (isadmin)
                                                        startActivity(intent_admin);
                                                    if (!isadmin)
                                                        startActivity(intent);

                                                }
                                            });
                                }

                            }
                        });
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignIn.class));
                Animatoo.animateSlideRight(MainActivity.this);
            }
        });

        login2 = findViewById(R.id.swipeLeft);
        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignIn.class));
                Animatoo.animateSlideRight(MainActivity.this);
            }
        });
    }

    public void getUserDetail(List<UserDetail> listUsers) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUsers.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserDetail userDetail = ds.getValue(UserDetail.class);
                    listUsers.add(userDetail);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getBuilding_spinner(List<String> items) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                items.add("תבחר כתובת בניין");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserDetail userDetail = ds.getValue(UserDetail.class);
                    if (userDetail.isIsadmin()) {
                        items.add(userDetail.getId_Building().toString());
                    }

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                option_Id_Building.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}