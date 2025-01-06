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

@Entity(tableName = "zones")
public class Zone {
    @PrimaryKey
    @ColumnInfo(name = "zone_id")
    @SerializedName("zone_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "zone")
    @SerializedName("zone")
    @Expose
    private String title;
    @ColumnInfo(name = "time_diff")
    @SerializedName("time_diff")
    @Expose
    private float timeDiff;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public Zone() {

    }

    public Zone(JSONObject object) throws Exception {
        this.setId(object.getInt("zone_id"));
        this.setCustId(object.getInt("custid"));
        this.setTitle(object.getString("zone"));
        this.setTimeDiff(!object.isNull("time_diff") ? object.getInt("time_diff") : 0);
    }

    public static ArrayList<Zone> getZones( int custId) throws Exception {
        ArrayList<Zone> list;
        list = (ArrayList<Zone>) ParkingDatabase.getInstance(TPApplication.getInstance()).zonesDao().getZones();
        return list;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).zonesDao().removeAll();
    }

    public static void removeById( int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).zonesDao().removeById(id);
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
        values.put("zone_id", this.id);
        values.put("custid", this.custId);
        values.put("zone", this.title);
        values.put("time_diff", this.timeDiff);
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

    public float getTimeDiff() {
        return timeDiff;
    }

    public void setTimeDiff(float timeDiff) {
        this.timeDiff = timeDiff;
    }

    public static void insertZone(Zone Zone){
        new Zone.InsertZone(Zone).execute();
    }

    private static class InsertZone extends AsyncTask<Void,Void,Void> {
        private Zone Zone;

        public InsertZone(Zone Zone) {
            this.Zone = Zone;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).zonesDao().insertZone(Zone);
            return null;
        }
    }

}
