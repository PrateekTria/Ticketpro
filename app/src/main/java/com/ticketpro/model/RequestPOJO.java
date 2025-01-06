package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rohit on 15-06-2020.
 */
public class RequestPOJO {
    @SerializedName("jsonrpc")
    @Expose
    private String jsonrpc = "2.0";
    @SerializedName("id")
    @Expose
    private String id = "82F85DB43CBF6";
    @SerializedName("params")
    @Expose
    private Params params;
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

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
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
