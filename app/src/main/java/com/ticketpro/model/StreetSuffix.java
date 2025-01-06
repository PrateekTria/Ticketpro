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

@Entity(tableName = "street_suffixes")
public class StreetSuffix {
    @PrimaryKey
    @ColumnInfo(name = "suffix_id")
    @SerializedName("suffix_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "street_suffix")
    @SerializedName("street_suffix")
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

    public StreetSuffix() {
    }

    public StreetSuffix(JSONObject object) throws Exception {
        this.setId(object.getInt("suffix_id"));
        this.setCustId(object.getInt("custid"));
        this.setTitle(object.getString("street_suffix"));
    }

    public static ArrayList<StreetSuffix> getStreetSuffixes() throws Exception {
        ArrayList<StreetSuffix> list = new ArrayList<StreetSuffix>();
        list = (ArrayList<StreetSuffix>) ParkingDatabase.getInstance(TPApplication.getInstance()).streetSuffixesDao().getStreetSuffixes();
        return list;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).streetSuffixesDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).streetSuffixesDao().removeById(id);
    }

    public static void insertStreetSuffix(StreetSuffix StreetSuffix) {
        new StreetSuffix.InsertStreetSuffix(StreetSuffix).execute();
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
        values.put("suffix_id", this.id);
        values.put("custid", this.custId);
        values.put("street_suffix", this.title);
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

    private static class InsertStreetSuffix extends AsyncTask<Void, Void, Void> {
        private StreetSuffix StreetSuffix;

        public InsertStreetSuffix(StreetSuffix StreetSuffix) {
            this.StreetSuffix = StreetSuffix;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).streetSuffixesDao().insertStreetSuffix(StreetSuffix);
            return null;
        }
    }

}
