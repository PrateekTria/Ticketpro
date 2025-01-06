package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.ALPRChalk;
import com.ticketpro.util.Converters;
import io.reactivex.Completable;
import io.reactivex.Maybe;
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
public final class ALPRPhotoChalkDao_Impl implements ALPRPhotoChalkDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ALPRChalk> __insertionAdapterOfALPRChalk;

  private final SharedSQLiteStatement __preparedStmtOfRemoveChalkById;

  private final SharedSQLiteStatement __preparedStmtOfUpdateChalk;

  private final SharedSQLiteStatement __preparedStmtOfUpdateChalkExpired;

  public ALPRPhotoChalkDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfALPRChalk = new EntityInsertionAdapter<ALPRChalk>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `ALPRPhotoChalk` (`Plate`,`Details`,`DataField1`,`DataField2`,`DataField3`,`Confidence`,`FirstDate`,`FirstTime`,`FirstDateTime`,`FirstParkingBay`,`FirstLocLat`,`FirstLocLong`,`FirstLocAcc`,`LastDate`,`LastTime`,`LastDateTime`,`LastParkingBay`,`LastLocLat`,`LastLocLong`,`LastLocAcc`,`PermitExpiryDate`,`PermitExpiryTime`,`chalkDuration`,`duration_code`,`chalkLocation`,`chalkTire`,`chalkId`,`userid`,`deviceId`,`custId`,`is_expired`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ALPRChalk value) {
        if (value.getPlate() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getPlate());
        }
        if (value.getDetails() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDetails());
        }
        if (value.getCustomData1() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getCustomData1());
        }
        if (value.getCustomData2() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getCustomData2());
        }
        if (value.getCustomData3() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getCustomData3());
        }
        if (value.getConfidence() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getConfidence());
        }
        if (value.getFirstDate() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getFirstDate());
        }
        if (value.getFirstTime() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getFirstTime());
        }
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getFirstDateTime());
        if (_tmp == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindLong(9, _tmp);
        }
        if (value.getFirstParkingBay() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getFirstParkingBay());
        }
        if (value.getFirstLocLat() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getFirstLocLat());
        }
        if (value.getFirstLocLong() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getFirstLocLong());
        }
        if (value.getFirstLocAcc() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getFirstLocAcc());
        }
        if (value.getLastDate() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getLastDate());
        }
        if (value.getLastTime() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getLastTime());
        }
        final Long _tmp_1;
        _tmp_1 = Converters.dateToTimestamp(value.getLastDateTime());
        if (_tmp_1 == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindLong(16, _tmp_1);
        }
        if (value.getLastParkingBay() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindString(17, value.getLastParkingBay());
        }
        if (value.getLastLocLat() == null) {
          stmt.bindNull(18);
        } else {
          stmt.bindString(18, value.getLastLocLat());
        }
        if (value.getLastLocLong() == null) {
          stmt.bindNull(19);
        } else {
          stmt.bindString(19, value.getLastLocLong());
        }
        if (value.getLastLocAcc() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.getLastLocAcc());
        }
        if (value.getPermitExpiryDate() == null) {
          stmt.bindNull(21);
        } else {
          stmt.bindString(21, value.getPermitExpiryDate());
        }
        if (value.getPermitExpiryTime() == null) {
          stmt.bindNull(22);
        } else {
          stmt.bindString(22, value.getPermitExpiryTime());
        }
        stmt.bindLong(23, value.getChalkDurationId());
        if (value.getChalkDurationCode() == null) {
          stmt.bindNull(24);
        } else {
          stmt.bindString(24, value.getChalkDurationCode());
        }
        if (value.getChalkLocation() == null) {
          stmt.bindNull(25);
        } else {
          stmt.bindString(25, value.getChalkLocation());
        }
        if (value.getChalkTire() == null) {
          stmt.bindNull(26);
        } else {
          stmt.bindString(26, value.getChalkTire());
        }
        stmt.bindLong(27, value.getChalkId());
        stmt.bindLong(28, value.getUserid());
        stmt.bindLong(29, value.getDeviceId());
        stmt.bindLong(30, value.getCustId());
        if (value.getIsExpired() == null) {
          stmt.bindNull(31);
        } else {
          stmt.bindString(31, value.getIsExpired());
        }
      }
    };
    this.__preparedStmtOfRemoveChalkById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from ALPRPhotoChalk where chalkId=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateChalk = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update ALPRPhotoChalk set LastDate=?, LastParkingBay=?,LastLocLat=?,LastLocLong=?,LastLocAcc=? where Plate=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateChalkExpired = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update ALPRPhotoChalk set is_expired=? where Plate=?";
        return _query;
      }
    };
  }

  @Override
  public void insertALPRChalk(final ALPRChalk... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfALPRChalk.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertALPRChalk(final ALPRChalk data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfALPRChalk.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertALPRChalkList(final List<ALPRChalk> chalkList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfALPRChalk.insert(chalkList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void removeChalkById(final long chalkId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveChalkById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, chalkId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveChalkById.release(_stmt);
    }
  }

  @Override
  public Completable updateChalk(final String plate, final String lastDate,
      final String lastParkingBay, final String lastLocLat, final String lastLocLong,
      final String lastLocAcc) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateChalk.acquire();
        int _argIndex = 1;
        if (lastDate == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, lastDate);
        }
        _argIndex = 2;
        if (lastParkingBay == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, lastParkingBay);
        }
        _argIndex = 3;
        if (lastLocLat == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, lastLocLat);
        }
        _argIndex = 4;
        if (lastLocLong == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, lastLocLong);
        }
        _argIndex = 5;
        if (lastLocAcc == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, lastLocAcc);
        }
        _argIndex = 6;
        if (plate == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, plate);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdateChalk.release(_stmt);
        }
      }
    });
  }

  @Override
  public Completable updateChalkExpired(final String plate, final String values) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateChalkExpired.acquire();
        int _argIndex = 1;
        if (values == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, values);
        }
        _argIndex = 2;
        if (plate == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, plate);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdateChalkExpired.release(_stmt);
        }
      }
    });
  }

  @Override
  public Maybe<List<ALPRChalk>> getChalkVehicles() {
    final String _sql = "select *,(select duration from durations where duration_id=ALPRPhotoChalk.chalkDuration) as duration_code from ALPRPhotoChalk order by LastDate";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return Maybe.fromCallable(new Callable<List<ALPRChalk>>() {
      @Override
      public List<ALPRChalk> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "Plate");
          final int _cursorIndexOfDetails = CursorUtil.getColumnIndexOrThrow(_cursor, "Details");
          final int _cursorIndexOfCustomData1 = CursorUtil.getColumnIndexOrThrow(_cursor, "DataField1");
          final int _cursorIndexOfCustomData2 = CursorUtil.getColumnIndexOrThrow(_cursor, "DataField2");
          final int _cursorIndexOfCustomData3 = CursorUtil.getColumnIndexOrThrow(_cursor, "DataField3");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "Confidence");
          final int _cursorIndexOfFirstDate = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstDate");
          final int _cursorIndexOfFirstTime = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstTime");
          final int _cursorIndexOfFirstDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstDateTime");
          final int _cursorIndexOfFirstParkingBay = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstParkingBay");
          final int _cursorIndexOfFirstLocLat = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstLocLat");
          final int _cursorIndexOfFirstLocLong = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstLocLong");
          final int _cursorIndexOfFirstLocAcc = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstLocAcc");
          final int _cursorIndexOfLastDate = CursorUtil.getColumnIndexOrThrow(_cursor, "LastDate");
          final int _cursorIndexOfLastTime = CursorUtil.getColumnIndexOrThrow(_cursor, "LastTime");
          final int _cursorIndexOfLastDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "LastDateTime");
          final int _cursorIndexOfLastParkingBay = CursorUtil.getColumnIndexOrThrow(_cursor, "LastParkingBay");
          final int _cursorIndexOfLastLocLat = CursorUtil.getColumnIndexOrThrow(_cursor, "LastLocLat");
          final int _cursorIndexOfLastLocLong = CursorUtil.getColumnIndexOrThrow(_cursor, "LastLocLong");
          final int _cursorIndexOfLastLocAcc = CursorUtil.getColumnIndexOrThrow(_cursor, "LastLocAcc");
          final int _cursorIndexOfPermitExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "PermitExpiryDate");
          final int _cursorIndexOfPermitExpiryTime = CursorUtil.getColumnIndexOrThrow(_cursor, "PermitExpiryTime");
          final int _cursorIndexOfChalkDurationId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkDuration");
          final int _cursorIndexOfChalkDurationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_code");
          final int _cursorIndexOfChalkLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkLocation");
          final int _cursorIndexOfChalkTire = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkTire");
          final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkId");
          final int _cursorIndexOfUserid = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custId");
          final int _cursorIndexOfIsExpired = CursorUtil.getColumnIndexOrThrow(_cursor, "is_expired");
          final int _cursorIndexOfChalkDurationCode_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_code");
          final List<ALPRChalk> _result = new ArrayList<ALPRChalk>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final ALPRChalk _item;
            _item = new ALPRChalk();
            final String _tmpPlate;
            if (_cursor.isNull(_cursorIndexOfPlate)) {
              _tmpPlate = null;
            } else {
              _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
            }
            _item.setPlate(_tmpPlate);
            final String _tmpDetails;
            if (_cursor.isNull(_cursorIndexOfDetails)) {
              _tmpDetails = null;
            } else {
              _tmpDetails = _cursor.getString(_cursorIndexOfDetails);
            }
            _item.setDetails(_tmpDetails);
            final String _tmpCustomData1;
            if (_cursor.isNull(_cursorIndexOfCustomData1)) {
              _tmpCustomData1 = null;
            } else {
              _tmpCustomData1 = _cursor.getString(_cursorIndexOfCustomData1);
            }
            _item.setCustomData1(_tmpCustomData1);
            final String _tmpCustomData2;
            if (_cursor.isNull(_cursorIndexOfCustomData2)) {
              _tmpCustomData2 = null;
            } else {
              _tmpCustomData2 = _cursor.getString(_cursorIndexOfCustomData2);
            }
            _item.setCustomData2(_tmpCustomData2);
            final String _tmpCustomData3;
            if (_cursor.isNull(_cursorIndexOfCustomData3)) {
              _tmpCustomData3 = null;
            } else {
              _tmpCustomData3 = _cursor.getString(_cursorIndexOfCustomData3);
            }
            _item.setCustomData3(_tmpCustomData3);
            final String _tmpConfidence;
            if (_cursor.isNull(_cursorIndexOfConfidence)) {
              _tmpConfidence = null;
            } else {
              _tmpConfidence = _cursor.getString(_cursorIndexOfConfidence);
            }
            _item.setConfidence(_tmpConfidence);
            final String _tmpFirstDate;
            if (_cursor.isNull(_cursorIndexOfFirstDate)) {
              _tmpFirstDate = null;
            } else {
              _tmpFirstDate = _cursor.getString(_cursorIndexOfFirstDate);
            }
            _item.setFirstDate(_tmpFirstDate);
            final String _tmpFirstTime;
            if (_cursor.isNull(_cursorIndexOfFirstTime)) {
              _tmpFirstTime = null;
            } else {
              _tmpFirstTime = _cursor.getString(_cursorIndexOfFirstTime);
            }
            _item.setFirstTime(_tmpFirstTime);
            final Date _tmpFirstDateTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfFirstDateTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfFirstDateTime);
            }
            _tmpFirstDateTime = Converters.fromTimestamp(_tmp);
            _item.setFirstDateTime(_tmpFirstDateTime);
            final String _tmpFirstParkingBay;
            if (_cursor.isNull(_cursorIndexOfFirstParkingBay)) {
              _tmpFirstParkingBay = null;
            } else {
              _tmpFirstParkingBay = _cursor.getString(_cursorIndexOfFirstParkingBay);
            }
            _item.setFirstParkingBay(_tmpFirstParkingBay);
            final String _tmpFirstLocLat;
            if (_cursor.isNull(_cursorIndexOfFirstLocLat)) {
              _tmpFirstLocLat = null;
            } else {
              _tmpFirstLocLat = _cursor.getString(_cursorIndexOfFirstLocLat);
            }
            _item.setFirstLocLat(_tmpFirstLocLat);
            final String _tmpFirstLocLong;
            if (_cursor.isNull(_cursorIndexOfFirstLocLong)) {
              _tmpFirstLocLong = null;
            } else {
              _tmpFirstLocLong = _cursor.getString(_cursorIndexOfFirstLocLong);
            }
            _item.setFirstLocLong(_tmpFirstLocLong);
            final String _tmpFirstLocAcc;
            if (_cursor.isNull(_cursorIndexOfFirstLocAcc)) {
              _tmpFirstLocAcc = null;
            } else {
              _tmpFirstLocAcc = _cursor.getString(_cursorIndexOfFirstLocAcc);
            }
            _item.setFirstLocAcc(_tmpFirstLocAcc);
            final String _tmpLastDate;
            if (_cursor.isNull(_cursorIndexOfLastDate)) {
              _tmpLastDate = null;
            } else {
              _tmpLastDate = _cursor.getString(_cursorIndexOfLastDate);
            }
            _item.setLastDate(_tmpLastDate);
            final String _tmpLastTime;
            if (_cursor.isNull(_cursorIndexOfLastTime)) {
              _tmpLastTime = null;
            } else {
              _tmpLastTime = _cursor.getString(_cursorIndexOfLastTime);
            }
            _item.setLastTime(_tmpLastTime);
            final Date _tmpLastDateTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfLastDateTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfLastDateTime);
            }
            _tmpLastDateTime = Converters.fromTimestamp(_tmp_1);
            _item.setLastDateTime(_tmpLastDateTime);
            final String _tmpLastParkingBay;
            if (_cursor.isNull(_cursorIndexOfLastParkingBay)) {
              _tmpLastParkingBay = null;
            } else {
              _tmpLastParkingBay = _cursor.getString(_cursorIndexOfLastParkingBay);
            }
            _item.setLastParkingBay(_tmpLastParkingBay);
            final String _tmpLastLocLat;
            if (_cursor.isNull(_cursorIndexOfLastLocLat)) {
              _tmpLastLocLat = null;
            } else {
              _tmpLastLocLat = _cursor.getString(_cursorIndexOfLastLocLat);
            }
            _item.setLastLocLat(_tmpLastLocLat);
            final String _tmpLastLocLong;
            if (_cursor.isNull(_cursorIndexOfLastLocLong)) {
              _tmpLastLocLong = null;
            } else {
              _tmpLastLocLong = _cursor.getString(_cursorIndexOfLastLocLong);
            }
            _item.setLastLocLong(_tmpLastLocLong);
            final String _tmpLastLocAcc;
            if (_cursor.isNull(_cursorIndexOfLastLocAcc)) {
              _tmpLastLocAcc = null;
            } else {
              _tmpLastLocAcc = _cursor.getString(_cursorIndexOfLastLocAcc);
            }
            _item.setLastLocAcc(_tmpLastLocAcc);
            final String _tmpPermitExpiryDate;
            if (_cursor.isNull(_cursorIndexOfPermitExpiryDate)) {
              _tmpPermitExpiryDate = null;
            } else {
              _tmpPermitExpiryDate = _cursor.getString(_cursorIndexOfPermitExpiryDate);
            }
            _item.setPermitExpiryDate(_tmpPermitExpiryDate);
            final String _tmpPermitExpiryTime;
            if (_cursor.isNull(_cursorIndexOfPermitExpiryTime)) {
              _tmpPermitExpiryTime = null;
            } else {
              _tmpPermitExpiryTime = _cursor.getString(_cursorIndexOfPermitExpiryTime);
            }
            _item.setPermitExpiryTime(_tmpPermitExpiryTime);
            final int _tmpChalkDurationId;
            _tmpChalkDurationId = _cursor.getInt(_cursorIndexOfChalkDurationId);
            _item.setChalkDurationId(_tmpChalkDurationId);
            final String _tmpChalkDurationCode;
            if (_cursor.isNull(_cursorIndexOfChalkDurationCode)) {
              _tmpChalkDurationCode = null;
            } else {
              _tmpChalkDurationCode = _cursor.getString(_cursorIndexOfChalkDurationCode);
            }
            _item.setChalkDurationCode(_tmpChalkDurationCode);
            final String _tmpChalkLocation;
            if (_cursor.isNull(_cursorIndexOfChalkLocation)) {
              _tmpChalkLocation = null;
            } else {
              _tmpChalkLocation = _cursor.getString(_cursorIndexOfChalkLocation);
            }
            _item.setChalkLocation(_tmpChalkLocation);
            final String _tmpChalkTire;
            if (_cursor.isNull(_cursorIndexOfChalkTire)) {
              _tmpChalkTire = null;
            } else {
              _tmpChalkTire = _cursor.getString(_cursorIndexOfChalkTire);
            }
            _item.setChalkTire(_tmpChalkTire);
            final long _tmpChalkId;
            _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
            _item.setChalkId(_tmpChalkId);
            final int _tmpUserid;
            _tmpUserid = _cursor.getInt(_cursorIndexOfUserid);
            _item.setUserid(_tmpUserid);
            final int _tmpDeviceId;
            _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
            _item.setDeviceId(_tmpDeviceId);
            final int _tmpCustId;
            _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
            _item.setCustId(_tmpCustId);
            final String _tmpIsExpired;
            if (_cursor.isNull(_cursorIndexOfIsExpired)) {
              _tmpIsExpired = null;
            } else {
              _tmpIsExpired = _cursor.getString(_cursorIndexOfIsExpired);
            }
            _item.setIsExpired(_tmpIsExpired);
            final String _tmpChalkDurationCode_1;
            if (_cursor.isNull(_cursorIndexOfChalkDurationCode_1)) {
              _tmpChalkDurationCode_1 = null;
            } else {
              _tmpChalkDurationCode_1 = _cursor.getString(_cursorIndexOfChalkDurationCode_1);
            }
            _item.setChalkDurationCode(_tmpChalkDurationCode_1);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Maybe<ALPRChalk> getLastChalkedVehicle() {
    final String _sql = "SELECT * from ALPRPhotoChalk where LastDate = (SELECT MAX(LastDate) from ALPRPhotoChalk)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return Maybe.fromCallable(new Callable<ALPRChalk>() {
      @Override
      public ALPRChalk call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "Plate");
          final int _cursorIndexOfDetails = CursorUtil.getColumnIndexOrThrow(_cursor, "Details");
          final int _cursorIndexOfCustomData1 = CursorUtil.getColumnIndexOrThrow(_cursor, "DataField1");
          final int _cursorIndexOfCustomData2 = CursorUtil.getColumnIndexOrThrow(_cursor, "DataField2");
          final int _cursorIndexOfCustomData3 = CursorUtil.getColumnIndexOrThrow(_cursor, "DataField3");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "Confidence");
          final int _cursorIndexOfFirstDate = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstDate");
          final int _cursorIndexOfFirstTime = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstTime");
          final int _cursorIndexOfFirstDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstDateTime");
          final int _cursorIndexOfFirstParkingBay = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstParkingBay");
          final int _cursorIndexOfFirstLocLat = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstLocLat");
          final int _cursorIndexOfFirstLocLong = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstLocLong");
          final int _cursorIndexOfFirstLocAcc = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstLocAcc");
          final int _cursorIndexOfLastDate = CursorUtil.getColumnIndexOrThrow(_cursor, "LastDate");
          final int _cursorIndexOfLastTime = CursorUtil.getColumnIndexOrThrow(_cursor, "LastTime");
          final int _cursorIndexOfLastDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "LastDateTime");
          final int _cursorIndexOfLastParkingBay = CursorUtil.getColumnIndexOrThrow(_cursor, "LastParkingBay");
          final int _cursorIndexOfLastLocLat = CursorUtil.getColumnIndexOrThrow(_cursor, "LastLocLat");
          final int _cursorIndexOfLastLocLong = CursorUtil.getColumnIndexOrThrow(_cursor, "LastLocLong");
          final int _cursorIndexOfLastLocAcc = CursorUtil.getColumnIndexOrThrow(_cursor, "LastLocAcc");
          final int _cursorIndexOfPermitExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "PermitExpiryDate");
          final int _cursorIndexOfPermitExpiryTime = CursorUtil.getColumnIndexOrThrow(_cursor, "PermitExpiryTime");
          final int _cursorIndexOfChalkDurationId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkDuration");
          final int _cursorIndexOfChalkDurationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_code");
          final int _cursorIndexOfChalkLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkLocation");
          final int _cursorIndexOfChalkTire = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkTire");
          final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkId");
          final int _cursorIndexOfUserid = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custId");
          final int _cursorIndexOfIsExpired = CursorUtil.getColumnIndexOrThrow(_cursor, "is_expired");
          final ALPRChalk _result;
          if(_cursor.moveToFirst()) {
            _result = new ALPRChalk();
            final String _tmpPlate;
            if (_cursor.isNull(_cursorIndexOfPlate)) {
              _tmpPlate = null;
            } else {
              _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
            }
            _result.setPlate(_tmpPlate);
            final String _tmpDetails;
            if (_cursor.isNull(_cursorIndexOfDetails)) {
              _tmpDetails = null;
            } else {
              _tmpDetails = _cursor.getString(_cursorIndexOfDetails);
            }
            _result.setDetails(_tmpDetails);
            final String _tmpCustomData1;
            if (_cursor.isNull(_cursorIndexOfCustomData1)) {
              _tmpCustomData1 = null;
            } else {
              _tmpCustomData1 = _cursor.getString(_cursorIndexOfCustomData1);
            }
            _result.setCustomData1(_tmpCustomData1);
            final String _tmpCustomData2;
            if (_cursor.isNull(_cursorIndexOfCustomData2)) {
              _tmpCustomData2 = null;
            } else {
              _tmpCustomData2 = _cursor.getString(_cursorIndexOfCustomData2);
            }
            _result.setCustomData2(_tmpCustomData2);
            final String _tmpCustomData3;
            if (_cursor.isNull(_cursorIndexOfCustomData3)) {
              _tmpCustomData3 = null;
            } else {
              _tmpCustomData3 = _cursor.getString(_cursorIndexOfCustomData3);
            }
            _result.setCustomData3(_tmpCustomData3);
            final String _tmpConfidence;
            if (_cursor.isNull(_cursorIndexOfConfidence)) {
              _tmpConfidence = null;
            } else {
              _tmpConfidence = _cursor.getString(_cursorIndexOfConfidence);
            }
            _result.setConfidence(_tmpConfidence);
            final String _tmpFirstDate;
            if (_cursor.isNull(_cursorIndexOfFirstDate)) {
              _tmpFirstDate = null;
            } else {
              _tmpFirstDate = _cursor.getString(_cursorIndexOfFirstDate);
            }
            _result.setFirstDate(_tmpFirstDate);
            final String _tmpFirstTime;
            if (_cursor.isNull(_cursorIndexOfFirstTime)) {
              _tmpFirstTime = null;
            } else {
              _tmpFirstTime = _cursor.getString(_cursorIndexOfFirstTime);
            }
            _result.setFirstTime(_tmpFirstTime);
            final Date _tmpFirstDateTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfFirstDateTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfFirstDateTime);
            }
            _tmpFirstDateTime = Converters.fromTimestamp(_tmp);
            _result.setFirstDateTime(_tmpFirstDateTime);
            final String _tmpFirstParkingBay;
            if (_cursor.isNull(_cursorIndexOfFirstParkingBay)) {
              _tmpFirstParkingBay = null;
            } else {
              _tmpFirstParkingBay = _cursor.getString(_cursorIndexOfFirstParkingBay);
            }
            _result.setFirstParkingBay(_tmpFirstParkingBay);
            final String _tmpFirstLocLat;
            if (_cursor.isNull(_cursorIndexOfFirstLocLat)) {
              _tmpFirstLocLat = null;
            } else {
              _tmpFirstLocLat = _cursor.getString(_cursorIndexOfFirstLocLat);
            }
            _result.setFirstLocLat(_tmpFirstLocLat);
            final String _tmpFirstLocLong;
            if (_cursor.isNull(_cursorIndexOfFirstLocLong)) {
              _tmpFirstLocLong = null;
            } else {
              _tmpFirstLocLong = _cursor.getString(_cursorIndexOfFirstLocLong);
            }
            _result.setFirstLocLong(_tmpFirstLocLong);
            final String _tmpFirstLocAcc;
            if (_cursor.isNull(_cursorIndexOfFirstLocAcc)) {
              _tmpFirstLocAcc = null;
            } else {
              _tmpFirstLocAcc = _cursor.getString(_cursorIndexOfFirstLocAcc);
            }
            _result.setFirstLocAcc(_tmpFirstLocAcc);
            final String _tmpLastDate;
            if (_cursor.isNull(_cursorIndexOfLastDate)) {
              _tmpLastDate = null;
            } else {
              _tmpLastDate = _cursor.getString(_cursorIndexOfLastDate);
            }
            _result.setLastDate(_tmpLastDate);
            final String _tmpLastTime;
            if (_cursor.isNull(_cursorIndexOfLastTime)) {
              _tmpLastTime = null;
            } else {
              _tmpLastTime = _cursor.getString(_cursorIndexOfLastTime);
            }
            _result.setLastTime(_tmpLastTime);
            final Date _tmpLastDateTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfLastDateTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfLastDateTime);
            }
            _tmpLastDateTime = Converters.fromTimestamp(_tmp_1);
            _result.setLastDateTime(_tmpLastDateTime);
            final String _tmpLastParkingBay;
            if (_cursor.isNull(_cursorIndexOfLastParkingBay)) {
              _tmpLastParkingBay = null;
            } else {
              _tmpLastParkingBay = _cursor.getString(_cursorIndexOfLastParkingBay);
            }
            _result.setLastParkingBay(_tmpLastParkingBay);
            final String _tmpLastLocLat;
            if (_cursor.isNull(_cursorIndexOfLastLocLat)) {
              _tmpLastLocLat = null;
            } else {
              _tmpLastLocLat = _cursor.getString(_cursorIndexOfLastLocLat);
            }
            _result.setLastLocLat(_tmpLastLocLat);
            final String _tmpLastLocLong;
            if (_cursor.isNull(_cursorIndexOfLastLocLong)) {
              _tmpLastLocLong = null;
            } else {
              _tmpLastLocLong = _cursor.getString(_cursorIndexOfLastLocLong);
            }
            _result.setLastLocLong(_tmpLastLocLong);
            final String _tmpLastLocAcc;
            if (_cursor.isNull(_cursorIndexOfLastLocAcc)) {
              _tmpLastLocAcc = null;
            } else {
              _tmpLastLocAcc = _cursor.getString(_cursorIndexOfLastLocAcc);
            }
            _result.setLastLocAcc(_tmpLastLocAcc);
            final String _tmpPermitExpiryDate;
            if (_cursor.isNull(_cursorIndexOfPermitExpiryDate)) {
              _tmpPermitExpiryDate = null;
            } else {
              _tmpPermitExpiryDate = _cursor.getString(_cursorIndexOfPermitExpiryDate);
            }
            _result.setPermitExpiryDate(_tmpPermitExpiryDate);
            final String _tmpPermitExpiryTime;
            if (_cursor.isNull(_cursorIndexOfPermitExpiryTime)) {
              _tmpPermitExpiryTime = null;
            } else {
              _tmpPermitExpiryTime = _cursor.getString(_cursorIndexOfPermitExpiryTime);
            }
            _result.setPermitExpiryTime(_tmpPermitExpiryTime);
            final int _tmpChalkDurationId;
            _tmpChalkDurationId = _cursor.getInt(_cursorIndexOfChalkDurationId);
            _result.setChalkDurationId(_tmpChalkDurationId);
            final String _tmpChalkDurationCode;
            if (_cursor.isNull(_cursorIndexOfChalkDurationCode)) {
              _tmpChalkDurationCode = null;
            } else {
              _tmpChalkDurationCode = _cursor.getString(_cursorIndexOfChalkDurationCode);
            }
            _result.setChalkDurationCode(_tmpChalkDurationCode);
            final String _tmpChalkLocation;
            if (_cursor.isNull(_cursorIndexOfChalkLocation)) {
              _tmpChalkLocation = null;
            } else {
              _tmpChalkLocation = _cursor.getString(_cursorIndexOfChalkLocation);
            }
            _result.setChalkLocation(_tmpChalkLocation);
            final String _tmpChalkTire;
            if (_cursor.isNull(_cursorIndexOfChalkTire)) {
              _tmpChalkTire = null;
            } else {
              _tmpChalkTire = _cursor.getString(_cursorIndexOfChalkTire);
            }
            _result.setChalkTire(_tmpChalkTire);
            final long _tmpChalkId;
            _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
            _result.setChalkId(_tmpChalkId);
            final int _tmpUserid;
            _tmpUserid = _cursor.getInt(_cursorIndexOfUserid);
            _result.setUserid(_tmpUserid);
            final int _tmpDeviceId;
            _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
            _result.setDeviceId(_tmpDeviceId);
            final int _tmpCustId;
            _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
            _result.setCustId(_tmpCustId);
            final String _tmpIsExpired;
            if (_cursor.isNull(_cursorIndexOfIsExpired)) {
              _tmpIsExpired = null;
            } else {
              _tmpIsExpired = _cursor.getString(_cursorIndexOfIsExpired);
            }
            _result.setIsExpired(_tmpIsExpired);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Maybe<ALPRChalk> getChalkVehicleById(final long chalkId) {
    final String _sql = "select * from ALPRPhotoChalk where chalkId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chalkId);
    return Maybe.fromCallable(new Callable<ALPRChalk>() {
      @Override
      public ALPRChalk call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "Plate");
          final int _cursorIndexOfDetails = CursorUtil.getColumnIndexOrThrow(_cursor, "Details");
          final int _cursorIndexOfCustomData1 = CursorUtil.getColumnIndexOrThrow(_cursor, "DataField1");
          final int _cursorIndexOfCustomData2 = CursorUtil.getColumnIndexOrThrow(_cursor, "DataField2");
          final int _cursorIndexOfCustomData3 = CursorUtil.getColumnIndexOrThrow(_cursor, "DataField3");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "Confidence");
          final int _cursorIndexOfFirstDate = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstDate");
          final int _cursorIndexOfFirstTime = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstTime");
          final int _cursorIndexOfFirstDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstDateTime");
          final int _cursorIndexOfFirstParkingBay = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstParkingBay");
          final int _cursorIndexOfFirstLocLat = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstLocLat");
          final int _cursorIndexOfFirstLocLong = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstLocLong");
          final int _cursorIndexOfFirstLocAcc = CursorUtil.getColumnIndexOrThrow(_cursor, "FirstLocAcc");
          final int _cursorIndexOfLastDate = CursorUtil.getColumnIndexOrThrow(_cursor, "LastDate");
          final int _cursorIndexOfLastTime = CursorUtil.getColumnIndexOrThrow(_cursor, "LastTime");
          final int _cursorIndexOfLastDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "LastDateTime");
          final int _cursorIndexOfLastParkingBay = CursorUtil.getColumnIndexOrThrow(_cursor, "LastParkingBay");
          final int _cursorIndexOfLastLocLat = CursorUtil.getColumnIndexOrThrow(_cursor, "LastLocLat");
          final int _cursorIndexOfLastLocLong = CursorUtil.getColumnIndexOrThrow(_cursor, "LastLocLong");
          final int _cursorIndexOfLastLocAcc = CursorUtil.getColumnIndexOrThrow(_cursor, "LastLocAcc");
          final int _cursorIndexOfPermitExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "PermitExpiryDate");
          final int _cursorIndexOfPermitExpiryTime = CursorUtil.getColumnIndexOrThrow(_cursor, "PermitExpiryTime");
          final int _cursorIndexOfChalkDurationId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkDuration");
          final int _cursorIndexOfChalkDurationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_code");
          final int _cursorIndexOfChalkLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkLocation");
          final int _cursorIndexOfChalkTire = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkTire");
          final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalkId");
          final int _cursorIndexOfUserid = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custId");
          final int _cursorIndexOfIsExpired = CursorUtil.getColumnIndexOrThrow(_cursor, "is_expired");
          final ALPRChalk _result;
          if(_cursor.moveToFirst()) {
            _result = new ALPRChalk();
            final String _tmpPlate;
            if (_cursor.isNull(_cursorIndexOfPlate)) {
              _tmpPlate = null;
            } else {
              _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
            }
            _result.setPlate(_tmpPlate);
            final String _tmpDetails;
            if (_cursor.isNull(_cursorIndexOfDetails)) {
              _tmpDetails = null;
            } else {
              _tmpDetails = _cursor.getString(_cursorIndexOfDetails);
            }
            _result.setDetails(_tmpDetails);
            final String _tmpCustomData1;
            if (_cursor.isNull(_cursorIndexOfCustomData1)) {
              _tmpCustomData1 = null;
            } else {
              _tmpCustomData1 = _cursor.getString(_cursorIndexOfCustomData1);
            }
            _result.setCustomData1(_tmpCustomData1);
            final String _tmpCustomData2;
            if (_cursor.isNull(_cursorIndexOfCustomData2)) {
              _tmpCustomData2 = null;
            } else {
              _tmpCustomData2 = _cursor.getString(_cursorIndexOfCustomData2);
            }
            _result.setCustomData2(_tmpCustomData2);
            final String _tmpCustomData3;
            if (_cursor.isNull(_cursorIndexOfCustomData3)) {
              _tmpCustomData3 = null;
            } else {
              _tmpCustomData3 = _cursor.getString(_cursorIndexOfCustomData3);
            }
            _result.setCustomData3(_tmpCustomData3);
            final String _tmpConfidence;
            if (_cursor.isNull(_cursorIndexOfConfidence)) {
              _tmpConfidence = null;
            } else {
              _tmpConfidence = _cursor.getString(_cursorIndexOfConfidence);
            }
            _result.setConfidence(_tmpConfidence);
            final String _tmpFirstDate;
            if (_cursor.isNull(_cursorIndexOfFirstDate)) {
              _tmpFirstDate = null;
            } else {
              _tmpFirstDate = _cursor.getString(_cursorIndexOfFirstDate);
            }
            _result.setFirstDate(_tmpFirstDate);
            final String _tmpFirstTime;
            if (_cursor.isNull(_cursorIndexOfFirstTime)) {
              _tmpFirstTime = null;
            } else {
              _tmpFirstTime = _cursor.getString(_cursorIndexOfFirstTime);
            }
            _result.setFirstTime(_tmpFirstTime);
            final Date _tmpFirstDateTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfFirstDateTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfFirstDateTime);
            }
            _tmpFirstDateTime = Converters.fromTimestamp(_tmp);
            _result.setFirstDateTime(_tmpFirstDateTime);
            final String _tmpFirstParkingBay;
            if (_cursor.isNull(_cursorIndexOfFirstParkingBay)) {
              _tmpFirstParkingBay = null;
            } else {
              _tmpFirstParkingBay = _cursor.getString(_cursorIndexOfFirstParkingBay);
            }
            _result.setFirstParkingBay(_tmpFirstParkingBay);
            final String _tmpFirstLocLat;
            if (_cursor.isNull(_cursorIndexOfFirstLocLat)) {
              _tmpFirstLocLat = null;
            } else {
              _tmpFirstLocLat = _cursor.getString(_cursorIndexOfFirstLocLat);
            }
            _result.setFirstLocLat(_tmpFirstLocLat);
            final String _tmpFirstLocLong;
            if (_cursor.isNull(_cursorIndexOfFirstLocLong)) {
              _tmpFirstLocLong = null;
            } else {
              _tmpFirstLocLong = _cursor.getString(_cursorIndexOfFirstLocLong);
            }
            _result.setFirstLocLong(_tmpFirstLocLong);
            final String _tmpFirstLocAcc;
            if (_cursor.isNull(_cursorIndexOfFirstLocAcc)) {
              _tmpFirstLocAcc = null;
            } else {
              _tmpFirstLocAcc = _cursor.getString(_cursorIndexOfFirstLocAcc);
            }
            _result.setFirstLocAcc(_tmpFirstLocAcc);
            final String _tmpLastDate;
            if (_cursor.isNull(_cursorIndexOfLastDate)) {
              _tmpLastDate = null;
            } else {
              _tmpLastDate = _cursor.getString(_cursorIndexOfLastDate);
            }
            _result.setLastDate(_tmpLastDate);
            final String _tmpLastTime;
            if (_cursor.isNull(_cursorIndexOfLastTime)) {
              _tmpLastTime = null;
            } else {
              _tmpLastTime = _cursor.getString(_cursorIndexOfLastTime);
            }
            _result.setLastTime(_tmpLastTime);
            final Date _tmpLastDateTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfLastDateTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfLastDateTime);
            }
            _tmpLastDateTime = Converters.fromTimestamp(_tmp_1);
            _result.setLastDateTime(_tmpLastDateTime);
            final String _tmpLastParkingBay;
            if (_cursor.isNull(_cursorIndexOfLastParkingBay)) {
              _tmpLastParkingBay = null;
            } else {
              _tmpLastParkingBay = _cursor.getString(_cursorIndexOfLastParkingBay);
            }
            _result.setLastParkingBay(_tmpLastParkingBay);
            final String _tmpLastLocLat;
            if (_cursor.isNull(_cursorIndexOfLastLocLat)) {
              _tmpLastLocLat = null;
            } else {
              _tmpLastLocLat = _cursor.getString(_cursorIndexOfLastLocLat);
            }
            _result.setLastLocLat(_tmpLastLocLat);
            final String _tmpLastLocLong;
            if (_cursor.isNull(_cursorIndexOfLastLocLong)) {
              _tmpLastLocLong = null;
            } else {
              _tmpLastLocLong = _cursor.getString(_cursorIndexOfLastLocLong);
            }
            _result.setLastLocLong(_tmpLastLocLong);
            final String _tmpLastLocAcc;
            if (_cursor.isNull(_cursorIndexOfLastLocAcc)) {
              _tmpLastLocAcc = null;
            } else {
              _tmpLastLocAcc = _cursor.getString(_cursorIndexOfLastLocAcc);
            }
            _result.setLastLocAcc(_tmpLastLocAcc);
            final String _tmpPermitExpiryDate;
            if (_cursor.isNull(_cursorIndexOfPermitExpiryDate)) {
              _tmpPermitExpiryDate = null;
            } else {
              _tmpPermitExpiryDate = _cursor.getString(_cursorIndexOfPermitExpiryDate);
            }
            _result.setPermitExpiryDate(_tmpPermitExpiryDate);
            final String _tmpPermitExpiryTime;
            if (_cursor.isNull(_cursorIndexOfPermitExpiryTime)) {
              _tmpPermitExpiryTime = null;
            } else {
              _tmpPermitExpiryTime = _cursor.getString(_cursorIndexOfPermitExpiryTime);
            }
            _result.setPermitExpiryTime(_tmpPermitExpiryTime);
            final int _tmpChalkDurationId;
            _tmpChalkDurationId = _cursor.getInt(_cursorIndexOfChalkDurationId);
            _result.setChalkDurationId(_tmpChalkDurationId);
            final String _tmpChalkDurationCode;
            if (_cursor.isNull(_cursorIndexOfChalkDurationCode)) {
              _tmpChalkDurationCode = null;
            } else {
              _tmpChalkDurationCode = _cursor.getString(_cursorIndexOfChalkDurationCode);
            }
            _result.setChalkDurationCode(_tmpChalkDurationCode);
            final String _tmpChalkLocation;
            if (_cursor.isNull(_cursorIndexOfChalkLocation)) {
              _tmpChalkLocation = null;
            } else {
              _tmpChalkLocation = _cursor.getString(_cursorIndexOfChalkLocation);
            }
            _result.setChalkLocation(_tmpChalkLocation);
            final String _tmpChalkTire;
            if (_cursor.isNull(_cursorIndexOfChalkTire)) {
              _tmpChalkTire = null;
            } else {
              _tmpChalkTire = _cursor.getString(_cursorIndexOfChalkTire);
            }
            _result.setChalkTire(_tmpChalkTire);
            final long _tmpChalkId;
            _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
            _result.setChalkId(_tmpChalkId);
            final int _tmpUserid;
            _tmpUserid = _cursor.getInt(_cursorIndexOfUserid);
            _result.setUserid(_tmpUserid);
            final int _tmpDeviceId;
            _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
            _result.setDeviceId(_tmpDeviceId);
            final int _tmpCustId;
            _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
            _result.setCustId(_tmpCustId);
            final String _tmpIsExpired;
            if (_cursor.isNull(_cursorIndexOfIsExpired)) {
              _tmpIsExpired = null;
            } else {
              _tmpIsExpired = _cursor.getString(_cursorIndexOfIsExpired);
            }
            _result.setIsExpired(_tmpIsExpired);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Maybe<List<String>> getAllPlates() {
    final String _sql = "Select Plate from ALPRPhotoChalk";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return Maybe.fromCallable(new Callable<List<String>>() {
      @Override
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final String _item;
            if (_cursor.isNull(0)) {
              _item = null;
            } else {
              _item = _cursor.getString(0);
            }
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
