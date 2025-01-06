package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.LPRNotify;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface LPRNotificationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLPRNotify(LPRNotify... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertLPRNotify(LPRNotify data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLPRNotifyList(List<LPRNotify> LPRNotifysList);

    @Query("select * from lpr_notifications order by date_notify DESC")
    List<LPRNotify> getLPRNotifications();

    @Query("select * from lpr_notifications where notification_id=:notificationId")
    LPRNotify getLPRNotificationById(int notificationId);

    @Query("Delete from lpr_notifications")
    void removeAllNotifications();

    @Query("Delete from lpr_notifications where notification_id=:notificationId")
    void removeNotificationById(String notificationId);

}
