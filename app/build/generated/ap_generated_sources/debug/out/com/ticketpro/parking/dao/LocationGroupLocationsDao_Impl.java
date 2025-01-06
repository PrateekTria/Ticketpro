package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.LocationGroupLocation;
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
public final class LocationGroupLocationsDao_Impl implements LocationGroupLocationsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LocationGroupLocation> __insertionAdapterOfLocationGroupLocation;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public LocationGroupLocationsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLocationGroupLocation = new EntityInsertionAdapter<LocationGroupLocation>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `location_group_locations` (`location_group_id`,`group_id`,`location_id`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LocationGroupLocation value) {
        stmt.bindLong(1, value.getLocationGroupId());
        stmt.bindLong(2, value.getGroupId());
        stmt.bindLong(3, value.getLocationId());
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from location_group_locations";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from location_group_locations where location_group_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertLocationGroupLocation(final LocationGroupLocation... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLocationGroupLocation.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertLocationGroupLocation(final LocationGroupLocation data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLocationGroupLocation.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertLocationGroupLocationList(
      final List<LocationGroupLocation> LocationGroupLocationsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLocationGroupLocation.insert(LocationGroupLocationsList);
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
  public List<LocationGroupLocation> getLocationGroupLocations() {
    final String _sql = "select * from location_group_locations";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfLocationGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "location_group_id");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "group_id");
      final int _cursorIndexOfLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "location_id");
      final List<LocationGroupLocation> _result = new ArrayList<LocationGroupLocation>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final LocationGroupLocation _item;
        _item = new LocationGroupLocation();
        final int _tmpLocationGroupId;
        _tmpLocationGroupId = _cursor.getInt(_cursorIndexOfLocationGroupId);
        _item.setLocationGroupId(_tmpLocationGroupId);
        final int _tmpGroupId;
        _tmpGroupId = _cursor.getInt(_cursorIndexOfGroupId);
        _item.setGroupId(_tmpGroupId);
        final int _tmpLocationId;
        _tmpLocationId = _cursor.getInt(_cursorIndexOfLocationId);
        _item.setLocationId(_tmpLocationId);
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
