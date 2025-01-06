package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.VoidTicketReason;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface VoidTicketReasonsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVoidTicketReason(VoidTicketReason... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVoidTicketReason(VoidTicketReason data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVoidTicketReasonList(List<VoidTicketReason> VoidTicketReasonsList);

    @Query("select * from void_ticket_reasons order by order_number")
    List<VoidTicketReason> getVoidReasons();

    @Query("Delete from void_ticket_reasons")
    void removeAll();

    @Query("Delete from void_ticket_reasons where reason_id=:id")
    void removeById(int id);

}
