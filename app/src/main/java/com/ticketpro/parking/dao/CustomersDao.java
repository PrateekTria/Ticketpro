package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.CustomerInfo;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface CustomersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCustomerInfo(CustomerInfo... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCustomerInfo(CustomerInfo data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertCustomerInfoList(List<CustomerInfo> CustomerInfosList);

    @Query("select * from customers where custid=:custId")
    CustomerInfo getCustomerInfoInfo(int custId);

    @Query("select * from customers")
    List<CustomerInfo> getCustomerInfos();

    @Query("Delete from customers where custid <>:activeCustId")
    void removeAll(int activeCustId);

    @Query("Delete from customers where custid=:id")
    void removeById(int id);
}
