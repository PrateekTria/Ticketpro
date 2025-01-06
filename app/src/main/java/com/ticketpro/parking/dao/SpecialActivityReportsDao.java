package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.SpecialActivityReport;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface SpecialActivityReportsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSpecialActivityReport(SpecialActivityReport... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSpecialActivityReport(SpecialActivityReport data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSpecialActivityReportList(List<SpecialActivityReport> SpecialActivityReportsList);

    @Query("select * from special_activity_reports where createDate=:currentDate AND custid=:custId")
    List<SpecialActivityReport> getSpecialActivityReports(int custId, String currentDate);

    @Query("select * from special_activity_reports where report_id=:primaryKey")
    SpecialActivityReport getSpecialActivityReportByPrimaryKey(String primaryKey);

    @Query("select max(report_id) as max_report_id from special_activity_reports")
    int getLastInsertId();

    @Query("Delete from special_activity_reports")
    void removeAll();

    @Query("Delete from special_activity_reports where report_id=:id")
    void removeById(int id);

}
