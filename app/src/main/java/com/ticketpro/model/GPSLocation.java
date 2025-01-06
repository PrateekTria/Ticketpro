package com.ticketpro.model;

import android.content.ContentValues;
import android.location.Location;
import android.os.AsyncTask;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.DateUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "gps_locations")
public class GPSLocation {
    @PrimaryKey
    @ColumnInfo(name = "location_id")
    @SerializedName("location_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "latitude")
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @ColumnInfo(name = "gpstime")
    @SerializedName("gpstime")
    @Expose
    private Date gpstime;
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
    @Ignore
    private Location providerLocation;
    @SerializedName("sync_data_id")
    @Expose
    @Ignore
    private int syncDataId;
    @SerializedName("primary_key")
    @Expose
    @Ignore
    private int primaryKey;

    public GPSLocation() {

    }

    public GPSLocation(JSONObject object) throws Exception {
        this.setId(object.getInt("location_id"));
        this.setCustId(object.getInt("custid"));
        this.setLatitude(object.getString("latitude"));
        this.setLongitude(object.getString("longitude"));
        this.setGpstime(DateUtil.getDateFromSQLString(object.getString("gpstime")));
        this.setLocation(object.getString("location"));
        this.setStreetNumber(object.getString("street_number"));
        this.setStreetPrefix(object.getString("street_prefix"));
        this.setStreetSuffix(object.getString("street_suffix"));
    }

    public static ArrayList<GPSLocation> getGPSLocations(int custId) throws Exception {
        ArrayList<GPSLocation> list = new ArrayList<GPSLocation>();
        list = (ArrayList<GPSLocation>) ParkingDatabase.getInstance(TPApplication.getInstance()).gpsLocationsDao().getGPSLocations();
        return list;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).gpsLocationsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).gpsLocationsDao().removeById(id);
    }

    public static void insertGPSLocation(GPSLocation GPSLocation) {
        new GPSLocation.InsertGPSLocation(GPSLocation).execute();
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
        values.put("latitude", this.latitude);
        values.put("longitude", this.longitude);
        values.put("gpstime", DateUtil.getSQLStringFromDate2(this.gpstime));
        values.put("location", this.location);
        values.put("street_number", this.streetNumber);
        values.put("street_prefix", this.streetPrefix);
        values.put("street_suffix", this.streetSuffix);

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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getGpstime() {
        return gpstime;
    }

    public void setGpstime(Date gpstime) {
        this.gpstime = gpstime;
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

    public Location getProviderLocation() {
        return providerLocation;
    }

    public void setProviderLocation(Location providerLocation) {
        this.providerLocation = providerLocation;
    }

    private static class InsertGPSLocation extends AsyncTask<Void, Void, Void> {
        private GPSLocation GPSLocation;

        public InsertGPSLocation(GPSLocation GPSLocation) {
            this.GPSLocation = GPSLocation;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).gpsLocationsDao().insertGPSLocation(GPSLocation);
            return null;
        }
    }
}
