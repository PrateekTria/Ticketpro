package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.GenetecPatrollerActivities;
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
public final class GenetecPatrollerActivitiesDao_Impl implements GenetecPatrollerActivitiesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GenetecPatrollerActivities> __insertionAdapterOfGenetecPatrollerActivities;

  public GenetecPatrollerActivitiesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGenetecPatrollerActivities = new EntityInsertionAdapter<GenetecPatrollerActivities>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Genetec_PatrollerActivities` (`record_Id`,`createdOn`,`custId`,`patroller_Id`,`user_id`,`device_Id`,`is_active`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, GenetecPatrollerActivities value) {
        stmt.bindLong(1, value.recordId);
        if (value.createdOn == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.createdOn);
        }
        if (value.custId == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, value.custId);
        }
        if (value.patrollerId == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.patrollerId);
        }
        if (value.userId == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.userId);
        }
        if (value.deviceId == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, value.deviceId);
        }
        if (value.isActive == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.isActive);
        }
      }
    };
  }

  @Override
  public void insert(final GenetecPatrollerActivities genetecPatrollerActivities) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfGenetecPatrollerActivities.insert(genetecPatrollerActivities);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<GenetecPatrollerActivities> getAllGenetecPatrollerActivities() {
    final String _sql = "SELECT * FROM Genetec_PatrollerActivities";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRecordId = CursorUtil.getColumnIndexOrThrow(_cursor, "record_Id");
      final int _cursorIndexOfCreatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "createdOn");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custId");
      final int _cursorIndexOfPatrollerId = CursorUtil.getColumnIndexOrThrow(_cursor, "patroller_Id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_Id");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final List<GenetecPatrollerActivities> _result = new ArrayList<GenetecPatrollerActivities>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final GenetecPatrollerActivities _item;
        _item = new GenetecPatrollerActivities();
        _item.recordId = _cursor.getInt(_cursorIndexOfRecordId);
        if (_cursor.isNull(_cursorIndexOfCreatedOn)) {
          _item.createdOn = null;
        } else {
          _item.createdOn = _cursor.getString(_cursorIndexOfCreatedOn);
        }
        if (_cursor.isNull(_cursorIndexOfCustId)) {
          _item.custId = null;
        } else {
          _item.custId = _cursor.getInt(_cursorIndexOfCustId);
        }
        if (_cursor.isNull(_cursorIndexOfPatrollerId)) {
          _item.patrollerId = null;
        } else {
          _item.patrollerId = _cursor.getString(_cursorIndexOfPatrollerId);
        }
        if (_cursor.isNull(_cursorIndexOfUserId)) {
          _item.userId = null;
        } else {
          _item.userId = _cursor.getString(_cursorIndexOfUserId);
        }
        if (_cursor.isNull(_cursorIndexOfDeviceId)) {
          _item.deviceId = null;
        } else {
          _item.deviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        }
        if (_cursor.isNull(_cursorIndexOfIsActive)) {
          _item.isActive = null;
        } else {
          _item.isActive = _cursor.getString(_cursorIndexOfIsActive);
        }
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
