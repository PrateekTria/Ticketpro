package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Direction;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface DirectionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDirection(Direction... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDirection(Direction data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDirectionList(List<Direction> DirectionsList);

    @Query("select * from directions order by order_number,direction")
    List<Direction> getDirections();

    @Query("Delete from directions")
    void removeAll();

    @Query("Delete from directions where direction_id=:id")
    void removeById(int id);
}
