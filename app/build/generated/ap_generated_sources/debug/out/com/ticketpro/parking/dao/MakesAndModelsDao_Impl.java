package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.MakeAndModel;
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
public final class MakesAndModelsDao_Impl implements MakesAndModelsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MakeAndModel> __insertionAdapterOfMakeAndModel;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public MakesAndModelsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMakeAndModel = new EntityInsertionAdapter<MakeAndModel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `makes_and_models` (`make_id`,`custid`,`make`,`make_code`,`model`,`model_code`,`order_number`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MakeAndModel value) {
        stmt.bindLong(1, value.getMakeId());
        stmt.bindLong(2, value.getCustId());
        if (value.getMakeTitle() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getMakeTitle());
        }
        if (value.getMakeCode() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getMakeCode());
        }
        if (value.getModelTitle() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getModelTitle());
        }
        if (value.getModelCode() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getModelCode());
        }
        stmt.bindLong(7, value.getOrderNumber());
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from makes_and_models";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from makes_and_models where make_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertMakesAndModel(final MakeAndModel... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMakeAndModel.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMakesAndModel(final MakeAndModel data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMakeAndModel.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMakesAndModelList(final List<MakeAndModel> MakesAndModelsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMakeAndModel.insert(MakesAndModelsList);
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
  public List<MakeAndModel> getMakesAndModels() {
    final String _sql = "select * from makes_and_models order by order_number,make";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMakeId = CursorUtil.getColumnIndexOrThrow(_cursor, "make_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfMakeTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfModelTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
      final int _cursorIndexOfModelCode = CursorUtil.getColumnIndexOrThrow(_cursor, "model_code");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final List<MakeAndModel> _result = new ArrayList<MakeAndModel>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MakeAndModel _item;
        _item = new MakeAndModel();
        final int _tmpMakeId;
        _tmpMakeId = _cursor.getInt(_cursorIndexOfMakeId);
        _item.setMakeId(_tmpMakeId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpMakeTitle;
        if (_cursor.isNull(_cursorIndexOfMakeTitle)) {
          _tmpMakeTitle = null;
        } else {
          _tmpMakeTitle = _cursor.getString(_cursorIndexOfMakeTitle);
        }
        _item.setMakeTitle(_tmpMakeTitle);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final String _tmpModelTitle;
        if (_cursor.isNull(_cursorIndexOfModelTitle)) {
          _tmpModelTitle = null;
        } else {
          _tmpModelTitle = _cursor.getString(_cursorIndexOfModelTitle);
        }
        _item.setModelTitle(_tmpModelTitle);
        final String _tmpModelCode;
        if (_cursor.isNull(_cursorIndexOfModelCode)) {
          _tmpModelCode = null;
        } else {
          _tmpModelCode = _cursor.getString(_cursorIndexOfModelCode);
        }
        _item.setModelCode(_tmpModelCode);
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

  @Override
  public MakeAndModel getMakeById(final int makeId) {
    final String _sql = "select * from makes_and_models where make_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, makeId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMakeId = CursorUtil.getColumnIndexOrThrow(_cursor, "make_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfMakeTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfModelTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
      final int _cursorIndexOfModelCode = CursorUtil.getColumnIndexOrThrow(_cursor, "model_code");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final MakeAndModel _result;
      if(_cursor.moveToFirst()) {
        _result = new MakeAndModel();
        final int _tmpMakeId;
        _tmpMakeId = _cursor.getInt(_cursorIndexOfMakeId);
        _result.setMakeId(_tmpMakeId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpMakeTitle;
        if (_cursor.isNull(_cursorIndexOfMakeTitle)) {
          _tmpMakeTitle = null;
        } else {
          _tmpMakeTitle = _cursor.getString(_cursorIndexOfMakeTitle);
        }
        _result.setMakeTitle(_tmpMakeTitle);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _result.setMakeCode(_tmpMakeCode);
        final String _tmpModelTitle;
        if (_cursor.isNull(_cursorIndexOfModelTitle)) {
          _tmpModelTitle = null;
        } else {
          _tmpModelTitle = _cursor.getString(_cursorIndexOfModelTitle);
        }
        _result.setModelTitle(_tmpModelTitle);
        final String _tmpModelCode;
        if (_cursor.isNull(_cursorIndexOfModelCode)) {
          _tmpModelCode = null;
        } else {
          _tmpModelCode = _cursor.getString(_cursorIndexOfModelCode);
        }
        _result.setModelCode(_tmpModelCode);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _result.setOrderNumber(_tmpOrderNumber);
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
  public MakeAndModel getMakeByCode(final String makeCode) {
    final String _sql = "select * from makes_and_models where make_code=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (makeCode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, makeCode);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMakeId = CursorUtil.getColumnIndexOrThrow(_cursor, "make_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfMakeTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfModelTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
      final int _cursorIndexOfModelCode = CursorUtil.getColumnIndexOrThrow(_cursor, "model_code");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final MakeAndModel _result;
      if(_cursor.moveToFirst()) {
        _result = new MakeAndModel();
        final int _tmpMakeId;
        _tmpMakeId = _cursor.getInt(_cursorIndexOfMakeId);
        _result.setMakeId(_tmpMakeId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpMakeTitle;
        if (_cursor.isNull(_cursorIndexOfMakeTitle)) {
          _tmpMakeTitle = null;
        } else {
          _tmpMakeTitle = _cursor.getString(_cursorIndexOfMakeTitle);
        }
        _result.setMakeTitle(_tmpMakeTitle);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _result.setMakeCode(_tmpMakeCode);
        final String _tmpModelTitle;
        if (_cursor.isNull(_cursorIndexOfModelTitle)) {
          _tmpModelTitle = null;
        } else {
          _tmpModelTitle = _cursor.getString(_cursorIndexOfModelTitle);
        }
        _result.setModelTitle(_tmpModelTitle);
        final String _tmpModelCode;
        if (_cursor.isNull(_cursorIndexOfModelCode)) {
          _tmpModelCode = null;
        } else {
          _tmpModelCode = _cursor.getString(_cursorIndexOfModelCode);
        }
        _result.setModelCode(_tmpModelCode);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _result.setOrderNumber(_tmpOrderNumber);
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
  public MakeAndModel getMakeByTitle(final String makeTitle) {
    final String _sql = "select * from makes_and_models where UPPER(make)=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (makeTitle == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, makeTitle);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMakeId = CursorUtil.getColumnIndexOrThrow(_cursor, "make_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfMakeTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfModelTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
      final int _cursorIndexOfModelCode = CursorUtil.getColumnIndexOrThrow(_cursor, "model_code");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final MakeAndModel _result;
      if(_cursor.moveToFirst()) {
        _result = new MakeAndModel();
        final int _tmpMakeId;
        _tmpMakeId = _cursor.getInt(_cursorIndexOfMakeId);
        _result.setMakeId(_tmpMakeId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpMakeTitle;
        if (_cursor.isNull(_cursorIndexOfMakeTitle)) {
          _tmpMakeTitle = null;
        } else {
          _tmpMakeTitle = _cursor.getString(_cursorIndexOfMakeTitle);
        }
        _result.setMakeTitle(_tmpMakeTitle);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _result.setMakeCode(_tmpMakeCode);
        final String _tmpModelTitle;
        if (_cursor.isNull(_cursorIndexOfModelTitle)) {
          _tmpModelTitle = null;
        } else {
          _tmpModelTitle = _cursor.getString(_cursorIndexOfModelTitle);
        }
        _result.setModelTitle(_tmpModelTitle);
        final String _tmpModelCode;
        if (_cursor.isNull(_cursorIndexOfModelCode)) {
          _tmpModelCode = null;
        } else {
          _tmpModelCode = _cursor.getString(_cursorIndexOfModelCode);
        }
        _result.setModelCode(_tmpModelCode);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _result.setOrderNumber(_tmpOrderNumber);
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
  public MakeAndModel getModelByCode(final String modelCode) {
    final String _sql = "select * from makes_and_models where model_code=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (modelCode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, modelCode);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMakeId = CursorUtil.getColumnIndexOrThrow(_cursor, "make_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfMakeTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfModelTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
      final int _cursorIndexOfModelCode = CursorUtil.getColumnIndexOrThrow(_cursor, "model_code");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final MakeAndModel _result;
      if(_cursor.moveToFirst()) {
        _result = new MakeAndModel();
        final int _tmpMakeId;
        _tmpMakeId = _cursor.getInt(_cursorIndexOfMakeId);
        _result.setMakeId(_tmpMakeId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpMakeTitle;
        if (_cursor.isNull(_cursorIndexOfMakeTitle)) {
          _tmpMakeTitle = null;
        } else {
          _tmpMakeTitle = _cursor.getString(_cursorIndexOfMakeTitle);
        }
        _result.setMakeTitle(_tmpMakeTitle);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _result.setMakeCode(_tmpMakeCode);
        final String _tmpModelTitle;
        if (_cursor.isNull(_cursorIndexOfModelTitle)) {
          _tmpModelTitle = null;
        } else {
          _tmpModelTitle = _cursor.getString(_cursorIndexOfModelTitle);
        }
        _result.setModelTitle(_tmpModelTitle);
        final String _tmpModelCode;
        if (_cursor.isNull(_cursorIndexOfModelCode)) {
          _tmpModelCode = null;
        } else {
          _tmpModelCode = _cursor.getString(_cursorIndexOfModelCode);
        }
        _result.setModelCode(_tmpModelCode);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _result.setOrderNumber(_tmpOrderNumber);
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
  public int getMakeIdByName(final String name) {
    final String _sql = "select make_id from makes_and_models where make=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
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

  @Override
  public int getMakeIdByCode(final String makeCode) {
    final String _sql = "select make_id from makes_and_models where make_code=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (makeCode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, makeCode);
    }
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

  @Override
  public int getModelIdByCode(final String modelCode) {
    final String _sql = "select make_id from makes_and_models where model_code=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (modelCode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, modelCode);
    }
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

  @Override
  public String getMakeCodeByName(final String name) {
    final String _sql = "select make_code from makes_and_models WHERE UPPER(make) = UPPER(?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final String _result;
      if(_cursor.moveToFirst()) {
        if (_cursor.isNull(0)) {
          _result = null;
        } else {
          _result = _cursor.getString(0);
        }
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
  public String getMakeCodeById(final int makeId) {
    final String _sql = "select make_code from makes_and_models where make_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, makeId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final String _result;
      if(_cursor.moveToFirst()) {
        if (_cursor.isNull(0)) {
          _result = null;
        } else {
          _result = _cursor.getString(0);
        }
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
