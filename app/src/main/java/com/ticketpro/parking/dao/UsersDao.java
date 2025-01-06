package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.User;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(User... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUsers(User data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUsersList(List<User> UsersList);

    @Query("select * from users order by username")
    List<User> getAllUsers();

    @Query("select * from users where UPPER(modules) like '%' ||:moduleName || '%' order by username")
    List<User> getUsers(String moduleName);

    @Query("select * from users where userid=:userId")
    User getUserInfo(int userId);

    @Query("Delete from users")
    void removeAll();

    @Query("Delete from users where userid=:id")
    void removeById(int id);

    @Query("select userid from users where password =:password")
    int getUserId(String password);

    @Query("select * from users where rmsid=:rmsid")
    User getUserByRmsidInfo(String rmsid);

}
