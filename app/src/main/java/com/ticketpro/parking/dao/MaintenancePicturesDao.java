package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.MaintenancePicture;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface MaintenancePicturesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMaintenancePicture(MaintenancePicture... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMaintenancePicture(MaintenancePicture data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMaintenancePictureList(List<MaintenancePicture> MaintenancePicturesList);

    @Query("select * from maintenance_pictures where maintenance_id=:maintenanceId")
    List<MaintenancePicture> getPictures(long maintenanceId);

    @Query("select max(picture_id) as max_picture_id from maintenance_pictures")
    int getNextPrimaryId();

    @Query("Delete from maintenance_pictures")
    void removeAll();

    @Query("Delete from maintenance_pictures where picture_id=:id")
    void removeById(int id);
}
