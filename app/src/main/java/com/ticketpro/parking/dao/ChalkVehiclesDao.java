package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.ChalkVehicle;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface ChalkVehiclesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChalkVehicle(ChalkVehicle... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertChalkVehicle(ChalkVehicle data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChalkVehicleList(List<ChalkVehicle> ChalkVehiclesList);

    @Query("select * from chalk_vehicles where userid=:userId order by chalk_date DESC")
    List<ChalkVehicle> getChalkVehicles(int userId);

    @Query("select * from chalk_vehicles order by chalk_date DESC")
    List<ChalkVehicle> getAllChalkedVehicle();

    @Query("select * from chalk_vehicles where sync_status='P' order by chalk_date DESC")
    List<ChalkVehicle> getPendingChalkedVehicle();

    @Query("select * from chalk_vehicles where sync_status IS NULL order by chalk_date DESC")
    List<ChalkVehicle> getPendingOldChalkedVehicle();

    @Query("select * from chalk_vehicles where sync_status='PI' order by chalk_date DESC")
    List<ChalkVehicle> getPendingPIChalkedVehicle();

    @Query("Update chalk_vehicles set sync_status=:status where chalk_id=:chalkId")
    void updateChalkVehicleStatus(String status, long chalkId);

    @Query("select * from chalk_vehicles where chalk_type=:type order by chalk_date DESC")
    List<ChalkVehicle> getChalkByType(String type);

    @Query("select * from chalk_vehicles where chalk_id=:chalkId")
    ChalkVehicle getChalkVehicleByPrimaryKey(long chalkId);

    @Query("select * from chalk_vehicles where chalk_id=:chalkId")
    ChalkVehicle getChalkVehicleById(long chalkId);

    @Query("select * from chalk_vehicles where plate=:plate and state_id in (select state_id from states where code=:state) order by chalk_date DESC")
    ChalkVehicle searchPreviousChalkByPlate(String plate, String state);

    @Query("Delete from chalk_vehicles")
    void removeAll();

    @Query("Delete from chalk_vehicles where chalk_id=:chalkId")
    void removeChalkVehicleById(long chalkId);
}
