package com.shabakov.maor_work.INCOME_EXPENSE;

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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.Vector;

public class Income_expenses extends AppCompatActivity {
    public static Boolean in_ex = false;
    private static final String TAG = "RecyclerViewAdapter";
    private TextView balance, total_income, total_expense;
    private Button add_income, add_expense;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore fstore;
    public static String id_Building;
    static Vector<Integer> money_list = new Vector<Integer>();
    static Vector<item_income_expense> list_income_expense = new Vector<item_income_expense>();
    RecyclerView recyclerView;
    View view;
    String date;
    private ImageButton Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_expenses);

        recyclerView = findViewById(R.id.recyclerv_view_income);
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fstore = FirebaseFirestore.getInstance();
        add_expense = findViewById(R.id.add_expense);
        add_income = findViewById(R.id.add_income);
        balance = findViewById(R.id.total_balance);
        total_expense = findViewById(R.id.total_expense);
        total_income = findViewById(R.id.total_income);
        view = findViewById(R.id.view);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pop_income_expense);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        get_id_Building();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Animatoo.animateSlideDown(Income_expenses.this);
            }
        });
        Back = findViewById(R.id.Back2);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                in_ex = true;
                EditText dialog_reason = dialog.findViewById(R.id.Reason0);
                EditText dialog_Amount = dialog.findViewById(R.id.Amount0);
                EditText dialog_name = dialog.findViewById(R.id.name02);
                ImageButton imageButton = dialog.findViewById(R.id.fixed_button02);
                dialog.show();
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        in_ex = true;
                        if (!isnumber(dialog_Amount.getText().toString())) {
                            dialog_Amount.setError("נא הכנס רק מספרים");
                            return;
                        }
                        if (isnumber(dialog_Amount.getText().toString())) {
                            item_income_expense item = new item_income_expense(dialog_name.getText().toString(), get_date(), dialog_reason.getText().toString(), false, dialog_Amount.getText().toString(), in_ex);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("Income_expense").child(id_Building).child(get_date() + "-" + item.getName() + "-" + item.getReason());
                            myRef.setValue(item);
                            list_income_expense.add(item);
                            dialog.dismiss();
                            dialog_Amount.getText().clear();
                            dialog_name.getText().clear();
                            dialog_reason.getText().clear();
                        }
                    }
                });
            }

        });

        add_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in_ex = false;
                EditText dialog_reason = dialog.findViewById(R.id.Reason0);
                EditText dialog_Amount = dialog.findViewById(R.id.Amount0);
                EditText dialog_name = dialog.findViewById(R.id.name02);
                ImageButton imageButton = dialog.findViewById(R.id.fixed_button02);
                dialog.show();
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isnumber(dialog_Amount.getText().toString())) {
                            dialog_Amount.setError("נא הכנס רק מספרים");
                            return;
                        }
                        if (isnumber(dialog_Amount.getText().toString())) {
                            item_income_expense item = new item_income_expense(dialog_name.getText().toString(), get_date(), dialog_reason.getText().toString(), false, dialog_Amount.getText().toString(), in_ex);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("Income_expense").child(id_Building).child(get_date() + "-" + item.getName() + "-" + item.getReason());
                            myRef.setValue(item);
                            list_income_expense.add(item);
                            dialog.dismiss();
                            dialog_Amount.getText().clear();
                            dialog_name.getText().clear();
                            dialog_reason.getText().clear();
                        }
                    }
                });
            }
        });

        get_id_Building();
        get_balance();
    }

    public String get_date() {
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());
        date = (currentDate);
        return date;
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
                        get_balance();
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
        RecylerViewAdapter_income_expense.RecyclerViewAdapter adapter = new RecylerViewAdapter_income_expense.RecyclerViewAdapter(this, list_income_expense, in_ex);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "initRecyclerView: init recyclerview.");

    }

    public int To_number(String n) {
        int num = Integer.parseInt(n);
        return num;
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
                money_list.removeAllElements();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    item_income_expense income_expense = new item_income_expense("656", "745", "789", false, "44", in_ex);
                    income_expense = ds.getValue(item_income_expense.class);
                    list_income_expense.add(income_expense);
                    int num = To_number(income_expense.getPrice());
                    if (!income_expense.getEx_or_in())
                        num = num * -1;
                    money_list.add(num);
                }
                initRecyclerView();
                get_balance();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    @SuppressLint("SetTextI18n")
    public void get_balance() {
        String n = "";
        int sum = 0, sum2 = 0;
        if (money_list.isEmpty()) {
            total_income.setText(n.valueOf(sum) + " ₪");
            total_expense.setText(n.valueOf(sum) + " ₪");
            balance.setText(n.valueOf(sum) + " ₪");
            return;
        }
        for (int i = 0; i < money_list.size(); i++) {
            if (money_list.get(i) < 0)
                sum2 += money_list.get(i);
            else sum += money_list.get(i);
        }

        total_income.setText((Integer.toString(sum)) + " ₪");
        balance.setText((Integer.toString(sum + sum2)) + " ₪");
        sum2 *= -1;
        total_expense.setText((Integer.toString(sum2)) + " ₪");
        sum2 = sum = 0;
    }

    public static Boolean isnumber(String str) {
        int intValue;
        try {
            intValue = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}