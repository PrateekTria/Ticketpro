package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepeatOffendersFromService {
    @SerializedName("custid")
    @Expose
    private int custId;
    @SerializedName("plate")
    @Expose
    private String plate;
    @SerializedName("violation_id")
    @Expose
    private int violationId;
    @SerializedName("state_code")
    @Expose
    private String stateCode;

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
}
