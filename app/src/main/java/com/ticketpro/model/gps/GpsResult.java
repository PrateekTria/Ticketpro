package com.ticketpro.model.gps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GpsResult {
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("StreetNumber")
    @Expose
    private String streetNumber;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }
}
