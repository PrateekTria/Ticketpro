package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PatrollersResponse {


    /**
     * result : [{"record_Id":"2","custId":"60","patroller_Id":"a2835e4f-fea8-4ca8-bf09-dcfadd147f26","vehicleName":"Nissan","guid":"58b1ad36-ba8a-4499-9d4c-9b54ba358474","is_active":"Y","createdOn":"2024-09-25 01:48:57.960"}]
     * id : 82F85DB43CBF6
     * jsonrpc : 2.0
     */
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("jsonrpc")
    @Expose
    private String jsonrpc;
    /**
     * record_Id : 2
     * custId : 60
     * patroller_Id : a2835e4f-fea8-4ca8-bf09-dcfadd147f26
     * vehicleName : Nissan
     * guid : 58b1ad36-ba8a-4499-9d4c-9b54ba358474
     * is_active : Y
     * createdOn : 2024-09-25 01:48:57.960
     */
    @SerializedName("result")
    @Expose
    private List<GenetecPatrollers> result= null;

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

    public List<GenetecPatrollers> getResult() {
        return result;
    }

    public void setResult(List<GenetecPatrollers> result) {
        this.result = result;
    }


}
