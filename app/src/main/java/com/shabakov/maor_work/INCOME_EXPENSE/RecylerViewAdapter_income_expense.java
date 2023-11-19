package com.shabakov.maor_work.INCOME_EXPENSE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.shabakov.maor_work.R;

import java.util.Vector;

public class RecylerViewAdapter_income_expense {
    public static class RecyclerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter_income_expense.RecyclerViewAdapter.ViewHolder> {
        private final int LAYOUT_ONE = 0;
        private final int LAYOUT_TWO = 1;
        private static final String TAG = "RecyclerViewAdapter";
        private Context context;
        private Vector<item_income_expense> list_income_expense = new Vector<item_income_expense>();
        private boolean in_ex;

        public RecyclerViewAdapter(Context context, Vector<item_income_expense> list_income_expense, boolean in_ex) {
            this.list_income_expense = list_income_expense;
            this.in_ex = in_ex;
        }

        @NonNull
        @Override
        public RecylerViewAdapter_income_expense.RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_item, parent, false);

            switch (viewType) {
                case LAYOUT_ONE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_item, parent, false);
                    return new ViewHolder(view);

                case LAYOUT_TWO:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
                    return new ViewHolder(view);
                default:
            }
            return new ViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            if (list_income_expense.get(position).getEx_or_in())
                return LAYOUT_ONE;
            else
                return LAYOUT_TWO;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.name.setText("שם דייר: " + list_income_expense.get(position).getName());
            holder.date.setText("תאריך: " + list_income_expense.get(position).getDate());
            holder.price.setText("תשלום :" + list_income_expense.get(position).getPrice() + "₪");
            holder.Reason.setText("סיבה : " + list_income_expense.get(position).getReason());
            holder.ischeck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list_income_expense.get(holder.getAdapterPosition()).setIscheck(true, Income_expenses.id_Building);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context = holder.itemView.getContext();
                    Intent intent = new Intent(context, All_income_expense.class);
                    context.startActivity(intent);
                    Animatoo.animateSlideDown(context);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list_income_expense.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, date, Reason;
            ImageButton ischeck;
            TextView price;

            @SuppressLint("ResourceType")
            public ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name01);
                date = itemView.findViewById(R.id.date01);
                Reason = itemView.findViewById(R.id.Reason);
                price = itemView.findViewById(R.id.Amount);
                ischeck = itemView.findViewById(R.id.Delete01);
            }
        }
    }

}