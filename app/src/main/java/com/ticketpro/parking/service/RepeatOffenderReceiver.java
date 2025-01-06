package com.ticketpro.parking.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ticketpro.model.Feature;
import com.ticketpro.model.RepeatOffender;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.api.RepeatOffenderNetworkCalls;
import com.ticketpro.parking.bl.ViolationBLProcessor;
import com.ticketpro.util.DateUtil;

public class RepeatOffenderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.hasExtra("MSG")) {
                String smgType = intent.getStringExtra("MSG");
                assert smgType != null;
                if (smgType.equals("SyncRepeatOffender")) {
                    boolean parkingRepeatoffenderActive = Feature.isFeatureAllowed("ParkingRepeatoffenderActive");
                    if (parkingRepeatoffenderActive) {
                        //BaseActivityImpl.instance.setBLProcessor(new ViolationBLProcessor());
                        if (TPApplication.getInstance().isServiceAvailable) {
                            synchronized (this) {
                                if (!RepeatOffender.getRepeatOffender().isEmpty()) {
                                    RepeatOffenderNetworkCalls.lastRepeatOffenderService(RepeatOffender.getRepeatOffender());
                          //          ((ViolationBLProcessor) BaseActivityImpl.instance.getScreenBLProcessor()).lastRepeatOffenderService(RepeatOffender.getRepeatOffender());
                                }
                                RepeatOffenderNetworkCalls.getlastRepeatOffenderService(TPApplication.getInstance().custId, DateUtil.getCurrentDate());
                            //    ((ViolationBLProcessor) BaseActivityImpl.instance.getScreenBLProcessor()).getlastRepeatOffenderService(TPApplication.getInstance().custId, DateUtil.getCurrentDate());
                            }
                        } else {
                            Log.d("REPEAT_OFFENDER", "NO INTER NET");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
