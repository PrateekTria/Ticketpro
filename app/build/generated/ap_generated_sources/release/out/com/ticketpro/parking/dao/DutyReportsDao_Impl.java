package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.DutyReport;
import com.ticketpro.util.Converters;
import io.reactivex.Completable;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.Void;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DutyReportsDao_Impl implements DutyReportsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DutyReport> __insertionAdapterOfDutyReport;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public DutyReportsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDutyReport = new EntityInsertionAdapter<DutyReport>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `duty_reports` (`report_id`,`userid`,`custid`,`duty_id`,`date_in`,`date_out`,`signature`,`device_id`,`duty_report_id`,`sync_status`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DutyReport value) {
        stmt.bindLong(1, value.getReportId());
        stmt.bindLong(2, value.getUserId());
        stmt.bindLong(3, value.getCustId());
        stmt.bindLong(4, value.getDutyId());
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getDateIn());
        if (_tmp == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, _tmp);
        }
        final Long _tmp_1;
        _tmp_1 = Converters.dateToTimestamp(value.getDateOut());
        if (_tmp_1 == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, _tmp_1);
        }
        if (value.getSignature() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getSignature());
        }
        stmt.bindLong(8, value.getDeviceId());
        if (value.getDuty_report_id() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getDuty_report_id());
        }
        if (value.getSync_status() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getSync_status());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from duty_reports";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from duty_reports where report_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertDutyReport(final DutyReport... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDutyReport.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertDutyReport(final DutyReport data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDutyReport.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertDutyReportList(final List<DutyReport> DutyReportsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDutyReport.insert(DutyReportsList);
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
  public List<DutyReport> getDutyReports(final int userId) {
    final String _sql = "select *,duties.code as duty_title from duty_reports, duties where userid=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfReportId = CursorUtil.getColumnIndexOrThrow(_cursor, "report_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfDutyId = CursorUtil.getColumnIndexOrThrow(_cursor, "duty_id");
      final int _cursorIndexOfDateIn = CursorUtil.getColumnIndexOrThrow(_cursor, "date_in");
      final int _cursorIndexOfDateOut = CursorUtil.getColumnIndexOrThrow(_cursor, "date_out");
      final int _cursorIndexOfSignature = CursorUtil.getColumnIndexOrThrow(_cursor, "signature");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfDutyReportId = CursorUtil.getColumnIndexOrThrow(_cursor, "duty_report_id");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final int _cursorIndexOfDutyId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "duty_id");
      final int _cursorIndexOfCustId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final List<DutyReport> _result = new ArrayList<DutyReport>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final DutyReport _item;
        _item = new DutyReport();
        final int _tmpReportId;
        _tmpReportId = _cursor.getInt(_cursorIndexOfReportId);
        _item.setReportId(_tmpReportId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpDutyId;
        _tmpDutyId = _cursor.getInt(_cursorIndexOfDutyId);
        _item.setDutyId(_tmpDutyId);
        final Date _tmpDateIn;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDateIn)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDateIn);
        }
        _tmpDateIn = Converters.fromTimestamp(_tmp);
        _item.setDateIn(_tmpDateIn);
        final Date _tmpDateOut;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfDateOut)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfDateOut);
        }
        _tmpDateOut = Converters.fromTimestamp(_tmp_1);
        _item.setDateOut(_tmpDateOut);
        final String _tmpSignature;
        if (_cursor.isNull(_cursorIndexOfSignature)) {
          _tmpSignature = null;
        } else {
          _tmpSignature = _cursor.getString(_cursorIndexOfSignature);
        }
        _item.setSignature(_tmpSignature);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final String _tmpDuty_report_id;
        if (_cursor.isNull(_cursorIndexOfDutyReportId)) {
          _tmpDuty_report_id = null;
        } else {
          _tmpDuty_report_id = _cursor.getString(_cursorIndexOfDutyReportId);
        }
        _item.setDuty_report_id(_tmpDuty_report_id);
        final String _tmpSync_status;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSync_status = null;
        } else {
          _tmpSync_status = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _item.setSync_status(_tmpSync_status);
        final int _tmpDutyId_1;
        _tmpDutyId_1 = _cursor.getInt(_cursorIndexOfDutyId_1);
        _item.setDutyId(_tmpDutyId_1);
        final int _tmpCustId_1;
        _tmpCustId_1 = _cursor.getInt(_cursorIndexOfCustId_1);
        _item.setCustId(_tmpCustId_1);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public DutyReport getDutyReportByPrimaryKey(final String primaryKey) {
    final String _sql = "select * from duty_reports where report_id=?";
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
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfDutyId = CursorUtil.getColumnIndexOrThrow(_cursor, "duty_id");
      final int _cursorIndexOfDateIn = CursorUtil.getColumnIndexOrThrow(_cursor, "date_in");
      final int _cursorIndexOfDateOut = CursorUtil.getColumnIndexOrThrow(_cursor, "date_out");
      final int _cursorIndexOfSignature = CursorUtil.getColumnIndexOrThrow(_cursor, "signature");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfDutyReportId = CursorUtil.getColumnIndexOrThrow(_cursor, "duty_report_id");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final DutyReport _result;
      if(_cursor.moveToFirst()) {
        _result = new DutyReport();
        final int _tmpReportId;
        _tmpReportId = _cursor.getInt(_cursorIndexOfReportId);
        _result.setReportId(_tmpReportId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final int _tmpDutyId;
        _tmpDutyId = _cursor.getInt(_cursorIndexOfDutyId);
        _result.setDutyId(_tmpDutyId);
        final Date _tmpDateIn;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDateIn)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDateIn);
        }
        _tmpDateIn = Converters.fromTimestamp(_tmp);
        _result.setDateIn(_tmpDateIn);
        final Date _tmpDateOut;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfDateOut)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfDateOut);
        }
        _tmpDateOut = Converters.fromTimestamp(_tmp_1);
        _result.setDateOut(_tmpDateOut);
        final String _tmpSignature;
        if (_cursor.isNull(_cursorIndexOfSignature)) {
          _tmpSignature = null;
        } else {
          _tmpSignature = _cursor.getString(_cursorIndexOfSignature);
        }
        _result.setSignature(_tmpSignature);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _result.setDeviceId(_tmpDeviceId);
        final String _tmpDuty_report_id;
        if (_cursor.isNull(_cursorIndexOfDutyReportId)) {
          _tmpDuty_report_id = null;
        } else {
          _tmpDuty_report_id = _cursor.getString(_cursorIndexOfDutyReportId);
        }
        _result.setDuty_report_id(_tmpDuty_report_id);
        final String _tmpSync_status;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSync_status = null;
        } else {
          _tmpSync_status = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _result.setSync_status(_tmpSync_status);
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
    final String _sql = "select max(report_id) as max_report_id from duty_reports";
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
