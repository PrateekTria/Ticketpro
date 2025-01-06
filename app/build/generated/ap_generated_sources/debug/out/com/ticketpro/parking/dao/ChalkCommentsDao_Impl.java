package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.ChalkComment;
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
public final class ChalkCommentsDao_Impl implements ChalkCommentsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ChalkComment> __insertionAdapterOfChalkComment;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveChalkCommentById;

  public ChalkCommentsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChalkComment = new EntityInsertionAdapter<ChalkComment>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `chalk_comments` (`chalk_comment_id`,`chalk_id`,`comment_id`,`comment`,`is_private`,`custid`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ChalkComment value) {
        stmt.bindLong(1, value.getChalkCommentId());
        stmt.bindLong(2, value.getChalkId());
        stmt.bindLong(3, value.getCommentId());
        if (value.getComment() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getComment());
        }
        if (value.getIsPrivate() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getIsPrivate());
        }
        stmt.bindLong(6, value.getCustId());
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from chalk_comments";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveChalkCommentById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from chalk_comments where chalk_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertChalkComment(final ChalkComment... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfChalkComment.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertChalkComment(final ChalkComment data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChalkComment.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertChalkCommentList(final List<ChalkComment> chalkCommentsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfChalkComment.insert(chalkCommentsList);
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
  public void removeChalkCommentById(final long chalkId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveChalkCommentById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, chalkId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveChalkCommentById.release(_stmt);
    }
  }

  @Override
  public List<ChalkComment> getChalkComments(final long chalkId) {
    final String _sql = "select * from chalk_comments where chalk_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chalkId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfChalkCommentId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_comment_id");
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfCommentId = CursorUtil.getColumnIndexOrThrow(_cursor, "comment_id");
      final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
      final int _cursorIndexOfIsPrivate = CursorUtil.getColumnIndexOrThrow(_cursor, "is_private");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final List<ChalkComment> _result = new ArrayList<ChalkComment>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ChalkComment _item;
        _item = new ChalkComment();
        final int _tmpChalkCommentId;
        _tmpChalkCommentId = _cursor.getInt(_cursorIndexOfChalkCommentId);
        _item.setChalkCommentId(_tmpChalkCommentId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
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
        final String _tmpIsPrivate;
        if (_cursor.isNull(_cursorIndexOfIsPrivate)) {
          _tmpIsPrivate = null;
        } else {
          _tmpIsPrivate = _cursor.getString(_cursorIndexOfIsPrivate);
        }
        _item.setIsPrivate(_tmpIsPrivate);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public ChalkComment getChalkCommentByPrimaryKey(final long commentId) {
    final String _sql = "select * from chalk_comments where chalk_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, commentId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfChalkCommentId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_comment_id");
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfCommentId = CursorUtil.getColumnIndexOrThrow(_cursor, "comment_id");
      final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
      final int _cursorIndexOfIsPrivate = CursorUtil.getColumnIndexOrThrow(_cursor, "is_private");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final ChalkComment _result;
      if(_cursor.moveToFirst()) {
        _result = new ChalkComment();
        final int _tmpChalkCommentId;
        _tmpChalkCommentId = _cursor.getInt(_cursorIndexOfChalkCommentId);
        _result.setChalkCommentId(_tmpChalkCommentId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _result.setChalkId(_tmpChalkId);
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
        final String _tmpIsPrivate;
        if (_cursor.isNull(_cursorIndexOfIsPrivate)) {
          _tmpIsPrivate = null;
        } else {
          _tmpIsPrivate = _cursor.getString(_cursorIndexOfIsPrivate);
        }
        _result.setIsPrivate(_tmpIsPrivate);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
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
    final String _sql = "select max(chalk_comment_id) as max_chalk_comment_id from chalk_comments";
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
