package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.LPRNotify;
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
public final class LPRNotificationsDao_Impl implements LPRNotificationsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LPRNotify> __insertionAdapterOfLPRNotify;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAllNotifications;

  private final SharedSQLiteStatement __preparedStmtOfRemoveNotificationById;

  public LPRNotificationsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLPRNotify = new EntityInsertionAdapter<LPRNotify>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `lpr_notifications` (`notification_id`,`plate`,`state`,`make`,`model`,`body`,`status`,`location`,`latitude`,`longitude`,`photo1`,`photo2`,`photo3`,`photo4`,`date_notify`,`first_chalk_time`,`last_seen`,`lpr_scan_id`,`lpr_camera_id`,`lpr_user_id`,`color`,`permit`,`permit_type`,`permit_status`,`duty_group`,`comments`,`comments2`,`violation_id`,`violation_desc`,`violation_code`,`device_id`,`zone`,`ticket_issues`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LPRNotify value) {
        if (value.getNotificationId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getNotificationId());
        }
        if (value.getPlate() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getPlate());
        }
        if (value.getState() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getState());
        }
        if (value.getMake() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getMake());
        }
        if (value.getModel() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getModel());
        }
        if (value.getBody() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getBody());
        }
        if (value.getStatus() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getStatus());
        }
        if (value.getLocation() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getLocation());
        }
        if (value.getLatitude() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getLatitude());
        }
        if (value.getLongitude() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getLongitude());
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
        if (value.getPhoto4() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getPhoto4());
        }
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getNotifyDate());
        if (_tmp == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindLong(15, _tmp);
        }
        final Long _tmp_1;
        _tmp_1 = Converters.dateToTimestamp(value.getFirstChalkTime());
        if (_tmp_1 == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindLong(16, _tmp_1);
        }
        final Long _tmp_2;
        _tmp_2 = Converters.dateToTimestamp(value.getLastSeen());
        if (_tmp_2 == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindLong(17, _tmp_2);
        }
        if (value.getLprScanId() == null) {
          stmt.bindNull(18);
        } else {
          stmt.bindString(18, value.getLprScanId());
        }
        if (value.getLprCameraId() == null) {
          stmt.bindNull(19);
        } else {
          stmt.bindString(19, value.getLprCameraId());
        }
        if (value.getLprUserId() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.getLprUserId());
        }
        if (value.getColor() == null) {
          stmt.bindNull(21);
        } else {
          stmt.bindString(21, value.getColor());
        }
        if (value.getPermit() == null) {
          stmt.bindNull(22);
        } else {
          stmt.bindString(22, value.getPermit());
        }
        if (value.getPermitType() == null) {
          stmt.bindNull(23);
        } else {
          stmt.bindString(23, value.getPermitType());
        }
        if (value.getPermitStatus() == null) {
          stmt.bindNull(24);
        } else {
          stmt.bindString(24, value.getPermitStatus());
        }
        if (value.getDutyGroup() == null) {
          stmt.bindNull(25);
        } else {
          stmt.bindString(25, value.getDutyGroup());
        }
        if (value.getComments() == null) {
          stmt.bindNull(26);
        } else {
          stmt.bindString(26, value.getComments());
        }
        if (value.getComments2() == null) {
          stmt.bindNull(27);
        } else {
          stmt.bindString(27, value.getComments2());
        }
        if (value.getViolationId() == null) {
          stmt.bindNull(28);
        } else {
          stmt.bindString(28, value.getViolationId());
        }
        if (value.getViolationDesc() == null) {
          stmt.bindNull(29);
        } else {
          stmt.bindString(29, value.getViolationDesc());
        }
        if (value.getViolationCode() == null) {
          stmt.bindNull(30);
        } else {
          stmt.bindString(30, value.getViolationCode());
        }
        if (value.getDeviceId() == null) {
          stmt.bindNull(31);
        } else {
          stmt.bindString(31, value.getDeviceId());
        }
        if (value.getZone() == null) {
          stmt.bindNull(32);
        } else {
          stmt.bindString(32, value.getZone());
        }
        final int _tmp_3;
        _tmp_3 = value.isTicketIssued() ? 1 : 0;
        stmt.bindLong(33, _tmp_3);
      }
    };
    this.__preparedStmtOfRemoveAllNotifications = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from lpr_notifications";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveNotificationById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from lpr_notifications where notification_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertLPRNotify(final LPRNotify... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLPRNotify.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertLPRNotify(final LPRNotify data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLPRNotify.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertLPRNotifyList(final List<LPRNotify> LPRNotifysList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLPRNotify.insert(LPRNotifysList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void removeAllNotifications() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveAllNotifications.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveAllNotifications.release(_stmt);
    }
  }

  @Override
  public void removeNotificationById(final String notificationId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveNotificationById.acquire();
    int _argIndex = 1;
    if (notificationId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, notificationId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveNotificationById.release(_stmt);
    }
  }

  @Override
  public List<LPRNotify> getLPRNotifications() {
    final String _sql = "select * from lpr_notifications order by date_notify DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfNotificationId = CursorUtil.getColumnIndexOrThrow(_cursor, "notification_id");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
      final int _cursorIndexOfMake = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfModel = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
      final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfPhoto1 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo1");
      final int _cursorIndexOfPhoto2 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo2");
      final int _cursorIndexOfPhoto3 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo3");
      final int _cursorIndexOfPhoto4 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo4");
      final int _cursorIndexOfNotifyDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date_notify");
      final int _cursorIndexOfFirstChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "first_chalk_time");
      final int _cursorIndexOfLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "last_seen");
      final int _cursorIndexOfLprScanId = CursorUtil.getColumnIndexOrThrow(_cursor, "lpr_scan_id");
      final int _cursorIndexOfLprCameraId = CursorUtil.getColumnIndexOrThrow(_cursor, "lpr_camera_id");
      final int _cursorIndexOfLprUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "lpr_user_id");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfPermitType = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_type");
      final int _cursorIndexOfPermitStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_status");
      final int _cursorIndexOfDutyGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "duty_group");
      final int _cursorIndexOfComments = CursorUtil.getColumnIndexOrThrow(_cursor, "comments");
      final int _cursorIndexOfComments2 = CursorUtil.getColumnIndexOrThrow(_cursor, "comments2");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfViolationDesc = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_desc");
      final int _cursorIndexOfViolationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_code");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfZone = CursorUtil.getColumnIndexOrThrow(_cursor, "zone");
      final int _cursorIndexOfTicketIssued = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_issues");
      final List<LPRNotify> _result = new ArrayList<LPRNotify>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final LPRNotify _item;
        _item = new LPRNotify();
        final String _tmpNotificationId;
        if (_cursor.isNull(_cursorIndexOfNotificationId)) {
          _tmpNotificationId = null;
        } else {
          _tmpNotificationId = _cursor.getString(_cursorIndexOfNotificationId);
        }
        _item.setNotificationId(_tmpNotificationId);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _item.setPlate(_tmpPlate);
        final String _tmpState;
        if (_cursor.isNull(_cursorIndexOfState)) {
          _tmpState = null;
        } else {
          _tmpState = _cursor.getString(_cursorIndexOfState);
        }
        _item.setState(_tmpState);
        final String _tmpMake;
        if (_cursor.isNull(_cursorIndexOfMake)) {
          _tmpMake = null;
        } else {
          _tmpMake = _cursor.getString(_cursorIndexOfMake);
        }
        _item.setMake(_tmpMake);
        final String _tmpModel;
        if (_cursor.isNull(_cursorIndexOfModel)) {
          _tmpModel = null;
        } else {
          _tmpModel = _cursor.getString(_cursorIndexOfModel);
        }
        _item.setModel(_tmpModel);
        final String _tmpBody;
        if (_cursor.isNull(_cursorIndexOfBody)) {
          _tmpBody = null;
        } else {
          _tmpBody = _cursor.getString(_cursorIndexOfBody);
        }
        _item.setBody(_tmpBody);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _item.setLocation(_tmpLocation);
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
        final String _tmpPhoto4;
        if (_cursor.isNull(_cursorIndexOfPhoto4)) {
          _tmpPhoto4 = null;
        } else {
          _tmpPhoto4 = _cursor.getString(_cursorIndexOfPhoto4);
        }
        _item.setPhoto4(_tmpPhoto4);
        final Date _tmpNotifyDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfNotifyDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfNotifyDate);
        }
        _tmpNotifyDate = Converters.fromTimestamp(_tmp);
        _item.setNotifyDate(_tmpNotifyDate);
        final Date _tmpFirstChalkTime;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfFirstChalkTime)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfFirstChalkTime);
        }
        _tmpFirstChalkTime = Converters.fromTimestamp(_tmp_1);
        _item.setFirstChalkTime(_tmpFirstChalkTime);
        final Date _tmpLastSeen;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfLastSeen)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfLastSeen);
        }
        _tmpLastSeen = Converters.fromTimestamp(_tmp_2);
        _item.setLastSeen(_tmpLastSeen);
        final String _tmpLprScanId;
        if (_cursor.isNull(_cursorIndexOfLprScanId)) {
          _tmpLprScanId = null;
        } else {
          _tmpLprScanId = _cursor.getString(_cursorIndexOfLprScanId);
        }
        _item.setLprScanId(_tmpLprScanId);
        final String _tmpLprCameraId;
        if (_cursor.isNull(_cursorIndexOfLprCameraId)) {
          _tmpLprCameraId = null;
        } else {
          _tmpLprCameraId = _cursor.getString(_cursorIndexOfLprCameraId);
        }
        _item.setLprCameraId(_tmpLprCameraId);
        final String _tmpLprUserId;
        if (_cursor.isNull(_cursorIndexOfLprUserId)) {
          _tmpLprUserId = null;
        } else {
          _tmpLprUserId = _cursor.getString(_cursorIndexOfLprUserId);
        }
        _item.setLprUserId(_tmpLprUserId);
        final String _tmpColor;
        if (_cursor.isNull(_cursorIndexOfColor)) {
          _tmpColor = null;
        } else {
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
        }
        _item.setColor(_tmpColor);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _item.setPermit(_tmpPermit);
        final String _tmpPermitType;
        if (_cursor.isNull(_cursorIndexOfPermitType)) {
          _tmpPermitType = null;
        } else {
          _tmpPermitType = _cursor.getString(_cursorIndexOfPermitType);
        }
        _item.setPermitType(_tmpPermitType);
        final String _tmpPermitStatus;
        if (_cursor.isNull(_cursorIndexOfPermitStatus)) {
          _tmpPermitStatus = null;
        } else {
          _tmpPermitStatus = _cursor.getString(_cursorIndexOfPermitStatus);
        }
        _item.setPermitStatus(_tmpPermitStatus);
        final String _tmpDutyGroup;
        if (_cursor.isNull(_cursorIndexOfDutyGroup)) {
          _tmpDutyGroup = null;
        } else {
          _tmpDutyGroup = _cursor.getString(_cursorIndexOfDutyGroup);
        }
        _item.setDutyGroup(_tmpDutyGroup);
        final String _tmpComments;
        if (_cursor.isNull(_cursorIndexOfComments)) {
          _tmpComments = null;
        } else {
          _tmpComments = _cursor.getString(_cursorIndexOfComments);
        }
        _item.setComments(_tmpComments);
        final String _tmpComments2;
        if (_cursor.isNull(_cursorIndexOfComments2)) {
          _tmpComments2 = null;
        } else {
          _tmpComments2 = _cursor.getString(_cursorIndexOfComments2);
        }
        _item.setComments2(_tmpComments2);
        final String _tmpViolationId;
        if (_cursor.isNull(_cursorIndexOfViolationId)) {
          _tmpViolationId = null;
        } else {
          _tmpViolationId = _cursor.getString(_cursorIndexOfViolationId);
        }
        _item.setViolationId(_tmpViolationId);
        final String _tmpViolationDesc;
        if (_cursor.isNull(_cursorIndexOfViolationDesc)) {
          _tmpViolationDesc = null;
        } else {
          _tmpViolationDesc = _cursor.getString(_cursorIndexOfViolationDesc);
        }
        _item.setViolationDesc(_tmpViolationDesc);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _item.setViolationCode(_tmpViolationCode);
        final String _tmpDeviceId;
        if (_cursor.isNull(_cursorIndexOfDeviceId)) {
          _tmpDeviceId = null;
        } else {
          _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
        }
        _item.setDeviceId(_tmpDeviceId);
        final String _tmpZone;
        if (_cursor.isNull(_cursorIndexOfZone)) {
          _tmpZone = null;
        } else {
          _tmpZone = _cursor.getString(_cursorIndexOfZone);
        }
        _item.setZone(_tmpZone);
        final boolean _tmpTicketIssued;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfTicketIssued);
        _tmpTicketIssued = _tmp_3 != 0;
        _item.setTicketIssued(_tmpTicketIssued);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LPRNotify getLPRNotificationById(final int notificationId) {
    final String _sql = "select * from lpr_notifications where notification_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, notificationId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfNotificationId = CursorUtil.getColumnIndexOrThrow(_cursor, "notification_id");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
      final int _cursorIndexOfMake = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfModel = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
      final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfPhoto1 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo1");
      final int _cursorIndexOfPhoto2 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo2");
      final int _cursorIndexOfPhoto3 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo3");
      final int _cursorIndexOfPhoto4 = CursorUtil.getColumnIndexOrThrow(_cursor, "photo4");
      final int _cursorIndexOfNotifyDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date_notify");
      final int _cursorIndexOfFirstChalkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "first_chalk_time");
      final int _cursorIndexOfLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "last_seen");
      final int _cursorIndexOfLprScanId = CursorUtil.getColumnIndexOrThrow(_cursor, "lpr_scan_id");
      final int _cursorIndexOfLprCameraId = CursorUtil.getColumnIndexOrThrow(_cursor, "lpr_camera_id");
      final int _cursorIndexOfLprUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "lpr_user_id");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfPermit = CursorUtil.getColumnIndexOrThrow(_cursor, "permit");
      final int _cursorIndexOfPermitType = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_type");
      final int _cursorIndexOfPermitStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "permit_status");
      final int _cursorIndexOfDutyGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "duty_group");
      final int _cursorIndexOfComments = CursorUtil.getColumnIndexOrThrow(_cursor, "comments");
      final int _cursorIndexOfComments2 = CursorUtil.getColumnIndexOrThrow(_cursor, "comments2");
      final int _cursorIndexOfViolationId = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_id");
      final int _cursorIndexOfViolationDesc = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_desc");
      final int _cursorIndexOfViolationCode = CursorUtil.getColumnIndexOrThrow(_cursor, "violation_code");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
      final int _cursorIndexOfZone = CursorUtil.getColumnIndexOrThrow(_cursor, "zone");
      final int _cursorIndexOfTicketIssued = CursorUtil.getColumnIndexOrThrow(_cursor, "ticket_issues");
      final LPRNotify _result;
      if(_cursor.moveToFirst()) {
        _result = new LPRNotify();
        final String _tmpNotificationId;
        if (_cursor.isNull(_cursorIndexOfNotificationId)) {
          _tmpNotificationId = null;
        } else {
          _tmpNotificationId = _cursor.getString(_cursorIndexOfNotificationId);
        }
        _result.setNotificationId(_tmpNotificationId);
        final String _tmpPlate;
        if (_cursor.isNull(_cursorIndexOfPlate)) {
          _tmpPlate = null;
        } else {
          _tmpPlate = _cursor.getString(_cursorIndexOfPlate);
        }
        _result.setPlate(_tmpPlate);
        final String _tmpState;
        if (_cursor.isNull(_cursorIndexOfState)) {
          _tmpState = null;
        } else {
          _tmpState = _cursor.getString(_cursorIndexOfState);
        }
        _result.setState(_tmpState);
        final String _tmpMake;
        if (_cursor.isNull(_cursorIndexOfMake)) {
          _tmpMake = null;
        } else {
          _tmpMake = _cursor.getString(_cursorIndexOfMake);
        }
        _result.setMake(_tmpMake);
        final String _tmpModel;
        if (_cursor.isNull(_cursorIndexOfModel)) {
          _tmpModel = null;
        } else {
          _tmpModel = _cursor.getString(_cursorIndexOfModel);
        }
        _result.setModel(_tmpModel);
        final String _tmpBody;
        if (_cursor.isNull(_cursorIndexOfBody)) {
          _tmpBody = null;
        } else {
          _tmpBody = _cursor.getString(_cursorIndexOfBody);
        }
        _result.setBody(_tmpBody);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _result.setStatus(_tmpStatus);
        final String _tmpLocation;
        if (_cursor.isNull(_cursorIndexOfLocation)) {
          _tmpLocation = null;
        } else {
          _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        }
        _result.setLocation(_tmpLocation);
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
        final String _tmpPhoto4;
        if (_cursor.isNull(_cursorIndexOfPhoto4)) {
          _tmpPhoto4 = null;
        } else {
          _tmpPhoto4 = _cursor.getString(_cursorIndexOfPhoto4);
        }
        _result.setPhoto4(_tmpPhoto4);
        final Date _tmpNotifyDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfNotifyDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfNotifyDate);
        }
        _tmpNotifyDate = Converters.fromTimestamp(_tmp);
        _result.setNotifyDate(_tmpNotifyDate);
        final Date _tmpFirstChalkTime;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfFirstChalkTime)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfFirstChalkTime);
        }
        _tmpFirstChalkTime = Converters.fromTimestamp(_tmp_1);
        _result.setFirstChalkTime(_tmpFirstChalkTime);
        final Date _tmpLastSeen;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfLastSeen)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfLastSeen);
        }
        _tmpLastSeen = Converters.fromTimestamp(_tmp_2);
        _result.setLastSeen(_tmpLastSeen);
        final String _tmpLprScanId;
        if (_cursor.isNull(_cursorIndexOfLprScanId)) {
          _tmpLprScanId = null;
        } else {
          _tmpLprScanId = _cursor.getString(_cursorIndexOfLprScanId);
        }
        _result.setLprScanId(_tmpLprScanId);
        final String _tmpLprCameraId;
        if (_cursor.isNull(_cursorIndexOfLprCameraId)) {
          _tmpLprCameraId = null;
        } else {
          _tmpLprCameraId = _cursor.getString(_cursorIndexOfLprCameraId);
        }
        _result.setLprCameraId(_tmpLprCameraId);
        final String _tmpLprUserId;
        if (_cursor.isNull(_cursorIndexOfLprUserId)) {
          _tmpLprUserId = null;
        } else {
          _tmpLprUserId = _cursor.getString(_cursorIndexOfLprUserId);
        }
        _result.setLprUserId(_tmpLprUserId);
        final String _tmpColor;
        if (_cursor.isNull(_cursorIndexOfColor)) {
          _tmpColor = null;
        } else {
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
        }
        _result.setColor(_tmpColor);
        final String _tmpPermit;
        if (_cursor.isNull(_cursorIndexOfPermit)) {
          _tmpPermit = null;
        } else {
          _tmpPermit = _cursor.getString(_cursorIndexOfPermit);
        }
        _result.setPermit(_tmpPermit);
        final String _tmpPermitType;
        if (_cursor.isNull(_cursorIndexOfPermitType)) {
          _tmpPermitType = null;
        } else {
          _tmpPermitType = _cursor.getString(_cursorIndexOfPermitType);
        }
        _result.setPermitType(_tmpPermitType);
        final String _tmpPermitStatus;
        if (_cursor.isNull(_cursorIndexOfPermitStatus)) {
          _tmpPermitStatus = null;
        } else {
          _tmpPermitStatus = _cursor.getString(_cursorIndexOfPermitStatus);
        }
        _result.setPermitStatus(_tmpPermitStatus);
        final String _tmpDutyGroup;
        if (_cursor.isNull(_cursorIndexOfDutyGroup)) {
          _tmpDutyGroup = null;
        } else {
          _tmpDutyGroup = _cursor.getString(_cursorIndexOfDutyGroup);
        }
        _result.setDutyGroup(_tmpDutyGroup);
        final String _tmpComments;
        if (_cursor.isNull(_cursorIndexOfComments)) {
          _tmpComments = null;
        } else {
          _tmpComments = _cursor.getString(_cursorIndexOfComments);
        }
        _result.setComments(_tmpComments);
        final String _tmpComments2;
        if (_cursor.isNull(_cursorIndexOfComments2)) {
          _tmpComments2 = null;
        } else {
          _tmpComments2 = _cursor.getString(_cursorIndexOfComments2);
        }
        _result.setComments2(_tmpComments2);
        final String _tmpViolationId;
        if (_cursor.isNull(_cursorIndexOfViolationId)) {
          _tmpViolationId = null;
        } else {
          _tmpViolationId = _cursor.getString(_cursorIndexOfViolationId);
        }
        _result.setViolationId(_tmpViolationId);
        final String _tmpViolationDesc;
        if (_cursor.isNull(_cursorIndexOfViolationDesc)) {
          _tmpViolationDesc = null;
        } else {
          _tmpViolationDesc = _cursor.getString(_cursorIndexOfViolationDesc);
        }
        _result.setViolationDesc(_tmpViolationDesc);
        final String _tmpViolationCode;
        if (_cursor.isNull(_cursorIndexOfViolationCode)) {
          _tmpViolationCode = null;
        } else {
          _tmpViolationCode = _cursor.getString(_cursorIndexOfViolationCode);
        }
        _result.setViolationCode(_tmpViolationCode);
        final String _tmpDeviceId;
        if (_cursor.isNull(_cursorIndexOfDeviceId)) {
          _tmpDeviceId = null;
        } else {
          _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
        }
        _result.setDeviceId(_tmpDeviceId);
        final String _tmpZone;
        if (_cursor.isNull(_cursorIndexOfZone)) {
          _tmpZone = null;
        } else {
          _tmpZone = _cursor.getString(_cursorIndexOfZone);
        }
        _result.setZone(_tmpZone);
        final boolean _tmpTicketIssued;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfTicketIssued);
        _tmpTicketIssued = _tmp_3 != 0;
        _result.setTicketIssued(_tmpTicketIssued);
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
