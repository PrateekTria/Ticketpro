package com.ticketpro.vendors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PayByPhonePlateParser {

    @SerializedName("parkingEntitlements")
    @Expose
    private List<ParkingEntitlement> parkingEntitlements = new ArrayList<>();
    @SerializedName("platesResponse")
    @Expose
    private List<PlatesResponse> platesResponse = new ArrayList<>();

   /* public Integer getQueryResponseId() {
        return queryResponseId;
    }

    public void setQueryResponseId(Integer queryResponseId) {
        this.queryResponseId = queryResponseId;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }*/

    public List<ParkingEntitlement> getParkingEntitlements() {
        return parkingEntitlements;
    }

    public void setParkingEntitlements(List<ParkingEntitlement> parkingEntitlements) {
        this.parkingEntitlements = parkingEntitlements;
    }
    public List<PlatesResponse> getPlatesResponse() {
        return platesResponse;
    }

    public void setPlatesResponse(List<PlatesResponse> platesResponse) {
        this.platesResponse = platesResponse;
    }
}
