package com.example.guidance.ServiceReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.guidance.foregroundservices.AmbientTempService;
import com.example.guidance.scheduler.Util;
import com.example.guidance.sensorservices.AmbientTempJobService;


/**
 * Created by Conor K on 15/02/2021.
 */
public class AmbientTempServiceReceiver extends BroadcastReceiver {

    private static final String TAG = "AmbientTempSR";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Service tried to stop");
        Util.scheduleJob(context);

//        context.startForegroundService(new Intent(context, AmbientTempService.class));


    }
}