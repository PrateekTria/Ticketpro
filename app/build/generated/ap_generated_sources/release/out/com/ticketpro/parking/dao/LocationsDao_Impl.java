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
public final class LocationsDao_Impl implements LocationsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Location> __insertionAdapterOfLocation;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  private final SharedSQLiteStatement __preparedStmtOfRemoveManuaLocation;

  public LocationsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLocation = new EntityInsertionAdapter<Location>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `locations` (`location_id`,`custid`,`location`,`zone_id`,`order_number`,`is_manual`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Location value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getCustId());
        if (value.getLocation() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getLocation());
        }
        stmt.bindLong(4, value.getZoneId());
        stmt.bindLong(5, value.getOrderNumber());
        if (value.getIsManual() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getIsManual());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from locations";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from locations where location_id=?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveManuaLocation = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM locations WHERE is_manual =? ";
        return _query;
      }
    };
  }

  @Override
  public void insertLocation(final Location... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLocation.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertLocation(final Location data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLocation.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertLocationList(final List<Location> LocationsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLocation.insert(LocationsList);
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
  public void removeManuaLocation(final String isManual) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveManuaLocation.acquire();
    int _argIndex = 1;
    if (isManual == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, isManual);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveManuaLocation.release(_stmt);
    }
  }

  @Override
  public Location getLocationByText(final String locationText) {
    final String _sql = "select * from locations where location=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (locationText == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, locationText);
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
      final Location _result;
      if(_cursor.moveToFirst()) {
        _result = new Location();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _result.setLocation(_tmpLocation);
        final int _tmpZoneId;
        _tmpZoneId = _cursor.getInt(_cursorIndexOfZoneId);
        _result.setZoneId(_tmpZoneId);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _result.setOrderNumber(_tmpOrderNumber);
        final String _tmpIsManual;
        if (_cursor.isNull(_cursorIndexOfIsManual)) {
          _tmpIsManual = null;
        } else {
          _tmpIsManual = _cursor.getString(_cursorIndexOfIsManual);
        }
        _result.setIsManual(_tmpIsManual);
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
  public List<Location> getManualLocations() {
    final String _sql = "select * from locations where is_manual='Y'";
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

  @Override
  public int getLastInsertId() {
    final String _sql = "select max(location_id) as max_location_id from locations";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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
  public List<Location> getLocations() {
    final String _sql = "select * from locations order by order_number,location";
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
