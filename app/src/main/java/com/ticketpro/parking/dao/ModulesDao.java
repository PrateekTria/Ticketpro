package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Module;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface ModulesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModule(Module... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModule(Module data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModuleList(List<Module> ModulesList);

    @Query("select * from modules")
    List<Module> getModules();

    @Query("select * from modules where UPPER(module_name)=:moduleName")
    Module getModuleByName(String moduleName);

    @Query("Delete from modules")
    void removeAll();

    @Query("Delete from modules where module_id=:id")
    void removeById(int id);
}


