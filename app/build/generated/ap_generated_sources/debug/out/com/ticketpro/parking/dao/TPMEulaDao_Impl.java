package com.ticketpro.parking.dao;

import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ticketpro.model.EulaModel;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TPMEulaDao_Impl implements TPMEulaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EulaModel> __insertionAdapterOfEulaModel;

  public TPMEulaDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEulaModel = new EntityInsertionAdapter<EulaModel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `tpm_eula` (`rec_id`,`EULA_text`,`Entry_date`,`Effective_date`,`cust_id`,`module_id`,`is_active`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, EulaModel value) {
        stmt.bindLong(1, value.getResId());
        if (value.getEulaText() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getEulaText());
        }
        if (value.getEntryDate() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getEntryDate());
        }
        if (value.getEffectiveDate() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getEffectiveDate());
        }
        stmt.bindLong(5, value.getCustId());
        stmt.bindLong(6, value.getModuleId());
        if (value.getIsActive() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getIsActive());
        }
      }
    };
  }

  @Override
  public void insertEulaModel(final EulaModel... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfEulaModel.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertEulaModel(final EulaModel data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfEulaModel.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertEulaModelList(final List<EulaModel> EulaModelsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfEulaModel.insert(EulaModelsList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
