package com.example.guidance.sensorservices;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;

import com.example.guidance.foregroundservices.AmbientTempService;
import com.example.guidance.foregroundservices.StepsService;
import com.example.guidance.realm.DatabaseFunctions;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Conor K on 14/02/2021.
 */
public class StepsJobService extends JobService implements SensorEventListener {

    private static final String TAG = "StepsJobService";
    private SensorManager mSensorManager = null;


    @Override
    public boolean onStartJob(JobParameters params) {
//        Log.d(TAG, "Steps Job Started");
//        doBackgroundWork(params);
//        return true;



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent serviceIntent = new Intent(this, AmbientTempService.class);
            startForegroundService(serviceIntent);
        } else {

            Date currentTime = Calendar.getInstance().getTime();
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            boolean test = mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
            Log.d(TAG, "Job Finished " + currentTime + " sensorManager " + test);

        }
        return false;

    }

    private void doBackgroundWork(JobParameters params) {


        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, "run: " + " time " + currentTime);


        // get sensor manager on starting the service
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // have a default sensor configured
        int sensorType = Sensor.TYPE_STEP_COUNTER;

        // we need the step counter sensor
        Sensor sensor = mSensorManager.getDefaultSensor(sensorType);

        // TODO we could have the sensor reading delay configurable also though that won't do much
        // in this use case since we work with the alarm manager
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);


        Log.d(TAG, "doBackgroundWork: mStepsCounter " + mSensorManager.toString());
        Log.d(TAG, "Job Finished " + currentTime);
        jobFinished(params, false);

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, "Job Cancelled Before Completion " + currentTime);
        Intent serviceIntent = new Intent(this, StepsService.class);
        stopService(serviceIntent);
        return false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float sensorValue = event.values[0];
        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, "onSensorChanged: " + sensorValue);
        DatabaseFunctions.saveStepsCounterToDatabase(getApplicationContext(), sensorValue, currentTime);
        Log.d(TAG, "unregistering sensor and stopping service ");
        // stop the sensor and service
        mSensorManager.unregisterListener(this);
        stopSelf();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
