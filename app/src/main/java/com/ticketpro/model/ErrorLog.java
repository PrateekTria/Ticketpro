package com.ticketpro.model;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.DateUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "error_logs")
public class ErrorLog {
    @PrimaryKey
    @ColumnInfo(name = "error_id")
    private int errorId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "userid")
    @SerializedName("userid")
    @Expose
    private int userId;
    @ColumnInfo(name = "device_id")
    @SerializedName("device_id")
    @Expose
    private int deviceId;
    @ColumnInfo(name = "error_type")
    @SerializedName("error_type")
    @Expose
    private String errorType;
    @ColumnInfo(name = "error_desc")
    @SerializedName("error_desc")
    @Expose
    private String errorDesc;
    @ColumnInfo(name = "error_date")
    private Date errorDate;
    @SerializedName("error_date")
    @Expose
    private String date;
    @ColumnInfo(name = "module")
    @SerializedName("module")
    @Expose
    private String module;
    @ColumnInfo(name = "app_version")
    @SerializedName("app_version")
    @Expose
    private String app_version;
    @ColumnInfo(name = "device")
    @SerializedName("device")
    @Expose
    private String device;

    public ErrorLog() {

    }

    public static ArrayList<ErrorLog> getErrorLogs() throws Exception {
        ArrayList<ErrorLog> list = new ArrayList<ErrorLog>();
        list = (ArrayList<ErrorLog>) ParkingDatabase.getInstance(TPApplication.getInstance()).errorLogsDao().getErrorLogs();
        return list;
    }

    public static int getNextPrimaryId() throws Exception {
        int nextId = 0;
        nextId = ParkingDatabase.getInstance(TPApplication.getInstance()).errorLogsDao().getNextPrimaryId();
        return nextId + 1;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).errorLogsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).errorLogsDao().removeById(id);
    }

    public static void insertErrorLog(ErrorLog ErrorLog) {
        ParkingDatabase.getInstance(TPApplication.getInstance()).errorLogsDao().insertErrorLog(ErrorLog).subscribeOn(Schedulers.io()).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
        //new ErrorLog.InsertErrorLog(ErrorLog).execute();
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("custid", this.custId);
        values.put("userid", this.userId);
        values.put("device_id", this.deviceId);
        values.put("error_type", this.errorType);
        values.put("error_desc", this.errorDesc);
        values.put("error_date", DateUtil.getSQLStringFromDate(this.errorDate));
        return values;
    }

    public JSONObject getJSONObject() {
        JSONObject values = new JSONObject();
        try {
            values.put("custid", this.custId);
            values.put("userid", this.userId);
            values.put("device_id", this.deviceId);
            values.put("error_type", this.errorType);
            values.put("error_desc", this.errorDesc);
            values.put("error_date", DateUtil.getSQLStringFromDate(this.errorDate));
            values.put("app_version", this.app_version);
            values.put("device", this.device);
            values.put("module", this.module);
        } catch (Exception e) {
            Log.e("ChalkVehicle", "Error " + e.getMessage());
        }

        return values;
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public Date getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(Date errorDate) {
        this.errorDate = errorDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    private static class InsertErrorLog extends AsyncTask<Void, Void, Void> {
        private ErrorLog ErrorLog;

        public InsertErrorLog(ErrorLog ErrorLog) {
            this.ErrorLog = ErrorLog;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).errorLogsDao().insertErrorLog(ErrorLog);
            return null;
        }
    }

}
