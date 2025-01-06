package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.SpecialActivityPicture;
import io.reactivex.Completable;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.Void;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SpecialActivityPictureDao_Impl implements SpecialActivityPictureDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SpecialActivityPicture> __insertionAdapterOfSpecialActivityPicture;

  private final SharedSQLiteStatement __preparedStmtOfRemoveSPAPictureById;

  private final SharedSQLiteStatement __preparedStmtOfRemoveSPAPictureAll;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePicture;

  public SpecialActivityPictureDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSpecialActivityPicture = new EntityInsertionAdapter<SpecialActivityPicture>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `special_activities_pictures` (`picture_id`,`report_id`,`picture_date`,`image_path`,`image_resolution`,`image_size`,`custid`,`image_name`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SpecialActivityPicture value) {
        stmt.bindLong(1, value.getPictureId());
        stmt.bindLong(2, value.getReportId());
        if (value.getPictureDate() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPictureDate());
        }
        if (value.getImagePath() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getImagePath());
        }
        if (value.getImageResulation() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getImageResulation());
        }
        if (value.getImageSize() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getImageSize());
        }
        stmt.bindLong(7, value.getCustid());
        if (value.getImageName() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getImageName());
        }
      }
    };
    this.__preparedStmtOfRemoveSPAPictureById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from special_activities_pictures where picture_id=? ";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveSPAPictureAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from special_activities_pictures where report_id=?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdatePicture = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update special_activities_pictures set report_id=?,custid=?,image_name=?,image_path=?,image_size=?, picture_date=?,image_resolution=? where picture_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertSpecialActivityPicture(
      final SpecialActivityPicture... specialActivityPictures) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSpecialActivityPicture.insert(specialActivityPictures);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertSpecialActivityPicture(
      final SpecialActivityPicture specialActivityPicture) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSpecialActivityPicture.insert(specialActivityPicture);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable insertSpecialActivityPictureList(
      final List<SpecialActivityPicture> specialActivityPictureList) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSpecialActivityPicture.insert(specialActivityPictureList);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void removeSPAPictureById(final long chalkId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveSPAPictureById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, chalkId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveSPAPictureById.release(_stmt);
    }
  }

  @Override
  public void removeSPAPictureAll(final long chalkId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveSPAPictureAll.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, chalkId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveSPAPictureAll.release(_stmt);
    }
  }

  @Override
  public void updatePicture(final int pictureId, final int reportId, final int custId,
      final String imageName, final String imagePath, final String imageResulation,
      final String pictureDate, final String imageSize) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePicture.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, reportId);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, custId);
    _argIndex = 3;
    if (imageName == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, imageName);
    }
    _argIndex = 4;
    if (imagePath == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, imagePath);
    }
    _argIndex = 5;
    if (imageSize == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, imageSize);
    }
    _argIndex = 6;
    if (pictureDate == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, pictureDate);
    }
    _argIndex = 7;
    if (imageResulation == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, imageResulation);
    }
    _argIndex = 8;
    _stmt.bindLong(_argIndex, pictureId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdatePicture.release(_stmt);
    }
  }

  @Override
  public int getNextPrimaryId() {
    final String _sql = "select max(picture_id) as max_picture_id from special_activities_pictures";
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
  public List<SpecialActivityPicture> getListOfImage(final int report_id) {
    final String _sql = "select * from special_activities_pictures where report_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, report_id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPictureId = CursorUtil.getColumnIndexOrThrow(_cursor, "picture_id");
      final int _cursorIndexOfReportId = CursorUtil.getColumnIndexOrThrow(_cursor, "report_id");
      final int _cursorIndexOfPictureDate = CursorUtil.getColumnIndexOrThrow(_cursor, "picture_date");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "image_path");
      final int _cursorIndexOfImageResulation = CursorUtil.getColumnIndexOrThrow(_cursor, "image_resolution");
      final int _cursorIndexOfImageSize = CursorUtil.getColumnIndexOrThrow(_cursor, "image_size");
      final int _cursorIndexOfCustid = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfImageName = CursorUtil.getColumnIndexOrThrow(_cursor, "image_name");
      final List<SpecialActivityPicture> _result = new ArrayList<SpecialActivityPicture>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final SpecialActivityPicture _item;
        _item = new SpecialActivityPicture();
        final int _tmpPictureId;
        _tmpPictureId = _cursor.getInt(_cursorIndexOfPictureId);
        _item.setPictureId(_tmpPictureId);
        final int _tmpReportId;
        _tmpReportId = _cursor.getInt(_cursorIndexOfReportId);
        _item.setReportId(_tmpReportId);
        final String _tmpPictureDate;
        if (_cursor.isNull(_cursorIndexOfPictureDate)) {
          _tmpPictureDate = null;
        } else {
          _tmpPictureDate = _cursor.getString(_cursorIndexOfPictureDate);
        }
        _item.setPictureDate(_tmpPictureDate);
        final String _tmpImagePath;
        if (_cursor.isNull(_cursorIndexOfImagePath)) {
          _tmpImagePath = null;
        } else {
          _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        }
        _item.setImagePath(_tmpImagePath);
        final String _tmpImageResulation;
        if (_cursor.isNull(_cursorIndexOfImageResulation)) {
          _tmpImageResulation = null;
        } else {
          _tmpImageResulation = _cursor.getString(_cursorIndexOfImageResulation);
        }
        _item.setImageResulation(_tmpImageResulation);
        final String _tmpImageSize;
        if (_cursor.isNull(_cursorIndexOfImageSize)) {
          _tmpImageSize = null;
        } else {
          _tmpImageSize = _cursor.getString(_cursorIndexOfImageSize);
        }
        _item.setImageSize(_tmpImageSize);
        final int _tmpCustid;
        _tmpCustid = _cursor.getInt(_cursorIndexOfCustid);
        _item.setCustid(_tmpCustid);
        final String _tmpImageName;
        if (_cursor.isNull(_cursorIndexOfImageName)) {
          _tmpImageName = null;
        } else {
          _tmpImageName = _cursor.getString(_cursorIndexOfImageName);
        }
        _item.setImageName(_tmpImageName);
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
