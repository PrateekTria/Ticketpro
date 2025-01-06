package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.TicketComment;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface TicketCommentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTicketComment(TicketComment... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTicketComment(TicketComment data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTicketCommentList(List<TicketComment> TicketCommentsList);

    @Query("select * from ticket_comments where ticket_id=:ticketId and citation_number=:citationNumber")
    List<TicketComment> getTicketComments(long ticketId,long citationNumber);

    @Query("select * from ticket_comments where citation_number=:citationNumber")
    List<TicketComment> getTicketCommentsByCitation(long citationNumber);

    @Query("select * from ticket_comments where ticket_comment_id=:commentId")
    TicketComment getCommentsByPrimaryKey(String commentId);

    @Query("select max(ticket_comment_id) as max_ticket_comment_id from ticket_comments")
    int getNextPrimaryId();

    @Query("Delete from ticket_comments where ticket_comment_id=:id")
    void removeById(long id);

    @Query("Delete from ticket_comments")
    void removeAll();
}
