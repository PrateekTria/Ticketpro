package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.RepeatOffender;
import io.reactivex.Completable;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.Void;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RepeatOffendersDao_Impl implements RepeatOffendersDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RepeatOffender> __insertionAdapterOfRepeatOffender;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  private final SharedSQLiteStatement __preparedStmtOfUpdateRepeatOffender;

  private final SharedSQLiteStatement __preparedStmtOfUpdateRepeatOffenders;

  private final SharedSQLiteStatement __preparedStmtOfCountUpdateVoidCase;

  private final SharedSQLiteStatement __preparedStmtOfInsertUpdate;

  private final SharedSQLiteStatement __preparedStmtOfUpdateInsert;

  public RepeatOffendersDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRepeatOffender = new EntityInsertionAdapter<RepeatOffender>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `repeat_offenders` (`repeat_offender_id`,`custid`,`plate`,`violation`,`count`,`violation_id`,`state_code`,`created_date`,`sync_status`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RepeatOffender value) {
        stmt.bindLong(1, value.getRepeatOffenderId());
        stmt.bindLong(2, value.getCustId());
        if (value.getPlate() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPlate());
        }
        if (value.getViolation() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getViolation());
        }
        stmt.bindLong(5, value.getCount());
        stmt.bindLong(6, value.getViolationId());
        if (value.getStateCode() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getStateCode());
        }
        if (value.getCreatTime() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getCreatTime());
        }
        if (value.getSyncStatus() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getSyncStatus());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from repeat_offenders";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from repeat_offenders where repeat_offender_id=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateRepeatOffender = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE repeat_offenders set sync_status=? where custid=? AND state_code=? AND plate=? AND violation_id=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateRepeatOffenders = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE repeat_offenders set count=count+1 where custid=? AND state_code=? AND plate=? AND violation_id=?";
        return _query;
      }
    };
    this.__preparedStmtOfCountUpdateVoidCase = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE repeat_offenders set count=count-1 where custid=? AND state_code=? AND plate= ? AND violation_id=?";
        return _query;
      }
    };
    this.__preparedStmtOfInsertUpdate = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "INSERT INTO repeat_offenders (custid,plate,violation,count,violation_id,state_code,created_date)VALUES(?,?,?, ?, ?, ? , ?)";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateInsert = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE repeat_offenders set count=count where custid=? AND state_code=? AND plate=? AND violation_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertRepeatOffender(final RepeatOffender... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRepeatOffender.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertRepeatOffender(final RepeatOffender data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRepeatOffender.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertRepeatOffenderList(final List<RepeatOffender> RepeatOffendersList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRepeatOffender.insert(RepeatOffendersList);
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
  public void updateRepeatOffender(final int custId, final String state_code, final String plate,
      final int violation_id, final String status) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateRepeatOffender.acquire();
    int _argIndex = 1;
    if (status == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, status);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, custId);
    _argIndex = 3;
    if (state_code == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, state_code);
    }
    _argIndex = 4;
    if (plate == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, plate);
    }
    _argIndex = 5;
    _stmt.bindLong(_argIndex, violation_id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateRepeatOffender.release(_stmt);
    }
  }

  @Override
  public void updateRepeatOffenders(final int custId, final String state_code, final String plate,
      final int violation_id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateRepeatOffenders.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, custId);
    _argIndex = 2;
    if (state_code == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, state_code);
    }
    _argIndex = 3;
    if (plate == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, plate);
    }
    _argIndex = 4;
    _stmt.bindLong(_argIndex, violation_id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateRepeatOffenders.release(_stmt);
    }
  }

  @Override
  public void countUpdateVoidCase(final int custId, final String state_code, final String plate,
      final int violation_id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfCountUpdateVoidCase.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, custId);
    _argIndex = 2;
    if (state_code == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, state_code);
    }
    _argIndex = 3;
    if (plate == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, plate);
    }
    _argIndex = 4;
    _stmt.bindLong(_argIndex, violation_id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfCountUpdateVoidCase.release(_stmt);
    }
  }

  @Override
  public void insertUpdate(final int custId, final String plate, final String violation,
      final int count, final int violation_id, final String state_code, final String created_date) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfInsertUpdate.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, custId);
    _argIndex = 2;
    if (plate == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, plate);
    }
    _argIndex = 3;
    if (violation == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, violation);
    }
    _argIndex = 4;
    _stmt.bindLong(_argIndex, count);
    _argIndex = 5;
    _stmt.bindLong(_argIndex, violation_id);
    _argIndex = 6;
    if (state_code == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, state_code);
    }
    _argIndex = 7;
    if (created_date == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, created_date);
    }
    __db.beginTransaction();
    try {
      _stmt.executeInsert();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfInsertUpdate.release(_stmt);
    }
  }

  @Override
  public void updateInsert(final int custId, final String state_code, final String plate,
      final int violation_id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateInsert.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, custId);
    _argIndex = 2;
    if (state_code == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, state_code);
    }
    _argIndex = 3;
    if (plate == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, plate);
    }
    _argIndex = 4;
    _stmt.bindLong(_argIndex, violation_id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateInsert.release(_stmt);
    }
  }

  @Override
  public RepeatOffender searchRepeatOffender(final String plate, final int violationId,
      final String stateCode) {
    final String _sql = "select * from repeat_offenders where plate=? and state_code=? and violation_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (plate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, plate);
    }
    _argIndex = 2;
    if (stateCode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, stateCode);
    }
    _argIndex = 3;
    _statement.bindLong(_argIndex, violationId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRepeatOffenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "repeat_offender_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfViolation = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final int _cursorIndexOfCount = CursorUtil.getColumnIndexOrThrow(_cursor, "count");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfCreatTime = CursorUtil.getColumnIndexOrThrow(_cursor, "created_date");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final RepeatOffender _result;
      if(_cursor.moveToFirst()) {
        _result = new RepeatOffender();
        final int _tmpRepeatOffenderId;
        _tmpRepeatOffenderId = _cursor.getInt(_cursorIndexOfRepeatOffenderId);
        _result.setRepeatOffenderId(_tmpRepeatOffenderId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _result.setPlate(_tmpPlate);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _result.setViolation(_tmpViolation);
        final int _tmpCount;
        _tmpCount = _cursor.getInt(_cursorIndexOfCount);
        _result.setCount(_tmpCount);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _result.setViolationId(_tmpViolationId);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _result.setStateCode(_tmpStateCode);
        final String _tmpCreatTime;
        if (_cursor.isNull(_cursorIndexOfCreatTime)) {
          _tmpCreatTime = null;
        } else {
          _tmpCreatTime = _cursor.getString(_cursorIndexOfCreatTime);
        }
        _result.setCreatTime(_tmpCreatTime);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _result.setSyncStatus(_tmpSyncStatus);
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
  public List<RepeatOffender> getRepeatOffender() {
    final String _sql = "select * from repeat_offenders WHERE sync_status= 'P'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRepeatOffenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "repeat_offender_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfViolation = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final int _cursorIndexOfCount = CursorUtil.getColumnIndexOrThrow(_cursor, "count");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfCreatTime = CursorUtil.getColumnIndexOrThrow(_cursor, "created_date");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final List<RepeatOffender> _result = new ArrayList<RepeatOffender>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final RepeatOffender _item;
        _item = new RepeatOffender();
        final int _tmpRepeatOffenderId;
        _tmpRepeatOffenderId = _cursor.getInt(_cursorIndexOfRepeatOffenderId);
        _item.setRepeatOffenderId(_tmpRepeatOffenderId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item.setViolation(_tmpViolation);
        final int _tmpCount;
        _tmpCount = _cursor.getInt(_cursorIndexOfCount);
        _item.setCount(_tmpCount);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item.setStateCode(_tmpStateCode);
        final String _tmpCreatTime;
        if (_cursor.isNull(_cursorIndexOfCreatTime)) {
          _tmpCreatTime = null;
        } else {
          _tmpCreatTime = _cursor.getString(_cursorIndexOfCreatTime);
        }
        _item.setCreatTime(_tmpCreatTime);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _item.setSyncStatus(_tmpSyncStatus);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<RepeatOffender> checkIsDataAlreadyInDBorNot(final int custId, final String state_code,
      final String plate, final int violation_id) {
    final String _sql = "Select * from repeat_offenders where custid=? AND state_code=? AND plate=? AND violation_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, custId);
    _argIndex = 2;
    if (state_code == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, state_code);
    }
    _argIndex = 3;
    if (plate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, plate);
    }
    _argIndex = 4;
    _statement.bindLong(_argIndex, violation_id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRepeatOffenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "repeat_offender_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfViolation = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final int _cursorIndexOfCount = CursorUtil.getColumnIndexOrThrow(_cursor, "count");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfCreatTime = CursorUtil.getColumnIndexOrThrow(_cursor, "created_date");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final List<RepeatOffender> _result = new ArrayList<RepeatOffender>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final RepeatOffender _item;
        _item = new RepeatOffender();
        final int _tmpRepeatOffenderId;
        _tmpRepeatOffenderId = _cursor.getInt(_cursorIndexOfRepeatOffenderId);
        _item.setRepeatOffenderId(_tmpRepeatOffenderId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item.setViolation(_tmpViolation);
        final int _tmpCount;
        _tmpCount = _cursor.getInt(_cursorIndexOfCount);
        _item.setCount(_tmpCount);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item.setStateCode(_tmpStateCode);
        final String _tmpCreatTime;
        if (_cursor.isNull(_cursorIndexOfCreatTime)) {
          _tmpCreatTime = null;
        } else {
          _tmpCreatTime = _cursor.getString(_cursorIndexOfCreatTime);
        }
        _item.setCreatTime(_tmpCreatTime);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _item.setSyncStatus(_tmpSyncStatus);
        _result.add(_item);
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
