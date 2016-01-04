package com.example.sanjeevkumar.play;

import java.io.IOException;
import android.app.Activity;
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
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    protected GoogleApiClient mGoogleApiClient;
    protected Location mCurrentLocation;
    protected String mLastUpdateTime;
    protected String mDestLatitudetext;
    protected String mDestLongitudetext;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    protected TextView mLastUpdateTimeTextView;
    protected Geocoder geocoder;
    protected List<Address> addresses;
    Location loc;

    protected LocationRequest mLocationRequest;
    protected boolean mRequestingLocationUpdates = true;
    protected double delta = 0.001000;
    protected boolean isDestinationSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLatitudeText = (TextView) findViewById(R.id.latitude_text);
        mLongitudeText = (TextView) findViewById(R.id.longitude_text);
        mLastUpdateTimeTextView = (TextView) findViewById(R.id.last_update_time_text_view);
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

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }


    protected void startLocationUpdates() {
        Toast.makeText(this,"in startLoc", Toast.LENGTH_SHORT).show();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void createLocationRequest() {
        Toast.makeText(this,"in start of createLocationRequest", Toast.LENGTH_SHORT).show();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Toast.makeText(this,"in end of createLocationRequest", Toast.LENGTH_SHORT).show();
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

    public void go(View view){
        try {
            EditText editText = (EditText) findViewById(R.id.dest_latitude_text);
            mDestLatitudetext = editText.getText().toString();
            editText = (EditText) findViewById(R.id.dest_longitude_text);
            mDestLongitudetext = editText.getText().toString();
            isDestinationSet = true;
        }
        catch (Exception ex) {
            Toast.makeText(this, "Enter cordinates", Toast.LENGTH_LONG).show();
        }
    }

    protected void checkForDestination() {
        double dLat = Math.abs(Double.parseDouble(mDestLatitudetext) - mCurrentLocation.getLatitude());
        double dLng = Math.abs(Double.parseDouble(mDestLatitudetext) - mCurrentLocation.getLongitude());
        Log.i(" delta changes", " " + dLat);
        Log.i(" delta changes", " " +  dLng);
        if(dLat < delta || dLng < delta) {
            playAlert();
        }
    }

    public void playAlert() {
        Log.i("in paly", "hey");
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.pearl);
        mediaPlayer.start();
    }

    public void showAddress(View view) {
        double x = mCurrentLocation.getLatitude();
        double y = mCurrentLocation.getLongitude();
        TextView address_text_view = (TextView) findViewById(R.id.address_text_view);
        geocoder = new Geocoder(MainActivity.this, Locale.ENGLISH);
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
}
