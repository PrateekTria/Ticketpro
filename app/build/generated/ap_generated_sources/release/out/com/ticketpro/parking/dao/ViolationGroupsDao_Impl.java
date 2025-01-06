package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.Violation;
import com.ticketpro.model.ViolationGroup;
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
public final class ViolationGroupsDao_Impl implements ViolationGroupsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ViolationGroup> __insertionAdapterOfViolationGroup;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public ViolationGroupsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfViolationGroup = new EntityInsertionAdapter<ViolationGroup>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `violation_groups` (`group_id`,`custid`,`group_code`,`group_name`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ViolationGroup value) {
        stmt.bindLong(1, value.getGroupId());
        stmt.bindLong(2, value.getCustId());
        if (value.getGroupCode() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getGroupCode());
        }
        if (value.getGroupName() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getGroupName());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from violation_groups";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from violation_groups where group_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertViolationGroup(final ViolationGroup... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfViolationGroup.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertViolationGroup(final ViolationGroup data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfViolationGroup.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertViolationGroupList(final List<ViolationGroup> ViolationGroupsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfViolationGroup.insert(ViolationGroupsList);
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
  public List<ViolationGroup> getViolationGroups() {
    final String _sql = "select * from violation_groups";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "group_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfGroupCode = CursorUtil.getColumnIndexOrThrow(_cursor, "group_code");
      final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "group_name");
      final List<ViolationGroup> _result = new ArrayList<ViolationGroup>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ViolationGroup _item;
        _item = new ViolationGroup();
        final int _tmpGroupId;
        _tmpGroupId = _cursor.getInt(_cursorIndexOfGroupId);
        _item.setGroupId(_tmpGroupId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpGroupCode;
        if (_cursor.isNull(_cursorIndexOfGroupCode)) {
          _tmpGroupCode = null;
        } else {
          _tmpGroupCode = _cursor.getString(_cursorIndexOfGroupCode);
        }
        _item.setGroupCode(_tmpGroupCode);
        final String _tmpGroupName;
        if (_cursor.isNull(_cursorIndexOfGroupName)) {
          _tmpGroupName = null;
        } else {
          _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
        }
        _item.setGroupName(_tmpGroupName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Violation> getViolationsByGroup(final String group) {
    final String _sql = "select * from violations where violations.violation_id in (select violation_id from violation_group_violations where group_id=(select group_id from violation_groups where group_code=?)) order by order_number";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (group == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, group);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
      final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
      final int _cursorIndexOfBaseFine = CursorUtil.getColumnIndexOrThrow(_cursor, "base_fine");
      final int _cursorIndexOfFine1 = CursorUtil.getColumnIndexOrThrow(_cursor, "fine1");
      final int _cursorIndexOfFine2 = CursorUtil.getColumnIndexOrThrow(_cursor, "fine2");
      final int _cursorIndexOfRepeatOffender = CursorUtil.getColumnIndexOrThrow(_cursor, "repeat_offender");
      final int _cursorIndexOfViolationDisplay = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_display");
      final int _cursorIndexOfDefaultComment = CursorUtil.getColumnIndexOrThrow(_cursor, "default_comment");
      final int _cursorIndexOfRequiredComments = CursorUtil.getColumnIndexOrThrow(_cursor, "required_comments");
      final int _cursorIndexOfRequiredPhotos = CursorUtil.getColumnIndexOrThrow(_cursor, "required_photos");
      final int _cursorIndexOfChalktimerequired = CursorUtil.getColumnIndexOrThrow(_cursor, "chalktimerequired");
      final int _cursorIndexOfHide = CursorUtil.getColumnIndexOrThrow(_cursor, "hide");
      final int _cursorIndexOfRepeatOffenderType = CursorUtil.getColumnIndexOrThrow(_cursor, "repeat_offender_type");
      final int _cursorIndexOfCodeExportt = CursorUtil.getColumnIndexOrThrow(_cursor, "code_export");
      final List<Violation> _result = new ArrayList<Violation>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Violation _item;
        _item = new Violation();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpCode;
        if (_cursor.isNull(_cursorIndexOfCode)) {
          _tmpCode = null;
        } else {
          _tmpCode = _cursor.getString(_cursorIndexOfCode);
        }
        _item.setCode(_tmpCode);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _item.setOrderNumber(_tmpOrderNumber);
        final double _tmpBaseFine;
        _tmpBaseFine = _cursor.getDouble(_cursorIndexOfBaseFine);
        _item.setBaseFine(_tmpBaseFine);
        final double _tmpFine1;
        _tmpFine1 = _cursor.getDouble(_cursorIndexOfFine1);
        _item.setFine1(_tmpFine1);
        final double _tmpFine2;
        _tmpFine2 = _cursor.getDouble(_cursorIndexOfFine2);
        _item.setFine2(_tmpFine2);
        final String _tmpRepeatOffender;
        if (_cursor.isNull(_cursorIndexOfRepeatOffender)) {
          _tmpRepeatOffender = null;
        } else {
          _tmpRepeatOffender = _cursor.getString(_cursorIndexOfRepeatOffender);
        }
        _item.setRepeatOffender(_tmpRepeatOffender);
        final String _tmpViolationDisplay;
        if (_cursor.isNull(_cursorIndexOfViolationDisplay)) {
          _tmpViolationDisplay = null;
        } else {
          _tmpViolationDisplay = _cursor.getString(_cursorIndexOfViolationDisplay);
        }
        _item.setViolationDisplay(_tmpViolationDisplay);
        final String _tmpDefaultComment;
        if (_cursor.isNull(_cursorIndexOfDefaultComment)) {
          _tmpDefaultComment = null;
        } else {
          _tmpDefaultComment = _cursor.getString(_cursorIndexOfDefaultComment);
        }
        _item.setDefaultComment(_tmpDefaultComment);
        final int _tmpRequiredComments;
        _tmpRequiredComments = _cursor.getInt(_cursorIndexOfRequiredComments);
        _item.setRequiredComments(_tmpRequiredComments);
        final int _tmpRequiredPhotos;
        _tmpRequiredPhotos = _cursor.getInt(_cursorIndexOfRequiredPhotos);
        _item.setRequiredPhotos(_tmpRequiredPhotos);
        final String _tmpChalktimerequired;
        if (_cursor.isNull(_cursorIndexOfChalktimerequired)) {
          _tmpChalktimerequired = null;
        } else {
          _tmpChalktimerequired = _cursor.getString(_cursorIndexOfChalktimerequired);
        }
        _item.setChalktimerequired(_tmpChalktimerequired);
        final String _tmpHide;
        if (_cursor.isNull(_cursorIndexOfHide)) {
          _tmpHide = null;
        } else {
          _tmpHide = _cursor.getString(_cursorIndexOfHide);
        }
        _item.setHide(_tmpHide);
        final String _tmpRepeatOffenderType;
        if (_cursor.isNull(_cursorIndexOfRepeatOffenderType)) {
          _tmpRepeatOffenderType = null;
        } else {
          _tmpRepeatOffenderType = _cursor.getString(_cursorIndexOfRepeatOffenderType);
        }
        _item.setRepeatOffenderType(_tmpRepeatOffenderType);
        final String _tmpCode_exportt;
        if (_cursor.isNull(_cursorIndexOfCodeExportt)) {
          _tmpCode_exportt = null;
        } else {
          _tmpCode_exportt = _cursor.getString(_cursorIndexOfCodeExportt);
        }
        _item.setCode_exportt(_tmpCode_exportt);
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
