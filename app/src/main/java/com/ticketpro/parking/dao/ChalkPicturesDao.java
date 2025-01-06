package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.TicketPicture;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface ChalkPicturesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChalkPicture(ChalkPicture... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertChalkPicture(ChalkPicture data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChalkPictureList(List<ChalkPicture> ChalkPicturesList);

    @Query("select * from chalk_pictures where chalk_id=:chalkId")
    List<ChalkPicture> getChalkPictures(long chalkId);

    @Query("select * from chalk_pictures where chalk_id=:chalkId")
    ChalkPicture getChalkPictureById(long chalkId);

    @Query("select * from chalk_pictures where picture_id=:pictureId")
    ChalkPicture getChalkPictureByPrimaryKey(String pictureId);

    @Query("select max(picture_id) as max_picture_id from chalk_pictures")
    int getNextPrimaryId();

    @Query("Delete from chalk_pictures")
    void removeAll();

    @Query("Delete from chalk_pictures where chalk_id=:chalkId")
    void removeChalkPictureById(long chalkId);

    @Query("Update chalk_pictures set sync_status=:values where picture_id=:sNo AND chalk_id=:chalkId")
    void updateChalkPictureStatus(long chalkId, String values,int sNo);

    @Query("select * from chalk_pictures where sync_status='P'")
    List<ChalkPicture> getPendingChalkImageList();

    @Query("select * from chalk_pictures where chalk_id=:id AND sync_status='P'")
    List<ChalkPicture> getPendingChalkImageListById(long id);
}
