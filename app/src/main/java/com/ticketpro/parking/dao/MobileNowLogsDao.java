package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.ticketpro.model.MobileNowLog;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface MobileNowLogsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMobileNowLog(MobileNowLog... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMobileNowLog(MobileNowLog data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMobileNowLogList(List<MobileNowLog> MobileNowLogsList);
}
