package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Duty;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface DutiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDuty(Duty... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertDuty(Duty data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDutyList(List<Duty> DutysList);

    @Query("select * from duties order by order_number,duty")
    List<Duty> getDuties();

    @Query("select * from duties where duty_id=:dutyId")
    Duty getDutyById(int dutyId);

    @Query("Delete from duties")
    void removeAll();

    @Query("Delete from duties where duty_id=:id")
    void removeById(int id);
}
