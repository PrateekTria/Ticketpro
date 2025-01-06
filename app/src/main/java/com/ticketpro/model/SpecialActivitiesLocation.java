package com.ticketpro.model;

import android.content.ContentValues;
import android.database.SQLException;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "special_activities_location")
public class SpecialActivitiesLocation implements Serializable {
    @PrimaryKey
    @SerializedName("recid")
    @Expose
    @ColumnInfo(name = "recid")
    private int recid;
    @SerializedName("custid")
    @Expose
    @ColumnInfo(name = "custid")
    private int custid;
    @SerializedName("Location")
    @Expose
    @ColumnInfo(name = "location")
    private String location;
    @SerializedName("is_active")
    @Expose
    @ColumnInfo(name = "is_active")
    private String isActive;
    @SerializedName("order_number")
    @Expose
    @ColumnInfo(name = "order_number")
    private int order_number;


    public SpecialActivitiesLocation() {
    }

    public SpecialActivitiesLocation(JSONObject object) throws Exception {
        this.setRecid(object.getInt("recid"));
        this.setCustid(object.getInt("custid"));
        this.setLocation(object.getString("Location"));
        this.setOrder_number(!object.isNull("order_number") ? object.getInt("order_number") : 0);
        this.setIsActive(object.getString("is_active"));
    }

    public static void insertSpecialActivitiesLocation(SpecialActivitiesLocation activitiesLocation) {
        ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityLocationDao().insertSpecialActivitiesLocation(activitiesLocation).subscribeOn(Schedulers.io()).subscribe();
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityLocationDao().removeAll();
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("recid", this.recid);
        values.put("custid", this.custid);
        values.put("location", this.location);
        values.put("order_number", this.order_number);
        values.put("is_active", this.isActive);

        return values;
    }

    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        try {
            object.put("recid", this.recid);
            object.put("custid", this.custid);
            object.put("location", this.location);
            object.put("order_number", this.order_number);
            object.put("is_active", this.isActive);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public ArrayList<String> getSpecialLocation(int custid) {
        ArrayList<String> aList = null;
        try {
            aList = new ArrayList<>();
            aList = (ArrayList<String>) ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityLocationDao().getSpecialLocation(custid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aList;
    }

    public int getRecid() {
        return recid;
    }

    public void setRecid(int recid) {
        this.recid = recid;
    }

    public int getCustid() {
        return custid;
    }

    public void setCustid(int custid) {
        this.custid = custid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public int getOrder_number() {
        return order_number;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }
}
