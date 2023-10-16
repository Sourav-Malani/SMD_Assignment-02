package com.ass2.i190434_190528;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the TextView
        TextView welcomeTextView = view.findViewById(R.id.welcome_text);

        // Find the item1_homepage view
        View item1HomepageView = view.findViewById(R.id.item1_homepage);

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

        // Set an OnClickListener for item1_homepage view
        item1HomepageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to open the rent_item page (you should replace RentItemActivity.class with your actual activity)
                Intent intent = new Intent(getActivity(), RentItem.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
