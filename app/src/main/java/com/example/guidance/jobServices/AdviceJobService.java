package com.example.guidance.jobServices;

import android.app.job.JobParameters;
import android.app.job.JobService;

/**
 * Created by Conor K on 20/03/2021.
 */
public class AdviceJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }

}
