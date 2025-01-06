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

@Entity(tableName = "modules")
public class Module {
    @Ignore
    public static final String TP_MODULE_PARKING = "Parking";
    @Ignore
    public static final String TP_MODULE_TRAFFIC = "Traffic";
    @PrimaryKey
    @ColumnInfo(name = "module_id")
    @SerializedName("module_id")
    @Expose
    private int moduleId;
    @ColumnInfo(name = "module_name")
    @SerializedName("module_name")
    @Expose
    private String moduleName;
    @ColumnInfo(name = "module_desc")
    @SerializedName("module_desc")
    @Expose
    private String moduleDesc;
    @ColumnInfo(name = "is_active")
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public Module() {

    }

    public Module(JSONObject object) throws Exception {
        this.setModuleId(!object.isNull("module_id") ? object.getInt("module_id") : 0);
        this.setModuleName(object.getString("module_name"));
        this.setModuleDesc(object.getString("module_desc"));
        this.setIsActive(object.getString("is_active"));
    }

    public static ArrayList<Module> getModules() throws Exception {
        ArrayList<Module> list = new ArrayList<Module>();
        list = (ArrayList<Module>) ParkingDatabase.getInstance(TPApplication.getInstance()).modulesDao().getModules();
        return list;
    }

    public static Module getModuleByName(String moduleName) throws Exception {
        Module object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).modulesDao().getModuleByName(moduleName);
        return object;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).modulesDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).modulesDao().removeById(id);
    }

    public static void insertModule(Module Module) {
        new Module.InsertModule(Module).execute();
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
        values.put("module_id", this.moduleId);
        values.put("module_name", this.moduleName);
        values.put("module_desc", this.moduleDesc);
        values.put("is_active", this.isActive);

        return values;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    private static class InsertModule extends AsyncTask<Void, Void, Void> {
        private Module Module;

        public InsertModule(Module Module) {
            this.Module = Module;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).modulesDao().insertModule(Module);
            return null;
        }
    }
}
