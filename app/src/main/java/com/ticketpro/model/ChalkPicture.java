package com.ticketpro.model;

import android.content.ContentValues;
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
import com.ticketpro.util.DateUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "chalk_pictures")
public class ChalkPicture {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "picture_id")
    @SerializedName("picture_id")
    @Expose
    private int pictureId;
    @ColumnInfo(name = "chalk_id")
    @SerializedName("chalk_id")
    @Expose
    private long chalkId;
    @ColumnInfo(name = "chalk_time")
    @SerializedName("chalk_time")
    @Expose
    private Date chalkDate;
    @ColumnInfo(name = "location")
    @SerializedName("location")
    @Expose
    private String location;
    @ColumnInfo(name = "latitute")
    @SerializedName("latitute")
    @Expose
    private String latitude;
    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @ColumnInfo(name = "image_path")
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @ColumnInfo(name = "image_resolution")
    @SerializedName("image_resolution")
    @Expose
    private String imageResolution;
    @ColumnInfo(name = "image_size")
    @SerializedName("image_size")
    @Expose
    private String imageSize;
    @ColumnInfo(name = "sync_status")
    @SerializedName("sync_status")
    @Expose
    private String syncStatus;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;

    @ColumnInfo(name = "download_image")
    @SerializedName("download_image")
    @Expose
    private String downloadImage;
    @Ignore
    private String imgName;

    public ChalkPicture() {

    }

    public ChalkPicture(JSONObject object) throws Exception {

        this.setChalkId(object.getLong("chalk_id"));
        this.setPictureId(object.getInt("picture_id"));
        this.setChalkDate(DateUtil.getDateFromSQLString(object.getString("chalk_time")));
        this.setLocation(object.getString("location"));
        this.setLatitude(object.getString("latitute"));
        this.setLongitude(object.getString("longitude"));
        this.setImagePath(object.getString("image_path"));
        this.setImageResolution(object.getString("image_resolution"));
        this.setImageSize(object.getString("image_size"));
        this.setSyncStatus(object.getString("sync_status"));
        this.setCustId(object.getInt("custid"));
    }


    public static ArrayList<ChalkPicture> getChalkPictures(long chalkId) throws Exception {
        return (ArrayList<ChalkPicture>) ParkingDatabase.getInstance(TPApplication.getInstance()).chalkPicturesDao().getChalkPictures(chalkId);
    }

    //Get all pending chalk image.
    public static ArrayList<ChalkPicture> getPendingChalkPictures() throws Exception {
        return (ArrayList<ChalkPicture>) ParkingDatabase.getInstance(TPApplication.getInstance()).chalkPicturesDao().getPendingChalkImageList();
    }

 //Get all pending chalk image.
    public static ArrayList<ChalkPicture> getPendingChalkPicturesById(long id) throws Exception {
        return (ArrayList<ChalkPicture>) ParkingDatabase.getInstance(TPApplication.getInstance()).chalkPicturesDao().getPendingChalkImageListById(id);
    }




    public static ChalkPicture getChalkPictureById(long chalkId) throws Exception {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).chalkPicturesDao().getChalkPictureById(chalkId);
    }


    public static ChalkPicture getChalkPictureByPrimaryKey(String pictureId) throws TPException {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).chalkPicturesDao().getChalkPictureByPrimaryKey(pictureId);
    }

    public static int getNextPrimaryId() {
        int nextId = 0;
        nextId = ParkingDatabase.getInstance(TPApplication.getInstance()).chalkPicturesDao().getNextPrimaryId();
        return nextId + 1;
    }

    public static void removeChalkPictureById(long chalkId) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).chalkPicturesDao().removeChalkPictureById(chalkId);
    }

    public static Completable insertChalkPicture(final ChalkPicture ChalkPicture) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                ParkingDatabase.getInstance(TPApplication.getInstance()).chalkPicturesDao().insertChalkPicture(ChalkPicture).subscribeOn(Schedulers.io()).subscribe();
            }
        }).subscribeOn(Schedulers.io());
        //new ChalkPicture.InsertChalkPicture(ChalkPicture).execute();
    }

    public static void __updatePictureStatus(int sNo, long chalkId, String uploadFlag) {
        try {
            ParkingDatabase.getInstance(TPApplication.getInstance()).chalkPicturesDao().updateChalkPictureStatus(chalkId, uploadFlag,sNo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();

            values.put("picture_id", this.pictureId);


        values.put("chalk_id", this.chalkId);
        values.put("chalk_time", DateUtil.getSQLStringFromDate2(this.chalkDate));
        values.put("location", this.location);
        values.put("longitude", this.longitude);
        values.put("latitute", this.latitude);
        values.put("image_path", this.imagePath);
        values.put("image_resolution", this.imageResolution);
        values.put("image_size", this.imageSize);
        values.put("sync_status", this.syncStatus);
        values.put("custid", this.custId);

        return values;
    }

    public JSONObject getJSONObject() {
        JSONObject values = new JSONObject();
        try {
            values.put("picture_id", this.pictureId);
            values.put("chalk_id", this.chalkId);
            values.put("chalk_time", DateUtil.getSQLStringFromDate2(this.chalkDate));
            values.put("location", this.location);
            values.put("longitude", this.longitude);
            values.put("latitute", this.latitude);
            values.put("image_path", this.imagePath);

            if (this.imagePath != null) {
                String[] tokens = this.imagePath.split("/");
                if (tokens.length > 0)
                    values.put("image_path", tokens[tokens.length - 1]);
            }

            values.put("image_resolution", this.imageResolution);
            values.put("image_size", this.imageSize);
            values.put("sync_status", this.syncStatus);
            values.put("custid", this.custId);
        } catch (Exception e) {
        }

        return values;
    }

    public String getDownloadImage() {
        return downloadImage;
    }

    public void setDownloadImage(String downloadImage) {
        this.downloadImage = downloadImage;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public long getChalkId() {
        return chalkId;
    }

    public void setChalkId(long chalkId) {
        this.chalkId = chalkId;
    }

    public Date getChalkDate() {
        return chalkDate;
    }

    public void setChalkDate(Date chalkDate) {
        this.chalkDate = chalkDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageResolution() {
        return imageResolution;
    }

    public void setImageResolution(String imageResolution) {
        this.imageResolution = imageResolution;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    private static class InsertChalkPicture extends AsyncTask<Void, Void, Void> {
        private ChalkPicture ChalkPicture;

        public InsertChalkPicture(ChalkPicture ChalkPicture) {
            this.ChalkPicture = ChalkPicture;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).chalkPicturesDao().insertChalkPicture(ChalkPicture);
            return null;
        }
    }
}
