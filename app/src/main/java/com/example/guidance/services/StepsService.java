package com.example.guidance.services;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.guidance.R;
import com.example.guidance.activity.MainActivity;
import com.example.guidance.app.App;

import java.util.Calendar;

import static com.example.guidance.realm.DatabaseFunctions.*;

/**
 * Created by Conor K on 18/02/2021.
 */
public class StepsService extends Service {

    public static final String TAG = "StepsService";
    // Steps counted in current session
    private static int mSteps = 0;
    // Value of the step counter sensor when the listener was registered.
    // (Total steps are calculated from this value.)
    private static int mCounterSteps = 0;

    // max batch latency is specified in microseconds
    private static final int BATCH_LATENCY_10s = 10000000;

    int sID;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        //TODO Make better notification
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle(getString(R.string.steps))
                .setContentText(getString(R.string.notification_steps))
                .setSmallIcon(R.drawable.ic_data)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(2, notification);
         sID = startId;
        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        final boolean batchMode = mSensorManager.registerListener(mListener, sensor, SensorManager.SENSOR_DELAY_NORMAL, BATCH_LATENCY_10s);


        return START_NOT_STICKY;

    }


    public static void resetSensor() {
        mSteps = 0;
        mCounterSteps = 0;
    }

    private static final SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {



            /*
            Taken from https://github.com/android/sensors-samples
            A step counter event contains the total number of steps since the listener
            was first registered. We need to keep track of this initial value to calculate the
            number of steps taken, as the first value a listener receives is undefined.
             */

            if (mCounterSteps < 1) {
                // initial value
                mCounterSteps = (int) event.values[0];
            }

            // Calculate steps taken based on first counter value received.
            mSteps = (int) event.values[0] - mCounterSteps;

            // Add the number of steps previously taken, otherwise the counter would start at 0.
            // This is needed to keep the counter consistent across rotation changes.
//            mSteps = mSteps + mPreviousCounterSteps;

            Log.d(TAG, "onSensorChanged: " + mSteps);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, " DESTROY Total Steps " + mSteps, Toast.LENGTH_SHORT).show();

        stopForeground(sID);

        super.onDestroy();
    }

    public static int getmSteps() {
        return mSteps;
    }
}
