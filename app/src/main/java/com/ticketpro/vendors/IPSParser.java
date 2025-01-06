package com.ticketpro.vendors;

import com.ticketpro.util.DateUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class IPSParser {
    public static final String OUTPUT_FORMAT_STD_DATE = "MM/dd/yyyy hh:mm:ss";

    public static ArrayList<IPSParkingPlate> getPlateStatus(String responseJSON) {
        ArrayList<IPSParkingPlate> plates = new ArrayList<>();
        IPSParkingPlate parking;
        JSONArray response;
        try {
            response = new JSONArray(responseJSON);
            if (response != null || response.length() > 0) {
                for (int i = 0; i < response.length(); i++) {
                    parking = new IPSParkingPlate(response.getJSONObject(i));
                    if (isGreaterThanToday(DateUtil.getDateFromTimeZoneSpace(parking.getParkingExpiryTime())))
                        plates.add(parking);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return plates;
    }

    public static IPSParkingSpace getSpaceStatus(String responseJSON) {
        IPSParkingSpace results = new IPSParkingSpace();
        JSONArray response;
        try {
            //responseJSON = "[{\r\n\t\"ExpiryTime\": \"11\\/28\\/2017 23:46:11\",\r\n\t\"Space\": \"800\",\r\n\t\"SpaceGroup\": \"1-1490\",\r\n\t\"StartDateTime\": \"11\\/28\\/2017 22:48:01\"\r\n}]";
            response = new JSONArray(responseJSON);
            if (response != null) {
                results = new IPSParkingSpace(response.getJSONObject(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return results;
    }


    public static IPSParkingMeter getMeterStatus(String responseJSON) {
        IPSParkingMeter parking = null;
        JSONArray response;
        try {
            response = new JSONArray(responseJSON);
            if (response != null) {
                parking = new IPSParkingMeter(response.getJSONObject(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return parking;
    }

    private static boolean isGreaterThanToday(Date date) {
        Calendar calendar = Calendar.getInstance();
        Calendar toCompare = Calendar.getInstance();
        toCompare.setTimeInMillis(date.getTime());

        return (calendar.get(Calendar.YEAR) == toCompare.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == toCompare.get(Calendar.MONTH)
                && calendar.get(Calendar.DAY_OF_MONTH) == toCompare.get(Calendar.DAY_OF_MONTH))||date.after(new Date());
    }

} 
