package com.ticketpro.model;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.exception.TPException;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.TPUtility;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "ticket_violations")
public class TicketViolation implements Cloneable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ticket_violation_id")
    @SerializedName("ticket_violation_id")
    @Expose
    private int ticketViolationId;
    @ColumnInfo(name = "violation_id")
    @SerializedName("violation_id")
    @Expose
    private int violationId;
    @ColumnInfo(name = "ticket_id")
    @SerializedName("ticket_id")
    @Expose
    private long ticketId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "violation_code")
    @SerializedName("violation_code")
    @Expose
    private String violationCode;
    @ColumnInfo(name = "fine")
    @SerializedName("fine")
    @Expose
    private double fine;
    @ColumnInfo(name = "citation_number")
    @SerializedName("citation_number")
    @Expose
    private long citationNumber;
    @ColumnInfo(name = "violation")
    private String violationDesc;
    @Ignore
    private String violationDisplay;
    @Ignore
    private boolean isWarning;
    @Ignore
    private ArrayList<TicketComment> ticketComments = new ArrayList<TicketComment>();
    @Ignore
    private int requiredComments;
    @Ignore
    private int requiredPhotos;
    @Ignore
    private boolean chalktimerequired;


    @Ignore
    private boolean isRepeatOffenderVertical;
    @Ignore
    private int verticalViolationId;

    public TicketViolation() {

    }

    public TicketViolation(JSONObject object) throws Exception {
        this.setTicketViolationId(object.getInt("ticket_violation_id"));
        this.setTicketId(object.getLong("ticket_id"));
        this.setCustId(!object.isNull("custid") ? object.getInt("custid") : 0);
        this.setViolationId(!object.isNull("violation_id") ? object.getInt("violation_id") : 0);
        this.setCitationNumber(object.getLong("citation_number"));
        this.setViolationCode(object.getString("violation_code"));

        if (!object.isNull("violation")) {
            this.setViolationDesc(object.getString("violation"));
        }

        if (!object.isNull("fine")) {
            this.setFine(object.getDouble("fine"));
        }
    }

    public static ArrayList<TicketViolation> getTicketViolations(long ticketId, long citationNumber) throws Exception {

        ArrayList<TicketViolation> list = new ArrayList<TicketViolation>();
        list = (ArrayList<TicketViolation>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketViolationsDao().getTicketViolations(ticketId, citationNumber);
        for (TicketViolation object : list) {
            try {
                Violation violation = Violation.getViolationById(object.getViolationId());
                if (violation != null) {
                    object.setViolationDesc(violation.getTitle());
                }
            } catch (Exception e) {
                Log.e("TicketViolation", TPUtility.getPrintStackTrace(e));
            }
        }
        return list;
    }

    public static ArrayList<TicketViolation> getTicketViolationsByCitation(long citationNumber)
            throws Exception {
        ArrayList<TicketViolation> list = new ArrayList<TicketViolation>();
        list = (ArrayList<TicketViolation>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketViolationsDao().getTicketViolationsByCitation(citationNumber);
        for (TicketViolation object : list) {
            try {
                Violation violation = Violation.getViolationById(object.getViolationId());
                if (violation != null) {
                    object.setViolationDesc(violation.getTitle());
                }
            } catch (Exception e) {
                Log.e("TicketViolation", TPUtility.getPrintStackTrace(e));
            }
        }
        return list;
    }

    public static ArrayList<TicketViolation> getTicketViolationWithComments(Context context, long ticketId) throws Exception {
        ArrayList<TicketViolation> list;
        list = (ArrayList<TicketViolation>) ParkingDatabase.getInstance(context).ticketViolationsDao().getTicketViolationWithComments(ticketId);
        for (TicketViolation object : list) {
            // Load Ticket Comments
            try {
                object.setTicketComments(TicketComment.getTicketComments(object.getTicketId(), object.getCitationNumber()));
            } catch (Exception e) {
                Log.e("TicketViolation", "Error1 " + e.getMessage());
            }

            // Load Violations
            try {
                Violation violation = Violation.getViolationById(object.getViolationId());
                if (violation != null) {
                    object.setViolationDesc(violation.getTitle());
                }
            } catch (Exception e) {
                Log.e("TicketViolation", "Error2 " + e.getMessage());
            }
        }
        return list;
    }

    public static ArrayList<TicketViolation> getTicketViolationByCitationWithComments(Context context, long citationNumber)
            throws Exception {
        ArrayList<TicketViolation> list = new ArrayList<TicketViolation>();
        list = (ArrayList<TicketViolation>) ParkingDatabase.getInstance(context).ticketViolationsDao().getTicketViolationByCitationWithComments(citationNumber);

        // Load Ticket Comments
        for (TicketViolation object : list) {
            try {
                object.setTicketComments(TicketComment.getTicketCommentsByCitation(context, citationNumber));
            } catch (Exception e) {
                Log.e("TicketViolation", "Error " + e.getMessage());
            }

            // Load Violations
            try {
                Violation violation = Violation.getViolationById(object.getViolationId());
                if (violation != null) {
                    object.setViolationDesc(violation.getTitle());
                }
            } catch (Exception e) {
                Log.e("TicketViolation", "Error " + e.getMessage());
            }
        }
        return list;
    }

    public static TicketViolation getViolationByPrimaryKey(Context context, String violationId) throws TPException {

        TicketViolation object = null;
        object = ParkingDatabase.getInstance(context).ticketViolationsDao().getViolationByPrimaryKey(violationId);
        return object;
    }

    public static int getNextPrimaryId() throws Exception {
        int nextId = 0;
        nextId = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketViolationsDao().getNextPrimaryId();
        return nextId + 1;
    }

    public static void insertTicketViolation(final TicketViolation TicketViolation) {
        final ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());

        instance.ticketViolationsDao().insertTicketViolation(TicketViolation).subscribeOn(Schedulers.io()).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.i("TicketViolation", "onComplete: ");
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });

        //new TicketViolation.InsertTicketViolation(TicketViolation).execute();
    }

    @NotNull
    @Override
    public Object clone() {
        TicketViolation object = null;
        try {
            object = (TicketViolation) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return object;
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        if (this.ticketViolationId != 0) {
            values.put("ticket_violation_id", this.ticketViolationId);
        } else {
            values.put("ticket_violation_id", TicketViolation.getNextPrimaryId());
        }

        values.put("ticket_id", this.ticketId);
        values.put("custid", this.custId);
        values.put("citation_number", this.citationNumber);
        values.put("violation_id", this.violationId);
        values.put("violation_code", this.violationCode);
        values.put("fine", this.fine);

        return values;
    }

    public JSONObject getJSONObject() {
        JSONObject values = new JSONObject();
        try {
            values.put("ticket_violation_id", this.ticketViolationId);
            values.put("ticket_id", this.ticketId);
            values.put("custid", this.custId);
            values.put("citation_number", this.citationNumber);
            values.put("violation_id", this.violationId);
            values.put("violation_code", this.violationCode);
            values.put("fine", this.fine);

        } catch (Exception e) {
            Log.e("TicketViolation", "Error " + e.getMessage());
        }

        return values;
    }

    public boolean isRepeatOffenderVertical() {
        return isRepeatOffenderVertical;
    }

    public void setRepeatOffenderVertical(boolean repeatOffenderVertical) {
        isRepeatOffenderVertical = repeatOffenderVertical;
    }

    public int getVerticalViolationId() {
        return verticalViolationId;
    }

    public void setVerticalViolationId(int verticalViolationId) {
        this.verticalViolationId = verticalViolationId;
    }

    public int getViolationId() {
        return violationId;
    }

    public void setViolationId(int violationId) {
        this.violationId = violationId;
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

    public String getViolationDesc() {
        return violationDesc;
    }

    public void setViolationDesc(String violationDesc) {
        this.violationDesc = violationDesc;
    }

    public String getViolationCode() {
        return violationCode;
    }

    public void setViolationCode(String violationCode) {
        this.violationCode = violationCode;
    }

    public ArrayList<TicketComment> getTicketComments() {
        return ticketComments;
    }

    public void setTicketComments(ArrayList<TicketComment> ticketComments) {
        this.ticketComments = ticketComments;
    }

    public int getTicketViolationId() {
        return ticketViolationId;
    }

    public void setTicketViolationId(int ticketViolationId) {
        this.ticketViolationId = ticketViolationId;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getViolationDisplay() {
        if (violationDisplay == null || violationDisplay.isEmpty()) {
            violationDisplay = violationDesc;
        }

        return violationDisplay;
    }

    public void setViolationDisplay(String violationDisplay) {
        this.violationDisplay = violationDisplay;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean isWarning) {
        this.isWarning = isWarning;
    }

    public int getRequiredComments() {
        return requiredComments;
    }

    public void setRequiredComments(int requiredComments) {
        this.requiredComments = requiredComments;
    }

    public int getRequiredPhotos() {
        return requiredPhotos;
    }

    public void setRequiredPhotos(int requiredPhotos) {
        this.requiredPhotos = requiredPhotos;
    }

    public boolean isChalktimerequired() {
        return chalktimerequired;
    }

    public void setChalktimerequired(boolean chalktimerequired) {
        this.chalktimerequired = chalktimerequired;
    }

    private static class InsertTicketViolation extends AsyncTask<Void, Void, Void> {
        private TicketViolation TicketViolation;

        public InsertTicketViolation(TicketViolation TicketViolation) {
            this.TicketViolation = TicketViolation;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).ticketViolationsDao().insertTicketViolation(TicketViolation);
            return null;
        }
    }
}
