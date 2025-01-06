package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.VendorServiceRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface VendorServiceRegistrationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVendorServiceRegistration(VendorServiceRegistration... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVendorServiceRegistration(VendorServiceRegistration data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVendorServiceRegistrationList(List<VendorServiceRegistration> VendorServiceRegistrationsList);

    @Query("select * from vendor_service_registrations")
    List<VendorServiceRegistration> getServiceRegistrations();

    @Query("select * from vendor_service_registrations where service_id=(select service_id from vendor_services where service_name=:serviceName)")
    VendorServiceRegistration getServiceRegistrationByName(String serviceName);

    @Query("select * from vendor_service_registrations where service_id=:serviceId and device_id in(:deviceId,null,0)")
    VendorServiceRegistration getServiceRegistrationByServiceId(int serviceId, int deviceId);

    @Query("select * from vendor_service_registrations where service_id=:serviceId and device_id in(:deviceId,null,0)")
    List<VendorServiceRegistration> getServiceRegistrationByServiceId1(int serviceId, int deviceId);


    @Query("Delete from vendor_service_registrations")
    void removeAll();

    @Query("Delete from vendor_service_registrations where registration_id=:id")
    void removeById(int id);
}
