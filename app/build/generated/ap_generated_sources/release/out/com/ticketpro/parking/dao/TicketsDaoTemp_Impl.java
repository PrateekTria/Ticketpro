package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.TicketTemp;
import com.ticketpro.util.Converters;
import io.reactivex.Completable;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.Void;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TicketsDaoTemp_Impl implements TicketsDaoTemp {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TicketTemp> __insertionAdapterOfTicketTemp;

  private final EntityDeletionOrUpdateAdapter<TicketTemp> __updateAdapterOfTicketTemp;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  public TicketsDaoTemp_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTicketTemp = new EntityInsertionAdapter<TicketTemp>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `tickets_temp` (`ticket_id`,`userid`,`custid`,`device_id`,`citation_number`,`ticket_date`,`state_id`,`state_code`,`plate`,`vin`,`expiration`,`make_id`,`make_code`,`body_id`,`body_code`,`color_id`,`color_code`,`street_prefix`,`street_suffix`,`street_number`,`location`,`direction`,`latitude`,`longitude`,`gpstime`,`is_gps_location`,`is_void`,`is_chalked`,`is_warn`,`is_driveaway`,`void_id`,`chalk_id`,`permit`,`meter`,`fine`,`time_marked`,`space`,`violation_code`,`violation`,`void_reason_code`,`duty_id`,`is_lpr`,`violation_id`,`photo_count`,`lpr_notification_id`,`status`,`placard`,`duty_report_id`,`app_version`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TicketTemp value) {
        stmt.bindLong(1, value.getTicketId());
        stmt.bindLong(2, value.getUserId());
        stmt.bindLong(3, value.getCustId());
        stmt.bindLong(4, value.getDeviceId());
        stmt.bindLong(5, value.getCitationNumber());
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getTicketDate());
        if (_tmp == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, _tmp);
        }
        stmt.bindLong(7, value.getStateId());
        if (value.getStateCode() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getStateCode());
        }
        if (value.getPlate() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getPlate());
        }
        if (value.getVin() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getVin());
        }
        if (value.getExpiration() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getExpiration());
        }
        stmt.bindLong(12, value.getMakeId());
        if (value.getMakeCode() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getMakeCode());
        }
        stmt.bindLong(14, value.getBodyId());
        if (value.getBodyCode() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getBodyCode());
        }
        stmt.bindLong(16, value.getColorId());
        if (value.getColorCode() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindString(17, value.getColorCode());
        }
        if (value.getStreetPrefix() == null) {
          stmt.bindNull(18);
        } else {
          stmt.bindString(18, value.getStreetPrefix());
        }
        if (value.getStreetSuffix() == null) {
          stmt.bindNull(19);
        } else {
          stmt.bindString(19, value.getStreetSuffix());
        }
        if (value.getStreetNumber() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.getStreetNumber());
        }
        if (value.getLocation() == null) {
          stmt.bindNull(21);
        } else {
          stmt.bindString(21, value.getLocation());
        }
        if (value.getDirection() == null) {
          stmt.bindNull(22);
        } else {
          stmt.bindString(22, value.getDirection());
        }
        if (value.getLatitude() == null) {
          stmt.bindNull(23);
        } else {
          stmt.bindString(23, value.getLatitude());
        }
        if (value.getLongitude() == null) {
          stmt.bindNull(24);
        } else {
          stmt.bindString(24, value.getLongitude());
        }
        final Long _tmp_1;
        _tmp_1 = Converters.dateToTimestamp(value.getGpstime());
        if (_tmp_1 == null) {
          stmt.bindNull(25);
        } else {
          stmt.bindLong(25, _tmp_1);
        }
        if (value.getIsGPSLocation() == null) {
          stmt.bindNull(26);
        } else {
          stmt.bindString(26, value.getIsGPSLocation());
        }
        if (value.getIsVoid() == null) {
          stmt.bindNull(27);
        } else {
          stmt.bindString(27, value.getIsVoid());
        }
        if (value.getIsChalked() == null) {
          stmt.bindNull(28);
        } else {
          stmt.bindString(28, value.getIsChalked());
        }
        if (value.getIsWarn() == null) {
          stmt.bindNull(29);
        } else {
          stmt.bindString(29, value.getIsWarn());
        }
        if (value.getIsDriveAway() == null) {
          stmt.bindNull(30);
        } else {
          stmt.bindString(30, value.getIsDriveAway());
        }
        stmt.bindLong(31, value.getVoidId());
        stmt.bindLong(32, value.getChalkId());
        if (value.getPermit() == null) {
          stmt.bindNull(33);
        } else {
          stmt.bindString(33, value.getPermit());
        }
        if (value.getMeter() == null) {
          stmt.bindNull(34);
        } else {
          stmt.bindString(34, value.getMeter());
        }
        stmt.bindDouble(35, value.getFine());
        final Long _tmp_2;
        _tmp_2 = Converters.dateToTimestamp(value.getTimeMarked());
        if (_tmp_2 == null) {
          stmt.bindNull(36);
        } else {
          stmt.bindLong(36, _tmp_2);
        }
        if (value.getSpace() == null) {
          stmt.bindNull(37);
        } else {
          stmt.bindString(37, value.getSpace());
        }
        if (value.getViolationCode() == null) {
          stmt.bindNull(38);
        } else {
          stmt.bindString(38, value.getViolationCode());
        }
        if (value.getViolation() == null) {
          stmt.bindNull(39);
        } else {
          stmt.bindString(39, value.getViolation());
        }
        if (value.getVoidReasonCode() == null) {
          stmt.bindNull(40);
        } else {
          stmt.bindString(40, value.getVoidReasonCode());
        }
        stmt.bindLong(41, value.getDutyId());
        if (value.getIsLPR() == null) {
          stmt.bindNull(42);
        } else {
          stmt.bindString(42, value.getIsLPR());
        }
        stmt.bindLong(43, value.getViolationId());
        stmt.bindLong(44, value.getPhoto_count());
        if (value.getLprNotificationId() == null) {
          stmt.bindNull(45);
        } else {
          stmt.bindString(45, value.getLprNotificationId());
        }
        if (value.getStatus() == null) {
          stmt.bindNull(46);
        } else {
          stmt.bindString(46, value.getStatus());
        }
        if (value.getPlacard() == null) {
          stmt.bindNull(47);
        } else {
          stmt.bindString(47, value.getPlacard());
        }
        if (value.getDutyReportId() == null) {
          stmt.bindNull(48);
        } else {
          stmt.bindString(48, value.getDutyReportId());
        }
        if (value.getAppVersion() == null) {
          stmt.bindNull(49);
        } else {
          stmt.bindString(49, value.getAppVersion());
        }
      }
    };
    this.__updateAdapterOfTicketTemp = new EntityDeletionOrUpdateAdapter<TicketTemp>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `tickets_temp` SET `ticket_id` = ?,`userid` = ?,`custid` = ?,`device_id` = ?,`citation_number` = ?,`ticket_date` = ?,`state_id` = ?,`state_code` = ?,`plate` = ?,`vin` = ?,`expiration` = ?,`make_id` = ?,`make_code` = ?,`body_id` = ?,`body_code` = ?,`color_id` = ?,`color_code` = ?,`street_prefix` = ?,`street_suffix` = ?,`street_number` = ?,`location` = ?,`direction` = ?,`latitude` = ?,`longitude` = ?,`gpstime` = ?,`is_gps_location` = ?,`is_void` = ?,`is_chalked` = ?,`is_warn` = ?,`is_driveaway` = ?,`void_id` = ?,`chalk_id` = ?,`permit` = ?,`meter` = ?,`fine` = ?,`time_marked` = ?,`space` = ?,`violation_code` = ?,`violation` = ?,`void_reason_code` = ?,`duty_id` = ?,`is_lpr` = ?,`violation_id` = ?,`photo_count` = ?,`lpr_notification_id` = ?,`status` = ?,`placard` = ?,`duty_report_id` = ?,`app_version` = ? WHERE `ticket_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TicketTemp value) {
        stmt.bindLong(1, value.getTicketId());
        stmt.bindLong(2, value.getUserId());
        stmt.bindLong(3, value.getCustId());
        stmt.bindLong(4, value.getDeviceId());
        stmt.bindLong(5, value.getCitationNumber());
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getTicketDate());
        if (_tmp == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, _tmp);
        }
        stmt.bindLong(7, value.getStateId());
        if (value.getStateCode() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getStateCode());
        }
        if (value.getPlate() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getPlate());
        }
        if (value.getVin() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getVin());
        }
        if (value.getExpiration() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getExpiration());
        }
        stmt.bindLong(12, value.getMakeId());
        if (value.getMakeCode() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getMakeCode());
        }
        stmt.bindLong(14, value.getBodyId());
        if (value.getBodyCode() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getBodyCode());
        }
        stmt.bindLong(16, value.getColorId());
        if (value.getColorCode() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindString(17, value.getColorCode());
        }
        if (value.getStreetPrefix() == null) {
          stmt.bindNull(18);
        } else {
          stmt.bindString(18, value.getStreetPrefix());
        }
        if (value.getStreetSuffix() == null) {
          stmt.bindNull(19);
        } else {
          stmt.bindString(19, value.getStreetSuffix());
        }
        if (value.getStreetNumber() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.getStreetNumber());
        }
        if (value.getLocation() == null) {
          stmt.bindNull(21);
        } else {
          stmt.bindString(21, value.getLocation());
        }
        if (value.getDirection() == null) {
          stmt.bindNull(22);
        } else {
          stmt.bindString(22, value.getDirection());
        }
        if (value.getLatitude() == null) {
          stmt.bindNull(23);
        } else {
          stmt.bindString(23, value.getLatitude());
        }
        if (value.getLongitude() == null) {
          stmt.bindNull(24);
        } else {
          stmt.bindString(24, value.getLongitude());
        }
        final Long _tmp_1;
        _tmp_1 = Converters.dateToTimestamp(value.getGpstime());
        if (_tmp_1 == null) {
          stmt.bindNull(25);
        } else {
          stmt.bindLong(25, _tmp_1);
        }
        if (value.getIsGPSLocation() == null) {
          stmt.bindNull(26);
        } else {
          stmt.bindString(26, value.getIsGPSLocation());
        }
        if (value.getIsVoid() == null) {
          stmt.bindNull(27);
        } else {
          stmt.bindString(27, value.getIsVoid());
        }
        if (value.getIsChalked() == null) {
          stmt.bindNull(28);
        } else {
          stmt.bindString(28, value.getIsChalked());
        }
        if (value.getIsWarn() == null) {
          stmt.bindNull(29);
        } else {
          stmt.bindString(29, value.getIsWarn());
        }
        if (value.getIsDriveAway() == null) {
          stmt.bindNull(30);
        } else {
          stmt.bindString(30, value.getIsDriveAway());
        }
        stmt.bindLong(31, value.getVoidId());
        stmt.bindLong(32, value.getChalkId());
        if (value.getPermit() == null) {
          stmt.bindNull(33);
        } else {
          stmt.bindString(33, value.getPermit());
        }
        if (value.getMeter() == null) {
          stmt.bindNull(34);
        } else {
          stmt.bindString(34, value.getMeter());
        }
        stmt.bindDouble(35, value.getFine());
        final Long _tmp_2;
        _tmp_2 = Converters.dateToTimestamp(value.getTimeMarked());
        if (_tmp_2 == null) {
          stmt.bindNull(36);
        } else {
          stmt.bindLong(36, _tmp_2);
        }
        if (value.getSpace() == null) {
          stmt.bindNull(37);
        } else {
          stmt.bindString(37, value.getSpace());
        }
        if (value.getViolationCode() == null) {
          stmt.bindNull(38);
        } else {
          stmt.bindString(38, value.getViolationCode());
        }
        if (value.getViolation() == null) {
          stmt.bindNull(39);
        } else {
          stmt.bindString(39, value.getViolation());
        }
        if (value.getVoidReasonCode() == null) {
          stmt.bindNull(40);
        } else {
          stmt.bindString(40, value.getVoidReasonCode());
        }
        stmt.bindLong(41, value.getDutyId());
        if (value.getIsLPR() == null) {
          stmt.bindNull(42);
        } else {
          stmt.bindString(42, value.getIsLPR());
        }
        stmt.bindLong(43, value.getViolationId());
        stmt.bindLong(44, value.getPhoto_count());
        if (value.getLprNotificationId() == null) {
          stmt.bindNull(45);
        } else {
          stmt.bindString(45, value.getLprNotificationId());
        }
        if (value.getStatus() == null) {
          stmt.bindNull(46);
        } else {
          stmt.bindString(46, value.getStatus());
        }
        if (value.getPlacard() == null) {
          stmt.bindNull(47);
        } else {
          stmt.bindString(47, value.getPlacard());
        }
        if (value.getDutyReportId() == null) {
          stmt.bindNull(48);
        } else {
          stmt.bindString(48, value.getDutyReportId());
        }
        if (value.getAppVersion() == null) {
          stmt.bindNull(49);
        } else {
          stmt.bindString(49, value.getAppVersion());
        }
        stmt.bindLong(50, value.getTicketId());
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from tickets_temp";
        return _query;
      }
    };
  }

  @Override
  public void insertTicket(final TicketTemp... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTicketTemp.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertTicket(final TicketTemp data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTicketTemp.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable insertTicketList(final List<TicketTemp> TicketsList) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTicketTemp.insert(TicketsList);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable updateTicket(final TicketTemp ticket) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTicketTemp.handle(ticket);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
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
  public TicketTemp getLastTicket() {
    final String _sql = "select * from tickets_temp";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTicketId = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfCitationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "citation_number");
      final int _cursorIndexOfTicketDate = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date");
      final int _cursorIndexOfStateId = CursorUtil.getColumnIndexOrThrow(_cursor, "state_id");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
      final int _cursorIndexOfExpiration = CursorUtil.getColumnIndexOrThrow(_cursor, "expiration");
      final int _cursorIndexOfMakeId = CursorUtil.getColumnIndexOrThrow(_cursor, "make_id");
      final int _cursorIndexOfMakeCode = CursorUtil.getColumnIndexOrThrow(_cursor, "make_code");
      final int _cursorIndexOfBodyId = CursorUtil.getColumnIndexOrThrow(_cursor, "body_id");
      final int _cursorIndexOfBodyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "body_code");
      final int _cursorIndexOfColorId = CursorUtil.getColumnIndexOrThrow(_cursor, "color_id");
      final int _cursorIndexOfColorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "color_code");
      final int _cursorIndexOfStreetPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_prefix");
      final int _cursorIndexOfStreetSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "street_suffix");
      final int _cursorIndexOfStreetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "street_number");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfGpstime = CursorUtil.getColumnIndexOrThrow(_cursor, "gpstime");
      final int _cursorIndexOfIsGPSLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "is_gps_location");
      final int _cursorIndexOfIsVoid = CursorUtil.getColumnIndexOrThrow(_cursor, "is_void");
      final int _cursorIndexOfIsChalked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_chalked");
      final int _cursorIndexOfIsWarn = CursorUtil.getColumnIndexOrThrow(_cursor, "is_warn");
      final int _cursorIndexOfIsDriveAway = CursorUtil.getColumnIndexOrThrow(_cursor, "is_driveaway");
      final int _cursorIndexOfVoidId = CursorUtil.getColumnIndexOrThrow(_cursor, "void_id");
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
      final int _cursorIndexOfFine = CursorUtil.getColumnIndexOrThrow(_cursor, "fine");
      final int _cursorIndexOfTimeMarked = CursorUtil.getColumnIndexOrThrow(_cursor, "time_marked");
      final int _cursorIndexOfSpace = CursorUtil.getColumnIndexOrThrow(_cursor, "space");
      final int _cursorIndexOfViolationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_code");
      final int _cursorIndexOfViolation = CursorUtil.getColumnIndexOrThrow(_cursor, "violation");
      final int _cursorIndexOfVoidReasonCode = CursorUtil.getColumnIndexOrThrow(_cursor, "void_reason_code");
      final int _cursorIndexOfDutyId = CursorUtil.getColumnIndexOrThrow(_cursor, "duty_id");
      final int _cursorIndexOfIsLPR = CursorUtil.getColumnIndexOrThrow(_cursor, "is_lpr");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfPhotoCount = CursorUtil.getColumnIndexOrThrow(_cursor, "photo_count");
      final int _cursorIndexOfLprNotificationId = CursorUtil.getColumnIndexOrThrow(_cursor, "lpr_notification_id");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfPlacard = CursorUtil.getColumnIndexOrThrow(_cursor, "placard");
      final int _cursorIndexOfDutyReportId = CursorUtil.getColumnIndexOrThrow(_cursor, "duty_report_id");
      final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "app_version");
      final TicketTemp _result;
      if(_cursor.moveToFirst()) {
        _result = new TicketTemp();
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _result.setTicketId(_tmpTicketId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _result.setDeviceId(_tmpDeviceId);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _result.setCitationNumber(_tmpCitationNumber);
        final Date _tmpTicketDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp);
        _result.setTicketDate(_tmpTicketDate);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _result.setStateId(_tmpStateId);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _result.setStateCode(_tmpStateCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _result.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _result.setVin(_tmpVin);
        final String _tmpExpiration;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmpExpiration = null;
        } else {
          _tmpExpiration = _cursor.getString(_cursorIndexOfExpiration);
        }
        _result.setExpiration(_tmpExpiration);
        final int _tmpMakeId;
        _tmpMakeId = _cursor.getInt(_cursorIndexOfMakeId);
        _result.setMakeId(_tmpMakeId);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _result.setMakeCode(_tmpMakeCode);
        final int _tmpBodyId;
        _tmpBodyId = _cursor.getInt(_cursorIndexOfBodyId);
        _result.setBodyId(_tmpBodyId);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _result.setBodyCode(_tmpBodyCode);
        final int _tmpColorId;
        _tmpColorId = _cursor.getInt(_cursorIndexOfColorId);
        _result.setColorId(_tmpColorId);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _result.setColorCode(_tmpColorCode);
        final String _tmpStreetPrefix;
        if (_cursor.isNull(_cursorIndexOfStreetPrefix)) {
          _tmpStreetPrefix = null;
        } else {
          _tmpStreetPrefix = _cursor.getString(_cursorIndexOfStreetPrefix);
        }
        _result.setStreetPrefix(_tmpStreetPrefix);
        final String _tmpStreetSuffix;
        if (_cursor.isNull(_cursorIndexOfStreetSuffix)) {
          _tmpStreetSuffix = null;
        } else {
          _tmpStreetSuffix = _cursor.getString(_cursorIndexOfStreetSuffix);
        }
        _result.setStreetSuffix(_tmpStreetSuffix);
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _result.setStreetNumber(_tmpStreetNumber);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _result.setLocation(_tmpLocation);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _result.setDirection(_tmpDirection);
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
        final Date _tmpGpstime;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_1);
        _result.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _result.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsVoid;
        if (_cursor.isNull(_cursorIndexOfIsVoid)) {
          _tmpIsVoid = null;
        } else {
          _tmpIsVoid = _cursor.getString(_cursorIndexOfIsVoid);
        }
        _result.setIsVoid(_tmpIsVoid);
        final String _tmpIsChalked;
        if (_cursor.isNull(_cursorIndexOfIsChalked)) {
          _tmpIsChalked = null;
        } else {
          _tmpIsChalked = _cursor.getString(_cursorIndexOfIsChalked);
        }
        _result.setIsChalked(_tmpIsChalked);
        final String _tmpIsWarn;
        if (_cursor.isNull(_cursorIndexOfIsWarn)) {
          _tmpIsWarn = null;
        } else {
          _tmpIsWarn = _cursor.getString(_cursorIndexOfIsWarn);
        }
        _result.setIsWarn(_tmpIsWarn);
        final String _tmpIsDriveAway;
        if (_cursor.isNull(_cursorIndexOfIsDriveAway)) {
          _tmpIsDriveAway = null;
        } else {
          _tmpIsDriveAway = _cursor.getString(_cursorIndexOfIsDriveAway);
        }
        _result.setIsDriveAway(_tmpIsDriveAway);
        final int _tmpVoidId;
        _tmpVoidId = _cursor.getInt(_cursorIndexOfVoidId);
        _result.setVoidId(_tmpVoidId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _result.setChalkId(_tmpChalkId);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _result.setPermit(_tmpPermit);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _result.setMeter(_tmpMeter);
        final double _tmpFine;
        _tmpFine = _cursor.getDouble(_cursorIndexOfFine);
        _result.setFine(_tmpFine);
        final Date _tmpTimeMarked;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfTimeMarked)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfTimeMarked);
        }
        _tmpTimeMarked = Converters.fromTimestamp(_tmp_2);
        _result.setTimeMarked(_tmpTimeMarked);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _result.setSpace(_tmpSpace);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _result.setViolationCode(_tmpViolationCode);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _result.setViolation(_tmpViolation);
        final String _tmpVoidReasonCode;
        if (_cursor.isNull(_cursorIndexOfVoidReasonCode)) {
          _tmpVoidReasonCode = null;
        } else {
          _tmpVoidReasonCode = _cursor.getString(_cursorIndexOfVoidReasonCode);
        }
        _result.setVoidReasonCode(_tmpVoidReasonCode);
        final int _tmpDutyId;
        _tmpDutyId = _cursor.getInt(_cursorIndexOfDutyId);
        _result.setDutyId(_tmpDutyId);
        final String _tmpIsLPR;
        if (_cursor.isNull(_cursorIndexOfIsLPR)) {
          _tmpIsLPR = null;
        } else {
          _tmpIsLPR = _cursor.getString(_cursorIndexOfIsLPR);
        }
        _result.setIsLPR(_tmpIsLPR);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _result.setViolationId(_tmpViolationId);
        final int _tmpPhoto_count;
        _tmpPhoto_count = _cursor.getInt(_cursorIndexOfPhotoCount);
        _result.setPhoto_count(_tmpPhoto_count);
        final String _tmpLprNotificationId;
        if (_cursor.isNull(_cursorIndexOfLprNotificationId)) {
          _tmpLprNotificationId = null;
        } else {
          _tmpLprNotificationId = _cursor.getString(_cursorIndexOfLprNotificationId);
        }
        _result.setLprNotificationId(_tmpLprNotificationId);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _result.setStatus(_tmpStatus);
        final String _tmpPlacard;
        if (_cursor.isNull(_cursorIndexOfPlacard)) {
          _tmpPlacard = null;
        } else {
          _tmpPlacard = _cursor.getString(_cursorIndexOfPlacard);
        }
        _result.setPlacard(_tmpPlacard);
        final String _tmpDutyReportId;
        if (_cursor.isNull(_cursorIndexOfDutyReportId)) {
          _tmpDutyReportId = null;
        } else {
          _tmpDutyReportId = _cursor.getString(_cursorIndexOfDutyReportId);
        }
        _result.setDutyReportId(_tmpDutyReportId);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _result.setAppVersion(_tmpAppVersion);
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
  public int getCount() {
    final String _sql = "SELECT COUNT(ticket_id) FROM tickets_temp";
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
