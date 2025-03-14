package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.ViolationGroupViolation;
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
public final class ViolationGroupViolationsDao_Impl implements ViolationGroupViolationsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ViolationGroupViolation> __insertionAdapterOfViolationGroupViolation;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public ViolationGroupViolationsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfViolationGroupViolation = new EntityInsertionAdapter<ViolationGroupViolation>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `violation_group_violations` (`violation_group_id`,`group_id`,`violation_id`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ViolationGroupViolation value) {
        stmt.bindLong(1, value.getViolationGroupId());
        stmt.bindLong(2, value.getGroupId());
        stmt.bindLong(3, value.getViolationId());
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from violation_group_violations";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from violation_group_violations where violation_group_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertViolationGroupViolation(final ViolationGroupViolation... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfViolationGroupViolation.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertViolationGroupViolation(final ViolationGroupViolation data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfViolationGroupViolation.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertViolationGroupViolationList(
      final List<ViolationGroupViolation> ViolationGroupViolationsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfViolationGroupViolation.insert(ViolationGroupViolationsList);
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
  public List<ViolationGroupViolation> getViolationGroupViolations() {
    final String _sql = "select * from violation_group_violations";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfViolationGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_group_id");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "group_id");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final List<ViolationGroupViolation> _result = new ArrayList<ViolationGroupViolation>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ViolationGroupViolation _item;
        _item = new ViolationGroupViolation();
        final int _tmpViolationGroupId;
        _tmpViolationGroupId = _cursor.getInt(_cursorIndexOfViolationGroupId);
        _item.setViolationGroupId(_tmpViolationGroupId);
        final int _tmpGroupId;
        _tmpGroupId = _cursor.getInt(_cursorIndexOfGroupId);
        _item.setGroupId(_tmpGroupId);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
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
