package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.StreetPrefix;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface StreetPrefixesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStreetPrefix(StreetPrefix... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStreetPrefix(StreetPrefix data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStreetPrefixList(List<StreetPrefix> StreetPrefixsList);

    @Query("select * from street_prefixes")
    List<StreetPrefix> getStreetPrefixes();

    @Query("Delete from street_prefixes")
    void removeAll();

    @Query("Delete from street_prefixes where prefix_id=:id")
    void removeById(int id);
}
