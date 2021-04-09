package com.example.guidance.services;

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

import static com.example.guidance.Util.Util.STEPS;

/**
 * Created by Conor K on 18/02/2021.
 */
public class StepsService extends Service {

    public static final String TAG = "StepsService";
    // Steps counted in current session
    private static int steps = 0;
    // Value of the step counter sensor when the listener was registered.
    // (Total steps are calculated from this value.)
    private static int counterSteps = 0;

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

        startForeground(STEPS, notification);
        sID = startId;

        //creates the step counter
        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        final boolean batchMode = mSensorManager.registerListener(mListener, sensor, SensorManager.SENSOR_DELAY_NORMAL, BATCH_LATENCY_10s);


        return START_NOT_STICKY;

    }

    /**
     * resets the stepCounter
     */
    public static void resetSensor() {
        steps = 0;
        counterSteps = 0;
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

            if (counterSteps < 1) {
                // initial value
                counterSteps = (int) event.values[0];
            }

            // Calculate steps taken based on first counter value received.
            steps = (int) event.values[0] - counterSteps;

            // Add the number of steps previously taken, otherwise the counter would start at 0.
            // This is needed to keep the counter consistent across rotation changes.
//            mSteps = mSteps + mPreviousCounterSteps;

            Log.d(TAG, "onSensorChanged: " + steps);
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
        stopForeground(sID);
        super.onDestroy();
    }

    /**
     * Returns the current value of the steps variable
     * */
    public static int getSteps() {
        return steps;
    }
}
