package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.CallInList;
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
public final class CallInListDao_Impl implements CallInListDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CallInList> __insertionAdapterOfCallInList;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveByid;

  public CallInListDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCallInList = new EntityInsertionAdapter<CallInList>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `call_in_list` (`call_in_id`,`custid`,`action_item`,`action_code`,`order_number`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CallInList value) {
        stmt.bindLong(1, value.getCallInId());
        stmt.bindLong(2, value.getCustId());
        if (value.getActionItem() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getActionItem());
        }
        if (value.getActionCode() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getActionCode());
        }
        stmt.bindLong(5, value.getOrderNumber());
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from call_in_list";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveByid = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from call_in_list where call_in_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertCallInList(final CallInList... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCallInList.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertCallInList(final CallInList data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCallInList.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertCallInList(final List<CallInList> callInListList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCallInList.insert(callInListList);
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
  public void removeByid(final int id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveByid.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveByid.release(_stmt);
    }
  }

  @Override
  public List<CallInList> getCallInList() {
    final String _sql = "select * from call_in_list order by order_number,action_item";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCallInId = CursorUtil.getColumnIndexOrThrow(_cursor, "call_in_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfActionItem = CursorUtil.getColumnIndexOrThrow(_cursor, "action_item");
      final int _cursorIndexOfActionCode = CursorUtil.getColumnIndexOrThrow(_cursor, "action_code");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final List<CallInList> _result = new ArrayList<CallInList>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final CallInList _item;
        _item = new CallInList();
        final int _tmpCallInId;
        _tmpCallInId = _cursor.getInt(_cursorIndexOfCallInId);
        _item.setCallInId(_tmpCallInId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpActionItem;
        if (_cursor.isNull(_cursorIndexOfActionItem)) {
          _tmpActionItem = null;
        } else {
          _tmpActionItem = _cursor.getString(_cursorIndexOfActionItem);
        }
        _item.setActionItem(_tmpActionItem);
        final String _tmpActionCode;
        if (_cursor.isNull(_cursorIndexOfActionCode)) {
          _tmpActionCode = null;
        } else {
          _tmpActionCode = _cursor.getString(_cursorIndexOfActionCode);
        }
        _item.setActionCode(_tmpActionCode);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _item.setOrderNumber(_tmpOrderNumber);
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
