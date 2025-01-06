package com.ticketpro.model.chalk_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChalkSuccess {
    @SerializedName("serviceMessage")
    @Expose
    private String serviceMessage;
    @SerializedName("apk_chalk_id")
    @Expose
    private Long apkChalkId;
    @SerializedName("result")
    @Expose
    private Boolean result;

    public String getServiceMessage() {
        return serviceMessage;
    }

    public void setServiceMessage(String serviceMessage) {
        this.serviceMessage = serviceMessage;
    }

    public Long getApkChalkId() {
        return apkChalkId;
    }

    public void setApkChalkId(Long apkChalkId) {
        this.apkChalkId = apkChalkId;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
