package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HotList_Rpc {
    @SerializedName("jsonrpc")
    @Expose
    private String jsonrpc = "2.0";
    @SerializedName("id")
    @Expose
    private String id = "82F85DB43CBF6";
    @SerializedName("params")
    @Expose
    private HotListRequest params;

    @SerializedName("hotlist")
    @Expose
    private HotListParams hotlist;
    @SerializedName("method")
    @Expose
    private String method;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HotListRequest getParams() {
        return params;
    }

    public void setParams(HotListRequest params) {
        this.params = params;
    }

    public HotListParams getHotlist() {
        return hotlist;
    }

    public void setHotlist(HotListParams hotlist) {
        this.hotlist = hotlist;
    }



    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
