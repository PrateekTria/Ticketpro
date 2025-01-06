package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.TicketViolation;
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
public final class TicketViolationsDao_Impl implements TicketViolationsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TicketViolation> __insertionAdapterOfTicketViolation;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public TicketViolationsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTicketViolation = new EntityInsertionAdapter<TicketViolation>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `ticket_violations` (`ticket_violation_id`,`violation_id`,`ticket_id`,`custid`,`violation_code`,`fine`,`citation_number`,`violation`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TicketViolation value) {
        stmt.bindLong(1, value.getTicketViolationId());
        stmt.bindLong(2, value.getViolationId());
        stmt.bindLong(3, value.getTicketId());
        stmt.bindLong(4, value.getCustId());
        if (value.getViolationCode() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getViolationCode());
        }
        stmt.bindDouble(6, value.getFine());
        stmt.bindLong(7, value.getCitationNumber());
        if (value.getViolationDesc() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getViolationDesc());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from ticket_violations";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from ticket_violations where ticket_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertTicketViolation(final TicketViolation... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTicketViolation.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertTicketViolation(final TicketViolation data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTicketViolation.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertTicketViolationList(final List<TicketViolation> TicketViolationsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTicketViolation.insert(TicketViolationsList);
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
  public List<TicketViolation> getTicketViolations(final long ticketId, final long citationNumber) {
    final String _sql = "select * from ticket_violations where ticket_id=? and citation_number=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, ticketId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, citationNumber);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTicketViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_violation_id");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfTicketId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfViolationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_code");
      final int _cursorIndexOfFine = CursorUtil.getColumnIndexOrThrow(_cursor, "fine");
      final int _cursorIndexOfCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "citation_number");
      final int _cursorIndexOfViolationDesc = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final List<TicketViolation> _result = new ArrayList<TicketViolation>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TicketViolation _item;
        _item = new TicketViolation();
        final int _tmpTicketViolationId;
        _tmpTicketViolationId = _cursor.getInt(_cursorIndexOfTicketViolationId);
        _item.setTicketViolationId(_tmpTicketViolationId);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        final double _tmpFine;
        _tmpFine = _cursor.getDouble(_cursorIndexOfFine);
        _item.setFine(_tmpFine);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _item.setCitationNumber(_tmpCitationNumber);
        final String _tmpViolationDesc;
        if (_cursor.isNull(_cursorIndexOfViolationDesc)) {
          _tmpViolationDesc = null;
        } else {
          _tmpViolationDesc = _cursor.getString(_cursorIndexOfViolationDesc);
        }
        _item.setViolationDesc(_tmpViolationDesc);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<TicketViolation> getTicketViolationsByCitation(final long citationNumber) {
    final String _sql = "select * from ticket_violations where citation_number=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, citationNumber);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTicketViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_violation_id");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfTicketId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfViolationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_code");
      final int _cursorIndexOfFine = CursorUtil.getColumnIndexOrThrow(_cursor, "fine");
      final int _cursorIndexOfCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "citation_number");
      final int _cursorIndexOfViolationDesc = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final List<TicketViolation> _result = new ArrayList<TicketViolation>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TicketViolation _item;
        _item = new TicketViolation();
        final int _tmpTicketViolationId;
        _tmpTicketViolationId = _cursor.getInt(_cursorIndexOfTicketViolationId);
        _item.setTicketViolationId(_tmpTicketViolationId);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        final double _tmpFine;
        _tmpFine = _cursor.getDouble(_cursorIndexOfFine);
        _item.setFine(_tmpFine);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _item.setCitationNumber(_tmpCitationNumber);
        final String _tmpViolationDesc;
        if (_cursor.isNull(_cursorIndexOfViolationDesc)) {
          _tmpViolationDesc = null;
        } else {
          _tmpViolationDesc = _cursor.getString(_cursorIndexOfViolationDesc);
        }
        _item.setViolationDesc(_tmpViolationDesc);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<TicketViolation> getTicketViolationWithComments(final long ticketId) {
    final String _sql = "select *,(select violations.violation from violations where violations.violation_id=ticket_violations.violation_id) as violation from ticket_violations where ticket_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, ticketId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTicketViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_violation_id");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfTicketId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfViolationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_code");
      final int _cursorIndexOfFine = CursorUtil.getColumnIndexOrThrow(_cursor, "fine");
      final int _cursorIndexOfCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "citation_number");
      final int _cursorIndexOfViolationDesc = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final int _cursorIndexOfViolationDesc_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final List<TicketViolation> _result = new ArrayList<TicketViolation>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TicketViolation _item;
        _item = new TicketViolation();
        final int _tmpTicketViolationId;
        _tmpTicketViolationId = _cursor.getInt(_cursorIndexOfTicketViolationId);
        _item.setTicketViolationId(_tmpTicketViolationId);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        final double _tmpFine;
        _tmpFine = _cursor.getDouble(_cursorIndexOfFine);
        _item.setFine(_tmpFine);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _item.setCitationNumber(_tmpCitationNumber);
        final String _tmpViolationDesc;
        if (_cursor.isNull(_cursorIndexOfViolationDesc)) {
          _tmpViolationDesc = null;
        } else {
          _tmpViolationDesc = _cursor.getString(_cursorIndexOfViolationDesc);
        }
        _item.setViolationDesc(_tmpViolationDesc);
        final String _tmpViolationDesc_1;
        if (_cursor.isNull(_cursorIndexOfViolationDesc_1)) {
          _tmpViolationDesc_1 = null;
        } else {
          _tmpViolationDesc_1 = _cursor.getString(_cursorIndexOfViolationDesc_1);
        }
        _item.setViolationDesc(_tmpViolationDesc_1);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<TicketViolation> getTicketViolationByCitationWithComments(final long citationNumber) {
    final String _sql = "select *,(select violations.violation from violations where violations.violation_id=ticket_violations.violation_id) as violation from ticket_violations where citation_number=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, citationNumber);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTicketViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_violation_id");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfTicketId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfViolationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_code");
      final int _cursorIndexOfFine = CursorUtil.getColumnIndexOrThrow(_cursor, "fine");
      final int _cursorIndexOfCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "citation_number");
      final int _cursorIndexOfViolationDesc = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final int _cursorIndexOfViolationDesc_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final List<TicketViolation> _result = new ArrayList<TicketViolation>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TicketViolation _item;
        _item = new TicketViolation();
        final int _tmpTicketViolationId;
        _tmpTicketViolationId = _cursor.getInt(_cursorIndexOfTicketViolationId);
        _item.setTicketViolationId(_tmpTicketViolationId);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        final double _tmpFine;
        _tmpFine = _cursor.getDouble(_cursorIndexOfFine);
        _item.setFine(_tmpFine);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _item.setCitationNumber(_tmpCitationNumber);
        final String _tmpViolationDesc;
        if (_cursor.isNull(_cursorIndexOfViolationDesc)) {
          _tmpViolationDesc = null;
        } else {
          _tmpViolationDesc = _cursor.getString(_cursorIndexOfViolationDesc);
        }
        _item.setViolationDesc(_tmpViolationDesc);
        final String _tmpViolationDesc_1;
        if (_cursor.isNull(_cursorIndexOfViolationDesc_1)) {
          _tmpViolationDesc_1 = null;
        } else {
          _tmpViolationDesc_1 = _cursor.getString(_cursorIndexOfViolationDesc_1);
        }
        _item.setViolationDesc(_tmpViolationDesc_1);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public TicketViolation getViolationByPrimaryKey(final String violationId) {
    final String _sql = "select * from ticket_violations where ticket_violation_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (violationId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, violationId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTicketViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_violation_id");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfTicketId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfViolationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_code");
      final int _cursorIndexOfFine = CursorUtil.getColumnIndexOrThrow(_cursor, "fine");
      final int _cursorIndexOfCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "citation_number");
      final int _cursorIndexOfViolationDesc = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final TicketViolation _result;
      if(_cursor.moveToFirst()) {
        _result = new TicketViolation();
        final int _tmpTicketViolationId;
        _tmpTicketViolationId = _cursor.getInt(_cursorIndexOfTicketViolationId);
        _result.setTicketViolationId(_tmpTicketViolationId);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _result.setViolationId(_tmpViolationId);
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _result.setTicketId(_tmpTicketId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _result.setViolationCode(_tmpViolationCode);
        final double _tmpFine;
        _tmpFine = _cursor.getDouble(_cursorIndexOfFine);
        _result.setFine(_tmpFine);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _result.setCitationNumber(_tmpCitationNumber);
        final String _tmpViolationDesc;
        if (_cursor.isNull(_cursorIndexOfViolationDesc)) {
          _tmpViolationDesc = null;
        } else {
          _tmpViolationDesc = _cursor.getString(_cursorIndexOfViolationDesc);
        }
        _result.setViolationDesc(_tmpViolationDesc);
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
  public int getNextPrimaryId() {
    final String _sql = "select max(ticket_violation_id) as max_ticket_violation_id from ticket_violations";
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
