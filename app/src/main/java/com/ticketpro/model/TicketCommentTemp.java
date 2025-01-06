package com.ticketpro.model;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.exception.TPException;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;

import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "ticket_comments_temp",indices = @Index(value = {"comment_id"}, unique = true))
public class TicketCommentTemp {
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
    

    public static ArrayList<TicketCommentTemp> getTicketCommentTemps() throws Exception {
        ArrayList<TicketCommentTemp> list = new ArrayList<TicketCommentTemp>();
        list = (ArrayList<TicketCommentTemp>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketCommentsDaoTemp().getTicketCommentTemps();
        return list;
    }
    public static int getCount() throws Exception {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).ticketCommentsDaoTemp().getCount();
    }

    public static ArrayList<TicketCommentTemp> getTicketCommentTempsByCitation(Context context, long citationNumber) throws Exception {
        ArrayList<TicketCommentTemp> list = new ArrayList<TicketCommentTemp>();
        list = (ArrayList<TicketCommentTemp>) ParkingDatabase.getInstance(context).ticketCommentsDaoTemp().getTicketCommentTempsByCitation(citationNumber);
        return list;
    }


    public static TicketCommentTemp getCommentsByPrimaryKey(String commentId) throws TPException {
        TicketCommentTemp object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketCommentsDaoTemp().getCommentsByPrimaryKey(commentId);
        return object;
    }

    public static int getNextPrimaryId() {
        int nextId = 0;
        nextId = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketCommentsDao().getNextPrimaryId();
        return nextId + 1;
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketCommentsDaoTemp().removeById(id);
    }
    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketCommentsDaoTemp().removeAll();
    }
    public static void insertTicketCommentTemp(final TicketCommentTemp TicketCommentTemp) {
        final ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());

        instance.ticketCommentsDaoTemp().insertTicketCommentTemp(TicketCommentTemp).subscribeOn(Schedulers.io()).subscribeWith(new CompletableObserver() {
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

//        new TicketCommentTemp.InsertTicketCommentTemp(TicketCommentTemp).execute();
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        if (this.ticketCommentId != 0) {
            values.put("ticket_comment_id", this.ticketCommentId);
        } else {
            values.put("ticket_comment_id", TicketCommentTemp.getNextPrimaryId());
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

    private static class InsertTicketCommentTemp extends AsyncTask<Void, Void, Void> {
        private TicketCommentTemp TicketCommentTemp;

        public InsertTicketCommentTemp(TicketCommentTemp TicketCommentTemp) {
            this.TicketCommentTemp = TicketCommentTemp;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).ticketCommentsDaoTemp().insertTicketCommentTemp(TicketCommentTemp);
            return null;
        }
    }
}
