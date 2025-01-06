package com.ticketpro.model;

import android.content.ContentValues;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.exception.TPException;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPUtility;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ticketpro.util.TPConstant.TAG;

@Entity(tableName = "devices")
public class DeviceInfo implements Serializable {
    @Ignore
    private static final long serialVersionUID = 1L;
    @Ignore
    static Logger log = Logger.getLogger("DeviceInfo");
    @PrimaryKey
    @ColumnInfo(name = "device_id")
    @SerializedName("device_id")
    @Expose
    private int deviceId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "device_name")
    @SerializedName("device_name")
    @Expose
    private String deviceName;
    @ColumnInfo(name = "device")
    @SerializedName("device")
    @Expose
    private String device;
    @ColumnInfo(name = "app_version")
    @SerializedName("app_version")
    @Expose
    private String appVersion;
    @ColumnInfo(name = "os_version")
    @SerializedName("os_version")
    @Expose
    private String osVersion;
    @ColumnInfo(name = "platform")
    @SerializedName("platform")
    @Expose
    private String platform;
    @ColumnInfo(name = "last_sync")
    @SerializedName("last_sync")
    @Expose
    private Date lastSync;
    @ColumnInfo(name = "lastTicketTime")
    private String lastTicketTime;
    @ColumnInfo(name = "start_citation_number")
    @SerializedName("start_citation_number")
    @Expose
    private long startCitationNumber;
    @ColumnInfo(name = "current_citation_number")
    @SerializedName("current_citation_number")
    @Expose
    private long currentCitationNumber;
    @ColumnInfo(name = "end_citation_number")
    @SerializedName("end_citation_number")
    @Expose
    private long endCitationNumber;
    @ColumnInfo(name = "start_warning_number")
    @SerializedName("start_warning_number")
    @Expose
    private long startWarningNumber;
    @ColumnInfo(name = "current_warning_number")
    @SerializedName("current_warning_number")
    @Expose
    private long currentWarningNumber;
    @ColumnInfo(name = "end_warning_number")
    @SerializedName("end_warning_number")
    @Expose
    private long endWarningNumber;
    @ColumnInfo(name = "start_photo_number")
    @SerializedName("start_photo_number")
    @Expose
    private long startPhotoNumber;
    @ColumnInfo(name = "current_photo_number")
    @SerializedName("current_photo_number")
    @Expose
    private long currentPhotoNumber;
    @ColumnInfo(name = "end_photo_number")
    @SerializedName("end_photo_number")
    @Expose
    private long endPhotoNumber;
    @ColumnInfo(name = "default_template_id")
    @SerializedName("default_template_id")
    @Expose
    private int defaultTemplateId;
    @ColumnInfo(name = "gcm_registration_id")
    @SerializedName("gcm_registration_id")
    @Expose
    private String GCMRegistrationId;
    @ColumnInfo(name = "default_printer_name")
    @SerializedName("default_printer_name")
    @Expose
    private String defaultPrinterName;

    public DeviceInfo() {

    }

    public DeviceInfo(JSONObject object) throws Exception {
        this.setCustId(object.getInt("custid"));
        this.setDeviceId(object.getInt("device_id"));
        this.setDeviceName(object.getString("device_name"));
        this.setDevice(object.getString("device"));
        this.setAppVersion(object.getString("app_version"));
        this.setOsVersion(object.optString("os_version"));
        this.setPlatform(object.getString("platform"));
        this.setLastSync(DateUtil.getDateFromSQLString(object.getString("last_sync")));
        this.setStartCitationNumber(!object.isNull("start_citation_number") ? object.getLong("start_citation_number") : 0);
        this.setCurrentCitationNumber(!object.isNull("current_citation_number") ? object.getLong("current_citation_number") : 0);
        this.setEndCitationNumber(!object.isNull("end_citation_number") ? object.getLong("end_citation_number") : 0);
        this.setStartWarningNumber(!object.isNull("start_warning_number") ? object.getLong("start_warning_number") : 0);
        this.setCurrentWarningNumber(!object.isNull("current_warning_number") ? object.getLong("current_warning_number") : 0);
        this.setEndWarningNumber(!object.isNull("end_warning_number") ? object.getLong("end_warning_number") : 0);
        this.setStartPhotoNumber(!object.isNull("start_photo_number") ? object.getLong("start_photo_number") : 0);
        this.setCurrentPhotoNumber(!object.isNull("current_photo_number") ? object.getLong("current_photo_number") : 0);
        this.setEndPhotoNumber(!object.isNull("end_photo_number") ? object.getLong("end_photo_number") : 0);
        this.setDefaultTemplateId(!object.isNull("default_template_id") ? object.getInt("default_template_id") : 0);
        this.setGCMRegistrationId(!object.isNull("gcm_registration_id") ? object.getString("gcm_registration_id") : null);

        this.setDefaultPrinterName(!object.isNull("default_printer_name") ? object.getString("default_printer_name") : null);
    }

    public static DeviceInfo getDeviceInfo() {

        String deviceName = TPApplication.getInstance().getDeviceName();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo = ParkingDatabase.getInstance(TPApplication.getInstance()).devicesDao().getDeviceInfo(deviceName);

        return deviceInfo;
    }

    public static DeviceInfo getDeviceInfo(String deviceName) throws TPException {
        try {
            return ParkingDatabase.getInstance(TPApplication.getInstance()).devicesDao().getDeviceInfo(deviceName);
        } catch (Exception e) {
            Log.e("DeviceInfo", "Error " + e.getMessage());
            TPException appEx = new TPException();
            appEx.setErrorMessage("Unable to get device info from local database.");
            throw appEx;
        }
    }

    public static void updateLastTicketTime(int deviceId, String timeStamp) throws TPException {
        ParkingDatabase.getInstance(TPApplication.getInstance()).devicesDao().updateLastTicketTime(deviceId, timeStamp);
    }

    public static String getLastTicketTime(int deviceId) throws TPException {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).devicesDao().getLastTicketTime(deviceId);
    }

    public static DeviceInfo getDeviceInfoById(int deviceId) throws TPException {
        try {
            return ParkingDatabase.getInstance(TPApplication.getInstance()).devicesDao().getDeviceInfoById(deviceId);

        } catch (Exception e) {
            Log.e("DeviceInfo", "Error " + e.getMessage());

            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Unable to get device info from local database.");
            throw appEx;
        }
    }

    public static ArrayList<DeviceInfo> getDevices() throws TPException {
        try {
            return (ArrayList<DeviceInfo>) ParkingDatabase.getInstance(TPApplication.getInstance()).devicesDao().getDevices();
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Unable to get device info from local database.");
            throw appEx;
        }
    }

    public static void insertDeviceInfo(final DeviceInfo DeviceInfo) {
        final ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());
        instance.devicesDao().insertDeviceInfo(DeviceInfo).subscribeOn(Schedulers.io()).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.i(TAG, "updateDeviceInfo: Device data updated successfully");
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
        //new DeviceInfo.InsertDeviceInfo(DeviceInfo).execute();
    }

    public String getOsVersion() {
        return osVersion;
    }
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        try {
            object.put("custid", this.custId);
            object.put("device_id", this.deviceId);
            object.put("device_name", this.deviceName);
            object.put("device1", this.device);
            object.put("app_version", this.appVersion);
            object.put("os_version", this.osVersion);
            object.put("platform", this.platform);
            object.put("last_sync", DateUtil.getSQLStringFromDate2(this.lastSync));
            object.put("start_citation_number", this.startCitationNumber);
            object.put("current_citation_number", this.currentCitationNumber);
            object.put("end_citation_number", this.endCitationNumber);
            object.put("start_warning_number", this.startWarningNumber);
            object.put("current_warning_number", this.currentWarningNumber);
            object.put("end_warning_number", this.endWarningNumber);
            object.put("start_photo_number", this.startPhotoNumber);
            object.put("current_photo_number", this.currentPhotoNumber);
            object.put("end_photo_number", this.endPhotoNumber);
            object.put("gcm_registration_id", this.GCMRegistrationId);
            object.put("default_template_id", this.defaultTemplateId);
            object.put("default_printer_name", this.defaultPrinterName);
            object.put("lastTicketTime", this.lastTicketTime);

        } catch (Exception e) {
            Log.e("DeviceInfo", "Error " + e.getMessage());
        }

        return object;
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("custid", this.custId);
        values.put("device_id", this.deviceId);
        values.put("device_name", this.deviceName);
        values.put("device1", this.device);
        values.put("app_version", this.appVersion);
        values.put("os_version", this.osVersion);
        values.put("platform", this.platform);
        values.put("last_sync", DateUtil.getSQLStringFromDate2(this.lastSync));
        values.put("start_citation_number", this.startCitationNumber);
        values.put("current_citation_number", this.currentCitationNumber);
        values.put("end_citation_number", this.endCitationNumber);
        values.put("start_warning_number", this.startWarningNumber);
        values.put("current_warning_number", this.currentWarningNumber);
        values.put("end_warning_number", this.endWarningNumber);
        values.put("start_photo_number", this.startPhotoNumber);
        values.put("current_photo_number", this.currentPhotoNumber);
        values.put("end_photo_number", this.endPhotoNumber);
        values.put("gcm_registration_id", this.GCMRegistrationId);
        values.put("default_template_id", this.defaultTemplateId);
        values.put("default_printer_name", this.defaultPrinterName);
        values.put("lastTicketTime", this.lastTicketTime);

        return values;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Date getLastSync() {
        return lastSync;
    }

    public void setLastSync(Date lastSync) {
        this.lastSync = lastSync;
    }

    public long getStartCitationNumber() {
        return startCitationNumber;
    }

    public void setStartCitationNumber(long startCitationNumber) {
        this.startCitationNumber = startCitationNumber;
    }

    public long getCurrentCitationNumber() {
        return currentCitationNumber;
    }

    public void setCurrentCitationNumber(long currentCitationNumber) {
        this.currentCitationNumber = currentCitationNumber;
    }

    public long getEndCitationNumber() {
        return endCitationNumber;
    }

    public void setEndCitationNumber(long endCitationNumber) {
        this.endCitationNumber = endCitationNumber;
    }

    public long getStartWarningNumber() {
        return startWarningNumber;
    }

    public void setStartWarningNumber(long startWarningNumber) {
        this.startWarningNumber = startWarningNumber;
    }

    public long getCurrentWarningNumber() {
        return currentWarningNumber;
    }

    public void setCurrentWarningNumber(long currentWarningNumber) {
        this.currentWarningNumber = currentWarningNumber;
    }

    public long getEndWarningNumber() {
        return endWarningNumber;
    }

    public void setEndWarningNumber(long endWarningNumber) {
        this.endWarningNumber = endWarningNumber;
    }

    public long getStartPhotoNumber() {
        return startPhotoNumber;
    }

    public void setStartPhotoNumber(long startPhotoNumber) {
        this.startPhotoNumber = startPhotoNumber;
    }

    public long getCurrentPhotoNumber() {
        return currentPhotoNumber;
    }

    public void setCurrentPhotoNumber(long currentPhotoNumber) {
        this.currentPhotoNumber = currentPhotoNumber;
    }

    public long getEndPhotoNumber() {
        return endPhotoNumber;
    }

    public void setEndPhotoNumber(long endPhotoNumber) {
        this.endPhotoNumber = endPhotoNumber;
    }

    public int getDefaultTemplateId() {
        return defaultTemplateId;
    }

    public void setDefaultTemplateId(int defaultTemplateId) {
        this.defaultTemplateId = defaultTemplateId;
    }

    public String getGCMRegistrationId() {
        return GCMRegistrationId;
    }

    public void setGCMRegistrationId(String gCMRegistrationId) {
        GCMRegistrationId = gCMRegistrationId;
    }

    public String getDefaultPrinterName() {
        return defaultPrinterName;
    }

    public void setDefaultPrinterName(String defaultPrinterName) {
        this.defaultPrinterName = defaultPrinterName;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getLastTicketTime() {
        return lastTicketTime;
    }

    public void setLastTicketTime(String lastTicketTime) {
        this.lastTicketTime = lastTicketTime;
    }

}
