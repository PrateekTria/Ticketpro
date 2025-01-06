package com.ticketpro.vendors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParkMobileSpace {
    @SerializedName("resultTimeStampLocal")
    @Expose
    private String resultTimeStampLocal;
    @SerializedName("parkingRights")
    @Expose
    private List<ParkMobileSpaceData> parkingRights;
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

    public List<ParkMobileSpaceData> getParkingRights() {
        return parkingRights;
    }

    public void setParkingRights(List<ParkMobileSpaceData> parkingRights) {
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
