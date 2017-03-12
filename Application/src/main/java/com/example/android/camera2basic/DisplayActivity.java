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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class DisplayActivity extends AppCompatActivity {

    HashMap<String,String> monthsCal = new HashMap<String,String>();
    HashMap<String,String> monthsCalFull = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        monthsCal.put("01","Jan");
        monthsCal.put("02","Feb");
        monthsCal.put("03","Mar");
        monthsCal.put("04","Apr");
        monthsCal.put("05","May");
        monthsCal.put("06","Jun");
        monthsCal.put("07","Jul");
        monthsCal.put("08","Aug");
        monthsCal.put("09","Sep");
        monthsCal.put("10","Oct");
        monthsCal.put("11","Nov");
        monthsCal.put("12","Dec");

        monthsCalFull.put("01","January");
        monthsCalFull.put("02","February");
        monthsCalFull.put("03","March");
        monthsCalFull.put("04","April");
        monthsCalFull.put("05","May");
        monthsCalFull.put("06","June");
        monthsCalFull.put("07","July");
        monthsCalFull.put("08","August");
        monthsCalFull.put("09","September");
        monthsCalFull.put("10","October");
        monthsCalFull.put("11","November");
        monthsCalFull.put("12","December");

        String id = getIntent().getExtras().getString("eventId");
        String lat = getIntent().getExtras().getString("latitude");
        String longi = getIntent().getExtras().getString("longitude");
        Log.d("longlat", lat+" "+longi);
        String hostId = getIntent().getExtras().getString("eventHostId");
        String coverIds = getIntent().getExtras().getString("coverIds");
        String relevantEvents = getIntent().getExtras().getString("relevantEvents");
        String aboutHost = getIntent().getExtras().getString("aboutHost");
        String eventHostName = getIntent().getExtras().getString("eventHostName");
        String monthTime = monthsCal.get(getIntent().getExtras().getString("start_time").substring(5,7));
        String dayTime = getIntent().getExtras().getString("start_time").substring(8,10);
        String timeFull = monthsCalFull.get(getIntent().getExtras().getString("start_time").substring(5,7))+" "+ dayTime+" " +getIntent().getExtras().getString("start_time").substring(11,16);
        String locationName = getIntent().getExtras().getString("locationName");
        String description = getIntent().getExtras().getString("description");
        String name = getIntent().getExtras().getString("name");
        String imageDir = getIntent().getExtras().getString("coverPhoto");

        Log.d("fulltime",timeFull);

        DisplayFragment fragment =  DisplayFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("latitude",lat);
        bundle.putString("longitude",longi);
        bundle.putString("aboutHost",aboutHost);
        bundle.putString("eventHostId",hostId);
        bundle.putString("fullTime", timeFull);
        bundle.putString("coverIds",coverIds);
        bundle.putString("description",description);
        bundle.putString("monthTime",monthTime);
        bundle.putString("name",name);
        bundle.putString("eventHostName",eventHostName);
        bundle.putString("dayTime",dayTime);
        bundle.putString("coverPhoto",imageDir);
        bundle.putString("locationName",locationName);
        bundle.putString("eventId",id);
        bundle.putString("relevantEvents",relevantEvents);


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
