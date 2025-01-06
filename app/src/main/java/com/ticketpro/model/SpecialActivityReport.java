package com.ticketpro.model;

import android.content.ContentValues;
import android.os.AsyncTask;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.exception.TPException;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.DateUtil;
import com.ticketpro.vendors.ParkingExpireInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "special_activity_reports")
public class SpecialActivityReport implements Serializable {
    @Ignore
    private static final long serialVersionUID = 1L;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "report_id")
    private int reportId;
    @ColumnInfo(name = "userid")
    @SerializedName("userid")
    private int userId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    private int custId;
    @ColumnInfo(name = "activity_id")
    @SerializedName("activity_id")
    private int activityId;
    @ColumnInfo(name = "disposition_id")
    @SerializedName("disposition_id")
    private int dispositionId;
    @ColumnInfo(name = "start_date")
    @SerializedName("start_date")
    private String startDate;
    @ColumnInfo(name = "end_date")
    @SerializedName("end_date")
    private String endDate;
    @ColumnInfo(name = "start_time")
    @SerializedName("start_time")
    private String startTime;
    @ColumnInfo(name = "end_time")
    @SerializedName("end_time")
    private String endTime;
    @ColumnInfo(name = "case_number")
    @SerializedName("case_number")
    private String caseNumber;
    @ColumnInfo(name = "street_address")
    @SerializedName("street_address")
    private String streetAddress;
    @ColumnInfo(name = "notes")
    @SerializedName("notes")
    private String notes;
    @ColumnInfo(name = "photo1")
    @SerializedName("photo1")
    private String photo1;
    @ColumnInfo(name = "photo2")
    @SerializedName("photo2")
    private String photo2;
    @ColumnInfo(name = "photo3")
    @SerializedName("photo3")
    private String photo3;
    @ColumnInfo(name = "latitude")
    @SerializedName("latitude")
    private String latitude;
    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    private String longitude;
    @ColumnInfo(name = "autoSelect")
    @SerializedName("autoSelect")
    private String autoSelect;
    @ColumnInfo(name = "createDate")
    @SerializedName("createDate")
    private String createdDate;
    @ColumnInfo(name = "duration")
    @SerializedName("duration")
    private String duration;
    @ColumnInfo(name = "device_id")
    @SerializedName("device_id")
    private int deviceId;
    @ColumnInfo(name = "location")
    @SerializedName("location")
    private String location;
    @ColumnInfo(name = "activityName")
    @SerializedName("activityName")
    private String activityName;
    @ColumnInfo(name = "ticket_count")
    @SerializedName("ticket_count")
    private String ticketCount;

    @Ignore
    private String totalDuration;

    @Ignore
    private ArrayList<SpecialActivityPicture> activityPictures;


    public SpecialActivityReport() {

    }

    public SpecialActivityReport(JSONObject object) throws Exception {

        try {
            this.setReportId(object.getInt("report_id"));
            this.setUserId(object.getInt("userid"));
            this.setCustId(!object.isNull("custid") ? object.getInt("custid") : 0);
            this.setActivityId(!object.isNull("activity_id") ? object.getInt("activity_id") : 0);
            this.setDispositionId(!object.isNull("disposition_id") ? object.getInt("disposition_id") : 0);
            this.setCaseNumber(object.getString("case_number"));
            this.setStreetAddress(object.getString("street_address"));
            this.setNotes(object.getString("notes"));
            this.setPhoto1(object.getString("photo1"));
            this.setPhoto2(object.getString("photo2"));
            this.setPhoto3(object.getString("photo3"));
            this.setLatitude(object.getString("latitude"));
            this.setLongitude(object.getString("longitude"));
            this.setAutoSelect(object.getString("autoSelect"));
            this.setCreatedDate(object.getString("createDate"));
            this.setDuration(object.getString("duration"));
            this.setLocation(object.getString("location"));
            this.setDeviceId(object.getInt("device_id"));
            this.setActivityName(object.getString("activityName"));
            this.setStartDate(object.getString("start_date"));
            this.setEndDate(object.getString("end_date"));
            this.setStartTime(object.getString("start_time"));
            this.setEndTime(object.getString("end_time"));
            this.setTicketCount(object.getString("ticket_count"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static ArrayList<SpecialActivityReport> getSpecialActivityReports(int custid, String currentDate) throws TPException {
        ArrayList<SpecialActivityReport> list = new ArrayList<SpecialActivityReport>();
        list = (ArrayList<SpecialActivityReport>) ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityReportsDao().getSpecialActivityReports(custid, currentDate);
        return list;
    }


    public static SpecialActivityReport getSpecialActivityReportByPrimaryKey(String primaryKey) throws TPException {
        SpecialActivityReport object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityReportsDao().getSpecialActivityReportByPrimaryKey(primaryKey);
        return object;
    }

    public static int getLastInsertId() {
        int lastId = 0;
        lastId = ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityReportsDao().getLastInsertId();
        return lastId;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityReportsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityReportsDao().removeById(id);
    }

    public static void insertSpecialActivityReport(SpecialActivityReport SpecialActivityReport) {
        new SpecialActivityReport.InsertSpecialActivityReport(SpecialActivityReport).execute();
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        if (this.reportId != 0)
            values.put("report_id", this.reportId);

        values.put("userid", this.userId);
        values.put("custid", this.custId);
        values.put("activity_id", this.activityId);
        values.put("disposition_id", this.dispositionId);
        values.put("start_date", this.startDate);
        values.put("end_date", this.endDate);
        values.put("start_time", this.startTime);
        values.put("end_time", this.endTime);
        values.put("case_number", this.caseNumber);
        values.put("notes", this.notes);
        values.put("street_address", this.streetAddress);
        values.put("photo1", this.photo1);
        values.put("photo2", this.photo2);
        values.put("photo3", this.photo3);
        values.put("longitude", this.longitude);
        values.put("latitude", this.latitude);
        values.put("autoSelect", this.autoSelect);
        values.put("createdDate", this.createdDate);
        values.put("duration", this.duration);
        values.put("location", this.location);
        values.put("device_id", this.deviceId);
        values.put("activityName", this.activityName);
        values.put("ticket_count",this.ticketCount);
        return values;
    }

    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        try {
            object.put("report_id", this.reportId);
            object.put("userid", this.userId);
            object.put("custid", this.custId);
            object.put("activity_id", this.activityId);
            object.put("disposition_id", this.dispositionId);
            object.put("start_date", this.startDate);
            object.put("end_date", this.endDate);
            object.put("start_time", this.startTime);
            object.put("end_time", this.endTime);
            object.put("case_number", this.caseNumber);
            object.put("street_address", this.streetAddress);
            object.put("notes", this.notes);
            object.put("photo1", this.photo1);
            object.put("photo2", this.photo2);
            object.put("photo3", this.photo3);
            object.put("longitude", this.longitude);
            object.put("latitude", this.latitude);
            object.put("autoSelect", this.autoSelect);
            object.put("createDate", this.createdDate);
            object.put("duration", this.duration);
            object.put("location", this.location);
            object.put("device_id", this.deviceId);
            object.put("activityName", this.activityName);
            object.put("ticket_count",this.ticketCount);

        } catch (Exception e) {
        }

        return object;
    }

    public ParkingExpireInfo getExpireInfo() {
        ParkingExpireInfo parkingExpireInfo = new ParkingExpireInfo();
        String expireStr = "";
        long diffMinutes, diffHours, diffDays;
        long expiredDiff = DateUtil.getDateFromStringForActivity(startTime).getTime() - DateUtil.getDateFromStringForActivity(endTime).getTime();
        if (expiredDiff > 0) {
            diffMinutes = expiredDiff / (60 * 1000) % 60;
            diffHours = expiredDiff / (60 * 60 * 1000) % 24;
            diffDays = expiredDiff / (24 * 60 * 60 * 1000);
            if (diffDays >= 1) {
                expireStr = diffDays + " d " + diffHours + " h ago";

            } else if (diffHours >= 1) {
                expireStr = diffHours + " h " + diffMinutes + " m ago";

            } else {
                expireStr = diffMinutes + " m ago";
            }

            parkingExpireInfo.setExpired(true);

        } else {
            expiredDiff = Math.abs(expiredDiff);
            diffMinutes = expiredDiff / (60 * 1000) % 60;
            diffHours = expiredDiff / (60 * 60 * 1000) % 24;
            diffDays = expiredDiff / (24 * 60 * 60 * 1000);

            if (diffDays >= 1) {
                expireStr =/*"in "+*/diffDays + " d " + diffHours + " h";

            } else if (diffHours >= 1) {
                expireStr =/*"in "+*/diffHours + " h " + diffMinutes + " m";

            } else {
                expireStr =/*"in "+*/diffMinutes + " m";
            }

            parkingExpireInfo.setExpired(false);
        }

        parkingExpireInfo.setExpireMsg(expireStr);
        parkingExpireInfo.setDiffDays((int) diffDays);
        parkingExpireInfo.setDiffHrs((int) diffHours);
        parkingExpireInfo.setDiffMinutes((int) diffMinutes);
        parkingExpireInfo.setDiffSeconds((int) diffMinutes * 60);

        return parkingExpireInfo;
    }

    public ArrayList<SpecialActivityPicture> getActivityPictures() {
        return activityPictures;
    }

    public void setActivityPictures(ArrayList<SpecialActivityPicture> activityPictures) {
        this.activityPictures = activityPictures;
    }

    public String getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(String ticketCount) {
        this.ticketCount = ticketCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getAutoSelect() {
        return autoSelect;
    }

    public void setAutoSelect(String autoSelect) {
        this.autoSelect = autoSelect;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getDispositionId() {
        return dispositionId;
    }

    public void setDispositionId(int dispositionId) {
        this.dispositionId = dispositionId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    private static class InsertSpecialActivityReport extends AsyncTask<Void, Void, Void> {
        private SpecialActivityReport SpecialActivityReport;

        public InsertSpecialActivityReport(SpecialActivityReport SpecialActivityReport) {
            this.SpecialActivityReport = SpecialActivityReport;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityReportsDao().insertSpecialActivityReport(SpecialActivityReport);
            return null;
        }
    }
}
