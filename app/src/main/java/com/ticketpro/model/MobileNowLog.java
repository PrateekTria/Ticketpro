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

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Date;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "mobile_now_logs")
public class MobileNowLog {
    @PrimaryKey
    @ColumnInfo(name = "log_id")
    private int logId;
    @SerializedName("custid")
    @Expose
    @ColumnInfo(name = "custid")
    private int custId;
    @SerializedName("userid")
    @Expose
    @ColumnInfo(name = "userid")
    private int userId;
    @SerializedName("device_id")
    @Expose
    @ColumnInfo(name = "device_id")
    private int deviceId;
    @SerializedName("request_params")
    @Expose
    @ColumnInfo(name = "request_params")
    private String requestParams;
    @SerializedName("service_mode")
    @Expose
    @ColumnInfo(name = "service_mode")
    private String serviceMode;
    @SerializedName("response_text")
    @Expose
    @ColumnInfo(name = "response_text")
    private String responseText;
    @SerializedName("request_date")
    @Expose
    @ColumnInfo(name = "request_date")
    private Date requestDate;
    @SerializedName("plate_number")
    @Expose
    @ColumnInfo(name = "plate_number")
    private String plate_number;
    @SerializedName("latitude")
    @Expose
    @ColumnInfo(name = "latitude")
    private String latitude;
    @SerializedName("longitude")
    @Expose
    @ColumnInfo(name = "longitude")
    private String longitude;
    @SerializedName("location")
    @Expose
    @ColumnInfo(name = "location")
    private String location;
    @SerializedName("duty_id")
    @Expose
    @ColumnInfo(name = "duty_id")
    private String duty_id;


    public MobileNowLog() {

    }

    public static void insertMobileNowLog(MobileNowLog mobileNowLog) {
        ParkingDatabase.getInstance(TPApplication.getInstance()).mobileNowLogsDao()
                .insertMobileNowLog(mobileNowLog).subscribeOn(Schedulers.io())
                .subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(@NotNull Throwable e) {

            }
        });
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("custid", this.custId);
        values.put("userid", this.userId);
        values.put("device_id", this.deviceId);
        values.put("request_params", this.requestParams);
        values.put("service_mode", this.serviceMode);
        values.put("response_text", this.responseText);
        values.put("request_date", DateUtil.getSQLStringFromDate(this.requestDate));
        values.put("plate_number", this.plate_number);
        values.put("latitude", this.latitude);
        values.put("longitude", this.longitude);
        values.put("location", this.location);
        values.put("duty_id", this.duty_id);

        return values;
    }

    public JSONObject getJSONObject() {
        JSONObject values = new JSONObject();
        try {
            values.put("custid", this.custId);
            values.put("userid", this.userId);
            values.put("device_id", this.deviceId);
            values.put("request_params", this.requestParams);
            values.put("service_mode", this.serviceMode);
            values.put("response_text", this.responseText);
            values.put("request_date", DateUtil.getSQLStringFromDate(this.requestDate));
            values.put("plate_number", this.plate_number);
            values.put("latitude", this.latitude);
            values.put("longitude", this.longitude);
            values.put("location", this.location);
            values.put("duty_id", this.duty_id);
        } catch (Exception e) {
            Log.e("ChalkVehicle", "Error " + e.getMessage());
        }

        return values;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDuty_id() {
        return duty_id;
    }

    public void setDuty_id(String duty_id) {
        this.duty_id = duty_id;
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

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(String serviceMode) {
        this.serviceMode = serviceMode;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }


}
