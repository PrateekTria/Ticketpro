package com.ticketpro.model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeterParams {

    @SerializedName("custId")
    @Expose
    private int custId;

    @SerializedName("meter")
    @Expose
    private String meter;

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

}
