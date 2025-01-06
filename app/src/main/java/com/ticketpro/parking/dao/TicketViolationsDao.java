package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.TicketViolation;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface TicketViolationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTicketViolation(TicketViolation... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTicketViolation(TicketViolation data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTicketViolationList(List<TicketViolation> TicketViolationsList);

    @Query("select * from ticket_violations where ticket_id=:ticketId and citation_number=:citationNumber")
    List<TicketViolation> getTicketViolations(long ticketId, long citationNumber);

    @Query("select * from ticket_violations where citation_number=:citationNumber")
    List<TicketViolation> getTicketViolationsByCitation(long citationNumber);

    @Query("select *,(select violations.violation from violations where violations.violation_id=ticket_violations.violation_id) as violation from ticket_violations where ticket_id=:ticketId")
    List<TicketViolation> getTicketViolationWithComments(long ticketId);

    @Query("select *,(select violations.violation from violations where violations.violation_id=ticket_violations.violation_id) as violation from ticket_violations where citation_number=:citationNumber")
    List<TicketViolation> getTicketViolationByCitationWithComments(long citationNumber);

    @Query("select * from ticket_violations where ticket_violation_id=:violationId")
    TicketViolation getViolationByPrimaryKey(String violationId);

    @Query("select max(ticket_violation_id) as max_ticket_violation_id from ticket_violations")
    int getNextPrimaryId();

    @Query("Delete from ticket_violations")
    void removeAll();

    @Query("Delete from ticket_violations where ticket_id=:id")
    void removeById(long id);

}
