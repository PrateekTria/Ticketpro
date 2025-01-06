package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EulaAcceptanceParams {

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("eula_id")
    @Expose
    private Integer eulaId;
    @SerializedName("is_accepted")
    @Expose
    private String isAccepted;
    @SerializedName("custId")
    @Expose
    private Integer custId;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getEulaId() {
        return eulaId;
    }

    public void setEulaId(Integer eulaId) {
        this.eulaId = eulaId;
    }

    public String getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(String isAccepted) {
        this.isAccepted = isAccepted;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }






}
