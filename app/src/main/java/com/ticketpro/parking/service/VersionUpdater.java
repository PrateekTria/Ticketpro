package com.ticketpro.parking.service;

import static android.content.Context.MODE_PRIVATE;
import static com.ticketpro.util.TPConstant.TAG;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.model.Params;
import com.ticketpro.model.RequestPOJO;
import com.ticketpro.model.VersionUpdate;
import com.ticketpro.model.VersionUpdateResponse;
import com.ticketpro.parking.proxy.Proxy;
import com.ticketpro.util.CallbackHandler;
import com.ticketpro.util.DownloadFilesTask;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.io.File;

import io.reactivex.annotations.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VersionUpdater {
    private static VersionUpdater instance;
    private Proxy serviceProxy;
    private Activity activity;
    private CallbackHandler callback;
    private ProgressDialog pd;
    private SharedPreferences mPreferences;

    private VersionUpdater() {

    }

    public static VersionUpdater getInstance() {
        if (instance == null) {
            instance = new VersionUpdater();
        }
        return instance;
    }

    public void checkForUpdate(CallbackHandler callbackHandler) {
        this.callback = callbackHandler;
        mPreferences = activity.getSharedPreferences(activity.getPackageName(), MODE_PRIVATE);

        try {
            int custId = mPreferences.getInt(TPConstant.PREFS_KEY_RESTORE_CUSTID, 0);
            int deviceId = mPreferences.getInt(TPConstant.PREFS_KEY_RESTORE_DEVICEID, 0);
            ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
            Params params = new Params();
            params.setCustId(custId);
            params.setDeviceId(deviceId);
            params.setModule(TPConstant.MODULE_NAME);
            RequestPOJO requestPOJO = new RequestPOJO();
            requestPOJO.setParams(params);
            requestPOJO.setMethod("getVersionUpdates");
            api.getVersionUpdates(requestPOJO).enqueue(new Callback<VersionUpdateResponse>() {
                @Override
                public void onResponse(@NonNull Call<VersionUpdateResponse> call, @NonNull Response<VersionUpdateResponse> response) {
                    try {
                        if (response.body() == null) {
                            callback.success("No Updates");
                            return;
                        }
                        VersionUpdate version = response.body().getResult();
                        String currentVersion = "0.0.0";
                        try {
                            currentVersion = getCurrentVersion();
                        } catch (NameNotFoundException ignored) {

                        }

                        if (version.getVersion() == null || !version.isNewVersion(currentVersion)) {
                            callback.success("No Updates");
                        } else {
                            displayUpdates(version);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.success("No Updates");

                    }
                }

                @Override
                public void onFailure(@io.reactivex.annotations.NonNull Call<VersionUpdateResponse> call, @io.reactivex.annotations.NonNull Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                    callback.failure("No Updates");
                }
            });
        } catch (Exception e) {
            callback.failure("Error");

        }

    }

    public void installNewVersion(String ApkURL) {
        final String filePath = TPUtility.getDataFolder() + TPConstant.MODULE_NAME + "_Updates.APK";
        String downloadURL;

       /* try {
            if (DatabaseHelper.getInstance() == null) {
                DatabaseHelper.init(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DatabaseHelper.init(activity);
        }
        //Backup Database
        DatabaseHelper.getInstance().backupDatabase();*/

        //Remove existing file
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        if (ApkURL.contains("http://") || ApkURL.contains("https://") || ApkURL.contains("ftp://")) {
            downloadURL = ApkURL;
        } else {
            downloadURL = TPConstant.UPDATE_URL + "/" + ApkURL;
        }

        pd = new ProgressDialog(activity);
        pd.setCancelable(false);
        pd.setMessage("Downloading Update...");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();

        DownloadFilesTask downloadTask = new DownloadFilesTask();
        downloadTask.setCallback(new CallbackHandler() {
            @Override
            public void success(String data) {
                pd.hide();
                Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                intent.setData(Uri.fromFile(new File(filePath)));
                activity.startActivity(intent);
            }

            @Override
            public void failure(String message) {

                pd.hide();
                callback.failure("Error");
            }
        });

        downloadTask.execute(downloadURL, filePath);
    }

    public String getCurrentVersion() throws NameNotFoundException {
        PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
        if (packageInfo != null) {
            return packageInfo.versionName;
        }

        return "0.0.0";
    }

    public void displayUpdates(final VersionUpdate version) {
        Intent intent = new Intent(activity, VersionUpdateDialogActivity.class);
        String msg = /*"<style>body { color: #000000;} </style>" +*/
                "<p><b>New Version: " + version.getVersion() + "</b></p>" +
                        "<p>Release Notes:</p>" + "<b>" +
                        StringUtil.getDisplayString(version.getNotes()) + "</b>";
        intent.putExtra("message", msg);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("versionUpdate", version);
        intent.putExtra("update", mBundle);
        activity.startActivity(intent);
        activity.finish();

        /*View view = LayoutInflater.from(activity).inflate(R.layout.item_view, null);
        TextView headerTV = (TextView) view.findViewById(R.id.headerTV);
        TextView valueTV = (TextView) view.findViewById(R.id.valueTV);
        TextView longTextTV = (TextView) view.findViewById(R.id.longTextTV);
        longTextTV.setVisibility(View.VISIBLE);

        String msg = ("New Version" + "\n") +
                "Release Notes";
        headerTV.setText(msg);
        valueTV.setText((" : " + version.getVersion()) + "\n" + " : ");
        longTextTV.setText(version.getNotes());

        WebView wv = new WebView(activity);
        wv.loadData(msg.toString(), "text/html", "utf-8");
        wv.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        wv.getSettings().setDefaultTextEncodingName("utf-8");

        if (forceInstall) {
            new AlertDialog.Builder(activity)
                    .setView(view)
                    .setCancelable(false)
                    .setTitle("Version Update")
                    .setPositiveButton("Install", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (version.getPath().equalsIgnoreCase("null") || TextUtils.isEmpty(version.getPath())) {
                                installNewVersion(version.getApkUrl());
                            } else
                                installNewVersionLocal(version.getPath());
                        }
                    }).show();
        } else {
            new AlertDialog.Builder(activity)
                    .setView(view)
                    .setCancelable(false)
                    .setTitle("Version Update")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            callback.success("Cancel Updates");
                        }
                    }).setPositiveButton("Install", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (version.getPath().equalsIgnoreCase("null") || TextUtils.isEmpty(version.getPath())) {
                        installNewVersion(version.getApkUrl());
                    } else
                        installNewVersionLocal(version.getPath());
                }
            }).show();
        }*/
    }

    private void installNewVersionLocal(String path) {
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setData(Uri.fromFile(new File(path)));
        activity.startActivity(intent);
    }

    public Proxy getServiceProxy() {
        return serviceProxy;
    }

    public void setServiceProxy(Proxy serviceProxy) {
        this.serviceProxy = serviceProxy;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

}
