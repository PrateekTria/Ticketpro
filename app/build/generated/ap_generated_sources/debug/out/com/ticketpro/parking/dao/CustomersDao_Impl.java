package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.CustomerInfo;
import io.reactivex.Completable;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.Void;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CustomersDao_Impl implements CustomersDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CustomerInfo> __insertionAdapterOfCustomerInfo;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public CustomersDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCustomerInfo = new EntityInsertionAdapter<CustomerInfo>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `customers` (`custid`,`name`,`address`,`contact_number`,`logo_image`,`agency_code`,`web_address`,`content_folder`,`ticket_color`,`ticket_back`,`TRCourtCode`,`TRPrintAgencyName`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CustomerInfo value) {
        stmt.bindLong(1, value.getCustId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getAddress() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getAddress());
        }
        if (value.getContactNumber() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getContactNumber());
        }
        if (value.getLogoImage() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getLogoImage());
        }
        if (value.getAgencyCode() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getAgencyCode());
        }
        if (value.getWebAddress() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getWebAddress());
        }
        if (value.getContentFolder() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getContentFolder());
        }
        if (value.getTicketColor() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getTicketColor());
        }
        if (value.getTicketBack() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getTicketBack());
        }
        if (value.getTRCourtName() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getTRCourtName());
        }
        if (value.getTRPrintAgencyName() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getTRPrintAgencyName());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from customers where custid <>?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from customers where custid=?";
        return _query;
      }
    };
  }

  @Override
  public void insertCustomerInfo(final CustomerInfo... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCustomerInfo.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertCustomerInfo(final CustomerInfo data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCustomerInfo.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertCustomerInfoList(final List<CustomerInfo> CustomerInfosList) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCustomerInfo.insert(CustomerInfosList);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void removeAll(final int activeCustId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveAll.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, activeCustId);
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
  public CustomerInfo getCustomerInfoInfo(final int custId) {
    final String _sql = "select * from customers where custid=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, custId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfContactNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_number");
      final int _cursorIndexOfLogoImage = CursorUtil.getColumnIndexOrThrow(_cursor, "logo_image");
      final int _cursorIndexOfAgencyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "agency_code");
      final int _cursorIndexOfWebAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "web_address");
      final int _cursorIndexOfContentFolder = CursorUtil.getColumnIndexOrThrow(_cursor, "content_folder");
      final int _cursorIndexOfTicketColor = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_color");
      final int _cursorIndexOfTicketBack = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_back");
      final int _cursorIndexOfTRCourtName = CursorUtil.getColumnIndexOrThrow(_cursor, "TRCourtCode");
      final int _cursorIndexOfTRPrintAgencyName = CursorUtil.getColumnIndexOrThrow(_cursor, "TRPrintAgencyName");
      final CustomerInfo _result;
      if(_cursor.moveToFirst()) {
        _result = new CustomerInfo();
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _result.setName(_tmpName);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _result.setAddress(_tmpAddress);
        final String _tmpContactNumber;
        if (_cursor.isNull(_cursorIndexOfContactNumber)) {
          _tmpContactNumber = null;
        } else {
          _tmpContactNumber = _cursor.getString(_cursorIndexOfContactNumber);
        }
        _result.setContactNumber(_tmpContactNumber);
        final String _tmpLogoImage;
        if (_cursor.isNull(_cursorIndexOfLogoImage)) {
          _tmpLogoImage = null;
        } else {
          _tmpLogoImage = _cursor.getString(_cursorIndexOfLogoImage);
        }
        _result.setLogoImage(_tmpLogoImage);
        final String _tmpAgencyCode;
        if (_cursor.isNull(_cursorIndexOfAgencyCode)) {
          _tmpAgencyCode = null;
        } else {
          _tmpAgencyCode = _cursor.getString(_cursorIndexOfAgencyCode);
        }
        _result.setAgencyCode(_tmpAgencyCode);
        final String _tmpWebAddress;
        if (_cursor.isNull(_cursorIndexOfWebAddress)) {
          _tmpWebAddress = null;
        } else {
          _tmpWebAddress = _cursor.getString(_cursorIndexOfWebAddress);
        }
        _result.setWebAddress(_tmpWebAddress);
        final String _tmpContentFolder;
        if (_cursor.isNull(_cursorIndexOfContentFolder)) {
          _tmpContentFolder = null;
        } else {
          _tmpContentFolder = _cursor.getString(_cursorIndexOfContentFolder);
        }
        _result.setContentFolder(_tmpContentFolder);
        final String _tmpTicketColor;
        if (_cursor.isNull(_cursorIndexOfTicketColor)) {
          _tmpTicketColor = null;
        } else {
          _tmpTicketColor = _cursor.getString(_cursorIndexOfTicketColor);
        }
        _result.setTicketColor(_tmpTicketColor);
        final String _tmpTicketBack;
        if (_cursor.isNull(_cursorIndexOfTicketBack)) {
          _tmpTicketBack = null;
        } else {
          _tmpTicketBack = _cursor.getString(_cursorIndexOfTicketBack);
        }
        _result.setTicketBack(_tmpTicketBack);
        final String _tmpTRCourtName;
        if (_cursor.isNull(_cursorIndexOfTRCourtName)) {
          _tmpTRCourtName = null;
        } else {
          _tmpTRCourtName = _cursor.getString(_cursorIndexOfTRCourtName);
        }
        _result.setTRCourtName(_tmpTRCourtName);
        final String _tmpTRPrintAgencyName;
        if (_cursor.isNull(_cursorIndexOfTRPrintAgencyName)) {
          _tmpTRPrintAgencyName = null;
        } else {
          _tmpTRPrintAgencyName = _cursor.getString(_cursorIndexOfTRPrintAgencyName);
        }
        _result.setTRPrintAgencyName(_tmpTRPrintAgencyName);
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
  public List<CustomerInfo> getCustomerInfos() {
    final String _sql = "select * from customers";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfContactNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_number");
      final int _cursorIndexOfLogoImage = CursorUtil.getColumnIndexOrThrow(_cursor, "logo_image");
      final int _cursorIndexOfAgencyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "agency_code");
      final int _cursorIndexOfWebAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "web_address");
      final int _cursorIndexOfContentFolder = CursorUtil.getColumnIndexOrThrow(_cursor, "content_folder");
      final int _cursorIndexOfTicketColor = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_color");
      final int _cursorIndexOfTicketBack = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_back");
      final int _cursorIndexOfTRCourtName = CursorUtil.getColumnIndexOrThrow(_cursor, "TRCourtCode");
      final int _cursorIndexOfTRPrintAgencyName = CursorUtil.getColumnIndexOrThrow(_cursor, "TRPrintAgencyName");
      final List<CustomerInfo> _result = new ArrayList<CustomerInfo>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final CustomerInfo _item;
        _item = new CustomerInfo();
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _item.setAddress(_tmpAddress);
        final String _tmpContactNumber;
        if (_cursor.isNull(_cursorIndexOfContactNumber)) {
          _tmpContactNumber = null;
        } else {
          _tmpContactNumber = _cursor.getString(_cursorIndexOfContactNumber);
        }
        _item.setContactNumber(_tmpContactNumber);
        final String _tmpLogoImage;
        if (_cursor.isNull(_cursorIndexOfLogoImage)) {
          _tmpLogoImage = null;
        } else {
          _tmpLogoImage = _cursor.getString(_cursorIndexOfLogoImage);
        }
        _item.setLogoImage(_tmpLogoImage);
        final String _tmpAgencyCode;
        if (_cursor.isNull(_cursorIndexOfAgencyCode)) {
          _tmpAgencyCode = null;
        } else {
          _tmpAgencyCode = _cursor.getString(_cursorIndexOfAgencyCode);
        }
        _item.setAgencyCode(_tmpAgencyCode);
        final String _tmpWebAddress;
        if (_cursor.isNull(_cursorIndexOfWebAddress)) {
          _tmpWebAddress = null;
        } else {
          _tmpWebAddress = _cursor.getString(_cursorIndexOfWebAddress);
        }
        _item.setWebAddress(_tmpWebAddress);
        final String _tmpContentFolder;
        if (_cursor.isNull(_cursorIndexOfContentFolder)) {
          _tmpContentFolder = null;
        } else {
          _tmpContentFolder = _cursor.getString(_cursorIndexOfContentFolder);
        }
        _item.setContentFolder(_tmpContentFolder);
        final String _tmpTicketColor;
        if (_cursor.isNull(_cursorIndexOfTicketColor)) {
          _tmpTicketColor = null;
        } else {
          _tmpTicketColor = _cursor.getString(_cursorIndexOfTicketColor);
        }
        _item.setTicketColor(_tmpTicketColor);
        final String _tmpTicketBack;
        if (_cursor.isNull(_cursorIndexOfTicketBack)) {
          _tmpTicketBack = null;
        } else {
          _tmpTicketBack = _cursor.getString(_cursorIndexOfTicketBack);
        }
        _item.setTicketBack(_tmpTicketBack);
        final String _tmpTRCourtName;
        if (_cursor.isNull(_cursorIndexOfTRCourtName)) {
          _tmpTRCourtName = null;
        } else {
          _tmpTRCourtName = _cursor.getString(_cursorIndexOfTRCourtName);
        }
        _item.setTRCourtName(_tmpTRCourtName);
        final String _tmpTRPrintAgencyName;
        if (_cursor.isNull(_cursorIndexOfTRPrintAgencyName)) {
          _tmpTRPrintAgencyName = null;
        } else {
          _tmpTRPrintAgencyName = _cursor.getString(_cursorIndexOfTRPrintAgencyName);
        }
        _item.setTRPrintAgencyName(_tmpTRPrintAgencyName);
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
