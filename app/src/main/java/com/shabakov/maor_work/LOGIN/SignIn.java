package com.shabakov.maor_work.LOGIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shabakov.maor_work.MODELS.CustomToast;
import com.shabakov.maor_work.R;
import com.shabakov.maor_work.MODELS.UserDetail;
import com.shabakov.maor_work.ui.HOME_PAGE.Admin_home_page1;
import com.shabakov.maor_work.ui.HOME_PAGE.HomeActivity;

public class SignIn extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText emailId, password;
    Button btnSignIn;
    ImageButton tvSignUp;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseFirestore fstore;
    String userID;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailId = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        btnSignIn = findViewById(R.id.login);
        tvSignUp = findViewById(R.id.register);
        fstore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    // moveToHomeActivity(mFirebaseUser);
                } else {
                    CustomToast.createToast(SignIn.this, "בבקשה התחבר",
                            false);
                }
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();

                if (email.isEmpty()) {
                    emailId.setError("אימייל לא תקין");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("סיסמה לא תקינה");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(SignIn.this, "שדות ריקות", Toast.LENGTH_LONG).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    firebaseAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(SignIn.this,
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(SignIn.this, "שם משתמש או סיסמה אינם נכונים",
                                                        Toast.LENGTH_LONG).show();
                                            } else {
                                                //
                                                moveToHomeActivity(task.getResult().getUser());
                                            }

                                        }
                                    });
                } else {
                    Toast.makeText(SignIn.this, "נכשל !", Toast.LENGTH_LONG).show();
                }

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, MainActivity.class));
                Animatoo.animateSlideLeft(SignIn.this);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void moveToHomeActivity(FirebaseUser mFirebaseUser) {

        firebaseDatabase.getReference("Users").child(mFirebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserDetail userDetail = snapshot.getValue(UserDetail.class);
                        String name = userDetail.getFullName();
                        Intent i = new Intent(getApplicationContext(), Admin_home_page1.class);
                        CustomToast.createToast(getApplicationContext(), "התחברות הצליחה",
                                false);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("name", name);
                        checkifuseradmin(mFirebaseUser.getUid());

                        //  startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void checkifuseradmin(String userID) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDatabaseReference = firebaseDatabase.getReference("Users");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(userID)) {
                        UserDetail userDetail = ds.getValue(UserDetail.class);
                        if (!userDetail.isIsadmin()) {
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(getApplicationContext(), Admin_home_page1.class));
                            finish();
                        }
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
    }
}