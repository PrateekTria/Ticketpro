package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Location;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface LocationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocation(Location... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocation(Location data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocationList(List<Location> LocationsList);

    @Query("select * from locations where location=:locationText")
    Location getLocationByText(String locationText);

    @Query("select * from locations where is_manual='Y'")
    List<Location> getManualLocations();

    @Query("select max(location_id) as max_location_id from locations")
    int getLastInsertId();

    @Query("select * from locations order by order_number,location")
    List<Location> getLocations();

    @Query("Delete from locations")
    void removeAll();

    @Query("Delete from locations where location_id=:id")
    void removeById(int id);

    @Query("DELETE FROM locations WHERE is_manual =:isManual ")
    void removeManuaLocation(String isManual);

}
