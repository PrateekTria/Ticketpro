package com.ticketpro.syncbackup.synmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CSV_Params {

    @SerializedName("custid")
    @Expose
    int custid;

    @SerializedName("userid")
    @Expose
    int userid;

    @SerializedName("deviceid")
    @Expose
    int deviceid;

    @SerializedName("module")
    @Expose
    String module;

    @SerializedName("csvinfo")
    @Expose
    List<Csvinfo> csvinfo;


    public void setCustid(int custid) {
        this.custid = custid;
    }
    public int getCustid() {
        return custid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
    public int getUserid() {
        return userid;
    }

    public void setDeviceid(int deviceid) {
        this.deviceid = deviceid;
    }
    public int getDeviceid() {
        return deviceid;
    }

    public void setCsvinfo(List<Csvinfo> csvinfo) {
        this.csvinfo = csvinfo;
    }
    public List<Csvinfo> getCsvinfo() {
        return csvinfo;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
