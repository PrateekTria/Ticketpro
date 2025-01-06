package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.TicketHistory;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface TicketHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTicketHistory(TicketHistory... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTicketHistory(TicketHistory data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTicketHistoryList(List<TicketHistory> TicketHistorysList);

    @Query("Delete from ticket_history")
    void removeAllTicketHistory();

    @Query("Delete from ticket_history where ticket_id=:ticketId")
    void removeTicketHistoryById(long ticketId);

    @Query("select max(ticket_date) as max_ticket_date from ticket_history")
    Date getMaxTicketDate();

    @Query("select min(ticket_date) as min_ticket_date from ticket_history")
    Date getMinTicketDate();

    @Query("select * from ticket_history order by ticket_date DESC")
    List<TicketHistory> getTickets();

    @Query("select * from ticket_history where plate=:plate and state_code=:state order by ticket_id DESC")
    List<TicketHistory> searchAllPreviousTicketsByPlate(String state, String plate);

    @Query("select * from ticket_history where plate=:plate and state_code=:state order by ticket_id DESC")
    TicketHistory searchPreviousTicketByPlate(String state, String plate);

    @Query("select * from ticket_history where vin=:vin and state_code=:state order by ticket_id DESC")
    TicketHistory searchPreviousTicketByVIN(String state,String vin);

    @Query("select * from ticket_history where meter=:meter order by ticket_id DESC")
    TicketHistory searchPreviousTicketByMeter(String meter);

    @Query("Delete from ticket_history")
    void removeAll();

    @Query("Delete from ticket_history where ticket_id=:id")
    void removeById(int id);
}
