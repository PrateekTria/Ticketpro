package com.ticketpro.model;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.TPUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "features")
public class Feature {
    public static final String STICKY_LOCATION = "isStickyLocation";
    public static final String STICKY_MAKE = "isStickyMake";
    public static final String STICKY_COLOR = "isStickyColor";
    public static final String STICKY_BODY = "isStickyBody";
    public static final String STICKY_SPACE = "isStickySpace";
    public static final String STICKY_TM = "isStickyTM";

    public static final String MAKE_REQUIRED = "isMakeRequired";
    public static final String COLOR_REQUIRED = "isColorRequired";
    public static final String BODY_REQUIRED = "isBodyRequired";
    public static final String LOCATION_REQUIRED = "isLocationRequired";
    public static final String METER_REQUIRED = "isMeterRequired";
    public static final String PERMIT_REQUIRED = "isPermitRequired";
    public static final String STATE_REQUIRED = "isStateRequired";
    public static final String SPACE_REQUIRED = "isSpaceRequired";
    public static final String PLATE_REQUIRED = "isPlateRequired";

    public static final String LOOKUP_PERMIT = "LookupPermit";
    public static final String LOOKUP_METER = "LookupMeter";
    public static final String TICKET_HISTORY_DAYS = "ticketHistoryDays";
    public static final String SEARCH_TICKET_GLOBAL = "searchTicketsGlobal";
    public static final String SEARCH_HOTLIST_GLOBAL = "searchHotlistGlobal";
    public static final String SEARCH_HISTORY_GLOBAL = "searchHistoryGlobal";
    public static final String UPDATE_CUTOFF_PERIOD = "UpdateCutoffPeriod";
    public static final String EDIT_TICKET_PICTURES = "EditTicketPictures";
    public static final String EDIT_TICKET_COMMENTS = "EditTicketComments";
    public static final String VOID_TICKET_LOG = "VoidTicketLog";
    public static final String VOID_TICKET = "VoidTicket";
    public static final String VOID_LAST_TICKET = "VoidLastTicket";
    public static final String VOID_TICKET_OTHER_COMMENT = "VoidComment";
    public static final String CHALK = "Chalk";
    public static final String SPECIAL_ACTIVITY = "SpecialActivity";
    public static final String PRINT_PREVIEW = "PrintPreview";
    public static final String METER_VIOLATION = "MeterViolation";
    public static final String VOICE_COMMANDS = "VoiceCommands";
    public static final String VOICE_MEMOS = "VoiceMemos";
    public static final String TAKE_PICTURES = "TakePictures";
    public static final String GPS = "GPS";
    public static final String PARK_GPS_API = "PARK_GPS_API";
    public static final String LPR = "LPR";
    public static final String MAX_PHOTOS = "MaxPhotos";
    public static final String MAX_VIOLATIONS = "MaxViolations";
    public static final String MAX_COMMENTS = "MaxComments";
    public static final String MAX_PRIVATE_COMMENTS = "MaxPrivateComments";
    public static final String VIN_SPECIAL_VALIDATE = "VinSpecialValidate";
    public static final String VIN_OVERRIDE = "VINOverride";
    public static final String PRINT_SCOFFLAW = "PrintScofflaw";
    public static final String PRINT_DISCLAIMER = "PrintDisclaimer";
    public static final String SCOFFLAW_RETURN_TO_TICKET = "ScoffReturnToTicket";
    public static final String HOTLIST_RETURN_TO_TICKET = "HotlistReturnToTicket";
    public static final String SPECIAL_MENU = "SpecialMenu";
    public static final String USE_LPR_STATE = "UseLPRState";
    public static final String CHECK_PLATE = "CheckPlate";
    public static final String RETURN_TO_CHALK = "ReturnToChalk";
    public static final String SHOW_VNV = "ShowVNV";
    public static final String BACKUP_ON_SYNC = "BackupOnSync";
    public static final String TRANSMIT_RETRY_INTERVAL = "TransmitRetryInterval";
    public static final String PRINT_TIME_ON_PHOTO = "PrintTimeOnPhoto";
    public static final String NOTIFY_TOW = "NotifyTow";

    public static final String USE_EXTERNAL_STORAGE = "UseExtStorage";
    public static final String CHECK_NETWORK_SIGNAL = "CheckNetworkSignal";
    public static final String CLEAR_COMMENTS_ON_SYNC = "ClearCommentOnSync";
    public static final String ALLOW_TM_EDIT = "AllowTMEdit";
    public static final String METER_NUMERIC_ENTRY = "MeterNumbericEntry";
    public static final String METER_SUPPRESS_MESSAGE = "MeterSuppressMessage";
    public static final String PHOTO_CHALK = "PhotoChalk";
    public static final String KEYPAD_ENTRY = "KeypadEntry";
    public static final String CHALK_TM_EDIT = "ChalkTMEdit";
    public static final String SHOW_CHALK_OPTIONS = "ShowChalkOptions";
    public static final String IS_STICKY = "isSticky";
    public static final String IS_REQUIRED = "isRequired";
    public static final String IS_HIDDEN = "isHidden";

    public static final String TICKET_COPY = "TicketCopy";
    public static final String SHOW_NOTIFY_LPR = "ShowNotifyLPR";
    public static final String TRANSACTION_TIMEOUT = "TransactionTimeout";
    public static final String CHALK_VIOLATION = "ChalkViolation";
    public static final String PHOTO_PURGE = "PhotoPurge";
    public static final String RECORDING_DURATION = "RecordingDuration";

    public static final String PLATE_CHECK_ALERT = "PlateCheckAlert";
    public static final String LOOKUP_PLATE_PERMIT = "GetAPermit";
    public static final String AUTODELETE_CHALKLOG = "AutoDeleteChalkLog";
    public static final String AUTODELETE = "auto_delete";
    public static final String EXPIRED_RESULT_CACHE_TIME = "ExpiredResultCacheTime";
    public static final String NETWORK_TIMEOUT = "NetworkTimeout";
    public static final String SHOW_PRINT_DIALOG = "ShowPrintDialog";
    public static final String EDMUNDS_VIN_LOOKUP = "EdmundsVinLookup";

    public static final String AUTOPROMPTVEHICLE = "AutoPromptVehicle";
    public static final String DISABLE_PASSPORT_PARKING_SWITCH = "DisablePPSwitch";
    public static final String N5_ADVANCE_PRINT_TEMPLATE = "N5AdvancePrintTemplate";
    public static final String CHECK_MESSAGES = "CheckMessages";
    public static final String MAINTENANCE_TYPES = "MaintenanceTypes";
    public static final String PROMPT_BEFORE_VOID = "PromptBeforeVoid";
    public static final String PARKING_CLEAR_TICKET_BY_HOUR = "ClearParkingTickets";
    public static final String PARKING_CLEARTICKET_SCHEDULER = "park_clearTicketSchedulerMins";
    public static final String IPS_MULTISPACE = "IPS_MULTISPACE";
    public static final String IPS_SPACEGROUP = "IPS_SPACEGROUP";
    public static final String IPS_SUBAREA = "IPS_SUBAREA";
    public static final String IPS_UTCTIME = "IPS_UTCTIME";
    public static final String IPS_MULTISPACE_EXPIRY_MIN = "IPS_MULTISPACE_EXPIRY_MINUTE";
    public static final String IPS_MULTISPACE_MAX_EXPIRY = "IPS_MULTISPACE_MAX_EXPIRY";
    public static final String IPS_MULTISPACE_VIOLATION_PROMPT = "IPS_MULTISPACE_PROMPT_VIOL";
    public static final String PARK_WARNING_RESET = "ParkWarningReset";
    public static final String park_new_passport = "park_new_passport";

    //Lookup Features (Add new Feature name for vendors in CAPS)
    public static final String PASSPORT_PARKING = "PASSPORTPARKING";
    public static final String PARK_MOBILE = "PARKMOBILE";
    public static final String PAYBY_PHONE = "PAYBYPHONE";
    public static final String DIGITAL_PAYTECH = "DIGITALPAYTECH";
    public static final String IPS_GROUP = "IPSGROUP";
    public static final String MOBILE_NOW = "MOBILENOW";
    public static final String PROGRESSIVE = "PROGRESSIVE";
    public static final String CS_LOOKUP = "CSLOOKUP";
    public static final String CALE = "CALE";
    public static final String PARKEON = "PARKEON";
    public static final String SAMTRANS = "SAMTRANS";
    public static final String PARK_GENETEC = "GENETEC";

    public static final String PHOTO_VIN = "PhotoVIN";
    public static final String ADD_HOTLIST = "AddHotlist";
    public static final String AUTO_LPR = "AutoLPR";
    public static final String AUTO_LPR_VEHICLE_INFO = "AutoLPRVehicleInfo";

    public static final String SAMTRANS_VIOL = "SAMTRANS_VIOL";
    public static final String CHALK_REMOVE_REASION = "CHALK_REMOVE_REASON";
    public static final String DEVICEINACTIVITYTIME = "FT_DEVICE_INACTIVITY_TIME";
    public static final String FT_LOCATION_UPDATE_TIME_MINUTES = "FT_LOCATION_UPDATE_TIME_MINUTES";
    public static final String FT_LOCATION_UPDATE_DISTANCE_METER = "FT_LOCATION_UPDATE_DISTANCE_METER";
    public static final String FIELD_TRACKER = "FT_FIELD_TRACKER";
    public static final String INACTIVITY_DURATION = "park_InactivityDuration";
    public static final String INACTIVITY_AUTOLOGOUT = "park_InactivityLogout";
    public static final String ALPR_RETURN_PROCESS = "park_alpr_returnProcess";
    public static final String ALPR_ADMINLAUNCH = "park_ALPR_admin";
    public static final String ALPR_PORTRAIT_ORIENTATION = "park_alpr_portraitOrientation";
    public static final String ALPR = "park_ALPR";
    public static final String TIMEOUT = "park_service_timeout";
    public static final String PARK_SEARCH_TRACK = "park_TrackPlateLookup";
    public static final String TOGGLE_NET_ON_OFF = "park_Toggle_net_on_off";
    public static final String PARK_STICKY_PHOTO = "PARK_STICKY_PHOTO";
    public static final String PARK_RECOVERY_DATA = "park_recovery_data";
    public static final String PARK_P_BUTTON="park_p_button";
    public static final String PARK_PLATE_SEARCH_ZERO_OR_O="park_plateSearch_zero_or_O";
    public static final String PARK_REPEATOFFENDER_HIDE="park_repeatOffender_vertical";
    public static final String PARK_OLD_CHALK_SYNC = "park_old_chalk_sync";
    public static final String PLACARD = "PLACARD";
    public static final String PARK_DIAGNOSTICS="park_Diagnostics";
    public static final String PARK_PLATE_REG="park_plate_reg";
    public static final String CALE2 = "CALE2";
    public static final String PASSPORT_PARKING2 = "PASSPORTPARKING2";
    public static final String PARK_SP_PLUS = "PARK_SP_PLUS";
    public static final String PARK_CUBTRAC = "PARK_CUBTRAC";
    public static final String PARK_OFFSTREET = "PARK_OFFSTREET";
    public static final String PARK_CURBSENSE = "PARK_CURBSENSE";
    public static final String PARK_ZONEPOLE = "PARK_ZONEPOLE";

    @PrimaryKey
    @ColumnInfo(name = "feature_id")
    @SerializedName("feature_id")
    @Expose
    private int featureId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "feature")
    @SerializedName("feature")
    @Expose
    private String feature;
    @ColumnInfo(name = "admin")
    @SerializedName("admin")
    @Expose
    private String allowedAdmin;
    @ColumnInfo(name = "officer")
    @SerializedName("officer")
    @Expose
    private String allowedOfficer;
    @ColumnInfo(name = "value")
    @SerializedName("value")
    @Expose
    private String value;
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

    public Feature() {
    }

    public Feature(JSONObject object) throws Exception {
        this.setFeatureId(object.getInt("feature_id"));
        this.setCustId(object.getInt("custid"));
        this.setFeature(object.getString("feature"));
        this.setAllowedAdmin(object.getString("admin"));
        this.setAllowedOfficer(object.getString("officer"));
        this.setValue(object.getString("value"));

        if (!object.isNull("order_number")) {
            this.setOrderNumber(object.getInt("order_number"));
        }
    }

    public static ArrayList<Feature> getFeatures(int custId) throws Exception {
        ArrayList<Feature> list = new ArrayList<Feature>();
        List<Feature> features = ParkingDatabase.getInstance(TPApplication.getInstance()).featuresDao().getFeatures().subscribeOn(Schedulers.io()).blockingGet();
        list = (ArrayList<Feature>) features;
        return list;
    }

    public static ArrayList<Feature> getFeatures(ArrayList<String> features) throws Exception {
        ArrayList<Feature> list = new ArrayList<>();
        String featuresString = "'" + TextUtils.join("','", features) + "'";
        ArrayList<Feature> list1 = (ArrayList<Feature>) ParkingDatabase.getInstance(TPApplication.getInstance()).featuresDao().getFeatures(features).subscribeOn(Schedulers.io()).blockingGet();
        //Add only allowed features
        for (Feature object : list1) {
            if ("Y".equalsIgnoreCase(object.getAllowedAdmin())
                    || "Y".equalsIgnoreCase(object.getAllowedOfficer())) {

                list.add(object);
            }
        }
        return list;
    }


    public static ArrayList<Feature> getFeaturesList(int custId) throws Exception {
        ArrayList<Feature> list = new ArrayList<>();
        //String featuresString = "'" + TextUtils.join("','", features) + "'";
        ArrayList<Feature> list1 = (ArrayList<Feature>) ParkingDatabase.getInstance(TPApplication.getInstance()).featuresDao().getFeaturesList(custId).subscribeOn(Schedulers.io()).blockingGet();
        //Add only allowed features
        for (Feature object : list1) {
            if ("Y".equalsIgnoreCase(object.getAllowedAdmin())
                    || "Y".equalsIgnoreCase(object.getAllowedOfficer())) {

                list.add(object);
            }
        }
        return list;
    }

    public static boolean isFeatureAllowed(String featureName) {
        try {
            ArrayList<Feature> features;
            features = (ArrayList<Feature>) ParkingDatabase.getInstance(TPApplication.getInstance())
                    .featuresDao().getFeature(featureName.toUpperCase()).subscribeOn(Schedulers.io()).blockingGet();
            Feature object = features.get(features.size() - 1);
            if (object == null) {
                return false;
            }
            // Check for user info
            User user = TPApplication.getInstance().getUserInfo();
            if (user == null) {
                return false;
            }

            // Check for admin features
            if (user.isAdmin() && object.getAllowedAdmin().equals("Y")) {
                return true;
            }
            // Check for officer feature
            if (user.isOfficer() && object.getAllowedOfficer().equals("Y")) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean isAutoDeleteFeatureAllowed(String featureName, Context context) {
        try {
            ArrayList<Feature> features = null;

            features = (ArrayList<Feature>) ParkingDatabase.getInstance(context).featuresDao().getFeature(featureName.toUpperCase()).subscribeOn(Schedulers.io()).blockingGet();

            Feature object = features.get(features.size() - 1);

            // Check for officer feature
            if (object == null) {
                return false;
            }
            // Check for officer feature
            if (object.getAllowedOfficer().equals("Y")) {
                return true;
            }
            // Check for officer feature
            if (object.getAllowedOfficer().equals("N")) {
                return false;
            }
            // Check for admin features
            if (object.getAllowedAdmin().equals("Y")) {
                return true;
            }
            if (object.getAllowedAdmin().equals("N")) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean isSystemFeatureAllowed(String featureName) {
        try {
            ArrayList<Feature> features;
            features = (ArrayList<Feature>) ParkingDatabase.getInstance(TPApplication.getInstance()).featuresDao().getFeature(featureName.toUpperCase()).subscribeOn(Schedulers.io()).blockingGet();
            Feature object = features.get(features.size() - 1);
            if (object == null) {
                return false;
            }
            if (object.getAllowedAdmin().equals("Y")
                    || object.getAllowedOfficer().equals("Y")) {

                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    public static String getFeatureValue(String featureName) {
        try {
            ArrayList<String> strings = (ArrayList<String>) ParkingDatabase.getInstance(TPApplication.getInstance()).featuresDao().getFeatureValue(featureName.toUpperCase()).subscribeOn(Schedulers.io()).blockingGet();
            return strings.get(strings.size() - 1);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param featureName
     * @return
     */
    public static String getFeatureIsActivie(String featureName) {
        try {
            ArrayList<String> strings = (ArrayList<String>) ParkingDatabase.getInstance(TPApplication.getInstance()).featuresDao().getFeatureValue(featureName.toUpperCase()).subscribeOn(Schedulers.io()).blockingGet();
            return strings.get(strings.size() - 1);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getFeatureParkingClearTicket(String featureName) {
        //String featureName = "AdminClearTicket";
        String value = "";
        try {
            if (featureName != null) {
                featureName = featureName.toUpperCase();
            }

            assert featureName != null;
            ArrayList<String> strings = (ArrayList<String>) ParkingDatabase.getInstance(TPApplication.getInstance()).featuresDao().getFeatureValue(featureName.toUpperCase()).subscribeOn(Schedulers.io()).blockingGet();
            return strings.get(strings.size() - 1);
        } catch (Exception e) {
            Log.e("Feature", "Error " + TPUtility.getPrintStackTrace(e));
        }

        return value;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).featuresDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).featuresDao().removeById(id);
    }

    public static boolean isNumericKeypadRequired(String fieldName) {
        if (Feature.isFeatureAllowed(Feature.KEYPAD_ENTRY)) {
            String fields = Feature.getFeatureValue(Feature.KEYPAD_ENTRY);

            return fields != null && fields.toLowerCase().contains(fieldName);
        }

        return false;
    }

    public static boolean isRequiredField(String fieldName) {
        if (Feature.isFeatureAllowed(Feature.IS_REQUIRED)) {
            String fields = Feature.getFeatureValue(Feature.IS_REQUIRED);

            return fields != null && fields.toLowerCase().contains(fieldName);
        }

        return false;
    }

    public static boolean isStickyField(String fieldName) {
        if (Feature.isFeatureAllowed(Feature.IS_STICKY)) {
            String fields = Feature.getFeatureValue(Feature.IS_STICKY);

            return fields != null && fields.toLowerCase().contains(fieldName);
        }

        return false;
    }

    public static boolean isHiddenField(String fieldName) {
        if (Feature.isFeatureAllowed(Feature.IS_HIDDEN)) {
            String fields = Feature.getFeatureValue(Feature.IS_HIDDEN);

            return fields != null && fields.toLowerCase().contains(fieldName);
        }

        return false;
    }

    /**
     * @param - Required current user logged device id as parameter
     * @return - Returns AUTO_LPR_VEHICLE_INFO Status - If the device value on server is same with the logged in device
     * it will return true else it will return false;
     * @author - bankesh
     * @DevMod? - Yes - This is in development and test mode for now.
     * @since - Version 87
     **/

    public static boolean isAutoLPRActive(int userId) {
        String fields = Feature.getFeatureValue(Feature.AUTO_LPR);

        if (fields == null) {
            return true;
        }
        if (fields.equalsIgnoreCase("null")) {
            return true;
        }
        if (fields.contains("null")) {
            return true;
        }
        if (fields.equalsIgnoreCase("")) {
            return true;
        }
        if (fields != null) {
            ArrayList allowedUserIds = new ArrayList(Arrays.asList(fields.split(",")));
            for (int i = 0; i < allowedUserIds.size(); i++) {
                if (allowedUserIds.get(i).toString().equalsIgnoreCase(String.valueOf(userId))) {
                    return true;
                }

            }
        }
        return false;
    }

    public static void insertFeature(final Feature Feature) {
        final ParkingDatabase database = ParkingDatabase.getInstance(TPApplication.getInstance());
        Completable completable = Completable.fromAction(() -> database.featuresDao().insertFeature(Feature).subscribeOn(Schedulers.io()).subscribe());
        completable.subscribe();
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
        values.put("feature_id", this.featureId);
        values.put("custid", this.custId);
        values.put("feature", this.feature.toUpperCase());
        values.put("admin", this.allowedAdmin);
        values.put("officer", this.allowedOfficer);
        values.put("value", this.value);
        values.put("order_number", this.orderNumber);

        return values;
    }

    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getAllowedAdmin() {
        return allowedAdmin;
    }

    public void setAllowedAdmin(String allowedAdmin) {
        this.allowedAdmin = allowedAdmin;
    }

    public String getAllowedOfficer() {
        return allowedOfficer;
    }

    public void setAllowedOfficer(String allowedOfficer) {
        this.allowedOfficer = allowedOfficer;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public boolean isAllowed() {
        return "Y".equalsIgnoreCase(getAllowedAdmin()) || "Y".equalsIgnoreCase(getAllowedOfficer());
    }

    private static class InsertFeature extends AsyncTask<Void, Void, Void> {
        private final Feature Feature;

        public InsertFeature(Feature Feature) {
            this.Feature = Feature;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).featuresDao().insertFeature(Feature);
            return null;
        }
    }

}
