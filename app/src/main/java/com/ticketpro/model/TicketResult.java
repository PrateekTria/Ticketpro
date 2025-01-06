package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rohit on 13-08-2020.
 */
public class TicketResult {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("success")
    @Expose
    private TicketSuccess success;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public TicketSuccess getSuccess() {
        return success;
    }

    public void setSuccess(TicketSuccess success) {
        this.success = success;
    }

}
