package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.Location;
import com.ticketpro.model.LocationGroup;
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
public final class LocationGroupsDao_Impl implements LocationGroupsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LocationGroup> __insertionAdapterOfLocationGroup;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public LocationGroupsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLocationGroup = new EntityInsertionAdapter<LocationGroup>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `location_groups` (`group_id`,`custid`,`group_code`,`group_name`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LocationGroup value) {
        stmt.bindLong(1, value.getGroupId());
        stmt.bindLong(2, value.getCustId());
        if (value.getGroupCode() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getGroupCode());
        }
        if (value.getGroupName() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getGroupName());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from location_groups";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from location_groups where group_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertLocationGroup(final LocationGroup... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLocationGroup.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertLocationGroup(final LocationGroup data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLocationGroup.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertLocationGroupList(final List<LocationGroup> LocationGroupsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLocationGroup.insert(LocationGroupsList);
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
  public List<LocationGroup> getLocationGroups() {
    final String _sql = "select * from location_groups";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "group_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfGroupCode = CursorUtil.getColumnIndexOrThrow(_cursor, "group_code");
      final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "group_name");
      final List<LocationGroup> _result = new ArrayList<LocationGroup>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final LocationGroup _item;
        _item = new LocationGroup();
        final int _tmpGroupId;
        _tmpGroupId = _cursor.getInt(_cursorIndexOfGroupId);
        _item.setGroupId(_tmpGroupId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpGroupCode;
        if (_cursor.isNull(_cursorIndexOfGroupCode)) {
          _tmpGroupCode = null;
        } else {
          _tmpGroupCode = _cursor.getString(_cursorIndexOfGroupCode);
        }
        _item.setGroupCode(_tmpGroupCode);
        final String _tmpGroupName;
        if (_cursor.isNull(_cursorIndexOfGroupName)) {
          _tmpGroupName = null;
        } else {
          _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
        }
        _item.setGroupName(_tmpGroupName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Location> getLocationsByGroup(final String group) {
    final String _sql = "select * from locations where locations.zone_id=-1 or locations.location_id in (select location_id from location_group_locations where group_id=(select group_id from location_groups where group_code=?)) order by order_number";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (group == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, group);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "location_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfZoneId = CursorUtil.getColumnIndexOrThrow(_cursor, "zone_id");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final int _cursorIndexOfIsManual = CursorUtil.getColumnIndexOrThrow(_cursor, "is_manual");
      final List<Location> _result = new ArrayList<Location>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Location _item;
        _item = new Location();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
        final int _tmpZoneId;
        _tmpZoneId = _cursor.getInt(_cursorIndexOfZoneId);
        _item.setZoneId(_tmpZoneId);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _item.setOrderNumber(_tmpOrderNumber);
        final String _tmpIsManual;
        if (_cursor.isNull(_cursorIndexOfIsManual)) {
          _tmpIsManual = null;
        } else {
          _tmpIsManual = _cursor.getString(_cursorIndexOfIsManual);
        }
        _item.setIsManual(_tmpIsManual);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Location> getALLLocationsByGroup() {
    final String _sql = "select * from locations order by order_number";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "location_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfZoneId = CursorUtil.getColumnIndexOrThrow(_cursor, "zone_id");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final int _cursorIndexOfIsManual = CursorUtil.getColumnIndexOrThrow(_cursor, "is_manual");
      final List<Location> _result = new ArrayList<Location>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Location _item;
        _item = new Location();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
        final int _tmpZoneId;
        _tmpZoneId = _cursor.getInt(_cursorIndexOfZoneId);
        _item.setZoneId(_tmpZoneId);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _item.setOrderNumber(_tmpOrderNumber);
        final String _tmpIsManual;
        if (_cursor.isNull(_cursorIndexOfIsManual)) {
          _tmpIsManual = null;
        } else {
          _tmpIsManual = _cursor.getString(_cursorIndexOfIsManual);
        }
        _item.setIsManual(_tmpIsManual);
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
