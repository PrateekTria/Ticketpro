package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Body;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface BodyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBody(Body... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBody(Body data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBodiesList(List<Body> bodyList);

    @Query("select * from bodies where UPPER(body)=:bodyTitle")
    Maybe<Body> getBodyByTitle(String bodyTitle);

    @Query("select * from bodies where code=:bodyCode")
    Maybe<Body> getBodyByCode(String bodyCode);

    @Query("select * from bodies where body_id=:bodyId")
    Maybe<Body> getBodyById(int bodyId);

    @Query("select * from bodies order by order_number,body")
    Maybe<List<Body>> getBodies();

    @Query("select body_id from bodies where body=:name")
    Maybe<Integer> getBodyIdByName(String name);

    @Query("select body_id from bodies where code=:code")
    Maybe<Integer> getBodyIdByCode(String code);

    @Query("select code from bodies where body=:name")
    Maybe<String> getBodyCodeByName(String name);

    @Query("select code from bodies where body_id=:id")
    Maybe<String> getBodyCodeById(int id);

    @Query("DELETE from bodies")
    void removeAll();

    @Query("DELETE from bodies where body_id=:id")
    void removeById(int id);
}
