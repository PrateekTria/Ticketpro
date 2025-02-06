package com.ticketpro.fcm;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ticketpro.model.LPRNotify;
import com.ticketpro.model.Placard;
import com.ticketpro.model.RemoteAction;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.DummyActivity;
import com.ticketpro.parking.activity.HomeActivity;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.activity.handlers.NotificationHandler;
import com.ticketpro.parking.service.RemoteActionHandler;
import com.ticketpro.parking.service.TPSyncAdapter;
import com.ticketpro.util.GCMUtilities;
import com.ticketpro.util.Preference;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private Logger log = Logger.getLogger("MyFirebaseMessagingService");
    private Context context;
    private Preference preference;

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage == null)
            return;

        String message = remoteMessage.getData().get("message");
        String messageType = remoteMessage.getData().get("messageType");
        Log.e(TAG, "From1: " + remoteMessage.getData().get("messageType"));
        Log.e(TAG, "From2: " + remoteMessage.getData().get("message"));
        if (messageType == null || messageType.isEmpty()) {
            try {
                generateNotification(message);
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        } else if (messageType.equals("placard")) {
            try {
                if (!message.contains("Unable")) {
                    String[] split1 = message.split("[#|\n]");
                    Placard placard = new Placard();
                    placard.setPlacardNo(split1[1].trim()+split1[2].trim());
                    placard.setPlacardDetails(message);
                    Placard.insertPlacard(placard);
                }
                generateNotification(message);

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        }else if (messageType.equals("notifyLPR_FCM")) {
            try {
                generateLPRNotification(message);
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        } else if (messageType.equals("SyncNow")) {
            Account syncAccount = new Account(TPSyncAdapter.ACCOUNT, TPSyncAdapter.ACCOUNT_TYPE);
            try {
                AccountManager accountManager = (AccountManager) getApplicationContext().getSystemService(Context.ACCOUNT_SERVICE);
                accountManager.addAccountExplicitly(syncAccount, null, null);
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            try {
                JSONObject jsonData = new JSONObject(message);
                Bundle extras = new Bundle();
                extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

                if (jsonData.has("fullSync") && jsonData.getBoolean("fullSync")) {
                    extras.putBoolean("DatabaseSync", true);
                    extras.putBoolean("FullSync", true);
                } else {
                    extras.putBoolean("DatabaseSync", true);
                    extras.putBoolean("FullSync", false);
                }

                ContentResolver.requestSync(syncAccount, TPSyncAdapter.AUTHORITY, extras);

            } catch (JSONException e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        } else if (messageType.equals("RemoteAction")) {
            processRemoteAction(message);
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, s);
        storeRegIdInPref(s);

        GCMUtilities.register(MyFirebaseMessagingService.this, s);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(TPConstant.FCM_TOKEN, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.apply();
    }

    private void processRemoteAction(String message) {
        try {
            RemoteAction action = new RemoteAction(new JSONObject(message));
            RemoteActionHandler actionHandler = new RemoteActionHandler(getApplicationContext(), action);
            actionHandler.sendEmptyMessage(RemoteActionHandler.ACTION_EXECUTE_TASK);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void generateNotification(String message) throws Exception {
        int icon = R.drawable.icon;
        long whatTime = System.currentTimeMillis();
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        int notificationId = (int) System.currentTimeMillis();

        Intent notificationIntent = null;
        Notification notification = null;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            notificationIntent = new Intent(getApplicationContext(), DummyActivity.class);
        } else {
            notificationIntent = new Intent(getApplicationContext(), HomeActivity.class);
        }

        notificationIntent.putExtra("Message", message);
        notificationIntent.putExtra("Type", "UserMessage");
        notificationIntent.putExtra("Title", "TicketPRO Message");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), notificationId, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(icon).setTicker(message).setWhen(whatTime);
        builder.setAutoCancel(true).setContentTitle("TicketPRO Message");
        builder.setContentText(message);
        notification = builder.build();
        notificationManager.notify("TicketPRO", notificationId, notification);
        //Deprecated and we are not using HonetComb version
		/*if (currentapiVersion < android.os.Build.VERSION_CODES.HONEYCOMB) {
			notification = new Notification(icon, message, whatTime);
			notification.setLatestEventInfo(context, "TicketPRO Message", message, contentIntent);
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notificationManager.notify("TicketPRO", notificationId, notification);
		} else {
			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			builder.setContentIntent(contentIntent);
			builder.setSmallIcon(icon).setTicker(message).setWhen(whatTime);
			builder.setAutoCancel(true).setContentTitle("TicketPRO Message");
			builder.setContentText(message);
			notification = builder.build();

		}*/


        if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            NotificationHandler handler = new NotificationHandler(getApplicationContext(), notificationIntent);
            handler.showNotification();
        } else {
            TPApplication.getInstance().notificationIntents.add(notificationIntent);
            TPApplication.getInstance().resumeFromNotification = true;
        }


        if (isAppRunning(getApplicationContext())) {
            //TPApplication.getInstance().notificationIntents.add(notificationIntent);
            //TPApplication.getInstance().resumeFromNotification = true;
            Handler uiHandler = new Handler(Looper.getMainLooper());

            final Intent finalNotificationIntent = notificationIntent;
            uiHandler.post(new Runnable() {

                @Override
                public void run() {

                    NotificationHandler handler = new NotificationHandler(MyFirebaseMessagingService.this, finalNotificationIntent);
                    try {
                        handler.showNotification();
                        System.out.println("=========================================>1");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } else {
            TPApplication.getInstance().notificationIntents.add(notificationIntent);
            TPApplication.getInstance().resumeFromNotification = true;
        }

    }

    private void generateLPRNotification(String message) throws Exception {
        int icon = R.drawable.icon;
        long whatTime = System.currentTimeMillis();
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        int notificationId = (int) System.currentTimeMillis();
        Intent notificationIntent = null;
        Notification notification = null;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //If message is empty, ignore the notification
        if (StringUtil.isEmpty(message)) {
            return;
        }
        if (isAppRunning(getApplicationContext())) {
            notificationIntent = new Intent(getApplicationContext(), DummyActivity.class);
        } else {
            notificationIntent = new Intent(getApplicationContext(), HomeActivity.class);
        }
        notificationIntent.putExtra("Message", Html.fromHtml(message).toString());
        notificationIntent.putExtra("Type", "LPRNotify");
        notificationIntent.putExtra("Title", "LPR Notification");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), notificationId, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
            builder.setContentIntent(contentIntent);
            builder.setSmallIcon(icon).setTicker("Received LPR Notification").setWhen(whatTime);
            builder.setAutoCancel(true).setContentTitle("LPR Notification");
            builder.setContentText("Received LPR Notification");

            notification = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Deprecated and we are not using HonetComb version
		/*if (currentapiVersion < android.os.Build.VERSION_CODES.HONEYCOMB) {
			notification = new Notification(icon, "Received LPR Notification", whatTime);
			notification.setLatestEventInfo(context, "LPR Notification", "Received LPR Notification", contentIntent);
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		} else {
			try{
				NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
				builder.setContentIntent(contentIntent);
				builder.setSmallIcon(icon).setTicker("Received LPR Notification").setWhen(whatTime);
				builder.setAutoCancel(true).setContentTitle("LPR Notification");
				builder.setContentText("Received LPR Notification");

				notification = builder.build();
			}catch(Exception e)
			{e.printStackTrace();}
		}*/

        notificationManager.notify(TPApplication.notificationId, notification);

        //Add LPR Notification Record
        try {
            LPRNotify notify = new LPRNotify(new JSONObject(message));
            LPRNotify.insertLPRNotify(notify).subscribe();
            /*DatabaseHelper dbHelper = DatabaseHelper.getInstance();

            if (dbHelper != null && notify != null) {
                dbHelper.openWritableDatabase();
                dbHelper.insertOrReplace(notify.getContentValues(), "lpr_notifications");
                dbHelper.closeWritableDb();
            }*/
        } catch (Exception e) {
            Log.e(TAG, "generateLPRNotification: " + TPUtility.getPrintStackTrace(e));
        }

        if (isAppRunning(getApplicationContext())) {
            //TPApplication.getInstance().notificationIntents.add(notificationIntent);
            //TPApplication.getInstance().resumeFromNotification = true;
            Handler uiHandler = new Handler(Looper.getMainLooper());

            final Intent finalNotificationIntent = notificationIntent;
            uiHandler.post(new Runnable() {

                @Override
                public void run() {

                    NotificationHandler handler = new NotificationHandler(MyFirebaseMessagingService.this, finalNotificationIntent);
                    try {
                        handler.showNotification();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } else {
            TPApplication.getInstance().notificationIntents.add(notificationIntent);
            TPApplication.getInstance().resumeFromNotification = true;
        }
    }

    private boolean isAppRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcess.processName.equals(packageName)) {
                return true;
            }
        }

        return false;
    }


    private String removeSpecialCharacters(String str) {
        // Replace Unicode escape sequences for <br /> with empty string
        str = str.replaceAll("\\\\u003Cbr \\/\\\\u003E", "");

        // Remove additional unwanted special characters like carriage returns and new lines
        str = str.replace("\r", "").replace("\n", "").replace("\\/", "");

        // Optionally, replace any other unwanted special characters or patterns here

        return str;
    }
}

