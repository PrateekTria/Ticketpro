package com.ticketpro.model;

import android.content.ContentValues;
import android.os.AsyncTask;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.DateUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

@Entity(tableName = "maintenance_logs")
public class MaintenanceLog {
    @PrimaryKey
    @ColumnInfo(name = "log_id")
    private long logId;
    @ColumnInfo(name = "custid")
    private int custId;
    @ColumnInfo(name = "userid")
    private int userId;
    @ColumnInfo(name = "device_id")
    private int deviceId;
    @ColumnInfo(name = "item_name")
    private String itemName;
    @ColumnInfo(name = "problem_type")
    private String problemType;
    @ColumnInfo(name = "comments")
    private String comments;
    @ColumnInfo(name = "log_date")
    private Date logDate;
    @ColumnInfo(name = "latitude")
    private String latitude;
    @ColumnInfo(name = "longitude")
    private String longitude;
    @ColumnInfo(name = "location")
    private String location;
    @Ignore
    private ArrayList<MaintenancePicture> pictures = new ArrayList<MaintenancePicture>();

    public MaintenanceLog() {

    }

    public MaintenanceLog(JSONObject object) throws Exception {
        this.setLogId(object.getLong("log_id"));
        this.setCustId(object.getInt("custid"));
        this.setUserId(!object.isNull("userid") ? object.getInt("userid") : 0);
        this.setDeviceId(!object.isNull("device_id") ? object.getInt("device_id") : 0);
        this.setItemName(object.getString("item_name"));
        this.setProblemType(object.getString("problem_type"));
        this.setComments(object.getString("comments"));
        this.setLocation(object.getString("location"));
        this.setLatitude(object.getString("latitude"));
        this.setLongitude(object.getString("longitude"));

        this.setLogDate(DateUtil.getDateFromSQLString(object.getString("log_date")));
    }

    public static ArrayList<MaintenanceLog> getLogs() throws Exception {
        ArrayList<MaintenanceLog> list = new ArrayList<MaintenanceLog>();
        list = (ArrayList<MaintenanceLog>) ParkingDatabase.getInstance(TPApplication.getInstance()).maintenanceLogsDao().getLogs();
        return list;
    }


    public static MaintenanceLog getLogById(int logId) throws Exception {
        MaintenanceLog logEntry = null;
        logEntry = ParkingDatabase.getInstance(TPApplication.getInstance()).maintenanceLogsDao().getLogById(logId);
        //Load picture records
        try {
            logEntry.setPictures(MaintenancePicture.getPictures(logEntry.getLogId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logEntry;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).maintenanceLogsDao().removeAll();
    }

    public static void removeById(long id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).maintenanceLogsDao().removeById(id);
    }

    public static void insertMaintenanceLog(MaintenanceLog MaintenanceLog) {
        new MaintenanceLog.InsertMaintenanceLog(MaintenanceLog).execute();
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("log_id", getLogId());
        values.put("custid", getCustId());
        values.put("userid", getUserId());
        values.put("device_id", getDeviceId());
        values.put("item_name", getItemName());
        values.put("problem_type", getProblemType());
        values.put("comments", getComments());
        values.put("location", getLocation());
        values.put("latitude", getLatitude());
        values.put("longitude", getLongitude());
        values.put("log_date", DateUtil.getSQLStringFromDate2(getLogDate()));

        return values;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public ArrayList<MaintenancePicture> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<MaintenancePicture> pictures) {
        this.pictures = pictures;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static class ItemComparator implements Comparator<MaintenanceLog> {
        @Override
        public int compare(MaintenanceLog item1, MaintenanceLog item2) {
            return item1.getItemName().compareToIgnoreCase(item1.getItemName());
        }
    }

    public static class TypeComparator implements Comparator<MaintenanceLog> {
        @Override
        public int compare(MaintenanceLog item1, MaintenanceLog item2) {
            return item1.getProblemType().compareToIgnoreCase(item1.getProblemType());
        }
    }

    public static class DateComparator implements Comparator<MaintenanceLog> {
        @Override
        public int compare(MaintenanceLog item1, MaintenanceLog item2) {
            return item1.getLogDate().compareTo(item2.getLogDate());
        }
    }

    private static class InsertMaintenanceLog extends AsyncTask<Void, Void, Void> {
        private MaintenanceLog MaintenanceLog;

        public InsertMaintenanceLog(MaintenanceLog MaintenanceLog) {
            this.MaintenanceLog = MaintenanceLog;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).maintenanceLogsDao().insertMaintenanceLog(MaintenanceLog);
            return null;
        }
    }
}
