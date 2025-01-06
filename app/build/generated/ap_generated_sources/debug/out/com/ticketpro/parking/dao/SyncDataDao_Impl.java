package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.SyncData;
import com.ticketpro.util.Converters;
import io.reactivex.Completable;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.Void;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SyncDataDao_Impl implements SyncDataDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SyncData> __insertionAdapterOfSyncData;

  private final SharedSQLiteStatement __preparedStmtOfRemoveDoneSyncData;

  private final SharedSQLiteStatement __preparedStmtOfRemoveSyncData;

  private final SharedSQLiteStatement __preparedStmtOfRemoveSyncUploads;

  public SyncDataDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSyncData = new EntityInsertionAdapter<SyncData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `sync_data` (`sync_data_id`,`userid`,`custid`,`activity`,`table_name`,`primary_key`,`activity_source`,`sql_query`,`activity_date`,`status`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SyncData value) {
        stmt.bindLong(1, value.getSyncDataId());
        stmt.bindLong(2, value.getUserId());
        stmt.bindLong(3, value.getCustId());
        if (value.getActivity() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getActivity());
        }
        if (value.getTableName() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getTableName());
        }
        if (value.getPrimaryKey() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getPrimaryKey());
        }
        if (value.getActivitySource() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getActivitySource());
        }
        if (value.getSqlQuery() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getSqlQuery());
        }
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getActivityDate());
        if (_tmp == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindLong(9, _tmp);
        }
        if (value.getStatus() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getStatus());
        }
      }
    };
    this.__preparedStmtOfRemoveDoneSyncData = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from sync_data where status='Done'";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveSyncData = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from sync_data where table_name=? and primary_key=?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveSyncUploads = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from sync_data where table_name='pending_images'";
        return _query;
      }
    };
  }

  @Override
  public void insertSyncData(final SyncData... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSyncData.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertSyncData(final SyncData data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSyncData.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertSyncDataList(final List<SyncData> SyncDatasList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSyncData.insert(SyncDatasList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void removeDoneSyncData() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveDoneSyncData.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveDoneSyncData.release(_stmt);
    }
  }

  @Override
  public void removeSyncData(final String tableName, final String primaryKey) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveSyncData.acquire();
    int _argIndex = 1;
    if (tableName == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, tableName);
    }
    _argIndex = 2;
    if (primaryKey == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, primaryKey);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveSyncData.release(_stmt);
    }
  }

  @Override
  public void removeSyncUploads() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveSyncUploads.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveSyncUploads.release(_stmt);
    }
  }

  @Override
  public List<SyncData> getSyncData() {
    final String _sql = "select * from sync_data where status='Pending'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfSyncDataId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_data_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfActivity = CursorUtil.getColumnIndexOrThrow(_cursor, "activity");
      final int _cursorIndexOfTableName = CursorUtil.getColumnIndexOrThrow(_cursor, "table_name");
      final int _cursorIndexOfPrimaryKey = CursorUtil.getColumnIndexOrThrow(_cursor, "primary_key");
      final int _cursorIndexOfActivitySource = CursorUtil.getColumnIndexOrThrow(_cursor, "activity_source");
      final int _cursorIndexOfSqlQuery = CursorUtil.getColumnIndexOrThrow(_cursor, "sql_query");
      final int _cursorIndexOfActivityDate = CursorUtil.getColumnIndexOrThrow(_cursor, "activity_date");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final List<SyncData> _result = new ArrayList<SyncData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final SyncData _item;
        _item = new SyncData();
        final int _tmpSyncDataId;
        _tmpSyncDataId = _cursor.getInt(_cursorIndexOfSyncDataId);
        _item.setSyncDataId(_tmpSyncDataId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpActivity;
        if (_cursor.isNull(_cursorIndexOfActivity)) {
          _tmpActivity = null;
        } else {
          _tmpActivity = _cursor.getString(_cursorIndexOfActivity);
        }
        _item.setActivity(_tmpActivity);
        final String _tmpTableName;
        if (_cursor.isNull(_cursorIndexOfTableName)) {
          _tmpTableName = null;
        } else {
          _tmpTableName = _cursor.getString(_cursorIndexOfTableName);
        }
        _item.setTableName(_tmpTableName);
        final String _tmpPrimaryKey;
        if (_cursor.isNull(_cursorIndexOfPrimaryKey)) {
          _tmpPrimaryKey = null;
        } else {
          _tmpPrimaryKey = _cursor.getString(_cursorIndexOfPrimaryKey);
        }
        _item.setPrimaryKey(_tmpPrimaryKey);
        final String _tmpActivitySource;
        if (_cursor.isNull(_cursorIndexOfActivitySource)) {
          _tmpActivitySource = null;
        } else {
          _tmpActivitySource = _cursor.getString(_cursorIndexOfActivitySource);
        }
        _item.setActivitySource(_tmpActivitySource);
        final String _tmpSqlQuery;
        if (_cursor.isNull(_cursorIndexOfSqlQuery)) {
          _tmpSqlQuery = null;
        } else {
          _tmpSqlQuery = _cursor.getString(_cursorIndexOfSqlQuery);
        }
        _item.setSqlQuery(_tmpSqlQuery);
        final Date _tmpActivityDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfActivityDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfActivityDate);
        }
        _tmpActivityDate = Converters.fromTimestamp(_tmp);
        _item.setActivityDate(_tmpActivityDate);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<SyncData> getImageUploadSyncData() {
    final String _sql = "select * from sync_data where activity='IMAGE_UPLOAD'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfSyncDataId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_data_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfActivity = CursorUtil.getColumnIndexOrThrow(_cursor, "activity");
      final int _cursorIndexOfTableName = CursorUtil.getColumnIndexOrThrow(_cursor, "table_name");
      final int _cursorIndexOfPrimaryKey = CursorUtil.getColumnIndexOrThrow(_cursor, "primary_key");
      final int _cursorIndexOfActivitySource = CursorUtil.getColumnIndexOrThrow(_cursor, "activity_source");
      final int _cursorIndexOfSqlQuery = CursorUtil.getColumnIndexOrThrow(_cursor, "sql_query");
      final int _cursorIndexOfActivityDate = CursorUtil.getColumnIndexOrThrow(_cursor, "activity_date");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final List<SyncData> _result = new ArrayList<SyncData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final SyncData _item;
        _item = new SyncData();
        final int _tmpSyncDataId;
        _tmpSyncDataId = _cursor.getInt(_cursorIndexOfSyncDataId);
        _item.setSyncDataId(_tmpSyncDataId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpActivity;
        if (_cursor.isNull(_cursorIndexOfActivity)) {
          _tmpActivity = null;
        } else {
          _tmpActivity = _cursor.getString(_cursorIndexOfActivity);
        }
        _item.setActivity(_tmpActivity);
        final String _tmpTableName;
        if (_cursor.isNull(_cursorIndexOfTableName)) {
          _tmpTableName = null;
        } else {
          _tmpTableName = _cursor.getString(_cursorIndexOfTableName);
        }
        _item.setTableName(_tmpTableName);
        final String _tmpPrimaryKey;
        if (_cursor.isNull(_cursorIndexOfPrimaryKey)) {
          _tmpPrimaryKey = null;
        } else {
          _tmpPrimaryKey = _cursor.getString(_cursorIndexOfPrimaryKey);
        }
        _item.setPrimaryKey(_tmpPrimaryKey);
        final String _tmpActivitySource;
        if (_cursor.isNull(_cursorIndexOfActivitySource)) {
          _tmpActivitySource = null;
        } else {
          _tmpActivitySource = _cursor.getString(_cursorIndexOfActivitySource);
        }
        _item.setActivitySource(_tmpActivitySource);
        final String _tmpSqlQuery;
        if (_cursor.isNull(_cursorIndexOfSqlQuery)) {
          _tmpSqlQuery = null;
        } else {
          _tmpSqlQuery = _cursor.getString(_cursorIndexOfSqlQuery);
        }
        _item.setSqlQuery(_tmpSqlQuery);
        final Date _tmpActivityDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfActivityDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfActivityDate);
        }
        _tmpActivityDate = Converters.fromTimestamp(_tmp);
        _item.setActivityDate(_tmpActivityDate);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<SyncData> getVoiceUploadSyncData() {
    final String _sql = "select * from sync_data where activity='VOICE_UPLOAD'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfSyncDataId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_data_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfActivity = CursorUtil.getColumnIndexOrThrow(_cursor, "activity");
      final int _cursorIndexOfTableName = CursorUtil.getColumnIndexOrThrow(_cursor, "table_name");
      final int _cursorIndexOfPrimaryKey = CursorUtil.getColumnIndexOrThrow(_cursor, "primary_key");
      final int _cursorIndexOfActivitySource = CursorUtil.getColumnIndexOrThrow(_cursor, "activity_source");
      final int _cursorIndexOfSqlQuery = CursorUtil.getColumnIndexOrThrow(_cursor, "sql_query");
      final int _cursorIndexOfActivityDate = CursorUtil.getColumnIndexOrThrow(_cursor, "activity_date");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final List<SyncData> _result = new ArrayList<SyncData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final SyncData _item;
        _item = new SyncData();
        final int _tmpSyncDataId;
        _tmpSyncDataId = _cursor.getInt(_cursorIndexOfSyncDataId);
        _item.setSyncDataId(_tmpSyncDataId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpActivity;
        if (_cursor.isNull(_cursorIndexOfActivity)) {
          _tmpActivity = null;
        } else {
          _tmpActivity = _cursor.getString(_cursorIndexOfActivity);
        }
        _item.setActivity(_tmpActivity);
        final String _tmpTableName;
        if (_cursor.isNull(_cursorIndexOfTableName)) {
          _tmpTableName = null;
        } else {
          _tmpTableName = _cursor.getString(_cursorIndexOfTableName);
        }
        _item.setTableName(_tmpTableName);
        final String _tmpPrimaryKey;
        if (_cursor.isNull(_cursorIndexOfPrimaryKey)) {
          _tmpPrimaryKey = null;
        } else {
          _tmpPrimaryKey = _cursor.getString(_cursorIndexOfPrimaryKey);
        }
        _item.setPrimaryKey(_tmpPrimaryKey);
        final String _tmpActivitySource;
        if (_cursor.isNull(_cursorIndexOfActivitySource)) {
          _tmpActivitySource = null;
        } else {
          _tmpActivitySource = _cursor.getString(_cursorIndexOfActivitySource);
        }
        _item.setActivitySource(_tmpActivitySource);
        final String _tmpSqlQuery;
        if (_cursor.isNull(_cursorIndexOfSqlQuery)) {
          _tmpSqlQuery = null;
        } else {
          _tmpSqlQuery = _cursor.getString(_cursorIndexOfSqlQuery);
        }
        _item.setSqlQuery(_tmpSqlQuery);
        final Date _tmpActivityDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfActivityDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfActivityDate);
        }
        _tmpActivityDate = Converters.fromTimestamp(_tmp);
        _item.setActivityDate(_tmpActivityDate);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getNextPrimaryId() {
    final String _sql = "select max(sync_data_id) as max_sync_data_id from sync_data";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
