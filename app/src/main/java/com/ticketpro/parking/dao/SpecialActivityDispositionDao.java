package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.SpecialActivityDisposition;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface SpecialActivityDispositionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSpecialActivityDisposition(SpecialActivityDisposition... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSpecialActivityDisposition(SpecialActivityDisposition data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSpecialActivityDispositionList(List<SpecialActivityDisposition> SpecialActivityDispositionsList);

    @Query("select * from special_activity_dispositions order by order_number,disposition")
    List<SpecialActivityDisposition> getSpecialActivityDispositions();

    @Query("select disposition_id from special_activity_dispositions where disposition=:name")
    int getSpecialActivityDispositionIdByName(String name);

    @Query("Delete from special_activity_dispositions")
    void removeAll();

    @Query("Delete from special_activity_dispositions where disposition_id=:id")
    void removeById(int id);
}
