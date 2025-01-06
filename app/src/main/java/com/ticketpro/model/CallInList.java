package com.ticketpro.model;

import android.content.ContentValues;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
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

@Entity(tableName = "call_in_list")
public class CallInList {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "call_in_id")
    @SerializedName("call_in_id")
    @Expose
    private int callInId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "action_item")
    @SerializedName("action_item")
    @Expose
    private String actionItem;
    @ColumnInfo(name = "action_code")
    @SerializedName("action_code")
    @Expose
    private String actionCode;
    @ColumnInfo(name = "order_number")
    @SerializedName("order_number")
    @Expose
    private int orderNumber;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public CallInList() {
    }

    public CallInList(JSONObject object) throws Exception {
        this.setCallInId(object.getInt("call_in_id"));
        this.setCustId(object.getInt("custid"));
        this.setActionItem(object.getString("action_item"));
        this.setActionCode(object.getString("action_code"));
        this.setOrderNumber(!object.isNull("order_number") ? object.getInt("order_number") : 0);
    }

    public static ArrayList<CallInList> getCallInList() {
        return (ArrayList<CallInList>) ParkingDatabase.getInstance(TPApplication.getInstance()).callInListDao().getCallInList();
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).callInListDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).callInListDao().removeByid(id);
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
        values.put("call_in_id", this.callInId);
        values.put("custid", this.custId);
        values.put("action_item", this.actionItem);
        values.put("action_code", this.actionCode);
        values.put("order_number", this.orderNumber);
        return values;
    }

    public int getCallInId() {
        return callInId;
    }

    public void setCallInId(int callInId) {
        this.callInId = callInId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getActionItem() {
        return actionItem;
    }

    public void setActionItem(String actionItem) {
        this.actionItem = actionItem;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }


    public static void insertCallInList(CallInList CallInList){
        new CallInList.InsertCallInList(CallInList).execute();
    }

    private static class InsertCallInList extends AsyncTask<Void,Void,Void> {
        private CallInList CallInList;

        public InsertCallInList(CallInList CallInList) {
            this.CallInList = CallInList;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).callInListDao().insertCallInList(CallInList);
            return null;
        }
    }
}
