package com.example.sanjeevkumar.mymap;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import android.media.MediaPlayer;
import android.location.Address;
import android.location.Geocoder;
import java.util.List;
import android.view.View;
import java.util.Locale;

import java.text.DateFormat;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,GoogleMap.OnMapClickListener {
    GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    protected Marker marker;
    protected Location mCurrentLocation;

    protected Button track_btn;
    protected TextView distance_text;
    protected TextView testing;
    protected double mDestinationLat = 0.0;
    protected double mDestinationLng = 0.0;


    protected MediaPlayer mediaPlayer;
    protected List<Address> addresses;
    protected String track_btn_text;
    protected Geocoder geocoder;
    protected float distanceToDestination = 0 ;
    protected double delta = 100; // range in meters to start notification
    protected boolean reachedDestination = false;
    protected boolean isDestinationSet = false;
    private static final String off = "Start"; // not started
    private static final String on = "Reset";
    protected LocationRequest mLocationRequest;
    protected String mLastUpdateTime;
    protected boolean mRequestingLocationUpdates = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mMap = mapFragment.getMap();
        buildGoogleApiClient();
        track_btn = (Button) findViewById(R.id.track_btn);
        distance_text = (TextView) findViewById(R.id.distance_text);
        testing = (TextView) findViewById(R.id.testing);
    }

    // functions for map handling
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add settings to map fragment
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        UiSettings uiSettings =  map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
    }

    @Override
    public void onMapClick(LatLng point) {
        if(marker != null) {
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().position(point).title("Your destination"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        mDestinationLat = point.latitude;
        mDestinationLng = point.longitude;
    }

    //auto getLocation
    protected void createLocationRequest() {
        //Toast.makeText(this,"in createLocationRequest", Toast.LENGTH_SHORT).show();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
       // Toast.makeText(this,"in end of createLocationRequest", Toast.LENGTH_SHORT).show();
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if(location == null) {
            Toast.makeText(this, "Please switch on your gps", Toast.LENGTH_LONG).show();
        }
        else {
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            //testing.setText(location.toString() + " Time: " + mLastUpdateTime);
            updateDistance();
        }
    }

    private void updateDistance() {
        checkForDestination();
    }

    //fragment class for different states of App
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("Getting error", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i("getting error", "Connection suspended here");
        mGoogleApiClient.connect();
    }

    // onClick events
    public void track(View view){
        track_btn_text = track_btn.getText().toString();

        //2 states of button
        //not started, start event
        if(track_btn_text.equals(off)) {
            startLocator();
        }
        else if(track_btn_text.equals(on)){
            stopLocator();
        }
    }

    protected void startLocator() {
        Log.i("Started tracker ", "" + mDestinationLat + " " + mDestinationLng);
        if(mDestinationLat != 0.0 && mDestinationLng != 0.0) {
            startLocationUpdates();
            toggleVariables();
        }
        else {
            Toast.makeText(this, "Mark your destinaton on map", Toast.LENGTH_SHORT).show();
        }
    }

    protected void stopLocator() {
        stopLocationUpdates();
        toggleVariables();
        if(reachedDestination) {
            Toast.makeText(this, "You reached to destination.", Toast.LENGTH_LONG).show();
            stopAlert();
        }
        else {
            Toast.makeText(this, "Stopped before destination.", Toast.LENGTH_LONG).show();
        }
    }

    private void toggleVariables() {
        isDestinationSet = !isDestinationSet;
        //start
        if(isDestinationSet) {
            mMap.setOnMapClickListener(null); //disable map marker
            track_btn.setText(on);
        }
        else {
            mMap.setOnMapClickListener(this); //enable map marker
            track_btn.setText(off);
            distanceToDestination = 0;
            mDestinationLat = 0.0;
            mDestinationLng = 0.0;
            distance_text.setText("");
            marker.remove();
        }
    }

    protected void checkForDestination() {
       distanceToDestination = getDistance();
        distance_text.setText(String.valueOf(distanceToDestination) + " m");
        if(distanceToDestination < delta) {
            reachedDestination = true;
            playAlert();
        }
    }

    private float getDistance() {

        if(mCurrentLocation != null) {
            // on first loc update, show address to destination
            if(distanceToDestination == 0) {
                showAddress();
            }
            float[] results = new float[1];
            try {
                Location.distanceBetween(mDestinationLat, mDestinationLng,
                        mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), results);
                if(results.length>0) {
                    distanceToDestination = results[0];
                }
            }
            catch (Exception ex) {
                Log.e("Distance error", "Unable to get distance");
            }
        }
        else {
            distanceToDestination = 0;
        }
        return distanceToDestination;
    }
    public void showAddress() {
        double x = mDestinationLat;
        double y = mDestinationLng;
        geocoder = new Geocoder(MapsActivity.this, Locale.ENGLISH);
        StringBuilder str = new StringBuilder();
        if(geocoder.isPresent()) {
            try {
                Address returnAddress;
                addresses = geocoder.getFromLocation(x, y, 1);
                if(addresses.isEmpty() == false) {
                    returnAddress = addresses.get(0);

                    for (int i = 0; i < returnAddress.getMaxAddressLineIndex(); i++) {
                        str.append(returnAddress.getAddressLine(i) + "\n");
                    }
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                }

            } catch (IOException ioe) {
                Log.i("Error :(", "Unable to get address");
            }
        }
        else {
            Toast.makeText(this, "Waiting for destination address...", Toast.LENGTH_LONG).show();
        }
    }
    private void playAlert() {
        mediaPlayer = MediaPlayer.create(this, R.raw.pearl);
        mediaPlayer.start();
    }
    private void stopAlert() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
