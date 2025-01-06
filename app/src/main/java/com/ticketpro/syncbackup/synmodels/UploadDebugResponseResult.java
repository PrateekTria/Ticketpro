package com.ticketpro.syncbackup.synmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadDebugResponseResult {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("msg")
    @Expose
    private String message;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
