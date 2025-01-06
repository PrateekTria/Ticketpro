package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.MaintenanceLog;
import com.ticketpro.util.Converters;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MaintenanceLogsDao_Impl implements MaintenanceLogsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MaintenanceLog> __insertionAdapterOfMaintenanceLog;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public MaintenanceLogsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMaintenanceLog = new EntityInsertionAdapter<MaintenanceLog>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `maintenance_logs` (`log_id`,`custid`,`userid`,`device_id`,`item_name`,`problem_type`,`comments`,`log_date`,`latitude`,`longitude`,`location`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MaintenanceLog value) {
        stmt.bindLong(1, value.getLogId());
        stmt.bindLong(2, value.getCustId());
        stmt.bindLong(3, value.getUserId());
        stmt.bindLong(4, value.getDeviceId());
        if (value.getItemName() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getItemName());
        }
        if (value.getProblemType() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getProblemType());
        }
        if (value.getComments() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getComments());
        }
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getLogDate());
        if (_tmp == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindLong(8, _tmp);
        }
        if (value.getLatitude() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getLatitude());
        }
        if (value.getLongitude() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getLongitude());
        }
        if (value.getLocation() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getLocation());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from maintenance_logs";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from maintenance_logs where log_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertMaintenanceLog(final MaintenanceLog... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMaintenanceLog.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMaintenanceLog(final MaintenanceLog data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMaintenanceLog.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMaintenanceLogList(final List<MaintenanceLog> MaintenanceLogsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMaintenanceLog.insert(MaintenanceLogsList);
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
  public void removeById(final long id) {
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
  public List<MaintenanceLog> getLogs() {
    final String _sql = "select * from maintenance_logs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfLogId = CursorUtil.getColumnIndexOrThrow(_cursor, "log_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "item_name");
      final int _cursorIndexOfProblemType = CursorUtil.getColumnIndexOrThrow(_cursor, "problem_type");
      final int _cursorIndexOfComments = CursorUtil.getColumnIndexOrThrow(_cursor, "comments");
      final int _cursorIndexOfLogDate = CursorUtil.getColumnIndexOrThrow(_cursor, "log_date");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final List<MaintenanceLog> _result = new ArrayList<MaintenanceLog>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MaintenanceLog _item;
        _item = new MaintenanceLog();
        final long _tmpLogId;
        _tmpLogId = _cursor.getLong(_cursorIndexOfLogId);
        _item.setLogId(_tmpLogId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final String _tmpItemName;
        if (_cursor.isNull(_cursorIndexOfItemName)) {
          _tmpItemName = null;
        } else {
          _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        }
        _item.setItemName(_tmpItemName);
        final String _tmpProblemType;
        if (_cursor.isNull(_cursorIndexOfProblemType)) {
          _tmpProblemType = null;
        } else {
          _tmpProblemType = _cursor.getString(_cursorIndexOfProblemType);
        }
        _item.setProblemType(_tmpProblemType);
        final String _tmpComments;
        if (_cursor.isNull(_cursorIndexOfComments)) {
          _tmpComments = null;
        } else {
          _tmpComments = _cursor.getString(_cursorIndexOfComments);
        }
        _item.setComments(_tmpComments);
        final Date _tmpLogDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfLogDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfLogDate);
        }
        _tmpLogDate = Converters.fromTimestamp(_tmp);
        _item.setLogDate(_tmpLogDate);
        final String _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        }
        _item.setLatitude(_tmpLatitude);
        final String _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        }
        _item.setLongitude(_tmpLongitude);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public MaintenanceLog getLogById(final int logId) {
    final String _sql = "select * from maintenance_logs where log_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, logId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfLogId = CursorUtil.getColumnIndexOrThrow(_cursor, "log_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "item_name");
      final int _cursorIndexOfProblemType = CursorUtil.getColumnIndexOrThrow(_cursor, "problem_type");
      final int _cursorIndexOfComments = CursorUtil.getColumnIndexOrThrow(_cursor, "comments");
      final int _cursorIndexOfLogDate = CursorUtil.getColumnIndexOrThrow(_cursor, "log_date");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final MaintenanceLog _result;
      if(_cursor.moveToFirst()) {
        _result = new MaintenanceLog();
        final long _tmpLogId;
        _tmpLogId = _cursor.getLong(_cursorIndexOfLogId);
        _result.setLogId(_tmpLogId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _result.setDeviceId(_tmpDeviceId);
        final String _tmpItemName;
        if (_cursor.isNull(_cursorIndexOfItemName)) {
          _tmpItemName = null;
        } else {
          _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        }
        _result.setItemName(_tmpItemName);
        final String _tmpProblemType;
        if (_cursor.isNull(_cursorIndexOfProblemType)) {
          _tmpProblemType = null;
        } else {
          _tmpProblemType = _cursor.getString(_cursorIndexOfProblemType);
        }
        _result.setProblemType(_tmpProblemType);
        final String _tmpComments;
        if (_cursor.isNull(_cursorIndexOfComments)) {
          _tmpComments = null;
        } else {
          _tmpComments = _cursor.getString(_cursorIndexOfComments);
        }
        _result.setComments(_tmpComments);
        final Date _tmpLogDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfLogDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfLogDate);
        }
        _tmpLogDate = Converters.fromTimestamp(_tmp);
        _result.setLogDate(_tmpLogDate);
        final String _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        }
        _result.setLatitude(_tmpLatitude);
        final String _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        }
        _result.setLongitude(_tmpLongitude);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _result.setLocation(_tmpLocation);
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
