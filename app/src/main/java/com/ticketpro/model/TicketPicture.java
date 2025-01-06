package com.ticketpro.model;

import android.content.ContentValues;
import android.os.AsyncTask;
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
import com.ticketpro.util.DateUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ticketpro.util.TPConstant.TAG;

@Entity(tableName = "ticket_pictures")
public class TicketPicture {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "s_no")
    private int s_no;
    @ColumnInfo(name = "picture_id")
    @SerializedName("picture_id")
    @Expose
    private int pictureId;
    @ColumnInfo(name = "ticket_id")
    @SerializedName("ticket_id")
    @Expose
    private long ticketId;
    @ColumnInfo(name = "citation_number")
    @SerializedName("citation_number")
    @Expose
    private long citationNumber;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "picture_date")
    @SerializedName("picture_date")
    @Expose
    private Date pictureDate;
    @ColumnInfo(name = "mark_print")
    @SerializedName("mark_print")
    @Expose
    private String markPrint;
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
    @SerializedName("sync_status")
    @Expose
    @ColumnInfo(name = "sync_status")
    private String syncStatus;
    @Ignore
    @SerializedName("lpr_notification")
    @Expose
    private String lprNotification;
    @Ignore
    @SerializedName("lpr_image_name")
    @Expose
    private String lprImageName;
    @SerializedName("download_image_url")
    @Expose
    @ColumnInfo(name = "download_image_url")
    private String downloadImageUrl;
    @ColumnInfo(name = "image_name")
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @ColumnInfo(name = "isSelfi")
    @SerializedName("isSelfi")
    @Expose
    private boolean isSelfi;

    @Ignore
    private boolean isPhotoSp;
    @Ignore
    private int nPictureCount;

    @Ignore
    private String isEdit;

    public TicketPicture() {
        this.lprNotification = "N";
    }

    public TicketPicture(JSONObject object) throws Exception {
        this.setTicketId(object.getLong("ticket_id"));
        this.setPictureId(object.getInt("picture_id"));
        this.setCitationNumber(object.getLong("citation_number"));
        this.setCustId(!object.isNull("custid") ? object.getInt("custid") : 0);
        this.setPictureDate(DateUtil.getDateFromSQLString(object.getString("picture_date")));
        this.setMarkPrint(object.getString("mark_print"));
        this.setImagePath(object.getString("image_path"));
        this.setImageResolution(object.getString("image_resolution"));
        this.setImageSize(object.getString("image_size"));
        this.setSyncStatus(object.getString("sync_status"));
        this.setDownloadImageUrl(object.getString("downloadImageUrl"));
        this.setImageName(object.getString("imageName"));
        try {
            if (object.has("isSelfi")) {
                this.setIsSelfi(object.getString("isSelfi"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<TicketPicture> getTicketPictures(long ticketId, long citationNumber) throws Exception {
        ArrayList<TicketPicture> list;
        list = (ArrayList<TicketPicture>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketPicturesDao().getTicketPictures(ticketId,citationNumber);
        return list;
    }

    public static ArrayList<TicketPicture> getTicketPicturesPending(long citationNumber) throws Exception {
        ArrayList<TicketPicture> list;
        list = (ArrayList<TicketPicture>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketPicturesDao().getTicketPicturePending(citationNumber);
        return list;
    }


    public static ArrayList<TicketPicture> getTicketPicturesByCitation(long citationNumber) throws Exception {

        ArrayList<TicketPicture> list = new ArrayList<TicketPicture>();
        list = (ArrayList<TicketPicture>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketPicturesDao().getTicketPicturesByCitation(citationNumber);
        return list;
    }

    public static ArrayList<TicketPicture> getTicketPicturesByCitationPI(long citationNumber) throws Exception {

        ArrayList<TicketPicture> list = new ArrayList<TicketPicture>();
        list = (ArrayList<TicketPicture>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketPicturesDao().getTicketPicturesByCitation(citationNumber);
        return list;
    }

    public static TicketPicture getTicketPictureByPrimaryKey(String ticketPictureId) throws TPException {
        TicketPicture object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketPicturesDao().getTicketPictureByPrimaryKey(ticketPictureId);
        return object;
    }

    /**
     *
     * @param citationNumber
     * @param status
     * @return
     * @throws TPException
     */
    public static TicketPicture getTicketPictureByPStatus(long citationNumber,String status) throws TPException {
        TicketPicture object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketPicturesDao().getTicketPictureByPStatus(citationNumber,status);
        return object;
    }

    public static int getNextPrimaryId() {
        int nextId = 0;
        nextId = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketPicturesDao().getNextPrimaryId();
        return nextId + 1;
    }

    public static void removePictureByTicketId(long ticketId) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketPicturesDao().removePictureByTicketId(ticketId);
    }

    public static void removePictureById(int pictureId) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketPicturesDao().removePictureById(pictureId);
    }

    /**
     *
     * @param citation
     * @param values
     */
    public static void updateTicketPictureSyncStatus(String citation, String values,int sNo) {
        try {
            ParkingDatabase.getInstance(TPApplication.getInstance()).ticketPicturesDao().updateTicket(citation, values,sNo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertTicketPicture(final TicketPicture TicketPicture) {
        final ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());
        CompletableObserver completableObserver = instance.ticketPicturesDao().insertTicketPicture(TicketPicture).subscribeOn(Schedulers.io()).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage(), e);
            }
        });
    }

    public String getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit;
    }

    public int getS_no() {
        return s_no;
    }

    public void setS_no(int s_no) {
        this.s_no = s_no;
    }

    public boolean isSelfi() {
        return isSelfi;
    }

    public void setSelfi(boolean selfi) {
        isSelfi = selfi;
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        if (this.pictureId != 0) {
            values.put("picture_id", this.pictureId);
        } else {
            values.put("picture_id", TicketPicture.getNextPrimaryId());
        }
        values.put("ticket_id", this.ticketId);
        values.put("custid", this.custId);
        values.put("citation_number", this.citationNumber);
        values.put("picture_date", DateUtil.getSQLStringFromDate2(this.pictureDate));
        values.put("mark_print", this.markPrint);
        values.put("image_path", this.imagePath);
        values.put("image_resolution", this.imageResolution);
        values.put("image_size", this.imageSize);
        values.put("sync_status", this.syncStatus);
        values.put("download_image_url", this.downloadImageUrl);
        values.put("image_name", this.imageName);

        try {
            if (this.isSelfi) {
                values.put("isSelfi", "Y");
            } else {
                values.put("isSelfi", "N");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return values;
    }

    public JSONObject getJSONObject() {
        JSONObject values = new JSONObject();
        try {
            values.put("picture_id", this.pictureId);
            values.put("ticket_id", this.ticketId);
            values.put("citation_number", this.citationNumber);
            values.put("custid", this.custId);
            values.put("picture_date", DateUtil.getSQLStringFromDate2(this.pictureDate));
            values.put("mark_print", this.markPrint);
            values.put("image_path", this.imagePath);
            values.put("download_image_url", this.downloadImageUrl);
            values.put("image_name", this.imageName);

            if (this.imagePath != null) {
                String[] tokens = this.imagePath.split("/");
                if (tokens.length > 0) {
                    values.put("image_path", tokens[tokens.length - 1]);
                }
            }

            values.put("image_resolution", this.imageResolution);
            values.put("image_size", this.imageSize);
            values.put("sync_status", this.syncStatus);
            values.put("lpr_notification", this.lprNotification);
            values.put("lpr_image_name", this.lprImageName);

            try {
                if (this.isSelfi) {
                    values.put("isSelfi", "Y");
                } else {
                    values.put("isSelfi", "N");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            Log.e("TicketPicture", "Error " + e.getMessage());
        }

        return values;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
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

    public Date getPictureDate() {
        return pictureDate;
    }

    public void setPictureDate(Date pictureDate) {
        this.pictureDate = pictureDate;
    }

    public String getMarkPrint() {
        return markPrint;
    }

    public void setMarkPrint(String markPrint) {
        this.markPrint = markPrint;
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

    public String getLprNotification() {
        return lprNotification;
    }

    public void setLprNotification(String lprNotification) {
        this.lprNotification = lprNotification;
    }

    public String getLprImageName() {
        return lprImageName;
    }

    public void setLprImageName(String lprImageName) {
        this.lprImageName = lprImageName;
    }

    public String getDownloadImageUrl() {
        return downloadImageUrl;
    }

    public void setDownloadImageUrl(String downloadImageUrl) {
        this.downloadImageUrl = downloadImageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getnPictureCount() {
        return nPictureCount;
    }

    public void setnPictureCount(int nPictureCount) {
        this.nPictureCount = nPictureCount;
    }

    /*
     * @returns - true or false on behalf of selfi
     * */
    public boolean isSelfi(String hasSelfi) {
        return isSelfi;
    }

    /*
     * @Required - Param-yes
     * @Params - Y or N (if it is selfi action pass Y
     * */
    public void setIsSelfi(String hasSelfi) {
        if (hasSelfi.equalsIgnoreCase("N")) {
            isSelfi = false;
        } else {
            isSelfi = true;
        }
    }

    public boolean isPhotoSp() {
        return isPhotoSp;
    }

    public void setPhotoSp(boolean photoSp) {
        isPhotoSp = photoSp;
    }

    private static class InsertTicketPicture extends AsyncTask<Void, Void, Void> {
        private TicketPicture TicketPicture;

        public InsertTicketPicture(TicketPicture TicketPicture) {
            this.TicketPicture = TicketPicture;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).ticketPicturesDao().insertTicketPicture(TicketPicture);
            return null;
        }
    }

}