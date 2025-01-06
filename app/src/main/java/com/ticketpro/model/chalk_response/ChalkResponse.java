package com.ticketpro.model.chalk_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChalkResponse {
    @SerializedName("result")
    @Expose
    private ChalkResult result;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("jsonrpc")
    @Expose
    private String jsonrpc;

    public ChalkResult getResult() {
        return result;
    }

    public void setResult(ChalkResult result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }
}
