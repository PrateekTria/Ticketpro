package com.ticketpro.parking.service;


import android.app.job.JobParameters;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.ticketpro.model.DeviceData;
import com.ticketpro.model.Feature;
import com.ticketpro.model.FirebaseModel;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.FirebaseDBUpdater;

import java.util.List;

public class ParkingJobService extends android.app.job.JobService {
    private static final String TAG = "ParkingJobService";

    @Override
    public boolean onStartJob(final JobParameters params) {
        new Thread(() -> {
            Log.d(TAG, "Job Started:" + System.currentTimeMillis());
            try {
                Looper.prepare();
                Toast.makeText(TPApplication.getInstance().currentAcivity, "Job Started", Toast.LENGTH_SHORT).show();

                if (Feature.isFeatureAllowed(Feature.FIELD_TRACKER)) {
                    List<DeviceData> pendingData = ParkingDatabase.getInstance(TPApplication.getInstance()).ftDeviceHistoryDao().getPendingLocationUpdates();
                    for (DeviceData deviceData : pendingData) {
                        final FirebaseModel firebaseModel = new FirebaseModel();
                        firebaseModel.setDeviceData(deviceData);
                        firebaseModel.setDeviceId(deviceData.getDeviceId());
                        firebaseModel.setCustId(deviceData.getCustId());
                        //Toast.makeText(mContext, "Update FB from DB method called. " + firebaseModel.getDeviceData().getActivityName(), Toast.LENGTH_SHORT).show();
                        FirebaseDBUpdater.getLocationFromlatlng(deviceData, firebaseModel);
                    }
                }
                List<DeviceData> syncedData = ParkingDatabase.getInstance(TPApplication.getInstance()).ftDeviceHistoryDao().getSyncedLocationUpdates();
                for (DeviceData deviceData : syncedData) {
                    ParkingDatabase.getInstance(TPApplication.getInstance()).ftDeviceHistoryDao().deleteRecord(deviceData.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            jobFinished(params, true);
            Log.d(TAG, "Job Finished");
        }).start();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Sync Job Failed");
        return true;
    }
}
