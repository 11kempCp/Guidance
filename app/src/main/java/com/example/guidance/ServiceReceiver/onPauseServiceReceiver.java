package com.example.guidance.ServiceReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import static com.example.guidance.Util.Util.scheduledUnscheduledJobs;





/**
 * Created by Conor K on 15/02/2021.
 */
public class onPauseServiceReceiver extends BroadcastReceiver {

    private static final String TAG = "onPauseServiceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        scheduledUnscheduledJobs(context);



    }


}
