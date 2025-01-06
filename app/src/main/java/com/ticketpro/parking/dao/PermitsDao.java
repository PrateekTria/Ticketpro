package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Permit;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface PermitsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPermit(Permit... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPermit(Permit data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPermitList(List<Permit> PermitsList);

    @Query("select * from permits")
    List<Permit> getPermits();

    @Query("select * from permits where permit_number=:permit order by permit_id DESC")
    Permit searchPermitHistory(String permit);

    @Query("select * from permits where permit_number=:permit order by permit_id DESC")
    List<Permit> searchAllPermitHistory(String permit);

    @Query("select * from permits where plate=:plate order by permit_id DESC")
    Permit searchPermitByPlate(String plate);

    @Query("select * from permits where vin=:vin and state_code=:state and custid=:custId order by permit_id DESC")
    Permit searchPermitByVin(String vin, String state, int custId);

    @Query("select * from permits where plate=:plate and state_code=:state order by permit_id DESC")
    List<Permit> searchAllPermitByPlate(String plate, String state);

    @Query("Delete from permits")
    void removeAll();

    @Query("Delete from permits where permit_id=:id")
    void removeById(int id);
}
