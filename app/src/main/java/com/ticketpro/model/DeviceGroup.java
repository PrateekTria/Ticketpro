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

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "device_groups")
public class DeviceGroup {
    @PrimaryKey
    @ColumnInfo(name = "group_id")
    @SerializedName("group_id")
    @Expose
    private int group_id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "group_name")
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @ColumnInfo(name = "device_ids")
    @SerializedName("device_ids")
    @Expose
    private String deviceId;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public DeviceGroup(JSONObject object) throws Exception {
        this.setGroup_id(object.getInt("group_id"));
        this.setCustId(object.getInt("custid"));
        this.setGroupName(object.getString("group_name"));
        this.setDeviceId(object.getString("device_ids"));
    }

    public DeviceGroup() {
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).deviceGroupsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).deviceGroupsDao().removeById(id);
    }

    public static String getDevicesIds(String group_name) throws Exception {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).deviceGroupsDao().getDevicesIds(group_name);
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
        values.put("group_id", this.group_id);
        values.put("custid", this.custId);
        values.put("group_name", this.groupName);
        values.put("device_ids", this.deviceId);

        return values;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public static void insertDeviceGroup(final DeviceGroup DeviceGroup){
        final ParkingDatabase database = ParkingDatabase.getInstance(TPApplication.getInstance());
        Completable completable = Completable.fromAction(() -> database.deviceGroupsDao().insertDeviceGroup(DeviceGroup).subscribeOn(Schedulers.io()).subscribe());
        completable.subscribe();// new DeviceGroup.InsertDeviceGroup(DeviceGroup).execute();
    }

    private static class InsertDeviceGroup extends AsyncTask<Void,Void,Void> {
        private DeviceGroup DeviceGroup;

        public InsertDeviceGroup(DeviceGroup DeviceGroup) {
            this.DeviceGroup = DeviceGroup;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).deviceGroupsDao().insertDeviceGroup(DeviceGroup);
            return null;
        }
    }
}
