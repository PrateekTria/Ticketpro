package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.MaintenancePicture;
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
public final class MaintenancePicturesDao_Impl implements MaintenancePicturesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MaintenancePicture> __insertionAdapterOfMaintenancePicture;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public MaintenancePicturesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMaintenancePicture = new EntityInsertionAdapter<MaintenancePicture>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `maintenance_pictures` (`picture_id`,`maintenance_id`,`image_path`,`image_size`,`image_resolution`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MaintenancePicture value) {
        stmt.bindLong(1, value.getPictureId());
        stmt.bindLong(2, value.getMaintenanceId());
        if (value.getImagePath() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getImagePath());
        }
        if (value.getImageSize() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getImageSize());
        }
        if (value.getImageResolution() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getImageResolution());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from maintenance_pictures";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from maintenance_pictures where picture_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertMaintenancePicture(final MaintenancePicture... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMaintenancePicture.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMaintenancePicture(final MaintenancePicture data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMaintenancePicture.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMaintenancePictureList(final List<MaintenancePicture> MaintenancePicturesList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMaintenancePicture.insert(MaintenancePicturesList);
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
  public List<MaintenancePicture> getPictures(final long maintenanceId) {
    final String _sql = "select * from maintenance_pictures where maintenance_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, maintenanceId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPictureId = CursorUtil.getColumnIndexOrThrow(_cursor, "picture_id");
      final int _cursorIndexOfMaintenanceId = CursorUtil.getColumnIndexOrThrow(_cursor, "maintenance_id");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "image_path");
      final int _cursorIndexOfImageSize = CursorUtil.getColumnIndexOrThrow(_cursor, "image_size");
      final int _cursorIndexOfImageResolution = CursorUtil.getColumnIndexOrThrow(_cursor, "image_resolution");
      final List<MaintenancePicture> _result = new ArrayList<MaintenancePicture>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MaintenancePicture _item;
        _item = new MaintenancePicture();
        final int _tmpPictureId;
        _tmpPictureId = _cursor.getInt(_cursorIndexOfPictureId);
        _item.setPictureId(_tmpPictureId);
        final long _tmpMaintenanceId;
        _tmpMaintenanceId = _cursor.getLong(_cursorIndexOfMaintenanceId);
        _item.setMaintenanceId(_tmpMaintenanceId);
        final String _tmpImagePath;
        if (_cursor.isNull(_cursorIndexOfImagePath)) {
          _tmpImagePath = null;
        } else {
          _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        }
        _item.setImagePath(_tmpImagePath);
        final String _tmpImageSize;
        if (_cursor.isNull(_cursorIndexOfImageSize)) {
          _tmpImageSize = null;
        } else {
          _tmpImageSize = _cursor.getString(_cursorIndexOfImageSize);
        }
        _item.setImageSize(_tmpImageSize);
        final String _tmpImageResolution;
        if (_cursor.isNull(_cursorIndexOfImageResolution)) {
          _tmpImageResolution = null;
        } else {
          _tmpImageResolution = _cursor.getString(_cursorIndexOfImageResolution);
        }
        _item.setImageResolution(_tmpImageResolution);
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
    final String _sql = "select max(picture_id) as max_picture_id from maintenance_pictures";
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
