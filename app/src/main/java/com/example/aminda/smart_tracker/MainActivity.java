package com.example.aminda.smart_tracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.aminda.smart_tracker.FetchData.BusRoutes;
import com.example.aminda.smart_tracker.Fragments.BusRouteFragment;
import com.example.aminda.smart_tracker.Fragments.GMapFragment;
import com.example.aminda.smart_tracker.Fragments.Login;
import com.example.aminda.smart_tracker.Fragments.SchoolFragment;
import com.example.aminda.smart_tracker.Fragments.StaffFragment;
import com.example.aminda.smart_tracker.Fragments.TrainLineFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    private FragmentManager fm;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private LocationManager locationManager;
    private LocationManager mLocationManager;
    private MapFragment mapFragment;
    private MqttClientMy client;
    private MqttClientMy driver;
    private Fragment fragment = null;
    private BusRouteFragment busRouteFragment = null;
    private boolean inHome = false;
    private boolean active = false;
    private boolean started = false;
    private String username;
    private String client_name = "yyyyy";
    private JSONArray busRoutesArray;
    private JSONArray busArray;
    private JSONArray coordinateArray;
    private String selectedBusRoute;
    private String selectedDriver;
    private int selectedDriverIndex;
    private String busTimeHigh;
    private String busTimeLow;
    private NavigationView navigationView;
    private String driverInfo;
    private JSONArray coordinateArray;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fm = getSupportFragmentManager();
        showMapFragment();
        setInHome(true);

        GMapFragment.setActivity(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation();

        startClient();



        SharedPreferences sharedpreferences = getSharedPreferences(String.valueOf(R.string.user_details), Context.MODE_PRIVATE);
        if(!sharedpreferences.getString(String.valueOf(R.string.USER_USERNAME),String.valueOf(R.string.notLogged)).equals(String.valueOf(R.string.notLogged))){
            username  = sharedpreferences.getString(String.valueOf(R.string.USER_USERNAME),String.valueOf(R.string.notLogged));
            setActive(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setTitle("Log Out");
            Log.d(TAG, "user logeed in");
            startDriverClient();
        }else{
            Log.d(TAG, "user not logeed in");
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        hideMapFragment();
//        setInHome(false);
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            showMapFragment();
            setInHome(true);
        }else if (id == R.id.nav_bus) {
            setInHome(false);
            BusRoutes br = new BusRoutes(this);
            br.execute("a");

        } else if (id == R.id.nav_train) {
            setInHome(false);
            fragment = new TrainLineFragment();
            showFragment(fragment);
        } else if (id == R.id.nav_staff) {
            fragment = new StaffFragment();
            showFragment(fragment);
        } else if (id == R.id.nav_school) {
            fragment = new SchoolFragment();
            showFragment(fragment);
        } else if (id == R.id.nav_start) {
            if(isActive()){
                if(isStarted()){
                    Toast.makeText(this, "Journey Stopped!", Toast.LENGTH_SHORT).show();
                    setStarted(false);
                    item.setTitle("Start");
                    item.setIcon(R.drawable.ic_play);
                }else{
                    Toast.makeText(this, "Journey Started!", Toast.LENGTH_SHORT).show();
                    setStarted(true);
                    item.setTitle("Stop");
                    item.setIcon(R.drawable.ic_pause);
                }
            }else{
                Toast.makeText(this, "Login required to start journey!", Toast.LENGTH_SHORT).show();
            }


        } else if (id == R.id.nav_logout) {

            if(!active){
                Login login = new Login();
                login.setMain(this);
                showFragment(login);
            }else{
                LogOut();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void LogOut() {
        setActive(false);
        setStarted(false);
        navigationView.getMenu().findItem(R.id.nav_logout).setTitle("Log In");
        setUsername("");
    }



    public void startClient(){
        MqttCallback callback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                Log.w("Mqtt", "connectionLost111111111111111111111111111111111111");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                String msg = mqttMessage.toString();
                String[] msgSplitted = msg.split(",");
                Log.d("Mqtt------------------", msgSplitted[0]+"   "+msgSplitted[1]);
                Log.d(TAG, "is in home"+isInHome());
                if(!isInHome()){
                    LatLng latLng = new LatLng(Double.parseDouble(msgSplitted[0]), Double.parseDouble(msgSplitted[1]));
                    GMapFragment.updateDriverLocation(latLng);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                Log.w("Mqtt", "deliveryComplete000000000000000000000000000000000000000");
            }
        };
        Log.w("Mqtt", "deliveryComplete000000000000000000000000000000000000000");
        client = new MqttClientMy(this.getApplicationContext(), "device_" + getClient_name() ,callback);

    }

    public void startDriverClient(){
        MqttCallback callback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                Log.w("Mqtt", "connectionLost111111111111111111111111111111111111");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                String msg = mqttMessage.toString();
                String[] msgSplitted = msg.split(",");
                Log.d("driver--------------", msgSplitted[0]+"   "+msgSplitted[1]);
                if(!isInHome()){
                    LatLng latLng = new LatLng(Double.parseDouble(msgSplitted[0]), Double.parseDouble(msgSplitted[1]));
                    GMapFragment.updateDriverLocation(latLng);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                Log.w("Mqtt", "deliveryComplete000000000000000000000000000000000000000");
            }
        };

        driver = new MqttClientMy(this.getApplicationContext(), getUsername() +"_device",callback);
    }

    public void subscribeToDriver(){
        client.subscribeToTopic(getSelectedDriver()+"/PUB");
        JSONObject driver = null;
        try {
            driver = (JSONObject) busArray.get(getSelectedDriverIndex());
            setDriverInfo("Plate No: "+(String)driver.get("busNo")+"\n"+
                    "Owner: "+(String)driver.get("ownerType")+"\n"+
                    "Bus Type: "+(String)driver.get("busType")+"\n"+
                    "Telephone No: "+(String)driver.get("telNo"));
            Log.d("666666666",(String)driver.get("busNo")+(String)driver.get("telNo")+(String)driver.get("ownerType")+(String)driver.get("busType"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showFragment(Fragment fragment){
        if(fragment != null){
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.screen_area, fragment);
            ft.commit();
        }
    }

    public void showMapFragment(){

        FragmentTransaction ft = fm.beginTransaction();
        if(fm.findFragmentByTag("mapFragment") == null){
            ft.add(R.id.screen_area, new GMapFragment(), "homeFragment");
        }else{
            ft.show(fm.findFragmentByTag("mapFragment"));
            Log.d("rht","aaaaaaaaaaaaaaaaaaaa.....resume map ....aaaaaaaaaaaaaaaaaaaaaa***");
        }
        ft.commit();
    }

    public void hideMapFragment(){
        FragmentTransaction ft = fm.beginTransaction();
        if(fm.findFragmentByTag("mapFragment") == null){

        }else{
            ft.hide(fm.findFragmentByTag("mapFragment"));
            Log.d("rht","aaaaaaaaaaaaaaaaaaaa.....hide map ....aaaaaaaaaaaaaaaaaaaaaa***");
        }
        ft.commit();
    }

    public MapFragment initMap() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        return mapFragment;
    }

    public void onLocationChanged(Location location) {
//        Toast.makeText(this, "msg", Toast.LENGTH_SHORT).show();
        if(isInHome()){
            GMapFragment.updateLocation(location);
        }else{
            GMapFragment.updateUserLocation(location);
        }
        if(driver != null){
            if(driver.isConnected() && isActive() && isStarted()){
                Log.d(TAG,"topic is "+ getUsername() +"/PUB");
                driver.publishToTopic(getUsername() +"/PUB",location.getLatitude()+","+location.getLongitude());
            }
        }
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }



    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Please Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {
            if(isInHome()){
                GMapFragment.updateLocation(mLocation);
            }else{
                GMapFragment.updateUserLocation(mLocation);
            }
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("iemi exception", "--->>>> imei no ermission graned ------------------------");
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }


    public boolean isInHome() {
        return inHome;
    }

    public void setInHome(boolean inHome) {
        this.inHome = inHome;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public JSONArray getBusRoutesArray() {
        return busRoutesArray;
    }

    public void setBusRoutesArray(JSONArray busRoutesArray) {
        this.busRoutesArray = busRoutesArray;
    }

    public JSONArray getBusArray() {
        return busArray;
    }

    public void setBusArray(JSONArray busArray) {
        this.busArray = busArray;
    }

    public String getSelectedBusRoute() {
        return selectedBusRoute;
    }

    public void setSelectedBusRoute(String selectedBusRoute) {
        this.selectedBusRoute = selectedBusRoute;
    }

    public String getSelectedDriver() {
        return selectedDriver;
    }

    public void setSelectedDriver(String selectedDriver) {
        this.selectedDriver = selectedDriver;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSelectedDriverIndex() {
        return selectedDriverIndex;
    }

    public void setSelectedDriverIndex(int selectedDriverIndex) {
        this.selectedDriverIndex = selectedDriverIndex;
    }

    public void saveUser(String username) {
        SharedPreferences sharedpreferences = getSharedPreferences(String.valueOf(R.string.user_details), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(String.valueOf(R.string.USER_USERNAME), username);
        editor.commit();
        navigationView.getMenu().findItem(R.id.nav_logout).setTitle("Log Out");
    }

    public String getClient_name() {
        Random r = new Random();
        int i = r.nextInt(10000000);
        return String.valueOf(i);
    }

    public String getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(String driverInfo) {
        this.driverInfo = driverInfo;
    }

    public JSONArray getCoordinateArray() {
        return coordinateArray;
    }

    public void setCoordinateArray(JSONArray coordinateArray) {
        this.coordinateArray = coordinateArray;
    }
}
