package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.Hotlist;
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
public final class HotlistDao_Impl implements HotlistDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Hotlist> __insertionAdapterOfHotlist;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public HotlistDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHotlist = new EntityInsertionAdapter<Hotlist>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `hotlist` (`hotlist_id`,`custid`,`state_code`,`plate`,`plate_type`,`begin_date`,`end_date`,`comments`,`number_of_tickets`,`amount_owed`,`date_added`,`is_active`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Hotlist value) {
        stmt.bindLong(1, value.getHostlistId());
        stmt.bindLong(2, value.getCustId());
        if (value.getStateCode() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getStateCode());
        }
        if (value.getPlate() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getPlate());
        }
        if (value.getPlateType() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getPlateType());
        }
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getBeginDate());
        if (_tmp == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, _tmp);
        }
        final Long _tmp_1;
        _tmp_1 = Converters.dateToTimestamp(value.getEndDate());
        if (_tmp_1 == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindLong(7, _tmp_1);
        }
        if (value.getComments() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getComments());
        }
        stmt.bindLong(9, value.getNumberOfTickets());
        stmt.bindDouble(10, value.getAmountOwed());
        final Long _tmp_2;
        _tmp_2 = Converters.dateToTimestamp(value.getDateAdded());
        if (_tmp_2 == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindLong(11, _tmp_2);
        }
        if (value.getActive() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getActive());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from hotlist";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from hotlist where hotlist_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertHotlist(final Hotlist... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfHotlist.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertHotlist(final Hotlist data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHotlist.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertHotlistList(final List<Hotlist> HotlistsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfHotlist.insert(HotlistsList);
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
  public List<Hotlist> getHostlist() {
    final String _sql = "select * from hotlist";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfHostlistId = CursorUtil.getColumnIndexOrThrow(_cursor, "hotlist_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfPlateType = CursorUtil.getColumnIndexOrThrow(_cursor, "plate_type");
      final int _cursorIndexOfBeginDate = CursorUtil.getColumnIndexOrThrow(_cursor, "begin_date");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
      final int _cursorIndexOfComments = CursorUtil.getColumnIndexOrThrow(_cursor, "comments");
      final int _cursorIndexOfNumberOfTickets = CursorUtil.getColumnIndexOrThrow(_cursor, "number_of_tickets");
      final int _cursorIndexOfAmountOwed = CursorUtil.getColumnIndexOrThrow(_cursor, "amount_owed");
      final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "date_added");
      final int _cursorIndexOfActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final List<Hotlist> _result = new ArrayList<Hotlist>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Hotlist _item;
        _item = new Hotlist();
        final int _tmpHostlistId;
        _tmpHostlistId = _cursor.getInt(_cursorIndexOfHostlistId);
        _item.setHostlistId(_tmpHostlistId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
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
        final String _tmpPlateType;
        if (_cursor.isNull(_cursorIndexOfPlateType)) {
          _tmpPlateType = null;
        } else {
          _tmpPlateType = _cursor.getString(_cursorIndexOfPlateType);
        }
        _item.setPlateType(_tmpPlateType);
        final Date _tmpBeginDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfBeginDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfBeginDate);
        }
        _tmpBeginDate = Converters.fromTimestamp(_tmp);
        _item.setBeginDate(_tmpBeginDate);
        final Date _tmpEndDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfEndDate);
        }
        _tmpEndDate = Converters.fromTimestamp(_tmp_1);
        _item.setEndDate(_tmpEndDate);
        final String _tmpComments;
        if (_cursor.isNull(_cursorIndexOfComments)) {
          _tmpComments = null;
        } else {
          _tmpComments = _cursor.getString(_cursorIndexOfComments);
        }
        _item.setComments(_tmpComments);
        final int _tmpNumberOfTickets;
        _tmpNumberOfTickets = _cursor.getInt(_cursorIndexOfNumberOfTickets);
        _item.setNumberOfTickets(_tmpNumberOfTickets);
        final double _tmpAmountOwed;
        _tmpAmountOwed = _cursor.getDouble(_cursorIndexOfAmountOwed);
        _item.setAmountOwed(_tmpAmountOwed);
        final Date _tmpDateAdded;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfDateAdded)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfDateAdded);
        }
        _tmpDateAdded = Converters.fromTimestamp(_tmp_2);
        _item.setDateAdded(_tmpDateAdded);
        final String _tmpActive;
        if (_cursor.isNull(_cursorIndexOfActive)) {
          _tmpActive = null;
        } else {
          _tmpActive = _cursor.getString(_cursorIndexOfActive);
        }
        _item.setActive(_tmpActive);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Hotlist getHostlistReportByPrimaryKey(final String hotListId) {
    final String _sql = "select * from hotlist where hotlist_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (hotListId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, hotListId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfHostlistId = CursorUtil.getColumnIndexOrThrow(_cursor, "hotlist_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfPlateType = CursorUtil.getColumnIndexOrThrow(_cursor, "plate_type");
      final int _cursorIndexOfBeginDate = CursorUtil.getColumnIndexOrThrow(_cursor, "begin_date");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
      final int _cursorIndexOfComments = CursorUtil.getColumnIndexOrThrow(_cursor, "comments");
      final int _cursorIndexOfNumberOfTickets = CursorUtil.getColumnIndexOrThrow(_cursor, "number_of_tickets");
      final int _cursorIndexOfAmountOwed = CursorUtil.getColumnIndexOrThrow(_cursor, "amount_owed");
      final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "date_added");
      final int _cursorIndexOfActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final Hotlist _result;
      if(_cursor.moveToFirst()) {
        _result = new Hotlist();
        final int _tmpHostlistId;
        _tmpHostlistId = _cursor.getInt(_cursorIndexOfHostlistId);
        _result.setHostlistId(_tmpHostlistId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
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
        final String _tmpPlateType;
        if (_cursor.isNull(_cursorIndexOfPlateType)) {
          _tmpPlateType = null;
        } else {
          _tmpPlateType = _cursor.getString(_cursorIndexOfPlateType);
        }
        _result.setPlateType(_tmpPlateType);
        final Date _tmpBeginDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfBeginDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfBeginDate);
        }
        _tmpBeginDate = Converters.fromTimestamp(_tmp);
        _result.setBeginDate(_tmpBeginDate);
        final Date _tmpEndDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfEndDate);
        }
        _tmpEndDate = Converters.fromTimestamp(_tmp_1);
        _result.setEndDate(_tmpEndDate);
        final String _tmpComments;
        if (_cursor.isNull(_cursorIndexOfComments)) {
          _tmpComments = null;
        } else {
          _tmpComments = _cursor.getString(_cursorIndexOfComments);
        }
        _result.setComments(_tmpComments);
        final int _tmpNumberOfTickets;
        _tmpNumberOfTickets = _cursor.getInt(_cursorIndexOfNumberOfTickets);
        _result.setNumberOfTickets(_tmpNumberOfTickets);
        final double _tmpAmountOwed;
        _tmpAmountOwed = _cursor.getDouble(_cursorIndexOfAmountOwed);
        _result.setAmountOwed(_tmpAmountOwed);
        final Date _tmpDateAdded;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfDateAdded)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfDateAdded);
        }
        _tmpDateAdded = Converters.fromTimestamp(_tmp_2);
        _result.setDateAdded(_tmpDateAdded);
        final String _tmpActive;
        if (_cursor.isNull(_cursorIndexOfActive)) {
          _tmpActive = null;
        } else {
          _tmpActive = _cursor.getString(_cursorIndexOfActive);
        }
        _result.setActive(_tmpActive);
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
  public List<Hotlist> searchHotlist(final String plate, final String state) {
    final String _sql = "select * from hotlist where plate=? and state_code=?";
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
      final int _cursorIndexOfHostlistId = CursorUtil.getColumnIndexOrThrow(_cursor, "hotlist_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfStateCode = CursorUtil.getColumnIndexOrThrow(_cursor, "state_code");
      final int _cursorIndexOfPlate = CursorUtil.getColumnIndexOrThrow(_cursor, "plate");
      final int _cursorIndexOfPlateType = CursorUtil.getColumnIndexOrThrow(_cursor, "plate_type");
      final int _cursorIndexOfBeginDate = CursorUtil.getColumnIndexOrThrow(_cursor, "begin_date");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
      final int _cursorIndexOfComments = CursorUtil.getColumnIndexOrThrow(_cursor, "comments");
      final int _cursorIndexOfNumberOfTickets = CursorUtil.getColumnIndexOrThrow(_cursor, "number_of_tickets");
      final int _cursorIndexOfAmountOwed = CursorUtil.getColumnIndexOrThrow(_cursor, "amount_owed");
      final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "date_added");
      final int _cursorIndexOfActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final List<Hotlist> _result = new ArrayList<Hotlist>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Hotlist _item;
        _item = new Hotlist();
        final int _tmpHostlistId;
        _tmpHostlistId = _cursor.getInt(_cursorIndexOfHostlistId);
        _item.setHostlistId(_tmpHostlistId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
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
        final String _tmpPlateType;
        if (_cursor.isNull(_cursorIndexOfPlateType)) {
          _tmpPlateType = null;
        } else {
          _tmpPlateType = _cursor.getString(_cursorIndexOfPlateType);
        }
        _item.setPlateType(_tmpPlateType);
        final Date _tmpBeginDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfBeginDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfBeginDate);
        }
        _tmpBeginDate = Converters.fromTimestamp(_tmp);
        _item.setBeginDate(_tmpBeginDate);
        final Date _tmpEndDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfEndDate);
        }
        _tmpEndDate = Converters.fromTimestamp(_tmp_1);
        _item.setEndDate(_tmpEndDate);
        final String _tmpComments;
        if (_cursor.isNull(_cursorIndexOfComments)) {
          _tmpComments = null;
        } else {
          _tmpComments = _cursor.getString(_cursorIndexOfComments);
        }
        _item.setComments(_tmpComments);
        final int _tmpNumberOfTickets;
        _tmpNumberOfTickets = _cursor.getInt(_cursorIndexOfNumberOfTickets);
        _item.setNumberOfTickets(_tmpNumberOfTickets);
        final double _tmpAmountOwed;
        _tmpAmountOwed = _cursor.getDouble(_cursorIndexOfAmountOwed);
        _item.setAmountOwed(_tmpAmountOwed);
        final Date _tmpDateAdded;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfDateAdded)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfDateAdded);
        }
        _tmpDateAdded = Converters.fromTimestamp(_tmp_2);
        _item.setDateAdded(_tmpDateAdded);
        final String _tmpActive;
        if (_cursor.isNull(_cursorIndexOfActive)) {
          _tmpActive = null;
        } else {
          _tmpActive = _cursor.getString(_cursorIndexOfActive);
        }
        _item.setActive(_tmpActive);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getNextPrimaryId() {
    final String _sql = "select max(hotlist_id) as max_hotlist_id from hotlist";
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
