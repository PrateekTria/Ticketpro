package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ParamsDeviceFeatures {
    @SerializedName("custId")
    @Expose
    private Integer custId;
    @SerializedName("userid")
    @Expose
    private Integer userId;
    @SerializedName("device_id")
    @Expose
    private Integer deviceId;
    @SerializedName("device")
    @Expose
    private String deviceName;

    @SerializedName("feature_name")
    @Expose
    private String featureName;

    @SerializedName("is_active")
    @Expose
    private String isActive;

    @SerializedName("admin")
    @Expose
    private String admin;

    @SerializedName("officer")
    @Expose
    private String officer;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("module_name")
    @Expose
    private String moduleName;

    @SerializedName("deviceFeaturesData")
    @Expose
    private ArrayList<DeviceFeatures> deviceFeaturesData;

    public ArrayList<DeviceFeatures> getDeviceFeaturesData() {
        return deviceFeaturesData;
    }

    public void setDeviceFeaturesData(ArrayList<DeviceFeatures> deviceFeaturesData) {
        this.deviceFeaturesData = deviceFeaturesData;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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
}
