package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rohit on 19-06-2020.
 */
public class VendorItemResponse {
    @SerializedName("result")
    @Expose
    private List<VendorItem> result = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("jsonrpc")
    @Expose
    private String jsonrpc;

    public List<VendorItem> getResult() {
        return result;
    }

    public void setResult(List<VendorItem> result) {
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
