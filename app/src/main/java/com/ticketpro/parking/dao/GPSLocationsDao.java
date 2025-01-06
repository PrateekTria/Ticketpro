package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.GPSLocation;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface GPSLocationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGPSLocation(GPSLocation... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGPSLocation(GPSLocation data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGPSLocationList(List<GPSLocation> GPSLocationsList);

    @Query("select * from gps_locations")
    List<GPSLocation> getGPSLocations();

    @Query("Delete from gps_locations")
    void removeAll();

    @Query("Delete from gps_locations where location_id=:id")
    void removeById(int id);
}
