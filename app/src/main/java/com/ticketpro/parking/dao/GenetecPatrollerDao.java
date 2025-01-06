package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Color;
import com.ticketpro.model.GenetecPatrollers;
import com.ticketpro.model.User;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface GenetecPatrollerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPatrollers(GenetecPatrollers... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertPatrollers(GenetecPatrollers data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertPatrollersList(List<GenetecPatrollers> PatrollersList);

    @Query("select * from genetec_patrollers order by patroller_Id")
    List<GenetecPatrollers> getAllPatrollers();

    @Query("select * from genetec_patrollers order by patroller_Id")
    List<GenetecPatrollers> getPatrollers();

    @Query("Delete from genetec_patrollers")
    void removeAll();

    @Query("Delete from genetec_patrollers where patroller_Id=:id")
    void removeById(int id);

}
