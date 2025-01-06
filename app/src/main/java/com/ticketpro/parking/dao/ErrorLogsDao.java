package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.ErrorLog;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface ErrorLogsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertErrorLog(ErrorLog... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertErrorLog(ErrorLog data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertErrorLogList(List<ErrorLog> ErrorLogsList);

    @Query("select * from error_logs")
    List<ErrorLog> getErrorLogs();

    @Query("select max(error_id) as max_error_id from error_logs")
    int getNextPrimaryId();

    @Query("Delete from error_logs")
    void removeAll();

    @Query("Delete from error_logs where error_id=:id")
    void removeById(int id);
}
