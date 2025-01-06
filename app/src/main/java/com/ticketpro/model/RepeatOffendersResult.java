package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepeatOffendersResult {

    @SerializedName("repeat_offender_id")
    @Expose
    private String repeatOffenderId;
    @SerializedName("custid")
    @Expose
    private String custid;
    @SerializedName("plate")
    @Expose
    private String plate;
    @SerializedName("violation")
    @Expose
    private String violation;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("violation_id")
    @Expose
    private String violationId;
    @SerializedName("state_code")
    @Expose
    private String stateCode;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("updated_date")
    @Expose
    private Object updatedDate;

    public String getRepeatOffenderId() {
        return repeatOffenderId;
    }

    public void setRepeatOffenderId(String repeatOffenderId) {
        this.repeatOffenderId = repeatOffenderId;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getViolationId() {
        return violationId;
    }

    public void setViolationId(String violationId) {
        this.violationId = violationId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Object getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Object updatedDate) {
        this.updatedDate = updatedDate;
    }

}
