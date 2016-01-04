package com.example.sanjeevkumar.mymap;

import java.io.IOException;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,GoogleMap.OnMapClickListener {
    GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mCurrentLocation;
    protected String mLastUpdateTime;
    protected String mDestLatitudetext;
    protected String mDestLongitudetext;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    protected TextView address_text_view;
    protected TextView mLastUpdateTimeTextView;
    protected EditText editTextLat;
    protected EditText editTextLng;
    protected float distanceToSeparation;
    protected Geocoder geocoder;
    protected List<Address> addresses;
    protected LocationRequest mLocationRequest;
    protected boolean mRequestingLocationUpdates = true;
    protected double delta = 100;
    protected boolean isDestinationSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //assign UI elements
        mMap = mapFragment.getMap();
        mLatitudeText = (TextView) findViewById(R.id.latitude_text);
        mLongitudeText = (TextView) findViewById(R.id.longitude_text);
        mLastUpdateTimeTextView = (TextView) findViewById(R.id.last_update_time_text_view);
        address_text_view = (TextView) findViewById(R.id.address_text_view);
        editTextLat = (EditText) findViewById(R.id.dest_latitude_text);
        editTextLng = (EditText) findViewById(R.id.dest_longitude_text);

        buildGoogleApiClient();
    }

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
        // Add a marker in bang and move the camera.
        LatLng bang = new LatLng(12.9599794, 77.6444823);
        map.addMarker(new MarkerOptions().position(bang).title("Marker in coconut"));
        map.moveCamera(CameraUpdateFactory.newLatLng(bang));
        map.setMyLocationEnabled(true);
        UiSettings uiSettings =  map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        map.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng point) {
        //address_text_view.setText(point.toString());
        mMap.addMarker(new MarkerOptions().position(point).title("Your destination"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        editTextLat.setText(String.valueOf(point.latitude));
        editTextLng.setText(String.valueOf(point.longitude));
        startTracking();
    }

    //auto getLocation
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
            updateUI();
        }
    }

    private void updateUI() {
        mLatitudeText.setText(String.valueOf(mCurrentLocation.getLatitude()));
        mLongitudeText.setText(String.valueOf(mCurrentLocation.getLongitude()));
        mLastUpdateTimeTextView.setText(mLastUpdateTime);
        if(isDestinationSet) {
            checkForDestination();
        }
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
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }

       /* mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        } */
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

    public void go(View view){
        startTracking();
    }

    public void startTracking() {
        try {

            mDestLatitudetext = editTextLat.getText().toString();
            mDestLongitudetext = editTextLng.getText().toString();
            isDestinationSet = true;
        }
        catch (Exception ex) {
            Toast.makeText(this, "Enter cordinates", Toast.LENGTH_LONG).show();
        }
    }

    public void showAddress(View view) {
        double x = mCurrentLocation.getLatitude();
        double y = mCurrentLocation.getLongitude();
        geocoder = new Geocoder(MapsActivity.this, Locale.ENGLISH);
        StringBuilder str = new StringBuilder();
        if(geocoder.isPresent()) {
            try {
                addresses = geocoder.getFromLocation(x, y, 1);
                Address returnAddress = addresses.get(0);


                for(int i= 0; i<returnAddress.getMaxAddressLineIndex(); i++) {
                    str.append(returnAddress.getAddressLine(i) + "\n");
                }
                address_text_view.setText(str);
                Toast.makeText(getApplicationContext(), str,Toast.LENGTH_SHORT).show();

            } catch (IOException ioe) {
                Log.i("Error :(", "Unable to get address");
            }
        }
        else {
            Toast.makeText(this, "Unable to start geocoder.", Toast.LENGTH_LONG).show();
        }
    }

    protected void checkForDestination() {

        float[] results = new float[1];
        Location.distanceBetween(Double.parseDouble(mDestLatitudetext), Double.parseDouble(mDestLongitudetext),
                mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), results);
        //double dLat = Math.abs(Double.parseDouble(mDestLatitudetext) - mCurrentLocation.getLatitude());
        //double dLng = Math.abs(Double.parseDouble(mDestLongitudetext) - mCurrentLocation.getLongitude());
        //Log.i(" delta changes", " " + dLat);
        //Log.i(" delta changes", " " + dLng);
        distanceToSeparation = results[0];
        Log.i("distance", String.valueOf(distanceToSeparation));
        address_text_view.setText(String.valueOf(distanceToSeparation));
        if(distanceToSeparation < delta) {
            playAlert();
        }
    }

    public void playAlert() {
        Log.i("in paly", "hey");
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.pearl);
        mediaPlayer.start();
    }
}