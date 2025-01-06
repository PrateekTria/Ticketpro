package com.ticketpro.vendors;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class IPSParkingMeter {
    public static final String OUTPUT_FORMAT_STD_DATE = "MM/dd/yyyy hh:mm:ss";
    private String expiryTime;
    private String meterNumber;
    private String startDateTime;

    public IPSParkingMeter() {

    }

    public IPSParkingMeter(JSONObject object) throws Exception {
        //this.expiryTime = object.getString("ExpiryTime");
        //this.startDateTime = object.getString("StartDateTime");
        this.expiryTime = updateRequiredTimeZone(object.getString("ExpiryTime"));
        this.startDateTime = updateRequiredTimeZone(object.getString("StartDateTime"));
        this.meterNumber = object.getString("MeterNumber");
    }

    public static Date getDateFromUTCString(String utcString) {
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }
        if (utcString.length() > 18) {
            utcString = utcString.substring(0, 18);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDateToString(Date date, String format,
                                            String timeZone) {
        // null check
        if (date == null) return null;
        // create SimpleDateFormat object with input format
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // default system timezone if passed null or empty
        if (timeZone == null || "".equalsIgnoreCase(timeZone.trim())) {
            timeZone = Calendar.getInstance().getTimeZone().getID();
        }
        // set timezone to SimpleDateFormat
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        // return Date in required format with timezone as String
        return sdf.format(date);
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String updateRequiredTimeZone(String utcString) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
            //format.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = format.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedDateTime(date);
    }

    public String convertedDateTime(Date date) {
        return formatDateToString(date, "MM/dd/yyyy hh:mm:ss", null);
    }


}
