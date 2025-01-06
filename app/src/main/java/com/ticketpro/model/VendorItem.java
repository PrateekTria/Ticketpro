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

@Entity(tableName = "vendor_items")
public class VendorItem {
    @PrimaryKey
    @ColumnInfo(name = "item_id")
    @SerializedName("item_id")
    @Expose
    private int itemId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "vendor_id")
    @SerializedName("vendor_id")
    @Expose
    private int vendorId;
    @ColumnInfo(name = "item_code")
    @SerializedName("item_code")
    @Expose
    private String itemCode;
    @ColumnInfo(name = "item_name")
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @ColumnInfo(name = "item_type")
    @SerializedName("item_type")
    @Expose
    private String itemType;
    @ColumnInfo(name = "duration")
    @SerializedName("duration")
    @Expose
    private int duration;
    @ColumnInfo(name = "violation_id")
    @SerializedName("violation_id")
    @Expose
    private int violationId;
    @ColumnInfo(name = "is_active")
    private String isActive;
    @SerializedName("order_number")
    @Expose
    @ColumnInfo(name = "order_number")
    private int orderNumber = 0;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public VendorItem() {

    }

    public VendorItem(JSONObject object) throws Exception {
        this.setItemId(!object.isNull("item_id") ? object.getInt("item_id") : 0);
        this.setVendorId(!object.isNull("vendor_id") ? object.getInt("vendor_id") : 0);
        this.setCustId(!object.isNull("custid") ? object.getInt("custid") : 0);
        this.setOrderNumber(!object.isNull("order_number") ? object.getInt("order_number") : 0);
        this.setDuration(!object.isNull("duration") ? object.getInt("duration") : 0);
        this.setViolationId(!object.isNull("violation_id") ? object.getInt("violation_id") : 0);
        this.setItemName(object.getString("item_name"));
        this.setItemCode(object.getString("item_code"));
        this.setItemType(object.getString("item_type"));
    }

    public static ArrayList<VendorItem> getVendorItems() throws Exception {
        ArrayList<VendorItem> list = new ArrayList<VendorItem>();
        list = (ArrayList<VendorItem>) ParkingDatabase.getInstance(TPApplication.getInstance()).vendorItemsDao().getVendorItems();
        return list;
    }

    public static ArrayList<VendorItem> getVendorZones(int vendorId) throws Exception {
        ArrayList<VendorItem> list = new ArrayList<VendorItem>();
        list = (ArrayList<VendorItem>) ParkingDatabase.getInstance(TPApplication.getInstance()).vendorItemsDao().getVendorZones(vendorId);
        return list;
    }

    public static ArrayList<VendorItem> getVendorSamtrans(int vendorId) throws Exception {
        ArrayList<VendorItem> list = new ArrayList<VendorItem>();
        list = (ArrayList<VendorItem>) ParkingDatabase.getInstance(TPApplication.getInstance()).vendorItemsDao().getVendorSamtrans(vendorId);
        return list;
    }

    public static ArrayList<VendorItem> getVendorItems(int vendorId, String itemType) throws Exception {
        ArrayList<VendorItem> list = new ArrayList<VendorItem>();
        list = (ArrayList<VendorItem>) ParkingDatabase.getInstance(TPApplication.getInstance()).vendorItemsDao().getVendorItems(vendorId, itemType);
        return list;
    }

    public static VendorItem getVendorZone(String zoneString) throws Exception {
        VendorItem object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).vendorItemsDao().getVendorZone(zoneString);
        return object;
    }

    public static VendorItem getVendorItemByName(String itemName) throws Exception {
        VendorItem object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).vendorItemsDao().getVendorItemByName(itemName);
        return object;
    }

    public static VendorItem getVendorZoneByType(String itemType) throws Exception {
        VendorItem object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).vendorItemsDao().getVendorZoneByType(itemType);
        return object;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).vendorItemsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).vendorItemsDao().removeById(id);
    }

    public static void insertVendorItem(VendorItem VendorItem) {
        new VendorItem.InsertVendorItem(VendorItem).execute();
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
        values.put("item_id", this.itemId);
        values.put("custid", this.custId);
        values.put("duration", this.duration);
        values.put("violation_id", this.violationId);
        values.put("item_name", this.itemName);
        values.put("item_code", this.itemCode);
        values.put("item_type", this.itemType);

        return values;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getViolationId() {
        return violationId;
    }

    public void setViolationId(int violationId) {
        this.violationId = violationId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    private static class InsertVendorItem extends AsyncTask<Void, Void, Void> {
        private VendorItem VendorItem;

        public InsertVendorItem(VendorItem VendorItem) {
            this.VendorItem = VendorItem;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).vendorItemsDao().insertVendorItem(VendorItem);
            return null;
        }
    }
}
