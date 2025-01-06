package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.ALPRChalk;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface ALPRPhotoChalkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertALPRChalk(ALPRChalk... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertALPRChalk(ALPRChalk data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertALPRChalkList(List<ALPRChalk> chalkList);

    @Query("select *,(select duration from durations where duration_id=ALPRPhotoChalk.chalkDuration) as duration_code from ALPRPhotoChalk order by LastDate")
    Maybe<List<ALPRChalk>> getChalkVehicles();

    @Query("SELECT * from ALPRPhotoChalk where LastDate = (SELECT MAX(LastDate) from ALPRPhotoChalk)")
    Maybe<ALPRChalk> getLastChalkedVehicle();

    @Query("select * from ALPRPhotoChalk where chalkId=:chalkId")
    Maybe<ALPRChalk> getChalkVehicleById(long chalkId);

    @Query("Delete from ALPRPhotoChalk where chalkId=:chalkId")
    void removeChalkById(long chalkId);

    @Query("Select Plate from ALPRPhotoChalk")
    Maybe<List<String>> getAllPlates();

    @Query("Update ALPRPhotoChalk set LastDate=:lastDate, LastParkingBay=:lastParkingBay,LastLocLat=:lastLocLat,LastLocLong=:lastLocLong,LastLocAcc=:lastLocAcc where Plate=:plate")
    Completable updateChalk(String plate, String lastDate, String lastParkingBay, String lastLocLat, String lastLocLong, String lastLocAcc);

    @Query("Update ALPRPhotoChalk set is_expired=:values where Plate=:plate")
    Completable updateChalkExpired(String plate, String values);
}
