package com.ticketpro.syncbackup.synmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dbinfo_Param {

    @SerializedName("custid")
    @Expose
    private Integer custid;
    @SerializedName("userid")
    @Expose
    private Integer userid;
    @SerializedName("deviceid")
    @Expose
    private Integer deviceid;

    @SerializedName("module")
    @Expose
    private String module;

    @SerializedName("dbinfo")
    @Expose
    private List<Dbinfo> dbinfo;

    public Integer getCustid() {
        return custid;
    }

    public void setCustid(Integer custid) {
        this.custid = custid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Integer deviceid) {
        this.deviceid = deviceid;
    }

    public List<Dbinfo> getDbinfo() {
        return dbinfo;
    }

    public void setDbinfo(List<Dbinfo> dbinfo) {
        this.dbinfo = dbinfo;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
