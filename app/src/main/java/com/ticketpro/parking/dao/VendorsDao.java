package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Vendor;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface VendorsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVendor(Vendor... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVendor(Vendor data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVendorList(List<Vendor> VendorsList);

    @Query("select * from vendors")
    List<Vendor> getVendors();

    @Query("select * from vendors where name=:name")
    Vendor getVendorByName(String name);

    @Query("Delete from vendors")
    void removeAll();

    @Query("Delete from vendors where vendor_id =:id")
    void removeById(int id);

}
