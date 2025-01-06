package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.State;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface StatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertState(State... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertState(State data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStateList(List<State> StatesList);

    @Query("select * from states order by order_number,state")
    List<State> getStates();

    @Query("select * from states where state=:name or code=:name")
    State getStateByName(String name);

    @Query("select state_id from states where state=:state or code=:state")
    int getStateIdByName(String state);

    @Query("select state_id from states where code=:code")
    int getStateIdByCode(String code);

    @Query("select code from states where state_id=:stateId")
    String getStateCodeById(int stateId);

    @Query("select * from states where code='CA'")
    State getDefaultState();

    @Query("Delete from states")
    void removeAll();

    @Query("Delete from states where state_id=:id")
    void removeById(int id);
}
