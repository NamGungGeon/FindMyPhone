package com.example.windows10.findmyphone;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Windows10 on 2017-08-18.
 */

public class GpsTracerInBackground extends Service implements Runnable {
    final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    private LocationManager locationManager = null;
    private Thread thread=null;
    private boolean isActive=true;
    @Override
    public void onCreate() {
        super.onCreate();
        thread = new Thread(this);
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        thread.start();
        //Toast.makeText(getApplicationContext(), "추적 시작", Toast.LENGTH_SHORT).show();
    }


    private boolean getGpsStatus() {
        String provider = android.provider.Settings.Secure
                .getString(getApplicationContext().getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        return provider.contains("gps");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {
        Looper.prepare();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && getGpsStatus()) {
            while(isActive){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, mLocationListener);
                writeGpsToFirebase(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                Toast.makeText(getApplicationContext(), "Tracing..", Toast.LENGTH_SHORT).show();
            }
        } else {
            isActive=false;
            thread.stop();
        }
    }

    private boolean writeGpsToFirebase(Location location){
        if(location!=null){
            HashMap<String, Object> val = new HashMap<>();
            Date currentTime = Calendar.getInstance().getTime();
            String _location = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
            val.put("/users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/gps/"+currentTime.toString(), _location);
            FirebaseDatabase.getInstance().getReference().updateChildren(val);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onDestroy() {
        isActive=false;
        thread.stop();
        super.onDestroy();
    }
}
