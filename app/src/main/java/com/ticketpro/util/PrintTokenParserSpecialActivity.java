package com.ticketpro.util;

import com.ticketpro.model.SpecialActivityReport;
import com.ticketpro.model.User;
import com.ticketpro.parking.activity.TPApplication;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class PrintTokenParserSpecialActivity {

    private ArrayList<SpecialActivityReport> printTicket;
    private String printTemplate;
    private boolean isPreview = false;
    private boolean isPreviousTicket = false;
    private boolean isSpecialTemplate = false;
    private boolean isMultiPrint = false;
    private boolean isTicketHistory = false;

    private Logger log = Logger.getLogger("PrintTokenParserSpecialActivity");

    public PrintTokenParserSpecialActivity(ArrayList<SpecialActivityReport> printTicket, String printTemplate) {
        this.printTicket = printTicket;
        this.printTemplate = printTemplate;
    }

    public String parseTickets() {
        ArrayList<String> printTickets = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String template;
            String header = "Activity  START  END  DURATION\n";
            for (int i = 0; i < printTicket.size() ; i++) {
                template = TPUtility.escapeSpecialChars(this.printTemplate);
                SpecialActivityReport report = printTicket.get(i);
                template = template.replaceAll("\\{REPORT_ID\\}", report.getReportId() + "");
                template = template.replaceAll("\\{ACTIVITY_ID\\}", report.getActivityId() + "");
                template = template.replaceAll("\\{ACTIVITY_NAME\\}", report.getActivityName() + "");
                template = template.replaceAll("\\{CASE_NUMBER\\}", report.getCaseNumber() + "");
                template = template.replaceAll("\\{START_TIME\\}", report.getStartTime() + "");
                template = template.replaceAll("\\{END_TIME\\}", report.getEndTime() + "");
                template = template.replaceAll("\\{ADDRESS\\}", report.getStreetAddress() + "");
                template = template.replaceAll("\\{NOTE\\}", report.getNotes() + "");
                template = template.replaceAll("\\{DATE\\}", report.getCreatedDate() + "");
                template = template.replaceAll("\\{DURATION\\}", report.getDuration() + "");
                template = template.replaceAll("\\{COUNT\\}", report.getTicketCount() + "");


                User userInfo = User.getUserInfo(TPApplication.getInstance().userId);
                if (userInfo != null) {
                    template = template.replaceAll("\\{BADGE\\}", userInfo.getBadge() + "");
                    template = template.replaceAll("\\{DEPT\\}", userInfo.getDepartment() + "");
                    template = template.replaceAll("\\{FIRST_NAME\\}", userInfo.getFirstName() + "");
                    template = template.replaceAll("\\{LAST_NAME\\}", userInfo.getLastName() + "");
                } else {
                    template = template.replaceAll("\\{BADGE\\}", "");
                    template = template.replaceAll("\\{DEPT\\}", "");
                    template = template.replaceAll("\\{FIRST_NAME\\}", "");
                    template = template.replaceAll("\\{LAST_NAME\\}", "");
                }

                template = template.replaceAll("null", "");
                printTickets.add(template);
                stringBuilder.append(template);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String totalH = getTotalH(printTicket);
        stringBuilder.append("\n Total         "+totalH);
        return stringBuilder.toString();
    }

    private String getTotalH(ArrayList<SpecialActivityReport> printTickets) {
        String total= "";
        int mTotal = 0;
        for (int i = 0; i <printTickets.size() ; i++) {
            final String s = printTickets.get(i).getDuration();
            final String c[] = s.split(" ");
            int m = Integer.parseInt(c[0]);
            mTotal = mTotal+m;
        }
        //final int i = mTotal / 60;
        return String.valueOf(mTotal+" Minutes");
    }

    public ArrayList<SpecialActivityReport> getPrintTicket() {
        return printTicket;
    }

    public void setPrintTicket(ArrayList<SpecialActivityReport> printTicket) {
        this.printTicket = printTicket;
    }

    public String getPrintTemplate() {
        return printTemplate;
    }

    public void setPrintTemplate(String printTemplate) {
        this.printTemplate = printTemplate;
    }

    public boolean isPreview() {
        return isPreview;
    }

    public void setPreview(boolean preview) {
        isPreview = preview;
    }

    public boolean isPreviousTicket() {
        return isPreviousTicket;
    }

    public void setPreviousTicket(boolean previousTicket) {
        isPreviousTicket = previousTicket;
    }

    public boolean isSpecialTemplate() {
        return isSpecialTemplate;
    }

    public void setSpecialTemplate(boolean specialTemplate) {
        isSpecialTemplate = specialTemplate;
    }

    public boolean isMultiPrint() {
        return isMultiPrint;
    }

    public void setMultiPrint(boolean multiPrint) {
        isMultiPrint = multiPrint;
    }

    public boolean isTicketHistory() {
        return isTicketHistory;
    }

    public void setTicketHistory(boolean ticketHistory) {
        isTicketHistory = ticketHistory;
    }
}
