package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.DeviceData;
import io.reactivex.Single;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FT_DeviceHistoryDao_Impl implements FT_DeviceHistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DeviceData> __insertionAdapterOfDeviceData;

  private final SharedSQLiteStatement __preparedStmtOfDeleteRecord;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSyncStatus;

  public FT_DeviceHistoryDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDeviceData = new EntityInsertionAdapter<DeviceData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `FT_DeviceHistory` (`id`,`dutyId`,`dutyName`,`custId`,`deviceId`,`deviceName`,`latitude`,`longitude`,`name`,`fullName`,`device`,`timeStamp`,`badge`,`isActive`,`userId`,`firstLogin`,`lastTicketTimeStamp`,`currTimeStamp`,`pushToken`,`moduleId`,`deviceInactivity`,`isLoggedIn`,`appVersion`,`address`,`activityName`,`sync_status`,`altitude`,`Violation`,`Citation`,`accuracy`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DeviceData value) {
        stmt.bindLong(1, value.getId());
        if (value.getDutyId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDutyId());
        }
        if (value.getDutyName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDutyName());
        }
        if (value.getCustId() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getCustId());
        }
        if (value.getDeviceId() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getDeviceId());
        }
        if (value.getDeviceName() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getDeviceName());
        }
        stmt.bindDouble(7, value.getLattitude());
        stmt.bindDouble(8, value.getLongitude());
        if (value.getName() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getName());
        }
        if (value.getFullName() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getFullName());
        }
        if (value.getDevice() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getDevice());
        }
        if (value.getTimeStamp() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getTimeStamp());
        }
        if (value.getBadge() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getBadge());
        }
        final int _tmp;
        _tmp = value.isActive() ? 1 : 0;
        stmt.bindLong(14, _tmp);
        if (value.getUserId() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getUserId());
        }
        if (value.getFirstLogin() == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindString(16, value.getFirstLogin());
        }
        if (value.getLastTicketTimeStamp() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindString(17, value.getLastTicketTimeStamp());
        }
        if (value.getCurrTimeStamp() == null) {
          stmt.bindNull(18);
        } else {
          stmt.bindString(18, value.getCurrTimeStamp());
        }
        if (value.getPushToken() == null) {
          stmt.bindNull(19);
        } else {
          stmt.bindString(19, value.getPushToken());
        }
        if (value.getModuleId() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.getModuleId());
        }
        final int _tmp_1;
        _tmp_1 = value.isDeviceInactivity() ? 1 : 0;
        stmt.bindLong(21, _tmp_1);
        final int _tmp_2;
        _tmp_2 = value.isLoggedIn() ? 1 : 0;
        stmt.bindLong(22, _tmp_2);
        if (value.getAppVersion() == null) {
          stmt.bindNull(23);
        } else {
          stmt.bindString(23, value.getAppVersion());
        }
        if (value.getAddress() == null) {
          stmt.bindNull(24);
        } else {
          stmt.bindString(24, value.getAddress());
        }
        if (value.getActivityName() == null) {
          stmt.bindNull(25);
        } else {
          stmt.bindString(25, value.getActivityName());
        }
        if (value.getSync_status() == null) {
          stmt.bindNull(26);
        } else {
          stmt.bindString(26, value.getSync_status());
        }
        stmt.bindDouble(27, value.getAltitude());
        if (value.getViolation() == null) {
          stmt.bindNull(28);
        } else {
          stmt.bindString(28, value.getViolation());
        }
        if (value.getCitation() == null) {
          stmt.bindNull(29);
        } else {
          stmt.bindString(29, value.getCitation());
        }
        stmt.bindDouble(30, value.getAccuracy());
      }
    };
    this.__preparedStmtOfDeleteRecord = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from FT_DeviceHistory where id= ? and sync_status='S'";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateSyncStatus = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update FT_DeviceHistory set sync_status = 'S' where currTimeStamp=?";
        return _query;
      }
    };
  }

  @Override
  public void insertDeviceData(final DeviceData... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDeviceData.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Single<Long> insertDeviceData(final DeviceData data) {
    return Single.fromCallable(new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfDeviceData.insertAndReturnId(data);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertDeviceDataList(final List<DeviceData> DeviceDatasList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDeviceData.insert(DeviceDatasList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteRecord(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteRecord.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteRecord.release(_stmt);
    }
  }

  @Override
  public void updateSyncStatus(final String s) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSyncStatus.acquire();
    int _argIndex = 1;
    if (s == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, s);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateSyncStatus.release(_stmt);
    }
  }

  @Override
  public List<DeviceData> getPendingLocationUpdates() {
    final String _sql = "select * from FT_DeviceHistory where sync_status='P'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDutyId = CursorUtil.getColumnIndexOrThrow(_cursor, "dutyId");
      final int _cursorIndexOfDutyName = CursorUtil.getColumnIndexOrThrow(_cursor, "dutyName");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custId");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
      final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceName");
      final int _cursorIndexOfLattitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
      final int _cursorIndexOfDevice = CursorUtil.getColumnIndexOrThrow(_cursor, "device");
      final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timeStamp");
      final int _cursorIndexOfBadge = CursorUtil.getColumnIndexOrThrow(_cursor, "badge");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfFirstLogin = CursorUtil.getColumnIndexOrThrow(_cursor, "firstLogin");
      final int _cursorIndexOfLastTicketTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTicketTimeStamp");
      final int _cursorIndexOfCurrTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "currTimeStamp");
      final int _cursorIndexOfPushToken = CursorUtil.getColumnIndexOrThrow(_cursor, "pushToken");
      final int _cursorIndexOfModuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "moduleId");
      final int _cursorIndexOfDeviceInactivity = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceInactivity");
      final int _cursorIndexOfIsLoggedIn = CursorUtil.getColumnIndexOrThrow(_cursor, "isLoggedIn");
      final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "appVersion");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfActivityName = CursorUtil.getColumnIndexOrThrow(_cursor, "activityName");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final int _cursorIndexOfAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "altitude");
      final int _cursorIndexOfViolation = CursorUtil.getColumnIndexOrThrow(_cursor, "Violation");
      final int _cursorIndexOfCitation = CursorUtil.getColumnIndexOrThrow(_cursor, "Citation");
      final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
      final List<DeviceData> _result = new ArrayList<DeviceData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final DeviceData _item;
        _item = new DeviceData();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpDutyId;
        if (_cursor.isNull(_cursorIndexOfDutyId)) {
          _tmpDutyId = null;
        } else {
          _tmpDutyId = _cursor.getString(_cursorIndexOfDutyId);
        }
        _item.setDutyId(_tmpDutyId);
        final String _tmpDutyName;
        if (_cursor.isNull(_cursorIndexOfDutyName)) {
          _tmpDutyName = null;
        } else {
          _tmpDutyName = _cursor.getString(_cursorIndexOfDutyName);
        }
        _item.setDutyName(_tmpDutyName);
        final String _tmpCustId;
        if (_cursor.isNull(_cursorIndexOfCustId)) {
          _tmpCustId = null;
        } else {
          _tmpCustId = _cursor.getString(_cursorIndexOfCustId);
        }
        _item.setCustId(_tmpCustId);
        final String _tmpDeviceId;
        if (_cursor.isNull(_cursorIndexOfDeviceId)) {
          _tmpDeviceId = null;
        } else {
          _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
        }
        _item.setDeviceId(_tmpDeviceId);
        final String _tmpDeviceName;
        if (_cursor.isNull(_cursorIndexOfDeviceName)) {
          _tmpDeviceName = null;
        } else {
          _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
        }
        _item.setDeviceName(_tmpDeviceName);
        final double _tmpLattitude;
        _tmpLattitude = _cursor.getDouble(_cursorIndexOfLattitude);
        _item.setLattitude(_tmpLattitude);
        final double _tmpLongitude;
        _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        _item.setLongitude(_tmpLongitude);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpFullName;
        if (_cursor.isNull(_cursorIndexOfFullName)) {
          _tmpFullName = null;
        } else {
          _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
        }
        _item.setFullName(_tmpFullName);
        final String _tmpDevice;
        if (_cursor.isNull(_cursorIndexOfDevice)) {
          _tmpDevice = null;
        } else {
          _tmpDevice = _cursor.getString(_cursorIndexOfDevice);
        }
        _item.setDevice(_tmpDevice);
        final String _tmpTimeStamp;
        if (_cursor.isNull(_cursorIndexOfTimeStamp)) {
          _tmpTimeStamp = null;
        } else {
          _tmpTimeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
        }
        _item.setTimeStamp(_tmpTimeStamp);
        final String _tmpBadge;
        if (_cursor.isNull(_cursorIndexOfBadge)) {
          _tmpBadge = null;
        } else {
          _tmpBadge = _cursor.getString(_cursorIndexOfBadge);
        }
        _item.setBadge(_tmpBadge);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _item.setActive(_tmpIsActive);
        final String _tmpUserId;
        if (_cursor.isNull(_cursorIndexOfUserId)) {
          _tmpUserId = null;
        } else {
          _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        }
        _item.setUserId(_tmpUserId);
        final String _tmpFirstLogin;
        if (_cursor.isNull(_cursorIndexOfFirstLogin)) {
          _tmpFirstLogin = null;
        } else {
          _tmpFirstLogin = _cursor.getString(_cursorIndexOfFirstLogin);
        }
        _item.setFirstLogin(_tmpFirstLogin);
        final String _tmpLastTicketTimeStamp;
        if (_cursor.isNull(_cursorIndexOfLastTicketTimeStamp)) {
          _tmpLastTicketTimeStamp = null;
        } else {
          _tmpLastTicketTimeStamp = _cursor.getString(_cursorIndexOfLastTicketTimeStamp);
        }
        _item.setLastTicketTimeStamp(_tmpLastTicketTimeStamp);
        final String _tmpCurrTimeStamp;
        if (_cursor.isNull(_cursorIndexOfCurrTimeStamp)) {
          _tmpCurrTimeStamp = null;
        } else {
          _tmpCurrTimeStamp = _cursor.getString(_cursorIndexOfCurrTimeStamp);
        }
        _item.setCurrTimeStamp(_tmpCurrTimeStamp);
        final String _tmpPushToken;
        if (_cursor.isNull(_cursorIndexOfPushToken)) {
          _tmpPushToken = null;
        } else {
          _tmpPushToken = _cursor.getString(_cursorIndexOfPushToken);
        }
        _item.setPushToken(_tmpPushToken);
        final String _tmpModuleId;
        if (_cursor.isNull(_cursorIndexOfModuleId)) {
          _tmpModuleId = null;
        } else {
          _tmpModuleId = _cursor.getString(_cursorIndexOfModuleId);
        }
        _item.setModuleId(_tmpModuleId);
        final boolean _tmpDeviceInactivity;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfDeviceInactivity);
        _tmpDeviceInactivity = _tmp_1 != 0;
        _item.setDeviceInactivity(_tmpDeviceInactivity);
        final boolean _tmpIsLoggedIn;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIsLoggedIn);
        _tmpIsLoggedIn = _tmp_2 != 0;
        _item.setLoggedIn(_tmpIsLoggedIn);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setAppVersion(_tmpAppVersion);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _item.setAddress(_tmpAddress);
        final String _tmpActivityName;
        if (_cursor.isNull(_cursorIndexOfActivityName)) {
          _tmpActivityName = null;
        } else {
          _tmpActivityName = _cursor.getString(_cursorIndexOfActivityName);
        }
        _item.setActivityName(_tmpActivityName);
        final String _tmpSync_status;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSync_status = null;
        } else {
          _tmpSync_status = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _item.setSync_status(_tmpSync_status);
        final double _tmpAltitude;
        _tmpAltitude = _cursor.getDouble(_cursorIndexOfAltitude);
        _item.setAltitude(_tmpAltitude);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item.setViolation(_tmpViolation);
        final String _tmpCitation;
        if (_cursor.isNull(_cursorIndexOfCitation)) {
          _tmpCitation = null;
        } else {
          _tmpCitation = _cursor.getString(_cursorIndexOfCitation);
        }
        _item.setCitation(_tmpCitation);
        final double _tmpAccuracy;
        _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
        _item.setAccuracy(_tmpAccuracy);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<DeviceData> getAllData() {
    final String _sql = "select * from FT_DeviceHistory";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDutyId = CursorUtil.getColumnIndexOrThrow(_cursor, "dutyId");
      final int _cursorIndexOfDutyName = CursorUtil.getColumnIndexOrThrow(_cursor, "dutyName");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custId");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
      final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceName");
      final int _cursorIndexOfLattitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
      final int _cursorIndexOfDevice = CursorUtil.getColumnIndexOrThrow(_cursor, "device");
      final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timeStamp");
      final int _cursorIndexOfBadge = CursorUtil.getColumnIndexOrThrow(_cursor, "badge");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfFirstLogin = CursorUtil.getColumnIndexOrThrow(_cursor, "firstLogin");
      final int _cursorIndexOfLastTicketTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTicketTimeStamp");
      final int _cursorIndexOfCurrTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "currTimeStamp");
      final int _cursorIndexOfPushToken = CursorUtil.getColumnIndexOrThrow(_cursor, "pushToken");
      final int _cursorIndexOfModuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "moduleId");
      final int _cursorIndexOfDeviceInactivity = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceInactivity");
      final int _cursorIndexOfIsLoggedIn = CursorUtil.getColumnIndexOrThrow(_cursor, "isLoggedIn");
      final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "appVersion");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfActivityName = CursorUtil.getColumnIndexOrThrow(_cursor, "activityName");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final int _cursorIndexOfAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "altitude");
      final int _cursorIndexOfViolation = CursorUtil.getColumnIndexOrThrow(_cursor, "Violation");
      final int _cursorIndexOfCitation = CursorUtil.getColumnIndexOrThrow(_cursor, "Citation");
      final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
      final List<DeviceData> _result = new ArrayList<DeviceData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final DeviceData _item;
        _item = new DeviceData();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpDutyId;
        if (_cursor.isNull(_cursorIndexOfDutyId)) {
          _tmpDutyId = null;
        } else {
          _tmpDutyId = _cursor.getString(_cursorIndexOfDutyId);
        }
        _item.setDutyId(_tmpDutyId);
        final String _tmpDutyName;
        if (_cursor.isNull(_cursorIndexOfDutyName)) {
          _tmpDutyName = null;
        } else {
          _tmpDutyName = _cursor.getString(_cursorIndexOfDutyName);
        }
        _item.setDutyName(_tmpDutyName);
        final String _tmpCustId;
        if (_cursor.isNull(_cursorIndexOfCustId)) {
          _tmpCustId = null;
        } else {
          _tmpCustId = _cursor.getString(_cursorIndexOfCustId);
        }
        _item.setCustId(_tmpCustId);
        final String _tmpDeviceId;
        if (_cursor.isNull(_cursorIndexOfDeviceId)) {
          _tmpDeviceId = null;
        } else {
          _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
        }
        _item.setDeviceId(_tmpDeviceId);
        final String _tmpDeviceName;
        if (_cursor.isNull(_cursorIndexOfDeviceName)) {
          _tmpDeviceName = null;
        } else {
          _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
        }
        _item.setDeviceName(_tmpDeviceName);
        final double _tmpLattitude;
        _tmpLattitude = _cursor.getDouble(_cursorIndexOfLattitude);
        _item.setLattitude(_tmpLattitude);
        final double _tmpLongitude;
        _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        _item.setLongitude(_tmpLongitude);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpFullName;
        if (_cursor.isNull(_cursorIndexOfFullName)) {
          _tmpFullName = null;
        } else {
          _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
        }
        _item.setFullName(_tmpFullName);
        final String _tmpDevice;
        if (_cursor.isNull(_cursorIndexOfDevice)) {
          _tmpDevice = null;
        } else {
          _tmpDevice = _cursor.getString(_cursorIndexOfDevice);
        }
        _item.setDevice(_tmpDevice);
        final String _tmpTimeStamp;
        if (_cursor.isNull(_cursorIndexOfTimeStamp)) {
          _tmpTimeStamp = null;
        } else {
          _tmpTimeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
        }
        _item.setTimeStamp(_tmpTimeStamp);
        final String _tmpBadge;
        if (_cursor.isNull(_cursorIndexOfBadge)) {
          _tmpBadge = null;
        } else {
          _tmpBadge = _cursor.getString(_cursorIndexOfBadge);
        }
        _item.setBadge(_tmpBadge);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _item.setActive(_tmpIsActive);
        final String _tmpUserId;
        if (_cursor.isNull(_cursorIndexOfUserId)) {
          _tmpUserId = null;
        } else {
          _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        }
        _item.setUserId(_tmpUserId);
        final String _tmpFirstLogin;
        if (_cursor.isNull(_cursorIndexOfFirstLogin)) {
          _tmpFirstLogin = null;
        } else {
          _tmpFirstLogin = _cursor.getString(_cursorIndexOfFirstLogin);
        }
        _item.setFirstLogin(_tmpFirstLogin);
        final String _tmpLastTicketTimeStamp;
        if (_cursor.isNull(_cursorIndexOfLastTicketTimeStamp)) {
          _tmpLastTicketTimeStamp = null;
        } else {
          _tmpLastTicketTimeStamp = _cursor.getString(_cursorIndexOfLastTicketTimeStamp);
        }
        _item.setLastTicketTimeStamp(_tmpLastTicketTimeStamp);
        final String _tmpCurrTimeStamp;
        if (_cursor.isNull(_cursorIndexOfCurrTimeStamp)) {
          _tmpCurrTimeStamp = null;
        } else {
          _tmpCurrTimeStamp = _cursor.getString(_cursorIndexOfCurrTimeStamp);
        }
        _item.setCurrTimeStamp(_tmpCurrTimeStamp);
        final String _tmpPushToken;
        if (_cursor.isNull(_cursorIndexOfPushToken)) {
          _tmpPushToken = null;
        } else {
          _tmpPushToken = _cursor.getString(_cursorIndexOfPushToken);
        }
        _item.setPushToken(_tmpPushToken);
        final String _tmpModuleId;
        if (_cursor.isNull(_cursorIndexOfModuleId)) {
          _tmpModuleId = null;
        } else {
          _tmpModuleId = _cursor.getString(_cursorIndexOfModuleId);
        }
        _item.setModuleId(_tmpModuleId);
        final boolean _tmpDeviceInactivity;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfDeviceInactivity);
        _tmpDeviceInactivity = _tmp_1 != 0;
        _item.setDeviceInactivity(_tmpDeviceInactivity);
        final boolean _tmpIsLoggedIn;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIsLoggedIn);
        _tmpIsLoggedIn = _tmp_2 != 0;
        _item.setLoggedIn(_tmpIsLoggedIn);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setAppVersion(_tmpAppVersion);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _item.setAddress(_tmpAddress);
        final String _tmpActivityName;
        if (_cursor.isNull(_cursorIndexOfActivityName)) {
          _tmpActivityName = null;
        } else {
          _tmpActivityName = _cursor.getString(_cursorIndexOfActivityName);
        }
        _item.setActivityName(_tmpActivityName);
        final String _tmpSync_status;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSync_status = null;
        } else {
          _tmpSync_status = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _item.setSync_status(_tmpSync_status);
        final double _tmpAltitude;
        _tmpAltitude = _cursor.getDouble(_cursorIndexOfAltitude);
        _item.setAltitude(_tmpAltitude);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item.setViolation(_tmpViolation);
        final String _tmpCitation;
        if (_cursor.isNull(_cursorIndexOfCitation)) {
          _tmpCitation = null;
        } else {
          _tmpCitation = _cursor.getString(_cursorIndexOfCitation);
        }
        _item.setCitation(_tmpCitation);
        final double _tmpAccuracy;
        _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
        _item.setAccuracy(_tmpAccuracy);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public DeviceData getLastSavedData(final Long lastInsertedDeviceDataID) {
    final String _sql = "select * from FT_DeviceHistory where id= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (lastInsertedDeviceDataID == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, lastInsertedDeviceDataID);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDutyId = CursorUtil.getColumnIndexOrThrow(_cursor, "dutyId");
      final int _cursorIndexOfDutyName = CursorUtil.getColumnIndexOrThrow(_cursor, "dutyName");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custId");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
      final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceName");
      final int _cursorIndexOfLattitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
      final int _cursorIndexOfDevice = CursorUtil.getColumnIndexOrThrow(_cursor, "device");
      final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timeStamp");
      final int _cursorIndexOfBadge = CursorUtil.getColumnIndexOrThrow(_cursor, "badge");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfFirstLogin = CursorUtil.getColumnIndexOrThrow(_cursor, "firstLogin");
      final int _cursorIndexOfLastTicketTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTicketTimeStamp");
      final int _cursorIndexOfCurrTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "currTimeStamp");
      final int _cursorIndexOfPushToken = CursorUtil.getColumnIndexOrThrow(_cursor, "pushToken");
      final int _cursorIndexOfModuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "moduleId");
      final int _cursorIndexOfDeviceInactivity = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceInactivity");
      final int _cursorIndexOfIsLoggedIn = CursorUtil.getColumnIndexOrThrow(_cursor, "isLoggedIn");
      final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "appVersion");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfActivityName = CursorUtil.getColumnIndexOrThrow(_cursor, "activityName");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final int _cursorIndexOfAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "altitude");
      final int _cursorIndexOfViolation = CursorUtil.getColumnIndexOrThrow(_cursor, "Violation");
      final int _cursorIndexOfCitation = CursorUtil.getColumnIndexOrThrow(_cursor, "Citation");
      final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
      final DeviceData _result;
      if(_cursor.moveToFirst()) {
        _result = new DeviceData();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpDutyId;
        if (_cursor.isNull(_cursorIndexOfDutyId)) {
          _tmpDutyId = null;
        } else {
          _tmpDutyId = _cursor.getString(_cursorIndexOfDutyId);
        }
        _result.setDutyId(_tmpDutyId);
        final String _tmpDutyName;
        if (_cursor.isNull(_cursorIndexOfDutyName)) {
          _tmpDutyName = null;
        } else {
          _tmpDutyName = _cursor.getString(_cursorIndexOfDutyName);
        }
        _result.setDutyName(_tmpDutyName);
        final String _tmpCustId;
        if (_cursor.isNull(_cursorIndexOfCustId)) {
          _tmpCustId = null;
        } else {
          _tmpCustId = _cursor.getString(_cursorIndexOfCustId);
        }
        _result.setCustId(_tmpCustId);
        final String _tmpDeviceId;
        if (_cursor.isNull(_cursorIndexOfDeviceId)) {
          _tmpDeviceId = null;
        } else {
          _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
        }
        _result.setDeviceId(_tmpDeviceId);
        final String _tmpDeviceName;
        if (_cursor.isNull(_cursorIndexOfDeviceName)) {
          _tmpDeviceName = null;
        } else {
          _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
        }
        _result.setDeviceName(_tmpDeviceName);
        final double _tmpLattitude;
        _tmpLattitude = _cursor.getDouble(_cursorIndexOfLattitude);
        _result.setLattitude(_tmpLattitude);
        final double _tmpLongitude;
        _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        _result.setLongitude(_tmpLongitude);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _result.setName(_tmpName);
        final String _tmpFullName;
        if (_cursor.isNull(_cursorIndexOfFullName)) {
          _tmpFullName = null;
        } else {
          _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
        }
        _result.setFullName(_tmpFullName);
        final String _tmpDevice;
        if (_cursor.isNull(_cursorIndexOfDevice)) {
          _tmpDevice = null;
        } else {
          _tmpDevice = _cursor.getString(_cursorIndexOfDevice);
        }
        _result.setDevice(_tmpDevice);
        final String _tmpTimeStamp;
        if (_cursor.isNull(_cursorIndexOfTimeStamp)) {
          _tmpTimeStamp = null;
        } else {
          _tmpTimeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
        }
        _result.setTimeStamp(_tmpTimeStamp);
        final String _tmpBadge;
        if (_cursor.isNull(_cursorIndexOfBadge)) {
          _tmpBadge = null;
        } else {
          _tmpBadge = _cursor.getString(_cursorIndexOfBadge);
        }
        _result.setBadge(_tmpBadge);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _result.setActive(_tmpIsActive);
        final String _tmpUserId;
        if (_cursor.isNull(_cursorIndexOfUserId)) {
          _tmpUserId = null;
        } else {
          _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        }
        _result.setUserId(_tmpUserId);
        final String _tmpFirstLogin;
        if (_cursor.isNull(_cursorIndexOfFirstLogin)) {
          _tmpFirstLogin = null;
        } else {
          _tmpFirstLogin = _cursor.getString(_cursorIndexOfFirstLogin);
        }
        _result.setFirstLogin(_tmpFirstLogin);
        final String _tmpLastTicketTimeStamp;
        if (_cursor.isNull(_cursorIndexOfLastTicketTimeStamp)) {
          _tmpLastTicketTimeStamp = null;
        } else {
          _tmpLastTicketTimeStamp = _cursor.getString(_cursorIndexOfLastTicketTimeStamp);
        }
        _result.setLastTicketTimeStamp(_tmpLastTicketTimeStamp);
        final String _tmpCurrTimeStamp;
        if (_cursor.isNull(_cursorIndexOfCurrTimeStamp)) {
          _tmpCurrTimeStamp = null;
        } else {
          _tmpCurrTimeStamp = _cursor.getString(_cursorIndexOfCurrTimeStamp);
        }
        _result.setCurrTimeStamp(_tmpCurrTimeStamp);
        final String _tmpPushToken;
        if (_cursor.isNull(_cursorIndexOfPushToken)) {
          _tmpPushToken = null;
        } else {
          _tmpPushToken = _cursor.getString(_cursorIndexOfPushToken);
        }
        _result.setPushToken(_tmpPushToken);
        final String _tmpModuleId;
        if (_cursor.isNull(_cursorIndexOfModuleId)) {
          _tmpModuleId = null;
        } else {
          _tmpModuleId = _cursor.getString(_cursorIndexOfModuleId);
        }
        _result.setModuleId(_tmpModuleId);
        final boolean _tmpDeviceInactivity;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfDeviceInactivity);
        _tmpDeviceInactivity = _tmp_1 != 0;
        _result.setDeviceInactivity(_tmpDeviceInactivity);
        final boolean _tmpIsLoggedIn;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIsLoggedIn);
        _tmpIsLoggedIn = _tmp_2 != 0;
        _result.setLoggedIn(_tmpIsLoggedIn);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _result.setAppVersion(_tmpAppVersion);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _result.setAddress(_tmpAddress);
        final String _tmpActivityName;
        if (_cursor.isNull(_cursorIndexOfActivityName)) {
          _tmpActivityName = null;
        } else {
          _tmpActivityName = _cursor.getString(_cursorIndexOfActivityName);
        }
        _result.setActivityName(_tmpActivityName);
        final String _tmpSync_status;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSync_status = null;
        } else {
          _tmpSync_status = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _result.setSync_status(_tmpSync_status);
        final double _tmpAltitude;
        _tmpAltitude = _cursor.getDouble(_cursorIndexOfAltitude);
        _result.setAltitude(_tmpAltitude);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _result.setViolation(_tmpViolation);
        final String _tmpCitation;
        if (_cursor.isNull(_cursorIndexOfCitation)) {
          _tmpCitation = null;
        } else {
          _tmpCitation = _cursor.getString(_cursorIndexOfCitation);
        }
        _result.setCitation(_tmpCitation);
        final double _tmpAccuracy;
        _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
        _result.setAccuracy(_tmpAccuracy);
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
  public List<DeviceData> getSyncedLocationUpdates() {
    final String _sql = "select * from FT_DeviceHistory where sync_status = 'S'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDutyId = CursorUtil.getColumnIndexOrThrow(_cursor, "dutyId");
      final int _cursorIndexOfDutyName = CursorUtil.getColumnIndexOrThrow(_cursor, "dutyName");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custId");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
      final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceName");
      final int _cursorIndexOfLattitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
      final int _cursorIndexOfDevice = CursorUtil.getColumnIndexOrThrow(_cursor, "device");
      final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timeStamp");
      final int _cursorIndexOfBadge = CursorUtil.getColumnIndexOrThrow(_cursor, "badge");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfFirstLogin = CursorUtil.getColumnIndexOrThrow(_cursor, "firstLogin");
      final int _cursorIndexOfLastTicketTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTicketTimeStamp");
      final int _cursorIndexOfCurrTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "currTimeStamp");
      final int _cursorIndexOfPushToken = CursorUtil.getColumnIndexOrThrow(_cursor, "pushToken");
      final int _cursorIndexOfModuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "moduleId");
      final int _cursorIndexOfDeviceInactivity = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceInactivity");
      final int _cursorIndexOfIsLoggedIn = CursorUtil.getColumnIndexOrThrow(_cursor, "isLoggedIn");
      final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "appVersion");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfActivityName = CursorUtil.getColumnIndexOrThrow(_cursor, "activityName");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final int _cursorIndexOfAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "altitude");
      final int _cursorIndexOfViolation = CursorUtil.getColumnIndexOrThrow(_cursor, "Violation");
      final int _cursorIndexOfCitation = CursorUtil.getColumnIndexOrThrow(_cursor, "Citation");
      final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
      final List<DeviceData> _result = new ArrayList<DeviceData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final DeviceData _item;
        _item = new DeviceData();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpDutyId;
        if (_cursor.isNull(_cursorIndexOfDutyId)) {
          _tmpDutyId = null;
        } else {
          _tmpDutyId = _cursor.getString(_cursorIndexOfDutyId);
        }
        _item.setDutyId(_tmpDutyId);
        final String _tmpDutyName;
        if (_cursor.isNull(_cursorIndexOfDutyName)) {
          _tmpDutyName = null;
        } else {
          _tmpDutyName = _cursor.getString(_cursorIndexOfDutyName);
        }
        _item.setDutyName(_tmpDutyName);
        final String _tmpCustId;
        if (_cursor.isNull(_cursorIndexOfCustId)) {
          _tmpCustId = null;
        } else {
          _tmpCustId = _cursor.getString(_cursorIndexOfCustId);
        }
        _item.setCustId(_tmpCustId);
        final String _tmpDeviceId;
        if (_cursor.isNull(_cursorIndexOfDeviceId)) {
          _tmpDeviceId = null;
        } else {
          _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
        }
        _item.setDeviceId(_tmpDeviceId);
        final String _tmpDeviceName;
        if (_cursor.isNull(_cursorIndexOfDeviceName)) {
          _tmpDeviceName = null;
        } else {
          _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
        }
        _item.setDeviceName(_tmpDeviceName);
        final double _tmpLattitude;
        _tmpLattitude = _cursor.getDouble(_cursorIndexOfLattitude);
        _item.setLattitude(_tmpLattitude);
        final double _tmpLongitude;
        _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        _item.setLongitude(_tmpLongitude);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpFullName;
        if (_cursor.isNull(_cursorIndexOfFullName)) {
          _tmpFullName = null;
        } else {
          _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
        }
        _item.setFullName(_tmpFullName);
        final String _tmpDevice;
        if (_cursor.isNull(_cursorIndexOfDevice)) {
          _tmpDevice = null;
        } else {
          _tmpDevice = _cursor.getString(_cursorIndexOfDevice);
        }
        _item.setDevice(_tmpDevice);
        final String _tmpTimeStamp;
        if (_cursor.isNull(_cursorIndexOfTimeStamp)) {
          _tmpTimeStamp = null;
        } else {
          _tmpTimeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
        }
        _item.setTimeStamp(_tmpTimeStamp);
        final String _tmpBadge;
        if (_cursor.isNull(_cursorIndexOfBadge)) {
          _tmpBadge = null;
        } else {
          _tmpBadge = _cursor.getString(_cursorIndexOfBadge);
        }
        _item.setBadge(_tmpBadge);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _item.setActive(_tmpIsActive);
        final String _tmpUserId;
        if (_cursor.isNull(_cursorIndexOfUserId)) {
          _tmpUserId = null;
        } else {
          _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        }
        _item.setUserId(_tmpUserId);
        final String _tmpFirstLogin;
        if (_cursor.isNull(_cursorIndexOfFirstLogin)) {
          _tmpFirstLogin = null;
        } else {
          _tmpFirstLogin = _cursor.getString(_cursorIndexOfFirstLogin);
        }
        _item.setFirstLogin(_tmpFirstLogin);
        final String _tmpLastTicketTimeStamp;
        if (_cursor.isNull(_cursorIndexOfLastTicketTimeStamp)) {
          _tmpLastTicketTimeStamp = null;
        } else {
          _tmpLastTicketTimeStamp = _cursor.getString(_cursorIndexOfLastTicketTimeStamp);
        }
        _item.setLastTicketTimeStamp(_tmpLastTicketTimeStamp);
        final String _tmpCurrTimeStamp;
        if (_cursor.isNull(_cursorIndexOfCurrTimeStamp)) {
          _tmpCurrTimeStamp = null;
        } else {
          _tmpCurrTimeStamp = _cursor.getString(_cursorIndexOfCurrTimeStamp);
        }
        _item.setCurrTimeStamp(_tmpCurrTimeStamp);
        final String _tmpPushToken;
        if (_cursor.isNull(_cursorIndexOfPushToken)) {
          _tmpPushToken = null;
        } else {
          _tmpPushToken = _cursor.getString(_cursorIndexOfPushToken);
        }
        _item.setPushToken(_tmpPushToken);
        final String _tmpModuleId;
        if (_cursor.isNull(_cursorIndexOfModuleId)) {
          _tmpModuleId = null;
        } else {
          _tmpModuleId = _cursor.getString(_cursorIndexOfModuleId);
        }
        _item.setModuleId(_tmpModuleId);
        final boolean _tmpDeviceInactivity;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfDeviceInactivity);
        _tmpDeviceInactivity = _tmp_1 != 0;
        _item.setDeviceInactivity(_tmpDeviceInactivity);
        final boolean _tmpIsLoggedIn;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIsLoggedIn);
        _tmpIsLoggedIn = _tmp_2 != 0;
        _item.setLoggedIn(_tmpIsLoggedIn);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setAppVersion(_tmpAppVersion);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _item.setAddress(_tmpAddress);
        final String _tmpActivityName;
        if (_cursor.isNull(_cursorIndexOfActivityName)) {
          _tmpActivityName = null;
        } else {
          _tmpActivityName = _cursor.getString(_cursorIndexOfActivityName);
        }
        _item.setActivityName(_tmpActivityName);
        final String _tmpSync_status;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSync_status = null;
        } else {
          _tmpSync_status = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _item.setSync_status(_tmpSync_status);
        final double _tmpAltitude;
        _tmpAltitude = _cursor.getDouble(_cursorIndexOfAltitude);
        _item.setAltitude(_tmpAltitude);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item.setViolation(_tmpViolation);
        final String _tmpCitation;
        if (_cursor.isNull(_cursorIndexOfCitation)) {
          _tmpCitation = null;
        } else {
          _tmpCitation = _cursor.getString(_cursorIndexOfCitation);
        }
        _item.setCitation(_tmpCitation);
        final double _tmpAccuracy;
        _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
        _item.setAccuracy(_tmpAccuracy);
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
