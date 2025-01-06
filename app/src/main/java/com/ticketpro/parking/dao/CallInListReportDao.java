package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.CallInReport;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface CallInListReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCallInReport(CallInReport... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCallInReport(CallInReport data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCallInReportList(List<CallInReport> callInReortList);

    @Query("select * from call_in_reports")
    List<CallInReport> getCallInReports();

    @Query("select * from call_in_reports where report_id=:primaryKey")
    CallInReport getCallInReportByPrimaryKey(String primaryKey);

    @Query("select max(report_id) as max_report_id from call_in_reports")
    int getLastInsertId();

    @Query("Delete from call_in_reports")
    void removeAll();

    @Query("Delete from call_in_reports where report_id=:id")
    void removeById(int id);
}
