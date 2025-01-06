package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.Meter;
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
public final class MetersDao_Impl implements MetersDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Meter> __insertionAdapterOfMeter;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public MetersDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMeter = new EntityInsertionAdapter<Meter>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `meters` (`meter_id`,`custid`,`meter`,`location`,`street_number`,`street_prefix`,`street_suffix`,`direction`,`violation_code`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Meter value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getCustId());
        if (value.getMeter() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getMeter());
        }
        if (value.getLocation() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getLocation());
        }
        if (value.getStreetNumber() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getStreetNumber());
        }
        if (value.getStreetPrefix() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getStreetPrefix());
        }
        if (value.getStreetSuffix() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getStreetSuffix());
        }
        if (value.getDirection() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getDirection());
        }
        if (value.getViolationCode() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getViolationCode());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from meters";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from meters where meter_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertMeter(final Meter... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMeter.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMeter(final Meter data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMeter.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMeterList(final List<Meter> MetersList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMeter.insert(MetersList);
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
  public List<Meter> getMeters() {
    final String _sql = "Select * from meters";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "meter_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfStreetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "street_number");
      final int _cursorIndexOfStreetPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_prefix");
      final int _cursorIndexOfStreetSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_suffix");
      final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
      final int _cursorIndexOfViolationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_code");
      final List<Meter> _result = new ArrayList<Meter>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Meter _item;
        _item = new Meter();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
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
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item.setDirection(_tmpDirection);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Meter searchMeterHistory(final String meter) {
    final String _sql = "select * from meters where meter=? order by meter_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (meter == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, meter);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "meter_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfStreetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "street_number");
      final int _cursorIndexOfStreetPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_prefix");
      final int _cursorIndexOfStreetSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_suffix");
      final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
      final int _cursorIndexOfViolationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_code");
      final Meter _result;
      if(_cursor.moveToFirst()) {
        _result = new Meter();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _result.setMeter(_tmpMeter);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _result.setLocation(_tmpLocation);
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _result.setStreetNumber(_tmpStreetNumber);
        final String _tmpStreetPrefix;
        if (_cursor.isNull(_cursorIndexOfStreetPrefix)) {
          _tmpStreetPrefix = null;
        } else {
          _tmpStreetPrefix = _cursor.getString(_cursorIndexOfStreetPrefix);
        }
        _result.setStreetPrefix(_tmpStreetPrefix);
        final String _tmpStreetSuffix;
        if (_cursor.isNull(_cursorIndexOfStreetSuffix)) {
          _tmpStreetSuffix = null;
        } else {
          _tmpStreetSuffix = _cursor.getString(_cursorIndexOfStreetSuffix);
        }
        _result.setStreetSuffix(_tmpStreetSuffix);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _result.setDirection(_tmpDirection);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _result.setViolationCode(_tmpViolationCode);
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
