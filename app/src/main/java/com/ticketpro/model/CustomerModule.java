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

@Entity(tableName = "customer_modules")
public class CustomerModule {
    @PrimaryKey
    @ColumnInfo(name = "customer_module_id")
    @SerializedName("customer_module_id")
    @Expose
    private int customerModuleId;
    @ColumnInfo(name = "module_id")
    @SerializedName("module_id")
    @Expose
    private int moduleId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "userid")
    @SerializedName("userid")
    @Expose
    private String userId;
    @ColumnInfo(name = "is_active")
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("sync_data_id")
    @Expose
    @Ignore
    private int syncDataId;
    @SerializedName("primary_key")
    @Expose
    @Ignore
    private int primaryKey;

    public CustomerModule() {

    }

    public CustomerModule(JSONObject object) throws Exception {
        this.setCustomerModuleId(!object.isNull("customer_module_id") ? object.getInt("customer_module_id") : 0);
        this.setModuleId(!object.isNull("module_id") ? object.getInt("module_id") : 0);
        this.setCustId(!object.isNull("custid") ? object.getInt("custid") : 0);
        this.setUserId(object.getString("userid"));
        this.setIsActive(object.getString("is_active"));
    }

    public static ArrayList<CustomerModule> getModules() throws Exception {
        return (ArrayList<CustomerModule>) ParkingDatabase.getInstance(TPApplication.getInstance()).customerModulesDao().getModules();
    }

    public static CustomerModule getModuleById(int moduleId) throws Exception {

        return ParkingDatabase.getInstance(TPApplication.getInstance()).customerModulesDao().getModuleById(moduleId);
    }

    public static CustomerModule getModuleByModuleName(String moduleName) throws Exception {
        Module module = Module.getModuleByName(moduleName);
        if (module == null) {
            return null;
        }

        return getModuleById(module.getModuleId());
    }

    public static CustomerModule getModuleByName(int moduleId, int userId) throws Exception {
        CustomerModule module = ParkingDatabase.getInstance(TPApplication.getInstance()).customerModulesDao().getModuleById(moduleId);
        /*String userIds = module.getUserId();
        if (userIds != null && !userIds.isEmpty() && !userIds.equalsIgnoreCase("null")) {
            List<String> wordList = Arrays.asList(userIds.split(","));
            if (!wordList.contains(userId + "")) {
                    continue;
            }
        }*/
        return module;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).customerModulesDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).customerModulesDao().removeById(id);
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
        values.put("customer_module_id", this.customerModuleId);
        values.put("module_id", this.moduleId);
        values.put("custid", this.custId);
        values.put("userid", this.userId);
        values.put("is_active", this.isActive);

        return values;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public int getCustomerModuleId() {
        return customerModuleId;
    }

    public void setCustomerModuleId(int customerModuleId) {
        this.customerModuleId = customerModuleId;
    }

    public boolean isActive() {
        if (this.isActive == null) {
            return false;
        }

        return this.isActive.equalsIgnoreCase("Y");
    }

    public static void insertCustomerModule(CustomerModule CustomerModule){
        new CustomerModule.InsertCustomerModule(CustomerModule).execute();
    }

    private static class InsertCustomerModule extends AsyncTask<Void,Void,Void> {
        private CustomerModule CustomerModule;

        public InsertCustomerModule(CustomerModule CustomerModule) {
            this.CustomerModule = CustomerModule;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).customerModulesDao().insertCustomerModule(CustomerModule);
            return null;
        }
    }
}
