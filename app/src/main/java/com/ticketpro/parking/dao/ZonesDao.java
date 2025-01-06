package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Zone;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface ZonesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertZone(Zone... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertZone(Zone data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertZoneList(List<Zone> ZonesList);

    @Query("select * from zones")
    List<Zone> getZones();

    @Query("Delete from zones")
    void removeAll();

    @Query("Delete from zones where zone_id=:id")
    void removeById(int id);

}
