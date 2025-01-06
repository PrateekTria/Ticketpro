package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.SpecialActivityPicture;
import com.ticketpro.model.User;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 03-09-2020.
 */
@Dao
public interface SpecialActivityPictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSpecialActivityPicture(SpecialActivityPicture... specialActivityPictures);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertSpecialActivityPicture(SpecialActivityPicture specialActivityPicture);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertSpecialActivityPictureList(List<SpecialActivityPicture> specialActivityPictureList);

    @Query("select max(picture_id) as max_picture_id from special_activities_pictures")
    int getNextPrimaryId();

    @Query("select * from special_activities_pictures where report_id=:report_id")
    List<SpecialActivityPicture> getListOfImage(int report_id);

    @Query("Delete from special_activities_pictures where picture_id=:chalkId ")
    void removeSPAPictureById(long chalkId);

    @Query("Delete from special_activities_pictures where report_id=:chalkId")
    void removeSPAPictureAll(long chalkId);

    @Query("Update special_activities_pictures set report_id=:reportId,custid=:custId,image_name=:imageName,image_path=:imagePath,image_size=:imageSize, picture_date=:pictureDate,image_resolution=:imageResulation where picture_id=:pictureId")
    void updatePicture(int pictureId, int reportId, int custId, String imageName, String imagePath, String imageResulation, String pictureDate, String imageSize);
}
