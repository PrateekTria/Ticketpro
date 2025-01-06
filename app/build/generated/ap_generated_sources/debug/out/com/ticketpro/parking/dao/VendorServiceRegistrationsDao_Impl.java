package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.VendorServiceRegistration;
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
public final class VendorServiceRegistrationsDao_Impl implements VendorServiceRegistrationsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<VendorServiceRegistration> __insertionAdapterOfVendorServiceRegistration;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public VendorServiceRegistrationsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVendorServiceRegistration = new EntityInsertionAdapter<VendorServiceRegistration>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `vendor_service_registrations` (`registration_id`,`service_id`,`custid`,`device_id`,`userid`,`param_mappings`,`service_mode`,`is_active`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, VendorServiceRegistration value) {
        stmt.bindLong(1, value.getRegistrationId());
        stmt.bindLong(2, value.getServiceId());
        stmt.bindLong(3, value.getCustId());
        stmt.bindLong(4, value.getDeviceId());
        stmt.bindLong(5, value.getUserId());
        if (value.getParamMappings() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getParamMappings());
        }
        if (value.getServiceMode() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getServiceMode());
        }
        if (value.getIsActive() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getIsActive());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from vendor_service_registrations";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from vendor_service_registrations where registration_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertVendorServiceRegistration(final VendorServiceRegistration... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfVendorServiceRegistration.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertVendorServiceRegistration(final VendorServiceRegistration data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfVendorServiceRegistration.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertVendorServiceRegistrationList(
      final List<VendorServiceRegistration> VendorServiceRegistrationsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfVendorServiceRegistration.insert(VendorServiceRegistrationsList);
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
  public List<VendorServiceRegistration> getServiceRegistrations() {
    final String _sql = "select * from vendor_service_registrations";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRegistrationId = CursorUtil.getColumnIndexOrThrow(_cursor, "registration_id");
      final int _cursorIndexOfServiceId = CursorUtil.getColumnIndexOrThrow(_cursor, "service_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfParamMappings = CursorUtil.getColumnIndexOrThrow(_cursor, "param_mappings");
      final int _cursorIndexOfServiceMode = CursorUtil.getColumnIndexOrThrow(_cursor, "service_mode");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final List<VendorServiceRegistration> _result = new ArrayList<VendorServiceRegistration>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final VendorServiceRegistration _item;
        _item = new VendorServiceRegistration();
        final int _tmpRegistrationId;
        _tmpRegistrationId = _cursor.getInt(_cursorIndexOfRegistrationId);
        _item.setRegistrationId(_tmpRegistrationId);
        final int _tmpServiceId;
        _tmpServiceId = _cursor.getInt(_cursorIndexOfServiceId);
        _item.setServiceId(_tmpServiceId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final String _tmpParamMappings;
        if (_cursor.isNull(_cursorIndexOfParamMappings)) {
          _tmpParamMappings = null;
        } else {
          _tmpParamMappings = _cursor.getString(_cursorIndexOfParamMappings);
        }
        _item.setParamMappings(_tmpParamMappings);
        final String _tmpServiceMode;
        if (_cursor.isNull(_cursorIndexOfServiceMode)) {
          _tmpServiceMode = null;
        } else {
          _tmpServiceMode = _cursor.getString(_cursorIndexOfServiceMode);
        }
        _item.setServiceMode(_tmpServiceMode);
        final String _tmpIsActive;
        if (_cursor.isNull(_cursorIndexOfIsActive)) {
          _tmpIsActive = null;
        } else {
          _tmpIsActive = _cursor.getString(_cursorIndexOfIsActive);
        }
        _item.setIsActive(_tmpIsActive);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public VendorServiceRegistration getServiceRegistrationByName(final String serviceName) {
    final String _sql = "select * from vendor_service_registrations where service_id=(select service_id from vendor_services where service_name=?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (serviceName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, serviceName);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRegistrationId = CursorUtil.getColumnIndexOrThrow(_cursor, "registration_id");
      final int _cursorIndexOfServiceId = CursorUtil.getColumnIndexOrThrow(_cursor, "service_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfParamMappings = CursorUtil.getColumnIndexOrThrow(_cursor, "param_mappings");
      final int _cursorIndexOfServiceMode = CursorUtil.getColumnIndexOrThrow(_cursor, "service_mode");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final VendorServiceRegistration _result;
      if(_cursor.moveToFirst()) {
        _result = new VendorServiceRegistration();
        final int _tmpRegistrationId;
        _tmpRegistrationId = _cursor.getInt(_cursorIndexOfRegistrationId);
        _result.setRegistrationId(_tmpRegistrationId);
        final int _tmpServiceId;
        _tmpServiceId = _cursor.getInt(_cursorIndexOfServiceId);
        _result.setServiceId(_tmpServiceId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _result.setDeviceId(_tmpDeviceId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final String _tmpParamMappings;
        if (_cursor.isNull(_cursorIndexOfParamMappings)) {
          _tmpParamMappings = null;
        } else {
          _tmpParamMappings = _cursor.getString(_cursorIndexOfParamMappings);
        }
        _result.setParamMappings(_tmpParamMappings);
        final String _tmpServiceMode;
        if (_cursor.isNull(_cursorIndexOfServiceMode)) {
          _tmpServiceMode = null;
        } else {
          _tmpServiceMode = _cursor.getString(_cursorIndexOfServiceMode);
        }
        _result.setServiceMode(_tmpServiceMode);
        final String _tmpIsActive;
        if (_cursor.isNull(_cursorIndexOfIsActive)) {
          _tmpIsActive = null;
        } else {
          _tmpIsActive = _cursor.getString(_cursorIndexOfIsActive);
        }
        _result.setIsActive(_tmpIsActive);
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
  public VendorServiceRegistration getServiceRegistrationByServiceId(final int serviceId,
      final int deviceId) {
    final String _sql = "select * from vendor_service_registrations where service_id=? and device_id in(?,null,0)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, serviceId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, deviceId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRegistrationId = CursorUtil.getColumnIndexOrThrow(_cursor, "registration_id");
      final int _cursorIndexOfServiceId = CursorUtil.getColumnIndexOrThrow(_cursor, "service_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfParamMappings = CursorUtil.getColumnIndexOrThrow(_cursor, "param_mappings");
      final int _cursorIndexOfServiceMode = CursorUtil.getColumnIndexOrThrow(_cursor, "service_mode");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final VendorServiceRegistration _result;
      if(_cursor.moveToFirst()) {
        _result = new VendorServiceRegistration();
        final int _tmpRegistrationId;
        _tmpRegistrationId = _cursor.getInt(_cursorIndexOfRegistrationId);
        _result.setRegistrationId(_tmpRegistrationId);
        final int _tmpServiceId;
        _tmpServiceId = _cursor.getInt(_cursorIndexOfServiceId);
        _result.setServiceId(_tmpServiceId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _result.setDeviceId(_tmpDeviceId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final String _tmpParamMappings;
        if (_cursor.isNull(_cursorIndexOfParamMappings)) {
          _tmpParamMappings = null;
        } else {
          _tmpParamMappings = _cursor.getString(_cursorIndexOfParamMappings);
        }
        _result.setParamMappings(_tmpParamMappings);
        final String _tmpServiceMode;
        if (_cursor.isNull(_cursorIndexOfServiceMode)) {
          _tmpServiceMode = null;
        } else {
          _tmpServiceMode = _cursor.getString(_cursorIndexOfServiceMode);
        }
        _result.setServiceMode(_tmpServiceMode);
        final String _tmpIsActive;
        if (_cursor.isNull(_cursorIndexOfIsActive)) {
          _tmpIsActive = null;
        } else {
          _tmpIsActive = _cursor.getString(_cursorIndexOfIsActive);
        }
        _result.setIsActive(_tmpIsActive);
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
  public List<VendorServiceRegistration> getServiceRegistrationByServiceId1(final int serviceId,
      final int deviceId) {
    final String _sql = "select * from vendor_service_registrations where service_id=? and device_id in(?,null,0)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, serviceId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, deviceId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRegistrationId = CursorUtil.getColumnIndexOrThrow(_cursor, "registration_id");
      final int _cursorIndexOfServiceId = CursorUtil.getColumnIndexOrThrow(_cursor, "service_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfParamMappings = CursorUtil.getColumnIndexOrThrow(_cursor, "param_mappings");
      final int _cursorIndexOfServiceMode = CursorUtil.getColumnIndexOrThrow(_cursor, "service_mode");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final List<VendorServiceRegistration> _result = new ArrayList<VendorServiceRegistration>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final VendorServiceRegistration _item;
        _item = new VendorServiceRegistration();
        final int _tmpRegistrationId;
        _tmpRegistrationId = _cursor.getInt(_cursorIndexOfRegistrationId);
        _item.setRegistrationId(_tmpRegistrationId);
        final int _tmpServiceId;
        _tmpServiceId = _cursor.getInt(_cursorIndexOfServiceId);
        _item.setServiceId(_tmpServiceId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final String _tmpParamMappings;
        if (_cursor.isNull(_cursorIndexOfParamMappings)) {
          _tmpParamMappings = null;
        } else {
          _tmpParamMappings = _cursor.getString(_cursorIndexOfParamMappings);
        }
        _item.setParamMappings(_tmpParamMappings);
        final String _tmpServiceMode;
        if (_cursor.isNull(_cursorIndexOfServiceMode)) {
          _tmpServiceMode = null;
        } else {
          _tmpServiceMode = _cursor.getString(_cursorIndexOfServiceMode);
        }
        _item.setServiceMode(_tmpServiceMode);
        final String _tmpIsActive;
        if (_cursor.isNull(_cursorIndexOfIsActive)) {
          _tmpIsActive = null;
        } else {
          _tmpIsActive = _cursor.getString(_cursorIndexOfIsActive);
        }
        _item.setIsActive(_tmpIsActive);
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
