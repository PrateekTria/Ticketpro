package com.ticketpro.model;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.text.TextUtils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "duties")
public class Duty implements Serializable {
    @Ignore
    private static final long serialVersionUID = 1L;
    @PrimaryKey
    @ColumnInfo(name = "duty_id")
    @SerializedName("duty_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "duty")
    @SerializedName("duty")
    @Expose
    private String title;
    @ColumnInfo(name = "code")
    @SerializedName("code")
    @Expose
    private String code;
    @ColumnInfo(name = "order_number")
    @SerializedName("order_number")
    @Expose
    private int orderNumber;
    @ColumnInfo(name = "allow_ticket")
    @SerializedName("allow_ticket")
    @Expose
    private String allowTicket;
    @ColumnInfo(name = "location_groups")
    @SerializedName("location_groups")
    @Expose
    private String locationGroups;
    @ColumnInfo(name = "comment_groups")
    @SerializedName("comment_groups")
    @Expose
    private String commentGroups;
    @ColumnInfo(name = "violation_groups")
    @SerializedName("violation_groups")
    @Expose
    private String violationGroups;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public Duty() {

    }

    public Duty(JSONObject object) throws Exception {

        this.setId(object.getInt("duty_id"));
        this.setCustId(object.getInt("custid"));
        this.setTitle(object.getString("duty"));
        this.setCode(object.getString("code"));
        this.setAllowTicket(object.getString("allow_ticket"));
        this.setLocationGroups(object.getString("location_groups"));
        this.setCommentGroups(object.getString("comment_groups"));
        this.setViolationGroups(object.getString("violation_groups"));
        this.setOrderNumber(!object.isNull("order_number") ? object.getInt("order_number") : 0);
    }

    public static ArrayList<Duty> getDuties() throws Exception {
        ArrayList<Duty> list = new ArrayList<Duty>();
        list = (ArrayList<Duty>) ParkingDatabase.getInstance(TPApplication.getInstance()).dutiesDao().getDuties();
        return list;
    }

    public static Duty getDutyById(int dutyId) throws Exception {
        Duty object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).dutiesDao().getDutyById(dutyId);
        return object;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).dutiesDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).dutiesDao().removeById(id);
    }

    public static void insertDuty(final Duty Duty) {
        final ParkingDatabase database = ParkingDatabase.getInstance(TPApplication.getInstance());
        Completable completable = Completable.fromAction(() -> database.dutiesDao().insertDuty(Duty).subscribeOn(Schedulers.io()).subscribe());
        completable.subscribe();//new Duty.InsertDuty(Duty).execute();
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
        values.put("duty_id", this.id);
        values.put("custid", this.custId);
        values.put("duty", this.title);
        values.put("code", this.code);
        values.put("allow_ticket", this.allowTicket);
        values.put("location_groups", this.locationGroups);
        values.put("comment_groups", this.commentGroups);
        values.put("violation_groups", this.violationGroups);
        values.put("order_number", this.orderNumber);

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getAllowTicket() {
        return allowTicket;
    }

    public void setAllowTicket(String allowTicket) {
        this.allowTicket = allowTicket;
    }

    public String getLocationGroups() {
        return locationGroups;
    }

    public void setLocationGroups(String locationGroups) {
        this.locationGroups = locationGroups;
    }

    public String getCommentGroups() {
        return commentGroups;
    }

    public void setCommentGroups(String commentGroups) {
        this.commentGroups = commentGroups;
    }

    public String getViolationGroups() {
        return violationGroups;
    }

    public void setViolationGroups(String violationGroups) {
        this.violationGroups = violationGroups;
    }

    public boolean isAllLocations() {
        if (this.locationGroups != null && !TextUtils.isEmpty(this.locationGroups)) {
            String[] groups = this.locationGroups.split(",");
            for (String group : groups) {
                if (group.equalsIgnoreCase("ALL")) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAllComments() {
        if (this.commentGroups != null) {
            String[] groups = this.commentGroups.split(",");
            for (String group : groups) {
                if (group.equalsIgnoreCase("ALL")) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isAllViolations() {
        if (this.violationGroups != null) {
            String[] groups = this.violationGroups.split(",");
            for (String group : groups) {
                if (group.equalsIgnoreCase("ALL")) {
                    return true;
                }
            }
        }

        return false;
    }

    private static class InsertDuty extends AsyncTask<Void, Void, Void> {
        private Duty Duty;

        public InsertDuty(Duty Duty) {
            this.Duty = Duty;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).dutiesDao().insertDuty(Duty);
            return null;
        }
    }

}
