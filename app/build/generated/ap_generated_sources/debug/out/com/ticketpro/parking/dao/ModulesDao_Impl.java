package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.Module;
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
public final class ModulesDao_Impl implements ModulesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Module> __insertionAdapterOfModule;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public ModulesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfModule = new EntityInsertionAdapter<Module>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `modules` (`module_id`,`module_name`,`module_desc`,`is_active`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Module value) {
        stmt.bindLong(1, value.getModuleId());
        if (value.getModuleName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getModuleName());
        }
        if (value.getModuleDesc() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getModuleDesc());
        }
        if (value.getIsActive() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getIsActive());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from modules";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from modules where module_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertModule(final Module... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfModule.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertModule(final Module data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfModule.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertModuleList(final List<Module> ModulesList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfModule.insert(ModulesList);
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
  public List<Module> getModules() {
    final String _sql = "select * from modules";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfModuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "module_id");
      final int _cursorIndexOfModuleName = CursorUtil.getColumnIndexOrThrow(_cursor, "module_name");
      final int _cursorIndexOfModuleDesc = CursorUtil.getColumnIndexOrThrow(_cursor, "module_desc");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final List<Module> _result = new ArrayList<Module>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Module _item;
        _item = new Module();
        final int _tmpModuleId;
        _tmpModuleId = _cursor.getInt(_cursorIndexOfModuleId);
        _item.setModuleId(_tmpModuleId);
        final String _tmpModuleName;
        if (_cursor.isNull(_cursorIndexOfModuleName)) {
          _tmpModuleName = null;
        } else {
          _tmpModuleName = _cursor.getString(_cursorIndexOfModuleName);
        }
        _item.setModuleName(_tmpModuleName);
        final String _tmpModuleDesc;
        if (_cursor.isNull(_cursorIndexOfModuleDesc)) {
          _tmpModuleDesc = null;
        } else {
          _tmpModuleDesc = _cursor.getString(_cursorIndexOfModuleDesc);
        }
        _item.setModuleDesc(_tmpModuleDesc);
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
  public Module getModuleByName(final String moduleName) {
    final String _sql = "select * from modules where UPPER(module_name)=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (moduleName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, moduleName);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfModuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "module_id");
      final int _cursorIndexOfModuleName = CursorUtil.getColumnIndexOrThrow(_cursor, "module_name");
      final int _cursorIndexOfModuleDesc = CursorUtil.getColumnIndexOrThrow(_cursor, "module_desc");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final Module _result;
      if(_cursor.moveToFirst()) {
        _result = new Module();
        final int _tmpModuleId;
        _tmpModuleId = _cursor.getInt(_cursorIndexOfModuleId);
        _result.setModuleId(_tmpModuleId);
        final String _tmpModuleName;
        if (_cursor.isNull(_cursorIndexOfModuleName)) {
          _tmpModuleName = null;
        } else {
          _tmpModuleName = _cursor.getString(_cursorIndexOfModuleName);
        }
        _result.setModuleName(_tmpModuleName);
        final String _tmpModuleDesc;
        if (_cursor.isNull(_cursorIndexOfModuleDesc)) {
          _tmpModuleDesc = null;
        } else {
          _tmpModuleDesc = _cursor.getString(_cursorIndexOfModuleDesc);
        }
        _result.setModuleDesc(_tmpModuleDesc);
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
