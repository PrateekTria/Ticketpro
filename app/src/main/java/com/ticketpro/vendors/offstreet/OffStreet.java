package com.ticketpro.vendors.offstreet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.util.DateUtil;
import com.ticketpro.vendors.ParkingExpireInfo;

import java.text.ParseException;
import java.util.Date;

/**
 * **********************************************************
 * File: OffStreet.java
 * Created by: SANJIV
 * Date: 11/4/24
 * Description:
 * **********************************************************
 */

public class OffStreet {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("permitId")
    @Expose
    private String permitId;
    @SerializedName("plate")
    @Expose
    private String plate;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("startDateTime")
    @Expose
    private String startDateTime;
    @SerializedName("endDateTime")
    @Expose
    private String endDateTime;
    @SerializedName("createdDateTime")
    @Expose
    private String createdDateTime;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("tenantName")
    @Expose
    private Object tenantName;

    private Date startDateLocal;
    private Date endDateLocal;

    public Date getStartDateLocal() {
        return startDateLocal;
    }

    public void setStartDateLocal(Date startDateLocal) {
        this.startDateLocal = startDateLocal;
    }

    public Date getEndDateLocal() {
        return endDateLocal;
    }

    public void setEndDateLocal(Date endDateLocal) {
        this.endDateLocal = endDateLocal;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPermitId() {
        return permitId;
    }

    public void setPermitId(String permitId) {
        this.permitId = permitId;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Object getTenantName() {
        return tenantName;
    }

    public void setTenantName(Object tenantName) {
        this.tenantName = tenantName;
    }

    public ParkingExpireInfo getExpireInfo() throws ParseException {
        endDateLocal = DateUtil.getCurbtracDateFromUTCString1(getEndDateTime());
        ParkingExpireInfo parkingExpireInfo=new ParkingExpireInfo();
        if (endDateLocal==null)
            return null;
        String expireStr="";
        long diffMinutes,diffHours,diffDays;
        long expiredDiff = new Date().getTime() - endDateLocal.getTime();
        if(expiredDiff > 0){
            diffMinutes = expiredDiff / (60 * 1000) % 60;
            diffHours = expiredDiff / (60 * 60 * 1000) % 24;
            diffDays = expiredDiff / (24 * 60 * 60 * 1000);
            if(diffDays>=1){
                expireStr=diffDays+" d "+diffHours +" h ago";

            }else if(diffHours>=1){
                expireStr=diffHours+" h "+diffMinutes +" m ago";

            }else{
                expireStr=diffMinutes +" m ago";
            }

            parkingExpireInfo.setExpired(true);

        }else{
            expiredDiff=Math.abs(expiredDiff);
            diffMinutes = expiredDiff / (60 * 1000) % 60;
            diffHours = expiredDiff / (60 * 60 * 1000) % 24;
            diffDays = expiredDiff / (24 * 60 * 60 * 1000);

            if(diffDays>=1){
                expireStr=/*"in "+*/diffDays+" d "+diffHours+" h";

            }else if(diffHours>=1){
                expireStr=/*"in "+*/diffHours+" h "+diffMinutes+" m";

            }else{
                expireStr=/*"in "+*/diffMinutes +" m";
            }

            parkingExpireInfo.setExpired(false);
        }

        parkingExpireInfo.setExpireMsg(expireStr);
        parkingExpireInfo.setDiffDays((int)diffDays);
        parkingExpireInfo.setDiffHrs((int)diffHours);
        parkingExpireInfo.setDiffMinutes((int)diffMinutes);
        parkingExpireInfo.setDiffSeconds((int)diffMinutes * 60);

        return parkingExpireInfo;
    }
}
