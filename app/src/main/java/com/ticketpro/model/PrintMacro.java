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

@Entity(tableName = "print_macros")
public class PrintMacro {
    @PrimaryKey
    @ColumnInfo(name = "print_macro_id")
    @SerializedName("print_macro_id")
    @Expose
    private int macroId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "macro_name")
    @SerializedName("macro_name")
    @Expose
    private String macroName;
    @ColumnInfo(name = "message")
    @SerializedName("message")
    @Expose
    private String message;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public PrintMacro() {

    }

    public PrintMacro(JSONObject object) throws Exception {
        this.setMacroId(object.getInt("print_macro_id"));
        this.setCustId(object.getInt("custid"));
        this.setMacroName(object.getString("macro_name"));
        this.setMessage(object.getString("message"));
    }

    public static ArrayList<PrintMacro> getPrintMacros() throws Exception {
        ArrayList<PrintMacro> list;
        list = (ArrayList<PrintMacro>) ParkingDatabase.getInstance(TPApplication.getInstance()).printMacrosDao().getPrintMacros();
        return list;
    }

    public static int getMacroIdByName(String name) throws Exception {
        int result;
        result = ParkingDatabase.getInstance(TPApplication.getInstance()).printMacrosDao().getMacroIdByName(name);
        return result;
    }

    public static String getPrintMacroMessageByName(String name) {
        ArrayList<String> result;
        result = (ArrayList<String>) ParkingDatabase.getInstance(TPApplication.getInstance()).printMacrosDao().getPrintMacroMessageByName(name);
        return result.get(result.size() - 1);
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).printMacrosDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).printMacrosDao().removeById(id);
    }

    public static void insertPrintMacro(PrintMacro PrintMacro) {
        new PrintMacro.InsertPrintMacro(PrintMacro).execute();
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
        values.put("print_macro_id", this.macroId);
        values.put("custid", this.custId);
        values.put("macro_name", this.macroName);
        values.put("message", this.message);

        return values;
    }

    public int getMacroId() {
        return macroId;
    }

    public void setMacroId(int macroId) {
        this.macroId = macroId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getMacroName() {
        return macroName;
    }

    public void setMacroName(String macroName) {
        this.macroName = macroName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private static class InsertPrintMacro extends AsyncTask<Void, Void, Void> {
        private PrintMacro PrintMacro;

        public InsertPrintMacro(PrintMacro PrintMacro) {
            this.PrintMacro = PrintMacro;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).printMacrosDao().insertPrintMacro(PrintMacro);
            return null;
        }
    }
}
