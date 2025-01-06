package com.ticketpro.vendors.passport2_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlateResponse {
    @SerializedName("data")
    @Expose
    private List<PP2Plate> data;

    public List<PP2Plate> getData() {
        return data;
    }

    public void setData(List<PP2Plate> data) {
        this.data = data;
    }
}
