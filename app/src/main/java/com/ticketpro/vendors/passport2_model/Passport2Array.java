package com.ticketpro.vendors.passport2_model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class Passport2Array {
    @SerializedName("data")
    @Expose
    private List<Passport2Data> data;

    public List<Passport2Data> getData() {
        return data;
    }

    public void setData(List<Passport2Data> data) {
        this.data = data;
    }
}
