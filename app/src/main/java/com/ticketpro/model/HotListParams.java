package com.ticketpro.model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class HotListParams {


    @SerializedName("custid")
    @Expose
    private int custId;

    @SerializedName("state_code")
    @Expose
    private String stateCode;

    @SerializedName("plate")
    @Expose
    private String plate;

    @SerializedName("plate_type")
    @Expose
    private String plateType;

    @SerializedName("begin_date")
    @Expose
    private String beginDate;

    @SerializedName("end_date")
    @Expose
    private String endDate;

    @SerializedName("comments")
    @Expose
    private String comments;

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
