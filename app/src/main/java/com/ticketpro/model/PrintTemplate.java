package com.ticketpro.model;

import android.content.ContentValues;
import android.os.AsyncTask;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;

import org.json.JSONObject;

import java.util.ArrayList;

@Entity(tableName = "print_templates")
public class PrintTemplate {
    @PrimaryKey
    @ColumnInfo(name = "template_id")
    @SerializedName("template_id")
    @Expose
    private int templateId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "printer_name")
    @SerializedName("printer_name")
    @Expose
    private String printerName;
    @ColumnInfo(name = "printer_type")
    @SerializedName("printer_type")
    @Expose
    private String printerType;
    @ColumnInfo(name = "display_name")
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @ColumnInfo(name = "template_name")
    @SerializedName("template_name")
    @Expose
    private String templateName;
    @ColumnInfo(name = "template_data")
    @SerializedName("template_data")
    @Expose
    private String templateData;
    @ColumnInfo(name = "is_report")
    @SerializedName("is_report")
    @Expose
    private String isReport;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public PrintTemplate() {

    }

    public PrintTemplate(JSONObject object) throws Exception {

        this.setTemplateId(object.getInt("template_id"));
        this.setCustId(object.getInt("custid"));
        this.setPrinterName(object.getString("printer_name"));
        this.setPrinterType(object.getString("printer_type"));
        this.setDisplayName(object.getString("display_name"));
        this.setTemplateName(object.getString("template_name"));
        this.setTemplateData(object.getString("template_data"));
        this.setIsReport(object.getString("is_report"));

    }

    public static ArrayList<PrintTemplate> getPrintTemplates(int custId) throws Exception {
        ArrayList<PrintTemplate> list = new ArrayList<PrintTemplate>();
        list = (ArrayList<PrintTemplate>) ParkingDatabase.getInstance(TPApplication.getInstance()).printTemplatesDao().getPrintTemplates();
        return list;
    }

    public static PrintTemplate getPrintTemplateById(int templateId) throws Exception {
        PrintTemplate object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).printTemplatesDao().getPrintTemplateById(templateId);
        return object;
    }

    public static PrintTemplate getPrintTemplateByName(String templateName) throws Exception {
        PrintTemplate object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).printTemplatesDao().getPrintTemplateByName(templateName);
        return object;
    }

    public static ArrayList<PrintTemplate> getChalkTemplates() {
        ArrayList<PrintTemplate> list = new ArrayList<PrintTemplate>();
        list = (ArrayList<PrintTemplate>) ParkingDatabase.getInstance(TPApplication.getInstance()).printTemplatesDao().getChalkTemplates();
        return list;
    }

    public static int getTemplateIdByName(String name) throws Exception {
        int result = 0;
        result = ParkingDatabase.getInstance(TPApplication.getInstance()).printTemplatesDao().getTemplateIdByName(name);
        return result;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).printTemplatesDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).printTemplatesDao().removeById(id);
    }

    public static void insertPrintTemplate(PrintTemplate PrintTemplate) {
        new PrintTemplate.InsertPrintTemplate(PrintTemplate).execute();
    }

    public int getSyncDataId() {
        return syncDataId;
    }

    public void setSyncDataId(int syncDataId) {
        this.syncDataId = syncDataId;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("template_id", this.templateId);
        values.put("custid", this.custId);
        values.put("printer_name", this.printerName);
        values.put("printer_type", this.printerType);
        values.put("display_name", this.displayName);
        values.put("template_name", this.templateName);
        values.put("template_data", this.templateData);
        values.put("is_report", this.isReport);

        return values;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getPrinterType() {
        return printerType;
    }

    public void setPrinterType(String printerType) {
        this.printerType = printerType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateData() {
        return templateData;
    }

    public void setTemplateData(String templateData) {
        this.templateData = templateData;
    }

    public String getIsReport() {
        return isReport;
    }

    public void setIsReport(String isReport) {
        this.isReport = isReport;
    }

    private static class InsertPrintTemplate extends AsyncTask<Void, Void, Void> {
        private PrintTemplate PrintTemplate;

        public InsertPrintTemplate(PrintTemplate PrintTemplate) {
            this.PrintTemplate = PrintTemplate;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).printTemplatesDao().insertPrintTemplate(PrintTemplate);
            return null;
        }
    }

}
