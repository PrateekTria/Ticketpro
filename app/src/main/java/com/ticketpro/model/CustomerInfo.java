package com.ticketpro.model;

import android.annotation.SuppressLint;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "customers")
public class CustomerInfo implements Serializable {
    @Ignore
    private static final long serialVersionUID = 1L;
    @PrimaryKey
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name;
    @ColumnInfo(name = "address")
    @SerializedName("address")
    @Expose
    private String address;
    @ColumnInfo(name = "contact_number")
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @ColumnInfo(name = "logo_image")
    @SerializedName("logo_image")
    @Expose
    private String logoImage;
    @ColumnInfo(name = "agency_code")
    @SerializedName("agency_code")
    @Expose
    private String agencyCode;
    @ColumnInfo(name = "web_address")
    @SerializedName("web_address")
    @Expose
    private String webAddress;
    @ColumnInfo(name = "content_folder")
    @SerializedName("content_folder")
    @Expose
    private String contentFolder;
    @ColumnInfo(name = "ticket_color")
    @SerializedName("ticket_color")
    @Expose
    private String ticketColor;
    @ColumnInfo(name = "ticket_back")
    @SerializedName("ticket_back")
    @Expose
    private String ticketBack;
    @ColumnInfo(name = "TRCourtCode")
    @SerializedName("TRCourtCode")
    @Expose
    private String TRCourtName;
    @ColumnInfo(name = "TRPrintAgencyName")
    @SerializedName("TRPrintAgencyName")
    @Expose
    private String TRPrintAgencyName;

    public CustomerInfo() {
    }

    public CustomerInfo(JSONObject object) throws Exception {
        this.setCustId(object.getInt("custid"));
        this.setName(object.getString("name"));
        this.setAddress(object.getString("address"));
        this.setContactNumber(object.getString("contact_number"));
        this.setLogoImage(object.getString("logo_image"));
        this.setAgencyCode(object.getString("agency_code"));
        this.setWebAddress(object.getString("web_address"));
        this.setContentFolder(object.getString("content_folder"));

        if (!object.isNull("ticket_color")) {
            this.setTicketColor(object.getString("ticket_color"));
        }

        if (!object.isNull("ticket_back")) {
            this.setTicketBack(object.getString("ticket_back"));
        }

        if (!object.isNull("TRCourtCode")) {
            this.setTRCourtName(object.getString("TRCourtCode"));
        }

        if (!object.isNull("TRPrintAgencyName")) {
            this.setTRPrintAgencyName(object.getString("TRPrintAgencyName"));
        }
    }

    public static CustomerInfo getCustomerInfo(int custId) throws Exception {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).customersDao().getCustomerInfoInfo(custId);
    }

    public static ArrayList<CustomerInfo> getCustomers() throws Exception {
        return (ArrayList<CustomerInfo>) ParkingDatabase.getInstance(TPApplication.getInstance()).customersDao().getCustomerInfos();
    }

    public static void removeAll(int activeCustId) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).customersDao().removeAll(activeCustId);
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).customersDao().removeById(id);
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static void inserCustomerInfo(final List<CustomerInfo> result) {
        final ParkingDatabase database = ParkingDatabase.getInstance(TPApplication.getInstance());
        Completable completable = Completable.fromAction(() -> database.customersDao().insertCustomerInfoList(result).subscribeOn(Schedulers.io()).subscribe());
        completable.subscribe();
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("custid", this.custId);
        values.put("name", this.name);
        values.put("address", this.address);
        values.put("contact_number", this.contactNumber);
        values.put("logo_image", this.logoImage);
        values.put("agency_code", this.agencyCode);
        values.put("web_address", this.webAddress);
        values.put("content_folder", this.contentFolder);
        values.put("ticket_color", this.ticketColor);
        values.put("ticket_back", this.ticketBack);
        values.put("TRCourtCode", this.TRCourtName);
        values.put("TRPrintAgencyName", this.TRPrintAgencyName);

        return values;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public String getContentFolder() {
        return contentFolder;
    }

    public void setContentFolder(String contentFolder) {
        this.contentFolder = contentFolder;
    }

    public String getTicketColor() {
        return ticketColor;
    }

    public void setTicketColor(String ticketColor) {
        this.ticketColor = ticketColor;
    }

    public String getTicketBack() {
        return ticketBack;
    }

    public void setTicketBack(String ticketBack) {
        this.ticketBack = ticketBack;
    }

    public String getTRCourtName() {
        return TRCourtName;
    }

    public void setTRCourtName(String tRCourtName) {
        TRCourtName = tRCourtName;
    }

    public String getTRPrintAgencyName() {
        return TRPrintAgencyName;
    }

    public void setTRPrintAgencyName(String tRPrintAgencyName) {
        TRPrintAgencyName = tRPrintAgencyName;
    }

    @SuppressLint("StaticFieldLeak")
    static class InsertCustomers extends AsyncTask<Void, Void, Void> {
        List<CustomerInfo> list;

        public InsertCustomers(List<CustomerInfo> users) {
            list = users;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).customersDao().insertCustomerInfoList(list);
            return null;
        }
    }

    public static void insertCustomerInfo(CustomerInfo CustomerInfo) {
        new CustomerInfo.InsertCustomerInfo(CustomerInfo).execute();
    }

    private static class InsertCustomerInfo extends AsyncTask<Void, Void, Void> {
        private CustomerInfo CustomerInfo;

        public InsertCustomerInfo(CustomerInfo CustomerInfo) {
            this.CustomerInfo = CustomerInfo;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).customersDao().insertCustomerInfo(CustomerInfo);
            return null;
        }
    }
}
