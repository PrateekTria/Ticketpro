package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Placard;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface PlacardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertPlacard(Placard... data);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    Completable insertPlacard(Placard data);

    @Query("select * from placard_temp")
    List<Placard> getPlacard();

    @Query("SELECT placard_no FROM placard_temp ORDER BY id DESC")
    List<String> getPlacardNo();

    @Query("select * from placard_temp where placard_no=:placardNo")
    Placard getPlacardById(String placardNo);


    @Query("Delete from placard_temp")
    void removeAll();

    @Query("Delete from placard_temp where placard_no=:name")
    void removeById(String name);
}
