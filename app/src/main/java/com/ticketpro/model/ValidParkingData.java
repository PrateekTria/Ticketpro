package com.ticketpro.model;

import com.ticketpro.util.DateUtil;
import com.ticketpro.vendors.ParkingExpireInfo;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Date;
import java.util.List;

@Root(name = "ValidParkingData", strict = false)
public class ValidParkingData {
    @Element(name = "IsExpired", required = false)
    private String IsExpired;

    @Element(name = "EndDateUtc", required = false)
    private String EndDateUtc;

    @Element(name = "Zone", required = false)
    private String Zone;

    @Element(name = "StartDateUtc", required = false)
    private String StartDateUtc;

    @Element(name = "Code", required = false)
    private String Code;
    @ElementList(entry = "Tariff", inline = true)
    private List<CaleTariff> tariffList;
    @Element(name = "Amount", required = false)
    private String Amount;
    private Date startDateLocal;
    private Date endDateLocal;

    public List<CaleTariff> getTariffList() {
        return tariffList;
    }

    public void setTariffList(List<CaleTariff> tariffList) {
        this.tariffList = tariffList;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public Date getStartDateLocal() {
        return startDateLocal;
    }

    public void setStartDateLocal(Date startDateLocal) {
        this.startDateLocal = startDateLocal;
    }

    public Date getEndDateLocal() {
        return endDateLocal;
    }

    public String getIsExpired() {
        return IsExpired;
    }

    public void setIsExpired(String IsExpired) {
        this.IsExpired = IsExpired;
    }

    public String getEndDateUtc() {
        return EndDateUtc;
    }

    public void setEndDateUtc(String EndDateUtc) {
        this.EndDateUtc = EndDateUtc;
    }


    public String getZone() {
        return Zone;
    }

    public void setZone(String Zone) {
        this.Zone = Zone;
    }


    public String getStartDateUtc() {
        return StartDateUtc;
    }

    public void setStartDateUtc(String StartDateUtc) {
        this.StartDateUtc = StartDateUtc;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }


    public ParkingExpireInfo getExpireInfo(){
        endDateLocal = DateUtil.getCaleDateFromUTCString(getEndDateUtc());
        ParkingExpireInfo parkingExpireInfo=new ParkingExpireInfo();

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