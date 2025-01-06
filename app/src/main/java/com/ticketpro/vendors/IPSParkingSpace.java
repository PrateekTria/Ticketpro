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


public class IPSParkingSpace {
    private Date ExpiryTime;
    private String ExactEndTime;
    private String ExactStartTime;
    private String Space;
    private String lot;
    private String SpaceGroup;
    private Date StartDateTime;
    private Date endDateLocal;
    private String timePurchased;
    public static final String OUTPUT_FORMAT_STD_DATE = "MM/dd/yyyy hh:mm:ss";
    private String Amount = "";
    private String PaymentMethod = "";

    public IPSParkingSpace() {

    }

    public IPSParkingSpace(JSONObject object) throws Exception {
        this.Space = object.getString("Space");
        this.SpaceGroup = object.getString("SpaceGroup");
        this.lot = object.getString("SpaceGroup");
        //this.StartDateTime = object.getString("StartDateTime");
        //this.ExpiryTime = object.getString("ExpiryTime");

        this.StartDateTime = DateUtil.getDateFromUTCStringMultiSpace(object.getString("StartDateTime"));
        this.ExpiryTime = DateUtil.getDateFromUTCStringMultiSpace(object.getString("ExpiryTime"));
        this.endDateLocal = DateUtil.getDateFromUTCStringMultiSpace(object.optString("ExpiryTime"));

    }


    /*
     * Multispace API Result parser - API is inconsistent on dev some value of APIs are null and some "null" and empty - document is missing
     * */
    public ArrayList<IPSParkingSpace> getIPSParkingSpaceResult(String Result) throws Exception {
        ArrayList<IPSParkingSpace> ipsParkingSpaces = new ArrayList<IPSParkingSpace>();
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
                if (object.optString("ExpiryTime") == null) {
                    //can't return
                } else if (DateUtil.getDateFromUTCStringMultiSpace(object.optString("ExpiryTime")) == null) {
                    //can't return and can't combined with null to
                } else {
                   if (!hasMoreExpiryDate(maxExpiry, DateUtil.getDateFromUTCStringMultiSpace(object.optString("ExpiryTime")))) {
                        IPSParkingSpace objIPSSpace = new IPSParkingSpace();

                        objIPSSpace.setSpace(object.optString("Space"));
                        objIPSSpace.setTimePurchased(object.optString("TimePurchased"));
                        objIPSSpace.setPaymentMethod(object.optString("PaymentMethod"));
                        objIPSSpace.setAmount(object.optString("Amount"));
                        //this.ExpiryTime = object.getString("ExpiryTime");
                        objIPSSpace.setSpaceGroup(object.optString("SpaceGroup"));
                        objIPSSpace.setLot(object.optString("SpaceGroup"));
                        if (object.optString("TimeZone").equalsIgnoreCase("null")) {
                            objIPSSpace.setStartDateTime(DateUtil.getDateFromUTCStringMultiSpace(object.optString("StartDateTime")));
                            objIPSSpace.setExpiryTime(DateUtil.getDateFromUTCStringMultiSpace(object.optString("ExpiryTime")));
                        }

                        objIPSSpace.setExactStartTime(object.optString("StartDateTime"));


                        objIPSSpace.setExactEndTime(object.optString("ExpiryTime"));

                        objIPSSpace.setEndDateLocal(DateUtil.getDateFromUTCStringMultiSpace(object.optString("ExpiryTime")));//for comparing purpose and other case

                        ipsParkingSpaces.add(objIPSSpace);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipsParkingSpaces;
    }

    public ArrayList<IPSParkingSpace> sortIPSResultByInputMinutes(ArrayList<IPSParkingSpace> parkingSpaces, long inputMinutes) {
        ArrayList<IPSParkingSpace> ipsParkingSpaces = new ArrayList<IPSParkingSpace>();
        try {//updated inside condition due to unnecessary changes -
            for (int i = 0; i < parkingSpaces.size(); i++) {
                IPSParkingSpace objIPSSpace = parkingSpaces.get(i);
                if (!moreThanInput(inputMinutes, objIPSSpace.getExpiryTime(), false)) {
                    ipsParkingSpaces.add(objIPSSpace);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipsParkingSpaces;
    }

    public ArrayList<IPSParkingSpace> sortIPSResultByInputHour(ArrayList<IPSParkingSpace> parkingSpaces, long inputMinutes) {
        ArrayList<IPSParkingSpace> ipsParkingSpaces = new ArrayList<IPSParkingSpace>();
        try {//updated inside condition due to unnecessary changes - upto single character as no more than 24 is allowed as above 10 is not required
            for (int i = 0; i < parkingSpaces.size(); i++) {
                IPSParkingSpace objIPSSpace = parkingSpaces.get(i);
                if (!moreThanInputHour(inputMinutes, objIPSSpace.getExpiryTime())) {
                    ipsParkingSpaces.add(objIPSSpace);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipsParkingSpaces;
    }
	/*public ArrayList<IPSParkingSpace> sortIPSResultByInputHours(ArrayList<IPSParkingSpace> parkingSpaces, long inputMinutes) {
		ArrayList<IPSParkingSpace> ipsParkingSpaces= new ArrayList<IPSParkingSpace>();
		try {//updated inside condition due to unnecessary changes -
			for(int i=0; i<parkingSpaces.size(); i++) {
				IPSParkingSpace objIPSSpace = parkingSpaces.get(i);
				if(!moreThanInput(inputMinutes, objIPSSpace.getExpiryTime(),true)) {
					ipsParkingSpaces.add(objIPSSpace);
				}
			}}catch (Exception e){
			e.printStackTrace();
		}
		return ipsParkingSpaces;
	}*/

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public Date getStartDateTime() {
        return StartDateTime;
    }

    public void setStartDateTime(Date StartDateTime) {
        this.StartDateTime = StartDateTime;
    }

    public String getSpaceGroup() {
        return SpaceGroup;
    }

    public void setSpaceGroup(String SpaceGroup) {
        this.SpaceGroup = SpaceGroup;
    }

    public String getSpace() {
        return Space;
    }

    public void setSpace(String Space) {
        this.Space = Space;
    }

    public Date getExpiryTime() {
        return ExpiryTime;
    }

    public void setExpiryTime(Date ExpiryTime) {
        this.ExpiryTime = ExpiryTime;
    }


    public void setTimePurchased(String timePurchased) {
        this.timePurchased = timePurchased;
    }

    public String getTimePurchased() {
        return timePurchased;
    }

    public Date getEndDateLocal() {
        return endDateLocal;
    }

    public void setEndDateLocal(Date endDateLocal) {
        this.endDateLocal = endDateLocal;
    }

    //added later to display purpose
    public String getExactStartTime() {
        return ExactStartTime;
    }

    public void setExactStartTime(String ExactStartTime) {
        this.ExactStartTime = ExactStartTime;
    }

    public String getExactEndTime() {
        return ExactEndTime;
    }

    public void setExactEndTime(String ExactEndTime) {
        this.ExactEndTime = ExactEndTime;
    }



	/*public static class ExpireComparator implements Comparator<IPSParkingSpace> {
		@Override
		public int compare(IPSParkingSpace item1, IPSParkingSpace item2) {
			return Integer.valueOf(item1.ExpiryTime).compareTo(Integer.valueOf(item2.ExpiryTime));
		}
	}*/


    public String updateRequiredTimeZone(Date requiredDate) {
        Date date = null;
        String utcString = requiredDate.toString();
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = format.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertedDateTime(date);

    }

    public String updateRequiredTimeZone(String requiredDate) {
        Date date = null;
        String utcString = requiredDate.toString();
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = format.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertedDateTime(date);

    }


    public static String formatDateToString(Date date, String format, String timeZone) {
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
            } else {
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
            } else {
                hasMoreThanLimit = true;
                return hasMoreThanLimit;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasMoreThanLimit;
    }

    /*
    public boolean moreThanInputMinute(long maxExpiryDifference, Date expiryTime,boolean checkByHoursInsteadMinutes){
        boolean hasMoreThanLimit = false;
        try{
            String expireStr="";

            long diffMinutes,diffHours,diffDays;
            long maxInputExpiryHours = 0;
            long maxInputExpiryMinutes = 0;
            long expiredDiff = new Date().getTime() - expiryTime.getTime();//this.getEndDateLocal().getTime();
            if(expiredDiff<0){
                expiredDiff = Math.abs(expiredDiff);
            }
            if(expiredDiff > 0){
                    diffMinutes = expiredDiff / (60 * 1000) % 60;
                    diffHours = expiredDiff / (60 * 60 * 1000) % 24;
                    diffDays = expiredDiff / (24 * 60 * 60 * 1000);
                    if(maxExpiryDifference>=60){
                        try {
                            int inputTime = (int)maxExpiryDifference;
                            int minutes = (inputTime % (24 * 60)) % 60;
                            maxInputExpiryMinutes = Long.valueOf(minutes);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        finally {
                            maxInputExpiryHours = maxExpiryDifference/60;
                        }
                    }
                    if(diffDays>=1){
                        hasMoreThanLimit = true;
                        return hasMoreThanLimit;
                    }else if(diffHours>0){
                         if(maxInputExpiryHours>=diffHours){
                             if(diffMinutes>maxInputExpiryMinutes){
                                hasMoreThanLimit = true;
                            }else {
                                hasMoreThanLimit = false;
                            }
                            return hasMoreThanLimit;
                        }else{
                            hasMoreThanLimit = true;
                            return hasMoreThanLimit;
                        }
                    }else{
                        if(diffMinutes>maxExpiryDifference){
                             hasMoreThanLimit = true;
                             return hasMoreThanLimit;
                        }else {
                            hasMoreThanLimit = false;
                            return hasMoreThanLimit;
                        }
                    }
                }
        }catch (Exception e){
            e.printStackTrace();
        }
        return hasMoreThanLimit;
    }

*/
    public boolean moreThanInputHours(long maxHourToCheckExpiry, Date expiryTime) {
        boolean hasMoreThanLimit = false;
        try {
            long diffMinutes, diffHours, diffDays;
            long maxInputExpiryHours = 0;
            long expiredDiff = new Date().getTime() - expiryTime.getTime();//this.getEndDateLocal().getTime();
            if (expiredDiff < 0) {
                expiredDiff = Math.abs(expiredDiff);
            }
            if (expiredDiff > 0) {
                diffMinutes = expiredDiff / (60 * 1000) % 60;
                diffHours = expiredDiff / (60 * 60 * 1000) % 24;
                diffDays = expiredDiff / (24 * 60 * 60 * 1000);

                if (diffDays >= 1) {
                    hasMoreThanLimit = true;
                    return hasMoreThanLimit;
                } else if (diffHours > 0) {
                    if (diffHours > maxHourToCheckExpiry) {
                        hasMoreThanLimit = true;
                        return hasMoreThanLimit;
                    } else {
                        hasMoreThanLimit = false;
                        return hasMoreThanLimit;
                    }
                } else {
                    hasMoreThanLimit = false;
                    return hasMoreThanLimit;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasMoreThanLimit;
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

    public String convertedDateTime(Date date) {
        return formatDateToString(date, "MM/dd/yyyy hh:mm:ss", null);
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public static class SpaceComparator implements Comparator<IPSParkingSpace> {
        @Override
        public int compare(IPSParkingSpace item1, IPSParkingSpace item2) {
            return Integer.compare(Integer.valueOf(item1.getSpace()), (Integer.valueOf(item2.getSpace())));
        }
    }

    public static class ExpireComparator implements Comparator<IPSParkingSpace> {
        @Override
        public int compare(IPSParkingSpace item1, IPSParkingSpace item2) {
            return item1.getExpiryTime().compareTo(item2.getExpiryTime());
        }
    }

    public static class LotComparator implements Comparator<IPSParkingSpace> {
        @Override
        public int compare(IPSParkingSpace item1, IPSParkingSpace item2) {
            return item1.getLot().compareTo(item2.getLot());
        }
    }

    //Trying to fix timezone issue


}
