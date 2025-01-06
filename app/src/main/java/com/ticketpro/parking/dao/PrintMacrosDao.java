package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.PrintMacro;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface PrintMacrosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPrintMacro(PrintMacro... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPrintMacro(PrintMacro data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPrintMacroList(List<PrintMacro> PrintMacrosList);

    @Query("select * from print_macros")
    List<PrintMacro> getPrintMacros();

    @Query("select print_macro_id from print_macros where macro_name=:name")
    int getMacroIdByName(String name);

    @Query("select message from print_macros where macro_name=:name")
    List<String> getPrintMacroMessageByName(String name);

    @Query("Delete from print_macros")
    void removeAll();

    @Query("Delete from print_macros where print_macro_id=:id")
    void removeById(int id);
}
