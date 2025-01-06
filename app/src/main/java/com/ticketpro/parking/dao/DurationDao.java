package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Duration;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface DurationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDuration(Duration... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDuration(Duration data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDurationList(List<Duration> DurationsList);

    @Query("select * from durations order by order_number,duration")
    List<Duration> getDurations();

    @Query("select * from durations where duration_id=:durationId")
    Duration getDurationById(int durationId);

    @Query("select * from durations where duration_id=:durationId")
    Duration getAutoDeleteById(int durationId);

    @Query("select duration_id from durations where duration=:duration")
    int getDurationIdByName(String duration);

    @Query("select duration_mins from durations where duration_id=:durationId")
    int getDurationMinsById(int durationId);

    @Query("select duration_mins from durations where duration=:duration")
    int getDurationMinsByName(String duration);

    @Query("select duration from durations where duration_id=:durationId")
    String getDurationTitleUsingId(int durationId);

    @Query("Delete from durations")
    void removeAll();

    @Query("Delete from durations where duration_id=:id")
    void removeById(int id);
}
