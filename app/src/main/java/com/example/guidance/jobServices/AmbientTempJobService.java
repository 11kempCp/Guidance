package com.example.guidance.jobServices;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;

import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.services.AmbientTempService;

import java.util.Calendar;
import java.util.Date;

import static com.example.guidance.realm.databasefunctions.AmbientTempDatabaseFunctions.insertAmbientTemp;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;

/**
 * Created by Conor K on 14/02/2021.
 */
public class AmbientTempJobService extends JobService
        implements SensorEventListener {


    private static final String TAG = "AmbientTempJobService";
    private SensorManager mSensorManager;


    @Override
    public boolean onStartJob(JobParameters params) {
        Data_Type data = getDataType(this);

        //validation to ensure that the user has allowed the ambient temperature dataType to be collected and stored
        if (data.isAmbient_temp()) {

            //if the API level is O or above then the AmbientTempService needs to be run as an foregroundService
            //otherwise it can be run inside this jobService
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent serviceIntent = new Intent(this, AmbientTempService.class);
                startForegroundService(serviceIntent);
            } else {

                mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
                mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);

            }
        }

        return false;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, "Job Cancelled Before Completion " + currentTime);
        Intent serviceIntent = new Intent(this, AmbientTempService.class);
        stopService(serviceIntent);
        return false;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float sensorValue = event.values[0];
        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, "onSensorChanged: " + sensorValue);
//        Toast.makeText(this, "Ambient Temp Sensor Changed " + sensorValue, Toast.LENGTH_SHORT).show();
        insertAmbientTemp(getApplicationContext(), sensorValue, currentTime);
        // stop the sensor and service
        mSensorManager.unregisterListener(this);
        stopSelf();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }


}
