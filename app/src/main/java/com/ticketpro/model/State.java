package com.ticketpro.model;

import android.content.ContentValues;
import android.content.Context;
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

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "states")
public class State {
    @PrimaryKey
    @ColumnInfo(name = "state_id")
    @SerializedName("state_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "state")
    @SerializedName("state")
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
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public State() {

    }

    public State(JSONObject object) throws Exception {
        this.setId(object.getInt("state_id"));
        this.setCustId(object.getInt("custid"));
        this.setTitle(object.getString("state"));
        this.setCode(object.getString("code"));
        this.setOrderNumber(!object.isNull("order_number") ? object.getInt("order_number") : 0);
    }

    public static ArrayList<State> getStates(int custId) throws Exception {
        ArrayList<State> list = new ArrayList<State>();
        list = (ArrayList<State>) ParkingDatabase.getInstance(TPApplication.getInstance()).statesDao().getStates();
        return list;
    }

    public static State getStateByName(String name) {
        State object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).statesDao().getStateByName(name);
        return object;
    }

    public static int getStateIdByName(String state) throws Exception {
        int result = 0;
        result = ParkingDatabase.getInstance(TPApplication.getInstance()).statesDao().getStateIdByName(state);
        return result;
    }

    public static int getStateIdByCode(String code) {
        int result = 0;
        result = ParkingDatabase.getInstance(TPApplication.getInstance()).statesDao().getStateIdByCode(code);
        return result;
    }

    public static String getStateCodeById(int stateId) {
        String result = "";
        result = ParkingDatabase.getInstance(TPApplication.getInstance()).statesDao().getStateCodeById(stateId);
        return result;
    }

    public static State getDefaultState(Context ctx) {
        State object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).statesDao().getDefaultState();
        return object;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).statesDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).statesDao().removeById(id);
    }

    public static void insertState(final State State) {
        final ParkingDatabase database = ParkingDatabase.getInstance(TPApplication.getInstance());
        Completable completable = Completable.fromAction(() -> database.statesDao().insertState(State).subscribeOn(Schedulers.io()).subscribe());
        completable.subscribe();//new State.InsertState(State).execute();
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
        values.put("state_id", this.id);
        values.put("custid", this.custId);
        values.put("state", this.title);
        values.put("code", this.code);
        values.put("order_number", this.orderNumber);
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

    private static class InsertState extends AsyncTask<Void, Void, Void> {
        private State State;

        public InsertState(State State) {
            this.State = State;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).statesDao().insertState(State);
            return null;
        }
    }
}
