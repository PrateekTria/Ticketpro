package com.ticketpro.model;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;

import org.json.JSONObject;

import java.util.ArrayList;

@Entity(tableName = "durations")
public class Duration {
    @PrimaryKey
    @ColumnInfo(name = "duration_id")
    @SerializedName("duration_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "duration")
    @SerializedName("duration")
    @Expose
    private String title;
    @ColumnInfo(name = "duration_mins")
    @SerializedName("duration_mins")
    @Expose
    private int durationMinutes;
    @ColumnInfo(name = "order_number")
    @SerializedName("order_number")
    @Expose
    private int orderNumber;
    @ColumnInfo(name = "default_violation_id")
    @SerializedName("default_violation_id")
    @Expose
    private int defaultViolationId;
    @ColumnInfo(name = "auto_delete")
    @SerializedName("auto_delete")
    @Expose
    private String autoDelete;
    @SerializedName("sync_data_id")
    @Expose
    @Ignore
    private int syncDataId;
    @SerializedName("primary_key")
    @Expose
    @Ignore
    private int primaryKey;

    public Duration() {

    }

    public Duration(JSONObject object) throws Exception {
        this.setId(object.getInt("duration_id"));
        this.setCustId(object.getInt("custid"));
        this.setTitle(object.getString("duration"));
        this.setDurationMinutes(!object.isNull("duration_mins") ? object.getInt("duration_mins") : 0);
        this.setOrderNumber(!object.isNull("order_number") ? object.getInt("order_number") : 0);
        this.setDefaultViolationId(!object.isNull("default_violation_id") ? object.getInt("default_violation_id") : 0);
        this.setAutoDelete(object.getString("auto_delete"));
    }

    public static ArrayList<Duration> getDurations() throws Exception {
        ArrayList<Duration> list = new ArrayList<Duration>();
        list = (ArrayList<Duration>) ParkingDatabase.getInstance(TPApplication.getInstance()).durationDao().getDurations();
        return list;
    }

    public static Duration getDurationById(int durationId) {
        Duration object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).durationDao().getDurationById(durationId);
        return object;
    }

    public static Duration getAutoDeleteById(int durationId) {
        Duration object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).durationDao().getAutoDeleteById(durationId);
        return object;
    }

    public static int getDurationIdByName(String duration) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).durationDao().getDurationIdByName(duration);
    }

    public static int getDurationMinsById(int durationId, Context context) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).durationDao().getDurationMinsById(durationId);
    }

    public static int getDurationMinsByName(String duration, Context context) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).durationDao().getDurationIdByName(duration);
    }

    public static String getDurationTitleById(int durationId) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).durationDao().getDurationTitleUsingId(durationId);
    }

    public static String getDurationTitleUsingId(int durationId) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).durationDao().getDurationTitleUsingId(durationId);
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).durationDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).durationDao().removeById(id);
    }

    public static void insertDuration(Duration Duration) {
        new Duration.InsertDuration(Duration).execute();
    }

    public int getSyncDataId() {
        return syncDataId;
    }

    public void setSyncDataId(int syncDataId) {
        this.syncDataId = syncDataId;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("duration_id", this.id);
        values.put("custid", this.custId);
        values.put("duration", this.title);
        values.put("duration_mins", this.durationMinutes);
        values.put("order_number", this.orderNumber);
        values.put("default_violation_id", this.defaultViolationId);
        values.put("auto_delete", this.autoDelete);

        return values;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getDefaultViolationId() {
        return defaultViolationId;
    }

    public void setDefaultViolationId(int defaultViolationId) {
        this.defaultViolationId = defaultViolationId;
    }

    public String getAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(String auto_delete) {
        this.autoDelete = auto_delete;
    }

    private static class InsertDuration extends AsyncTask<Void, Void, Void> {
        private Duration Duration;

        public InsertDuration(Duration Duration) {
            this.Duration = Duration;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).durationDao().insertDuration(Duration);
            return null;
        }
    }

}
