package com.shabakov.maor_work.MODELS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shabakov.maor_work.R;

public class CustomToast extends AppCompatActivity {

    private TextView toastTextView;

    public static void createToast(Context context, String message, boolean error) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.activity_custom_toast, null);
        TextView toastTextView = view.findViewById(R.id.textViewToast);

        SpannableString spannableString = new SpannableString(message);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), 0);
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);

        toastTextView.setText(spannableString);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);

        if (error) {
            toastTextView.setTextColor(Color.parseColor("#830300"));
        } else {
            toastTextView.setTextColor(Color.parseColor("#149414"));
        }

        toast.setGravity(Gravity.BOTTOM, 20, 20);
        toast.show();
    }
}