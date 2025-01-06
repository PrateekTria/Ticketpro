package com.ticketpro.util;

import android.app.Activity;
import android.content.Context;

import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Duration;
import com.ticketpro.model.Feature;
import com.ticketpro.model.Ticket;
import com.ticketpro.parking.service.TicketDeleteWorker;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DataUtility {
    // Auto-Delete Expired chalk logs
    private static final Logger log = Logger.getLogger(DataUtility.class);

    public static void checkExpiredChalks(Context ctx) {
        boolean isAutoDeleteRequired = Feature.isFeatureAllowed(Feature.AUTODELETE_CHALKLOG);

        Duration duration;
        try {
            ArrayList<ChalkVehicle> chalks = ChalkVehicle.getAllChalkedVehicle();
            for (ChalkVehicle chalk : chalks) {
                DataUtility.setExpiredChalk(chalk, ctx);
                if (!isAutoDeleteRequired) {
                    continue;
                }

                long difference = (new Date().getTime() - chalk.getChalkDate().getTime());
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;
                long elapsedDays = difference / daysInMilli;

                long chalkId = chalk.getChalkId();
                if (elapsedDays > 0) {
                    if (chalk.isExpired()) {
                        duration = Duration.getAutoDeleteById(chalk.getDurationId());
                        if (duration != null) {
                            String deleteChalk = duration.getAutoDelete();
                            if (deleteChalk != null && deleteChalk.equalsIgnoreCase("Y")) {
                                ChalkVehicle.removeChalkById(chalkId, "");
                            }
                        }
                    }
                }
            }
        } catch (Exception ae) {
            ae.printStackTrace();
        }
    }

    public static void setDeleteScheduler(Activity activity) {
        // Initiate periodic task for data cleanup
//        Intent intent = new Intent(activity, TicketCleanupReceiver.class);
//        intent.putExtra("TypeClearTickets", "Tickets");
//        PendingIntent pIntent;
//        AlarmManager alarm = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        String featureParkingClearTicket;
        
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();
        if (Feature.isAutoDeleteFeatureAllowed(Feature.PARKING_CLEARTICKET_SCHEDULER, activity)) {
            featureParkingClearTicket = Feature.getFeatureParkingClearTicket(Feature.PARKING_CLEARTICKET_SCHEDULER);
            log.info("delete feature is active and value is set to :" + featureParkingClearTicket);
            int minutes = Integer.parseInt(featureParkingClearTicket);
            //intent.putExtra("fromFeature", true);
            //pIntent = PendingIntent.getBroadcast(activity, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //assert alarm != null;
            //alarm.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), minutes * 60 * 1000, pIntent);

            PeriodicWorkRequest workRequest = new PeriodicWorkRequest
                    .Builder(TicketDeleteWorker.class, minutes, TimeUnit.MINUTES)
                    .setConstraints(constraints).addTag(TPConstant.DELETE_TICKETS)
                    .build();
            WorkManager.getInstance(activity).cancelAllWorkByTag(TPConstant.DELETE_TICKETS);
            WorkManager.getInstance(activity).enqueue(workRequest);
        } else {
            Calendar currentDate = Calendar.getInstance(Locale.US);
            Calendar dueDate = Calendar.getInstance(Locale.US);
            dueDate.set(Calendar.HOUR_OF_DAY, 0);
            dueDate.set(Calendar.MINUTE, 15);
            dueDate.set(Calendar.SECOND, 59);
            if (dueDate.before(currentDate)) {
                dueDate.add(Calendar.HOUR_OF_DAY, 24);
            }
            long timeDiff = dueDate.getTimeInMillis() - currentDate.getTimeInMillis();
            OneTimeWorkRequest dailyRequest = new OneTimeWorkRequest
                    .Builder(TicketDeleteWorker.class)
                    .setConstraints(constraints)
                    .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                    .addTag(TPConstant.DELETE_TICKETS)
                    .build();
            WorkManager.getInstance(activity).cancelAllWorkByTag(TPConstant.DELETE_TICKETS);
            WorkManager.getInstance(activity).enqueue(dailyRequest);
            log.info("delete tickets at midnight");
//            intent.putExtra("fromFeature", false);
//            pIntent = PendingIntent.getBroadcast(activity, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            assert alarm != null;
//            alarm.setRepeating(AlarmManager.RTC, dueDate.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);
        }
    }

    public static void setExpiredChalk(ChalkVehicle chalk, Context ctx) {
        try {
            int mins = Duration.getDurationMinsById(chalk.getDurationId(), ctx);
            long diff = (new Date().getTime() - chalk.getChalkDate().getTime());
            long expTime = (diff / 1000) / 60;
            if (expTime > mins) {
                if (!chalk.isExpired()) {
                    chalk.setIsExpired("Y");
                    ChalkVehicle.insertChalkVehicle(chalk).subscribe();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void removeOldTicketsByFeature(Logger log) {
        try {
            DataUtility.log.info("Ticket Cleanup Scheduler executed");
            String value = Feature.getFeatureValue(Feature.PARKING_CLEAR_TICKET_BY_HOUR);
            value = "-" + value + " minutes";
            Ticket.removeAllOlderTicketsByHour(value, log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeOldTicketsAtMidnight(Logger log) {
        try {
            DataUtility.log.info("Ticket Cleanup Scheduler executed at midnight");
            Ticket.removeAllOlderTicketsAtMidnight(log, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
