package com.ticketpro.parking.service;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.widget.Toast;

import com.ticketpro.model.Feature;

public class JobUtil {

    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, ParkingJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        //Toast.makeText(context, "Sync Job Started", Toast.LENGTH_SHORT).show();
        builder.setPeriodic(15 * 60 * 1000);
        builder.setPersisted(true);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setBackoffCriteria(15 * 60 * 1000, JobInfo.BACKOFF_POLICY_LINEAR);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        assert jobScheduler != null;
        jobScheduler.schedule(builder.build());
    }

    public static void cancelJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        assert jobScheduler != null;
        jobScheduler.cancel(0);
    }

}
