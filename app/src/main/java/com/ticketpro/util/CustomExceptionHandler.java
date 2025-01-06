package com.ticketpro.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.ticketpro.model.ErrorLog;
import com.ticketpro.parking.activity.BaseActivity;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.service.ServiceHandlerImpl;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Date;

public class CustomExceptionHandler implements UncaughtExceptionHandler {

    private Context ctx;
    private BaseActivity screen;

    public CustomExceptionHandler(Context ctx, BaseActivity screen) {
        this.ctx = ctx;
        this.screen = screen;
    }

    public void uncaughtException(@NotNull Thread thread, @NotNull Throwable exception) {
        Logger log = Logger.getLogger("CustomExceptionHandler");
        log.error(exception.getMessage());
        FirebaseCrashlytics.getInstance().recordException(exception);
        FirebaseCrashlytics.getInstance().log(String.valueOf(TPApplication.getInstance().userId + TPApplication.getInstance().deviceId));
        SharedPreferences mPreferences = ctx.getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        TPApplication app = (TPApplication) ctx.getApplicationContext();
        editor.putBoolean(TPConstant.PREFS_KEY_RESTORE_SESSION, true);
        editor.putInt(TPConstant.PREFS_KEY_RESTORE_USERID, app.getCurrentUserId());
        editor.putInt(TPConstant.PREFS_KEY_RESTORE_DEVICEID, app.getDeviceId());
        editor.putInt(TPConstant.PREFS_KEY_RESTORE_CUSTID, app.getCustId());

        if (app.getActiveDutyInfo() != null) {
            editor.putInt(TPConstant.PREFS_KEY_RESTORE_DUTYID, app.getActiveDutyInfo().getId());
        }

        try {
            ErrorLog error = new ErrorLog();
            error.setErrorId(ErrorLog.getNextPrimaryId());
            error.setUserId(app.getCurrentUserId());
            error.setDeviceId(app.getDeviceId());
            error.setCustId(app.getCustId());

            String versionString = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
            if (versionString == null || versionString.isEmpty()) {
                versionString = "[TP]";
            } else {
                versionString = "[TP v" + versionString + "]";
            }
            error.setApp_version(TPApplication.getInstance().getDeviceInfo().getAppVersion());
            error.setModule(TPConstant.MODULE_NAME);
            error.setDevice(app.getDeviceInfo().getDevice());
            error.setErrorType(versionString + ":" + this.screen.getClass().getSimpleName());
            error.setErrorDesc(TPUtility.getPrintStackTrace(exception));
            error.setErrorDate(new Date());

            final ErrorLog errorFinal = error;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (isNetworkConnected()) {
                            ServiceHandlerImpl service = new ServiceHandlerImpl();
                            ArrayList<ErrorLog> errors = new ArrayList<>();
                            errors.add(errorFinal);
                            service.sendErrorLog(errors);
                        } else {
                            ErrorLog.insertErrorLog(errorFinal);
                            /*DatabaseHelper.getInstance().openWritableDatabase();
                            DatabaseHelper.getInstance().insertOrReplace(errorFinal.getContentValues(), TPConstant.TABLE_ERROR_LOGS);
                            DatabaseHelper.getInstance().closeWritableDb();*/
                        }
                    } catch (Exception e) {
                        Log.e("Exception Handler", e.getMessage());
                    }
                }
            }).start();
        } catch (Exception e) {
            Log.e("Exception Handler", e.getMessage());
        }

        editor.commit();
        System.exit(1);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Network currentNetwork = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            currentNetwork = connectivityManager.getActiveNetwork();
        }
        NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(currentNetwork);
        LinkProperties linkProperties = connectivityManager.getLinkProperties(currentNetwork);

        //final boolean internetPingSuccess = canPingInternetConnection();

        return networkInfo != null
                && networkInfo.isAvailable()
                && networkInfo.isConnected(); //&& internetPingSuccess;

    }

    /*public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
            return isConnected(TPConstant.SERVICE_URL);
        } else {
            return false;
        }
    }*/

    public boolean isConnected(String url) {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Don't process if network is not available or disconnected
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            return false;
        }

        try {
            HttpGet request = new HttpGet(url);
            DefaultHttpClient httpClient = new TPHttpClient();
            httpClient.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
                @Override
                public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                    return 0;
                }
            });

            HttpResponse response = httpClient.execute(request);
            return response.getStatusLine().getStatusCode() == 200;
        } catch (IOException e) {
            Log.e("NetworkConnection", "Error " + e.getMessage());
        }

        return false;
    }
}
