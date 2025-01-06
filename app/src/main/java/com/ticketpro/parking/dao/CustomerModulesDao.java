package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.CustomerModule;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface CustomerModulesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCustomerModule(CustomerModule... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCustomerModule(CustomerModule data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCustomerModuleList(List<CustomerModule> CustomerModulesList);

    @Query("select * from customer_modules")
    List<CustomerModule> getModules();

    @Query("select * from customer_modules where module_id=:moduleId")
    CustomerModule getModuleById(int moduleId);

    @Query("Delete from customer_modules")
    void removeAll();

    @Query("Delete from customer_modules where customer_module_id=:id")
    void removeById(int id);
}
