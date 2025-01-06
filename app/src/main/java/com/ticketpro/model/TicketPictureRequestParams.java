package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketPictureRequestParams {
    @SerializedName("custId")
    @Expose
    private int custId;

    @SerializedName("citationNumber")
    @Expose
    private int citationNumber;

    @SerializedName("ticketPicture")
    @Expose
    private  TicketPicture ticketPicture;

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getCitationNumber() {
        return citationNumber;
    }

    public void setCitationNumber(int citationNumber) {
        this.citationNumber = citationNumber;
    }

    public TicketPicture getticketPicture() {
        return ticketPicture;
    }

    public void setticketPicture(TicketPicture ticketPicture) {
        this.ticketPicture = ticketPicture;
    }
}
