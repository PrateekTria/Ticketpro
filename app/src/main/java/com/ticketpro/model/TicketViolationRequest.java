package com.ticketpro.model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketViolationRequest {

    @SerializedName("custId")
    @Expose
    private int custId;


    @SerializedName("citationNumber")
    @Expose
    private int citationNumber;


    public int getCitationNumber() {
        return citationNumber;
    }

    public void setCitationNumberId(int citationNumber) {
        this.citationNumber = citationNumber;
    }

    public int getCustId() {
        return custId;
    }


    public void setCustId(int custId) {
        this.custId = custId;
    }
}
