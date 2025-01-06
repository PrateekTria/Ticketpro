package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Hotlist;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface HotlistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHotlist(Hotlist... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertHotlist(Hotlist data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHotlistList(List<Hotlist> HotlistsList);

    @Query("select * from hotlist")
    List<Hotlist> getHostlist();

    @Query("select * from hotlist where hotlist_id=:hotListId")
    Hotlist getHostlistReportByPrimaryKey(String hotListId);

    @Query("select * from hotlist where plate=:plate and state_code=:state")
    List<Hotlist> searchHotlist(String plate, String state);

    @Query("select max(hotlist_id) as max_hotlist_id from hotlist")
    int getNextPrimaryId();

    @Query("Delete from hotlist")
    void removeAll();

    @Query("Delete from hotlist where hotlist_id=:id")
    void removeById(int id);
}
