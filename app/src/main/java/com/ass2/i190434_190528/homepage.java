package com.ass2.i190434_190528;


import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        // Get the TextView
        TextView welcomeTextView = findViewById(R.id.welcome_text);

        // Create a SpannableStringBuilder for the text
        SpannableStringBuilder spannableText = new SpannableStringBuilder("Welcome, Sourav");

        // Set "Welcome" text color to black
        spannableText.setSpan(
                new ForegroundColorSpan(Color.BLACK),
                0, 7, // Start and end indices for "Welcome"
                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Set "Sourav" text color to orange
        spannableText.setSpan(
                new ForegroundColorSpan(Color.parseColor("#FFA500")), // Orange color
                8, spannableText.length(), // Start and end indices for "Sourav"
                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Set the formatted text to the TextView
        welcomeTextView.setText(spannableText);
    }
}
