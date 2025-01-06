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
import java.util.Map;
import java.util.TreeMap;

@Entity(tableName = "makes_and_models")
public class MakeAndModel {
    @PrimaryKey
    @ColumnInfo(name = "make_id")
    @SerializedName("make_id")
    @Expose
    private int makeId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "make")
    @SerializedName("make")
    @Expose
    private String makeTitle;
    @ColumnInfo(name = "make_code")
    @SerializedName("make_code")
    @Expose
    private String makeCode;
    @ColumnInfo(name = "model")
    @SerializedName("model")
    @Expose
    private String modelTitle;
    @ColumnInfo(name = "model_code")
    @SerializedName("model_code")
    @Expose
    private String modelCode;
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

    public MakeAndModel() {
    }

    public MakeAndModel(JSONObject object) throws Exception {
        this.setMakeId(object.getInt("make_id"));
        this.setCustId(object.getInt("custid"));
        this.setMakeTitle(object.getString("make"));
        this.setMakeCode(object.getString("make_code"));
        this.setModelTitle(object.getString("model"));
        this.setModelCode(object.getString("model_code"));
        this.setOrderNumber(!object.isNull("order_number") ? object.getInt("order_number") : 0);
    }

    public static ArrayList<MakeAndModel> getMakesAndModels(int custId) throws Exception {
        ArrayList<MakeAndModel> list = new ArrayList<MakeAndModel>();
        list = (ArrayList<MakeAndModel>) ParkingDatabase.getInstance(TPApplication.getInstance()).makesAndModelsDao().getMakesAndModels();
        return list;
    }

    public static MakeAndModel getMakeById(int makeId) {
        MakeAndModel object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).makesAndModelsDao().getMakeById(makeId);
        return object;
    }

    public static MakeAndModel getMakeByCode(String makeCode) {
        MakeAndModel object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).makesAndModelsDao().getMakeByCode(makeCode);
        return object;
    }

    public static MakeAndModel getMakeByTitle(String makeTitle) {
        MakeAndModel object = null;
        if (makeTitle != null) {
            makeTitle = makeTitle.toUpperCase();
        }
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).makesAndModelsDao().getMakeByTitle(makeTitle);
        return object;
    }

    public static MakeAndModel getModelByCode(String modelCode) {
        MakeAndModel object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).makesAndModelsDao().getModelByCode(modelCode);
        return object;
    }

    public static int getMakeIdByName(String name) {
        int result = 0;
        result = ParkingDatabase.getInstance(TPApplication.getInstance()).makesAndModelsDao().getMakeIdByName(name);
        return result;
    }

    public static int getMakeIdByCode(String makeCode) {
        int result = 0;
        result = ParkingDatabase.getInstance(TPApplication.getInstance()).makesAndModelsDao().getMakeIdByCode(makeCode);
        return result;
    }

    public static int getModelIdByCode(String modelCode) {
        int result = 0;
        result = ParkingDatabase.getInstance(TPApplication.getInstance()).makesAndModelsDao().getModelIdByCode(modelCode);
        return result;
    }

    public static String getMakeCodeByName(String name) {
        String result = "";
        result = ParkingDatabase.getInstance(TPApplication.getInstance()).makesAndModelsDao().getMakeCodeByName(name);
        return result;
    }

    public static String getMakeCodeById(int makeId) {
        String result = "";
        result = ParkingDatabase.getInstance(TPApplication.getInstance()).makesAndModelsDao().getMakeCodeById(makeId);
        return result;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).makesAndModelsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).makesAndModelsDao().removeById(id);
    }

    public static String getMakeStandardName(String makeName) {
        if (makeName == null) {
            return null;
        }
        final Map<String, String> map = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        map.put("CHEVROLET", "CHEVY");
        map.put("chevrolet_sonic", "CHEVY");
        map.put("chevrolet_aveo", "CHEVY");
        map.put("jeep_compass", "JEEP");
        map.put("jeep_liberty", "JEEP");
        map.put("volvo_c30", "VOLVO");
        map.put("saturn_ion", "SATURN");
        map.put("MERCEDES-BENZ", "MERCEDES BENZ");
        map.put("fiat_500", "FIAT");
        map.put("scion_xa", "SCION");
        String make = map.get(makeName);
        if (make == null) {
            return "UNK";
        }
        return make;
    }

    public static void insertMakeAndModel(MakeAndModel MakeAndModel) {
        new MakeAndModel.InsertMakeAndModel(MakeAndModel).execute();
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
        values.put("make_id", this.makeId);
        values.put("custid", this.custId);
        values.put("make", this.makeTitle);
        values.put("make_code", this.makeCode);
        values.put("model", this.modelTitle);
        values.put("model_code", this.modelCode);
        values.put("order_number", this.orderNumber);
        return values;
    }

    public int getMakeId() {
        return makeId;
    }

    public void setMakeId(int makeId) {
        this.makeId = makeId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getMakeTitle() {
        return makeTitle;
    }

    public void setMakeTitle(String makeTitle) {
        this.makeTitle = makeTitle;
    }

    public String getMakeCode() {
        return makeCode;
    }

    public void setMakeCode(String makeCode) {
        this.makeCode = makeCode;
    }

    public String getModelTitle() {
        return modelTitle;
    }

    public void setModelTitle(String modelTitle) {
        this.modelTitle = modelTitle;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    private static class InsertMakeAndModel extends AsyncTask<Void, Void, Void> {
        private MakeAndModel MakeAndModel;

        public InsertMakeAndModel(MakeAndModel MakeAndModel) {
            this.MakeAndModel = MakeAndModel;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).makesAndModelsDao().insertMakesAndModel(MakeAndModel);
            return null;
        }
    }

}
