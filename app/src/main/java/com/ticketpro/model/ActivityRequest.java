package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.ArrayList;

public class ActivityRequest {

    @SerializedName("activityReports")
    @Expose
    private JSONArray jsonArray;

    @SerializedName("hotlist")
    @Expose
    private ArrayList<Hotlist> hotlist;

    @SerializedName("dutyReports")
    @Expose
    private DutyReport dutyReport;

    @SerializedName("activity")
    @Expose
    private JSONArray jsonArrayList;


    public JSONArray getActivityRequest() {
        return jsonArray;
    }

    public void setActivityRequest(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public ArrayList<Hotlist> gethotlistRequest() {
        return hotlist;
    }

    public void sethotlistRequest(ArrayList<Hotlist> hotlist) {
        this.hotlist = hotlist;
    }
    public DutyReport getDutyReport() {
        return dutyReport;
    }

    public void setDutyReport(DutyReport dutyReport) {
        this.dutyReport = dutyReport;
    }

    public JSONArray getActivityRequestList() {
        return jsonArrayList;
    }

    public void setActivityRequestList(JSONArray jsonArrayList) {
        this.jsonArrayList = jsonArrayList;
    }
}
