package com.example.guidance.sensorservices;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.guidance.R;
import com.example.guidance.activity.MainActivity;
import com.example.guidance.model.Ambient_Temperature;

import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

/**
 * Created by Conor K on 14/02/2021.
 */
public class AmbientTempJobService extends JobService
        implements SensorEventListener
{


    private static final String TAG = "AmbientTempJobService";
    private boolean jobCancelled = false;
//    Realm realm;

    private SensorManager mSensorManager = null;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob");
        doBackgroundWork(params);

//        String NOTIFICATION_CHANNEL_ID = "example.AmbientTempService";
//        String channelName = "Background Service";
//        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
//        chan.setLightColor(Color.BLUE);
//        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        assert manager != null;
//        manager.createNotificationChannel(chan);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//        Notification notification = notificationBuilder.setOngoing(true)
//                .setContentTitle("App is running in background")
//                .setPriority(NotificationManager.IMPORTANCE_MIN)
//                .setCategory(Notification.CATEGORY_SERVICE)
//                .build();
//
//
//        startForeground(2 , notification);


        return false;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d ("Service status", "Running");
                return true;
            }
        }
        Log.d ("Service status", "Not running");
        return false;
    }




    private void doBackgroundWork(JobParameters params) {
        new Thread(() -> {
            Date currentTime = Calendar.getInstance().getTime();
//            Log.d(TAG, "run: " + " time " + currentTime);
            if (jobCancelled) {
                Log.d(TAG, "jobCancelled " + jobCancelled);
                jobCancelled = false;
                return;
            }


            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);


            Log.d(TAG, "Job Finished " + currentTime);
            jobFinished(params, false);
        }).start();

    }



    @Override
    public boolean onStopJob(JobParameters params) {
        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, "Job Cancelled Before Completion " + currentTime);
        jobCancelled = true;
        return false;
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        float sensorValue = event.values[0];
        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, "onSensorChanged: " + sensorValue);

        Realm.init(getApplicationContext());
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



    //    @Override
//    public void onDestroy() {
//
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("restartservice");
//        broadcastIntent.setClass(this, AmbientTempServiceReceiver.class);
//
//        super.onDestroy();
//    }
}
