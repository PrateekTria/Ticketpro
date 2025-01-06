package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Message;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface MessagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(Message... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(Message data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessageList(List<Message> MessagesList);

    @Query("select * from messages where department=:department and expiry_date>=date('now') order by message_date ASC")
    List<Message> getMessages(String department);

    @Query("Delete from messages")
    void removeAll();

    @Query("Delete from messages where message_id=:id")
    void removeById(int id);
}
