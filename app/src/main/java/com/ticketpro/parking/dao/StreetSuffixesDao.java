package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.StreetSuffix;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface StreetSuffixesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStreetSuffix(StreetSuffix... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStreetSuffix(StreetSuffix data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStreetSuffixList(List<StreetSuffix> StreetSuffixsList);

    @Query("select * from street_suffixes")
    List<StreetSuffix> getStreetSuffixes();

    @Query("Delete from street_suffixes")
    void removeAll();

    @Query("Delete from street_suffixes where suffix_id=:id")
    void removeById(int id);
}
