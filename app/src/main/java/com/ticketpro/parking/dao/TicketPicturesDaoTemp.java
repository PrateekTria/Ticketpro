package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.TicketPictureTemp;

import java.util.List;

import io.reactivex.Completable;
@Dao
public interface TicketPicturesDaoTemp {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTicketPictureTemp(TicketPictureTemp... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTicketPictureTemp(TicketPictureTemp data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTicketPictureTempList(List<TicketPictureTemp> TicketPictureTempsList);

    @Query("select * from ticket_pictures_temp")
    List<TicketPictureTemp> getTicketPictureTemps();

    @Query("select * from ticket_pictures_temp where citation_number=:citationNumber")
    List<TicketPictureTemp> getTicketPictureTempsByCitation(long citationNumber);

    @Query("select * from ticket_pictures_temp where picture_id=:ticketPictureId")
    TicketPictureTemp getTicketPictureTempByPrimaryKey(String ticketPictureId);

    @Query("select max(picture_id) as max_picture_id from ticket_pictures_temp")
    int getNextPrimaryId();

    @Query("Delete from ticket_pictures_temp where ticket_id=:ticketId")
    void removePictureByTicketId(long ticketId);

    @Query("Delete from ticket_pictures_temp where s_no=:pictureId")
    void removePictureById(int pictureId);

    @Query("Delete from ticket_pictures_temp")
    void removeAll();


    @Query("SELECT COUNT(s_no) FROM ticket_pictures_temp")
    int getCount();
}
