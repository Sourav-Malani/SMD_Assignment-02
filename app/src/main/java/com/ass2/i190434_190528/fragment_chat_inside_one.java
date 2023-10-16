package com.ass2.i190434_190528;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class fragment_chat_inside_one extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_inside_one, container, false);

        // Find the AudiocallIcon view by its ID
        ImageView audioCallIcon = view.findViewById(R.id.AudiocallIcon);

        // Set OnClickListener for AudiocallIcon
        audioCallIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to AudioCallActivity (replace with your activity)
                Intent intent = new Intent(getActivity(), AudioCall.class);
                startActivity(intent);
            }
        });

        // Find the videoIcon view by its ID
        ImageView videoIcon = view.findViewById(R.id.videoIcon);

        // Set OnClickListener for videoIcon
        videoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to VideoCallActivity (replace with your activity)
                Intent intent = new Intent(getActivity(), VideoCall.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
