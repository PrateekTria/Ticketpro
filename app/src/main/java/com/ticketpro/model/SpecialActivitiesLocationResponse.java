package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rohit on 03-09-2020.
 */
public class SpecialActivitiesLocationResponse {
    @SerializedName("result")
    @Expose
    private List<SpecialActivitiesLocation> result = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("jsonrpc")
    @Expose
    private String jsonrpc;

    public List<SpecialActivitiesLocation> getResult() {
        return result;
    }

    public void setResult(List<SpecialActivitiesLocation> result) {
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