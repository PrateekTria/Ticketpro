package com.ticketpro.util;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.os.Build;

import com.ticketpro.model.Feature;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class DateUtil {

    public static final int MENU_CANCEL = 0;
    final private static Calendar c = Calendar.getInstance();
    private static int mYear = c.get(Calendar.YEAR);
    private static int mMonth = c.get(Calendar.MONTH);
    private static int mDay = c.get(Calendar.DAY_OF_MONTH);
    private static int mHour = c.get(Calendar.HOUR_OF_DAY);
    private static int mMinute = c.get(Calendar.MINUTE);

    public static String getLastString(String str) {
        if (str == null || str.equals("null") || str.equals("")) {
            return "";
        }

        if (str.length() > 0) {
            String arr[] = str.split("\\.");
            int lastIndex = arr.length - 1;
            return arr[lastIndex];
        }

        return "";
    }

    public static String getStringFromDate(Date time) {
        if (time == null || time.equals("null") || time.equals("")) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        return sdf.format(time);
    }

    public static String getStringFromDate3(Date time) {
        if (time == null || time.equals("null") || time.equals("")) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return sdf.format(time);
    }
    public static String getStringFromDate4(Date time) {
        if (time == null || time.equals("null") || time.equals("")) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }

    public static String getStringFromDate1(Date time) {
        if (time == null || time.equals("null") || time.equals("")) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy");
        return sdf.format(time);
    }

    public static String getStringFromTimestamp(String timestamp) {
        if (timestamp == null || timestamp.equals("null") || timestamp.equals("")) {
            return "";
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            return sdf.format(new Date(Long.parseLong(timestamp) * 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    public static Date getDateFromTimestamp(String timestamp) {
        if (timestamp == null || timestamp.equals("null") || timestamp.equals("")) {
            return null;
        }

        try {
            return new Date(Long.parseLong(timestamp) * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Date getDateFromUTCString(String utcString) {
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        try {
            return sdf.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Date getCaleDateFromUTCString(String utcString) {
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        //sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Date getDateFromUTCStringPaybyPhone(String utcString) {
        System.out.println(utcString);
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Date getCaleDateFromUTCStringParkeon(String utcString) {
        System.out.println(utcString);
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Date getSamtransDateFromUTCStringParkeon(String utcString) {
        System.out.println(utcString);
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");


        try {

            Date format = sdf.parse(utcString);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(format);

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf1.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Date getSamtransDate(String utcString) {
        System.out.println(utcString);
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");


        try {

            Date format = sdf.parse(utcString);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(format);

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf1.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getConvertedDate(String date1) {
        String date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));


            Date format = sdf.parse(date1);
            //Date format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date1);
            date = new SimpleDateFormat("E MMM dd HH:mm").format(format);
            //date = new SimpleDateFormat("MM/dd/yyyy").parse(format1);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return date;
    }


    public static String getConvertedDateSamtrans(String date1) {
        String date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            // sdf.setTimeZone(TimeZone.getTimeZone("UTC"));


            Date format = sdf.parse(date1);
            //Date format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date1);
            date = new SimpleDateFormat("E MMM dd/yy hh:mm aa").format(format);

            //date = new SimpleDateFormat("MM/dd/yyyy").parse(format1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String getConvertedDateParkeon(String date1) {
        String date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date format = sdf.parse(date1);
            //Date format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date1);
            date = new SimpleDateFormat("E MMM dd yyyy hh:mm aa").format(format);
            //date = new SimpleDateFormat("MM/dd/yyyy").parse(format1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String getSpecialActivity(String date1) {
        String date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date format = sdf.parse(date1);
            //Date format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date1);
            date = new SimpleDateFormat("yyyy-MM-dd").format(format);
            //date = new SimpleDateFormat("MM/dd/yyyy").parse(format1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String getStartDateSamtrans() {
        String startDate = null;
        try {
            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(yesterday());
            String input = format /*+ " 00:00:00"*/;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat outputformat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = null;
            date = df.parse(input);
            startDate = outputformat.format(date);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return startDate;
    }

    public static Date getPP2DateFromUTCString(String utcString) {
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }

        @SuppressLint({"NewApi", "LocalSuppress"}) SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SSSX");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getPP2DateFromUTCString1(String utcString) {
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }

        String returnDate = null;
        try {
            @SuppressLint({"NewApi", "LocalSuppress"}) SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SSSX");
            sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date format = sdf.parse(utcString);
            returnDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa").format(format);

            return returnDate;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    private static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.DATE, -1);
        cal.add(Calendar.HOUR, -24);
        return cal.getTime();
    }

    public static String getEndDateSamtrans() {
        String startDate = null;
        try {
            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            String input = format /*+ " 23:59:59"*/;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat outputformat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = null;
            date = df.parse(input);
            startDate = outputformat.format(date);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return startDate;
    }

    public static Date getDateFromUTCStringMultiSpace(String utcString) {
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }
        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        try {
            if (Feature.isFeatureAllowed(Feature.IPS_UTCTIME)) {
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            }
            return sdf.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateFromIPS(String utcString) {
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }
        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");


        try {
            /*if (isUTCTime) {
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            } else {
                sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            }*/
            if (Feature.isFeatureAllowed(Feature.IPS_UTCTIME)) {
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            }
            return sdf.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateInMOnthDayFormat(String utcString) {
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }
        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        DateFormat sdf1 = new SimpleDateFormat("MM/dd HH:mm");
        try {
            Date date = sdf.parse(utcString);
            return sdf1.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Date getDateFromTimeZoneSpace(String utcString) {
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        try {
            return sdf.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Date getDateFromZuluString(String zuluString) {
        if (zuluString == null || zuluString.equals("null") || zuluString.equals("")) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            return sdf.parse(zuluString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getStringFromDateShortYear(Date date) {
        if (date == null || date.equals("null") || date.equals("")) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm");
        return sdf.format(date);
    }

    public static String getDateFromUTCString1(Date utcString) {
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        try {
            return sdf.format(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String getStringFromDateShortYear2(Date date) {
        if (date == null || date.equals("null") || date.equals("")) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        return sdf.format(date);
    }

    public static String getShortDateTime(Date date) {
        if (date == null || date.equals("null") || date.equals("")) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm a");
        return sdf.format(date);
    }

    public static String getDateStringFromDate(Date time) {
        if (time == null || time.equals("null") || time.equals("") || time.equals("0000-00-00")) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(time);
    }

    public static String getTimeStringFromDate(Date time) {
        if (time == null || time.equals("null") || time.equals("")) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(time);

    }

    public static String getStringFromDate2(Date time) {
        if (time == null || time.equals("null") || time.equals("")) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm");
        return sdf.format(time);

    }

    public static String getExpDateString(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        return sdf.format(time);
    }

    public static String getSQLStringFromDate(Date time) {
        if (time == null || time.equals("null") || time.equals("")) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(time);
    }

    public static String getSQLStringFromDate2(Date date) {
        if (date == null || date.equals("null") || date.equals("")) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getCurrentTime() {
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    public static String getCurrentTimeSA() {
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getTimeFromDate(Date date) {
        if (date == null || date.equals("null") || date.equals("")) {
            return "";
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getCurrentDateTime() {
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getCurrentDateTimeAC() {
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getCurrentDateTimeActivity() {
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getCurrentDateTime1() {
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getCurrentDateTimeMillis() {
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getCurrentDate() {
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    public static Date getDateFromString(String dateStr) {
        if (dateStr == null || dateStr.equals("null") || dateStr.equals("")) {
            return null;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static Date getDateFromStringForActivity(String dateStr) {
        if (dateStr == null || dateStr.equals("null") || dateStr.equals("")) {
            return null;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static Date getDateFromSQLString(String dateStr) {
        if (dateStr == null || dateStr.equals("null") || dateStr.equals("") || dateStr.equals("0000-00-00")) {
            return null;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(dateStr);

        } catch (ParseException e) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                return sdf.parse(dateStr);

            } catch (Exception e1) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    return sdf1.parse(dateStr);
                } catch (Exception e2) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //Return Date object as per String passed
    public static Date getDateObjectFromString(String dateStr) {
        if (dateStr == null || dateStr.equals("null") || dateStr.equals("") || dateStr.equals("0000-00-00")) {
            return null;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date parsed = format.parse(dateStr);
            java.sql.Date date = new java.sql.Date(parsed.getTime());

            return date;
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public static String getBeginEndDate(Date time) {
        if (time == null || time.equals("null") || time.equals("")) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
    }


    public static Dialog getCurrentTimeDialog(Context ctx, OnTimeSetListener timeSetListener) {
        return new TimePickerDialog(ctx, timeSetListener, mHour, mMinute, false);
    }

    public static Dialog getCurrentDateDialog(Context ctx, OnDateSetListener dateSetListener) {
        return new DatePickerDialog(ctx, dateSetListener, mYear, mMonth, mDay);
    }

    public static int dateToJulian(Calendar calendar) {
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        return dayOfYear;
    }

    //Supported TimeZone GMT,PST,GMT+5
    public static String getTimeZoneDateFromAnyDate(Date date, String timeZoneType, String requiredOutputFormat) {
        DateFormat simpleDateFormat = new SimpleDateFormat(requiredOutputFormat);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneType));
        return simpleDateFormat.format(date);
    }

    /**
     * Get a diff between two dates
     *
     * @param oldDate the old date
     * @param newDate the new date
     * @return the diff value, in the minutes
     */
    public static long getTimeDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            //return TimeUnit.HOURS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
            return TimeUnit.MINUTES.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long getTimeDiffHours(String startDate, String endDate) {
        try {

            //return TimeUnit.HOURS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return TimeUnit.HOURS.convert(formatter.parse(startDate).getTime() - formatter.parse(endDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long getTimeDiffHoursActivity(String startDate, String endDate) {
        try {

            //return TimeUnit.HOURS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            return TimeUnit.HOURS.convert(formatter.parse(startDate).getTime() - formatter.parse(endDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long getTimeDiffHoursForSA(String startDate, String endDate) {
        try {

            //return TimeUnit.HOURS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            return TimeUnit.HOURS.convert(formatter.parse(startDate).getTime() - formatter.parse(endDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Date curbtracDateUtc(String end) throws ParseException {

        ZonedDateTime zonedDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            zonedDateTime = ZonedDateTime.parse(end);
        }

        LocalDateTime localDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        }

        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
        }
        String formattedDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDate = localDateTime.format(formatter);

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");
            //sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf1.parse(formattedDate);
        }

        return null;

    }


    public static Date getCurbtracDateFromUTCString1(String utcString) {
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }
        String s = parseDate(utcString);

        @SuppressLint({"NewApi", "LocalSuppress"})
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
        //sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String getCurbtracDateFromUTCString2(String utcString) {
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }
        String s = parseDate(utcString);

        @SuppressLint({"NewApi", "LocalSuppress"})
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
        //sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date parse = sdf.parse(s);
            SimpleDateFormat sddf = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
            return sddf.format(parse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String parseDate(String dateString) {
        // Parse the date string using ZonedDateTime
        ZonedDateTime zonedDateTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            zonedDateTime = ZonedDateTime.parse(dateString);
        }

        // Format the date to the desired pattern
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a"));
        }
        return null;
    }
}
