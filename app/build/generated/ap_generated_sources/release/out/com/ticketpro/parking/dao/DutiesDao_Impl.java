package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.Duty;
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
public final class DutiesDao_Impl implements DutiesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Duty> __insertionAdapterOfDuty;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public DutiesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDuty = new EntityInsertionAdapter<Duty>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `duties` (`duty_id`,`custid`,`duty`,`code`,`order_number`,`allow_ticket`,`location_groups`,`comment_groups`,`violation_groups`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Duty value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getCustId());
        if (value.getTitle() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTitle());
        }
        if (value.getCode() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getCode());
        }
        stmt.bindLong(5, value.getOrderNumber());
        if (value.getAllowTicket() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getAllowTicket());
        }
        if (value.getLocationGroups() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getLocationGroups());
        }
        if (value.getCommentGroups() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getCommentGroups());
        }
        if (value.getViolationGroups() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getViolationGroups());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from duties";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from duties where duty_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertDuty(final Duty... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDuty.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertDuty(final Duty data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDuty.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertDutyList(final List<Duty> DutysList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDuty.insert(DutysList);
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
  public List<Duty> getDuties() {
    final String _sql = "select * from duties order by order_number,duty";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "duty_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "duty");
      final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final int _cursorIndexOfAllowTicket = CursorUtil.getColumnIndexOrThrow(_cursor, "allow_ticket");
      final int _cursorIndexOfLocationGroups = CursorUtil.getColumnIndexOrThrow(_cursor, "location_groups");
      final int _cursorIndexOfCommentGroups = CursorUtil.getColumnIndexOrThrow(_cursor, "comment_groups");
      final int _cursorIndexOfViolationGroups = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_groups");
      final List<Duty> _result = new ArrayList<Duty>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Duty _item;
        _item = new Duty();
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
        final String _tmpCode;
        if (_cursor.isNull(_cursorIndexOfCode)) {
          _tmpCode = null;
        } else {
          _tmpCode = _cursor.getString(_cursorIndexOfCode);
        }
        _item.setCode(_tmpCode);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _item.setOrderNumber(_tmpOrderNumber);
        final String _tmpAllowTicket;
        if (_cursor.isNull(_cursorIndexOfAllowTicket)) {
          _tmpAllowTicket = null;
        } else {
          _tmpAllowTicket = _cursor.getString(_cursorIndexOfAllowTicket);
        }
        _item.setAllowTicket(_tmpAllowTicket);
        final String _tmpLocationGroups;
        if (_cursor.isNull(_cursorIndexOfLocationGroups)) {
          _tmpLocationGroups = null;
        } else {
          _tmpLocationGroups = _cursor.getString(_cursorIndexOfLocationGroups);
        }
        _item.setLocationGroups(_tmpLocationGroups);
        final String _tmpCommentGroups;
        if (_cursor.isNull(_cursorIndexOfCommentGroups)) {
          _tmpCommentGroups = null;
        } else {
          _tmpCommentGroups = _cursor.getString(_cursorIndexOfCommentGroups);
        }
        _item.setCommentGroups(_tmpCommentGroups);
        final String _tmpViolationGroups;
        if (_cursor.isNull(_cursorIndexOfViolationGroups)) {
          _tmpViolationGroups = null;
        } else {
          _tmpViolationGroups = _cursor.getString(_cursorIndexOfViolationGroups);
        }
        _item.setViolationGroups(_tmpViolationGroups);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Duty getDutyById(final int dutyId) {
    final String _sql = "select * from duties where duty_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dutyId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "duty_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "duty");
      final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final int _cursorIndexOfAllowTicket = CursorUtil.getColumnIndexOrThrow(_cursor, "allow_ticket");
      final int _cursorIndexOfLocationGroups = CursorUtil.getColumnIndexOrThrow(_cursor, "location_groups");
      final int _cursorIndexOfCommentGroups = CursorUtil.getColumnIndexOrThrow(_cursor, "comment_groups");
      final int _cursorIndexOfViolationGroups = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_groups");
      final Duty _result;
      if(_cursor.moveToFirst()) {
        _result = new Duty();
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
        final String _tmpCode;
        if (_cursor.isNull(_cursorIndexOfCode)) {
          _tmpCode = null;
        } else {
          _tmpCode = _cursor.getString(_cursorIndexOfCode);
        }
        _result.setCode(_tmpCode);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _result.setOrderNumber(_tmpOrderNumber);
        final String _tmpAllowTicket;
        if (_cursor.isNull(_cursorIndexOfAllowTicket)) {
          _tmpAllowTicket = null;
        } else {
          _tmpAllowTicket = _cursor.getString(_cursorIndexOfAllowTicket);
        }
        _result.setAllowTicket(_tmpAllowTicket);
        final String _tmpLocationGroups;
        if (_cursor.isNull(_cursorIndexOfLocationGroups)) {
          _tmpLocationGroups = null;
        } else {
          _tmpLocationGroups = _cursor.getString(_cursorIndexOfLocationGroups);
        }
        _result.setLocationGroups(_tmpLocationGroups);
        final String _tmpCommentGroups;
        if (_cursor.isNull(_cursorIndexOfCommentGroups)) {
          _tmpCommentGroups = null;
        } else {
          _tmpCommentGroups = _cursor.getString(_cursorIndexOfCommentGroups);
        }
        _result.setCommentGroups(_tmpCommentGroups);
        final String _tmpViolationGroups;
        if (_cursor.isNull(_cursorIndexOfViolationGroups)) {
          _tmpViolationGroups = null;
        } else {
          _tmpViolationGroups = _cursor.getString(_cursorIndexOfViolationGroups);
        }
        _result.setViolationGroups(_tmpViolationGroups);
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
