package com.ticketpro.parking.service;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.ticketpro.model.DeviceData;
import com.ticketpro.model.Feature;
import com.ticketpro.model.FirebaseModel;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.FirebaseDBUpdater;

import java.util.List;

public class DeviceDataWorker extends Worker {
    public DeviceDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (Feature.isFeatureAllowed(Feature.FIELD_TRACKER)) {
            List<DeviceData> pendingData = ParkingDatabase.getInstance(TPApplication.getInstance()).ftDeviceHistoryDao().getPendingLocationUpdates();
            for (DeviceData deviceData : pendingData) {
                final FirebaseModel firebaseModel = new FirebaseModel();
                firebaseModel.setDeviceData(deviceData);
                firebaseModel.setDeviceId(deviceData.getDeviceId());
                firebaseModel.setCustId(deviceData.getCustId());
                FirebaseDBUpdater.getLocationFromlatlng(deviceData, firebaseModel);
            }
        }
        List<DeviceData> syncedData = ParkingDatabase.getInstance(TPApplication.getInstance()).ftDeviceHistoryDao().getSyncedLocationUpdates();
        for (DeviceData deviceData : syncedData) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).ftDeviceHistoryDao().deleteRecord(deviceData.getId());
        }
        return Result.success();
    }
}
