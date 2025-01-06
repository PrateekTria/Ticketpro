package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.GenetecPatrollers;
import io.reactivex.Completable;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class GenetecPatrollerDao_Impl implements GenetecPatrollerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GenetecPatrollers> __insertionAdapterOfGenetecPatrollers;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public GenetecPatrollerDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGenetecPatrollers = new EntityInsertionAdapter<GenetecPatrollers>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Genetec_Patrollers` (`record_Id`,`custId`,`patroller_Id`,`vehicleName`,`is_active`,`createdOn`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, GenetecPatrollers value) {
        stmt.bindLong(1, value.getRecord_Id());
        if (value.getCustId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, value.getCustId());
        }
        if (value.getPatrollerId() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPatrollerId());
        }
        if (value.getVehicleName() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getVehicleName());
        }
        if (value.getIsActive() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getIsActive());
        }
        if (value.getCreatedOn() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getCreatedOn());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from genetec_patrollers";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from genetec_patrollers where patroller_Id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertPatrollers(final GenetecPatrollers... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfGenetecPatrollers.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertPatrollers(final GenetecPatrollers data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGenetecPatrollers.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable insertPatrollersList(final List<GenetecPatrollers> PatrollersList) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGenetecPatrollers.insert(PatrollersList);
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
  public List<GenetecPatrollers> getAllPatrollers() {
    final String _sql = "select * from genetec_patrollers order by patroller_Id";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRecordId = CursorUtil.getColumnIndexOrThrow(_cursor, "record_Id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custId");
      final int _cursorIndexOfPatrollerId = CursorUtil.getColumnIndexOrThrow(_cursor, "patroller_Id");
      final int _cursorIndexOfVehicleName = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleName");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final int _cursorIndexOfCreatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "createdOn");
      final List<GenetecPatrollers> _result = new ArrayList<GenetecPatrollers>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final GenetecPatrollers _item;
        _item = new GenetecPatrollers();
        final int _tmpRecord_Id;
        _tmpRecord_Id = _cursor.getInt(_cursorIndexOfRecordId);
        _item.setRecord_Id(_tmpRecord_Id);
        final Integer _tmpCustId;
        if (_cursor.isNull(_cursorIndexOfCustId)) {
          _tmpCustId = null;
        } else {
          _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        }
        _item.setCustId(_tmpCustId);
        final String _tmpPatrollerId;
        if (_cursor.isNull(_cursorIndexOfPatrollerId)) {
          _tmpPatrollerId = null;
        } else {
          _tmpPatrollerId = _cursor.getString(_cursorIndexOfPatrollerId);
        }
        _item.setPatrollerId(_tmpPatrollerId);
        final String _tmpVehicleName;
        if (_cursor.isNull(_cursorIndexOfVehicleName)) {
          _tmpVehicleName = null;
        } else {
          _tmpVehicleName = _cursor.getString(_cursorIndexOfVehicleName);
        }
        _item.setVehicleName(_tmpVehicleName);
        final String _tmpIsActive;
        if (_cursor.isNull(_cursorIndexOfIsActive)) {
          _tmpIsActive = null;
        } else {
          _tmpIsActive = _cursor.getString(_cursorIndexOfIsActive);
        }
        _item.setIsActive(_tmpIsActive);
        final String _tmpCreatedOn;
        if (_cursor.isNull(_cursorIndexOfCreatedOn)) {
          _tmpCreatedOn = null;
        } else {
          _tmpCreatedOn = _cursor.getString(_cursorIndexOfCreatedOn);
        }
        _item.setCreatedOn(_tmpCreatedOn);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<GenetecPatrollers> getPatrollers() {
    final String _sql = "select * from genetec_patrollers order by patroller_Id";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRecordId = CursorUtil.getColumnIndexOrThrow(_cursor, "record_Id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custId");
      final int _cursorIndexOfPatrollerId = CursorUtil.getColumnIndexOrThrow(_cursor, "patroller_Id");
      final int _cursorIndexOfVehicleName = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleName");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final int _cursorIndexOfCreatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "createdOn");
      final List<GenetecPatrollers> _result = new ArrayList<GenetecPatrollers>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final GenetecPatrollers _item;
        _item = new GenetecPatrollers();
        final int _tmpRecord_Id;
        _tmpRecord_Id = _cursor.getInt(_cursorIndexOfRecordId);
        _item.setRecord_Id(_tmpRecord_Id);
        final Integer _tmpCustId;
        if (_cursor.isNull(_cursorIndexOfCustId)) {
          _tmpCustId = null;
        } else {
          _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        }
        _item.setCustId(_tmpCustId);
        final String _tmpPatrollerId;
        if (_cursor.isNull(_cursorIndexOfPatrollerId)) {
          _tmpPatrollerId = null;
        } else {
          _tmpPatrollerId = _cursor.getString(_cursorIndexOfPatrollerId);
        }
        _item.setPatrollerId(_tmpPatrollerId);
        final String _tmpVehicleName;
        if (_cursor.isNull(_cursorIndexOfVehicleName)) {
          _tmpVehicleName = null;
        } else {
          _tmpVehicleName = _cursor.getString(_cursorIndexOfVehicleName);
        }
        _item.setVehicleName(_tmpVehicleName);
        final String _tmpIsActive;
        if (_cursor.isNull(_cursorIndexOfIsActive)) {
          _tmpIsActive = null;
        } else {
          _tmpIsActive = _cursor.getString(_cursorIndexOfIsActive);
        }
        _item.setIsActive(_tmpIsActive);
        final String _tmpCreatedOn;
        if (_cursor.isNull(_cursorIndexOfCreatedOn)) {
          _tmpCreatedOn = null;
        } else {
          _tmpCreatedOn = _cursor.getString(_cursorIndexOfCreatedOn);
        }
        _item.setCreatedOn(_tmpCreatedOn);
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
