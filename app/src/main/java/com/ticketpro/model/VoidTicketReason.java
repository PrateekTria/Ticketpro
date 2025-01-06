package com.ticketpro.model;

import android.content.ContentValues;
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

@Entity(tableName = "void_ticket_reasons")
public class VoidTicketReason {
    @PrimaryKey
    @ColumnInfo(name = "reason_id")
    @SerializedName("reason_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "reason_title")
    @SerializedName("reason_title")
    @Expose
    private String title;
    @ColumnInfo(name = "reason_code")
    @SerializedName("reason_code")
    @Expose
    private String code;
    @ColumnInfo(name = "order_number")
    @SerializedName("order_number")
    @Expose
    private int orderNumber;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public VoidTicketReason() {

    }

    public VoidTicketReason(JSONObject object) throws Exception {
        this.setId(object.getInt("reason_id"));
        this.setCustId(object.getInt("custid"));
        this.setTitle(object.getString("reason_title"));
        this.setCode(object.getString("reason_code"));
        this.setOrderNumber(!object.isNull("order_number") ? object.getInt("order_number") : 0);
    }

    public static ArrayList<VoidTicketReason> getVoidReasons(int custId) throws Exception {
        ArrayList<VoidTicketReason> list = new ArrayList<VoidTicketReason>();
        list = (ArrayList<VoidTicketReason>) ParkingDatabase.getInstance(TPApplication.getInstance()).voidTicketReasonsDao().getVoidReasons();
        return list;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).voidTicketReasonsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).voidTicketReasonsDao().removeById(id);
    }

    public static void insertVoidTicketReason(VoidTicketReason VoidTicketReason) {
        new VoidTicketReason.InsertVoidTicketReason(VoidTicketReason).execute();
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
        values.put("reason_id", this.id);
        values.put("custid", this.custId);
        values.put("reason_title", this.title);
        values.put("reason_code", this.code);
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

    private static class InsertVoidTicketReason extends AsyncTask<Void, Void, Void> {
        private VoidTicketReason VoidTicketReason;

        public InsertVoidTicketReason(VoidTicketReason VoidTicketReason) {
            this.VoidTicketReason = VoidTicketReason;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).voidTicketReasonsDao().insertVoidTicketReason(VoidTicketReason);
            return null;
        }
    }

}
