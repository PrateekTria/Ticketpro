package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.SpecialActivity;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface SpecialActivitiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSpecialActivity(SpecialActivity... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSpecialActivity(SpecialActivity data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSpecialActivityList(List<SpecialActivity> SpecialActivitysList);

    @Query("select * from special_activities order by order_number")
    List<SpecialActivity> getSpecialActivities();

    @Query("select activity_id from special_activities where activity=:name")
    int getActivityIdByName(String name);

    @Query("Delete from special_activities")
    void removeAll();

    @Query("Delete from special_activities where activity_id=:id")
    void removeById(int id);

    @Query("select autoSelect from special_activities where activity=:name")
    String getAutoSelect(String name);
}
