package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Violation;
import com.ticketpro.model.ViolationGroup;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface ViolationGroupsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertViolationGroup(ViolationGroup... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertViolationGroup(ViolationGroup data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertViolationGroupList(List<ViolationGroup> ViolationGroupsList);

    @Query("select * from violation_groups")
    List<ViolationGroup> getViolationGroups();

    @Query("select * from violations where violations.violation_id in (select violation_id from violation_group_violations where group_id=(select group_id from violation_groups where group_code=:group)) order by order_number")
    List<Violation> getViolationsByGroup(String group);

    @Query("Delete from violation_groups")
    void removeAll();

    @Query("Delete from violation_groups where group_id=:id")
    void removeById(int id);


}
