package com.ticketpro.model;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "hotlist")
public class Hotlist {
    @PrimaryKey
    @ColumnInfo(name = "hotlist_id")
    @SerializedName("hotlist_id")
    @Expose
    private int hostlistId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "state_code")
    @SerializedName("state_code")
    @Expose
    private String stateCode;
    @ColumnInfo(name = "plate")
    @SerializedName("plate")
    @Expose
    private String plate;
    @ColumnInfo(name = "plate_type")
    @SerializedName("plate_type")
    @Expose
    private String plateType;
    @ColumnInfo(name = "begin_date")
    @SerializedName("begin_date")
    @Expose
    private Date beginDate;
    @ColumnInfo(name = "end_date")
    @SerializedName("end_date")
    @Expose
    private Date endDate;
    @ColumnInfo(name = "comments")
    @SerializedName("comments")
    @Expose
    private String comments;
    @ColumnInfo(name = "number_of_tickets")
    @SerializedName("number_of_tickets")
    @Expose
    private int numberOfTickets;
    @ColumnInfo(name = "amount_owed")
    @SerializedName("amount_owed")
    @Expose
    private double amountOwed;
    @ColumnInfo(name = "date_added")
    @SerializedName("date_added")
    @Expose
    private Date dateAdded;
    @ColumnInfo(name = "is_active")
    @SerializedName("is_active")
    @Expose
    private String active;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public Hotlist() {

    }

    public Hotlist(JSONObject object) throws Exception {

        this.setHostlistId(object.getInt("hotlist_id"));
        this.setCustId(object.getInt("custid"));
        this.setStateCode(object.getString("state_code"));
        this.setPlate(object.getString("plate"));
        this.setPlateType(object.getString("plate_type"));
        this.setBeginDate(DateUtil.getDateFromSQLString(object.getString("begin_date")));
        this.setEndDate(DateUtil.getDateFromSQLString(object.getString("end_date")));
        this.setComments(object.getString("comments"));
        this.setNumberOfTickets(!object.isNull("number_of_tickets") ? object.getInt("number_of_tickets") : 0);
        this.setAmountOwed(!object.isNull("amount_owed") ? object.getDouble("amount_owed") : 0);
        //this.setDateAdded(DateUtil.getDateFromSQLString(object.getString("date_added")));
        this.setActive(object.getString("is_active"));
    }

    public static ArrayList<Hotlist> getHostlist() throws Exception {
        ArrayList<Hotlist> list = new ArrayList<Hotlist>();
        list = (ArrayList<Hotlist>) ParkingDatabase.getInstance(TPApplication.getInstance()).hotlistDao().getHostlist();
        return list;
    }

    public static Hotlist getHostlistReportByPrimaryKey(String hotListId) throws TPException {
        Hotlist object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).hotlistDao().getHostlistReportByPrimaryKey(hotListId);
        return object;
    }

    public static ArrayList<Hotlist> searchHotlist(String plate, String state) throws Exception {
        ArrayList<Hotlist> list = new ArrayList<Hotlist>();
        list = (ArrayList<Hotlist>) ParkingDatabase.getInstance(TPApplication.getInstance()).hotlistDao().searchHotlist(plate, state);
        return list;
    }

    public static int getNextPrimaryId() {
        int nextId = 0;
        try {
            nextId = ParkingDatabase.getInstance(TPApplication.getInstance()).hotlistDao().getNextPrimaryId();
        } catch (Exception ignored) {

        }
        return nextId + 1;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).hotlistDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).hotlistDao().removeById(id);
    }

    public static Completable insertHotlist(final Hotlist Hotlist) {
        final ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                instance.hotlistDao().insertHotlist(Hotlist).subscribeOn(Schedulers.io()).subscribe();
            }
        });
        //new Hotlist.InsertHotlist(Hotlist).execute();
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

    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        try {
            object.put("hotlist_id", this.hostlistId);
            object.put("custid", this.custId);
            object.put("state_code", this.stateCode);
            object.put("plate", this.plate);
            object.put("plate_type", this.plateType);
            object.put("begin_date", this.beginDate);
            object.put("end_date", this.endDate);
            object.put("comments", this.comments);
            object.put("number_of_tickets", this.numberOfTickets);
            object.put("amount_owed", this.amountOwed);
            object.put("is_active", this.active);

        } catch (Exception e) {
            Log.e("CallInReport", "Error " + e.getMessage());
        }

        return object;
    }

    public ContentValues getContentValues() throws Exception {

        ContentValues values = new ContentValues();
        values.put("hotlist_id", this.hostlistId);
        values.put("custid", this.custId);
        values.put("state_code", this.stateCode);
        values.put("plate", this.plate);
        values.put("plate_type", this.plateType);
        values.put("begin_date", DateUtil.getSQLStringFromDate2(this.beginDate));
        values.put("end_date", DateUtil.getSQLStringFromDate2(this.endDate));
        values.put("comments", this.comments);
        values.put("number_of_tickets", this.numberOfTickets);
        values.put("amount_owed", this.amountOwed);
        values.put("date_added", DateUtil.getSQLStringFromDate2(this.dateAdded));
        values.put("is_active", this.active);

        return values;

    }

    public int getHostlistId() {
        return hostlistId;
    }

    public void setHostlistId(int hostlistId) {
        this.hostlistId = hostlistId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public double getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(double amountOwed) {
        this.amountOwed = amountOwed;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
