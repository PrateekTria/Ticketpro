package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.User;
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
public final class UsersDao_Impl implements UsersDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<User> __insertionAdapterOfUser;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public UsersDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUser = new EntityInsertionAdapter<User>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `users` (`userid`,`custid`,`username`,`password`,`user_type`,`first_name`,`last_name`,`badge`,`department`,`is_active`,`email_address`,`modules`,`title`,`print_name`,`rmsid`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, User value) {
        stmt.bindLong(1, value.getUserId());
        stmt.bindLong(2, value.getCustId());
        if (value.getUsername() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getUsername());
        }
        if (value.getPassword() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getPassword());
        }
        if (value.getUserType() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getUserType());
        }
        if (value.getFirstName() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getFirstName());
        }
        if (value.getLastName() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getLastName());
        }
        if (value.getBadge() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getBadge());
        }
        if (value.getDepartment() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getDepartment());
        }
        if (value.getIsAtive() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getIsAtive());
        }
        if (value.getEmailAddress() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getEmailAddress());
        }
        if (value.getModules() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getModules());
        }
        if (value.getTitle() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getTitle());
        }
        if (value.getPrint_name() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getPrint_name());
        }
        if (value.getRmsid() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getRmsid());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from users";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from users where userid=?";
        return _query;
      }
    };
  }

  @Override
  public void insertUsers(final User... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfUser.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Completable insertUsers(final User data) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUser.insert(data);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable insertUsersList(final List<User> UsersList) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUser.insert(UsersList);
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
  public List<User> getAllUsers() {
    final String _sql = "select * from users order by username";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfUserType = CursorUtil.getColumnIndexOrThrow(_cursor, "user_type");
      final int _cursorIndexOfFirstName = CursorUtil.getColumnIndexOrThrow(_cursor, "first_name");
      final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "last_name");
      final int _cursorIndexOfBadge = CursorUtil.getColumnIndexOrThrow(_cursor, "badge");
      final int _cursorIndexOfDepartment = CursorUtil.getColumnIndexOrThrow(_cursor, "department");
      final int _cursorIndexOfIsAtive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final int _cursorIndexOfEmailAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "email_address");
      final int _cursorIndexOfModules = CursorUtil.getColumnIndexOrThrow(_cursor, "modules");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfPrintName = CursorUtil.getColumnIndexOrThrow(_cursor, "print_name");
      final int _cursorIndexOfRmsid = CursorUtil.getColumnIndexOrThrow(_cursor, "rmsid");
      final List<User> _result = new ArrayList<User>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final User _item;
        _item = new User();
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpUsername;
        if (_cursor.isNull(_cursorIndexOfUsername)) {
          _tmpUsername = null;
        } else {
          _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        }
        _item.setUsername(_tmpUsername);
        final String _tmpPassword;
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _tmpPassword = null;
        } else {
          _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        }
        _item.setPassword(_tmpPassword);
        final String _tmpUserType;
        if (_cursor.isNull(_cursorIndexOfUserType)) {
          _tmpUserType = null;
        } else {
          _tmpUserType = _cursor.getString(_cursorIndexOfUserType);
        }
        _item.setUserType(_tmpUserType);
        final String _tmpFirstName;
        if (_cursor.isNull(_cursorIndexOfFirstName)) {
          _tmpFirstName = null;
        } else {
          _tmpFirstName = _cursor.getString(_cursorIndexOfFirstName);
        }
        _item.setFirstName(_tmpFirstName);
        final String _tmpLastName;
        if (_cursor.isNull(_cursorIndexOfLastName)) {
          _tmpLastName = null;
        } else {
          _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
        }
        _item.setLastName(_tmpLastName);
        final String _tmpBadge;
        if (_cursor.isNull(_cursorIndexOfBadge)) {
          _tmpBadge = null;
        } else {
          _tmpBadge = _cursor.getString(_cursorIndexOfBadge);
        }
        _item.setBadge(_tmpBadge);
        final String _tmpDepartment;
        if (_cursor.isNull(_cursorIndexOfDepartment)) {
          _tmpDepartment = null;
        } else {
          _tmpDepartment = _cursor.getString(_cursorIndexOfDepartment);
        }
        _item.setDepartment(_tmpDepartment);
        final String _tmpIsAtive;
        if (_cursor.isNull(_cursorIndexOfIsAtive)) {
          _tmpIsAtive = null;
        } else {
          _tmpIsAtive = _cursor.getString(_cursorIndexOfIsAtive);
        }
        _item.setIsAtive(_tmpIsAtive);
        final String _tmpEmailAddress;
        if (_cursor.isNull(_cursorIndexOfEmailAddress)) {
          _tmpEmailAddress = null;
        } else {
          _tmpEmailAddress = _cursor.getString(_cursorIndexOfEmailAddress);
        }
        _item.setEmailAddress(_tmpEmailAddress);
        final String _tmpModules;
        if (_cursor.isNull(_cursorIndexOfModules)) {
          _tmpModules = null;
        } else {
          _tmpModules = _cursor.getString(_cursorIndexOfModules);
        }
        _item.setModules(_tmpModules);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpPrint_name;
        if (_cursor.isNull(_cursorIndexOfPrintName)) {
          _tmpPrint_name = null;
        } else {
          _tmpPrint_name = _cursor.getString(_cursorIndexOfPrintName);
        }
        _item.setPrint_name(_tmpPrint_name);
        final String _tmpRmsid;
        if (_cursor.isNull(_cursorIndexOfRmsid)) {
          _tmpRmsid = null;
        } else {
          _tmpRmsid = _cursor.getString(_cursorIndexOfRmsid);
        }
        _item.setRmsid(_tmpRmsid);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<User> getUsers(final String moduleName) {
    final String _sql = "select * from users where UPPER(modules) like '%' ||? || '%' order by username";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (moduleName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, moduleName);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfUserType = CursorUtil.getColumnIndexOrThrow(_cursor, "user_type");
      final int _cursorIndexOfFirstName = CursorUtil.getColumnIndexOrThrow(_cursor, "first_name");
      final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "last_name");
      final int _cursorIndexOfBadge = CursorUtil.getColumnIndexOrThrow(_cursor, "badge");
      final int _cursorIndexOfDepartment = CursorUtil.getColumnIndexOrThrow(_cursor, "department");
      final int _cursorIndexOfIsAtive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final int _cursorIndexOfEmailAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "email_address");
      final int _cursorIndexOfModules = CursorUtil.getColumnIndexOrThrow(_cursor, "modules");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfPrintName = CursorUtil.getColumnIndexOrThrow(_cursor, "print_name");
      final int _cursorIndexOfRmsid = CursorUtil.getColumnIndexOrThrow(_cursor, "rmsid");
      final List<User> _result = new ArrayList<User>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final User _item;
        _item = new User();
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpUsername;
        if (_cursor.isNull(_cursorIndexOfUsername)) {
          _tmpUsername = null;
        } else {
          _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        }
        _item.setUsername(_tmpUsername);
        final String _tmpPassword;
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _tmpPassword = null;
        } else {
          _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        }
        _item.setPassword(_tmpPassword);
        final String _tmpUserType;
        if (_cursor.isNull(_cursorIndexOfUserType)) {
          _tmpUserType = null;
        } else {
          _tmpUserType = _cursor.getString(_cursorIndexOfUserType);
        }
        _item.setUserType(_tmpUserType);
        final String _tmpFirstName;
        if (_cursor.isNull(_cursorIndexOfFirstName)) {
          _tmpFirstName = null;
        } else {
          _tmpFirstName = _cursor.getString(_cursorIndexOfFirstName);
        }
        _item.setFirstName(_tmpFirstName);
        final String _tmpLastName;
        if (_cursor.isNull(_cursorIndexOfLastName)) {
          _tmpLastName = null;
        } else {
          _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
        }
        _item.setLastName(_tmpLastName);
        final String _tmpBadge;
        if (_cursor.isNull(_cursorIndexOfBadge)) {
          _tmpBadge = null;
        } else {
          _tmpBadge = _cursor.getString(_cursorIndexOfBadge);
        }
        _item.setBadge(_tmpBadge);
        final String _tmpDepartment;
        if (_cursor.isNull(_cursorIndexOfDepartment)) {
          _tmpDepartment = null;
        } else {
          _tmpDepartment = _cursor.getString(_cursorIndexOfDepartment);
        }
        _item.setDepartment(_tmpDepartment);
        final String _tmpIsAtive;
        if (_cursor.isNull(_cursorIndexOfIsAtive)) {
          _tmpIsAtive = null;
        } else {
          _tmpIsAtive = _cursor.getString(_cursorIndexOfIsAtive);
        }
        _item.setIsAtive(_tmpIsAtive);
        final String _tmpEmailAddress;
        if (_cursor.isNull(_cursorIndexOfEmailAddress)) {
          _tmpEmailAddress = null;
        } else {
          _tmpEmailAddress = _cursor.getString(_cursorIndexOfEmailAddress);
        }
        _item.setEmailAddress(_tmpEmailAddress);
        final String _tmpModules;
        if (_cursor.isNull(_cursorIndexOfModules)) {
          _tmpModules = null;
        } else {
          _tmpModules = _cursor.getString(_cursorIndexOfModules);
        }
        _item.setModules(_tmpModules);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpPrint_name;
        if (_cursor.isNull(_cursorIndexOfPrintName)) {
          _tmpPrint_name = null;
        } else {
          _tmpPrint_name = _cursor.getString(_cursorIndexOfPrintName);
        }
        _item.setPrint_name(_tmpPrint_name);
        final String _tmpRmsid;
        if (_cursor.isNull(_cursorIndexOfRmsid)) {
          _tmpRmsid = null;
        } else {
          _tmpRmsid = _cursor.getString(_cursorIndexOfRmsid);
        }
        _item.setRmsid(_tmpRmsid);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public User getUserInfo(final int userId) {
    final String _sql = "select * from users where userid=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfUserType = CursorUtil.getColumnIndexOrThrow(_cursor, "user_type");
      final int _cursorIndexOfFirstName = CursorUtil.getColumnIndexOrThrow(_cursor, "first_name");
      final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "last_name");
      final int _cursorIndexOfBadge = CursorUtil.getColumnIndexOrThrow(_cursor, "badge");
      final int _cursorIndexOfDepartment = CursorUtil.getColumnIndexOrThrow(_cursor, "department");
      final int _cursorIndexOfIsAtive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final int _cursorIndexOfEmailAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "email_address");
      final int _cursorIndexOfModules = CursorUtil.getColumnIndexOrThrow(_cursor, "modules");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfPrintName = CursorUtil.getColumnIndexOrThrow(_cursor, "print_name");
      final int _cursorIndexOfRmsid = CursorUtil.getColumnIndexOrThrow(_cursor, "rmsid");
      final User _result;
      if(_cursor.moveToFirst()) {
        _result = new User();
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpUsername;
        if (_cursor.isNull(_cursorIndexOfUsername)) {
          _tmpUsername = null;
        } else {
          _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        }
        _result.setUsername(_tmpUsername);
        final String _tmpPassword;
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _tmpPassword = null;
        } else {
          _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        }
        _result.setPassword(_tmpPassword);
        final String _tmpUserType;
        if (_cursor.isNull(_cursorIndexOfUserType)) {
          _tmpUserType = null;
        } else {
          _tmpUserType = _cursor.getString(_cursorIndexOfUserType);
        }
        _result.setUserType(_tmpUserType);
        final String _tmpFirstName;
        if (_cursor.isNull(_cursorIndexOfFirstName)) {
          _tmpFirstName = null;
        } else {
          _tmpFirstName = _cursor.getString(_cursorIndexOfFirstName);
        }
        _result.setFirstName(_tmpFirstName);
        final String _tmpLastName;
        if (_cursor.isNull(_cursorIndexOfLastName)) {
          _tmpLastName = null;
        } else {
          _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
        }
        _result.setLastName(_tmpLastName);
        final String _tmpBadge;
        if (_cursor.isNull(_cursorIndexOfBadge)) {
          _tmpBadge = null;
        } else {
          _tmpBadge = _cursor.getString(_cursorIndexOfBadge);
        }
        _result.setBadge(_tmpBadge);
        final String _tmpDepartment;
        if (_cursor.isNull(_cursorIndexOfDepartment)) {
          _tmpDepartment = null;
        } else {
          _tmpDepartment = _cursor.getString(_cursorIndexOfDepartment);
        }
        _result.setDepartment(_tmpDepartment);
        final String _tmpIsAtive;
        if (_cursor.isNull(_cursorIndexOfIsAtive)) {
          _tmpIsAtive = null;
        } else {
          _tmpIsAtive = _cursor.getString(_cursorIndexOfIsAtive);
        }
        _result.setIsAtive(_tmpIsAtive);
        final String _tmpEmailAddress;
        if (_cursor.isNull(_cursorIndexOfEmailAddress)) {
          _tmpEmailAddress = null;
        } else {
          _tmpEmailAddress = _cursor.getString(_cursorIndexOfEmailAddress);
        }
        _result.setEmailAddress(_tmpEmailAddress);
        final String _tmpModules;
        if (_cursor.isNull(_cursorIndexOfModules)) {
          _tmpModules = null;
        } else {
          _tmpModules = _cursor.getString(_cursorIndexOfModules);
        }
        _result.setModules(_tmpModules);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result.setTitle(_tmpTitle);
        final String _tmpPrint_name;
        if (_cursor.isNull(_cursorIndexOfPrintName)) {
          _tmpPrint_name = null;
        } else {
          _tmpPrint_name = _cursor.getString(_cursorIndexOfPrintName);
        }
        _result.setPrint_name(_tmpPrint_name);
        final String _tmpRmsid;
        if (_cursor.isNull(_cursorIndexOfRmsid)) {
          _tmpRmsid = null;
        } else {
          _tmpRmsid = _cursor.getString(_cursorIndexOfRmsid);
        }
        _result.setRmsid(_tmpRmsid);
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
  public int getUserId(final String password) {
    final String _sql = "select userid from users where password =?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (password == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, password);
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

  @Override
  public User getUserByRmsidInfo(final String rmsid) {
    final String _sql = "select * from users where rmsid=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (rmsid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, rmsid);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userid");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfUserType = CursorUtil.getColumnIndexOrThrow(_cursor, "user_type");
      final int _cursorIndexOfFirstName = CursorUtil.getColumnIndexOrThrow(_cursor, "first_name");
      final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "last_name");
      final int _cursorIndexOfBadge = CursorUtil.getColumnIndexOrThrow(_cursor, "badge");
      final int _cursorIndexOfDepartment = CursorUtil.getColumnIndexOrThrow(_cursor, "department");
      final int _cursorIndexOfIsAtive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
      final int _cursorIndexOfEmailAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "email_address");
      final int _cursorIndexOfModules = CursorUtil.getColumnIndexOrThrow(_cursor, "modules");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfPrintName = CursorUtil.getColumnIndexOrThrow(_cursor, "print_name");
      final int _cursorIndexOfRmsid = CursorUtil.getColumnIndexOrThrow(_cursor, "rmsid");
      final User _result;
      if(_cursor.moveToFirst()) {
        _result = new User();
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpUsername;
        if (_cursor.isNull(_cursorIndexOfUsername)) {
          _tmpUsername = null;
        } else {
          _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        }
        _result.setUsername(_tmpUsername);
        final String _tmpPassword;
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _tmpPassword = null;
        } else {
          _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        }
        _result.setPassword(_tmpPassword);
        final String _tmpUserType;
        if (_cursor.isNull(_cursorIndexOfUserType)) {
          _tmpUserType = null;
        } else {
          _tmpUserType = _cursor.getString(_cursorIndexOfUserType);
        }
        _result.setUserType(_tmpUserType);
        final String _tmpFirstName;
        if (_cursor.isNull(_cursorIndexOfFirstName)) {
          _tmpFirstName = null;
        } else {
          _tmpFirstName = _cursor.getString(_cursorIndexOfFirstName);
        }
        _result.setFirstName(_tmpFirstName);
        final String _tmpLastName;
        if (_cursor.isNull(_cursorIndexOfLastName)) {
          _tmpLastName = null;
        } else {
          _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
        }
        _result.setLastName(_tmpLastName);
        final String _tmpBadge;
        if (_cursor.isNull(_cursorIndexOfBadge)) {
          _tmpBadge = null;
        } else {
          _tmpBadge = _cursor.getString(_cursorIndexOfBadge);
        }
        _result.setBadge(_tmpBadge);
        final String _tmpDepartment;
        if (_cursor.isNull(_cursorIndexOfDepartment)) {
          _tmpDepartment = null;
        } else {
          _tmpDepartment = _cursor.getString(_cursorIndexOfDepartment);
        }
        _result.setDepartment(_tmpDepartment);
        final String _tmpIsAtive;
        if (_cursor.isNull(_cursorIndexOfIsAtive)) {
          _tmpIsAtive = null;
        } else {
          _tmpIsAtive = _cursor.getString(_cursorIndexOfIsAtive);
        }
        _result.setIsAtive(_tmpIsAtive);
        final String _tmpEmailAddress;
        if (_cursor.isNull(_cursorIndexOfEmailAddress)) {
          _tmpEmailAddress = null;
        } else {
          _tmpEmailAddress = _cursor.getString(_cursorIndexOfEmailAddress);
        }
        _result.setEmailAddress(_tmpEmailAddress);
        final String _tmpModules;
        if (_cursor.isNull(_cursorIndexOfModules)) {
          _tmpModules = null;
        } else {
          _tmpModules = _cursor.getString(_cursorIndexOfModules);
        }
        _result.setModules(_tmpModules);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result.setTitle(_tmpTitle);
        final String _tmpPrint_name;
        if (_cursor.isNull(_cursorIndexOfPrintName)) {
          _tmpPrint_name = null;
        } else {
          _tmpPrint_name = _cursor.getString(_cursorIndexOfPrintName);
        }
        _result.setPrint_name(_tmpPrint_name);
        final String _tmpRmsid;
        if (_cursor.isNull(_cursorIndexOfRmsid)) {
          _tmpRmsid = null;
        } else {
          _tmpRmsid = _cursor.getString(_cursorIndexOfRmsid);
        }
        _result.setRmsid(_tmpRmsid);
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
