package com.example.aminda.smart_tracker.Fragments;


import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aminda.smart_tracker.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
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



    public GMapFragment() {
        // Required empty public constructor
    }

    public static void setActivity(MainActivity Activity) {
        activity = Activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Log.d("rht","aaaaaaaaaaaaaaaaaaaa.....map fragment created....aaaaaaaaaaaaaaaaaaaaaa***");
        return  view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.initMap().getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        LatLng sri_lanka = new LatLng(7.241829, 80.7556483);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sri_lanka));
    }

    public static void updateLocation(Location location, String type){
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        float zoomLevel = 10.0f;
        if(type == "user") {
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
        }
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,zoomLevel));
    }

}
