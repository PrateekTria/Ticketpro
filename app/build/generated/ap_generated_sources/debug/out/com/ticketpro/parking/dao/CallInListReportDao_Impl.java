package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.CallInReport;
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
public final class CallInListReportDao_Impl implements CallInListReportDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CallInReport> __insertionAdapterOfCallInReport;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public CallInListReportDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCallInReport = new EntityInsertionAdapter<CallInReport>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `call_in_reports` (`report_id`,`userid`,`custid`,`call_in_date`,`plate`,`plate_type`,`state`,`from_search`,`from_hit`,`action_taken`,`photo1`,`photo2`,`photo3`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CallInReport value) {
        stmt.bindLong(1, value.getReportId());
        stmt.bindLong(2, value.getUserId());
        stmt.bindLong(3, value.getCustid());
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getCallInDate());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        if (value.getPlate() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getPlate());
        }
        if (value.getPlateType() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getPlateType());
        }
        if (value.getState() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getState());
        }
        if (value.getFromSearch() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getFromSearch());
        }
        if (value.getFromHit() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getFromHit());
        }
        if (value.getActionTaken() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getActionTaken());
        }
        if (value.getPhoto1() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getPhoto1());
        }
        if (value.getPhoto2() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getPhoto2());
        }
        if (value.getPhoto3() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getPhoto3());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from call_in_reports";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from call_in_reports where report_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertCallInReport(final CallInReport... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCallInReport.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertCallInReport(final CallInReport data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCallInReport.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertCallInReportList(final List<CallInReport> callInReortList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCallInReport.insert(callInReortList);
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
  public List<CallInReport> getCallInReports() {
    final String _sql = "select * from call_in_reports";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfReportId = CursorUtil.getColumnIndexOrThrow(_cursor, "report_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfCustid = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfCallInDate = CursorUtil.getColumnIndexOrThrow(_cursor, "call_in_date");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfPlateType = CursorUtil.getColumnIndexOrThrow(_cursor, "plate_type");
      final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
      final int _cursorIndexOfFromSearch = CursorUtil.getColumnIndexOrThrow(_cursor, "from_search");
      final int _cursorIndexOfFromHit = CursorUtil.getColumnIndexOrThrow(_cursor, "from_hit");
      final int _cursorIndexOfActionTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "action_taken");
      final int _cursorIndexOfPhoto1 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo1");
      final int _cursorIndexOfPhoto2 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo2");
      final int _cursorIndexOfPhoto3 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo3");
      final List<CallInReport> _result = new ArrayList<CallInReport>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final CallInReport _item;
        _item = new CallInReport();
        final int _tmpReportId;
        _tmpReportId = _cursor.getInt(_cursorIndexOfReportId);
        _item.setReportId(_tmpReportId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustid;
        _tmpCustid = _cursor.getInt(_cursorIndexOfCustid);
        _item.setCustid(_tmpCustid);
        final Date _tmpCallInDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfCallInDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfCallInDate);
        }
        _tmpCallInDate = Converters.fromTimestamp(_tmp);
        _item.setCallInDate(_tmpCallInDate);
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
        final String _tmpState;
        if (_cursor.isNull(_cursorIndexOfState)) {
          _tmpState = null;
        } else {
          _tmpState = _cursor.getString(_cursorIndexOfState);
        }
        _item.setState(_tmpState);
        final String _tmpFromSearch;
        if (_cursor.isNull(_cursorIndexOfFromSearch)) {
          _tmpFromSearch = null;
        } else {
          _tmpFromSearch = _cursor.getString(_cursorIndexOfFromSearch);
        }
        _item.setFromSearch(_tmpFromSearch);
        final String _tmpFromHit;
        if (_cursor.isNull(_cursorIndexOfFromHit)) {
          _tmpFromHit = null;
        } else {
          _tmpFromHit = _cursor.getString(_cursorIndexOfFromHit);
        }
        _item.setFromHit(_tmpFromHit);
        final String _tmpActionTaken;
        if (_cursor.isNull(_cursorIndexOfActionTaken)) {
          _tmpActionTaken = null;
        } else {
          _tmpActionTaken = _cursor.getString(_cursorIndexOfActionTaken);
        }
        _item.setActionTaken(_tmpActionTaken);
        final String _tmpPhoto1;
        if (_cursor.isNull(_cursorIndexOfPhoto1)) {
          _tmpPhoto1 = null;
        } else {
          _tmpPhoto1 = _cursor.getString(_cursorIndexOfPhoto1);
        }
        _item.setPhoto1(_tmpPhoto1);
        final String _tmpPhoto2;
        if (_cursor.isNull(_cursorIndexOfPhoto2)) {
          _tmpPhoto2 = null;
        } else {
          _tmpPhoto2 = _cursor.getString(_cursorIndexOfPhoto2);
        }
        _item.setPhoto2(_tmpPhoto2);
        final String _tmpPhoto3;
        if (_cursor.isNull(_cursorIndexOfPhoto3)) {
          _tmpPhoto3 = null;
        } else {
          _tmpPhoto3 = _cursor.getString(_cursorIndexOfPhoto3);
        }
        _item.setPhoto3(_tmpPhoto3);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public CallInReport getCallInReportByPrimaryKey(final String primaryKey) {
    final String _sql = "select * from call_in_reports where report_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (primaryKey == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, primaryKey);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfReportId = CursorUtil.getColumnIndexOrThrow(_cursor, "report_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfCustid = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfCallInDate = CursorUtil.getColumnIndexOrThrow(_cursor, "call_in_date");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfPlateType = CursorUtil.getColumnIndexOrThrow(_cursor, "plate_type");
      final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
      final int _cursorIndexOfFromSearch = CursorUtil.getColumnIndexOrThrow(_cursor, "from_search");
      final int _cursorIndexOfFromHit = CursorUtil.getColumnIndexOrThrow(_cursor, "from_hit");
      final int _cursorIndexOfActionTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "action_taken");
      final int _cursorIndexOfPhoto1 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo1");
      final int _cursorIndexOfPhoto2 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo2");
      final int _cursorIndexOfPhoto3 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo3");
      final CallInReport _result;
      if(_cursor.moveToFirst()) {
        _result = new CallInReport();
        final int _tmpReportId;
        _tmpReportId = _cursor.getInt(_cursorIndexOfReportId);
        _result.setReportId(_tmpReportId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final int _tmpCustid;
        _tmpCustid = _cursor.getInt(_cursorIndexOfCustid);
        _result.setCustid(_tmpCustid);
        final Date _tmpCallInDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfCallInDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfCallInDate);
        }
        _tmpCallInDate = Converters.fromTimestamp(_tmp);
        _result.setCallInDate(_tmpCallInDate);
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
        final String _tmpState;
        if (_cursor.isNull(_cursorIndexOfState)) {
          _tmpState = null;
        } else {
          _tmpState = _cursor.getString(_cursorIndexOfState);
        }
        _result.setState(_tmpState);
        final String _tmpFromSearch;
        if (_cursor.isNull(_cursorIndexOfFromSearch)) {
          _tmpFromSearch = null;
        } else {
          _tmpFromSearch = _cursor.getString(_cursorIndexOfFromSearch);
        }
        _result.setFromSearch(_tmpFromSearch);
        final String _tmpFromHit;
        if (_cursor.isNull(_cursorIndexOfFromHit)) {
          _tmpFromHit = null;
        } else {
          _tmpFromHit = _cursor.getString(_cursorIndexOfFromHit);
        }
        _result.setFromHit(_tmpFromHit);
        final String _tmpActionTaken;
        if (_cursor.isNull(_cursorIndexOfActionTaken)) {
          _tmpActionTaken = null;
        } else {
          _tmpActionTaken = _cursor.getString(_cursorIndexOfActionTaken);
        }
        _result.setActionTaken(_tmpActionTaken);
        final String _tmpPhoto1;
        if (_cursor.isNull(_cursorIndexOfPhoto1)) {
          _tmpPhoto1 = null;
        } else {
          _tmpPhoto1 = _cursor.getString(_cursorIndexOfPhoto1);
        }
        _result.setPhoto1(_tmpPhoto1);
        final String _tmpPhoto2;
        if (_cursor.isNull(_cursorIndexOfPhoto2)) {
          _tmpPhoto2 = null;
        } else {
          _tmpPhoto2 = _cursor.getString(_cursorIndexOfPhoto2);
        }
        _result.setPhoto2(_tmpPhoto2);
        final String _tmpPhoto3;
        if (_cursor.isNull(_cursorIndexOfPhoto3)) {
          _tmpPhoto3 = null;
        } else {
          _tmpPhoto3 = _cursor.getString(_cursorIndexOfPhoto3);
        }
        _result.setPhoto3(_tmpPhoto3);
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
  public int getLastInsertId() {
    final String _sql = "select max(report_id) as max_report_id from call_in_reports";
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
