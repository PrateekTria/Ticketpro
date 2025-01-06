package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Feature;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface FeaturesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFeature(Feature... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertFeature(Feature data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFeatureList(List<Feature> FeaturesList);

    @Query("select * from features order by order_number")
    Maybe<List<Feature>> getFeatures();

    @Query("select * from features where UPPER(feature) in (:features) order by order_number")
    Maybe<List<Feature>> getFeatures(List<String> features);

    @Query("select * from features where custid=:custId")
    Maybe<List<Feature>> getFeaturesList(int custId);

    @Query("select * from features where UPPER(feature)=:featureName order by order_number")
    Maybe<List<Feature>> getFeature(String featureName);

    @Query("select value from features where UPPER(feature)=:featureName order by order_number")
    Single<List<String>> getFeatureValue(String featureName);

    @Query("Delete from features")
    void removeAll();

    @Query("Delete from features where feature_id=:id")
    void removeById(int id);
}
