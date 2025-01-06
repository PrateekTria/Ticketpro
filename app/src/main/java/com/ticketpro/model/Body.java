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
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "bodies")
public class Body {
    @PrimaryKey
    @ColumnInfo(name = "body_id")
    @SerializedName("body_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "body")
    @SerializedName("body")
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
    @SerializedName("sync_data_id")
    @Expose
    @Ignore
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public Body() {

    }

    public Body(JSONObject object) throws Exception {

        this.setId(object.getInt("body_id"));
        this.setCustId(object.getInt("custid"));
        this.setTitle(object.getString("body"));
        this.setCode(object.getString("code"));
        this.setOrderNumber(!object.isNull("order_number") ? object.getInt("order_number") : 0);

    }

    public static ArrayList<Body> getBodies(int custid) throws Exception {
        return (ArrayList<Body>) ParkingDatabase.getInstance(TPApplication.getInstance()).bodyDao().getBodies().subscribeOn(Schedulers.io()).blockingGet();
    }

    public static Body getBodyById(int bodyId) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).bodyDao().getBodyById(bodyId).subscribeOn(Schedulers.io()).blockingGet();
    }

    public static Body getBodyByCode(String bodyCode) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).bodyDao().getBodyByCode(bodyCode).subscribeOn(Schedulers.io()).blockingGet();
    }

    public static Body getBodyByTitle(String bodyTitle) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).bodyDao().getBodyByTitle(bodyTitle).subscribeOn(Schedulers.io()).blockingGet();
    }

    public static int getBodyIdByName(String name) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).bodyDao().getBodyIdByName(name).subscribeOn(Schedulers.io()).blockingGet();
    }

    public static int getBodyIdByCode(String code) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).bodyDao().getBodyIdByName(code).subscribeOn(Schedulers.io()).blockingGet();
    }

    public static String getBodyCodeByName(String name) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).bodyDao().getBodyCodeByName(name).subscribeOn(Schedulers.io()).blockingGet();
    }

    public static String getBodyCodeById(int bodyId) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).bodyDao().getBodyCodeById(bodyId).subscribeOn(Schedulers.io()).blockingGet();
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).bodyDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).bodyDao().removeById(id);
    }

    public static String getBodyStandardName(String bodyName) {
        if (bodyName == null) {
            return null;
        }
        final Map<String, String> map = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        map.put("antique", "UNK");
        map.put("sedan-compact", "SEDAN");
        map.put("sedan-convertible", "CONVERTIBLE");
        map.put("sedan-sport", "SEDAN");
        map.put("sedan-standard", "SEDAN");
        map.put("sedan-wagon", "STATION WAGON");
        map.put("suv-crossover", "SUV");
        map.put("suv-standard", "SUV");
        map.put("truck-standard", "TRUCK");
        map.put("van-full", "VAN");
        map.put("van-mini", "MINIVAN");

        String body = map.get(bodyName);
        if (body == null) {
            return "UNK";
        }

        return body;
    }

    public static void insertBody(Body Body) {
        new Body.InsertBody(Body).execute();
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
        values.put("body_id", this.id);
        values.put("custid", this.custId);
        values.put("body", this.title);
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

    private static class InsertBody extends AsyncTask<Void, Void, Void> {
        private Body Body;

        public InsertBody(Body Body) {
            this.Body = Body;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).bodyDao().insertBody(Body);
            return null;
        }
    }
}
