package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TicketCommentRequest {

    @SerializedName("violationId")
    @Expose
    private Integer violationId;

    @SerializedName("comments")
    @Expose
    private List<TicketComment> ticketComments = null;

    public int getViolationId() {
        return violationId;
    }

    public void setViolationId(int violationId) {
        this.violationId = violationId;
    }

    public List<TicketComment> getTicketCommment() {
        return ticketComments;
    }

    public void setTicketCommment(List<TicketComment> ticketComments) {
        this.ticketComments = ticketComments;
    }
}
