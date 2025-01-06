package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.Placard;
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
public final class PlacardDao_Impl implements PlacardDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Placard> __insertionAdapterOfPlacard;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public PlacardDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlacard = new EntityInsertionAdapter<Placard>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `placard_temp` (`id`,`placard_no`,`placard_details`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Placard value) {
        stmt.bindLong(1, value.getId());
        if (value.getPlacardNo() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getPlacardNo());
        }
        if (value.getPlacardDetails() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPlacardDetails());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from placard_temp";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from placard_temp where placard_no=?";
        return _query;
      }
    };
  }

  @Override
  public Completable insertPlacard(final Placard... data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlacard.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
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
  public void removeById(final String name) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveById.acquire();
    int _argIndex = 1;
    if (name == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, name);
    }
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
  public List<Placard> getPlacard() {
    final String _sql = "select * from placard_temp";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfPlacardNo = CursorUtil.getColumnIndexOrThrow(_cursor, "placard_no");
      final int _cursorIndexOfPlacardDetails = CursorUtil.getColumnIndexOrThrow(_cursor, "placard_details");
      final List<Placard> _result = new ArrayList<Placard>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Placard _item;
        _item = new Placard();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpPlacardNo;
        if (_cursor.isNull(_cursorIndexOfPlacardNo)) {
          _tmpPlacardNo = null;
        } else {
          _tmpPlacardNo = _cursor.getString(_cursorIndexOfPlacardNo);
        }
        _item.setPlacardNo(_tmpPlacardNo);
        final String _tmpPlacardDetails;
        if (_cursor.isNull(_cursorIndexOfPlacardDetails)) {
          _tmpPlacardDetails = null;
        } else {
          _tmpPlacardDetails = _cursor.getString(_cursorIndexOfPlacardDetails);
        }
        _item.setPlacardDetails(_tmpPlacardDetails);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<String> getPlacardNo() {
    final String _sql = "SELECT placard_no FROM placard_temp ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final String _item;
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getString(0);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Placard getPlacardById(final String placardNo) {
    final String _sql = "select * from placard_temp where placard_no=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (placardNo == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, placardNo);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfPlacardNo = CursorUtil.getColumnIndexOrThrow(_cursor, "placard_no");
      final int _cursorIndexOfPlacardDetails = CursorUtil.getColumnIndexOrThrow(_cursor, "placard_details");
      final Placard _result;
      if(_cursor.moveToFirst()) {
        _result = new Placard();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpPlacardNo;
        if (_cursor.isNull(_cursorIndexOfPlacardNo)) {
          _tmpPlacardNo = null;
        } else {
          _tmpPlacardNo = _cursor.getString(_cursorIndexOfPlacardNo);
        }
        _result.setPlacardNo(_tmpPlacardNo);
        final String _tmpPlacardDetails;
        if (_cursor.isNull(_cursorIndexOfPlacardDetails)) {
          _tmpPlacardDetails = null;
        } else {
          _tmpPlacardDetails = _cursor.getString(_cursorIndexOfPlacardDetails);
        }
        _result.setPlacardDetails(_tmpPlacardDetails);
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
