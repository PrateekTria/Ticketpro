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

@Entity(tableName = "comment_group_comments")
public class CommentGroupComment {
    @PrimaryKey
    @ColumnInfo(name = "comment_group_id")
    @SerializedName("comment_group_id")
    @Expose
    private int commentGroupId;
    @ColumnInfo(name = "group_id")
    @SerializedName("group_id")
    @Expose
    private int groupId;
    @ColumnInfo(name = "comment_id")
    @SerializedName("comment_id")
    @Expose
    private int commentId;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public CommentGroupComment() {

    }

    public CommentGroupComment(JSONObject object) throws Exception {

        this.setGroupId(object.getInt("group_id"));
        this.setCommentGroupId(object.getInt("comment_group_id"));
        this.setCommentId(object.getInt("comment_id"));

    }

    public static ArrayList<CommentGroupComment> getCommentGroupComments() throws Exception {
        return (ArrayList<CommentGroupComment>) ParkingDatabase.getInstance(TPApplication.getInstance()).commentgroupCommentsDao().getCommentGroupComments();
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).commentgroupCommentsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).commentgroupCommentsDao().removeById(id);
    }

    public static void insertCommentGroupComment(CommentGroupComment CommentGroupComment) {
        new CommentGroupComment.InsertCommentGroupComment(CommentGroupComment).execute();
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
        values.put("group_id", this.groupId);
        values.put("comment_group_id", this.commentGroupId);
        values.put("comment_id", this.commentId);

        return values;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getCommentGroupId() {
        return commentGroupId;
    }

    public void setCommentGroupId(int commentGroupId) {
        this.commentGroupId = commentGroupId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    private static class InsertCommentGroupComment extends AsyncTask<Void, Void, Void> {
        private CommentGroupComment CommentGroupComment;

        public InsertCommentGroupComment(CommentGroupComment CommentGroupComment) {
            this.CommentGroupComment = CommentGroupComment;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).commentgroupCommentsDao().insertCommentGroupComment(CommentGroupComment);
            return null;
        }
    }

}
