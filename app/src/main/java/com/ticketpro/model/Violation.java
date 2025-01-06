package com.ticketpro.model;

import android.content.ContentValues;
import android.database.SQLException;
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

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "violations")
public class Violation {
    @PrimaryKey
    @ColumnInfo(name = "violation_id")
    @SerializedName("violation_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;

    @ColumnInfo(name = "violation")
    @SerializedName("violation")
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
    @ColumnInfo(name = "base_fine")
    @SerializedName("base_fine")
    @Expose
    private double baseFine;
    @ColumnInfo(name = "fine1")
    @SerializedName("fine1")
    @Expose
    private double fine1;
    @ColumnInfo(name = "fine2")
    @SerializedName("fine2")
    @Expose
    private double fine2;
    @ColumnInfo(name = "repeat_offender")
    @SerializedName("repeat_offender")
    @Expose
    private String repeatOffender;
    @ColumnInfo(name = "violation_display")
    @SerializedName("violation_display")
    @Expose
    private String violationDisplay;
    @ColumnInfo(name = "default_comment")
    @SerializedName("default_comment")
    @Expose
    private String defaultComment;
    @ColumnInfo(name = "required_comments")
    @SerializedName("required_comments")
    @Expose
    private int requiredComments;
    @ColumnInfo(name = "required_photos")
    @SerializedName("required_photos")
    @Expose
    private int requiredPhotos;
    @ColumnInfo(name = "chalktimerequired")
    @SerializedName("chalktimerequired")
    @Expose
    private String chalktimerequired;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    @ColumnInfo(name = "hide")
    @SerializedName("hide")
    @Expose
    private String hide;

    @ColumnInfo(name = "repeat_offender_type")
    @SerializedName("repeat_offender_type")
    @Expose
    private String repeatOffenderType;

    @ColumnInfo(name = "code_export")
    @SerializedName("code_export")
    @Expose
    private String code_exportt;

    public Violation() {

    }


    public Violation(JSONObject object) throws Exception {

        this.setId(object.getInt("violation_id"));
        this.setCustId(object.getInt("custid"));
        this.setTitle(object.getString("violation"));
        this.setCode(object.getString("code"));
        this.setOrderNumber(!object.isNull("order_number") ? object.getInt("order_number") : 0);

        if (!object.isNull("base_fine")) {
            this.setBaseFine(object.getDouble("base_fine"));
        }

        if (!object.isNull("fine1")) {
            this.setFine1(object.getDouble("fine1"));
        }

        if (!object.isNull("fine2")) {
            this.setFine2(object.getDouble("fine2"));
        }

        if (!object.isNull("violation_display")) {
            this.setViolationDisplay(object.getString("violation_display"));
        }

        this.setRepeatOffender(object.getString("repeat_offender"));

        if (!object.isNull("default_comment")) {
            this.setDefaultComment(object.getString("default_comment"));
        }

        if (!object.isNull("required_comments")) {
            this.setRequiredComments(object.getInt("required_comments"));
        }

        if (!object.isNull("required_photos")) {
            this.setRequiredPhotos(object.getInt("required_photos"));
        }
        if (!object.isNull("chalktimerequired")) {
            this.setChalktimerequired(object.getString("chalktimerequired"));
        }
    }

    public static ArrayList<Violation> getViolations(int custId) throws Exception {
        ArrayList<Violation> list;
        list = (ArrayList<Violation>) ParkingDatabase.getInstance(TPApplication.getInstance()).violationsDao().getViolations();
        return list;
    }

    public static Violation getViolationById(int violationId) throws SQLException {
        Violation violation;
        violation = ParkingDatabase.getInstance(TPApplication.getInstance()).violationsDao().getViolationById(violationId);
        return violation;
    }

    public static Violation getViolationByCode(String violationCode) throws SQLException {
        Violation violation = null;
        violation = ParkingDatabase.getInstance(TPApplication.getInstance()).violationsDao().getViolationByCode(violationCode);
        return violation;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).violationsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).violationsDao().removeById(id);
    }
    public static Violation getViolationByCode_Export(String code_export) throws SQLException {
        Violation violation = null;
        violation = ParkingDatabase.getInstance(TPApplication.getInstance()).violationsDao().getViolationByCode_Export(code_export);
        return violation;
    }

    public String getChalktimerequired() {
        return chalktimerequired;
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

    public String getHide() {
        return hide;
    }

    public void setHide(String hide) {
        this.hide = hide;
    }

    public void setCode_exportt(String code_exportt) {
        this.code_exportt = code_exportt;
    }

    public String getCode_exportt() {
        return code_exportt;
    }

    public String getRepeatOffenderType() {
        return repeatOffenderType;
    }

    public void setRepeatOffenderType(String repeatOffenderType) {
        this.repeatOffenderType = repeatOffenderType;
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("violation_id", this.id);
        values.put("custid", this.custId);
        values.put("violation", this.title);
        values.put("code", this.code);
        values.put("order_number", this.orderNumber);
        values.put("base_fine", this.baseFine);
        values.put("fine1", this.fine1);
        values.put("fine2", this.fine2);
        values.put("violation_display", this.violationDisplay);
        values.put("repeat_offender", this.repeatOffender);
        values.put("default_comment", this.defaultComment);
        values.put("required_comments", this.requiredComments);
        values.put("required_photos", this.requiredPhotos);
        values.put("chalktimerequired", this.chalktimerequired);

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

    public double getBaseFine() {
        return baseFine;
    }

    public void setBaseFine(double baseFine) {
        this.baseFine = baseFine;
    }

    public double getFine1() {
        return fine1;
    }

    public void setFine1(double fine1) {
        this.fine1 = fine1;
    }

    public double getFine2() {
        return fine2;
    }

    public void setFine2(double fine2) {
        this.fine2 = fine2;
    }

    public String getRepeatOffender() {
        return repeatOffender;
    }

    public String getViolationDisplay() {
        return violationDisplay;
    }

    public void setViolationDisplay(String violationDisplay) {
        this.violationDisplay = violationDisplay;
    }

    public boolean isRepeatOffender() {
        if (this.repeatOffender != null && this.repeatOffender.equalsIgnoreCase("Y")) {
            return true;
        }

        return false;
    }

    public void setRepeatOffender(String repeatOffender) {
        this.repeatOffender = repeatOffender;
    }

    public String getDefaultComment() {
        return defaultComment;
    }

    public void setDefaultComment(String defaultComment) {
        this.defaultComment = defaultComment;
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
        if ("Y".equalsIgnoreCase(chalktimerequired)) {
            return true;
        }

        return false;
    }

    public void setChalktimerequired(String chalktimerequired) {
        this.chalktimerequired = chalktimerequired;
    }

    public static void insertViolation(final Violation Violation){
        final ParkingDatabase database = ParkingDatabase.getInstance(TPApplication.getInstance());
        Completable completable = Completable.fromAction(() -> database.violationsDao().insertViolation(Violation).subscribeOn(Schedulers.io()).subscribe());
        completable.subscribe();//new Violation.InsertViolation(Violation).execute();
    }

    private static class InsertViolation extends AsyncTask<Void,Void,Void> {
        private Violation Violation;

        public InsertViolation(Violation Violation) {
            this.Violation = Violation;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).violationsDao().insertViolation(Violation);
            return null;
        }
    }
}