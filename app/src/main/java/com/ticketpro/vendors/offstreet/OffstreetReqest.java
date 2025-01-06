package com.ticketpro.vendors.offstreet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * **********************************************************
 * File: OffstreetReqest.java
 * Created by: tspl
 * Date: 11/4/24
 * Description:
 * **********************************************************
 */

public class OffstreetReqest {
    @SerializedName("locationId")
    @Expose
    private String locationId;
    @SerializedName("plate")
    @Expose
    private String plate;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }
}
