package com.ticketpro.parking.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPTask;

import io.reactivex.annotations.NonNull;

public class TPAsyncTask extends AsyncTask<TPTask, Void, Boolean> {
    private TPTask task;
    private Activity activity;
    private ProgressDialog progressDialog;
    private boolean showMessage;

    public TPAsyncTask(Activity activity) {
        this(activity, null, true, true);
    }

    public TPAsyncTask(Activity activity, String message) {
        this(activity, message, true, true);
    }

    public TPAsyncTask(Activity activity, String message, boolean cancellable) {
        this(activity, message, true, cancellable);
    }

    public TPAsyncTask(Activity activity, boolean showMessage) {
        this(activity, null, showMessage, true);
    }

    public TPAsyncTask(Activity activity, boolean showMessage, boolean cancellable) {
        this(activity, null, showMessage, cancellable);
    }

    public TPAsyncTask(Activity activity, String message, boolean showMessage, boolean cancellable) {
        this.activity = activity;
        this.showMessage = showMessage;
        if (StringUtil.isEmpty(message)) {
            message = "Processing...";
        }

        progressDialog = new ProgressDialog(this.activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);

        if (cancellable) {
            progressDialog.setCancelable(false);
            progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Cancel", (dialog, which) -> dialog.dismiss());
        }

        if (showMessage) {
            progressDialog.show();
        }
    }

    public void setTaskCallback(TPTask taskCallback) {
        this.task = taskCallback;
    }

    @Override
    protected Boolean doInBackground(@NonNull TPTask... params) {
        if (params.length > 0) {
            task = params[0];
            task.execute();

            if (task.asyncTask != null && !isCancelled()) {
                while (task.asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                    if (isCancelled()) {
                        break;
                    }
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean response) {
        try {
            if (showMessage && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (isCancelled()) {
                if (task != null) {
                    if (task.asyncTask != null) {
                        task.asyncTask.cancel(true);
                    }
                    task.cancel();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TPTask getTask() {
        return task;
    }

    public void setTask(TPTask task) {
        this.task = task;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    public boolean isShowMessage() {
        return showMessage;
    }

    public void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }
}
