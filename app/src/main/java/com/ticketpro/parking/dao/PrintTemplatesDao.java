package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.PrintTemplate;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface PrintTemplatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPrintTemplate(PrintTemplate... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPrintTemplate(PrintTemplate data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPrintTemplateList(List<PrintTemplate> PrintTemplatesList);

    @Query("select * from print_templates")
    List<PrintTemplate> getPrintTemplates();

    @Query("select * from print_templates where template_id=:templateId")
    PrintTemplate getPrintTemplateById(int templateId);

    @Query("select * from print_templates where template_name=:templateName")
    PrintTemplate getPrintTemplateByName(String templateName);

    @Query("select * from print_templates where template_name like 'Chalk%'")
    List<PrintTemplate> getChalkTemplates();

    @Query("select template_id from print_templates where template_name=:name")
    int getTemplateIdByName(String name);

    @Query("Delete from print_templates")
    void removeAll();

    @Query("Delete from print_templates where template_id=:id")
    void removeById(int id);
}
