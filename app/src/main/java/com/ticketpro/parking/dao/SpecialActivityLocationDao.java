package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.SpecialActivitiesLocation;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 03-09-2020.
 */
@Dao
public interface SpecialActivityLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSpecialActivitiesLocation(SpecialActivitiesLocation... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertSpecialActivitiesLocation(SpecialActivitiesLocation data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSpecialActivitiesLocationList(List<SpecialActivitiesLocation> specialActivitiesLocations);

    @Query("select location from Special_Activities_location where custid=:custId order by order_number")
    List<String> getSpecialLocation(int custId);

    @Query("Delete from special_activities_location")
    void removeAll();
}
