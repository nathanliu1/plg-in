/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.camera2basic;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;
import android.support.v13.app.FragmentCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import com.facebook.FacebookSdk;

public class CameraActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    File imageData;
    Bitmap imageBitmap;

    private TextRecognizer detector;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Boolean isBitmap;
    Uri imagePath;

    Boolean wikiDone = false;
    String returnedText = "";





    runWikiLocationAPI wikilocation =new runWikiLocationAPI();

    private static final int MY_PERMISSION_VALUES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestAllPermissions();
            return;
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        detector = new TextRecognizer.Builder(getApplicationContext()).build();

        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }
    }

    private void requestAllPermissions() {
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}
                ,MY_PERMISSION_VALUES);
    }

    public void setImageData(File imageData) {
        this.imageData = imageData;
    }

    public File getImageData() {
        return imageData;
    }

    public void setImageDataBM(Bitmap imageData) {
        this.imageBitmap = imageData;
    }

    public Bitmap getImageDataBM() {
        return imageBitmap;
    }

    public void setImageDataPath(Uri imageData) {
        this.imagePath = imageData;
    }

    public Uri getImageDataPath() {
        return imagePath;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "You're connected!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection suspended...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed to connect...", Toast.LENGTH_SHORT).show();
    }

    public void getLocationPerformWikiAPI(boolean isBitmap) {
        this.isBitmap = isBitmap;
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            launchMediaScanIntent();
            if (!isBitmap) {
                try {
                    String filePath = getImageData().getPath();
                    Bitmap toConvert = BitmapFactory.decodeFile(filePath);
                    if (detector.isOperational() && toConvert != null) {
                        Frame frame = new Frame.Builder().setBitmap(toConvert).build();
                        SparseArray<TextBlock> textBlocks = detector.detect(frame);
                        String blocks = "";
                        String lines = "";
                        String words = "";
                        for (int index = 0; index < textBlocks.size(); index++) {
                            //extract scanned text blocks here
                            TextBlock tBlock = textBlocks.valueAt(index);
                            blocks = blocks + tBlock.getValue() + "\n" + "\n";
                            for (Text line : tBlock.getComponents()) {
                                //extract scanned text lines here
                                lines = lines + line.getValue() + "\n";
                                for (Text element : line.getComponents()) {
                                    //extract scanned text words here
                                    words = words + element.getValue() + ", ";
                                }
                            }
                        }
                        if (textBlocks.size() == 0) {
                            Log.d("no text","no text mang");
                        } else {

                           Log.d("words",words);
                        }
                    } else {
                        Log.d("error","Could not set up the detector!");
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                            .show();
                    Log.e("error", e.toString());
                }
            }  else {
                try {
                    Bitmap toConvert = getImageDataBM();
                    if (detector.isOperational() && toConvert != null) {
                        Frame frame = new Frame.Builder().setBitmap(toConvert).build();
                        SparseArray<TextBlock> textBlocks = detector.detect(frame);
                        String blocks = "";
                        String lines = "";
                        String words = "";
                        for (int index = 0; index < textBlocks.size(); index++) {
                            //extract scanned text blocks here
                            TextBlock tBlock = textBlocks.valueAt(index);
                            blocks = blocks + tBlock.getValue() + "\n" + "\n";
                            for (Text line : tBlock.getComponents()) {
                                //extract scanned text lines here
                                lines = lines + line.getValue() + "\n";
                                for (Text element : line.getComponents()) {
                                    //extract scanned text words here
                                    words = words + element.getValue() + ", ";
                                }
                            }
                        }
                        if (textBlocks.size() == 0) {
                            Log.d("no text","no text mang");
                        } else {
                            returnedText = words;
                            Log.d("words",words);
                        }
                    } else {
                        Log.d("error","Could not set up the detector!");
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                            .show();
                    Log.e("error", e.toString());
                }
            }
            if (returnedText != "") {

            }
        }
    }

    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        if (!isBitmap) {
            mediaScanIntent.setData(getImageDataPath());
        }
        this.sendBroadcast(mediaScanIntent);
    }


//    @Override
//    public void processFinish(ArrayList<HashMap<String, String>> output) {
//        wikiDone = true;
//        if (returnedText != "" && !returnedWikiData.isEmpty()) {
//            HashMap<String, String> decision = new HashMap<String, String>();
//            decision = MatchingToLocation.sendForMatching(returnedWikiData, allClarifaiValuesOutput);
//            Log.i("decision", decision.toString());
//            Intent i = new Intent(this, DisplayActivity.class);
//            i.putExtra("title", decision.get("title").toString());
//            i.putExtra("pageid", decision.get("pageid").toString());
//            i.putExtra("extract", decision.get("extract").toString());
//            i.putExtra("lat", decision.get("lat").toString());
//            i.putExtra("long", decision.get("long").toString());
//            i.putExtra("distance", Double.parseDouble(decision.get("distance").toString()));
//            if (!isBitmap) {
//                Log.i("iamgelocation", getImageData().getAbsolutePath());
//                i.putExtra("image", getImageData().getAbsolutePath());
//            } else {
//                i.putExtra("image", getImageDataPath());
//            }
//            this.startActivity(i);
//        }
//    }
}