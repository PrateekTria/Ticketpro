package com.ticketpro.vendors.cubtrack.cbt_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CubtracRequest {
    @SerializedName("zone")
    @Expose
    private String zone;
    @SerializedName("license_plate")
    @Expose
    private String licensePlate;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
