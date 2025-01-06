package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Location;
import com.ticketpro.model.LocationGroup;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface LocationGroupsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocationGroup(LocationGroup... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocationGroup(LocationGroup data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocationGroupList(List<LocationGroup> LocationGroupsList);

    @Query("select * from location_groups")
    List<LocationGroup> getLocationGroups();

    @Query("select * from locations where locations.zone_id=-1 or locations.location_id in (select location_id from location_group_locations where group_id=(select group_id from location_groups where group_code=:group)) order by order_number")
    List<Location> getLocationsByGroup(String group);

    @Query("select * from locations order by order_number")
    List<Location> getALLLocationsByGroup();

    @Query("Delete from location_groups")
    void removeAll();

    @Query("Delete from location_groups where group_id=:id")
    void removeById(int id);

}
