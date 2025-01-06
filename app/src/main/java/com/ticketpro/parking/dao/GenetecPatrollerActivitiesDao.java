package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ticketpro.model.GenetecPatrollerActivities;
import com.ticketpro.vendors.GenetecPatrollerActivity;

import java.util.List;

@Dao
public interface GenetecPatrollerActivitiesDao {


    @Insert
    void insert(GenetecPatrollerActivities genetecPatrollerActivities);

    @Query("SELECT * FROM Genetec_PatrollerActivities")
    List<GenetecPatrollerActivities> getAllGenetecPatrollerActivities();
}
