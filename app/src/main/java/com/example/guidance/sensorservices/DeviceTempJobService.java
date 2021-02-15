package com.example.guidance.sensorservices;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.guidance.model.Ambient_Temperature;

import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Conor K on 14/02/2021.
 */
public class DeviceTempJobService extends JobService implements SensorEventListener {

    static Realm realm;


    private static final String TAG = "DeviceTempJobService";
    private boolean jobCancelled = false;


    private SensorManager mSensorManager = null;
    private Sensor mDeviceTemp;
    public static final String KEY_SENSOR_TYPE = "sensor_type";


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Steps Job Started");
        realm = Realm.getDefaultInstance();

        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(JobParameters params) {


        new Thread(() -> {

            Date currentTime = Calendar.getInstance().getTime();
//            Log.d(TAG, "run: " + " time " + currentTime);

            if (jobCancelled) {
                return;
            }

            // get sensor manager on starting the service
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

            // have a default sensor configured
            int sensorType = Sensor.TYPE_AMBIENT_TEMPERATURE;

            // we need the step counter sensor
            Sensor sensor = mSensorManager.getDefaultSensor(sensorType);

            // TODO we could have the sensor reading delay configurable also though that won't do much
            // in this use case since we work with the alarm manager
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);


//            Log.d(TAG, "doBackgroundWork: mAmbientTemp " + mSensorManager.toString());


            Log.d(TAG, "Job Finished " + currentTime);
            jobFinished(params, false);
        }).start();

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, "Job Cancelled Before Completion " + currentTime);
        jobCancelled = true;
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float sensorValue = event.values[0];
        Date currentTime = Calendar.getInstance().getTime();
//        Log.d(TAG, "onSensorChanged: " + sensorValue);


        realm.executeTransactionAsync(r -> {
            Ambient_Temperature init = r.createObject(Ambient_Temperature.class, new ObjectId());
            init.setAmbientTemp(sensorValue);
            init.setDateTime(currentTime);
            Log.d(TAG, "executed transaction : " + currentTime);

        });

        // stop the sensor and service
        mSensorManager.unregisterListener(this);
        stopSelf();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    @Override
    public void onDestroy() {


        super.onDestroy();
    }
}
