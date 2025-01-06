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
import java.util.List;

@Entity(tableName = "vendor_service_registrations")
public class VendorServiceRegistration {
    @PrimaryKey
    @ColumnInfo(name = "registration_id")
    @SerializedName("registration_id")
    @Expose
    private int registrationId;
    @ColumnInfo(name = "service_id")
    @SerializedName("service_id")
    @Expose
    private int serviceId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "device_id")
    @SerializedName("device_id")
    @Expose
    private int deviceId;
    @ColumnInfo(name = "userid")
    @SerializedName("userid")
    @Expose
    private int userId;
    @ColumnInfo(name = "param_mappings")
    @SerializedName("param_mappings")
    @Expose
    private String paramMappings;
    @ColumnInfo(name = "service_mode")
    @SerializedName("service_mode")
    @Expose
    private String serviceMode;
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

    public VendorServiceRegistration() {

    }

    public VendorServiceRegistration(JSONObject object) throws Exception {
        this.setRegistrationId(!object.isNull("registration_id") ? object.getInt("registration_id") : 0);
        this.setServiceId(!object.isNull("service_id") ? object.getInt("service_id") : 0);
        this.setCustId(!object.isNull("custid") ? object.getInt("custid") : 0);
        this.setUserId(!object.isNull("userid") ? object.getInt("userid") : 0);
        this.setDeviceId(!object.isNull("device_id") ? object.getInt("device_id") : 0);

        this.setParamMappings(object.getString("param_mappings"));
        this.setServiceMode(object.getString("service_mode"));
        this.setIsActive(object.getString("is_active"));
    }

    public static ArrayList<VendorServiceRegistration> getServiceRegistrations() throws Exception {
        ArrayList<VendorServiceRegistration> list = new ArrayList<VendorServiceRegistration>();
        list = (ArrayList<VendorServiceRegistration>) ParkingDatabase.getInstance(TPApplication.getInstance()).vendorServiceRegistrationsDao().getServiceRegistrations();
        return list;
    }

    public static VendorServiceRegistration getServiceRegistrationByName(String serviceName) throws Exception {
        VendorServiceRegistration object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).vendorServiceRegistrationsDao().getServiceRegistrationByName(serviceName);
        return object;
    }

    public static VendorServiceRegistration getServiceRegistrationByServiceId(int serviceId, int deviceId) throws Exception {
        VendorServiceRegistration object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).vendorServiceRegistrationsDao().getServiceRegistrationByServiceId(serviceId, deviceId);
        return object;
    }

    public static List<VendorServiceRegistration> getServiceRegistrationByServiceId1(int serviceId, int deviceId) throws Exception {
        List<VendorServiceRegistration> object;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).vendorServiceRegistrationsDao().getServiceRegistrationByServiceId1(serviceId, deviceId);
        return object;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).vendorServiceRegistrationsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).vendorServiceRegistrationsDao().removeById(id);
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
        values.put("registration_id", this.registrationId);
        values.put("service_id", this.serviceId);
        values.put("custid", this.custId);
        values.put("userid", this.userId);
        values.put("device_id", this.deviceId);
        values.put("param_mappings", this.paramMappings);
        values.put("service_mode", this.serviceMode);
        values.put("is_active", this.isActive);

        return values;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getParamMappings() {
        return paramMappings;
    }

    public void setParamMappings(String paramMappings) {
        this.paramMappings = paramMappings;
    }

    public String getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(String serviceMode) {
        this.serviceMode = serviceMode;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public static void insertVendorServiceRegistration(VendorServiceRegistration VendorServiceRegistration) {
        new VendorServiceRegistration.InsertVendorServiceRegistration(VendorServiceRegistration).execute();
    }

    private static class InsertVendorServiceRegistration extends AsyncTask<Void, Void, Void> {
        private VendorServiceRegistration VendorServiceRegistration;

        public InsertVendorServiceRegistration(VendorServiceRegistration VendorServiceRegistration) {
            this.VendorServiceRegistration = VendorServiceRegistration;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).vendorServiceRegistrationsDao().insertVendorServiceRegistration(VendorServiceRegistration);
            return null;
        }
    }
}
