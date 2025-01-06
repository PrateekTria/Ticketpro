package com.ticketpro.vendors;

import com.ticketpro.model.Feature;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.util.DateUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class IPSParkingPlate {
    public static final String OUTPUT_FORMAT_STD_DATE = "MM/dd/yyyy hh:mm:ss";
    private String LicensePlateNumber;
    private String subAreaName;
    private String ParkingStartTime;
    private String ParkingExpiryTime;
    private Date systemDate;
    private Date ExpiredTime;
    private String SpaceGroup;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    private String amount;

    public IPSParkingPlate() {

    }

    public IPSParkingPlate(JSONObject object) throws Exception {
        this.LicensePlateNumber = object.getString("LicensePlateNumber");
        this.subAreaName = object.getString("SubAreaName");
        //this.ParkingStartTime = object.getString("ParkingStartTime");
        //this.ParkingExpiryTime = object.getString("ParkingExpiryTime");
        this.ParkingStartTime = updateRequiredTimeZone(object.getString("ParkingStartTime"));
        this.ParkingExpiryTime = updateRequiredTimeZone(object.getString("ParkingExpiryTime"));
    }

    public static Date getDateFromUTCString(String utcString) {
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }
        if (utcString.length() > 18) {
            utcString = utcString.substring(0, 18);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDateToString(Date date, String format,
                                            String timeZone) {
        // null check
        if (date == null) return null;
        // create SimpleDateFormat object with input format
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // default system timezone if passed null or empty
        if (timeZone == null || "".equalsIgnoreCase(timeZone.trim())) {
            timeZone = Calendar.getInstance().getTimeZone().getID();
        }
        // set timezone to SimpleDateFormat
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        // return Date in required format with timezone as String
        return sdf.format(date);
    }

    public String getSubAreaName() {
        return subAreaName;
    }

    public void setSubAreaName(String subAreaName) {
        this.subAreaName = subAreaName;
    }

    public String getLicensePlateNumber() {
        return LicensePlateNumber;
    }

    public void setLicensePlateNumber(String LicensePlateNumber) {
        this.LicensePlateNumber = LicensePlateNumber;
    }

    public String getParkingStartTime() {
        return ParkingStartTime;
    }

    public void setParkingStartTime(String ParkingStartTime) {
        this.ParkingStartTime = ParkingStartTime;
    }

    public String getParkingExpiryTime() {
        return ParkingExpiryTime;
    }

    public void setParkingExpiryTime(String ParkingExpiryTime) {
        this.ParkingExpiryTime = ParkingExpiryTime;
    }

    public Date getSystemDate() {
        return systemDate;
    }
/*
	public static Date getDateFromUTCString(String utcString) {
		if (utcString == null || utcString.equals("null") || utcString.equals("")) {
			return null;
		}

		if (utcString.length() > 18) {
			utcString = utcString.substring(0, 18);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(utcString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
*/

    public void setSystemDate(Date systemDate) {
        this.systemDate = systemDate;
    }

    public ParkingExpireInfo getExpireInfo() {
        ParkingExpireInfo parkingExpireInfo = new ParkingExpireInfo();

        String expireStr = "";

        ExpiredTime = getDateFromUTCString(this.getParkingExpiryTime());

        long expiryTime = Long.parseLong(ExpiredTime.toString());

        long diffMinutes, diffHours, diffDays;

        long expiredDiff = this.getSystemDate().getTime() - expiryTime;

        if (expiredDiff > 0) {
            diffMinutes = expiredDiff / (60 * 1000) % 60;
            diffHours = expiredDiff / (60 * 60 * 1000) % 24;
            diffDays = expiredDiff / (24 * 60 * 60 * 1000);
            if (diffDays >= 1) {
                expireStr = diffDays + " d " + diffHours + " h ago";

            } else if (diffHours >= 1) {
                expireStr = diffHours + " h " + diffMinutes + " m ago";

            } else {
                expireStr = diffMinutes + " m ago";
            }

            parkingExpireInfo.setExpired(true);

        } else {
            expiredDiff = Math.abs(expiredDiff);
            diffMinutes = expiredDiff / (60 * 1000) % 60;
            diffHours = expiredDiff / (60 * 60 * 1000) % 24;
            diffDays = expiredDiff / (24 * 60 * 60 * 1000);

            if (diffDays >= 1) {
                expireStr = "in " + diffDays + " d " + diffHours + " h";

            } else if (diffHours >= 1) {
                expireStr = "in " + diffHours + " h " + diffMinutes + " m";

            } else {
                expireStr = "in " + diffMinutes + " m";
            }

            parkingExpireInfo.setExpired(false);
        }

        parkingExpireInfo.setExpireMsg(expireStr);
        parkingExpireInfo.setDiffDays((int) diffDays);
        parkingExpireInfo.setDiffHrs((int) diffHours);
        parkingExpireInfo.setDiffMinutes((int) diffMinutes);
        parkingExpireInfo.setDiffSeconds((int) diffMinutes * 60);

        return parkingExpireInfo;
    }

    public ParkingExpireInfo getExpireInfo(Date expiryTime) {
        ParkingExpireInfo parkingExpireInfo = new ParkingExpireInfo();

        String expireStr = "";
        long diffMinutes, diffHours, diffDays;
        long expiredDiff = new Date().getTime() - expiryTime.getTime();//this.getEndDateLocal().getTime();
        if (expiredDiff > 0) {
            diffMinutes = expiredDiff / (60 * 1000) % 60;
            diffHours = expiredDiff / (60 * 60 * 1000) % 24;
            diffDays = expiredDiff / (24 * 60 * 60 * 1000);
            if (diffDays >= 1) {
                expireStr = "(-" + diffDays + " d " + diffHours + " h)";

            } else if (diffHours >= 1) {
                expireStr = "(-" + diffHours + " h " + diffMinutes + " m)";

            } else {
                expireStr = "(-" + diffMinutes + " m)";
            }

            parkingExpireInfo.setExpired(true);

        } else {
            expiredDiff = Math.abs(expiredDiff);
            diffMinutes = expiredDiff / (60 * 1000) % 60;
            diffHours = expiredDiff / (60 * 60 * 1000) % 24;
            diffDays = expiredDiff / (24 * 60 * 60 * 1000);

            if (diffDays >= 1) {
                expireStr =/*"in "+*/diffDays + " d " + diffHours + " h";

            } else if (diffHours >= 1) {
                expireStr =/*"in "+*/diffHours + " h " + diffMinutes + " m";

            } else {
                expireStr =/*"in "+*/diffMinutes + " m";
            }

            parkingExpireInfo.setExpired(false);
        }

        parkingExpireInfo.setExpireMsg(expireStr);
        parkingExpireInfo.setDiffDays((int) diffDays);
        parkingExpireInfo.setDiffHrs((int) diffHours);
        parkingExpireInfo.setDiffMinutes((int) diffMinutes);
        parkingExpireInfo.setDiffSeconds((int) diffMinutes * 60);

        return parkingExpireInfo;
    }

    public String updateRequiredTimeZone(String utcString) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
            //format.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = format.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertedDateTime(date);

    }

    public String convertedDateTime(Date date) {
        return formatDateToString(date, "MM/dd/yyyy hh:mm:ss a", null);
    }
    public ArrayList<IPSParkingPlate> getIPSParkingPlateResult(String Result) throws Exception {
        ArrayList<IPSParkingPlate> ipsParkingSpaces = new ArrayList<IPSParkingPlate>();
        try {
            this.SpaceGroup = TPApplication.getInstance().IPSSpaceGroup;
            JSONArray jsonArray = new JSONArray(Result);
            String maxExpiryHours;

            long maxExpiry = 12;

            if (Feature.isFeatureAllowed(Feature.IPS_MULTISPACE_MAX_EXPIRY)) {
                maxExpiryHours = Feature.getFeatureValue(Feature.IPS_MULTISPACE_MAX_EXPIRY);
                maxExpiry = Long.parseLong(maxExpiryHours);
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                if (object.optString("ParkingExpiryTime") == null) {
                    //can't return
                } else if (DateUtil.getDateFromUTCStringMultiSpace(object.optString("ParkingExpiryTime")) == null) {
                    //can't return and can't combined with null to
                } else {
                    if (!hasMoreExpiryDate(maxExpiry, DateUtil.getDateFromUTCStringMultiSpace(object.optString("ParkingExpiryTime")))) {
                        IPSParkingPlate objIPSSpace = new IPSParkingPlate();

                        objIPSSpace.setLicensePlateNumber(object.optString("LicensePlateNumber"));
                        objIPSSpace.setParkingStartTime(object.optString("ParkingStartTime"));
                        objIPSSpace.setParkingExpiryTime(object.optString("ParkingExpiryTime"));
                        objIPSSpace.setSubAreaName(object.optString("SubAreaName"));
                        ipsParkingSpaces.add(objIPSSpace);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipsParkingSpaces;
    }

    public ArrayList<IPSParkingPlate> sortIPSResultByInputMinutes(ArrayList<IPSParkingPlate> parkingSpaces, long inputMinutes) {
        ArrayList<IPSParkingPlate> ipsParkingSpaces = new ArrayList<>();
        try {//updated inside condition due to unnecessary changes -
            for (int i = 0; i < parkingSpaces.size(); i++) {
                IPSParkingPlate objIPSSpace = parkingSpaces.get(i);
                if (!moreThanInput(inputMinutes, DateUtil.getDateFromUTCStringMultiSpace(objIPSSpace.getParkingExpiryTime()), false)) {
                    ipsParkingSpaces.add(objIPSSpace);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipsParkingSpaces;
    }

    public boolean moreThanInput(long maxExpiryDifference, Date expiryTime, boolean checkByHoursInsteadMinutes) {
        boolean hasMoreThanLimit = false;
        try {
            String expireStr = "";
            long diffMinutes, diffHours, diffDays;
            long totalMinutes = 0;
            long expiredDiff = new Date().getTime() - expiryTime.getTime();//this.getEndDateLocal().getTime();
            /*if (expiredDiff < 0) {
                expiredDiff = Math.abs(expiredDiff);
            }*/
            if (expiredDiff > 0) {
                diffMinutes = expiredDiff / (60 * 1000) % 60;
                diffHours = expiredDiff / (60 * 60 * 1000) % 24;
                diffDays = expiredDiff / (24 * 60 * 60 * 1000);
                totalMinutes = (diffHours * 60) + diffMinutes;
                if (totalMinutes > maxExpiryDifference) {
                    hasMoreThanLimit = true;
                    return hasMoreThanLimit;
                } else {
                    hasMoreThanLimit = false;
                    return hasMoreThanLimit;
                }
            } else{
                hasMoreThanLimit = true;
                return hasMoreThanLimit;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasMoreThanLimit;
    }

    public boolean moreThanInputHour(long maxHourToCheckExpiry, Date expiryTime) {
        boolean hasMoreThanLimit = false;
        try {
            long diffMinutes, diffHours, diffDays;
            long inputMinutesByUser = 0;
            long totalMinute = 0;
            long expiredDiff = new Date().getTime() - expiryTime.getTime();//this.getEndDateLocal().getTime();
            /*if (expiredDiff < 0) {
                expiredDiff = Math.abs(expiredDiff);
            }*/
            if (expiredDiff > 0) {
                diffMinutes = expiredDiff / (60 * 1000) % 60;
                diffHours = expiredDiff / (60 * 60 * 1000) % 24;
                diffDays = expiredDiff / (24 * 60 * 60 * 1000);
                inputMinutesByUser = maxHourToCheckExpiry * 60;
                totalMinute = (diffHours * 60) + diffMinutes;
                if (totalMinute > inputMinutesByUser) {
                    hasMoreThanLimit = true;
                    return hasMoreThanLimit;
                } else {
                    hasMoreThanLimit = false;
                    return hasMoreThanLimit;
                }
            } else{
                hasMoreThanLimit = true;
                return hasMoreThanLimit;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasMoreThanLimit;
    }

    public boolean hasMoreExpiryDate(long maxExpiryDifference, Date expiryTime) {
        boolean hasMoreThanLimit = false;
        try {
            String expireStr = "";
            long diffMinutes, diffHours, diffDays;
            long expiredDiff = new Date().getTime() - expiryTime.getTime();//this.getEndDateLocal().getTime();
            if (expiredDiff > 0) {
                diffMinutes = expiredDiff / (60 * 1000) % 60;
                diffHours = expiredDiff / (60 * 60 * 1000) % 24;
                diffDays = expiredDiff / (24 * 60 * 60 * 1000);
                if (diffDays >= 1) {
                    expireStr = "(-" + diffDays + " d " + diffHours + " h)";
                    hasMoreThanLimit = true;
                } else if (diffHours > maxExpiryDifference) {
                    expireStr = "(-" + diffHours + " h " + diffMinutes + " m)";
                    hasMoreThanLimit = true;
                } else {
                    expireStr = "(-" + diffMinutes + " m)";
                    hasMoreThanLimit = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasMoreThanLimit;
    }

    public String getSpaceGroup() {
        return SpaceGroup;
    }

    public void setSpaceGroup(String spaceGroup) {
        SpaceGroup = spaceGroup;
    }

    public ArrayList<IPSParkingPlate> sortIPSResultByInputHour(ArrayList<IPSParkingPlate> parkingSpaces, long inputMinutes) {
        ArrayList<IPSParkingPlate> ipsParkingSpaces = new ArrayList<IPSParkingPlate>();
        try {//updated inside condition due to unnecessary changes - upto single character as no more than 24 is allowed as above 10 is not required
            for (int i = 0; i < parkingSpaces.size(); i++) {
                IPSParkingPlate objIPSSpace = parkingSpaces.get(i);
                if (!moreThanInputHour(inputMinutes, DateUtil.getDateFromUTCStringMultiSpace(objIPSSpace.getParkingExpiryTime()))) {
                    ipsParkingSpaces.add(objIPSSpace);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipsParkingSpaces;
    }

    public static class PlateComparator implements Comparator<IPSParkingPlate> {
        @Override
        public int compare(IPSParkingPlate item1, IPSParkingPlate item2) {
            return item1.getLicensePlateNumber().compareTo(item2.getLicensePlateNumber());
        }
    }

    public static class ExpireComparator implements Comparator<IPSParkingPlate> {
        @Override
        public int compare(IPSParkingPlate item1, IPSParkingPlate item2) {
            return item1.getParkingExpiryTime().compareTo(item2.getParkingExpiryTime());
        }
    }

    public static class StartTimeComparator implements Comparator<IPSParkingPlate> {
        @Override
        public int compare(IPSParkingPlate item1, IPSParkingPlate item2) {
            return item1.getParkingStartTime().compareTo(item2.getParkingStartTime());
        }
    }


}
