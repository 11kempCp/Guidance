package com.example.guidance.services;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.guidance.R;
import com.example.guidance.activity.MainActivity;
import com.example.guidance.app.App;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

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
    private static Date mTime;
    private static boolean locationUpdates;


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.d(TAG, "onLocationResult: null");
                    return;
                }
                removeLocationUpdates();
                //Likely will complain
                onNewLocation(LocationService.this, locationResult);

//                stopForeground(true);
//                stopSelfResult(sID);
            }
        };

//        createLocationRequest();
//        getLastLocation(this);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: ");

        if(LocationAvailability.hasLocationAvailability(intent)){
            Log.d(TAG, "true");
        }else {
            Log.d(TAG, "false");
        }


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
        Log.d(TAG, "startLocationUpdates: ");
        locationUpdates = true;
        try {

            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    Looper.myLooper());

            if(mFusedLocationClient.getLocationAvailability() == null){
                Log.d(TAG, "startLocationUpdates: mFusedLocationClient.getLocationAvailability() == null");
            }

        } catch (SecurityException unlikely) {
            Log.d(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }

    public static void removeLocationUpdates() {
        Log.d(TAG, "removeLocationUpdates");
        locationUpdates = false;
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
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }


    private void getLastLocation(Context context) {


        try {

            mFusedLocationClient.getLastLocation().addOnSuccessListener(task -> {
                if (task != null) {
                    mLocation = task;
                    mTime = Calendar.getInstance().getTime();

                    locationEntry(context, mTime, mLocation.getLatitude(), mLocation.getLongitude());
//                            removeLocationUpdates(context);
                    Log.d(TAG, "getLastLocation: " + mLocation.getLatitude() + " " + mLocation.getLongitude() + " " + mTime);

                    if (!locationUpdates) {
//                                stopForeground(true);
//                                stopSelfResult(sID);
                    }

                } else {
                    Log.w(TAG, "Failed to getLastLocation.");
                }
            });
        } catch (SecurityException unlikely) {
            Log.d(TAG, "Lost location permission." + unlikely);
        }
    }


    private static void onNewLocation(Context context, LocationResult location) {
        mLocation = location.getLastLocation();
        mTime = Calendar.getInstance().getTime();
        Log.d(TAG, "New location: " + mLocation.getLatitude() + " " + mLocation.getLongitude() + " " + mTime);

        locationEntry(context, mTime, mLocation.getLatitude(), mLocation.getLongitude());

    }


    @Override
    public void onDestroy() {

        removeLocationUpdates();


    }
}
