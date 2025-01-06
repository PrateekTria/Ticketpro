package com.ticketpro.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Button;

import com.ticketpro.model.Feature;
import com.ticketpro.parking.R;

import java.io.PrintWriter;
import java.io.StringWriter;

public class UIHelper {

    ProgressDialog loadingDialog;
    Activity activity;

    public UIHelper(Activity activity) {
        this.activity = activity;
    }

    public static void toggleButtonState(Button button, boolean enabled, int enableDrawable) {
        if (button == null) {
            return;
        }

        if (enabled) {
            button.setEnabled(true);
            button.setBackgroundResource(enableDrawable);
        } else {
            button.setEnabled(false);
            button.setBackgroundResource(R.drawable.btn_disabled);
        }
    }

    public static synchronized String getPrintStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));

        return sw.toString();
    }

    public static void toggleButtonState(Button button, boolean enabled) {
        toggleButtonState(button, enabled, R.drawable.btn_yellow);
    }

    public static boolean isGpsDeviceValue(int device) {
        boolean b = false;
        String featureValue = Feature.getFeatureValue(Feature.GPS);
        if (featureValue != null && !TextUtils.isEmpty(featureValue)) {
            String[] split = featureValue.split(",");
            for (int i = 0; i < split.length; i++) {
                if (String.valueOf(device).equals(split[i])) {
                    b = true;
                    break;
                }
            }
        }
        return b;
    }

    public void showLoadingDialog(final String message) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    loadingDialog = ProgressDialog.show(activity, "", message, true, false);
                }
            });
        }
    }

    public void updateLoadingDialog(final String message) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    loadingDialog.setMessage(message);
                }
            });
        }
    }

    public boolean isDialogActive() {
        if (loadingDialog != null) {
            return loadingDialog.isShowing();
        } else {
            return false;
        }
    }

    public void dismissLoadingDialog() {
        if (activity != null && loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public void showErrorDialog(String errorMessage) {
        new AlertDialog.Builder(activity).setMessage(errorMessage).setTitle("Error").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }).create().show();
    }

    public void showErrorDialogOnGuiThread(final String errorMessage) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    new AlertDialog.Builder(activity).setMessage(errorMessage).setTitle("Error").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            dismissLoadingDialog();
                        }
                    }).create().show();
                }
            });
        }
    }
}
