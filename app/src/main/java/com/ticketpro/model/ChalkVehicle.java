package com.ticketpro.model;

import android.content.ContentValues;
import android.content.Context;
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
import com.ticketpro.parking.service.ServiceHandler;
import com.ticketpro.parking.service.ServiceHandlerImpl;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "chalk_vehicles")
public class ChalkVehicle {
    @PrimaryKey
    @ColumnInfo(name = "chalk_id")
    @SerializedName("chalk_id")
    @Expose
    private long chalkId;
    @ColumnInfo(name = "userid")
    @SerializedName("userid")
    @Expose
    private int userId;
    @ColumnInfo(name = "device_id")
    @SerializedName("device_id")
    @Expose
    private int deviceId;
    @ColumnInfo(name = "chalk_date")
    @SerializedName("chalk_date")
    @Expose
    private Date chalkDate;
    @ColumnInfo(name = "permit")
    @SerializedName("permit")
    @Expose
    private String permit;
    @ColumnInfo(name = "plate")
    @SerializedName("plate")
    @Expose
    private String plate;
    @ColumnInfo(name = "vin")
    @SerializedName("vin")
    @Expose
    private String vin;
    @ColumnInfo(name = "state_id")
    @SerializedName("state_id")
    @Expose
    private int stateId;
    @Ignore
    private String stateCode;
    @ColumnInfo(name = "zone_id")
    @SerializedName("zone_id")
    @Expose
    private int zoneId;
    @Ignore
    private String zoneCode;
    @ColumnInfo(name = "expiration")
    @SerializedName("expiration")
    @Expose
    private Date expiration;
    @ColumnInfo(name = "duration_id")
    @SerializedName("duration_id")
    @Expose
    private int durationId;
    @ColumnInfo(name = "duration_code")
    private String durationCode;
    @ColumnInfo(name = "space")
    @SerializedName("space")
    @Expose
    private String space;
    @ColumnInfo(name = "meter")
    @SerializedName("meter")
    @Expose
    private String meter;
    @ColumnInfo(name = "tire")
    @SerializedName("tire")
    @Expose
    private String tire;
    @ColumnInfo(name = "chalkx")
    @SerializedName("chalkx")
    @Expose
    private int chalkx;
    @ColumnInfo(name = "chalky")
    @SerializedName("chalky")
    @Expose
    private int chalky;
    @ColumnInfo(name = "stemx")
    @SerializedName("stemx")
    @Expose
    private int stemx;
    @ColumnInfo(name = "stemy")
    @SerializedName("stemy")
    @Expose
    private int stemy;
    @ColumnInfo(name = "chalk_type")
    @SerializedName("chalk_type")
    @Expose
    private String chalkType;
    @ColumnInfo(name = "location")
    @SerializedName("location")
    @Expose
    private String location;
    @ColumnInfo(name = "street_prefix")
    @SerializedName("street_prefix")
    @Expose
    private String streetPrefix;
    @ColumnInfo(name = "street_suffix")
    @SerializedName("street_suffix")
    @Expose
    private String streetSuffix;
    @ColumnInfo(name = "street_number")
    @SerializedName("street_number")
    @Expose
    private String streetNumber;
    @ColumnInfo(name = "direction")
    @SerializedName("direction")
    @Expose
    private String direction;
    @ColumnInfo(name = "latitude")
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @ColumnInfo(name = "gpstime")
    @SerializedName("gpstime")
    @Expose
    private Date gpstime;
    @ColumnInfo(name = "is_gps_location")
    @SerializedName("is_gps_location")
    @Expose
    private String isGPSLocation;
    @ColumnInfo(name = "is_expired")
    private String isExpired;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "wheel_size")
    @SerializedName("wheel_size")
    @Expose
    private int wheelSize;
    @Ignore
    private boolean isLPRCaptured;
    @ColumnInfo(name = "notes")
    @SerializedName("notes")
    @Expose
    private String notes;
    @ColumnInfo(name = "make_code")
    @SerializedName("make_code")
    @Expose
    private String makeCode;
    @ColumnInfo(name = "color_code")
    @SerializedName("color_code")
    @Expose
    private String colorCode;
    @ColumnInfo(name = "make")
    private String make;
    @ColumnInfo(name = "color")
    private String color;

    @ColumnInfo(name = "sync_status")
    @SerializedName("sync_status")
    @Expose
    private String syncStatus;

    @Ignore
    @SerializedName("chalkComments")
    @Expose
    private ArrayList<ChalkComment> comments = new ArrayList<ChalkComment>();
    @Ignore
    @SerializedName("chalkPictures")
    @Expose
    private ArrayList<ChalkPicture> chalkPictures = new ArrayList<ChalkPicture>();

    public ChalkVehicle() {

    }

    public ChalkVehicle(JSONObject object) throws Exception {
        this.setChalkId(object.getLong("chalk_id"));
        this.setDeviceId(object.getInt("device_id"));
        this.setUserId(object.getInt("userid"));
        this.setChalkDate(DateUtil.getDateFromSQLString(object.getString("chalk_date")));
        this.setPermit(object.getString("permit"));
        this.setVin(object.getString("vin"));
        this.setPlate(object.getString("plate"));
        this.setStateId(!object.isNull("state_id") ? object.getInt("state_id") : 0);
        this.setZoneId(!object.isNull("zone_id") ? object.getInt("zone_id") : 0);
        this.setExpiration(DateUtil.getDateFromSQLString(object.getString("expiration")));
        this.setDurationId(!object.isNull("duration_id") ? object.getInt("duration_id") : 0);
        this.setSpace(object.getString("space"));
        this.setMeter(object.getString("meter"));
        this.setTire(object.getString("tire"));
        this.setChalkx(!object.isNull("chalkx") ? object.getInt("chalkx") : 0);
        this.setChalky(!object.isNull("chalky") ? object.getInt("chalky") : 0);
        this.setStemx(!object.isNull("stemx") ? object.getInt("stemx") : 0);
        this.setStemy(!object.isNull("stemy") ? object.getInt("stemy") : 0);
        this.setChalkType(object.getString("chalk_type"));
        this.setLocation(object.getString("location"));
        this.setStreetPrefix(object.getString("street_prefix"));
        this.setStreetSuffix(object.getString("street_suffix"));
        this.setStreetNumber(object.getString("street_number"));
        this.setDirection(object.getString("direction"));
        this.setLatitude(object.getString("latitude"));
        this.setLongitude(object.getString("longitude"));
        this.setGpstime(DateUtil.getDateFromSQLString(object.getString("gpstime")));
        this.setIsGPSLocation(object.getString("is_gps_location"));
        this.setIsExpired(object.getString("is_expired"));
        this.setWheelSize(!object.isNull("wheel_size") ? object.getInt("wheel_size") : 0);
        this.setCustId(!object.isNull("custid") ? object.getInt("custid") : 0);
        this.setNotes(!object.isNull("notes") ? object.getString("notes") : null);
        this.setMakeCode(!object.isNull("make_code") ? object.getString("make_code") : null);
        this.setColorCode(!object.isNull("color_code") ? object.getString("color_code") : null);
    }

    public static ArrayList<ChalkVehicle> getChalkVehicles(int userId) throws Exception {
        ArrayList<ChalkVehicle> list = (ArrayList<ChalkVehicle>) ParkingDatabase.getInstance(TPApplication.getInstance()).chalkVehiclesDao().getChalkVehicles(userId);
        for (ChalkVehicle object : list) {
            //Update Chalk Comments.
            try {
                object.setComments(ChalkComment.getChalkComments(object.chalkId));
            } catch (Exception e) {
                Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
            }

            //Get all chalk pictures
            try {
                object.setChalkPictures(ChalkPicture.getChalkPictures(object.chalkId));
            } catch (Exception e) {
                Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
            }
        }
        return list;
    }

    public static ArrayList<ChalkVehicle> getAllChalkedVehicle() throws Exception {
        ArrayList<ChalkVehicle> list;
        list = (ArrayList<ChalkVehicle>) ParkingDatabase.getInstance(TPApplication.getInstance()).chalkVehiclesDao().getAllChalkedVehicle();
        for (ChalkVehicle object : list) {
            //Update Chalk Comments.
            try {
                object.setComments(ChalkComment.getChalkComments(object.chalkId));
            } catch (Exception e) {
                Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
            }
            //Get all chalk pictures
            try {
                object.setChalkPictures(ChalkPicture.getChalkPictures(object.chalkId));
            } catch (Exception e) {
                Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
            }
        }
        return list;
    }

    /**
     *
     * @param values
     */
    public static void updateChalkStatus(long chalkId, String values) {
        try {
            ParkingDatabase.getInstance(TPApplication.getInstance()).chalkVehiclesDao().updateChalkVehicleStatus(values,chalkId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<ChalkVehicle> getPendingChalkedVehicle() throws Exception {
        ArrayList<ChalkVehicle> list;
        list = (ArrayList<ChalkVehicle>) ParkingDatabase.getInstance(TPApplication.getInstance()).chalkVehiclesDao().getPendingChalkedVehicle();
        for (ChalkVehicle object : list) {
            //Update Chalk Comments.
            try {
                object.setComments(ChalkComment.getChalkComments(object.chalkId));
            } catch (Exception e) {
                Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
            }
            //Get all chalk pictures
            try {
                object.setChalkPictures(ChalkPicture.getChalkPictures(object.chalkId));
            } catch (Exception e) {
                Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
            }
        }
        return list;
    }
    public static ArrayList<ChalkVehicle> getPendingOldChalkedVehicle() throws Exception {
        ArrayList<ChalkVehicle> list;
        list = (ArrayList<ChalkVehicle>) ParkingDatabase.getInstance(TPApplication.getInstance()).chalkVehiclesDao().getPendingOldChalkedVehicle();
        for (ChalkVehicle object : list) {
            //Update Chalk Comments.
            try {
                object.setComments(ChalkComment.getChalkComments(object.chalkId));
            } catch (Exception e) {
                Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
            }
            //Get all chalk pictures
            try {
                object.setChalkPictures(ChalkPicture.getChalkPictures(object.chalkId));
            } catch (Exception e) {
                Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
            }
        }
        return list;
    }



    public static ArrayList<ChalkVehicle> getPendingPIChalkedVehicle() throws Exception {
        ArrayList<ChalkVehicle> list;
        list = (ArrayList<ChalkVehicle>) ParkingDatabase.getInstance(TPApplication.getInstance()).chalkVehiclesDao().getPendingPIChalkedVehicle();
        for (ChalkVehicle object : list) {
            //Update Chalk Comments.
            try {
                object.setComments(ChalkComment.getChalkComments(object.chalkId));
            } catch (Exception e) {
                Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
            }
            //Get all chalk pictures
            try {
                object.setChalkPictures(ChalkPicture.getChalkPictures(object.chalkId));
            } catch (Exception e) {
                Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
            }
        }
        return list;
    }

    public static ArrayList<ChalkVehicle> getChalkByType(int userId, String type, Context ctx) throws Exception {
        ArrayList<ChalkVehicle> list = new ArrayList<ChalkVehicle>();

        list = (ArrayList<ChalkVehicle>) ParkingDatabase.getInstance(TPApplication.getInstance()).chalkVehiclesDao().getChalkByType(type);
        for (ChalkVehicle object : list) {
            //Update Chalk Comments.
            try {
                object.setComments(ChalkComment.getChalkComments(object.chalkId));
            } catch (Exception e) {
                Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
            }

            //Get all chalk pictures
            try {
                object.setChalkPictures(ChalkPicture.getChalkPictures(object.chalkId));
            } catch (Exception e) {
                Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
            }
        }

        return list;
    }


    public static boolean isValidChalkId(long chalkId) throws Exception {
        ChalkVehicle chalkVehicle = ParkingDatabase.getInstance(TPApplication.getInstance()).chalkVehiclesDao().getChalkVehicleByPrimaryKey(chalkId);
        return chalkVehicle != null;
    }

    public static ChalkVehicle getChalkVehicleById(long chalkId) throws Exception {
        ChalkVehicle object;

        object = ParkingDatabase.getInstance(TPApplication.getInstance()).chalkVehiclesDao().getChalkVehicleById(chalkId);
        //Update Chalk Comments.
        try {

            object.setComments(ChalkComment.getChalkComments(object.chalkId));
        } catch (Exception e) {
            Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
        }

        //Get all chalk pictures
        try {
            object.setChalkPictures(ChalkPicture.getChalkPictures(object.chalkId));
        } catch (Exception e) {
            Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
        }
        return object;
    }


    public static ChalkVehicle getChalkVehicleByPrimaryKey(String chalkId) throws TPException {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).chalkVehiclesDao().getChalkVehicleByPrimaryKey(Long.parseLong(chalkId));
    }

    public static ChalkVehicle searchPreviousChalkByPlate(String plate, String state) throws TPException {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).chalkVehiclesDao().searchPreviousChalkByPlate(plate, state);
    }

    public static long getNextPrimaryId() throws Exception {
        return new Date().getTime();
    }

    public static void removeAllChalks() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).chalkVehiclesDao().removeAll();
        ParkingDatabase.getInstance(TPApplication.getInstance()).chalkPicturesDao().removeAll();
        ParkingDatabase.getInstance(TPApplication.getInstance()).chalkCommentsDao().removeAll();
    }

    public static void removeChalkById(final long chalkId, final String item) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).chalkVehiclesDao().removeChalkVehicleById(chalkId);
        ParkingDatabase.getInstance(TPApplication.getInstance()).chalkPicturesDao().removeChalkPictureById(chalkId);
        ParkingDatabase.getInstance(TPApplication.getInstance()).chalkCommentsDao().removeChalkCommentById(chalkId);

        //Update Chalk Status on SERVER
        new Thread(() -> {
            ServiceHandler service = new ServiceHandlerImpl();
            try {
                service.updateChalkStatus(chalkId, "N", item);
            } catch (Exception e) {
                Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
            }
        }).start();
    }

    public static Completable insertChalkVehicle(final ChalkVehicle ChalkVehicle) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                ParkingDatabase.getInstance(TPApplication.getInstance()).chalkVehiclesDao().insertChalkVehicle(ChalkVehicle).subscribeOn(Schedulers.io()).subscribe();
            }
        });
    }


    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();
        values.put("chalk_id", this.chalkId);
        values.put("device_id", this.deviceId);
        values.put("userid", this.userId);
        values.put("chalk_date", DateUtil.getSQLStringFromDate2(this.chalkDate));
        values.put("permit", this.permit);
        values.put("vin", this.vin);
        values.put("plate", this.plate);
        values.put("state_id", this.stateId);
        values.put("zone_id", this.zoneId);
        values.put("expiration", DateUtil.getSQLStringFromDate2(this.expiration));
        values.put("duration_id", this.durationId);
        values.put("space", this.space);
        values.put("meter", this.meter);
        values.put("tire", this.tire);
        values.put("chalkx", this.chalkx);
        values.put("chalky", this.chalky);
        values.put("stemx", this.stemx);
        values.put("stemy", this.stemy);
        values.put("chalk_type", this.chalkType);
        values.put("location", this.location);
        values.put("street_prefix", this.streetPrefix);
        values.put("street_suffix", this.streetSuffix);
        values.put("street_number", this.streetNumber);
        values.put("direction", this.direction);
        values.put("latitude", this.latitude);
        values.put("longitude", this.longitude);
        values.put("gpstime", DateUtil.getSQLStringFromDate2(this.gpstime));
        values.put("is_gps_location", this.isGPSLocation);
        values.put("wheel_size", this.wheelSize);
        values.put("custid", this.custId);
        values.put("notes", this.notes);
        values.put("make_code", this.makeCode);
        values.put("color_code", this.colorCode);
        values.put("color", this.color);
        values.put("make", this.make);

        return values;
    }

    public JSONObject getJSONObject() {
        JSONObject values = new JSONObject();
        try {

            values.put("chalk_id", this.chalkId);
            values.put("device_id", this.deviceId);
            values.put("userid", this.userId);
            values.put("chalk_date", DateUtil.getSQLStringFromDate2(this.chalkDate));
            values.put("permit", this.permit);
            values.put("vin", this.vin);
            values.put("plate", this.plate);
            values.put("state_id", this.stateId);
            values.put("zone_id", this.zoneId);
            values.put("expiration", DateUtil.getSQLStringFromDate2(this.expiration));
            values.put("duration_id", this.durationId);
            values.put("meter", this.meter);
            values.put("tire", this.tire);
            values.put("chalkx", this.chalkx);
            values.put("chalky", this.chalky);
            values.put("stemx", this.stemx);
            values.put("stemy", this.stemy);
            values.put("chalk_type", this.chalkType);
            values.put("location", this.location != null ? this.location.toUpperCase() : "");
            values.put("street_prefix", this.streetPrefix != null ? this.streetPrefix.toUpperCase() : "");
            values.put("street_suffix", this.streetSuffix != null ? this.streetSuffix.toUpperCase() : "");
            values.put("street_number", this.streetNumber != null ? this.streetNumber.toUpperCase() : "");
            values.put("direction", this.direction != null ? this.direction.toUpperCase() : "");
            values.put("space", this.space != null ? this.space.toUpperCase() : "");
            values.put("latitude", this.latitude);
            values.put("longitude", this.longitude);
            values.put("gpstime", DateUtil.getSQLStringFromDate2(this.gpstime));
            values.put("is_gps_location", this.isGPSLocation);
            values.put("wheel_size", this.wheelSize);
            values.put("custid", this.custId);
            values.put("notes", this.notes);
            values.put("make_code", this.makeCode);
            values.put("color_code", this.colorCode);

        } catch (Exception e) {
            Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
        }

        return values;
    }

    public long getChalkId() {
        return chalkId;
    }

    public void setChalkId(long chalkId) {
        this.chalkId = chalkId;
    }

    public int getUserId() {
        return userId;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public Date getChalkDate() {
        return chalkDate;
    }

    public void setChalkDate(Date chalkDate) {
        this.chalkDate = chalkDate;
    }

    public String getPermit() {
        return permit;
    }

    public void setPermit(String permit) {
        this.permit = permit;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

    public String getTire() {
        return tire;
    }

    public void setTire(String tire) {
        this.tire = tire;
    }

    public int getChalkx() {
        return chalkx;
    }

    public void setChalkx(int chalkx) {
        this.chalkx = chalkx;
    }

    public int getChalky() {
        return chalky;
    }

    public void setChalky(int chalky) {
        this.chalky = chalky;
    }

    public String getChalkType() {
        return chalkType;
    }

    public void setChalkType(String chalkType) {
        this.chalkType = chalkType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStreetPrefix() {
        return streetPrefix;
    }

    public void setStreetPrefix(String streetPrefix) {
        this.streetPrefix = streetPrefix;
    }

    public String getStreetSuffix() {
        return streetSuffix;
    }

    public void setStreetSuffix(String streetSuffix) {
        this.streetSuffix = streetSuffix;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getGpstime() {
        return gpstime;
    }

    public void setGpstime(Date gpstime) {
        this.gpstime = gpstime;
    }

    public String getIsGPSLocation() {
        return isGPSLocation;
    }

    public void setIsGPSLocation(String isGPSLocation) {
        this.isGPSLocation = isGPSLocation;
    }

    public int getDurationId() {
        return durationId;
    }

    public void setDurationId(int durationId) {
        this.durationId = durationId;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public ArrayList<ChalkComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<ChalkComment> comments) {
        this.comments = comments;
    }

    public int getStemx() {
        return stemx;
    }

    public void setStemx(int stemx) {
        this.stemx = stemx;
    }

    public int getStemy() {
        return stemy;
    }

    public void setStemy(int stemy) {
        this.stemy = stemy;
    }

    public String getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(String isExpired) {
        this.isExpired = isExpired;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getDurationCode() {
        return durationCode;
    }

    public void setDurationCode(String durationCode) {
        this.durationCode = durationCode;
    }

    public boolean isGPSLocation() {
        if (this.isGPSLocation != null && this.isGPSLocation.equals("Y"))
            return true;

        return false;
    }

    public boolean isExpired() {
        if (this.isExpired != null && this.isExpired.equals("Y"))
            return true;

        return false;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public ArrayList<ChalkPicture> getChalkPictures() {
        return chalkPictures;
    }

    public void setChalkPictures(ArrayList<ChalkPicture> chalkPictures) {
        this.chalkPictures = chalkPictures;
    }

    public int getWheelSize() {
        return wheelSize;
    }

    public void setWheelSize(int wheelSize) {
        this.wheelSize = wheelSize;
    }

    public boolean isLPRCaptured() {
        return isLPRCaptured;
    }

    public void setLPRCaptured(boolean isLPRCaptured) {
        this.isLPRCaptured = isLPRCaptured;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getMakeCode() {
        return makeCode;
    }

    public void setMakeCode(String makeCode) {
        this.makeCode = makeCode;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getMake() {
        if (make == null) {
            MakeAndModel makeModel = MakeAndModel.getMakeByCode(makeCode);
            if (makeModel != null) {
                make = makeModel.getMakeTitle();
            }
        }

        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getColor() {
        if (color == null) {
            Color colorObj = Color.getColorByCode(colorCode);
            if (colorObj != null) {
                color = colorObj.getTitle();
            }
        }

        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public static class PlateComparator implements Comparator<ChalkVehicle> {
        @Override
        public int compare(ChalkVehicle chalkVehicle1, ChalkVehicle chalkVehicle2) {
            return chalkVehicle1.getPlate().compareToIgnoreCase(chalkVehicle2.getPlate());
        }
    }

    public static class DateComparator implements Comparator<ChalkVehicle> {
        @Override
        public int compare(ChalkVehicle chalkVehicle1, ChalkVehicle chalkVehicle2) {
            return chalkVehicle1.getChalkDate().compareTo(chalkVehicle2.getChalkDate());
        }
    }

    public static class ZoneComparator implements Comparator<ChalkVehicle> {
        @Override
        public int compare(ChalkVehicle chalkVehicle1, ChalkVehicle chalkVehicle2) {
            return chalkVehicle1.getDurationCode().compareToIgnoreCase(chalkVehicle2.getDurationCode());
        }
    }

    public static class LocationComparator implements Comparator<ChalkVehicle> {
        @Override
        public int compare(ChalkVehicle chalkVehicle1, ChalkVehicle chalkVehicle2) {
            return chalkVehicle1.getLocation().compareToIgnoreCase(chalkVehicle2.getLocation());
        }
    }

/*    private static class InsertChalkVehicle extends AsyncTask<Void,Void,Void> {
        private ChalkVehicle ChalkVehicle;

        public InsertChalkVehicle(ChalkVehicle ChalkVehicle) {
            this.ChalkVehicle = ChalkVehicle;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).chalkVehiclesDao().insertChalkVehicle(ChalkVehicle);
            return null;
        }
    }*/
}
