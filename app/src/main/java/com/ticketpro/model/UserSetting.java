package com.ticketpro.model;

import android.content.ContentValues;
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
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "user_settings")
public class UserSetting implements Serializable {
    @Ignore
    private static final long serialVersionUID = 1L;
    @PrimaryKey
    @ColumnInfo(name = "setting_id")
    @SerializedName("setting_id")
    @Expose
    private int settingId;
    @ColumnInfo(name = "userid")
    @SerializedName("userid")
    @Expose
    private int userId;
    @ColumnInfo(name = "device_id")
    @SerializedName("device_id")
    @Expose
    private int deviceId;
    @ColumnInfo(name = "auto_sync_interval")
    @SerializedName("auto_sync_interval")
    @Expose
    private int autoSyncInterval;
    @ColumnInfo(name = "data_retention_period")
    @SerializedName("data_retention_period")
    @Expose
    private int dataRetentionPeriod;
    @Ignore
    private int cacheExpiry;
    @ColumnInfo(name = "gps")
    @SerializedName("gps")
    @Expose
    private String gps;
    @ColumnInfo(name = "data_backup")
    @SerializedName("data_backup")
    @Expose
    private String dataBackup;
    @ColumnInfo(name = "user_prefs")
    @SerializedName("user_prefs")
    @Expose
    private String userPrefs;
    @Ignore
    private boolean locationKeyboard;
    @Ignore
    private boolean commentsKeyboard;
    @Ignore
    private boolean violationKeyboard;
    @Ignore
    private boolean skipLocationEntry;
    @Ignore
    private boolean autoLookup;
    @Ignore
    private boolean secondLocation;
    @Ignore
    private boolean accordionLookup = true;
    @Ignore
    private boolean searchContains = false;
    @Ignore
    private boolean makeKeyboard;
    @Ignore
    private boolean bodyKeyboard;
    @Ignore
    private boolean colorKeyboard;
    @Ignore
    private boolean isStickyMarker;
    @Ignore
    private boolean isAutoPromptVehicle = true;
    @Ignore
    private boolean isALPRVehicleRequired;

    public UserSetting() {

    }

    public UserSetting(JSONObject object) throws Exception {
        this.setUserId(object.getInt("userid"));
        this.setDeviceId(object.getInt("device_id"));
        this.setAutoSyncInterval(object.getInt("auto_sync_interval"));
        this.setDataRetentionPeriod(object.getInt("data_retention_period"));
        this.setGps(object.getString("gps"));
        this.setDataBackup(object.getString("data_backup"));
        this.setUserPrefs(object.getString("user_prefs"));
    }

    public static UserSetting getUserSettings(int userId) throws TPException {
        UserSetting settings = null;
        settings = ParkingDatabase.getInstance(TPApplication.getInstance()).userSettingsDao().getUserSettings(userId);
        return settings;
    }

    public static String getUserPrefsString(UserSetting userSettings) {
        JSONObject userPrefs = new JSONObject();
        try {
            userPrefs.put(TPConstant.PREFS_KEY_SETTING_LOCATION_KEYBOARD, userSettings.isLocationKeyboard());
            userPrefs.put(TPConstant.PREFS_KEY_SETTING_VIOLATION_KEYBOARD, userSettings.isViolationKeyboard());
            userPrefs.put(TPConstant.PREFS_KEY_SETTING_COMMENT_KEYBOARD, userSettings.isCommentsKeyboard());
            userPrefs.put(TPConstant.PREFS_KEY_SETTING_SKIP_LOCATION_ENTRY, userSettings.isSkipLocationEntry());
            userPrefs.put(TPConstant.PREFS_KEY_SETTING_AUTO_LOOKUP, userSettings.isAutoLookup());
            userPrefs.put(TPConstant.PREFS_KEY_STICKY_MARKERS, userSettings.isStickyMarker());
            userPrefs.put(TPConstant.PREFS_KEY_SETTING_SECOND_LOCATION_ENTRY, userSettings.isSecondLocation());
            userPrefs.put(TPConstant.PREFS_KEY_SETTING_ACCORDION_LOOKUP, userSettings.isAccordionLookUp());
            userPrefs.put(TPConstant.PREFS_KEY_SETTING_SEARCH_CONTAINS, userSettings.isSearchContains());
            userPrefs.put(TPConstant.PREFS_KEY_SETTING_CACHE_EXPIRY, userSettings.getCacheExpiry());
            userPrefs.put(TPConstant.PREFS_KEY_SETTING_MAKE_KEYBOARD, userSettings.isMakeKeyboard());
            userPrefs.put(TPConstant.PREFS_KEY_SETTING_BODY_KEYBOARD, userSettings.isBodyKeyboard());
            userPrefs.put(TPConstant.PREFS_KEY_SETTING_COLOR_KEYBOARD, userSettings.isColorKeyboard());
            userPrefs.put(TPConstant.PREFS_KEY_SETTING_AUTO_PROMPT_VEHICLE, userSettings.isAutoPromptVehicle);
            //userPrefs.put(TPConstant.PREFS_KEY_SETTING_ALPR_VEHICLE_INFO_REQUIRED, userSettings.isALPRVehicleRequired());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userPrefs.toString();
    }

    public static void updateUserPrefs(UserSetting settings) {
        String userPrefs = settings.getUserPrefs();
        if (!StringUtil.isEmpty(userPrefs.trim())) {
            try {
                JSONObject jsonObject = new JSONObject(userPrefs);

                settings.setLocationKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_LOCATION_KEYBOARD));
                settings.setCommentsKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_COMMENT_KEYBOARD));
                settings.setViolationKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_VIOLATION_KEYBOARD));
                settings.setSkipLocationEntry(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_SKIP_LOCATION_ENTRY));
                settings.setAutoLookup(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_AUTO_LOOKUP));
                settings.setSecondLocationEntry(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_SECOND_LOCATION_ENTRY));
                settings.setAccordionLookUp(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_ACCORDION_LOOKUP));
                settings.setSearchContains(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_SEARCH_CONTAINS));
                settings.setStickyMarker(jsonObject.getBoolean(TPConstant.PREFS_KEY_STICKY_MARKERS));

                if (jsonObject.has(TPConstant.PREFS_KEY_SETTING_CACHE_EXPIRY)) {
                    settings.setCacheExpiry(jsonObject.getInt(TPConstant.PREFS_KEY_SETTING_CACHE_EXPIRY));
                }

                if (jsonObject.has(TPConstant.PREFS_KEY_SETTING_MAKE_KEYBOARD)) {
                    settings.setMakeKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_MAKE_KEYBOARD));
                }

                if (jsonObject.has(TPConstant.PREFS_KEY_SETTING_BODY_KEYBOARD)) {
                    settings.setBodyKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_BODY_KEYBOARD));
                }

                if (jsonObject.has(TPConstant.PREFS_KEY_SETTING_COLOR_KEYBOARD)) {
                    settings.setColorKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_COLOR_KEYBOARD));
                }

                if (jsonObject.has(TPConstant.PREFS_KEY_SETTING_AUTO_PROMPT_VEHICLE)) {
                    settings.setAutoPromptVehicle(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_AUTO_PROMPT_VEHICLE));
                }
				
				/*if(jsonObject.has(TPConstant.PREFS_KEY_SETTING_ALPR_VEHICLE_INFO_REQUIRED)){
					settings.setALPRVehicleRequired(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_ALPR_VEHICLE_INFO_REQUIRED));
				}*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).userSettingsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).userSettingsDao().removeById(id);
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("userid", this.userId);
        values.put("device_id", this.deviceId);
        values.put("auto_sync_interval", this.autoSyncInterval);
        values.put("data_retention_period", this.dataRetentionPeriod);
        values.put("gps", this.gps);
        values.put("data_backup", this.dataBackup);
        values.put("user_prefs", this.userPrefs);

        return values;
    }

    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        try {
            object.put("userid", this.userId);
            object.put("device_id", this.deviceId);
            object.put("auto_sync_interval", this.autoSyncInterval);
            object.put("data_retention_period", this.dataRetentionPeriod);
            object.put("gps", this.gps);
            object.put("data_backup", this.dataBackup);
            object.put("user_prefs", this.userPrefs);

        } catch (Exception e) {
            Log.e("UserSettings", "Error " + e.getMessage());
        }

        return object;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getAutoSyncInterval() {
        return autoSyncInterval;
    }

    public void setAutoSyncInterval(int autoSyncInterval) {
        this.autoSyncInterval = autoSyncInterval;
    }

    public int getDataRetentionPeriod() {
        return dataRetentionPeriod;
    }

    public void setDataRetentionPeriod(int dataRetentionPeriod) {
        this.dataRetentionPeriod = dataRetentionPeriod;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getDataBackup() {
        return dataBackup;
    }

    public void setDataBackup(String dataBackup) {
        this.dataBackup = dataBackup;
    }

    public String getUserPrefs() {
        return userPrefs;
    }

    public void setUserPrefs(String userPrefs) {
        this.userPrefs = userPrefs;
    }

    public boolean isGPSEnabled() {
        if (this.gps != null && this.gps.equalsIgnoreCase("Y")) {
            return true;
        }

        return false;
    }

    public boolean isDataBackupEnabled() {
        if (this.dataBackup != null && this.dataBackup.equalsIgnoreCase("Y")) {
            return true;
        }

        return false;
    }

    public boolean isLocationKeyboard() {
        return locationKeyboard;
    }

    public void setLocationKeyboard(boolean locationKeyboard) {
        this.locationKeyboard = locationKeyboard;
    }

    public boolean isCommentsKeyboard() {
        return commentsKeyboard;
    }

    public void setCommentsKeyboard(boolean commentsKeyboard) {
        this.commentsKeyboard = commentsKeyboard;
    }

    public boolean isViolationKeyboard() {
        return violationKeyboard;
    }

    public void setViolationKeyboard(boolean violationKeyboard) {
        this.violationKeyboard = violationKeyboard;
    }

    public boolean isMakeKeyboard() {
        return makeKeyboard;
    }

    public void setMakeKeyboard(boolean makeKeyboard) {
        this.makeKeyboard = makeKeyboard;
    }

    public boolean isSkipLocationEntry() {
        return skipLocationEntry;
    }

    public void setSkipLocationEntry(boolean skipLocationEntry) {
        this.skipLocationEntry = skipLocationEntry;
    }

    public boolean isAutoLookup() {
        return autoLookup;
    }

    public void setAutoLookup(boolean autoLookup) {
        this.autoLookup = autoLookup;
    }

    public boolean isSecondLocation() {
        return secondLocation;
    }

    public void setSecondLocation(boolean secondLocation) {
        this.secondLocation = secondLocation;
    }

    public void setSecondLocationEntry(boolean secondLocation) {
        this.secondLocation = secondLocation;
    }

    public boolean isAccordionLookUp() {
        return accordionLookup;
    }

    public void setAccordionLookUp(boolean accordionLookup) {
        this.accordionLookup = accordionLookup;
    }

    public boolean isSearchContains() {
        return searchContains;
    }

    public void setSearchContains(boolean searchContains) {
        this.searchContains = searchContains;
    }

    public boolean isBodyKeyboard() {
        return bodyKeyboard;
    }

    public void setBodyKeyboard(boolean bodyKeyboard) {
        this.bodyKeyboard = bodyKeyboard;
    }

    public boolean isColorKeyboard() {
        return colorKeyboard;
    }

    public void setColorKeyboard(boolean colorKeyboard) {
        this.colorKeyboard = colorKeyboard;
    }

    public int getCacheExpiry() {
        if (cacheExpiry == 0) {
            cacheExpiry = 10;
        }

        return cacheExpiry;
    }

    public void setCacheExpiry(int cacheExpiry) {
        this.cacheExpiry = cacheExpiry;
    }

    public boolean isStickyMarker() {
        return isStickyMarker;
    }

    public void setStickyMarker(boolean isStickyMarker) {
        this.isStickyMarker = isStickyMarker;
    }

    public boolean isAutoPromptVehicle() {
        return isAutoPromptVehicle;
    }

    public void setAutoPromptVehicle(boolean isAutoPromptVehicle) {
        this.isAutoPromptVehicle = isAutoPromptVehicle;
    }

    public int getSettingId() {
        return settingId;
    }

    public void setSettingId(int settingId) {
        this.settingId = settingId;
    }
	
	/*public boolean isALPRVehicleRequired() {
		return isALPRVehicleRequired;
	}

	public void setALPRVehicleRequired(boolean isALPRVehicleRequired) {
		this.isALPRVehicleRequired = isALPRVehicleRequired;
	}*/

    public static void insertUserSetting(final UserSetting UserSetting) {
        final ParkingDatabase database = ParkingDatabase.getInstance(TPApplication.getInstance());
        Completable completable = Completable.fromAction(() -> database.userSettingsDao().insertUserSetting(UserSetting)).subscribeOn(Schedulers.io());
        completable.subscribe();//new UserSetting.InsertUserSetting(UserSetting).execute();
    }

    public static void insertUserSettingList(final List<UserSetting> UserSetting) throws Exception{
        try {
            final ParkingDatabase database = ParkingDatabase.getInstance(TPApplication.getInstance());
            Completable completable = Completable.fromAction(() -> database.userSettingsDao().insertUserSettingList(UserSetting)).subscribeOn(Schedulers.io());
            CompletableObserver completableObserver = completable.subscribeWith(new CompletableObserver() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onComplete() {

                }

                @Override
                public void onError(Throwable e) {

                }
            });

        } catch (Error e) {
            e.printStackTrace();
        }
    }
}