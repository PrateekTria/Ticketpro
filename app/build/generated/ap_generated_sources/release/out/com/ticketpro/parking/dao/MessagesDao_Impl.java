package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.Message;
import com.ticketpro.util.Converters;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MessagesDao_Impl implements MessagesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Message> __insertionAdapterOfMessage;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public MessagesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMessage = new EntityInsertionAdapter<Message>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `messages` (`message_id`,`custid`,`message_date`,`from_userid`,`to_userid`,`subject`,`message`,`expiry_date`,`department`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Message value) {
        stmt.bindLong(1, value.getMessageId());
        stmt.bindLong(2, value.getCustId());
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getMessageDate());
        if (_tmp == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, _tmp);
        }
        stmt.bindLong(4, value.getFromUserId());
        stmt.bindLong(5, value.getToUserId());
        if (value.getSubject() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getSubject());
        }
        if (value.getMessage() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getMessage());
        }
        final Long _tmp_1;
        _tmp_1 = Converters.dateToTimestamp(value.getExpiryDate());
        if (_tmp_1 == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindLong(8, _tmp_1);
        }
        if (value.getDepartment() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getDepartment());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from messages";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from messages where message_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertMessage(final Message... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMessage.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMessage(final Message data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMessage.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMessageList(final List<Message> MessagesList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMessage.insert(MessagesList);
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
  public List<Message> getMessages(final String department) {
    final String _sql = "select * from messages where department=? and expiry_date>=date('now') order by message_date ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (department == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, department);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "message_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfMessageDate = CursorUtil.getColumnIndexOrThrow(_cursor, "message_date");
      final int _cursorIndexOfFromUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "from_userid");
      final int _cursorIndexOfToUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "to_userid");
      final int _cursorIndexOfSubject = CursorUtil.getColumnIndexOrThrow(_cursor, "subject");
      final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
      final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiry_date");
      final int _cursorIndexOfDepartment = CursorUtil.getColumnIndexOrThrow(_cursor, "department");
      final List<Message> _result = new ArrayList<Message>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Message _item;
        _item = new Message();
        final int _tmpMessageId;
        _tmpMessageId = _cursor.getInt(_cursorIndexOfMessageId);
        _item.setMessageId(_tmpMessageId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final Date _tmpMessageDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfMessageDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfMessageDate);
        }
        _tmpMessageDate = Converters.fromTimestamp(_tmp);
        _item.setMessageDate(_tmpMessageDate);
        final int _tmpFromUserId;
        _tmpFromUserId = _cursor.getInt(_cursorIndexOfFromUserId);
        _item.setFromUserId(_tmpFromUserId);
        final int _tmpToUserId;
        _tmpToUserId = _cursor.getInt(_cursorIndexOfToUserId);
        _item.setToUserId(_tmpToUserId);
        final String _tmpSubject;
        if (_cursor.isNull(_cursorIndexOfSubject)) {
          _tmpSubject = null;
        } else {
          _tmpSubject = _cursor.getString(_cursorIndexOfSubject);
        }
        _item.setSubject(_tmpSubject);
        final String _tmpMessage;
        if (_cursor.isNull(_cursorIndexOfMessage)) {
          _tmpMessage = null;
        } else {
          _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
        }
        _item.setMessage(_tmpMessage);
        final Date _tmpExpiryDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfExpiryDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfExpiryDate);
        }
        _tmpExpiryDate = Converters.fromTimestamp(_tmp_1);
        _item.setExpiryDate(_tmpExpiryDate);
        final String _tmpDepartment;
        if (_cursor.isNull(_cursorIndexOfDepartment)) {
          _tmpDepartment = null;
        } else {
          _tmpDepartment = _cursor.getString(_cursorIndexOfDepartment);
        }
        _item.setDepartment(_tmpDepartment);
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
