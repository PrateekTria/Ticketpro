package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ticketpro.model.Ticket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */

@Dao
public interface TicketsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTicket(Ticket... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTicket(Ticket data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTicketList(List<Ticket> TicketsList);

    @Update
    Completable updateTicket(Ticket ticket);

    /*@Query("Delete from tickets")
    void removeAll();*/

    @Query("select max(ticket_id) as max_ticket_id from tickets")
    long getNextPrimaryId();

    @Query("select * from tickets where userid=:userId and ticket_id=(select max(ticket_id) from tickets)")
    Ticket getLastTicket(int userId);

    @Query("select * from tickets order by ticket_id DESC")
    List<Ticket> getAllTickets();

    @Query("select * from tickets where userid=:userId order by ticket_id DESC")
    List<Ticket> getAllTicketsByUSerID(int userId);

    @Query("select * from tickets where space=:spaceNumber order by ticket_id DESC")
    List<Ticket> getTicketsBySpace(String spaceNumber);

    @Query("select * from tickets where ticket_id=:ticketId")
    Ticket getTicketsByPrimaryId(String ticketId);

    @Query("select * from tickets where ticket_id=:ticketId")
    Ticket getTicketWithViolations(long ticketId);

    @Query("select * from tickets where citation_number=:citationNumber")
    Ticket getTicketByCitationWithViolations(long citationNumber);

    @Query("select * from tickets where plate=:plate and state_code=:state order by ticket_id DESC")
    List<Ticket> searchAllPreviousTicketByPlate(String state, String plate);

    @Query("select * from tickets where plate=:plate and state_code=:state order by ticket_id DESC")
    Ticket searchPreviousTicketByPlate(String state, String plate);

    @Query("select * from tickets where vin=:vin and state_code=:state order by ticket_id DESC")
    Ticket searchPreviousTicketByVIN(String state, String vin);

    @Query("select * from tickets where meter=:meter order by ticket_id DESC")
    Ticket searchPreviousTicketByMeter(String meter);

    @Query("select citation_number from tickets where status='P'")
    List<Long> getPendingCitations();

    @Query("select ticket_id from tickets where citation_number=:citationNumber")
    long getTicketIdFromCitationNumber(long citationNumber);

    @Query("select ticket_id from tickets where date(ticket_date) < date('now','localtime')")
    long getTicketId();

   /* @Query("Delete from tickets where date(ticket_date) < date('now','localtime')")
    void removeAllOlderTickets();*/

    @Query("select  * from tickets where status='S' and strftime('%Y-%m-%d %H:%M',datetime(ticket_date/1000,'unixepoch')) < strftime('%Y-%m-%d %H:%M',datetime('now',:time))")
    List<Ticket> removeAllOlderTicketsByHours(String time);

    @Query("select * from tickets where status='S' and strftime('%Y-%m-%d',datetime(ticket_date/1000,'unixepoch')) <strftime('%Y-%m-%d','now')")
    List<Ticket> removeAllOlderTicketsAtMidnight();

    @Query("Delete from tickets where ticket_id=:ticketId")
    void removeById(long ticketId);

    @Query("Update tickets set status=:values where citation_number=:citation")
    void updateTicket(String citation, String values);

    @Query("Update tickets set is_driveaway=:values where citation_number=:citation")
    void updateTicketDriveway(String citation, String values);

    @Query("Update tickets set is_warn=:values where citation_number=:citation")
    void updateTicketWarning(String citation, String values);

    @Query("Update tickets set is_void=:values where citation_number=:citation")
    void updateTicketVoid(String citation, String values);

    @Query("select ticket_id from tickets where date(ticket_date) < date('now','localtime') and ticket_id=:ticketId")
    long getTicketIds(long ticketId);

    @Query("select * from tickets where (plate=:plate OR vin=:vin) and location= :location and ticket_date=:time")
    Ticket checkDuplicateRecords(String plate, String vin, String location, Date time);


    @Query("select * from tickets where plate=:plate and violation_id=:vid and location=:loc")
    Ticket checkDuplicateRecordsPlate(String plate, int vid, String loc);


    @Query("select * from tickets where chalk_id=:chalkId")
    Ticket chalkSyncOrNot(long chalkId);


    /*@Query("Delete from tickets where status='S'")
    void removeSyncedTickets();*/

   /* @Query("Delete from tickets where date(ticket_date) < date('now','localtime')")
    void removeOldTickets();*/

    @Query("SELECT COUNT(*) FROM tickets where userid=:userId AND (ticket_date BETWEEN :statDate AND :endDate) ")
    String getTicketCount(int userId, Date statDate, Date endDate);


    @Query("SELECT * FROM tickets where status='P'  order by citation_number ASC")
    List<Ticket> getPendingTickets();

    @Query("SELECT * FROM tickets where status='P' AND citation_number IN(:citation) order by citation_number ASC")
    List<Ticket> getPendingCurrentTickets(int[] citation);

    @Query("SELECT * FROM tickets where status='PI'")
    List<Ticket> getPendingTicketsPI();


    @Query("SELECT * from tickets  where  date(ticket_date/1000, 'unixepoch') < date(strftime('%s','now'),'unixepoch') and userid =:userId and status = 'S'")
    Ticket checkPindingTickets(int userId);

    @Query("SELECT * from tickets  where  date(ticket_date/1000, 'unixepoch') < date(strftime('%s','now'),'unixepoch') and userid=:userId and status = 'S'")
    List<Ticket> removeAllPendingTicket(int userId);

    @Query("SELECT * from tickets  where citation_number=:citation")
    Ticket checkDuplicate(String citation);

    @Query("SELECT photo_count from tickets where citation_number=:citation")
    int getPhotoCount(String citation);

    @Query("Update tickets set photo_count=:values where citation_number=:citation")
    void updateTicketPhotoCount(String citation, int values);
}
