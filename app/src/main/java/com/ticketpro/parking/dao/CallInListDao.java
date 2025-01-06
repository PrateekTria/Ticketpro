package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.CallInList;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface CallInListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCallInList(CallInList... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCallInList(CallInList data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCallInList(List<CallInList> callInListList);

    @Query("select * from call_in_list order by order_number,action_item")
    List<CallInList> getCallInList();

    @Query("Delete from call_in_list")
    void removeAll();

    @Query("Delete from call_in_list where call_in_id=:id")
    void removeByid(int id);
}
