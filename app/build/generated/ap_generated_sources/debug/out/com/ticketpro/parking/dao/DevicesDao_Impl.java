package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.DeviceInfo;
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
public final class DevicesDao_Impl implements DevicesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DeviceInfo> __insertionAdapterOfDeviceInfo;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastTicketTime;

  public DevicesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDeviceInfo = new EntityInsertionAdapter<DeviceInfo>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `devices` (`device_id`,`custid`,`device_name`,`device`,`app_version`,`os_version`,`platform`,`last_sync`,`lastTicketTime`,`start_citation_number`,`current_citation_number`,`end_citation_number`,`start_warning_number`,`current_warning_number`,`end_warning_number`,`start_photo_number`,`current_photo_number`,`end_photo_number`,`default_template_id`,`gcm_registration_id`,`default_printer_name`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DeviceInfo value) {
        stmt.bindLong(1, value.getDeviceId());
        stmt.bindLong(2, value.getCustId());
        if (value.getDeviceName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDeviceName());
        }
        if (value.getDevice() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDevice());
        }
        if (value.getAppVersion() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getAppVersion());
        }
        if (value.getOsVersion() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getOsVersion());
        }
        if (value.getPlatform() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getPlatform());
        }
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getLastSync());
        if (_tmp == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindLong(8, _tmp);
        }
        if (value.getLastTicketTime() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getLastTicketTime());
        }
        stmt.bindLong(10, value.getStartCitationNumber());
        stmt.bindLong(11, value.getCurrentCitationNumber());
        stmt.bindLong(12, value.getEndCitationNumber());
        stmt.bindLong(13, value.getStartWarningNumber());
        stmt.bindLong(14, value.getCurrentWarningNumber());
        stmt.bindLong(15, value.getEndWarningNumber());
        stmt.bindLong(16, value.getStartPhotoNumber());
        stmt.bindLong(17, value.getCurrentPhotoNumber());
        stmt.bindLong(18, value.getEndPhotoNumber());
        stmt.bindLong(19, value.getDefaultTemplateId());
        if (value.getGCMRegistrationId() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.getGCMRegistrationId());
        }
        if (value.getDefaultPrinterName() == null) {
          stmt.bindNull(21);
        } else {
          stmt.bindString(21, value.getDefaultPrinterName());
        }
      }
    };
    this.__preparedStmtOfUpdateLastTicketTime = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update devices SET lastTicketTime= ? where device_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertDeviceInfo(final DeviceInfo... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDeviceInfo.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertDeviceInfo(final DeviceInfo data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDeviceInfo.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void updateLastTicketTime(final int deviceId, final String timeStamp) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastTicketTime.acquire();
    int _argIndex = 1;
    if (timeStamp == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, timeStamp);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, deviceId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateLastTicketTime.release(_stmt);
    }
  }

  @Override
  public DeviceInfo getDeviceInfo(final String deviceName) {
    final String _sql = "select * from devices where device_name=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (deviceName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, deviceName);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
      final int _cursorIndexOfDevice = CursorUtil.getColumnIndexOrThrow(_cursor, "device");
      final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "app_version");
      final int _cursorIndexOfOsVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "os_version");
      final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
      final int _cursorIndexOfLastSync = CursorUtil.getColumnIndexOrThrow(_cursor, "last_sync");
      final int _cursorIndexOfLastTicketTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTicketTime");
      final int _cursorIndexOfStartCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "start_citation_number");
      final int _cursorIndexOfCurrentCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "current_citation_number");
      final int _cursorIndexOfEndCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "end_citation_number");
      final int _cursorIndexOfStartWarningNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "start_warning_number");
      final int _cursorIndexOfCurrentWarningNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "current_warning_number");
      final int _cursorIndexOfEndWarningNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "end_warning_number");
      final int _cursorIndexOfStartPhotoNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "start_photo_number");
      final int _cursorIndexOfCurrentPhotoNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "current_photo_number");
      final int _cursorIndexOfEndPhotoNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "end_photo_number");
      final int _cursorIndexOfDefaultTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "default_template_id");
      final int _cursorIndexOfGCMRegistrationId = CursorUtil.getColumnIndexOrThrow(_cursor, "gcm_registration_id");
      final int _cursorIndexOfDefaultPrinterName = CursorUtil.getColumnIndexOrThrow(_cursor, "default_printer_name");
      final DeviceInfo _result;
      if(_cursor.moveToFirst()) {
        _result = new DeviceInfo();
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _result.setDeviceId(_tmpDeviceId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpDeviceName;
        if (_cursor.isNull(_cursorIndexOfDeviceName)) {
          _tmpDeviceName = null;
        } else {
          _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
        }
        _result.setDeviceName(_tmpDeviceName);
        final String _tmpDevice;
        if (_cursor.isNull(_cursorIndexOfDevice)) {
          _tmpDevice = null;
        } else {
          _tmpDevice = _cursor.getString(_cursorIndexOfDevice);
        }
        _result.setDevice(_tmpDevice);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _result.setAppVersion(_tmpAppVersion);
        final String _tmpOsVersion;
        if (_cursor.isNull(_cursorIndexOfOsVersion)) {
          _tmpOsVersion = null;
        } else {
          _tmpOsVersion = _cursor.getString(_cursorIndexOfOsVersion);
        }
        _result.setOsVersion(_tmpOsVersion);
        final String _tmpPlatform;
        if (_cursor.isNull(_cursorIndexOfPlatform)) {
          _tmpPlatform = null;
        } else {
          _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
        }
        _result.setPlatform(_tmpPlatform);
        final Date _tmpLastSync;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfLastSync)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfLastSync);
        }
        _tmpLastSync = Converters.fromTimestamp(_tmp);
        _result.setLastSync(_tmpLastSync);
        final String _tmpLastTicketTime;
        if (_cursor.isNull(_cursorIndexOfLastTicketTime)) {
          _tmpLastTicketTime = null;
        } else {
          _tmpLastTicketTime = _cursor.getString(_cursorIndexOfLastTicketTime);
        }
        _result.setLastTicketTime(_tmpLastTicketTime);
        final long _tmpStartCitationNumber;
        _tmpStartCitationNumber = _cursor.getLong(_cursorIndexOfStartCitationNumber);
        _result.setStartCitationNumber(_tmpStartCitationNumber);
        final long _tmpCurrentCitationNumber;
        _tmpCurrentCitationNumber = _cursor.getLong(_cursorIndexOfCurrentCitationNumber);
        _result.setCurrentCitationNumber(_tmpCurrentCitationNumber);
        final long _tmpEndCitationNumber;
        _tmpEndCitationNumber = _cursor.getLong(_cursorIndexOfEndCitationNumber);
        _result.setEndCitationNumber(_tmpEndCitationNumber);
        final long _tmpStartWarningNumber;
        _tmpStartWarningNumber = _cursor.getLong(_cursorIndexOfStartWarningNumber);
        _result.setStartWarningNumber(_tmpStartWarningNumber);
        final long _tmpCurrentWarningNumber;
        _tmpCurrentWarningNumber = _cursor.getLong(_cursorIndexOfCurrentWarningNumber);
        _result.setCurrentWarningNumber(_tmpCurrentWarningNumber);
        final long _tmpEndWarningNumber;
        _tmpEndWarningNumber = _cursor.getLong(_cursorIndexOfEndWarningNumber);
        _result.setEndWarningNumber(_tmpEndWarningNumber);
        final long _tmpStartPhotoNumber;
        _tmpStartPhotoNumber = _cursor.getLong(_cursorIndexOfStartPhotoNumber);
        _result.setStartPhotoNumber(_tmpStartPhotoNumber);
        final long _tmpCurrentPhotoNumber;
        _tmpCurrentPhotoNumber = _cursor.getLong(_cursorIndexOfCurrentPhotoNumber);
        _result.setCurrentPhotoNumber(_tmpCurrentPhotoNumber);
        final long _tmpEndPhotoNumber;
        _tmpEndPhotoNumber = _cursor.getLong(_cursorIndexOfEndPhotoNumber);
        _result.setEndPhotoNumber(_tmpEndPhotoNumber);
        final int _tmpDefaultTemplateId;
        _tmpDefaultTemplateId = _cursor.getInt(_cursorIndexOfDefaultTemplateId);
        _result.setDefaultTemplateId(_tmpDefaultTemplateId);
        final String _tmpGCMRegistrationId;
        if (_cursor.isNull(_cursorIndexOfGCMRegistrationId)) {
          _tmpGCMRegistrationId = null;
        } else {
          _tmpGCMRegistrationId = _cursor.getString(_cursorIndexOfGCMRegistrationId);
        }
        _result.setGCMRegistrationId(_tmpGCMRegistrationId);
        final String _tmpDefaultPrinterName;
        if (_cursor.isNull(_cursorIndexOfDefaultPrinterName)) {
          _tmpDefaultPrinterName = null;
        } else {
          _tmpDefaultPrinterName = _cursor.getString(_cursorIndexOfDefaultPrinterName);
        }
        _result.setDefaultPrinterName(_tmpDefaultPrinterName);
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
  public String getLastTicketTime(final int deviceId) {
    final String _sql = "select lastTicketTime from devices where device_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deviceId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final String _result;
      if(_cursor.moveToFirst()) {
        if (_cursor.isNull(0)) {
          _result = null;
        } else {
          _result = _cursor.getString(0);
        }
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
  public DeviceInfo getDeviceInfoById(final int deviceId) {
    final String _sql = "select * from devices where device_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deviceId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
      final int _cursorIndexOfDevice = CursorUtil.getColumnIndexOrThrow(_cursor, "device");
      final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "app_version");
      final int _cursorIndexOfOsVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "os_version");
      final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
      final int _cursorIndexOfLastSync = CursorUtil.getColumnIndexOrThrow(_cursor, "last_sync");
      final int _cursorIndexOfLastTicketTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTicketTime");
      final int _cursorIndexOfStartCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "start_citation_number");
      final int _cursorIndexOfCurrentCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "current_citation_number");
      final int _cursorIndexOfEndCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "end_citation_number");
      final int _cursorIndexOfStartWarningNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "start_warning_number");
      final int _cursorIndexOfCurrentWarningNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "current_warning_number");
      final int _cursorIndexOfEndWarningNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "end_warning_number");
      final int _cursorIndexOfStartPhotoNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "start_photo_number");
      final int _cursorIndexOfCurrentPhotoNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "current_photo_number");
      final int _cursorIndexOfEndPhotoNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "end_photo_number");
      final int _cursorIndexOfDefaultTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "default_template_id");
      final int _cursorIndexOfGCMRegistrationId = CursorUtil.getColumnIndexOrThrow(_cursor, "gcm_registration_id");
      final int _cursorIndexOfDefaultPrinterName = CursorUtil.getColumnIndexOrThrow(_cursor, "default_printer_name");
      final DeviceInfo _result;
      if(_cursor.moveToFirst()) {
        _result = new DeviceInfo();
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _result.setDeviceId(_tmpDeviceId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpDeviceName;
        if (_cursor.isNull(_cursorIndexOfDeviceName)) {
          _tmpDeviceName = null;
        } else {
          _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
        }
        _result.setDeviceName(_tmpDeviceName);
        final String _tmpDevice;
        if (_cursor.isNull(_cursorIndexOfDevice)) {
          _tmpDevice = null;
        } else {
          _tmpDevice = _cursor.getString(_cursorIndexOfDevice);
        }
        _result.setDevice(_tmpDevice);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _result.setAppVersion(_tmpAppVersion);
        final String _tmpOsVersion;
        if (_cursor.isNull(_cursorIndexOfOsVersion)) {
          _tmpOsVersion = null;
        } else {
          _tmpOsVersion = _cursor.getString(_cursorIndexOfOsVersion);
        }
        _result.setOsVersion(_tmpOsVersion);
        final String _tmpPlatform;
        if (_cursor.isNull(_cursorIndexOfPlatform)) {
          _tmpPlatform = null;
        } else {
          _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
        }
        _result.setPlatform(_tmpPlatform);
        final Date _tmpLastSync;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfLastSync)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfLastSync);
        }
        _tmpLastSync = Converters.fromTimestamp(_tmp);
        _result.setLastSync(_tmpLastSync);
        final String _tmpLastTicketTime;
        if (_cursor.isNull(_cursorIndexOfLastTicketTime)) {
          _tmpLastTicketTime = null;
        } else {
          _tmpLastTicketTime = _cursor.getString(_cursorIndexOfLastTicketTime);
        }
        _result.setLastTicketTime(_tmpLastTicketTime);
        final long _tmpStartCitationNumber;
        _tmpStartCitationNumber = _cursor.getLong(_cursorIndexOfStartCitationNumber);
        _result.setStartCitationNumber(_tmpStartCitationNumber);
        final long _tmpCurrentCitationNumber;
        _tmpCurrentCitationNumber = _cursor.getLong(_cursorIndexOfCurrentCitationNumber);
        _result.setCurrentCitationNumber(_tmpCurrentCitationNumber);
        final long _tmpEndCitationNumber;
        _tmpEndCitationNumber = _cursor.getLong(_cursorIndexOfEndCitationNumber);
        _result.setEndCitationNumber(_tmpEndCitationNumber);
        final long _tmpStartWarningNumber;
        _tmpStartWarningNumber = _cursor.getLong(_cursorIndexOfStartWarningNumber);
        _result.setStartWarningNumber(_tmpStartWarningNumber);
        final long _tmpCurrentWarningNumber;
        _tmpCurrentWarningNumber = _cursor.getLong(_cursorIndexOfCurrentWarningNumber);
        _result.setCurrentWarningNumber(_tmpCurrentWarningNumber);
        final long _tmpEndWarningNumber;
        _tmpEndWarningNumber = _cursor.getLong(_cursorIndexOfEndWarningNumber);
        _result.setEndWarningNumber(_tmpEndWarningNumber);
        final long _tmpStartPhotoNumber;
        _tmpStartPhotoNumber = _cursor.getLong(_cursorIndexOfStartPhotoNumber);
        _result.setStartPhotoNumber(_tmpStartPhotoNumber);
        final long _tmpCurrentPhotoNumber;
        _tmpCurrentPhotoNumber = _cursor.getLong(_cursorIndexOfCurrentPhotoNumber);
        _result.setCurrentPhotoNumber(_tmpCurrentPhotoNumber);
        final long _tmpEndPhotoNumber;
        _tmpEndPhotoNumber = _cursor.getLong(_cursorIndexOfEndPhotoNumber);
        _result.setEndPhotoNumber(_tmpEndPhotoNumber);
        final int _tmpDefaultTemplateId;
        _tmpDefaultTemplateId = _cursor.getInt(_cursorIndexOfDefaultTemplateId);
        _result.setDefaultTemplateId(_tmpDefaultTemplateId);
        final String _tmpGCMRegistrationId;
        if (_cursor.isNull(_cursorIndexOfGCMRegistrationId)) {
          _tmpGCMRegistrationId = null;
        } else {
          _tmpGCMRegistrationId = _cursor.getString(_cursorIndexOfGCMRegistrationId);
        }
        _result.setGCMRegistrationId(_tmpGCMRegistrationId);
        final String _tmpDefaultPrinterName;
        if (_cursor.isNull(_cursorIndexOfDefaultPrinterName)) {
          _tmpDefaultPrinterName = null;
        } else {
          _tmpDefaultPrinterName = _cursor.getString(_cursorIndexOfDefaultPrinterName);
        }
        _result.setDefaultPrinterName(_tmpDefaultPrinterName);
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
  public List<DeviceInfo> getDevices() {
    final String _sql = "select * from devices";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
      final int _cursorIndexOfDevice = CursorUtil.getColumnIndexOrThrow(_cursor, "device");
      final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "app_version");
      final int _cursorIndexOfOsVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "os_version");
      final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
      final int _cursorIndexOfLastSync = CursorUtil.getColumnIndexOrThrow(_cursor, "last_sync");
      final int _cursorIndexOfLastTicketTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTicketTime");
      final int _cursorIndexOfStartCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "start_citation_number");
      final int _cursorIndexOfCurrentCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "current_citation_number");
      final int _cursorIndexOfEndCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "end_citation_number");
      final int _cursorIndexOfStartWarningNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "start_warning_number");
      final int _cursorIndexOfCurrentWarningNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "current_warning_number");
      final int _cursorIndexOfEndWarningNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "end_warning_number");
      final int _cursorIndexOfStartPhotoNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "start_photo_number");
      final int _cursorIndexOfCurrentPhotoNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "current_photo_number");
      final int _cursorIndexOfEndPhotoNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "end_photo_number");
      final int _cursorIndexOfDefaultTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "default_template_id");
      final int _cursorIndexOfGCMRegistrationId = CursorUtil.getColumnIndexOrThrow(_cursor, "gcm_registration_id");
      final int _cursorIndexOfDefaultPrinterName = CursorUtil.getColumnIndexOrThrow(_cursor, "default_printer_name");
      final List<DeviceInfo> _result = new ArrayList<DeviceInfo>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final DeviceInfo _item;
        _item = new DeviceInfo();
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpDeviceName;
        if (_cursor.isNull(_cursorIndexOfDeviceName)) {
          _tmpDeviceName = null;
        } else {
          _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
        }
        _item.setDeviceName(_tmpDeviceName);
        final String _tmpDevice;
        if (_cursor.isNull(_cursorIndexOfDevice)) {
          _tmpDevice = null;
        } else {
          _tmpDevice = _cursor.getString(_cursorIndexOfDevice);
        }
        _item.setDevice(_tmpDevice);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setAppVersion(_tmpAppVersion);
        final String _tmpOsVersion;
        if (_cursor.isNull(_cursorIndexOfOsVersion)) {
          _tmpOsVersion = null;
        } else {
          _tmpOsVersion = _cursor.getString(_cursorIndexOfOsVersion);
        }
        _item.setOsVersion(_tmpOsVersion);
        final String _tmpPlatform;
        if (_cursor.isNull(_cursorIndexOfPlatform)) {
          _tmpPlatform = null;
        } else {
          _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
        }
        _item.setPlatform(_tmpPlatform);
        final Date _tmpLastSync;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfLastSync)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfLastSync);
        }
        _tmpLastSync = Converters.fromTimestamp(_tmp);
        _item.setLastSync(_tmpLastSync);
        final String _tmpLastTicketTime;
        if (_cursor.isNull(_cursorIndexOfLastTicketTime)) {
          _tmpLastTicketTime = null;
        } else {
          _tmpLastTicketTime = _cursor.getString(_cursorIndexOfLastTicketTime);
        }
        _item.setLastTicketTime(_tmpLastTicketTime);
        final long _tmpStartCitationNumber;
        _tmpStartCitationNumber = _cursor.getLong(_cursorIndexOfStartCitationNumber);
        _item.setStartCitationNumber(_tmpStartCitationNumber);
        final long _tmpCurrentCitationNumber;
        _tmpCurrentCitationNumber = _cursor.getLong(_cursorIndexOfCurrentCitationNumber);
        _item.setCurrentCitationNumber(_tmpCurrentCitationNumber);
        final long _tmpEndCitationNumber;
        _tmpEndCitationNumber = _cursor.getLong(_cursorIndexOfEndCitationNumber);
        _item.setEndCitationNumber(_tmpEndCitationNumber);
        final long _tmpStartWarningNumber;
        _tmpStartWarningNumber = _cursor.getLong(_cursorIndexOfStartWarningNumber);
        _item.setStartWarningNumber(_tmpStartWarningNumber);
        final long _tmpCurrentWarningNumber;
        _tmpCurrentWarningNumber = _cursor.getLong(_cursorIndexOfCurrentWarningNumber);
        _item.setCurrentWarningNumber(_tmpCurrentWarningNumber);
        final long _tmpEndWarningNumber;
        _tmpEndWarningNumber = _cursor.getLong(_cursorIndexOfEndWarningNumber);
        _item.setEndWarningNumber(_tmpEndWarningNumber);
        final long _tmpStartPhotoNumber;
        _tmpStartPhotoNumber = _cursor.getLong(_cursorIndexOfStartPhotoNumber);
        _item.setStartPhotoNumber(_tmpStartPhotoNumber);
        final long _tmpCurrentPhotoNumber;
        _tmpCurrentPhotoNumber = _cursor.getLong(_cursorIndexOfCurrentPhotoNumber);
        _item.setCurrentPhotoNumber(_tmpCurrentPhotoNumber);
        final long _tmpEndPhotoNumber;
        _tmpEndPhotoNumber = _cursor.getLong(_cursorIndexOfEndPhotoNumber);
        _item.setEndPhotoNumber(_tmpEndPhotoNumber);
        final int _tmpDefaultTemplateId;
        _tmpDefaultTemplateId = _cursor.getInt(_cursorIndexOfDefaultTemplateId);
        _item.setDefaultTemplateId(_tmpDefaultTemplateId);
        final String _tmpGCMRegistrationId;
        if (_cursor.isNull(_cursorIndexOfGCMRegistrationId)) {
          _tmpGCMRegistrationId = null;
        } else {
          _tmpGCMRegistrationId = _cursor.getString(_cursorIndexOfGCMRegistrationId);
        }
        _item.setGCMRegistrationId(_tmpGCMRegistrationId);
        final String _tmpDefaultPrinterName;
        if (_cursor.isNull(_cursorIndexOfDefaultPrinterName)) {
          _tmpDefaultPrinterName = null;
        } else {
          _tmpDefaultPrinterName = _cursor.getString(_cursorIndexOfDefaultPrinterName);
        }
        _item.setDefaultPrinterName(_tmpDefaultPrinterName);
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
