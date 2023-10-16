package com.ass2.i190434_190528;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        EditText editText = view.findViewById(R.id.searchInput);

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    // Handle the Enter key press here
                    String searchedText = editText.getText().toString();
                    Intent intent = new Intent(requireActivity(), Searched.class);
                    intent.putExtra("searchedText", searchedText);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        // Return the inflated view
        return view;
    }
}
