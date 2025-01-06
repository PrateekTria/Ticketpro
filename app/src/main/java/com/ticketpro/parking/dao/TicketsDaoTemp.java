package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketTemp;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;

@Dao
public interface TicketsDaoTemp {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTicket(TicketTemp... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTicket(TicketTemp data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTicketList(List<TicketTemp> TicketsList);

    @Update
    Completable updateTicket(TicketTemp ticket);

    /*@Query("Delete from tickets")
    void removeAll();*/

    @Query("select * from tickets_temp")
    TicketTemp getLastTicket();

    @Query("Delete from tickets_temp")
    void removeAll();

    @Query("SELECT COUNT(ticket_id) FROM tickets_temp")
    int getCount();


}
