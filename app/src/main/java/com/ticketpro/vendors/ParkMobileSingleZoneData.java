package com.ticketpro.vendors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParkMobileSingleZoneData {
    @SerializedName("resultTimeStampLocal")
    @Expose
    private String resultTimeStampLocal;
    @SerializedName("parkingRights")
    @Expose
    private List<ParkingRight> parkingRights;
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;
    @SerializedName("resultCount")
    @Expose
    private Integer resultCount;

    public String getResultTimeStampLocal() {
        return resultTimeStampLocal;
    }

    public void setResultTimeStampLocal(String resultTimeStampLocal) {
        this.resultTimeStampLocal = resultTimeStampLocal;
    }

    public List<ParkingRight> getParkingRights() {
        return parkingRights;
    }

    public void setParkingRights(List<ParkingRight> parkingRights) {
        this.parkingRights = parkingRights;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }
}
