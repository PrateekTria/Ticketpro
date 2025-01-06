package com.ticketpro.model;


import static com.ticketpro.util.TPConstant.TAG;

import android.os.AsyncTask;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "Genetec_Patrollers")
public class GenetecPatrollers implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "record_Id")
    @SerializedName("record_Id")
    private int record_Id;

    @ColumnInfo(name = "custId")
    @SerializedName("custId")
    @Expose
    private Integer custId;

    @ColumnInfo(name = "patroller_Id")
    @SerializedName("patroller_Id")
    @Expose
    private String patrollerId;

    @ColumnInfo(name = "vehicleName")
    @SerializedName("vehicleName")
    @Expose
    private String vehicleName;

    @ColumnInfo(name = "is_active")
    @SerializedName("is_active")
    @Expose
    private String isActive;

    @ColumnInfo(name = "createdOn")
    @SerializedName("createdOn")
    @Expose
    private String createdOn;

    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;

    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public GenetecPatrollers() {
    }

    public GenetecPatrollers(JSONObject object) throws Exception {
        this.setRecord_Id(object.getInt("record_Id"));
        this.setCustId(object.getInt("custId"));
        this.setPatrollerId(object.getString("patroller_Id"));
        this.setVehicleName(object.getString("vehicleName"));
        this.setIsActive(object.getString("is_active"));
        this.setCreatedOn(object.getString("createdOn"));



    }

    public static ArrayList<GenetecPatrollers> getPatrollers(String moduleName) throws Exception {
        ArrayList<GenetecPatrollers> list;

        if (!"ALL".equalsIgnoreCase(moduleName) && moduleName != null) {
            list = (ArrayList<GenetecPatrollers>) ParkingDatabase.getInstance(TPApplication.getInstance()).genetecPatrollerDao().getPatrollers();
        } else {
            list = (ArrayList<GenetecPatrollers>) ParkingDatabase.getInstance(TPApplication.getInstance()).genetecPatrollerDao().getAllPatrollers();
        }
        return list;
    }


    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).genetecPatrollerDao().removeAll();
    }


    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).genetecPatrollerDao().removeById(id);
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getPatrollerId() {
        return patrollerId;
    }

    public void setPatrollerId(String patrollerId) {
        this.patrollerId = patrollerId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }


    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getRecord_Id() {
        return record_Id;
    }

    public void setRecord_Id(int record_Id) {
        this.record_Id = record_Id;
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

    public static void insertPatrollers(final List<GenetecPatrollers> genetecPatrollersList) {
        final ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());
        final long l = System.currentTimeMillis();
        Log.i(TAG, "insertPatrollers: " + l);
        instance.genetecPatrollerDao().insertPatrollersList(genetecPatrollersList).subscribeOn(Schedulers.io()).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "onSubscribe: " + System.currentTimeMillis());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: " + (System.currentTimeMillis() - l));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError: ", e);
            }
        });
    }

    public static void insertPatrollers(GenetecPatrollers genetecPatrollers) {
        ParkingDatabase.getInstance(TPApplication.getInstance()).genetecPatrollerDao().insertPatrollers(genetecPatrollers).subscribeOn(Schedulers.io()).subscribe();
    }
}
