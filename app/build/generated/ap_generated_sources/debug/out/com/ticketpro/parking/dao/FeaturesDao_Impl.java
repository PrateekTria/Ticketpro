package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EmptyResultSetException;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.RxRoom;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.Feature;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.lang.Void;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FeaturesDao_Impl implements FeaturesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Feature> __insertionAdapterOfFeature;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public FeaturesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFeature = new EntityInsertionAdapter<Feature>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `features` (`feature_id`,`custid`,`feature`,`admin`,`officer`,`value`,`order_number`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Feature value) {
        stmt.bindLong(1, value.getFeatureId());
        stmt.bindLong(2, value.getCustId());
        if (value.getFeature() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getFeature());
        }
        if (value.getAllowedAdmin() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getAllowedAdmin());
        }
        if (value.getAllowedOfficer() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getAllowedOfficer());
        }
        if (value.getValue() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getValue());
        }
        stmt.bindLong(7, value.getOrderNumber());
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from features";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from features where feature_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertFeature(final Feature... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFeature.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertFeature(final Feature data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFeature.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public void insertFeatureList(final List<Feature> FeaturesList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFeature.insert(FeaturesList);
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
  public Maybe<List<Feature>> getFeatures() {
    final String _sql = "select * from features order by order_number";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return Maybe.fromCallable(new Callable<List<Feature>>() {
      @Override
      public List<Feature> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFeatureId = CursorUtil.getColumnIndexOrThrow(_cursor, "feature_id");
          final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
          final int _cursorIndexOfFeature = CursorUtil.getColumnIndexOrThrow(_cursor, "feature");
          final int _cursorIndexOfAllowedAdmin = CursorUtil.getColumnIndexOrThrow(_cursor, "admin");
          final int _cursorIndexOfAllowedOfficer = CursorUtil.getColumnIndexOrThrow(_cursor, "officer");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
          final List<Feature> _result = new ArrayList<Feature>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Feature _item;
            _item = new Feature();
            final int _tmpFeatureId;
            _tmpFeatureId = _cursor.getInt(_cursorIndexOfFeatureId);
            _item.setFeatureId(_tmpFeatureId);
            final int _tmpCustId;
            _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
            _item.setCustId(_tmpCustId);
            final String _tmpFeature;
            if (_cursor.isNull(_cursorIndexOfFeature)) {
              _tmpFeature = null;
            } else {
              _tmpFeature = _cursor.getString(_cursorIndexOfFeature);
            }
            _item.setFeature(_tmpFeature);
            final String _tmpAllowedAdmin;
            if (_cursor.isNull(_cursorIndexOfAllowedAdmin)) {
              _tmpAllowedAdmin = null;
            } else {
              _tmpAllowedAdmin = _cursor.getString(_cursorIndexOfAllowedAdmin);
            }
            _item.setAllowedAdmin(_tmpAllowedAdmin);
            final String _tmpAllowedOfficer;
            if (_cursor.isNull(_cursorIndexOfAllowedOfficer)) {
              _tmpAllowedOfficer = null;
            } else {
              _tmpAllowedOfficer = _cursor.getString(_cursorIndexOfAllowedOfficer);
            }
            _item.setAllowedOfficer(_tmpAllowedOfficer);
            final String _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getString(_cursorIndexOfValue);
            }
            _item.setValue(_tmpValue);
            final int _tmpOrderNumber;
            _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
            _item.setOrderNumber(_tmpOrderNumber);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Maybe<List<Feature>> getFeatures(final List<String> features) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("select * from features where UPPER(feature) in (");
    final int _inputSize = features.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") order by order_number");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : features) {
      if (_item == null) {
        _statement.bindNull(_argIndex);
      } else {
        _statement.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    return Maybe.fromCallable(new Callable<List<Feature>>() {
      @Override
      public List<Feature> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFeatureId = CursorUtil.getColumnIndexOrThrow(_cursor, "feature_id");
          final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
          final int _cursorIndexOfFeature = CursorUtil.getColumnIndexOrThrow(_cursor, "feature");
          final int _cursorIndexOfAllowedAdmin = CursorUtil.getColumnIndexOrThrow(_cursor, "admin");
          final int _cursorIndexOfAllowedOfficer = CursorUtil.getColumnIndexOrThrow(_cursor, "officer");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
          final List<Feature> _result = new ArrayList<Feature>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Feature _item_1;
            _item_1 = new Feature();
            final int _tmpFeatureId;
            _tmpFeatureId = _cursor.getInt(_cursorIndexOfFeatureId);
            _item_1.setFeatureId(_tmpFeatureId);
            final int _tmpCustId;
            _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
            _item_1.setCustId(_tmpCustId);
            final String _tmpFeature;
            if (_cursor.isNull(_cursorIndexOfFeature)) {
              _tmpFeature = null;
            } else {
              _tmpFeature = _cursor.getString(_cursorIndexOfFeature);
            }
            _item_1.setFeature(_tmpFeature);
            final String _tmpAllowedAdmin;
            if (_cursor.isNull(_cursorIndexOfAllowedAdmin)) {
              _tmpAllowedAdmin = null;
            } else {
              _tmpAllowedAdmin = _cursor.getString(_cursorIndexOfAllowedAdmin);
            }
            _item_1.setAllowedAdmin(_tmpAllowedAdmin);
            final String _tmpAllowedOfficer;
            if (_cursor.isNull(_cursorIndexOfAllowedOfficer)) {
              _tmpAllowedOfficer = null;
            } else {
              _tmpAllowedOfficer = _cursor.getString(_cursorIndexOfAllowedOfficer);
            }
            _item_1.setAllowedOfficer(_tmpAllowedOfficer);
            final String _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getString(_cursorIndexOfValue);
            }
            _item_1.setValue(_tmpValue);
            final int _tmpOrderNumber;
            _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
            _item_1.setOrderNumber(_tmpOrderNumber);
            _result.add(_item_1);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Maybe<List<Feature>> getFeaturesList(final int custId) {
    final String _sql = "select * from features where custid=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, custId);
    return Maybe.fromCallable(new Callable<List<Feature>>() {
      @Override
      public List<Feature> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFeatureId = CursorUtil.getColumnIndexOrThrow(_cursor, "feature_id");
          final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
          final int _cursorIndexOfFeature = CursorUtil.getColumnIndexOrThrow(_cursor, "feature");
          final int _cursorIndexOfAllowedAdmin = CursorUtil.getColumnIndexOrThrow(_cursor, "admin");
          final int _cursorIndexOfAllowedOfficer = CursorUtil.getColumnIndexOrThrow(_cursor, "officer");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
          final List<Feature> _result = new ArrayList<Feature>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Feature _item;
            _item = new Feature();
            final int _tmpFeatureId;
            _tmpFeatureId = _cursor.getInt(_cursorIndexOfFeatureId);
            _item.setFeatureId(_tmpFeatureId);
            final int _tmpCustId;
            _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
            _item.setCustId(_tmpCustId);
            final String _tmpFeature;
            if (_cursor.isNull(_cursorIndexOfFeature)) {
              _tmpFeature = null;
            } else {
              _tmpFeature = _cursor.getString(_cursorIndexOfFeature);
            }
            _item.setFeature(_tmpFeature);
            final String _tmpAllowedAdmin;
            if (_cursor.isNull(_cursorIndexOfAllowedAdmin)) {
              _tmpAllowedAdmin = null;
            } else {
              _tmpAllowedAdmin = _cursor.getString(_cursorIndexOfAllowedAdmin);
            }
            _item.setAllowedAdmin(_tmpAllowedAdmin);
            final String _tmpAllowedOfficer;
            if (_cursor.isNull(_cursorIndexOfAllowedOfficer)) {
              _tmpAllowedOfficer = null;
            } else {
              _tmpAllowedOfficer = _cursor.getString(_cursorIndexOfAllowedOfficer);
            }
            _item.setAllowedOfficer(_tmpAllowedOfficer);
            final String _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getString(_cursorIndexOfValue);
            }
            _item.setValue(_tmpValue);
            final int _tmpOrderNumber;
            _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
            _item.setOrderNumber(_tmpOrderNumber);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Maybe<List<Feature>> getFeature(final String featureName) {
    final String _sql = "select * from features where UPPER(feature)=? order by order_number";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (featureName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, featureName);
    }
    return Maybe.fromCallable(new Callable<List<Feature>>() {
      @Override
      public List<Feature> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFeatureId = CursorUtil.getColumnIndexOrThrow(_cursor, "feature_id");
          final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
          final int _cursorIndexOfFeature = CursorUtil.getColumnIndexOrThrow(_cursor, "feature");
          final int _cursorIndexOfAllowedAdmin = CursorUtil.getColumnIndexOrThrow(_cursor, "admin");
          final int _cursorIndexOfAllowedOfficer = CursorUtil.getColumnIndexOrThrow(_cursor, "officer");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfOrderNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "order_number");
          final List<Feature> _result = new ArrayList<Feature>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Feature _item;
            _item = new Feature();
            final int _tmpFeatureId;
            _tmpFeatureId = _cursor.getInt(_cursorIndexOfFeatureId);
            _item.setFeatureId(_tmpFeatureId);
            final int _tmpCustId;
            _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
            _item.setCustId(_tmpCustId);
            final String _tmpFeature;
            if (_cursor.isNull(_cursorIndexOfFeature)) {
              _tmpFeature = null;
            } else {
              _tmpFeature = _cursor.getString(_cursorIndexOfFeature);
            }
            _item.setFeature(_tmpFeature);
            final String _tmpAllowedAdmin;
            if (_cursor.isNull(_cursorIndexOfAllowedAdmin)) {
              _tmpAllowedAdmin = null;
            } else {
              _tmpAllowedAdmin = _cursor.getString(_cursorIndexOfAllowedAdmin);
            }
            _item.setAllowedAdmin(_tmpAllowedAdmin);
            final String _tmpAllowedOfficer;
            if (_cursor.isNull(_cursorIndexOfAllowedOfficer)) {
              _tmpAllowedOfficer = null;
            } else {
              _tmpAllowedOfficer = _cursor.getString(_cursorIndexOfAllowedOfficer);
            }
            _item.setAllowedOfficer(_tmpAllowedOfficer);
            final String _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getString(_cursorIndexOfValue);
            }
            _item.setValue(_tmpValue);
            final int _tmpOrderNumber;
            _tmpOrderNumber = _cursor.getInt(_cursorIndexOfOrderNumber);
            _item.setOrderNumber(_tmpOrderNumber);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Single<List<String>> getFeatureValue(final String featureName) {
    final String _sql = "select value from features where UPPER(feature)=? order by order_number";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (featureName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, featureName);
    }
    return RxRoom.createSingle(new Callable<List<String>>() {
      @Override
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final String _item;
            if (_cursor.isNull(0)) {
              _item = null;
            } else {
              _item = _cursor.getString(0);
            }
            _result.add(_item);
          }
          if(_result == null) {
            throw new EmptyResultSetException("Query returned empty result set: " + _statement.getSql());
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
