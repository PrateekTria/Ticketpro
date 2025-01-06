package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Violation;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface ViolationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertViolation(Violation... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertViolation(Violation data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertViolationList(List<Violation> ViolationsList);

    @Query("select * from violations order by order_number,violation")
    List<Violation> getViolations();

    @Query("select * from violations where violation_id=:violationId")
    Violation getViolationById(int violationId);

    @Query("select * from violations where code=:violationCode")
    Violation getViolationByCode(String violationCode);

    @Query("Delete from violations")
    void removeAll();

    @Query("Delete from violations where violation_id=:id")
    void removeById(int id);
    // This code is added by mohit 20/02/2023
    @Query("select * from violations where code_export=:code_export")
    Violation getViolationByCode_Export(String code_export);
}
