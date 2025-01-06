package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.ChalkPicture;
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
public final class ChalkPicturesDao_Impl implements ChalkPicturesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ChalkPicture> __insertionAdapterOfChalkPicture;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveChalkPictureById;

  private final SharedSQLiteStatement __preparedStmtOfUpdateChalkPictureStatus;

  public ChalkPicturesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChalkPicture = new EntityInsertionAdapter<ChalkPicture>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `chalk_pictures` (`picture_id`,`chalk_id`,`chalk_time`,`location`,`latitute`,`longitude`,`image_path`,`image_resolution`,`image_size`,`sync_status`,`custid`,`download_image`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ChalkPicture value) {
        stmt.bindLong(1, value.getPictureId());
        stmt.bindLong(2, value.getChalkId());
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getChalkDate());
        if (_tmp == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, _tmp);
        }
        if (value.getLocation() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getLocation());
        }
        if (value.getLatitude() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getLatitude());
        }
        if (value.getLongitude() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getLongitude());
        }
        if (value.getImagePath() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getImagePath());
        }
        if (value.getImageResolution() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getImageResolution());
        }
        if (value.getImageSize() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getImageSize());
        }
        if (value.getSyncStatus() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getSyncStatus());
        }
        stmt.bindLong(11, value.getCustId());
        if (value.getDownloadImage() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getDownloadImage());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from chalk_pictures";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveChalkPictureById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from chalk_pictures where chalk_id=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateChalkPictureStatus = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update chalk_pictures set sync_status=? where picture_id=? AND chalk_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertChalkPicture(final ChalkPicture... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfChalkPicture.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertChalkPicture(final ChalkPicture data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChalkPicture.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertChalkPictureList(final List<ChalkPicture> ChalkPicturesList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfChalkPicture.insert(ChalkPicturesList);
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
  public void removeChalkPictureById(final long chalkId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveChalkPictureById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, chalkId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveChalkPictureById.release(_stmt);
    }
  }

  @Override
  public void updateChalkPictureStatus(final long chalkId, final String values, final int sNo) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateChalkPictureStatus.acquire();
    int _argIndex = 1;
    if (values == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, values);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, sNo);
    _argIndex = 3;
    _stmt.bindLong(_argIndex, chalkId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateChalkPictureStatus.release(_stmt);
    }
  }

  @Override
  public List<ChalkPicture> getChalkPictures(final long chalkId) {
    final String _sql = "select * from chalk_pictures where chalk_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chalkId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPictureId = CursorUtil.getColumnIndexOrThrow(_cursor, "picture_id");
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfChalkDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitute");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "image_path");
      final int _cursorIndexOfImageResolution = CursorUtil.getColumnIndexOrThrow(_cursor, "image_resolution");
      final int _cursorIndexOfImageSize = CursorUtil.getColumnIndexOrThrow(_cursor, "image_size");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfDownloadImage = CursorUtil.getColumnIndexOrThrow(_cursor, "download_image");
      final List<ChalkPicture> _result = new ArrayList<ChalkPicture>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ChalkPicture _item;
        _item = new ChalkPicture();
        final int _tmpPictureId;
        _tmpPictureId = _cursor.getInt(_cursorIndexOfPictureId);
        _item.setPictureId(_tmpPictureId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final Date _tmpChalkDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfChalkDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfChalkDate);
        }
        _tmpChalkDate = Converters.fromTimestamp(_tmp);
        _item.setChalkDate(_tmpChalkDate);
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
        final String _tmpImagePath;
        if (_cursor.isNull(_cursorIndexOfImagePath)) {
          _tmpImagePath = null;
        } else {
          _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        }
        _item.setImagePath(_tmpImagePath);
        final String _tmpImageResolution;
        if (_cursor.isNull(_cursorIndexOfImageResolution)) {
          _tmpImageResolution = null;
        } else {
          _tmpImageResolution = _cursor.getString(_cursorIndexOfImageResolution);
        }
        _item.setImageResolution(_tmpImageResolution);
        final String _tmpImageSize;
        if (_cursor.isNull(_cursorIndexOfImageSize)) {
          _tmpImageSize = null;
        } else {
          _tmpImageSize = _cursor.getString(_cursorIndexOfImageSize);
        }
        _item.setImageSize(_tmpImageSize);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _item.setSyncStatus(_tmpSyncStatus);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpDownloadImage;
        if (_cursor.isNull(_cursorIndexOfDownloadImage)) {
          _tmpDownloadImage = null;
        } else {
          _tmpDownloadImage = _cursor.getString(_cursorIndexOfDownloadImage);
        }
        _item.setDownloadImage(_tmpDownloadImage);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public ChalkPicture getChalkPictureById(final long chalkId) {
    final String _sql = "select * from chalk_pictures where chalk_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chalkId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPictureId = CursorUtil.getColumnIndexOrThrow(_cursor, "picture_id");
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfChalkDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitute");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "image_path");
      final int _cursorIndexOfImageResolution = CursorUtil.getColumnIndexOrThrow(_cursor, "image_resolution");
      final int _cursorIndexOfImageSize = CursorUtil.getColumnIndexOrThrow(_cursor, "image_size");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfDownloadImage = CursorUtil.getColumnIndexOrThrow(_cursor, "download_image");
      final ChalkPicture _result;
      if(_cursor.moveToFirst()) {
        _result = new ChalkPicture();
        final int _tmpPictureId;
        _tmpPictureId = _cursor.getInt(_cursorIndexOfPictureId);
        _result.setPictureId(_tmpPictureId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _result.setChalkId(_tmpChalkId);
        final Date _tmpChalkDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfChalkDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfChalkDate);
        }
        _tmpChalkDate = Converters.fromTimestamp(_tmp);
        _result.setChalkDate(_tmpChalkDate);
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
        final String _tmpImagePath;
        if (_cursor.isNull(_cursorIndexOfImagePath)) {
          _tmpImagePath = null;
        } else {
          _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        }
        _result.setImagePath(_tmpImagePath);
        final String _tmpImageResolution;
        if (_cursor.isNull(_cursorIndexOfImageResolution)) {
          _tmpImageResolution = null;
        } else {
          _tmpImageResolution = _cursor.getString(_cursorIndexOfImageResolution);
        }
        _result.setImageResolution(_tmpImageResolution);
        final String _tmpImageSize;
        if (_cursor.isNull(_cursorIndexOfImageSize)) {
          _tmpImageSize = null;
        } else {
          _tmpImageSize = _cursor.getString(_cursorIndexOfImageSize);
        }
        _result.setImageSize(_tmpImageSize);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _result.setSyncStatus(_tmpSyncStatus);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpDownloadImage;
        if (_cursor.isNull(_cursorIndexOfDownloadImage)) {
          _tmpDownloadImage = null;
        } else {
          _tmpDownloadImage = _cursor.getString(_cursorIndexOfDownloadImage);
        }
        _result.setDownloadImage(_tmpDownloadImage);
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
  public ChalkPicture getChalkPictureByPrimaryKey(final String pictureId) {
    final String _sql = "select * from chalk_pictures where picture_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (pictureId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, pictureId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPictureId = CursorUtil.getColumnIndexOrThrow(_cursor, "picture_id");
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfChalkDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitute");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "image_path");
      final int _cursorIndexOfImageResolution = CursorUtil.getColumnIndexOrThrow(_cursor, "image_resolution");
      final int _cursorIndexOfImageSize = CursorUtil.getColumnIndexOrThrow(_cursor, "image_size");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfDownloadImage = CursorUtil.getColumnIndexOrThrow(_cursor, "download_image");
      final ChalkPicture _result;
      if(_cursor.moveToFirst()) {
        _result = new ChalkPicture();
        final int _tmpPictureId;
        _tmpPictureId = _cursor.getInt(_cursorIndexOfPictureId);
        _result.setPictureId(_tmpPictureId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _result.setChalkId(_tmpChalkId);
        final Date _tmpChalkDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfChalkDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfChalkDate);
        }
        _tmpChalkDate = Converters.fromTimestamp(_tmp);
        _result.setChalkDate(_tmpChalkDate);
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
        final String _tmpImagePath;
        if (_cursor.isNull(_cursorIndexOfImagePath)) {
          _tmpImagePath = null;
        } else {
          _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        }
        _result.setImagePath(_tmpImagePath);
        final String _tmpImageResolution;
        if (_cursor.isNull(_cursorIndexOfImageResolution)) {
          _tmpImageResolution = null;
        } else {
          _tmpImageResolution = _cursor.getString(_cursorIndexOfImageResolution);
        }
        _result.setImageResolution(_tmpImageResolution);
        final String _tmpImageSize;
        if (_cursor.isNull(_cursorIndexOfImageSize)) {
          _tmpImageSize = null;
        } else {
          _tmpImageSize = _cursor.getString(_cursorIndexOfImageSize);
        }
        _result.setImageSize(_tmpImageSize);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _result.setSyncStatus(_tmpSyncStatus);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpDownloadImage;
        if (_cursor.isNull(_cursorIndexOfDownloadImage)) {
          _tmpDownloadImage = null;
        } else {
          _tmpDownloadImage = _cursor.getString(_cursorIndexOfDownloadImage);
        }
        _result.setDownloadImage(_tmpDownloadImage);
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
  public int getNextPrimaryId() {
    final String _sql = "select max(picture_id) as max_picture_id from chalk_pictures";
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

  @Override
  public List<ChalkPicture> getPendingChalkImageList() {
    final String _sql = "select * from chalk_pictures where sync_status='P'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPictureId = CursorUtil.getColumnIndexOrThrow(_cursor, "picture_id");
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfChalkDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitute");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "image_path");
      final int _cursorIndexOfImageResolution = CursorUtil.getColumnIndexOrThrow(_cursor, "image_resolution");
      final int _cursorIndexOfImageSize = CursorUtil.getColumnIndexOrThrow(_cursor, "image_size");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfDownloadImage = CursorUtil.getColumnIndexOrThrow(_cursor, "download_image");
      final List<ChalkPicture> _result = new ArrayList<ChalkPicture>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ChalkPicture _item;
        _item = new ChalkPicture();
        final int _tmpPictureId;
        _tmpPictureId = _cursor.getInt(_cursorIndexOfPictureId);
        _item.setPictureId(_tmpPictureId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final Date _tmpChalkDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfChalkDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfChalkDate);
        }
        _tmpChalkDate = Converters.fromTimestamp(_tmp);
        _item.setChalkDate(_tmpChalkDate);
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
        final String _tmpImagePath;
        if (_cursor.isNull(_cursorIndexOfImagePath)) {
          _tmpImagePath = null;
        } else {
          _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        }
        _item.setImagePath(_tmpImagePath);
        final String _tmpImageResolution;
        if (_cursor.isNull(_cursorIndexOfImageResolution)) {
          _tmpImageResolution = null;
        } else {
          _tmpImageResolution = _cursor.getString(_cursorIndexOfImageResolution);
        }
        _item.setImageResolution(_tmpImageResolution);
        final String _tmpImageSize;
        if (_cursor.isNull(_cursorIndexOfImageSize)) {
          _tmpImageSize = null;
        } else {
          _tmpImageSize = _cursor.getString(_cursorIndexOfImageSize);
        }
        _item.setImageSize(_tmpImageSize);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _item.setSyncStatus(_tmpSyncStatus);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpDownloadImage;
        if (_cursor.isNull(_cursorIndexOfDownloadImage)) {
          _tmpDownloadImage = null;
        } else {
          _tmpDownloadImage = _cursor.getString(_cursorIndexOfDownloadImage);
        }
        _item.setDownloadImage(_tmpDownloadImage);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ChalkPicture> getPendingChalkImageListById(final long id) {
    final String _sql = "select * from chalk_pictures where chalk_id=? AND sync_status='P'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPictureId = CursorUtil.getColumnIndexOrThrow(_cursor, "picture_id");
      final int _cursorIndexOfChalkId = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_id");
      final int _cursorIndexOfChalkDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chalk_time");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitute");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "image_path");
      final int _cursorIndexOfImageResolution = CursorUtil.getColumnIndexOrThrow(_cursor, "image_resolution");
      final int _cursorIndexOfImageSize = CursorUtil.getColumnIndexOrThrow(_cursor, "image_size");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_status");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfDownloadImage = CursorUtil.getColumnIndexOrThrow(_cursor, "download_image");
      final List<ChalkPicture> _result = new ArrayList<ChalkPicture>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ChalkPicture _item;
        _item = new ChalkPicture();
        final int _tmpPictureId;
        _tmpPictureId = _cursor.getInt(_cursorIndexOfPictureId);
        _item.setPictureId(_tmpPictureId);
        final long _tmpChalkId;
        _tmpChalkId = _cursor.getLong(_cursorIndexOfChalkId);
        _item.setChalkId(_tmpChalkId);
        final Date _tmpChalkDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfChalkDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfChalkDate);
        }
        _tmpChalkDate = Converters.fromTimestamp(_tmp);
        _item.setChalkDate(_tmpChalkDate);
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
        final String _tmpImagePath;
        if (_cursor.isNull(_cursorIndexOfImagePath)) {
          _tmpImagePath = null;
        } else {
          _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        }
        _item.setImagePath(_tmpImagePath);
        final String _tmpImageResolution;
        if (_cursor.isNull(_cursorIndexOfImageResolution)) {
          _tmpImageResolution = null;
        } else {
          _tmpImageResolution = _cursor.getString(_cursorIndexOfImageResolution);
        }
        _item.setImageResolution(_tmpImageResolution);
        final String _tmpImageSize;
        if (_cursor.isNull(_cursorIndexOfImageSize)) {
          _tmpImageSize = null;
        } else {
          _tmpImageSize = _cursor.getString(_cursorIndexOfImageSize);
        }
        _item.setImageSize(_tmpImageSize);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        _item.setSyncStatus(_tmpSyncStatus);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpDownloadImage;
        if (_cursor.isNull(_cursorIndexOfDownloadImage)) {
          _tmpDownloadImage = null;
        } else {
          _tmpDownloadImage = _cursor.getString(_cursorIndexOfDownloadImage);
        }
        _item.setDownloadImage(_tmpDownloadImage);
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
