package com.ass2.i190434_190528;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Searched extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searched);

        // Retrieve the searched text from the intent extra
        String searchedText = getIntent().getStringExtra("searchedText");

        if (searchedText != null) {
            TextView searchedItemTextView = findViewById(R.id.searched_item_text);
            searchedItemTextView.setText(searchedText);
        }
    }

}
