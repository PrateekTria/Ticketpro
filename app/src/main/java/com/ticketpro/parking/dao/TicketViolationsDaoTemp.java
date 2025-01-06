package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.TicketViolation;
import com.ticketpro.model.TicketViolationTemp;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface TicketViolationsDaoTemp {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTicketViolation(TicketViolationTemp... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTicketViolation(TicketViolationTemp data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTicketViolationList(List<TicketViolationTemp> TicketViolationsList);

    @Query("select * from ticket_violations_temp")
    List<TicketViolationTemp> getTicketViolations();

    @Query("select * from ticket_violations where citation_number=:citationNumber")
    List<TicketViolationTemp> getTicketViolationsByCitation(long citationNumber);

    @Query("select *,(select violations.violation from violations where violations.violation_id=ticket_violations.violation_id) as violation from ticket_violations where ticket_id=:ticketId")
    List<TicketViolationTemp> getTicketViolationWithComments(long ticketId);

    @Query("select *,(select violations.violation from violations where violations.violation_id=ticket_violations.violation_id) as violation from ticket_violations where citation_number=:citationNumber")
    List<TicketViolationTemp> getTicketViolationByCitationWithComments(long citationNumber);

    @Query("select * from ticket_violations_temp where ticket_violation_id=:violationId")
    TicketViolationTemp getViolationByPrimaryKey(String violationId);

    @Query("select max(ticket_violation_id) as max_ticket_violation_id from ticket_violations_temp")
    int getNextPrimaryId();

    @Query("Delete from ticket_violations_temp")
    void removeAll();

    @Query("Delete from ticket_violations_temp where ticket_violation_id=:id")
    void removeById(long id);

    @Query("SELECT COUNT(ticket_violation_id) FROM ticket_violations_temp")
    int getCount();
}
