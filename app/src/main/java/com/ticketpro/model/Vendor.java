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

@Entity(tableName = "vendors")
public class Vendor {
    @PrimaryKey
    @ColumnInfo(name = "vendor_id")
    @SerializedName("vendor_id")
    @Expose
    private int vendorId;
    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name;
    @ColumnInfo(name = "address")
    @SerializedName("address")
    @Expose
    private String address;
    @ColumnInfo(name = "contact_number")
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public Vendor() {

    }

    public Vendor(JSONObject object) throws Exception {
        this.setVendorId(!object.isNull("vendor_id") ? object.getInt("vendor_id") : 0);
        this.setName(object.getString("name"));
        this.setAddress(object.getString("address"));
        this.setContactNumber(object.getString("contact_number"));
    }

    public static ArrayList<Vendor> getVendors() throws Exception {
        ArrayList<Vendor> list = new ArrayList<Vendor>();

        list = (ArrayList<Vendor>) ParkingDatabase.getInstance(TPApplication.getInstance()).vendorsDao().getVendors();
        return list;
    }

    public static Vendor getVendorByName(String name) throws Exception {
        Vendor vendor = null;
        vendor = ParkingDatabase.getInstance(TPApplication.getInstance()).vendorsDao().getVendorByName(name);
        return vendor;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).vendorsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).vendorsDao().removeById(id);
    }

    public static void insertVendor(Vendor Vendor) {
        new Vendor.InsertVendor(Vendor).execute();
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
        values.put("vendor_id", this.vendorId);
        values.put("name", this.name);
        values.put("address", this.address);
        values.put("contact_number", this.contactNumber);

        return values;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    private static class InsertVendor extends AsyncTask<Void, Void, Void> {
        private Vendor Vendor;

        public InsertVendor(Vendor Vendor) {
            this.Vendor = Vendor;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).vendorsDao().insertVendor(Vendor);
            return null;
        }
    }

}
