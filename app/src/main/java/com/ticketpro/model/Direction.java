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

@Entity(tableName = "directions")
public class Direction {
    @PrimaryKey
    @ColumnInfo(name = "direction_id")
    @SerializedName("direction_id")
    @Expose
    private int id;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "direction")
    @SerializedName("direction")
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

    public Direction() {

    }

    public Direction(JSONObject object) throws Exception {

        this.setId(object.getInt("direction_id"));
        this.setCustId(object.getInt("custid"));
        this.setTitle(object.getString("direction"));
        this.setCode(object.getString("code"));
        this.setOrderNumber(!object.isNull("order_number") ? object.getInt("order_number") : 0);

    }

    public static ArrayList<Direction> getDirections() throws Exception {
        return (ArrayList<Direction>) ParkingDatabase.getInstance(TPApplication.getInstance()).directionsDao().getDirections();
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).directionsDao().removeAll();
		/*SQLiteDatabase sqliteDatabase = DatabaseHelper.getInstance().openReadableDatabase();
		sqliteDatabase.delete("directions", "", null);
		//sqliteDatabase.close();*/
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).directionsDao().removeById(id);
		/*SQLiteDatabase sqliteDatabase = DatabaseHelper.getInstance().openReadableDatabase();
		sqliteDatabase.delete("directions", "direction_id="+id, null);
		//sqliteDatabase.close();*/
    }

    public static void insertDirection(Direction Direction) {
        new Direction.InsertDirection(Direction).execute();
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
        values.put("direction_id", this.id);
        values.put("custid", this.custId);
        values.put("direction", this.title);
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

    private static class InsertDirection extends AsyncTask<Void, Void, Void> {
        private Direction Direction;

        public InsertDirection(Direction Direction) {
            this.Direction = Direction;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).directionsDao().insertDirection(Direction);
            return null;
        }
    }

}
