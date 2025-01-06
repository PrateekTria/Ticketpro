package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.VendorService;
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
public final class VendorServicesDao_Impl implements VendorServicesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<VendorService> __insertionAdapterOfVendorService;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public VendorServicesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVendorService = new EntityInsertionAdapter<VendorService>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `vendor_services` (`service_id`,`vendor_id`,`service_name`,`test_url`,`prod_url`,`params`,`security_key`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, VendorService value) {
        stmt.bindLong(1, value.getServiceId());
        stmt.bindLong(2, value.getVendorId());
        if (value.getServiceName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getServiceName());
        }
        if (value.getTestURL() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getTestURL());
        }
        if (value.getProdURL() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getProdURL());
        }
        if (value.getParams() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getParams());
        }
        if (value.getSecurityKey() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getSecurityKey());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from vendor_services";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from vendor_services where service_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertVendorService(final VendorService... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfVendorService.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertVendorService(final VendorService data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfVendorService.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertVendorServiceList(final List<VendorService> VendorServicesList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfVendorService.insert(VendorServicesList);
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
  public List<VendorService> getServices() {
    final String _sql = "select * from vendor_services";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfServiceId = CursorUtil.getColumnIndexOrThrow(_cursor, "service_id");
      final int _cursorIndexOfVendorId = CursorUtil.getColumnIndexOrThrow(_cursor, "vendor_id");
      final int _cursorIndexOfServiceName = CursorUtil.getColumnIndexOrThrow(_cursor, "service_name");
      final int _cursorIndexOfTestURL = CursorUtil.getColumnIndexOrThrow(_cursor, "test_url");
      final int _cursorIndexOfProdURL = CursorUtil.getColumnIndexOrThrow(_cursor, "prod_url");
      final int _cursorIndexOfParams = CursorUtil.getColumnIndexOrThrow(_cursor, "params");
      final int _cursorIndexOfSecurityKey = CursorUtil.getColumnIndexOrThrow(_cursor, "security_key");
      final List<VendorService> _result = new ArrayList<VendorService>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final VendorService _item;
        _item = new VendorService();
        final int _tmpServiceId;
        _tmpServiceId = _cursor.getInt(_cursorIndexOfServiceId);
        _item.setServiceId(_tmpServiceId);
        final int _tmpVendorId;
        _tmpVendorId = _cursor.getInt(_cursorIndexOfVendorId);
        _item.setVendorId(_tmpVendorId);
        final String _tmpServiceName;
        if (_cursor.isNull(_cursorIndexOfServiceName)) {
          _tmpServiceName = null;
        } else {
          _tmpServiceName = _cursor.getString(_cursorIndexOfServiceName);
        }
        _item.setServiceName(_tmpServiceName);
        final String _tmpTestURL;
        if (_cursor.isNull(_cursorIndexOfTestURL)) {
          _tmpTestURL = null;
        } else {
          _tmpTestURL = _cursor.getString(_cursorIndexOfTestURL);
        }
        _item.setTestURL(_tmpTestURL);
        final String _tmpProdURL;
        if (_cursor.isNull(_cursorIndexOfProdURL)) {
          _tmpProdURL = null;
        } else {
          _tmpProdURL = _cursor.getString(_cursorIndexOfProdURL);
        }
        _item.setProdURL(_tmpProdURL);
        final String _tmpParams;
        if (_cursor.isNull(_cursorIndexOfParams)) {
          _tmpParams = null;
        } else {
          _tmpParams = _cursor.getString(_cursorIndexOfParams);
        }
        _item.setParams(_tmpParams);
        final String _tmpSecurityKey;
        if (_cursor.isNull(_cursorIndexOfSecurityKey)) {
          _tmpSecurityKey = null;
        } else {
          _tmpSecurityKey = _cursor.getString(_cursorIndexOfSecurityKey);
        }
        _item.setSecurityKey(_tmpSecurityKey);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<VendorService> getServiceByName(final String serviceName) {
    final String _sql = "select * from vendor_services where service_name=?";
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
      final int _cursorIndexOfServiceId = CursorUtil.getColumnIndexOrThrow(_cursor, "service_id");
      final int _cursorIndexOfVendorId = CursorUtil.getColumnIndexOrThrow(_cursor, "vendor_id");
      final int _cursorIndexOfServiceName = CursorUtil.getColumnIndexOrThrow(_cursor, "service_name");
      final int _cursorIndexOfTestURL = CursorUtil.getColumnIndexOrThrow(_cursor, "test_url");
      final int _cursorIndexOfProdURL = CursorUtil.getColumnIndexOrThrow(_cursor, "prod_url");
      final int _cursorIndexOfParams = CursorUtil.getColumnIndexOrThrow(_cursor, "params");
      final int _cursorIndexOfSecurityKey = CursorUtil.getColumnIndexOrThrow(_cursor, "security_key");
      final List<VendorService> _result = new ArrayList<VendorService>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final VendorService _item;
        _item = new VendorService();
        final int _tmpServiceId;
        _tmpServiceId = _cursor.getInt(_cursorIndexOfServiceId);
        _item.setServiceId(_tmpServiceId);
        final int _tmpVendorId;
        _tmpVendorId = _cursor.getInt(_cursorIndexOfVendorId);
        _item.setVendorId(_tmpVendorId);
        final String _tmpServiceName;
        if (_cursor.isNull(_cursorIndexOfServiceName)) {
          _tmpServiceName = null;
        } else {
          _tmpServiceName = _cursor.getString(_cursorIndexOfServiceName);
        }
        _item.setServiceName(_tmpServiceName);
        final String _tmpTestURL;
        if (_cursor.isNull(_cursorIndexOfTestURL)) {
          _tmpTestURL = null;
        } else {
          _tmpTestURL = _cursor.getString(_cursorIndexOfTestURL);
        }
        _item.setTestURL(_tmpTestURL);
        final String _tmpProdURL;
        if (_cursor.isNull(_cursorIndexOfProdURL)) {
          _tmpProdURL = null;
        } else {
          _tmpProdURL = _cursor.getString(_cursorIndexOfProdURL);
        }
        _item.setProdURL(_tmpProdURL);
        final String _tmpParams;
        if (_cursor.isNull(_cursorIndexOfParams)) {
          _tmpParams = null;
        } else {
          _tmpParams = _cursor.getString(_cursorIndexOfParams);
        }
        _item.setParams(_tmpParams);
        final String _tmpSecurityKey;
        if (_cursor.isNull(_cursorIndexOfSecurityKey)) {
          _tmpSecurityKey = null;
        } else {
          _tmpSecurityKey = _cursor.getString(_cursorIndexOfSecurityKey);
        }
        _item.setSecurityKey(_tmpSecurityKey);
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
