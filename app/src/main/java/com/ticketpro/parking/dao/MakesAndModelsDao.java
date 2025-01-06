package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.MakeAndModel;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface MakesAndModelsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMakesAndModel(MakeAndModel... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMakesAndModel(MakeAndModel data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMakesAndModelList(List<MakeAndModel> MakesAndModelsList);

    @Query("select * from makes_and_models order by order_number,make")
    List<MakeAndModel> getMakesAndModels();

    @Query("select * from makes_and_models where make_id=:makeId")
    MakeAndModel getMakeById(int makeId);

    @Query("select * from makes_and_models where make_code=:makeCode")
    MakeAndModel getMakeByCode(String makeCode);

    @Query("select * from makes_and_models where UPPER(make)=:makeTitle")
    MakeAndModel getMakeByTitle(String makeTitle);

    @Query("select * from makes_and_models where model_code=:modelCode")
    MakeAndModel getModelByCode(String modelCode);

    @Query("select make_id from makes_and_models where make=:name")
    int getMakeIdByName(String name);

    @Query("select make_id from makes_and_models where make_code=:makeCode")
    int getMakeIdByCode(String makeCode);

    @Query("select make_id from makes_and_models where model_code=:modelCode")
    int getModelIdByCode(String modelCode);

    @Query("select make_code from makes_and_models WHERE UPPER(make) = UPPER(:name)")
    String getMakeCodeByName(String name);

    @Query("select make_code from makes_and_models where make_id=:makeId")
    String getMakeCodeById(int makeId);

    @Query("Delete from makes_and_models")
    void removeAll();

    @Query("Delete from makes_and_models where make_id=:id")
    void removeById(int id);
}
