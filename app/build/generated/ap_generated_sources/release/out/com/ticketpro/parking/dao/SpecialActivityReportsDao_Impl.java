package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.SpecialActivityReport;
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
public final class SpecialActivityReportsDao_Impl implements SpecialActivityReportsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SpecialActivityReport> __insertionAdapterOfSpecialActivityReport;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public SpecialActivityReportsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSpecialActivityReport = new EntityInsertionAdapter<SpecialActivityReport>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `special_activity_reports` (`report_id`,`userid`,`custid`,`activity_id`,`disposition_id`,`start_date`,`end_date`,`start_time`,`end_time`,`case_number`,`street_address`,`notes`,`photo1`,`photo2`,`photo3`,`latitude`,`longitude`,`autoSelect`,`createDate`,`duration`,`device_id`,`location`,`activityName`,`ticket_count`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SpecialActivityReport value) {
        stmt.bindLong(1, value.getReportId());
        stmt.bindLong(2, value.getUserId());
        stmt.bindLong(3, value.getCustId());
        stmt.bindLong(4, value.getActivityId());
        stmt.bindLong(5, value.getDispositionId());
        if (value.getStartDate() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getStartDate());
        }
        if (value.getEndDate() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getEndDate());
        }
        if (value.getStartTime() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getStartTime());
        }
        if (value.getEndTime() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getEndTime());
        }
        if (value.getCaseNumber() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getCaseNumber());
        }
        if (value.getStreetAddress() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getStreetAddress());
        }
        if (value.getNotes() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getNotes());
        }
        if (value.getPhoto1() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getPhoto1());
        }
        if (value.getPhoto2() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getPhoto2());
        }
        if (value.getPhoto3() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getPhoto3());
        }
        if (value.getLatitude() == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindString(16, value.getLatitude());
        }
        if (value.getLongitude() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindString(17, value.getLongitude());
        }
        if (value.getAutoSelect() == null) {
          stmt.bindNull(18);
        } else {
          stmt.bindString(18, value.getAutoSelect());
        }
        if (value.getCreatedDate() == null) {
          stmt.bindNull(19);
        } else {
          stmt.bindString(19, value.getCreatedDate());
        }
        if (value.getDuration() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.getDuration());
        }
        stmt.bindLong(21, value.getDeviceId());
        if (value.getLocation() == null) {
          stmt.bindNull(22);
        } else {
          stmt.bindString(22, value.getLocation());
        }
        if (value.getActivityName() == null) {
          stmt.bindNull(23);
        } else {
          stmt.bindString(23, value.getActivityName());
        }
        if (value.getTicketCount() == null) {
          stmt.bindNull(24);
        } else {
          stmt.bindString(24, value.getTicketCount());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from special_activity_reports";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from special_activity_reports where report_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertSpecialActivityReport(final SpecialActivityReport... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSpecialActivityReport.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertSpecialActivityReport(final SpecialActivityReport data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSpecialActivityReport.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertSpecialActivityReportList(
      final List<SpecialActivityReport> SpecialActivityReportsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSpecialActivityReport.insert(SpecialActivityReportsList);
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
  public List<SpecialActivityReport> getSpecialActivityReports(final int custId,
      final String currentDate) {
    final String _sql = "select * from special_activity_reports where createDate=? AND custid=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (currentDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, currentDate);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, custId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfReportId = CursorUtil.getColumnIndexOrThrow(_cursor, "report_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfActivityId = CursorUtil.getColumnIndexOrThrow(_cursor, "activity_id");
      final int _cursorIndexOfDispositionId = CursorUtil.getColumnIndexOrThrow(_cursor, "disposition_id");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_time");
      final int _cursorIndexOfCaseNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "case_number");
      final int _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfPhoto1 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo1");
      final int _cursorIndexOfPhoto2 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo2");
      final int _cursorIndexOfPhoto3 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo3");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfAutoSelect = CursorUtil.getColumnIndexOrThrow(_cursor, "autoSelect");
      final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createDate");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfActivityName = CursorUtil.getColumnIndexOrThrow(_cursor, "activityName");
      final int _cursorIndexOfTicketCount = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_count");
      final List<SpecialActivityReport> _result = new ArrayList<SpecialActivityReport>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final SpecialActivityReport _item;
        _item = new SpecialActivityReport();
        final int _tmpReportId;
        _tmpReportId = _cursor.getInt(_cursorIndexOfReportId);
        _item.setReportId(_tmpReportId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpActivityId;
        _tmpActivityId = _cursor.getInt(_cursorIndexOfActivityId);
        _item.setActivityId(_tmpActivityId);
        final int _tmpDispositionId;
        _tmpDispositionId = _cursor.getInt(_cursorIndexOfDispositionId);
        _item.setDispositionId(_tmpDispositionId);
        final String _tmpStartDate;
        if (_cursor.isNull(_cursorIndexOfStartDate)) {
          _tmpStartDate = null;
        } else {
          _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
        }
        _item.setStartDate(_tmpStartDate);
        final String _tmpEndDate;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmpEndDate = null;
        } else {
          _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
        }
        _item.setEndDate(_tmpEndDate);
        final String _tmpStartTime;
        if (_cursor.isNull(_cursorIndexOfStartTime)) {
          _tmpStartTime = null;
        } else {
          _tmpStartTime = _cursor.getString(_cursorIndexOfStartTime);
        }
        _item.setStartTime(_tmpStartTime);
        final String _tmpEndTime;
        if (_cursor.isNull(_cursorIndexOfEndTime)) {
          _tmpEndTime = null;
        } else {
          _tmpEndTime = _cursor.getString(_cursorIndexOfEndTime);
        }
        _item.setEndTime(_tmpEndTime);
        final String _tmpCaseNumber;
        if (_cursor.isNull(_cursorIndexOfCaseNumber)) {
          _tmpCaseNumber = null;
        } else {
          _tmpCaseNumber = _cursor.getString(_cursorIndexOfCaseNumber);
        }
        _item.setCaseNumber(_tmpCaseNumber);
        final String _tmpStreetAddress;
        if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
          _tmpStreetAddress = null;
        } else {
          _tmpStreetAddress = _cursor.getString(_cursorIndexOfStreetAddress);
        }
        _item.setStreetAddress(_tmpStreetAddress);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _item.setNotes(_tmpNotes);
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
        final String _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        }
        _item.setLatitude(_tmpLatitude);
        final String _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        }
        _item.setLongitude(_tmpLongitude);
        final String _tmpAutoSelect;
        if (_cursor.isNull(_cursorIndexOfAutoSelect)) {
          _tmpAutoSelect = null;
        } else {
          _tmpAutoSelect = _cursor.getString(_cursorIndexOfAutoSelect);
        }
        _item.setAutoSelect(_tmpAutoSelect);
        final String _tmpCreatedDate;
        if (_cursor.isNull(_cursorIndexOfCreatedDate)) {
          _tmpCreatedDate = null;
        } else {
          _tmpCreatedDate = _cursor.getString(_cursorIndexOfCreatedDate);
        }
        _item.setCreatedDate(_tmpCreatedDate);
        final String _tmpDuration;
        if (_cursor.isNull(_cursorIndexOfDuration)) {
          _tmpDuration = null;
        } else {
          _tmpDuration = _cursor.getString(_cursorIndexOfDuration);
        }
        _item.setDuration(_tmpDuration);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
        final String _tmpActivityName;
        if (_cursor.isNull(_cursorIndexOfActivityName)) {
          _tmpActivityName = null;
        } else {
          _tmpActivityName = _cursor.getString(_cursorIndexOfActivityName);
        }
        _item.setActivityName(_tmpActivityName);
        final String _tmpTicketCount;
        if (_cursor.isNull(_cursorIndexOfTicketCount)) {
          _tmpTicketCount = null;
        } else {
          _tmpTicketCount = _cursor.getString(_cursorIndexOfTicketCount);
        }
        _item.setTicketCount(_tmpTicketCount);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public SpecialActivityReport getSpecialActivityReportByPrimaryKey(final String primaryKey) {
    final String _sql = "select * from special_activity_reports where report_id=?";
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
      final int _cursorIndexOfActivityId = CursorUtil.getColumnIndexOrThrow(_cursor, "activity_id");
      final int _cursorIndexOfDispositionId = CursorUtil.getColumnIndexOrThrow(_cursor, "disposition_id");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_time");
      final int _cursorIndexOfCaseNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "case_number");
      final int _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfPhoto1 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo1");
      final int _cursorIndexOfPhoto2 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo2");
      final int _cursorIndexOfPhoto3 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo3");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfAutoSelect = CursorUtil.getColumnIndexOrThrow(_cursor, "autoSelect");
      final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createDate");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfActivityName = CursorUtil.getColumnIndexOrThrow(_cursor, "activityName");
      final int _cursorIndexOfTicketCount = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_count");
      final SpecialActivityReport _result;
      if(_cursor.moveToFirst()) {
        _result = new SpecialActivityReport();
        final int _tmpReportId;
        _tmpReportId = _cursor.getInt(_cursorIndexOfReportId);
        _result.setReportId(_tmpReportId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final int _tmpActivityId;
        _tmpActivityId = _cursor.getInt(_cursorIndexOfActivityId);
        _result.setActivityId(_tmpActivityId);
        final int _tmpDispositionId;
        _tmpDispositionId = _cursor.getInt(_cursorIndexOfDispositionId);
        _result.setDispositionId(_tmpDispositionId);
        final String _tmpStartDate;
        if (_cursor.isNull(_cursorIndexOfStartDate)) {
          _tmpStartDate = null;
        } else {
          _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
        }
        _result.setStartDate(_tmpStartDate);
        final String _tmpEndDate;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmpEndDate = null;
        } else {
          _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
        }
        _result.setEndDate(_tmpEndDate);
        final String _tmpStartTime;
        if (_cursor.isNull(_cursorIndexOfStartTime)) {
          _tmpStartTime = null;
        } else {
          _tmpStartTime = _cursor.getString(_cursorIndexOfStartTime);
        }
        _result.setStartTime(_tmpStartTime);
        final String _tmpEndTime;
        if (_cursor.isNull(_cursorIndexOfEndTime)) {
          _tmpEndTime = null;
        } else {
          _tmpEndTime = _cursor.getString(_cursorIndexOfEndTime);
        }
        _result.setEndTime(_tmpEndTime);
        final String _tmpCaseNumber;
        if (_cursor.isNull(_cursorIndexOfCaseNumber)) {
          _tmpCaseNumber = null;
        } else {
          _tmpCaseNumber = _cursor.getString(_cursorIndexOfCaseNumber);
        }
        _result.setCaseNumber(_tmpCaseNumber);
        final String _tmpStreetAddress;
        if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
          _tmpStreetAddress = null;
        } else {
          _tmpStreetAddress = _cursor.getString(_cursorIndexOfStreetAddress);
        }
        _result.setStreetAddress(_tmpStreetAddress);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _result.setNotes(_tmpNotes);
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
        final String _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        }
        _result.setLatitude(_tmpLatitude);
        final String _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        }
        _result.setLongitude(_tmpLongitude);
        final String _tmpAutoSelect;
        if (_cursor.isNull(_cursorIndexOfAutoSelect)) {
          _tmpAutoSelect = null;
        } else {
          _tmpAutoSelect = _cursor.getString(_cursorIndexOfAutoSelect);
        }
        _result.setAutoSelect(_tmpAutoSelect);
        final String _tmpCreatedDate;
        if (_cursor.isNull(_cursorIndexOfCreatedDate)) {
          _tmpCreatedDate = null;
        } else {
          _tmpCreatedDate = _cursor.getString(_cursorIndexOfCreatedDate);
        }
        _result.setCreatedDate(_tmpCreatedDate);
        final String _tmpDuration;
        if (_cursor.isNull(_cursorIndexOfDuration)) {
          _tmpDuration = null;
        } else {
          _tmpDuration = _cursor.getString(_cursorIndexOfDuration);
        }
        _result.setDuration(_tmpDuration);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _result.setDeviceId(_tmpDeviceId);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _result.setLocation(_tmpLocation);
        final String _tmpActivityName;
        if (_cursor.isNull(_cursorIndexOfActivityName)) {
          _tmpActivityName = null;
        } else {
          _tmpActivityName = _cursor.getString(_cursorIndexOfActivityName);
        }
        _result.setActivityName(_tmpActivityName);
        final String _tmpTicketCount;
        if (_cursor.isNull(_cursorIndexOfTicketCount)) {
          _tmpTicketCount = null;
        } else {
          _tmpTicketCount = _cursor.getString(_cursorIndexOfTicketCount);
        }
        _result.setTicketCount(_tmpTicketCount);
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
    final String _sql = "select max(report_id) as max_report_id from special_activity_reports";
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
