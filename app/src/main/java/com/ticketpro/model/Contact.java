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

@Entity(tableName = "contacts")
public class Contact {
    @PrimaryKey
    @ColumnInfo(name = "contact_id")
    @SerializedName("contact_id")
    @Expose
    private int contactId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "email_id")
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @ColumnInfo(name = "phone")
    @SerializedName("phone")
    @Expose
    private String phone;
    @ColumnInfo(name = "contact_type")
    @SerializedName("contact_type")
    @Expose
    private String contactType;
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

    public Contact() {
    }

    public Contact(JSONObject object) throws Exception {
        this.setContactId(object.getInt("contact_id"));
        this.setCustId(object.getInt("custid"));
        this.setEmailId(object.getString("email_id"));
        this.setPhone(object.getString("phone"));
        this.setContactType(object.getString("contact_type"));
        this.setIsActive(object.getString("is_active"));

    }

    public static ArrayList<Contact> getContacts() throws Exception {
        ArrayList<Contact> list = new ArrayList<Contact>();
        list = (ArrayList<Contact>) ParkingDatabase.getInstance(TPApplication.getInstance()).contactsDao().getContacts();
        return list;
    }

    public static ArrayList<Contact> getTowNotifyContacts() throws Exception {
        ArrayList<Contact> list = new ArrayList<Contact>();
        list = (ArrayList<Contact>) ParkingDatabase.getInstance(TPApplication.getInstance()).contactsDao().getTowNotifyContacts();
        return list;
    }

    public static ArrayList<Contact> getSupportContacts() throws Exception {
        ArrayList<Contact> list = new ArrayList<Contact>();
        list = (ArrayList<Contact>) ParkingDatabase.getInstance(TPApplication.getInstance()).contactsDao().getSupportContacts();
        return list;
    }

    public static String getSupportNumber() throws Exception {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).contactsDao().getSupportNumber();
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).contactsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).contactsDao().removeById(id);
    }

    public static void insertContacts(Contact contact) {
        new InsertContacts(contact).execute();
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
        values.put("contact_id", this.contactId);
        values.put("custid", this.custId);
        values.put("email_id", this.emailId);
        values.put("phone", this.phone);
        values.put("contact_type", this.contactType);
        values.put("is_active", this.isActive);
        return values;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    private static class InsertContacts extends AsyncTask<Void, Void, Void> {
        private Contact contact;

        public InsertContacts(Contact contact) {
            this.contact = contact;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).contactsDao().insertContact(contact);
            return null;
        }
    }
}
