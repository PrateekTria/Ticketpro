package com.ticketpro.model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepeatOffenderParams {


    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "plate")
    @SerializedName("plate")
    @Expose
    private String plate;
    @ColumnInfo(name = "violation_id")
    @SerializedName("violation_id")
    @Expose
    private int violationId;
    @ColumnInfo(name = "state_code")
    @SerializedName("state_code")
    @Expose
    private String stateCode;
    @ColumnInfo(name = "created_date")
    @SerializedName("created_date")
    @Expose
    private String creatTime;
    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public int getViolationId() {
        return violationId;
    }

    public void setViolationId(int violationId) {
        this.violationId = violationId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }
}
