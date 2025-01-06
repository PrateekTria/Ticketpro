package com.ticketpro.model;

import android.content.ContentValues;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "special_activities_pictures")
public class SpecialActivityPicture implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "picture_id")
    private int pictureId;
    @ColumnInfo(name = "report_id")
    private int reportId;
    @ColumnInfo(name = "picture_date")
    private String pictureDate;
    @ColumnInfo(name = "image_path")
    private String imagePath;
    @ColumnInfo(name = "image_resolution")
    private String imageResulation;
    @ColumnInfo(name = "image_size")
    private String imageSize;
    @ColumnInfo(name = "custid")
    private int custid;
    @ColumnInfo(name = "image_name")
    private String imageName;

    public SpecialActivityPicture() {
    }

    public static ArrayList<SpecialActivityPicture> getListOfImage(int report_id) {
        ArrayList<SpecialActivityPicture> objectList;
        objectList = (ArrayList<SpecialActivityPicture>) ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityPictureDao().getListOfImage(report_id);
        return objectList;
    }


    public static int getNextPrimaryId() {
        int nextId = 0;
        try {
            nextId = ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityPictureDao().getNextPrimaryId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nextId + 1;
    }

    public static void removeSPAPictureById(long chalkId) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityPictureDao().removeSPAPictureById(chalkId);
    }

    public static void removeSPAPictureAll(long chalkId) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityPictureDao().removeSPAPictureAll(chalkId);
    }

    public static void updatePicture(int pictureId, SpecialActivityPicture specialActivityPicture) {
        if (specialActivityPicture.getPictureId() == 0) {
            specialActivityPicture.setPictureId(SpecialActivityPicture.getNextPrimaryId());
        }
        try {
            ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityPictureDao().updatePicture(pictureId, specialActivityPicture.getReportId(), specialActivityPicture.getCustid(), specialActivityPicture.getImageName(), specialActivityPicture.getImagePath(), specialActivityPicture.getImageResulation(), specialActivityPicture.getPictureDate(), specialActivityPicture.getImageSize());
            // SQLiteDatabase sqliteDatabase = DatabaseHelper.getInstance().openReadableDatabase();
            //sqliteDatabase.update("Special_Actvities_Pictures", values, "picture_id ="+pictureId, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        if (this.pictureId != 0)
            values.put("picture_id", this.pictureId);
        else
            values.put("picture_id", SpecialActivityPicture.getNextPrimaryId());
        values.put("picture_date", this.pictureDate);
        values.put("image_path", this.imagePath);
        values.put("image_resolution", this.imageResulation);
        values.put("image_size", this.imageSize);
        values.put("custid", this.custid);
        values.put("image_name", this.imageName);
        values.put("report_id", this.reportId);

        return values;
    }

    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        try {
            object.put("picture_id", this.pictureId);
            object.put("picture_date", this.pictureDate);
            object.put("image_path", this.imagePath);
            object.put("image_resolution", this.imageResulation);
            object.put("image_size", this.imageSize);
            object.put("custid", this.custid);
            object.put("image_name", this.imageName);
            object.put("report_id", this.reportId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public String getPictureDate() {
        return pictureDate;
    }

    public void setPictureDate(String pictureDate) {
        this.pictureDate = pictureDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageResulation() {
        return imageResulation;
    }

    public void setImageResulation(String imageResulation) {
        this.imageResulation = imageResulation;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public int getCustid() {
        return custid;
    }

    public void setCustid(int custid) {
        this.custid = custid;
    }
}
