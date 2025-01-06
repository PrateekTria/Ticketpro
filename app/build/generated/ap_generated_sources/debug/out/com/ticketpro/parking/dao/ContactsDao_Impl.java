package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.Contact;
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
public final class ContactsDao_Impl implements ContactsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Contact> __insertionAdapterOfContact;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public ContactsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfContact = new EntityInsertionAdapter<Contact>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `contacts` (`contact_id`,`custid`,`email_id`,`phone`,`contact_type`,`is_active`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Contact value) {
        stmt.bindLong(1, value.getContactId());
        stmt.bindLong(2, value.getCustId());
        if (value.getEmailId() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getEmailId());
        }
        if (value.getPhone() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getPhone());
        }
        if (value.getContactType() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getContactType());
        }
        if (value.getIsActive() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getIsActive());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from contacts";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from contacts where contact_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertContact(final Contact... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfContact.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertContact(final Contact data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfContact.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertContactList(final List<Contact> ContactsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfContact.insert(ContactsList);
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
  public List<Contact> getContacts() {
    final String _sql = "select * from contacts where custid <> 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfEmailId = CursorUtil.getColumnIndexOrThrow(_cursor, "email_id");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfContactType = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_type");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final List<Contact> _result = new ArrayList<Contact>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Contact _item;
        _item = new Contact();
        final int _tmpContactId;
        _tmpContactId = _cursor.getInt(_cursorIndexOfContactId);
        _item.setContactId(_tmpContactId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpEmailId;
        if (_cursor.isNull(_cursorIndexOfEmailId)) {
          _tmpEmailId = null;
        } else {
          _tmpEmailId = _cursor.getString(_cursorIndexOfEmailId);
        }
        _item.setEmailId(_tmpEmailId);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _item.setPhone(_tmpPhone);
        final String _tmpContactType;
        if (_cursor.isNull(_cursorIndexOfContactType)) {
          _tmpContactType = null;
        } else {
          _tmpContactType = _cursor.getString(_cursorIndexOfContactType);
        }
        _item.setContactType(_tmpContactType);
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
  public List<Contact> getTowNotifyContacts() {
    final String _sql = "select * from contacts where contact_type='TowNotify'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfEmailId = CursorUtil.getColumnIndexOrThrow(_cursor, "email_id");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfContactType = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_type");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final List<Contact> _result = new ArrayList<Contact>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Contact _item;
        _item = new Contact();
        final int _tmpContactId;
        _tmpContactId = _cursor.getInt(_cursorIndexOfContactId);
        _item.setContactId(_tmpContactId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpEmailId;
        if (_cursor.isNull(_cursorIndexOfEmailId)) {
          _tmpEmailId = null;
        } else {
          _tmpEmailId = _cursor.getString(_cursorIndexOfEmailId);
        }
        _item.setEmailId(_tmpEmailId);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _item.setPhone(_tmpPhone);
        final String _tmpContactType;
        if (_cursor.isNull(_cursorIndexOfContactType)) {
          _tmpContactType = null;
        } else {
          _tmpContactType = _cursor.getString(_cursorIndexOfContactType);
        }
        _item.setContactType(_tmpContactType);
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
  public List<Contact> getSupportContacts() {
    final String _sql = "select * from contacts where contact_type='Support'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfContactId = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfEmailId = CursorUtil.getColumnIndexOrThrow(_cursor, "email_id");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfContactType = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_type");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final List<Contact> _result = new ArrayList<Contact>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Contact _item;
        _item = new Contact();
        final int _tmpContactId;
        _tmpContactId = _cursor.getInt(_cursorIndexOfContactId);
        _item.setContactId(_tmpContactId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpEmailId;
        if (_cursor.isNull(_cursorIndexOfEmailId)) {
          _tmpEmailId = null;
        } else {
          _tmpEmailId = _cursor.getString(_cursorIndexOfEmailId);
        }
        _item.setEmailId(_tmpEmailId);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _item.setPhone(_tmpPhone);
        final String _tmpContactType;
        if (_cursor.isNull(_cursorIndexOfContactType)) {
          _tmpContactType = null;
        } else {
          _tmpContactType = _cursor.getString(_cursorIndexOfContactType);
        }
        _item.setContactType(_tmpContactType);
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
  public String getSupportNumber() {
    final String _sql = "select phone from contacts where custid=1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
