package com.ass2.i190434_190528;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UploadImage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image);
    }
    public void videoButtonClicked(View view) {
        Intent intent = new Intent(this, UploadVideo.class);
        startActivity(intent);
    }

    public void photoButtonClicked(View view) {
        Intent intent = new Intent(this, UploadImage.class);
        startActivity(intent);

    }
}
