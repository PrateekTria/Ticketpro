package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.StreetSuffix;
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
public final class StreetSuffixesDao_Impl implements StreetSuffixesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StreetSuffix> __insertionAdapterOfStreetSuffix;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public StreetSuffixesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStreetSuffix = new EntityInsertionAdapter<StreetSuffix>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `street_suffixes` (`suffix_id`,`custid`,`street_suffix`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, StreetSuffix value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getCustId());
        if (value.getTitle() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTitle());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from street_suffixes";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from street_suffixes where suffix_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertStreetSuffix(final StreetSuffix... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfStreetSuffix.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertStreetSuffix(final StreetSuffix data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfStreetSuffix.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertStreetSuffixList(final List<StreetSuffix> StreetSuffixsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfStreetSuffix.insert(StreetSuffixsList);
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
  public List<StreetSuffix> getStreetSuffixes() {
    final String _sql = "select * from street_suffixes";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "suffix_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "street_suffix");
      final List<StreetSuffix> _result = new ArrayList<StreetSuffix>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final StreetSuffix _item;
        _item = new StreetSuffix();
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
