package com.example.guidance.foregroundservices;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.guidance.R;
import com.example.guidance.activity.MainActivity;
import com.example.guidance.app.App;
import com.example.guidance.realm.DatabaseFunctions;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Conor K on 18/02/2021.
 */
public class StepsService extends Service implements SensorEventListener {

    public static final String TAG = "StepsService";

    private SensorManager mSensorManager = null;

    private int sID;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: ");
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        //TODO Make better notification
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText("Guidance")
                .setSmallIcon(R.drawable.ic_data)
                .setContentIntent(pendingIntent)
                .build();

        Log.d(TAG, "onStartCommand: startingForeground");
        startForeground(1, notification);
        sID = startId;
        return START_NOT_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float sensorValue = event.values[0];
        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, "onSensorChanged: " + sensorValue + " currentTime " + currentTime);
        //TODO change current time to event.gettime
        DatabaseFunctions.saveAmbientTempToDatabase(getApplicationContext(), sensorValue, currentTime);
        Log.d(TAG, "Unregistering Sensor & Service");
        // stop the sensor and service
        mSensorManager.unregisterListener(this);
        stopForeground(true);
        stopSelfResult(sID);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
