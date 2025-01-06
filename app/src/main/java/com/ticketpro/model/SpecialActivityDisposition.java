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

@Entity(tableName = "special_activity_dispositions")
public class SpecialActivityDisposition {
    @PrimaryKey
    @ColumnInfo(name = "disposition_id")
    @SerializedName("disposition_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "disposition")
    @SerializedName("disposition")
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
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public SpecialActivityDisposition() {

    }

    public SpecialActivityDisposition(JSONObject object) throws Exception {

        this.setId(object.getInt("disposition_id"));
        this.setCustId(object.getInt("custid"));
        this.setTitle(object.getString("disposition"));
        this.setCode(object.getString("code"));
        this.setOrderNumber(!object.isNull("order_number") ? object.getInt("order_number") : 0);

    }

    public static ArrayList<SpecialActivityDisposition> getSpecialActivityDispositions() throws Exception {
        ArrayList<SpecialActivityDisposition> list = new ArrayList<SpecialActivityDisposition>();
        list = (ArrayList<SpecialActivityDisposition>) ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityDispositionDao().getSpecialActivityDispositions();
        return list;
    }

    public static int getSpecialActivityDispositionIdByName(String name) throws Exception {
        int result = 0;
        result = ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityDispositionDao().getSpecialActivityDispositionIdByName(name);
        return result;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityDispositionDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityDispositionDao().removeById(id);
    }

    public static void insertSpecialActivityDisposition(SpecialActivityDisposition SpecialActivityDisposition) {
        new SpecialActivityDisposition.InsertSpecialActivityDisposition(SpecialActivityDisposition).execute();
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
        values.put("disposition_id", this.id);
        values.put("custid", this.custId);
        values.put("disposition", this.title);
        values.put("code", this.code);
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

    private static class InsertSpecialActivityDisposition extends AsyncTask<Void, Void, Void> {
        private SpecialActivityDisposition SpecialActivityDisposition;

        public InsertSpecialActivityDisposition(SpecialActivityDisposition SpecialActivityDisposition) {
            this.SpecialActivityDisposition = SpecialActivityDisposition;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityDispositionDao().insertSpecialActivityDisposition(SpecialActivityDisposition);
            return null;
        }
    }

}
