package com.ticketpro.model.chalk_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChalkResult {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("serviceError")
    @Expose
    private String serviceError;
    @SerializedName("success")
    @Expose
    private ChalkSuccess success;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getServiceError() {
        return serviceError;
    }

    public void setServiceError(String serviceError) {
        this.serviceError = serviceError;
    }

    public ChalkSuccess getSuccess() {
        return success;
    }

    public void setSuccess(ChalkSuccess success) {
        this.success = success;
    }

}
