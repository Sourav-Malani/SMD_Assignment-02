package com.ass2.i190434_190528;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ChatFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        ImageView arrow1 = rootView.findViewById(R.id.arrow1); // Replace 'arrow1' with the actual ID of the first ImageView.

        arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of the fragment you want to navigate to
                Fragment fragment = new fragment_chat_inside_one();

                // Get the FragmentManager
                FragmentManager fragmentManager = getParentFragmentManager();

                // Begin a transaction to replace the current fragment with the new one
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null); // Optional: Add the transaction to the back stack
                transaction.commit();
            }
        });

        return rootView;
    }
}
