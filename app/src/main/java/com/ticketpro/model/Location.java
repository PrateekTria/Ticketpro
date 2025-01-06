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

@Entity(tableName = "locations")
public class Location {
    @PrimaryKey
    @ColumnInfo(name = "location_id")
    @SerializedName("location_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "location")
    @SerializedName("location")
    @Expose
    private String location;
    @ColumnInfo(name = "zone_id")
    @SerializedName("zone_id")
    @Expose
    private int zoneId;
    @ColumnInfo(name = "order_number")
    @SerializedName("order_number")
    @Expose
    private int orderNumber;
    @ColumnInfo(name = "is_manual")
    private String isManual;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;


    public Location() {
    }

    public Location(JSONObject object) throws Exception {
        this.setId(object.getInt("location_id"));
        this.setCustId(object.getInt("custid"));
        this.setLocation(object.getString("location"));
        this.setZoneId(!object.isNull("zone_id") ? object.getInt("zone_id") : 0);
        this.setOrderNumber(!object.isNull("order_number") ? object.getInt("order_number") : 0);
    }

    public static Location getLocationByText(String locationText) throws Exception {
        Location object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).locationsDao().getLocationByText(locationText);
        return object;
    }

    public static ArrayList<Location> getManualLocations() {
        ArrayList<Location> list = new ArrayList();
        list = (ArrayList<Location>) ParkingDatabase.getInstance(TPApplication.getInstance()).locationsDao().getManualLocations();
        return list;
    }

    public static int getLastInsertId() throws Exception {
        int lastId = 0;
        lastId = ParkingDatabase.getInstance(TPApplication.getInstance()).locationsDao().getLastInsertId();
        return lastId;
    }

    public static ArrayList<Location> getLocations(int custId) throws Exception {
        ArrayList<Location> list = new ArrayList<Location>();
        list = (ArrayList<Location>) ParkingDatabase.getInstance(TPApplication.getInstance()).locationsDao().getLocations();
        return list;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).locationsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).locationsDao().removeById(id);
    }

    public static void removeManuaLocation() throws Exception {
        try {
            String isManual = "Y";
            ParkingDatabase.getInstance(TPApplication.getInstance()).locationsDao().removeManuaLocation(isManual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertLocation(Location Location) {
        new Location.InsertLocation(Location).execute();
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
        values.put("location_id", this.id);
        values.put("custid", this.custId);
        values.put("location", this.location);
        values.put("zone_id", this.zoneId);
        values.put("order_number", this.orderNumber);
        values.put("is_manual", this.isManual);
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getIsManual() {
        return this.isManual;
    }

    public void setIsManual(String isManual) {
        this.isManual = isManual;
    }

    private static class InsertLocation extends AsyncTask<Void, Void, Void> {
        private Location Location;

        public InsertLocation(Location Location) {
            this.Location = Location;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).locationsDao().insertLocation(Location);
            return null;
        }
    }
}
