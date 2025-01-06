package com.ticketpro.parking.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.Location;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.ALPRChalk;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.CustomerInfo;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.model.Duration;
import com.ticketpro.model.Duty;
import com.ticketpro.model.DutyReport;

import com.ticketpro.model.GenetecPatrollers;
import com.ticketpro.model.Hotlist;
import com.ticketpro.model.PlateLookupResult;
import com.ticketpro.model.SpecialActivityReport;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.model.User;
import com.ticketpro.model.UserSetting;
import com.ticketpro.model.Violation;
import com.ticketpro.parking.service.ParkingTicketWorker;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;
import com.ticketpro.vendors.CachedMap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TPApplication extends MultiDexApplication {

    public static int notificationId = 0;
    private static TPApplication singleton;
    final String ACCOUNT_NAME = "TicketPRO Parking";
    final String ACCOUNT_TYPE = "com.ticketpro.parking.service.account";
    final String PROVIDER = "com.ticketpro.parking.service.provider";
    public Duty activeDutyInfo;
    public Activity currentAcivity;
    public int networkLevel = 0;
    public String lastGPSLocation = "";
    public ArrayList<Intent> notificationIntents = new ArrayList<Intent>();
    public String externalStoragePath = null;
    public String deviceName;
    public String lastPlateNumber = "";
    public String lastVinNumber = "";
    public String deviceIdName;
    public boolean enableCale2 = true;
    private SharedPreferences mPreferences;

    public String getDeviceIdName() {
        return deviceIdName;
    }

    public void setDeviceIdName(String deviceIdName) {
        this.deviceIdName = deviceIdName;
    }

    public String pushToken;
    public boolean isGPSEnabled;
    public boolean offlineMode = false;
    public boolean isVendorCode = false;
    public boolean dbConfigured = false;
    public boolean stickyComments = false;
    public boolean stickyViolations = false;
    public boolean stickyPhoto = false;
    public boolean stickyLocation = false;
    public boolean printDebugMode = false;
    public boolean showPrintDialog = false;
    public int lastViewFinderWidth = 290;
    public int lastViewFinderHeight = 150;
    public boolean voiceMode = false;
    public boolean LPRFlashLED = false;
    public boolean pictureFlashLED = false;
    public boolean barcodeFlashLED = false;
    public boolean resumeFromNotification = false;
    public boolean useExtStorage = false;
    public boolean checkNetworkSignal = false;
    public boolean enableChalkAlerts = true;
    public boolean enableMobileNow = true;
    public boolean enablePassportParking = true;
    public boolean enablePassportParking2 = true;
    public boolean enableVinPassportParking = true;
    public boolean enableParkMobile = true;
    public boolean enablePayByPhone = true;
    public boolean enableDPT = true;
    public boolean enableIPS = true;
    public boolean enableCale = true;
    public boolean enableParkeon = true;
    public boolean enableSamtrans = true;
    public boolean enableGenetec = true;
    public boolean enableProgressive = true;
    public boolean enableCubtrac = true;
    public boolean enableOffStreet = true;
    public boolean enableCurbsense = true;
    public boolean enableZonePole = true;
    public boolean disableSync = false;
    public int transactionTimeout = 30000;
    public boolean isServiceAvailable = true;
    public Location gpsLocation;
    public Point stickyMarkerS;
    public Point stickyMarkerC;
    public ArrayList<ChalkVehicle> chalkList = new ArrayList<ChalkVehicle>();
    public String IPSSpaceGroup = null;
    public String IPSSubArea = null;
    public boolean hasKeyboardPopup = false;
    public CachedMap cachedMap = new CachedMap();
    public ArrayList<Ticket> ticketList;
    public PlateLookupResultPriorTickets plateLookupActivity;
    public PlateLookupResultAdvance plateLookupResultActivity;
    public boolean updateCheckInProgress = false;
    public int deviceId;
    public int userId;
    public int patrollerId;
    public int custId;
    private Date lastGPSTime;
    private Date lastNetworkTime;
    private String firstLogin;
    private DeviceInfo deviceInfo;
    private CustomerInfo customerInfo;
    private User userInfo;
    private GenetecPatrollers genetecPatrollersInfo;
    private Ticket activeTicket;
    private Ticket sharedTicket;
    private ChalkVehicle activeChalk;
    private ALPRChalk alprChalk;
    private UserSetting userSettings;
    private DutyReport activeDutyReport;
    private TicketComment stickyComment;
    private TicketViolation stickyViolation;
    private TicketPicture stickyPhotos;
    private Hotlist activeHotlist;
    private int dutyId;
    private String dutyName;
    private PlateLookupResult activeLookupResult;
    private ArrayList<TicketViolation> lastViolations = null;
    private ArrayList<TicketPicture> lastPhotos = new ArrayList<>();
    private SpecialActivityReport report;

    private int ipsParkMobile = 0;
    private String netOnOff = "";
    public int[]  multipleCitation;

    public int[] getMultipleCitation() {
        return multipleCitation;
    }

    public void setMultipleCitation(int[] multipleCitation) {
        this.multipleCitation = multipleCitation;
    }
    public static TPApplication getInstance() {
        return singleton;
    }

    public static int getNotificationId() {
        return notificationId;
    }

    public static void setNotificationId(int notificationId) {
        TPApplication.notificationId = notificationId;
    }

    public static TPApplication getSingleton() {
        return singleton;
    }

    public static void setSingleton(TPApplication singleton) {
        TPApplication.singleton = singleton;
    }

    public boolean isEnablePassportParking2() {
        return enablePassportParking2;
    }

    public void setEnablePassportParking2(boolean enablePassportParking2) {
        this.enablePassportParking2 = enablePassportParking2;
    }

    public boolean isEnableCubtrac() {
        return enableCubtrac;
    }

    public void setEnableCubtrac(boolean enableCubtrac) {
        this.enableCubtrac = enableCubtrac;
    }

    public int getIpsParkMobile() {
        return ipsParkMobile;
    }

    public boolean isVendorCode() {
        return isVendorCode;
    }

    public void setVendorCode(boolean vendorCode) {
        isVendorCode = vendorCode;
    }

    public void setIpsParkMobile(int ipsParkMobile) {
        this.ipsParkMobile = ipsParkMobile;
    }

    public SpecialActivityReport getReport() {
        return report;
    }

    public void setReport(SpecialActivityReport report) {
        this.report = report;
    }

    public String getFirstLogin() {
        return firstLogin;
    }

    public String getNetOnOff() {
        return netOnOff;
    }

    public boolean isEnableOffStreet() {
        return enableOffStreet;
    }

    public void setEnableOffStreet(boolean enableOffStreet) {
        this.enableOffStreet = enableOffStreet;
    }

    public void setNetOnOff(String netOnOff) {
        this.netOnOff = netOnOff;
    }

    public void setFirstLogin(String firstLogin) {
        this.firstLogin = firstLogin;
    }

    public ALPRChalk getAlprChalk() {
        return alprChalk;
    }

    public void setAlprChalk(ALPRChalk alprChalk) {
        this.alprChalk = alprChalk;
    }

    public int getDutyId() {
        return dutyId;
    }

    public void setDutyId(int dutyId) {
        this.dutyId = dutyId;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();
       /* PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(
                ParkingTicketWorker.class,
                5, TimeUnit.MINUTES).setConstraints(constraints)
                .addTag("Sync Pending Ticket")
                .build();

        WorkManager.getInstance(this).enqueue(periodicWork);*/


        // Unique name for the WorkRequest
        String uniqueWorkName = "uniqueWorkName";

        PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(
                ParkingTicketWorker.class,
                5, TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                uniqueWorkName,
                ExistingPeriodicWorkPolicy.REPLACE, // or REPLACE, depending on your requirements
                periodicWork
        );
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void resetUserSession() {
        this.userSettings = null;
        this.activeDutyReport = null;
        this.userInfo = null;
        this.activeDutyInfo = null;
        this.activeTicket = null;
        this.activeChalk = null;
        this.alprChalk = null;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    public ArrayList<TicketPicture> getLastPhotos() {
        return lastPhotos;
    }

    public void setLastPhotos(ArrayList<TicketPicture> lastPhotos) {
        this.lastPhotos = lastPhotos;
    }

    public Date getLastGPSTime() {
        return lastGPSTime;
    }

    public void setLastGPSTime(Date lastGPSTime) {
        this.lastGPSTime = lastGPSTime;
    }

    public boolean isGPSEnabled() {
        return isGPSEnabled;
    }

    public void setGPSEnabled(boolean isGPSEnabled) {
        this.isGPSEnabled = isGPSEnabled;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        if (deviceInfo != null) {
            deviceId = deviceInfo.getDeviceId();
        }
        this.deviceInfo = deviceInfo;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        if (customerInfo != null) {
            custId = customerInfo.getCustId();
        }

        this.customerInfo = customerInfo;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        if (userInfo != null) {
            userId = userInfo.getUserId();
        }

        this.userInfo = userInfo;
    }

    public GenetecPatrollers getGenetecPatrollersInfo() {
        return genetecPatrollersInfo;
    }

    public void setGenetecPatrollersInfo(GenetecPatrollers genetecPatrollersInfo) {
        if (genetecPatrollersInfo != null) {
            patrollerId = Integer.parseInt(genetecPatrollersInfo.getPatrollerId());
        }

        this.genetecPatrollersInfo = genetecPatrollersInfo;
    }

    public Duty getActiveDutyInfo() {
        return activeDutyInfo;
    }

    public void setActiveDutyInfo(Duty activeDutyInfo) {
        this.activeDutyInfo = activeDutyInfo;
    }

    public Ticket getActiveTicket() {
        return activeTicket;
    }

    public void setActiveTicket(Ticket activeTicket) {
        this.activeTicket = activeTicket;
    }

    public Ticket getSharedTicket() {
        return sharedTicket;
    }

    public void setSharedTicket(Ticket sharedTicket) {
        this.sharedTicket = sharedTicket;
    }

    public Ticket createNewTicket() {
        Ticket ticket = new Ticket();
        try {
            if (this.deviceInfo != null) {
                ticket.setDeviceId(this.deviceInfo.getDeviceId());
                // ticket.setTicketId(Ticket.getNextPrimaryId());
                ticket.setUserId(this.userInfo.getUserId());
                ticket.setTicketDate(new Date());
                ticket.setTicketDateNew(DateUtil.getCurrentDateTime1());
                ticket.setCustId(this.deviceInfo.getCustId());
                ticket.setDutyId(this.activeDutyInfo.getId());
                ticket.setIsLPR("N");

                if (this.deviceInfo.getCurrentCitationNumber() < this.deviceInfo.getStartCitationNumber()) {
                    this.deviceInfo.setCurrentCitationNumber(this.deviceInfo.getStartCitationNumber());
                }

                if (this.deviceInfo.getCurrentCitationNumber() == 0) {
                    this.deviceInfo.setCurrentCitationNumber(1);
                }

                //Get Next Citation#
                ticket.setCitationNumber(this.deviceInfo.getCurrentCitationNumber());
            } else {
                mPreferences = getApplicationContext().getSharedPreferences(getPackageName(), MODE_PRIVATE);
                int deviceId = mPreferences.getInt(TPConstant.PREFS_KEY_RESTORE_DEVICEID, 0);
                DeviceInfo deviceInfo = DeviceInfo.getDeviceInfoById(deviceId);
                if (deviceInfo != null) {
                    TPApplication.getInstance().setDeviceInfo(deviceInfo);

                    // Get Customer Info
                    CustomerInfo customerInfo = CustomerInfo.getCustomerInfo(deviceInfo.getCustId());
                    TPApplication.getInstance().setCustomerInfo(customerInfo);
                    mPreferences = getApplicationContext().getSharedPreferences(getPackageName(), MODE_PRIVATE);
                    int dutyId = mPreferences.getInt(TPConstant.PREFS_KEY_RESTORE_DUTYID, 0);
                    int userId = mPreferences.getInt(TPConstant.PREFS_KEY_RESTORE_USERID, 0);
                    int custId = mPreferences.getInt(TPConstant.PREFS_KEY_RESTORE_CUSTID, 0);
                    if (dutyId > 0 ) {
                        Duty duty = Duty.getDutyById(dutyId);
                        TPApplication.getInstance().setActiveDutyInfo(duty);

                        DutyReport dutyReport = new DutyReport();
                        dutyReport.setDateIn(new Date());
                        dutyReport.setDutyId(dutyId);
                        dutyReport.setUserId(userId);
                        dutyReport.setCustId(custId);
                        TPApplication.getInstance().setActiveDutyReport(dutyReport);
                    }
                    TPApplication.getInstance().setUserInfo(User.getUserInfo(userId));
                    ticket.setDeviceId(deviceId);
                    // ticket.setTicketId(Ticket.getNextPrimaryId());
                    ticket.setUserId(userId);
                    ticket.setTicketDate(new Date());
                    ticket.setTicketDateNew(DateUtil.getCurrentDateTime1());
                    ticket.setCustId(this.deviceInfo.getCustId());
                    ticket.setDutyId(this.activeDutyInfo.getId());
                    ticket.setIsLPR("N");

                    if (this.deviceInfo.getCurrentCitationNumber() < this.deviceInfo.getStartCitationNumber()) {
                        this.deviceInfo.setCurrentCitationNumber(this.deviceInfo.getStartCitationNumber());
                    }

                    if (this.deviceInfo.getCurrentCitationNumber() == 0) {
                        this.deviceInfo.setCurrentCitationNumber(1);
                    }

                    //Get Next Citation#
                    ticket.setCitationNumber(this.deviceInfo.getCurrentCitationNumber());
                }
            }
        } catch (Exception e) {
            Log.e("TPApp", TPUtility.getPrintStackTrace(e));
        }
        return ticket;
    }

    public ALPRChalk createPhotoChalk() {
        ALPRChalk chalk = new ALPRChalk();
        try {
            if (this.deviceInfo != null) {
                chalk.setDeviceId(this.deviceInfo.getDeviceId());
                chalk.setUserid(this.userInfo.getUserId());
                chalk.setChalkId(ChalkVehicle.getNextPrimaryId());
                chalk.setCustId(this.custId);
            }
        } catch (Exception ignored) {
        }

        return chalk;
    }

    public ChalkVehicle createNewChalk() {
        ChalkVehicle chalk = new ChalkVehicle();
        try {
            if (this.deviceInfo != null) {
                chalk.setDeviceId(this.deviceInfo.getDeviceId());
                chalk.setUserId(this.userInfo.getUserId());
                chalk.setChalkId(ChalkVehicle.getNextPrimaryId());
                chalk.setChalkDate(new Date());
                //deviceInfo.getCustId();
                chalk.setCustId(this.deviceInfo.getCustId());
            }
        } catch (Exception ignored) {
        }

        return chalk;
    }

    public ChalkVehicle getActiveChalk() {
        return activeChalk;
    }

    public void setActiveChalk(ChalkVehicle activeChalk) {
        this.activeChalk = activeChalk;
    }

    public boolean isGPSEnabledForActiveUser() {
        return userSettings == null || userSettings.getGps() == null || !userSettings.getGps().equals("N");
    }

    public Ticket createTicketForChalk(ALPRChalk chalk) {
        Ticket ticket = null;
        try {
            ticket = this.createNewTicket();
            ticket.setChalkId(chalk.getChalkId());
            ticket.setIsChalked("Y");
            ticket.setMeter("");
            ticket.setStateCode("");
            ticket.setStateId(0);
            ticket.setVin("");
            ticket.setPlate(chalk.getPlate());
            ticket.setLocation(chalk.getChalkLocation());
            ticket.setStreetNumber("");
            ticket.setStreetPrefix("");
            ticket.setStreetSuffix("");
            ticket.setDirection("");
            ticket.setLatitude(chalk.getLastLocLat());
            ticket.setLongitude(chalk.getLastLocLong());
            ticket.setIsGPSLocation("");
            ticket.setTimeMarked(chalk.getFirstDateTime());
            ticket.setSpace("");
            ticket.setCustId(this.custId);
            ticket.setTimeZone("");
            ticket.setMakeCode("");
            ticket.setColorCode("");

            Duration duration = Duration.getDurationById(chalk.getChalkDurationId());
            if (duration != null) {
                ticket.setChalkDuration(duration.getDurationMinutes());
                ticket.setChalkZone(duration.getTitle());

                Violation violation = Violation.getViolationById(duration.getDefaultViolationId());
                if (violation != null) {
                    TicketViolation tv = new TicketViolation();
                    tv.setTicketId(ticket.getTicketId());
                    tv.setViolationDesc(violation.getTitle());
                    tv.setViolationDisplay(violation.getViolationDisplay());
                    tv.setViolationCode(violation.getCode());
                    tv.setViolationId(violation.getId());
                    tv.setCitationNumber(ticket.getCitationNumber());
                    tv.setFine(violation.getBaseFine());
                    ticket.getTicketViolations().add(tv);
                }
            }
            Date dt = new Date();
            long milliseconds = (dt.getTime() - chalk.getFirstDateTime().getTime());
            int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
            int hours = (int) ((milliseconds / (1000 * 60 * 60)));
            String hrs = (hours < 10) ? ("0" + hours) : hours + "";
            String mins = (minutes < 10) ? ("0" + minutes) : minutes + "";
            ticket.setElapsed(hrs + ":" + mins + " hrs/min");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ticket;
    }

    public Ticket createTicketForChalk(ChalkVehicle chalk) {
        Ticket ticket = null;
        try {
            ticket = this.createNewTicket();
            ticket.setChalkId(chalk.getChalkId());
            ticket.setIsChalked("Y");
            ticket.setMeter(chalk.getMeter());
            ticket.setStateCode(chalk.getStateCode());
            ticket.setStateId(chalk.getStateId());
            ticket.setVin(chalk.getVin());
            ticket.setPlate(chalk.getPlate());
            ticket.setLocation(chalk.getLocation());
            ticket.setStreetNumber(chalk.getStreetNumber());
            ticket.setStreetPrefix(chalk.getStreetPrefix());
            ticket.setStreetSuffix(chalk.getStreetSuffix());
            ticket.setDirection(chalk.getDirection());
            ticket.setLatitude(chalk.getLatitude());
            ticket.setLongitude(chalk.getLongitude());
            ticket.setIsGPSLocation(chalk.getIsGPSLocation());
            ticket.setTimeMarked(chalk.getChalkDate());
            ticket.setSpace(chalk.getSpace());
            ticket.setCustId(this.custId);
            ticket.setTimeZone(chalk.getDurationCode());
            ticket.setMakeCode(chalk.getMakeCode());
            ticket.setColorCode(chalk.getColorCode());

            Duration duration = Duration.getDurationById(chalk.getDurationId());
            if (duration != null) {
                ticket.setChalkDuration(duration.getDurationMinutes());
                ticket.setChalkZone(duration.getTitle());

                Violation violation = Violation.getViolationById(duration.getDefaultViolationId());
                if (violation != null) {
                    TicketViolation tv = new TicketViolation();
                    tv.setTicketId(ticket.getTicketId());
                    tv.setViolationDesc(violation.getTitle());
                    tv.setViolationDisplay(violation.getViolationDisplay());
                    tv.setViolationCode(violation.getCode());
                    tv.setViolationId(violation.getId());
                    tv.setCitationNumber(ticket.getCitationNumber());
                    tv.setFine(violation.getBaseFine());
                    ticket.getTicketViolations().add(tv);
                }
            }

            Date dt = new Date();
            long milliseconds = (dt.getTime() - chalk.getChalkDate().getTime());
            int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
            int hours = (int) ((milliseconds / (1000 * 60 * 60)));
            String hrs = (hours < 10) ? ("0" + hours) : hours + "";
            String mins = (minutes < 10) ? ("0" + minutes) : minutes + "";
            ticket.setElapsed(hrs + ":" + mins + " hrs/min");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ticket;
    }

    public boolean isOfflineMode() {
        return offlineMode;
    }

    public void setOfflineMode(boolean offlineMode) {
        this.offlineMode = offlineMode;
    }

    public boolean isDbConfigured() {
        return dbConfigured;
    }

    public void setDbConfigured(boolean dbConfigured) {
        this.dbConfigured = dbConfigured;
    }

    public UserSetting getUserSettings() {
        if (userSettings == null) {
            try {
                userSettings = UserSetting.getUserSettings(userId);
            } catch (TPException e) {
                userSettings = new UserSetting();
            }
        }

        return userSettings;
    }

    public void setUserSettings(UserSetting userSettings) {
        this.userSettings = userSettings;
    }

    public void updateUserSettings() {
        try {
            UserSetting.insertUserSetting(getUserSettings());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DutyReport getActiveDutyReport() {
        return activeDutyReport;
    }

    public void setActiveDutyReport(DutyReport activeDutyReport) {
        this.activeDutyReport = activeDutyReport;
    }

    public boolean isStickyComments() {
        return stickyComments;
    }

    public void setStickyComments(boolean stickyComments) {
        this.stickyComments = stickyComments;
    }

    public TicketComment getStickyComment() {
        return stickyComment;
    }

    public void setStickyComment(TicketComment stickyComment) {
        this.stickyComment = stickyComment;
    }

    public boolean isEnableCale2() {
        return enableCale2;
    }

    public void setEnableCale2(boolean enableCale2) {
        this.enableCale2 = enableCale2;
    }

    public TicketViolation getStickyViolation() {
        return stickyViolation;
    }

    public void setStickyViolation(TicketViolation stickyViolation) {
        this.stickyViolation = stickyViolation;
    }

    public TicketPicture getStickyPhotos() {
        return stickyPhotos;
    }

    public void setStickyPhotos(TicketPicture stickyPhotos) {
        this.stickyPhotos = stickyPhotos;
    }

    public Hotlist getActiveHotlist() {
        return activeHotlist;
    }

    public void setActiveHotlist(Hotlist activeHotlist) {
        this.activeHotlist = activeHotlist;
    }

    public boolean isPrintDebugMode() {
        return printDebugMode;
    }

    public void setPrintDebugMode(boolean printDebugMode) {
        this.printDebugMode = printDebugMode;
    }

    public int getLastViewFinderWidth() {
        return lastViewFinderWidth;
    }

    public void setLastViewFinderWidth(int lastViewFinderWidth) {
        this.lastViewFinderWidth = lastViewFinderWidth;
    }

    public int getLastViewFinderHeight() {
        return lastViewFinderHeight;
    }

    public void setLastViewFinderHeight(int lastViewFinderHeight) {
        this.lastViewFinderHeight = lastViewFinderHeight;
    }

    public PlateLookupResult getActiveLookupResult() {
        return activeLookupResult;
    }

    public void setActiveLookupResult(PlateLookupResult activeLookupResult) {
        this.activeLookupResult = activeLookupResult;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isVoiceMode() {
        return voiceMode;
    }

    public void setVoiceMode(boolean voiceMode) {
        this.voiceMode = voiceMode;
    }

    public String getLastPlateNumber() {
        return lastPlateNumber;
    }

    public void setLastPlateNumber(String lastPlateNumber) {
        this.lastPlateNumber = lastPlateNumber;
    }

    public String getLastVinNumber() {
        return lastVinNumber;
    }

    public void setLastVinNumber(String lastVinNumber) {
        this.lastVinNumber = lastVinNumber;
    }

    public ArrayList<TicketViolation> getLastViolations() {
        return lastViolations;
    }

    public void setLastViolations(ArrayList<TicketViolation> lastViolations) {
        this.lastViolations = lastViolations;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getCurrentUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public Date getLastNetworkTime() {
        return lastNetworkTime;
    }

    public void setLastNetworkTime(Date lastNetworkTime) {
        this.lastNetworkTime = lastNetworkTime;
    }

    public boolean isSyncRequired() {
        if (this.lastNetworkTime == null) {
            return false;
        }

        long diff = new Date().getTime() - this.lastNetworkTime.getTime();
        long diffMinutes = diff / (60 * 1000);

        return diffMinutes >= 15;
    }

    public void initUserPrefs(UserSetting userSettings, SharedPreferences mPreferences) {
        TPApplication TPApp = (TPApplication) getApplicationContext();
        try {
            String userPrefs = userSettings.getUserPrefs();
            if (userPrefs == null || userPrefs.trim().isEmpty()) {
                initDefaultUserPrefs(userSettings);
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(userPrefs);
                userSettings.setLocationKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_LOCATION_KEYBOARD));

                userSettings.setCommentsKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_COMMENT_KEYBOARD));
                userSettings.setViolationKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_VIOLATION_KEYBOARD));
                userSettings.setSkipLocationEntry(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_SKIP_LOCATION_ENTRY));
                userSettings.setAutoLookup(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_AUTO_LOOKUP));
                userSettings.setSecondLocationEntry(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_SECOND_LOCATION_ENTRY));
                userSettings.setAccordionLookUp(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_ACCORDION_LOOKUP));
                userSettings.setSearchContains(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_SEARCH_CONTAINS));

                if (jsonObject.has(TPConstant.PREFS_KEY_SETTING_CACHE_EXPIRY)) {
                    userSettings.setCacheExpiry(jsonObject.getInt(TPConstant.PREFS_KEY_SETTING_CACHE_EXPIRY));
                }

                userSettings.setMakeKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_MAKE_KEYBOARD));
                userSettings.setBodyKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_BODY_KEYBOARD));
                userSettings.setColorKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_COLOR_KEYBOARD));

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            TPApp.setUserSettings(userSettings);
        }
    }

    public void initDefaultUserPrefs(UserSetting userSettings) {
        TPApplication TPApp = (TPApplication) getApplicationContext();
        try {
            String userPrefs = TPConstant.USER_DEFAULT_PREFS_SETTINGS;
            try {
                JSONObject jsonObject = new JSONObject(userPrefs);
                userSettings.setLocationKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_LOCATION_KEYBOARD));
                userSettings.setCommentsKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_COMMENT_KEYBOARD));
                userSettings.setViolationKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_VIOLATION_KEYBOARD));
                userSettings.setSkipLocationEntry(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_SKIP_LOCATION_ENTRY));
                userSettings.setAutoLookup(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_AUTO_LOOKUP));
                userSettings.setSecondLocationEntry(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_SECOND_LOCATION_ENTRY));
                userSettings.setAccordionLookUp(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_ACCORDION_LOOKUP));
                userSettings.setSearchContains(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_SEARCH_CONTAINS));

                if (jsonObject.has(TPConstant.PREFS_KEY_SETTING_CACHE_EXPIRY)) {
                    userSettings.setCacheExpiry(jsonObject.getInt(TPConstant.PREFS_KEY_SETTING_CACHE_EXPIRY));
                }

                userSettings.setMakeKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_MAKE_KEYBOARD));
                userSettings.setBodyKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_BODY_KEYBOARD));
                userSettings.setColorKeyboard(jsonObject.getBoolean(TPConstant.PREFS_KEY_SETTING_COLOR_KEYBOARD));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            TPApp.setUserSettings(userSettings);
        }
    }

    /**
     * @return Current screen density Screen density needs to optimize for high
     * res. devices for better appearance
     * @Require - Current class context to invoke resource and get display
     * metrics
     */
    public String getDensityName(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            return "xxxhdpi";
        }
        if (density >= 3.0) {
            return "xxhdpi";
        }
        if (density >= 2.0) {
            return "xhdpi";
        }
        if (density >= 1.5) {
            return "hdpi";
        }
        if (density >= 1.0) {
            return "mdpi";
        }
        return "ldpi";
    }

    public Activity getCurrentAcivity() {
        return currentAcivity;
    }

    public void setCurrentAcivity(Activity currentAcivity) {
        this.currentAcivity = currentAcivity;
    }

    public int getNetworkLevel() {
        return networkLevel;
    }

    public void setNetworkLevel(int networkLevel) {
        this.networkLevel = networkLevel;
    }

    public String getLastGPSLocation() {
        return lastGPSLocation;
    }

    public void setLastGPSLocation(String lastGPSLocation) {
        this.lastGPSLocation = lastGPSLocation;
    }

    public ArrayList<Intent> getNotificationIntents() {
        return notificationIntents;
    }

    public void setNotificationIntents(ArrayList<Intent> notificationIntents) {
        this.notificationIntents = notificationIntents;
    }

    public String getExternalStoragePath() {
        return externalStoragePath;
    }

    public void setExternalStoragePath(String externalStoragePath) {
        this.externalStoragePath = externalStoragePath;
    }

    public boolean isStickyViolations() {
        return stickyViolations;
    }

    public void setStickyViolations(boolean stickyViolations) {
        this.stickyViolations = stickyViolations;
    }

    public boolean isStickyPhoto() {
        return stickyPhoto;
    }

    public void setStickyPhoto(boolean stickyPhoto) {
        this.stickyPhoto = stickyPhoto;
    }

    public boolean isShowPrintDialog() {
        return showPrintDialog;
    }

    public void setShowPrintDialog(boolean showPrintDialog) {
        this.showPrintDialog = showPrintDialog;
    }

    public boolean isLPRFlashLED() {
        return LPRFlashLED;
    }

    public void setLPRFlashLED(boolean lPRFlashLED) {
        LPRFlashLED = lPRFlashLED;
    }

    public boolean isPictureFlashLED() {
        return pictureFlashLED;
    }

    public void setPictureFlashLED(boolean pictureFlashLED) {
        this.pictureFlashLED = pictureFlashLED;
    }

    public boolean isBarcodeFlashLED() {
        return barcodeFlashLED;
    }

    public void setBarcodeFlashLED(boolean barcodeFlashLED) {
        this.barcodeFlashLED = barcodeFlashLED;
    }

    public boolean isResumeFromNotification() {
        return resumeFromNotification;
    }

    public void setResumeFromNotification(boolean resumeFromNotification) {
        this.resumeFromNotification = resumeFromNotification;
    }

    public boolean isUseExtStorage() {
        return useExtStorage;
    }

    public void setUseExtStorage(boolean useExtStorage) {
        this.useExtStorage = useExtStorage;
    }

    public boolean isCheckNetworkSignal() {
        return checkNetworkSignal;
    }

    public void setCheckNetworkSignal(boolean checkNetworkSignal) {
        this.checkNetworkSignal = checkNetworkSignal;
    }

    public boolean isEnableChalkAlerts() {
        return enableChalkAlerts;
    }

    public void setEnableChalkAlerts(boolean enableChalkAlerts) {
        this.enableChalkAlerts = enableChalkAlerts;
    }

    public boolean isEnableMobileNow() {
        return enableMobileNow;
    }

    public void setEnableMobileNow(boolean enableMobileNow) {
        this.enableMobileNow = enableMobileNow;
    }

    public boolean isEnablePassportParking() {
        return enablePassportParking;
    }

    public void setEnablePassportParking(boolean enablePassportParking) {
        this.enablePassportParking = enablePassportParking;
    }

    public boolean isEnableVinPassportParking() {
        return enableVinPassportParking;
    }

    public void setEnableVinPassportParking(boolean enableVinPassportParking) {
        this.enableVinPassportParking = enableVinPassportParking;
    }

    public boolean isEnableParkMobile() {
        return enableParkMobile;
    }

    public void setEnableParkMobile(boolean enableParkMobile) {
        this.enableParkMobile = enableParkMobile;
    }

    public boolean isEnablePayByPhone() {
        return enablePayByPhone;
    }

    public void setEnablePayByPhone(boolean enablePayByPhone) {
        this.enablePayByPhone = enablePayByPhone;
    }

    public boolean isEnableDPT() {
        return enableDPT;
    }

    public void setEnableDPT(boolean enableDPT) {
        this.enableDPT = enableDPT;
    }

    public boolean isEnableIPS() {
        return enableIPS;
    }

    public void setEnableIPS(boolean enableIPS) {
        this.enableIPS = enableIPS;
    }

    public boolean isEnableCale() {
        return enableCale;
    }

    public void setEnableCale(boolean enableCale) {
        this.enableCale = enableCale;
    }

    public boolean isEnableParkeon() {
        return enableParkeon;
    }

    public void setEnableParkeon(boolean enableParkeon) {
        this.enableParkeon = enableParkeon;
    }

    public boolean isEnableSamtrans() {
        return enableSamtrans;
    }

    public void setEnableSamtrans(boolean enableSamtrans) {
        this.enableSamtrans = enableSamtrans;
    }

    public boolean isEnableProgressive() {
        return enableProgressive;
    }

    public void setEnableProgressive(boolean enableProgressive) {
        this.enableProgressive = enableProgressive;
    }

    public boolean isDisableSync() {
        return disableSync;
    }

    public void setDisableSync(boolean disableSync) {
        this.disableSync = disableSync;
    }

    public int getTransactionTimeout() {
        return transactionTimeout;
    }

    public void setTransactionTimeout(int transactionTimeout) {
        this.transactionTimeout = transactionTimeout;
    }

    public boolean isServiceAvailable() {
        return isServiceAvailable;
    }

    public void setServiceAvailable(boolean isServiceAvailable) {
        this.isServiceAvailable = isServiceAvailable;
    }

    public Point getStickyMarkerS() {
        return stickyMarkerS;
    }

    public void setStickyMarkerS(Point stickyMarkerS) {
        this.stickyMarkerS = stickyMarkerS;
    }

    public Point getStickyMarkerC() {
        return stickyMarkerC;
    }

    public void setStickyMarkerC(Point stickyMarkerC) {
        this.stickyMarkerC = stickyMarkerC;
    }

    public ArrayList<ChalkVehicle> getChalkList() {
        return chalkList;
    }

    public void setChalkList(ArrayList<ChalkVehicle> chalkList) {
        this.chalkList = chalkList;
    }

    public String getIPSSpaceGroup() {
        return IPSSpaceGroup;
    }

    public void setIPSSpaceGroup(String iPSSpaceGroup) {
        IPSSpaceGroup = iPSSpaceGroup;
    }

    public boolean isHasKeyboardPopup() {
        return hasKeyboardPopup;
    }

    public void setHasKeyboardPopup(boolean hasKeyboardPopup) {
        this.hasKeyboardPopup = hasKeyboardPopup;
    }

    public CachedMap getCachedMap() {
        return cachedMap;
    }

    public void setCachedMap(CachedMap cachedMap) {
        this.cachedMap = cachedMap;
    }

    public ArrayList<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(ArrayList<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public String getACCOUNT_NAME() {
        return ACCOUNT_NAME;
    }

    public String getACCOUNT_TYPE() {
        return ACCOUNT_TYPE;
    }

    public String getPROVIDER() {
        return PROVIDER;
    }

    public PlateLookupResultPriorTickets getPlateLookupActivity() {
        return plateLookupActivity;
    }

    public void setPlateLookupActivity(PlateLookupResultPriorTickets plateLookupActivity) {
        this.plateLookupActivity = plateLookupActivity;
    }

    public PlateLookupResultAdvance getPlateLookupResultActivity() {
        return plateLookupResultActivity;
    }

    public void setPlateLookupResultActivity(PlateLookupResultAdvance plateLookupResultActivity) {
        this.plateLookupResultActivity = plateLookupResultActivity;
    }

    public Location getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(Location gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public boolean isStickyLocation() {
        return stickyLocation;
    }

    public void setStickyLocation(boolean stickyLocation) {
        this.stickyLocation = stickyLocation;
    }

    public static enum Module {
        PARKING, TRAFFIC
    }

    public static enum TicketSource {
        DEFAULT, MAIN_MENU, CHALK, CHALK_NOTIFICATION, LPR_NOTIFICATION
    }

}
