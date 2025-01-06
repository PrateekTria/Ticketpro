package com.ticketpro.model;

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

import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "lprbodymap")
public class LprBodyMap {
    @PrimaryKey
    @ColumnInfo(name = "body_id")
    @SerializedName("body_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "LPRBody")
    @SerializedName("LPRBody")
    @Expose
    private String title;
    @ColumnInfo(name = "TicketBody")
    @SerializedName("TicketBody")
    @Expose
    private String code;
    @ColumnInfo(name = "order_number")
    @SerializedName("order_number")
    @Expose
    private int orderNumber;
    @SerializedName("sync_data_id")
    @Expose
    @Ignore
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;


    public LprBodyMap() {
    }
    public LprBodyMap(JSONObject object) throws Exception {
        this.setId(object.getInt("body_id"));
        this.setCustId(object.getInt("custid"));
        this.setTitle(object.getString("LPRBody"));
        this.setCode(object.getString("TicketBody"));
        this.setOrderNumber(!object.isNull("order_number") ? object.getInt("order_number") : 0);

    }

    public static ArrayList<LprBodyMap> getBodies(int custid) throws Exception {
        return (ArrayList<LprBodyMap>) ParkingDatabase.getInstance(TPApplication.getInstance()).lprbodyDao().getBodies().subscribeOn(Schedulers.io()).blockingGet();
    }

    public static LprBodyMap getBodyById(int bodyId) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).lprbodyDao().getBodyById(bodyId).subscribeOn(Schedulers.io()).blockingGet();
    }

    public static LprBodyMap getBodyByCode(String bodyCode) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).lprbodyDao().getBodyByCode(bodyCode).subscribeOn(Schedulers.io()).blockingGet();
    }

    public static LprBodyMap getBodyByTitle(String bodyTitle) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).lprbodyDao().getBodyByTitle(bodyTitle).subscribeOn(Schedulers.io()).blockingGet();
    }

    public static int getBodyIdByName(String name) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).lprbodyDao().getBodyIdByName(name).subscribeOn(Schedulers.io()).blockingGet();
    }

    public static int getBodyIdByCode(String code) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).lprbodyDao().getBodyIdByName(code).subscribeOn(Schedulers.io()).blockingGet();
    }

    public static String getBodyCodeByName(String name) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).lprbodyDao().getBodyCodeByName(name).subscribeOn(Schedulers.io()).blockingGet();
    }

    public static String getBodyCodeById(int bodyId) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).lprbodyDao().getBodyCodeById(bodyId).subscribeOn(Schedulers.io()).blockingGet();
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).lprbodyDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).lprbodyDao().removeById(id);
    }


    public static void insertBody(LprBodyMap Body) {
        new LprBodyMap.InsertBody(Body).execute();
    }

    private static class InsertBody extends AsyncTask<Void, Void, Void> {
        private LprBodyMap Body;

        public InsertBody(LprBodyMap Body) {
            this.Body = Body;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).lprbodyDao().insertBody(Body);
            return null;
        }
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
}
