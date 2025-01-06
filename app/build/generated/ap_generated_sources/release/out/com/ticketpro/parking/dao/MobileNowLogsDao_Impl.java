package com.ticketpro.parking.dao;

import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.MobileNowLog;
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
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MobileNowLogsDao_Impl implements MobileNowLogsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MobileNowLog> __insertionAdapterOfMobileNowLog;

  public MobileNowLogsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMobileNowLog = new EntityInsertionAdapter<MobileNowLog>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `mobile_now_logs` (`log_id`,`custid`,`userid`,`device_id`,`request_params`,`service_mode`,`response_text`,`request_date`,`plate_number`,`latitude`,`longitude`,`location`,`duty_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MobileNowLog value) {
        stmt.bindLong(1, value.getLogId());
        stmt.bindLong(2, value.getCustId());
        stmt.bindLong(3, value.getUserId());
        stmt.bindLong(4, value.getDeviceId());
        if (value.getRequestParams() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getRequestParams());
        }
        if (value.getServiceMode() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getServiceMode());
        }
        if (value.getResponseText() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getResponseText());
        }
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getRequestDate());
        if (_tmp == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindLong(8, _tmp);
        }
        if (value.getPlate_number() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getPlate_number());
        }
        if (value.getLatitude() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getLatitude());
        }
        if (value.getLongitude() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getLongitude());
        }
        if (value.getLocation() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getLocation());
        }
        if (value.getDuty_id() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getDuty_id());
        }
      }
    };
  }

  @Override
  public Completable insertMobileNowLog(final MobileNowLog... data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMobileNowLog.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable insertMobileNowLog(final MobileNowLog data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMobileNowLog.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable insertMobileNowLogList(final List<MobileNowLog> MobileNowLogsList) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMobileNowLog.insert(MobileNowLogsList);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
