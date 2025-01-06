package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.CommentGroupComment;
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
public final class CommentgroupCommentsDao_Impl implements CommentgroupCommentsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CommentGroupComment> __insertionAdapterOfCommentGroupComment;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public CommentgroupCommentsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCommentGroupComment = new EntityInsertionAdapter<CommentGroupComment>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `comment_group_comments` (`comment_group_id`,`group_id`,`comment_id`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CommentGroupComment value) {
        stmt.bindLong(1, value.getCommentGroupId());
        stmt.bindLong(2, value.getGroupId());
        stmt.bindLong(3, value.getCommentId());
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from comment_group_comments";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from comment_group_comments where comment_group_id= ?";
        return _query;
      }
    };
  }

  @Override
  public void insertCommentGroupComment(final CommentGroupComment... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCommentGroupComment.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertCommentGroupComment(final CommentGroupComment data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCommentGroupComment.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertCommentGroupCommentList(
      final List<CommentGroupComment> CommentGroupCommentsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCommentGroupComment.insert(CommentGroupCommentsList);
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
  public List<CommentGroupComment> getCommentGroupComments() {
    final String _sql = "select * from comment_group_comments";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCommentGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "comment_group_id");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "group_id");
      final int _cursorIndexOfCommentId = CursorUtil.getColumnIndexOrThrow(_cursor, "comment_id");
      final List<CommentGroupComment> _result = new ArrayList<CommentGroupComment>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final CommentGroupComment _item;
        _item = new CommentGroupComment();
        final int _tmpCommentGroupId;
        _tmpCommentGroupId = _cursor.getInt(_cursorIndexOfCommentGroupId);
        _item.setCommentGroupId(_tmpCommentGroupId);
        final int _tmpGroupId;
        _tmpGroupId = _cursor.getInt(_cursorIndexOfGroupId);
        _item.setGroupId(_tmpGroupId);
        final int _tmpCommentId;
        _tmpCommentId = _cursor.getInt(_cursorIndexOfCommentId);
        _item.setCommentId(_tmpCommentId);
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
