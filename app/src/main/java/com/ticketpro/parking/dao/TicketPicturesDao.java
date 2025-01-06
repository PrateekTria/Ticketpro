package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.TicketPicture;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface TicketPicturesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTicketPicture(TicketPicture... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTicketPicture(TicketPicture data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTicketPictureList(List<TicketPicture> TicketPicturesList);

    @Query("select * from ticket_pictures where ticket_id=:ticketId and citation_number=:citationNumber")
    List<TicketPicture> getTicketPictures(long ticketId,long citationNumber);

    @Query("select * from ticket_pictures where citation_number=:citationNumber")
    List<TicketPicture> getTicketPicturesByCitation(long citationNumber);

    @Query("select * from ticket_pictures where picture_id=:ticketPictureId")
    TicketPicture getTicketPictureByPrimaryKey(String ticketPictureId);

    @Query("select * from ticket_pictures where citation_number=:citNumber AND sync_status=:status")
    TicketPicture getTicketPictureByPStatus(long citNumber, String status);

    @Query("select max(picture_id) as max_picture_id from ticket_pictures")
    int getNextPrimaryId();

    @Query("Delete from ticket_pictures where ticket_id=:ticketId")
    void removePictureByTicketId(long ticketId);

    @Query("Delete from ticket_pictures where s_no=:pictureId")
    void removePictureById(int pictureId);

    @Query("Delete from ticket_pictures")
    void removeAll();


    @Query("Update ticket_pictures set sync_status=:values where citation_number=:citation AND s_no=:sNo")
    void updateTicket(String citation, String values,int sNo);

    @Query("select * from ticket_pictures where citation_number=:citation AND sync_status='P'")
    List<TicketPicture> getTicketPicturePending(long citation);

}
