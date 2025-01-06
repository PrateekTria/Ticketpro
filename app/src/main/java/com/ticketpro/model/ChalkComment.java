package com.ticketpro.model;

import android.content.ContentValues;

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

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "chalk_comments")
public class ChalkComment {
    @PrimaryKey
    @ColumnInfo(name = "chalk_comment_id")
    @SerializedName("chalk_comment_id")
    @Expose
    private int chalkCommentId;
    @ColumnInfo(name = "chalk_id")
    @SerializedName("chalk_id")
    @Expose
    private long chalkId;
    @ColumnInfo(name = "comment_id")
    @SerializedName("comment_id")
    @Expose
    private int commentId;
    @ColumnInfo(name = "comment")
    @SerializedName("comment")
    @Expose
    private String comment;
    @ColumnInfo(name = "is_private", defaultValue = "N")
    @SerializedName("is_private")
    @Expose
    private String isPrivate;
    @Ignore
    private boolean isVoiceComment = false;
    @Ignore
    private String audioFile;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;

    public ChalkComment() {

    }

    public ChalkComment(JSONObject object) throws Exception {
        this.setCommentId(object.getInt("chalk_comment_id"));
        this.setChalkId(object.getLong("chalk_id"));
        this.setCommentId(object.getInt("comment_id"));
        this.setComment(object.getString("comment"));
        this.setIsPrivate(object.getString("is_private"));
        this.setCustId(object.getInt("custid"));
    }

    public static ArrayList<ChalkComment> getChalkComments(long chalkId) throws Exception {
        return (ArrayList<ChalkComment>) ParkingDatabase.getInstance(TPApplication.getInstance()).chalkCommentsDao().getChalkComments(chalkId);
    }

    /*public static ChalkComment getChalkCommentByPrimaryKey(long commentId) throws TPException {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).chalkCommentsDao().getChalkCommentByPrimaryKey(commentId);
    }*/

    public static int getNextPrimaryId() throws Exception {
        int nextId;
        nextId = ParkingDatabase.getInstance(TPApplication.getInstance()).chalkCommentsDao().getNextPrimaryId();
        return nextId + 1;
    }

    public static Completable insertChalkComment(final ChalkComment ChalkComment) {
        return Completable.fromAction(() -> ParkingDatabase.getInstance(TPApplication.getInstance()).chalkCommentsDao().insertChalkComment(ChalkComment).subscribeOn(Schedulers.io()).subscribe()).subscribeOn(Schedulers.io());
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        try {
            if (this.chalkCommentId != 0) {
                values.put("chalk_comment_id", this.chalkCommentId);
            } else {
                values.put("chalk_comment_id", ChalkComment.getNextPrimaryId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        values.put("comment_id", this.commentId);
        values.put("chalk_id", this.chalkId);
        values.put("comment", this.comment);
        values.put("is_private", this.isPrivate);
        values.put("custid", this.custId);
        return values;
    }

    public JSONObject getJSONObject() {
        JSONObject values = new JSONObject();
        try {
            values.put("chalk_comment_id", this.chalkCommentId);
            values.put("comment_id", this.commentId);
            values.put("chalk_id", this.chalkId);
            values.put("comment", this.comment);
            values.put("is_private", this.isPrivate);
            values.put("custid", this.custId);
        } catch (Exception ignored) {
        }

        return values;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public long getChalkId() {
        return chalkId;
    }

    public void setChalkId(long chalkId) {
        this.chalkId = chalkId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getChalkCommentId() {
        return chalkCommentId;
    }

    public void setChalkCommentId(int chalkCommentId) {
        this.chalkCommentId = chalkCommentId;
    }

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public boolean isPrivateComment() {
        if (this.isPrivate == null)
            return false;

        return this.isPrivate.equalsIgnoreCase("Y");
    }

    public boolean isVoiceComment() {
        return isVoiceComment;
    }

    public void setVoiceComment(boolean isVoiceComment) {
        this.isVoiceComment = isVoiceComment;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

}
