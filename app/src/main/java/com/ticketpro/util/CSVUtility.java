package com.ticketpro.util;

import android.util.Log;

import com.ticketpro.model.LPRNotify;
import com.ticketpro.model.MobileNowLog;
import com.ticketpro.model.SystemBackup;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.parking.activity.TPApplication;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class CSVUtility {

    public static void writeTicketCSV(Ticket ticket) {

        File csvFile = TPUtility.getCurrentCSVFile("Tickets");
        CSVWriter writer = null;
        boolean columnsFlag = false;
        try {
            if (!csvFile.exists()) {
                columnsFlag = true;
            }

            writer = new CSVWriter(new FileWriter(csvFile, true));
            List<String[]> entries = new ArrayList<String[]>();

            if (columnsFlag) {
                entries.add(new String[]{
                        "ticket_id",
                        "custid",
                        "userid",
                        "device_id",
                        "citation_number",
                        "ticket_date",
                        "plate",
                        "vin",
                        "expiration",
                        "state_id",
                        "make_id",
                        "body_id",
                        "color_id",
                        "void_id",
                        "chalk_id",
                        "street_prefix",
                        "street_suffix",
                        "location",
                        "latitude",
                        "longitude",
                        "gpstime",
                        "is_gps_location",
                        "is_void",
                        "is_warn",
                        "is_chalked",
                        "is_driveaway",
                        "state_code",
                        "make_code",
                        "body_code",
                        "color_code",
                        "meter",
                        "permit",
                        "street_number",
                        "direction",
                        "fine",
                        "time_marked",
                        "space",
                        "violation_code",
                        "violation",
                        "export_date",
                        "void_reason_code",
                        "duty_id",
                        "is_lpr",
                        "violation_id",
                        "photo_count",
                        "lpr_notification_id",
                        "sync_completed",
                        "placard",
                        "duty_report_id",
                        "app_version"
                });
            }

            ArrayList<String> mStringList = new ArrayList<String>();
            mStringList.add(ticket.getTicketId() + "");
            mStringList.add(TPApplication.getInstance().custId + "");
            mStringList.add(ticket.getUserId() + "");
            mStringList.add(ticket.getDeviceId() + "");
            mStringList.add(TPUtility.prefixZeros(ticket.getCitationNumber(), 8));
            mStringList.add(DateUtil.getSQLStringFromDate2(ticket.getTicketDate()));
            mStringList.add(ticket.getPlate() + "");
            mStringList.add(ticket.getVin() + "");
            mStringList.add(TPUtility.getExpiration(ticket.getExpiration()));
            mStringList.add(ticket.getStateId() + "");
            mStringList.add(ticket.getMakeId() + "");
            mStringList.add(ticket.getBodyId() + "");
            mStringList.add(ticket.getColorId() + "");
            mStringList.add(ticket.getVoidId() + "");
            //mStringList.add(ticket.getCustId()+"");
            mStringList.add(ticket.getChalkId() + "");
            mStringList.add(ticket.getStreetPrefix() + "");
            mStringList.add(ticket.getStreetSuffix() + "");
            mStringList.add(ticket.getLocation() + "");
            mStringList.add(ticket.getLatitude() + "");
            mStringList.add(ticket.getLongitude() + "");
            mStringList.add(DateUtil.getSQLStringFromDate2(ticket.getGpstime()) + "");
            mStringList.add(ticket.getIsGPSLocation() + "");
            mStringList.add(ticket.getIsVoid() + "");
            mStringList.add(ticket.getIsWarn() + "");
            mStringList.add(ticket.getIsChalked() + "");
            mStringList.add(ticket.getIsDriveAway() + "");
            mStringList.add(ticket.getStateCode() + "");
            mStringList.add(ticket.getMakeCode() + "");
            mStringList.add(ticket.getBodyCode() + "");
            mStringList.add(ticket.getColorCode() + "");
            mStringList.add(ticket.getMeter() + "");
            mStringList.add(ticket.getPermit() + "");
            mStringList.add(ticket.getStreetNumber() + "");
            mStringList.add(ticket.getDirection() + "");
            mStringList.add(ticket.getFine() + "");
            mStringList.add(DateUtil.getSQLStringFromDate2(ticket.getTimeMarked()));
            mStringList.add(ticket.getSpace() + "");
            mStringList.add(ticket.getViolationCode() + "");
            mStringList.add(ticket.getViolation() + "");
            mStringList.add(""); //Export Date
            mStringList.add(ticket.getVoidReasonCode() + "");
            mStringList.add(ticket.getDutyId() + "");
            mStringList.add(ticket.getIsLPR() + "");
            mStringList.add(ticket.getViolationId() + "");
            mStringList.add(ticket.getPhotoCount() + "");
            mStringList.add(ticket.getLprNotificationId() + "");
            mStringList.add(ticket.getStatus() + "");
            mStringList.add(ticket.getPlacard() + "");
            mStringList.add(ticket.getDutyReportId() + "");
            mStringList.add(ticket.getAppVersion() + "");
            //Convert to Array
            String[] mStringArray = new String[mStringList.size()];
            mStringArray = mStringList.toArray(mStringArray);
            entries.add(mStringArray);

            writer.writeAll(entries);


        } catch (IOException e) {
            Log.e("CSVWriter", "Error " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }


    public static void writeViolationCSV(ArrayList<TicketViolation> violations, long citationNumber) {
        File csvFile = TPUtility.getCurrentCSVFile("Violations");
        CSVWriter writer = null;
        boolean columnsFlag = false;
        try {
            if (!csvFile.exists()) {
                columnsFlag = true;
            }
            writer = new CSVWriter(new FileWriter(csvFile, true));
            List<String[]> entries = new ArrayList<String[]>();

            if (columnsFlag) {
                entries.add(new String[]{
                        "ticket_violation_id",
                        "violation_id",
                        "ticket_id",
                        "citation_number",
                        "violation_code",
                        "fine",
                        "custid"});
            }

            for (TicketViolation violation : violations) {
                ArrayList<String> mStringList = new ArrayList<String>();
                mStringList.add(violation.getTicketViolationId() + "");
                mStringList.add(violation.getViolationId() + "");
                mStringList.add(violation.getTicketId() + "");
                mStringList.add(TPUtility.prefixZeros(citationNumber, 8));
                mStringList.add(violation.getViolationCode() + "");
                mStringList.add(violation.getFine() + "");
                mStringList.add(TPApplication.getInstance().custId + "");
                //Convert to Array
                String[] mStringArray = new String[mStringList.size()];
                mStringArray = mStringList.toArray(mStringArray);
                entries.add(mStringArray);

                CSVUtility.writeCommentCSV(violation.getTicketComments(),citationNumber);
                citationNumber = citationNumber + 1;
            }
            writer.writeAll(entries);
        } catch (IOException e) {
            Log.e("CSVWriter", "Error " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }


    public static void writeCommentCSV(ArrayList<TicketComment> comments, long citationNumber) {

        File csvFile = TPUtility.getCurrentCSVFile("Comments");
        CSVWriter writer = null;
        boolean columnsFlag = false;
        try {
            if (!csvFile.exists()) {
                columnsFlag = true;
            }

            writer = new CSVWriter(new FileWriter(csvFile, true));
            List<String[]> entries = new ArrayList<String[]>();

            if (columnsFlag) {
                entries.add(new String[]{
                        "ticket_comment_id",
                        "comment_id",
                        "ticket_id",
                        "citation_number",
                        "comment",
                        "is_private",
                        "custid"});
            }


            for (TicketComment comment : comments) {
                ArrayList<String> mStringList = new ArrayList<String>();

                mStringList.add(comment.getTicketCommentId() + "");
                mStringList.add(comment.getCommentId() + "");
                mStringList.add(comment.getTicketId() + "");
                mStringList.add(TPUtility.prefixZeros(citationNumber, 8));
                mStringList.add(comment.getComment() + "");
                mStringList.add(comment.isPrivate() + "");
                mStringList.add(TPApplication.getInstance().custId + "");
                //Convert to Array
                String[] mStringArray = new String[mStringList.size()];
                mStringArray = mStringList.toArray(mStringArray);
                entries.add(mStringArray);
            }

            writer.writeAll(entries);

        } catch (IOException e) {
            Log.e("CSVWriter", "Error " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }


    public static void writePictureCSV(ArrayList<TicketPicture> pictures) {

        File csvFile = TPUtility.getCurrentCSVFile("Pictures");
        CSVWriter writer = null;
        boolean columnsFlag = false;
        try {
            if (!csvFile.exists()) {
                columnsFlag = true;
            }

            writer = new CSVWriter(new FileWriter(csvFile, true));
            List<String[]> entries = new ArrayList<String[]>();

            if (columnsFlag) {
                entries.add(new String[]{
                        "picture_id",
                        "ticket_id",
                        "citation_number",
                        "picture_date",
                        "mark_print",
                        "image_path",
                        "image_resolution",
                        "image_size",
                        "sync_status",
                        "download_image_url",
                        "image_name",
                        "edit",
                        "custid"});
            }


            for (TicketPicture picture : pictures) {
                ArrayList<String> mStringList = new ArrayList<String>();
                mStringList.add(picture.getPictureId() + "");
                mStringList.add(picture.getTicketId() + "");
                mStringList.add(TPUtility.prefixZeros(picture.getCitationNumber(), 8));
                mStringList.add(DateUtil.getSQLStringFromDate(picture.getPictureDate()) + "");
                mStringList.add(picture.getMarkPrint() + "");

                //Remove path, update filename only.
                if (picture.getImagePath() != null) {
                    String[] tokens = picture.getImagePath().split("/");
                    if (tokens.length > 0) {
                        mStringList.add(tokens[tokens.length - 1] + "");
                    } else {
                        mStringList.add(picture.getImagePath() + "");
                    }
                } else {
                    mStringList.add("");
                }

                mStringList.add(picture.getImageResolution() + "");
                mStringList.add(picture.getImageSize() + "");
                mStringList.add(picture.getSyncStatus() + "");
                mStringList.add(picture.getDownloadImageUrl() + "");
                mStringList.add(picture.getImageName() + "");
                mStringList.add(picture.getIsEdit() + "");
                mStringList.add(TPApplication.getInstance().custId + "");

                //Convert to Array
                String[] mStringArray = new String[mStringList.size()];
                mStringArray = mStringList.toArray(mStringArray);
                entries.add(mStringArray);
            }

            writer.writeAll(entries);

        } catch (IOException e) {
            Log.e("CSVWriter", "Error " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }


    public static void writeLPRNotifyCSV(LPRNotify lprNotify) {

        File csvFile = TPUtility.getLPRNotifyCSVFile();
        CSVWriter writer = null;
        boolean columnsFlag = false;
        try {
            if (!csvFile.exists()) {
                columnsFlag = true;
            }

            writer = new CSVWriter(new FileWriter(csvFile, true));
            List<String[]> entries = new ArrayList<String[]>();

            if (columnsFlag) {
                entries.add(new String[]{
                        "notification_id",
                        "plate",
                        "state",
                        "make",
                        "model",
                        "body",
                        "status",
                        "location",
                        "latitude",
                        "longitude",
                        "photo1",
                        "photo2",
                        "lpr_scan_id",
                        "lpr_camera_id",
                        "lpr_user_id",
                        "color",
                        "permit",
                        "permit_status",
                        "permit_type",
                        "duty_group",
                        "comments",
                        "violation_desc",
                        "violation_code",
                        "violation_id",
                        "date_notify"});
            }

            ArrayList<String> mStringList = new ArrayList<String>();

            mStringList.add(lprNotify.getNotificationId() + "");
            mStringList.add(lprNotify.getPlate() + "");
            mStringList.add(lprNotify.getState() + "");
            mStringList.add(lprNotify.getMake() + "");
            mStringList.add(lprNotify.getModel() + "");
            mStringList.add(lprNotify.getBody() + "");
            mStringList.add(lprNotify.getStatus() + "");
            mStringList.add(lprNotify.getLocation() + "");
            mStringList.add(lprNotify.getLatitude() + "");
            mStringList.add(lprNotify.getLongitude() + "");
            mStringList.add(lprNotify.getPhoto1() + "");
            mStringList.add(lprNotify.getPhoto2() + "");
            mStringList.add(lprNotify.getLprScanId() + "");
            mStringList.add(lprNotify.getLprCameraId() + "");
            mStringList.add(lprNotify.getLprUserId() + "");
            mStringList.add(lprNotify.getColor() + "");
            mStringList.add(lprNotify.getPermit() + "");
            mStringList.add(lprNotify.getPermitStatus() + "");
            mStringList.add(lprNotify.getPermitType() + "");
            mStringList.add(lprNotify.getDutyGroup() + "");
            mStringList.add(lprNotify.getComments() + "");
            mStringList.add(lprNotify.getViolationDesc() + "");
            mStringList.add(lprNotify.getViolationCode() + "");
            mStringList.add(lprNotify.getViolationId() + "");

            mStringList.add(DateUtil.getSQLStringFromDate(lprNotify.getNotifyDate()) + "");

            //Convert to Array
            String[] mStringArray = new String[mStringList.size()];
            mStringArray = mStringList.toArray(mStringArray);
            entries.add(mStringArray);

            writer.writeAll(entries);

        } catch (IOException e) {
            Log.e("CSVWriter", "Error " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }


    public static List<LPRNotify> getLPRNotifyCSV() {
        File csvFile = TPUtility.getLPRNotifyCSVFile();
        CSVReader reader = null;
        List<LPRNotify> list = new ArrayList<LPRNotify>();

        try {
            reader = new CSVReader(new FileReader(csvFile));
            String[] record = null;
            reader.readNext();

            while ((record = reader.readNext()) != null) {
                LPRNotify lprNotify = new LPRNotify();
                lprNotify.setNotificationId(record[0]);
                lprNotify.setPlate(record[1]);
                lprNotify.setState(record[2]);
                lprNotify.setMake(record[3]);
                lprNotify.setModel(record[4]);
                lprNotify.setBody(record[5]);
                lprNotify.setStatus(record[6]);
                lprNotify.setLocation(record[7]);
                lprNotify.setLatitude(record[8]);
                lprNotify.setLongitude(record[9]);
                lprNotify.setPhoto1(record[10]);
                lprNotify.setPhoto2(record[11]);
                lprNotify.setLprScanId(record[12]);
                lprNotify.setLprCameraId(record[13]);
                lprNotify.setLprUserId(record[14]);
                lprNotify.setColor(record[15]);
                lprNotify.setPermit(record[16]);
                lprNotify.setPermitStatus(record[17]);
                lprNotify.setPermitType(record[18]);
                lprNotify.setDutyGroup(record[19]);
                lprNotify.setComments(record[20]);
                lprNotify.setViolationDesc(record[21]);
                lprNotify.setViolationCode(record[22]);
                lprNotify.setViolationId(record[23]);
                lprNotify.setNotifyDate(DateUtil.getDateFromSQLString(record[24]));

                list.add(0, lprNotify);
            }

        } catch (IOException e) {
            Log.e("CSVReader", "Error " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }

        return list;
    }

    public static boolean deleteLPRNotifyCSV() {
        File csvFile = TPUtility.getLPRNotifyCSVFile();
        if (csvFile.exists()) {
            return csvFile.delete();
        }

        return false;
    }

    public static void writeSystemBackupCSV(Ticket ticket) {
        File csvFile = TPUtility.getSystemBackupCSVFile();
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(csvFile, false));
            List<String[]> entries = new ArrayList<String[]>();

            entries.add(new String[]{
                    "citation_number",
                    "backup_date"
            });

            ArrayList<String> mStringList = new ArrayList<String>();
            mStringList.add(TPUtility.prefixZeros(ticket.getCitationNumber(), 8));
            mStringList.add(DateUtil.getSQLStringFromDate2(new Date()) + "");

            //Convert to Array
            String[] mStringArray = new String[mStringList.size()];
            mStringArray = mStringList.toArray(mStringArray);
            entries.add(mStringArray);

            writer.writeAll(entries);

        } catch (IOException e) {
            Log.e("CSVWriter", "Error " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }


    public static SystemBackup getSystemBackupCSV() {
        File csvFile = TPUtility.getSystemBackupCSVFile();

        CSVReader reader = null;
        SystemBackup backup = null;
        if (!csvFile.exists()) {
            try {
                csvFile.createNewFile();
            } catch (IOException e) {
                Log.e("CSVReader", "Error " + e.getMessage());
            }
        }

        try {
            reader = new CSVReader(new FileReader(csvFile));
            String[] record = null;
            reader.readNext();

            if ((record = reader.readNext()) != null) {
                backup = new SystemBackup();
                backup.setCitationNumber(record[0]);
                backup.setBackupDate(DateUtil.getDateFromSQLString(record[1]));
            }
        } catch (IOException e) {
            Log.e("CSVReader", "Error " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }

        return backup;
    }

    public static void writeNetworkLogCSV(String process, Date startDate, Date endDate, String GPS, String status) {
        File csvFile = TPUtility.getNetworkLogsCSVFile();
        CSVWriter writer = null;
        boolean columnsFlag = false;
        try {
            if (!csvFile.exists()) {
                columnsFlag = true;
            }

            writer = new CSVWriter(new FileWriter(csvFile, true));
            List<String[]> entries = new ArrayList<String[]>();

            if (columnsFlag) {
                entries.add(new String[]{
                        "Date",
                        "Process",
                        "StartDate",
                        "EndDate",
                        "Duration(MS)",
                        "Location",
                        "Status"
                });
            }

            ArrayList<String> mStringList = new ArrayList<String>();
            mStringList.add(DateUtil.getCurrentDateTime());
            mStringList.add(process);
            mStringList.add(DateUtil.getStringFromDate2(startDate));
            mStringList.add(DateUtil.getStringFromDate2(endDate));
            mStringList.add((endDate.getTime() - startDate.getTime()) + "");
            mStringList.add(GPS);
            mStringList.add(status);

            //Convert to Array
            String[] mStringArray = new String[mStringList.size()];
            mStringArray = mStringList.toArray(mStringArray);
            entries.add(mStringArray);

            writer.writeAll(entries);

        } catch (IOException e) {
            Log.e("CSVWriter", "Error " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }


    public static void writeMobileLogCSV(MobileNowLog mobileNowLog) {
        File csvFile = TPUtility.getMobileLogsCSVFile();
        CSVWriter writer = null;
        boolean columnsFlag = false;
        try {
            if (!csvFile.exists()) {
                columnsFlag = true;
            }

            writer = new CSVWriter(new FileWriter(csvFile, true));
            List<String[]> entries = new ArrayList<String[]>();

            if (columnsFlag) {
                entries.add(new String[]{
                        "request_date",
                        "request_params",
                        "service_mode",
                        "response_text",
                        "userid",
                        "custid",
                        "device_id",
                        "plate_number"
                });
            }

            ArrayList<String> mStringList = new ArrayList<String>();
            mStringList.add(String.valueOf(mobileNowLog.getRequestDate()));
            mStringList.add(mobileNowLog.getRequestParams());
            mStringList.add(mobileNowLog.getServiceMode());
            mStringList.add(mobileNowLog.getResponseText());
            mStringList.add(String.valueOf(mobileNowLog.getUserId()));
            mStringList.add(String.valueOf(TPApplication.getInstance().custId));
            mStringList.add(String.valueOf(mobileNowLog.getDeviceId()));
            mStringList.add(mobileNowLog.getPlate_number());

            //Convert to Array
            String[] mStringArray = new String[mStringList.size()];
            mStringArray = mStringList.toArray(mStringArray);
            entries.add(mStringArray);

            writer.writeAll(entries);

        } catch (IOException e) {
            Log.e("CSVWriter", "Error " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }

}