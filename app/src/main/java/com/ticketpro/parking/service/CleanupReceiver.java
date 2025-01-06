package com.ticketpro.parking.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ticketpro.util.DataUtility;

public class CleanupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        System.out.println("*****************Receiver call*************");
        try {
            DataUtility.checkExpiredChalks(context);
            //DataUtility.removeOldTickets(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}