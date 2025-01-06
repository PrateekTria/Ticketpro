package com.ticketpro.parking.service;


import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.ticketpro.model.VersionUpdate;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.HomeActivity;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.io.File;
import java.util.Objects;

public class VersionUpdateDialogActivity extends Activity {

    private static final int APP_INSTALL_REQUEST = 0;
    private final String TAG = "VersionUpdateDialog";
    private VersionUpdate version;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.version_update);
        WebView wv = findViewById(R.id.webView);
        Button backBtn = findViewById(R.id.backBtn);
        dialog = new ProgressDialog(VersionUpdateDialogActivity.this);
        dialog.setMessage("Downloading...");
        String s = getIntent().getStringExtra("message");
        version = Objects.requireNonNull(getIntent().getBundleExtra("update")).getParcelable("versionUpdate");
        assert version != null;
        if (version.isForceInstall()) {
            backBtn.setVisibility(View.GONE);
        }
        wv.getSettings().setJavaScriptEnabled(true);
        if (version.getNotes_path() == null) {
            wv.loadData(s, "text/html", "utf-8");
        } else if (version.getNotes_path().equalsIgnoreCase("null")) {
            wv.loadData(s, "text/html", "utf-8");
        } else {
            File file = new File(version.getNotes_path());
            wv.loadUrl("file:///" + file);
        }
    }

    public void acceptAction(View view) {
        if (version.getPath() != null) {
            if (version.getPath().equalsIgnoreCase("null") || version.getPath().trim().isEmpty()) {
                installNewVersion(version.getApkUrl());
            } else {
                installNewVersionLocal(version.getPath());
            }
        } else {
            installNewVersion(version.getApkUrl());
        }

    }

    public void backAction(View view) {
        startActivity(new Intent(this, HomeActivity.class).putExtra("from", "vesionUpdate"));
    }

    @Override
    public void onBackPressed() {

    }

    private void installNewVersionLocal(String path) {
        try {
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
            Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", new File(path));
            intent.setData(uri);
            startActivityForResult(intent, APP_INSTALL_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    DownloadManager manager;

    private void installNewVersion(String ApkURL) {
        final String filePath = TPUtility.getDataFolder() + TPConstant.MODULE_NAME + "_Updates.APK";
        String downloadURL;

        // Backup Database
        ParkingDatabase.backupDatabase(TPApplication.getInstance());

        // Remove existing file
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        if (ApkURL.contains("http://") || ApkURL.contains("https://") || ApkURL.contains("ftp://")) {
            downloadURL = ApkURL;
        } else {
            downloadURL = TPConstant.UPDATE_URL + "/" + ApkURL;
        }
        manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(downloadURL);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Uri uri1 = Uri.parse("file://" + filePath);
        request.setDestinationUri(uri1);
        request.setTitle("ticketPRO Parking");
        request.setDescription("Downloading...");
        long id = manager.enqueue(request);
        dialog.show();
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                 if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Intent install = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                install.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                Uri uri = FileProvider.getUriForFile(VersionUpdateDialogActivity.this, VersionUpdateDialogActivity.this.getApplicationContext().getPackageName() + ".provider", new File(filePath));
                install.setData(uri);
                startActivityForResult(install, APP_INSTALL_REQUEST);
                unregisterReceiver(this);
            }
        };
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        /*DownloadFilesTask downloadTask = new DownloadFilesTask();
        downloadTask.setCallback(new CallbackHandler() {
            @Override
            public void success(String data) {
                Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                Uri uri = FileProvider.getUriForFile(VersionUpdateDialogActivity.this, VersionUpdateDialogActivity.this.getApplicationContext().getPackageName() + ".provider", new File(filePath));
                intent.setData(uri);
                startActivityForResult(intent, APP_INSTALL_REQUEST);
            }

            @Override
            public void failure(String message) {
                finish();
            }
        });

        downloadTask.execute(downloadURL, filePath);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == APP_INSTALL_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.e(TAG, "Package Installation Success");
            } else if (resultCode == RESULT_FIRST_USER) {
                Log.e(TAG, "Package Installation Cancelled by USER");
            } else {
                Log.e(TAG, "Something went wrong - INSTALLATION FAILED");
            }
        }
    }
}
