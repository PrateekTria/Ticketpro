package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.RepeatOffender;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface RepeatOffendersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRepeatOffender(RepeatOffender... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertRepeatOffender(RepeatOffender data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRepeatOffenderList(List<RepeatOffender> RepeatOffendersList);

    @Query("select * from repeat_offenders where plate=:plate and state_code=:stateCode and violation_id=:violationId")
    RepeatOffender searchRepeatOffender(String plate, int violationId, String stateCode);

    @Query("select * from repeat_offenders WHERE sync_status= 'P'")
    List<RepeatOffender> getRepeatOffender();

    @Query("Delete from repeat_offenders")
    void removeAll();

    @Query("Delete from repeat_offenders where repeat_offender_id=:id")
    void removeById(int id);

    @Query("UPDATE repeat_offenders set sync_status=:status where custid=:custId AND state_code=:state_code AND plate=:plate AND violation_id=:violation_id")
    void updateRepeatOffender(int custId, String state_code, String plate, int violation_id, String status);

    @Query("UPDATE repeat_offenders set count=count+1 where custid=:custId AND state_code=:state_code AND plate=:plate AND violation_id=:violation_id")
    void updateRepeatOffenders(int custId, String state_code, String plate, int violation_id);

    @Query("UPDATE repeat_offenders set count=count-1 where custid=:custId AND state_code=:state_code AND plate= :plate AND violation_id=:violation_id")
    void countUpdateVoidCase(int custId, String state_code, String plate, int violation_id);

    @Query("Select * from repeat_offenders where custid=:custId AND state_code=:state_code AND plate=:plate AND violation_id=:violation_id")
    List<RepeatOffender> checkIsDataAlreadyInDBorNot(int custId, String state_code, String plate, int violation_id);

    @Query("INSERT INTO repeat_offenders (custid,plate,violation,count,violation_id,state_code,created_date)VALUES(:custId,:plate,:violation, :count, :violation_id, :state_code , :created_date)")
    void insertUpdate(int custId, String plate, String violation, int count, int violation_id, String state_code, String created_date);

    @Query("UPDATE repeat_offenders set count=count where custid=:custId AND state_code=:state_code AND plate=:plate AND violation_id=:violation_id")
    void updateInsert(int custId, String state_code, String plate, int violation_id);
}
