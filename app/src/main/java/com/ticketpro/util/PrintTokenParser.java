package com.ticketpro.util;

import android.content.Context;

import com.ticketpro.model.Body;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Color;
import com.ticketpro.model.CustomerInfo;
import com.ticketpro.model.Duration;
import com.ticketpro.model.Duty;
import com.ticketpro.model.Feature;
import com.ticketpro.model.MakeAndModel;
import com.ticketpro.model.PrintMacro;
import com.ticketpro.model.State;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.model.User;
import com.ticketpro.print.TicketPrinter;

import org.apache.log4j.Logger;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class PrintTokenParser {

    private Ticket printTicket;
    private String printTemplate;
    private boolean isPreview = false;
    private boolean isPreviousTicket = false;
    private boolean isSpecialTemplate = false;
    private boolean isMultiPrint = false;
    private boolean isTicketHistory = false;

    private Logger log = Logger.getLogger("PrintTokenParser");

    public PrintTokenParser(Ticket ticket, String printTemplate) {
        this.printTicket = ticket;
        this.printTemplate = printTemplate;
    }

    public PrintTokenParser(Ticket ticket, String printTemplate, boolean isPreviousTicket) {
        this.printTicket = ticket;
        this.printTemplate = printTemplate;
        this.isPreviousTicket = isPreviousTicket;
    }

    public void applyPrintSettings(Context context) {
        String methodName = TicketPrinter.getSelectedMethod(context);
        if (TicketPrinter.COMMUNICATION_METHOD_PRINTSERVICE.equalsIgnoreCase(methodName)) {

        }
    }

    public String parseTicket() {
        ArrayList<String> printTickets = parseTickets();
        StringBuffer ticketString = new StringBuffer();

        for (String str : printTickets) {
            ticketString.append(str);
        }

        return ticketString.toString();
    }

    public ArrayList<String> parseTickets() {
        ArrayList<String> printTickets = new ArrayList<String>();
        DecimalFormat dec = new DecimalFormat("0.00");
        dec.setRoundingMode(RoundingMode.DOWN);

        for (TicketViolation violation : printTicket.getTicketViolations()) {
            try {
                String printTemplate = TPUtility.escapeSpecialChars(this.printTemplate);
                printTemplate = printTemplate.replaceAll("\\{CITE\\}", TPUtility.prefixZeros(violation.getCitationNumber(), 8));
                printTemplate = printTemplate.replaceAll("\\{DATE\\}", DateUtil.getStringFromDate(printTicket.getTicketDate()) + "");
                printTemplate = printTemplate.replaceAll("\\{CITE_DATE\\}", DateUtil.getDateStringFromDate(printTicket.getTicketDate()) + "");
                printTemplate = printTemplate.replaceAll("\\{CITE_TIME\\}", DateUtil.getTimeStringFromDate(printTicket.getTicketDate()) + "");
                printTemplate = printTemplate.replaceAll("\\{METER\\}", printTicket.getMeter() + "");
                printTemplate = printTemplate.replaceAll("\\{LOCATION\\}", TPUtility.getFullAddress(printTicket) + "");
                printTemplate = printTemplate.replaceAll("\\{VIOLATION\\}", TPUtility.escapeSpecialChars(violation.getViolationDesc()));
                printTemplate = printTemplate.replaceAll("\\{PLATE\\}", printTicket.getPlate() + "");
                printTemplate = printTemplate.replaceAll("\\{EXPDATE\\}", printTicket.getExpiration() + "");
                printTemplate = printTemplate.replaceAll("\\{VIN\\}", printTicket.getVin() + "");
                printTemplate = printTemplate.replaceAll("\\{STATE_CODE\\}", printTicket.getStateCode() + "");
                printTemplate = printTemplate.replaceAll("\\{MAKE_CODE\\}", printTicket.getMakeCode() + "");
                printTemplate = printTemplate.replaceAll("\\{BODY_CODE\\}", printTicket.getBodyCode() + "");
                printTemplate = printTemplate.replaceAll("\\{COLOR_CODE\\}", printTicket.getColorCode() + "");

                if (!StringUtil.isEmpty(printTicket.getStateCode())) {
                    State state = State.getStateByName(printTicket.getStateCode());
                    if (state != null) {
                        printTemplate = printTemplate.replaceAll("\\{STATE\\}", state.getTitle());
                    }
                }

                printTemplate = printTemplate.replaceAll("\\{STATE\\}", "");

                if (!StringUtil.isEmpty(printTicket.getMakeCode())) {
                    MakeAndModel make = MakeAndModel.getMakeByCode(printTicket.getMakeCode());
                    if (make != null) {
                        printTemplate = printTemplate.replaceAll("\\{MAKE\\}", make.getMakeTitle());
                    }
                } else if (!StringUtil.isEmpty(printTicket.getMakeTitle())) {
                    printTemplate = printTemplate.replaceAll("\\{MAKE\\}", printTicket.getMakeTitle());
                }

                printTemplate = printTemplate.replaceAll("\\{MAKE\\}", "");

                if (!StringUtil.isEmpty(printTicket.getBodyCode())) {
                    Body body = Body.getBodyByCode(printTicket.getBodyCode());
                    if (body != null) {
                        printTemplate = printTemplate.replaceAll("\\{BODY\\}", body.getTitle());
                    }
                } else if (!StringUtil.isEmpty(printTicket.getBodyTitle())) {
                    printTemplate = printTemplate.replaceAll("\\{BODY\\}", printTicket.getBodyTitle());
                }

                printTemplate = printTemplate.replaceAll("\\{BODY\\}", "");

                if (!StringUtil.isEmpty(printTicket.getColorCode())) {
                    Color color = Color.getColorByCode(printTicket.getColorCode());
                    if (color != null) {
                        printTemplate = printTemplate.replaceAll("\\{COLOR\\}", color.getTitle());
                    }
                } else if (!StringUtil.isEmpty(printTicket.getBodyTitle())) {
                    printTemplate = printTemplate.replaceAll("\\{COLOR\\}", printTicket.getColorTitle());
                }

                printTemplate = printTemplate.replaceAll("\\{COLOR\\}", "");

                printTemplate = printTemplate.replaceAll("\\{PERMIT\\}", printTicket.getPermit() + "");
                printTemplate = printTemplate.replaceAll("\\{VIOLATION_CODE\\}", violation.getViolationCode() + "");
                printTemplate = printTemplate.replaceAll("\\{FULL_LOC\\}", TPUtility.getFullAddress(printTicket));
                printTemplate = printTemplate.replaceAll("\\{LOC_BLOCK\\}", printTicket.getStreetNumber());
                printTemplate = printTemplate.replaceAll("\\{LOC_DIRECTION\\}", printTicket.getDirection());
                printTemplate = printTemplate.replaceAll("\\{LOC_PREFIX\\}", printTicket.getStreetPrefix());
                printTemplate = printTemplate.replaceAll("\\{LOC_SUFFIX\\}", printTicket.getStreetSuffix());
                printTemplate = printTemplate.replaceAll("\\{LONG_LAT\\}", printTicket.getLongitude() + " - " + printTicket.getLatitude());
                printTemplate = printTemplate.replaceAll("\\{LONG\\}", printTicket.getLongitude());
                printTemplate = printTemplate.replaceAll("\\{LAT\\}", printTicket.getLatitude());
                printTemplate = printTemplate.replaceAll("\\{USERID\\}", printTicket.getUserId() + "");
                printTemplate = printTemplate.replaceAll("\\{MARKED\\}", DateUtil.getStringFromDate(printTicket.getTimeMarked()) + "");
                printTemplate = printTemplate.replaceAll("\\{MARKED_DATE\\}", DateUtil.getDateStringFromDate(printTicket.getTimeMarked()) + "");
                printTemplate = printTemplate.replaceAll("\\{MARKED_TIME\\}", DateUtil.getTimeStringFromDate(printTicket.getTimeMarked()) + "");

                String agencyCode = "";
                String webAddress = "";
                CustomerInfo customerInfo = CustomerInfo.getCustomerInfo(printTicket.getCustId());
                if (customerInfo != null) {
                    agencyCode = customerInfo.getAgencyCode();
                    webAddress = customerInfo.getWebAddress();

                    printTemplate = printTemplate.replaceAll("\\{CUST_TICKET_COLOR\\}", customerInfo.getTicketColor());
                    printTemplate = printTemplate.replaceAll("\\{CUST_TICKET_BACK\\}", customerInfo.getTicketBack());
                    printTemplate = printTemplate.replaceAll("\\{CUST_COURT_NAME\\}", customerInfo.getTRCourtName());
                    printTemplate = printTemplate.replaceAll("\\{CUST_PRINT_AGENCY\\}", customerInfo.getTRPrintAgencyName());
                }

                printTemplate = printTemplate.replaceAll("\\{AGENCY_CODE\\}", agencyCode);
                printTemplate = printTemplate.replaceAll("\\{WEBADDRESS\\}", webAddress);

                if (printTicket.isVoided() || (isPreviousTicket && printTicket.isWarn()) || (!isPreviousTicket && violation.isWarning())) {
                    printTemplate = printTemplate.replaceAll("\\{FINE\\}", "\\$0");

                    // Remove QR Code
                    try {
                        int indexOfQR = printTemplate.indexOf("QR_BC");
                        if (indexOfQR != -1) {
                            int indexOfStartData = printTemplate.indexOf("|", indexOfQR);
                            int indexOfEndData = printTemplate.indexOf("|", indexOfStartData + 1);
                            String data = printTemplate.substring(indexOfStartData + 1, indexOfEndData);
                            printTemplate = printTemplate.replace("QR_BC", "");
                            printTemplate = printTemplate.replace(data, "");
                        }
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                } else {
                    printTemplate = printTemplate.replaceAll("\\{FINE\\}", "\\$" + dec.format(violation.getFine()));
                }

                ArrayList<TicketComment> ticketComments = TPUtility.getPrintableComments(violation.getTicketComments());
                if (ticketComments.size() > 0) {
                    printTemplate = printTemplate.replaceAll("\\{COMMENTS\\}", TPUtility.escapeSpecialChars(ticketComments.get(0).getComment()));
                    printTemplate = printTemplate.replaceAll("\\{COMMENT1\\}", TPUtility.escapeSpecialChars(ticketComments.get(0).getComment()));

                    if (ticketComments.size() > 1) {
                        printTemplate = printTemplate.replaceAll("\\{COMMENT2\\}", TPUtility.escapeSpecialChars(ticketComments.get(1).getComment()));
                    }
                }

                printTemplate = printTemplate.replaceAll("\\{COMMENTS\\}", "");
                printTemplate = printTemplate.replaceAll("\\{COMMENT1\\}", "");
                printTemplate = printTemplate.replaceAll("\\{COMMENT2\\}", "");

                if (printTicket.isVoided()) {
                    printTemplate = printTemplate.replaceAll("\\{VOIDMSG\\}", PrintMacro.getPrintMacroMessageByName("VOIDMSG"));
                } else {
                    printTemplate = printTemplate.replaceAll("\\{VOIDMSG\\}", "");
                }

                if ((isPreviousTicket && printTicket.isWarn()) || (!isPreviousTicket && violation.isWarning())) {
                    printTemplate = printTemplate.replaceAll("\\{WARNMSG\\}", PrintMacro.getPrintMacroMessageByName("WARNMSG"));
                } else {
                    printTemplate = printTemplate.replaceAll("\\{WARNMSG\\}", "");
                }

                if (printTicket.getTicketPictures().size() > 0) {
                    printTemplate = printTemplate.replaceAll("\\{PHOTOMSG\\}", PrintMacro.getPrintMacroMessageByName("PHOTOMSG"));
                } else {
                    printTemplate = printTemplate.replaceAll("\\{PHOTOMSG\\}", "");
                }

                if (isPreviousTicket && Feature.isFeatureAllowed(Feature.TICKET_COPY)) {
                    printTemplate = printTemplate.replaceAll("\\{COPY_MSG\\}", PrintMacro.getPrintMacroMessageByName("COPY_MSG"));
                } else {
                    printTemplate = printTemplate.replaceAll("\\{COPY_MSG\\}", "");
                }

                Duty duty = Duty.getDutyById(printTicket.getDutyId());
                if (duty != null) {
                    printTemplate = printTemplate.replaceAll("\\{DUTY\\}", duty.getTitle());
                } else {
                    printTemplate = printTemplate.replaceAll("\\{DUTY\\}", "");
                }

                if (printTicket.getExpiration() != null && printTicket.getExpiration().contains("/")) {
                    try {
                        String[] expArray = printTicket.getExpiration().split("/");
                        printTemplate = printTemplate.replaceAll("\\{EXP_MONTH\\}", expArray[0]);
                        printTemplate = printTemplate.replaceAll("\\{EXP_YEAR\\}", expArray[1]);
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }

                printTemplate = printTemplate.replaceAll("\\{EXP_MONTH\\}", "");
                printTemplate = printTemplate.replaceAll("\\{EXP_YEAR\\}", "");

                User userInfo = User.getUserInfo(printTicket.getUserId());
                if (userInfo != null) {
                    printTemplate = printTemplate.replaceAll("\\{BADGE\\}", userInfo.getBadge() + "");
                    printTemplate = printTemplate.replaceAll("\\{OFFICER_NAME\\}", userInfo.getPrint_name() + "");
                    printTemplate = printTemplate.replaceAll("\\{DEPT\\}", userInfo.getDepartment() + "");
                    printTemplate = printTemplate.replaceAll("\\{FIRST_NAME\\}", userInfo.getFirstName() + "");
                    printTemplate = printTemplate.replaceAll("\\{LAST_NAME\\}", userInfo.getLastName() + "");
                } else {
                    printTemplate = printTemplate.replaceAll("\\{BADGE\\}", "");
                    printTemplate = printTemplate.replaceAll("\\{OFFICER_NAME\\}", "");
                    printTemplate = printTemplate.replaceAll("\\{DEPT\\}", "");
                    printTemplate = printTemplate.replaceAll("\\{FIRST_NAME\\}", "");
                    printTemplate = printTemplate.replaceAll("\\{LAST_NAME\\}", "");
                }

                if (printTicket.isChalked() && printTicket.getChalkId() > 0) {
                    if (!StringUtil.isEmpty(printTicket.getTimeZone()) && !StringUtil.isEmpty(printTicket.getElapsed())) {
                        printTemplate = printTemplate.replaceAll("\\{TIME_ZONE\\}", printTicket.getTimeZone());
                        printTemplate = printTemplate.replaceAll("\\{ELAPSED\\}", printTicket.getElapsed());
                    } else {
                        try {
                            ChalkVehicle chalk = ChalkVehicle.getChalkVehicleById(printTicket.getChalkId());
                            if (chalk != null) {
                                Date dt = printTicket.getTicketDate();
                                if (dt == null) {
                                    dt = new Date();
                                }

                                String durationTitle = Duration.getDurationTitleById(chalk.getDurationId());
                                long milliseconds = (printTicket.getTicketDate().getTime() - chalk.getChalkDate().getTime());
                                int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                                int hours = (int) ((milliseconds / (1000 * 60 * 60)));

                                String hrs = (hours < 10) ? ("0" + hours) : hours + "";
                                String mins = (minutes < 10) ? ("0" + minutes) : minutes + "";
                                printTemplate = printTemplate.replaceAll("\\{ELAPSED\\}", hrs + ":" + mins + " hrs/min");
                                printTemplate = printTemplate.replaceAll("\\{TIME_ZONE\\}", durationTitle);
                            }
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }
                    }
                }

                if (printTicket.getSpace() != null && printTicket.getSpace().length() > 0) {
                    printTemplate = printTemplate.replaceAll("\\{SPACE\\}", printTicket.getSpace() + ""); // Space
                    // From
                    // Chalk
                    // Vehicle
                } else {
                    printTemplate = printTemplate.replaceAll("\\{SPACE\\}", "");
                }

                if (isTicketHistory) {
                    printTemplate = printTemplate.replaceAll("\\{CUST_MSG1\\}", PrintMacro.getPrintMacroMessageByName("CUST_MSG1"));
                } else {
                    printTemplate = printTemplate.replaceAll("\\{CUST_MSG1\\}", "");
                }

                printTemplate = printTemplate.replaceAll("\\{ELAPSED\\}", "");
                printTemplate = printTemplate.replaceAll("\\{TIME_ZONE\\}", "");
                printTemplate = applyPrintMacros(printTemplate);

                if (customerInfo != null) {
                    printTemplate = printTemplate.replaceAll("\\{CUSTOMER\\}", customerInfo.getName());
                    printTemplate = printTemplate.replaceAll("\\{CUST_ADDRESS\\}", customerInfo.getAddress());
                    printTemplate = printTemplate.replaceAll("\\{CUST_PHONE\\}", customerInfo.getContactNumber());
                } else {
                    printTemplate = printTemplate.replaceAll("\\{CUSTOMER\\}", "");
                    printTemplate = printTemplate.replaceAll("\\{CUST_ADDRESS\\}", "");
                    printTemplate = printTemplate.replaceAll("\\{CUST_PHONE\\}", "");
                }

                // Empty Tokens
                printTemplate = printTemplate.replaceAll("\\{COPY_MSG\\}", "");
                printTemplate = printTemplate.replaceAll("\\{CUST_MSG1\\}", "");
                printTemplate = printTemplate.replaceAll("\\{CUST_MSG2\\}", "");
                printTemplate = printTemplate.replaceAll("\\{CUST_MSG3\\}", "");
                printTemplate = printTemplate.replaceAll("\\{PERMIT_EXPIRE\\}", "");

                printTemplate = printTemplate.replaceAll("null", "");

                printTickets.add(printTemplate);

                // Exit if printing special template
                if (isSpecialTemplate) {
                    break;
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        }

        return printTickets;
    }

    private String applyPrintMacros(String printTemplate) {
        try {
            ArrayList<PrintMacro> macros = PrintMacro.getPrintMacros();
            for (PrintMacro macro : macros) {
                printTemplate = printTemplate.replaceAll("\\{" + macro.getMacroName() + "\\}", macro.getMessage());
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return printTemplate;
    }

    public String getDisplayString(String str) {
        if (str == null || str.equals("null")) {
            return "";
        }

        String regexp = "[0-9]+";
        if (str.matches(regexp)) {
            return str;
        }

        str = TPUtility.escapeSpecialChars(str);

        return str.toUpperCase();
    }

    public Ticket getTicket() {
        return printTicket;
    }

    public void setTicket(Ticket ticket) {
        this.printTicket = ticket;
    }

    public boolean isPreview() {
        return isPreview;
    }

    public void setPreview(boolean isPreview) {
        this.isPreview = isPreview;
    }

    public String getPrintTemplate() {
        return printTemplate;
    }

    public void setPrintTemplate(String printTemplate) {
        this.printTemplate = printTemplate;
    }

    public Ticket getPrintTicket() {
        return printTicket;
    }

    public void setPrintTicket(Ticket printTicket) {
        this.printTicket = printTicket;
    }

    public boolean isPreviousTicket() {
        return isPreviousTicket;
    }

    public void setPreviousTicket(boolean isPreviousTicket) {
        this.isPreviousTicket = isPreviousTicket;
    }

    public boolean isSpecialTemplate() {
        return isSpecialTemplate;
    }

    public void setSpecialTemplate(boolean isSpecialTemplate) {
        this.isSpecialTemplate = isSpecialTemplate;
    }

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public boolean isMultiPrint() {
        return isMultiPrint;
    }

    public void setMultiPrint(boolean isMultiPrint) {
        this.isMultiPrint = isMultiPrint;
    }

    public boolean isTicketHistory() {
        return isTicketHistory;
    }

    public void setTicketHistory(boolean isTicketHistory) {
        this.isTicketHistory = isTicketHistory;
    }
}