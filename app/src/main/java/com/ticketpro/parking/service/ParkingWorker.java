package com.ticketpro.parking.service;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.ticketpro.model.ChalkVehicle;

import java.util.ArrayList;

public class ParkingWorker extends Worker {
    Activity context;
    public ParkingWorker(@NonNull Activity context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }


    @NonNull
    @Override
    public Result doWork() {

        __init();

        return Result.success();
    }

    private void __init() {
        try {
            ArrayList<ChalkVehicle> pendingChalkedVehicle = ChalkVehicle.getPendingChalkedVehicle();
            if (pendingChalkedVehicle!=null && pendingChalkedVehicle.size()>0){
                Intent serviceIntent = new Intent(context, JobIntentServiceSaveChalk.class);
                JobIntentServiceSaveChalk.enqueueWork(context, serviceIntent);

            }else { Log.d("WriteTicketActivity","No pending chalk available.");}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        Log.d("ParkingWorker","--------------CALL ListenableFuture-------");
        mFuture = ResolvableFuture.create();
        try {
            ArrayList<ChalkVehicle> pendingChalkedVehicle = ChalkVehicle.getPendingChalkedVehicle();
            if (pendingChalkedVehicle!=null && pendingChalkedVehicle.size()>0){
                Intent serviceIntent = new Intent(context, JobIntentServiceSaveChalk.class);
                JobIntentServiceSaveChalk.enqueueWork(context, serviceIntent);

            }else { Log.d("WriteTicketActivity","No pending chalk available.");}

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mFuture;
    }*/
}
