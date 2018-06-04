package com.example.aminda.smart_tracker.Fragments;


import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.aminda.smart_tracker.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.aminda.smart_tracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GMapFragment extends Fragment implements OnMapReadyCallback {


    private static final String TAG = "GMapFragment";
    private static MainActivity activity;
    private static GoogleMap gmap;
    private static Marker marker;
    private static Marker driver_marker;
    private static String driverInfo;
    private static Button findUserBtn;
    private static Button findDriverBtn;



    public GMapFragment() {
        // Required empty public constructor
    }

    public static void setActivity(MainActivity Activity) {
        activity = Activity;
    }

    public static String getDriverInfo() {
        return driverInfo;
    }

    public static void setDriverInfo(String driverInfo) {
        GMapFragment.driverInfo = driverInfo;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Log.d("rht","aaaaaaaaaaaaaaaaaaaa.....map fragment created....aaaaaaaaaaaaaaaaaaaaaa***");
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        findUserBtn  = (Button) view.findViewById(R.id.find_userBtn);
        findDriverBtn  = (Button) view.findViewById(R.id.find_busBtn);
        findUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusUser();
            }
        });
        findDriverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusDriver();
            }
        });
        hideIcons();
        return  view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        activity.initMap().getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        Log.d("ssssssssssssssssssssss","ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        marker = null;
        driver_marker = null;
        setDriverInfo(null);
        LatLng sri_lanka = new LatLng(7.241829, 80.7556483);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sri_lanka));
    }

    public static void updateLocation(Location location){
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        float zoomLevel = 10.0f;

        if (marker == null) {
            marker = gmap.addMarker(new MarkerOptions()
                    .position(loc)
                    .title("You are Here!")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name)));
            marker.showInfoWindow();
        } else {
            marker.setPosition(loc);
        }
        zoomLevel = 15.0f;//This goes up to 21

        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,zoomLevel));
    }

    public static void updateUserLocation(Location location){
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        float zoomLevel = 10.0f;
        if (marker == null) {
            marker = gmap.addMarker(new MarkerOptions()
                    .position(loc)
                    .title("You are Here!")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name)));
//            marker.showInfoWindow();
        } else {
            marker.setPosition(loc);
        }
    }

    public static void updateDriverLocation(LatLng loc){
        if (driver_marker == null) {
            driver_marker = gmap.addMarker(new MarkerOptions()
                    .position(loc)
                    .title(getDriverInfo())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_bus_blue_round)));
//            marker.showInfoWindow();
        } else {
            driver_marker.setPosition(loc);
        }
    }

    public static void updateDriverInfo(String info){
        setDriverInfo(info);
    }

    public static void focusUser(){
        LatLng loc = marker.getPosition();
        float zoomLevel = 15.0f;//This goes up to 21
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,zoomLevel));
    }

    public static void focusDriver(){
        LatLng loc = driver_marker.getPosition();
        float zoomLevel = 15.0f;//This goes up to 21
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,zoomLevel));
    }


    public static void showIcons(){
        findUserBtn.setVisibility(View.VISIBLE);
        findDriverBtn.setVisibility(View.VISIBLE);
    }

    public static void hideIcons(){
        findUserBtn.setVisibility(View.INVISIBLE);
        findDriverBtn.setVisibility(View.INVISIBLE);
    }
}
