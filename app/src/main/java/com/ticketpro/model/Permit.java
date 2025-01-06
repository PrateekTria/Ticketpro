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

@Entity(tableName = "permits")
public class Permit {
    @PrimaryKey
    @ColumnInfo(name = "permit_id")
    @SerializedName("permit_id")
    @Expose
    private int permitId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "permit_number")
    @SerializedName("permit_number")
    @Expose
    private String permitNumber;
    @ColumnInfo(name = "permit_type")
    @SerializedName("permit_type")
    @Expose
    private String permitType;
    @ColumnInfo(name = "permit_code")
    @SerializedName("permit_code")
    @Expose
    private String permitCode;
    @ColumnInfo(name = "plate")
    @SerializedName("plate")
    @Expose
    private String plate;
    @ColumnInfo(name = "plate_type")
    @SerializedName("plate_type")
    @Expose
    private String plateType;
    @ColumnInfo(name = "vin")
    @SerializedName("vin")
    @Expose
    private String vin;
    @ColumnInfo(name = "exp_date")
    @SerializedName("exp_date")
    @Expose
    private Date expiration;
    @Ignore
    private int stateId;
    @ColumnInfo(name = "state_code")
    @SerializedName("state_code")
    @Expose
    private String stateCode;
    @Ignore
    private int bodyId;
    @ColumnInfo(name = "body_code")
    @SerializedName("body_code")
    @Expose
    private String bodyCode;
    @Ignore
    private int colorId;
    @ColumnInfo(name = "color_code")
    @SerializedName("color_code")
    @Expose
    private String colorCode;
    @Ignore
    private int makeId;
    @ColumnInfo(name = "make_code")
    @SerializedName("make_code")
    @Expose
    private String makeCode;
    @Ignore
    private int classId;
    @ColumnInfo(name = "class_code")
    @SerializedName("class_code")
    @Expose
    private String classCode;
    @ColumnInfo(name = "begin_date")
    @SerializedName("begin_date")
    @Expose
    private Date beginDate;
    @ColumnInfo(name = "end_date")
    @SerializedName("end_date")
    @Expose
    private Date endDate;
    @ColumnInfo(name = "permit_holder")
    @SerializedName("permit_holder")
    @Expose
    private String permitHolder;
    @ColumnInfo(name = "student_id")
    @SerializedName("student_id")
    @Expose
    private int studentId;
    @ColumnInfo(name = "address1")
    @SerializedName("address1")
    @Expose
    private String address1;
    @ColumnInfo(name = "address2")
    @SerializedName("address2")
    @Expose
    private String address2;
    @ColumnInfo(name = "phone")
    @SerializedName("phone")
    @Expose
    private String phone;
    @ColumnInfo(name = "model_code")
    @SerializedName("model_code")
    @Expose
    private String modelCode;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public Permit() {

    }

    public Permit(JSONObject object) throws Exception {

        this.setPermitId(object.getInt("permit_id"));
        this.setCustId(object.getInt("custid"));
        this.setPlate(object.getString("plate"));
        this.setVin(object.getString("vin"));
        this.setStateCode(object.getString("state_code"));
        this.setMakeCode(object.getString("make_code"));
        this.setModelCode(object.getString("model_code"));
        this.setBodyCode(object.getString("body_code"));
        this.setColorCode(object.getString("color_code"));
        this.setClassCode(object.getString("class_code"));
        this.setPermitNumber(object.getString("permit_number"));
        this.setPermitType(object.getString("permit_type"));
        this.setPermitCode(object.getString("permit_code"));
        this.setPlateType(object.getString("plate_type"));
        this.setExpiration(!object.isNull("exp_date") ? DateUtil.getDateFromSQLString(object.getString("exp_date")) : null);
        this.setBeginDate(!object.isNull("begin_date") ? DateUtil.getDateFromSQLString(object.getString("begin_date")) : null);
        this.setEndDate(!object.isNull("end_date") ? DateUtil.getDateFromSQLString(object.getString("end_date")) : null);
        this.setStudentId(!object.isNull("student_id") ? object.getInt("student_id") : 0);
        this.setPermitHolder(object.getString("permit_holder"));
        this.setAddress1(object.getString("address1"));
        this.setAddress2(object.getString("address2"));
        this.setPhone(object.getString("phone"));

    }

    public static ArrayList<Permit> getPermits() throws Exception {
        ArrayList<Permit> list;
        list = (ArrayList<Permit>) ParkingDatabase.getInstance(TPApplication.getInstance()).permitsDao().getPermits();
        return list;
    }

    public static Permit searchPermitHistory(String permit) throws Exception {
        Permit object;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).permitsDao().searchPermitHistory(permit);
        return object;
    }

    public static ArrayList<Permit> searchAllPermitHistory(String permit) throws Exception {
        ArrayList<Permit> permitList;
        permitList = (ArrayList<Permit>) ParkingDatabase.getInstance(TPApplication.getInstance()).permitsDao().searchAllPermitHistory(permit);
        return permitList;
    }

    public static Permit searchPermitByPlate(String plate) throws Exception {
        Permit object;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).permitsDao().searchPermitByPlate(plate);
        return object;
    }

    public static Permit searchPermitByVin(String vin, String state, int custId) throws Exception {
        Permit object;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).permitsDao().searchPermitByVin(vin, state, custId);
        return object;
    }

    public static ArrayList<Permit> searchAllPermitByPlate(String plate, String state) throws Exception {
        ArrayList<Permit> permitTickets;
        permitTickets = (ArrayList<Permit>) ParkingDatabase.getInstance(TPApplication.getInstance()).permitsDao().searchAllPermitByPlate(plate, state);
        return permitTickets;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).permitsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).permitsDao().removeById(id);
    }

    public static void insertPermit(Permit Permit) {
        new Permit.InsertPermit(Permit).execute();
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
        values.put("permit_id", this.permitId);
        values.put("custid", this.custId);
        values.put("plate", this.plate);
        values.put("vin", this.vin);
        values.put("state_code", this.stateCode);
        values.put("make_code", this.makeCode);
        values.put("model_code", this.modelCode);
        values.put("body_code", this.bodyCode);
        values.put("class_code", this.classCode);
        values.put("color_code", this.colorCode);
        values.put("permit_number", this.permitNumber);
        values.put("permit_type", this.permitType);
        values.put("permit_code", this.permitCode);
        values.put("plate_type", this.plateType);
        values.put("exp_date", DateUtil.getSQLStringFromDate2(this.expiration));
        values.put("begin_date", DateUtil.getSQLStringFromDate2(this.beginDate));
        values.put("end_date", DateUtil.getSQLStringFromDate2(this.endDate));
        values.put("student_id", this.studentId);
        values.put("permit_holder", this.permitHolder);
        values.put("address1", this.address1);
        values.put("address2", this.address2);
        values.put("phone", this.phone);
        return values;
    }

    public int getPermitId() {
        return permitId;
    }

    public void setPermitId(int permitId) {
        this.permitId = permitId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getPermitNumber() {
        return permitNumber;
    }

    public void setPermitNumber(String permitNumber) {
        this.permitNumber = permitNumber;
    }

    public String getPermitType() {
        return permitType;
    }

    public void setPermitType(String permitType) {
        this.permitType = permitType;
    }

    public String getPermitCode() {
        return permitCode;
    }

    public void setPermitCode(String permitCode) {
        this.permitCode = permitCode;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public int getBodyId() {
        return bodyId;
    }

    public void setBodyId(int bodyId) {
        this.bodyId = bodyId;
    }

    public String getBodyCode() {
        return bodyCode;
    }

    public void setBodyCode(String bodyCode) {
        this.bodyCode = bodyCode;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public int getMakeId() {
        return makeId;
    }

    public void setMakeId(int makeId) {
        this.makeId = makeId;
    }

    public String getMakeCode() {
        return makeCode;
    }

    public void setMakeCode(String makeCode) {
        this.makeCode = makeCode;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPermitHolder() {
        return permitHolder;
    }

    public void setPermitHolder(String permitHolder) {
        this.permitHolder = permitHolder;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    private static class InsertPermit extends AsyncTask<Void, Void, Void> {
        private Permit Permit;

        public InsertPermit(Permit Permit) {
            this.Permit = Permit;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).permitsDao().insertPermit(Permit);
            return null;
        }
    }

}
