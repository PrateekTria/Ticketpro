package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.util.Converters;
import io.reactivex.Completable;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.Void;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ChalkVehiclesDao_Impl implements ChalkVehiclesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ChalkVehicle> __insertionAdapterOfChalkVehicle;

  private final SharedSQLiteStatement __preparedStmtOfUpdateChalkVehicleStatus;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveChalkVehicleById;

  public ChalkVehiclesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChalkVehicle = new EntityInsertionAdapter<ChalkVehicle>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `chalk_vehicles` (`chalk_id`,`userid`,`device_id`,`chalk_date`,`permit`,`plate`,`vin`,`state_id`,`zone_id`,`expiration`,`duration_id`,`duration_code`,`space`,`meter`,`tire`,`chalkx`,`chalky`,`stemx`,`stemy`,`chalk_type`,`location`,`street_prefix`,`street_suffix`,`street_number`,`direction`,`latitude`,`longitude`,`gpstime`,`is_gps_location`,`is_expired`,`custid`,`wheel_size`,`notes`,`make_code`,`color_code`,`make`,`color`,`sync_status`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ChalkVehicle value) {
        stmt.bindLong(1, value.getChalkId());
        stmt.bindLong(2, value.getUserId());
        stmt.bindLong(3, value.getDeviceId());
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getChalkDate());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        if (value.getPermit() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getPermit());
        }
        if (value.getPlate() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getPlate());
        }
        if (value.getVin() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getVin());
        }
        stmt.bindLong(8, value.getStateId());
        stmt.bindLong(9, value.getZoneId());
        final Long _tmp_1;
        _tmp_1 = Converters.dateToTimestamp(value.getExpiration());
        if (_tmp_1 == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindLong(10, _tmp_1);
        }
        stmt.bindLong(11, value.getDurationId());
        if (value.getDurationCode() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getDurationCode());
        }
        if (value.getSpace() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getSpace());
        }
        if (value.getMeter() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getMeter());
        }
        if (value.getTire() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getTire());
        }
        stmt.bindLong(16, value.getChalkx());
        stmt.bindLong(17, value.getChalky());
        stmt.bindLong(18, value.getStemx());
        stmt.bindLong(19, value.getStemy());
        if (value.getChalkType() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.getChalkType());
        }
        if (value.getLocation() == null) {
          stmt.bindNull(21);
        } else {
          stmt.bindString(21, value.getLocation());
        }
        if (value.getStreetPrefix() == null) {
          stmt.bindNull(22);
        } else {
          stmt.bindString(22, value.getStreetPrefix());
        }
        if (value.getStreetSuffix() == null) {
          stmt.bindNull(23);
        } else {
          stmt.bindString(23, value.getStreetSuffix());
        }
        if (value.getStreetNumber() == null) {
          stmt.bindNull(24);
        } else {
          stmt.bindString(24, value.getStreetNumber());
        }
        if (value.getDirection() == null) {
          stmt.bindNull(25);
        } else {
          stmt.bindString(25, value.getDirection());
        }
        if (value.getLatitude() == null) {
          stmt.bindNull(26);
        } else {
          stmt.bindString(26, value.getLatitude());
        }
        if (value.getLongitude() == null) {
          stmt.bindNull(27);
        } else {
          stmt.bindString(27, value.getLongitude());
        }
        final Long _tmp_2;
        _tmp_2 = Converters.dateToTimestamp(value.getGpstime());
        if (_tmp_2 == null) {
          stmt.bindNull(28);
        } else {
          stmt.bindLong(28, _tmp_2);
        }
        if (value.getIsGPSLocation() == null) {
          stmt.bindNull(29);
        } else {
          stmt.bindString(29, value.getIsGPSLocation());
        }
        if (value.getIsExpired() == null) {
          stmt.bindNull(30);
        } else {
          stmt.bindString(30, value.getIsExpired());
        }
        stmt.bindLong(31, value.getCustId());
        stmt.bindLong(32, value.getWheelSize());
        if (value.getNotes() == null) {
          stmt.bindNull(33);
        } else {
          stmt.bindString(33, value.getNotes());
        }
        if (value.getMakeCode() == null) {
          stmt.bindNull(34);
        } else {
          stmt.bindString(34, value.getMakeCode());
        }
        if (value.getColorCode() == null) {
          stmt.bindNull(35);
        } else {
          stmt.bindString(35, value.getColorCode());
        }
        if (value.getMake() == null) {
          stmt.bindNull(36);
        } else {
          stmt.bindString(36, value.getMake());
        }
        if (value.getColor() == null) {
          stmt.bindNull(37);
        } else {
          stmt.bindString(37, value.getColor());
        }
        if (value.getSyncStatus() == null) {
          stmt.bindNull(38);
        } else {
          stmt.bindString(38, value.getSyncStatus());
        }
      }
    };
    this.__preparedStmtOfUpdateChalkVehicleStatus = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update chalk_vehicles set sync_status=? where chalk_id=?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from chalk_vehicles";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveChalkVehicleById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from chalk_vehicles where chalk_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertChalkVehicle(final ChalkVehicle... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfChalkVehicle.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertChalkVehicle(final ChalkVehicle data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChalkVehicle.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertChalkVehicleList(final List<ChalkVehicle> ChalkVehiclesList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfChalkVehicle.insert(ChalkVehiclesList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateChalkVehicleStatus(final String status, final long chalkId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateChalkVehicleStatus.acquire();
    int _argIndex = 1;
    if (status == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, status);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, chalkId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateChalkVehicleStatus.release(_stmt);
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
  public void removeChalkVehicleById(final long chalkId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveChalkVehicleById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, chalkId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveChalkVehicleById.release(_stmt);
    }
  }

  @Override
  public List<ChalkVehicle> getChalkVehicles(final int userId) {
    final String _sql = "select * from chalk_vehicles where userid=? order by chalk_date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfChalkDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_date");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfStateId = CursorUtil.getColumnIndexOrThrow(_cursor, "state_id");
      final int _cursorIndexOfZoneId = CursorUtil.getColumnIndexOrThrow(_cursor, "zone_id");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "expiration");
      final int _cursorIndexOfDurationId = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_id");
      final int _cursorIndexOfDurationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_code");
      final int _cursorIndexOfSpace = CursorUtil.getColumnIndexOrThrow(_cursor, "space");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfTire = CursorUtil.getColumnIndexOrThrow(_cursor, "tire");
      final int _cursorIndexOfChalkx = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkx");
      final int _cursorIndexOfChalky = CursorUtil.getColumnIndexOrThrow(_cursor, "chalky");
      final int _cursorIndexOfStemx = CursorUtil.getColumnIndexOrThrow(_cursor, "stemx");
      final int _cursorIndexOfStemy = CursorUtil.getColumnIndexOrThrow(_cursor, "stemy");
      final int _cursorIndexOfChalkType = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_type");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfStreetPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_prefix");
      final int _cursorIndexOfStreetSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_suffix");
      final int _cursorIndexOfStreetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "street_number");
      final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfGpstime = CursorUtil.getColumnIndexOrThrow(_cursor, "gpstime");
      final int _cursorIndexOfIsGPSLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "is_gps_location");
      final int _cursorIndexOfIsExpired = CursorUtil.getColumnIndexOrThrow(_cursor, "is_expired");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfWheelSize = CursorUtil.getColumnIndexOrThrow(_cursor, "wheel_size");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfMake = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final List<ChalkVehicle> _result = new ArrayList<ChalkVehicle>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ChalkVehicle _item;
        _item = new ChalkVehicle();
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final Date _tmpChalkDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfChalkDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfChalkDate);
        }
        _tmpChalkDate = Converters.fromTimestamp(_tmp);
        _item.setChalkDate(_tmpChalkDate);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _item.setStateId(_tmpStateId);
        final int _tmpZoneId;
        _tmpZoneId = _cursor.getInt(_cursorIndexOfZoneId);
        _item.setZoneId(_tmpZoneId);
        final Date _tmpExpiration;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfExpiration);
        }
        _tmpExpiration = Converters.fromTimestamp(_tmp_1);
        _item.setExpiration(_tmpExpiration);
        final int _tmpDurationId;
        _tmpDurationId = _cursor.getInt(_cursorIndexOfDurationId);
        _item.setDurationId(_tmpDurationId);
        final String _tmpDurationCode;
        if (_cursor.isNull(_cursorIndexOfDurationCode)) {
          _tmpDurationCode = null;
        } else {
          _tmpDurationCode = _cursor.getString(_cursorIndexOfDurationCode);
        }
        _item.setDurationCode(_tmpDurationCode);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _item.setSpace(_tmpSpace);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final String _tmpTire;
        if (_cursor.isNull(_cursorIndexOfTire)) {
          _tmpTire = null;
        } else {
          _tmpTire = _cursor.getString(_cursorIndexOfTire);
        }
        _item.setTire(_tmpTire);
        final int _tmpChalkx;
        _tmpChalkx = _cursor.getInt(_cursorIndexOfChalkx);
        _item.setChalkx(_tmpChalkx);
        final int _tmpChalky;
        _tmpChalky = _cursor.getInt(_cursorIndexOfChalky);
        _item.setChalky(_tmpChalky);
        final int _tmpStemx;
        _tmpStemx = _cursor.getInt(_cursorIndexOfStemx);
        _item.setStemx(_tmpStemx);
        final int _tmpStemy;
        _tmpStemy = _cursor.getInt(_cursorIndexOfStemy);
        _item.setStemy(_tmpStemy);
        final String _tmpChalkType;
        if (_cursor.isNull(_cursorIndexOfChalkType)) {
          _tmpChalkType = null;
        } else {
          _tmpChalkType = _cursor.getString(_cursorIndexOfChalkType);
        }
        _item.setChalkType(_tmpChalkType);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
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
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item.setStreetNumber(_tmpStreetNumber);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item.setDirection(_tmpDirection);
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
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_2);
        _item.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _item.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsExpired;
        if (_cursor.isNull(_cursorIndexOfIsExpired)) {
          _tmpIsExpired = null;
        } else {
          _tmpIsExpired = _cursor.getString(_cursorIndexOfIsExpired);
        }
        _item.setIsExpired(_tmpIsExpired);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpWheelSize;
        _tmpWheelSize = _cursor.getInt(_cursorIndexOfWheelSize);
        _item.setWheelSize(_tmpWheelSize);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _item.setNotes(_tmpNotes);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpMake;
        if (_cursor.isNull(_cursorIndexOfMake)) {
          _tmpMake = null;
        } else {
          _tmpMake = _cursor.getString(_cursorIndexOfMake);
        }
        _item.setMake(_tmpMake);
        final String _tmpColor;
        if (_cursor.isNull(_cursorIndexOfColor)) {
          _tmpColor = null;
        } else {
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
        }
        _item.setColor(_tmpColor);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _item.setSyncStatus(_tmpSyncStatus);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ChalkVehicle> getAllChalkedVehicle() {
    final String _sql = "select * from chalk_vehicles order by chalk_date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfChalkDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_date");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfStateId = CursorUtil.getColumnIndexOrThrow(_cursor, "state_id");
      final int _cursorIndexOfZoneId = CursorUtil.getColumnIndexOrThrow(_cursor, "zone_id");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "expiration");
      final int _cursorIndexOfDurationId = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_id");
      final int _cursorIndexOfDurationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_code");
      final int _cursorIndexOfSpace = CursorUtil.getColumnIndexOrThrow(_cursor, "space");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfTire = CursorUtil.getColumnIndexOrThrow(_cursor, "tire");
      final int _cursorIndexOfChalkx = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkx");
      final int _cursorIndexOfChalky = CursorUtil.getColumnIndexOrThrow(_cursor, "chalky");
      final int _cursorIndexOfStemx = CursorUtil.getColumnIndexOrThrow(_cursor, "stemx");
      final int _cursorIndexOfStemy = CursorUtil.getColumnIndexOrThrow(_cursor, "stemy");
      final int _cursorIndexOfChalkType = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_type");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfStreetPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_prefix");
      final int _cursorIndexOfStreetSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_suffix");
      final int _cursorIndexOfStreetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "street_number");
      final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfGpstime = CursorUtil.getColumnIndexOrThrow(_cursor, "gpstime");
      final int _cursorIndexOfIsGPSLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "is_gps_location");
      final int _cursorIndexOfIsExpired = CursorUtil.getColumnIndexOrThrow(_cursor, "is_expired");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfWheelSize = CursorUtil.getColumnIndexOrThrow(_cursor, "wheel_size");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfMake = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final List<ChalkVehicle> _result = new ArrayList<ChalkVehicle>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ChalkVehicle _item;
        _item = new ChalkVehicle();
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final Date _tmpChalkDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfChalkDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfChalkDate);
        }
        _tmpChalkDate = Converters.fromTimestamp(_tmp);
        _item.setChalkDate(_tmpChalkDate);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _item.setStateId(_tmpStateId);
        final int _tmpZoneId;
        _tmpZoneId = _cursor.getInt(_cursorIndexOfZoneId);
        _item.setZoneId(_tmpZoneId);
        final Date _tmpExpiration;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfExpiration);
        }
        _tmpExpiration = Converters.fromTimestamp(_tmp_1);
        _item.setExpiration(_tmpExpiration);
        final int _tmpDurationId;
        _tmpDurationId = _cursor.getInt(_cursorIndexOfDurationId);
        _item.setDurationId(_tmpDurationId);
        final String _tmpDurationCode;
        if (_cursor.isNull(_cursorIndexOfDurationCode)) {
          _tmpDurationCode = null;
        } else {
          _tmpDurationCode = _cursor.getString(_cursorIndexOfDurationCode);
        }
        _item.setDurationCode(_tmpDurationCode);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _item.setSpace(_tmpSpace);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final String _tmpTire;
        if (_cursor.isNull(_cursorIndexOfTire)) {
          _tmpTire = null;
        } else {
          _tmpTire = _cursor.getString(_cursorIndexOfTire);
        }
        _item.setTire(_tmpTire);
        final int _tmpChalkx;
        _tmpChalkx = _cursor.getInt(_cursorIndexOfChalkx);
        _item.setChalkx(_tmpChalkx);
        final int _tmpChalky;
        _tmpChalky = _cursor.getInt(_cursorIndexOfChalky);
        _item.setChalky(_tmpChalky);
        final int _tmpStemx;
        _tmpStemx = _cursor.getInt(_cursorIndexOfStemx);
        _item.setStemx(_tmpStemx);
        final int _tmpStemy;
        _tmpStemy = _cursor.getInt(_cursorIndexOfStemy);
        _item.setStemy(_tmpStemy);
        final String _tmpChalkType;
        if (_cursor.isNull(_cursorIndexOfChalkType)) {
          _tmpChalkType = null;
        } else {
          _tmpChalkType = _cursor.getString(_cursorIndexOfChalkType);
        }
        _item.setChalkType(_tmpChalkType);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
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
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item.setStreetNumber(_tmpStreetNumber);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item.setDirection(_tmpDirection);
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
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_2);
        _item.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _item.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsExpired;
        if (_cursor.isNull(_cursorIndexOfIsExpired)) {
          _tmpIsExpired = null;
        } else {
          _tmpIsExpired = _cursor.getString(_cursorIndexOfIsExpired);
        }
        _item.setIsExpired(_tmpIsExpired);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpWheelSize;
        _tmpWheelSize = _cursor.getInt(_cursorIndexOfWheelSize);
        _item.setWheelSize(_tmpWheelSize);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _item.setNotes(_tmpNotes);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpMake;
        if (_cursor.isNull(_cursorIndexOfMake)) {
          _tmpMake = null;
        } else {
          _tmpMake = _cursor.getString(_cursorIndexOfMake);
        }
        _item.setMake(_tmpMake);
        final String _tmpColor;
        if (_cursor.isNull(_cursorIndexOfColor)) {
          _tmpColor = null;
        } else {
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
        }
        _item.setColor(_tmpColor);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _item.setSyncStatus(_tmpSyncStatus);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ChalkVehicle> getPendingChalkedVehicle() {
    final String _sql = "select * from chalk_vehicles where sync_status='P' order by chalk_date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfChalkDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_date");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfStateId = CursorUtil.getColumnIndexOrThrow(_cursor, "state_id");
      final int _cursorIndexOfZoneId = CursorUtil.getColumnIndexOrThrow(_cursor, "zone_id");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "expiration");
      final int _cursorIndexOfDurationId = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_id");
      final int _cursorIndexOfDurationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_code");
      final int _cursorIndexOfSpace = CursorUtil.getColumnIndexOrThrow(_cursor, "space");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfTire = CursorUtil.getColumnIndexOrThrow(_cursor, "tire");
      final int _cursorIndexOfChalkx = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkx");
      final int _cursorIndexOfChalky = CursorUtil.getColumnIndexOrThrow(_cursor, "chalky");
      final int _cursorIndexOfStemx = CursorUtil.getColumnIndexOrThrow(_cursor, "stemx");
      final int _cursorIndexOfStemy = CursorUtil.getColumnIndexOrThrow(_cursor, "stemy");
      final int _cursorIndexOfChalkType = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_type");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfStreetPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_prefix");
      final int _cursorIndexOfStreetSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_suffix");
      final int _cursorIndexOfStreetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "street_number");
      final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfGpstime = CursorUtil.getColumnIndexOrThrow(_cursor, "gpstime");
      final int _cursorIndexOfIsGPSLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "is_gps_location");
      final int _cursorIndexOfIsExpired = CursorUtil.getColumnIndexOrThrow(_cursor, "is_expired");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfWheelSize = CursorUtil.getColumnIndexOrThrow(_cursor, "wheel_size");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfMake = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final List<ChalkVehicle> _result = new ArrayList<ChalkVehicle>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ChalkVehicle _item;
        _item = new ChalkVehicle();
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final Date _tmpChalkDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfChalkDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfChalkDate);
        }
        _tmpChalkDate = Converters.fromTimestamp(_tmp);
        _item.setChalkDate(_tmpChalkDate);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _item.setStateId(_tmpStateId);
        final int _tmpZoneId;
        _tmpZoneId = _cursor.getInt(_cursorIndexOfZoneId);
        _item.setZoneId(_tmpZoneId);
        final Date _tmpExpiration;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfExpiration);
        }
        _tmpExpiration = Converters.fromTimestamp(_tmp_1);
        _item.setExpiration(_tmpExpiration);
        final int _tmpDurationId;
        _tmpDurationId = _cursor.getInt(_cursorIndexOfDurationId);
        _item.setDurationId(_tmpDurationId);
        final String _tmpDurationCode;
        if (_cursor.isNull(_cursorIndexOfDurationCode)) {
          _tmpDurationCode = null;
        } else {
          _tmpDurationCode = _cursor.getString(_cursorIndexOfDurationCode);
        }
        _item.setDurationCode(_tmpDurationCode);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _item.setSpace(_tmpSpace);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final String _tmpTire;
        if (_cursor.isNull(_cursorIndexOfTire)) {
          _tmpTire = null;
        } else {
          _tmpTire = _cursor.getString(_cursorIndexOfTire);
        }
        _item.setTire(_tmpTire);
        final int _tmpChalkx;
        _tmpChalkx = _cursor.getInt(_cursorIndexOfChalkx);
        _item.setChalkx(_tmpChalkx);
        final int _tmpChalky;
        _tmpChalky = _cursor.getInt(_cursorIndexOfChalky);
        _item.setChalky(_tmpChalky);
        final int _tmpStemx;
        _tmpStemx = _cursor.getInt(_cursorIndexOfStemx);
        _item.setStemx(_tmpStemx);
        final int _tmpStemy;
        _tmpStemy = _cursor.getInt(_cursorIndexOfStemy);
        _item.setStemy(_tmpStemy);
        final String _tmpChalkType;
        if (_cursor.isNull(_cursorIndexOfChalkType)) {
          _tmpChalkType = null;
        } else {
          _tmpChalkType = _cursor.getString(_cursorIndexOfChalkType);
        }
        _item.setChalkType(_tmpChalkType);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
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
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item.setStreetNumber(_tmpStreetNumber);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item.setDirection(_tmpDirection);
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
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_2);
        _item.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _item.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsExpired;
        if (_cursor.isNull(_cursorIndexOfIsExpired)) {
          _tmpIsExpired = null;
        } else {
          _tmpIsExpired = _cursor.getString(_cursorIndexOfIsExpired);
        }
        _item.setIsExpired(_tmpIsExpired);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpWheelSize;
        _tmpWheelSize = _cursor.getInt(_cursorIndexOfWheelSize);
        _item.setWheelSize(_tmpWheelSize);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _item.setNotes(_tmpNotes);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpMake;
        if (_cursor.isNull(_cursorIndexOfMake)) {
          _tmpMake = null;
        } else {
          _tmpMake = _cursor.getString(_cursorIndexOfMake);
        }
        _item.setMake(_tmpMake);
        final String _tmpColor;
        if (_cursor.isNull(_cursorIndexOfColor)) {
          _tmpColor = null;
        } else {
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
        }
        _item.setColor(_tmpColor);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _item.setSyncStatus(_tmpSyncStatus);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ChalkVehicle> getPendingOldChalkedVehicle() {
    final String _sql = "select * from chalk_vehicles where sync_status IS NULL order by chalk_date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfChalkDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_date");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfStateId = CursorUtil.getColumnIndexOrThrow(_cursor, "state_id");
      final int _cursorIndexOfZoneId = CursorUtil.getColumnIndexOrThrow(_cursor, "zone_id");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "expiration");
      final int _cursorIndexOfDurationId = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_id");
      final int _cursorIndexOfDurationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_code");
      final int _cursorIndexOfSpace = CursorUtil.getColumnIndexOrThrow(_cursor, "space");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfTire = CursorUtil.getColumnIndexOrThrow(_cursor, "tire");
      final int _cursorIndexOfChalkx = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkx");
      final int _cursorIndexOfChalky = CursorUtil.getColumnIndexOrThrow(_cursor, "chalky");
      final int _cursorIndexOfStemx = CursorUtil.getColumnIndexOrThrow(_cursor, "stemx");
      final int _cursorIndexOfStemy = CursorUtil.getColumnIndexOrThrow(_cursor, "stemy");
      final int _cursorIndexOfChalkType = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_type");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfStreetPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_prefix");
      final int _cursorIndexOfStreetSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_suffix");
      final int _cursorIndexOfStreetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "street_number");
      final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfGpstime = CursorUtil.getColumnIndexOrThrow(_cursor, "gpstime");
      final int _cursorIndexOfIsGPSLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "is_gps_location");
      final int _cursorIndexOfIsExpired = CursorUtil.getColumnIndexOrThrow(_cursor, "is_expired");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfWheelSize = CursorUtil.getColumnIndexOrThrow(_cursor, "wheel_size");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfMake = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final List<ChalkVehicle> _result = new ArrayList<ChalkVehicle>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ChalkVehicle _item;
        _item = new ChalkVehicle();
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final Date _tmpChalkDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfChalkDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfChalkDate);
        }
        _tmpChalkDate = Converters.fromTimestamp(_tmp);
        _item.setChalkDate(_tmpChalkDate);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _item.setStateId(_tmpStateId);
        final int _tmpZoneId;
        _tmpZoneId = _cursor.getInt(_cursorIndexOfZoneId);
        _item.setZoneId(_tmpZoneId);
        final Date _tmpExpiration;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfExpiration);
        }
        _tmpExpiration = Converters.fromTimestamp(_tmp_1);
        _item.setExpiration(_tmpExpiration);
        final int _tmpDurationId;
        _tmpDurationId = _cursor.getInt(_cursorIndexOfDurationId);
        _item.setDurationId(_tmpDurationId);
        final String _tmpDurationCode;
        if (_cursor.isNull(_cursorIndexOfDurationCode)) {
          _tmpDurationCode = null;
        } else {
          _tmpDurationCode = _cursor.getString(_cursorIndexOfDurationCode);
        }
        _item.setDurationCode(_tmpDurationCode);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _item.setSpace(_tmpSpace);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final String _tmpTire;
        if (_cursor.isNull(_cursorIndexOfTire)) {
          _tmpTire = null;
        } else {
          _tmpTire = _cursor.getString(_cursorIndexOfTire);
        }
        _item.setTire(_tmpTire);
        final int _tmpChalkx;
        _tmpChalkx = _cursor.getInt(_cursorIndexOfChalkx);
        _item.setChalkx(_tmpChalkx);
        final int _tmpChalky;
        _tmpChalky = _cursor.getInt(_cursorIndexOfChalky);
        _item.setChalky(_tmpChalky);
        final int _tmpStemx;
        _tmpStemx = _cursor.getInt(_cursorIndexOfStemx);
        _item.setStemx(_tmpStemx);
        final int _tmpStemy;
        _tmpStemy = _cursor.getInt(_cursorIndexOfStemy);
        _item.setStemy(_tmpStemy);
        final String _tmpChalkType;
        if (_cursor.isNull(_cursorIndexOfChalkType)) {
          _tmpChalkType = null;
        } else {
          _tmpChalkType = _cursor.getString(_cursorIndexOfChalkType);
        }
        _item.setChalkType(_tmpChalkType);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
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
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item.setStreetNumber(_tmpStreetNumber);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item.setDirection(_tmpDirection);
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
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_2);
        _item.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _item.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsExpired;
        if (_cursor.isNull(_cursorIndexOfIsExpired)) {
          _tmpIsExpired = null;
        } else {
          _tmpIsExpired = _cursor.getString(_cursorIndexOfIsExpired);
        }
        _item.setIsExpired(_tmpIsExpired);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpWheelSize;
        _tmpWheelSize = _cursor.getInt(_cursorIndexOfWheelSize);
        _item.setWheelSize(_tmpWheelSize);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _item.setNotes(_tmpNotes);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpMake;
        if (_cursor.isNull(_cursorIndexOfMake)) {
          _tmpMake = null;
        } else {
          _tmpMake = _cursor.getString(_cursorIndexOfMake);
        }
        _item.setMake(_tmpMake);
        final String _tmpColor;
        if (_cursor.isNull(_cursorIndexOfColor)) {
          _tmpColor = null;
        } else {
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
        }
        _item.setColor(_tmpColor);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _item.setSyncStatus(_tmpSyncStatus);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ChalkVehicle> getPendingPIChalkedVehicle() {
    final String _sql = "select * from chalk_vehicles where sync_status='PI' order by chalk_date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfChalkDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_date");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfStateId = CursorUtil.getColumnIndexOrThrow(_cursor, "state_id");
      final int _cursorIndexOfZoneId = CursorUtil.getColumnIndexOrThrow(_cursor, "zone_id");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "expiration");
      final int _cursorIndexOfDurationId = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_id");
      final int _cursorIndexOfDurationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_code");
      final int _cursorIndexOfSpace = CursorUtil.getColumnIndexOrThrow(_cursor, "space");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfTire = CursorUtil.getColumnIndexOrThrow(_cursor, "tire");
      final int _cursorIndexOfChalkx = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkx");
      final int _cursorIndexOfChalky = CursorUtil.getColumnIndexOrThrow(_cursor, "chalky");
      final int _cursorIndexOfStemx = CursorUtil.getColumnIndexOrThrow(_cursor, "stemx");
      final int _cursorIndexOfStemy = CursorUtil.getColumnIndexOrThrow(_cursor, "stemy");
      final int _cursorIndexOfChalkType = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_type");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfStreetPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_prefix");
      final int _cursorIndexOfStreetSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_suffix");
      final int _cursorIndexOfStreetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "street_number");
      final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfGpstime = CursorUtil.getColumnIndexOrThrow(_cursor, "gpstime");
      final int _cursorIndexOfIsGPSLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "is_gps_location");
      final int _cursorIndexOfIsExpired = CursorUtil.getColumnIndexOrThrow(_cursor, "is_expired");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfWheelSize = CursorUtil.getColumnIndexOrThrow(_cursor, "wheel_size");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfMake = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final List<ChalkVehicle> _result = new ArrayList<ChalkVehicle>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ChalkVehicle _item;
        _item = new ChalkVehicle();
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final Date _tmpChalkDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfChalkDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfChalkDate);
        }
        _tmpChalkDate = Converters.fromTimestamp(_tmp);
        _item.setChalkDate(_tmpChalkDate);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _item.setStateId(_tmpStateId);
        final int _tmpZoneId;
        _tmpZoneId = _cursor.getInt(_cursorIndexOfZoneId);
        _item.setZoneId(_tmpZoneId);
        final Date _tmpExpiration;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfExpiration);
        }
        _tmpExpiration = Converters.fromTimestamp(_tmp_1);
        _item.setExpiration(_tmpExpiration);
        final int _tmpDurationId;
        _tmpDurationId = _cursor.getInt(_cursorIndexOfDurationId);
        _item.setDurationId(_tmpDurationId);
        final String _tmpDurationCode;
        if (_cursor.isNull(_cursorIndexOfDurationCode)) {
          _tmpDurationCode = null;
        } else {
          _tmpDurationCode = _cursor.getString(_cursorIndexOfDurationCode);
        }
        _item.setDurationCode(_tmpDurationCode);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _item.setSpace(_tmpSpace);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final String _tmpTire;
        if (_cursor.isNull(_cursorIndexOfTire)) {
          _tmpTire = null;
        } else {
          _tmpTire = _cursor.getString(_cursorIndexOfTire);
        }
        _item.setTire(_tmpTire);
        final int _tmpChalkx;
        _tmpChalkx = _cursor.getInt(_cursorIndexOfChalkx);
        _item.setChalkx(_tmpChalkx);
        final int _tmpChalky;
        _tmpChalky = _cursor.getInt(_cursorIndexOfChalky);
        _item.setChalky(_tmpChalky);
        final int _tmpStemx;
        _tmpStemx = _cursor.getInt(_cursorIndexOfStemx);
        _item.setStemx(_tmpStemx);
        final int _tmpStemy;
        _tmpStemy = _cursor.getInt(_cursorIndexOfStemy);
        _item.setStemy(_tmpStemy);
        final String _tmpChalkType;
        if (_cursor.isNull(_cursorIndexOfChalkType)) {
          _tmpChalkType = null;
        } else {
          _tmpChalkType = _cursor.getString(_cursorIndexOfChalkType);
        }
        _item.setChalkType(_tmpChalkType);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
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
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item.setStreetNumber(_tmpStreetNumber);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item.setDirection(_tmpDirection);
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
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_2);
        _item.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _item.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsExpired;
        if (_cursor.isNull(_cursorIndexOfIsExpired)) {
          _tmpIsExpired = null;
        } else {
          _tmpIsExpired = _cursor.getString(_cursorIndexOfIsExpired);
        }
        _item.setIsExpired(_tmpIsExpired);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpWheelSize;
        _tmpWheelSize = _cursor.getInt(_cursorIndexOfWheelSize);
        _item.setWheelSize(_tmpWheelSize);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _item.setNotes(_tmpNotes);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpMake;
        if (_cursor.isNull(_cursorIndexOfMake)) {
          _tmpMake = null;
        } else {
          _tmpMake = _cursor.getString(_cursorIndexOfMake);
        }
        _item.setMake(_tmpMake);
        final String _tmpColor;
        if (_cursor.isNull(_cursorIndexOfColor)) {
          _tmpColor = null;
        } else {
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
        }
        _item.setColor(_tmpColor);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _item.setSyncStatus(_tmpSyncStatus);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ChalkVehicle> getChalkByType(final String type) {
    final String _sql = "select * from chalk_vehicles where chalk_type=? order by chalk_date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfChalkDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_date");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfStateId = CursorUtil.getColumnIndexOrThrow(_cursor, "state_id");
      final int _cursorIndexOfZoneId = CursorUtil.getColumnIndexOrThrow(_cursor, "zone_id");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "expiration");
      final int _cursorIndexOfDurationId = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_id");
      final int _cursorIndexOfDurationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_code");
      final int _cursorIndexOfSpace = CursorUtil.getColumnIndexOrThrow(_cursor, "space");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfTire = CursorUtil.getColumnIndexOrThrow(_cursor, "tire");
      final int _cursorIndexOfChalkx = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkx");
      final int _cursorIndexOfChalky = CursorUtil.getColumnIndexOrThrow(_cursor, "chalky");
      final int _cursorIndexOfStemx = CursorUtil.getColumnIndexOrThrow(_cursor, "stemx");
      final int _cursorIndexOfStemy = CursorUtil.getColumnIndexOrThrow(_cursor, "stemy");
      final int _cursorIndexOfChalkType = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_type");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfStreetPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_prefix");
      final int _cursorIndexOfStreetSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_suffix");
      final int _cursorIndexOfStreetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "street_number");
      final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfGpstime = CursorUtil.getColumnIndexOrThrow(_cursor, "gpstime");
      final int _cursorIndexOfIsGPSLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "is_gps_location");
      final int _cursorIndexOfIsExpired = CursorUtil.getColumnIndexOrThrow(_cursor, "is_expired");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfWheelSize = CursorUtil.getColumnIndexOrThrow(_cursor, "wheel_size");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfMake = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final List<ChalkVehicle> _result = new ArrayList<ChalkVehicle>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ChalkVehicle _item;
        _item = new ChalkVehicle();
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final Date _tmpChalkDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfChalkDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfChalkDate);
        }
        _tmpChalkDate = Converters.fromTimestamp(_tmp);
        _item.setChalkDate(_tmpChalkDate);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _item.setStateId(_tmpStateId);
        final int _tmpZoneId;
        _tmpZoneId = _cursor.getInt(_cursorIndexOfZoneId);
        _item.setZoneId(_tmpZoneId);
        final Date _tmpExpiration;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfExpiration);
        }
        _tmpExpiration = Converters.fromTimestamp(_tmp_1);
        _item.setExpiration(_tmpExpiration);
        final int _tmpDurationId;
        _tmpDurationId = _cursor.getInt(_cursorIndexOfDurationId);
        _item.setDurationId(_tmpDurationId);
        final String _tmpDurationCode;
        if (_cursor.isNull(_cursorIndexOfDurationCode)) {
          _tmpDurationCode = null;
        } else {
          _tmpDurationCode = _cursor.getString(_cursorIndexOfDurationCode);
        }
        _item.setDurationCode(_tmpDurationCode);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _item.setSpace(_tmpSpace);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final String _tmpTire;
        if (_cursor.isNull(_cursorIndexOfTire)) {
          _tmpTire = null;
        } else {
          _tmpTire = _cursor.getString(_cursorIndexOfTire);
        }
        _item.setTire(_tmpTire);
        final int _tmpChalkx;
        _tmpChalkx = _cursor.getInt(_cursorIndexOfChalkx);
        _item.setChalkx(_tmpChalkx);
        final int _tmpChalky;
        _tmpChalky = _cursor.getInt(_cursorIndexOfChalky);
        _item.setChalky(_tmpChalky);
        final int _tmpStemx;
        _tmpStemx = _cursor.getInt(_cursorIndexOfStemx);
        _item.setStemx(_tmpStemx);
        final int _tmpStemy;
        _tmpStemy = _cursor.getInt(_cursorIndexOfStemy);
        _item.setStemy(_tmpStemy);
        final String _tmpChalkType;
        if (_cursor.isNull(_cursorIndexOfChalkType)) {
          _tmpChalkType = null;
        } else {
          _tmpChalkType = _cursor.getString(_cursorIndexOfChalkType);
        }
        _item.setChalkType(_tmpChalkType);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
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
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item.setStreetNumber(_tmpStreetNumber);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item.setDirection(_tmpDirection);
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
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_2);
        _item.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _item.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsExpired;
        if (_cursor.isNull(_cursorIndexOfIsExpired)) {
          _tmpIsExpired = null;
        } else {
          _tmpIsExpired = _cursor.getString(_cursorIndexOfIsExpired);
        }
        _item.setIsExpired(_tmpIsExpired);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpWheelSize;
        _tmpWheelSize = _cursor.getInt(_cursorIndexOfWheelSize);
        _item.setWheelSize(_tmpWheelSize);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _item.setNotes(_tmpNotes);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpMake;
        if (_cursor.isNull(_cursorIndexOfMake)) {
          _tmpMake = null;
        } else {
          _tmpMake = _cursor.getString(_cursorIndexOfMake);
        }
        _item.setMake(_tmpMake);
        final String _tmpColor;
        if (_cursor.isNull(_cursorIndexOfColor)) {
          _tmpColor = null;
        } else {
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
        }
        _item.setColor(_tmpColor);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _item.setSyncStatus(_tmpSyncStatus);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public ChalkVehicle getChalkVehicleByPrimaryKey(final long chalkId) {
    final String _sql = "select * from chalk_vehicles where chalk_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chalkId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfChalkDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_date");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfStateId = CursorUtil.getColumnIndexOrThrow(_cursor, "state_id");
      final int _cursorIndexOfZoneId = CursorUtil.getColumnIndexOrThrow(_cursor, "zone_id");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "expiration");
      final int _cursorIndexOfDurationId = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_id");
      final int _cursorIndexOfDurationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_code");
      final int _cursorIndexOfSpace = CursorUtil.getColumnIndexOrThrow(_cursor, "space");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfTire = CursorUtil.getColumnIndexOrThrow(_cursor, "tire");
      final int _cursorIndexOfChalkx = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkx");
      final int _cursorIndexOfChalky = CursorUtil.getColumnIndexOrThrow(_cursor, "chalky");
      final int _cursorIndexOfStemx = CursorUtil.getColumnIndexOrThrow(_cursor, "stemx");
      final int _cursorIndexOfStemy = CursorUtil.getColumnIndexOrThrow(_cursor, "stemy");
      final int _cursorIndexOfChalkType = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_type");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfStreetPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_prefix");
      final int _cursorIndexOfStreetSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_suffix");
      final int _cursorIndexOfStreetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "street_number");
      final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfGpstime = CursorUtil.getColumnIndexOrThrow(_cursor, "gpstime");
      final int _cursorIndexOfIsGPSLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "is_gps_location");
      final int _cursorIndexOfIsExpired = CursorUtil.getColumnIndexOrThrow(_cursor, "is_expired");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfWheelSize = CursorUtil.getColumnIndexOrThrow(_cursor, "wheel_size");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfMake = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final ChalkVehicle _result;
      if(_cursor.moveToFirst()) {
        _result = new ChalkVehicle();
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _result.setChalkId(_tmpChalkId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _result.setDeviceId(_tmpDeviceId);
        final Date _tmpChalkDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfChalkDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfChalkDate);
        }
        _tmpChalkDate = Converters.fromTimestamp(_tmp);
        _result.setChalkDate(_tmpChalkDate);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _result.setPermit(_tmpPermit);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _result.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _result.setVin(_tmpVin);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _result.setStateId(_tmpStateId);
        final int _tmpZoneId;
        _tmpZoneId = _cursor.getInt(_cursorIndexOfZoneId);
        _result.setZoneId(_tmpZoneId);
        final Date _tmpExpiration;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfExpiration);
        }
        _tmpExpiration = Converters.fromTimestamp(_tmp_1);
        _result.setExpiration(_tmpExpiration);
        final int _tmpDurationId;
        _tmpDurationId = _cursor.getInt(_cursorIndexOfDurationId);
        _result.setDurationId(_tmpDurationId);
        final String _tmpDurationCode;
        if (_cursor.isNull(_cursorIndexOfDurationCode)) {
          _tmpDurationCode = null;
        } else {
          _tmpDurationCode = _cursor.getString(_cursorIndexOfDurationCode);
        }
        _result.setDurationCode(_tmpDurationCode);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _result.setSpace(_tmpSpace);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _result.setMeter(_tmpMeter);
        final String _tmpTire;
        if (_cursor.isNull(_cursorIndexOfTire)) {
          _tmpTire = null;
        } else {
          _tmpTire = _cursor.getString(_cursorIndexOfTire);
        }
        _result.setTire(_tmpTire);
        final int _tmpChalkx;
        _tmpChalkx = _cursor.getInt(_cursorIndexOfChalkx);
        _result.setChalkx(_tmpChalkx);
        final int _tmpChalky;
        _tmpChalky = _cursor.getInt(_cursorIndexOfChalky);
        _result.setChalky(_tmpChalky);
        final int _tmpStemx;
        _tmpStemx = _cursor.getInt(_cursorIndexOfStemx);
        _result.setStemx(_tmpStemx);
        final int _tmpStemy;
        _tmpStemy = _cursor.getInt(_cursorIndexOfStemy);
        _result.setStemy(_tmpStemy);
        final String _tmpChalkType;
        if (_cursor.isNull(_cursorIndexOfChalkType)) {
          _tmpChalkType = null;
        } else {
          _tmpChalkType = _cursor.getString(_cursorIndexOfChalkType);
        }
        _result.setChalkType(_tmpChalkType);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _result.setLocation(_tmpLocation);
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
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _result.setStreetNumber(_tmpStreetNumber);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _result.setDirection(_tmpDirection);
        final String _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        }
        _result.setLatitude(_tmpLatitude);
        final String _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        }
        _result.setLongitude(_tmpLongitude);
        final Date _tmpGpstime;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_2);
        _result.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _result.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsExpired;
        if (_cursor.isNull(_cursorIndexOfIsExpired)) {
          _tmpIsExpired = null;
        } else {
          _tmpIsExpired = _cursor.getString(_cursorIndexOfIsExpired);
        }
        _result.setIsExpired(_tmpIsExpired);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final int _tmpWheelSize;
        _tmpWheelSize = _cursor.getInt(_cursorIndexOfWheelSize);
        _result.setWheelSize(_tmpWheelSize);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _result.setNotes(_tmpNotes);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _result.setMakeCode(_tmpMakeCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _result.setColorCode(_tmpColorCode);
        final String _tmpMake;
        if (_cursor.isNull(_cursorIndexOfMake)) {
          _tmpMake = null;
        } else {
          _tmpMake = _cursor.getString(_cursorIndexOfMake);
        }
        _result.setMake(_tmpMake);
        final String _tmpColor;
        if (_cursor.isNull(_cursorIndexOfColor)) {
          _tmpColor = null;
        } else {
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
        }
        _result.setColor(_tmpColor);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _result.setSyncStatus(_tmpSyncStatus);
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
  public ChalkVehicle getChalkVehicleById(final long chalkId) {
    final String _sql = "select * from chalk_vehicles where chalk_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chalkId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfChalkDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_date");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfStateId = CursorUtil.getColumnIndexOrThrow(_cursor, "state_id");
      final int _cursorIndexOfZoneId = CursorUtil.getColumnIndexOrThrow(_cursor, "zone_id");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "expiration");
      final int _cursorIndexOfDurationId = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_id");
      final int _cursorIndexOfDurationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_code");
      final int _cursorIndexOfSpace = CursorUtil.getColumnIndexOrThrow(_cursor, "space");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfTire = CursorUtil.getColumnIndexOrThrow(_cursor, "tire");
      final int _cursorIndexOfChalkx = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkx");
      final int _cursorIndexOfChalky = CursorUtil.getColumnIndexOrThrow(_cursor, "chalky");
      final int _cursorIndexOfStemx = CursorUtil.getColumnIndexOrThrow(_cursor, "stemx");
      final int _cursorIndexOfStemy = CursorUtil.getColumnIndexOrThrow(_cursor, "stemy");
      final int _cursorIndexOfChalkType = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_type");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfStreetPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_prefix");
      final int _cursorIndexOfStreetSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_suffix");
      final int _cursorIndexOfStreetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "street_number");
      final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfGpstime = CursorUtil.getColumnIndexOrThrow(_cursor, "gpstime");
      final int _cursorIndexOfIsGPSLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "is_gps_location");
      final int _cursorIndexOfIsExpired = CursorUtil.getColumnIndexOrThrow(_cursor, "is_expired");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfWheelSize = CursorUtil.getColumnIndexOrThrow(_cursor, "wheel_size");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfMake = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final ChalkVehicle _result;
      if(_cursor.moveToFirst()) {
        _result = new ChalkVehicle();
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _result.setChalkId(_tmpChalkId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _result.setDeviceId(_tmpDeviceId);
        final Date _tmpChalkDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfChalkDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfChalkDate);
        }
        _tmpChalkDate = Converters.fromTimestamp(_tmp);
        _result.setChalkDate(_tmpChalkDate);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _result.setPermit(_tmpPermit);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _result.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _result.setVin(_tmpVin);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _result.setStateId(_tmpStateId);
        final int _tmpZoneId;
        _tmpZoneId = _cursor.getInt(_cursorIndexOfZoneId);
        _result.setZoneId(_tmpZoneId);
        final Date _tmpExpiration;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfExpiration);
        }
        _tmpExpiration = Converters.fromTimestamp(_tmp_1);
        _result.setExpiration(_tmpExpiration);
        final int _tmpDurationId;
        _tmpDurationId = _cursor.getInt(_cursorIndexOfDurationId);
        _result.setDurationId(_tmpDurationId);
        final String _tmpDurationCode;
        if (_cursor.isNull(_cursorIndexOfDurationCode)) {
          _tmpDurationCode = null;
        } else {
          _tmpDurationCode = _cursor.getString(_cursorIndexOfDurationCode);
        }
        _result.setDurationCode(_tmpDurationCode);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _result.setSpace(_tmpSpace);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _result.setMeter(_tmpMeter);
        final String _tmpTire;
        if (_cursor.isNull(_cursorIndexOfTire)) {
          _tmpTire = null;
        } else {
          _tmpTire = _cursor.getString(_cursorIndexOfTire);
        }
        _result.setTire(_tmpTire);
        final int _tmpChalkx;
        _tmpChalkx = _cursor.getInt(_cursorIndexOfChalkx);
        _result.setChalkx(_tmpChalkx);
        final int _tmpChalky;
        _tmpChalky = _cursor.getInt(_cursorIndexOfChalky);
        _result.setChalky(_tmpChalky);
        final int _tmpStemx;
        _tmpStemx = _cursor.getInt(_cursorIndexOfStemx);
        _result.setStemx(_tmpStemx);
        final int _tmpStemy;
        _tmpStemy = _cursor.getInt(_cursorIndexOfStemy);
        _result.setStemy(_tmpStemy);
        final String _tmpChalkType;
        if (_cursor.isNull(_cursorIndexOfChalkType)) {
          _tmpChalkType = null;
        } else {
          _tmpChalkType = _cursor.getString(_cursorIndexOfChalkType);
        }
        _result.setChalkType(_tmpChalkType);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _result.setLocation(_tmpLocation);
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
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _result.setStreetNumber(_tmpStreetNumber);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _result.setDirection(_tmpDirection);
        final String _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        }
        _result.setLatitude(_tmpLatitude);
        final String _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        }
        _result.setLongitude(_tmpLongitude);
        final Date _tmpGpstime;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_2);
        _result.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _result.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsExpired;
        if (_cursor.isNull(_cursorIndexOfIsExpired)) {
          _tmpIsExpired = null;
        } else {
          _tmpIsExpired = _cursor.getString(_cursorIndexOfIsExpired);
        }
        _result.setIsExpired(_tmpIsExpired);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final int _tmpWheelSize;
        _tmpWheelSize = _cursor.getInt(_cursorIndexOfWheelSize);
        _result.setWheelSize(_tmpWheelSize);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _result.setNotes(_tmpNotes);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _result.setMakeCode(_tmpMakeCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _result.setColorCode(_tmpColorCode);
        final String _tmpMake;
        if (_cursor.isNull(_cursorIndexOfMake)) {
          _tmpMake = null;
        } else {
          _tmpMake = _cursor.getString(_cursorIndexOfMake);
        }
        _result.setMake(_tmpMake);
        final String _tmpColor;
        if (_cursor.isNull(_cursorIndexOfColor)) {
          _tmpColor = null;
        } else {
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
        }
        _result.setColor(_tmpColor);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _result.setSyncStatus(_tmpSyncStatus);
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
  public ChalkVehicle searchPreviousChalkByPlate(final String plate, final String state) {
    final String _sql = "select * from chalk_vehicles where plate=? and state_id in (select state_id from states where code=?) order by chalk_date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (plate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, plate);
    }
    _argIndex = 2;
    if (state == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, state);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfChalkDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_date");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfStateId = CursorUtil.getColumnIndexOrThrow(_cursor, "state_id");
      final int _cursorIndexOfZoneId = CursorUtil.getColumnIndexOrThrow(_cursor, "zone_id");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "expiration");
      final int _cursorIndexOfDurationId = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_id");
      final int _cursorIndexOfDurationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_code");
      final int _cursorIndexOfSpace = CursorUtil.getColumnIndexOrThrow(_cursor, "space");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfTire = CursorUtil.getColumnIndexOrThrow(_cursor, "tire");
      final int _cursorIndexOfChalkx = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkx");
      final int _cursorIndexOfChalky = CursorUtil.getColumnIndexOrThrow(_cursor, "chalky");
      final int _cursorIndexOfStemx = CursorUtil.getColumnIndexOrThrow(_cursor, "stemx");
      final int _cursorIndexOfStemy = CursorUtil.getColumnIndexOrThrow(_cursor, "stemy");
      final int _cursorIndexOfChalkType = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_type");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfStreetPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_prefix");
      final int _cursorIndexOfStreetSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_suffix");
      final int _cursorIndexOfStreetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "street_number");
      final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfGpstime = CursorUtil.getColumnIndexOrThrow(_cursor, "gpstime");
      final int _cursorIndexOfIsGPSLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "is_gps_location");
      final int _cursorIndexOfIsExpired = CursorUtil.getColumnIndexOrThrow(_cursor, "is_expired");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfWheelSize = CursorUtil.getColumnIndexOrThrow(_cursor, "wheel_size");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfMake = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final ChalkVehicle _result;
      if(_cursor.moveToFirst()) {
        _result = new ChalkVehicle();
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _result.setChalkId(_tmpChalkId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _result.setDeviceId(_tmpDeviceId);
        final Date _tmpChalkDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfChalkDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfChalkDate);
        }
        _tmpChalkDate = Converters.fromTimestamp(_tmp);
        _result.setChalkDate(_tmpChalkDate);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _result.setPermit(_tmpPermit);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _result.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _result.setVin(_tmpVin);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _result.setStateId(_tmpStateId);
        final int _tmpZoneId;
        _tmpZoneId = _cursor.getInt(_cursorIndexOfZoneId);
        _result.setZoneId(_tmpZoneId);
        final Date _tmpExpiration;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfExpiration);
        }
        _tmpExpiration = Converters.fromTimestamp(_tmp_1);
        _result.setExpiration(_tmpExpiration);
        final int _tmpDurationId;
        _tmpDurationId = _cursor.getInt(_cursorIndexOfDurationId);
        _result.setDurationId(_tmpDurationId);
        final String _tmpDurationCode;
        if (_cursor.isNull(_cursorIndexOfDurationCode)) {
          _tmpDurationCode = null;
        } else {
          _tmpDurationCode = _cursor.getString(_cursorIndexOfDurationCode);
        }
        _result.setDurationCode(_tmpDurationCode);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _result.setSpace(_tmpSpace);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _result.setMeter(_tmpMeter);
        final String _tmpTire;
        if (_cursor.isNull(_cursorIndexOfTire)) {
          _tmpTire = null;
        } else {
          _tmpTire = _cursor.getString(_cursorIndexOfTire);
        }
        _result.setTire(_tmpTire);
        final int _tmpChalkx;
        _tmpChalkx = _cursor.getInt(_cursorIndexOfChalkx);
        _result.setChalkx(_tmpChalkx);
        final int _tmpChalky;
        _tmpChalky = _cursor.getInt(_cursorIndexOfChalky);
        _result.setChalky(_tmpChalky);
        final int _tmpStemx;
        _tmpStemx = _cursor.getInt(_cursorIndexOfStemx);
        _result.setStemx(_tmpStemx);
        final int _tmpStemy;
        _tmpStemy = _cursor.getInt(_cursorIndexOfStemy);
        _result.setStemy(_tmpStemy);
        final String _tmpChalkType;
        if (_cursor.isNull(_cursorIndexOfChalkType)) {
          _tmpChalkType = null;
        } else {
          _tmpChalkType = _cursor.getString(_cursorIndexOfChalkType);
        }
        _result.setChalkType(_tmpChalkType);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _result.setLocation(_tmpLocation);
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
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _result.setStreetNumber(_tmpStreetNumber);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _result.setDirection(_tmpDirection);
        final String _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        }
        _result.setLatitude(_tmpLatitude);
        final String _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        }
        _result.setLongitude(_tmpLongitude);
        final Date _tmpGpstime;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_2);
        _result.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _result.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsExpired;
        if (_cursor.isNull(_cursorIndexOfIsExpired)) {
          _tmpIsExpired = null;
        } else {
          _tmpIsExpired = _cursor.getString(_cursorIndexOfIsExpired);
        }
        _result.setIsExpired(_tmpIsExpired);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final int _tmpWheelSize;
        _tmpWheelSize = _cursor.getInt(_cursorIndexOfWheelSize);
        _result.setWheelSize(_tmpWheelSize);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _result.setNotes(_tmpNotes);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _result.setMakeCode(_tmpMakeCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _result.setColorCode(_tmpColorCode);
        final String _tmpMake;
        if (_cursor.isNull(_cursorIndexOfMake)) {
          _tmpMake = null;
        } else {
          _tmpMake = _cursor.getString(_cursorIndexOfMake);
        }
        _result.setMake(_tmpMake);
        final String _tmpColor;
        if (_cursor.isNull(_cursorIndexOfColor)) {
          _tmpColor = null;
        } else {
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
        }
        _result.setColor(_tmpColor);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _result.setSyncStatus(_tmpSyncStatus);
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
