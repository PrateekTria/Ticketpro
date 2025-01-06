package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.DutyReport;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface DutyReportsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDutyReport(DutyReport... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertDutyReport(DutyReport data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDutyReportList(List<DutyReport> DutyReportsList);

    @Query("select *,duties.code as duty_title from duty_reports, duties where userid=:userId")
    List<DutyReport> getDutyReports(int userId);

    @Query("select * from duty_reports where report_id=:primaryKey")
    DutyReport getDutyReportByPrimaryKey(String primaryKey);

    @Query("select max(report_id) as max_report_id from duty_reports")
    int getLastInsertId();

    @Query("Delete from duty_reports")
    void removeAll();

    @Query("Delete from duty_reports where report_id=:id")
    void removeById(int id);
}
