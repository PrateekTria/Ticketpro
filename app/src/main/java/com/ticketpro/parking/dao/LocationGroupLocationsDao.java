package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.LocationGroupLocation;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface LocationGroupLocationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocationGroupLocation(LocationGroupLocation... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocationGroupLocation(LocationGroupLocation data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocationGroupLocationList(List<LocationGroupLocation> LocationGroupLocationsList);

    @Query("select * from location_group_locations")
    List<LocationGroupLocation> getLocationGroupLocations();

    @Query("Delete from location_group_locations")
    void removeAll();

    @Query("Delete from location_group_locations where location_group_id=:id")
    void removeById(int id);
}
