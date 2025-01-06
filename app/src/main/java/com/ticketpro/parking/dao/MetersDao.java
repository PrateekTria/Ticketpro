package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Meter;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface MetersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeter(Meter... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeter(Meter data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeterList(List<Meter> MetersList);

    @Query("Select * from meters")
    List<Meter> getMeters();

    @Query("select * from meters where meter=:meter order by meter_id DESC")
    Meter searchMeterHistory(String meter);

    @Query("Delete from meters")
    void removeAll();

    @Query("Delete from meters where meter_id=:id")
    void removeById(int id);
}
