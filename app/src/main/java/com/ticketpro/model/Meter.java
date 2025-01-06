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

@Entity(tableName = "meters")
public class Meter {
    @PrimaryKey
    @ColumnInfo(name = "meter_id")
    @SerializedName("meter_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "meter")
    @SerializedName("meter")
    @Expose
    private String meter;
    @ColumnInfo(name = "location")
    @SerializedName("location")
    @Expose
    private String location;
    @ColumnInfo(name = "street_number")
    @SerializedName("street_number")
    @Expose
    private String streetNumber;
    @ColumnInfo(name = "street_prefix")
    @SerializedName("street_prefix")
    @Expose
    private String streetPrefix;
    @ColumnInfo(name = "street_suffix")
    @SerializedName("street_suffix")
    @Expose
    private String streetSuffix;
    @ColumnInfo(name = "direction")
    @SerializedName("direction")
    @Expose
    private String direction;
    @ColumnInfo(name = "violation_code")
    @SerializedName("violation_code")
    @Expose
    private String violationCode;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public Meter() {
    }

    public Meter(JSONObject object) throws Exception {

        this.setId(object.getInt("meter_id"));
        this.setCustId(object.getInt("custid"));
        this.setMeter(object.getString("meter"));
        this.setLocation(object.getString("location"));
        this.setStreetNumber(object.getString("street_number"));
        this.setStreetPrefix(object.getString("street_prefix"));
        this.setStreetSuffix(object.getString("street_suffix"));
        this.setDirection(object.getString("direction"));
        this.setViolationCode(object.getString("violation_code"));

    }

    public static ArrayList<Meter> getMeters(int custId) throws Exception {
        ArrayList<Meter> list = new ArrayList<Meter>();
        list = (ArrayList<Meter>) ParkingDatabase.getInstance(TPApplication.getInstance()).metersDao().getMeters();
        return list;
    }

    public static Meter searchMeterHistory(String meter) throws Exception {
        Meter object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).metersDao().searchMeterHistory(meter);
        return object;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).metersDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).metersDao().removeById(id);
    }

    public static void insertMeter(Meter Meter) {
        new Meter.InsertMeter(Meter).execute();
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
        values.put("meter_id", this.id);
        values.put("custid", this.custId);
        values.put("meter", this.meter);
        values.put("location", this.location);
        values.put("street_number", this.streetNumber);
        values.put("street_prefix", this.streetPrefix);
        values.put("street_suffix", this.streetSuffix);
        values.put("direction", this.direction);
        values.put("violation_code", this.violationCode);
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

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetPrefix() {
        return streetPrefix;
    }

    public void setStreetPrefix(String streetPrefix) {
        this.streetPrefix = streetPrefix;
    }

    public String getStreetSuffix() {
        return streetSuffix;
    }

    public void setStreetSuffix(String streetSuffix) {
        this.streetSuffix = streetSuffix;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getViolationCode() {
        return violationCode;
    }

    public void setViolationCode(String violationCode) {
        this.violationCode = violationCode;
    }

    private static class InsertMeter extends AsyncTask<Void, Void, Void> {
        private Meter Meter;

        public InsertMeter(Meter Meter) {
            this.Meter = Meter;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).metersDao().insertMeter(Meter);
            return null;
        }
    }

}
