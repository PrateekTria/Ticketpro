package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.LprBodyMap;

import java.util.List;

import io.reactivex.Maybe;
@Dao
public interface PlrBodyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLprBody(LprBodyMap... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBody(LprBodyMap data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBodiesList(List<LprBodyMap> bodyList);

    @Query("select * from lprbodymap where UPPER(LPRBody)=:bodyTitle")
    Maybe<LprBodyMap> getBodyByTitle(String bodyTitle);

    @Query("select * from lprbodymap where TicketBody=:bodyCode")
    Maybe<LprBodyMap> getBodyByCode(String bodyCode);

    @Query("select * from lprbodymap where body_id=:bodyId")
    Maybe<LprBodyMap> getBodyById(int bodyId);

    @Query("select * from lprbodymap order by order_number,LPRBody")
    Maybe<List<LprBodyMap>> getBodies();

    @Query("select body_id from lprbodymap where LPRBody=:name")
    Maybe<Integer> getBodyIdByName(String name);

    @Query("select body_id from lprbodymap where TicketBody=:code")
    Maybe<Integer> getBodyIdByCode(String code);

    @Query("select TicketBody from lprbodymap where UPPER(LPRBody)=:name")
    Maybe<String> getBodyCodeByName(String name);


    @Query("select TicketBody from lprbodymap where body_id=:id")
    Maybe<String> getBodyCodeById(int id);

    @Query("DELETE from lprbodymap")
    void removeAll();

    @Query("DELETE from lprbodymap where body_id=:id")
    void removeById(int id);
}
