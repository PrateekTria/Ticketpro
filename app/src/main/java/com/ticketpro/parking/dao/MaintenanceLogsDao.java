package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.MaintenanceLog;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface MaintenanceLogsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMaintenanceLog(MaintenanceLog... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMaintenanceLog(MaintenanceLog data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMaintenanceLogList(List<MaintenanceLog> MaintenanceLogsList);

    @Query("select * from maintenance_logs")
    List<MaintenanceLog> getLogs();

    @Query("select * from maintenance_logs where log_id=:logId")
    MaintenanceLog getLogById(int logId);

    @Query("Delete from maintenance_logs")
    void removeAll();

    @Query("Delete from maintenance_logs where log_id=:id")
    void removeById(long id);
}
