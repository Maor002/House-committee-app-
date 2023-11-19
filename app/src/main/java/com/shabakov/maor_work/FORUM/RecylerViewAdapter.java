package com.shabakov.maor_work.FORUM;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shabakov.maor_work.INCOME_EXPENSE.Income_expenses;
import com.shabakov.maor_work.R;
import com.shabakov.maor_work.MODELS.Show_profile;
import com.shabakov.maor_work.MODELS.p_user;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class RecylerViewAdapter {

    public static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private static final String TAG = "RecyclerViewAdapter";
        private Context context;
        private List<Show_profile> users = new ArrayList<>();
        public Dialog dialog;
        public static Vector<p_user> p_users = new Vector<p_user>();
        FirebaseAuth mFirebaseAuth;
        DatabaseReference mDatabaseReference;
        FirebaseDatabase firebaseDatabase;
        EditText number;
        String ID;

        public RecyclerViewAdapter(Context context, List<Show_profile> users) {
            this.users = users;

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @NonNull
        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users_pop, parent, false);
            ViewHolder holder = new ViewHolder(view);

            dialog = new Dialog((context));
            dialog.setContentView(R.layout.row_users);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setTitle("פרטי דייר");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TextView dialog_name = (TextView) dialog.findViewById(R.id.nameTv);
                    TextView dialog_pay = (TextView) dialog.findViewById(R.id.payTv);
                    dialog_name.setText(users.get(holder.getAdapterPosition()).getFname());
                    dialog_pay.setText(users.get(holder.getAdapterPosition()).getPay());
                    ID = users.get(holder.getAdapterPosition()).getID();
                    Button accept = dialog.findViewById(R.id.accept);
                    Button cancel = dialog.findViewById(R.id.cancel_button);

                    dialog.show();
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            number = (EditText) dialog.findViewById(R.id.payTv);
                            if (Income_expenses.isnumber(number.getText().toString())) {
                                number = (EditText) dialog.findViewById(R.id.payTv);
                                p_user user = new p_user(ID, number.getText().toString());
                                p_users.add(user);
                                holder.payTv.setText(number.getText());
                                dialog_pay.setText(number.getText());
                                //   admin_list_tenants.save.setVisibility(View.VISIBLE);
                                mFirebaseAuth = FirebaseAuth.getInstance();
                                firebaseDatabase = FirebaseDatabase.getInstance();
                                //  fstore= FirebaseFirestore.getInstance();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                ref.addValueEventListener(new ValueEventListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!RecylerViewAdapter.RecyclerViewAdapter.p_users.isEmpty()) {
                                            mDatabaseReference = firebaseDatabase.getReference("Users");
                                            for (int i = 0; i < p_users.size(); i++) {
                                                mDatabaseReference.child(RecylerViewAdapter.RecyclerViewAdapter.p_users.get(i).getID()).child("debt_pay").
                                                        setValue(RecylerViewAdapter.RecyclerViewAdapter.p_users.get(i).getNew_pay());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        // Failed to read value
                                        Log.w(TAG, "Failed to read value.", error.toException());
                                    }
                                });
                            } else return;
                            dialog.dismiss();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //     admin_list_tenants.save.setVisibility(View.INVISIBLE);
                            dialog.dismiss();
                        }
                    });

                }

            });

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: called.");

            holder.emailTv.setText("אימייל: " + users.get(position).getEmail());
            holder.nameTv.setText("שם מלא: " + users.get(position).getFname());
            holder.payTv.setText("יש לשלם: " + users.get(position).getPay());

        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView nameTv, payTv, emailTv;
            Button accept, cancel;
            RelativeLayout parentLayout;

            public ViewHolder(View itemView) {
                super(itemView);

                nameTv = itemView.findViewById(R.id.nameTv);
                payTv = itemView.findViewById(R.id.payTv);
                emailTv = itemView.findViewById(R.id.emailTv);
                parentLayout = itemView.findViewById(R.id.parent_layout);
                accept = itemView.findViewById(R.id.accept);
                cancel = itemView.findViewById(R.id.cancel_button);
            }
        }
    }
}