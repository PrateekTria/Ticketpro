package com.ticketpro.model;

import com.ticketpro.util.DateUtil;
import com.ticketpro.vendors.ParkingExpireInfo;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Date;
import java.util.List;

@Root(name = "ValidParkingData", strict = false)
public class ValidParkingData1 {

    @Element(name = "IsExpired", required = false)
    private String IsExpired;
    @Element(name = "ParkingSpace", required = false)
    private String ParkingSpace;
    @Element(name = "Zone", required = false)
    private String Zone;
    @Element(name = "Amount", required = false)
    private String Amount;
    @Element(name = "Code", required = false)
    private String Code;
    @Element(name = "DateChangedUtc", required = false)
    private String DateChangedUtc;
    @Element(name = "EndDateUtc", required = false)
    private String EndDateUtc;
    @Element(name = "TicketNumber", required = false)
    private String TicketNumber;
    @Element(name = "DateCreatedUtc", required = false)
    private String DateCreatedUtc;
    @Element(name = "ParkingZone", required = false)
    private String ParkingZone;
    @Element(name = "StartDateUtc", required = false)
    private String StartDateUtc;
    @Element(name = "ContainsTerminalOutOfCommunication", required = false)
    private String ContainsTerminalOutOfCommunication;
    @Element(name = "PurchaseDateUtc", required = false)
    private String PurchaseDateUtc;
    @ElementList(entry = "Tariff", inline = true)
    private List<CaleTariff> tariffList;

    private Date startDateLocal;
    private Date endDateLocal;

    public List<CaleTariff> getTariffList() {
        return tariffList;
    }

    public void setTariffList(List<CaleTariff> tariffList) {
        this.tariffList = tariffList;
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

    public void setEndDateLocal(Date endDateLocal) {
        this.endDateLocal = endDateLocal;
    }

    public String getIsExpired() {
        return IsExpired;
    }

    public void setIsExpired(String IsExpired) {
        this.IsExpired = IsExpired;
    }

    public String getParkingSpace() {
        return ParkingSpace;
    }

    public void setParkingSpace(String ParkingSpace) {
        this.ParkingSpace = ParkingSpace;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String Zone) {
        this.Zone = Zone;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }


    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }


    public String getDateChangedUtc() {
        return DateChangedUtc;
    }

    public void setDateChangedUtc(String DateChangedUtc) {
        this.DateChangedUtc = DateChangedUtc;
    }

    public String getEndDateUtc() {
        return EndDateUtc;
    }

    public void setEndDateUtc(String EndDateUtc) {
        this.EndDateUtc = EndDateUtc;
    }

    public String getTicketNumber() {
        return TicketNumber;
    }

    public void setTicketNumber(String TicketNumber) {
        this.TicketNumber = TicketNumber;
    }

    public String getDateCreatedUtc() {
        return DateCreatedUtc;
    }

    public void setDateCreatedUtc(String DateCreatedUtc) {
        this.DateCreatedUtc = DateCreatedUtc;
    }

    public String getParkingZone() {
        return ParkingZone;
    }

    public void setParkingZone(String ParkingZone) {
        this.ParkingZone = ParkingZone;
    }

    public String getStartDateUtc() {
        return StartDateUtc;
    }

    public void setStartDateUtc(String StartDateUtc) {
        this.StartDateUtc = StartDateUtc;
    }

    public String getContainsTerminalOutOfCommunication() {
        return ContainsTerminalOutOfCommunication;
    }

    public void setContainsTerminalOutOfCommunication(String ContainsTerminalOutOfCommunication) {
        this.ContainsTerminalOutOfCommunication = ContainsTerminalOutOfCommunication;
    }

    public String getPurchaseDateUtc() {
        return PurchaseDateUtc;
    }

    public void setPurchaseDateUtc(String PurchaseDateUtc) {
        this.PurchaseDateUtc = PurchaseDateUtc;
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
