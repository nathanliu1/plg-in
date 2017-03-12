package com.example.android.camera2basic;

import android.content.Intent;
import android.hardware.camera2.params.Face;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, FacebookLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
