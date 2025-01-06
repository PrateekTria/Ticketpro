package com.ticketpro.parking.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.PrintTemplate;
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
public final class PrintTemplatesDao_Impl implements PrintTemplatesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PrintTemplate> __insertionAdapterOfPrintTemplate;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAll;

  private final SharedSQLiteStatement __preparedStmtOfRemoveById;

  public PrintTemplatesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPrintTemplate = new EntityInsertionAdapter<PrintTemplate>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `print_templates` (`template_id`,`custid`,`printer_name`,`printer_type`,`display_name`,`template_name`,`template_data`,`is_report`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, PrintTemplate value) {
        stmt.bindLong(1, value.getTemplateId());
        stmt.bindLong(2, value.getCustId());
        if (value.getPrinterName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPrinterName());
        }
        if (value.getPrinterType() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getPrinterType());
        }
        if (value.getDisplayName() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getDisplayName());
        }
        if (value.getTemplateName() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getTemplateName());
        }
        if (value.getTemplateData() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getTemplateData());
        }
        if (value.getIsReport() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getIsReport());
        }
      }
    };
    this.__preparedStmtOfRemoveAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from print_templates";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from print_templates where template_id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertPrintTemplate(final PrintTemplate... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPrintTemplate.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertPrintTemplate(final PrintTemplate data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPrintTemplate.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertPrintTemplateList(final List<PrintTemplate> PrintTemplatesList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPrintTemplate.insert(PrintTemplatesList);
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
  public List<PrintTemplate> getPrintTemplates() {
    final String _sql = "select * from print_templates";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "template_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfPrinterName = CursorUtil.getColumnIndexOrThrow(_cursor, "printer_name");
      final int _cursorIndexOfPrinterType = CursorUtil.getColumnIndexOrThrow(_cursor, "printer_type");
      final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "display_name");
      final int _cursorIndexOfTemplateName = CursorUtil.getColumnIndexOrThrow(_cursor, "template_name");
      final int _cursorIndexOfTemplateData = CursorUtil.getColumnIndexOrThrow(_cursor, "template_data");
      final int _cursorIndexOfIsReport = CursorUtil.getColumnIndexOrThrow(_cursor, "is_report");
      final List<PrintTemplate> _result = new ArrayList<PrintTemplate>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final PrintTemplate _item;
        _item = new PrintTemplate();
        final int _tmpTemplateId;
        _tmpTemplateId = _cursor.getInt(_cursorIndexOfTemplateId);
        _item.setTemplateId(_tmpTemplateId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpPrinterName;
        if (_cursor.isNull(_cursorIndexOfPrinterName)) {
          _tmpPrinterName = null;
        } else {
          _tmpPrinterName = _cursor.getString(_cursorIndexOfPrinterName);
        }
        _item.setPrinterName(_tmpPrinterName);
        final String _tmpPrinterType;
        if (_cursor.isNull(_cursorIndexOfPrinterType)) {
          _tmpPrinterType = null;
        } else {
          _tmpPrinterType = _cursor.getString(_cursorIndexOfPrinterType);
        }
        _item.setPrinterType(_tmpPrinterType);
        final String _tmpDisplayName;
        if (_cursor.isNull(_cursorIndexOfDisplayName)) {
          _tmpDisplayName = null;
        } else {
          _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
        }
        _item.setDisplayName(_tmpDisplayName);
        final String _tmpTemplateName;
        if (_cursor.isNull(_cursorIndexOfTemplateName)) {
          _tmpTemplateName = null;
        } else {
          _tmpTemplateName = _cursor.getString(_cursorIndexOfTemplateName);
        }
        _item.setTemplateName(_tmpTemplateName);
        final String _tmpTemplateData;
        if (_cursor.isNull(_cursorIndexOfTemplateData)) {
          _tmpTemplateData = null;
        } else {
          _tmpTemplateData = _cursor.getString(_cursorIndexOfTemplateData);
        }
        _item.setTemplateData(_tmpTemplateData);
        final String _tmpIsReport;
        if (_cursor.isNull(_cursorIndexOfIsReport)) {
          _tmpIsReport = null;
        } else {
          _tmpIsReport = _cursor.getString(_cursorIndexOfIsReport);
        }
        _item.setIsReport(_tmpIsReport);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public PrintTemplate getPrintTemplateById(final int templateId) {
    final String _sql = "select * from print_templates where template_id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, templateId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "template_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfPrinterName = CursorUtil.getColumnIndexOrThrow(_cursor, "printer_name");
      final int _cursorIndexOfPrinterType = CursorUtil.getColumnIndexOrThrow(_cursor, "printer_type");
      final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "display_name");
      final int _cursorIndexOfTemplateName = CursorUtil.getColumnIndexOrThrow(_cursor, "template_name");
      final int _cursorIndexOfTemplateData = CursorUtil.getColumnIndexOrThrow(_cursor, "template_data");
      final int _cursorIndexOfIsReport = CursorUtil.getColumnIndexOrThrow(_cursor, "is_report");
      final PrintTemplate _result;
      if(_cursor.moveToFirst()) {
        _result = new PrintTemplate();
        final int _tmpTemplateId;
        _tmpTemplateId = _cursor.getInt(_cursorIndexOfTemplateId);
        _result.setTemplateId(_tmpTemplateId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpPrinterName;
        if (_cursor.isNull(_cursorIndexOfPrinterName)) {
          _tmpPrinterName = null;
        } else {
          _tmpPrinterName = _cursor.getString(_cursorIndexOfPrinterName);
        }
        _result.setPrinterName(_tmpPrinterName);
        final String _tmpPrinterType;
        if (_cursor.isNull(_cursorIndexOfPrinterType)) {
          _tmpPrinterType = null;
        } else {
          _tmpPrinterType = _cursor.getString(_cursorIndexOfPrinterType);
        }
        _result.setPrinterType(_tmpPrinterType);
        final String _tmpDisplayName;
        if (_cursor.isNull(_cursorIndexOfDisplayName)) {
          _tmpDisplayName = null;
        } else {
          _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
        }
        _result.setDisplayName(_tmpDisplayName);
        final String _tmpTemplateName;
        if (_cursor.isNull(_cursorIndexOfTemplateName)) {
          _tmpTemplateName = null;
        } else {
          _tmpTemplateName = _cursor.getString(_cursorIndexOfTemplateName);
        }
        _result.setTemplateName(_tmpTemplateName);
        final String _tmpTemplateData;
        if (_cursor.isNull(_cursorIndexOfTemplateData)) {
          _tmpTemplateData = null;
        } else {
          _tmpTemplateData = _cursor.getString(_cursorIndexOfTemplateData);
        }
        _result.setTemplateData(_tmpTemplateData);
        final String _tmpIsReport;
        if (_cursor.isNull(_cursorIndexOfIsReport)) {
          _tmpIsReport = null;
        } else {
          _tmpIsReport = _cursor.getString(_cursorIndexOfIsReport);
        }
        _result.setIsReport(_tmpIsReport);
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
  public PrintTemplate getPrintTemplateByName(final String templateName) {
    final String _sql = "select * from print_templates where template_name=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (templateName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, templateName);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "template_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfPrinterName = CursorUtil.getColumnIndexOrThrow(_cursor, "printer_name");
      final int _cursorIndexOfPrinterType = CursorUtil.getColumnIndexOrThrow(_cursor, "printer_type");
      final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "display_name");
      final int _cursorIndexOfTemplateName = CursorUtil.getColumnIndexOrThrow(_cursor, "template_name");
      final int _cursorIndexOfTemplateData = CursorUtil.getColumnIndexOrThrow(_cursor, "template_data");
      final int _cursorIndexOfIsReport = CursorUtil.getColumnIndexOrThrow(_cursor, "is_report");
      final PrintTemplate _result;
      if(_cursor.moveToFirst()) {
        _result = new PrintTemplate();
        final int _tmpTemplateId;
        _tmpTemplateId = _cursor.getInt(_cursorIndexOfTemplateId);
        _result.setTemplateId(_tmpTemplateId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _result.setCustId(_tmpCustId);
        final String _tmpPrinterName;
        if (_cursor.isNull(_cursorIndexOfPrinterName)) {
          _tmpPrinterName = null;
        } else {
          _tmpPrinterName = _cursor.getString(_cursorIndexOfPrinterName);
        }
        _result.setPrinterName(_tmpPrinterName);
        final String _tmpPrinterType;
        if (_cursor.isNull(_cursorIndexOfPrinterType)) {
          _tmpPrinterType = null;
        } else {
          _tmpPrinterType = _cursor.getString(_cursorIndexOfPrinterType);
        }
        _result.setPrinterType(_tmpPrinterType);
        final String _tmpDisplayName;
        if (_cursor.isNull(_cursorIndexOfDisplayName)) {
          _tmpDisplayName = null;
        } else {
          _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
        }
        _result.setDisplayName(_tmpDisplayName);
        final String _tmpTemplateName;
        if (_cursor.isNull(_cursorIndexOfTemplateName)) {
          _tmpTemplateName = null;
        } else {
          _tmpTemplateName = _cursor.getString(_cursorIndexOfTemplateName);
        }
        _result.setTemplateName(_tmpTemplateName);
        final String _tmpTemplateData;
        if (_cursor.isNull(_cursorIndexOfTemplateData)) {
          _tmpTemplateData = null;
        } else {
          _tmpTemplateData = _cursor.getString(_cursorIndexOfTemplateData);
        }
        _result.setTemplateData(_tmpTemplateData);
        final String _tmpIsReport;
        if (_cursor.isNull(_cursorIndexOfIsReport)) {
          _tmpIsReport = null;
        } else {
          _tmpIsReport = _cursor.getString(_cursorIndexOfIsReport);
        }
        _result.setIsReport(_tmpIsReport);
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
  public List<PrintTemplate> getChalkTemplates() {
    final String _sql = "select * from print_templates where template_name like 'Chalk%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTemplateId = CursorUtil.getColumnIndexOrThrow(_cursor, "template_id");
      final int _cursorIndexOfCustId = CursorUtil.getColumnIndexOrThrow(_cursor, "custid");
      final int _cursorIndexOfPrinterName = CursorUtil.getColumnIndexOrThrow(_cursor, "printer_name");
      final int _cursorIndexOfPrinterType = CursorUtil.getColumnIndexOrThrow(_cursor, "printer_type");
      final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "display_name");
      final int _cursorIndexOfTemplateName = CursorUtil.getColumnIndexOrThrow(_cursor, "template_name");
      final int _cursorIndexOfTemplateData = CursorUtil.getColumnIndexOrThrow(_cursor, "template_data");
      final int _cursorIndexOfIsReport = CursorUtil.getColumnIndexOrThrow(_cursor, "is_report");
      final List<PrintTemplate> _result = new ArrayList<PrintTemplate>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final PrintTemplate _item;
        _item = new PrintTemplate();
        final int _tmpTemplateId;
        _tmpTemplateId = _cursor.getInt(_cursorIndexOfTemplateId);
        _item.setTemplateId(_tmpTemplateId);
        final int _tmpCustId;
        _tmpCustId = _cursor.getInt(_cursorIndexOfCustId);
        _item.setCustId(_tmpCustId);
        final String _tmpPrinterName;
        if (_cursor.isNull(_cursorIndexOfPrinterName)) {
          _tmpPrinterName = null;
        } else {
          _tmpPrinterName = _cursor.getString(_cursorIndexOfPrinterName);
        }
        _item.setPrinterName(_tmpPrinterName);
        final String _tmpPrinterType;
        if (_cursor.isNull(_cursorIndexOfPrinterType)) {
          _tmpPrinterType = null;
        } else {
          _tmpPrinterType = _cursor.getString(_cursorIndexOfPrinterType);
        }
        _item.setPrinterType(_tmpPrinterType);
        final String _tmpDisplayName;
        if (_cursor.isNull(_cursorIndexOfDisplayName)) {
          _tmpDisplayName = null;
        } else {
          _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
        }
        _item.setDisplayName(_tmpDisplayName);
        final String _tmpTemplateName;
        if (_cursor.isNull(_cursorIndexOfTemplateName)) {
          _tmpTemplateName = null;
        } else {
          _tmpTemplateName = _cursor.getString(_cursorIndexOfTemplateName);
        }
        _item.setTemplateName(_tmpTemplateName);
        final String _tmpTemplateData;
        if (_cursor.isNull(_cursorIndexOfTemplateData)) {
          _tmpTemplateData = null;
        } else {
          _tmpTemplateData = _cursor.getString(_cursorIndexOfTemplateData);
        }
        _item.setTemplateData(_tmpTemplateData);
        final String _tmpIsReport;
        if (_cursor.isNull(_cursorIndexOfIsReport)) {
          _tmpIsReport = null;
        } else {
          _tmpIsReport = _cursor.getString(_cursorIndexOfIsReport);
        }
        _item.setIsReport(_tmpIsReport);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getTemplateIdByName(final String name) {
    final String _sql = "select template_id from print_templates where template_name=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
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
