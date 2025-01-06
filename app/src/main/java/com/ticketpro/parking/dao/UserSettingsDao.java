package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.UserSetting;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface UserSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserSetting(UserSetting... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUserSetting(UserSetting data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserSettingList(List<UserSetting> UserSettingsList);

    @Query("select * from user_settings where userid=:userId")
    UserSetting getUserSettings(int userId);

    @Query("Delete from user_settings")
    void removeAll();

    @Query("Delete from user_settings where userid=:id")
    void removeById(int id);
}
