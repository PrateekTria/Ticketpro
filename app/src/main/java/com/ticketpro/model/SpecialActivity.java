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

@Entity(tableName = "special_activities")
public class SpecialActivity {
    @PrimaryKey
    @ColumnInfo(name = "activity_id")
    @SerializedName("activity_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "activity")
    @SerializedName("activity")
    @Expose
    private String title;
    @ColumnInfo(name = "code")
    @SerializedName("code")
    @Expose
    private String code;
    @ColumnInfo(name = "order_number")
    @SerializedName("order_number")
    @Expose
    private int orderNumber;
    @ColumnInfo(name = "autoSelect")
    @SerializedName("autoSelect")
    @Expose
    private String autoSelect;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public SpecialActivity() {

    }

    public SpecialActivity(JSONObject object) throws Exception {

        this.setId(object.getInt("activity_id"));
        this.setCustId(object.getInt("custid"));
        this.setTitle(object.getString("activity"));
        this.setCode(object.getString("code"));
        this.setOrderNumber(!object.isNull("order_number") ? object.getInt("order_number") : 0);
        this.setAutoSelect(object.getString("autoSelect"));

    }

    public static ArrayList<SpecialActivity> getSpecialActivities() throws Exception {
        ArrayList<SpecialActivity> list = new ArrayList<SpecialActivity>();
        list = (ArrayList<SpecialActivity>) ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivitiesDao().getSpecialActivities();
        return list;
    }

    public static int getActivityIdByName(String name) throws Exception {
        int result = 0;
        result = ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivitiesDao().getActivityIdByName(name);
        return result;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivitiesDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivitiesDao().removeById(id);
    }

    public static void insertSpecialActivity(SpecialActivity SpecialActivity) {
        new SpecialActivity.InsertSpecialActivity(SpecialActivity).execute();
    }

    public static String getAutoSelect(String name) throws Exception {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivitiesDao().getAutoSelect(name);
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
        values.put("activity_id", this.id);
        values.put("custid", this.custId);
        values.put("activity", this.title);
        values.put("code", this.code);
        values.put("order_number", this.orderNumber);
		values.put("autoSelect", this.autoSelect);
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getAutoSelect() {
        return autoSelect;
    }

    public void setAutoSelect(String autoSelect) {
        this.autoSelect = autoSelect;
    }

    private static class InsertSpecialActivity extends AsyncTask<Void, Void, Void> {
        private SpecialActivity SpecialActivity;

        public InsertSpecialActivity(SpecialActivity SpecialActivity) {
            this.SpecialActivity = SpecialActivity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivitiesDao().insertSpecialActivity(SpecialActivity);
            return null;
        }
    }

}
