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

@Entity(tableName = "location_group_locations")
public class LocationGroupLocation {
    @PrimaryKey
    @ColumnInfo(name = "location_group_id")
    @SerializedName("location_group_id")
    @Expose
    private int locationGroupId;
    @ColumnInfo(name = "group_id")
    @SerializedName("group_id")
    @Expose
    private int groupId;
    @ColumnInfo(name = "location_id")
    @SerializedName("location_id")
    @Expose
    private int locationId;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public LocationGroupLocation() {

    }

    public LocationGroupLocation(JSONObject object) throws Exception {

        this.setGroupId(object.getInt("group_id"));
        this.setLocationGroupId(object.getInt("location_group_id"));
        this.setLocationId(object.getInt("location_id"));

    }

    public static ArrayList<LocationGroupLocation> getLocationGroupLocations(int custid) throws Exception {
        ArrayList<LocationGroupLocation> list = new ArrayList<LocationGroupLocation>();
        list = (ArrayList<LocationGroupLocation>) ParkingDatabase.getInstance(TPApplication.getInstance()).locationGroupLocationsDao().getLocationGroupLocations();
        return list;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).locationGroupLocationsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).locationGroupLocationsDao().removeById(id);
    }

    public static void insertLocationGroupLocation(LocationGroupLocation LocationGroupLocation) {
        new LocationGroupLocation.InsertLocationGroupLocation(LocationGroupLocation).execute();
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
        values.put("group_id", this.groupId);
        values.put("location_group_id", this.locationGroupId);
        values.put("location_id", this.locationId);

        return values;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getLocationGroupId() {
        return locationGroupId;
    }

    public void setLocationGroupId(int locationGroupId) {
        this.locationGroupId = locationGroupId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    private static class InsertLocationGroupLocation extends AsyncTask<Void, Void, Void> {
        private LocationGroupLocation LocationGroupLocation;

        public InsertLocationGroupLocation(LocationGroupLocation LocationGroupLocation) {
            this.LocationGroupLocation = LocationGroupLocation;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).locationGroupLocationsDao().insertLocationGroupLocation(LocationGroupLocation);
            return null;
        }
    }

}
