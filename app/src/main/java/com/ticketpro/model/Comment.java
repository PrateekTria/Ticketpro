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

@Entity(tableName = "comments")
public class Comment {
    @PrimaryKey
    @ColumnInfo(name = "comment_id")
    @SerializedName("comment_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "comment")
    @SerializedName("comment")
    @Expose
    private String title;
    @ColumnInfo(name = "code")
    @SerializedName("code")
    @Expose
    private String code;
    @ColumnInfo(name = "order_number")
    @SerializedName("order_number")
    @Expose
    private int orderNumber;

    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public Comment() {
    }

    public Comment(JSONObject object) throws Exception {
        this.setId(!object.isNull("comment_id") ? object.getInt("comment_id") : 0);
        this.setCustId(!object.isNull("custid") ? object.getInt("custid") : 0);
        this.setTitle(object.getString("comment"));
        this.setCode(object.getString("code"));
        this.setOrderNumber(!object.isNull("order_number") ? object.getInt("order_number") : 0);
    }

    public static ArrayList<Comment> getComments() throws Exception {
        return (ArrayList<Comment>) ParkingDatabase.getInstance(TPApplication.getInstance()).commentsDao().getComments();
    }

    public static ArrayList<Comment> getCustomComments() throws Exception {
        return (ArrayList<Comment>) ParkingDatabase.getInstance(TPApplication.getInstance()).commentsDao().getCustomeComments();
    }


    public static Comment getCommentById(int commentId) throws Exception {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).commentsDao().getCommentById(commentId);
    }

    public static Comment getCommentByText(String commentText) throws Exception {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).commentsDao().getCommentByText(commentText);
    }

    public static Comment getCommentsByText(String commentText) throws Exception {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).commentsDao().getCommentByText(commentText);
    }

    public static int getLastInsertId() throws Exception {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).commentsDao().getLastInsertId();
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).commentsDao().removeAll();
    }

    public static void removeAddedComments() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).commentsDao().removeAddedComments();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).commentsDao().removeById(id);
    }

    public static void insertComment(Comment Comment) {
        new Comment.InsertComment(Comment).execute();
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
        if (this.id != 0) {
            values.put("comment_id", this.id);
        }

        values.put("custid", this.custId);
        values.put("comment", this.title);
        values.put("code", this.code);
        values.put("order_number", this.orderNumber);
        return values;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    private static class InsertComment extends AsyncTask<Void, Void, Void> {
        private Comment Comment;

        public InsertComment(Comment Comment) {
            this.Comment = Comment;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).commentsDao().insertComment(Comment);
            return null;
        }
    }
}
