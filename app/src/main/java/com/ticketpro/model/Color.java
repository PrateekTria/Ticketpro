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

@Entity(tableName = "colors")
public class Color {
    @PrimaryKey
    @ColumnInfo(name = "color_id")
    @SerializedName("color_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "color")
    @SerializedName("color")
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

    public Color() {

    }

    public Color(JSONObject object) throws Exception {
        this.setId(object.getInt("color_id"));
        this.setCustId(object.getInt("custid"));
        this.setTitle(object.getString("color"));
        this.setCode(object.getString("code"));
        this.setOrderNumber(!object.isNull("order_number") ? object.getInt("order_number") : 0);
    }

    public static ArrayList<Color> getColors(int custId) throws Exception {
        return (ArrayList<Color>) ParkingDatabase.getInstance(TPApplication.getInstance()).colorsDao().getColors();
    }

    public static Color getColorById(int colorId) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).colorsDao().getColorById(colorId);
    }

    public static Color getColorByCode(String colorCode) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).colorsDao().getColorByCode(colorCode);
    }

    public static Color getColorByTitle(String colorTitle) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).colorsDao().getColorByTitle(colorTitle);
    }

    public static int getColorIdByName(String name) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).colorsDao().getColorIdByName(name);
    }

    public static int getColorIdByCode(String code) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).colorsDao().getColorIdByCode(code);
    }

    public static String getColorCodeByName(String name) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).colorsDao().getColorCodeByName(name);
    }

    public static String getColorCodeById(int colorId) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).colorsDao().getColorCodeById(colorId);
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).colorsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).colorsDao().removeById(id);
    }

    public static void insertColor(Color Color) {
        new Color.InsertColor(Color).execute();
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
        values.put("color_id", this.id);
        values.put("custid", this.custId);
        values.put("color", this.title);
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

    public String getColorStandardName(String colorName) {
        if (colorName == null) {
            return null;
        }
        final Map<String, String> map = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        map.put("_ignore_", "UNK");

        String color = map.get(colorName);
        if (color == null) {
            return "UNK";
        }
        return color;
    }

    private static class InsertColor extends AsyncTask<Void, Void, Void> {
        private Color Color;

        public InsertColor(Color Color) {
            this.Color = Color;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).colorsDao().insertColor(Color);
            return null;
        }
    }
}
