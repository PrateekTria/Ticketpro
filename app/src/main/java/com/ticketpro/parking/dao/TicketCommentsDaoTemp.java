package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.TicketCommentTemp;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface TicketCommentsDaoTemp {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTicketCommentTemp(TicketCommentTemp... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTicketCommentTemp(TicketCommentTemp data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTicketCommentTempList(List<TicketCommentTemp> TicketCommentTempsList);

    @Query("select * from ticket_comments_temp")
    List<TicketCommentTemp> getTicketCommentTemps();

    @Query("select * from ticket_comments_temp where citation_number=:citationNumber")
    List<TicketCommentTemp> getTicketCommentTempsByCitation(long citationNumber);

    @Query("select * from ticket_comments_temp where ticket_comment_id=:commentId")
    TicketCommentTemp getCommentsByPrimaryKey(String commentId);

    @Query("select max(ticket_comment_id) as max_ticket_comment_id from ticket_comments_temp")
    int getNextPrimaryId();

    @Query("Delete from ticket_comments_temp where ticket_comment_id=:id")
    void removeById(long id);

    @Query("Delete from ticket_comments_temp")
    void removeAll();

    @Query("SELECT COUNT(ticket_comment_id) FROM ticket_comments_temp")
    int getCount();
}
