package com.example.android.camera2basic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);


//        String title = getIntent().getExtras().getString("title");
//        String pageid = getIntent().getExtras().getString("pageid");
//        String extract = getIntent().getExtras().getString("extract");
        String relevantEvents = getIntent().getExtras().getString("relevantEvents");
        String eventHostName = getIntent().getExtras().getString("eventHostName");
        String start_time = getIntent().getExtras().getString("start_time");
        String locationName = getIntent().getExtras().getString("locationName");
        String desription = getIntent().getExtras().getString("description");
        String name = getIntent().getExtras().getString("name");
        String imageDir = getIntent().getExtras().getString("coverPhoto");


        DisplayFragment fragment =  DisplayFragment.newInstance();
        Bundle bundle = new Bundle();
//        bundle.putString("title", title);
//        bundle.putString("pageid", pageid);
//        bundle.putString("extract", extract);
//        bundle.putDouble("distance", distance);
//        bundle.putString("lat",lat);
//        bundle.putString("long",longitude);
        bundle.putString("coverPhoto",imageDir);


        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
    }

    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }
}
