package com.example.guidance.ServiceReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.guidance.foregroundservices.AmbientTempService;
import com.example.guidance.foregroundservices.StepsService;
import com.example.guidance.scheduler.Util;
import com.example.guidance.sensorservices.AmbientTempJobService;
import com.example.guidance.sensorservices.StepsJobService;

import static com.example.guidance.scheduler.Util.AMBIENT_TEMP;
import static com.example.guidance.scheduler.Util.STEPS;
import static com.example.guidance.scheduler.Util.isJobScheduled;


/**
 * Created by Conor K on 15/02/2021.
 */
public class AmbientTempServiceReceiver extends BroadcastReceiver {

    private static final String TAG = "AmbientTempSR";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
//        Util.scheduleJob(context);


        //TODO switch statement to swap through and check what services are not scheduled
        switch(intent.getAction()){
            case "AmbientTempService":
                if(!isJobScheduled(context, AMBIENT_TEMP)){
                    boolean scheduled = Util.scheduleJob(context, AmbientTempJobService.class, AMBIENT_TEMP, 15);
                    Log.d(TAG, "Scheduled AMBIENT_TEMP " + scheduled);
                }else {
                    Log.d(TAG, "AMBIENT_TEMP Already Scheduled");
                }
                break;
            case "StepsService":
                if(!isJobScheduled(context, STEPS)){
                    //TODO change from 15 minutes to once per day
                    boolean scheduled = Util.scheduleJob(context, StepsJobService.class, STEPS, 15);
                    Log.d(TAG, "Scheduled STEPS " + scheduled);
                }else {
                    Log.d(TAG, "STEPS Already Scheduled");

                }
                break;
        }
    }
}