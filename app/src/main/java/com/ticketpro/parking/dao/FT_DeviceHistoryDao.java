package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.DeviceData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface FT_DeviceHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDeviceData(DeviceData... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertDeviceData(DeviceData data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDeviceDataList(List<DeviceData> DeviceDatasList);

    @Query("select * from FT_DeviceHistory where sync_status='P'")
    List<DeviceData> getPendingLocationUpdates();

    @Query("Delete from FT_DeviceHistory where id= :id and sync_status='S'")
    void deleteRecord(long id);

    @Query("select * from FT_DeviceHistory")
    List<DeviceData> getAllData();

    @Query("select * from FT_DeviceHistory where id= :lastInsertedDeviceDataID")
    DeviceData getLastSavedData(Long lastInsertedDeviceDataID);

    @Query("Update FT_DeviceHistory set sync_status = 'S' where currTimeStamp=:s")
    void updateSyncStatus(String s);

    @Query("select * from FT_DeviceHistory where sync_status = 'S'")
    List<DeviceData> getSyncedLocationUpdates();
}
