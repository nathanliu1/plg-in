package com.example.android.camera2basic;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


/**
 * Shows the quote detail page.
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class DisplayFragment extends Fragment {

    String monthTime;
    String dayTime;
    String pathUrl;
    String name;
    String description;
    String eventId;
    String location;
    String fullTime;
    String hostId;
    String hostAbout;
    String eventHostName;
    String relevantIds;
    String lat;
    String longitude;
    String[] returnedVals;
    int i = 0;
    MapView mMapView;
    GoogleMap googleMap;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;
    String savedFavorite;
    Boolean isSaved = false;


    public static DisplayFragment newInstance() {
        return new DisplayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_display, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        collapsingToolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        pathUrl = getArguments().getString("coverPhoto");
        monthTime = getArguments().getString("monthTime");
        dayTime = getArguments().getString("dayTime");
        name = getArguments().getString("name");
        lat = "43.469463" ;
        longitude = "-80.543800";
        eventHostName = getArguments().getString("eventHostName");
        description = getArguments().getString("description");
        eventId = getArguments().getString("eventId");
        location = getArguments().getString("locationName");
        fullTime =getArguments().getString("fullTime");
        hostAbout = getArguments().getString("aboutHost");
        relevantIds = getArguments().getString("coverIds");
        hostId = getArguments().getString("eventHostId");
        returnedVals = getArguments().getString("coverIds").split("\\s+");
        Log.d("thisidddd",relevantIds);
        Log.d("thisidddd",returnedVals[0].toString());
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng locationMapped = new LatLng(Double.parseDouble(lat), Double.parseDouble(longitude));
                googleMap.addMarker(new MarkerOptions().position(locationMapped).title("Science").snippet("You are 20 meters away from here"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(locationMapped).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });


        return rootView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
//        TextView titleView = (TextView)view.findViewById(R.id.author);
//        titleView.setText(title);
//        mFloatingAction = (FloatingActionButton) view.findViewById(R.id.appbarbutton);
//        TextView extractView = (TextView)view.findViewById(R.id.quote);
//        extractView.setText(extract);
        TextView monthView = (TextView) view.findViewById(R.id.eventMonth);
        monthView.setText(monthTime.toUpperCase());
        TextView dayView = (TextView) view.findViewById(R.id.eventDay);
        dayView.setText(dayTime);
        TextView nameView = (TextView) view.findViewById(R.id.eventTitle);
        nameView.setText(name);
        TextView hostName = (TextView) view.findViewById(R.id.eventHost);
        hostName.setText(Html.fromHtml("<font color=#ffffff>Public - Hosted by </font><font color=#ffcc00>"+eventHostName+"</font>"));
        TextView descriptionView = (TextView) view.findViewById(R.id.description);
        descriptionView.setText(description);
        TextView fbView = (TextView) view.findViewById(R.id.fbtime);
        fbView.setText(fullTime);
        TextView locationView = (TextView) view.findViewById(R.id.fblocation);
        locationView.setText(location);
        TextView linkView = (TextView) view.findViewById(R.id.fblink);
        linkView.setText("events/"+eventId);
        TextView hostView = (TextView) view.findViewById(R.id.pageName);
        hostView.setText(eventHostName);
        TextView hostAboutView = (TextView) view.findViewById(R.id.pageInfo);
        hostAboutView.setText(hostAbout);
        final ImageView[] relevant = new ImageView[3];
        relevant[0] = (ImageView) view.findViewById(R.id.relevant1);
        relevant[1] = (ImageView) view.findViewById(R.id.relevant2);
        relevant[2] = (ImageView) view.findViewById(R.id.relevant3);
        if (returnedVals[0].length()>0) {
            Log.d("dad",returnedVals[0]);
            Glide.with(this).load(returnedVals[0]).fitCenter().into(relevant[0]);
        }
        if (returnedVals.length>1) {
            if (returnedVals[1].length() > 0) {
                Glide.with(this).load(returnedVals[1]).fitCenter().into(relevant[1]);
            }
        }
        if (returnedVals.length>2) {
            if (returnedVals[2].length() > 0) {
                Glide.with(this).load(returnedVals[2]).fitCenter().into(relevant[2]);
            }
        }

        View fbInfo = view.findViewById(R.id.fbEventInfo);
        fbInfo.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {
                                          final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("fb://event/"+eventId));
                                          startActivity(intent);
                                      }
                                  }
        );
        View fbPage = view.findViewById(R.id.fbPageInfo);
        fbPage.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {
                                          final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("fb://page/"+hostId));
                                          startActivity(intent);
                                      }
                                  }
        );


//        descriptionView.setText("test text mang");
        ImageView backdropImg = (ImageView) view.findViewById(R.id.backdrop);
        Log.d("path",pathUrl);
        Glide.with(this).load(pathUrl).fitCenter().into(backdropImg);

//        view.findViewById(R.id.appbarbutton).setOnClickListener(this);
    }


}
