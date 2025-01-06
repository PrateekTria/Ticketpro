package com.ticketpro.model;

import com.ticketpro.util.DateUtil;
import com.ticketpro.vendors.ParkingExpireInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ParkeonZoneInfo {
    private String end_date;

    private String zone_id;

    private String received_date;

    private String session_id;

    private String plate_number;

    private String type;

    private String start_date;

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


    public String getEnd_date ()
    {
        return end_date;
    }

    public void setEnd_date (String end_date)
    {
        this.end_date = end_date;
    }

    public String getZone_id ()
    {
        return zone_id;
    }

    public void setZone_id (String zone_id)
    {
        this.zone_id = zone_id;
    }

    public String getReceived_date ()
    {
        return received_date;
    }

    public void setReceived_date (String received_date)
    {
        this.received_date = received_date;
    }

    public String getSession_id ()
    {
        return session_id;
    }

    public void setSession_id (String session_id)
    {
        this.session_id = session_id;
    }

    public String getPlate_number ()
    {
        return plate_number;
    }

    public void setPlate_number (String plate_number)
    {
        this.plate_number = plate_number;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getStart_date ()
    {
        return start_date;
    }

    public void setStart_date (String start_date)
    {
        this.start_date = start_date;
    }



    public ParkingExpireInfo getExpireInfo(){
        endDateLocal = DateUtil.getCaleDateFromUTCStringParkeon(getEnd_date());
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


    @Override
    public String toString()
    {
        return "ClassPojo [end_date = "+end_date+", zone_id = "+zone_id+", received_date = "+received_date+", session_id = "+session_id+", plate_number = "+plate_number+", type = "+type+", start_date = "+start_date+"]";
    }

    public static class ExpireComparator implements Comparator<ParkeonZoneInfo> {
        @Override
        public int compare(ParkeonZoneInfo item1, ParkeonZoneInfo item2) {
            return item1.endDateLocal.compareTo(item2.endDateLocal);
        }
    }


    public static class PlateComparator implements Comparator<ParkeonZoneInfo> {
        @Override
        public int compare(ParkeonZoneInfo item1, ParkeonZoneInfo item2) {
            return item1.getPlate_number().compareToIgnoreCase(item2.getPlate_number());
        }
    }

    public static ArrayList<ParkeonZoneInfo>_removeDuplicateValueFromArray(List<ParkeonZoneInfo> data){
        ArrayList<ParkeonZoneInfo> data1 = (ArrayList<ParkeonZoneInfo>) data;
        List<ParkeonZoneInfo> noRepeat = new ArrayList<>();
        for (ParkeonZoneInfo event : data1) {
            boolean isFound = false;
            // check if the event name exists in noRepeat
            for (ParkeonZoneInfo e : noRepeat) {
                if (e.getPlate_number().equals(event.getPlate_number())) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) noRepeat.add(event);
        }
        return (ArrayList<ParkeonZoneInfo>) noRepeat;
    }

}
