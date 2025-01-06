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
public final class ViolationsDao_Impl implements ViolationsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Violation> __insertionAdapterOfViolation;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public ViolationsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfViolation = new EntityInsertionAdapter<Violation>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `violations` (`violation_id`,`custid`,`violation`,`code`,`order_number`,`base_fine`,`fine1`,`fine2`,`repeat_offender`,`violation_display`,`default_comment`,`required_comments`,`required_photos`,`chalktimerequired`,`hide`,`repeat_offender_type`,`code_export`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Violation value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getCustId());
        if (value.getTitle() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTitle());
        }
        if (value.getCode() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getCode());
        }
        stmt.bindLong(5, value.getOrderNumber());
        stmt.bindDouble(6, value.getBaseFine());
        stmt.bindDouble(7, value.getFine1());
        stmt.bindDouble(8, value.getFine2());
        if (value.getRepeatOffender() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getRepeatOffender());
        }
        if (value.getViolationDisplay() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getViolationDisplay());
        }
        if (value.getDefaultComment() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getDefaultComment());
        }
        stmt.bindLong(12, value.getRequiredComments());
        stmt.bindLong(13, value.getRequiredPhotos());
        if (value.getChalktimerequired() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getChalktimerequired());
        }
        if (value.getHide() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getHide());
        }
        if (value.getRepeatOffenderType() == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindString(16, value.getRepeatOffenderType());
        }
        if (value.getCode_exportt() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindString(17, value.getCode_exportt());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from violations";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from violations where violation_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertViolation(final Violation... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfViolation.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertViolation(final Violation data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfViolation.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertViolationList(final List<Violation> ViolationsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfViolation.insert(ViolationsList);
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
  public List<Violation> getViolations() {
    final String _sql = "select * from violations order by order_number,violation";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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

  @Override
  public Violation getViolationById(final int violationId) {
    final String _sql = "select * from violations where violation_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, violationId);
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
      final Violation _result;
      if(_cursor.moveToFirst()) {
        _result = new Violation();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result.setTitle(_tmpTitle);
        final String _tmpCode;
        if (_cursor.isNull(_cursorIndexOfCode)) {
          _tmpCode = null;
        } else {
          _tmpCode = _cursor.getString(_cursorIndexOfCode);
        }
        _result.setCode(_tmpCode);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _result.setOrderNumber(_tmpOrderNumber);
        final double _tmpBaseFine;
        _tmpBaseFine = _cursor.getDouble(_cursorIndexOfBaseFine);
        _result.setBaseFine(_tmpBaseFine);
        final double _tmpFine1;
        _tmpFine1 = _cursor.getDouble(_cursorIndexOfFine1);
        _result.setFine1(_tmpFine1);
        final double _tmpFine2;
        _tmpFine2 = _cursor.getDouble(_cursorIndexOfFine2);
        _result.setFine2(_tmpFine2);
        final String _tmpRepeatOffender;
        if (_cursor.isNull(_cursorIndexOfRepeatOffender)) {
          _tmpRepeatOffender = null;
        } else {
          _tmpRepeatOffender = _cursor.getString(_cursorIndexOfRepeatOffender);
        }
        _result.setRepeatOffender(_tmpRepeatOffender);
        final String _tmpViolationDisplay;
        if (_cursor.isNull(_cursorIndexOfViolationDisplay)) {
          _tmpViolationDisplay = null;
        } else {
          _tmpViolationDisplay = _cursor.getString(_cursorIndexOfViolationDisplay);
        }
        _result.setViolationDisplay(_tmpViolationDisplay);
        final String _tmpDefaultComment;
        if (_cursor.isNull(_cursorIndexOfDefaultComment)) {
          _tmpDefaultComment = null;
        } else {
          _tmpDefaultComment = _cursor.getString(_cursorIndexOfDefaultComment);
        }
        _result.setDefaultComment(_tmpDefaultComment);
        final int _tmpRequiredComments;
        _tmpRequiredComments = _cursor.getInt(_cursorIndexOfRequiredComments);
        _result.setRequiredComments(_tmpRequiredComments);
        final int _tmpRequiredPhotos;
        _tmpRequiredPhotos = _cursor.getInt(_cursorIndexOfRequiredPhotos);
        _result.setRequiredPhotos(_tmpRequiredPhotos);
        final String _tmpChalktimerequired;
        if (_cursor.isNull(_cursorIndexOfChalktimerequired)) {
          _tmpChalktimerequired = null;
        } else {
          _tmpChalktimerequired = _cursor.getString(_cursorIndexOfChalktimerequired);
        }
        _result.setChalktimerequired(_tmpChalktimerequired);
        final String _tmpHide;
        if (_cursor.isNull(_cursorIndexOfHide)) {
          _tmpHide = null;
        } else {
          _tmpHide = _cursor.getString(_cursorIndexOfHide);
        }
        _result.setHide(_tmpHide);
        final String _tmpRepeatOffenderType;
        if (_cursor.isNull(_cursorIndexOfRepeatOffenderType)) {
          _tmpRepeatOffenderType = null;
        } else {
          _tmpRepeatOffenderType = _cursor.getString(_cursorIndexOfRepeatOffenderType);
        }
        _result.setRepeatOffenderType(_tmpRepeatOffenderType);
        final String _tmpCode_exportt;
        if (_cursor.isNull(_cursorIndexOfCodeExportt)) {
          _tmpCode_exportt = null;
        } else {
          _tmpCode_exportt = _cursor.getString(_cursorIndexOfCodeExportt);
        }
        _result.setCode_exportt(_tmpCode_exportt);
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
  public Violation getViolationByCode(final String violationCode) {
    final String _sql = "select * from violations where code=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (violationCode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, violationCode);
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
      final Violation _result;
      if(_cursor.moveToFirst()) {
        _result = new Violation();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result.setTitle(_tmpTitle);
        final String _tmpCode;
        if (_cursor.isNull(_cursorIndexOfCode)) {
          _tmpCode = null;
        } else {
          _tmpCode = _cursor.getString(_cursorIndexOfCode);
        }
        _result.setCode(_tmpCode);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _result.setOrderNumber(_tmpOrderNumber);
        final double _tmpBaseFine;
        _tmpBaseFine = _cursor.getDouble(_cursorIndexOfBaseFine);
        _result.setBaseFine(_tmpBaseFine);
        final double _tmpFine1;
        _tmpFine1 = _cursor.getDouble(_cursorIndexOfFine1);
        _result.setFine1(_tmpFine1);
        final double _tmpFine2;
        _tmpFine2 = _cursor.getDouble(_cursorIndexOfFine2);
        _result.setFine2(_tmpFine2);
        final String _tmpRepeatOffender;
        if (_cursor.isNull(_cursorIndexOfRepeatOffender)) {
          _tmpRepeatOffender = null;
        } else {
          _tmpRepeatOffender = _cursor.getString(_cursorIndexOfRepeatOffender);
        }
        _result.setRepeatOffender(_tmpRepeatOffender);
        final String _tmpViolationDisplay;
        if (_cursor.isNull(_cursorIndexOfViolationDisplay)) {
          _tmpViolationDisplay = null;
        } else {
          _tmpViolationDisplay = _cursor.getString(_cursorIndexOfViolationDisplay);
        }
        _result.setViolationDisplay(_tmpViolationDisplay);
        final String _tmpDefaultComment;
        if (_cursor.isNull(_cursorIndexOfDefaultComment)) {
          _tmpDefaultComment = null;
        } else {
          _tmpDefaultComment = _cursor.getString(_cursorIndexOfDefaultComment);
        }
        _result.setDefaultComment(_tmpDefaultComment);
        final int _tmpRequiredComments;
        _tmpRequiredComments = _cursor.getInt(_cursorIndexOfRequiredComments);
        _result.setRequiredComments(_tmpRequiredComments);
        final int _tmpRequiredPhotos;
        _tmpRequiredPhotos = _cursor.getInt(_cursorIndexOfRequiredPhotos);
        _result.setRequiredPhotos(_tmpRequiredPhotos);
        final String _tmpChalktimerequired;
        if (_cursor.isNull(_cursorIndexOfChalktimerequired)) {
          _tmpChalktimerequired = null;
        } else {
          _tmpChalktimerequired = _cursor.getString(_cursorIndexOfChalktimerequired);
        }
        _result.setChalktimerequired(_tmpChalktimerequired);
        final String _tmpHide;
        if (_cursor.isNull(_cursorIndexOfHide)) {
          _tmpHide = null;
        } else {
          _tmpHide = _cursor.getString(_cursorIndexOfHide);
        }
        _result.setHide(_tmpHide);
        final String _tmpRepeatOffenderType;
        if (_cursor.isNull(_cursorIndexOfRepeatOffenderType)) {
          _tmpRepeatOffenderType = null;
        } else {
          _tmpRepeatOffenderType = _cursor.getString(_cursorIndexOfRepeatOffenderType);
        }
        _result.setRepeatOffenderType(_tmpRepeatOffenderType);
        final String _tmpCode_exportt;
        if (_cursor.isNull(_cursorIndexOfCodeExportt)) {
          _tmpCode_exportt = null;
        } else {
          _tmpCode_exportt = _cursor.getString(_cursorIndexOfCodeExportt);
        }
        _result.setCode_exportt(_tmpCode_exportt);
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
  public Violation getViolationByCode_Export(final String code_export) {
    final String _sql = "select * from violations where code_export=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (code_export == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, code_export);
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
      final Violation _result;
      if(_cursor.moveToFirst()) {
        _result = new Violation();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result.setTitle(_tmpTitle);
        final String _tmpCode;
        if (_cursor.isNull(_cursorIndexOfCode)) {
          _tmpCode = null;
        } else {
          _tmpCode = _cursor.getString(_cursorIndexOfCode);
        }
        _result.setCode(_tmpCode);
        final int _tmpOrderNumber;
        _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
        _result.setOrderNumber(_tmpOrderNumber);
        final double _tmpBaseFine;
        _tmpBaseFine = _cursor.getDouble(_cursorIndexOfBaseFine);
        _result.setBaseFine(_tmpBaseFine);
        final double _tmpFine1;
        _tmpFine1 = _cursor.getDouble(_cursorIndexOfFine1);
        _result.setFine1(_tmpFine1);
        final double _tmpFine2;
        _tmpFine2 = _cursor.getDouble(_cursorIndexOfFine2);
        _result.setFine2(_tmpFine2);
        final String _tmpRepeatOffender;
        if (_cursor.isNull(_cursorIndexOfRepeatOffender)) {
          _tmpRepeatOffender = null;
        } else {
          _tmpRepeatOffender = _cursor.getString(_cursorIndexOfRepeatOffender);
        }
        _result.setRepeatOffender(_tmpRepeatOffender);
        final String _tmpViolationDisplay;
        if (_cursor.isNull(_cursorIndexOfViolationDisplay)) {
          _tmpViolationDisplay = null;
        } else {
          _tmpViolationDisplay = _cursor.getString(_cursorIndexOfViolationDisplay);
        }
        _result.setViolationDisplay(_tmpViolationDisplay);
        final String _tmpDefaultComment;
        if (_cursor.isNull(_cursorIndexOfDefaultComment)) {
          _tmpDefaultComment = null;
        } else {
          _tmpDefaultComment = _cursor.getString(_cursorIndexOfDefaultComment);
        }
        _result.setDefaultComment(_tmpDefaultComment);
        final int _tmpRequiredComments;
        _tmpRequiredComments = _cursor.getInt(_cursorIndexOfRequiredComments);
        _result.setRequiredComments(_tmpRequiredComments);
        final int _tmpRequiredPhotos;
        _tmpRequiredPhotos = _cursor.getInt(_cursorIndexOfRequiredPhotos);
        _result.setRequiredPhotos(_tmpRequiredPhotos);
        final String _tmpChalktimerequired;
        if (_cursor.isNull(_cursorIndexOfChalktimerequired)) {
          _tmpChalktimerequired = null;
        } else {
          _tmpChalktimerequired = _cursor.getString(_cursorIndexOfChalktimerequired);
        }
        _result.setChalktimerequired(_tmpChalktimerequired);
        final String _tmpHide;
        if (_cursor.isNull(_cursorIndexOfHide)) {
          _tmpHide = null;
        } else {
          _tmpHide = _cursor.getString(_cursorIndexOfHide);
        }
        _result.setHide(_tmpHide);
        final String _tmpRepeatOffenderType;
        if (_cursor.isNull(_cursorIndexOfRepeatOffenderType)) {
          _tmpRepeatOffenderType = null;
        } else {
          _tmpRepeatOffenderType = _cursor.getString(_cursorIndexOfRepeatOffenderType);
        }
        _result.setRepeatOffenderType(_tmpRepeatOffenderType);
        final String _tmpCode_exportt;
        if (_cursor.isNull(_cursorIndexOfCodeExportt)) {
          _tmpCode_exportt = null;
        } else {
          _tmpCode_exportt = _cursor.getString(_cursorIndexOfCodeExportt);
        }
        _result.setCode_exportt(_tmpCode_exportt);
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
