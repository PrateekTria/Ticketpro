package com.ticketpro.model.gps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GpsResponse {
    @SerializedName("result")
    @Expose
    private GpsResult result;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("jsonrpc")
    @Expose
    private String jsonrpc;

    public GpsResult getResult() {
        return result;
    }

    public void setResult(GpsResult result) {
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
