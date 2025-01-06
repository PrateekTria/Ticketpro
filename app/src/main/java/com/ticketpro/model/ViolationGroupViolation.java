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

@Entity(tableName = "violation_group_violations")
public class ViolationGroupViolation {
    @PrimaryKey
    @ColumnInfo(name = "violation_group_id")
    @SerializedName("violation_group_id")
    @Expose
    private int violationGroupId;
    @ColumnInfo(name = "group_id")
    @SerializedName("group_id")
    @Expose
    private int groupId;
    @ColumnInfo(name = "violation_id")
    @SerializedName("violation_id")
    @Expose
    private int violationId;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public ViolationGroupViolation() {

    }

    public ViolationGroupViolation(JSONObject object) throws Exception {

        this.setGroupId(object.getInt("group_id"));
        this.setViolationGroupId(object.getInt("violation_group_id"));
        this.setViolationId(object.getInt("violation_id"));

    }

    public static ArrayList<ViolationGroupViolation> getViolationGroupViolations(int custid) throws Exception {

        ArrayList<ViolationGroupViolation> list = new ArrayList<ViolationGroupViolation>();
        list = (ArrayList<ViolationGroupViolation>) ParkingDatabase.getInstance(TPApplication.getInstance()).violationGroupViolationsDao().getViolationGroupViolations();
        return list;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).violationGroupViolationsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).violationGroupViolationsDao().removeById(id);
    }

    public static void insertViolationGroupViolation(ViolationGroupViolation ViolationGroupViolation) {
        new ViolationGroupViolation.InsertViolationGroupViolation(ViolationGroupViolation).execute();
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
        values.put("violation_group_id", this.violationGroupId);
        values.put("violation_id", this.violationId);

        return values;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getViolationGroupId() {
        return violationGroupId;
    }

    public void setViolationGroupId(int violationGroupId) {
        this.violationGroupId = violationGroupId;
    }

    public int getViolationId() {
        return violationId;
    }

    public void setViolationId(int violationId) {
        this.violationId = violationId;
    }

    private static class InsertViolationGroupViolation extends AsyncTask<Void, Void, Void> {
        private ViolationGroupViolation ViolationGroupViolation;

        public InsertViolationGroupViolation(ViolationGroupViolation ViolationGroupViolation) {
            this.ViolationGroupViolation = ViolationGroupViolation;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).violationGroupViolationsDao().insertViolationGroupViolation(ViolationGroupViolation);
            return null;
        }
    }

}
