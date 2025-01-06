package com.ticketpro.syncbackup.synmodels;

import com.google.gson.annotations.SerializedName;

public class CSVinfo_Json_rpc {
    @SerializedName("id")
    String id;

    @SerializedName("method")
    String method;

    @SerializedName("params")
    CSV_Params params;

    @SerializedName("jsonrpc")
    String jsonrpc;


    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    public String getMethod() {
        return method;
    }

    public void setParams(CSV_Params params) {
        this.params = params;
    }
    public CSV_Params getParams() {
        return params;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }
    public String getJsonrpc() {
        return jsonrpc;
    }
}
