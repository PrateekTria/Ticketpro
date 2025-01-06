package com.ticketpro.vendors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Zone {
    @SerializedName("signageZoneCode")
    @Expose
    private String signageZoneCode;
    @SerializedName("internalZoneCode")
    @Expose
    private String internalZoneCode;
    @SerializedName("supplierId")
    @Expose
    private Integer supplierId;
    @SerializedName("locationCode")
    @Expose
    private String locationCode;
    @SerializedName("description")
    @Expose
    private String description;

    public String getSignageZoneCode() {
        return signageZoneCode;
    }

    public void setSignageZoneCode(String signageZoneCode) {
        this.signageZoneCode = signageZoneCode;
    }

    public String getInternalZoneCode() {
        return internalZoneCode;
    }

    public void setInternalZoneCode(String internalZoneCode) {
        this.internalZoneCode = internalZoneCode;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
