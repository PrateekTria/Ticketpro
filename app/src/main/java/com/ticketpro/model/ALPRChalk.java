package com.ticketpro.model;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.parking.service.ServiceHandler;
import com.ticketpro.parking.service.ServiceHandlerImpl;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPUtility;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import au.com.bytecode.opencsv.CSVWriter;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import static com.ticketpro.util.TPConstant.TAG;

/**
 * Created by Rohit on 20-04-2020.
 */

@Entity(tableName = "ALPRPhotoChalk", indices = {@Index(value = {"Plate"}, unique = true)})
public class ALPRChalk {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "Plate")
    private String Plate;
    @ColumnInfo(name = "Details")
    private String Details;
    @ColumnInfo(name = "DataField1")
    private String CustomData1;
    @ColumnInfo(name = "DataField2")
    private String CustomData2;
    @ColumnInfo(name = "DataField3")
    private String CustomData3;
    @ColumnInfo(name = "Confidence")
    private String Confidence;
    @ColumnInfo(name = "FirstDate")
    private String FirstDate;
    @ColumnInfo(name = "FirstTime")
    private String FirstTime;
    @ColumnInfo(name = "FirstDateTime")
    private Date FirstDateTime;
    @ColumnInfo(name = "FirstParkingBay")
    private String FirstParkingBay;
    @ColumnInfo(name = "FirstLocLat")
    private String FirstLocLat;
    @ColumnInfo(name = "FirstLocLong")
    private String FirstLocLong;
    @ColumnInfo(name = "FirstLocAcc")
    private String FirstLocAcc;
    @ColumnInfo(name = "LastDate")
    private String LastDate;
    @ColumnInfo(name = "LastTime")
    private String LastTime;
    @ColumnInfo(name = "LastDateTime")
    private Date LastDateTime;
    @ColumnInfo(name = "LastParkingBay")
    private String LastParkingBay;
    @ColumnInfo(name = "LastLocLat")
    private String LastLocLat;
    @ColumnInfo(name = "LastLocLong")
    private String LastLocLong;
    @ColumnInfo(name = "LastLocAcc")
    private String LastLocAcc;
    @ColumnInfo(name = "PermitExpiryDate")
    private String PermitExpiryDate;
    @ColumnInfo(name = "PermitExpiryTime")
    private String PermitExpiryTime;
    @ColumnInfo(name = "chalkDuration")
    private int chalkDurationId;
    @ColumnInfo(name = "duration_code")
    private String chalkDurationCode;
    @ColumnInfo(name = "chalkLocation")
    private String chalkLocation;
    @ColumnInfo(name = "chalkTire")
    private String chalkTire;
    @ColumnInfo(name = "chalkId")
    private long chalkId;
    @ColumnInfo(name = "userid")
    private int userid;
    @ColumnInfo(name = "deviceId")
    private int deviceId;
    @ColumnInfo(name = "custId")
    private int custId;
    @ColumnInfo(name = "is_expired")
    private String isExpired;
    @Ignore
    private ArrayList<ChalkPicture> chalkPictures = new ArrayList<>();

    public static ArrayList<ALPRChalk> getChalkVehicles() throws Exception {
        ArrayList<ALPRChalk> alprChalks = new ArrayList<>();
        alprChalks = (ArrayList<ALPRChalk>) ParkingDatabase.getInstance(TPApplication.getInstance()).alprPhotoChalkDao().getChalkVehicles().subscribeOn(Schedulers.io()).blockingGet();
        for (ALPRChalk alprChalk : alprChalks) {
            try {
                alprChalk.setChalkPictures(ChalkPicture.getChalkPictures(alprChalk.chalkId));
            } catch (Exception e) {
                Log.e("ALPRChalk", TPUtility.getPrintStackTrace(e));
            }
        }

        return alprChalks;
    }

    public static ALPRChalk getLastChalkedVehicle() {
        ALPRChalk alprChalk = null;
        alprChalk = ParkingDatabase.getInstance(TPApplication.getInstance()).alprPhotoChalkDao().getLastChalkedVehicle().subscribeOn(Schedulers.io()).blockingGet();
        try {
            alprChalk.setChalkPictures(ChalkPicture.getChalkPictures(alprChalk.chalkId));
        } catch (Exception e) {
            Log.e("ALPRChalk", TPUtility.getPrintStackTrace(e));
        }

        return alprChalk;
    }

    public static ArrayList<String> getAllPlates() {
        return (ArrayList<String>) ParkingDatabase.getInstance(TPApplication.getInstance()).alprPhotoChalkDao().getAllPlates().subscribeOn(Schedulers.io()).blockingGet();
    }

    public static void updateChalkExpired(String plate, String values) {
        try {
            ParkingDatabase.getInstance(TPApplication.getInstance()).alprPhotoChalkDao().updateChalkExpired(plate, values);
            /*DatabaseHelper.getInstance().openReadableDatabase().update(TPConstant.TABLE_ALPR_CHALK, values, "Plate = ?", new String[]{plate});
            DatabaseHelper.getInstance().closeWritableDb();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getNextPrimaryId() throws Exception {
        return new Date().getTime();
    }

    public static ALPRChalk getChalkVehicleById(long chalkId) {
        ALPRChalk alprChalk = null;
        alprChalk = ParkingDatabase.getInstance(TPApplication.getInstance()).alprPhotoChalkDao().getChalkVehicleById(chalkId).subscribeOn(Schedulers.io()).blockingGet();
        try {
            if (alprChalk !=null){
            alprChalk.setChalkPictures(ChalkPicture.getChalkPictures(alprChalk.chalkId));}
        } catch (Exception e) {
            Log.e("ALPRChalk", TPUtility.getPrintStackTrace(e));
        }
        return alprChalk;
    }

    public static void updateCSV(String zone, String location) {
        File file = new File(TPUtility.getALPRImagesFolder(), "chalkList.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            /*SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM ALPRPhotoChalk WHERE duration_code ='" + zone + "' AND chalkLocation='" + location + "'", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3),
                        curCSV.getString(4), curCSV.getString(5), curCSV.getString(6), curCSV.getString(7), curCSV.getString(8),
                        curCSV.getString(9), curCSV.getString(10), curCSV.getString(11), curCSV.getString(12), curCSV.getString(13),
                        curCSV.getString(14), curCSV.getString(15), curCSV.getString(16), curCSV.getString(17), curCSV.getString(18),
                        curCSV.getString(19), curCSV.getString(20), curCSV.getString(21), curCSV.getString(22), curCSV.getString(23),
                        curCSV.getString(24), curCSV.getString(25), curCSV.getString(26), curCSV.getString(27), curCSV.getString(28),
                        curCSV.getString(29), curCSV.getString(30)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateChalk(final String plate, final ALPRChalk values) {
        try {
            Completable.fromAction(new Action() {
                @Override
                public void run() {
                    ParkingDatabase.getInstance(TPApplication.getInstance()).alprPhotoChalkDao().updateChalk(plate, values.getLastDate(), values.getLastParkingBay(), values.getLastLocLat(), values.getLastLocLong(), values.getLastLocAcc());
                }
            }).subscribeOn(Schedulers.io()).subscribe();

            //DatabaseHelper.getInstance().openReadableDatabase().update(TPConstant.TABLE_ALPR_CHALK, values, "Plate = ?", new String[]{plate});
            //DatabaseHelper.getInstance().closeWritableDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeChalkById(final long chalkId, final String item) {
        ParkingDatabase.getInstance(TPApplication.getInstance()).alprPhotoChalkDao().removeChalkById(chalkId);
        ParkingDatabase.getInstance(TPApplication.getInstance()).chalkPicturesDao().removeChalkPictureById(chalkId);
        ParkingDatabase.getInstance(TPApplication.getInstance()).chalkCommentsDao().getChalkCommentByPrimaryKey(chalkId);

        //Update Chalk Status on SERVER
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServiceHandler service = new ServiceHandlerImpl();
                try {
                    service.updateChalkStatus(chalkId, "N", item);
                } catch (Exception e) {
                    Log.e("ChalkVehicle", TPUtility.getPrintStackTrace(e));
                }
            }
        }).start();
    }

    public static void insertALPRChalk(ALPRChalk alprChalk) {
        ParkingDatabase.getInstance(TPApplication.getInstance()).alprPhotoChalkDao().insertALPRChalk(alprChalk).subscribeOn(Schedulers.io()).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe: ");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: "+e.getMessage() );
            }
        });
    }

    public static ChalkVehicle convertToChalk(ALPRChalk alprChalk) {
        ChalkVehicle chalkVehicle =  new ChalkVehicle();
        chalkVehicle.setChalkId(alprChalk.getChalkId());
        chalkVehicle.setDeviceId(alprChalk.getDeviceId());
        chalkVehicle.setUserId(alprChalk.getUserid());
        chalkVehicle.setChalkDate(alprChalk.getFirstDateTime());
        chalkVehicle.setPlate(alprChalk.getPlate());
        chalkVehicle.setDurationId(alprChalk.getChalkDurationId());
        chalkVehicle.setChalkType("PHOTO");
        chalkVehicle.setExpiration(TPUtility.addMinutesToDate(Duration.getDurationMinsById(alprChalk.getChalkDurationId(), TPApplication.getInstance().getApplicationContext()), alprChalk.getFirstDateTime()));
        chalkVehicle.setLocation(alprChalk.getChalkLocation());
        chalkVehicle.setLatitude(alprChalk.getFirstLocLat());
        chalkVehicle.setLongitude(alprChalk.getFirstLocLat());
        chalkVehicle.setCustId(alprChalk.getCustId());
        return chalkVehicle;
    }

    public JSONObject getJSONObject() {
        JSONObject values = new JSONObject();
        try {
            values.put("chalk_id", this.chalkId);
            values.put("device_id", this.deviceId);
            values.put("userid", this.userid);
            values.put("chalk_date", DateUtil.getSQLStringFromDate2(this.FirstDateTime));
            values.put("plate", this.Plate);
            values.put("duration_id", this.chalkDurationId);
            values.put("chalk_type", "PHOTO");
            values.put("expiration", DateUtil.getSQLStringFromDate2(TPUtility.addMinutesToDate(Duration.getDurationMinsById(chalkDurationId, TPApplication.getInstance().getApplicationContext()), this.FirstDateTime)));
            values.put("location", this.chalkLocation != null ? this.chalkLocation.toUpperCase() : "");
            values.put("latitude", this.getFirstLocLat());
            values.put("longitude", this.getFirstLocLong());
            values.put("custid", this.custId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return values;
    }

    public String getFirstDate() {
        return FirstDate;
    }

    public void setFirstDate(String firstDate) {
        FirstDate = firstDate;
    }

    public String getFirstTime() {
        return FirstTime;
    }

    public void setFirstTime(String firstTime) {
        FirstTime = firstTime;
    }

    public String getLastDate() {
        return LastDate;
    }

    public void setLastDate(String lastDate) {
        LastDate = lastDate;
    }

    public String getLastTime() {
        return LastTime;
    }

    public void setLastTime(String lastTime) {
        LastTime = lastTime;
    }

    public String getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(String isExpired) {
        this.isExpired = isExpired;
    }

    public boolean isExpired() {
        return this.isExpired != null && this.isExpired.equals("Y");
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getChalkTire() {
        return chalkTire;
    }

    public void setChalkTire(String chalkTire) {
        this.chalkTire = chalkTire;
    }

    public String getChalkDurationCode() {
        return chalkDurationCode;
    }

    public void setChalkDurationCode(String chalkDurationCode) {
        this.chalkDurationCode = chalkDurationCode;
    }

    public long getChalkId() {
        return chalkId;
    }

    public void setChalkId(long chalkId) {
        this.chalkId = chalkId;
    }

    public String getChalkLocation() {
        return chalkLocation;
    }

    public void setChalkLocation(String chalkLocation) {
        this.chalkLocation = chalkLocation;
    }

    public String getPlate() {
        return Plate;
    }

    public void setPlate(String plate) {
        Plate = plate;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getCustomData1() {
        return CustomData1;
    }

    public void setCustomData1(String customData1) {
        CustomData1 = customData1;
    }

    public String getCustomData2() {
        return CustomData2;
    }

    public void setCustomData2(String customData2) {
        CustomData2 = customData2;
    }

    public String getCustomData3() {
        return CustomData3;
    }

    public void setCustomData3(String customData3) {
        CustomData3 = customData3;
    }

    public String getConfidence() {
        return Confidence;
    }

    public void setConfidence(String confidence) {
        Confidence = confidence;
    }

    public Date getFirstDateTime() {
        return FirstDateTime;
    }

    public void setFirstDateTime(Date firstDateTime) {
        FirstDateTime = firstDateTime;
    }

    public String getFirstParkingBay() {
        return FirstParkingBay;
    }

    public void setFirstParkingBay(String firstParkingBay) {
        FirstParkingBay = firstParkingBay;
    }

    public String getFirstLocLat() {
        return FirstLocLat;
    }

    public void setFirstLocLat(String firstLocLat) {
        FirstLocLat = firstLocLat;
    }

    public String getFirstLocLong() {
        return FirstLocLong;
    }

    public void setFirstLocLong(String firstLocLong) {
        FirstLocLong = firstLocLong;
    }

    public String getFirstLocAcc() {
        return FirstLocAcc;
    }

    public void setFirstLocAcc(String firstLocAcc) {
        FirstLocAcc = firstLocAcc;
    }

    public Date getLastDateTime() {
        return LastDateTime;
    }

    public void setLastDateTime(Date lastDateTime) {
        LastDateTime = lastDateTime;
    }

    public String getLastParkingBay() {
        return LastParkingBay;
    }

    public void setLastParkingBay(String lastParkingBay) {
        LastParkingBay = lastParkingBay;
    }

    public String getLastLocLat() {
        return LastLocLat;
    }

    public void setLastLocLat(String lastLocLat) {
        LastLocLat = lastLocLat;
    }

    public String getLastLocLong() {
        return LastLocLong;
    }

    public void setLastLocLong(String lastLocLong) {
        LastLocLong = lastLocLong;
    }

    public String getLastLocAcc() {
        return LastLocAcc;
    }

    public void setLastLocAcc(String lastLocAcc) {
        LastLocAcc = lastLocAcc;
    }

    public String getPermitExpiryDate() {
        return PermitExpiryDate;
    }

    public void setPermitExpiryDate(String permitExpiryDate) {
        PermitExpiryDate = permitExpiryDate;
    }

    public String getPermitExpiryTime() {
        return PermitExpiryTime;
    }

    public void setPermitExpiryTime(String permitExpiryTime) {
        PermitExpiryTime = permitExpiryTime;
    }

    public ArrayList<ChalkPicture> getChalkPictures() {
        return chalkPictures;
    }

    public void setChalkPictures(ArrayList<ChalkPicture> chalkPictures) {
        this.chalkPictures = chalkPictures;
    }

    public int getChalkDurationId() {
        return chalkDurationId;
    }

    public void setChalkDurationId(int chalkDurationId) {
        this.chalkDurationId = chalkDurationId;
    }

    public static class LocationComparator implements Comparator<ALPRChalk> {
        @Override
        public int compare(ALPRChalk alprChalk1, ALPRChalk alprChalk2) {

            return alprChalk1.getChalkLocation().compareToIgnoreCase(alprChalk2.getChalkLocation());
        }
    }

    public static class PlateComparator implements Comparator<ALPRChalk> {
        @Override
        public int compare(ALPRChalk alprChalk1, ALPRChalk alprChalk2) {
            return alprChalk1.getPlate().compareToIgnoreCase(alprChalk2.getPlate());
        }
    }

    public static class DateComparator implements Comparator<ALPRChalk> {
        @Override
        public int compare(ALPRChalk ALPRChalk1, ALPRChalk ALPRChalk2) {
            return ALPRChalk1.getFirstDateTime().compareTo(ALPRChalk2.getFirstDateTime());
        }
    }

    public static class ZoneComparator implements Comparator<ALPRChalk> {
        @Override
        public int compare(ALPRChalk ALPRChalk1, ALPRChalk ALPRChalk2) {
            return ALPRChalk1.getChalkDurationCode().compareToIgnoreCase(ALPRChalk2.getChalkDurationCode());
        }
    }

    private static class InsertALPRChalk extends AsyncTask<Void, Void, Void> {
        private ALPRChalk alprChalk;

        public InsertALPRChalk(ALPRChalk alprChalk) {
            this.alprChalk = alprChalk;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }
}
