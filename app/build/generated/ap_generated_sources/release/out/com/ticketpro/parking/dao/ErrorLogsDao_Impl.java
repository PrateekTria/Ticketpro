package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.ErrorLog;
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
public final class ErrorLogsDao_Impl implements ErrorLogsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ErrorLog> __insertionAdapterOfErrorLog;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public ErrorLogsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfErrorLog = new EntityInsertionAdapter<ErrorLog>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `error_logs` (`error_id`,`custid`,`userid`,`device_id`,`error_type`,`error_desc`,`error_date`,`date`,`module`,`app_version`,`device`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ErrorLog value) {
        stmt.bindLong(1, value.getErrorId());
        stmt.bindLong(2, value.getCustId());
        stmt.bindLong(3, value.getUserId());
        stmt.bindLong(4, value.getDeviceId());
        if (value.getErrorType() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getErrorType());
        }
        if (value.getErrorDesc() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getErrorDesc());
        }
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getErrorDate());
        if (_tmp == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindLong(7, _tmp);
        }
        if (value.getDate() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getDate());
        }
        if (value.getModule() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getModule());
        }
        if (value.getApp_version() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getApp_version());
        }
        if (value.getDevice() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getDevice());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from error_logs";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from error_logs where error_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertErrorLog(final ErrorLog... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfErrorLog.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertErrorLog(final ErrorLog data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfErrorLog.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertErrorLogList(final List<ErrorLog> ErrorLogsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfErrorLog.insert(ErrorLogsList);
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
  public List<ErrorLog> getErrorLogs() {
    final String _sql = "select * from error_logs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfErrorId = CursorUtil.getColumnIndexOrThrow(_cursor, "error_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfErrorType = CursorUtil.getColumnIndexOrThrow(_cursor, "error_type");
      final int _cursorIndexOfErrorDesc = CursorUtil.getColumnIndexOrThrow(_cursor, "error_desc");
      final int _cursorIndexOfErrorDate = CursorUtil.getColumnIndexOrThrow(_cursor, "error_date");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfModule = CursorUtil.getColumnIndexOrThrow(_cursor, "module");
      final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "app_version");
      final int _cursorIndexOfDevice = CursorUtil.getColumnIndexOrThrow(_cursor, "device");
      final List<ErrorLog> _result = new ArrayList<ErrorLog>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ErrorLog _item;
        _item = new ErrorLog();
        final int _tmpErrorId;
        _tmpErrorId = _cursor.getInt(_cursorIndexOfErrorId);
        _item.setErrorId(_tmpErrorId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final String _tmpErrorType;
        if (_cursor.isNull(_cursorIndexOfErrorType)) {
          _tmpErrorType = null;
        } else {
          _tmpErrorType = _cursor.getString(_cursorIndexOfErrorType);
        }
        _item.setErrorType(_tmpErrorType);
        final String _tmpErrorDesc;
        if (_cursor.isNull(_cursorIndexOfErrorDesc)) {
          _tmpErrorDesc = null;
        } else {
          _tmpErrorDesc = _cursor.getString(_cursorIndexOfErrorDesc);
        }
        _item.setErrorDesc(_tmpErrorDesc);
        final Date _tmpErrorDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfErrorDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfErrorDate);
        }
        _tmpErrorDate = Converters.fromTimestamp(_tmp);
        _item.setErrorDate(_tmpErrorDate);
        final String _tmpDate;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmpDate = null;
        } else {
          _tmpDate = _cursor.getString(_cursorIndexOfDate);
        }
        _item.setDate(_tmpDate);
        final String _tmpModule;
        if (_cursor.isNull(_cursorIndexOfModule)) {
          _tmpModule = null;
        } else {
          _tmpModule = _cursor.getString(_cursorIndexOfModule);
        }
        _item.setModule(_tmpModule);
        final String _tmpApp_version;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpApp_version = null;
        } else {
          _tmpApp_version = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setApp_version(_tmpApp_version);
        final String _tmpDevice;
        if (_cursor.isNull(_cursorIndexOfDevice)) {
          _tmpDevice = null;
        } else {
          _tmpDevice = _cursor.getString(_cursorIndexOfDevice);
        }
        _item.setDevice(_tmpDevice);
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
    final String _sql = "select max(error_id) as max_error_id from error_logs";
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
