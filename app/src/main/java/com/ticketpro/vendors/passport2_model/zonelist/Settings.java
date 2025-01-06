
package com.ticketpro.vendors.passport2_model.zonelist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Settings {

    @SerializedName("allows_extensions")
    @Expose
    private Boolean allowsExtensions;
    @SerializedName("comp_time")
    @Expose
    private Integer compTime;
    @SerializedName("repark_lockout")
    @Expose
    private Integer reparkLockout;
    @SerializedName("allows_stops")
    @Expose
    private Boolean allowsStops;
    @SerializedName("grace_time")
    @Expose
    private Integer graceTime;
    @SerializedName("max_parking_time")
    @Expose
    private Integer maxParkingTime;

    public Boolean getAllowsExtensions() {
        return allowsExtensions;
    }

    public void setAllowsExtensions(Boolean allowsExtensions) {
        this.allowsExtensions = allowsExtensions;
    }

    public Integer getCompTime() {
        return compTime;
    }

    public void setCompTime(Integer compTime) {
        this.compTime = compTime;
    }

    public Integer getReparkLockout() {
        return reparkLockout;
    }

    public void setReparkLockout(Integer reparkLockout) {
        this.reparkLockout = reparkLockout;
    }

    public Boolean getAllowsStops() {
        return allowsStops;
    }

    public void setAllowsStops(Boolean allowsStops) {
        this.allowsStops = allowsStops;
    }

    public Integer getGraceTime() {
        return graceTime;
    }

    public void setGraceTime(Integer graceTime) {
        this.graceTime = graceTime;
    }

    public Integer getMaxParkingTime() {
        return maxParkingTime;
    }

    public void setMaxParkingTime(Integer maxParkingTime) {
        this.maxParkingTime = maxParkingTime;
    }

}
