package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.Permit;
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
public final class PermitsDao_Impl implements PermitsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Permit> __insertionAdapterOfPermit;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public PermitsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPermit = new EntityInsertionAdapter<Permit>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `permits` (`permit_id`,`custid`,`permit_number`,`permit_type`,`permit_code`,`plate`,`plate_type`,`vin`,`exp_date`,`state_code`,`body_code`,`color_code`,`make_code`,`class_code`,`begin_date`,`end_date`,`permit_holder`,`student_id`,`address1`,`address2`,`phone`,`model_code`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Permit value) {
        stmt.bindLong(1, value.getPermitId());
        stmt.bindLong(2, value.getCustId());
        if (value.getPermitNumber() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPermitNumber());
        }
        if (value.getPermitType() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getPermitType());
        }
        if (value.getPermitCode() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getPermitCode());
        }
        if (value.getPlate() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getPlate());
        }
        if (value.getPlateType() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getPlateType());
        }
        if (value.getVin() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getVin());
        }
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getExpiration());
        if (_tmp == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindLong(9, _tmp);
        }
        if (value.getStateCode() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getStateCode());
        }
        if (value.getBodyCode() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getBodyCode());
        }
        if (value.getColorCode() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getColorCode());
        }
        if (value.getMakeCode() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getMakeCode());
        }
        if (value.getClassCode() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getClassCode());
        }
        final Long _tmp_1;
        _tmp_1 = Converters.dateToTimestamp(value.getBeginDate());
        if (_tmp_1 == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindLong(15, _tmp_1);
        }
        final Long _tmp_2;
        _tmp_2 = Converters.dateToTimestamp(value.getEndDate());
        if (_tmp_2 == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindLong(16, _tmp_2);
        }
        if (value.getPermitHolder() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindString(17, value.getPermitHolder());
        }
        stmt.bindLong(18, value.getStudentId());
        if (value.getAddress1() == null) {
          stmt.bindNull(19);
        } else {
          stmt.bindString(19, value.getAddress1());
        }
        if (value.getAddress2() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.getAddress2());
        }
        if (value.getPhone() == null) {
          stmt.bindNull(21);
        } else {
          stmt.bindString(21, value.getPhone());
        }
        if (value.getModelCode() == null) {
          stmt.bindNull(22);
        } else {
          stmt.bindString(22, value.getModelCode());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from permits";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from permits where permit_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertPermit(final Permit... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPermit.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertPermit(final Permit data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPermit.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertPermitList(final List<Permit> PermitsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPermit.insert(PermitsList);
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
  public List<Permit> getPermits() {
    final String _sql = "select * from permits";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPermitId = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfPermitNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_number");
      final int _cursorIndexOfPermitType = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_type");
      final int _cursorIndexOfPermitCode = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_code");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfPlateType = CursorUtil.getColumnIndexOrThrow(_cursor, "plate_type");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "exp_date");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfBodyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "body_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfClassCode = CursorUtil.getColumnIndexOrThrow(_cursor, "class_code");
      final int _cursorIndexOfBeginDate = CursorUtil.getColumnIndexOrThrow(_cursor, "begin_date");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
      final int _cursorIndexOfPermitHolder = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_holder");
      final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "student_id");
      final int _cursorIndexOfAddress1 = CursorUtil.getColumnIndexOrThrow(_cursor, "address1");
      final int _cursorIndexOfAddress2 = CursorUtil.getColumnIndexOrThrow(_cursor, "address2");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfModelCode = CursorUtil.getColumnIndexOrThrow(_cursor, "model_code");
      final List<Permit> _result = new ArrayList<Permit>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Permit _item;
        _item = new Permit();
        final int _tmpPermitId;
        _tmpPermitId = _cursor.getInt(_cursorIndexOfPermitId);
        _item.setPermitId(_tmpPermitId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpPermitNumber;
        if (_cursor.isNull(_cursorIndexOfPermitNumber)) {
          _tmpPermitNumber = null;
        } else {
          _tmpPermitNumber = _cursor.getString(_cursorIndexOfPermitNumber);
        }
        _item.setPermitNumber(_tmpPermitNumber);
        final String _tmpPermitType;
        if (_cursor.isNull(_cursorIndexOfPermitType)) {
          _tmpPermitType = null;
        } else {
          _tmpPermitType = _cursor.getString(_cursorIndexOfPermitType);
        }
        _item.setPermitType(_tmpPermitType);
        final String _tmpPermitCode;
        if (_cursor.isNull(_cursorIndexOfPermitCode)) {
          _tmpPermitCode = null;
        } else {
          _tmpPermitCode = _cursor.getString(_cursorIndexOfPermitCode);
        }
        _item.setPermitCode(_tmpPermitCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpPlateType;
        if (_cursor.isNull(_cursorIndexOfPlateType)) {
          _tmpPlateType = null;
        } else {
          _tmpPlateType = _cursor.getString(_cursorIndexOfPlateType);
        }
        _item.setPlateType(_tmpPlateType);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final Date _tmpExpiration;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfExpiration);
        }
        _tmpExpiration = Converters.fromTimestamp(_tmp);
        _item.setExpiration(_tmpExpiration);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item.setStateCode(_tmpStateCode);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _item.setBodyCode(_tmpBodyCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final String _tmpClassCode;
        if (_cursor.isNull(_cursorIndexOfClassCode)) {
          _tmpClassCode = null;
        } else {
          _tmpClassCode = _cursor.getString(_cursorIndexOfClassCode);
        }
        _item.setClassCode(_tmpClassCode);
        final Date _tmpBeginDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfBeginDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfBeginDate);
        }
        _tmpBeginDate = Converters.fromTimestamp(_tmp_1);
        _item.setBeginDate(_tmpBeginDate);
        final Date _tmpEndDate;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfEndDate);
        }
        _tmpEndDate = Converters.fromTimestamp(_tmp_2);
        _item.setEndDate(_tmpEndDate);
        final String _tmpPermitHolder;
        if (_cursor.isNull(_cursorIndexOfPermitHolder)) {
          _tmpPermitHolder = null;
        } else {
          _tmpPermitHolder = _cursor.getString(_cursorIndexOfPermitHolder);
        }
        _item.setPermitHolder(_tmpPermitHolder);
        final int _tmpStudentId;
        _tmpStudentId = _cursor.getInt(_cursorIndexOfStudentId);
        _item.setStudentId(_tmpStudentId);
        final String _tmpAddress1;
        if (_cursor.isNull(_cursorIndexOfAddress1)) {
          _tmpAddress1 = null;
        } else {
          _tmpAddress1 = _cursor.getString(_cursorIndexOfAddress1);
        }
        _item.setAddress1(_tmpAddress1);
        final String _tmpAddress2;
        if (_cursor.isNull(_cursorIndexOfAddress2)) {
          _tmpAddress2 = null;
        } else {
          _tmpAddress2 = _cursor.getString(_cursorIndexOfAddress2);
        }
        _item.setAddress2(_tmpAddress2);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _item.setPhone(_tmpPhone);
        final String _tmpModelCode;
        if (_cursor.isNull(_cursorIndexOfModelCode)) {
          _tmpModelCode = null;
        } else {
          _tmpModelCode = _cursor.getString(_cursorIndexOfModelCode);
        }
        _item.setModelCode(_tmpModelCode);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Permit searchPermitHistory(final String permit) {
    final String _sql = "select * from permits where permit_number=? order by permit_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (permit == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, permit);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPermitId = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfPermitNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_number");
      final int _cursorIndexOfPermitType = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_type");
      final int _cursorIndexOfPermitCode = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_code");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfPlateType = CursorUtil.getColumnIndexOrThrow(_cursor, "plate_type");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "exp_date");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfBodyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "body_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfClassCode = CursorUtil.getColumnIndexOrThrow(_cursor, "class_code");
      final int _cursorIndexOfBeginDate = CursorUtil.getColumnIndexOrThrow(_cursor, "begin_date");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
      final int _cursorIndexOfPermitHolder = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_holder");
      final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "student_id");
      final int _cursorIndexOfAddress1 = CursorUtil.getColumnIndexOrThrow(_cursor, "address1");
      final int _cursorIndexOfAddress2 = CursorUtil.getColumnIndexOrThrow(_cursor, "address2");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfModelCode = CursorUtil.getColumnIndexOrThrow(_cursor, "model_code");
      final Permit _result;
      if(_cursor.moveToFirst()) {
        _result = new Permit();
        final int _tmpPermitId;
        _tmpPermitId = _cursor.getInt(_cursorIndexOfPermitId);
        _result.setPermitId(_tmpPermitId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpPermitNumber;
        if (_cursor.isNull(_cursorIndexOfPermitNumber)) {
          _tmpPermitNumber = null;
        } else {
          _tmpPermitNumber = _cursor.getString(_cursorIndexOfPermitNumber);
        }
        _result.setPermitNumber(_tmpPermitNumber);
        final String _tmpPermitType;
        if (_cursor.isNull(_cursorIndexOfPermitType)) {
          _tmpPermitType = null;
        } else {
          _tmpPermitType = _cursor.getString(_cursorIndexOfPermitType);
        }
        _result.setPermitType(_tmpPermitType);
        final String _tmpPermitCode;
        if (_cursor.isNull(_cursorIndexOfPermitCode)) {
          _tmpPermitCode = null;
        } else {
          _tmpPermitCode = _cursor.getString(_cursorIndexOfPermitCode);
        }
        _result.setPermitCode(_tmpPermitCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _result.setPlate(_tmpPlate);
        final String _tmpPlateType;
        if (_cursor.isNull(_cursorIndexOfPlateType)) {
          _tmpPlateType = null;
        } else {
          _tmpPlateType = _cursor.getString(_cursorIndexOfPlateType);
        }
        _result.setPlateType(_tmpPlateType);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _result.setVin(_tmpVin);
        final Date _tmpExpiration;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfExpiration);
        }
        _tmpExpiration = Converters.fromTimestamp(_tmp);
        _result.setExpiration(_tmpExpiration);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _result.setStateCode(_tmpStateCode);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _result.setBodyCode(_tmpBodyCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _result.setColorCode(_tmpColorCode);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _result.setMakeCode(_tmpMakeCode);
        final String _tmpClassCode;
        if (_cursor.isNull(_cursorIndexOfClassCode)) {
          _tmpClassCode = null;
        } else {
          _tmpClassCode = _cursor.getString(_cursorIndexOfClassCode);
        }
        _result.setClassCode(_tmpClassCode);
        final Date _tmpBeginDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfBeginDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfBeginDate);
        }
        _tmpBeginDate = Converters.fromTimestamp(_tmp_1);
        _result.setBeginDate(_tmpBeginDate);
        final Date _tmpEndDate;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfEndDate);
        }
        _tmpEndDate = Converters.fromTimestamp(_tmp_2);
        _result.setEndDate(_tmpEndDate);
        final String _tmpPermitHolder;
        if (_cursor.isNull(_cursorIndexOfPermitHolder)) {
          _tmpPermitHolder = null;
        } else {
          _tmpPermitHolder = _cursor.getString(_cursorIndexOfPermitHolder);
        }
        _result.setPermitHolder(_tmpPermitHolder);
        final int _tmpStudentId;
        _tmpStudentId = _cursor.getInt(_cursorIndexOfStudentId);
        _result.setStudentId(_tmpStudentId);
        final String _tmpAddress1;
        if (_cursor.isNull(_cursorIndexOfAddress1)) {
          _tmpAddress1 = null;
        } else {
          _tmpAddress1 = _cursor.getString(_cursorIndexOfAddress1);
        }
        _result.setAddress1(_tmpAddress1);
        final String _tmpAddress2;
        if (_cursor.isNull(_cursorIndexOfAddress2)) {
          _tmpAddress2 = null;
        } else {
          _tmpAddress2 = _cursor.getString(_cursorIndexOfAddress2);
        }
        _result.setAddress2(_tmpAddress2);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _result.setPhone(_tmpPhone);
        final String _tmpModelCode;
        if (_cursor.isNull(_cursorIndexOfModelCode)) {
          _tmpModelCode = null;
        } else {
          _tmpModelCode = _cursor.getString(_cursorIndexOfModelCode);
        }
        _result.setModelCode(_tmpModelCode);
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
  public List<Permit> searchAllPermitHistory(final String permit) {
    final String _sql = "select * from permits where permit_number=? order by permit_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (permit == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, permit);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPermitId = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfPermitNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_number");
      final int _cursorIndexOfPermitType = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_type");
      final int _cursorIndexOfPermitCode = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_code");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfPlateType = CursorUtil.getColumnIndexOrThrow(_cursor, "plate_type");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "exp_date");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfBodyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "body_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfClassCode = CursorUtil.getColumnIndexOrThrow(_cursor, "class_code");
      final int _cursorIndexOfBeginDate = CursorUtil.getColumnIndexOrThrow(_cursor, "begin_date");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
      final int _cursorIndexOfPermitHolder = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_holder");
      final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "student_id");
      final int _cursorIndexOfAddress1 = CursorUtil.getColumnIndexOrThrow(_cursor, "address1");
      final int _cursorIndexOfAddress2 = CursorUtil.getColumnIndexOrThrow(_cursor, "address2");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfModelCode = CursorUtil.getColumnIndexOrThrow(_cursor, "model_code");
      final List<Permit> _result = new ArrayList<Permit>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Permit _item;
        _item = new Permit();
        final int _tmpPermitId;
        _tmpPermitId = _cursor.getInt(_cursorIndexOfPermitId);
        _item.setPermitId(_tmpPermitId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpPermitNumber;
        if (_cursor.isNull(_cursorIndexOfPermitNumber)) {
          _tmpPermitNumber = null;
        } else {
          _tmpPermitNumber = _cursor.getString(_cursorIndexOfPermitNumber);
        }
        _item.setPermitNumber(_tmpPermitNumber);
        final String _tmpPermitType;
        if (_cursor.isNull(_cursorIndexOfPermitType)) {
          _tmpPermitType = null;
        } else {
          _tmpPermitType = _cursor.getString(_cursorIndexOfPermitType);
        }
        _item.setPermitType(_tmpPermitType);
        final String _tmpPermitCode;
        if (_cursor.isNull(_cursorIndexOfPermitCode)) {
          _tmpPermitCode = null;
        } else {
          _tmpPermitCode = _cursor.getString(_cursorIndexOfPermitCode);
        }
        _item.setPermitCode(_tmpPermitCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpPlateType;
        if (_cursor.isNull(_cursorIndexOfPlateType)) {
          _tmpPlateType = null;
        } else {
          _tmpPlateType = _cursor.getString(_cursorIndexOfPlateType);
        }
        _item.setPlateType(_tmpPlateType);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final Date _tmpExpiration;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfExpiration);
        }
        _tmpExpiration = Converters.fromTimestamp(_tmp);
        _item.setExpiration(_tmpExpiration);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item.setStateCode(_tmpStateCode);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _item.setBodyCode(_tmpBodyCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final String _tmpClassCode;
        if (_cursor.isNull(_cursorIndexOfClassCode)) {
          _tmpClassCode = null;
        } else {
          _tmpClassCode = _cursor.getString(_cursorIndexOfClassCode);
        }
        _item.setClassCode(_tmpClassCode);
        final Date _tmpBeginDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfBeginDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfBeginDate);
        }
        _tmpBeginDate = Converters.fromTimestamp(_tmp_1);
        _item.setBeginDate(_tmpBeginDate);
        final Date _tmpEndDate;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfEndDate);
        }
        _tmpEndDate = Converters.fromTimestamp(_tmp_2);
        _item.setEndDate(_tmpEndDate);
        final String _tmpPermitHolder;
        if (_cursor.isNull(_cursorIndexOfPermitHolder)) {
          _tmpPermitHolder = null;
        } else {
          _tmpPermitHolder = _cursor.getString(_cursorIndexOfPermitHolder);
        }
        _item.setPermitHolder(_tmpPermitHolder);
        final int _tmpStudentId;
        _tmpStudentId = _cursor.getInt(_cursorIndexOfStudentId);
        _item.setStudentId(_tmpStudentId);
        final String _tmpAddress1;
        if (_cursor.isNull(_cursorIndexOfAddress1)) {
          _tmpAddress1 = null;
        } else {
          _tmpAddress1 = _cursor.getString(_cursorIndexOfAddress1);
        }
        _item.setAddress1(_tmpAddress1);
        final String _tmpAddress2;
        if (_cursor.isNull(_cursorIndexOfAddress2)) {
          _tmpAddress2 = null;
        } else {
          _tmpAddress2 = _cursor.getString(_cursorIndexOfAddress2);
        }
        _item.setAddress2(_tmpAddress2);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _item.setPhone(_tmpPhone);
        final String _tmpModelCode;
        if (_cursor.isNull(_cursorIndexOfModelCode)) {
          _tmpModelCode = null;
        } else {
          _tmpModelCode = _cursor.getString(_cursorIndexOfModelCode);
        }
        _item.setModelCode(_tmpModelCode);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Permit searchPermitByPlate(final String plate) {
    final String _sql = "select * from permits where plate=? order by permit_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (plate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, plate);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPermitId = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfPermitNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_number");
      final int _cursorIndexOfPermitType = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_type");
      final int _cursorIndexOfPermitCode = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_code");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfPlateType = CursorUtil.getColumnIndexOrThrow(_cursor, "plate_type");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "exp_date");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfBodyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "body_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfClassCode = CursorUtil.getColumnIndexOrThrow(_cursor, "class_code");
      final int _cursorIndexOfBeginDate = CursorUtil.getColumnIndexOrThrow(_cursor, "begin_date");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
      final int _cursorIndexOfPermitHolder = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_holder");
      final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "student_id");
      final int _cursorIndexOfAddress1 = CursorUtil.getColumnIndexOrThrow(_cursor, "address1");
      final int _cursorIndexOfAddress2 = CursorUtil.getColumnIndexOrThrow(_cursor, "address2");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfModelCode = CursorUtil.getColumnIndexOrThrow(_cursor, "model_code");
      final Permit _result;
      if(_cursor.moveToFirst()) {
        _result = new Permit();
        final int _tmpPermitId;
        _tmpPermitId = _cursor.getInt(_cursorIndexOfPermitId);
        _result.setPermitId(_tmpPermitId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpPermitNumber;
        if (_cursor.isNull(_cursorIndexOfPermitNumber)) {
          _tmpPermitNumber = null;
        } else {
          _tmpPermitNumber = _cursor.getString(_cursorIndexOfPermitNumber);
        }
        _result.setPermitNumber(_tmpPermitNumber);
        final String _tmpPermitType;
        if (_cursor.isNull(_cursorIndexOfPermitType)) {
          _tmpPermitType = null;
        } else {
          _tmpPermitType = _cursor.getString(_cursorIndexOfPermitType);
        }
        _result.setPermitType(_tmpPermitType);
        final String _tmpPermitCode;
        if (_cursor.isNull(_cursorIndexOfPermitCode)) {
          _tmpPermitCode = null;
        } else {
          _tmpPermitCode = _cursor.getString(_cursorIndexOfPermitCode);
        }
        _result.setPermitCode(_tmpPermitCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _result.setPlate(_tmpPlate);
        final String _tmpPlateType;
        if (_cursor.isNull(_cursorIndexOfPlateType)) {
          _tmpPlateType = null;
        } else {
          _tmpPlateType = _cursor.getString(_cursorIndexOfPlateType);
        }
        _result.setPlateType(_tmpPlateType);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _result.setVin(_tmpVin);
        final Date _tmpExpiration;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfExpiration);
        }
        _tmpExpiration = Converters.fromTimestamp(_tmp);
        _result.setExpiration(_tmpExpiration);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _result.setStateCode(_tmpStateCode);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _result.setBodyCode(_tmpBodyCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _result.setColorCode(_tmpColorCode);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _result.setMakeCode(_tmpMakeCode);
        final String _tmpClassCode;
        if (_cursor.isNull(_cursorIndexOfClassCode)) {
          _tmpClassCode = null;
        } else {
          _tmpClassCode = _cursor.getString(_cursorIndexOfClassCode);
        }
        _result.setClassCode(_tmpClassCode);
        final Date _tmpBeginDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfBeginDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfBeginDate);
        }
        _tmpBeginDate = Converters.fromTimestamp(_tmp_1);
        _result.setBeginDate(_tmpBeginDate);
        final Date _tmpEndDate;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfEndDate);
        }
        _tmpEndDate = Converters.fromTimestamp(_tmp_2);
        _result.setEndDate(_tmpEndDate);
        final String _tmpPermitHolder;
        if (_cursor.isNull(_cursorIndexOfPermitHolder)) {
          _tmpPermitHolder = null;
        } else {
          _tmpPermitHolder = _cursor.getString(_cursorIndexOfPermitHolder);
        }
        _result.setPermitHolder(_tmpPermitHolder);
        final int _tmpStudentId;
        _tmpStudentId = _cursor.getInt(_cursorIndexOfStudentId);
        _result.setStudentId(_tmpStudentId);
        final String _tmpAddress1;
        if (_cursor.isNull(_cursorIndexOfAddress1)) {
          _tmpAddress1 = null;
        } else {
          _tmpAddress1 = _cursor.getString(_cursorIndexOfAddress1);
        }
        _result.setAddress1(_tmpAddress1);
        final String _tmpAddress2;
        if (_cursor.isNull(_cursorIndexOfAddress2)) {
          _tmpAddress2 = null;
        } else {
          _tmpAddress2 = _cursor.getString(_cursorIndexOfAddress2);
        }
        _result.setAddress2(_tmpAddress2);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _result.setPhone(_tmpPhone);
        final String _tmpModelCode;
        if (_cursor.isNull(_cursorIndexOfModelCode)) {
          _tmpModelCode = null;
        } else {
          _tmpModelCode = _cursor.getString(_cursorIndexOfModelCode);
        }
        _result.setModelCode(_tmpModelCode);
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
  public Permit searchPermitByVin(final String vin, final String state, final int custId) {
    final String _sql = "select * from permits where vin=? and state_code=? and custid=? order by permit_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (vin == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, vin);
    }
    _argIndex = 2;
    if (state == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, state);
    }
    _argIndex = 3;
    _statement.bindLong(_argIndex, custId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPermitId = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfPermitNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_number");
      final int _cursorIndexOfPermitType = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_type");
      final int _cursorIndexOfPermitCode = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_code");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfPlateType = CursorUtil.getColumnIndexOrThrow(_cursor, "plate_type");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "exp_date");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfBodyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "body_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfClassCode = CursorUtil.getColumnIndexOrThrow(_cursor, "class_code");
      final int _cursorIndexOfBeginDate = CursorUtil.getColumnIndexOrThrow(_cursor, "begin_date");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
      final int _cursorIndexOfPermitHolder = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_holder");
      final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "student_id");
      final int _cursorIndexOfAddress1 = CursorUtil.getColumnIndexOrThrow(_cursor, "address1");
      final int _cursorIndexOfAddress2 = CursorUtil.getColumnIndexOrThrow(_cursor, "address2");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfModelCode = CursorUtil.getColumnIndexOrThrow(_cursor, "model_code");
      final Permit _result;
      if(_cursor.moveToFirst()) {
        _result = new Permit();
        final int _tmpPermitId;
        _tmpPermitId = _cursor.getInt(_cursorIndexOfPermitId);
        _result.setPermitId(_tmpPermitId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpPermitNumber;
        if (_cursor.isNull(_cursorIndexOfPermitNumber)) {
          _tmpPermitNumber = null;
        } else {
          _tmpPermitNumber = _cursor.getString(_cursorIndexOfPermitNumber);
        }
        _result.setPermitNumber(_tmpPermitNumber);
        final String _tmpPermitType;
        if (_cursor.isNull(_cursorIndexOfPermitType)) {
          _tmpPermitType = null;
        } else {
          _tmpPermitType = _cursor.getString(_cursorIndexOfPermitType);
        }
        _result.setPermitType(_tmpPermitType);
        final String _tmpPermitCode;
        if (_cursor.isNull(_cursorIndexOfPermitCode)) {
          _tmpPermitCode = null;
        } else {
          _tmpPermitCode = _cursor.getString(_cursorIndexOfPermitCode);
        }
        _result.setPermitCode(_tmpPermitCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _result.setPlate(_tmpPlate);
        final String _tmpPlateType;
        if (_cursor.isNull(_cursorIndexOfPlateType)) {
          _tmpPlateType = null;
        } else {
          _tmpPlateType = _cursor.getString(_cursorIndexOfPlateType);
        }
        _result.setPlateType(_tmpPlateType);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _result.setVin(_tmpVin);
        final Date _tmpExpiration;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfExpiration);
        }
        _tmpExpiration = Converters.fromTimestamp(_tmp);
        _result.setExpiration(_tmpExpiration);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _result.setStateCode(_tmpStateCode);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _result.setBodyCode(_tmpBodyCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _result.setColorCode(_tmpColorCode);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _result.setMakeCode(_tmpMakeCode);
        final String _tmpClassCode;
        if (_cursor.isNull(_cursorIndexOfClassCode)) {
          _tmpClassCode = null;
        } else {
          _tmpClassCode = _cursor.getString(_cursorIndexOfClassCode);
        }
        _result.setClassCode(_tmpClassCode);
        final Date _tmpBeginDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfBeginDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfBeginDate);
        }
        _tmpBeginDate = Converters.fromTimestamp(_tmp_1);
        _result.setBeginDate(_tmpBeginDate);
        final Date _tmpEndDate;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfEndDate);
        }
        _tmpEndDate = Converters.fromTimestamp(_tmp_2);
        _result.setEndDate(_tmpEndDate);
        final String _tmpPermitHolder;
        if (_cursor.isNull(_cursorIndexOfPermitHolder)) {
          _tmpPermitHolder = null;
        } else {
          _tmpPermitHolder = _cursor.getString(_cursorIndexOfPermitHolder);
        }
        _result.setPermitHolder(_tmpPermitHolder);
        final int _tmpStudentId;
        _tmpStudentId = _cursor.getInt(_cursorIndexOfStudentId);
        _result.setStudentId(_tmpStudentId);
        final String _tmpAddress1;
        if (_cursor.isNull(_cursorIndexOfAddress1)) {
          _tmpAddress1 = null;
        } else {
          _tmpAddress1 = _cursor.getString(_cursorIndexOfAddress1);
        }
        _result.setAddress1(_tmpAddress1);
        final String _tmpAddress2;
        if (_cursor.isNull(_cursorIndexOfAddress2)) {
          _tmpAddress2 = null;
        } else {
          _tmpAddress2 = _cursor.getString(_cursorIndexOfAddress2);
        }
        _result.setAddress2(_tmpAddress2);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _result.setPhone(_tmpPhone);
        final String _tmpModelCode;
        if (_cursor.isNull(_cursorIndexOfModelCode)) {
          _tmpModelCode = null;
        } else {
          _tmpModelCode = _cursor.getString(_cursorIndexOfModelCode);
        }
        _result.setModelCode(_tmpModelCode);
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
  public List<Permit> searchAllPermitByPlate(final String plate, final String state) {
    final String _sql = "select * from permits where plate=? and state_code=? order by permit_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (plate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, plate);
    }
    _argIndex = 2;
    if (state == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, state);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPermitId = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfPermitNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_number");
      final int _cursorIndexOfPermitType = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_type");
      final int _cursorIndexOfPermitCode = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_code");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfPlateType = CursorUtil.getColumnIndexOrThrow(_cursor, "plate_type");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "exp_date");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfBodyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "body_code");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfClassCode = CursorUtil.getColumnIndexOrThrow(_cursor, "class_code");
      final int _cursorIndexOfBeginDate = CursorUtil.getColumnIndexOrThrow(_cursor, "begin_date");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
      final int _cursorIndexOfPermitHolder = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_holder");
      final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "student_id");
      final int _cursorIndexOfAddress1 = CursorUtil.getColumnIndexOrThrow(_cursor, "address1");
      final int _cursorIndexOfAddress2 = CursorUtil.getColumnIndexOrThrow(_cursor, "address2");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfModelCode = CursorUtil.getColumnIndexOrThrow(_cursor, "model_code");
      final List<Permit> _result = new ArrayList<Permit>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Permit _item;
        _item = new Permit();
        final int _tmpPermitId;
        _tmpPermitId = _cursor.getInt(_cursorIndexOfPermitId);
        _item.setPermitId(_tmpPermitId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpPermitNumber;
        if (_cursor.isNull(_cursorIndexOfPermitNumber)) {
          _tmpPermitNumber = null;
        } else {
          _tmpPermitNumber = _cursor.getString(_cursorIndexOfPermitNumber);
        }
        _item.setPermitNumber(_tmpPermitNumber);
        final String _tmpPermitType;
        if (_cursor.isNull(_cursorIndexOfPermitType)) {
          _tmpPermitType = null;
        } else {
          _tmpPermitType = _cursor.getString(_cursorIndexOfPermitType);
        }
        _item.setPermitType(_tmpPermitType);
        final String _tmpPermitCode;
        if (_cursor.isNull(_cursorIndexOfPermitCode)) {
          _tmpPermitCode = null;
        } else {
          _tmpPermitCode = _cursor.getString(_cursorIndexOfPermitCode);
        }
        _item.setPermitCode(_tmpPermitCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpPlateType;
        if (_cursor.isNull(_cursorIndexOfPlateType)) {
          _tmpPlateType = null;
        } else {
          _tmpPlateType = _cursor.getString(_cursorIndexOfPlateType);
        }
        _item.setPlateType(_tmpPlateType);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final Date _tmpExpiration;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfExpiration);
        }
        _tmpExpiration = Converters.fromTimestamp(_tmp);
        _item.setExpiration(_tmpExpiration);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item.setStateCode(_tmpStateCode);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _item.setBodyCode(_tmpBodyCode);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final String _tmpClassCode;
        if (_cursor.isNull(_cursorIndexOfClassCode)) {
          _tmpClassCode = null;
        } else {
          _tmpClassCode = _cursor.getString(_cursorIndexOfClassCode);
        }
        _item.setClassCode(_tmpClassCode);
        final Date _tmpBeginDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfBeginDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfBeginDate);
        }
        _tmpBeginDate = Converters.fromTimestamp(_tmp_1);
        _item.setBeginDate(_tmpBeginDate);
        final Date _tmpEndDate;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfEndDate);
        }
        _tmpEndDate = Converters.fromTimestamp(_tmp_2);
        _item.setEndDate(_tmpEndDate);
        final String _tmpPermitHolder;
        if (_cursor.isNull(_cursorIndexOfPermitHolder)) {
          _tmpPermitHolder = null;
        } else {
          _tmpPermitHolder = _cursor.getString(_cursorIndexOfPermitHolder);
        }
        _item.setPermitHolder(_tmpPermitHolder);
        final int _tmpStudentId;
        _tmpStudentId = _cursor.getInt(_cursorIndexOfStudentId);
        _item.setStudentId(_tmpStudentId);
        final String _tmpAddress1;
        if (_cursor.isNull(_cursorIndexOfAddress1)) {
          _tmpAddress1 = null;
        } else {
          _tmpAddress1 = _cursor.getString(_cursorIndexOfAddress1);
        }
        _item.setAddress1(_tmpAddress1);
        final String _tmpAddress2;
        if (_cursor.isNull(_cursorIndexOfAddress2)) {
          _tmpAddress2 = null;
        } else {
          _tmpAddress2 = _cursor.getString(_cursorIndexOfAddress2);
        }
        _item.setAddress2(_tmpAddress2);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _item.setPhone(_tmpPhone);
        final String _tmpModelCode;
        if (_cursor.isNull(_cursorIndexOfModelCode)) {
          _tmpModelCode = null;
        } else {
          _tmpModelCode = _cursor.getString(_cursorIndexOfModelCode);
        }
        _item.setModelCode(_tmpModelCode);
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
