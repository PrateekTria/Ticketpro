package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.CustomerModule;
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
public final class CustomerModulesDao_Impl implements CustomerModulesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CustomerModule> __insertionAdapterOfCustomerModule;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public CustomerModulesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCustomerModule = new EntityInsertionAdapter<CustomerModule>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `customer_modules` (`customer_module_id`,`module_id`,`custid`,`userid`,`is_active`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CustomerModule value) {
        stmt.bindLong(1, value.getCustomerModuleId());
        stmt.bindLong(2, value.getModuleId());
        stmt.bindLong(3, value.getCustId());
        if (value.getUserId() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getUserId());
        }
        if (value.getIsActive() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getIsActive());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from customer_modules";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from customer_modules where customer_module_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertCustomerModule(final CustomerModule... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCustomerModule.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertCustomerModule(final CustomerModule data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCustomerModule.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertCustomerModuleList(final List<CustomerModule> CustomerModulesList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCustomerModule.insert(CustomerModulesList);
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
  public List<CustomerModule> getModules() {
    final String _sql = "select * from customer_modules";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCustomerModuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_module_id");
      final int _cursorIndexOfModuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "module_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final List<CustomerModule> _result = new ArrayList<CustomerModule>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final CustomerModule _item;
        _item = new CustomerModule();
        final int _tmpCustomerModuleId;
        _tmpCustomerModuleId = _cursor.getInt(_cursorIndexOfCustomerModuleId);
        _item.setCustomerModuleId(_tmpCustomerModuleId);
        final int _tmpModuleId;
        _tmpModuleId = _cursor.getInt(_cursorIndexOfModuleId);
        _item.setModuleId(_tmpModuleId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpUserId;
        if (_cursor.isNull(_cursorIndexOfUserId)) {
          _tmpUserId = null;
        } else {
          _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        }
        _item.setUserId(_tmpUserId);
        final String _tmpIsActive;
        if (_cursor.isNull(_cursorIndexOfIsActive)) {
          _tmpIsActive = null;
        } else {
          _tmpIsActive = _cursor.getString(_cursorIndexOfIsActive);
        }
        _item.setIsActive(_tmpIsActive);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public CustomerModule getModuleById(final int moduleId) {
    final String _sql = "select * from customer_modules where module_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, moduleId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCustomerModuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_module_id");
      final int _cursorIndexOfModuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "module_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final CustomerModule _result;
      if(_cursor.moveToFirst()) {
        _result = new CustomerModule();
        final int _tmpCustomerModuleId;
        _tmpCustomerModuleId = _cursor.getInt(_cursorIndexOfCustomerModuleId);
        _result.setCustomerModuleId(_tmpCustomerModuleId);
        final int _tmpModuleId;
        _tmpModuleId = _cursor.getInt(_cursorIndexOfModuleId);
        _result.setModuleId(_tmpModuleId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpUserId;
        if (_cursor.isNull(_cursorIndexOfUserId)) {
          _tmpUserId = null;
        } else {
          _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        }
        _result.setUserId(_tmpUserId);
        final String _tmpIsActive;
        if (_cursor.isNull(_cursorIndexOfIsActive)) {
          _tmpIsActive = null;
        } else {
          _tmpIsActive = _cursor.getString(_cursorIndexOfIsActive);
        }
        _result.setIsActive(_tmpIsActive);
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
