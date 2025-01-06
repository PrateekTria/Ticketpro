package com.ticketpro.model;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import static com.ticketpro.util.TPConstant.TAG;

@Entity(tableName = "placard_temp")
public class Placard {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "placard_no")
    private String placardNo;

    @ColumnInfo(name = "placard_details")
    private String placardDetails;

    public Placard() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlacardNo() {
        return placardNo;
    }

    public void setPlacardNo(String placardNo) {
        this.placardNo = placardNo;
    }

    public String getPlacardDetails() {
        return placardDetails;
    }

    public void setPlacardDetails(String placardDetails) {
        this.placardDetails = placardDetails;
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("id", this.id);
        values.put("placard_no", this.placardNo);
        values.put("placard_details", this.placardDetails);
        return values;
    }

    public static void insertPlacard(Placard placard) {
        ParkingDatabase.getInstance(TPApplication.getInstance()).placardDao().insertPlacard(placard).subscribeOn(Schedulers.io()).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "onSubscribe: ");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError: "+e.getMessage() );
            }
        });

    }

    public static ArrayList<Placard> __getListOfPlacard(){
        ArrayList<Placard> arrayList;
        arrayList = (ArrayList<Placard>) ParkingDatabase.getInstance(TPApplication.getInstance()).placardDao().getPlacard();
        return arrayList;
    }
    public static ArrayList<String> __getListOfPlacardNo(){
        ArrayList<String> arrayList;
        arrayList = (ArrayList<String>) ParkingDatabase.getInstance(TPApplication.getInstance()).placardDao().getPlacardNo();
        return arrayList;
    }
    public static Placard __getPlacardByNo(String placardNo){
        return ParkingDatabase.getInstance(TPApplication.getInstance()).placardDao().getPlacardById(placardNo);
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).placardDao().removeAll();
    }

    public static void removeById(String name) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).placardDao().removeById(name);
    }

}
