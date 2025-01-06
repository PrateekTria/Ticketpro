package com.ticketpro.parking.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ticketpro.util.DataUtility;

import org.apache.log4j.Logger;

public class TicketCleanupReceiver extends BroadcastReceiver {
    public Logger log = Logger.getLogger("TicketCleanupReceiver");

    @Override
    public void onReceive(final Context context, Intent intent) {
        System.out.println("*****************Receiver call*************");
        try {
            if (intent.hasExtra("TypeClearTickets")) {
                log.info("Broadcast received : TicketCleanup");
                String smgType = intent.getStringExtra("TypeClearTickets");
                boolean fromFeature = intent.getBooleanExtra("fromFeature", false);
                clearParkingTicketByFeature(context, smgType, fromFeature);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearParkingTicketByFeature(Context context, String smgType, boolean fromFeature) {
        try {
            if (smgType.equals("Delete") || smgType.equals("Tickets")) {
                log.info("clearParkingTicketByFeature");
                if (fromFeature) {
                 //   DataUtility.removeOldTicketsByFeature(log);
                } else {
                    DataUtility.removeOldTicketsAtMidnight(log);
                }
                DataUtility.checkExpiredChalks(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
