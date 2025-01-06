package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.DeviceGroup;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface DeviceGroupsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDeviceGroup(DeviceGroup... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertDeviceGroup(DeviceGroup data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDeviceGroupList(List<DeviceGroup> deviceGroupList);

    @Query("Delete from device_groups")
    void removeAll();

    @Query("Delete from device_groups where group_id=:id")
    void removeById(int id);

    @Query("SELECT device_ids FROM device_groups WHERE group_name=:group_name")
    String getDevicesIds(String group_name);
}
