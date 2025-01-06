package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.Duration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DurationDao_Impl implements DurationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Duration> __insertionAdapterOfDuration;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public DurationDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDuration = new EntityInsertionAdapter<Duration>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `durations` (`duration_id`,`custid`,`duration`,`duration_mins`,`order_number`,`default_violation_id`,`auto_delete`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Duration value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getCustId());
        if (value.getTitle() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTitle());
        }
        stmt.bindLong(4, value.getDurationMinutes());
        stmt.bindLong(5, value.getOrderNumber());
        stmt.bindLong(6, value.getDefaultViolationId());
        if (value.getAutoDelete() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getAutoDelete());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from durations";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from durations where duration_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertDuration(final Duration... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDuration.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertDuration(final Duration data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDuration.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertDurationList(final List<Duration> DurationsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDuration.insert(DurationsList);
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
  public List<Duration> getDurations() {
    final String _sql = "select * from durations order by order_number,duration";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_mins");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final int _cursorIndexOfDefaultViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "default_violation_id");
      final int _cursorIndexOfAutoDelete = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_delete");
      final List<Duration> _result = new ArrayList<Duration>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Duration _item;
        _item = new Duration();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final int _tmpDurationMinutes;
        _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
        _item.setDurationMinutes(_tmpDurationMinutes);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _item.setOrderNumber(_tmpOrderNumber);
        final int _tmpDefaultViolationId;
        _tmpDefaultViolationId = _cursor.getInt(_cursorIndexOfDefaultViolationId);
        _item.setDefaultViolationId(_tmpDefaultViolationId);
        final String _tmpAutoDelete;
        if (_cursor.isNull(_cursorIndexOfAutoDelete)) {
          _tmpAutoDelete = null;
        } else {
          _tmpAutoDelete = _cursor.getString(_cursorIndexOfAutoDelete);
        }
        _item.setAutoDelete(_tmpAutoDelete);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Duration getDurationById(final int durationId) {
    final String _sql = "select * from durations where duration_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, durationId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_mins");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final int _cursorIndexOfDefaultViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "default_violation_id");
      final int _cursorIndexOfAutoDelete = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_delete");
      final Duration _result;
      if(_cursor.moveToFirst()) {
        _result = new Duration();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result.setTitle(_tmpTitle);
        final int _tmpDurationMinutes;
        _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
        _result.setDurationMinutes(_tmpDurationMinutes);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _result.setOrderNumber(_tmpOrderNumber);
        final int _tmpDefaultViolationId;
        _tmpDefaultViolationId = _cursor.getInt(_cursorIndexOfDefaultViolationId);
        _result.setDefaultViolationId(_tmpDefaultViolationId);
        final String _tmpAutoDelete;
        if (_cursor.isNull(_cursorIndexOfAutoDelete)) {
          _tmpAutoDelete = null;
        } else {
          _tmpAutoDelete = _cursor.getString(_cursorIndexOfAutoDelete);
        }
        _result.setAutoDelete(_tmpAutoDelete);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Duration getAutoDeleteById(final int durationId) {
    final String _sql = "select * from durations where duration_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, durationId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_mins");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final int _cursorIndexOfDefaultViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "default_violation_id");
      final int _cursorIndexOfAutoDelete = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_delete");
      final Duration _result;
      if(_cursor.moveToFirst()) {
        _result = new Duration();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result.setTitle(_tmpTitle);
        final int _tmpDurationMinutes;
        _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
        _result.setDurationMinutes(_tmpDurationMinutes);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _result.setOrderNumber(_tmpOrderNumber);
        final int _tmpDefaultViolationId;
        _tmpDefaultViolationId = _cursor.getInt(_cursorIndexOfDefaultViolationId);
        _result.setDefaultViolationId(_tmpDefaultViolationId);
        final String _tmpAutoDelete;
        if (_cursor.isNull(_cursorIndexOfAutoDelete)) {
          _tmpAutoDelete = null;
        } else {
          _tmpAutoDelete = _cursor.getString(_cursorIndexOfAutoDelete);
        }
        _result.setAutoDelete(_tmpAutoDelete);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getDurationIdByName(final String duration) {
    final String _sql = "select duration_id from durations where duration=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (duration == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, duration);
    }
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

  @Override
  public int getDurationMinsById(final int durationId) {
    final String _sql = "select duration_mins from durations where duration_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, durationId);
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

  @Override
  public int getDurationMinsByName(final String duration) {
    final String _sql = "select duration_mins from durations where duration=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (duration == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, duration);
    }
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

  @Override
  public String getDurationTitleUsingId(final int durationId) {
    final String _sql = "select duration from durations where duration_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, durationId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final String _result;
      if(_cursor.moveToFirst()) {
        if (_cursor.isNull(0)) {
          _result = null;
        } else {
          _result = _cursor.getString(0);
        }
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
