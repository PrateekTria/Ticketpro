package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.SyncData;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface SyncDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSyncData(SyncData... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertSyncData(SyncData data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSyncDataList(List<SyncData> SyncDatasList);

    @Query("select * from sync_data where status='Pending'")
    List<SyncData> getSyncData();

    @Query("select * from sync_data where activity='IMAGE_UPLOAD'")
    List<SyncData> getImageUploadSyncData();

    @Query("select * from sync_data where activity='VOICE_UPLOAD'")
    List<SyncData> getVoiceUploadSyncData();

    @Query("select max(sync_data_id) as max_sync_data_id from sync_data")
    int getNextPrimaryId();

    @Query("Delete from sync_data where status='Done'")
    void removeDoneSyncData();

    @Query("Delete from sync_data where table_name=:tableName and primary_key=:primaryKey")
    void removeSyncData(String tableName, String primaryKey);

    @Query("Delete from sync_data where table_name='pending_images'")
    void removeSyncUploads();
}
