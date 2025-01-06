package com.ticketpro.parking.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.DummyActivity;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.activity.handlers.NotificationHandler;

import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    @SuppressWarnings("deprecation")
    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String notificationType = extras.getString("Type");
        String title = extras.getString("Title");
        String message = extras.getString("Message");
        long chalkId = extras.getLong("ChalkId", 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, DummyActivity.class);
        notificationIntent.putExtra("ChalkId", chalkId);

        if (notificationType == null) {
            return;
        }

        //Validate chalk notifications
        if (notificationType.equals("Chalk")
                || notificationType.equals("PhotoChalk")
                || notificationType.equals("LocationChalk")) {

            try {
                boolean chalkValidation = ChalkVehicle.isValidChalkId(chalkId);
                if (!chalkValidation) {
                    Log.d("NotificationReceiver", "Ignoring chalk notification as chalk entry does not exists.");
                    return;
                }
            } catch (Exception e) {
                Log.d("NotificationReceiver", "Failed to validate chalk details");
            }

            try {
                notificationIntent.putExtra("ChalkId", extras.getLong("ChalkId"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Return if chalk notification is disabled
            if (!TPApplication.getInstance().enableChalkAlerts) {
                return;
            }
        }

        int icon = R.drawable.icon;
        long whatTime = System.currentTimeMillis();
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        int notificationId = (int) System.currentTimeMillis();

        Notification notification = null;

        notificationIntent.putExtra("Message", message.toString());
        notificationIntent.putExtra("Type", notificationType.toString());
        notificationIntent.putExtra("Title", title.toString());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        PendingIntent contentIntent = PendingIntent.getActivity(context, notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentIntent(contentIntent);
            builder.setSmallIcon(icon).setTicker(message).setWhen(whatTime);
            builder.setAutoCancel(true).setContentTitle(title);
            builder.setContentText(message);

            notification = builder.build();
            //Deprecated and we are not using HonetComb version

			/*if (currentapiVersion < android.os.Build.VERSION_CODES.HONEYCOMB) {
				notification = new Notification(icon, message, whatTime);
				notification.setLatestEventInfo(context, title, message, contentIntent);
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				notification.defaults |= Notification.DEFAULT_SOUND;
				notification.defaults |= Notification.DEFAULT_VIBRATE;
			} else {
				NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
				builder.setContentIntent(contentIntent);
				builder.setSmallIcon(icon).setTicker(message).setWhen(whatTime);
				builder.setAutoCancel(true).setContentTitle(title);
				builder.setContentText(message);

				notification = builder.build();
			}*/

            notificationManager.notify("TicketPRO", notificationId, notification);

            if (isAppRunning(context)) {
                NotificationHandler handler = new NotificationHandler(context, notificationIntent);
                handler.showNotification();
            } else {
                TPApplication.getInstance().notificationIntents.add(notificationIntent);
                TPApplication.getInstance().resumeFromNotification = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isAppRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert activityManager != null;
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        final String packageName = context.getPackageName();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }

        return false;
    }
} 