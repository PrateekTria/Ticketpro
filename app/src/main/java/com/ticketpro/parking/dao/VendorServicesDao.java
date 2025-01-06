package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.VendorService;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface VendorServicesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVendorService(VendorService... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVendorService(VendorService data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVendorServiceList(List<VendorService> VendorServicesList);

    @Query("select * from vendor_services")
    List<VendorService> getServices();

    @Query("select * from vendor_services where service_name=:serviceName")
    List<VendorService> getServiceByName(String serviceName);

    @Query("Delete from vendor_services")
    void removeAll();

    @Query("Delete from vendor_services where service_id=:id")
    void removeById(int id);
}
