package com.shabakov.maor_work.FAULTS;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shabakov.maor_work.R;

import java.util.Vector;

public class RecylerViewAdapter_fault {
    public static class RecyclerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter_fault.RecyclerViewAdapter.ViewHolder> {
        private static final String TAG = "RecyclerViewAdapter";
        private Context context;
        public Dialog dialog;
        private Vector<Fault_item> faults = new Vector<Fault_item>();

        public RecyclerViewAdapter(Context context, Vector<Fault_item> faults) {
            this.faults = faults;

        }

        @NonNull
        @Override
        public RecylerViewAdapter_fault.RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fault_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            holder.Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    faults.get(holder.getAdapterPosition()).setIscheck(true, Faults.id_Building);
                }
            });

            dialog = new Dialog((context));
            dialog.setContentView(R.layout.fault_item_pop);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.content.setText("תוכן: " + faults.get(position).getDetail());
            holder.title.setText("כותרת: " + faults.get(position).getTitel());
            holder.name.setText("שם דייר: " + faults.get(position).getName());
            holder.date.setText("תאריך: " + faults.get(position).getDate());
        }

        @Override
        public int getItemCount() {
            return faults.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, content, name, date;
            ImageButton Delete;
            ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title1);
                content = itemView.findViewById(R.id.content1);
                name = itemView.findViewById(R.id.name1);
                Delete = itemView.findViewById(R.id.Delete);
                imageView = itemView.findViewById(R.id.image2);
                date = itemView.findViewById(R.id.date);

            }
        }
    }

}