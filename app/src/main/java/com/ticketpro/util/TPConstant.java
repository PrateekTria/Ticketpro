package com.ticketpro.util;

public class TPConstant {
    public static final long SPLASH_TIME = 3000L;
    public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;
    public static final int STATES_SEARCH_LIST = 0;
    public static final int EXP_SEARCH_LIST = 1;
    public static final int MAKE_SEARCH_LIST = 2;
    public static final int BODY_SEARCH_LIST = 3;
    public static final int COLOR_SEARCH_LIST = 4;
    public static final int SELECT_LOCATION_LIST = 5;
    public final static String INVOCATION_USER = "dj^ZjVwGs&dbalHS�gd"; //"dj^ZjVwGs&dbalHS�gd" Standard user: not allowed to change preferences or view list entries
    public final static String INVOCATION_ADMIN = "Bsv$28!Gsda7jeK^V1s"; //Privileged user: able to change settings and/or edit list entries

    public static final String MODULE_NAME = "PARKING";

    public static final String TAG = "TicketPRO";
    public static final String SECRET_KEY = "82F85DB43CBF6";
    public static final String LOG_FILE_NAME = "debug.log";
    public static final String GENERIC_ERROR_MESSAGE = "Internal System Error. Please try again.";
    public static final String PASSWORD_WRONG = "Password is not valid. Please try again.";
    public static final String GPS_LOCATION_ERROR = "Failed to get GPS location.";
    public static final String CHALK_TYPE_PLATE = "PLATE";
    public static final String CHALK_TYPE_LOCATION = "LOCATION";
    public static final String CHALK_TYPE_PHOTO = "PHOTO";

    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "onServerExpirationTimeMs";
    public static final String DISPLAY_MESSAGE_ACTION = "com.ticketpro.parking.DISPLAY_MESSAGE";

    //Parking New GCM_SENER_ID
    //public static final String GCM_SENDER_ID = "1058104561041";
    public static final String GOOGLE_MAP_API_KEY_TICKETPROAPP = "AIzaSyDoMyTlPaSi06ENXOumZzXbXAWLVwFT5m4";
    //Test original Id
    public static final String GCM_SENDER_ID = "921303961093";

    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_MESSAGE_TYPE = "messageType";
    public static final String EXTRA_END_SHIFT = "EndShift";
    public static final String EXTRA_RESTORE_SESSION = "RestoreSession";
    public static final String MAX_COMMENT_CHAR_LIMIT = "Max - 32 Chars Allowed";
    public static final long GPS_STOP_DELAY = 50000;
    public static final String EDUMUND_GOV_URL = "http://vpic.nhtsa.dot.gov/api/vehicles/decodevinvalues/";
    public static final String INTENT_RECAPTURE_CONSTANT = "RecaptureImage";
    public static final String FCM_TOKEN = "fcm_token";
    public static final String SAMTRANS_TOKEN = "samtransToken";
    public static final String PARKING_TXT_FILE = "parking.txt";
    public static final String PUSH_TOKEN = "";
    public static final String CURRENT_SAMTRANS_SPACE = "currentSamtransPlace";
    public static final String PARKEON_ZONE_ID = "parkeon_zone_id";
    public static final String PARKEON_ZONE_NAME = "parkeon_zone_name";
    public static final String PLATE = "plate";
    public static String LOT_NAME = "Lot 1";
    public static String LOT_DESC = "Lot Desc";
    public static final String SPACE = "space";
    public static final String LOCATION = "Location";


    public static boolean IS_DEBUG_MODE = false;
    public static final String API_BASE_URL = "https://preproduction-svc-cwo2.calesystems.com/cwo2exportservice/Enforcement/1/EnforcementService.svc/";

    public static final int PRODUCTION = 1000;
    public static final int DEVELOPMENT = 2000;
    public static final String VENDOR_CODE = "vendor_code";
    public static final int STAGING = 3000;
    public static boolean IS_STAGING_BUILD = false;

    //public static String SERVICE_URL="http://192.168.0.101/TicketPROAdmin/public/index.php/service";
    //public static String ASSETS_URL="http://192.168.0.101/TicketPROAdmin/public/assets/customers";
    //public static String UPDATE_URL="http://192.168.0.101/TicketPROAdmin/public/updates";
    //public static String IMAGES_URL="http://192.168.0.101/TicketPROAdmin/public/images/customers";
    //public static String LPR_URL="http://lpr.ticketproweb.com/LPRWcfService/LPRService.svc?wsdl";
    //public static boolean IS_DEVELOPMENT_BUILD = true;

    //Development URL Bankesh
	/*public static String SERVICE_URL="http://192.168.1.7/ticketpro/public/index.php/service";
  	public static String ASSETS_URL="http://192.168.1.7/ticketpro/public/assets/customers";
  	public static String UPDATE_URL="http://192.168.1.7/ticketpro/public/updates";
  	public static String IMAGES_URL="http://192.168.1.7/ticketpro/public/images/customers";
    //public static String LPR_URL="http://lprdev.ticketproweb.com/LPRWcfService/LPRService.svc?wsdl";
    public static boolean IS_DEVELOPMENT_BUILD=true;
	public static String LPR_URL="http://lpr.ticketproweb.com/LPRWcfService/LPRService.svc?wsdl";*/

    //public static String SERVICE_URL="http://216.237.9.106/public/index.php/service";
    //public static String ASSETS_URL="http://216.237.9.106/public/assets/customers";
    //public static String IMAGES_URL="http://216.237.9.106/public/images/customers";
    //public static String SERVICE_URL="http://216.237.9.107/public/index.php/service";
    //public static String ASSETS_URL="http://216.237.9.107/public/assets/customers";
    //public static String IMAGES_URL="http://216.237.9.107/public/images/customers";

    //Production URL
    public static String FILE_UPLOAD = "https://tpwebservices.ticketproweb.com/public/index.php/service";
    public static String SERVICE_URL = "https://tpwebservices.ticketproweb.com/public/index.php/service/genericv1";
    public static String RX_SERVICE_URL = "https://tpwebservices.ticketproweb.com/public/index.php/";
    public static String ASSETS_URL = "https://tpwebservices.ticketproweb.com/public/assets/customers";
    public static String UPDATE_URL = "https://tpwebservices.ticketproweb.com/public/updates";
    public static String IMAGES_URL = "https://tpwebservices.ticketproweb.com/public/images/customers";
    public static String LPR_URL = "http://lpr.ticketproweb.com/LPRWcfService/LPRService.svc?wsdl";
    public static String FIREBASE_DB_URL = "http://tracker.ticketproweb.com:8081/api/";
    public static boolean IS_DEVELOPMENT_BUILD = false;

    //Development URL
    /*public static String RX_SERVICE_URL="https://tpwebservicesdev.ticketproweb.com/public/index.php/";
    public static String SERVICE_URL = "https://tpwebservicesdev.ticketproweb.com/public/index.php/service";
    public static String ASSETS_URL = "https://tpwebservicesdev.ticketproweb.com/public/assets/customers";
    public static String UPDATE_URL = "https://tpwebservicesdev.ticketproweb.com/public/updates";
    public static String IMAGES_URL = "https://tpwebservicesdev.ticketproweb.com/public/images/customers";
    public static String LPR_URL = "http://lprdev.ticketproweb.com/LPRWcfService/LPRService.svc?wsdl";
    public static String FIREBASE_DB_URL = "http://trackerdev.ticketproweb.com:8081/api/";
    public static boolean IS_DEVELOPMENT_BUILD = true;*/

    //Local URL
	/*public static String SERVICE_URL="https://tpwebservicesdev.ticketproweb.com/public/index.php/service";
    public static String ASSETS_URL="https://tpwebservicesdev.ticketproweb.com/public/assets/customers";
    public static String UPDATE_URL="https://tpwebservicesdev.ticketproweb.com/public/updates";
    public static String IMAGES_URL="https://tpwebservicesdev.ticketproweb.com/public/images/customers";
	public static String LPR_URL="http://lprdev.ticketproweb.com/LPRWcfService/LPRService.svc?wsdl";
	public static String FIREBASE_DB_URL="http://119.82.68.243:8083/api/";
	public static boolean IS_DEVELOPMENT_BUILD = true;*/


    //public static String LPR_URL="http://lpr.ticketproweb.com/LPRWcfService/LPRService.svc?wsdl";
    //public static String SERVICE_URL="http://10.0.2.2/TicketPROAdmin/public/service";
    //public static String ASSETS_URL="http://10.0.2.2/TicketPROAdmin/public/assets/customers";
    //public static String IMAGES_URL="http://10.0.2.2/TicketPROAdmin/public/images/customers";
    //public static String LPR_URL="http://lpr.ticketproweb.com/LPRWcfService/LPRService.svc?wsdl";
    //public static boolean IS_DEVELOPMENT_BUILD = false;


    public static final String PREFS_KEY_DBCONFIGURATION = "dbConfigured";
    public static final String PREFS_KEY_DBSYNCTIME = "dbSyncDateTime";
    public static final String PREFS_SCHEDULE_TIME = "scheduleTime";
    public static final String PREFS_KEY_RESTORE_SESSION = "restoreSession";
    public static final String PREFS_KEY_RESTORE_USERID = "restoreUserId";
    public static final String PREFS_KEY_LAST_USERNAME = "lastUsername";
    public static final String PREFS_KEY_LAST_USERPWD = "lastUserPwd";
    public static final String PREFS_KEY_RESTORE_SHAREDOBJECT = "restoreSharedObject";
    public static final String PREFS_KEY_RESTORE_DEVICEID = "restoreDeviceId";
    public static final String PREFS_KEY_RESTORE_DUTYID = "restoreDutyId";
    public static final String PREFS_KEY_RESTORE_CUSTID = "restoreCustId";
    public static final String PREFS_KEY_STICKY_COMMENTS = "stickyComments";
    public static final String PREFS_KEY_LASTTICKETTIME = "lastTicketTime";
    public static final String PREFS_KEY_STICKY_VIOLATIONS = "stickyViolations";
    public static final String PREFS_KEY_STICKY_PHOTO = "stickyPhoto";
    public static final String PREFS_KEY_PRINT_DESCLAIMER = "printDesclaimer";
    public static final String PREFS_KEY_PRINT_DEBUG = "printDebug";
    public static final String PREFS_KEY_FB_TOKEN = "fbToken";
    public static final String PREFS_KEY_SHOW_PRINTDIALOG = "showPrintDialog";
    public static final String PREFS_KEY_IS_LOGGED_IN = "loggedIn";
    public static final String PREFS_KEY_LPR_FLASHLED = "LPRFlashLED";
    public static final String PREFS_KEY_BARCODE_FLASHLED = "BarcodeFlashLED";
    public static final String PREFS_KEY_PICTURE_FLASHLED = "PictureFlashLED";
    public static final String PREFS_KEY_ENABLE_CHALKALERTS = "enableChalkAlerts";
    public static final String PREFS_KEY_ENABLE_MOBILENOW = "enableMobileNow";
    public static final String PREFS_KEY_ENABLE_PASSPORT_PARKING = "enablePassportParking";
    public static final String PREFS_KEY_ENABLE_PASSPORT_PARKING2 = "enablePassportParking2";
    public static final String PREFS_KEY_ENABLE_CUBTRAC = "enableCubtrac";
    public static final String PREFS_KEY_ENABLE_PARK_MOBILE = "enableParkMobile";
    public static final String PREFS_KEY_ENABLE_PAY_BY_PHONE = "enablePayByPhone";
    public static final String PREFS_KEY_ENABLE_PROGRESSIVE = "enableProgressive";
    public static final String PREFS_KEY_ENABLE_DPT = "enableDPT";
    public static final String PREFS_KEY_ENABLE_CALE = "enableCale";
    public static final String PREFS_KEY_ENABLE_OFFSTEER = "enableOffstreet";
    public static final String PREFS_KEY_ENABLE_CALE2 = "enableCale2";

    public static final String PREFS_KEY_ENABLE_PARKEON = "enableParkeon";
    public static final String PREFS_KEY_ENABLE_SAMTRANS = "enableSmatrans";
    public static final String PREFS_KEY_ENABLE_IPS = "enableIPS";
    public static final String PREFS_KEY_ENABLE_LOG = "enableLOG";

    public static final String TABLE_TICKETS = "tickets";
    public static final String TABLE_TICKET_COMMENTS = "ticket_comments";
    public static final String TABLE_TICKET_PICTURES = "ticket_pictures";
    public static final String TABLE_TICKET_VIOLATIONS = "ticket_violations";
    public static final String TABLE_CHALKS = "chalk_vehicles";
    public static final String TABLE_CHALK_PICTURES = "chalk_pictures";
    public static final String TABLE_CHALK_COMMENTS = "chalk_comments";
    public static final String TABLE_DUTY_REPORTS = "duty_reports";
    public static final String TABLE_SETTINGS = "user_settings";
    public static final String TABLE_DEVICES = "devices";
    public static final String TABLE_CUSTOMERS = "customers";
    public static final String TABLE_SPECIAL_ACTIVITY_REPORTS = "special_activity_reports";
    public static final String TABLE_CALL_IN_REPORTS = "call_in_reports";
    public static final String TABLE_ERROR_LOGS = "error_logs";
    public static final String TABLE_SYNC_DATA = "sync_data";
    public static final String TABLE_PENDING_IMAGES = "pending_images";
    public static final String TABLE_PENDING_VOICES = "pending_voice_comments";
    public static final String TABLE_ALPR_CHALK = "ALPRPhotoChalk";
    public static final String TABLE_HOTLIST = "hotlist";
    public static final String LOCAL_BROADCAST_EVENT = "TicketPRO-Events";
    public static final String LOCAL_BROADCAST_REFRESH_CHALK_LIST = "REFRESH_CHALK_LIST";
    public static final String PREFS_KEY_APP_SHORTCUT = "AppShortcut";
    public static final String REPEAT_OFFENDER = "repeat_offenders";
    public static final String TABLE_FT_DeviceHistory = "FT_DeviceHistory";

    public static final String PREFS_KEY_SETTING_LOCATION_KEYBOARD = "LocationKeyboard";
    public static final String PREFS_KEY_SETTING_COMMENT_KEYBOARD = "CommentsKeyboard";
    public static final String PREFS_KEY_SETTING_VIOLATION_KEYBOARD = "ViolationKeyboard";
    public static final String PREFS_KEY_SETTING_MAKE_KEYBOARD = "MakeKeyboard";
    public static final String PREFS_KEY_SETTING_BODY_KEYBOARD = "BodyKeyboard";
    public static final String PREFS_KEY_SETTING_COLOR_KEYBOARD = "ColorKeyboard";
    public static final String PREFS_KEY_SETTING_SKIP_LOCATION_ENTRY = "SkipLocationEntry";
    public static final String PREFS_KEY_SETTING_AUTO_LOOKUP = "AutoLookup";
    public static final String PREFS_KEY_SETTING_SECOND_LOCATION_ENTRY = "SecondLocation";
    public static final String PREFS_KEY_SETTING_ACCORDION_LOOKUP = "AccordionLookup";
    public static final String PREFS_KEY_ENABLE_VIN_PASSPORT_PARKING = "VinPassport";
    public static final String PREFS_KEY_SETTING_SEARCH_CONTAINS = "SearchContains";
    public static final String PREFS_KEY_SETTING_AUTO_BRIGHTNESS = "AutoBrightness";
    public static final String PREFS_KEY_SETTING_SAVED_BRIGHTNESS = "SavedBrightness";
    public static final String PREFS_KEY_SETTING_STICKY_ZOOM = "StickyZoom";
    public static final String PREFS_KEY_SETTING_STICKY_LPR_ZOOM = "StickyLPRZoom";
    public static final String PREFS_KEY_SETTING_CACHE_EXPIRY = "CacheExpiry";
    public static final String PREFS_KEY_STICKY_MARKERS = "StickyMarkers";
    public static final String PREFS_KEY_STICKY_MARKER_C_X = "StickyMarkerCX";
    public static final String PREFS_KEY_STICKY_MARKER_C_Y = "StickyMarkerCY";
    public static final String PREFS_KEY_STICKY_MARKER_S_X = "StickyMarkerSX";
    public static final String PREFS_KEY_STICKY_MARKER_S_Y = "StickyMarkerSY";
    public static final String PREFS_KEY_CLEAR_LOCATION = "ClearLocation";
    public static final String PREFS_KEY_SETTING_AUTO_PROMPT_VEHICLE = "AutoPromptVehicle";
    public static final String PREFS_KEY_SETTING_ALPR_VEHICLE_INFO_REQUIRED = "ALPRVehicleInfoRequired";
    public static final String PREFS_KEY_SETTING_ALPR_VEHICLE_NIGHT_MODE = "ALPRVehicleNightMode";
    public static final String PREFS_KEY_SETTING_HOME_KEYBOARD = "HomeKeyboard";

    public static final String USER_DEFAULT_PREFS_SETTINGS = "{ \"SecondLocation\": false, \"LocationKeyboard\": false, \"MakeKeyboard\": false, \"AccordionLookup\": false, \"ColorKeyboard\": false, \"AutoPromptVehicle\": false, \"CacheExpiry\": 10, \"CommentsKeyboard\": false, \"SearchContains\": false, \"StickyMarkers\": false, \"AutoLookup\": false, \"SkipLocationEntry\": false, \"BodyKeyboard\": false, \"ViolationKeyboard\": false }";


    public static String timeStamp1 = "0";
    public static String DELETE_TICKETS = "DELETE TICKETS";
    public static double longitude;
    public static double latitude;
    //public static String activityName = "";

    public static boolean isCharging = false;
    public static boolean acCharge = false;
    public static boolean usbCharge = false;
    public static boolean parkeon = false;
    public static String licensekey;


    public static final String PARK_KEEP_STICKY_VIOLATION = "PARK_KeepStickyViolation";

    public static String lastInsertedDeviceDataID = "lastInsertedDeviceDataID";
}
