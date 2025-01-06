package com.ticketpro.model;

import android.content.ContentValues;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ticketpro.exception.TPException;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.DateUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "sync_data")
public class SyncData {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sync_data_id")
    private int syncDataId;
    @ColumnInfo(name = "userid")
    private int userId;
    @ColumnInfo(name = "custid")
    private int custId;
    @ColumnInfo(name = "activity")
    private String activity;
    @ColumnInfo(name = "table_name")
    private String tableName;
    @ColumnInfo(name = "primary_key")
    private String primaryKey;
    @ColumnInfo(name = "activity_source")
    private String activitySource;
    @ColumnInfo(name = "sql_query")
    private String sqlQuery;
    @ColumnInfo(name = "activity_date")
    private Date activityDate;
    @ColumnInfo(name = "status")
    private String status;

    public SyncData() {

    }

    public SyncData(JSONObject object) throws Exception {
        this.setSyncDataId(object.getInt("sync_data_id"));
        this.setCustId(object.getInt("custid"));
        this.setUserId(object.getInt("userid"));
        this.setActivity(object.getString("activity"));
        this.setTableName(object.getString("table_name"));
        this.setPrimaryKey(object.getString("primary_key"));
        this.setActivitySource(object.getString("activity_source"));
        this.setSqlQuery(object.getString("sql_query"));
        this.setActivityDate(DateUtil.getDateFromSQLString(object.getString("activity_date")));
        this.setStatus(object.getString("status"));
    }

    public static ArrayList<SyncData> getSyncData() throws TPException {
        ArrayList<SyncData> list;
        list = (ArrayList<SyncData>) ParkingDatabase.getInstance(TPApplication.getInstance()).syncDataDao().getSyncData();
        return list;
    }


    public static ArrayList<SyncData> getImageUploadSyncData() throws TPException {
        ArrayList<SyncData> list;
        list = (ArrayList<SyncData>) ParkingDatabase.getInstance(TPApplication.getInstance()).syncDataDao().getImageUploadSyncData();
        return list;
    }

    public static ArrayList<SyncData> getVoiceUploadSyncData() throws TPException {
        ArrayList<SyncData> list;
        list = (ArrayList<SyncData>) ParkingDatabase.getInstance(TPApplication.getInstance()).syncDataDao().getVoiceUploadSyncData();
        return list;
    }

    public static int getNextPrimaryId() throws Exception {
        int nextId = 0;
        nextId = ParkingDatabase.getInstance(TPApplication.getInstance()).syncDataDao().getNextPrimaryId();
        return nextId + 1;
    }

    public static void removeDoneSyncData() {
        ParkingDatabase.getInstance(TPApplication.getInstance()).syncDataDao().removeDoneSyncData();
    }

    public static void removeSyncData(String tableName, String primaryKey) {
        ParkingDatabase.getInstance(TPApplication.getInstance()).syncDataDao().removeSyncData(tableName, primaryKey);
    }

    public static void removeSyncUploads() {
        ParkingDatabase.getInstance(TPApplication.getInstance()).syncDataDao().removeSyncUploads();
    }

    public static Completable insertSyncData(final SyncData SyncData) {
        final ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());
        return Completable.fromAction(() ->
                instance.syncDataDao().insertSyncData(SyncData).subscribeOn(Schedulers.io()).subscribe());
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        if (this.syncDataId == 0) {
            this.syncDataId = SyncData.getNextPrimaryId();
        }

        values.put("sync_data_id", this.syncDataId);
        values.put("userid", this.userId);
        values.put("custid", this.custId);
        values.put("activity", this.activity);
        values.put("table_name", this.tableName);
        values.put("primary_key", this.primaryKey);
        values.put("activity_source", this.activitySource);
        values.put("sql_query", this.sqlQuery);
        values.put("activity_date", DateUtil.getSQLStringFromDate2(this.activityDate));
        values.put("status", this.status);

        return values;
    }

    public int getSyncDataId() {
        return syncDataId;
    }

    public void setSyncDataId(int syncDataId) {
        this.syncDataId = syncDataId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getActivitySource() {
        return activitySource;
    }

    public void setActivitySource(String activitySource) {
        this.activitySource = activitySource;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
