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
import com.ticketpro.util.DateUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "messages")
public class Message {
    @PrimaryKey
    @ColumnInfo(name = "message_id")
    @SerializedName("message_id")
    @Expose
    private int messageId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "message_date")
    @SerializedName("message_date")
    @Expose
    private Date messageDate;
    @ColumnInfo(name = "from_userid")
    @SerializedName("from_userid")
    @Expose
    private int fromUserId;
    @ColumnInfo(name = "to_userid")
    @SerializedName("to_userid")
    @Expose
    private int toUserId;
    @ColumnInfo(name = "subject")
    @SerializedName("subject")
    @Expose
    private String subject;
    @ColumnInfo(name = "message")
    @SerializedName("message")
    @Expose
    private String message;
    @ColumnInfo(name = "expiry_date")
    @SerializedName("expiry_date")
    @Expose
    private Date expiryDate;
    @ColumnInfo(name = "department")
    @SerializedName("department")
    @Expose
    private String department;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public Message() {
    }

    public Message(JSONObject object) throws Exception {
        this.setMessageId(object.getInt("message_id"));
        this.setCustId(object.getInt("custid"));
        this.setFromUserId(!object.isNull("from_userid") ? object.getInt("from_userid") : 0);
        this.setToUserId(!object.isNull("to_userid") ? object.getInt("to_userid") : 0);
        this.setSubject(object.getString("subject"));
        this.setMessage(object.getString("message"));
        this.setDepartment(object.getString("department"));
        this.setMessageDate(DateUtil.getDateFromSQLString(object.getString("message_date")));
        this.setExpiryDate(DateUtil.getDateFromSQLString(object.getString("expiry_date")));
    }

    public static ArrayList<Message> getMessages(String department) throws Exception {
        ArrayList<Message> list = new ArrayList<Message>();
        list = (ArrayList<Message>) ParkingDatabase.getInstance(TPApplication.getInstance()).messagesDao().getMessages(department);
        return list;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).messagesDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).messagesDao().removeById(id);
    }

    public static void insertMessage(Message Message) {
        new Message.InsertMessage(Message).execute();
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
        values.put("message_id", this.messageId);
        values.put("custid", this.custId);
        values.put("from_userid", this.fromUserId);
        values.put("to_userid", this.toUserId);
        values.put("subject", this.subject);
        values.put("message", this.message);
        values.put("department", this.department);
        values.put("message_date", DateUtil.getSQLStringFromDate2(this.messageDate));
        values.put("expiry_date", DateUtil.getSQLStringFromDate2(this.expiryDate));
        return values;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    private static class InsertMessage extends AsyncTask<Void, Void, Void> {
        private Message Message;

        public InsertMessage(Message Message) {
            this.Message = Message;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).messagesDao().insertMessage(Message);
            return null;
        }
    }
}
