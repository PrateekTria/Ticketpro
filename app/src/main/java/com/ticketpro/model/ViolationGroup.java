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

@Entity(tableName = "violation_groups")
public class ViolationGroup {
    @PrimaryKey
    @ColumnInfo(name = "group_id")
    @SerializedName("group_id")
    @Expose
    private int groupId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "group_code")
    @SerializedName("group_code")
    @Expose
    private String groupCode;
    @ColumnInfo(name = "group_name")
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public ViolationGroup() {

    }

    public ViolationGroup(JSONObject object) throws Exception {
        this.setGroupId(object.getInt("group_id"));
        this.setCustId(object.getInt("custid"));
        this.setGroupCode(object.getString("group_code"));
        this.setGroupName(object.getString("group_name"));
    }

    public static ArrayList<ViolationGroup> getViolationGroups(int custid) throws Exception {
        ArrayList<ViolationGroup> list = new ArrayList<ViolationGroup>();
        list = (ArrayList<ViolationGroup>) ParkingDatabase.getInstance(TPApplication.getInstance()).violationGroupsDao().getViolationGroups();
        return list;
    }

    public static ArrayList<Violation> getViolationsByGroup(String group) throws Exception {
        ArrayList<Violation> list = new ArrayList<Violation>();
        list = (ArrayList<Violation>) ParkingDatabase.getInstance(TPApplication.getInstance()).violationGroupsDao().getViolationsByGroup(group);
        return list;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).violationGroupsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).violationGroupsDao().removeById(id);
    }

    public static void insertViolationGroup(ViolationGroup violationGroup) {
        new InsertViolationGroup(violationGroup).execute();
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
        values.put("custid", this.custId);
        values.put("group_code", this.groupCode);
        values.put("group_name", this.groupName);

        return values;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    private static class InsertViolationGroup extends AsyncTask<Void, Void, Void> {
        private final ViolationGroup violationGroup;

        public InsertViolationGroup(ViolationGroup violationGroup) {
            this.violationGroup = violationGroup;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).violationGroupsDao().insertViolationGroup(violationGroup);
            return null;
        }
    }


}
