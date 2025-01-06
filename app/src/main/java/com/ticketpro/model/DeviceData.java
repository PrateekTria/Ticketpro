package com.ticketpro.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.TPConstant;

import java.util.ArrayList;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "FT_DeviceHistory")
public class DeviceData {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "dutyId")
    @SerializedName("DutyId")
    @Expose
    private String dutyId;
    @ColumnInfo(name = "dutyName")
    @SerializedName("DutyName")
    @Expose
    private String dutyName;
    @ColumnInfo(name = "custId")
    @SerializedName("CustId")
    @Expose
    private String custId;
    @ColumnInfo(name = "deviceId")
    @SerializedName("DeviceId")
    @Expose
    private String deviceId;
    @ColumnInfo(name = "deviceName")
    @SerializedName("DeviceName")
    @Expose
    private String deviceName;
    @ColumnInfo(name = "latitude")
    @SerializedName("Lattitude")
    @Expose
    private double lattitude;
    @ColumnInfo(name = "longitude")
    @SerializedName("Longitude")
    @Expose
    private double longitude;
    @ColumnInfo(name = "name")
    @SerializedName("Name")
    @Expose
    private String name;
    @ColumnInfo(name = "fullName")
    @SerializedName("FullName")
    @Expose
    private String fullName;
    @ColumnInfo(name = "device")
    @SerializedName("Device")
    @Expose
    private String device;
    @ColumnInfo(name = "timeStamp")
    @SerializedName("TimeStamp")
    @Expose
    private String timeStamp;
    @ColumnInfo(name = "badge")
    @SerializedName("Badge")
    @Expose
    private String badge;
    @ColumnInfo(name = "isActive")
    @SerializedName("IsActive")
    @Expose
    private boolean isActive;
    @ColumnInfo(name = "userId")
    @SerializedName("UserId")
    @Expose
    private String userId;
    @ColumnInfo(name = "firstLogin")
    @SerializedName("FirstLogin")
    @Expose
    private String firstLogin;
    @ColumnInfo(name = "lastTicketTimeStamp")
    @SerializedName("lastTicketTimeStamp")
    @Expose
    private String lastTicketTimeStamp;
    @ColumnInfo(name = "currTimeStamp")
    @SerializedName("currentTicketTimeStamp")
    @Expose
    private String currTimeStamp;
    @ColumnInfo(name = "pushToken")
    @SerializedName("pushToken")
    @Expose
    private String pushToken;
    @ColumnInfo(name = "moduleId")
    @SerializedName("moduleId")
    @Expose
    private String moduleId;
    @ColumnInfo(name = "deviceInactivity")
    @SerializedName("deviceInactivity")
    @Expose
    private boolean deviceInactivity;
    @ColumnInfo(name = "isLoggedIn")
    @SerializedName("isLoggedIn")
    @Expose
    private boolean isLoggedIn;
    @ColumnInfo(name = "appVersion")
    @SerializedName("appVersion")
    @Expose
    private String appVersion;
    @ColumnInfo(name = "address")
    @SerializedName("address")
    @Expose
    private String address;
    @ColumnInfo(name = "activityName")
    @SerializedName("activityName")
    @Expose
    private String activityName;
    @ColumnInfo(name = "sync_status")
    private String sync_status;
    @ColumnInfo(name = "altitude")
    private double altitude;
    @SerializedName("Violation")
    @Expose
    @ColumnInfo(name = "Violation")
    private String Violation;
    @SerializedName("Citation")
    @Expose
    @ColumnInfo(name = "Citation")
    private String Citation;
    @SerializedName("accuracy")
    @Expose
    @ColumnInfo(name = "accuracy")
    private double accuracy;

    public DeviceData() {

    }

    public static void deleteRecord(long id) {
        try {
            ParkingDatabase.getInstance(TPApplication.getInstance()).ftDeviceHistoryDao().deleteRecord(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("CheckResult")
    public static void insertDeviceData(DeviceData DeviceData, SharedPreferences sharedPreferences) {
        ParkingDatabase.getInstance(TPApplication.getInstance()).ftDeviceHistoryDao().insertDeviceData(DeviceData).subscribeOn(Schedulers.io()).subscribeWith(new SingleObserver<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Long aLong) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong(TPConstant.lastInsertedDeviceDataID, aLong);
                editor.apply();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getDutyId() {
        return dutyId;
    }

    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(String firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getLastTicketTimeStamp() {
        return lastTicketTimeStamp;
    }

    public void setLastTicketTimeStamp(String lastTicketTimeStamp) {
        this.lastTicketTimeStamp = lastTicketTimeStamp;
    }

    public String getCurrTimeStamp() {
        return currTimeStamp;
    }

    public void setCurrTimeStamp(String currTimeStamp) {
        this.currTimeStamp = currTimeStamp;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public boolean isDeviceInactivity() {
        return deviceInactivity;
    }

    public void setDeviceInactivity(boolean deviceInactivity) {
        this.deviceInactivity = deviceInactivity;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public ContentValues getContentValues() throws Exception {

        ContentValues values = new ContentValues();
        values.put("dutyId", this.dutyId);
        values.put("custId", this.custId);
        values.put("dutyName", this.dutyName);
        values.put("deviceId", this.deviceId);
        values.put("deviceName", this.deviceName);
        values.put("latitude", this.lattitude);
        values.put("longitude", this.longitude);
        values.put("name", this.name);
        values.put("fullName", this.fullName);
        values.put("device", this.device);
        values.put("timeStamp", this.timeStamp);
        values.put("badge", this.badge);
        values.put("isActive", this.isActive);
        values.put("userid", this.userId);
        values.put("firstLogin", this.firstLogin);
        values.put("lastTicketTimeStamp", this.lastTicketTimeStamp);
        values.put("currTimeStamp", this.currTimeStamp);
        values.put("pushToken", this.pushToken);
        values.put("moduleId", this.moduleId);
        values.put("deviceInactivity", this.deviceInactivity);
        values.put("isLoggedIn", this.isLoggedIn);
        values.put("appVersion", this.appVersion);
        values.put("address", this.address);
        values.put("sync_status", this.sync_status);
        values.put("activityName", this.activityName);
        values.put("accuracy", this.accuracy);
        return values;
    }

    public String getSync_status() {
        return sync_status;
    }

    public void setSync_status(String sync_status) {
        this.sync_status = sync_status;
    }

    public ArrayList<DeviceData> getPendingLocationUpdates(Context mContext) {
        ArrayList<DeviceData> deviceDataArrayList;
        deviceDataArrayList = (ArrayList<DeviceData>) ParkingDatabase.getInstance(TPApplication.getInstance()).ftDeviceHistoryDao().getPendingLocationUpdates();
        return deviceDataArrayList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getCitation() {
        return Citation;
    }

    public void setCitation(String citation) {
        Citation = citation;
    }

    public String getViolation() {
        return Violation;
    }

    public void setViolation(String violation) {
        Violation = violation;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
}
