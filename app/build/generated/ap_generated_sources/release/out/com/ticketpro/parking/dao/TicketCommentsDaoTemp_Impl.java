package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.TicketCommentTemp;
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
public final class TicketCommentsDaoTemp_Impl implements TicketCommentsDaoTemp {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TicketCommentTemp> __insertionAdapterOfTicketCommentTemp;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  public TicketCommentsDaoTemp_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTicketCommentTemp = new EntityInsertionAdapter<TicketCommentTemp>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `ticket_comments_temp` (`ticket_comment_id`,`ticket_id`,`custid`,`comment_id`,`comment`,`citation_number`,`is_private`,`is_voice_comment`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TicketCommentTemp value) {
        stmt.bindLong(1, value.getTicketCommentId());
        stmt.bindLong(2, value.getTicketId());
        stmt.bindLong(3, value.getCustId());
        stmt.bindLong(4, value.getCommentId());
        if (value.getComment() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getComment());
        }
        stmt.bindLong(6, value.getCitationNumber());
        if (value.getIsPrivate() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getIsPrivate());
        }
        final int _tmp;
        _tmp = value.isVoiceComment() ? 1 : 0;
        stmt.bindLong(8, _tmp);
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from ticket_comments_temp where ticket_comment_id=?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from ticket_comments_temp";
        return _query;
      }
    };
  }

  @Override
  public void insertTicketCommentTemp(final TicketCommentTemp... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTicketCommentTemp.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertTicketCommentTemp(final TicketCommentTemp data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTicketCommentTemp.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertTicketCommentTempList(final List<TicketCommentTemp> TicketCommentTempsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTicketCommentTemp.insert(TicketCommentTempsList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void removeById(final long id) {
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
  public List<TicketCommentTemp> getTicketCommentTemps() {
    final String _sql = "select * from ticket_comments_temp";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTicketCommentId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_comment_id");
      final int _cursorIndexOfTicketId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfCommentId = CursorUtil.getColumnIndexOrThrow(_cursor, "comment_id");
      final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
      final int _cursorIndexOfCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "citation_number");
      final int _cursorIndexOfIsPrivate = CursorUtil.getColumnIndexOrThrow(_cursor, "is_private");
      final int _cursorIndexOfIsVoiceComment = CursorUtil.getColumnIndexOrThrow(_cursor, "is_voice_comment");
      final List<TicketCommentTemp> _result = new ArrayList<TicketCommentTemp>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TicketCommentTemp _item;
        _item = new TicketCommentTemp();
        final int _tmpTicketCommentId;
        _tmpTicketCommentId = _cursor.getInt(_cursorIndexOfTicketCommentId);
        _item.setTicketCommentId(_tmpTicketCommentId);
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpCommentId;
        _tmpCommentId = _cursor.getInt(_cursorIndexOfCommentId);
        _item.setCommentId(_tmpCommentId);
        final String _tmpComment;
        if (_cursor.isNull(_cursorIndexOfComment)) {
          _tmpComment = null;
        } else {
          _tmpComment = _cursor.getString(_cursorIndexOfComment);
        }
        _item.setComment(_tmpComment);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _item.setCitationNumber(_tmpCitationNumber);
        final String _tmpIsPrivate;
        if (_cursor.isNull(_cursorIndexOfIsPrivate)) {
          _tmpIsPrivate = null;
        } else {
          _tmpIsPrivate = _cursor.getString(_cursorIndexOfIsPrivate);
        }
        _item.setIsPrivate(_tmpIsPrivate);
        final boolean _tmpIsVoiceComment;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsVoiceComment);
        _tmpIsVoiceComment = _tmp != 0;
        _item.setVoiceComment(_tmpIsVoiceComment);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<TicketCommentTemp> getTicketCommentTempsByCitation(final long citationNumber) {
    final String _sql = "select * from ticket_comments_temp where citation_number=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, citationNumber);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTicketCommentId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_comment_id");
      final int _cursorIndexOfTicketId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfCommentId = CursorUtil.getColumnIndexOrThrow(_cursor, "comment_id");
      final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
      final int _cursorIndexOfCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "citation_number");
      final int _cursorIndexOfIsPrivate = CursorUtil.getColumnIndexOrThrow(_cursor, "is_private");
      final int _cursorIndexOfIsVoiceComment = CursorUtil.getColumnIndexOrThrow(_cursor, "is_voice_comment");
      final List<TicketCommentTemp> _result = new ArrayList<TicketCommentTemp>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TicketCommentTemp _item;
        _item = new TicketCommentTemp();
        final int _tmpTicketCommentId;
        _tmpTicketCommentId = _cursor.getInt(_cursorIndexOfTicketCommentId);
        _item.setTicketCommentId(_tmpTicketCommentId);
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpCommentId;
        _tmpCommentId = _cursor.getInt(_cursorIndexOfCommentId);
        _item.setCommentId(_tmpCommentId);
        final String _tmpComment;
        if (_cursor.isNull(_cursorIndexOfComment)) {
          _tmpComment = null;
        } else {
          _tmpComment = _cursor.getString(_cursorIndexOfComment);
        }
        _item.setComment(_tmpComment);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _item.setCitationNumber(_tmpCitationNumber);
        final String _tmpIsPrivate;
        if (_cursor.isNull(_cursorIndexOfIsPrivate)) {
          _tmpIsPrivate = null;
        } else {
          _tmpIsPrivate = _cursor.getString(_cursorIndexOfIsPrivate);
        }
        _item.setIsPrivate(_tmpIsPrivate);
        final boolean _tmpIsVoiceComment;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsVoiceComment);
        _tmpIsVoiceComment = _tmp != 0;
        _item.setVoiceComment(_tmpIsVoiceComment);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public TicketCommentTemp getCommentsByPrimaryKey(final String commentId) {
    final String _sql = "select * from ticket_comments_temp where ticket_comment_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (commentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, commentId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTicketCommentId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_comment_id");
      final int _cursorIndexOfTicketId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfCommentId = CursorUtil.getColumnIndexOrThrow(_cursor, "comment_id");
      final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
      final int _cursorIndexOfCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "citation_number");
      final int _cursorIndexOfIsPrivate = CursorUtil.getColumnIndexOrThrow(_cursor, "is_private");
      final int _cursorIndexOfIsVoiceComment = CursorUtil.getColumnIndexOrThrow(_cursor, "is_voice_comment");
      final TicketCommentTemp _result;
      if(_cursor.moveToFirst()) {
        _result = new TicketCommentTemp();
        final int _tmpTicketCommentId;
        _tmpTicketCommentId = _cursor.getInt(_cursorIndexOfTicketCommentId);
        _result.setTicketCommentId(_tmpTicketCommentId);
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _result.setTicketId(_tmpTicketId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final int _tmpCommentId;
        _tmpCommentId = _cursor.getInt(_cursorIndexOfCommentId);
        _result.setCommentId(_tmpCommentId);
        final String _tmpComment;
        if (_cursor.isNull(_cursorIndexOfComment)) {
          _tmpComment = null;
        } else {
          _tmpComment = _cursor.getString(_cursorIndexOfComment);
        }
        _result.setComment(_tmpComment);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _result.setCitationNumber(_tmpCitationNumber);
        final String _tmpIsPrivate;
        if (_cursor.isNull(_cursorIndexOfIsPrivate)) {
          _tmpIsPrivate = null;
        } else {
          _tmpIsPrivate = _cursor.getString(_cursorIndexOfIsPrivate);
        }
        _result.setIsPrivate(_tmpIsPrivate);
        final boolean _tmpIsVoiceComment;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsVoiceComment);
        _tmpIsVoiceComment = _tmp != 0;
        _result.setVoiceComment(_tmpIsVoiceComment);
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
  public int getNextPrimaryId() {
    final String _sql = "select max(ticket_comment_id) as max_ticket_comment_id from ticket_comments_temp";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getCount() {
    final String _sql = "SELECT COUNT(ticket_comment_id) FROM ticket_comments_temp";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
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
