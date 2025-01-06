package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.VendorItem;
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
public final class VendorItemsDao_Impl implements VendorItemsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<VendorItem> __insertionAdapterOfVendorItem;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public VendorItemsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVendorItem = new EntityInsertionAdapter<VendorItem>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `vendor_items` (`item_id`,`custid`,`vendor_id`,`item_code`,`item_name`,`item_type`,`duration`,`violation_id`,`is_active`,`order_number`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, VendorItem value) {
        stmt.bindLong(1, value.getItemId());
        stmt.bindLong(2, value.getCustId());
        stmt.bindLong(3, value.getVendorId());
        if (value.getItemCode() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getItemCode());
        }
        if (value.getItemName() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getItemName());
        }
        if (value.getItemType() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getItemType());
        }
        stmt.bindLong(7, value.getDuration());
        stmt.bindLong(8, value.getViolationId());
        if (value.getIsActive() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getIsActive());
        }
        stmt.bindLong(10, value.getOrderNumber());
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from vendor_items";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from vendor_items where item_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertVendorItem(final VendorItem... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfVendorItem.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertVendorItem(final VendorItem data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfVendorItem.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertVendorItemList(final List<VendorItem> VendorItemsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfVendorItem.insert(VendorItemsList);
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
  public List<VendorItem> getVendorItems() {
    final String _sql = "select * from vendor_items order by order_number";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "item_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfVendorId = CursorUtil.getColumnIndexOrThrow(_cursor, "vendor_id");
      final int _cursorIndexOfItemCode = CursorUtil.getColumnIndexOrThrow(_cursor, "item_code");
      final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "item_name");
      final int _cursorIndexOfItemType = CursorUtil.getColumnIndexOrThrow(_cursor, "item_type");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final List<VendorItem> _result = new ArrayList<VendorItem>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final VendorItem _item;
        _item = new VendorItem();
        final int _tmpItemId;
        _tmpItemId = _cursor.getInt(_cursorIndexOfItemId);
        _item.setItemId(_tmpItemId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpVendorId;
        _tmpVendorId = _cursor.getInt(_cursorIndexOfVendorId);
        _item.setVendorId(_tmpVendorId);
        final String _tmpItemCode;
        if (_cursor.isNull(_cursorIndexOfItemCode)) {
          _tmpItemCode = null;
        } else {
          _tmpItemCode = _cursor.getString(_cursorIndexOfItemCode);
        }
        _item.setItemCode(_tmpItemCode);
        final String _tmpItemName;
        if (_cursor.isNull(_cursorIndexOfItemName)) {
          _tmpItemName = null;
        } else {
          _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        }
        _item.setItemName(_tmpItemName);
        final String _tmpItemType;
        if (_cursor.isNull(_cursorIndexOfItemType)) {
          _tmpItemType = null;
        } else {
          _tmpItemType = _cursor.getString(_cursorIndexOfItemType);
        }
        _item.setItemType(_tmpItemType);
        final int _tmpDuration;
        _tmpDuration = _cursor.getInt(_cursorIndexOfDuration);
        _item.setDuration(_tmpDuration);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final String _tmpIsActive;
        if (_cursor.isNull(_cursorIndexOfIsActive)) {
          _tmpIsActive = null;
        } else {
          _tmpIsActive = _cursor.getString(_cursorIndexOfIsActive);
        }
        _item.setIsActive(_tmpIsActive);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _item.setOrderNumber(_tmpOrderNumber);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<VendorItem> getVendorZones(final int vendorId) {
    final String _sql = "select * from vendor_items where item_type='zone' and vendor_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, vendorId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "item_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfVendorId = CursorUtil.getColumnIndexOrThrow(_cursor, "vendor_id");
      final int _cursorIndexOfItemCode = CursorUtil.getColumnIndexOrThrow(_cursor, "item_code");
      final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "item_name");
      final int _cursorIndexOfItemType = CursorUtil.getColumnIndexOrThrow(_cursor, "item_type");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final List<VendorItem> _result = new ArrayList<VendorItem>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final VendorItem _item;
        _item = new VendorItem();
        final int _tmpItemId;
        _tmpItemId = _cursor.getInt(_cursorIndexOfItemId);
        _item.setItemId(_tmpItemId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpVendorId;
        _tmpVendorId = _cursor.getInt(_cursorIndexOfVendorId);
        _item.setVendorId(_tmpVendorId);
        final String _tmpItemCode;
        if (_cursor.isNull(_cursorIndexOfItemCode)) {
          _tmpItemCode = null;
        } else {
          _tmpItemCode = _cursor.getString(_cursorIndexOfItemCode);
        }
        _item.setItemCode(_tmpItemCode);
        final String _tmpItemName;
        if (_cursor.isNull(_cursorIndexOfItemName)) {
          _tmpItemName = null;
        } else {
          _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        }
        _item.setItemName(_tmpItemName);
        final String _tmpItemType;
        if (_cursor.isNull(_cursorIndexOfItemType)) {
          _tmpItemType = null;
        } else {
          _tmpItemType = _cursor.getString(_cursorIndexOfItemType);
        }
        _item.setItemType(_tmpItemType);
        final int _tmpDuration;
        _tmpDuration = _cursor.getInt(_cursorIndexOfDuration);
        _item.setDuration(_tmpDuration);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final String _tmpIsActive;
        if (_cursor.isNull(_cursorIndexOfIsActive)) {
          _tmpIsActive = null;
        } else {
          _tmpIsActive = _cursor.getString(_cursorIndexOfIsActive);
        }
        _item.setIsActive(_tmpIsActive);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _item.setOrderNumber(_tmpOrderNumber);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<VendorItem> getVendorSamtrans(final int vendorId) {
    final String _sql = "select * from vendor_items where vendor_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, vendorId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "item_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfVendorId = CursorUtil.getColumnIndexOrThrow(_cursor, "vendor_id");
      final int _cursorIndexOfItemCode = CursorUtil.getColumnIndexOrThrow(_cursor, "item_code");
      final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "item_name");
      final int _cursorIndexOfItemType = CursorUtil.getColumnIndexOrThrow(_cursor, "item_type");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final List<VendorItem> _result = new ArrayList<VendorItem>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final VendorItem _item;
        _item = new VendorItem();
        final int _tmpItemId;
        _tmpItemId = _cursor.getInt(_cursorIndexOfItemId);
        _item.setItemId(_tmpItemId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpVendorId;
        _tmpVendorId = _cursor.getInt(_cursorIndexOfVendorId);
        _item.setVendorId(_tmpVendorId);
        final String _tmpItemCode;
        if (_cursor.isNull(_cursorIndexOfItemCode)) {
          _tmpItemCode = null;
        } else {
          _tmpItemCode = _cursor.getString(_cursorIndexOfItemCode);
        }
        _item.setItemCode(_tmpItemCode);
        final String _tmpItemName;
        if (_cursor.isNull(_cursorIndexOfItemName)) {
          _tmpItemName = null;
        } else {
          _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        }
        _item.setItemName(_tmpItemName);
        final String _tmpItemType;
        if (_cursor.isNull(_cursorIndexOfItemType)) {
          _tmpItemType = null;
        } else {
          _tmpItemType = _cursor.getString(_cursorIndexOfItemType);
        }
        _item.setItemType(_tmpItemType);
        final int _tmpDuration;
        _tmpDuration = _cursor.getInt(_cursorIndexOfDuration);
        _item.setDuration(_tmpDuration);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final String _tmpIsActive;
        if (_cursor.isNull(_cursorIndexOfIsActive)) {
          _tmpIsActive = null;
        } else {
          _tmpIsActive = _cursor.getString(_cursorIndexOfIsActive);
        }
        _item.setIsActive(_tmpIsActive);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _item.setOrderNumber(_tmpOrderNumber);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<VendorItem> getVendorItems(final int vendorId, final String itemType) {
    final String _sql = "select * from vendor_items where item_type=? and vendor_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (itemType == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, itemType);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, vendorId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "item_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfVendorId = CursorUtil.getColumnIndexOrThrow(_cursor, "vendor_id");
      final int _cursorIndexOfItemCode = CursorUtil.getColumnIndexOrThrow(_cursor, "item_code");
      final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "item_name");
      final int _cursorIndexOfItemType = CursorUtil.getColumnIndexOrThrow(_cursor, "item_type");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final List<VendorItem> _result = new ArrayList<VendorItem>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final VendorItem _item;
        _item = new VendorItem();
        final int _tmpItemId;
        _tmpItemId = _cursor.getInt(_cursorIndexOfItemId);
        _item.setItemId(_tmpItemId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpVendorId;
        _tmpVendorId = _cursor.getInt(_cursorIndexOfVendorId);
        _item.setVendorId(_tmpVendorId);
        final String _tmpItemCode;
        if (_cursor.isNull(_cursorIndexOfItemCode)) {
          _tmpItemCode = null;
        } else {
          _tmpItemCode = _cursor.getString(_cursorIndexOfItemCode);
        }
        _item.setItemCode(_tmpItemCode);
        final String _tmpItemName;
        if (_cursor.isNull(_cursorIndexOfItemName)) {
          _tmpItemName = null;
        } else {
          _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        }
        _item.setItemName(_tmpItemName);
        final String _tmpItemType;
        if (_cursor.isNull(_cursorIndexOfItemType)) {
          _tmpItemType = null;
        } else {
          _tmpItemType = _cursor.getString(_cursorIndexOfItemType);
        }
        _item.setItemType(_tmpItemType);
        final int _tmpDuration;
        _tmpDuration = _cursor.getInt(_cursorIndexOfDuration);
        _item.setDuration(_tmpDuration);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final String _tmpIsActive;
        if (_cursor.isNull(_cursorIndexOfIsActive)) {
          _tmpIsActive = null;
        } else {
          _tmpIsActive = _cursor.getString(_cursorIndexOfIsActive);
        }
        _item.setIsActive(_tmpIsActive);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _item.setOrderNumber(_tmpOrderNumber);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public VendorItem getVendorZone(final String zoneString) {
    final String _sql = "select * from vendor_items where item_code=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (zoneString == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, zoneString);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "item_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfVendorId = CursorUtil.getColumnIndexOrThrow(_cursor, "vendor_id");
      final int _cursorIndexOfItemCode = CursorUtil.getColumnIndexOrThrow(_cursor, "item_code");
      final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "item_name");
      final int _cursorIndexOfItemType = CursorUtil.getColumnIndexOrThrow(_cursor, "item_type");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final VendorItem _result;
      if(_cursor.moveToFirst()) {
        _result = new VendorItem();
        final int _tmpItemId;
        _tmpItemId = _cursor.getInt(_cursorIndexOfItemId);
        _result.setItemId(_tmpItemId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final int _tmpVendorId;
        _tmpVendorId = _cursor.getInt(_cursorIndexOfVendorId);
        _result.setVendorId(_tmpVendorId);
        final String _tmpItemCode;
        if (_cursor.isNull(_cursorIndexOfItemCode)) {
          _tmpItemCode = null;
        } else {
          _tmpItemCode = _cursor.getString(_cursorIndexOfItemCode);
        }
        _result.setItemCode(_tmpItemCode);
        final String _tmpItemName;
        if (_cursor.isNull(_cursorIndexOfItemName)) {
          _tmpItemName = null;
        } else {
          _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        }
        _result.setItemName(_tmpItemName);
        final String _tmpItemType;
        if (_cursor.isNull(_cursorIndexOfItemType)) {
          _tmpItemType = null;
        } else {
          _tmpItemType = _cursor.getString(_cursorIndexOfItemType);
        }
        _result.setItemType(_tmpItemType);
        final int _tmpDuration;
        _tmpDuration = _cursor.getInt(_cursorIndexOfDuration);
        _result.setDuration(_tmpDuration);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _result.setViolationId(_tmpViolationId);
        final String _tmpIsActive;
        if (_cursor.isNull(_cursorIndexOfIsActive)) {
          _tmpIsActive = null;
        } else {
          _tmpIsActive = _cursor.getString(_cursorIndexOfIsActive);
        }
        _result.setIsActive(_tmpIsActive);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _result.setOrderNumber(_tmpOrderNumber);
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
  public VendorItem getVendorItemByName(final String itemName) {
    final String _sql = "select * from vendor_items where item_name=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (itemName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, itemName);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "item_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfVendorId = CursorUtil.getColumnIndexOrThrow(_cursor, "vendor_id");
      final int _cursorIndexOfItemCode = CursorUtil.getColumnIndexOrThrow(_cursor, "item_code");
      final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "item_name");
      final int _cursorIndexOfItemType = CursorUtil.getColumnIndexOrThrow(_cursor, "item_type");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final VendorItem _result;
      if(_cursor.moveToFirst()) {
        _result = new VendorItem();
        final int _tmpItemId;
        _tmpItemId = _cursor.getInt(_cursorIndexOfItemId);
        _result.setItemId(_tmpItemId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final int _tmpVendorId;
        _tmpVendorId = _cursor.getInt(_cursorIndexOfVendorId);
        _result.setVendorId(_tmpVendorId);
        final String _tmpItemCode;
        if (_cursor.isNull(_cursorIndexOfItemCode)) {
          _tmpItemCode = null;
        } else {
          _tmpItemCode = _cursor.getString(_cursorIndexOfItemCode);
        }
        _result.setItemCode(_tmpItemCode);
        final String _tmpItemName;
        if (_cursor.isNull(_cursorIndexOfItemName)) {
          _tmpItemName = null;
        } else {
          _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        }
        _result.setItemName(_tmpItemName);
        final String _tmpItemType;
        if (_cursor.isNull(_cursorIndexOfItemType)) {
          _tmpItemType = null;
        } else {
          _tmpItemType = _cursor.getString(_cursorIndexOfItemType);
        }
        _result.setItemType(_tmpItemType);
        final int _tmpDuration;
        _tmpDuration = _cursor.getInt(_cursorIndexOfDuration);
        _result.setDuration(_tmpDuration);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _result.setViolationId(_tmpViolationId);
        final String _tmpIsActive;
        if (_cursor.isNull(_cursorIndexOfIsActive)) {
          _tmpIsActive = null;
        } else {
          _tmpIsActive = _cursor.getString(_cursorIndexOfIsActive);
        }
        _result.setIsActive(_tmpIsActive);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _result.setOrderNumber(_tmpOrderNumber);
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
  public VendorItem getVendorZoneByType(final String itemType) {
    final String _sql = "select * from vendor_items where item_type=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (itemType == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, itemType);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "item_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfVendorId = CursorUtil.getColumnIndexOrThrow(_cursor, "vendor_id");
      final int _cursorIndexOfItemCode = CursorUtil.getColumnIndexOrThrow(_cursor, "item_code");
      final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "item_name");
      final int _cursorIndexOfItemType = CursorUtil.getColumnIndexOrThrow(_cursor, "item_type");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final VendorItem _result;
      if(_cursor.moveToFirst()) {
        _result = new VendorItem();
        final int _tmpItemId;
        _tmpItemId = _cursor.getInt(_cursorIndexOfItemId);
        _result.setItemId(_tmpItemId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final int _tmpVendorId;
        _tmpVendorId = _cursor.getInt(_cursorIndexOfVendorId);
        _result.setVendorId(_tmpVendorId);
        final String _tmpItemCode;
        if (_cursor.isNull(_cursorIndexOfItemCode)) {
          _tmpItemCode = null;
        } else {
          _tmpItemCode = _cursor.getString(_cursorIndexOfItemCode);
        }
        _result.setItemCode(_tmpItemCode);
        final String _tmpItemName;
        if (_cursor.isNull(_cursorIndexOfItemName)) {
          _tmpItemName = null;
        } else {
          _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        }
        _result.setItemName(_tmpItemName);
        final String _tmpItemType;
        if (_cursor.isNull(_cursorIndexOfItemType)) {
          _tmpItemType = null;
        } else {
          _tmpItemType = _cursor.getString(_cursorIndexOfItemType);
        }
        _result.setItemType(_tmpItemType);
        final int _tmpDuration;
        _tmpDuration = _cursor.getInt(_cursorIndexOfDuration);
        _result.setDuration(_tmpDuration);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _result.setViolationId(_tmpViolationId);
        final String _tmpIsActive;
        if (_cursor.isNull(_cursorIndexOfIsActive)) {
          _tmpIsActive = null;
        } else {
          _tmpIsActive = _cursor.getString(_cursorIndexOfIsActive);
        }
        _result.setIsActive(_tmpIsActive);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _result.setOrderNumber(_tmpOrderNumber);
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
