package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.GPSLocation;
import com.ticketpro.util.Converters;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class GPSLocationsDao_Impl implements GPSLocationsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GPSLocation> __insertionAdapterOfGPSLocation;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public GPSLocationsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGPSLocation = new EntityInsertionAdapter<GPSLocation>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `gps_locations` (`location_id`,`custid`,`latitude`,`longitude`,`gpstime`,`location`,`street_number`,`street_prefix`,`street_suffix`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, GPSLocation value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getCustId());
        if (value.getLatitude() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getLatitude());
        }
        if (value.getLongitude() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getLongitude());
        }
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getGpstime());
        if (_tmp == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, _tmp);
        }
        if (value.getLocation() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getLocation());
        }
        if (value.getStreetNumber() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getStreetNumber());
        }
        if (value.getStreetPrefix() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getStreetPrefix());
        }
        if (value.getStreetSuffix() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getStreetSuffix());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from gps_locations";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from gps_locations where location_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertGPSLocation(final GPSLocation... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfGPSLocation.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertGPSLocation(final GPSLocation data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfGPSLocation.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertGPSLocationList(final List<GPSLocation> GPSLocationsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfGPSLocation.insert(GPSLocationsList);
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
  public List<GPSLocation> getGPSLocations() {
    final String _sql = "select * from gps_locations";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "location_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfGpstime = CursorUtil.getColumnIndexOrThrow(_cursor, "gpstime");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfStreetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "street_number");
      final int _cursorIndexOfStreetPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_prefix");
      final int _cursorIndexOfStreetSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_suffix");
      final List<GPSLocation> _result = new ArrayList<GPSLocation>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final GPSLocation _item;
        _item = new GPSLocation();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        }
        _item.setLatitude(_tmpLatitude);
        final String _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        }
        _item.setLongitude(_tmpLongitude);
        final Date _tmpGpstime;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp);
        _item.setGpstime(_tmpGpstime);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item.setStreetNumber(_tmpStreetNumber);
        final String _tmpStreetPrefix;
        if (_cursor.isNull(_cursorIndexOfStreetPrefix)) {
          _tmpStreetPrefix = null;
        } else {
          _tmpStreetPrefix = _cursor.getString(_cursorIndexOfStreetPrefix);
        }
        _item.setStreetPrefix(_tmpStreetPrefix);
        final String _tmpStreetSuffix;
        if (_cursor.isNull(_cursorIndexOfStreetSuffix)) {
          _tmpStreetSuffix = null;
        } else {
          _tmpStreetSuffix = _cursor.getString(_cursorIndexOfStreetSuffix);
        }
        _item.setStreetSuffix(_tmpStreetSuffix);
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
