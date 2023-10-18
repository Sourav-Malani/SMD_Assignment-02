package com.ass2.i190434_190528;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EditProfile extends Activity {
    TextView btn_saveChanges;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        btn_saveChanges = findViewById(R.id.btn_saveChanges);

        btn_saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save changes to the database
                // After the user completes their profile
                SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("profileCompleted", true);
                editor.apply();

                Intent intent = new Intent(EditProfile.this, bottomnavigation.class);
                startActivity(intent);
            }
        });

    }
}
