package com.example.guidance.foregroundservices;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.guidance.model.Ambient_Temperature;

import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

/**
 * Created by Conor K on 16/02/2021.
 */
public class AmbientTempService extends Service implements SensorEventListener {

    private static final String TAG = "AmbientTempService";

    private SensorManager mSensorManager = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Date currentTime = Calendar.getInstance().getTime();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);

        Log.d(TAG, "Job Finished " + currentTime);

    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        float sensorValue = event.values[0];
        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, "onSensorChanged: " + sensorValue);

//        Realm.init(Realm.getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {
            Ambient_Temperature init = r.createObject(Ambient_Temperature.class, new ObjectId());
            init.setAmbientTemp(sensorValue);
            init.setDateTime(currentTime);
            Log.d(TAG, "executed transaction : " + currentTime);
        });
        //remove if problem realm is already closed
        realm.close();


        Log.d(TAG, "unregistering sensor and stopping service ");
        // stop the sensor and service
        mSensorManager.unregisterListener(this);
        stopSelf();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
