package com.ticketpro.model;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.TPUtility;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "ticket_violations_temp",indices = @Index(value = {"violation_code"}, unique = true))
public class TicketViolationTemp implements Cloneable{
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

    public TicketViolationTemp() {

    }
    public static int getCount() throws Exception {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).ticketViolationsDaoTemp().getCount();
    }
    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketViolationsDaoTemp().removeAll();
    }
    public static ArrayList<TicketViolationTemp> getTicketViolations() throws Exception {

        ArrayList<TicketViolationTemp> list = new ArrayList<TicketViolationTemp>();
        list = (ArrayList<TicketViolationTemp>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketViolationsDaoTemp().getTicketViolations();
        for (TicketViolationTemp object : list) {
            try {
                Violation violation = Violation.getViolationById(object.getViolationId());
                if (violation != null) {
                    object.setViolationDesc(violation.getTitle());
                }
            } catch (Exception e) {
                Log.e("TicketViolationTemp", TPUtility.getPrintStackTrace(e));
            }
        }
        return list;
    }

    public static ArrayList<TicketViolationTemp> getTicketViolationsByCitation(long citationNumber)
            throws Exception {
        ArrayList<TicketViolationTemp> list = new ArrayList<TicketViolationTemp>();
        list = (ArrayList<TicketViolationTemp>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketViolationsDaoTemp().getTicketViolationsByCitation(citationNumber);
        for (TicketViolationTemp object : list) {
            try {
                Violation violation = Violation.getViolationById(object.getViolationId());
                if (violation != null) {
                    object.setViolationDesc(violation.getTitle());
                }
            } catch (Exception e) {
                Log.e("TicketViolationTemp", TPUtility.getPrintStackTrace(e));
            }
        }
        return list;
    }

    public static ArrayList<TicketViolationTemp> getTicketViolationWithComments(Context context, long ticketId) throws Exception {
        ArrayList<TicketViolationTemp> list;
        list = (ArrayList<TicketViolationTemp>) ParkingDatabase.getInstance(context).ticketViolationsDaoTemp().getTicketViolationWithComments(ticketId);
        for (TicketViolationTemp object : list) {
            // Load Ticket Comments
            try {
                object.setTicketComments(TicketComment.getTicketComments(object.getTicketId(), object.getCitationNumber()));
            } catch (Exception e) {
                Log.e("TicketViolationTemp", "Error1 " + e.getMessage());
            }

            // Load Violations
            try {
                Violation violation = Violation.getViolationById(object.getViolationId());
                if (violation != null) {
                    object.setViolationDesc(violation.getTitle());
                }
            } catch (Exception e) {
                Log.e("TicketViolationTemp", "Error2 " + e.getMessage());
            }
        }
        return list;
    }



    public static int getNextPrimaryId() throws Exception {
        int nextId = 0;
        nextId = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketViolationsDaoTemp().getNextPrimaryId();
        return nextId + 1;
    }

    public static void insertTicketViolationTemp(final TicketViolationTemp TicketViolationTemp) {
        final ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());

        instance.ticketViolationsDaoTemp().insertTicketViolation(TicketViolationTemp).subscribeOn(Schedulers.io()).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.i("TicketViolationTemp", "onComplete: ");
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });

        //new TicketViolationTemp.InsertTicketViolationTemp(TicketViolationTemp).execute();
    }

    @NotNull
    @Override
    public Object clone() {
        TicketViolationTemp object = null;
        try {
            object = (TicketViolationTemp) super.clone();
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
            values.put("ticket_violation_id", TicketViolationTemp.getNextPrimaryId());
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
            Log.e("TicketViolationTemp", "Error " + e.getMessage());
        }

        return values;
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
