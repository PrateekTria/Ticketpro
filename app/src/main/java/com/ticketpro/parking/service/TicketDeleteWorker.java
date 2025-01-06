package com.ticketpro.parking.service;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.ticketpro.model.Feature;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.util.DataUtility;

import org.apache.log4j.Logger;

public class TicketDeleteWorker extends Worker {
    private final Logger log = Logger.getLogger("TicketCleanup");

    public TicketDeleteWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        Activity currentAcivity = TPApplication.getInstance().currentAcivity;
        clearParkingTicketByFeature(currentAcivity, Feature.isAutoDeleteFeatureAllowed(Feature.PARKING_CLEARTICKET_SCHEDULER, currentAcivity));
        return ListenableWorker.Result.success();
    }

    public void clearParkingTicketByFeature(Context context, boolean fromFeature) {
        try {
            log.info("clearParkingTicketByFeature");
            if (fromFeature) {
                DataUtility.removeOldTicketsByFeature(log);
            } else {
                DataUtility.removeOldTicketsAtMidnight(log);
            }
            DataUtility.checkExpiredChalks(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
