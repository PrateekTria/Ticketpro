package com.ticketpro.vendors;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParkMobileZoneList {

    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;
    @SerializedName("zones")
    @Expose
    private List<Zone> zones;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }

}