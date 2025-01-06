package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PermitParams {
    @SerializedName("custId")
    @Expose
    private int custId;

    @SerializedName("permit")
    @Expose
    private String permit;
    @SerializedName("vin")
    @Expose
    private String vin;
    @SerializedName("state")
    @Expose
    private String state;

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getPermit() {
        return permit;
    }

    public void setPermit(String permit) {
        this.permit = permit;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
