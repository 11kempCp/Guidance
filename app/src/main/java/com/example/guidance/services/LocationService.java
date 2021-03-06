package com.example.guidance.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.guidance.R;
import com.example.guidance.activity.MainActivity;
import com.example.guidance.app.App;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.guidance.realm.DatabaseFunctions.locationEntry;
import static com.example.guidance.scheduler.Util.LOCATION;

/**
 * Created by Conor K on 03/03/2021.
 */
public class LocationService extends Service {


    public static final String TAG = "LocationService";
    static int sID;
    private static FusedLocationProviderClient mFusedLocationClient;
    private static LocationCallback mLocationCallback;
    private static Handler mServiceHandler;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static LocationRequest mLocationRequest;
    private static Location mLocation;
    private static Date currentTime;
    private static boolean receivingUpdates;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.d(TAG, "onLocationResult: null");
                    removeLocationUpdates();
                    return;
                }
                removeLocationUpdates();
                onNewLocation(LocationService.this ,locationResult);
            }
        };

//        createLocationRequest();
//        getLastLocation(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        Log.d(TAG, "onStartCommand: ");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle(getString(R.string.location))
                .setContentText(getString(R.string.notification_location))
                .setSmallIcon(R.drawable.ic_location)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(LOCATION, notification);

        sID = startId;

        if (mLocationRequest == null) {
            createLocationRequest();
        }

        getLastLocation(this);

        startLocationUpdates();

        return START_NOT_STICKY;
    }

    private void startLocationUpdates() {
//        Log.d(TAG, "startLocationUpdates: ");
        receivingUpdates = true;
        try {

            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    Looper.myLooper());

        } catch (SecurityException unlikely) {
            receivingUpdates = false;
            Log.d(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
//        removeLocationUpdates();
    }

    public static void removeLocationUpdates() {
        Log.d(TAG, "removeLocationUpdates");
        receivingUpdates = false;
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private static void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    private void getLastLocation(Context context) {


        try {

            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLocation = task.getResult();
                            currentTime = Calendar.getInstance().getTime();
                            locationEntry(context, currentTime, mLocation.getLatitude(), mLocation.getLongitude());
//                            removeLocationUpdates(context);

                            if(!receivingUpdates){
                                stopForeground(true);
                                stopSelfResult(sID);
                            }

//                            Log.d(TAG, "getLastLocation: " + mLocation.getLatitude() + " " + mLocation.getLongitude() + " " + currentTime);
                        } else {
                            Log.w(TAG, "Failed to get location.");
                            stopForeground(true);
                            stopSelfResult(sID);
                        }
                    });

        } catch (SecurityException unlikely) {
            Log.d(TAG, "Lost location permission." + unlikely);
        }
    }

    private void onNewLocation(Context context, LocationResult location) {
        mLocation = location.getLastLocation();
        currentTime = Calendar.getInstance().getTime();
//        Log.d(TAG, "New location: " + mLocation.getLatitude() + " " + mLocation.getLongitude() + " " + currentTime);
        locationEntry(context, currentTime, mLocation.getLatitude(), mLocation.getLongitude());

        stopForeground(true);
        stopSelfResult(sID);
    }


    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelfResult(sID);
        removeLocationUpdates();
    }
}