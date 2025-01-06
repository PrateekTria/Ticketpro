package com.ticketpro.model;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.exception.TPException;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.activity.TPApplication.TicketSource;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "tickets")
public class Ticket implements Serializable {

    @Ignore
    private static final Logger log = Logger.getLogger("Ticket");
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ticket_id")
    @SerializedName("ticket_id")
    @Expose
    private long ticketId;
    @ColumnInfo(name = "userid")
    @SerializedName("userid")
    @Expose
    private int userId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "device_id")
    @SerializedName("device_id")
    @Expose
    private int deviceId;
    @ColumnInfo(name = "citation_number")
    @SerializedName("citation_number")
    @Expose
    private long citationNumber;
    @ColumnInfo(name = "ticket_date")
    @SerializedName("ticket_date")
    @Expose
    private Date ticketDate;
    @ColumnInfo(name = "state_id")
    @SerializedName("state_id")
    @Expose
    private int stateId;
    @ColumnInfo(name = "state_code")
    @SerializedName("state_code")
    @Expose
    private String stateCode;
    @ColumnInfo(name = "plate")
    @SerializedName("plate")
    @Expose
    private String plate;
    @ColumnInfo(name = "vin")
    @SerializedName("vin")
    @Expose
    private String vin;
    @ColumnInfo(name = "expiration")
    @SerializedName("expiration")
    @Expose
    private String expiration;
    @ColumnInfo(name = "make_id")
    @SerializedName("make_id")
    @Expose
    private int makeId;
    @ColumnInfo(name = "make_code")
    @SerializedName("make_code")
    @Expose
    private String makeCode;
    @ColumnInfo(name = "body_id")
    @SerializedName("body_id")
    @Expose
    private int bodyId;
    @ColumnInfo(name = "body_code")
    @SerializedName("body_code")
    @Expose
    private String bodyCode;
    @ColumnInfo(name = "color_id")
    @SerializedName("color_id")
    @Expose
    private int colorId;
    @ColumnInfo(name = "color_code")
    @SerializedName("color_code")
    @Expose
    private String colorCode;
    @ColumnInfo(name = "street_prefix")
    @SerializedName("street_prefix")
    @Expose
    private String streetPrefix;
    @ColumnInfo(name = "street_suffix")
    @SerializedName("street_suffix")
    @Expose
    private String streetSuffix;
    @ColumnInfo(name = "street_number")
    @SerializedName("street_number")
    @Expose
    private String streetNumber;
    @ColumnInfo(name = "location")
    @SerializedName("location")
    @Expose
    private String location;
    @ColumnInfo(name = "direction")
    @SerializedName("direction")
    @Expose
    private String direction;
    @ColumnInfo(name = "latitude")
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @ColumnInfo(name = "gpstime")
    @SerializedName("gpstime")
    @Expose
    private Date gpstime;
    @ColumnInfo(name = "is_gps_location")
    @SerializedName("is_gps_location")
    @Expose
    private String isGPSLocation;
    @ColumnInfo(name = "is_void")
    @SerializedName("is_void")
    @Expose
    private String isVoid;
    @ColumnInfo(name = "is_chalked")
    @SerializedName("is_chalked")
    @Expose
    private String isChalked;
    @ColumnInfo(name = "is_warn")
    @SerializedName("is_warn")
    @Expose
    private String isWarn;
    @ColumnInfo(name = "is_driveaway")
    @SerializedName("is_driveaway")
    @Expose
    private String isDriveAway;
    @ColumnInfo(name = "void_id")
    @SerializedName("void_id")
    @Expose
    private int voidId;
    @ColumnInfo(name = "chalk_id")
    @SerializedName("chalk_id")
    @Expose
    private long chalkId;
    @ColumnInfo(name = "permit")
    @SerializedName("permit")
    @Expose
    private String permit;
    @ColumnInfo(name = "meter")
    @SerializedName("meter")
    @Expose
    private String meter;
    @ColumnInfo(name = "fine")
    @SerializedName("fine")
    @Expose
    private double fine;
    @ColumnInfo(name = "time_marked")
    @SerializedName("time_marked")
    @Expose
    private Date timeMarked;
    @ColumnInfo(name = "space")
    @SerializedName("space")
    @Expose
    private String space;
    @ColumnInfo(name = "violation_code")
    @SerializedName("violation_code")
    @Expose
    private String violationCode;
    @ColumnInfo(name = "violation")
    @SerializedName("violation")
    @Expose
    private String violation;
    @ColumnInfo(name = "void_reason_code")
    private String voidReasonCode;
    @ColumnInfo(name = "duty_id")
    @SerializedName("duty_id")
    @Expose
    private int dutyId;
    @ColumnInfo(name = "is_lpr")
    @SerializedName("is_lpr")
    @Expose
    private String isLPR;
    @ColumnInfo(name = "violation_id")
    @SerializedName("violation_id")
    @Expose
    private int violationId;
    @ColumnInfo(name = "photo_count")
    @SerializedName("photo_count")
    @Expose
    private int photo_count;
    @ColumnInfo(name = "lpr_notification_id")
    private String lprNotificationId;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "placard")
    private String placard;
    @SerializedName("duty_report_id")
    @Expose
    @ColumnInfo(name = "duty_report_id")
    private String dutyReportId;

    @SerializedName("app_version")
    @Expose
    @ColumnInfo(name = "app_version")
    private String appVersion;

    @SerializedName("chalk_time")
    @Expose
    @ColumnInfo(name = "chalk_time")
    private String chalkTime;

    @SerializedName("chalk_last_seen")
    @Expose
    @ColumnInfo(name = "chalk_last_seen")
    private String chalkLastSeen;

    @ColumnInfo(name = "ticket_date_new")
    @SerializedName("ticket_date_new")
    @Expose
    private String ticketDateNew;

    @Ignore
    private String dutyName;
    @Ignore
    private String timeZone;
    @Ignore
    private String elapsed;
    @Ignore
    private String colorTitle;
    @Ignore
    private String bodyTitle;
    @Ignore
    private String makeTitle;
    @Ignore
    private int chalkDuration;
    @Ignore
    private String chalkZone;
    @Ignore
    private TicketSource ticketSource;
    @Ignore
    @SerializedName("ticketPictures")
    @Expose
    private ArrayList<TicketPicture> ticketPictures = new ArrayList<>();
    @Ignore
    @SerializedName("ticketViolations")
    @Expose
    private ArrayList<TicketViolation> ticketViolations = new ArrayList<>();
    @Ignore
    @SerializedName("ticketComments")
    @Expose
    private ArrayList<TicketComment> ticketComments = new ArrayList<>();
    @Ignore
    private TicketComment ticketComment;
    @Ignore
    private TicketViolation ticketViolation;
    @Ignore
    private TicketPicture ticketPicture;

    public Ticket() {

    }

    public Ticket(JSONObject object) throws Exception {
        this.setTicketId(!object.isNull("ticket_id") ? object.getLong("ticket_id") : 0);
        this.setDeviceId(!object.isNull("device_id") ? object.getInt("device_id") : 0);
        this.setUserId(!object.isNull("userid") ? object.getInt("userid") : 0);
        this.setCustId(!object.isNull("custid") ? object.getInt("custid") : 0);
        this.setCitationNumber(object.getLong("citation_number"));
        this.setTicketDate(DateUtil.getDateFromSQLString(object.getString("ticket_date")));
        this.setStateId(!object.isNull("state_id") ? object.getInt("state_id") : 0);
        this.setPlate(object.getString("plate"));
        this.setVin(object.getString("vin"));
        this.setExpiration(object.getString("expiration"));
        this.setMakeId(!object.isNull("make_id") ? object.getInt("make_id") : 0);
        this.setBodyId(!object.isNull("body_id") ? object.getInt("body_id") : 0);
        this.setColorId(!object.isNull("color_id") ? object.getInt("color_id") : 0);
        this.setStreetPrefix(object.getString("street_prefix"));
        this.setStreetSuffix(object.getString("street_suffix"));
        this.setLocation(object.getString("location"));
        this.setLatitude(object.getString("latitude"));
        this.setLongitude(object.getString("longitude"));
        this.setGpstime(DateUtil.getDateFromSQLString(object.getString("gpstime")));
        this.setIsGPSLocation(object.getString("is_gps_location"));
        this.setIsVoid(object.getString("is_void"));
        this.setIsChalked(object.getString("is_chalked"));
        this.setIsWarn(object.getString("is_warn"));
        this.setIsDriveAway(object.getString("is_driveaway"));
        this.setVoidId(!object.isNull("void_id") ? object.getInt("void_id") : 0);
        this.setChalkId(!object.isNull("chalk_id") ? object.getLong("chalk_id") : 0);
        this.setMeter(object.getString("meter"));
        this.setPermit(object.getString("permit"));
        this.setBodyCode(object.getString("body_code"));
        this.setStateCode(object.getString("state_code"));
        this.setColorCode(object.getString("color_code"));
        this.setMakeCode(object.getString("make_code"));
        this.setStreetNumber(object.getString("street_number"));
        this.setDirection(object.getString("direction"));
        this.setFine(!object.isNull("fine") ? object.getDouble("fine") : 0);
        this.setTimeMarked(DateUtil.getDateFromSQLString(object.getString("time_marked")));
        this.setViolationCode(object.getString("violation_code"));
        this.setViolation(object.getString("violation"));
        this.setVoidReasonCode(object.getString("void_reason_code"));
        this.setDutyId(!object.isNull("duty_id") ? object.getInt("duty_id") : 0);
        this.setIsLPR(object.getString("is_lpr"));
        this.setViolationId(!object.isNull("violation_id") ? object.getInt("violation_id") : 0);
        this.setSpace(object.getString("space"));
        this.setPhotoCount(!object.isNull("photo_count") ? object.getInt("photo_count") : 0);
        try {
            this.setLprNotificationId(object.getString("lpr_notification_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean chalkSyncOrNot(long chalkId) throws Exception {
        Ticket ticket = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao()
                .chalkSyncOrNot(chalkId);

        if (ticket!=null && ticket.status.equals("S"))
            return true;

        return false;
    }


    public static boolean chalkTicketIssue(long chalkId) throws Exception {
        Ticket ticket = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().chalkSyncOrNot(chalkId);
        if (ticket==null)
            return false;
        return true;
    }


    public static boolean checkOlderTickets(int userId){
        Ticket ticket = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().checkPindingTickets(userId);
        return ticket != null;
    }
    public static void removeOlderTickets(int userId){
        ArrayList<Ticket> tickets = (ArrayList<Ticket>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().removeAllPendingTicket(userId);
        log.debug("Tickets to delete :" + tickets.size());
        /*if (tickets.size() > 0) {
            ParkingDatabase.backupDatabase(TPApplication.getInstance());
        }*/
        for (Ticket ticket : tickets) {
            if(!DateUtil.getDateStringFromDate(ticket.getTicketDate()).equals(DateUtil.getDateStringFromDate(new Date()))) {
                long ticketId = ticket.getTicketId();
                log.debug("deleting ticket by user_id: " + ticket.getCitationNumber());
                ParkingDatabase.getInstance(TPApplication.getInstance()).ticketViolationsDao().removeById(ticketId);
                ParkingDatabase.getInstance(TPApplication.getInstance()).ticketCommentsDao().removeById(ticketId);
                ParkingDatabase.getInstance(TPApplication.getInstance()).ticketPicturesDao().removePictureByTicketId(ticketId);
                ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().removeById(ticketId);
                SyncData.removeSyncData(TPConstant.TABLE_TICKETS, String.valueOf(ticketId));
                SyncData.removeSyncData(TPConstant.TABLE_TICKET_COMMENTS, String.valueOf(ticketId));
                SyncData.removeSyncData(TPConstant.TABLE_TICKET_PICTURES, String.valueOf(ticketId));
                SyncData.removeSyncData(TPConstant.TABLE_TICKET_VIOLATIONS, String.valueOf(ticketId));
            }
        }
    }
    public static boolean checkDuplicateRecordsPlates(String plate, int vid, Date time,String location) throws Exception {
        Ticket ticket = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().checkDuplicateRecordsPlate(plate, vid,location);
        if (ticket!=null){
            if (time.equals(ticket.getTicketDate())){
                return true;
            }else if (ticket.oneMinuteDifference(time,ticket.getTicketDate())){
                return true;
            }
        }
        return false;
    }

    public static boolean isTicketPending(long citationNumber) {

        Ticket ticket = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().checkDuplicate(String.valueOf(citationNumber));
        if (ticket.getStatus().equals("P")){
            return true;
        }
        return false;
    }

    boolean twoDateIsEqualOrNot(Date d1, Date d2) {

        String dateTimeString1 = DateUtil.getSQLStringFromDate2(d1);
        String dateTimeString2 = DateUtil.getStringFromDate2(d2);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = sdf.parse(dateTimeString1);
            Date date2 = sdf.parse(dateTimeString2);

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date1);

            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date2);

            boolean isEqual = (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR))
                    && (calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH))
                    && (calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH))
                    && (calendar1.get(Calendar.HOUR_OF_DAY) == calendar2.get(Calendar.HOUR_OF_DAY))
                    && (calendar1.get(Calendar.MINUTE) == calendar2.get(Calendar.MINUTE))
                    && (calendar1.get(Calendar.SECOND) == calendar2.get(Calendar.SECOND));

            if (isEqual) {
                // The two date and time values are equal
                return true;
            } else {
                // The two date and time values are not equal
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    return false;
    }

    boolean oneMinuteDifference(Date d1, Date d2){
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(d1);
        Calendar oneMinuteBeforeEndTime = Calendar.getInstance();
        oneMinuteBeforeEndTime.setTime(d2);
        oneMinuteBeforeEndTime.add(Calendar.MINUTE, -1);

        if (currentTime.after(oneMinuteBeforeEndTime)) {
            // Current time is greater than one minute before the end time
            return false;
        } else {
            // Current time is not greater than one minute before the end time
            return true;
        }
    }

    //DriveWay

    public static void ticketDriveWay(String citation, String values) {
        try {
            ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().updateTicketDriveway(citation, values);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //DriveWay

    public static void ticketWarn(String citation, String values) {
        try {
            ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().updateTicketWarning(citation, values);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //DriveWay

    public static void ticketVoid(String citation, String values) {
        try {
            ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().updateTicketVoid(citation, values);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get Photo count
    public static int getPhotoCount(String citation){
       return ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().getPhotoCount(citation);
    }

    //Update photo count
    public static void ticketUpdatePhotoCount(String citation, int values) {
        try {
            ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().updateTicketPhotoCount(citation, values);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkDuplicateRecords(String plate, String vin, String location, Date time) throws Exception {
        Ticket ticket = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao()
                .checkDuplicateRecords(plate, vin, location, time);
        return ticket != null;
    }

    public static void removeAllOlderTicketsByHour(String hours, Logger log) throws Exception {
        ArrayList<Ticket> tickets = (ArrayList<Ticket>) ParkingDatabase
                .getInstance(TPApplication.getInstance()).ticketsDao()
                .removeAllOlderTicketsByHours(hours);
        log.debug("Tickets to delete in 5 hours :" + tickets.size());
        /*if (tickets.size() > 0) {
            ParkingDatabase.backupDatabase(TPApplication.getInstance());
        }*/
        for (Ticket ticket : tickets) {

            long ticketId = ticket.getTicketId();
            log.debug("deleting ticket: " + ticket.getCitationNumber());
            ParkingDatabase.getInstance(TPApplication.getInstance()).ticketViolationsDao().removeById(ticketId);
            ParkingDatabase.getInstance(TPApplication.getInstance()).ticketCommentsDao().removeById(ticketId);
            ParkingDatabase.getInstance(TPApplication.getInstance()).ticketPicturesDao().removePictureByTicketId(ticketId);
            ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().removeById(ticketId);
            SyncData.removeSyncData(TPConstant.TABLE_TICKETS, String.valueOf(ticketId));
            SyncData.removeSyncData(TPConstant.TABLE_TICKET_COMMENTS, String.valueOf(ticketId));
            SyncData.removeSyncData(TPConstant.TABLE_TICKET_PICTURES, String.valueOf(ticketId));
            SyncData.removeSyncData(TPConstant.TABLE_TICKET_VIOLATIONS, String.valueOf(ticketId));
        }
    }
    public static void removeAllOlderTicketsAtMidnight(Logger log, boolean b) throws Exception {
        ArrayList<Ticket> tickets = (ArrayList<Ticket>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().removeAllOlderTicketsAtMidnight();
        //log.debug("Tickets to delete at midnight :" + tickets.size());
        /*if (tickets.size() > 0) {
            ParkingDatabase.backupDatabase(TPApplication.getInstance());
        }*/
       SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        boolean diff = false;
        if (tickets.size()>0) {
            for (Ticket ticket : tickets) {
                // This code is added by mohit 25/01/2023
                if (!DateUtil.getDateStringFromDate(ticket.getTicketDate()).equals(DateUtil.getDateStringFromDate(new Date()))) {
                    long ticketId = ticket.getTicketId();
                    //log.debug("deleting ticket: " + ticket.getCitationNumber());
                    ParkingDatabase.getInstance(TPApplication.getInstance()).ticketViolationsDao().removeById(ticketId);
                    ParkingDatabase.getInstance(TPApplication.getInstance()).ticketCommentsDao().removeById(ticketId);
                    ParkingDatabase.getInstance(TPApplication.getInstance()).ticketPicturesDao().removePictureByTicketId(ticketId);
                    ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().removeById(ticketId);
                    SyncData.removeSyncData(TPConstant.TABLE_TICKETS, String.valueOf(ticketId));
                    SyncData.removeSyncData(TPConstant.TABLE_TICKET_COMMENTS, String.valueOf(ticketId));
                    SyncData.removeSyncData(TPConstant.TABLE_TICKET_PICTURES, String.valueOf(ticketId));
                    SyncData.removeSyncData(TPConstant.TABLE_TICKET_VIOLATIONS, String.valueOf(ticketId));
                }

            }
        }
    }

    public static void removeTicketById(long ticketId) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().removeById(ticketId);
        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketCommentsDao().removeById(ticketId);
        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketPicturesDao().removePictureByTicketId(ticketId);
        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketViolationsDao().removeById(ticketId);
        SyncData.removeSyncData(TPConstant.TABLE_TICKETS, String.valueOf(ticketId));
        SyncData.removeSyncData(TPConstant.TABLE_TICKET_COMMENTS, String.valueOf(ticketId));
        SyncData.removeSyncData(TPConstant.TABLE_TICKET_PICTURES, String.valueOf(ticketId));
        SyncData.removeSyncData(TPConstant.TABLE_TICKET_VIOLATIONS, String.valueOf(ticketId));
    }

    public static long getNextPrimaryId() {
        long nextId = 0;
        nextId = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().getNextPrimaryId();
        return nextId + 1;
    }

    public static String getTicketCountFromTicketTable(int userId, Date statDate, Date endDate) {
        String count = "";
        count = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().getTicketCount(userId, statDate, endDate);
        return count;
    }

    public static void updateTicket(String citation, String values) {
        try {
            ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().updateTicket(citation, values);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isDuplicateCitation(long citationNumber) {
        long l = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().getTicketIdFromCitationNumber(citationNumber);
        boolean result = false;
        if (l != 0) {
            result = true;
        }

        return result;
    }

    public static Ticket getLastTicket(int userId) {
        try {
            Ticket object = null;
            object = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().getLastTicket(userId);
            try {
                object.setTicketPictures(TicketPicture.getTicketPictures(object.getTicketId(), object.getCitationNumber()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                object.setTicketViolations(TicketViolation.getTicketViolationByCitationWithComments(TPApplication.getInstance(), object.getCitationNumber()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;

        } catch (Exception e) {
            Log.e("Ticket", "Error " + e.getMessage());
            return null;
        }
    }

    public static Ticket getLastTicketForCheckDuplicateRecord(int userId) {
        try {
            Ticket object = null;
            object = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().getLastTicket(userId);
            if (object == null)
                return null;
            try {
                object.setTicketPictures(TicketPicture.getTicketPictures(object.getTicketId(), object.getCitationNumber()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                object.setTicketViolations(TicketViolation.getTicketViolationByCitationWithComments(TPApplication.getInstance(), object.getCitationNumber()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;

        } catch (Exception e) {
            Log.e("Ticket", "Error " + e.getMessage());
            return null;
        }
    }


    public static ArrayList<Ticket> getTickets(int userId, String userType) throws Exception {
        ArrayList<Ticket> list;
        if (userType.matches("Admin")) {
            list = (ArrayList<Ticket>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().getAllTickets();
        } else {
            list = (ArrayList<Ticket>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().getAllTicketsByUSerID(userId);
        }
        return list;
    }

    public static ArrayList<Ticket> getTicketsBySpace(String spaceNumber) throws Exception {
        return (ArrayList<Ticket>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().getTicketsBySpace(spaceNumber);
    }

    public static Ticket getTicketsByPrimaryId(String ticketId) throws TPException {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().getTicketsByPrimaryId(ticketId);
    }

    public static Ticket getTicketByCitationWithViolations(long citationNumber) throws TPException {
        Ticket object;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().getTicketByCitationWithViolations(citationNumber);
        try {
            object.setTicketViolations(TicketViolation.getTicketViolationByCitationWithComments(TPApplication.getInstance(), citationNumber));
            object.setTicketPictures(TicketPicture.getTicketPicturesByCitation(citationNumber));
            object.setTicketComments(TicketComment.getTicketCommentsByCitation(TPApplication.getInstance(), citationNumber));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public static boolean isDuplicateTicket(long citationNumber) throws TPException {
        Ticket ticket = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().getTicketByCitationWithViolations(citationNumber);
        // Check if ticket is already available
        return ticket == null;
    }

    public static ArrayList<Ticket> searchAllPreviousTicketByPlate(String state, String plate) throws Exception {
        return (ArrayList<Ticket>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().searchAllPreviousTicketByPlate(state, plate);
    }

    public static Ticket searchPreviousTicketByPlate(String state, String plate) throws TPException {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().searchPreviousTicketByPlate(state, plate);
    }

    public static Ticket searchPreviousTicketByVIN(String state, String vin) throws TPException {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().searchPreviousTicketByVIN(state, vin);
    }

    public static Ticket searchPreviousTicketByMeter(String meter) throws TPException {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().searchPreviousTicketByMeter(meter);
    }

    public static ArrayList<String> getPendingCitations() throws TPException {
        ArrayList<Long> pendingCitations = (ArrayList<Long>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().getPendingCitations();
        ArrayList<String> strings = new ArrayList<>();
        for (Long aLong : pendingCitations) {
            strings.add(String.valueOf(aLong));
        }
        return strings;
    }

    public static long getTicketIdByCitationNumber(long citationNumber){
        return ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDao().getTicketIdFromCitationNumber(citationNumber);
    }

    public static void updateTicket(Ticket ticket){
        final ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());
        instance.ticketsDao().updateTicket(ticket).subscribeOn(Schedulers.io()).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(@NonNull Throwable e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        });
    }

    @SuppressLint("CheckResult")
    public static void insertTicket(@NotNull final Ticket ticket,String audioFile) {
        Ticket activeTicket;
        activeTicket = ticket;
        activeTicket.setStatus(StringUtil.isEmpty(activeTicket.status) ? "P" : activeTicket.status);
        final ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());
        long time = System.currentTimeMillis();
        activeTicket.setTicketId(0);
        Log.i("Ticket Start", "insertTicket: " + time);
        instance.ticketsDao().insertTicket(activeTicket).subscribeOn(Schedulers.io()).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.i("Ticket End", "onComplete: " + (System.currentTimeMillis() - time));
                long ticketId = instance.ticketsDao().getTicketIdFromCitationNumber(activeTicket.getCitationNumber());
                activeTicket.setTicketId(ticketId);
                saveTicketData(activeTicket, audioFile);

            }

            @Override
            public void onError(@NonNull Throwable e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        });
    }

    private static void saveTicketData(Ticket activeTicket, String audioFile) {
        if (audioFile != null && !audioFile.isEmpty()) {
            TicketComment comment = new TicketComment();
            comment.setAudioFile(audioFile);
            comment.setVoiceComment(true);
            comment.setComment(audioFile);
            comment.setTicketId(activeTicket.getTicketId());
            comment.setCitationNumber(activeTicket.getCitationNumber());
            comment.setCustId(activeTicket.getCustId());
            TicketComment.insertTicketComment(comment);
        }


        if (activeTicket.getTicketPictures() != null && activeTicket.getTicketPictures().size() > 0) {
            for (TicketPicture picture : activeTicket.getTicketPictures()) {
                picture.setTicketId(activeTicket.getTicketId());
                picture.setCitationNumber(activeTicket.getCitationNumber());
                picture.setCustId(activeTicket.getCustId());
                try {
                    if (picture.isPhotoSp()) {
                        String imagePath = picture.getImagePath();
                        if (imagePath != null) {
                            String filename = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                            String[] split = filename.split("-");
                            String s = split[0];
                            String cit = Long.toString(activeTicket.getCitationNumber());
                            if (!s.equals(cit)) {
                                String filename1 = activeTicket.getCitationNumber() + "-" + split[1] + "-" + split[2];
                                InputStream in = null;
                                OutputStream out = null;
                                try {

                                    //create output directory if it doesn't exist
                                    File sourceLocation = new File(picture.getImagePath());
                                    File targetLocation = new File(TPUtility.getTicketsFolder(), filename1);
                                    Uri picUri = Uri.fromFile(targetLocation);
                                    picture.setImagePath(picUri.getPath());
                                    in = new FileInputStream(sourceLocation);
                                    out = new FileOutputStream(targetLocation);

                                    byte[] buffer = new byte[1024];
                                    int read;
                                    while ((read = in.read(buffer)) != -1) {
                                        out.write(buffer, 0, read);
                                    }
                                    in.close();
                                    in = null;

                                    // write the output file (You have now copied the file)
                                    out.flush();
                                    out.close();
                                    out = null;

                                } catch (FileNotFoundException fnfe1) {
                                    Log.e("tag", fnfe1.getMessage());
                                } catch (Exception e) {
                                    Log.e("tag", e.getMessage());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                TicketPicture.insertTicketPicture(picture);
            }
        }

        for (TicketViolation violation : activeTicket.getTicketViolations()) {
            if (violation.getViolationCode().equals(activeTicket.getViolationCode())) {
            if (violation.getTicketComments() != null && violation.getTicketComments().size() > 0) {
                for (TicketComment comment : violation.getTicketComments()) {
                    //int ticketCommentId = TicketComment.getNextPrimaryId();
                    comment.setTicketId(activeTicket.getTicketId());
                    comment.setCitationNumber(activeTicket.getCitationNumber());
                    comment.setCustId(activeTicket.getCustId());
                    TicketComment.insertTicketComment(comment);
                }
            }

                violation.setCitationNumber(activeTicket.getCitationNumber());
                violation.setTicketId(activeTicket.getTicketId());
                violation.setCustId(activeTicket.getCustId());
                TicketViolation.insertTicketViolation(violation);
            }
        }
    }

    public static ArrayList<Ticket> getPendingTickets() {
        ArrayList<Ticket> tickets ;
        ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());
        tickets = (ArrayList<Ticket>) instance.ticketsDao().getPendingTickets();
        try {
            for(Ticket ticket: tickets){
                ticket.setTicketViolations(TicketViolation.getTicketViolationByCitationWithComments(TPApplication.getInstance(), ticket.getCitationNumber()));
                ticket.setTicketPictures(TicketPicture.getTicketPicturesByCitation(ticket.getCitationNumber()));
                ticket.setTicketComments(TicketComment.getTicketCommentsByCitation(TPApplication.getInstance(), ticket.getCitationNumber()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tickets;
    }

    /**
     *
     * @return
     */
    public static ArrayList<Ticket> getPendingTicketsPI() {
        ArrayList<Ticket> tickets ;
        ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());
        tickets = (ArrayList<Ticket>) instance.ticketsDao().getPendingTicketsPI();
        try {
            for(Ticket ticket: tickets){
                //ticket.setTicketViolations(TicketViolation.getTicketViolationByCitationWithComments(TPApplication.getInstance(), ticket.getCitationNumber()));
                ticket.setTicketPictures(TicketPicture.getTicketPicturesByCitation(ticket.getCitationNumber()));
                //ticket.setTicketComments(TicketComment.getTicketCommentsByCitation(TPApplication.getInstance(), ticket.getCitationNumber()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public static ArrayList<Ticket> getPendingCurrentTickets(int[] citation) {
        ArrayList<Ticket> tickets ;
        ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());
        tickets = (ArrayList<Ticket>) instance.ticketsDao().getPendingCurrentTickets(citation);
        try {
            for(Ticket ticket: tickets){
                ticket.setTicketViolations(TicketViolation.getTicketViolationByCitationWithComments(TPApplication.getInstance(), ticket.getCitationNumber()));
                ticket.setTicketPictures(TicketPicture.getTicketPicturesByCitation(ticket.getCitationNumber()));
                ticket.setTicketComments(TicketComment.getTicketCommentsByCitation(TPApplication.getInstance(), ticket.getCitationNumber()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tickets;
    }
    public String getDutyReportId() {
        return dutyReportId;
    }

    public void setDutyReportId(String dutyReportId) {
        this.dutyReportId = dutyReportId;
    }

    public TicketComment getTicketComment() {
        return ticketComment;
    }

    public void setTicketComment(TicketComment ticketComment) {
        this.ticketComment = ticketComment;
    }

    public TicketViolation getTicketViolation() {
        return ticketViolation;
    }

    public void setTicketViolation(TicketViolation ticketViolation) {
        this.ticketViolation = ticketViolation;
    }

    public TicketPicture getTicketPicture() {
        return ticketPicture;
    }

    public void setTicketPicture(TicketPicture ticketPicture) {
        this.ticketPicture = ticketPicture;
    }

    public ArrayList<TicketComment> getTicketComments() {
        return ticketComments;
    }

    public void setTicketComments(ArrayList<TicketComment> ticketComments) {
        this.ticketComments = ticketComments;
    }

    public JSONObject getJSONObject() {
        JSONObject values = new JSONObject();
        try {
            values.put("ticket_id", this.ticketId);
            values.put("device_id", this.deviceId);
            values.put("userid", this.userId);
            values.put("custid", this.custId);
            values.put("citation_number", this.citationNumber);
            values.put("ticket_date", DateUtil.getSQLStringFromDate2(this.ticketDate));
            values.put("state_id", this.stateId);
            values.put("plate", this.plate);
            values.put("vin", this.vin);
            values.put("expiration", TPUtility.getExpiration(this.expiration));
            values.put("make_id", this.makeId);
            values.put("body_id", this.bodyId);
            values.put("color_id", this.colorId);
            values.put("street_prefix", this.streetPrefix != null ? this.streetPrefix.toUpperCase().trim() : "");
            values.put("street_suffix", this.streetSuffix != null ? this.streetSuffix.toUpperCase().trim() : "");
            values.put("location", this.location != null ? this.location.toUpperCase().trim() : "");
            values.put("street_number", this.streetNumber != null ? this.streetNumber.toUpperCase().trim() : "");
            values.put("direction", this.direction != null ? this.direction.toUpperCase() : "");
            values.put("space", this.space != null ? this.space.toUpperCase() : "");
            values.put("latitude", this.latitude);
            values.put("longitude", this.longitude);
            values.put("gpstime", DateUtil.getSQLStringFromDate2(this.gpstime));
            values.put("is_gps_location", this.isGPSLocation);
            values.put("is_void", this.isVoid);
            values.put("is_warn", this.isWarn);
            values.put("is_chalked", this.isChalked);
            values.put("is_driveaway", this.isDriveAway);
            values.put("chalk_id", this.chalkId);
            values.put("void_id", this.voidId);
            values.put("body_code", this.bodyCode);
            values.put("state_code", this.stateCode);
            values.put("color_code", this.colorCode);
            values.put("make_code", this.makeCode);
            values.put("meter", this.meter);
            values.put("permit", this.permit);
            values.put("fine", this.fine);
            values.put("time_marked", DateUtil.getSQLStringFromDate2(this.timeMarked));
            values.put("violation_code", this.violationCode);
            values.put("violation", this.violation);
            values.put("void_reason_code", this.voidReasonCode);
            values.put("duty_id", this.dutyId);
            values.put("is_lpr", this.isLPR);
            values.put("violation_id", this.violationId);
            values.put("photo_count", TPUtility.getPhotoCount(this.isLPR, TicketPicture.getTicketPictures(ticketId, citationNumber).size()));
            values.put("status", this.status);
            values.put("lpr_notification_id", this.lprNotificationId);
            values.put("placard", this.placard);
            values.put("duty_report_id", this.dutyReportId);

        } catch (Exception e) {
            Log.e("Ticket", "Error " + e.getMessage());
        }

        return values;
    }

    public String getChalkTime() {
        return chalkTime;
    }

    public void setChalkTime(String chalkTime) {
        this.chalkTime = chalkTime;
    }

    public String getChalkLastSeen() {
        return chalkLastSeen;
    }

    public void setChalkLastSeen(String chalkLastSeen) {
        this.chalkLastSeen = chalkLastSeen;
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
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

    public String getTicketDateNew() {
        return ticketDateNew;
    }

    public void setTicketDateNew(String ticketDateNew) {
        this.ticketDateNew = ticketDateNew;
    }

    public long getCitationNumber() {
        return citationNumber;
    }

    public void setCitationNumber(long citationNumber) {
        this.citationNumber = citationNumber;
    }

    public Date getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(Date ticketDate) {
        this.ticketDate = ticketDate;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public int getMakeId() {
        return makeId;
    }

    public void setMakeId(int makeId) {
        this.makeId = makeId;
    }

    public int getBodyId() {
        return bodyId;
    }

    public void setBodyId(int bodyId) {
        this.bodyId = bodyId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getStreetPrefix() {
        return streetPrefix;
    }

    public void setStreetPrefix(String streetPrefix) {
        this.streetPrefix = streetPrefix;
    }

    public String getStreetSuffix() {
        return streetSuffix;
    }

    public void setStreetSuffix(String streetSuffix) {
        this.streetSuffix = streetSuffix;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getGpstime() {
        return gpstime;
    }

    public void setGpstime(Date gpstime) {
        this.gpstime = gpstime;
    }

    public String getIsGPSLocation() {
        return isGPSLocation;
    }

    public void setIsGPSLocation(String isGPSLocation) {
        this.isGPSLocation = isGPSLocation;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getMakeCode() {
        return makeCode;
    }

    public void setMakeCode(String makeCode) {
        this.makeCode = makeCode;
    }

    public String getBodyCode() {
        return bodyCode;
    }

    public void setBodyCode(String bodyCode) {
        this.bodyCode = bodyCode;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public ArrayList<TicketPicture> getTicketPictures() {
        return ticketPictures;
    }

    public void setTicketPictures(ArrayList<TicketPicture> ticketPictures) {
        this.ticketPictures = ticketPictures;
    }

    public ArrayList<TicketViolation> getTicketViolations() {
        return ticketViolations;
    }

    public void setTicketViolations(ArrayList<TicketViolation> ticketViolations) {
        this.ticketViolations = ticketViolations;
    }

    public String getIsVoid() {
        return isVoid;
    }

    public void setIsVoid(String isVoid) {
        this.isVoid = isVoid;
    }

    public String getIsChalked() {
        return isChalked;
    }

    public void setIsChalked(String isChalked) {
        this.isChalked = isChalked;
    }

    public String getIsWarn() {
        return isWarn;
    }

    public void setIsWarn(String isWarn) {
        this.isWarn = isWarn;
    }

    public String getIsDriveAway() {
        return isDriveAway;
    }

    public void setIsDriveAway(String isDriveAway) {
        this.isDriveAway = isDriveAway;
    }

    public int getVoidId() {
        return voidId;
    }

    public void setVoidId(int voidId) {
        this.voidId = voidId;
    }

    public String getPermit() {
        return permit;
    }

    public void setPermit(String permit) {
        this.permit = permit;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

    public long getChalkId() {
        return chalkId;
    }

    public void setChalkId(long chalkId) {
        this.chalkId = chalkId;
    }

    public boolean isGPSLocation() {
        return "Y".equalsIgnoreCase(isGPSLocation);
    }

    public boolean isChalked() {
        return "Y".equalsIgnoreCase(isChalked);
    }

    public boolean isVoided() {
        return "Y".equalsIgnoreCase(isVoid);
    }

    public boolean isDriveAway() {
        return "Y".equalsIgnoreCase(isDriveAway);
    }

    public boolean isWarn() {
        return "Y".equalsIgnoreCase(isWarn);
    }

    public boolean isLPR() {
        return "Y".equalsIgnoreCase(isLPR);
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public Date getTimeMarked() {
        return timeMarked;
    }

    public void setTimeMarked(Date timeMarked) {
        this.timeMarked = timeMarked;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getViolationCode() {
        return violationCode;
    }

    public void setViolationCode(String violationCode) {
        this.violationCode = violationCode;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    public String getVoidReasonCode() {
        return voidReasonCode;
    }

    public void setVoidReasonCode(String voidReasonCode) {
        this.voidReasonCode = voidReasonCode;
    }

    public int getDutyId() {
        return dutyId;
    }

    public void setDutyId(int dutyId) {
        this.dutyId = dutyId;
    }

    public String getIsLPR() {
        return isLPR;
    }

    public void setIsLPR(String isLPR) {
        this.isLPR = isLPR;
    }

    public int getViolationId() {
        return violationId;
    }

    public void setViolationId(int violationId) {
        this.violationId = violationId;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getElapsed() {
        return elapsed;
    }

    public void setElapsed(String elapsed) {
        this.elapsed = elapsed;
    }

    public String getColorTitle() {
        return colorTitle;
    }

    public void setColorTitle(String colorTitle) {
        this.colorTitle = colorTitle;
    }

    public String getBodyTitle() {
        return bodyTitle;
    }

    public void setBodyTitle(String bodyTitle) {
        this.bodyTitle = bodyTitle;
    }

    public String getMakeTitle() {
        return makeTitle;
    }

    public void setMakeTitle(String makeTitle) {
        this.makeTitle = makeTitle;
    }

    public int getPhotoCount() {
        return photo_count;
    }

    public void setPhotoCount(int photo_count) {
        this.photo_count = photo_count;
    }

    public int getPhoto_count() {
        return photo_count;
    }

    public void setPhoto_count(int photo_count) {
        this.photo_count = photo_count;
    }

    public int getChalkDuration() {
        return chalkDuration;
    }

    public void setChalkDuration(int chalkDuration) {
        this.chalkDuration = chalkDuration;
    }

    public String getChalkZone() {
        return chalkZone;
    }

    public void setChalkZone(String chalkZone) {
        this.chalkZone = chalkZone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TicketSource getTicketSource() {
        return ticketSource;
    }

    public void setTicketSource(TicketSource ticketSource) {
        this.ticketSource = ticketSource;
    }

    public String getLprNotificationId() {
        return lprNotificationId;
    }

    public void setLprNotificationId(String lprNotificationId) {
        this.lprNotificationId = lprNotificationId;
    }

    public String getPlacard() {
        return placard;
    }

    public void setPlacard(String placard) {
        this.placard = placard;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public static class PlateComparator implements Comparator<Ticket> {
        @Override
        public int compare(Ticket ticket1, Ticket ticket2) {
            return ticket1.getPlate().compareToIgnoreCase(ticket2.getPlate());
        }
    }

    public static class VinComparator implements Comparator<Ticket> {
        @Override
        public int compare(Ticket ticket1, Ticket ticket2) {
            return ticket1.getVin().compareToIgnoreCase(ticket2.getVin());
        }
    }

    public static class CitationComparator implements Comparator<Ticket> {
        @Override
        public int compare(Ticket ticket1, Ticket ticket2) {
            long c1 = ticket1.getCitationNumber();
            long c2 = ticket2.getCitationNumber();
            return (Long.compare(c2, c1));
        }
    }

    public static class DateComparator implements Comparator<Ticket> {
        @Override
        public int compare(Ticket ticket1, Ticket ticket2) {
            return ticket1.getTicketDate().compareTo(ticket2.getTicketDate());
        }
    }

}