package com.ticketpro.model;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.exception.TPException;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;

import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "ticket_comments")
public class TicketComment {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ticket_comment_id")
    @SerializedName("ticket_comment_id")
    @Expose
    private int ticketCommentId;
    @ColumnInfo(name = "ticket_id")
    @SerializedName("ticket_id")
    @Expose
    private long ticketId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "comment_id")
    @SerializedName("comment_id")
    @Expose
    private int commentId;
    @ColumnInfo(name = "comment")
    @SerializedName("comment")
    @Expose
    private String comment;
    @ColumnInfo(name = "citation_number")
    @SerializedName("citation_number")
    @Expose
    private long citationNumber;
    @ColumnInfo(name = "is_private")
    @SerializedName("is_private")
    @Expose
    private String isPrivate;
    @Ignore
    private String tag;
    @ColumnInfo(name = "is_voice_comment")
    @SerializedName("is_voice_comment")
    @Expose
    private boolean isVoiceComment = false;
    @Ignore
    private String audioFile;


    public TicketComment() {

    }

    public TicketComment(JSONObject object) throws Exception {

        this.setTicketCommentId(object.getInt("ticket_comment_id"));
        this.setTicketId(object.getLong("ticket_id"));
        this.setCustId(!object.isNull("custid") ? object.getInt("custid") : 0);
        this.setCommentId(object.getInt("comment_id"));
        this.setCitationNumber(object.getLong("citation_number"));
        this.setComment(object.getString("comment"));
        this.setIsPrivate(object.getString("is_private"));
        if (!object.isNull("is_voice_comment")) {
            this.setVoiceComment(object.getString("is_voice_comment").equals("Y"));
        }

    }

    public static ArrayList<TicketComment> getTicketComments(long ticketId, long citationNumber) throws Exception {
        ArrayList<TicketComment> list = new ArrayList<TicketComment>();
        list = (ArrayList<TicketComment>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketCommentsDao().getTicketComments(ticketId, citationNumber);
        return list;
    }


    public static ArrayList<TicketComment> getTicketCommentsByCitation(Context context, long citationNumber) throws Exception {
        ArrayList<TicketComment> list = new ArrayList<TicketComment>();
        list = (ArrayList<TicketComment>) ParkingDatabase.getInstance(context).ticketCommentsDao().getTicketCommentsByCitation(citationNumber);
        return list;
    }


    public static TicketComment getCommentsByPrimaryKey(String commentId) throws TPException {
        TicketComment object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketCommentsDao().getCommentsByPrimaryKey(commentId);
        return object;
    }

    public static int getNextPrimaryId() {
        int nextId = 0;
        nextId = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketCommentsDao().getNextPrimaryId();
        return nextId + 1;
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketCommentsDao().removeById(id);
    }

    public static void insertTicketComment(final TicketComment TicketComment) {
        final ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());

        instance.ticketCommentsDao().insertTicketComment(TicketComment).subscribeOn(Schedulers.io()).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });

//        new TicketComment.InsertTicketComment(TicketComment).execute();
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        if (this.ticketCommentId != 0) {
            values.put("ticket_comment_id", this.ticketCommentId);
        } else {
            values.put("ticket_comment_id", TicketComment.getNextPrimaryId());
        }

        values.put("comment_id", this.commentId);
        values.put("custid", this.custId);
        values.put("ticket_id", this.ticketId);
        values.put("citation_number", this.citationNumber);
        values.put("comment", this.comment);
        values.put("is_private", this.isPrivate);
        values.put("is_voice_comment", this.isVoiceComment ? "Y" : "N");

        return values;
    }

    public JSONObject getJSONObject() {
        JSONObject values = new JSONObject();
        try {
            values.put("ticket_comment_id", this.ticketCommentId);
            values.put("comment_id", this.commentId);
            values.put("ticket_id", this.ticketId);
            values.put("custid", this.custId);
            values.put("citation_number", this.citationNumber);
            if (this.comment != null) {
                this.comment = this.comment.toUpperCase();
            }

            values.put("comment", this.comment);
            values.put("is_private", this.isPrivate);
            values.put("is_voice_comment", this.isVoiceComment ? "Y" : "N");

        } catch (Exception e) {

        }

        return values;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public long getCitationNumber() {
        return citationNumber;
    }

    public void setCitationNumber(long citationNumber) {
        this.citationNumber = citationNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getTicketCommentId() {
        return ticketCommentId;
    }

    public void setTicketCommentId(int ticketCommentId) {
        this.ticketCommentId = ticketCommentId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
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

    public boolean isPrivate() {
        if (this.isPrivate != null && this.isPrivate.equals("Y"))
            return true;

        return false;
    }

    private static class InsertTicketComment extends AsyncTask<Void, Void, Void> {
        private TicketComment TicketComment;

        public InsertTicketComment(TicketComment TicketComment) {
            this.TicketComment = TicketComment;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).ticketCommentsDao().insertTicketComment(TicketComment);
            return null;
        }
    }
}
