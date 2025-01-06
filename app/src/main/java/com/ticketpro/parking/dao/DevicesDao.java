package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.DeviceInfo;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */

@Dao
public interface DevicesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDeviceInfo(DeviceInfo... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertDeviceInfo(DeviceInfo data);

    @Query("select * from devices where device_name=:deviceName")
    DeviceInfo getDeviceInfo(String deviceName);

    @Query("Update devices SET lastTicketTime= :timeStamp where device_id=:deviceId")
    void updateLastTicketTime(int deviceId, String timeStamp);

    @Query("select lastTicketTime from devices where device_id=:deviceId")
    String getLastTicketTime(int deviceId);

    @Query("select * from devices where device_id=:deviceId")
    DeviceInfo getDeviceInfoById(int deviceId);

    @Query("select * from devices")
    List<DeviceInfo> getDevices();

}
