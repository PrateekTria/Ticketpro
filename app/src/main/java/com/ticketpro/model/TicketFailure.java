package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rohit on 13-08-2020.
 */
public class TicketFailure {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("ticketId")
    @Expose
    private String ticketId;
    @SerializedName("citationNumber")
    @Expose
    private Integer citationNumber;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getCitationNumber() {
        return citationNumber;
    }

    public void setCitationNumber(Integer citationNumber) {
        this.citationNumber = citationNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
