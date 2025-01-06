package com.ticketpro.model;

import android.content.ContentValues;
import android.os.AsyncTask;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;

import org.json.JSONObject;

import java.util.ArrayList;

@Entity(tableName = "maintenance_pictures")
public class MaintenancePicture {
    @PrimaryKey
    @ColumnInfo(name = "picture_id")
    private int pictureId;
    @ColumnInfo(name = "maintenance_id")
    private long maintenanceId;
    @ColumnInfo(name = "image_path")
    private String imagePath;
    @ColumnInfo(name = "image_size")
    private String imageSize;
    @ColumnInfo(name = "image_resolution")
    private String imageResolution;

    public MaintenancePicture() {

    }

    public MaintenancePicture(JSONObject object) throws Exception {
        this.setPictureId(object.getInt("picture_id"));
        this.setMaintenanceId(object.getInt("maintenance_id"));
        this.setImagePath(object.getString("image_path"));
        this.setImageSize(object.getString("image_size"));
        this.setImageResolution(object.getString("image_resolution"));
    }

    public static ArrayList<MaintenancePicture> getPictures(long maintenanceId) throws Exception {
        ArrayList<MaintenancePicture> list = new ArrayList<MaintenancePicture>();
        list = (ArrayList<MaintenancePicture>) ParkingDatabase.getInstance(TPApplication.getInstance()).maintenancePicturesDao().getPictures(maintenanceId);
        return list;
    }

    public static int getNextPrimaryId() {
        int nextId = 0;
        nextId = ParkingDatabase.getInstance(TPApplication.getInstance()).maintenancePicturesDao().getNextPrimaryId();
        return nextId + 1;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).maintenancePicturesDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).maintenancePicturesDao().removeById(id);
    }

    public static void insertMaintenancePicture(MaintenancePicture MaintenancePicture) {
        new MaintenancePicture.InsertMaintenancePicture(MaintenancePicture).execute();
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        if (pictureId == 0) {
            values.put("picture_id", getNextPrimaryId());
        } else {
            values.put("picture_id", getPictureId());
        }

        values.put("maintenance_id", getMaintenanceId());
        values.put("image_path", getImagePath());
        values.put("image_size", getImageSize());
        values.put("image_resolution", getImageResolution());

        return values;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public long getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(long maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getImageResolution() {
        return imageResolution;
    }

    public void setImageResolution(String imageResolution) {
        this.imageResolution = imageResolution;
    }

    private static class InsertMaintenancePicture extends AsyncTask<Void, Void, Void> {
        private MaintenancePicture MaintenancePicture;

        public InsertMaintenancePicture(MaintenancePicture MaintenancePicture) {
            this.MaintenancePicture = MaintenancePicture;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).maintenancePicturesDao().insertMaintenancePicture(MaintenancePicture);
            return null;
        }
    }
}
