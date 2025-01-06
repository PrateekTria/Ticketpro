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

@Entity(tableName = "street_prefixes")
public class StreetPrefix {
    @PrimaryKey
    @ColumnInfo(name = "prefix_id")
    @SerializedName("prefix_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "street_prefix")
    @SerializedName("street_prefix")
    @Expose
    private String title;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public StreetPrefix() {

    }

    public StreetPrefix(JSONObject object) throws Exception {
        this.setId(object.getInt("prefix_id"));
        this.setCustId(object.getInt("custid"));
        this.setTitle(object.getString("street_prefix"));
    }

    public static ArrayList<StreetPrefix> getStreetPrefixes(int custId) throws Exception {
        ArrayList<StreetPrefix> list = new ArrayList<StreetPrefix>();
        list = (ArrayList<StreetPrefix>) ParkingDatabase.getInstance(TPApplication.getInstance()).streetPrefixesDao().getStreetPrefixes();
        return list;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).streetPrefixesDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).streetPrefixesDao().removeById(id);
    }

    public static void insertStreetPrefix(StreetPrefix StreetPrefix) {
        new StreetPrefix.InsertStreetPrefix(StreetPrefix).execute();
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
        values.put("prefix_id", this.id);
        values.put("custid", this.custId);
        values.put("street_prefix", this.title);
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

    private static class InsertStreetPrefix extends AsyncTask<Void, Void, Void> {
        private StreetPrefix StreetPrefix;

        public InsertStreetPrefix(StreetPrefix StreetPrefix) {
            this.StreetPrefix = StreetPrefix;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).streetPrefixesDao().insertStreetPrefix(StreetPrefix);
            return null;
        }
    }

}
