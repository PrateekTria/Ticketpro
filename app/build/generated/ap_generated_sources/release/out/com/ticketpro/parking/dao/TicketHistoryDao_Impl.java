package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.TicketHistory;
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
public final class TicketHistoryDao_Impl implements TicketHistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TicketHistory> __insertionAdapterOfTicketHistory;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAllTicketHistory;

  private final SharedSQLiteStatement __preparedStmtOfRemoveTicketHistoryById;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public TicketHistoryDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTicketHistory = new EntityInsertionAdapter<TicketHistory>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `ticket_history` (`ticket_id`,`ticket_date`,`plate`,`vin`,`expiration`,`state_code`,`make_code`,`body_code`,`color_code`,`permit`,`meter`,`is_void`,`is_chalked`,`is_warn`,`is_driveaway`,`violation_code`,`violation`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TicketHistory value) {
        stmt.bindLong(1, value.getTicketId());
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getTicketDate());
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, _tmp);
        }
        if (value.getPlate() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPlate());
        }
        if (value.getVin() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getVin());
        }
        if (value.getExpiration() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getExpiration());
        }
        if (value.getStateCode() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getStateCode());
        }
        if (value.getMakeCode() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getMakeCode());
        }
        if (value.getBodyCode() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getBodyCode());
        }
        if (value.getColorCode() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getColorCode());
        }
        if (value.getPermit() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getPermit());
        }
        if (value.getMeter() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getMeter());
        }
        if (value.getIsVoid() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getIsVoid());
        }
        if (value.getIsChalked() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getIsChalked());
        }
        if (value.getIsWarn() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getIsWarn());
        }
        if (value.getIsDriveAway() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getIsDriveAway());
        }
        if (value.getViolationCode() == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindString(16, value.getViolationCode());
        }
        if (value.getViolation() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindString(17, value.getViolation());
        }
      }
    };
    this.__preparedStmtOfRemoveAllTicketHistory = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from ticket_history";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveTicketHistoryById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from ticket_history where ticket_id=?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from ticket_history where ticket_id=?";
        return _query;
      }
    };
  }

  @Override
  public Completable insertTicketHistory(final TicketHistory... data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTicketHistory.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable insertTicketHistory(final TicketHistory data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTicketHistory.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable insertTicketHistoryList(final List<TicketHistory> TicketHistorysList) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTicketHistory.insert(TicketHistorysList);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void removeAllTicketHistory() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveAllTicketHistory.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveAllTicketHistory.release(_stmt);
    }
  }

  @Override
  public void removeTicketHistoryById(final long ticketId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveTicketHistoryById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, ticketId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveTicketHistoryById.release(_stmt);
    }
  }

  @Override
  public void removeAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveAllTicketHistory.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveAllTicketHistory.release(_stmt);
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
  public Date getMaxTicketDate() {
    final String _sql = "select max(ticket_date) as max_ticket_date from ticket_history";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final Date _result;
      if(_cursor.moveToFirst()) {
        final Long _tmp;
        if (_cursor.isNull(0)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(0);
        }
        _result = Converters.fromTimestamp(_tmp);
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
  public Date getMinTicketDate() {
    final String _sql = "select min(ticket_date) as min_ticket_date from ticket_history";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final Date _result;
      if(_cursor.moveToFirst()) {
        final Long _tmp;
        if (_cursor.isNull(0)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(0);
        }
        _result = Converters.fromTimestamp(_tmp);
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
  public List<TicketHistory> getTickets() {
    final String _sql = "select * from ticket_history order by ticket_date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTicketId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_id");
      final int _cursorIndexOfTicketDate = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "expiration");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfBodyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "body_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfIsVoid = CursorUtil.getColumnIndexOrThrow(_cursor, "is_void");
      final int _cursorIndexOfIsChalked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_chalked");
      final int _cursorIndexOfIsWarn = CursorUtil.getColumnIndexOrThrow(_cursor, "is_warn");
      final int _cursorIndexOfIsDriveAway = CursorUtil.getColumnIndexOrThrow(_cursor, "is_driveaway");
      final int _cursorIndexOfViolationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_code");
      final int _cursorIndexOfViolation = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final List<TicketHistory> _result = new ArrayList<TicketHistory>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TicketHistory _item;
        _item = new TicketHistory();
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final Date _tmpTicketDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp);
        _item.setTicketDate(_tmpTicketDate);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final String _tmpExpiration;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmpExpiration = null;
        } else {
          _tmpExpiration = _cursor.getString(_cursorIndexOfExpiration);
        }
        _item.setExpiration(_tmpExpiration);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item.setStateCode(_tmpStateCode);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _item.setBodyCode(_tmpBodyCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final String _tmpIsVoid;
        if (_cursor.isNull(_cursorIndexOfIsVoid)) {
          _tmpIsVoid = null;
        } else {
          _tmpIsVoid = _cursor.getString(_cursorIndexOfIsVoid);
        }
        _item.setIsVoid(_tmpIsVoid);
        final String _tmpIsChalked;
        if (_cursor.isNull(_cursorIndexOfIsChalked)) {
          _tmpIsChalked = null;
        } else {
          _tmpIsChalked = _cursor.getString(_cursorIndexOfIsChalked);
        }
        _item.setIsChalked(_tmpIsChalked);
        final String _tmpIsWarn;
        if (_cursor.isNull(_cursorIndexOfIsWarn)) {
          _tmpIsWarn = null;
        } else {
          _tmpIsWarn = _cursor.getString(_cursorIndexOfIsWarn);
        }
        _item.setIsWarn(_tmpIsWarn);
        final String _tmpIsDriveAway;
        if (_cursor.isNull(_cursorIndexOfIsDriveAway)) {
          _tmpIsDriveAway = null;
        } else {
          _tmpIsDriveAway = _cursor.getString(_cursorIndexOfIsDriveAway);
        }
        _item.setIsDriveAway(_tmpIsDriveAway);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item.setViolation(_tmpViolation);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<TicketHistory> searchAllPreviousTicketsByPlate(final String state,
      final String plate) {
    final String _sql = "select * from ticket_history where plate=? and state_code=? order by ticket_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (plate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, plate);
    }
    _argIndex = 2;
    if (state == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, state);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTicketId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_id");
      final int _cursorIndexOfTicketDate = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "expiration");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfBodyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "body_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfIsVoid = CursorUtil.getColumnIndexOrThrow(_cursor, "is_void");
      final int _cursorIndexOfIsChalked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_chalked");
      final int _cursorIndexOfIsWarn = CursorUtil.getColumnIndexOrThrow(_cursor, "is_warn");
      final int _cursorIndexOfIsDriveAway = CursorUtil.getColumnIndexOrThrow(_cursor, "is_driveaway");
      final int _cursorIndexOfViolationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_code");
      final int _cursorIndexOfViolation = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final List<TicketHistory> _result = new ArrayList<TicketHistory>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TicketHistory _item;
        _item = new TicketHistory();
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final Date _tmpTicketDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp);
        _item.setTicketDate(_tmpTicketDate);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final String _tmpExpiration;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmpExpiration = null;
        } else {
          _tmpExpiration = _cursor.getString(_cursorIndexOfExpiration);
        }
        _item.setExpiration(_tmpExpiration);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item.setStateCode(_tmpStateCode);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _item.setBodyCode(_tmpBodyCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final String _tmpIsVoid;
        if (_cursor.isNull(_cursorIndexOfIsVoid)) {
          _tmpIsVoid = null;
        } else {
          _tmpIsVoid = _cursor.getString(_cursorIndexOfIsVoid);
        }
        _item.setIsVoid(_tmpIsVoid);
        final String _tmpIsChalked;
        if (_cursor.isNull(_cursorIndexOfIsChalked)) {
          _tmpIsChalked = null;
        } else {
          _tmpIsChalked = _cursor.getString(_cursorIndexOfIsChalked);
        }
        _item.setIsChalked(_tmpIsChalked);
        final String _tmpIsWarn;
        if (_cursor.isNull(_cursorIndexOfIsWarn)) {
          _tmpIsWarn = null;
        } else {
          _tmpIsWarn = _cursor.getString(_cursorIndexOfIsWarn);
        }
        _item.setIsWarn(_tmpIsWarn);
        final String _tmpIsDriveAway;
        if (_cursor.isNull(_cursorIndexOfIsDriveAway)) {
          _tmpIsDriveAway = null;
        } else {
          _tmpIsDriveAway = _cursor.getString(_cursorIndexOfIsDriveAway);
        }
        _item.setIsDriveAway(_tmpIsDriveAway);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item.setViolation(_tmpViolation);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public TicketHistory searchPreviousTicketByPlate(final String state, final String plate) {
    final String _sql = "select * from ticket_history where plate=? and state_code=? order by ticket_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (plate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, plate);
    }
    _argIndex = 2;
    if (state == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, state);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTicketId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_id");
      final int _cursorIndexOfTicketDate = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "expiration");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfBodyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "body_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfIsVoid = CursorUtil.getColumnIndexOrThrow(_cursor, "is_void");
      final int _cursorIndexOfIsChalked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_chalked");
      final int _cursorIndexOfIsWarn = CursorUtil.getColumnIndexOrThrow(_cursor, "is_warn");
      final int _cursorIndexOfIsDriveAway = CursorUtil.getColumnIndexOrThrow(_cursor, "is_driveaway");
      final int _cursorIndexOfViolationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_code");
      final int _cursorIndexOfViolation = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final TicketHistory _result;
      if(_cursor.moveToFirst()) {
        _result = new TicketHistory();
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _result.setTicketId(_tmpTicketId);
        final Date _tmpTicketDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp);
        _result.setTicketDate(_tmpTicketDate);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _result.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _result.setVin(_tmpVin);
        final String _tmpExpiration;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmpExpiration = null;
        } else {
          _tmpExpiration = _cursor.getString(_cursorIndexOfExpiration);
        }
        _result.setExpiration(_tmpExpiration);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _result.setStateCode(_tmpStateCode);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _result.setMakeCode(_tmpMakeCode);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _result.setBodyCode(_tmpBodyCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _result.setColorCode(_tmpColorCode);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _result.setPermit(_tmpPermit);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _result.setMeter(_tmpMeter);
        final String _tmpIsVoid;
        if (_cursor.isNull(_cursorIndexOfIsVoid)) {
          _tmpIsVoid = null;
        } else {
          _tmpIsVoid = _cursor.getString(_cursorIndexOfIsVoid);
        }
        _result.setIsVoid(_tmpIsVoid);
        final String _tmpIsChalked;
        if (_cursor.isNull(_cursorIndexOfIsChalked)) {
          _tmpIsChalked = null;
        } else {
          _tmpIsChalked = _cursor.getString(_cursorIndexOfIsChalked);
        }
        _result.setIsChalked(_tmpIsChalked);
        final String _tmpIsWarn;
        if (_cursor.isNull(_cursorIndexOfIsWarn)) {
          _tmpIsWarn = null;
        } else {
          _tmpIsWarn = _cursor.getString(_cursorIndexOfIsWarn);
        }
        _result.setIsWarn(_tmpIsWarn);
        final String _tmpIsDriveAway;
        if (_cursor.isNull(_cursorIndexOfIsDriveAway)) {
          _tmpIsDriveAway = null;
        } else {
          _tmpIsDriveAway = _cursor.getString(_cursorIndexOfIsDriveAway);
        }
        _result.setIsDriveAway(_tmpIsDriveAway);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _result.setViolationCode(_tmpViolationCode);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _result.setViolation(_tmpViolation);
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
  public TicketHistory searchPreviousTicketByVIN(final String state, final String vin) {
    final String _sql = "select * from ticket_history where vin=? and state_code=? order by ticket_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (vin == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, vin);
    }
    _argIndex = 2;
    if (state == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, state);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTicketId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_id");
      final int _cursorIndexOfTicketDate = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "expiration");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfBodyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "body_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfIsVoid = CursorUtil.getColumnIndexOrThrow(_cursor, "is_void");
      final int _cursorIndexOfIsChalked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_chalked");
      final int _cursorIndexOfIsWarn = CursorUtil.getColumnIndexOrThrow(_cursor, "is_warn");
      final int _cursorIndexOfIsDriveAway = CursorUtil.getColumnIndexOrThrow(_cursor, "is_driveaway");
      final int _cursorIndexOfViolationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_code");
      final int _cursorIndexOfViolation = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final TicketHistory _result;
      if(_cursor.moveToFirst()) {
        _result = new TicketHistory();
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _result.setTicketId(_tmpTicketId);
        final Date _tmpTicketDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp);
        _result.setTicketDate(_tmpTicketDate);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _result.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _result.setVin(_tmpVin);
        final String _tmpExpiration;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmpExpiration = null;
        } else {
          _tmpExpiration = _cursor.getString(_cursorIndexOfExpiration);
        }
        _result.setExpiration(_tmpExpiration);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _result.setStateCode(_tmpStateCode);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _result.setMakeCode(_tmpMakeCode);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _result.setBodyCode(_tmpBodyCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _result.setColorCode(_tmpColorCode);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _result.setPermit(_tmpPermit);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _result.setMeter(_tmpMeter);
        final String _tmpIsVoid;
        if (_cursor.isNull(_cursorIndexOfIsVoid)) {
          _tmpIsVoid = null;
        } else {
          _tmpIsVoid = _cursor.getString(_cursorIndexOfIsVoid);
        }
        _result.setIsVoid(_tmpIsVoid);
        final String _tmpIsChalked;
        if (_cursor.isNull(_cursorIndexOfIsChalked)) {
          _tmpIsChalked = null;
        } else {
          _tmpIsChalked = _cursor.getString(_cursorIndexOfIsChalked);
        }
        _result.setIsChalked(_tmpIsChalked);
        final String _tmpIsWarn;
        if (_cursor.isNull(_cursorIndexOfIsWarn)) {
          _tmpIsWarn = null;
        } else {
          _tmpIsWarn = _cursor.getString(_cursorIndexOfIsWarn);
        }
        _result.setIsWarn(_tmpIsWarn);
        final String _tmpIsDriveAway;
        if (_cursor.isNull(_cursorIndexOfIsDriveAway)) {
          _tmpIsDriveAway = null;
        } else {
          _tmpIsDriveAway = _cursor.getString(_cursorIndexOfIsDriveAway);
        }
        _result.setIsDriveAway(_tmpIsDriveAway);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _result.setViolationCode(_tmpViolationCode);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _result.setViolation(_tmpViolation);
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
  public TicketHistory searchPreviousTicketByMeter(final String meter) {
    final String _sql = "select * from ticket_history where meter=? order by ticket_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (meter == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, meter);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTicketId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_id");
      final int _cursorIndexOfTicketDate = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "expiration");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfBodyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "body_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfIsVoid = CursorUtil.getColumnIndexOrThrow(_cursor, "is_void");
      final int _cursorIndexOfIsChalked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_chalked");
      final int _cursorIndexOfIsWarn = CursorUtil.getColumnIndexOrThrow(_cursor, "is_warn");
      final int _cursorIndexOfIsDriveAway = CursorUtil.getColumnIndexOrThrow(_cursor, "is_driveaway");
      final int _cursorIndexOfViolationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_code");
      final int _cursorIndexOfViolation = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final TicketHistory _result;
      if(_cursor.moveToFirst()) {
        _result = new TicketHistory();
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _result.setTicketId(_tmpTicketId);
        final Date _tmpTicketDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp);
        _result.setTicketDate(_tmpTicketDate);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _result.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _result.setVin(_tmpVin);
        final String _tmpExpiration;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmpExpiration = null;
        } else {
          _tmpExpiration = _cursor.getString(_cursorIndexOfExpiration);
        }
        _result.setExpiration(_tmpExpiration);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _result.setStateCode(_tmpStateCode);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _result.setMakeCode(_tmpMakeCode);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _result.setBodyCode(_tmpBodyCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _result.setColorCode(_tmpColorCode);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _result.setPermit(_tmpPermit);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _result.setMeter(_tmpMeter);
        final String _tmpIsVoid;
        if (_cursor.isNull(_cursorIndexOfIsVoid)) {
          _tmpIsVoid = null;
        } else {
          _tmpIsVoid = _cursor.getString(_cursorIndexOfIsVoid);
        }
        _result.setIsVoid(_tmpIsVoid);
        final String _tmpIsChalked;
        if (_cursor.isNull(_cursorIndexOfIsChalked)) {
          _tmpIsChalked = null;
        } else {
          _tmpIsChalked = _cursor.getString(_cursorIndexOfIsChalked);
        }
        _result.setIsChalked(_tmpIsChalked);
        final String _tmpIsWarn;
        if (_cursor.isNull(_cursorIndexOfIsWarn)) {
          _tmpIsWarn = null;
        } else {
          _tmpIsWarn = _cursor.getString(_cursorIndexOfIsWarn);
        }
        _result.setIsWarn(_tmpIsWarn);
        final String _tmpIsDriveAway;
        if (_cursor.isNull(_cursorIndexOfIsDriveAway)) {
          _tmpIsDriveAway = null;
        } else {
          _tmpIsDriveAway = _cursor.getString(_cursorIndexOfIsDriveAway);
        }
        _result.setIsDriveAway(_tmpIsDriveAway);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _result.setViolationCode(_tmpViolationCode);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _result.setViolation(_tmpViolation);
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
