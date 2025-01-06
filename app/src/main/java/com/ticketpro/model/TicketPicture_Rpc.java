package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketPicture_Rpc {

    @SerializedName("jsonrpc")
    @Expose
    private String jsonrpc = "2.0";
    @SerializedName("id")
    @Expose
    private String id = "82F85DB43CBF6";
    @SerializedName("params")
    @Expose
    private TicketPictureRequestParams params;
    @SerializedName("repeatOffenderParams")
    @Expose
    private RepeatOffenderParams repeatOffenderParams;
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

    public TicketPictureRequestParams getParams() {
        return params;
    }

    public void setParams(TicketPictureRequestParams params) {
        this.params = params;
    }

    public RepeatOffenderParams getRepeatOffenderParams() {
        return repeatOffenderParams;
    }

    public void setRepeatOffenderParams(RepeatOffenderParams repeatOffenderParams) {
        this.repeatOffenderParams = repeatOffenderParams;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
