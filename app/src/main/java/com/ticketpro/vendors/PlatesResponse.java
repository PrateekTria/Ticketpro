package com.ticketpro.vendors;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PlatesResponse {

    @SerializedName("plateValue")
    @Expose
    private String plateValue;
    @SerializedName("parkingEntitlements")
    @Expose
    private List<ParkingEntitlement> parkingEntitlements = null;
    @SerializedName("eligibilities")
    @Expose
    private List<Object> eligibilities = null;

    public String getPlateValue() {
        return plateValue;
    }

    public void setPlateValue(String plateValue) {
        this.plateValue = plateValue;
    }

    public List<ParkingEntitlement> getParkingEntitlements() {
        return parkingEntitlements;
    }

    public void setParkingEntitlements(List<ParkingEntitlement> parkingEntitlements) {
        this.parkingEntitlements = parkingEntitlements;
    }

    public List<Object> getEligibilities() {
        return eligibilities;
    }

    public void setEligibilities(List<Object> eligibilities) {
        this.eligibilities = eligibilities;
    }

}
