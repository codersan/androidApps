package com.example.sanjeevkumar.mymap;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.List;

import java.io.IOException;
import android.media.MediaPlayer;
import android.location.Address;
import android.location.Geocoder;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;
import java.util.Map;

/**
 * Created by sanjeevkumar on 10/23/15.
 */

public class AppMethods {
    protected Context appContext;
    protected MediaPlayer mediaPlayer;
    protected Geocoder geocoder;
    protected List<Address> addresses;
    protected String track_btn_text;
    protected float distanceToDestination = 0 ;
    protected double delta = 100; // range in meters to start notification
    protected boolean reachedDestination = false;
    protected static double mDestinationLat = 0.0;
    protected static double mDestinationLng = 0.0;
    protected boolean isDestinationSet = false;
    private static final String off = "Start"; // not started
    private static final String on = "Reset";
    MapsActivity mapsActivityInstance = new MapsActivity();

    public AppMethods(Context c) {
        appContext=c;
    }
    //set Lat , Lng on onMapClick
    protected void setGeoPoints(LatLng point) {
        mDestinationLat = point.latitude;
        mDestinationLng = point.longitude;
    }

    protected void updateDistance() {
        checkForDestination();
    }

    public void track(View view){
        track_btn_text = MapsActivity.track_btn.getText().toString();

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
            mapsActivityInstance.startLocationUpdates();
            toggleVariables();
        }
        else {
            Toast.makeText(appContext, "Mark your destinaton on map", Toast.LENGTH_SHORT).show();
        }
    }

    protected void stopLocator() {
        mapsActivityInstance.stopLocationUpdates();
        toggleVariables();
        if(reachedDestination) {
            Toast.makeText(appContext, "You reached to destination.", Toast.LENGTH_LONG).show();
            stopAlert();
        }
        else {
            Toast.makeText(appContext, "Stopped before destination.", Toast.LENGTH_LONG).show();
        }
    }

    private void toggleVariables() {
        isDestinationSet = !isDestinationSet;
        //start
        if(isDestinationSet) {
            MapsActivity.mMap.setOnMapClickListener(null); //disable map marker
            MapsActivity.track_btn.setText(on);
        }
        else {
            MapsActivity.mMap.setOnMapClickListener(mapsActivityInstance); //enable map marker
            MapsActivity.track_btn.setText(off);
            distanceToDestination = 0;
            mDestinationLat = 0.0;
            mDestinationLng = 0.0;
            MapsActivity.distance_text.setText("");
            MapsActivity.marker.remove();
        }
    }

    protected  void checkForDestination() {
        distanceToDestination = getDistance();
        MapsActivity.distance_text.setText(String.valueOf(distanceToDestination) + " m");
        if(distanceToDestination < delta) {
            reachedDestination = true;
            playAlert();
        }
    }

    private float getDistance() {

        if(MapsActivity.mCurrentLocation != null) {
            // on first loc update, show address to destination
            if(distanceToDestination == 0) {
                showAddress();
            }
            float[] results = new float[1];
            try {
                Location.distanceBetween(mDestinationLat, mDestinationLng,
                        MapsActivity.mCurrentLocation.getLatitude(), MapsActivity.mCurrentLocation.getLongitude(), results);
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
        geocoder = new Geocoder(appContext, Locale.ENGLISH);
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
                    Toast.makeText(appContext, str, Toast.LENGTH_SHORT).show();
                }

            } catch (IOException ioe) {
                Log.i("Error :(", "Unable to get address");
            }
        }
        else {
            Toast.makeText(appContext, "Waiting for destination address...", Toast.LENGTH_LONG).show();
        }
    }
    private void playAlert() {
        mediaPlayer = MediaPlayer.create(appContext, R.raw.pearl);
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
