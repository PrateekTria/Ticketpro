
package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FirebaseModel {

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    @SerializedName("deviceData")
    @Expose
    private DeviceData deviceData;

    @SerializedName("custId")
    @Expose
    private String custId;

    public FirebaseModel(String deviceId, DeviceData deviceData, String custId) {
        this.deviceId = deviceId;
        this.deviceData = deviceData;
        this.custId = custId;
    }

    public FirebaseModel() {

    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceData getDeviceData() {
        return deviceData;
    }

    public void setDeviceData(DeviceData deviceData) {
        this.deviceData = deviceData;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }
}
