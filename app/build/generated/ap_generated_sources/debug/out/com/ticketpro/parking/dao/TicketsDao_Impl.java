package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.Ticket;
import com.ticketpro.util.Converters;
import io.reactivex.Completable;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class TicketsDao_Impl implements TicketsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Ticket> __insertionAdapterOfTicket;

  private final EntityDeletionOrUpdateAdapter<Ticket> __updateAdapterOfTicket;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTicket;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTicketDriveway;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTicketWarning;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTicketVoid;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTicketPhotoCount;

  public TicketsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTicket = new EntityInsertionAdapter<Ticket>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `tickets` (`ticket_id`,`userid`,`custid`,`device_id`,`citation_number`,`ticket_date`,`state_id`,`state_code`,`plate`,`vin`,`expiration`,`make_id`,`make_code`,`body_id`,`body_code`,`color_id`,`color_code`,`street_prefix`,`street_suffix`,`street_number`,`location`,`direction`,`latitude`,`longitude`,`gpstime`,`is_gps_location`,`is_void`,`is_chalked`,`is_warn`,`is_driveaway`,`void_id`,`chalk_id`,`permit`,`meter`,`fine`,`time_marked`,`space`,`violation_code`,`violation`,`void_reason_code`,`duty_id`,`is_lpr`,`violation_id`,`photo_count`,`lpr_notification_id`,`status`,`placard`,`duty_report_id`,`app_version`,`chalk_time`,`chalk_last_seen`,`ticket_date_new`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Ticket value) {
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
        if (value.getChalkTime() == null) {
          stmt.bindNull(50);
        } else {
          stmt.bindString(50, value.getChalkTime());
        }
        if (value.getChalkLastSeen() == null) {
          stmt.bindNull(51);
        } else {
          stmt.bindString(51, value.getChalkLastSeen());
        }
        if (value.getTicketDateNew() == null) {
          stmt.bindNull(52);
        } else {
          stmt.bindString(52, value.getTicketDateNew());
        }
      }
    };
    this.__updateAdapterOfTicket = new EntityDeletionOrUpdateAdapter<Ticket>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `tickets` SET `ticket_id` = ?,`userid` = ?,`custid` = ?,`device_id` = ?,`citation_number` = ?,`ticket_date` = ?,`state_id` = ?,`state_code` = ?,`plate` = ?,`vin` = ?,`expiration` = ?,`make_id` = ?,`make_code` = ?,`body_id` = ?,`body_code` = ?,`color_id` = ?,`color_code` = ?,`street_prefix` = ?,`street_suffix` = ?,`street_number` = ?,`location` = ?,`direction` = ?,`latitude` = ?,`longitude` = ?,`gpstime` = ?,`is_gps_location` = ?,`is_void` = ?,`is_chalked` = ?,`is_warn` = ?,`is_driveaway` = ?,`void_id` = ?,`chalk_id` = ?,`permit` = ?,`meter` = ?,`fine` = ?,`time_marked` = ?,`space` = ?,`violation_code` = ?,`violation` = ?,`void_reason_code` = ?,`duty_id` = ?,`is_lpr` = ?,`violation_id` = ?,`photo_count` = ?,`lpr_notification_id` = ?,`status` = ?,`placard` = ?,`duty_report_id` = ?,`app_version` = ?,`chalk_time` = ?,`chalk_last_seen` = ?,`ticket_date_new` = ? WHERE `ticket_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Ticket value) {
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
        if (value.getChalkTime() == null) {
          stmt.bindNull(50);
        } else {
          stmt.bindString(50, value.getChalkTime());
        }
        if (value.getChalkLastSeen() == null) {
          stmt.bindNull(51);
        } else {
          stmt.bindString(51, value.getChalkLastSeen());
        }
        if (value.getTicketDateNew() == null) {
          stmt.bindNull(52);
        } else {
          stmt.bindString(52, value.getTicketDateNew());
        }
        stmt.bindLong(53, value.getTicketId());
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from tickets where ticket_id=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTicket = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update tickets set status=? where citation_number=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTicketDriveway = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update tickets set is_driveaway=? where citation_number=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTicketWarning = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update tickets set is_warn=? where citation_number=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTicketVoid = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update tickets set is_void=? where citation_number=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTicketPhotoCount = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update tickets set photo_count=? where citation_number=?";
        return _query;
      }
    };
  }

  @Override
  public void insertTicket(final Ticket... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTicket.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertTicket(final Ticket data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTicket.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable insertTicketList(final List<Ticket> TicketsList) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTicket.insert(TicketsList);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable updateTicket(final Ticket ticket) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTicket.handle(ticket);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void removeById(final long ticketId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, ticketId);
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
  public void updateTicket(final String citation, final String values) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTicket.acquire();
    int _argIndex = 1;
    if (values == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, values);
    }
    _argIndex = 2;
    if (citation == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, citation);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateTicket.release(_stmt);
    }
  }

  @Override
  public void updateTicketDriveway(final String citation, final String values) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTicketDriveway.acquire();
    int _argIndex = 1;
    if (values == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, values);
    }
    _argIndex = 2;
    if (citation == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, citation);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateTicketDriveway.release(_stmt);
    }
  }

  @Override
  public void updateTicketWarning(final String citation, final String values) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTicketWarning.acquire();
    int _argIndex = 1;
    if (values == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, values);
    }
    _argIndex = 2;
    if (citation == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, citation);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateTicketWarning.release(_stmt);
    }
  }

  @Override
  public void updateTicketVoid(final String citation, final String values) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTicketVoid.acquire();
    int _argIndex = 1;
    if (values == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, values);
    }
    _argIndex = 2;
    if (citation == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, citation);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateTicketVoid.release(_stmt);
    }
  }

  @Override
  public void updateTicketPhotoCount(final String citation, final int values) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTicketPhotoCount.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, values);
    _argIndex = 2;
    if (citation == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, citation);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateTicketPhotoCount.release(_stmt);
    }
  }

  @Override
  public long getNextPrimaryId() {
    final String _sql = "select max(ticket_id) as max_ticket_id from tickets";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final long _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getLong(0);
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
  public Ticket getLastTicket(final int userId) {
    final String _sql = "select * from tickets where userid=? and ticket_id=(select max(ticket_id) from tickets)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final Ticket _result;
      if(_cursor.moveToFirst()) {
        _result = new Ticket();
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
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _result.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _result.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _result.setTicketDateNew(_tmpTicketDateNew);
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
  public List<Ticket> getAllTickets() {
    final String _sql = "select * from tickets order by ticket_id DESC";
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final List<Ticket> _result = new ArrayList<Ticket>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Ticket _item;
        _item = new Ticket();
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _item.setCitationNumber(_tmpCitationNumber);
        final Date _tmpTicketDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp);
        _item.setTicketDate(_tmpTicketDate);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _item.setStateId(_tmpStateId);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item.setStateCode(_tmpStateCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final String _tmpExpiration;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmpExpiration = null;
        } else {
          _tmpExpiration = _cursor.getString(_cursorIndexOfExpiration);
        }
        _item.setExpiration(_tmpExpiration);
        final int _tmpMakeId;
        _tmpMakeId = _cursor.getInt(_cursorIndexOfMakeId);
        _item.setMakeId(_tmpMakeId);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final int _tmpBodyId;
        _tmpBodyId = _cursor.getInt(_cursorIndexOfBodyId);
        _item.setBodyId(_tmpBodyId);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _item.setBodyCode(_tmpBodyCode);
        final int _tmpColorId;
        _tmpColorId = _cursor.getInt(_cursorIndexOfColorId);
        _item.setColorId(_tmpColorId);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpStreetPrefix;
        if (_cursor.isNull(_cursorIndexOfStreetPrefix)) {
          _tmpStreetPrefix = null;
        } else {
          _tmpStreetPrefix = _cursor.getString(_cursorIndexOfStreetPrefix);
        }
        _item.setStreetPrefix(_tmpStreetPrefix);
        final String _tmpStreetSuffix;
        if (_cursor.isNull(_cursorIndexOfStreetSuffix)) {
          _tmpStreetSuffix = null;
        } else {
          _tmpStreetSuffix = _cursor.getString(_cursorIndexOfStreetSuffix);
        }
        _item.setStreetSuffix(_tmpStreetSuffix);
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item.setStreetNumber(_tmpStreetNumber);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item.setDirection(_tmpDirection);
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
        final Date _tmpGpstime;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_1);
        _item.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _item.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsVoid;
        if (_cursor.isNull(_cursorIndexOfIsVoid)) {
          _tmpIsVoid = null;
        } else {
          _tmpIsVoid = _cursor.getString(_cursorIndexOfIsVoid);
        }
        _item.setIsVoid(_tmpIsVoid);
        final String _tmpIsChalked;
        if (_cursor.isNull(_cursorIndexOfIsChalked)) {
          _tmpIsChalked = null;
        } else {
          _tmpIsChalked = _cursor.getString(_cursorIndexOfIsChalked);
        }
        _item.setIsChalked(_tmpIsChalked);
        final String _tmpIsWarn;
        if (_cursor.isNull(_cursorIndexOfIsWarn)) {
          _tmpIsWarn = null;
        } else {
          _tmpIsWarn = _cursor.getString(_cursorIndexOfIsWarn);
        }
        _item.setIsWarn(_tmpIsWarn);
        final String _tmpIsDriveAway;
        if (_cursor.isNull(_cursorIndexOfIsDriveAway)) {
          _tmpIsDriveAway = null;
        } else {
          _tmpIsDriveAway = _cursor.getString(_cursorIndexOfIsDriveAway);
        }
        _item.setIsDriveAway(_tmpIsDriveAway);
        final int _tmpVoidId;
        _tmpVoidId = _cursor.getInt(_cursorIndexOfVoidId);
        _item.setVoidId(_tmpVoidId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final double _tmpFine;
        _tmpFine = _cursor.getDouble(_cursorIndexOfFine);
        _item.setFine(_tmpFine);
        final Date _tmpTimeMarked;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfTimeMarked)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfTimeMarked);
        }
        _tmpTimeMarked = Converters.fromTimestamp(_tmp_2);
        _item.setTimeMarked(_tmpTimeMarked);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _item.setSpace(_tmpSpace);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item.setViolation(_tmpViolation);
        final String _tmpVoidReasonCode;
        if (_cursor.isNull(_cursorIndexOfVoidReasonCode)) {
          _tmpVoidReasonCode = null;
        } else {
          _tmpVoidReasonCode = _cursor.getString(_cursorIndexOfVoidReasonCode);
        }
        _item.setVoidReasonCode(_tmpVoidReasonCode);
        final int _tmpDutyId;
        _tmpDutyId = _cursor.getInt(_cursorIndexOfDutyId);
        _item.setDutyId(_tmpDutyId);
        final String _tmpIsLPR;
        if (_cursor.isNull(_cursorIndexOfIsLPR)) {
          _tmpIsLPR = null;
        } else {
          _tmpIsLPR = _cursor.getString(_cursorIndexOfIsLPR);
        }
        _item.setIsLPR(_tmpIsLPR);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final int _tmpPhoto_count;
        _tmpPhoto_count = _cursor.getInt(_cursorIndexOfPhotoCount);
        _item.setPhoto_count(_tmpPhoto_count);
        final String _tmpLprNotificationId;
        if (_cursor.isNull(_cursorIndexOfLprNotificationId)) {
          _tmpLprNotificationId = null;
        } else {
          _tmpLprNotificationId = _cursor.getString(_cursorIndexOfLprNotificationId);
        }
        _item.setLprNotificationId(_tmpLprNotificationId);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpPlacard;
        if (_cursor.isNull(_cursorIndexOfPlacard)) {
          _tmpPlacard = null;
        } else {
          _tmpPlacard = _cursor.getString(_cursorIndexOfPlacard);
        }
        _item.setPlacard(_tmpPlacard);
        final String _tmpDutyReportId;
        if (_cursor.isNull(_cursorIndexOfDutyReportId)) {
          _tmpDutyReportId = null;
        } else {
          _tmpDutyReportId = _cursor.getString(_cursorIndexOfDutyReportId);
        }
        _item.setDutyReportId(_tmpDutyReportId);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setAppVersion(_tmpAppVersion);
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _item.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _item.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _item.setTicketDateNew(_tmpTicketDateNew);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Ticket> getAllTicketsByUSerID(final int userId) {
    final String _sql = "select * from tickets where userid=? order by ticket_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final List<Ticket> _result = new ArrayList<Ticket>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Ticket _item;
        _item = new Ticket();
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _item.setCitationNumber(_tmpCitationNumber);
        final Date _tmpTicketDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp);
        _item.setTicketDate(_tmpTicketDate);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _item.setStateId(_tmpStateId);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item.setStateCode(_tmpStateCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final String _tmpExpiration;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmpExpiration = null;
        } else {
          _tmpExpiration = _cursor.getString(_cursorIndexOfExpiration);
        }
        _item.setExpiration(_tmpExpiration);
        final int _tmpMakeId;
        _tmpMakeId = _cursor.getInt(_cursorIndexOfMakeId);
        _item.setMakeId(_tmpMakeId);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final int _tmpBodyId;
        _tmpBodyId = _cursor.getInt(_cursorIndexOfBodyId);
        _item.setBodyId(_tmpBodyId);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _item.setBodyCode(_tmpBodyCode);
        final int _tmpColorId;
        _tmpColorId = _cursor.getInt(_cursorIndexOfColorId);
        _item.setColorId(_tmpColorId);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpStreetPrefix;
        if (_cursor.isNull(_cursorIndexOfStreetPrefix)) {
          _tmpStreetPrefix = null;
        } else {
          _tmpStreetPrefix = _cursor.getString(_cursorIndexOfStreetPrefix);
        }
        _item.setStreetPrefix(_tmpStreetPrefix);
        final String _tmpStreetSuffix;
        if (_cursor.isNull(_cursorIndexOfStreetSuffix)) {
          _tmpStreetSuffix = null;
        } else {
          _tmpStreetSuffix = _cursor.getString(_cursorIndexOfStreetSuffix);
        }
        _item.setStreetSuffix(_tmpStreetSuffix);
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item.setStreetNumber(_tmpStreetNumber);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item.setDirection(_tmpDirection);
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
        final Date _tmpGpstime;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_1);
        _item.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _item.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsVoid;
        if (_cursor.isNull(_cursorIndexOfIsVoid)) {
          _tmpIsVoid = null;
        } else {
          _tmpIsVoid = _cursor.getString(_cursorIndexOfIsVoid);
        }
        _item.setIsVoid(_tmpIsVoid);
        final String _tmpIsChalked;
        if (_cursor.isNull(_cursorIndexOfIsChalked)) {
          _tmpIsChalked = null;
        } else {
          _tmpIsChalked = _cursor.getString(_cursorIndexOfIsChalked);
        }
        _item.setIsChalked(_tmpIsChalked);
        final String _tmpIsWarn;
        if (_cursor.isNull(_cursorIndexOfIsWarn)) {
          _tmpIsWarn = null;
        } else {
          _tmpIsWarn = _cursor.getString(_cursorIndexOfIsWarn);
        }
        _item.setIsWarn(_tmpIsWarn);
        final String _tmpIsDriveAway;
        if (_cursor.isNull(_cursorIndexOfIsDriveAway)) {
          _tmpIsDriveAway = null;
        } else {
          _tmpIsDriveAway = _cursor.getString(_cursorIndexOfIsDriveAway);
        }
        _item.setIsDriveAway(_tmpIsDriveAway);
        final int _tmpVoidId;
        _tmpVoidId = _cursor.getInt(_cursorIndexOfVoidId);
        _item.setVoidId(_tmpVoidId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final double _tmpFine;
        _tmpFine = _cursor.getDouble(_cursorIndexOfFine);
        _item.setFine(_tmpFine);
        final Date _tmpTimeMarked;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfTimeMarked)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfTimeMarked);
        }
        _tmpTimeMarked = Converters.fromTimestamp(_tmp_2);
        _item.setTimeMarked(_tmpTimeMarked);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _item.setSpace(_tmpSpace);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item.setViolation(_tmpViolation);
        final String _tmpVoidReasonCode;
        if (_cursor.isNull(_cursorIndexOfVoidReasonCode)) {
          _tmpVoidReasonCode = null;
        } else {
          _tmpVoidReasonCode = _cursor.getString(_cursorIndexOfVoidReasonCode);
        }
        _item.setVoidReasonCode(_tmpVoidReasonCode);
        final int _tmpDutyId;
        _tmpDutyId = _cursor.getInt(_cursorIndexOfDutyId);
        _item.setDutyId(_tmpDutyId);
        final String _tmpIsLPR;
        if (_cursor.isNull(_cursorIndexOfIsLPR)) {
          _tmpIsLPR = null;
        } else {
          _tmpIsLPR = _cursor.getString(_cursorIndexOfIsLPR);
        }
        _item.setIsLPR(_tmpIsLPR);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final int _tmpPhoto_count;
        _tmpPhoto_count = _cursor.getInt(_cursorIndexOfPhotoCount);
        _item.setPhoto_count(_tmpPhoto_count);
        final String _tmpLprNotificationId;
        if (_cursor.isNull(_cursorIndexOfLprNotificationId)) {
          _tmpLprNotificationId = null;
        } else {
          _tmpLprNotificationId = _cursor.getString(_cursorIndexOfLprNotificationId);
        }
        _item.setLprNotificationId(_tmpLprNotificationId);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpPlacard;
        if (_cursor.isNull(_cursorIndexOfPlacard)) {
          _tmpPlacard = null;
        } else {
          _tmpPlacard = _cursor.getString(_cursorIndexOfPlacard);
        }
        _item.setPlacard(_tmpPlacard);
        final String _tmpDutyReportId;
        if (_cursor.isNull(_cursorIndexOfDutyReportId)) {
          _tmpDutyReportId = null;
        } else {
          _tmpDutyReportId = _cursor.getString(_cursorIndexOfDutyReportId);
        }
        _item.setDutyReportId(_tmpDutyReportId);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setAppVersion(_tmpAppVersion);
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _item.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _item.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _item.setTicketDateNew(_tmpTicketDateNew);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Ticket> getTicketsBySpace(final String spaceNumber) {
    final String _sql = "select * from tickets where space=? order by ticket_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (spaceNumber == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, spaceNumber);
    }
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final List<Ticket> _result = new ArrayList<Ticket>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Ticket _item;
        _item = new Ticket();
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _item.setCitationNumber(_tmpCitationNumber);
        final Date _tmpTicketDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp);
        _item.setTicketDate(_tmpTicketDate);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _item.setStateId(_tmpStateId);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item.setStateCode(_tmpStateCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final String _tmpExpiration;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmpExpiration = null;
        } else {
          _tmpExpiration = _cursor.getString(_cursorIndexOfExpiration);
        }
        _item.setExpiration(_tmpExpiration);
        final int _tmpMakeId;
        _tmpMakeId = _cursor.getInt(_cursorIndexOfMakeId);
        _item.setMakeId(_tmpMakeId);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final int _tmpBodyId;
        _tmpBodyId = _cursor.getInt(_cursorIndexOfBodyId);
        _item.setBodyId(_tmpBodyId);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _item.setBodyCode(_tmpBodyCode);
        final int _tmpColorId;
        _tmpColorId = _cursor.getInt(_cursorIndexOfColorId);
        _item.setColorId(_tmpColorId);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpStreetPrefix;
        if (_cursor.isNull(_cursorIndexOfStreetPrefix)) {
          _tmpStreetPrefix = null;
        } else {
          _tmpStreetPrefix = _cursor.getString(_cursorIndexOfStreetPrefix);
        }
        _item.setStreetPrefix(_tmpStreetPrefix);
        final String _tmpStreetSuffix;
        if (_cursor.isNull(_cursorIndexOfStreetSuffix)) {
          _tmpStreetSuffix = null;
        } else {
          _tmpStreetSuffix = _cursor.getString(_cursorIndexOfStreetSuffix);
        }
        _item.setStreetSuffix(_tmpStreetSuffix);
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item.setStreetNumber(_tmpStreetNumber);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item.setDirection(_tmpDirection);
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
        final Date _tmpGpstime;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_1);
        _item.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _item.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsVoid;
        if (_cursor.isNull(_cursorIndexOfIsVoid)) {
          _tmpIsVoid = null;
        } else {
          _tmpIsVoid = _cursor.getString(_cursorIndexOfIsVoid);
        }
        _item.setIsVoid(_tmpIsVoid);
        final String _tmpIsChalked;
        if (_cursor.isNull(_cursorIndexOfIsChalked)) {
          _tmpIsChalked = null;
        } else {
          _tmpIsChalked = _cursor.getString(_cursorIndexOfIsChalked);
        }
        _item.setIsChalked(_tmpIsChalked);
        final String _tmpIsWarn;
        if (_cursor.isNull(_cursorIndexOfIsWarn)) {
          _tmpIsWarn = null;
        } else {
          _tmpIsWarn = _cursor.getString(_cursorIndexOfIsWarn);
        }
        _item.setIsWarn(_tmpIsWarn);
        final String _tmpIsDriveAway;
        if (_cursor.isNull(_cursorIndexOfIsDriveAway)) {
          _tmpIsDriveAway = null;
        } else {
          _tmpIsDriveAway = _cursor.getString(_cursorIndexOfIsDriveAway);
        }
        _item.setIsDriveAway(_tmpIsDriveAway);
        final int _tmpVoidId;
        _tmpVoidId = _cursor.getInt(_cursorIndexOfVoidId);
        _item.setVoidId(_tmpVoidId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final double _tmpFine;
        _tmpFine = _cursor.getDouble(_cursorIndexOfFine);
        _item.setFine(_tmpFine);
        final Date _tmpTimeMarked;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfTimeMarked)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfTimeMarked);
        }
        _tmpTimeMarked = Converters.fromTimestamp(_tmp_2);
        _item.setTimeMarked(_tmpTimeMarked);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _item.setSpace(_tmpSpace);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item.setViolation(_tmpViolation);
        final String _tmpVoidReasonCode;
        if (_cursor.isNull(_cursorIndexOfVoidReasonCode)) {
          _tmpVoidReasonCode = null;
        } else {
          _tmpVoidReasonCode = _cursor.getString(_cursorIndexOfVoidReasonCode);
        }
        _item.setVoidReasonCode(_tmpVoidReasonCode);
        final int _tmpDutyId;
        _tmpDutyId = _cursor.getInt(_cursorIndexOfDutyId);
        _item.setDutyId(_tmpDutyId);
        final String _tmpIsLPR;
        if (_cursor.isNull(_cursorIndexOfIsLPR)) {
          _tmpIsLPR = null;
        } else {
          _tmpIsLPR = _cursor.getString(_cursorIndexOfIsLPR);
        }
        _item.setIsLPR(_tmpIsLPR);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final int _tmpPhoto_count;
        _tmpPhoto_count = _cursor.getInt(_cursorIndexOfPhotoCount);
        _item.setPhoto_count(_tmpPhoto_count);
        final String _tmpLprNotificationId;
        if (_cursor.isNull(_cursorIndexOfLprNotificationId)) {
          _tmpLprNotificationId = null;
        } else {
          _tmpLprNotificationId = _cursor.getString(_cursorIndexOfLprNotificationId);
        }
        _item.setLprNotificationId(_tmpLprNotificationId);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpPlacard;
        if (_cursor.isNull(_cursorIndexOfPlacard)) {
          _tmpPlacard = null;
        } else {
          _tmpPlacard = _cursor.getString(_cursorIndexOfPlacard);
        }
        _item.setPlacard(_tmpPlacard);
        final String _tmpDutyReportId;
        if (_cursor.isNull(_cursorIndexOfDutyReportId)) {
          _tmpDutyReportId = null;
        } else {
          _tmpDutyReportId = _cursor.getString(_cursorIndexOfDutyReportId);
        }
        _item.setDutyReportId(_tmpDutyReportId);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setAppVersion(_tmpAppVersion);
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _item.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _item.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _item.setTicketDateNew(_tmpTicketDateNew);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Ticket getTicketsByPrimaryId(final String ticketId) {
    final String _sql = "select * from tickets where ticket_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (ticketId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, ticketId);
    }
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final Ticket _result;
      if(_cursor.moveToFirst()) {
        _result = new Ticket();
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
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _result.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _result.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _result.setTicketDateNew(_tmpTicketDateNew);
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
  public Ticket getTicketWithViolations(final long ticketId) {
    final String _sql = "select * from tickets where ticket_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, ticketId);
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final Ticket _result;
      if(_cursor.moveToFirst()) {
        _result = new Ticket();
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
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _result.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _result.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _result.setTicketDateNew(_tmpTicketDateNew);
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
  public Ticket getTicketByCitationWithViolations(final long citationNumber) {
    final String _sql = "select * from tickets where citation_number=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, citationNumber);
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final Ticket _result;
      if(_cursor.moveToFirst()) {
        _result = new Ticket();
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
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _result.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _result.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _result.setTicketDateNew(_tmpTicketDateNew);
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
  public List<Ticket> searchAllPreviousTicketByPlate(final String state, final String plate) {
    final String _sql = "select * from tickets where plate=? and state_code=? order by ticket_id DESC";
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final List<Ticket> _result = new ArrayList<Ticket>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Ticket _item;
        _item = new Ticket();
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _item.setCitationNumber(_tmpCitationNumber);
        final Date _tmpTicketDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp);
        _item.setTicketDate(_tmpTicketDate);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _item.setStateId(_tmpStateId);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item.setStateCode(_tmpStateCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final String _tmpExpiration;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmpExpiration = null;
        } else {
          _tmpExpiration = _cursor.getString(_cursorIndexOfExpiration);
        }
        _item.setExpiration(_tmpExpiration);
        final int _tmpMakeId;
        _tmpMakeId = _cursor.getInt(_cursorIndexOfMakeId);
        _item.setMakeId(_tmpMakeId);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final int _tmpBodyId;
        _tmpBodyId = _cursor.getInt(_cursorIndexOfBodyId);
        _item.setBodyId(_tmpBodyId);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _item.setBodyCode(_tmpBodyCode);
        final int _tmpColorId;
        _tmpColorId = _cursor.getInt(_cursorIndexOfColorId);
        _item.setColorId(_tmpColorId);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpStreetPrefix;
        if (_cursor.isNull(_cursorIndexOfStreetPrefix)) {
          _tmpStreetPrefix = null;
        } else {
          _tmpStreetPrefix = _cursor.getString(_cursorIndexOfStreetPrefix);
        }
        _item.setStreetPrefix(_tmpStreetPrefix);
        final String _tmpStreetSuffix;
        if (_cursor.isNull(_cursorIndexOfStreetSuffix)) {
          _tmpStreetSuffix = null;
        } else {
          _tmpStreetSuffix = _cursor.getString(_cursorIndexOfStreetSuffix);
        }
        _item.setStreetSuffix(_tmpStreetSuffix);
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item.setStreetNumber(_tmpStreetNumber);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item.setDirection(_tmpDirection);
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
        final Date _tmpGpstime;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_1);
        _item.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _item.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsVoid;
        if (_cursor.isNull(_cursorIndexOfIsVoid)) {
          _tmpIsVoid = null;
        } else {
          _tmpIsVoid = _cursor.getString(_cursorIndexOfIsVoid);
        }
        _item.setIsVoid(_tmpIsVoid);
        final String _tmpIsChalked;
        if (_cursor.isNull(_cursorIndexOfIsChalked)) {
          _tmpIsChalked = null;
        } else {
          _tmpIsChalked = _cursor.getString(_cursorIndexOfIsChalked);
        }
        _item.setIsChalked(_tmpIsChalked);
        final String _tmpIsWarn;
        if (_cursor.isNull(_cursorIndexOfIsWarn)) {
          _tmpIsWarn = null;
        } else {
          _tmpIsWarn = _cursor.getString(_cursorIndexOfIsWarn);
        }
        _item.setIsWarn(_tmpIsWarn);
        final String _tmpIsDriveAway;
        if (_cursor.isNull(_cursorIndexOfIsDriveAway)) {
          _tmpIsDriveAway = null;
        } else {
          _tmpIsDriveAway = _cursor.getString(_cursorIndexOfIsDriveAway);
        }
        _item.setIsDriveAway(_tmpIsDriveAway);
        final int _tmpVoidId;
        _tmpVoidId = _cursor.getInt(_cursorIndexOfVoidId);
        _item.setVoidId(_tmpVoidId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final double _tmpFine;
        _tmpFine = _cursor.getDouble(_cursorIndexOfFine);
        _item.setFine(_tmpFine);
        final Date _tmpTimeMarked;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfTimeMarked)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfTimeMarked);
        }
        _tmpTimeMarked = Converters.fromTimestamp(_tmp_2);
        _item.setTimeMarked(_tmpTimeMarked);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _item.setSpace(_tmpSpace);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item.setViolation(_tmpViolation);
        final String _tmpVoidReasonCode;
        if (_cursor.isNull(_cursorIndexOfVoidReasonCode)) {
          _tmpVoidReasonCode = null;
        } else {
          _tmpVoidReasonCode = _cursor.getString(_cursorIndexOfVoidReasonCode);
        }
        _item.setVoidReasonCode(_tmpVoidReasonCode);
        final int _tmpDutyId;
        _tmpDutyId = _cursor.getInt(_cursorIndexOfDutyId);
        _item.setDutyId(_tmpDutyId);
        final String _tmpIsLPR;
        if (_cursor.isNull(_cursorIndexOfIsLPR)) {
          _tmpIsLPR = null;
        } else {
          _tmpIsLPR = _cursor.getString(_cursorIndexOfIsLPR);
        }
        _item.setIsLPR(_tmpIsLPR);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final int _tmpPhoto_count;
        _tmpPhoto_count = _cursor.getInt(_cursorIndexOfPhotoCount);
        _item.setPhoto_count(_tmpPhoto_count);
        final String _tmpLprNotificationId;
        if (_cursor.isNull(_cursorIndexOfLprNotificationId)) {
          _tmpLprNotificationId = null;
        } else {
          _tmpLprNotificationId = _cursor.getString(_cursorIndexOfLprNotificationId);
        }
        _item.setLprNotificationId(_tmpLprNotificationId);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpPlacard;
        if (_cursor.isNull(_cursorIndexOfPlacard)) {
          _tmpPlacard = null;
        } else {
          _tmpPlacard = _cursor.getString(_cursorIndexOfPlacard);
        }
        _item.setPlacard(_tmpPlacard);
        final String _tmpDutyReportId;
        if (_cursor.isNull(_cursorIndexOfDutyReportId)) {
          _tmpDutyReportId = null;
        } else {
          _tmpDutyReportId = _cursor.getString(_cursorIndexOfDutyReportId);
        }
        _item.setDutyReportId(_tmpDutyReportId);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setAppVersion(_tmpAppVersion);
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _item.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _item.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _item.setTicketDateNew(_tmpTicketDateNew);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Ticket searchPreviousTicketByPlate(final String state, final String plate) {
    final String _sql = "select * from tickets where plate=? and state_code=? order by ticket_id DESC";
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final Ticket _result;
      if(_cursor.moveToFirst()) {
        _result = new Ticket();
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
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _result.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _result.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _result.setTicketDateNew(_tmpTicketDateNew);
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
  public Ticket searchPreviousTicketByVIN(final String state, final String vin) {
    final String _sql = "select * from tickets where vin=? and state_code=? order by ticket_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final Ticket _result;
      if(_cursor.moveToFirst()) {
        _result = new Ticket();
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
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _result.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _result.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _result.setTicketDateNew(_tmpTicketDateNew);
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
  public Ticket searchPreviousTicketByMeter(final String meter) {
    final String _sql = "select * from tickets where meter=? order by ticket_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (meter == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, meter);
    }
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final Ticket _result;
      if(_cursor.moveToFirst()) {
        _result = new Ticket();
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
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _result.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _result.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _result.setTicketDateNew(_tmpTicketDateNew);
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
  public List<Long> getPendingCitations() {
    final String _sql = "select citation_number from tickets where status='P'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final List<Long> _result = new ArrayList<Long>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Long _item;
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getLong(0);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public long getTicketIdFromCitationNumber(final long citationNumber) {
    final String _sql = "select ticket_id from tickets where citation_number=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, citationNumber);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final long _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getLong(0);
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
  public long getTicketId() {
    final String _sql = "select ticket_id from tickets where date(ticket_date) < date('now','localtime')";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final long _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getLong(0);
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
  public List<Ticket> removeAllOlderTicketsByHours(final String time) {
    final String _sql = "select  * from tickets where status='S' and strftime('%Y-%m-%d %H:%M',datetime(ticket_date/1000,'unixepoch')) < strftime('%Y-%m-%d %H:%M',datetime('now',?))";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (time == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, time);
    }
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final List<Ticket> _result = new ArrayList<Ticket>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Ticket _item;
        _item = new Ticket();
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _item.setCitationNumber(_tmpCitationNumber);
        final Date _tmpTicketDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp);
        _item.setTicketDate(_tmpTicketDate);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _item.setStateId(_tmpStateId);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item.setStateCode(_tmpStateCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final String _tmpExpiration;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmpExpiration = null;
        } else {
          _tmpExpiration = _cursor.getString(_cursorIndexOfExpiration);
        }
        _item.setExpiration(_tmpExpiration);
        final int _tmpMakeId;
        _tmpMakeId = _cursor.getInt(_cursorIndexOfMakeId);
        _item.setMakeId(_tmpMakeId);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final int _tmpBodyId;
        _tmpBodyId = _cursor.getInt(_cursorIndexOfBodyId);
        _item.setBodyId(_tmpBodyId);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _item.setBodyCode(_tmpBodyCode);
        final int _tmpColorId;
        _tmpColorId = _cursor.getInt(_cursorIndexOfColorId);
        _item.setColorId(_tmpColorId);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpStreetPrefix;
        if (_cursor.isNull(_cursorIndexOfStreetPrefix)) {
          _tmpStreetPrefix = null;
        } else {
          _tmpStreetPrefix = _cursor.getString(_cursorIndexOfStreetPrefix);
        }
        _item.setStreetPrefix(_tmpStreetPrefix);
        final String _tmpStreetSuffix;
        if (_cursor.isNull(_cursorIndexOfStreetSuffix)) {
          _tmpStreetSuffix = null;
        } else {
          _tmpStreetSuffix = _cursor.getString(_cursorIndexOfStreetSuffix);
        }
        _item.setStreetSuffix(_tmpStreetSuffix);
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item.setStreetNumber(_tmpStreetNumber);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item.setDirection(_tmpDirection);
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
        final Date _tmpGpstime;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_1);
        _item.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _item.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsVoid;
        if (_cursor.isNull(_cursorIndexOfIsVoid)) {
          _tmpIsVoid = null;
        } else {
          _tmpIsVoid = _cursor.getString(_cursorIndexOfIsVoid);
        }
        _item.setIsVoid(_tmpIsVoid);
        final String _tmpIsChalked;
        if (_cursor.isNull(_cursorIndexOfIsChalked)) {
          _tmpIsChalked = null;
        } else {
          _tmpIsChalked = _cursor.getString(_cursorIndexOfIsChalked);
        }
        _item.setIsChalked(_tmpIsChalked);
        final String _tmpIsWarn;
        if (_cursor.isNull(_cursorIndexOfIsWarn)) {
          _tmpIsWarn = null;
        } else {
          _tmpIsWarn = _cursor.getString(_cursorIndexOfIsWarn);
        }
        _item.setIsWarn(_tmpIsWarn);
        final String _tmpIsDriveAway;
        if (_cursor.isNull(_cursorIndexOfIsDriveAway)) {
          _tmpIsDriveAway = null;
        } else {
          _tmpIsDriveAway = _cursor.getString(_cursorIndexOfIsDriveAway);
        }
        _item.setIsDriveAway(_tmpIsDriveAway);
        final int _tmpVoidId;
        _tmpVoidId = _cursor.getInt(_cursorIndexOfVoidId);
        _item.setVoidId(_tmpVoidId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final double _tmpFine;
        _tmpFine = _cursor.getDouble(_cursorIndexOfFine);
        _item.setFine(_tmpFine);
        final Date _tmpTimeMarked;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfTimeMarked)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfTimeMarked);
        }
        _tmpTimeMarked = Converters.fromTimestamp(_tmp_2);
        _item.setTimeMarked(_tmpTimeMarked);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _item.setSpace(_tmpSpace);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item.setViolation(_tmpViolation);
        final String _tmpVoidReasonCode;
        if (_cursor.isNull(_cursorIndexOfVoidReasonCode)) {
          _tmpVoidReasonCode = null;
        } else {
          _tmpVoidReasonCode = _cursor.getString(_cursorIndexOfVoidReasonCode);
        }
        _item.setVoidReasonCode(_tmpVoidReasonCode);
        final int _tmpDutyId;
        _tmpDutyId = _cursor.getInt(_cursorIndexOfDutyId);
        _item.setDutyId(_tmpDutyId);
        final String _tmpIsLPR;
        if (_cursor.isNull(_cursorIndexOfIsLPR)) {
          _tmpIsLPR = null;
        } else {
          _tmpIsLPR = _cursor.getString(_cursorIndexOfIsLPR);
        }
        _item.setIsLPR(_tmpIsLPR);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final int _tmpPhoto_count;
        _tmpPhoto_count = _cursor.getInt(_cursorIndexOfPhotoCount);
        _item.setPhoto_count(_tmpPhoto_count);
        final String _tmpLprNotificationId;
        if (_cursor.isNull(_cursorIndexOfLprNotificationId)) {
          _tmpLprNotificationId = null;
        } else {
          _tmpLprNotificationId = _cursor.getString(_cursorIndexOfLprNotificationId);
        }
        _item.setLprNotificationId(_tmpLprNotificationId);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpPlacard;
        if (_cursor.isNull(_cursorIndexOfPlacard)) {
          _tmpPlacard = null;
        } else {
          _tmpPlacard = _cursor.getString(_cursorIndexOfPlacard);
        }
        _item.setPlacard(_tmpPlacard);
        final String _tmpDutyReportId;
        if (_cursor.isNull(_cursorIndexOfDutyReportId)) {
          _tmpDutyReportId = null;
        } else {
          _tmpDutyReportId = _cursor.getString(_cursorIndexOfDutyReportId);
        }
        _item.setDutyReportId(_tmpDutyReportId);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setAppVersion(_tmpAppVersion);
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _item.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _item.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _item.setTicketDateNew(_tmpTicketDateNew);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Ticket> removeAllOlderTicketsAtMidnight() {
    final String _sql = "select * from tickets where status='S' and strftime('%Y-%m-%d',datetime(ticket_date/1000,'unixepoch')) <strftime('%Y-%m-%d','now')";
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final List<Ticket> _result = new ArrayList<Ticket>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Ticket _item;
        _item = new Ticket();
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _item.setCitationNumber(_tmpCitationNumber);
        final Date _tmpTicketDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp);
        _item.setTicketDate(_tmpTicketDate);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _item.setStateId(_tmpStateId);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item.setStateCode(_tmpStateCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final String _tmpExpiration;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmpExpiration = null;
        } else {
          _tmpExpiration = _cursor.getString(_cursorIndexOfExpiration);
        }
        _item.setExpiration(_tmpExpiration);
        final int _tmpMakeId;
        _tmpMakeId = _cursor.getInt(_cursorIndexOfMakeId);
        _item.setMakeId(_tmpMakeId);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final int _tmpBodyId;
        _tmpBodyId = _cursor.getInt(_cursorIndexOfBodyId);
        _item.setBodyId(_tmpBodyId);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _item.setBodyCode(_tmpBodyCode);
        final int _tmpColorId;
        _tmpColorId = _cursor.getInt(_cursorIndexOfColorId);
        _item.setColorId(_tmpColorId);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpStreetPrefix;
        if (_cursor.isNull(_cursorIndexOfStreetPrefix)) {
          _tmpStreetPrefix = null;
        } else {
          _tmpStreetPrefix = _cursor.getString(_cursorIndexOfStreetPrefix);
        }
        _item.setStreetPrefix(_tmpStreetPrefix);
        final String _tmpStreetSuffix;
        if (_cursor.isNull(_cursorIndexOfStreetSuffix)) {
          _tmpStreetSuffix = null;
        } else {
          _tmpStreetSuffix = _cursor.getString(_cursorIndexOfStreetSuffix);
        }
        _item.setStreetSuffix(_tmpStreetSuffix);
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item.setStreetNumber(_tmpStreetNumber);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item.setDirection(_tmpDirection);
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
        final Date _tmpGpstime;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_1);
        _item.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _item.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsVoid;
        if (_cursor.isNull(_cursorIndexOfIsVoid)) {
          _tmpIsVoid = null;
        } else {
          _tmpIsVoid = _cursor.getString(_cursorIndexOfIsVoid);
        }
        _item.setIsVoid(_tmpIsVoid);
        final String _tmpIsChalked;
        if (_cursor.isNull(_cursorIndexOfIsChalked)) {
          _tmpIsChalked = null;
        } else {
          _tmpIsChalked = _cursor.getString(_cursorIndexOfIsChalked);
        }
        _item.setIsChalked(_tmpIsChalked);
        final String _tmpIsWarn;
        if (_cursor.isNull(_cursorIndexOfIsWarn)) {
          _tmpIsWarn = null;
        } else {
          _tmpIsWarn = _cursor.getString(_cursorIndexOfIsWarn);
        }
        _item.setIsWarn(_tmpIsWarn);
        final String _tmpIsDriveAway;
        if (_cursor.isNull(_cursorIndexOfIsDriveAway)) {
          _tmpIsDriveAway = null;
        } else {
          _tmpIsDriveAway = _cursor.getString(_cursorIndexOfIsDriveAway);
        }
        _item.setIsDriveAway(_tmpIsDriveAway);
        final int _tmpVoidId;
        _tmpVoidId = _cursor.getInt(_cursorIndexOfVoidId);
        _item.setVoidId(_tmpVoidId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final double _tmpFine;
        _tmpFine = _cursor.getDouble(_cursorIndexOfFine);
        _item.setFine(_tmpFine);
        final Date _tmpTimeMarked;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfTimeMarked)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfTimeMarked);
        }
        _tmpTimeMarked = Converters.fromTimestamp(_tmp_2);
        _item.setTimeMarked(_tmpTimeMarked);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _item.setSpace(_tmpSpace);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item.setViolation(_tmpViolation);
        final String _tmpVoidReasonCode;
        if (_cursor.isNull(_cursorIndexOfVoidReasonCode)) {
          _tmpVoidReasonCode = null;
        } else {
          _tmpVoidReasonCode = _cursor.getString(_cursorIndexOfVoidReasonCode);
        }
        _item.setVoidReasonCode(_tmpVoidReasonCode);
        final int _tmpDutyId;
        _tmpDutyId = _cursor.getInt(_cursorIndexOfDutyId);
        _item.setDutyId(_tmpDutyId);
        final String _tmpIsLPR;
        if (_cursor.isNull(_cursorIndexOfIsLPR)) {
          _tmpIsLPR = null;
        } else {
          _tmpIsLPR = _cursor.getString(_cursorIndexOfIsLPR);
        }
        _item.setIsLPR(_tmpIsLPR);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final int _tmpPhoto_count;
        _tmpPhoto_count = _cursor.getInt(_cursorIndexOfPhotoCount);
        _item.setPhoto_count(_tmpPhoto_count);
        final String _tmpLprNotificationId;
        if (_cursor.isNull(_cursorIndexOfLprNotificationId)) {
          _tmpLprNotificationId = null;
        } else {
          _tmpLprNotificationId = _cursor.getString(_cursorIndexOfLprNotificationId);
        }
        _item.setLprNotificationId(_tmpLprNotificationId);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpPlacard;
        if (_cursor.isNull(_cursorIndexOfPlacard)) {
          _tmpPlacard = null;
        } else {
          _tmpPlacard = _cursor.getString(_cursorIndexOfPlacard);
        }
        _item.setPlacard(_tmpPlacard);
        final String _tmpDutyReportId;
        if (_cursor.isNull(_cursorIndexOfDutyReportId)) {
          _tmpDutyReportId = null;
        } else {
          _tmpDutyReportId = _cursor.getString(_cursorIndexOfDutyReportId);
        }
        _item.setDutyReportId(_tmpDutyReportId);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setAppVersion(_tmpAppVersion);
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _item.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _item.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _item.setTicketDateNew(_tmpTicketDateNew);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public long getTicketIds(final long ticketId) {
    final String _sql = "select ticket_id from tickets where date(ticket_date) < date('now','localtime') and ticket_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, ticketId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final long _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getLong(0);
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
  public Ticket checkDuplicateRecords(final String plate, final String vin, final String location,
      final Date time) {
    final String _sql = "select * from tickets where (plate=? OR vin=?) and location= ? and ticket_date=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    if (plate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, plate);
    }
    _argIndex = 2;
    if (vin == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, vin);
    }
    _argIndex = 3;
    if (location == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, location);
    }
    _argIndex = 4;
    final Long _tmp;
    _tmp = Converters.dateToTimestamp(time);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final Ticket _result;
      if(_cursor.moveToFirst()) {
        _result = new Ticket();
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
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp_1);
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
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_2);
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
        final Long _tmp_3;
        if (_cursor.isNull(_cursorIndexOfTimeMarked)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getLong(_cursorIndexOfTimeMarked);
        }
        _tmpTimeMarked = Converters.fromTimestamp(_tmp_3);
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
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _result.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _result.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _result.setTicketDateNew(_tmpTicketDateNew);
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
  public Ticket checkDuplicateRecordsPlate(final String plate, final int vid, final String loc) {
    final String _sql = "select * from tickets where plate=? and violation_id=? and location=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (plate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, plate);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, vid);
    _argIndex = 3;
    if (loc == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, loc);
    }
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final Ticket _result;
      if(_cursor.moveToFirst()) {
        _result = new Ticket();
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
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _result.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _result.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _result.setTicketDateNew(_tmpTicketDateNew);
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
  public Ticket chalkSyncOrNot(final long chalkId) {
    final String _sql = "select * from tickets where chalk_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chalkId);
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final Ticket _result;
      if(_cursor.moveToFirst()) {
        _result = new Ticket();
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
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _result.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _result.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _result.setTicketDateNew(_tmpTicketDateNew);
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
  public String getTicketCount(final int userId, final Date statDate, final Date endDate) {
    final String _sql = "SELECT COUNT(*) FROM tickets where userid=? AND (ticket_date BETWEEN ? AND ?) ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    final Long _tmp;
    _tmp = Converters.dateToTimestamp(statDate);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    _argIndex = 3;
    final Long _tmp_1;
    _tmp_1 = Converters.dateToTimestamp(endDate);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp_1);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final String _result;
      if(_cursor.moveToFirst()) {
        final String _tmp_2;
        if (_cursor.isNull(0)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(0);
        }
        _result = _tmp_2;
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
  public List<Ticket> getPendingTickets() {
    final String _sql = "SELECT * FROM tickets where status='P'  order by citation_number ASC";
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final List<Ticket> _result = new ArrayList<Ticket>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Ticket _item;
        _item = new Ticket();
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _item.setCitationNumber(_tmpCitationNumber);
        final Date _tmpTicketDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp);
        _item.setTicketDate(_tmpTicketDate);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _item.setStateId(_tmpStateId);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item.setStateCode(_tmpStateCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final String _tmpExpiration;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmpExpiration = null;
        } else {
          _tmpExpiration = _cursor.getString(_cursorIndexOfExpiration);
        }
        _item.setExpiration(_tmpExpiration);
        final int _tmpMakeId;
        _tmpMakeId = _cursor.getInt(_cursorIndexOfMakeId);
        _item.setMakeId(_tmpMakeId);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final int _tmpBodyId;
        _tmpBodyId = _cursor.getInt(_cursorIndexOfBodyId);
        _item.setBodyId(_tmpBodyId);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _item.setBodyCode(_tmpBodyCode);
        final int _tmpColorId;
        _tmpColorId = _cursor.getInt(_cursorIndexOfColorId);
        _item.setColorId(_tmpColorId);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpStreetPrefix;
        if (_cursor.isNull(_cursorIndexOfStreetPrefix)) {
          _tmpStreetPrefix = null;
        } else {
          _tmpStreetPrefix = _cursor.getString(_cursorIndexOfStreetPrefix);
        }
        _item.setStreetPrefix(_tmpStreetPrefix);
        final String _tmpStreetSuffix;
        if (_cursor.isNull(_cursorIndexOfStreetSuffix)) {
          _tmpStreetSuffix = null;
        } else {
          _tmpStreetSuffix = _cursor.getString(_cursorIndexOfStreetSuffix);
        }
        _item.setStreetSuffix(_tmpStreetSuffix);
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item.setStreetNumber(_tmpStreetNumber);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item.setDirection(_tmpDirection);
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
        final Date _tmpGpstime;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_1);
        _item.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _item.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsVoid;
        if (_cursor.isNull(_cursorIndexOfIsVoid)) {
          _tmpIsVoid = null;
        } else {
          _tmpIsVoid = _cursor.getString(_cursorIndexOfIsVoid);
        }
        _item.setIsVoid(_tmpIsVoid);
        final String _tmpIsChalked;
        if (_cursor.isNull(_cursorIndexOfIsChalked)) {
          _tmpIsChalked = null;
        } else {
          _tmpIsChalked = _cursor.getString(_cursorIndexOfIsChalked);
        }
        _item.setIsChalked(_tmpIsChalked);
        final String _tmpIsWarn;
        if (_cursor.isNull(_cursorIndexOfIsWarn)) {
          _tmpIsWarn = null;
        } else {
          _tmpIsWarn = _cursor.getString(_cursorIndexOfIsWarn);
        }
        _item.setIsWarn(_tmpIsWarn);
        final String _tmpIsDriveAway;
        if (_cursor.isNull(_cursorIndexOfIsDriveAway)) {
          _tmpIsDriveAway = null;
        } else {
          _tmpIsDriveAway = _cursor.getString(_cursorIndexOfIsDriveAway);
        }
        _item.setIsDriveAway(_tmpIsDriveAway);
        final int _tmpVoidId;
        _tmpVoidId = _cursor.getInt(_cursorIndexOfVoidId);
        _item.setVoidId(_tmpVoidId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final double _tmpFine;
        _tmpFine = _cursor.getDouble(_cursorIndexOfFine);
        _item.setFine(_tmpFine);
        final Date _tmpTimeMarked;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfTimeMarked)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfTimeMarked);
        }
        _tmpTimeMarked = Converters.fromTimestamp(_tmp_2);
        _item.setTimeMarked(_tmpTimeMarked);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _item.setSpace(_tmpSpace);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item.setViolation(_tmpViolation);
        final String _tmpVoidReasonCode;
        if (_cursor.isNull(_cursorIndexOfVoidReasonCode)) {
          _tmpVoidReasonCode = null;
        } else {
          _tmpVoidReasonCode = _cursor.getString(_cursorIndexOfVoidReasonCode);
        }
        _item.setVoidReasonCode(_tmpVoidReasonCode);
        final int _tmpDutyId;
        _tmpDutyId = _cursor.getInt(_cursorIndexOfDutyId);
        _item.setDutyId(_tmpDutyId);
        final String _tmpIsLPR;
        if (_cursor.isNull(_cursorIndexOfIsLPR)) {
          _tmpIsLPR = null;
        } else {
          _tmpIsLPR = _cursor.getString(_cursorIndexOfIsLPR);
        }
        _item.setIsLPR(_tmpIsLPR);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final int _tmpPhoto_count;
        _tmpPhoto_count = _cursor.getInt(_cursorIndexOfPhotoCount);
        _item.setPhoto_count(_tmpPhoto_count);
        final String _tmpLprNotificationId;
        if (_cursor.isNull(_cursorIndexOfLprNotificationId)) {
          _tmpLprNotificationId = null;
        } else {
          _tmpLprNotificationId = _cursor.getString(_cursorIndexOfLprNotificationId);
        }
        _item.setLprNotificationId(_tmpLprNotificationId);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpPlacard;
        if (_cursor.isNull(_cursorIndexOfPlacard)) {
          _tmpPlacard = null;
        } else {
          _tmpPlacard = _cursor.getString(_cursorIndexOfPlacard);
        }
        _item.setPlacard(_tmpPlacard);
        final String _tmpDutyReportId;
        if (_cursor.isNull(_cursorIndexOfDutyReportId)) {
          _tmpDutyReportId = null;
        } else {
          _tmpDutyReportId = _cursor.getString(_cursorIndexOfDutyReportId);
        }
        _item.setDutyReportId(_tmpDutyReportId);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setAppVersion(_tmpAppVersion);
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _item.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _item.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _item.setTicketDateNew(_tmpTicketDateNew);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Ticket> getPendingCurrentTickets(final int[] citation) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM tickets where status='P' AND citation_number IN(");
    final int _inputSize = citation.length;
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") order by citation_number ASC");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int _item : citation) {
      _statement.bindLong(_argIndex, _item);
      _argIndex ++;
    }
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final List<Ticket> _result = new ArrayList<Ticket>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Ticket _item_1;
        _item_1 = new Ticket();
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item_1.setTicketId(_tmpTicketId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item_1.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item_1.setCustId(_tmpCustId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item_1.setDeviceId(_tmpDeviceId);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _item_1.setCitationNumber(_tmpCitationNumber);
        final Date _tmpTicketDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp);
        _item_1.setTicketDate(_tmpTicketDate);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _item_1.setStateId(_tmpStateId);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item_1.setStateCode(_tmpStateCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item_1.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item_1.setVin(_tmpVin);
        final String _tmpExpiration;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmpExpiration = null;
        } else {
          _tmpExpiration = _cursor.getString(_cursorIndexOfExpiration);
        }
        _item_1.setExpiration(_tmpExpiration);
        final int _tmpMakeId;
        _tmpMakeId = _cursor.getInt(_cursorIndexOfMakeId);
        _item_1.setMakeId(_tmpMakeId);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item_1.setMakeCode(_tmpMakeCode);
        final int _tmpBodyId;
        _tmpBodyId = _cursor.getInt(_cursorIndexOfBodyId);
        _item_1.setBodyId(_tmpBodyId);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _item_1.setBodyCode(_tmpBodyCode);
        final int _tmpColorId;
        _tmpColorId = _cursor.getInt(_cursorIndexOfColorId);
        _item_1.setColorId(_tmpColorId);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item_1.setColorCode(_tmpColorCode);
        final String _tmpStreetPrefix;
        if (_cursor.isNull(_cursorIndexOfStreetPrefix)) {
          _tmpStreetPrefix = null;
        } else {
          _tmpStreetPrefix = _cursor.getString(_cursorIndexOfStreetPrefix);
        }
        _item_1.setStreetPrefix(_tmpStreetPrefix);
        final String _tmpStreetSuffix;
        if (_cursor.isNull(_cursorIndexOfStreetSuffix)) {
          _tmpStreetSuffix = null;
        } else {
          _tmpStreetSuffix = _cursor.getString(_cursorIndexOfStreetSuffix);
        }
        _item_1.setStreetSuffix(_tmpStreetSuffix);
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item_1.setStreetNumber(_tmpStreetNumber);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item_1.setLocation(_tmpLocation);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item_1.setDirection(_tmpDirection);
        final String _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        }
        _item_1.setLatitude(_tmpLatitude);
        final String _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        }
        _item_1.setLongitude(_tmpLongitude);
        final Date _tmpGpstime;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_1);
        _item_1.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _item_1.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsVoid;
        if (_cursor.isNull(_cursorIndexOfIsVoid)) {
          _tmpIsVoid = null;
        } else {
          _tmpIsVoid = _cursor.getString(_cursorIndexOfIsVoid);
        }
        _item_1.setIsVoid(_tmpIsVoid);
        final String _tmpIsChalked;
        if (_cursor.isNull(_cursorIndexOfIsChalked)) {
          _tmpIsChalked = null;
        } else {
          _tmpIsChalked = _cursor.getString(_cursorIndexOfIsChalked);
        }
        _item_1.setIsChalked(_tmpIsChalked);
        final String _tmpIsWarn;
        if (_cursor.isNull(_cursorIndexOfIsWarn)) {
          _tmpIsWarn = null;
        } else {
          _tmpIsWarn = _cursor.getString(_cursorIndexOfIsWarn);
        }
        _item_1.setIsWarn(_tmpIsWarn);
        final String _tmpIsDriveAway;
        if (_cursor.isNull(_cursorIndexOfIsDriveAway)) {
          _tmpIsDriveAway = null;
        } else {
          _tmpIsDriveAway = _cursor.getString(_cursorIndexOfIsDriveAway);
        }
        _item_1.setIsDriveAway(_tmpIsDriveAway);
        final int _tmpVoidId;
        _tmpVoidId = _cursor.getInt(_cursorIndexOfVoidId);
        _item_1.setVoidId(_tmpVoidId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item_1.setChalkId(_tmpChalkId);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item_1.setPermit(_tmpPermit);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item_1.setMeter(_tmpMeter);
        final double _tmpFine;
        _tmpFine = _cursor.getDouble(_cursorIndexOfFine);
        _item_1.setFine(_tmpFine);
        final Date _tmpTimeMarked;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfTimeMarked)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfTimeMarked);
        }
        _tmpTimeMarked = Converters.fromTimestamp(_tmp_2);
        _item_1.setTimeMarked(_tmpTimeMarked);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _item_1.setSpace(_tmpSpace);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item_1.setViolationCode(_tmpViolationCode);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item_1.setViolation(_tmpViolation);
        final String _tmpVoidReasonCode;
        if (_cursor.isNull(_cursorIndexOfVoidReasonCode)) {
          _tmpVoidReasonCode = null;
        } else {
          _tmpVoidReasonCode = _cursor.getString(_cursorIndexOfVoidReasonCode);
        }
        _item_1.setVoidReasonCode(_tmpVoidReasonCode);
        final int _tmpDutyId;
        _tmpDutyId = _cursor.getInt(_cursorIndexOfDutyId);
        _item_1.setDutyId(_tmpDutyId);
        final String _tmpIsLPR;
        if (_cursor.isNull(_cursorIndexOfIsLPR)) {
          _tmpIsLPR = null;
        } else {
          _tmpIsLPR = _cursor.getString(_cursorIndexOfIsLPR);
        }
        _item_1.setIsLPR(_tmpIsLPR);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item_1.setViolationId(_tmpViolationId);
        final int _tmpPhoto_count;
        _tmpPhoto_count = _cursor.getInt(_cursorIndexOfPhotoCount);
        _item_1.setPhoto_count(_tmpPhoto_count);
        final String _tmpLprNotificationId;
        if (_cursor.isNull(_cursorIndexOfLprNotificationId)) {
          _tmpLprNotificationId = null;
        } else {
          _tmpLprNotificationId = _cursor.getString(_cursorIndexOfLprNotificationId);
        }
        _item_1.setLprNotificationId(_tmpLprNotificationId);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item_1.setStatus(_tmpStatus);
        final String _tmpPlacard;
        if (_cursor.isNull(_cursorIndexOfPlacard)) {
          _tmpPlacard = null;
        } else {
          _tmpPlacard = _cursor.getString(_cursorIndexOfPlacard);
        }
        _item_1.setPlacard(_tmpPlacard);
        final String _tmpDutyReportId;
        if (_cursor.isNull(_cursorIndexOfDutyReportId)) {
          _tmpDutyReportId = null;
        } else {
          _tmpDutyReportId = _cursor.getString(_cursorIndexOfDutyReportId);
        }
        _item_1.setDutyReportId(_tmpDutyReportId);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item_1.setAppVersion(_tmpAppVersion);
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _item_1.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _item_1.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _item_1.setTicketDateNew(_tmpTicketDateNew);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Ticket> getPendingTicketsPI() {
    final String _sql = "SELECT * FROM tickets where status='PI'";
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final List<Ticket> _result = new ArrayList<Ticket>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Ticket _item;
        _item = new Ticket();
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _item.setCitationNumber(_tmpCitationNumber);
        final Date _tmpTicketDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp);
        _item.setTicketDate(_tmpTicketDate);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _item.setStateId(_tmpStateId);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item.setStateCode(_tmpStateCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final String _tmpExpiration;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmpExpiration = null;
        } else {
          _tmpExpiration = _cursor.getString(_cursorIndexOfExpiration);
        }
        _item.setExpiration(_tmpExpiration);
        final int _tmpMakeId;
        _tmpMakeId = _cursor.getInt(_cursorIndexOfMakeId);
        _item.setMakeId(_tmpMakeId);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final int _tmpBodyId;
        _tmpBodyId = _cursor.getInt(_cursorIndexOfBodyId);
        _item.setBodyId(_tmpBodyId);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _item.setBodyCode(_tmpBodyCode);
        final int _tmpColorId;
        _tmpColorId = _cursor.getInt(_cursorIndexOfColorId);
        _item.setColorId(_tmpColorId);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpStreetPrefix;
        if (_cursor.isNull(_cursorIndexOfStreetPrefix)) {
          _tmpStreetPrefix = null;
        } else {
          _tmpStreetPrefix = _cursor.getString(_cursorIndexOfStreetPrefix);
        }
        _item.setStreetPrefix(_tmpStreetPrefix);
        final String _tmpStreetSuffix;
        if (_cursor.isNull(_cursorIndexOfStreetSuffix)) {
          _tmpStreetSuffix = null;
        } else {
          _tmpStreetSuffix = _cursor.getString(_cursorIndexOfStreetSuffix);
        }
        _item.setStreetSuffix(_tmpStreetSuffix);
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item.setStreetNumber(_tmpStreetNumber);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item.setDirection(_tmpDirection);
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
        final Date _tmpGpstime;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_1);
        _item.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _item.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsVoid;
        if (_cursor.isNull(_cursorIndexOfIsVoid)) {
          _tmpIsVoid = null;
        } else {
          _tmpIsVoid = _cursor.getString(_cursorIndexOfIsVoid);
        }
        _item.setIsVoid(_tmpIsVoid);
        final String _tmpIsChalked;
        if (_cursor.isNull(_cursorIndexOfIsChalked)) {
          _tmpIsChalked = null;
        } else {
          _tmpIsChalked = _cursor.getString(_cursorIndexOfIsChalked);
        }
        _item.setIsChalked(_tmpIsChalked);
        final String _tmpIsWarn;
        if (_cursor.isNull(_cursorIndexOfIsWarn)) {
          _tmpIsWarn = null;
        } else {
          _tmpIsWarn = _cursor.getString(_cursorIndexOfIsWarn);
        }
        _item.setIsWarn(_tmpIsWarn);
        final String _tmpIsDriveAway;
        if (_cursor.isNull(_cursorIndexOfIsDriveAway)) {
          _tmpIsDriveAway = null;
        } else {
          _tmpIsDriveAway = _cursor.getString(_cursorIndexOfIsDriveAway);
        }
        _item.setIsDriveAway(_tmpIsDriveAway);
        final int _tmpVoidId;
        _tmpVoidId = _cursor.getInt(_cursorIndexOfVoidId);
        _item.setVoidId(_tmpVoidId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final double _tmpFine;
        _tmpFine = _cursor.getDouble(_cursorIndexOfFine);
        _item.setFine(_tmpFine);
        final Date _tmpTimeMarked;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfTimeMarked)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfTimeMarked);
        }
        _tmpTimeMarked = Converters.fromTimestamp(_tmp_2);
        _item.setTimeMarked(_tmpTimeMarked);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _item.setSpace(_tmpSpace);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item.setViolation(_tmpViolation);
        final String _tmpVoidReasonCode;
        if (_cursor.isNull(_cursorIndexOfVoidReasonCode)) {
          _tmpVoidReasonCode = null;
        } else {
          _tmpVoidReasonCode = _cursor.getString(_cursorIndexOfVoidReasonCode);
        }
        _item.setVoidReasonCode(_tmpVoidReasonCode);
        final int _tmpDutyId;
        _tmpDutyId = _cursor.getInt(_cursorIndexOfDutyId);
        _item.setDutyId(_tmpDutyId);
        final String _tmpIsLPR;
        if (_cursor.isNull(_cursorIndexOfIsLPR)) {
          _tmpIsLPR = null;
        } else {
          _tmpIsLPR = _cursor.getString(_cursorIndexOfIsLPR);
        }
        _item.setIsLPR(_tmpIsLPR);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final int _tmpPhoto_count;
        _tmpPhoto_count = _cursor.getInt(_cursorIndexOfPhotoCount);
        _item.setPhoto_count(_tmpPhoto_count);
        final String _tmpLprNotificationId;
        if (_cursor.isNull(_cursorIndexOfLprNotificationId)) {
          _tmpLprNotificationId = null;
        } else {
          _tmpLprNotificationId = _cursor.getString(_cursorIndexOfLprNotificationId);
        }
        _item.setLprNotificationId(_tmpLprNotificationId);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpPlacard;
        if (_cursor.isNull(_cursorIndexOfPlacard)) {
          _tmpPlacard = null;
        } else {
          _tmpPlacard = _cursor.getString(_cursorIndexOfPlacard);
        }
        _item.setPlacard(_tmpPlacard);
        final String _tmpDutyReportId;
        if (_cursor.isNull(_cursorIndexOfDutyReportId)) {
          _tmpDutyReportId = null;
        } else {
          _tmpDutyReportId = _cursor.getString(_cursorIndexOfDutyReportId);
        }
        _item.setDutyReportId(_tmpDutyReportId);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setAppVersion(_tmpAppVersion);
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _item.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _item.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _item.setTicketDateNew(_tmpTicketDateNew);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Ticket checkPindingTickets(final int userId) {
    final String _sql = "SELECT * from tickets  where  date(ticket_date/1000, 'unixepoch') < date(strftime('%s','now'),'unixepoch') and userid =? and status = 'S'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final Ticket _result;
      if(_cursor.moveToFirst()) {
        _result = new Ticket();
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
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _result.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _result.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _result.setTicketDateNew(_tmpTicketDateNew);
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
  public List<Ticket> removeAllPendingTicket(final int userId) {
    final String _sql = "SELECT * from tickets  where  date(ticket_date/1000, 'unixepoch') < date(strftime('%s','now'),'unixepoch') and userid=? and status = 'S'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final List<Ticket> _result = new ArrayList<Ticket>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Ticket _item;
        _item = new Ticket();
        final long _tmpTicketId;
        _tmpTicketId = _cursor.getLong(_cursorIndexOfTicketId);
        _item.setTicketId(_tmpTicketId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final int _tmpDeviceId;
        _tmpDeviceId = _cursor.getInt(_cursorIndexOfDeviceId);
        _item.setDeviceId(_tmpDeviceId);
        final long _tmpCitationNumber;
        _tmpCitationNumber = _cursor.getLong(_cursorIndexOfCitationNumber);
        _item.setCitationNumber(_tmpCitationNumber);
        final Date _tmpTicketDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTicketDate);
        }
        _tmpTicketDate = Converters.fromTimestamp(_tmp);
        _item.setTicketDate(_tmpTicketDate);
        final int _tmpStateId;
        _tmpStateId = _cursor.getInt(_cursorIndexOfStateId);
        _item.setStateId(_tmpStateId);
        final String _tmpStateCode;
        if (_cursor.isNull(_cursorIndexOfStateCode)) {
          _tmpStateCode = null;
        } else {
          _tmpStateCode = _cursor.getString(_cursorIndexOfStateCode);
        }
        _item.setStateCode(_tmpStateCode);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpVin;
        if (_cursor.isNull(_cursorIndexOfVin)) {
          _tmpVin = null;
        } else {
          _tmpVin = _cursor.getString(_cursorIndexOfVin);
        }
        _item.setVin(_tmpVin);
        final String _tmpExpiration;
        if (_cursor.isNull(_cursorIndexOfExpiration)) {
          _tmpExpiration = null;
        } else {
          _tmpExpiration = _cursor.getString(_cursorIndexOfExpiration);
        }
        _item.setExpiration(_tmpExpiration);
        final int _tmpMakeId;
        _tmpMakeId = _cursor.getInt(_cursorIndexOfMakeId);
        _item.setMakeId(_tmpMakeId);
        final String _tmpMakeCode;
        if (_cursor.isNull(_cursorIndexOfMakeCode)) {
          _tmpMakeCode = null;
        } else {
          _tmpMakeCode = _cursor.getString(_cursorIndexOfMakeCode);
        }
        _item.setMakeCode(_tmpMakeCode);
        final int _tmpBodyId;
        _tmpBodyId = _cursor.getInt(_cursorIndexOfBodyId);
        _item.setBodyId(_tmpBodyId);
        final String _tmpBodyCode;
        if (_cursor.isNull(_cursorIndexOfBodyCode)) {
          _tmpBodyCode = null;
        } else {
          _tmpBodyCode = _cursor.getString(_cursorIndexOfBodyCode);
        }
        _item.setBodyCode(_tmpBodyCode);
        final int _tmpColorId;
        _tmpColorId = _cursor.getInt(_cursorIndexOfColorId);
        _item.setColorId(_tmpColorId);
        final String _tmpColorCode;
        if (_cursor.isNull(_cursorIndexOfColorCode)) {
          _tmpColorCode = null;
        } else {
          _tmpColorCode = _cursor.getString(_cursorIndexOfColorCode);
        }
        _item.setColorCode(_tmpColorCode);
        final String _tmpStreetPrefix;
        if (_cursor.isNull(_cursorIndexOfStreetPrefix)) {
          _tmpStreetPrefix = null;
        } else {
          _tmpStreetPrefix = _cursor.getString(_cursorIndexOfStreetPrefix);
        }
        _item.setStreetPrefix(_tmpStreetPrefix);
        final String _tmpStreetSuffix;
        if (_cursor.isNull(_cursorIndexOfStreetSuffix)) {
          _tmpStreetSuffix = null;
        } else {
          _tmpStreetSuffix = _cursor.getString(_cursorIndexOfStreetSuffix);
        }
        _item.setStreetSuffix(_tmpStreetSuffix);
        final String _tmpStreetNumber;
        if (_cursor.isNull(_cursorIndexOfStreetNumber)) {
          _tmpStreetNumber = null;
        } else {
          _tmpStreetNumber = _cursor.getString(_cursorIndexOfStreetNumber);
        }
        _item.setStreetNumber(_tmpStreetNumber);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
        final String _tmpDirection;
        if (_cursor.isNull(_cursorIndexOfDirection)) {
          _tmpDirection = null;
        } else {
          _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
        }
        _item.setDirection(_tmpDirection);
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
        final Date _tmpGpstime;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfGpstime)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfGpstime);
        }
        _tmpGpstime = Converters.fromTimestamp(_tmp_1);
        _item.setGpstime(_tmpGpstime);
        final String _tmpIsGPSLocation;
        if (_cursor.isNull(_cursorIndexOfIsGPSLocation)) {
          _tmpIsGPSLocation = null;
        } else {
          _tmpIsGPSLocation = _cursor.getString(_cursorIndexOfIsGPSLocation);
        }
        _item.setIsGPSLocation(_tmpIsGPSLocation);
        final String _tmpIsVoid;
        if (_cursor.isNull(_cursorIndexOfIsVoid)) {
          _tmpIsVoid = null;
        } else {
          _tmpIsVoid = _cursor.getString(_cursorIndexOfIsVoid);
        }
        _item.setIsVoid(_tmpIsVoid);
        final String _tmpIsChalked;
        if (_cursor.isNull(_cursorIndexOfIsChalked)) {
          _tmpIsChalked = null;
        } else {
          _tmpIsChalked = _cursor.getString(_cursorIndexOfIsChalked);
        }
        _item.setIsChalked(_tmpIsChalked);
        final String _tmpIsWarn;
        if (_cursor.isNull(_cursorIndexOfIsWarn)) {
          _tmpIsWarn = null;
        } else {
          _tmpIsWarn = _cursor.getString(_cursorIndexOfIsWarn);
        }
        _item.setIsWarn(_tmpIsWarn);
        final String _tmpIsDriveAway;
        if (_cursor.isNull(_cursorIndexOfIsDriveAway)) {
          _tmpIsDriveAway = null;
        } else {
          _tmpIsDriveAway = _cursor.getString(_cursorIndexOfIsDriveAway);
        }
        _item.setIsDriveAway(_tmpIsDriveAway);
        final int _tmpVoidId;
        _tmpVoidId = _cursor.getInt(_cursorIndexOfVoidId);
        _item.setVoidId(_tmpVoidId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpMeter;
        if (_cursor.isNull(_cursorIndexOfMeter)) {
          _tmpMeter = null;
        } else {
          _tmpMeter = _cursor.getString(_cursorIndexOfMeter);
        }
        _item.setMeter(_tmpMeter);
        final double _tmpFine;
        _tmpFine = _cursor.getDouble(_cursorIndexOfFine);
        _item.setFine(_tmpFine);
        final Date _tmpTimeMarked;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfTimeMarked)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfTimeMarked);
        }
        _tmpTimeMarked = Converters.fromTimestamp(_tmp_2);
        _item.setTimeMarked(_tmpTimeMarked);
        final String _tmpSpace;
        if (_cursor.isNull(_cursorIndexOfSpace)) {
          _tmpSpace = null;
        } else {
          _tmpSpace = _cursor.getString(_cursorIndexOfSpace);
        }
        _item.setSpace(_tmpSpace);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        final String _tmpViolation;
        if (_cursor.isNull(_cursorIndexOfViolation)) {
          _tmpViolation = null;
        } else {
          _tmpViolation = _cursor.getString(_cursorIndexOfViolation);
        }
        _item.setViolation(_tmpViolation);
        final String _tmpVoidReasonCode;
        if (_cursor.isNull(_cursorIndexOfVoidReasonCode)) {
          _tmpVoidReasonCode = null;
        } else {
          _tmpVoidReasonCode = _cursor.getString(_cursorIndexOfVoidReasonCode);
        }
        _item.setVoidReasonCode(_tmpVoidReasonCode);
        final int _tmpDutyId;
        _tmpDutyId = _cursor.getInt(_cursorIndexOfDutyId);
        _item.setDutyId(_tmpDutyId);
        final String _tmpIsLPR;
        if (_cursor.isNull(_cursorIndexOfIsLPR)) {
          _tmpIsLPR = null;
        } else {
          _tmpIsLPR = _cursor.getString(_cursorIndexOfIsLPR);
        }
        _item.setIsLPR(_tmpIsLPR);
        final int _tmpViolationId;
        _tmpViolationId = _cursor.getInt(_cursorIndexOfViolationId);
        _item.setViolationId(_tmpViolationId);
        final int _tmpPhoto_count;
        _tmpPhoto_count = _cursor.getInt(_cursorIndexOfPhotoCount);
        _item.setPhoto_count(_tmpPhoto_count);
        final String _tmpLprNotificationId;
        if (_cursor.isNull(_cursorIndexOfLprNotificationId)) {
          _tmpLprNotificationId = null;
        } else {
          _tmpLprNotificationId = _cursor.getString(_cursorIndexOfLprNotificationId);
        }
        _item.setLprNotificationId(_tmpLprNotificationId);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpPlacard;
        if (_cursor.isNull(_cursorIndexOfPlacard)) {
          _tmpPlacard = null;
        } else {
          _tmpPlacard = _cursor.getString(_cursorIndexOfPlacard);
        }
        _item.setPlacard(_tmpPlacard);
        final String _tmpDutyReportId;
        if (_cursor.isNull(_cursorIndexOfDutyReportId)) {
          _tmpDutyReportId = null;
        } else {
          _tmpDutyReportId = _cursor.getString(_cursorIndexOfDutyReportId);
        }
        _item.setDutyReportId(_tmpDutyReportId);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setAppVersion(_tmpAppVersion);
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _item.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _item.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _item.setTicketDateNew(_tmpTicketDateNew);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Ticket checkDuplicate(final String citation) {
    final String _sql = "SELECT * from tickets  where citation_number=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (citation == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, citation);
    }
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
      final int _cursorIndexOfChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfChalkLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_last_seen");
      final int _cursorIndexOfTicketDateNew = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_date_new");
      final Ticket _result;
      if(_cursor.moveToFirst()) {
        _result = new Ticket();
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
        final String _tmpChalkTime;
        if (_cursor.isNull(_cursorIndexOfChalkTime)) {
          _tmpChalkTime = null;
        } else {
          _tmpChalkTime = _cursor.getString(_cursorIndexOfChalkTime);
        }
        _result.setChalkTime(_tmpChalkTime);
        final String _tmpChalkLastSeen;
        if (_cursor.isNull(_cursorIndexOfChalkLastSeen)) {
          _tmpChalkLastSeen = null;
        } else {
          _tmpChalkLastSeen = _cursor.getString(_cursorIndexOfChalkLastSeen);
        }
        _result.setChalkLastSeen(_tmpChalkLastSeen);
        final String _tmpTicketDateNew;
        if (_cursor.isNull(_cursorIndexOfTicketDateNew)) {
          _tmpTicketDateNew = null;
        } else {
          _tmpTicketDateNew = _cursor.getString(_cursorIndexOfTicketDateNew);
        }
        _result.setTicketDateNew(_tmpTicketDateNew);
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
  public int getPhotoCount(final String citation) {
    final String _sql = "SELECT photo_count from tickets where citation_number=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (citation == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, citation);
    }
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
