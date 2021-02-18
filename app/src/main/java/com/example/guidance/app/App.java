package com.example.guidance.app;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * Created by Conor K on 17/02/2021.
 */
public class App extends Application {

    public static final String CHANNEL_ID = "exampleServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = getSystemService(NotificationManager.class);

            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID,"Example Service Channel", NotificationManager.IMPORTANCE_DEFAULT);


            manager.createNotificationChannel(serviceChannel);
        }

    }
}
