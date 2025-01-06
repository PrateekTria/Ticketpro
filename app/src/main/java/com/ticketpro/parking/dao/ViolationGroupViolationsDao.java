package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.ViolationGroupViolation;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface ViolationGroupViolationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertViolationGroupViolation(ViolationGroupViolation... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertViolationGroupViolation(ViolationGroupViolation data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertViolationGroupViolationList(List<ViolationGroupViolation> ViolationGroupViolationsList);

    @Query("select * from violation_group_violations")
    List<ViolationGroupViolation> getViolationGroupViolations();

    @Query("Delete from violation_group_violations")
    void removeAll();

    @Query("Delete from violation_group_violations where violation_group_id=:id")
    void removeById(int id);
}
