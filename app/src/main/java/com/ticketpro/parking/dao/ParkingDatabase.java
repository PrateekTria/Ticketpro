package com.ticketpro.parking.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Handler;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ticketpro.model.ALPRChalk;
import com.ticketpro.model.Body;
import com.ticketpro.model.CallInList;
import com.ticketpro.model.CallInReport;
import com.ticketpro.model.ChalkComment;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Color;
import com.ticketpro.model.Comment;
import com.ticketpro.model.CommentGroup;
import com.ticketpro.model.CommentGroupComment;
import com.ticketpro.model.Contact;
import com.ticketpro.model.CustomerInfo;
import com.ticketpro.model.CustomerModule;
import com.ticketpro.model.DeviceData;
import com.ticketpro.model.DeviceGroup;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.model.Direction;
import com.ticketpro.model.Duration;
import com.ticketpro.model.Duty;
import com.ticketpro.model.DutyReport;
import com.ticketpro.model.ErrorLog;
import com.ticketpro.model.EulaModel;
import com.ticketpro.model.Feature;
import com.ticketpro.model.GPSLocation;
//import com.ticketpro.model.GenetecPatrollerActivities;
//import com.ticketpro.model.GenetecPatrollers;
import com.ticketpro.model.GenetecPatrollerActivities;
import com.ticketpro.model.GenetecPatrollers;
import com.ticketpro.model.Hotlist;
import com.ticketpro.model.LPRNotify;
import com.ticketpro.model.Location;
import com.ticketpro.model.LocationGroup;
import com.ticketpro.model.LocationGroupLocation;
import com.ticketpro.model.LprBodyMap;
import com.ticketpro.model.MaintenanceLog;
import com.ticketpro.model.MaintenancePicture;
import com.ticketpro.model.MakeAndModel;
import com.ticketpro.model.Message;
import com.ticketpro.model.Meter;
import com.ticketpro.model.MobileNowLog;
import com.ticketpro.model.Module;
import com.ticketpro.model.Permit;
import com.ticketpro.model.Placard;
import com.ticketpro.model.PrintMacro;
import com.ticketpro.model.PrintTemplate;
import com.ticketpro.model.RepeatOffender;
import com.ticketpro.model.SpecialActivitiesLocation;
import com.ticketpro.model.SpecialActivity;
import com.ticketpro.model.SpecialActivityDisposition;
import com.ticketpro.model.SpecialActivityPicture;
import com.ticketpro.model.SpecialActivityReport;
import com.ticketpro.model.State;
import com.ticketpro.model.StreetPrefix;
import com.ticketpro.model.StreetSuffix;
import com.ticketpro.model.SyncData;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketCommentTemp;
import com.ticketpro.model.TicketHistory;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketPictureTemp;
import com.ticketpro.model.TicketTemp;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.model.TicketViolationTemp;
import com.ticketpro.model.User;
import com.ticketpro.model.UserSetting;
import com.ticketpro.model.Vendor;
import com.ticketpro.model.VendorItem;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceRegistration;
import com.ticketpro.model.Violation;
import com.ticketpro.model.ViolationGroup;
import com.ticketpro.model.ViolationGroupViolation;
import com.ticketpro.model.VoidTicketReason;
import com.ticketpro.model.Zone;
import com.ticketpro.util.Converters;
import com.ticketpro.util.TPUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Rohit on 21-07-2020.
 */

@Database(entities = {ALPRChalk.class, DeviceData.class, Body.class, LprBodyMap.class, CallInList.class,
        CallInReport.class, ChalkComment.class, ChalkPicture.class, ChalkVehicle.class,
        Color.class, CommentGroupComment.class, CommentGroup.class, Comment.class,
        Contact.class, CustomerModule.class, CustomerInfo.class, DeviceGroup.class,
        DeviceInfo.class, Direction.class, Duration.class, Duty.class, DutyReport.class,
        ErrorLog.class, Feature.class, GPSLocation.class, Hotlist.class,
        LocationGroupLocation.class, LocationGroup.class, Location.class, LPRNotify.class,
        MaintenanceLog.class, MaintenancePicture.class, MakeAndModel.class, Message.class,
        Meter.class, MobileNowLog.class, Module.class, Permit.class, PrintMacro.class,
        PrintTemplate.class, RepeatOffender.class, SpecialActivity.class,
        SpecialActivitiesLocation.class, SpecialActivityPicture.class,
        SpecialActivityDisposition.class, SpecialActivityReport.class, State.class,
        StreetPrefix.class, StreetSuffix.class, SyncData.class, TicketComment.class,
        TicketPicture.class, TicketHistory.class, TicketViolation.class, Ticket.class,
        EulaModel.class, UserSetting.class, User.class, Vendor.class, VendorService.class,
        VendorServiceRegistration.class, VendorItem.class, ViolationGroupViolation.class,
        ViolationGroup.class, Violation.class, VoidTicketReason.class, Zone.class, Placard.class,
        TicketTemp.class, TicketViolationTemp.class, TicketPictureTemp.class, TicketCommentTemp.class, GenetecPatrollers.class, GenetecPatrollerActivities.class


}, version = 24, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ParkingDatabase extends RoomDatabase {
    // Migration from 14 to 15, Room 2.1.0
    static final Migration MIGRATION_14_15 = new Migration(14, 15) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL(
                        "ALTER TABLE FT_DeviceHistory ADD COLUMN Violation TEXT ");
                database.execSQL(
                        "ALTER TABLE FT_DeviceHistory ADD COLUMN Citation TEXT ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    // Migration from 15 to 16, Room 2.1.0
    static final Migration MIGRATION_15_16 = new Migration(15, 16) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL(
                        "ALTER TABLE FT_DeviceHistory ADD COLUMN accuracy REAL NOT NULL DEFAULT 0");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    // Migration from 16 to 17
    static final Migration MIGRATION_16_17 = new Migration(16, 17) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL(
                        "ALTER TABLE chalk_pictures ADD COLUMN download_image TEXT ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    // Migration from 3 to 4, Room 2.1.0
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL(
                        "ALTER TABLE special_activity_reports ADD COLUMN start_time TEXT ");
                database.execSQL(
                        "ALTER TABLE special_activity_reports ADD COLUMN end_time TEXT ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    // Migration from 4 to 5, Room 2.1.0
    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL(
                        "ALTER TABLE tickets ADD COLUMN duty_report_id TEXT ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                database.execSQL(
                        "ALTER TABLE duty_reports ADD COLUMN sync_status TEXT ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                database.execSQL(
                        "ALTER TABLE duty_reports ADD COLUMN duty_report_id TEXT ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL(
                        "ALTER TABLE tickets ADD COLUMN app_version TEXT ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL(
                        "ALTER TABLE special_activity_reports ADD COLUMN ticket_count TEXT ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL(
                        "ALTER TABLE error_logs ADD COLUMN module TEXT ");
                database.execSQL(
                        "ALTER TABLE error_logs ADD COLUMN app_version TEXT ");
                database.execSQL(
                        "ALTER TABLE error_logs ADD COLUMN device TEXT ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };


    static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL(
                        "ALTER TABLE users ADD COLUMN rmsid TEXT ");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL(
                        "ALTER TABLE mobile_now_logs ADD COLUMN latitude TEXT ");
                database.execSQL(
                        "ALTER TABLE mobile_now_logs ADD COLUMN longitude TEXT ");
                database.execSQL(
                        "ALTER TABLE mobile_now_logs ADD COLUMN location TEXT ");
                database.execSQL(
                        "ALTER TABLE mobile_now_logs ADD COLUMN duty_id TEXT ");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL("CREATE TABLE IF NOT EXISTS lprbodymap (body_id INTEGER NOT NULL DEFAULT 0, " +
                        "custid INTEGER NOT NULL DEFAULT 0,LPRBody TEXT, TicketBody TEXT, order_number INTEGER NOT NULL DEFAULT 0," +
                        " PRIMARY KEY(body_id))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    static final Migration MIGRATION_11_12 = new Migration(11, 12) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL(
                        "ALTER TABLE vendor_items ADD COLUMN order_number INTEGER NOT NULL DEFAULT 0");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    static final Migration MIGRATION_13_14 = new Migration(13, 14) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL(
                        "ALTER TABLE FT_DeviceHistory ADD COLUMN altitude REAL NOT NULL DEFAULT 0");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    static final Migration MIGRATION_12_13 = new Migration(12, 13) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL(
                        "CREATE TABLE IF NOT EXISTS \"temp_chalk_pictures\" (\n" +
                                "\t\"picture_id\"\tINTEGER NOT NULL DEFAULT 0,\n" +
                                "\t\"chalk_id\"\tINTEGER NOT NULL DEFAULT 0,\n" +
                                "\t\"chalk_time\"\tINTEGER,\n" +
                                "\t\"location\"\tTEXT,\n" +
                                "\t\"latitute\"\tTEXT,\n" +
                                "\t\"longitude\"\tTEXT,\n" +
                                "\t\"image_path\"\tTEXT,\n" +
                                "\t\"image_resolution\"\tTEXT,\n" +
                                "\t\"image_size\"\tTEXT,\n" +
                                "\t\"sync_status\"\tTEXT,\n" +
                                "\t\"custid\"\tINTEGER NOT NULL DEFAULT 0,\n" +
                                "\tPRIMARY KEY(\"picture_id\" AUTOINCREMENT)\n" +
                                ");");
                database.execSQL("INSERT INTO `temp_chalk_pictures` SELECT * FROM `chalk_pictures`");
                database.execSQL("ALTER TABLE `chalk_pictures` RENAME TO `original_chalk_pictures`");
                database.execSQL("ALTER TABLE `temp_chalk_pictures` RENAME TO `chalk_pictures`");
                database.execSQL("DROP TABLE IF EXISTS `original_chalk_pictures`");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    // Migration from 16 to 17
    static final Migration MIGRATION_17_18 = new Migration(17, 18) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS \"tickets_temp\" (\n" +
                            "\t\"ticket_id\"\tINTEGER NOT NULL ,\n" +
                            "\t\"userid\"\tINTEGER NOT NULL ,\n" +
                            "\t\"custid\"\tINTEGER NOT NULL ,\n" +
                            "\t\"device_id\"\tINTEGER NOT NULL ,\n" +
                            "\t\"citation_number\"\tINTEGER NOT NULL ,\n" +
                            "\t\"state_id\"\tINTEGER NOT NULL ,\n" +
                            "\t\"make_id\"\tINTEGER NOT NULL ,\n" +
                            "\t\"body_id\"\tINTEGER NOT NULL ,\n" +
                            "\t\"color_id\"\tINTEGER NOT NULL ,\n" +
                            "\t\"chalk_id\"\tINTEGER NOT NULL ,\n" +
                            "\t\"duty_id\"\tINTEGER NOT NULL ,\n" +
                            "\t\"violation_id\"\tINTEGER NOT NULL ,\n" +
                            "\t\"photo_count\"\tINTEGER NOT NULL ,\n" +
                            "\t\"ticket_date\"\tINTEGER ,\n" +
                            "\t\"state_code\"\tTEXT,\n" +
                            "\t\"plate\"\tTEXT,\n" +
                            "\t\"vin\"\tTEXT,\n" +
                            "\t\"expiration\"\tTEXT,\n" +
                            "\t\"make_code\"\tTEXT,\n" +
                            "\t\"body_code\"\tTEXT,\n" +
                            "\t\"color_code\"\tTEXT,\n" +
                            "\t\"street_prefix\"\tTEXT,\n" +
                            "\t\"street_suffix\"\tTEXT,\n" +
                            "\t\"street_number\"\tTEXT,\n" +
                            "\t\"location\"\tTEXT,\n" +
                            "\t\"direction\"\tTEXT,\n" +
                            "\t\"latitude\"\tTEXT,\n" +
                            "\t\"longitude\"\tTEXT,\n" +
                            "\t\"gpstime\"\tINTEGER ,\n" +
                            "\t\"is_gps_location\"\tTEXT,\n" +
                            "\t\"is_void\"\tTEXT,\n" +
                            "\t\"is_chalked\"\tTEXT,\n" +
                            "\t\"is_warn\"\tTEXT,\n" +
                            "\t\"is_driveaway\"\tTEXT,\n" +
                            "\t\"void_id\"\tINTEGER NOT NULL,\n" +
                            "\t\"permit\"\tTEXT,\n" +
                            "\t\"meter\"\tTEXT,\n" +
                            "\t\"fine\"\tREAL NOT NULL,\n" +
                            "\t\"time_marked\"\tINTEGER ,\n" +
                            "\t\"space\"\tTEXT,\n" +
                            "\t\"violation_code\"\tTEXT,\n" +
                            "\t\"violation\"\tTEXT,\n" +
                            "\t\"void_reason_code\"\tTEXT,\n" +
                            "\t\"is_lpr\"\tTEXT,\n" +
                            "\t\"lpr_notification_id\"\tTEXT,\n" +
                            "\t\"status\"\tTEXT,\n" +
                            "\t\"placard\"\tTEXT,\n" +
                            "\t\"duty_report_id\"\tTEXT,\n" +
                            "\t\"app_version\"\tTEXT,\n" +

                            "\tPRIMARY KEY(\"ticket_id\" AUTOINCREMENT)\n" +
                            ");");

            database.execSQL(
                    "CREATE TABLE \"ticket_violations_temp\" (\n" +
                            "\t\"citation_number\"\tINTEGER NOT NULL,\n" +
                            "\t\"custid\"\tINTEGER NOT NULL,\n" +
                            "\t\"ticket_id\"\tINTEGER NOT NULL,\n" +
                            "\t\"ticket_violation_id\"\tINTEGER NOT NULL,\n" +
                            "\t\"violation_id\"\tINTEGER NOT NULL,\n" +
                            "\t\"violation_code\"\tTEXT,\n" +
                            "\t\"fine\"\tREAL NOT NULL,\n" +
                            "\t\"violation\"\tTEXT,\n" +
                            "\tPRIMARY KEY(\"ticket_violation_id\" AUTOINCREMENT)\n" +
                            ");");
            //Picture
            database.execSQL(
                    "CREATE TABLE \"ticket_pictures_temp\" (\n" +
                            "\t\"citation_number\"\tINTEGER NOT NULL,\n" +
                            "\t\"custid\"\tINTEGER NOT NULL,\n" +
                            "\t\"ticket_id\"\tINTEGER NOT NULL,\n" +
                            "\t\"s_no\"\tINTEGER NOT NULL,\n" +
                            "\t\"picture_id\"\tINTEGER NOT NULL,\n" +
                            "\t\"isSelfi\"\tINTEGER NOT NULL,\n" +
                            "\t\"image_path\"\tTEXT,\n" +
                            "\t\"image_resolution\"\tTEXT,\n" +
                            "\t\"image_size\"\tTEXT,\n" +
                            "\t\"mark_print\"\tTEXT,\n" +
                            "\t\"picture_date\"\tINTEGER,\n" +
                            "\t\"sync_status\"\tTEXT,\n" +
                            "\t\"download_image_url\"\tTEXT,\n" +
                            "\t\"image_name\"\tTEXT,\n" +
                            "\tPRIMARY KEY(\"s_no\" AUTOINCREMENT)\n" +
                            ");");

            //Comment
            database.execSQL(
                    "CREATE TABLE \"ticket_comments_temp\" (\n" +
                            "\t\"citation_number\"\tINTEGER NOT NULL,\n" +
                            "\t\"custid\"\tINTEGER NOT NULL,\n" +
                            "\t\"ticket_id\"\tINTEGER NOT NULL,\n" +
                            "\t\"ticket_comment_id\"\tINTEGER NOT NULL,\n" +
                            "\t\"is_voice_comment\"\tINTEGER NOT NULL,\n" +
                            "\t\"comment\"\tTEXT,\n" +
                            "\t\"is_private\"\tTEXT,\n" +
                            "\t\"comment_id\"\tINTEGER NOT NULL,\n" +
                            "\tPRIMARY KEY(\"ticket_comment_id\" AUTOINCREMENT)\n" +
                            ");");

            database.execSQL("CREATE UNIQUE INDEX index_tickets_temp_custid ON tickets_temp (custid)");
            database.execSQL("CREATE UNIQUE INDEX index_tickets_temp_violation_code ON ticket_violations_temp (violation_code)");
            database.execSQL("CREATE UNIQUE INDEX index_tickets_temp_picture_date ON ticket_pictures_temp (picture_date)");
            database.execSQL("CREATE UNIQUE INDEX index_tickets_temp_comment_id ON ticket_comments_temp (comment_id)");


        }
    };


    //Migration 18_19

    static final Migration MIGRATION_18_19 = new Migration(18, 19) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL(
                        "ALTER TABLE tickets ADD COLUMN chalk_time TEXT ");  //18238 32586 23643 50970
                database.execSQL(
                        "ALTER TABLE tickets ADD COLUMN chalk_last_seen TEXT ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    //Migration 19_20

    //Added by Mohit
    static final Migration MIGRATION_19_20 = new Migration(19, 20) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL("ALTER TABLE violations ADD COLUMN hide TEXT ");
                database.execSQL("ALTER TABLE violations ADD COLUMN code_export TEXT ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    static final Migration MIGRATION_20_21 = new Migration(20, 21) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL("ALTER TABLE violations ADD COLUMN repeat_offender_type TEXT ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };


    static final Migration MIGRATION_21_22 = new Migration(21, 22) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL("ALTER TABLE chalk_vehicles ADD COLUMN sync_status TEXT ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    static final Migration MIGRATION_22_23 = new Migration(22, 23) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL("ALTER TABLE tickets ADD COLUMN ticket_date_new TEXT ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };


    public static final Migration MIGRATION_23_24 = new Migration(23, 24) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create the Genetec_Patrollers table
            database.execSQL("CREATE TABLE IF NOT EXISTS `Genetec_Patrollers` (" +
                    "`record_Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`custId` INTEGER, " +
                    "`patroller_Id` TEXT, " +
                    "`vehicleName` TEXT, " +
                    "`is_active` TEXT, " +
                    "`createdOn` TEXT)");



            // Create the Genetec_PatrollerActivities table
            database.execSQL("CREATE TABLE IF NOT EXISTS `Genetec_PatrollerActivities` (" +
                    "`record_Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`createdOn` TEXT, " + // Change to long if storing epoch time
                    "`custId` INTEGER, " +
                    "`patroller_Id` TEXT, " +
                    "`user_id` TEXT, " +
                    "`device_Id` INTEGER, " +
                    "`is_active` TEXT)");
        }
    };




    private static final String DB_NAME = "ticketpronew.db";
    public static ParkingDatabase instance;
    private static String DATABASE_PATH = "";

    public static ParkingDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (ParkingDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), ParkingDatabase.class, DB_NAME)
                            .allowMainThreadQueries()
                            //.addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_3_4)
                            .addMigrations(MIGRATION_4_5)
                            .addMigrations(MIGRATION_5_6)
                            .addMigrations(MIGRATION_6_7)
                            .addMigrations(MIGRATION_7_8)
                            .addMigrations(MIGRATION_8_9)
                            .addMigrations(MIGRATION_9_10)
                            .addMigrations(MIGRATION_10_11)
                            .addMigrations(MIGRATION_11_12)
                            .addMigrations(MIGRATION_12_13)
                            .addMigrations(MIGRATION_13_14)
                            .addMigrations(MIGRATION_14_15)
                            .addMigrations(MIGRATION_15_16)
                            .addMigrations(MIGRATION_16_17)
                            .addMigrations(MIGRATION_17_18)
                            .addMigrations(MIGRATION_18_19)
                            .addMigrations(MIGRATION_19_20)
                            .addMigrations(MIGRATION_20_21)
                            .addMigrations(MIGRATION_21_22)
                            .addMigrations(MIGRATION_22_23)
                            .addMigrations(MIGRATION_23_24)
                            .build();
                }
            }
        }
        return instance;
    }

    static void destroyInstance() {
        if (instance.isOpen()) {
            instance.close();
        }
        instance = null;
    }

    public static void backupDatabase(final Context context) {
        if (instance != null) {
            destroyInstance();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //DATABASE_PATH = context.getDatabasePath(DB_NAME).getAbsolutePath();
                DATABASE_PATH = "/data/data/com.ticketpro.parking/databases/" + DB_NAME;
                try {
                    if (DATABASE_PATH == null) {
                        DATABASE_PATH = context.getDatabasePath(DB_NAME).getAbsolutePath();
                    }

                    File dbFile = new File(DATABASE_PATH);
                    FileInputStream fis;

                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                    String dateStr = sdf.format(cal.getTime());

                    fis = new FileInputStream(dbFile);
                    String outFileName = TPUtility.getBackupFolder() + "ticketPRO_" + dateStr + ".sqlite";
                    OutputStream output = new FileOutputStream(outFileName);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        output.write(buffer, 0, length);
                    }

                    output.flush();
                    output.close();
                    fis.close();

                    //log.info("Done database backup..." + outFileName);

                } catch (IOException e) {
                    //log.error(TPUtility.getPrintStackTrace(e));
                }

            }
        }, 1500);
    }

    public abstract ALPRPhotoChalkDao alprPhotoChalkDao();

    public abstract BodyDao bodyDao();

    public abstract PlrBodyDao lprbodyDao();

    public abstract CallInListDao callInListDao();

    public abstract CallInListReportDao callInListReportDao();

    public abstract ChalkCommentsDao chalkCommentsDao();

    public abstract ChalkPicturesDao chalkPicturesDao();

    public abstract ChalkVehiclesDao chalkVehiclesDao();

    public abstract ColorsDao colorsDao();

    public abstract CommentgroupCommentsDao commentgroupCommentsDao();

    public abstract CommentgroupsDao commentgroupsDao();

    public abstract CommentsDao commentsDao();

    public abstract ContactsDao contactsDao();

    public abstract CustomerModulesDao customerModulesDao();

    public abstract CustomersDao customersDao();

    public abstract DeviceGroupsDao deviceGroupsDao();

    public abstract DevicesDao devicesDao();

    public abstract DirectionsDao directionsDao();

    public abstract DurationDao durationDao();

    public abstract DutiesDao dutiesDao();

    public abstract DutyReportsDao dutyReportsDao();

    public abstract ErrorLogsDao errorLogsDao();

    public abstract FeaturesDao featuresDao();

    public abstract FT_DeviceHistoryDao ftDeviceHistoryDao();

    public abstract GPSLocationsDao gpsLocationsDao();

    public abstract HotlistDao hotlistDao();

    public abstract LocationGroupsDao locationGroupsDao();

    public abstract LocationGroupLocationsDao locationGroupLocationsDao();

    public abstract LocationsDao locationsDao();

    public abstract LPRNotificationsDao lprNotificationsDao();

    public abstract MaintenanceLogsDao maintenanceLogsDao();

    public abstract MaintenancePicturesDao maintenancePicturesDao();

    public abstract MakesAndModelsDao makesAndModelsDao();

    public abstract MessagesDao messagesDao();

    public abstract MetersDao metersDao();

    public abstract MobileNowLogsDao mobileNowLogsDao();

    public abstract ModulesDao modulesDao();

    public abstract PermitsDao permitsDao();

    public abstract PrintMacrosDao printMacrosDao();

    public abstract PrintTemplatesDao printTemplatesDao();

    public abstract RepeatOffendersDao repeatOffendersDao();

    public abstract SpecialActivitiesDao specialActivitiesDao();

    public abstract SpecialActivityDispositionDao specialActivityDispositionDao();

    public abstract SpecialActivityReportsDao specialActivityReportsDao();

    public abstract SpecialActivityLocationDao specialActivityLocationDao();

    public abstract SpecialActivityPictureDao specialActivityPictureDao();

    public abstract StatesDao statesDao();

    public abstract StreetPrefixesDao streetPrefixesDao();

    public abstract StreetSuffixesDao streetSuffixesDao();

    public abstract SyncDataDao syncDataDao();

    public abstract TicketCommentsDao ticketCommentsDao();

    public abstract TicketCommentsDaoTemp ticketCommentsDaoTemp();

    public abstract TicketHistoryDao ticketHistoryDao();

    public abstract TicketPicturesDao ticketPicturesDao();

    public abstract TicketPicturesDaoTemp ticketPicturesDaoTemp();

    public abstract TicketsDao ticketsDao();

    public abstract TicketsDaoTemp ticketsDaoTemp();

    public abstract TicketViolationsDao ticketViolationsDao();

    public abstract TicketViolationsDaoTemp ticketViolationsDaoTemp();

    public abstract TPMEulaDao tpmEulaDao();

    public abstract UsersDao usersDao();

    public abstract UserSettingsDao userSettingsDao();

    public abstract VendorsDao vendorsDao();

    public abstract VendorItemsDao vendorItemsDao();

    public abstract VendorServicesDao vendorServicesDao();

    public abstract VendorServiceRegistrationsDao vendorServiceRegistrationsDao();

    public abstract ViolationGroupsDao violationGroupsDao();

    public abstract ViolationGroupViolationsDao violationGroupViolationsDao();

    public abstract ViolationsDao violationsDao();

    public abstract VoidTicketReasonsDao voidTicketReasonsDao();

    public abstract ZonesDao zonesDao();

    public abstract PlacardDao placardDao();

    public abstract GenetecPatrollerDao genetecPatrollerDao();

    public abstract GenetecPatrollerActivitiesDao genetecPatrollerActivitiesDao();


}
