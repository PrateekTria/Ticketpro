package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.ticketpro.model.EulaModel;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface TPMEulaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEulaModel(EulaModel... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEulaModel(EulaModel data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEulaModelList(List<EulaModel> EulaModelsList);

}
