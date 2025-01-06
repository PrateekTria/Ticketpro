package com.ticketpro.parking.service;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.ticketpro.model.Feature;
import com.ticketpro.parking.activity.HomeActivity;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.util.TPUtility;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class ServicePhotoPurge extends JobIntentService {
    private static final String TAG = "ServicePhotoPurge";
    private static final Logger log = Logger.getLogger("ServicePhotoPurge");
    public TPApplication TPApp;

    public static void enqueueWork(HomeActivity context, Intent serviceIntent) {
        enqueueWork(context, ServicePhotoPurge.class, 120, serviceIntent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        try {
            photoPurge();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }

    private void photoPurge() {
        if (Feature.isSystemFeatureAllowed(Feature.PHOTO_PURGE)) {
            String daysString = Feature.getFeatureValue(Feature.PHOTO_PURGE);
            if (daysString != null) {
                if (daysString.isEmpty()) {
                    daysString = Feature.getFeatureValue(Feature.PHOTO_PURGE);
                }
                try {
                    int days = Integer.parseInt(daysString);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.DATE, -days);

                    File dir = new File(TPUtility.getDataFolder() + "TicketImages");
                    File[] files = dir.listFiles();

                    if (files != null && files.length>0) {
                        for (File file : files) {
                            long lastmodified = file.lastModified();
                            if (lastmodified < cal.getTimeInMillis()) {
                                log.info("Deleting ticket image " + file.getName());
                                file.delete();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }

                //Deleting LPR Images on photoPurge Day basis
                try {
                    int days = Integer.parseInt(daysString);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.DATE, -days);

                    File dir = new File(TPUtility.getDataFolder() + "LPRImages");
                    File[] files = dir.listFiles();
//                    assert files != null;
                    if (files!=null && files.length>0){
                    for (File file : files) {
                        long lastmodified = file.lastModified();
                        if (lastmodified < cal.getTimeInMillis()) {
                            file.delete();
                            log.info("Deleting LPR ticket image " + file.getName());
                        }
                    }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }

                //Deleting Chalk Images on photoPurge Day basis
                try {
                    int days = Integer.parseInt(daysString);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.DATE, -days);

                    File dir = new File(TPUtility.getDataFolder() + "ChalkImages");
                    File[] files = dir.listFiles();
                    if (files!=null && files.length>1){
                    for (File file : files) {
                        long lastmodified = file.lastModified();
                        if (lastmodified < cal.getTimeInMillis()) {
                            file.delete();
                            log.info("Deleting Chalk image " + file.getName());
                        }
                    }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }

                //Deleting CSV Files on photoPurge Day basis
                try {
                    int days = Integer.parseInt(daysString);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.DATE, -days);

                    File dir = new File(TPUtility.getDataFolder() + "CSVBackups");
                    File[] files = dir.listFiles();
                    if (files != null && files.length>0) {
                        for (File file : files) {
                            long lastmodified = file.lastModified();
                            if (lastmodified < cal.getTimeInMillis()) {
                                file.delete();
                                log.info("Deleting CSVBackup files" + file.getName());
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }

        }

    }
}
