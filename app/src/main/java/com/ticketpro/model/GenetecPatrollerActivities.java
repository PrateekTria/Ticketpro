package com.ticketpro.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "Genetec_PatrollerActivities")
public class GenetecPatrollerActivities implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "record_Id")
    @SerializedName("record_Id")
    public int recordId;

    @ColumnInfo(name = "createdOn")
    @SerializedName("createdOn")
    public String createdOn; // Use long to store epoch time

    @ColumnInfo(name = "custId")
    @SerializedName("custId")
    public Integer custId;

    @ColumnInfo(name = "patroller_Id")
    @SerializedName("patroller_Id")
    public String patrollerId;

    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    public String userId;

    @ColumnInfo(name = "device_Id")
    @SerializedName("device_Id")
    public Integer deviceId;

    @ColumnInfo(name = "is_active")
    @SerializedName("is_active")
    public String isActive;


    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
