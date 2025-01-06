package com.ticketpro.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "device_features")
public class DeviceFeatures implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "device_features_id")
    @SerializedName("device_features_id")
    @Expose
    private int deviceFeaturesId;
    @ColumnInfo(name = "userid")
    @SerializedName("userid")
    @Expose
    private int userId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "device_id")
    @SerializedName("device_id")
    @Expose
    private int deviceId;

    @ColumnInfo(name = "device")
    @SerializedName("device")
    @Expose
    private String device;

    @ColumnInfo(name = "feature_name")
    @SerializedName("feature_name")
    @Expose
    private String featureName;

    @ColumnInfo(name = "is_active")
    @SerializedName("is_active")
    @Expose
    private String isActive;

    @ColumnInfo(name = "admin")
    @SerializedName("admin")
    @Expose
    private String admin;
    @ColumnInfo(name = "officer")
    @SerializedName("officer")
    @Expose
    private String officer;

    @ColumnInfo(name = "value")
    @SerializedName("value")
    @Expose
    private String value;

    @ColumnInfo(name = "module_name")
    @SerializedName("module_name")
    @Expose
    private String moduleName;

    @ColumnInfo(name = "created_date")
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    @ColumnInfo(name = "updated_date")
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;

    //Insert data in db

    //update data in db




    public int getDeviceFeaturesId() {
        return deviceFeaturesId;
    }

    public void setDeviceFeaturesId(int deviceFeaturesId) {
        this.deviceFeaturesId = deviceFeaturesId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getOfficer() {
        return officer;
    }

    public void setOfficer(String officer) {
        this.officer = officer;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}
