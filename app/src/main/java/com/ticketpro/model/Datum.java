
package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.util.DateUtil;
import com.ticketpro.vendors.ParkingExpireInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Datum {

    @SerializedName("lotName")
    @Expose
    private String lotName;
    @SerializedName("spaceNumber")
    @Expose
    private String spaceNumber;
    @SerializedName("datePurchased")
    @Expose
    private String datePurchased;
    @SerializedName("expiryDate")
    @Expose
    private String expiryDate;
    @SerializedName("dataSource")
    @Expose
    private String dataSource;


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


    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public String getSpaceNumber() {
        return spaceNumber;
    }

    public void setSpaceNumber(String spaceNumber) {
        this.spaceNumber = spaceNumber;
    }

    public String getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(String datePurchased) {
        this.datePurchased = datePurchased;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public ParkingExpireInfo getExpireInfo(){
        endDateLocal = DateUtil.getSamtransDate(getExpiryDate());
        ParkingExpireInfo parkingExpireInfo=new ParkingExpireInfo();

        String expireStr="";
        long diffMinutes,diffHours,diffDays;
        long expiredDiff = new Date().getTime() - this.getEndDateLocal().getTime();
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

   /* public static class ExpireComparator implements Comparator<Datum> {
        @Override
        public int compare(Datum item1, Datum item2) {
            return item1.endDateLocal.compareTo(item2.endDateLocal);
        }
    }*/

    public static class ExpireComparator implements Comparator<Datum> {
        @Override
        public int compare(Datum item1, Datum item2) {
            return item1.endDateLocal.compareTo(item2.endDateLocal);
        }
    }
    public static class ExpireComparator1 implements Comparator<Datum> {
        @Override
        public int compare(Datum item1, Datum item2) {

            return DateUtil.getSamtransDate(item1.expiryDate).compareTo(DateUtil.getSamtransDate(item2.expiryDate));
        }
    }
    public static class SpaceComparator implements Comparator<Datum> {
        @Override
        public int compare(Datum item1, Datum item2) {

            return Integer.compare(Integer.valueOf(item1.getSpaceNumber()), (Integer.valueOf(item2.getSpaceNumber())));

        }
    }

    public static ArrayList<Datum>_removeDuplicateValueFromArray(List<Datum> data){
        ArrayList<Datum> data1 = (ArrayList<Datum>) data;
        List<Datum> noRepeat = new ArrayList<>();
        for (Datum event : data1) {
            boolean isFound = false;
            // check if the event name exists in noRepeat
            for (Datum e : noRepeat) {
                if (e.getSpaceNumber().equals(event.getSpaceNumber()) && e.getExpiryDate().equals(event.getExpiryDate())) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) noRepeat.add(event);
        }
        return (ArrayList<Datum>) noRepeat;
    }


    public static ArrayList<Datum> removeSpaceByRecentPurchaed(List<Datum> noRepeat) {
        ArrayList<Datum> nodublicateData = new ArrayList<>();
        for (Datum event : noRepeat) {
            boolean isFound = false;
            // check if the event name exists in noRepeat
            for (Datum e : nodublicateData) {
                if (e.getSpaceNumber().equals(event.getSpaceNumber())) {
                    if (DateUtil.getSamtransDate(e.getDatePurchased()).after(DateUtil.getSamtransDate(event.getDatePurchased()))) {
                        isFound = true;
                        break;
                    }
                }
            }
            if (!isFound) nodublicateData.add(event);
        }


        return nodublicateData;
    }
}
