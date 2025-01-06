package com.ticketpro.parking.dao;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ParkingDatabase_Impl extends ParkingDatabase {
  private volatile ALPRPhotoChalkDao _aLPRPhotoChalkDao;

  private volatile BodyDao _bodyDao;

  private volatile PlrBodyDao _plrBodyDao;

  private volatile CallInListDao _callInListDao;

  private volatile CallInListReportDao _callInListReportDao;

  private volatile ChalkCommentsDao _chalkCommentsDao;

  private volatile ChalkPicturesDao _chalkPicturesDao;

  private volatile ChalkVehiclesDao _chalkVehiclesDao;

  private volatile ColorsDao _colorsDao;

  private volatile CommentgroupCommentsDao _commentgroupCommentsDao;

  private volatile CommentgroupsDao _commentgroupsDao;

  private volatile CommentsDao _commentsDao;

  private volatile ContactsDao _contactsDao;

  private volatile CustomerModulesDao _customerModulesDao;

  private volatile CustomersDao _customersDao;

  private volatile DeviceGroupsDao _deviceGroupsDao;

  private volatile DevicesDao _devicesDao;

  private volatile DirectionsDao _directionsDao;

  private volatile DurationDao _durationDao;

  private volatile DutiesDao _dutiesDao;

  private volatile DutyReportsDao _dutyReportsDao;

  private volatile ErrorLogsDao _errorLogsDao;

  private volatile FeaturesDao _featuresDao;

  private volatile FT_DeviceHistoryDao _fTDeviceHistoryDao;

  private volatile GPSLocationsDao _gPSLocationsDao;

  private volatile HotlistDao _hotlistDao;

  private volatile LocationGroupsDao _locationGroupsDao;

  private volatile LocationGroupLocationsDao _locationGroupLocationsDao;

  private volatile LocationsDao _locationsDao;

  private volatile LPRNotificationsDao _lPRNotificationsDao;

  private volatile MaintenanceLogsDao _maintenanceLogsDao;

  private volatile MaintenancePicturesDao _maintenancePicturesDao;

  private volatile MakesAndModelsDao _makesAndModelsDao;

  private volatile MessagesDao _messagesDao;

  private volatile MetersDao _metersDao;

  private volatile MobileNowLogsDao _mobileNowLogsDao;

  private volatile ModulesDao _modulesDao;

  private volatile PermitsDao _permitsDao;

  private volatile PrintMacrosDao _printMacrosDao;

  private volatile PrintTemplatesDao _printTemplatesDao;

  private volatile RepeatOffendersDao _repeatOffendersDao;

  private volatile SpecialActivitiesDao _specialActivitiesDao;

  private volatile SpecialActivityDispositionDao _specialActivityDispositionDao;

  private volatile SpecialActivityReportsDao _specialActivityReportsDao;

  private volatile SpecialActivityLocationDao _specialActivityLocationDao;

  private volatile SpecialActivityPictureDao _specialActivityPictureDao;

  private volatile StatesDao _statesDao;

  private volatile StreetPrefixesDao _streetPrefixesDao;

  private volatile StreetSuffixesDao _streetSuffixesDao;

  private volatile SyncDataDao _syncDataDao;

  private volatile TicketCommentsDao _ticketCommentsDao;

  private volatile TicketCommentsDaoTemp _ticketCommentsDaoTemp;

  private volatile TicketHistoryDao _ticketHistoryDao;

  private volatile TicketPicturesDao _ticketPicturesDao;

  private volatile TicketPicturesDaoTemp _ticketPicturesDaoTemp;

  private volatile TicketsDao _ticketsDao;

  private volatile TicketsDaoTemp _ticketsDaoTemp;

  private volatile TicketViolationsDao _ticketViolationsDao;

  private volatile TicketViolationsDaoTemp _ticketViolationsDaoTemp;

  private volatile TPMEulaDao _tPMEulaDao;

  private volatile UsersDao _usersDao;

  private volatile UserSettingsDao _userSettingsDao;

  private volatile VendorsDao _vendorsDao;

  private volatile VendorItemsDao _vendorItemsDao;

  private volatile VendorServicesDao _vendorServicesDao;

  private volatile VendorServiceRegistrationsDao _vendorServiceRegistrationsDao;

  private volatile ViolationGroupsDao _violationGroupsDao;

  private volatile ViolationGroupViolationsDao _violationGroupViolationsDao;

  private volatile ViolationsDao _violationsDao;

  private volatile VoidTicketReasonsDao _voidTicketReasonsDao;

  private volatile ZonesDao _zonesDao;

  private volatile PlacardDao _placardDao;

  private volatile GenetecPatrollerDao _genetecPatrollerDao;

  private volatile GenetecPatrollerActivitiesDao _genetecPatrollerActivitiesDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(24) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ALPRPhotoChalk` (`Plate` TEXT NOT NULL, `Details` TEXT, `DataField1` TEXT, `DataField2` TEXT, `DataField3` TEXT, `Confidence` TEXT, `FirstDate` TEXT, `FirstTime` TEXT, `FirstDateTime` INTEGER, `FirstParkingBay` TEXT, `FirstLocLat` TEXT, `FirstLocLong` TEXT, `FirstLocAcc` TEXT, `LastDate` TEXT, `LastTime` TEXT, `LastDateTime` INTEGER, `LastParkingBay` TEXT, `LastLocLat` TEXT, `LastLocLong` TEXT, `LastLocAcc` TEXT, `PermitExpiryDate` TEXT, `PermitExpiryTime` TEXT, `chalkDuration` INTEGER NOT NULL, `duration_code` TEXT, `chalkLocation` TEXT, `chalkTire` TEXT, `chalkId` INTEGER NOT NULL, `userid` INTEGER NOT NULL, `deviceId` INTEGER NOT NULL, `custId` INTEGER NOT NULL, `is_expired` TEXT, PRIMARY KEY(`Plate`))");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_ALPRPhotoChalk_Plate` ON `ALPRPhotoChalk` (`Plate`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `FT_DeviceHistory` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dutyId` TEXT, `dutyName` TEXT, `custId` TEXT, `deviceId` TEXT, `deviceName` TEXT, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `name` TEXT, `fullName` TEXT, `device` TEXT, `timeStamp` TEXT, `badge` TEXT, `isActive` INTEGER NOT NULL, `userId` TEXT, `firstLogin` TEXT, `lastTicketTimeStamp` TEXT, `currTimeStamp` TEXT, `pushToken` TEXT, `moduleId` TEXT, `deviceInactivity` INTEGER NOT NULL, `isLoggedIn` INTEGER NOT NULL, `appVersion` TEXT, `address` TEXT, `activityName` TEXT, `sync_status` TEXT, `altitude` REAL NOT NULL, `Violation` TEXT, `Citation` TEXT, `accuracy` REAL NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `bodies` (`body_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `body` TEXT, `code` TEXT, `order_number` INTEGER NOT NULL, PRIMARY KEY(`body_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `lprbodymap` (`body_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `LPRBody` TEXT, `TicketBody` TEXT, `order_number` INTEGER NOT NULL, PRIMARY KEY(`body_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `call_in_list` (`call_in_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `action_item` TEXT, `action_code` TEXT, `order_number` INTEGER NOT NULL, PRIMARY KEY(`call_in_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `call_in_reports` (`report_id` INTEGER NOT NULL, `userid` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `call_in_date` INTEGER, `plate` TEXT, `plate_type` TEXT, `state` TEXT, `from_search` TEXT, `from_hit` TEXT, `action_taken` TEXT, `photo1` TEXT, `photo2` TEXT, `photo3` TEXT, PRIMARY KEY(`report_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `chalk_comments` (`chalk_comment_id` INTEGER NOT NULL, `chalk_id` INTEGER NOT NULL, `comment_id` INTEGER NOT NULL, `comment` TEXT, `is_private` TEXT DEFAULT 'N', `custid` INTEGER NOT NULL, PRIMARY KEY(`chalk_comment_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `chalk_pictures` (`picture_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `chalk_id` INTEGER NOT NULL, `chalk_time` INTEGER, `location` TEXT, `latitute` TEXT, `longitude` TEXT, `image_path` TEXT, `image_resolution` TEXT, `image_size` TEXT, `sync_status` TEXT, `custid` INTEGER NOT NULL, `download_image` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `chalk_vehicles` (`chalk_id` INTEGER NOT NULL, `userid` INTEGER NOT NULL, `device_id` INTEGER NOT NULL, `chalk_date` INTEGER, `permit` TEXT, `plate` TEXT, `vin` TEXT, `state_id` INTEGER NOT NULL, `zone_id` INTEGER NOT NULL, `expiration` INTEGER, `duration_id` INTEGER NOT NULL, `duration_code` TEXT, `space` TEXT, `meter` TEXT, `tire` TEXT, `chalkx` INTEGER NOT NULL, `chalky` INTEGER NOT NULL, `stemx` INTEGER NOT NULL, `stemy` INTEGER NOT NULL, `chalk_type` TEXT, `location` TEXT, `street_prefix` TEXT, `street_suffix` TEXT, `street_number` TEXT, `direction` TEXT, `latitude` TEXT, `longitude` TEXT, `gpstime` INTEGER, `is_gps_location` TEXT, `is_expired` TEXT, `custid` INTEGER NOT NULL, `wheel_size` INTEGER NOT NULL, `notes` TEXT, `make_code` TEXT, `color_code` TEXT, `make` TEXT, `color` TEXT, `sync_status` TEXT, PRIMARY KEY(`chalk_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `colors` (`color_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `color` TEXT, `code` TEXT, `order_number` INTEGER NOT NULL, PRIMARY KEY(`color_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `comment_group_comments` (`comment_group_id` INTEGER NOT NULL, `group_id` INTEGER NOT NULL, `comment_id` INTEGER NOT NULL, PRIMARY KEY(`comment_group_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `comment_groups` (`group_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `group_code` TEXT, `group_name` TEXT, PRIMARY KEY(`group_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `comments` (`comment_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `comment` TEXT, `code` TEXT, `order_number` INTEGER NOT NULL, PRIMARY KEY(`comment_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `contacts` (`contact_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `email_id` TEXT, `phone` TEXT, `contact_type` TEXT, `is_active` TEXT, PRIMARY KEY(`contact_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `customer_modules` (`customer_module_id` INTEGER NOT NULL, `module_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `userid` TEXT, `is_active` TEXT, PRIMARY KEY(`customer_module_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `customers` (`custid` INTEGER NOT NULL, `name` TEXT, `address` TEXT, `contact_number` TEXT, `logo_image` TEXT, `agency_code` TEXT, `web_address` TEXT, `content_folder` TEXT, `ticket_color` TEXT, `ticket_back` TEXT, `TRCourtCode` TEXT, `TRPrintAgencyName` TEXT, PRIMARY KEY(`custid`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `device_groups` (`group_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `group_name` TEXT, `device_ids` TEXT, PRIMARY KEY(`group_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `devices` (`device_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `device_name` TEXT, `device` TEXT, `app_version` TEXT, `os_version` TEXT, `platform` TEXT, `last_sync` INTEGER, `lastTicketTime` TEXT, `start_citation_number` INTEGER NOT NULL, `current_citation_number` INTEGER NOT NULL, `end_citation_number` INTEGER NOT NULL, `start_warning_number` INTEGER NOT NULL, `current_warning_number` INTEGER NOT NULL, `end_warning_number` INTEGER NOT NULL, `start_photo_number` INTEGER NOT NULL, `current_photo_number` INTEGER NOT NULL, `end_photo_number` INTEGER NOT NULL, `default_template_id` INTEGER NOT NULL, `gcm_registration_id` TEXT, `default_printer_name` TEXT, PRIMARY KEY(`device_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `directions` (`direction_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `direction` TEXT, `code` TEXT, `order_number` INTEGER NOT NULL, PRIMARY KEY(`direction_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `durations` (`duration_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `duration` TEXT, `duration_mins` INTEGER NOT NULL, `order_number` INTEGER NOT NULL, `default_violation_id` INTEGER NOT NULL, `auto_delete` TEXT, PRIMARY KEY(`duration_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `duties` (`duty_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `duty` TEXT, `code` TEXT, `order_number` INTEGER NOT NULL, `allow_ticket` TEXT, `location_groups` TEXT, `comment_groups` TEXT, `violation_groups` TEXT, PRIMARY KEY(`duty_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `duty_reports` (`report_id` INTEGER NOT NULL, `userid` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `duty_id` INTEGER NOT NULL, `date_in` INTEGER, `date_out` INTEGER, `signature` TEXT, `device_id` INTEGER NOT NULL, `duty_report_id` TEXT, `sync_status` TEXT, PRIMARY KEY(`report_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `error_logs` (`error_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `userid` INTEGER NOT NULL, `device_id` INTEGER NOT NULL, `error_type` TEXT, `error_desc` TEXT, `error_date` INTEGER, `date` TEXT, `module` TEXT, `app_version` TEXT, `device` TEXT, PRIMARY KEY(`error_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `features` (`feature_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `feature` TEXT, `admin` TEXT, `officer` TEXT, `value` TEXT, `order_number` INTEGER NOT NULL, PRIMARY KEY(`feature_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `gps_locations` (`location_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `latitude` TEXT, `longitude` TEXT, `gpstime` INTEGER, `location` TEXT, `street_number` TEXT, `street_prefix` TEXT, `street_suffix` TEXT, PRIMARY KEY(`location_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `hotlist` (`hotlist_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `state_code` TEXT, `plate` TEXT, `plate_type` TEXT, `begin_date` INTEGER, `end_date` INTEGER, `comments` TEXT, `number_of_tickets` INTEGER NOT NULL, `amount_owed` REAL NOT NULL, `date_added` INTEGER, `is_active` TEXT, PRIMARY KEY(`hotlist_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `location_group_locations` (`location_group_id` INTEGER NOT NULL, `group_id` INTEGER NOT NULL, `location_id` INTEGER NOT NULL, PRIMARY KEY(`location_group_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `location_groups` (`group_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `group_code` TEXT, `group_name` TEXT, PRIMARY KEY(`group_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `locations` (`location_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `location` TEXT, `zone_id` INTEGER NOT NULL, `order_number` INTEGER NOT NULL, `is_manual` TEXT, PRIMARY KEY(`location_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `lpr_notifications` (`notification_id` TEXT NOT NULL, `plate` TEXT, `state` TEXT, `make` TEXT, `model` TEXT, `body` TEXT, `status` TEXT, `location` TEXT, `latitude` TEXT, `longitude` TEXT, `photo1` TEXT, `photo2` TEXT, `photo3` TEXT, `photo4` TEXT, `date_notify` INTEGER, `first_chalk_time` INTEGER, `last_seen` INTEGER, `lpr_scan_id` TEXT, `lpr_camera_id` TEXT, `lpr_user_id` TEXT, `color` TEXT, `permit` TEXT, `permit_type` TEXT, `permit_status` TEXT, `duty_group` TEXT, `comments` TEXT, `comments2` TEXT, `violation_id` TEXT, `violation_desc` TEXT, `violation_code` TEXT, `device_id` TEXT, `zone` TEXT, `ticket_issues` INTEGER NOT NULL, PRIMARY KEY(`notification_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `maintenance_logs` (`log_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `userid` INTEGER NOT NULL, `device_id` INTEGER NOT NULL, `item_name` TEXT, `problem_type` TEXT, `comments` TEXT, `log_date` INTEGER, `latitude` TEXT, `longitude` TEXT, `location` TEXT, PRIMARY KEY(`log_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `maintenance_pictures` (`picture_id` INTEGER NOT NULL, `maintenance_id` INTEGER NOT NULL, `image_path` TEXT, `image_size` TEXT, `image_resolution` TEXT, PRIMARY KEY(`picture_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `makes_and_models` (`make_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `make` TEXT, `make_code` TEXT, `model` TEXT, `model_code` TEXT, `order_number` INTEGER NOT NULL, PRIMARY KEY(`make_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `messages` (`message_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `message_date` INTEGER, `from_userid` INTEGER NOT NULL, `to_userid` INTEGER NOT NULL, `subject` TEXT, `message` TEXT, `expiry_date` INTEGER, `department` TEXT, PRIMARY KEY(`message_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `meters` (`meter_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `meter` TEXT, `location` TEXT, `street_number` TEXT, `street_prefix` TEXT, `street_suffix` TEXT, `direction` TEXT, `violation_code` TEXT, PRIMARY KEY(`meter_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `mobile_now_logs` (`log_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `userid` INTEGER NOT NULL, `device_id` INTEGER NOT NULL, `request_params` TEXT, `service_mode` TEXT, `response_text` TEXT, `request_date` INTEGER, `plate_number` TEXT, `latitude` TEXT, `longitude` TEXT, `location` TEXT, `duty_id` TEXT, PRIMARY KEY(`log_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `modules` (`module_id` INTEGER NOT NULL, `module_name` TEXT, `module_desc` TEXT, `is_active` TEXT, PRIMARY KEY(`module_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `permits` (`permit_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `permit_number` TEXT, `permit_type` TEXT, `permit_code` TEXT, `plate` TEXT, `plate_type` TEXT, `vin` TEXT, `exp_date` INTEGER, `state_code` TEXT, `body_code` TEXT, `color_code` TEXT, `make_code` TEXT, `class_code` TEXT, `begin_date` INTEGER, `end_date` INTEGER, `permit_holder` TEXT, `student_id` INTEGER NOT NULL, `address1` TEXT, `address2` TEXT, `phone` TEXT, `model_code` TEXT, PRIMARY KEY(`permit_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `print_macros` (`print_macro_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `macro_name` TEXT, `message` TEXT, PRIMARY KEY(`print_macro_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `print_templates` (`template_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `printer_name` TEXT, `printer_type` TEXT, `display_name` TEXT, `template_name` TEXT, `template_data` TEXT, `is_report` TEXT, PRIMARY KEY(`template_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `repeat_offenders` (`repeat_offender_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `plate` TEXT, `violation` TEXT, `count` INTEGER NOT NULL, `violation_id` INTEGER NOT NULL, `state_code` TEXT, `created_date` TEXT, `sync_status` TEXT, PRIMARY KEY(`repeat_offender_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `special_activities` (`activity_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `activity` TEXT, `code` TEXT, `order_number` INTEGER NOT NULL, `autoSelect` TEXT, PRIMARY KEY(`activity_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `special_activities_location` (`recid` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `location` TEXT, `is_active` TEXT, `order_number` INTEGER NOT NULL, PRIMARY KEY(`recid`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `special_activities_pictures` (`picture_id` INTEGER NOT NULL, `report_id` INTEGER NOT NULL, `picture_date` TEXT, `image_path` TEXT, `image_resolution` TEXT, `image_size` TEXT, `custid` INTEGER NOT NULL, `image_name` TEXT, PRIMARY KEY(`picture_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `special_activity_dispositions` (`disposition_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `disposition` TEXT, `code` TEXT, `order_number` INTEGER NOT NULL, PRIMARY KEY(`disposition_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `special_activity_reports` (`report_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userid` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `activity_id` INTEGER NOT NULL, `disposition_id` INTEGER NOT NULL, `start_date` TEXT, `end_date` TEXT, `start_time` TEXT, `end_time` TEXT, `case_number` TEXT, `street_address` TEXT, `notes` TEXT, `photo1` TEXT, `photo2` TEXT, `photo3` TEXT, `latitude` TEXT, `longitude` TEXT, `autoSelect` TEXT, `createDate` TEXT, `duration` TEXT, `device_id` INTEGER NOT NULL, `location` TEXT, `activityName` TEXT, `ticket_count` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `states` (`state_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `state` TEXT, `code` TEXT, `order_number` INTEGER NOT NULL, PRIMARY KEY(`state_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `street_prefixes` (`prefix_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `street_prefix` TEXT, PRIMARY KEY(`prefix_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `street_suffixes` (`suffix_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `street_suffix` TEXT, PRIMARY KEY(`suffix_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `sync_data` (`sync_data_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userid` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `activity` TEXT, `table_name` TEXT, `primary_key` TEXT, `activity_source` TEXT, `sql_query` TEXT, `activity_date` INTEGER, `status` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ticket_comments` (`ticket_comment_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `ticket_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `comment_id` INTEGER NOT NULL, `comment` TEXT, `citation_number` INTEGER NOT NULL, `is_private` TEXT, `is_voice_comment` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ticket_pictures` (`s_no` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `picture_id` INTEGER NOT NULL, `ticket_id` INTEGER NOT NULL, `citation_number` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `picture_date` INTEGER, `mark_print` TEXT, `image_path` TEXT, `image_resolution` TEXT, `image_size` TEXT, `sync_status` TEXT, `download_image_url` TEXT, `image_name` TEXT, `isSelfi` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ticket_history` (`ticket_id` INTEGER NOT NULL, `ticket_date` INTEGER, `plate` TEXT, `vin` TEXT, `expiration` TEXT, `state_code` TEXT, `make_code` TEXT, `body_code` TEXT, `color_code` TEXT, `permit` TEXT, `meter` TEXT, `is_void` TEXT, `is_chalked` TEXT, `is_warn` TEXT, `is_driveaway` TEXT, `violation_code` TEXT, `violation` TEXT, PRIMARY KEY(`ticket_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ticket_violations` (`ticket_violation_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `violation_id` INTEGER NOT NULL, `ticket_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `violation_code` TEXT, `fine` REAL NOT NULL, `citation_number` INTEGER NOT NULL, `violation` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `tickets` (`ticket_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userid` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `device_id` INTEGER NOT NULL, `citation_number` INTEGER NOT NULL, `ticket_date` INTEGER, `state_id` INTEGER NOT NULL, `state_code` TEXT, `plate` TEXT, `vin` TEXT, `expiration` TEXT, `make_id` INTEGER NOT NULL, `make_code` TEXT, `body_id` INTEGER NOT NULL, `body_code` TEXT, `color_id` INTEGER NOT NULL, `color_code` TEXT, `street_prefix` TEXT, `street_suffix` TEXT, `street_number` TEXT, `location` TEXT, `direction` TEXT, `latitude` TEXT, `longitude` TEXT, `gpstime` INTEGER, `is_gps_location` TEXT, `is_void` TEXT, `is_chalked` TEXT, `is_warn` TEXT, `is_driveaway` TEXT, `void_id` INTEGER NOT NULL, `chalk_id` INTEGER NOT NULL, `permit` TEXT, `meter` TEXT, `fine` REAL NOT NULL, `time_marked` INTEGER, `space` TEXT, `violation_code` TEXT, `violation` TEXT, `void_reason_code` TEXT, `duty_id` INTEGER NOT NULL, `is_lpr` TEXT, `violation_id` INTEGER NOT NULL, `photo_count` INTEGER NOT NULL, `lpr_notification_id` TEXT, `status` TEXT, `placard` TEXT, `duty_report_id` TEXT, `app_version` TEXT, `chalk_time` TEXT, `chalk_last_seen` TEXT, `ticket_date_new` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `tpm_eula` (`rec_id` INTEGER NOT NULL, `EULA_text` TEXT, `Entry_date` TEXT, `Effective_date` TEXT, `cust_id` INTEGER NOT NULL, `module_id` INTEGER NOT NULL, `is_active` TEXT, PRIMARY KEY(`rec_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `user_settings` (`setting_id` INTEGER NOT NULL, `userid` INTEGER NOT NULL, `device_id` INTEGER NOT NULL, `auto_sync_interval` INTEGER NOT NULL, `data_retention_period` INTEGER NOT NULL, `gps` TEXT, `data_backup` TEXT, `user_prefs` TEXT, PRIMARY KEY(`setting_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`userid` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `username` TEXT, `password` TEXT, `user_type` TEXT, `first_name` TEXT, `last_name` TEXT, `badge` TEXT, `department` TEXT, `is_active` TEXT, `email_address` TEXT, `modules` TEXT, `title` TEXT, `print_name` TEXT, `rmsid` TEXT, PRIMARY KEY(`userid`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `vendors` (`vendor_id` INTEGER NOT NULL, `name` TEXT, `address` TEXT, `contact_number` TEXT, PRIMARY KEY(`vendor_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `vendor_services` (`service_id` INTEGER NOT NULL, `vendor_id` INTEGER NOT NULL, `service_name` TEXT, `test_url` TEXT, `prod_url` TEXT, `params` TEXT, `security_key` TEXT, PRIMARY KEY(`service_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `vendor_service_registrations` (`registration_id` INTEGER NOT NULL, `service_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `device_id` INTEGER NOT NULL, `userid` INTEGER NOT NULL, `param_mappings` TEXT, `service_mode` TEXT, `is_active` TEXT, PRIMARY KEY(`registration_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `vendor_items` (`item_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `vendor_id` INTEGER NOT NULL, `item_code` TEXT, `item_name` TEXT, `item_type` TEXT, `duration` INTEGER NOT NULL, `violation_id` INTEGER NOT NULL, `is_active` TEXT, `order_number` INTEGER NOT NULL, PRIMARY KEY(`item_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `violation_group_violations` (`violation_group_id` INTEGER NOT NULL, `group_id` INTEGER NOT NULL, `violation_id` INTEGER NOT NULL, PRIMARY KEY(`violation_group_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `violation_groups` (`group_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `group_code` TEXT, `group_name` TEXT, PRIMARY KEY(`group_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `violations` (`violation_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `violation` TEXT, `code` TEXT, `order_number` INTEGER NOT NULL, `base_fine` REAL NOT NULL, `fine1` REAL NOT NULL, `fine2` REAL NOT NULL, `repeat_offender` TEXT, `violation_display` TEXT, `default_comment` TEXT, `required_comments` INTEGER NOT NULL, `required_photos` INTEGER NOT NULL, `chalktimerequired` TEXT, `hide` TEXT, `repeat_offender_type` TEXT, `code_export` TEXT, PRIMARY KEY(`violation_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `void_ticket_reasons` (`reason_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `reason_title` TEXT, `reason_code` TEXT, `order_number` INTEGER NOT NULL, PRIMARY KEY(`reason_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `zones` (`zone_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `zone` TEXT, `time_diff` REAL NOT NULL, PRIMARY KEY(`zone_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `placard_temp` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `placard_no` TEXT, `placard_details` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `tickets_temp` (`ticket_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userid` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `device_id` INTEGER NOT NULL, `citation_number` INTEGER NOT NULL, `ticket_date` INTEGER, `state_id` INTEGER NOT NULL, `state_code` TEXT, `plate` TEXT, `vin` TEXT, `expiration` TEXT, `make_id` INTEGER NOT NULL, `make_code` TEXT, `body_id` INTEGER NOT NULL, `body_code` TEXT, `color_id` INTEGER NOT NULL, `color_code` TEXT, `street_prefix` TEXT, `street_suffix` TEXT, `street_number` TEXT, `location` TEXT, `direction` TEXT, `latitude` TEXT, `longitude` TEXT, `gpstime` INTEGER, `is_gps_location` TEXT, `is_void` TEXT, `is_chalked` TEXT, `is_warn` TEXT, `is_driveaway` TEXT, `void_id` INTEGER NOT NULL, `chalk_id` INTEGER NOT NULL, `permit` TEXT, `meter` TEXT, `fine` REAL NOT NULL, `time_marked` INTEGER, `space` TEXT, `violation_code` TEXT, `violation` TEXT, `void_reason_code` TEXT, `duty_id` INTEGER NOT NULL, `is_lpr` TEXT, `violation_id` INTEGER NOT NULL, `photo_count` INTEGER NOT NULL, `lpr_notification_id` TEXT, `status` TEXT, `placard` TEXT, `duty_report_id` TEXT, `app_version` TEXT)");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_tickets_temp_custid` ON `tickets_temp` (`custid`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ticket_violations_temp` (`ticket_violation_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `violation_id` INTEGER NOT NULL, `ticket_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `violation_code` TEXT, `fine` REAL NOT NULL, `citation_number` INTEGER NOT NULL, `violation` TEXT)");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_ticket_violations_temp_violation_code` ON `ticket_violations_temp` (`violation_code`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ticket_pictures_temp` (`s_no` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `picture_id` INTEGER NOT NULL, `ticket_id` INTEGER NOT NULL, `citation_number` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `picture_date` INTEGER, `mark_print` TEXT, `image_path` TEXT, `image_resolution` TEXT, `image_size` TEXT, `sync_status` TEXT, `download_image_url` TEXT, `image_name` TEXT, `isSelfi` INTEGER NOT NULL)");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_ticket_pictures_temp_picture_date` ON `ticket_pictures_temp` (`picture_date`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ticket_comments_temp` (`ticket_comment_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `ticket_id` INTEGER NOT NULL, `custid` INTEGER NOT NULL, `comment_id` INTEGER NOT NULL, `comment` TEXT, `citation_number` INTEGER NOT NULL, `is_private` TEXT, `is_voice_comment` INTEGER NOT NULL)");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_ticket_comments_temp_comment_id` ON `ticket_comments_temp` (`comment_id`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Genetec_Patrollers` (`record_Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `custId` INTEGER, `patroller_Id` TEXT, `vehicleName` TEXT, `is_active` TEXT, `createdOn` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Genetec_PatrollerActivities` (`record_Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `createdOn` TEXT, `custId` INTEGER, `patroller_Id` TEXT, `user_id` TEXT, `device_Id` INTEGER, `is_active` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c9c79c4390070148959475dfcc06ef99')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `ALPRPhotoChalk`");
        _db.execSQL("DROP TABLE IF EXISTS `FT_DeviceHistory`");
        _db.execSQL("DROP TABLE IF EXISTS `bodies`");
        _db.execSQL("DROP TABLE IF EXISTS `lprbodymap`");
        _db.execSQL("DROP TABLE IF EXISTS `call_in_list`");
        _db.execSQL("DROP TABLE IF EXISTS `call_in_reports`");
        _db.execSQL("DROP TABLE IF EXISTS `chalk_comments`");
        _db.execSQL("DROP TABLE IF EXISTS `chalk_pictures`");
        _db.execSQL("DROP TABLE IF EXISTS `chalk_vehicles`");
        _db.execSQL("DROP TABLE IF EXISTS `colors`");
        _db.execSQL("DROP TABLE IF EXISTS `comment_group_comments`");
        _db.execSQL("DROP TABLE IF EXISTS `comment_groups`");
        _db.execSQL("DROP TABLE IF EXISTS `comments`");
        _db.execSQL("DROP TABLE IF EXISTS `contacts`");
        _db.execSQL("DROP TABLE IF EXISTS `customer_modules`");
        _db.execSQL("DROP TABLE IF EXISTS `customers`");
        _db.execSQL("DROP TABLE IF EXISTS `device_groups`");
        _db.execSQL("DROP TABLE IF EXISTS `devices`");
        _db.execSQL("DROP TABLE IF EXISTS `directions`");
        _db.execSQL("DROP TABLE IF EXISTS `durations`");
        _db.execSQL("DROP TABLE IF EXISTS `duties`");
        _db.execSQL("DROP TABLE IF EXISTS `duty_reports`");
        _db.execSQL("DROP TABLE IF EXISTS `error_logs`");
        _db.execSQL("DROP TABLE IF EXISTS `features`");
        _db.execSQL("DROP TABLE IF EXISTS `gps_locations`");
        _db.execSQL("DROP TABLE IF EXISTS `hotlist`");
        _db.execSQL("DROP TABLE IF EXISTS `location_group_locations`");
        _db.execSQL("DROP TABLE IF EXISTS `location_groups`");
        _db.execSQL("DROP TABLE IF EXISTS `locations`");
        _db.execSQL("DROP TABLE IF EXISTS `lpr_notifications`");
        _db.execSQL("DROP TABLE IF EXISTS `maintenance_logs`");
        _db.execSQL("DROP TABLE IF EXISTS `maintenance_pictures`");
        _db.execSQL("DROP TABLE IF EXISTS `makes_and_models`");
        _db.execSQL("DROP TABLE IF EXISTS `messages`");
        _db.execSQL("DROP TABLE IF EXISTS `meters`");
        _db.execSQL("DROP TABLE IF EXISTS `mobile_now_logs`");
        _db.execSQL("DROP TABLE IF EXISTS `modules`");
        _db.execSQL("DROP TABLE IF EXISTS `permits`");
        _db.execSQL("DROP TABLE IF EXISTS `print_macros`");
        _db.execSQL("DROP TABLE IF EXISTS `print_templates`");
        _db.execSQL("DROP TABLE IF EXISTS `repeat_offenders`");
        _db.execSQL("DROP TABLE IF EXISTS `special_activities`");
        _db.execSQL("DROP TABLE IF EXISTS `special_activities_location`");
        _db.execSQL("DROP TABLE IF EXISTS `special_activities_pictures`");
        _db.execSQL("DROP TABLE IF EXISTS `special_activity_dispositions`");
        _db.execSQL("DROP TABLE IF EXISTS `special_activity_reports`");
        _db.execSQL("DROP TABLE IF EXISTS `states`");
        _db.execSQL("DROP TABLE IF EXISTS `street_prefixes`");
        _db.execSQL("DROP TABLE IF EXISTS `street_suffixes`");
        _db.execSQL("DROP TABLE IF EXISTS `sync_data`");
        _db.execSQL("DROP TABLE IF EXISTS `ticket_comments`");
        _db.execSQL("DROP TABLE IF EXISTS `ticket_pictures`");
        _db.execSQL("DROP TABLE IF EXISTS `ticket_history`");
        _db.execSQL("DROP TABLE IF EXISTS `ticket_violations`");
        _db.execSQL("DROP TABLE IF EXISTS `tickets`");
        _db.execSQL("DROP TABLE IF EXISTS `tpm_eula`");
        _db.execSQL("DROP TABLE IF EXISTS `user_settings`");
        _db.execSQL("DROP TABLE IF EXISTS `users`");
        _db.execSQL("DROP TABLE IF EXISTS `vendors`");
        _db.execSQL("DROP TABLE IF EXISTS `vendor_services`");
        _db.execSQL("DROP TABLE IF EXISTS `vendor_service_registrations`");
        _db.execSQL("DROP TABLE IF EXISTS `vendor_items`");
        _db.execSQL("DROP TABLE IF EXISTS `violation_group_violations`");
        _db.execSQL("DROP TABLE IF EXISTS `violation_groups`");
        _db.execSQL("DROP TABLE IF EXISTS `violations`");
        _db.execSQL("DROP TABLE IF EXISTS `void_ticket_reasons`");
        _db.execSQL("DROP TABLE IF EXISTS `zones`");
        _db.execSQL("DROP TABLE IF EXISTS `placard_temp`");
        _db.execSQL("DROP TABLE IF EXISTS `tickets_temp`");
        _db.execSQL("DROP TABLE IF EXISTS `ticket_violations_temp`");
        _db.execSQL("DROP TABLE IF EXISTS `ticket_pictures_temp`");
        _db.execSQL("DROP TABLE IF EXISTS `ticket_comments_temp`");
        _db.execSQL("DROP TABLE IF EXISTS `Genetec_Patrollers`");
        _db.execSQL("DROP TABLE IF EXISTS `Genetec_PatrollerActivities`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsALPRPhotoChalk = new HashMap<String, TableInfo.Column>(31);
        _columnsALPRPhotoChalk.put("Plate", new TableInfo.Column("Plate", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("Details", new TableInfo.Column("Details", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("DataField1", new TableInfo.Column("DataField1", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("DataField2", new TableInfo.Column("DataField2", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("DataField3", new TableInfo.Column("DataField3", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("Confidence", new TableInfo.Column("Confidence", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("FirstDate", new TableInfo.Column("FirstDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("FirstTime", new TableInfo.Column("FirstTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("FirstDateTime", new TableInfo.Column("FirstDateTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("FirstParkingBay", new TableInfo.Column("FirstParkingBay", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("FirstLocLat", new TableInfo.Column("FirstLocLat", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("FirstLocLong", new TableInfo.Column("FirstLocLong", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("FirstLocAcc", new TableInfo.Column("FirstLocAcc", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("LastDate", new TableInfo.Column("LastDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("LastTime", new TableInfo.Column("LastTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("LastDateTime", new TableInfo.Column("LastDateTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("LastParkingBay", new TableInfo.Column("LastParkingBay", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("LastLocLat", new TableInfo.Column("LastLocLat", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("LastLocLong", new TableInfo.Column("LastLocLong", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("LastLocAcc", new TableInfo.Column("LastLocAcc", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("PermitExpiryDate", new TableInfo.Column("PermitExpiryDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("PermitExpiryTime", new TableInfo.Column("PermitExpiryTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("chalkDuration", new TableInfo.Column("chalkDuration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("duration_code", new TableInfo.Column("duration_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("chalkLocation", new TableInfo.Column("chalkLocation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("chalkTire", new TableInfo.Column("chalkTire", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("chalkId", new TableInfo.Column("chalkId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("userid", new TableInfo.Column("userid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("deviceId", new TableInfo.Column("deviceId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("custId", new TableInfo.Column("custId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsALPRPhotoChalk.put("is_expired", new TableInfo.Column("is_expired", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysALPRPhotoChalk = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesALPRPhotoChalk = new HashSet<TableInfo.Index>(1);
        _indicesALPRPhotoChalk.add(new TableInfo.Index("index_ALPRPhotoChalk_Plate", true, Arrays.asList("Plate")));
        final TableInfo _infoALPRPhotoChalk = new TableInfo("ALPRPhotoChalk", _columnsALPRPhotoChalk, _foreignKeysALPRPhotoChalk, _indicesALPRPhotoChalk);
        final TableInfo _existingALPRPhotoChalk = TableInfo.read(_db, "ALPRPhotoChalk");
        if (! _infoALPRPhotoChalk.equals(_existingALPRPhotoChalk)) {
          return new RoomOpenHelper.ValidationResult(false, "ALPRPhotoChalk(com.ticketpro.model.ALPRChalk).\n"
                  + " Expected:\n" + _infoALPRPhotoChalk + "\n"
                  + " Found:\n" + _existingALPRPhotoChalk);
        }
        final HashMap<String, TableInfo.Column> _columnsFTDeviceHistory = new HashMap<String, TableInfo.Column>(30);
        _columnsFTDeviceHistory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("dutyId", new TableInfo.Column("dutyId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("dutyName", new TableInfo.Column("dutyName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("custId", new TableInfo.Column("custId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("deviceId", new TableInfo.Column("deviceId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("deviceName", new TableInfo.Column("deviceName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("latitude", new TableInfo.Column("latitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("longitude", new TableInfo.Column("longitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("fullName", new TableInfo.Column("fullName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("device", new TableInfo.Column("device", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("timeStamp", new TableInfo.Column("timeStamp", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("badge", new TableInfo.Column("badge", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("userId", new TableInfo.Column("userId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("firstLogin", new TableInfo.Column("firstLogin", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("lastTicketTimeStamp", new TableInfo.Column("lastTicketTimeStamp", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("currTimeStamp", new TableInfo.Column("currTimeStamp", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("pushToken", new TableInfo.Column("pushToken", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("moduleId", new TableInfo.Column("moduleId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("deviceInactivity", new TableInfo.Column("deviceInactivity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("isLoggedIn", new TableInfo.Column("isLoggedIn", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("appVersion", new TableInfo.Column("appVersion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("activityName", new TableInfo.Column("activityName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("sync_status", new TableInfo.Column("sync_status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("altitude", new TableInfo.Column("altitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("Violation", new TableInfo.Column("Violation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("Citation", new TableInfo.Column("Citation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFTDeviceHistory.put("accuracy", new TableInfo.Column("accuracy", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFTDeviceHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFTDeviceHistory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFTDeviceHistory = new TableInfo("FT_DeviceHistory", _columnsFTDeviceHistory, _foreignKeysFTDeviceHistory, _indicesFTDeviceHistory);
        final TableInfo _existingFTDeviceHistory = TableInfo.read(_db, "FT_DeviceHistory");
        if (! _infoFTDeviceHistory.equals(_existingFTDeviceHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "FT_DeviceHistory(com.ticketpro.model.DeviceData).\n"
                  + " Expected:\n" + _infoFTDeviceHistory + "\n"
                  + " Found:\n" + _existingFTDeviceHistory);
        }
        final HashMap<String, TableInfo.Column> _columnsBodies = new HashMap<String, TableInfo.Column>(5);
        _columnsBodies.put("body_id", new TableInfo.Column("body_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodies.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodies.put("body", new TableInfo.Column("body", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodies.put("code", new TableInfo.Column("code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBodies.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBodies = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBodies = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBodies = new TableInfo("bodies", _columnsBodies, _foreignKeysBodies, _indicesBodies);
        final TableInfo _existingBodies = TableInfo.read(_db, "bodies");
        if (! _infoBodies.equals(_existingBodies)) {
          return new RoomOpenHelper.ValidationResult(false, "bodies(com.ticketpro.model.Body).\n"
                  + " Expected:\n" + _infoBodies + "\n"
                  + " Found:\n" + _existingBodies);
        }
        final HashMap<String, TableInfo.Column> _columnsLprbodymap = new HashMap<String, TableInfo.Column>(5);
        _columnsLprbodymap.put("body_id", new TableInfo.Column("body_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprbodymap.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprbodymap.put("LPRBody", new TableInfo.Column("LPRBody", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprbodymap.put("TicketBody", new TableInfo.Column("TicketBody", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprbodymap.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLprbodymap = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLprbodymap = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLprbodymap = new TableInfo("lprbodymap", _columnsLprbodymap, _foreignKeysLprbodymap, _indicesLprbodymap);
        final TableInfo _existingLprbodymap = TableInfo.read(_db, "lprbodymap");
        if (! _infoLprbodymap.equals(_existingLprbodymap)) {
          return new RoomOpenHelper.ValidationResult(false, "lprbodymap(com.ticketpro.model.LprBodyMap).\n"
                  + " Expected:\n" + _infoLprbodymap + "\n"
                  + " Found:\n" + _existingLprbodymap);
        }
        final HashMap<String, TableInfo.Column> _columnsCallInList = new HashMap<String, TableInfo.Column>(5);
        _columnsCallInList.put("call_in_id", new TableInfo.Column("call_in_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallInList.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallInList.put("action_item", new TableInfo.Column("action_item", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallInList.put("action_code", new TableInfo.Column("action_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallInList.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCallInList = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCallInList = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCallInList = new TableInfo("call_in_list", _columnsCallInList, _foreignKeysCallInList, _indicesCallInList);
        final TableInfo _existingCallInList = TableInfo.read(_db, "call_in_list");
        if (! _infoCallInList.equals(_existingCallInList)) {
          return new RoomOpenHelper.ValidationResult(false, "call_in_list(com.ticketpro.model.CallInList).\n"
                  + " Expected:\n" + _infoCallInList + "\n"
                  + " Found:\n" + _existingCallInList);
        }
        final HashMap<String, TableInfo.Column> _columnsCallInReports = new HashMap<String, TableInfo.Column>(13);
        _columnsCallInReports.put("report_id", new TableInfo.Column("report_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallInReports.put("userid", new TableInfo.Column("userid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallInReports.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallInReports.put("call_in_date", new TableInfo.Column("call_in_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallInReports.put("plate", new TableInfo.Column("plate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallInReports.put("plate_type", new TableInfo.Column("plate_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallInReports.put("state", new TableInfo.Column("state", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallInReports.put("from_search", new TableInfo.Column("from_search", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallInReports.put("from_hit", new TableInfo.Column("from_hit", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallInReports.put("action_taken", new TableInfo.Column("action_taken", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallInReports.put("photo1", new TableInfo.Column("photo1", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallInReports.put("photo2", new TableInfo.Column("photo2", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallInReports.put("photo3", new TableInfo.Column("photo3", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCallInReports = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCallInReports = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCallInReports = new TableInfo("call_in_reports", _columnsCallInReports, _foreignKeysCallInReports, _indicesCallInReports);
        final TableInfo _existingCallInReports = TableInfo.read(_db, "call_in_reports");
        if (! _infoCallInReports.equals(_existingCallInReports)) {
          return new RoomOpenHelper.ValidationResult(false, "call_in_reports(com.ticketpro.model.CallInReport).\n"
                  + " Expected:\n" + _infoCallInReports + "\n"
                  + " Found:\n" + _existingCallInReports);
        }
        final HashMap<String, TableInfo.Column> _columnsChalkComments = new HashMap<String, TableInfo.Column>(6);
        _columnsChalkComments.put("chalk_comment_id", new TableInfo.Column("chalk_comment_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkComments.put("chalk_id", new TableInfo.Column("chalk_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkComments.put("comment_id", new TableInfo.Column("comment_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkComments.put("comment", new TableInfo.Column("comment", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkComments.put("is_private", new TableInfo.Column("is_private", "TEXT", false, 0, "'N'", TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkComments.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChalkComments = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesChalkComments = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoChalkComments = new TableInfo("chalk_comments", _columnsChalkComments, _foreignKeysChalkComments, _indicesChalkComments);
        final TableInfo _existingChalkComments = TableInfo.read(_db, "chalk_comments");
        if (! _infoChalkComments.equals(_existingChalkComments)) {
          return new RoomOpenHelper.ValidationResult(false, "chalk_comments(com.ticketpro.model.ChalkComment).\n"
                  + " Expected:\n" + _infoChalkComments + "\n"
                  + " Found:\n" + _existingChalkComments);
        }
        final HashMap<String, TableInfo.Column> _columnsChalkPictures = new HashMap<String, TableInfo.Column>(12);
        _columnsChalkPictures.put("picture_id", new TableInfo.Column("picture_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkPictures.put("chalk_id", new TableInfo.Column("chalk_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkPictures.put("chalk_time", new TableInfo.Column("chalk_time", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkPictures.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkPictures.put("latitute", new TableInfo.Column("latitute", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkPictures.put("longitude", new TableInfo.Column("longitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkPictures.put("image_path", new TableInfo.Column("image_path", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkPictures.put("image_resolution", new TableInfo.Column("image_resolution", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkPictures.put("image_size", new TableInfo.Column("image_size", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkPictures.put("sync_status", new TableInfo.Column("sync_status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkPictures.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkPictures.put("download_image", new TableInfo.Column("download_image", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChalkPictures = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesChalkPictures = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoChalkPictures = new TableInfo("chalk_pictures", _columnsChalkPictures, _foreignKeysChalkPictures, _indicesChalkPictures);
        final TableInfo _existingChalkPictures = TableInfo.read(_db, "chalk_pictures");
        if (! _infoChalkPictures.equals(_existingChalkPictures)) {
          return new RoomOpenHelper.ValidationResult(false, "chalk_pictures(com.ticketpro.model.ChalkPicture).\n"
                  + " Expected:\n" + _infoChalkPictures + "\n"
                  + " Found:\n" + _existingChalkPictures);
        }
        final HashMap<String, TableInfo.Column> _columnsChalkVehicles = new HashMap<String, TableInfo.Column>(38);
        _columnsChalkVehicles.put("chalk_id", new TableInfo.Column("chalk_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("userid", new TableInfo.Column("userid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("device_id", new TableInfo.Column("device_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("chalk_date", new TableInfo.Column("chalk_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("permit", new TableInfo.Column("permit", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("plate", new TableInfo.Column("plate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("vin", new TableInfo.Column("vin", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("state_id", new TableInfo.Column("state_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("zone_id", new TableInfo.Column("zone_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("expiration", new TableInfo.Column("expiration", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("duration_id", new TableInfo.Column("duration_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("duration_code", new TableInfo.Column("duration_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("space", new TableInfo.Column("space", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("meter", new TableInfo.Column("meter", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("tire", new TableInfo.Column("tire", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("chalkx", new TableInfo.Column("chalkx", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("chalky", new TableInfo.Column("chalky", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("stemx", new TableInfo.Column("stemx", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("stemy", new TableInfo.Column("stemy", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("chalk_type", new TableInfo.Column("chalk_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("street_prefix", new TableInfo.Column("street_prefix", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("street_suffix", new TableInfo.Column("street_suffix", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("street_number", new TableInfo.Column("street_number", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("direction", new TableInfo.Column("direction", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("latitude", new TableInfo.Column("latitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("longitude", new TableInfo.Column("longitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("gpstime", new TableInfo.Column("gpstime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("is_gps_location", new TableInfo.Column("is_gps_location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("is_expired", new TableInfo.Column("is_expired", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("wheel_size", new TableInfo.Column("wheel_size", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("make_code", new TableInfo.Column("make_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("color_code", new TableInfo.Column("color_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("make", new TableInfo.Column("make", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("color", new TableInfo.Column("color", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChalkVehicles.put("sync_status", new TableInfo.Column("sync_status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChalkVehicles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesChalkVehicles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoChalkVehicles = new TableInfo("chalk_vehicles", _columnsChalkVehicles, _foreignKeysChalkVehicles, _indicesChalkVehicles);
        final TableInfo _existingChalkVehicles = TableInfo.read(_db, "chalk_vehicles");
        if (! _infoChalkVehicles.equals(_existingChalkVehicles)) {
          return new RoomOpenHelper.ValidationResult(false, "chalk_vehicles(com.ticketpro.model.ChalkVehicle).\n"
                  + " Expected:\n" + _infoChalkVehicles + "\n"
                  + " Found:\n" + _existingChalkVehicles);
        }
        final HashMap<String, TableInfo.Column> _columnsColors = new HashMap<String, TableInfo.Column>(5);
        _columnsColors.put("color_id", new TableInfo.Column("color_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsColors.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsColors.put("color", new TableInfo.Column("color", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsColors.put("code", new TableInfo.Column("code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsColors.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysColors = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesColors = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoColors = new TableInfo("colors", _columnsColors, _foreignKeysColors, _indicesColors);
        final TableInfo _existingColors = TableInfo.read(_db, "colors");
        if (! _infoColors.equals(_existingColors)) {
          return new RoomOpenHelper.ValidationResult(false, "colors(com.ticketpro.model.Color).\n"
                  + " Expected:\n" + _infoColors + "\n"
                  + " Found:\n" + _existingColors);
        }
        final HashMap<String, TableInfo.Column> _columnsCommentGroupComments = new HashMap<String, TableInfo.Column>(3);
        _columnsCommentGroupComments.put("comment_group_id", new TableInfo.Column("comment_group_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommentGroupComments.put("group_id", new TableInfo.Column("group_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommentGroupComments.put("comment_id", new TableInfo.Column("comment_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCommentGroupComments = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCommentGroupComments = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCommentGroupComments = new TableInfo("comment_group_comments", _columnsCommentGroupComments, _foreignKeysCommentGroupComments, _indicesCommentGroupComments);
        final TableInfo _existingCommentGroupComments = TableInfo.read(_db, "comment_group_comments");
        if (! _infoCommentGroupComments.equals(_existingCommentGroupComments)) {
          return new RoomOpenHelper.ValidationResult(false, "comment_group_comments(com.ticketpro.model.CommentGroupComment).\n"
                  + " Expected:\n" + _infoCommentGroupComments + "\n"
                  + " Found:\n" + _existingCommentGroupComments);
        }
        final HashMap<String, TableInfo.Column> _columnsCommentGroups = new HashMap<String, TableInfo.Column>(4);
        _columnsCommentGroups.put("group_id", new TableInfo.Column("group_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommentGroups.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommentGroups.put("group_code", new TableInfo.Column("group_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommentGroups.put("group_name", new TableInfo.Column("group_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCommentGroups = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCommentGroups = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCommentGroups = new TableInfo("comment_groups", _columnsCommentGroups, _foreignKeysCommentGroups, _indicesCommentGroups);
        final TableInfo _existingCommentGroups = TableInfo.read(_db, "comment_groups");
        if (! _infoCommentGroups.equals(_existingCommentGroups)) {
          return new RoomOpenHelper.ValidationResult(false, "comment_groups(com.ticketpro.model.CommentGroup).\n"
                  + " Expected:\n" + _infoCommentGroups + "\n"
                  + " Found:\n" + _existingCommentGroups);
        }
        final HashMap<String, TableInfo.Column> _columnsComments = new HashMap<String, TableInfo.Column>(5);
        _columnsComments.put("comment_id", new TableInfo.Column("comment_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("comment", new TableInfo.Column("comment", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("code", new TableInfo.Column("code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysComments = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesComments = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoComments = new TableInfo("comments", _columnsComments, _foreignKeysComments, _indicesComments);
        final TableInfo _existingComments = TableInfo.read(_db, "comments");
        if (! _infoComments.equals(_existingComments)) {
          return new RoomOpenHelper.ValidationResult(false, "comments(com.ticketpro.model.Comment).\n"
                  + " Expected:\n" + _infoComments + "\n"
                  + " Found:\n" + _existingComments);
        }
        final HashMap<String, TableInfo.Column> _columnsContacts = new HashMap<String, TableInfo.Column>(6);
        _columnsContacts.put("contact_id", new TableInfo.Column("contact_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContacts.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContacts.put("email_id", new TableInfo.Column("email_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContacts.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContacts.put("contact_type", new TableInfo.Column("contact_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsContacts.put("is_active", new TableInfo.Column("is_active", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysContacts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesContacts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoContacts = new TableInfo("contacts", _columnsContacts, _foreignKeysContacts, _indicesContacts);
        final TableInfo _existingContacts = TableInfo.read(_db, "contacts");
        if (! _infoContacts.equals(_existingContacts)) {
          return new RoomOpenHelper.ValidationResult(false, "contacts(com.ticketpro.model.Contact).\n"
                  + " Expected:\n" + _infoContacts + "\n"
                  + " Found:\n" + _existingContacts);
        }
        final HashMap<String, TableInfo.Column> _columnsCustomerModules = new HashMap<String, TableInfo.Column>(5);
        _columnsCustomerModules.put("customer_module_id", new TableInfo.Column("customer_module_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomerModules.put("module_id", new TableInfo.Column("module_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomerModules.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomerModules.put("userid", new TableInfo.Column("userid", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomerModules.put("is_active", new TableInfo.Column("is_active", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCustomerModules = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCustomerModules = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCustomerModules = new TableInfo("customer_modules", _columnsCustomerModules, _foreignKeysCustomerModules, _indicesCustomerModules);
        final TableInfo _existingCustomerModules = TableInfo.read(_db, "customer_modules");
        if (! _infoCustomerModules.equals(_existingCustomerModules)) {
          return new RoomOpenHelper.ValidationResult(false, "customer_modules(com.ticketpro.model.CustomerModule).\n"
                  + " Expected:\n" + _infoCustomerModules + "\n"
                  + " Found:\n" + _existingCustomerModules);
        }
        final HashMap<String, TableInfo.Column> _columnsCustomers = new HashMap<String, TableInfo.Column>(12);
        _columnsCustomers.put("custid", new TableInfo.Column("custid", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("contact_number", new TableInfo.Column("contact_number", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("logo_image", new TableInfo.Column("logo_image", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("agency_code", new TableInfo.Column("agency_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("web_address", new TableInfo.Column("web_address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("content_folder", new TableInfo.Column("content_folder", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("ticket_color", new TableInfo.Column("ticket_color", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("ticket_back", new TableInfo.Column("ticket_back", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("TRCourtCode", new TableInfo.Column("TRCourtCode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("TRPrintAgencyName", new TableInfo.Column("TRPrintAgencyName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCustomers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCustomers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCustomers = new TableInfo("customers", _columnsCustomers, _foreignKeysCustomers, _indicesCustomers);
        final TableInfo _existingCustomers = TableInfo.read(_db, "customers");
        if (! _infoCustomers.equals(_existingCustomers)) {
          return new RoomOpenHelper.ValidationResult(false, "customers(com.ticketpro.model.CustomerInfo).\n"
                  + " Expected:\n" + _infoCustomers + "\n"
                  + " Found:\n" + _existingCustomers);
        }
        final HashMap<String, TableInfo.Column> _columnsDeviceGroups = new HashMap<String, TableInfo.Column>(4);
        _columnsDeviceGroups.put("group_id", new TableInfo.Column("group_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceGroups.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceGroups.put("group_name", new TableInfo.Column("group_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceGroups.put("device_ids", new TableInfo.Column("device_ids", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDeviceGroups = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDeviceGroups = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDeviceGroups = new TableInfo("device_groups", _columnsDeviceGroups, _foreignKeysDeviceGroups, _indicesDeviceGroups);
        final TableInfo _existingDeviceGroups = TableInfo.read(_db, "device_groups");
        if (! _infoDeviceGroups.equals(_existingDeviceGroups)) {
          return new RoomOpenHelper.ValidationResult(false, "device_groups(com.ticketpro.model.DeviceGroup).\n"
                  + " Expected:\n" + _infoDeviceGroups + "\n"
                  + " Found:\n" + _existingDeviceGroups);
        }
        final HashMap<String, TableInfo.Column> _columnsDevices = new HashMap<String, TableInfo.Column>(21);
        _columnsDevices.put("device_id", new TableInfo.Column("device_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("device_name", new TableInfo.Column("device_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("device", new TableInfo.Column("device", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("app_version", new TableInfo.Column("app_version", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("os_version", new TableInfo.Column("os_version", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("platform", new TableInfo.Column("platform", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("last_sync", new TableInfo.Column("last_sync", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("lastTicketTime", new TableInfo.Column("lastTicketTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("start_citation_number", new TableInfo.Column("start_citation_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("current_citation_number", new TableInfo.Column("current_citation_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("end_citation_number", new TableInfo.Column("end_citation_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("start_warning_number", new TableInfo.Column("start_warning_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("current_warning_number", new TableInfo.Column("current_warning_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("end_warning_number", new TableInfo.Column("end_warning_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("start_photo_number", new TableInfo.Column("start_photo_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("current_photo_number", new TableInfo.Column("current_photo_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("end_photo_number", new TableInfo.Column("end_photo_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("default_template_id", new TableInfo.Column("default_template_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("gcm_registration_id", new TableInfo.Column("gcm_registration_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("default_printer_name", new TableInfo.Column("default_printer_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDevices = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDevices = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDevices = new TableInfo("devices", _columnsDevices, _foreignKeysDevices, _indicesDevices);
        final TableInfo _existingDevices = TableInfo.read(_db, "devices");
        if (! _infoDevices.equals(_existingDevices)) {
          return new RoomOpenHelper.ValidationResult(false, "devices(com.ticketpro.model.DeviceInfo).\n"
                  + " Expected:\n" + _infoDevices + "\n"
                  + " Found:\n" + _existingDevices);
        }
        final HashMap<String, TableInfo.Column> _columnsDirections = new HashMap<String, TableInfo.Column>(5);
        _columnsDirections.put("direction_id", new TableInfo.Column("direction_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDirections.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDirections.put("direction", new TableInfo.Column("direction", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDirections.put("code", new TableInfo.Column("code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDirections.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDirections = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDirections = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDirections = new TableInfo("directions", _columnsDirections, _foreignKeysDirections, _indicesDirections);
        final TableInfo _existingDirections = TableInfo.read(_db, "directions");
        if (! _infoDirections.equals(_existingDirections)) {
          return new RoomOpenHelper.ValidationResult(false, "directions(com.ticketpro.model.Direction).\n"
                  + " Expected:\n" + _infoDirections + "\n"
                  + " Found:\n" + _existingDirections);
        }
        final HashMap<String, TableInfo.Column> _columnsDurations = new HashMap<String, TableInfo.Column>(7);
        _columnsDurations.put("duration_id", new TableInfo.Column("duration_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDurations.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDurations.put("duration", new TableInfo.Column("duration", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDurations.put("duration_mins", new TableInfo.Column("duration_mins", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDurations.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDurations.put("default_violation_id", new TableInfo.Column("default_violation_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDurations.put("auto_delete", new TableInfo.Column("auto_delete", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDurations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDurations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDurations = new TableInfo("durations", _columnsDurations, _foreignKeysDurations, _indicesDurations);
        final TableInfo _existingDurations = TableInfo.read(_db, "durations");
        if (! _infoDurations.equals(_existingDurations)) {
          return new RoomOpenHelper.ValidationResult(false, "durations(com.ticketpro.model.Duration).\n"
                  + " Expected:\n" + _infoDurations + "\n"
                  + " Found:\n" + _existingDurations);
        }
        final HashMap<String, TableInfo.Column> _columnsDuties = new HashMap<String, TableInfo.Column>(9);
        _columnsDuties.put("duty_id", new TableInfo.Column("duty_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDuties.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDuties.put("duty", new TableInfo.Column("duty", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDuties.put("code", new TableInfo.Column("code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDuties.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDuties.put("allow_ticket", new TableInfo.Column("allow_ticket", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDuties.put("location_groups", new TableInfo.Column("location_groups", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDuties.put("comment_groups", new TableInfo.Column("comment_groups", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDuties.put("violation_groups", new TableInfo.Column("violation_groups", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDuties = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDuties = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDuties = new TableInfo("duties", _columnsDuties, _foreignKeysDuties, _indicesDuties);
        final TableInfo _existingDuties = TableInfo.read(_db, "duties");
        if (! _infoDuties.equals(_existingDuties)) {
          return new RoomOpenHelper.ValidationResult(false, "duties(com.ticketpro.model.Duty).\n"
                  + " Expected:\n" + _infoDuties + "\n"
                  + " Found:\n" + _existingDuties);
        }
        final HashMap<String, TableInfo.Column> _columnsDutyReports = new HashMap<String, TableInfo.Column>(10);
        _columnsDutyReports.put("report_id", new TableInfo.Column("report_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDutyReports.put("userid", new TableInfo.Column("userid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDutyReports.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDutyReports.put("duty_id", new TableInfo.Column("duty_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDutyReports.put("date_in", new TableInfo.Column("date_in", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDutyReports.put("date_out", new TableInfo.Column("date_out", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDutyReports.put("signature", new TableInfo.Column("signature", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDutyReports.put("device_id", new TableInfo.Column("device_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDutyReports.put("duty_report_id", new TableInfo.Column("duty_report_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDutyReports.put("sync_status", new TableInfo.Column("sync_status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDutyReports = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDutyReports = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDutyReports = new TableInfo("duty_reports", _columnsDutyReports, _foreignKeysDutyReports, _indicesDutyReports);
        final TableInfo _existingDutyReports = TableInfo.read(_db, "duty_reports");
        if (! _infoDutyReports.equals(_existingDutyReports)) {
          return new RoomOpenHelper.ValidationResult(false, "duty_reports(com.ticketpro.model.DutyReport).\n"
                  + " Expected:\n" + _infoDutyReports + "\n"
                  + " Found:\n" + _existingDutyReports);
        }
        final HashMap<String, TableInfo.Column> _columnsErrorLogs = new HashMap<String, TableInfo.Column>(11);
        _columnsErrorLogs.put("error_id", new TableInfo.Column("error_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsErrorLogs.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsErrorLogs.put("userid", new TableInfo.Column("userid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsErrorLogs.put("device_id", new TableInfo.Column("device_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsErrorLogs.put("error_type", new TableInfo.Column("error_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsErrorLogs.put("error_desc", new TableInfo.Column("error_desc", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsErrorLogs.put("error_date", new TableInfo.Column("error_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsErrorLogs.put("date", new TableInfo.Column("date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsErrorLogs.put("module", new TableInfo.Column("module", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsErrorLogs.put("app_version", new TableInfo.Column("app_version", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsErrorLogs.put("device", new TableInfo.Column("device", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysErrorLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesErrorLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoErrorLogs = new TableInfo("error_logs", _columnsErrorLogs, _foreignKeysErrorLogs, _indicesErrorLogs);
        final TableInfo _existingErrorLogs = TableInfo.read(_db, "error_logs");
        if (! _infoErrorLogs.equals(_existingErrorLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "error_logs(com.ticketpro.model.ErrorLog).\n"
                  + " Expected:\n" + _infoErrorLogs + "\n"
                  + " Found:\n" + _existingErrorLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsFeatures = new HashMap<String, TableInfo.Column>(7);
        _columnsFeatures.put("feature_id", new TableInfo.Column("feature_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeatures.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeatures.put("feature", new TableInfo.Column("feature", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeatures.put("admin", new TableInfo.Column("admin", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeatures.put("officer", new TableInfo.Column("officer", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeatures.put("value", new TableInfo.Column("value", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeatures.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFeatures = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFeatures = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFeatures = new TableInfo("features", _columnsFeatures, _foreignKeysFeatures, _indicesFeatures);
        final TableInfo _existingFeatures = TableInfo.read(_db, "features");
        if (! _infoFeatures.equals(_existingFeatures)) {
          return new RoomOpenHelper.ValidationResult(false, "features(com.ticketpro.model.Feature).\n"
                  + " Expected:\n" + _infoFeatures + "\n"
                  + " Found:\n" + _existingFeatures);
        }
        final HashMap<String, TableInfo.Column> _columnsGpsLocations = new HashMap<String, TableInfo.Column>(9);
        _columnsGpsLocations.put("location_id", new TableInfo.Column("location_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGpsLocations.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGpsLocations.put("latitude", new TableInfo.Column("latitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGpsLocations.put("longitude", new TableInfo.Column("longitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGpsLocations.put("gpstime", new TableInfo.Column("gpstime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGpsLocations.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGpsLocations.put("street_number", new TableInfo.Column("street_number", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGpsLocations.put("street_prefix", new TableInfo.Column("street_prefix", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGpsLocations.put("street_suffix", new TableInfo.Column("street_suffix", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGpsLocations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGpsLocations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGpsLocations = new TableInfo("gps_locations", _columnsGpsLocations, _foreignKeysGpsLocations, _indicesGpsLocations);
        final TableInfo _existingGpsLocations = TableInfo.read(_db, "gps_locations");
        if (! _infoGpsLocations.equals(_existingGpsLocations)) {
          return new RoomOpenHelper.ValidationResult(false, "gps_locations(com.ticketpro.model.GPSLocation).\n"
                  + " Expected:\n" + _infoGpsLocations + "\n"
                  + " Found:\n" + _existingGpsLocations);
        }
        final HashMap<String, TableInfo.Column> _columnsHotlist = new HashMap<String, TableInfo.Column>(12);
        _columnsHotlist.put("hotlist_id", new TableInfo.Column("hotlist_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotlist.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotlist.put("state_code", new TableInfo.Column("state_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotlist.put("plate", new TableInfo.Column("plate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotlist.put("plate_type", new TableInfo.Column("plate_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotlist.put("begin_date", new TableInfo.Column("begin_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotlist.put("end_date", new TableInfo.Column("end_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotlist.put("comments", new TableInfo.Column("comments", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotlist.put("number_of_tickets", new TableInfo.Column("number_of_tickets", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotlist.put("amount_owed", new TableInfo.Column("amount_owed", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotlist.put("date_added", new TableInfo.Column("date_added", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotlist.put("is_active", new TableInfo.Column("is_active", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHotlist = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHotlist = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHotlist = new TableInfo("hotlist", _columnsHotlist, _foreignKeysHotlist, _indicesHotlist);
        final TableInfo _existingHotlist = TableInfo.read(_db, "hotlist");
        if (! _infoHotlist.equals(_existingHotlist)) {
          return new RoomOpenHelper.ValidationResult(false, "hotlist(com.ticketpro.model.Hotlist).\n"
                  + " Expected:\n" + _infoHotlist + "\n"
                  + " Found:\n" + _existingHotlist);
        }
        final HashMap<String, TableInfo.Column> _columnsLocationGroupLocations = new HashMap<String, TableInfo.Column>(3);
        _columnsLocationGroupLocations.put("location_group_id", new TableInfo.Column("location_group_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocationGroupLocations.put("group_id", new TableInfo.Column("group_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocationGroupLocations.put("location_id", new TableInfo.Column("location_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLocationGroupLocations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLocationGroupLocations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLocationGroupLocations = new TableInfo("location_group_locations", _columnsLocationGroupLocations, _foreignKeysLocationGroupLocations, _indicesLocationGroupLocations);
        final TableInfo _existingLocationGroupLocations = TableInfo.read(_db, "location_group_locations");
        if (! _infoLocationGroupLocations.equals(_existingLocationGroupLocations)) {
          return new RoomOpenHelper.ValidationResult(false, "location_group_locations(com.ticketpro.model.LocationGroupLocation).\n"
                  + " Expected:\n" + _infoLocationGroupLocations + "\n"
                  + " Found:\n" + _existingLocationGroupLocations);
        }
        final HashMap<String, TableInfo.Column> _columnsLocationGroups = new HashMap<String, TableInfo.Column>(4);
        _columnsLocationGroups.put("group_id", new TableInfo.Column("group_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocationGroups.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocationGroups.put("group_code", new TableInfo.Column("group_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocationGroups.put("group_name", new TableInfo.Column("group_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLocationGroups = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLocationGroups = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLocationGroups = new TableInfo("location_groups", _columnsLocationGroups, _foreignKeysLocationGroups, _indicesLocationGroups);
        final TableInfo _existingLocationGroups = TableInfo.read(_db, "location_groups");
        if (! _infoLocationGroups.equals(_existingLocationGroups)) {
          return new RoomOpenHelper.ValidationResult(false, "location_groups(com.ticketpro.model.LocationGroup).\n"
                  + " Expected:\n" + _infoLocationGroups + "\n"
                  + " Found:\n" + _existingLocationGroups);
        }
        final HashMap<String, TableInfo.Column> _columnsLocations = new HashMap<String, TableInfo.Column>(6);
        _columnsLocations.put("location_id", new TableInfo.Column("location_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocations.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocations.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocations.put("zone_id", new TableInfo.Column("zone_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocations.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocations.put("is_manual", new TableInfo.Column("is_manual", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLocations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLocations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLocations = new TableInfo("locations", _columnsLocations, _foreignKeysLocations, _indicesLocations);
        final TableInfo _existingLocations = TableInfo.read(_db, "locations");
        if (! _infoLocations.equals(_existingLocations)) {
          return new RoomOpenHelper.ValidationResult(false, "locations(com.ticketpro.model.Location).\n"
                  + " Expected:\n" + _infoLocations + "\n"
                  + " Found:\n" + _existingLocations);
        }
        final HashMap<String, TableInfo.Column> _columnsLprNotifications = new HashMap<String, TableInfo.Column>(33);
        _columnsLprNotifications.put("notification_id", new TableInfo.Column("notification_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("plate", new TableInfo.Column("plate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("state", new TableInfo.Column("state", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("make", new TableInfo.Column("make", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("model", new TableInfo.Column("model", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("body", new TableInfo.Column("body", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("latitude", new TableInfo.Column("latitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("longitude", new TableInfo.Column("longitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("photo1", new TableInfo.Column("photo1", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("photo2", new TableInfo.Column("photo2", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("photo3", new TableInfo.Column("photo3", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("photo4", new TableInfo.Column("photo4", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("date_notify", new TableInfo.Column("date_notify", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("first_chalk_time", new TableInfo.Column("first_chalk_time", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("last_seen", new TableInfo.Column("last_seen", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("lpr_scan_id", new TableInfo.Column("lpr_scan_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("lpr_camera_id", new TableInfo.Column("lpr_camera_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("lpr_user_id", new TableInfo.Column("lpr_user_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("color", new TableInfo.Column("color", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("permit", new TableInfo.Column("permit", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("permit_type", new TableInfo.Column("permit_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("permit_status", new TableInfo.Column("permit_status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("duty_group", new TableInfo.Column("duty_group", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("comments", new TableInfo.Column("comments", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("comments2", new TableInfo.Column("comments2", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("violation_id", new TableInfo.Column("violation_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("violation_desc", new TableInfo.Column("violation_desc", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("violation_code", new TableInfo.Column("violation_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("device_id", new TableInfo.Column("device_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("zone", new TableInfo.Column("zone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLprNotifications.put("ticket_issues", new TableInfo.Column("ticket_issues", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLprNotifications = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLprNotifications = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLprNotifications = new TableInfo("lpr_notifications", _columnsLprNotifications, _foreignKeysLprNotifications, _indicesLprNotifications);
        final TableInfo _existingLprNotifications = TableInfo.read(_db, "lpr_notifications");
        if (! _infoLprNotifications.equals(_existingLprNotifications)) {
          return new RoomOpenHelper.ValidationResult(false, "lpr_notifications(com.ticketpro.model.LPRNotify).\n"
                  + " Expected:\n" + _infoLprNotifications + "\n"
                  + " Found:\n" + _existingLprNotifications);
        }
        final HashMap<String, TableInfo.Column> _columnsMaintenanceLogs = new HashMap<String, TableInfo.Column>(11);
        _columnsMaintenanceLogs.put("log_id", new TableInfo.Column("log_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaintenanceLogs.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaintenanceLogs.put("userid", new TableInfo.Column("userid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaintenanceLogs.put("device_id", new TableInfo.Column("device_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaintenanceLogs.put("item_name", new TableInfo.Column("item_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaintenanceLogs.put("problem_type", new TableInfo.Column("problem_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaintenanceLogs.put("comments", new TableInfo.Column("comments", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaintenanceLogs.put("log_date", new TableInfo.Column("log_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaintenanceLogs.put("latitude", new TableInfo.Column("latitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaintenanceLogs.put("longitude", new TableInfo.Column("longitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaintenanceLogs.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMaintenanceLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMaintenanceLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMaintenanceLogs = new TableInfo("maintenance_logs", _columnsMaintenanceLogs, _foreignKeysMaintenanceLogs, _indicesMaintenanceLogs);
        final TableInfo _existingMaintenanceLogs = TableInfo.read(_db, "maintenance_logs");
        if (! _infoMaintenanceLogs.equals(_existingMaintenanceLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "maintenance_logs(com.ticketpro.model.MaintenanceLog).\n"
                  + " Expected:\n" + _infoMaintenanceLogs + "\n"
                  + " Found:\n" + _existingMaintenanceLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsMaintenancePictures = new HashMap<String, TableInfo.Column>(5);
        _columnsMaintenancePictures.put("picture_id", new TableInfo.Column("picture_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaintenancePictures.put("maintenance_id", new TableInfo.Column("maintenance_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaintenancePictures.put("image_path", new TableInfo.Column("image_path", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaintenancePictures.put("image_size", new TableInfo.Column("image_size", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaintenancePictures.put("image_resolution", new TableInfo.Column("image_resolution", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMaintenancePictures = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMaintenancePictures = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMaintenancePictures = new TableInfo("maintenance_pictures", _columnsMaintenancePictures, _foreignKeysMaintenancePictures, _indicesMaintenancePictures);
        final TableInfo _existingMaintenancePictures = TableInfo.read(_db, "maintenance_pictures");
        if (! _infoMaintenancePictures.equals(_existingMaintenancePictures)) {
          return new RoomOpenHelper.ValidationResult(false, "maintenance_pictures(com.ticketpro.model.MaintenancePicture).\n"
                  + " Expected:\n" + _infoMaintenancePictures + "\n"
                  + " Found:\n" + _existingMaintenancePictures);
        }
        final HashMap<String, TableInfo.Column> _columnsMakesAndModels = new HashMap<String, TableInfo.Column>(7);
        _columnsMakesAndModels.put("make_id", new TableInfo.Column("make_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMakesAndModels.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMakesAndModels.put("make", new TableInfo.Column("make", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMakesAndModels.put("make_code", new TableInfo.Column("make_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMakesAndModels.put("model", new TableInfo.Column("model", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMakesAndModels.put("model_code", new TableInfo.Column("model_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMakesAndModels.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMakesAndModels = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMakesAndModels = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMakesAndModels = new TableInfo("makes_and_models", _columnsMakesAndModels, _foreignKeysMakesAndModels, _indicesMakesAndModels);
        final TableInfo _existingMakesAndModels = TableInfo.read(_db, "makes_and_models");
        if (! _infoMakesAndModels.equals(_existingMakesAndModels)) {
          return new RoomOpenHelper.ValidationResult(false, "makes_and_models(com.ticketpro.model.MakeAndModel).\n"
                  + " Expected:\n" + _infoMakesAndModels + "\n"
                  + " Found:\n" + _existingMakesAndModels);
        }
        final HashMap<String, TableInfo.Column> _columnsMessages = new HashMap<String, TableInfo.Column>(9);
        _columnsMessages.put("message_id", new TableInfo.Column("message_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("message_date", new TableInfo.Column("message_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("from_userid", new TableInfo.Column("from_userid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("to_userid", new TableInfo.Column("to_userid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("subject", new TableInfo.Column("subject", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("message", new TableInfo.Column("message", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("expiry_date", new TableInfo.Column("expiry_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("department", new TableInfo.Column("department", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMessages = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMessages = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMessages = new TableInfo("messages", _columnsMessages, _foreignKeysMessages, _indicesMessages);
        final TableInfo _existingMessages = TableInfo.read(_db, "messages");
        if (! _infoMessages.equals(_existingMessages)) {
          return new RoomOpenHelper.ValidationResult(false, "messages(com.ticketpro.model.Message).\n"
                  + " Expected:\n" + _infoMessages + "\n"
                  + " Found:\n" + _existingMessages);
        }
        final HashMap<String, TableInfo.Column> _columnsMeters = new HashMap<String, TableInfo.Column>(9);
        _columnsMeters.put("meter_id", new TableInfo.Column("meter_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeters.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeters.put("meter", new TableInfo.Column("meter", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeters.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeters.put("street_number", new TableInfo.Column("street_number", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeters.put("street_prefix", new TableInfo.Column("street_prefix", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeters.put("street_suffix", new TableInfo.Column("street_suffix", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeters.put("direction", new TableInfo.Column("direction", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeters.put("violation_code", new TableInfo.Column("violation_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMeters = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMeters = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMeters = new TableInfo("meters", _columnsMeters, _foreignKeysMeters, _indicesMeters);
        final TableInfo _existingMeters = TableInfo.read(_db, "meters");
        if (! _infoMeters.equals(_existingMeters)) {
          return new RoomOpenHelper.ValidationResult(false, "meters(com.ticketpro.model.Meter).\n"
                  + " Expected:\n" + _infoMeters + "\n"
                  + " Found:\n" + _existingMeters);
        }
        final HashMap<String, TableInfo.Column> _columnsMobileNowLogs = new HashMap<String, TableInfo.Column>(13);
        _columnsMobileNowLogs.put("log_id", new TableInfo.Column("log_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMobileNowLogs.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMobileNowLogs.put("userid", new TableInfo.Column("userid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMobileNowLogs.put("device_id", new TableInfo.Column("device_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMobileNowLogs.put("request_params", new TableInfo.Column("request_params", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMobileNowLogs.put("service_mode", new TableInfo.Column("service_mode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMobileNowLogs.put("response_text", new TableInfo.Column("response_text", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMobileNowLogs.put("request_date", new TableInfo.Column("request_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMobileNowLogs.put("plate_number", new TableInfo.Column("plate_number", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMobileNowLogs.put("latitude", new TableInfo.Column("latitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMobileNowLogs.put("longitude", new TableInfo.Column("longitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMobileNowLogs.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMobileNowLogs.put("duty_id", new TableInfo.Column("duty_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMobileNowLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMobileNowLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMobileNowLogs = new TableInfo("mobile_now_logs", _columnsMobileNowLogs, _foreignKeysMobileNowLogs, _indicesMobileNowLogs);
        final TableInfo _existingMobileNowLogs = TableInfo.read(_db, "mobile_now_logs");
        if (! _infoMobileNowLogs.equals(_existingMobileNowLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "mobile_now_logs(com.ticketpro.model.MobileNowLog).\n"
                  + " Expected:\n" + _infoMobileNowLogs + "\n"
                  + " Found:\n" + _existingMobileNowLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsModules = new HashMap<String, TableInfo.Column>(4);
        _columnsModules.put("module_id", new TableInfo.Column("module_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsModules.put("module_name", new TableInfo.Column("module_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsModules.put("module_desc", new TableInfo.Column("module_desc", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsModules.put("is_active", new TableInfo.Column("is_active", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysModules = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesModules = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoModules = new TableInfo("modules", _columnsModules, _foreignKeysModules, _indicesModules);
        final TableInfo _existingModules = TableInfo.read(_db, "modules");
        if (! _infoModules.equals(_existingModules)) {
          return new RoomOpenHelper.ValidationResult(false, "modules(com.ticketpro.model.Module).\n"
                  + " Expected:\n" + _infoModules + "\n"
                  + " Found:\n" + _existingModules);
        }
        final HashMap<String, TableInfo.Column> _columnsPermits = new HashMap<String, TableInfo.Column>(22);
        _columnsPermits.put("permit_id", new TableInfo.Column("permit_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("permit_number", new TableInfo.Column("permit_number", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("permit_type", new TableInfo.Column("permit_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("permit_code", new TableInfo.Column("permit_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("plate", new TableInfo.Column("plate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("plate_type", new TableInfo.Column("plate_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("vin", new TableInfo.Column("vin", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("exp_date", new TableInfo.Column("exp_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("state_code", new TableInfo.Column("state_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("body_code", new TableInfo.Column("body_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("color_code", new TableInfo.Column("color_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("make_code", new TableInfo.Column("make_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("class_code", new TableInfo.Column("class_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("begin_date", new TableInfo.Column("begin_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("end_date", new TableInfo.Column("end_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("permit_holder", new TableInfo.Column("permit_holder", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("student_id", new TableInfo.Column("student_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("address1", new TableInfo.Column("address1", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("address2", new TableInfo.Column("address2", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPermits.put("model_code", new TableInfo.Column("model_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPermits = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPermits = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPermits = new TableInfo("permits", _columnsPermits, _foreignKeysPermits, _indicesPermits);
        final TableInfo _existingPermits = TableInfo.read(_db, "permits");
        if (! _infoPermits.equals(_existingPermits)) {
          return new RoomOpenHelper.ValidationResult(false, "permits(com.ticketpro.model.Permit).\n"
                  + " Expected:\n" + _infoPermits + "\n"
                  + " Found:\n" + _existingPermits);
        }
        final HashMap<String, TableInfo.Column> _columnsPrintMacros = new HashMap<String, TableInfo.Column>(4);
        _columnsPrintMacros.put("print_macro_id", new TableInfo.Column("print_macro_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrintMacros.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrintMacros.put("macro_name", new TableInfo.Column("macro_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrintMacros.put("message", new TableInfo.Column("message", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPrintMacros = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPrintMacros = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPrintMacros = new TableInfo("print_macros", _columnsPrintMacros, _foreignKeysPrintMacros, _indicesPrintMacros);
        final TableInfo _existingPrintMacros = TableInfo.read(_db, "print_macros");
        if (! _infoPrintMacros.equals(_existingPrintMacros)) {
          return new RoomOpenHelper.ValidationResult(false, "print_macros(com.ticketpro.model.PrintMacro).\n"
                  + " Expected:\n" + _infoPrintMacros + "\n"
                  + " Found:\n" + _existingPrintMacros);
        }
        final HashMap<String, TableInfo.Column> _columnsPrintTemplates = new HashMap<String, TableInfo.Column>(8);
        _columnsPrintTemplates.put("template_id", new TableInfo.Column("template_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrintTemplates.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrintTemplates.put("printer_name", new TableInfo.Column("printer_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrintTemplates.put("printer_type", new TableInfo.Column("printer_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrintTemplates.put("display_name", new TableInfo.Column("display_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrintTemplates.put("template_name", new TableInfo.Column("template_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrintTemplates.put("template_data", new TableInfo.Column("template_data", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrintTemplates.put("is_report", new TableInfo.Column("is_report", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPrintTemplates = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPrintTemplates = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPrintTemplates = new TableInfo("print_templates", _columnsPrintTemplates, _foreignKeysPrintTemplates, _indicesPrintTemplates);
        final TableInfo _existingPrintTemplates = TableInfo.read(_db, "print_templates");
        if (! _infoPrintTemplates.equals(_existingPrintTemplates)) {
          return new RoomOpenHelper.ValidationResult(false, "print_templates(com.ticketpro.model.PrintTemplate).\n"
                  + " Expected:\n" + _infoPrintTemplates + "\n"
                  + " Found:\n" + _existingPrintTemplates);
        }
        final HashMap<String, TableInfo.Column> _columnsRepeatOffenders = new HashMap<String, TableInfo.Column>(9);
        _columnsRepeatOffenders.put("repeat_offender_id", new TableInfo.Column("repeat_offender_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepeatOffenders.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepeatOffenders.put("plate", new TableInfo.Column("plate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepeatOffenders.put("violation", new TableInfo.Column("violation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepeatOffenders.put("count", new TableInfo.Column("count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepeatOffenders.put("violation_id", new TableInfo.Column("violation_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepeatOffenders.put("state_code", new TableInfo.Column("state_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepeatOffenders.put("created_date", new TableInfo.Column("created_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepeatOffenders.put("sync_status", new TableInfo.Column("sync_status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRepeatOffenders = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRepeatOffenders = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRepeatOffenders = new TableInfo("repeat_offenders", _columnsRepeatOffenders, _foreignKeysRepeatOffenders, _indicesRepeatOffenders);
        final TableInfo _existingRepeatOffenders = TableInfo.read(_db, "repeat_offenders");
        if (! _infoRepeatOffenders.equals(_existingRepeatOffenders)) {
          return new RoomOpenHelper.ValidationResult(false, "repeat_offenders(com.ticketpro.model.RepeatOffender).\n"
                  + " Expected:\n" + _infoRepeatOffenders + "\n"
                  + " Found:\n" + _existingRepeatOffenders);
        }
        final HashMap<String, TableInfo.Column> _columnsSpecialActivities = new HashMap<String, TableInfo.Column>(6);
        _columnsSpecialActivities.put("activity_id", new TableInfo.Column("activity_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivities.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivities.put("activity", new TableInfo.Column("activity", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivities.put("code", new TableInfo.Column("code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivities.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivities.put("autoSelect", new TableInfo.Column("autoSelect", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSpecialActivities = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSpecialActivities = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSpecialActivities = new TableInfo("special_activities", _columnsSpecialActivities, _foreignKeysSpecialActivities, _indicesSpecialActivities);
        final TableInfo _existingSpecialActivities = TableInfo.read(_db, "special_activities");
        if (! _infoSpecialActivities.equals(_existingSpecialActivities)) {
          return new RoomOpenHelper.ValidationResult(false, "special_activities(com.ticketpro.model.SpecialActivity).\n"
                  + " Expected:\n" + _infoSpecialActivities + "\n"
                  + " Found:\n" + _existingSpecialActivities);
        }
        final HashMap<String, TableInfo.Column> _columnsSpecialActivitiesLocation = new HashMap<String, TableInfo.Column>(5);
        _columnsSpecialActivitiesLocation.put("recid", new TableInfo.Column("recid", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivitiesLocation.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivitiesLocation.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivitiesLocation.put("is_active", new TableInfo.Column("is_active", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivitiesLocation.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSpecialActivitiesLocation = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSpecialActivitiesLocation = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSpecialActivitiesLocation = new TableInfo("special_activities_location", _columnsSpecialActivitiesLocation, _foreignKeysSpecialActivitiesLocation, _indicesSpecialActivitiesLocation);
        final TableInfo _existingSpecialActivitiesLocation = TableInfo.read(_db, "special_activities_location");
        if (! _infoSpecialActivitiesLocation.equals(_existingSpecialActivitiesLocation)) {
          return new RoomOpenHelper.ValidationResult(false, "special_activities_location(com.ticketpro.model.SpecialActivitiesLocation).\n"
                  + " Expected:\n" + _infoSpecialActivitiesLocation + "\n"
                  + " Found:\n" + _existingSpecialActivitiesLocation);
        }
        final HashMap<String, TableInfo.Column> _columnsSpecialActivitiesPictures = new HashMap<String, TableInfo.Column>(8);
        _columnsSpecialActivitiesPictures.put("picture_id", new TableInfo.Column("picture_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivitiesPictures.put("report_id", new TableInfo.Column("report_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivitiesPictures.put("picture_date", new TableInfo.Column("picture_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivitiesPictures.put("image_path", new TableInfo.Column("image_path", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivitiesPictures.put("image_resolution", new TableInfo.Column("image_resolution", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivitiesPictures.put("image_size", new TableInfo.Column("image_size", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivitiesPictures.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivitiesPictures.put("image_name", new TableInfo.Column("image_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSpecialActivitiesPictures = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSpecialActivitiesPictures = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSpecialActivitiesPictures = new TableInfo("special_activities_pictures", _columnsSpecialActivitiesPictures, _foreignKeysSpecialActivitiesPictures, _indicesSpecialActivitiesPictures);
        final TableInfo _existingSpecialActivitiesPictures = TableInfo.read(_db, "special_activities_pictures");
        if (! _infoSpecialActivitiesPictures.equals(_existingSpecialActivitiesPictures)) {
          return new RoomOpenHelper.ValidationResult(false, "special_activities_pictures(com.ticketpro.model.SpecialActivityPicture).\n"
                  + " Expected:\n" + _infoSpecialActivitiesPictures + "\n"
                  + " Found:\n" + _existingSpecialActivitiesPictures);
        }
        final HashMap<String, TableInfo.Column> _columnsSpecialActivityDispositions = new HashMap<String, TableInfo.Column>(5);
        _columnsSpecialActivityDispositions.put("disposition_id", new TableInfo.Column("disposition_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityDispositions.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityDispositions.put("disposition", new TableInfo.Column("disposition", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityDispositions.put("code", new TableInfo.Column("code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityDispositions.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSpecialActivityDispositions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSpecialActivityDispositions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSpecialActivityDispositions = new TableInfo("special_activity_dispositions", _columnsSpecialActivityDispositions, _foreignKeysSpecialActivityDispositions, _indicesSpecialActivityDispositions);
        final TableInfo _existingSpecialActivityDispositions = TableInfo.read(_db, "special_activity_dispositions");
        if (! _infoSpecialActivityDispositions.equals(_existingSpecialActivityDispositions)) {
          return new RoomOpenHelper.ValidationResult(false, "special_activity_dispositions(com.ticketpro.model.SpecialActivityDisposition).\n"
                  + " Expected:\n" + _infoSpecialActivityDispositions + "\n"
                  + " Found:\n" + _existingSpecialActivityDispositions);
        }
        final HashMap<String, TableInfo.Column> _columnsSpecialActivityReports = new HashMap<String, TableInfo.Column>(24);
        _columnsSpecialActivityReports.put("report_id", new TableInfo.Column("report_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("userid", new TableInfo.Column("userid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("activity_id", new TableInfo.Column("activity_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("disposition_id", new TableInfo.Column("disposition_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("start_date", new TableInfo.Column("start_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("end_date", new TableInfo.Column("end_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("start_time", new TableInfo.Column("start_time", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("end_time", new TableInfo.Column("end_time", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("case_number", new TableInfo.Column("case_number", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("street_address", new TableInfo.Column("street_address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("photo1", new TableInfo.Column("photo1", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("photo2", new TableInfo.Column("photo2", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("photo3", new TableInfo.Column("photo3", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("latitude", new TableInfo.Column("latitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("longitude", new TableInfo.Column("longitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("autoSelect", new TableInfo.Column("autoSelect", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("createDate", new TableInfo.Column("createDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("duration", new TableInfo.Column("duration", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("device_id", new TableInfo.Column("device_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("activityName", new TableInfo.Column("activityName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpecialActivityReports.put("ticket_count", new TableInfo.Column("ticket_count", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSpecialActivityReports = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSpecialActivityReports = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSpecialActivityReports = new TableInfo("special_activity_reports", _columnsSpecialActivityReports, _foreignKeysSpecialActivityReports, _indicesSpecialActivityReports);
        final TableInfo _existingSpecialActivityReports = TableInfo.read(_db, "special_activity_reports");
        if (! _infoSpecialActivityReports.equals(_existingSpecialActivityReports)) {
          return new RoomOpenHelper.ValidationResult(false, "special_activity_reports(com.ticketpro.model.SpecialActivityReport).\n"
                  + " Expected:\n" + _infoSpecialActivityReports + "\n"
                  + " Found:\n" + _existingSpecialActivityReports);
        }
        final HashMap<String, TableInfo.Column> _columnsStates = new HashMap<String, TableInfo.Column>(5);
        _columnsStates.put("state_id", new TableInfo.Column("state_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStates.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStates.put("state", new TableInfo.Column("state", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStates.put("code", new TableInfo.Column("code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStates.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStates = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStates = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStates = new TableInfo("states", _columnsStates, _foreignKeysStates, _indicesStates);
        final TableInfo _existingStates = TableInfo.read(_db, "states");
        if (! _infoStates.equals(_existingStates)) {
          return new RoomOpenHelper.ValidationResult(false, "states(com.ticketpro.model.State).\n"
                  + " Expected:\n" + _infoStates + "\n"
                  + " Found:\n" + _existingStates);
        }
        final HashMap<String, TableInfo.Column> _columnsStreetPrefixes = new HashMap<String, TableInfo.Column>(3);
        _columnsStreetPrefixes.put("prefix_id", new TableInfo.Column("prefix_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStreetPrefixes.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStreetPrefixes.put("street_prefix", new TableInfo.Column("street_prefix", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStreetPrefixes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStreetPrefixes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStreetPrefixes = new TableInfo("street_prefixes", _columnsStreetPrefixes, _foreignKeysStreetPrefixes, _indicesStreetPrefixes);
        final TableInfo _existingStreetPrefixes = TableInfo.read(_db, "street_prefixes");
        if (! _infoStreetPrefixes.equals(_existingStreetPrefixes)) {
          return new RoomOpenHelper.ValidationResult(false, "street_prefixes(com.ticketpro.model.StreetPrefix).\n"
                  + " Expected:\n" + _infoStreetPrefixes + "\n"
                  + " Found:\n" + _existingStreetPrefixes);
        }
        final HashMap<String, TableInfo.Column> _columnsStreetSuffixes = new HashMap<String, TableInfo.Column>(3);
        _columnsStreetSuffixes.put("suffix_id", new TableInfo.Column("suffix_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStreetSuffixes.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStreetSuffixes.put("street_suffix", new TableInfo.Column("street_suffix", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStreetSuffixes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStreetSuffixes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStreetSuffixes = new TableInfo("street_suffixes", _columnsStreetSuffixes, _foreignKeysStreetSuffixes, _indicesStreetSuffixes);
        final TableInfo _existingStreetSuffixes = TableInfo.read(_db, "street_suffixes");
        if (! _infoStreetSuffixes.equals(_existingStreetSuffixes)) {
          return new RoomOpenHelper.ValidationResult(false, "street_suffixes(com.ticketpro.model.StreetSuffix).\n"
                  + " Expected:\n" + _infoStreetSuffixes + "\n"
                  + " Found:\n" + _existingStreetSuffixes);
        }
        final HashMap<String, TableInfo.Column> _columnsSyncData = new HashMap<String, TableInfo.Column>(10);
        _columnsSyncData.put("sync_data_id", new TableInfo.Column("sync_data_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncData.put("userid", new TableInfo.Column("userid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncData.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncData.put("activity", new TableInfo.Column("activity", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncData.put("table_name", new TableInfo.Column("table_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncData.put("primary_key", new TableInfo.Column("primary_key", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncData.put("activity_source", new TableInfo.Column("activity_source", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncData.put("sql_query", new TableInfo.Column("sql_query", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncData.put("activity_date", new TableInfo.Column("activity_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncData.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSyncData = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSyncData = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSyncData = new TableInfo("sync_data", _columnsSyncData, _foreignKeysSyncData, _indicesSyncData);
        final TableInfo _existingSyncData = TableInfo.read(_db, "sync_data");
        if (! _infoSyncData.equals(_existingSyncData)) {
          return new RoomOpenHelper.ValidationResult(false, "sync_data(com.ticketpro.model.SyncData).\n"
                  + " Expected:\n" + _infoSyncData + "\n"
                  + " Found:\n" + _existingSyncData);
        }
        final HashMap<String, TableInfo.Column> _columnsTicketComments = new HashMap<String, TableInfo.Column>(8);
        _columnsTicketComments.put("ticket_comment_id", new TableInfo.Column("ticket_comment_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketComments.put("ticket_id", new TableInfo.Column("ticket_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketComments.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketComments.put("comment_id", new TableInfo.Column("comment_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketComments.put("comment", new TableInfo.Column("comment", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketComments.put("citation_number", new TableInfo.Column("citation_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketComments.put("is_private", new TableInfo.Column("is_private", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketComments.put("is_voice_comment", new TableInfo.Column("is_voice_comment", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTicketComments = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTicketComments = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTicketComments = new TableInfo("ticket_comments", _columnsTicketComments, _foreignKeysTicketComments, _indicesTicketComments);
        final TableInfo _existingTicketComments = TableInfo.read(_db, "ticket_comments");
        if (! _infoTicketComments.equals(_existingTicketComments)) {
          return new RoomOpenHelper.ValidationResult(false, "ticket_comments(com.ticketpro.model.TicketComment).\n"
                  + " Expected:\n" + _infoTicketComments + "\n"
                  + " Found:\n" + _existingTicketComments);
        }
        final HashMap<String, TableInfo.Column> _columnsTicketPictures = new HashMap<String, TableInfo.Column>(14);
        _columnsTicketPictures.put("s_no", new TableInfo.Column("s_no", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPictures.put("picture_id", new TableInfo.Column("picture_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPictures.put("ticket_id", new TableInfo.Column("ticket_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPictures.put("citation_number", new TableInfo.Column("citation_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPictures.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPictures.put("picture_date", new TableInfo.Column("picture_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPictures.put("mark_print", new TableInfo.Column("mark_print", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPictures.put("image_path", new TableInfo.Column("image_path", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPictures.put("image_resolution", new TableInfo.Column("image_resolution", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPictures.put("image_size", new TableInfo.Column("image_size", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPictures.put("sync_status", new TableInfo.Column("sync_status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPictures.put("download_image_url", new TableInfo.Column("download_image_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPictures.put("image_name", new TableInfo.Column("image_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPictures.put("isSelfi", new TableInfo.Column("isSelfi", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTicketPictures = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTicketPictures = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTicketPictures = new TableInfo("ticket_pictures", _columnsTicketPictures, _foreignKeysTicketPictures, _indicesTicketPictures);
        final TableInfo _existingTicketPictures = TableInfo.read(_db, "ticket_pictures");
        if (! _infoTicketPictures.equals(_existingTicketPictures)) {
          return new RoomOpenHelper.ValidationResult(false, "ticket_pictures(com.ticketpro.model.TicketPicture).\n"
                  + " Expected:\n" + _infoTicketPictures + "\n"
                  + " Found:\n" + _existingTicketPictures);
        }
        final HashMap<String, TableInfo.Column> _columnsTicketHistory = new HashMap<String, TableInfo.Column>(17);
        _columnsTicketHistory.put("ticket_id", new TableInfo.Column("ticket_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketHistory.put("ticket_date", new TableInfo.Column("ticket_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketHistory.put("plate", new TableInfo.Column("plate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketHistory.put("vin", new TableInfo.Column("vin", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketHistory.put("expiration", new TableInfo.Column("expiration", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketHistory.put("state_code", new TableInfo.Column("state_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketHistory.put("make_code", new TableInfo.Column("make_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketHistory.put("body_code", new TableInfo.Column("body_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketHistory.put("color_code", new TableInfo.Column("color_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketHistory.put("permit", new TableInfo.Column("permit", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketHistory.put("meter", new TableInfo.Column("meter", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketHistory.put("is_void", new TableInfo.Column("is_void", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketHistory.put("is_chalked", new TableInfo.Column("is_chalked", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketHistory.put("is_warn", new TableInfo.Column("is_warn", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketHistory.put("is_driveaway", new TableInfo.Column("is_driveaway", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketHistory.put("violation_code", new TableInfo.Column("violation_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketHistory.put("violation", new TableInfo.Column("violation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTicketHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTicketHistory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTicketHistory = new TableInfo("ticket_history", _columnsTicketHistory, _foreignKeysTicketHistory, _indicesTicketHistory);
        final TableInfo _existingTicketHistory = TableInfo.read(_db, "ticket_history");
        if (! _infoTicketHistory.equals(_existingTicketHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "ticket_history(com.ticketpro.model.TicketHistory).\n"
                  + " Expected:\n" + _infoTicketHistory + "\n"
                  + " Found:\n" + _existingTicketHistory);
        }
        final HashMap<String, TableInfo.Column> _columnsTicketViolations = new HashMap<String, TableInfo.Column>(8);
        _columnsTicketViolations.put("ticket_violation_id", new TableInfo.Column("ticket_violation_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketViolations.put("violation_id", new TableInfo.Column("violation_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketViolations.put("ticket_id", new TableInfo.Column("ticket_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketViolations.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketViolations.put("violation_code", new TableInfo.Column("violation_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketViolations.put("fine", new TableInfo.Column("fine", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketViolations.put("citation_number", new TableInfo.Column("citation_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketViolations.put("violation", new TableInfo.Column("violation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTicketViolations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTicketViolations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTicketViolations = new TableInfo("ticket_violations", _columnsTicketViolations, _foreignKeysTicketViolations, _indicesTicketViolations);
        final TableInfo _existingTicketViolations = TableInfo.read(_db, "ticket_violations");
        if (! _infoTicketViolations.equals(_existingTicketViolations)) {
          return new RoomOpenHelper.ValidationResult(false, "ticket_violations(com.ticketpro.model.TicketViolation).\n"
                  + " Expected:\n" + _infoTicketViolations + "\n"
                  + " Found:\n" + _existingTicketViolations);
        }
        final HashMap<String, TableInfo.Column> _columnsTickets = new HashMap<String, TableInfo.Column>(52);
        _columnsTickets.put("ticket_id", new TableInfo.Column("ticket_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("userid", new TableInfo.Column("userid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("device_id", new TableInfo.Column("device_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("citation_number", new TableInfo.Column("citation_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("ticket_date", new TableInfo.Column("ticket_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("state_id", new TableInfo.Column("state_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("state_code", new TableInfo.Column("state_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("plate", new TableInfo.Column("plate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("vin", new TableInfo.Column("vin", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("expiration", new TableInfo.Column("expiration", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("make_id", new TableInfo.Column("make_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("make_code", new TableInfo.Column("make_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("body_id", new TableInfo.Column("body_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("body_code", new TableInfo.Column("body_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("color_id", new TableInfo.Column("color_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("color_code", new TableInfo.Column("color_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("street_prefix", new TableInfo.Column("street_prefix", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("street_suffix", new TableInfo.Column("street_suffix", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("street_number", new TableInfo.Column("street_number", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("direction", new TableInfo.Column("direction", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("latitude", new TableInfo.Column("latitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("longitude", new TableInfo.Column("longitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("gpstime", new TableInfo.Column("gpstime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("is_gps_location", new TableInfo.Column("is_gps_location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("is_void", new TableInfo.Column("is_void", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("is_chalked", new TableInfo.Column("is_chalked", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("is_warn", new TableInfo.Column("is_warn", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("is_driveaway", new TableInfo.Column("is_driveaway", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("void_id", new TableInfo.Column("void_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("chalk_id", new TableInfo.Column("chalk_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("permit", new TableInfo.Column("permit", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("meter", new TableInfo.Column("meter", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("fine", new TableInfo.Column("fine", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("time_marked", new TableInfo.Column("time_marked", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("space", new TableInfo.Column("space", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("violation_code", new TableInfo.Column("violation_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("violation", new TableInfo.Column("violation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("void_reason_code", new TableInfo.Column("void_reason_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("duty_id", new TableInfo.Column("duty_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("is_lpr", new TableInfo.Column("is_lpr", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("violation_id", new TableInfo.Column("violation_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("photo_count", new TableInfo.Column("photo_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("lpr_notification_id", new TableInfo.Column("lpr_notification_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("placard", new TableInfo.Column("placard", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("duty_report_id", new TableInfo.Column("duty_report_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("app_version", new TableInfo.Column("app_version", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("chalk_time", new TableInfo.Column("chalk_time", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("chalk_last_seen", new TableInfo.Column("chalk_last_seen", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTickets.put("ticket_date_new", new TableInfo.Column("ticket_date_new", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTickets = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTickets = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTickets = new TableInfo("tickets", _columnsTickets, _foreignKeysTickets, _indicesTickets);
        final TableInfo _existingTickets = TableInfo.read(_db, "tickets");
        if (! _infoTickets.equals(_existingTickets)) {
          return new RoomOpenHelper.ValidationResult(false, "tickets(com.ticketpro.model.Ticket).\n"
                  + " Expected:\n" + _infoTickets + "\n"
                  + " Found:\n" + _existingTickets);
        }
        final HashMap<String, TableInfo.Column> _columnsTpmEula = new HashMap<String, TableInfo.Column>(7);
        _columnsTpmEula.put("rec_id", new TableInfo.Column("rec_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTpmEula.put("EULA_text", new TableInfo.Column("EULA_text", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTpmEula.put("Entry_date", new TableInfo.Column("Entry_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTpmEula.put("Effective_date", new TableInfo.Column("Effective_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTpmEula.put("cust_id", new TableInfo.Column("cust_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTpmEula.put("module_id", new TableInfo.Column("module_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTpmEula.put("is_active", new TableInfo.Column("is_active", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTpmEula = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTpmEula = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTpmEula = new TableInfo("tpm_eula", _columnsTpmEula, _foreignKeysTpmEula, _indicesTpmEula);
        final TableInfo _existingTpmEula = TableInfo.read(_db, "tpm_eula");
        if (! _infoTpmEula.equals(_existingTpmEula)) {
          return new RoomOpenHelper.ValidationResult(false, "tpm_eula(com.ticketpro.model.EulaModel).\n"
                  + " Expected:\n" + _infoTpmEula + "\n"
                  + " Found:\n" + _existingTpmEula);
        }
        final HashMap<String, TableInfo.Column> _columnsUserSettings = new HashMap<String, TableInfo.Column>(8);
        _columnsUserSettings.put("setting_id", new TableInfo.Column("setting_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("userid", new TableInfo.Column("userid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("device_id", new TableInfo.Column("device_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("auto_sync_interval", new TableInfo.Column("auto_sync_interval", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("data_retention_period", new TableInfo.Column("data_retention_period", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("gps", new TableInfo.Column("gps", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("data_backup", new TableInfo.Column("data_backup", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("user_prefs", new TableInfo.Column("user_prefs", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserSettings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserSettings = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserSettings = new TableInfo("user_settings", _columnsUserSettings, _foreignKeysUserSettings, _indicesUserSettings);
        final TableInfo _existingUserSettings = TableInfo.read(_db, "user_settings");
        if (! _infoUserSettings.equals(_existingUserSettings)) {
          return new RoomOpenHelper.ValidationResult(false, "user_settings(com.ticketpro.model.UserSetting).\n"
                  + " Expected:\n" + _infoUserSettings + "\n"
                  + " Found:\n" + _existingUserSettings);
        }
        RoomOpenHelper.ValidationResult _result;
        _result = onValidateSchema2(_db);
        if (!_result.isValid) {
          return _result;
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }

      private RoomOpenHelper.ValidationResult onValidateSchema2(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(15);
        _columnsUsers.put("userid", new TableInfo.Column("userid", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("username", new TableInfo.Column("username", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("password", new TableInfo.Column("password", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("user_type", new TableInfo.Column("user_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("first_name", new TableInfo.Column("first_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("last_name", new TableInfo.Column("last_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("badge", new TableInfo.Column("badge", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("department", new TableInfo.Column("department", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("is_active", new TableInfo.Column("is_active", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email_address", new TableInfo.Column("email_address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("modules", new TableInfo.Column("modules", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("print_name", new TableInfo.Column("print_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("rmsid", new TableInfo.Column("rmsid", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(_db, "users");
        if (! _infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.ticketpro.model.User).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsVendors = new HashMap<String, TableInfo.Column>(4);
        _columnsVendors.put("vendor_id", new TableInfo.Column("vendor_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendors.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendors.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendors.put("contact_number", new TableInfo.Column("contact_number", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVendors = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVendors = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVendors = new TableInfo("vendors", _columnsVendors, _foreignKeysVendors, _indicesVendors);
        final TableInfo _existingVendors = TableInfo.read(_db, "vendors");
        if (! _infoVendors.equals(_existingVendors)) {
          return new RoomOpenHelper.ValidationResult(false, "vendors(com.ticketpro.model.Vendor).\n"
                  + " Expected:\n" + _infoVendors + "\n"
                  + " Found:\n" + _existingVendors);
        }
        final HashMap<String, TableInfo.Column> _columnsVendorServices = new HashMap<String, TableInfo.Column>(7);
        _columnsVendorServices.put("service_id", new TableInfo.Column("service_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorServices.put("vendor_id", new TableInfo.Column("vendor_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorServices.put("service_name", new TableInfo.Column("service_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorServices.put("test_url", new TableInfo.Column("test_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorServices.put("prod_url", new TableInfo.Column("prod_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorServices.put("params", new TableInfo.Column("params", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorServices.put("security_key", new TableInfo.Column("security_key", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVendorServices = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVendorServices = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVendorServices = new TableInfo("vendor_services", _columnsVendorServices, _foreignKeysVendorServices, _indicesVendorServices);
        final TableInfo _existingVendorServices = TableInfo.read(_db, "vendor_services");
        if (! _infoVendorServices.equals(_existingVendorServices)) {
          return new RoomOpenHelper.ValidationResult(false, "vendor_services(com.ticketpro.model.VendorService).\n"
                  + " Expected:\n" + _infoVendorServices + "\n"
                  + " Found:\n" + _existingVendorServices);
        }
        final HashMap<String, TableInfo.Column> _columnsVendorServiceRegistrations = new HashMap<String, TableInfo.Column>(8);
        _columnsVendorServiceRegistrations.put("registration_id", new TableInfo.Column("registration_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorServiceRegistrations.put("service_id", new TableInfo.Column("service_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorServiceRegistrations.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorServiceRegistrations.put("device_id", new TableInfo.Column("device_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorServiceRegistrations.put("userid", new TableInfo.Column("userid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorServiceRegistrations.put("param_mappings", new TableInfo.Column("param_mappings", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorServiceRegistrations.put("service_mode", new TableInfo.Column("service_mode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorServiceRegistrations.put("is_active", new TableInfo.Column("is_active", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVendorServiceRegistrations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVendorServiceRegistrations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVendorServiceRegistrations = new TableInfo("vendor_service_registrations", _columnsVendorServiceRegistrations, _foreignKeysVendorServiceRegistrations, _indicesVendorServiceRegistrations);
        final TableInfo _existingVendorServiceRegistrations = TableInfo.read(_db, "vendor_service_registrations");
        if (! _infoVendorServiceRegistrations.equals(_existingVendorServiceRegistrations)) {
          return new RoomOpenHelper.ValidationResult(false, "vendor_service_registrations(com.ticketpro.model.VendorServiceRegistration).\n"
                  + " Expected:\n" + _infoVendorServiceRegistrations + "\n"
                  + " Found:\n" + _existingVendorServiceRegistrations);
        }
        final HashMap<String, TableInfo.Column> _columnsVendorItems = new HashMap<String, TableInfo.Column>(10);
        _columnsVendorItems.put("item_id", new TableInfo.Column("item_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorItems.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorItems.put("vendor_id", new TableInfo.Column("vendor_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorItems.put("item_code", new TableInfo.Column("item_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorItems.put("item_name", new TableInfo.Column("item_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorItems.put("item_type", new TableInfo.Column("item_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorItems.put("duration", new TableInfo.Column("duration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorItems.put("violation_id", new TableInfo.Column("violation_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorItems.put("is_active", new TableInfo.Column("is_active", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendorItems.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVendorItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVendorItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVendorItems = new TableInfo("vendor_items", _columnsVendorItems, _foreignKeysVendorItems, _indicesVendorItems);
        final TableInfo _existingVendorItems = TableInfo.read(_db, "vendor_items");
        if (! _infoVendorItems.equals(_existingVendorItems)) {
          return new RoomOpenHelper.ValidationResult(false, "vendor_items(com.ticketpro.model.VendorItem).\n"
                  + " Expected:\n" + _infoVendorItems + "\n"
                  + " Found:\n" + _existingVendorItems);
        }
        final HashMap<String, TableInfo.Column> _columnsViolationGroupViolations = new HashMap<String, TableInfo.Column>(3);
        _columnsViolationGroupViolations.put("violation_group_id", new TableInfo.Column("violation_group_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolationGroupViolations.put("group_id", new TableInfo.Column("group_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolationGroupViolations.put("violation_id", new TableInfo.Column("violation_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysViolationGroupViolations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesViolationGroupViolations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoViolationGroupViolations = new TableInfo("violation_group_violations", _columnsViolationGroupViolations, _foreignKeysViolationGroupViolations, _indicesViolationGroupViolations);
        final TableInfo _existingViolationGroupViolations = TableInfo.read(_db, "violation_group_violations");
        if (! _infoViolationGroupViolations.equals(_existingViolationGroupViolations)) {
          return new RoomOpenHelper.ValidationResult(false, "violation_group_violations(com.ticketpro.model.ViolationGroupViolation).\n"
                  + " Expected:\n" + _infoViolationGroupViolations + "\n"
                  + " Found:\n" + _existingViolationGroupViolations);
        }
        final HashMap<String, TableInfo.Column> _columnsViolationGroups = new HashMap<String, TableInfo.Column>(4);
        _columnsViolationGroups.put("group_id", new TableInfo.Column("group_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolationGroups.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolationGroups.put("group_code", new TableInfo.Column("group_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolationGroups.put("group_name", new TableInfo.Column("group_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysViolationGroups = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesViolationGroups = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoViolationGroups = new TableInfo("violation_groups", _columnsViolationGroups, _foreignKeysViolationGroups, _indicesViolationGroups);
        final TableInfo _existingViolationGroups = TableInfo.read(_db, "violation_groups");
        if (! _infoViolationGroups.equals(_existingViolationGroups)) {
          return new RoomOpenHelper.ValidationResult(false, "violation_groups(com.ticketpro.model.ViolationGroup).\n"
                  + " Expected:\n" + _infoViolationGroups + "\n"
                  + " Found:\n" + _existingViolationGroups);
        }
        final HashMap<String, TableInfo.Column> _columnsViolations = new HashMap<String, TableInfo.Column>(17);
        _columnsViolations.put("violation_id", new TableInfo.Column("violation_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolations.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolations.put("violation", new TableInfo.Column("violation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolations.put("code", new TableInfo.Column("code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolations.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolations.put("base_fine", new TableInfo.Column("base_fine", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolations.put("fine1", new TableInfo.Column("fine1", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolations.put("fine2", new TableInfo.Column("fine2", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolations.put("repeat_offender", new TableInfo.Column("repeat_offender", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolations.put("violation_display", new TableInfo.Column("violation_display", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolations.put("default_comment", new TableInfo.Column("default_comment", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolations.put("required_comments", new TableInfo.Column("required_comments", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolations.put("required_photos", new TableInfo.Column("required_photos", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolations.put("chalktimerequired", new TableInfo.Column("chalktimerequired", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolations.put("hide", new TableInfo.Column("hide", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolations.put("repeat_offender_type", new TableInfo.Column("repeat_offender_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsViolations.put("code_export", new TableInfo.Column("code_export", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysViolations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesViolations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoViolations = new TableInfo("violations", _columnsViolations, _foreignKeysViolations, _indicesViolations);
        final TableInfo _existingViolations = TableInfo.read(_db, "violations");
        if (! _infoViolations.equals(_existingViolations)) {
          return new RoomOpenHelper.ValidationResult(false, "violations(com.ticketpro.model.Violation).\n"
                  + " Expected:\n" + _infoViolations + "\n"
                  + " Found:\n" + _existingViolations);
        }
        final HashMap<String, TableInfo.Column> _columnsVoidTicketReasons = new HashMap<String, TableInfo.Column>(5);
        _columnsVoidTicketReasons.put("reason_id", new TableInfo.Column("reason_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVoidTicketReasons.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVoidTicketReasons.put("reason_title", new TableInfo.Column("reason_title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVoidTicketReasons.put("reason_code", new TableInfo.Column("reason_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVoidTicketReasons.put("order_number", new TableInfo.Column("order_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVoidTicketReasons = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVoidTicketReasons = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVoidTicketReasons = new TableInfo("void_ticket_reasons", _columnsVoidTicketReasons, _foreignKeysVoidTicketReasons, _indicesVoidTicketReasons);
        final TableInfo _existingVoidTicketReasons = TableInfo.read(_db, "void_ticket_reasons");
        if (! _infoVoidTicketReasons.equals(_existingVoidTicketReasons)) {
          return new RoomOpenHelper.ValidationResult(false, "void_ticket_reasons(com.ticketpro.model.VoidTicketReason).\n"
                  + " Expected:\n" + _infoVoidTicketReasons + "\n"
                  + " Found:\n" + _existingVoidTicketReasons);
        }
        final HashMap<String, TableInfo.Column> _columnsZones = new HashMap<String, TableInfo.Column>(4);
        _columnsZones.put("zone_id", new TableInfo.Column("zone_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsZones.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsZones.put("zone", new TableInfo.Column("zone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsZones.put("time_diff", new TableInfo.Column("time_diff", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysZones = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesZones = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoZones = new TableInfo("zones", _columnsZones, _foreignKeysZones, _indicesZones);
        final TableInfo _existingZones = TableInfo.read(_db, "zones");
        if (! _infoZones.equals(_existingZones)) {
          return new RoomOpenHelper.ValidationResult(false, "zones(com.ticketpro.model.Zone).\n"
                  + " Expected:\n" + _infoZones + "\n"
                  + " Found:\n" + _existingZones);
        }
        final HashMap<String, TableInfo.Column> _columnsPlacardTemp = new HashMap<String, TableInfo.Column>(3);
        _columnsPlacardTemp.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlacardTemp.put("placard_no", new TableInfo.Column("placard_no", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlacardTemp.put("placard_details", new TableInfo.Column("placard_details", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlacardTemp = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlacardTemp = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlacardTemp = new TableInfo("placard_temp", _columnsPlacardTemp, _foreignKeysPlacardTemp, _indicesPlacardTemp);
        final TableInfo _existingPlacardTemp = TableInfo.read(_db, "placard_temp");
        if (! _infoPlacardTemp.equals(_existingPlacardTemp)) {
          return new RoomOpenHelper.ValidationResult(false, "placard_temp(com.ticketpro.model.Placard).\n"
                  + " Expected:\n" + _infoPlacardTemp + "\n"
                  + " Found:\n" + _existingPlacardTemp);
        }
        final HashMap<String, TableInfo.Column> _columnsTicketsTemp = new HashMap<String, TableInfo.Column>(49);
        _columnsTicketsTemp.put("ticket_id", new TableInfo.Column("ticket_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("userid", new TableInfo.Column("userid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("device_id", new TableInfo.Column("device_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("citation_number", new TableInfo.Column("citation_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("ticket_date", new TableInfo.Column("ticket_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("state_id", new TableInfo.Column("state_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("state_code", new TableInfo.Column("state_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("plate", new TableInfo.Column("plate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("vin", new TableInfo.Column("vin", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("expiration", new TableInfo.Column("expiration", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("make_id", new TableInfo.Column("make_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("make_code", new TableInfo.Column("make_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("body_id", new TableInfo.Column("body_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("body_code", new TableInfo.Column("body_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("color_id", new TableInfo.Column("color_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("color_code", new TableInfo.Column("color_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("street_prefix", new TableInfo.Column("street_prefix", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("street_suffix", new TableInfo.Column("street_suffix", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("street_number", new TableInfo.Column("street_number", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("direction", new TableInfo.Column("direction", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("latitude", new TableInfo.Column("latitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("longitude", new TableInfo.Column("longitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("gpstime", new TableInfo.Column("gpstime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("is_gps_location", new TableInfo.Column("is_gps_location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("is_void", new TableInfo.Column("is_void", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("is_chalked", new TableInfo.Column("is_chalked", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("is_warn", new TableInfo.Column("is_warn", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("is_driveaway", new TableInfo.Column("is_driveaway", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("void_id", new TableInfo.Column("void_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("chalk_id", new TableInfo.Column("chalk_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("permit", new TableInfo.Column("permit", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("meter", new TableInfo.Column("meter", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("fine", new TableInfo.Column("fine", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("time_marked", new TableInfo.Column("time_marked", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("space", new TableInfo.Column("space", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("violation_code", new TableInfo.Column("violation_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("violation", new TableInfo.Column("violation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("void_reason_code", new TableInfo.Column("void_reason_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("duty_id", new TableInfo.Column("duty_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("is_lpr", new TableInfo.Column("is_lpr", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("violation_id", new TableInfo.Column("violation_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("photo_count", new TableInfo.Column("photo_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("lpr_notification_id", new TableInfo.Column("lpr_notification_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("placard", new TableInfo.Column("placard", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("duty_report_id", new TableInfo.Column("duty_report_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketsTemp.put("app_version", new TableInfo.Column("app_version", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTicketsTemp = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTicketsTemp = new HashSet<TableInfo.Index>(1);
        _indicesTicketsTemp.add(new TableInfo.Index("index_tickets_temp_custid", true, Arrays.asList("custid")));
        final TableInfo _infoTicketsTemp = new TableInfo("tickets_temp", _columnsTicketsTemp, _foreignKeysTicketsTemp, _indicesTicketsTemp);
        final TableInfo _existingTicketsTemp = TableInfo.read(_db, "tickets_temp");
        if (! _infoTicketsTemp.equals(_existingTicketsTemp)) {
          return new RoomOpenHelper.ValidationResult(false, "tickets_temp(com.ticketpro.model.TicketTemp).\n"
                  + " Expected:\n" + _infoTicketsTemp + "\n"
                  + " Found:\n" + _existingTicketsTemp);
        }
        final HashMap<String, TableInfo.Column> _columnsTicketViolationsTemp = new HashMap<String, TableInfo.Column>(8);
        _columnsTicketViolationsTemp.put("ticket_violation_id", new TableInfo.Column("ticket_violation_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketViolationsTemp.put("violation_id", new TableInfo.Column("violation_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketViolationsTemp.put("ticket_id", new TableInfo.Column("ticket_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketViolationsTemp.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketViolationsTemp.put("violation_code", new TableInfo.Column("violation_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketViolationsTemp.put("fine", new TableInfo.Column("fine", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketViolationsTemp.put("citation_number", new TableInfo.Column("citation_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketViolationsTemp.put("violation", new TableInfo.Column("violation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTicketViolationsTemp = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTicketViolationsTemp = new HashSet<TableInfo.Index>(1);
        _indicesTicketViolationsTemp.add(new TableInfo.Index("index_ticket_violations_temp_violation_code", true, Arrays.asList("violation_code")));
        final TableInfo _infoTicketViolationsTemp = new TableInfo("ticket_violations_temp", _columnsTicketViolationsTemp, _foreignKeysTicketViolationsTemp, _indicesTicketViolationsTemp);
        final TableInfo _existingTicketViolationsTemp = TableInfo.read(_db, "ticket_violations_temp");
        if (! _infoTicketViolationsTemp.equals(_existingTicketViolationsTemp)) {
          return new RoomOpenHelper.ValidationResult(false, "ticket_violations_temp(com.ticketpro.model.TicketViolationTemp).\n"
                  + " Expected:\n" + _infoTicketViolationsTemp + "\n"
                  + " Found:\n" + _existingTicketViolationsTemp);
        }
        final HashMap<String, TableInfo.Column> _columnsTicketPicturesTemp = new HashMap<String, TableInfo.Column>(14);
        _columnsTicketPicturesTemp.put("s_no", new TableInfo.Column("s_no", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPicturesTemp.put("picture_id", new TableInfo.Column("picture_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPicturesTemp.put("ticket_id", new TableInfo.Column("ticket_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPicturesTemp.put("citation_number", new TableInfo.Column("citation_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPicturesTemp.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPicturesTemp.put("picture_date", new TableInfo.Column("picture_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPicturesTemp.put("mark_print", new TableInfo.Column("mark_print", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPicturesTemp.put("image_path", new TableInfo.Column("image_path", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPicturesTemp.put("image_resolution", new TableInfo.Column("image_resolution", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPicturesTemp.put("image_size", new TableInfo.Column("image_size", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPicturesTemp.put("sync_status", new TableInfo.Column("sync_status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPicturesTemp.put("download_image_url", new TableInfo.Column("download_image_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPicturesTemp.put("image_name", new TableInfo.Column("image_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketPicturesTemp.put("isSelfi", new TableInfo.Column("isSelfi", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTicketPicturesTemp = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTicketPicturesTemp = new HashSet<TableInfo.Index>(1);
        _indicesTicketPicturesTemp.add(new TableInfo.Index("index_ticket_pictures_temp_picture_date", true, Arrays.asList("picture_date")));
        final TableInfo _infoTicketPicturesTemp = new TableInfo("ticket_pictures_temp", _columnsTicketPicturesTemp, _foreignKeysTicketPicturesTemp, _indicesTicketPicturesTemp);
        final TableInfo _existingTicketPicturesTemp = TableInfo.read(_db, "ticket_pictures_temp");
        if (! _infoTicketPicturesTemp.equals(_existingTicketPicturesTemp)) {
          return new RoomOpenHelper.ValidationResult(false, "ticket_pictures_temp(com.ticketpro.model.TicketPictureTemp).\n"
                  + " Expected:\n" + _infoTicketPicturesTemp + "\n"
                  + " Found:\n" + _existingTicketPicturesTemp);
        }
        final HashMap<String, TableInfo.Column> _columnsTicketCommentsTemp = new HashMap<String, TableInfo.Column>(8);
        _columnsTicketCommentsTemp.put("ticket_comment_id", new TableInfo.Column("ticket_comment_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketCommentsTemp.put("ticket_id", new TableInfo.Column("ticket_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketCommentsTemp.put("custid", new TableInfo.Column("custid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketCommentsTemp.put("comment_id", new TableInfo.Column("comment_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketCommentsTemp.put("comment", new TableInfo.Column("comment", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketCommentsTemp.put("citation_number", new TableInfo.Column("citation_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketCommentsTemp.put("is_private", new TableInfo.Column("is_private", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTicketCommentsTemp.put("is_voice_comment", new TableInfo.Column("is_voice_comment", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTicketCommentsTemp = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTicketCommentsTemp = new HashSet<TableInfo.Index>(1);
        _indicesTicketCommentsTemp.add(new TableInfo.Index("index_ticket_comments_temp_comment_id", true, Arrays.asList("comment_id")));
        final TableInfo _infoTicketCommentsTemp = new TableInfo("ticket_comments_temp", _columnsTicketCommentsTemp, _foreignKeysTicketCommentsTemp, _indicesTicketCommentsTemp);
        final TableInfo _existingTicketCommentsTemp = TableInfo.read(_db, "ticket_comments_temp");
        if (! _infoTicketCommentsTemp.equals(_existingTicketCommentsTemp)) {
          return new RoomOpenHelper.ValidationResult(false, "ticket_comments_temp(com.ticketpro.model.TicketCommentTemp).\n"
                  + " Expected:\n" + _infoTicketCommentsTemp + "\n"
                  + " Found:\n" + _existingTicketCommentsTemp);
        }
        final HashMap<String, TableInfo.Column> _columnsGenetecPatrollers = new HashMap<String, TableInfo.Column>(6);
        _columnsGenetecPatrollers.put("record_Id", new TableInfo.Column("record_Id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGenetecPatrollers.put("custId", new TableInfo.Column("custId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGenetecPatrollers.put("patroller_Id", new TableInfo.Column("patroller_Id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGenetecPatrollers.put("vehicleName", new TableInfo.Column("vehicleName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGenetecPatrollers.put("is_active", new TableInfo.Column("is_active", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGenetecPatrollers.put("createdOn", new TableInfo.Column("createdOn", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGenetecPatrollers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGenetecPatrollers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGenetecPatrollers = new TableInfo("Genetec_Patrollers", _columnsGenetecPatrollers, _foreignKeysGenetecPatrollers, _indicesGenetecPatrollers);
        final TableInfo _existingGenetecPatrollers = TableInfo.read(_db, "Genetec_Patrollers");
        if (! _infoGenetecPatrollers.equals(_existingGenetecPatrollers)) {
          return new RoomOpenHelper.ValidationResult(false, "Genetec_Patrollers(com.ticketpro.model.GenetecPatrollers).\n"
                  + " Expected:\n" + _infoGenetecPatrollers + "\n"
                  + " Found:\n" + _existingGenetecPatrollers);
        }
        final HashMap<String, TableInfo.Column> _columnsGenetecPatrollerActivities = new HashMap<String, TableInfo.Column>(7);
        _columnsGenetecPatrollerActivities.put("record_Id", new TableInfo.Column("record_Id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGenetecPatrollerActivities.put("createdOn", new TableInfo.Column("createdOn", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGenetecPatrollerActivities.put("custId", new TableInfo.Column("custId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGenetecPatrollerActivities.put("patroller_Id", new TableInfo.Column("patroller_Id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGenetecPatrollerActivities.put("user_id", new TableInfo.Column("user_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGenetecPatrollerActivities.put("device_Id", new TableInfo.Column("device_Id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGenetecPatrollerActivities.put("is_active", new TableInfo.Column("is_active", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGenetecPatrollerActivities = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGenetecPatrollerActivities = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGenetecPatrollerActivities = new TableInfo("Genetec_PatrollerActivities", _columnsGenetecPatrollerActivities, _foreignKeysGenetecPatrollerActivities, _indicesGenetecPatrollerActivities);
        final TableInfo _existingGenetecPatrollerActivities = TableInfo.read(_db, "Genetec_PatrollerActivities");
        if (! _infoGenetecPatrollerActivities.equals(_existingGenetecPatrollerActivities)) {
          return new RoomOpenHelper.ValidationResult(false, "Genetec_PatrollerActivities(com.ticketpro.model.GenetecPatrollerActivities).\n"
                  + " Expected:\n" + _infoGenetecPatrollerActivities + "\n"
                  + " Found:\n" + _existingGenetecPatrollerActivities);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "c9c79c4390070148959475dfcc06ef99", "e935495a2004b37fe4240949564df7d0");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "ALPRPhotoChalk","FT_DeviceHistory","bodies","lprbodymap","call_in_list","call_in_reports","chalk_comments","chalk_pictures","chalk_vehicles","colors","comment_group_comments","comment_groups","comments","contacts","customer_modules","customers","device_groups","devices","directions","durations","duties","duty_reports","error_logs","features","gps_locations","hotlist","location_group_locations","location_groups","locations","lpr_notifications","maintenance_logs","maintenance_pictures","makes_and_models","messages","meters","mobile_now_logs","modules","permits","print_macros","print_templates","repeat_offenders","special_activities","special_activities_location","special_activities_pictures","special_activity_dispositions","special_activity_reports","states","street_prefixes","street_suffixes","sync_data","ticket_comments","ticket_pictures","ticket_history","ticket_violations","tickets","tpm_eula","user_settings","users","vendors","vendor_services","vendor_service_registrations","vendor_items","violation_group_violations","violation_groups","violations","void_ticket_reasons","zones","placard_temp","tickets_temp","ticket_violations_temp","ticket_pictures_temp","ticket_comments_temp","Genetec_Patrollers","Genetec_PatrollerActivities");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `ALPRPhotoChalk`");
      _db.execSQL("DELETE FROM `FT_DeviceHistory`");
      _db.execSQL("DELETE FROM `bodies`");
      _db.execSQL("DELETE FROM `lprbodymap`");
      _db.execSQL("DELETE FROM `call_in_list`");
      _db.execSQL("DELETE FROM `call_in_reports`");
      _db.execSQL("DELETE FROM `chalk_comments`");
      _db.execSQL("DELETE FROM `chalk_pictures`");
      _db.execSQL("DELETE FROM `chalk_vehicles`");
      _db.execSQL("DELETE FROM `colors`");
      _db.execSQL("DELETE FROM `comment_group_comments`");
      _db.execSQL("DELETE FROM `comment_groups`");
      _db.execSQL("DELETE FROM `comments`");
      _db.execSQL("DELETE FROM `contacts`");
      _db.execSQL("DELETE FROM `customer_modules`");
      _db.execSQL("DELETE FROM `customers`");
      _db.execSQL("DELETE FROM `device_groups`");
      _db.execSQL("DELETE FROM `devices`");
      _db.execSQL("DELETE FROM `directions`");
      _db.execSQL("DELETE FROM `durations`");
      _db.execSQL("DELETE FROM `duties`");
      _db.execSQL("DELETE FROM `duty_reports`");
      _db.execSQL("DELETE FROM `error_logs`");
      _db.execSQL("DELETE FROM `features`");
      _db.execSQL("DELETE FROM `gps_locations`");
      _db.execSQL("DELETE FROM `hotlist`");
      _db.execSQL("DELETE FROM `location_group_locations`");
      _db.execSQL("DELETE FROM `location_groups`");
      _db.execSQL("DELETE FROM `locations`");
      _db.execSQL("DELETE FROM `lpr_notifications`");
      _db.execSQL("DELETE FROM `maintenance_logs`");
      _db.execSQL("DELETE FROM `maintenance_pictures`");
      _db.execSQL("DELETE FROM `makes_and_models`");
      _db.execSQL("DELETE FROM `messages`");
      _db.execSQL("DELETE FROM `meters`");
      _db.execSQL("DELETE FROM `mobile_now_logs`");
      _db.execSQL("DELETE FROM `modules`");
      _db.execSQL("DELETE FROM `permits`");
      _db.execSQL("DELETE FROM `print_macros`");
      _db.execSQL("DELETE FROM `print_templates`");
      _db.execSQL("DELETE FROM `repeat_offenders`");
      _db.execSQL("DELETE FROM `special_activities`");
      _db.execSQL("DELETE FROM `special_activities_location`");
      _db.execSQL("DELETE FROM `special_activities_pictures`");
      _db.execSQL("DELETE FROM `special_activity_dispositions`");
      _db.execSQL("DELETE FROM `special_activity_reports`");
      _db.execSQL("DELETE FROM `states`");
      _db.execSQL("DELETE FROM `street_prefixes`");
      _db.execSQL("DELETE FROM `street_suffixes`");
      _db.execSQL("DELETE FROM `sync_data`");
      _db.execSQL("DELETE FROM `ticket_comments`");
      _db.execSQL("DELETE FROM `ticket_pictures`");
      _db.execSQL("DELETE FROM `ticket_history`");
      _db.execSQL("DELETE FROM `ticket_violations`");
      _db.execSQL("DELETE FROM `tickets`");
      _db.execSQL("DELETE FROM `tpm_eula`");
      _db.execSQL("DELETE FROM `user_settings`");
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `vendors`");
      _db.execSQL("DELETE FROM `vendor_services`");
      _db.execSQL("DELETE FROM `vendor_service_registrations`");
      _db.execSQL("DELETE FROM `vendor_items`");
      _db.execSQL("DELETE FROM `violation_group_violations`");
      _db.execSQL("DELETE FROM `violation_groups`");
      _db.execSQL("DELETE FROM `violations`");
      _db.execSQL("DELETE FROM `void_ticket_reasons`");
      _db.execSQL("DELETE FROM `zones`");
      _db.execSQL("DELETE FROM `placard_temp`");
      _db.execSQL("DELETE FROM `tickets_temp`");
      _db.execSQL("DELETE FROM `ticket_violations_temp`");
      _db.execSQL("DELETE FROM `ticket_pictures_temp`");
      _db.execSQL("DELETE FROM `ticket_comments_temp`");
      _db.execSQL("DELETE FROM `Genetec_Patrollers`");
      _db.execSQL("DELETE FROM `Genetec_PatrollerActivities`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ALPRPhotoChalkDao.class, ALPRPhotoChalkDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BodyDao.class, BodyDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PlrBodyDao.class, PlrBodyDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CallInListDao.class, CallInListDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CallInListReportDao.class, CallInListReportDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChalkCommentsDao.class, ChalkCommentsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChalkPicturesDao.class, ChalkPicturesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChalkVehiclesDao.class, ChalkVehiclesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ColorsDao.class, ColorsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CommentgroupCommentsDao.class, CommentgroupCommentsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CommentgroupsDao.class, CommentgroupsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CommentsDao.class, CommentsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ContactsDao.class, ContactsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CustomerModulesDao.class, CustomerModulesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CustomersDao.class, CustomersDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DeviceGroupsDao.class, DeviceGroupsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DevicesDao.class, DevicesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DirectionsDao.class, DirectionsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DurationDao.class, DurationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DutiesDao.class, DutiesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DutyReportsDao.class, DutyReportsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ErrorLogsDao.class, ErrorLogsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FeaturesDao.class, FeaturesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FT_DeviceHistoryDao.class, FT_DeviceHistoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GPSLocationsDao.class, GPSLocationsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HotlistDao.class, HotlistDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LocationGroupsDao.class, LocationGroupsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LocationGroupLocationsDao.class, LocationGroupLocationsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LocationsDao.class, LocationsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LPRNotificationsDao.class, LPRNotificationsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MaintenanceLogsDao.class, MaintenanceLogsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MaintenancePicturesDao.class, MaintenancePicturesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MakesAndModelsDao.class, MakesAndModelsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MessagesDao.class, MessagesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MetersDao.class, MetersDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MobileNowLogsDao.class, MobileNowLogsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ModulesDao.class, ModulesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PermitsDao.class, PermitsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PrintMacrosDao.class, PrintMacrosDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PrintTemplatesDao.class, PrintTemplatesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RepeatOffendersDao.class, RepeatOffendersDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SpecialActivitiesDao.class, SpecialActivitiesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SpecialActivityDispositionDao.class, SpecialActivityDispositionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SpecialActivityReportsDao.class, SpecialActivityReportsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SpecialActivityLocationDao.class, SpecialActivityLocationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SpecialActivityPictureDao.class, SpecialActivityPictureDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StatesDao.class, StatesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StreetPrefixesDao.class, StreetPrefixesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StreetSuffixesDao.class, StreetSuffixesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SyncDataDao.class, SyncDataDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TicketCommentsDao.class, TicketCommentsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TicketCommentsDaoTemp.class, TicketCommentsDaoTemp_Impl.getRequiredConverters());
    _typeConvertersMap.put(TicketHistoryDao.class, TicketHistoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TicketPicturesDao.class, TicketPicturesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TicketPicturesDaoTemp.class, TicketPicturesDaoTemp_Impl.getRequiredConverters());
    _typeConvertersMap.put(TicketsDao.class, TicketsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TicketsDaoTemp.class, TicketsDaoTemp_Impl.getRequiredConverters());
    _typeConvertersMap.put(TicketViolationsDao.class, TicketViolationsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TicketViolationsDaoTemp.class, TicketViolationsDaoTemp_Impl.getRequiredConverters());
    _typeConvertersMap.put(TPMEulaDao.class, TPMEulaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UsersDao.class, UsersDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserSettingsDao.class, UserSettingsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(VendorsDao.class, VendorsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(VendorItemsDao.class, VendorItemsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(VendorServicesDao.class, VendorServicesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(VendorServiceRegistrationsDao.class, VendorServiceRegistrationsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ViolationGroupsDao.class, ViolationGroupsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ViolationGroupViolationsDao.class, ViolationGroupViolationsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ViolationsDao.class, ViolationsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(VoidTicketReasonsDao.class, VoidTicketReasonsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ZonesDao.class, ZonesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PlacardDao.class, PlacardDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GenetecPatrollerDao.class, GenetecPatrollerDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GenetecPatrollerActivitiesDao.class, GenetecPatrollerActivitiesDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public ALPRPhotoChalkDao alprPhotoChalkDao() {
    if (_aLPRPhotoChalkDao != null) {
      return _aLPRPhotoChalkDao;
    } else {
      synchronized(this) {
        if(_aLPRPhotoChalkDao == null) {
          _aLPRPhotoChalkDao = new ALPRPhotoChalkDao_Impl(this);
        }
        return _aLPRPhotoChalkDao;
      }
    }
  }

  @Override
  public BodyDao bodyDao() {
    if (_bodyDao != null) {
      return _bodyDao;
    } else {
      synchronized(this) {
        if(_bodyDao == null) {
          _bodyDao = new BodyDao_Impl(this);
        }
        return _bodyDao;
      }
    }
  }

  @Override
  public PlrBodyDao lprbodyDao() {
    if (_plrBodyDao != null) {
      return _plrBodyDao;
    } else {
      synchronized(this) {
        if(_plrBodyDao == null) {
          _plrBodyDao = new PlrBodyDao_Impl(this);
        }
        return _plrBodyDao;
      }
    }
  }

  @Override
  public CallInListDao callInListDao() {
    if (_callInListDao != null) {
      return _callInListDao;
    } else {
      synchronized(this) {
        if(_callInListDao == null) {
          _callInListDao = new CallInListDao_Impl(this);
        }
        return _callInListDao;
      }
    }
  }

  @Override
  public CallInListReportDao callInListReportDao() {
    if (_callInListReportDao != null) {
      return _callInListReportDao;
    } else {
      synchronized(this) {
        if(_callInListReportDao == null) {
          _callInListReportDao = new CallInListReportDao_Impl(this);
        }
        return _callInListReportDao;
      }
    }
  }

  @Override
  public ChalkCommentsDao chalkCommentsDao() {
    if (_chalkCommentsDao != null) {
      return _chalkCommentsDao;
    } else {
      synchronized(this) {
        if(_chalkCommentsDao == null) {
          _chalkCommentsDao = new ChalkCommentsDao_Impl(this);
        }
        return _chalkCommentsDao;
      }
    }
  }

  @Override
  public ChalkPicturesDao chalkPicturesDao() {
    if (_chalkPicturesDao != null) {
      return _chalkPicturesDao;
    } else {
      synchronized(this) {
        if(_chalkPicturesDao == null) {
          _chalkPicturesDao = new ChalkPicturesDao_Impl(this);
        }
        return _chalkPicturesDao;
      }
    }
  }

  @Override
  public ChalkVehiclesDao chalkVehiclesDao() {
    if (_chalkVehiclesDao != null) {
      return _chalkVehiclesDao;
    } else {
      synchronized(this) {
        if(_chalkVehiclesDao == null) {
          _chalkVehiclesDao = new ChalkVehiclesDao_Impl(this);
        }
        return _chalkVehiclesDao;
      }
    }
  }

  @Override
  public ColorsDao colorsDao() {
    if (_colorsDao != null) {
      return _colorsDao;
    } else {
      synchronized(this) {
        if(_colorsDao == null) {
          _colorsDao = new ColorsDao_Impl(this);
        }
        return _colorsDao;
      }
    }
  }

  @Override
  public CommentgroupCommentsDao commentgroupCommentsDao() {
    if (_commentgroupCommentsDao != null) {
      return _commentgroupCommentsDao;
    } else {
      synchronized(this) {
        if(_commentgroupCommentsDao == null) {
          _commentgroupCommentsDao = new CommentgroupCommentsDao_Impl(this);
        }
        return _commentgroupCommentsDao;
      }
    }
  }

  @Override
  public CommentgroupsDao commentgroupsDao() {
    if (_commentgroupsDao != null) {
      return _commentgroupsDao;
    } else {
      synchronized(this) {
        if(_commentgroupsDao == null) {
          _commentgroupsDao = new CommentgroupsDao_Impl(this);
        }
        return _commentgroupsDao;
      }
    }
  }

  @Override
  public CommentsDao commentsDao() {
    if (_commentsDao != null) {
      return _commentsDao;
    } else {
      synchronized(this) {
        if(_commentsDao == null) {
          _commentsDao = new CommentsDao_Impl(this);
        }
        return _commentsDao;
      }
    }
  }

  @Override
  public ContactsDao contactsDao() {
    if (_contactsDao != null) {
      return _contactsDao;
    } else {
      synchronized(this) {
        if(_contactsDao == null) {
          _contactsDao = new ContactsDao_Impl(this);
        }
        return _contactsDao;
      }
    }
  }

  @Override
  public CustomerModulesDao customerModulesDao() {
    if (_customerModulesDao != null) {
      return _customerModulesDao;
    } else {
      synchronized(this) {
        if(_customerModulesDao == null) {
          _customerModulesDao = new CustomerModulesDao_Impl(this);
        }
        return _customerModulesDao;
      }
    }
  }

  @Override
  public CustomersDao customersDao() {
    if (_customersDao != null) {
      return _customersDao;
    } else {
      synchronized(this) {
        if(_customersDao == null) {
          _customersDao = new CustomersDao_Impl(this);
        }
        return _customersDao;
      }
    }
  }

  @Override
  public DeviceGroupsDao deviceGroupsDao() {
    if (_deviceGroupsDao != null) {
      return _deviceGroupsDao;
    } else {
      synchronized(this) {
        if(_deviceGroupsDao == null) {
          _deviceGroupsDao = new DeviceGroupsDao_Impl(this);
        }
        return _deviceGroupsDao;
      }
    }
  }

  @Override
  public DevicesDao devicesDao() {
    if (_devicesDao != null) {
      return _devicesDao;
    } else {
      synchronized(this) {
        if(_devicesDao == null) {
          _devicesDao = new DevicesDao_Impl(this);
        }
        return _devicesDao;
      }
    }
  }

  @Override
  public DirectionsDao directionsDao() {
    if (_directionsDao != null) {
      return _directionsDao;
    } else {
      synchronized(this) {
        if(_directionsDao == null) {
          _directionsDao = new DirectionsDao_Impl(this);
        }
        return _directionsDao;
      }
    }
  }

  @Override
  public DurationDao durationDao() {
    if (_durationDao != null) {
      return _durationDao;
    } else {
      synchronized(this) {
        if(_durationDao == null) {
          _durationDao = new DurationDao_Impl(this);
        }
        return _durationDao;
      }
    }
  }

  @Override
  public DutiesDao dutiesDao() {
    if (_dutiesDao != null) {
      return _dutiesDao;
    } else {
      synchronized(this) {
        if(_dutiesDao == null) {
          _dutiesDao = new DutiesDao_Impl(this);
        }
        return _dutiesDao;
      }
    }
  }

  @Override
  public DutyReportsDao dutyReportsDao() {
    if (_dutyReportsDao != null) {
      return _dutyReportsDao;
    } else {
      synchronized(this) {
        if(_dutyReportsDao == null) {
          _dutyReportsDao = new DutyReportsDao_Impl(this);
        }
        return _dutyReportsDao;
      }
    }
  }

  @Override
  public ErrorLogsDao errorLogsDao() {
    if (_errorLogsDao != null) {
      return _errorLogsDao;
    } else {
      synchronized(this) {
        if(_errorLogsDao == null) {
          _errorLogsDao = new ErrorLogsDao_Impl(this);
        }
        return _errorLogsDao;
      }
    }
  }

  @Override
  public FeaturesDao featuresDao() {
    if (_featuresDao != null) {
      return _featuresDao;
    } else {
      synchronized(this) {
        if(_featuresDao == null) {
          _featuresDao = new FeaturesDao_Impl(this);
        }
        return _featuresDao;
      }
    }
  }

  @Override
  public FT_DeviceHistoryDao ftDeviceHistoryDao() {
    if (_fTDeviceHistoryDao != null) {
      return _fTDeviceHistoryDao;
    } else {
      synchronized(this) {
        if(_fTDeviceHistoryDao == null) {
          _fTDeviceHistoryDao = new FT_DeviceHistoryDao_Impl(this);
        }
        return _fTDeviceHistoryDao;
      }
    }
  }

  @Override
  public GPSLocationsDao gpsLocationsDao() {
    if (_gPSLocationsDao != null) {
      return _gPSLocationsDao;
    } else {
      synchronized(this) {
        if(_gPSLocationsDao == null) {
          _gPSLocationsDao = new GPSLocationsDao_Impl(this);
        }
        return _gPSLocationsDao;
      }
    }
  }

  @Override
  public HotlistDao hotlistDao() {
    if (_hotlistDao != null) {
      return _hotlistDao;
    } else {
      synchronized(this) {
        if(_hotlistDao == null) {
          _hotlistDao = new HotlistDao_Impl(this);
        }
        return _hotlistDao;
      }
    }
  }

  @Override
  public LocationGroupsDao locationGroupsDao() {
    if (_locationGroupsDao != null) {
      return _locationGroupsDao;
    } else {
      synchronized(this) {
        if(_locationGroupsDao == null) {
          _locationGroupsDao = new LocationGroupsDao_Impl(this);
        }
        return _locationGroupsDao;
      }
    }
  }

  @Override
  public LocationGroupLocationsDao locationGroupLocationsDao() {
    if (_locationGroupLocationsDao != null) {
      return _locationGroupLocationsDao;
    } else {
      synchronized(this) {
        if(_locationGroupLocationsDao == null) {
          _locationGroupLocationsDao = new LocationGroupLocationsDao_Impl(this);
        }
        return _locationGroupLocationsDao;
      }
    }
  }

  @Override
  public LocationsDao locationsDao() {
    if (_locationsDao != null) {
      return _locationsDao;
    } else {
      synchronized(this) {
        if(_locationsDao == null) {
          _locationsDao = new LocationsDao_Impl(this);
        }
        return _locationsDao;
      }
    }
  }

  @Override
  public LPRNotificationsDao lprNotificationsDao() {
    if (_lPRNotificationsDao != null) {
      return _lPRNotificationsDao;
    } else {
      synchronized(this) {
        if(_lPRNotificationsDao == null) {
          _lPRNotificationsDao = new LPRNotificationsDao_Impl(this);
        }
        return _lPRNotificationsDao;
      }
    }
  }

  @Override
  public MaintenanceLogsDao maintenanceLogsDao() {
    if (_maintenanceLogsDao != null) {
      return _maintenanceLogsDao;
    } else {
      synchronized(this) {
        if(_maintenanceLogsDao == null) {
          _maintenanceLogsDao = new MaintenanceLogsDao_Impl(this);
        }
        return _maintenanceLogsDao;
      }
    }
  }

  @Override
  public MaintenancePicturesDao maintenancePicturesDao() {
    if (_maintenancePicturesDao != null) {
      return _maintenancePicturesDao;
    } else {
      synchronized(this) {
        if(_maintenancePicturesDao == null) {
          _maintenancePicturesDao = new MaintenancePicturesDao_Impl(this);
        }
        return _maintenancePicturesDao;
      }
    }
  }

  @Override
  public MakesAndModelsDao makesAndModelsDao() {
    if (_makesAndModelsDao != null) {
      return _makesAndModelsDao;
    } else {
      synchronized(this) {
        if(_makesAndModelsDao == null) {
          _makesAndModelsDao = new MakesAndModelsDao_Impl(this);
        }
        return _makesAndModelsDao;
      }
    }
  }

  @Override
  public MessagesDao messagesDao() {
    if (_messagesDao != null) {
      return _messagesDao;
    } else {
      synchronized(this) {
        if(_messagesDao == null) {
          _messagesDao = new MessagesDao_Impl(this);
        }
        return _messagesDao;
      }
    }
  }

  @Override
  public MetersDao metersDao() {
    if (_metersDao != null) {
      return _metersDao;
    } else {
      synchronized(this) {
        if(_metersDao == null) {
          _metersDao = new MetersDao_Impl(this);
        }
        return _metersDao;
      }
    }
  }

  @Override
  public MobileNowLogsDao mobileNowLogsDao() {
    if (_mobileNowLogsDao != null) {
      return _mobileNowLogsDao;
    } else {
      synchronized(this) {
        if(_mobileNowLogsDao == null) {
          _mobileNowLogsDao = new MobileNowLogsDao_Impl(this);
        }
        return _mobileNowLogsDao;
      }
    }
  }

  @Override
  public ModulesDao modulesDao() {
    if (_modulesDao != null) {
      return _modulesDao;
    } else {
      synchronized(this) {
        if(_modulesDao == null) {
          _modulesDao = new ModulesDao_Impl(this);
        }
        return _modulesDao;
      }
    }
  }

  @Override
  public PermitsDao permitsDao() {
    if (_permitsDao != null) {
      return _permitsDao;
    } else {
      synchronized(this) {
        if(_permitsDao == null) {
          _permitsDao = new PermitsDao_Impl(this);
        }
        return _permitsDao;
      }
    }
  }

  @Override
  public PrintMacrosDao printMacrosDao() {
    if (_printMacrosDao != null) {
      return _printMacrosDao;
    } else {
      synchronized(this) {
        if(_printMacrosDao == null) {
          _printMacrosDao = new PrintMacrosDao_Impl(this);
        }
        return _printMacrosDao;
      }
    }
  }

  @Override
  public PrintTemplatesDao printTemplatesDao() {
    if (_printTemplatesDao != null) {
      return _printTemplatesDao;
    } else {
      synchronized(this) {
        if(_printTemplatesDao == null) {
          _printTemplatesDao = new PrintTemplatesDao_Impl(this);
        }
        return _printTemplatesDao;
      }
    }
  }

  @Override
  public RepeatOffendersDao repeatOffendersDao() {
    if (_repeatOffendersDao != null) {
      return _repeatOffendersDao;
    } else {
      synchronized(this) {
        if(_repeatOffendersDao == null) {
          _repeatOffendersDao = new RepeatOffendersDao_Impl(this);
        }
        return _repeatOffendersDao;
      }
    }
  }

  @Override
  public SpecialActivitiesDao specialActivitiesDao() {
    if (_specialActivitiesDao != null) {
      return _specialActivitiesDao;
    } else {
      synchronized(this) {
        if(_specialActivitiesDao == null) {
          _specialActivitiesDao = new SpecialActivitiesDao_Impl(this);
        }
        return _specialActivitiesDao;
      }
    }
  }

  @Override
  public SpecialActivityDispositionDao specialActivityDispositionDao() {
    if (_specialActivityDispositionDao != null) {
      return _specialActivityDispositionDao;
    } else {
      synchronized(this) {
        if(_specialActivityDispositionDao == null) {
          _specialActivityDispositionDao = new SpecialActivityDispositionDao_Impl(this);
        }
        return _specialActivityDispositionDao;
      }
    }
  }

  @Override
  public SpecialActivityReportsDao specialActivityReportsDao() {
    if (_specialActivityReportsDao != null) {
      return _specialActivityReportsDao;
    } else {
      synchronized(this) {
        if(_specialActivityReportsDao == null) {
          _specialActivityReportsDao = new SpecialActivityReportsDao_Impl(this);
        }
        return _specialActivityReportsDao;
      }
    }
  }

  @Override
  public SpecialActivityLocationDao specialActivityLocationDao() {
    if (_specialActivityLocationDao != null) {
      return _specialActivityLocationDao;
    } else {
      synchronized(this) {
        if(_specialActivityLocationDao == null) {
          _specialActivityLocationDao = new SpecialActivityLocationDao_Impl(this);
        }
        return _specialActivityLocationDao;
      }
    }
  }

  @Override
  public SpecialActivityPictureDao specialActivityPictureDao() {
    if (_specialActivityPictureDao != null) {
      return _specialActivityPictureDao;
    } else {
      synchronized(this) {
        if(_specialActivityPictureDao == null) {
          _specialActivityPictureDao = new SpecialActivityPictureDao_Impl(this);
        }
        return _specialActivityPictureDao;
      }
    }
  }

  @Override
  public StatesDao statesDao() {
    if (_statesDao != null) {
      return _statesDao;
    } else {
      synchronized(this) {
        if(_statesDao == null) {
          _statesDao = new StatesDao_Impl(this);
        }
        return _statesDao;
      }
    }
  }

  @Override
  public StreetPrefixesDao streetPrefixesDao() {
    if (_streetPrefixesDao != null) {
      return _streetPrefixesDao;
    } else {
      synchronized(this) {
        if(_streetPrefixesDao == null) {
          _streetPrefixesDao = new StreetPrefixesDao_Impl(this);
        }
        return _streetPrefixesDao;
      }
    }
  }

  @Override
  public StreetSuffixesDao streetSuffixesDao() {
    if (_streetSuffixesDao != null) {
      return _streetSuffixesDao;
    } else {
      synchronized(this) {
        if(_streetSuffixesDao == null) {
          _streetSuffixesDao = new StreetSuffixesDao_Impl(this);
        }
        return _streetSuffixesDao;
      }
    }
  }

  @Override
  public SyncDataDao syncDataDao() {
    if (_syncDataDao != null) {
      return _syncDataDao;
    } else {
      synchronized(this) {
        if(_syncDataDao == null) {
          _syncDataDao = new SyncDataDao_Impl(this);
        }
        return _syncDataDao;
      }
    }
  }

  @Override
  public TicketCommentsDao ticketCommentsDao() {
    if (_ticketCommentsDao != null) {
      return _ticketCommentsDao;
    } else {
      synchronized(this) {
        if(_ticketCommentsDao == null) {
          _ticketCommentsDao = new TicketCommentsDao_Impl(this);
        }
        return _ticketCommentsDao;
      }
    }
  }

  @Override
  public TicketCommentsDaoTemp ticketCommentsDaoTemp() {
    if (_ticketCommentsDaoTemp != null) {
      return _ticketCommentsDaoTemp;
    } else {
      synchronized(this) {
        if(_ticketCommentsDaoTemp == null) {
          _ticketCommentsDaoTemp = new TicketCommentsDaoTemp_Impl(this);
        }
        return _ticketCommentsDaoTemp;
      }
    }
  }

  @Override
  public TicketHistoryDao ticketHistoryDao() {
    if (_ticketHistoryDao != null) {
      return _ticketHistoryDao;
    } else {
      synchronized(this) {
        if(_ticketHistoryDao == null) {
          _ticketHistoryDao = new TicketHistoryDao_Impl(this);
        }
        return _ticketHistoryDao;
      }
    }
  }

  @Override
  public TicketPicturesDao ticketPicturesDao() {
    if (_ticketPicturesDao != null) {
      return _ticketPicturesDao;
    } else {
      synchronized(this) {
        if(_ticketPicturesDao == null) {
          _ticketPicturesDao = new TicketPicturesDao_Impl(this);
        }
        return _ticketPicturesDao;
      }
    }
  }

  @Override
  public TicketPicturesDaoTemp ticketPicturesDaoTemp() {
    if (_ticketPicturesDaoTemp != null) {
      return _ticketPicturesDaoTemp;
    } else {
      synchronized(this) {
        if(_ticketPicturesDaoTemp == null) {
          _ticketPicturesDaoTemp = new TicketPicturesDaoTemp_Impl(this);
        }
        return _ticketPicturesDaoTemp;
      }
    }
  }

  @Override
  public TicketsDao ticketsDao() {
    if (_ticketsDao != null) {
      return _ticketsDao;
    } else {
      synchronized(this) {
        if(_ticketsDao == null) {
          _ticketsDao = new TicketsDao_Impl(this);
        }
        return _ticketsDao;
      }
    }
  }

  @Override
  public TicketsDaoTemp ticketsDaoTemp() {
    if (_ticketsDaoTemp != null) {
      return _ticketsDaoTemp;
    } else {
      synchronized(this) {
        if(_ticketsDaoTemp == null) {
          _ticketsDaoTemp = new TicketsDaoTemp_Impl(this);
        }
        return _ticketsDaoTemp;
      }
    }
  }

  @Override
  public TicketViolationsDao ticketViolationsDao() {
    if (_ticketViolationsDao != null) {
      return _ticketViolationsDao;
    } else {
      synchronized(this) {
        if(_ticketViolationsDao == null) {
          _ticketViolationsDao = new TicketViolationsDao_Impl(this);
        }
        return _ticketViolationsDao;
      }
    }
  }

  @Override
  public TicketViolationsDaoTemp ticketViolationsDaoTemp() {
    if (_ticketViolationsDaoTemp != null) {
      return _ticketViolationsDaoTemp;
    } else {
      synchronized(this) {
        if(_ticketViolationsDaoTemp == null) {
          _ticketViolationsDaoTemp = new TicketViolationsDaoTemp_Impl(this);
        }
        return _ticketViolationsDaoTemp;
      }
    }
  }

  @Override
  public TPMEulaDao tpmEulaDao() {
    if (_tPMEulaDao != null) {
      return _tPMEulaDao;
    } else {
      synchronized(this) {
        if(_tPMEulaDao == null) {
          _tPMEulaDao = new TPMEulaDao_Impl(this);
        }
        return _tPMEulaDao;
      }
    }
  }

  @Override
  public UsersDao usersDao() {
    if (_usersDao != null) {
      return _usersDao;
    } else {
      synchronized(this) {
        if(_usersDao == null) {
          _usersDao = new UsersDao_Impl(this);
        }
        return _usersDao;
      }
    }
  }

  @Override
  public UserSettingsDao userSettingsDao() {
    if (_userSettingsDao != null) {
      return _userSettingsDao;
    } else {
      synchronized(this) {
        if(_userSettingsDao == null) {
          _userSettingsDao = new UserSettingsDao_Impl(this);
        }
        return _userSettingsDao;
      }
    }
  }

  @Override
  public VendorsDao vendorsDao() {
    if (_vendorsDao != null) {
      return _vendorsDao;
    } else {
      synchronized(this) {
        if(_vendorsDao == null) {
          _vendorsDao = new VendorsDao_Impl(this);
        }
        return _vendorsDao;
      }
    }
  }

  @Override
  public VendorItemsDao vendorItemsDao() {
    if (_vendorItemsDao != null) {
      return _vendorItemsDao;
    } else {
      synchronized(this) {
        if(_vendorItemsDao == null) {
          _vendorItemsDao = new VendorItemsDao_Impl(this);
        }
        return _vendorItemsDao;
      }
    }
  }

  @Override
  public VendorServicesDao vendorServicesDao() {
    if (_vendorServicesDao != null) {
      return _vendorServicesDao;
    } else {
      synchronized(this) {
        if(_vendorServicesDao == null) {
          _vendorServicesDao = new VendorServicesDao_Impl(this);
        }
        return _vendorServicesDao;
      }
    }
  }

  @Override
  public VendorServiceRegistrationsDao vendorServiceRegistrationsDao() {
    if (_vendorServiceRegistrationsDao != null) {
      return _vendorServiceRegistrationsDao;
    } else {
      synchronized(this) {
        if(_vendorServiceRegistrationsDao == null) {
          _vendorServiceRegistrationsDao = new VendorServiceRegistrationsDao_Impl(this);
        }
        return _vendorServiceRegistrationsDao;
      }
    }
  }

  @Override
  public ViolationGroupsDao violationGroupsDao() {
    if (_violationGroupsDao != null) {
      return _violationGroupsDao;
    } else {
      synchronized(this) {
        if(_violationGroupsDao == null) {
          _violationGroupsDao = new ViolationGroupsDao_Impl(this);
        }
        return _violationGroupsDao;
      }
    }
  }

  @Override
  public ViolationGroupViolationsDao violationGroupViolationsDao() {
    if (_violationGroupViolationsDao != null) {
      return _violationGroupViolationsDao;
    } else {
      synchronized(this) {
        if(_violationGroupViolationsDao == null) {
          _violationGroupViolationsDao = new ViolationGroupViolationsDao_Impl(this);
        }
        return _violationGroupViolationsDao;
      }
    }
  }

  @Override
  public ViolationsDao violationsDao() {
    if (_violationsDao != null) {
      return _violationsDao;
    } else {
      synchronized(this) {
        if(_violationsDao == null) {
          _violationsDao = new ViolationsDao_Impl(this);
        }
        return _violationsDao;
      }
    }
  }

  @Override
  public VoidTicketReasonsDao voidTicketReasonsDao() {
    if (_voidTicketReasonsDao != null) {
      return _voidTicketReasonsDao;
    } else {
      synchronized(this) {
        if(_voidTicketReasonsDao == null) {
          _voidTicketReasonsDao = new VoidTicketReasonsDao_Impl(this);
        }
        return _voidTicketReasonsDao;
      }
    }
  }

  @Override
  public ZonesDao zonesDao() {
    if (_zonesDao != null) {
      return _zonesDao;
    } else {
      synchronized(this) {
        if(_zonesDao == null) {
          _zonesDao = new ZonesDao_Impl(this);
        }
        return _zonesDao;
      }
    }
  }

  @Override
  public PlacardDao placardDao() {
    if (_placardDao != null) {
      return _placardDao;
    } else {
      synchronized(this) {
        if(_placardDao == null) {
          _placardDao = new PlacardDao_Impl(this);
        }
        return _placardDao;
      }
    }
  }

  @Override
  public GenetecPatrollerDao genetecPatrollerDao() {
    if (_genetecPatrollerDao != null) {
      return _genetecPatrollerDao;
    } else {
      synchronized(this) {
        if(_genetecPatrollerDao == null) {
          _genetecPatrollerDao = new GenetecPatrollerDao_Impl(this);
        }
        return _genetecPatrollerDao;
      }
    }
  }

  @Override
  public GenetecPatrollerActivitiesDao genetecPatrollerActivitiesDao() {
    if (_genetecPatrollerActivitiesDao != null) {
      return _genetecPatrollerActivitiesDao;
    } else {
      synchronized(this) {
        if(_genetecPatrollerActivitiesDao == null) {
          _genetecPatrollerActivitiesDao = new GenetecPatrollerActivitiesDao_Impl(this);
        }
        return _genetecPatrollerActivitiesDao;
      }
    }
  }
}
