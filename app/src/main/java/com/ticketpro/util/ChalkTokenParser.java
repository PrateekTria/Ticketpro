package com.ticketpro.util;

import android.content.Context;

import com.ticketpro.model.ChalkComment;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Color;
import com.ticketpro.model.CustomerInfo;
import com.ticketpro.model.Duration;
import com.ticketpro.model.MakeAndModel;
import com.ticketpro.model.PrintMacro;
import com.ticketpro.model.State;
import com.ticketpro.model.User;
import com.ticketpro.print.TicketPrinter;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;

public class ChalkTokenParser {
    private ChalkVehicle activeChalk;
    private String printTemplate;
    private boolean isPreview = false;

    private Logger log = Logger.getLogger("ChalkTokenParser");

    public ChalkTokenParser(ChalkVehicle chalk, String printTemplate) {
        this.activeChalk = chalk;
        this.printTemplate = printTemplate;
    }

    public void applyPrintSettings(Context context) {
        String methodName = TicketPrinter.getSelectedMethod(context);
        if (TicketPrinter.COMMUNICATION_METHOD_PRINTSERVICE.equalsIgnoreCase(methodName)) {

        }
    }

    public String parseTokens() {
        try {
            String printTemplate = TPUtility.escapeSpecialChars(this.printTemplate);
            printTemplate = printTemplate.replaceAll("\\{CHALK_ID\\}", activeChalk.getChalkId() + "");
            printTemplate = printTemplate.replaceAll("\\{DATE\\}", DateUtil.getStringFromDate(activeChalk.getChalkDate()) + "");
            printTemplate = printTemplate.replaceAll("\\{METER\\}", activeChalk.getMeter() + "");
            printTemplate = printTemplate.replaceAll("\\{LOCATION\\}", TPUtility.getFullAddress(activeChalk) + "");
            printTemplate = printTemplate.replaceAll("\\{PLATE\\}", activeChalk.getPlate() + "");
            printTemplate = printTemplate.replaceAll("\\{EXPDATE\\}", activeChalk.getExpiration() + "");
            printTemplate = printTemplate.replaceAll("\\{VIN\\}", activeChalk.getVin() + "");
            printTemplate = printTemplate.replaceAll("\\{STATE_CODE\\}", activeChalk.getStateCode() + "");
            printTemplate = printTemplate.replaceAll("\\{MAKE_CODE\\}", activeChalk.getMakeCode() + "");
            printTemplate = printTemplate.replaceAll("\\{COLOR_CODE\\}", activeChalk.getColorCode() + "");
            printTemplate = printTemplate.replaceAll("\\{NOTES\\}", activeChalk.getNotes() + "");

            if (!StringUtil.isEmpty(activeChalk.getStateCode())) {
                State state = State.getStateByName(activeChalk.getStateCode());
                if (state != null) {
                    printTemplate = printTemplate.replaceAll("\\{STATE\\}", state.getTitle());
                }
            }

            if (!StringUtil.isEmpty(activeChalk.getMakeCode())) {
                MakeAndModel make = MakeAndModel.getMakeByCode(activeChalk.getMakeCode());
                if (make != null) {
                    printTemplate = printTemplate.replaceAll("\\{MAKE\\}", make.getMakeTitle());
                }
            }

            if (!StringUtil.isEmpty(activeChalk.getColorCode())) {
                Color color = Color.getColorByCode(activeChalk.getColorCode());
                if (color != null) {
                    printTemplate = printTemplate.replaceAll("\\{COLOR\\}", color.getTitle());
                }
            }

            printTemplate = printTemplate.replaceAll("\\{STATE\\}", "");
            printTemplate = printTemplate.replaceAll("\\{MAKE\\}", "");
            printTemplate = printTemplate.replaceAll("\\{COLOR\\}", "");

            printTemplate = printTemplate.replaceAll("\\{PERMIT\\}", activeChalk.getPermit() + "");
            printTemplate = printTemplate.replaceAll("\\{FULL_LOC\\}", TPUtility.getFullAddress(activeChalk));
            printTemplate = printTemplate.replaceAll("\\{LOC_BLOCK\\}", activeChalk.getStreetNumber());
            printTemplate = printTemplate.replaceAll("\\{LOC_DIRECTION\\}", activeChalk.getDirection());
            printTemplate = printTemplate.replaceAll("\\{LOC_PREFIX\\}", activeChalk.getStreetPrefix());
            printTemplate = printTemplate.replaceAll("\\{LOC_SUFFIX\\}", activeChalk.getStreetSuffix());
            printTemplate = printTemplate.replaceAll("\\{LONG_LAT\\}", activeChalk.getLongitude() + " - " + activeChalk.getLatitude());
            printTemplate = printTemplate.replaceAll("\\{LONG\\}", activeChalk.getLongitude());
            printTemplate = printTemplate.replaceAll("\\{LAT\\}", activeChalk.getLatitude());
            printTemplate = printTemplate.replaceAll("\\{USERID\\}", activeChalk.getUserId() + "");

            String agencyCode = "";
            String webAddress = "";

            CustomerInfo customerInfo = CustomerInfo.getCustomerInfo(activeChalk.getCustId());
            if (customerInfo != null) {
                agencyCode = customerInfo.getAgencyCode();
                webAddress = customerInfo.getWebAddress();
            }

            printTemplate = printTemplate.replaceAll("\\{AGENCY_CODE\\}", agencyCode);
            printTemplate = printTemplate.replaceAll("\\{WEBADDRESS\\}", webAddress);

            ArrayList<ChalkComment> chalkComments = activeChalk.getComments();
            if (chalkComments != null && chalkComments.size() > 0) {
                printTemplate = printTemplate.replaceAll("\\{COMMENTS\\}", TPUtility.escapeSpecialChars(chalkComments.get(0).getComment()));
                printTemplate = printTemplate.replaceAll("\\{COMMENT1\\}", TPUtility.escapeSpecialChars(chalkComments.get(0).getComment()));

                if (chalkComments.size() > 1) {
                    printTemplate = printTemplate.replaceAll("\\{COMMENT2\\}", TPUtility.escapeSpecialChars(chalkComments.get(1).getComment()));
                }
            }

            printTemplate = printTemplate.replaceAll("\\{COMMENTS\\}", "");
            printTemplate = printTemplate.replaceAll("\\{COMMENT1\\}", "");
            printTemplate = printTemplate.replaceAll("\\{COMMENT2\\}", "");

            User userInfo = User.getUserInfo(activeChalk.getUserId());
            if (userInfo != null) {
                printTemplate = printTemplate.replaceAll("\\{BADGE\\}", userInfo.getBadge() + "");
                printTemplate = printTemplate.replaceAll("\\{DEPT\\}", userInfo.getDepartment() + "");
                printTemplate = printTemplate.replaceAll("\\{FIRST_NAME\\}", userInfo.getFirstName() + "");
                printTemplate = printTemplate.replaceAll("\\{LAST_NAME\\}", userInfo.getLastName() + "");
            } else {
                printTemplate = printTemplate.replaceAll("\\{BADGE\\}", "");
                printTemplate = printTemplate.replaceAll("\\{DEPT\\}", "");
                printTemplate = printTemplate.replaceAll("\\{FIRST_NAME\\}", "");
                printTemplate = printTemplate.replaceAll("\\{LAST_NAME\\}", "");
            }

            try {
                String durationTitle = Duration.getDurationTitleById(activeChalk.getDurationId());
                long milliseconds = (new Date().getTime() - activeChalk.getChalkDate().getTime());
                int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                int hours = (int) ((milliseconds / (1000 * 60 * 60)));

                String hrs = (hours < 10) ? ("0" + hours) : hours + "";
                String mins = (minutes < 10) ? ("0" + minutes) : minutes + "";
                printTemplate = printTemplate.replaceAll("\\{ELAPSED\\}", hrs + ":" + mins + " hrs/min");
                printTemplate = printTemplate.replaceAll("\\{TIME_ZONE\\}", durationTitle);
                printTemplate = printTemplate.replaceAll("\\{ZONE\\}", durationTitle);
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            if (!StringUtil.isEmpty(activeChalk.getSpace())) {
                printTemplate = printTemplate.replaceAll("\\{SPACE\\}", activeChalk.getSpace());
            } else {
                printTemplate = printTemplate.replaceAll("\\{SPACE\\}", "");
            }

            printTemplate = printTemplate.replaceAll("\\{ELAPSED\\}", "");
            printTemplate = printTemplate.replaceAll("\\{TIME_ZONE\\}", "");
            printTemplate = applyPrintMacros(printTemplate);

            // Empty Tokens
            printTemplate = printTemplate.replaceAll("\\{COPY_MSG\\}", "");
            printTemplate = printTemplate.replaceAll("\\{CUST_MSG1\\}", "");
            printTemplate = printTemplate.replaceAll("\\{CUST_MSG2\\}", "");
            printTemplate = printTemplate.replaceAll("\\{CUST_MSG3\\}", "");
            printTemplate = printTemplate.replaceAll("\\{PERMIT_EXPIRE\\}", "");

            if (customerInfo != null) {
                printTemplate = printTemplate.replaceAll("\\{CUSTOMER\\}", customerInfo.getName());
                printTemplate = printTemplate.replaceAll("\\{CUST_ADDRESS\\}", customerInfo.getAddress());
                printTemplate = printTemplate.replaceAll("\\{CUST_PHONE\\}", customerInfo.getContactNumber());
            } else {
                printTemplate = printTemplate.replaceAll("\\{CUSTOMER\\}", "");
                printTemplate = printTemplate.replaceAll("\\{CUST_ADDRESS\\}", "");
                printTemplate = printTemplate.replaceAll("\\{CUST_PHONE\\}", "");
            }

            printTemplate = printTemplate.replaceAll("null", "");

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return printTemplate;
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

    public ChalkVehicle getActiveChalk() {
        return activeChalk;
    }

    public void setActiveChalk(ChalkVehicle activeChalk) {
        this.activeChalk = activeChalk;
    }

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

}
