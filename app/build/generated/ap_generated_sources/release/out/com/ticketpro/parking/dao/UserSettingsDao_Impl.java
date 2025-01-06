package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.UserSetting;
import io.reactivex.Completable;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.Void;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserSettingsDao_Impl implements UserSettingsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserSetting> __insertionAdapterOfUserSetting;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public UserSettingsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserSetting = new EntityInsertionAdapter<UserSetting>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `user_settings` (`setting_id`,`userid`,`device_id`,`auto_sync_interval`,`data_retention_period`,`gps`,`data_backup`,`user_prefs`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, UserSetting value) {
        stmt.bindLong(1, value.getSettingId());
        stmt.bindLong(2, value.getUserId());
        stmt.bindLong(3, value.getDeviceId());
        stmt.bindLong(4, value.getAutoSyncInterval());
        stmt.bindLong(5, value.getDataRetentionPeriod());
        if (value.getGps() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getGps());
        }
        if (value.getDataBackup() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getDataBackup());
        }
        if (value.getUserPrefs() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getUserPrefs());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from user_settings";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from user_settings where userid=?";
        return _query;
      }
    };
  }

  @Override
  public void insertUserSetting(final UserSetting... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfUserSetting.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertUserSetting(final UserSetting data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserSetting.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertUserSettingList(final List<UserSetting> UserSettingsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfUserSetting.insert(UserSettingsList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void removeAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveAll.release(_stmt);
    }
  }

  @Override
  public void removeById(final int id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveById.release(_stmt);
    }
  }

  @Override
  public UserSetting getUserSettings(final int userId) {
    final String _sql = "select * from user_settings where userid=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfSettingId = CursorUtil.getColumnIndexOrThrow(_cursor, "setting_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfAutoSyncInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_sync_interval");
      final int _cursorIndexOfDataRetentionPeriod = CursorUtil.getColumnIndexOrThrow(_cursor, "data_retention_period");
      final int _cursorIndexOfGps = CursorUtil.getColumnIndexOrThrow(_cursor, "gps");
      final int _cursorIndexOfDataBackup = CursorUtil.getColumnIndexOrThrow(_cursor, "data_backup");
      final int _cursorIndexOfUserPrefs = CursorUtil.getColumnIndexOrThrow(_cursor, "user_prefs");
      final UserSetting _result;
      if(_cursor.moveToFirst()) {
        _result = new UserSetting();
        final int _tmpSettingId;
        _tmpSettingId = _cursor.getInt(_cursorIndexOfSettingId);
        _result.setSettingId(_tmpSettingId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _result.setDeviceId(_tmpDeviceId);
        final int _tmpAutoSyncInterval;
        _tmpAutoSyncInterval = _cursor.getInt(_cursorIndexOfAutoSyncInterval);
        _result.setAutoSyncInterval(_tmpAutoSyncInterval);
        final int _tmpDataRetentionPeriod;
        _tmpDataRetentionPeriod = _cursor.getInt(_cursorIndexOfDataRetentionPeriod);
        _result.setDataRetentionPeriod(_tmpDataRetentionPeriod);
        final String _tmpGps;
        if (_cursor.isNull(_cursorIndexOfGps)) {
          _tmpGps = null;
        } else {
          _tmpGps = _cursor.getString(_cursorIndexOfGps);
        }
        _result.setGps(_tmpGps);
        final String _tmpDataBackup;
        if (_cursor.isNull(_cursorIndexOfDataBackup)) {
          _tmpDataBackup = null;
        } else {
          _tmpDataBackup = _cursor.getString(_cursorIndexOfDataBackup);
        }
        _result.setDataBackup(_tmpDataBackup);
        final String _tmpUserPrefs;
        if (_cursor.isNull(_cursorIndexOfUserPrefs)) {
          _tmpUserPrefs = null;
        } else {
          _tmpUserPrefs = _cursor.getString(_cursorIndexOfUserPrefs);
        }
        _result.setUserPrefs(_tmpUserPrefs);
      } else {
        _result = null;
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
