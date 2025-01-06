package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EulaAcceptanceResult {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("EulaAcceptedByCust")
    @Expose
    private String eulaAcceptedByCust;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getEulaAcceptedByCust() {
        return eulaAcceptedByCust;
    }

    public void setEulaAcceptedByCust(String eulaAcceptedByCust) {
        this.eulaAcceptedByCust = eulaAcceptedByCust;
    }


}
