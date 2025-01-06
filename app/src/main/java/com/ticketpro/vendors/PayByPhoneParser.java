package com.ticketpro.vendors;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.StringTokenizer;

import android.util.Log;

import com.ticketpro.util.DateUtil;

public class PayByPhoneParser {

    public static ArrayList<PayByPhoneParking> getParkings(String response) {
        int index = 0;
        String methodName = "";
        String parameters = "";
        String headers = "";

        if (response == null || response.isEmpty()) {
            return null;
        }

        StringBuilder responses = new StringBuilder();
        StringTokenizer tokens = new StringTokenizer(response, "\n", true);
        while (tokens.hasMoreTokens()) {
            String line = tokens.nextToken();
            if (line.equalsIgnoreCase("\n")) {
                continue;
            }

            if (index == 0) {
                methodName = line.substring(0, line.indexOf("("));
                parameters = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
            } else if (index == 1) {
                String[] resultTokens = line.split(",");
                if (resultTokens.length == 2) {
                    if (Objects.equals(resultTokens[0], "ERROR")) {
                        Log.e("PayByPhone", "ERROR - " + resultTokens[1]);
                        break;
                    }
                }
            } else if (index == 2) {
                headers = line;
            } else {
                responses.append(line);
                responses.append("\n");
            }

            index++;
        }

        return getParkingsByMethod(methodName, headers, parameters, responses.toString());
    }


    public static ArrayList<PayByPhoneParking> getParkings(String response, String plate) {
        ArrayList<PayByPhoneParking> results = new ArrayList<>();
        ArrayList<PayByPhoneParking> parkings = getParkings(response);
        if (parkings == null) {
            return null;
        }

        for (PayByPhoneParking parking : parkings) {
            if (parking.getPlate() != null && parking.getPlate().equalsIgnoreCase(plate)) {
                results.add(parking);
            }
        }

        return results;
    }

    /**
     * This function takes in a ParkingEntitlement object and returns an ArrayList of PayByPhoneParking
     * objects
     *
     * @param parkingEntitlement The parking entitlement that you want to get the parking details for.
     * @return An ArrayList of PayByPhoneParking objects.
     */
    public static ArrayList<PayByPhoneParking> getParkings(ParkingEntitlement parkingEntitlement) {
        ArrayList<PayByPhoneParking> results = new ArrayList<>();
        PayByPhoneParking payByPhoneParking = new PayByPhoneParking();
        payByPhoneParking.setEndDateTime(DateUtil.getDateFromUTCStringPaybyPhone(parkingEntitlement.getEndDateTime()));
        payByPhoneParking.setStartDateTime(DateUtil.getDateFromUTCStringPaybyPhone(parkingEntitlement.getStartDateTime()));
        payByPhoneParking.setPlate(parkingEntitlement.getPlate());
        results.add(payByPhoneParking);
        return results;
    }

    /**
     * This function takes in a string of XML and returns an ArrayList of PayByPhoneParking objects
     *
     * @param method The method to call.
     * @param headers The headers of the request.
     * @param parkings The JSON string returned from the API call.
     * @return An ArrayList of PayByPhoneParking objects.
     */
    public static ArrayList<PayByPhoneParking> getParkingsByMethod(String method, String headers, String parameters, String parkings) {
        ArrayList<PayByPhoneParking> results = new ArrayList<>();
        if (parkings == null || parkings.isEmpty()) {
            try {
                if (method.equalsIgnoreCase("GetPaidAtWaveNumber")) {
                    results = getPaidAtWaveNumber(parkings, parameters);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return results;
        }

        if (method.equalsIgnoreCase("GetPaidAtWaveNumber")) {
            results = getPaidAtWaveNumber(parkings, parameters);
        } else if (method.equalsIgnoreCase("GetPaidAtStallNumber")) {
            results = getPaidAtStallNumber(parkings, parameters);
        } else {
            results = getGenericResults(parkings, parameters);
        }

        return results;
    }

    public static ArrayList<PayByPhoneParking> getPaidAtWaveNumber(String parkings, String parameters) {
        ArrayList<PayByPhoneParking> results = new ArrayList<>();
        Date systemDate = new Date();
        if (parameters != null && !parameters.isEmpty()) {
            String[] values = parameters.split(",");
            if (values.length > 5) {
                systemDate = getDateFromUTCString(getUnescapedString(values[3]));
            }
        }

        StringTokenizer tokens = new StringTokenizer(parkings, "\n", true);
        while (tokens.hasMoreTokens()) {
            PayByPhoneParking result = new PayByPhoneParking();
            String parking = tokens.nextToken();
            String[] parkingTokens = parking.split(",");
            if (parkingTokens.length >= 6) {
                result.setSystemDate(systemDate);
                result.setPlate(getUnescapedString(parkingTokens[0]));
                result.setState(getUnescapedString(parkingTokens[1]));
                result.setStallNumber(getUnescapedString(parkingTokens[2]));
                result.setVendorStallNumber(getUnescapedString(parkingTokens[3]));
                result.setStartDateTime(getDateFromUTCString(getUnescapedString(parkingTokens[4])));
                result.setEndDateTime(getDateFromUTCString(getUnescapedString(parkingTokens[5])));

                results.add(result);
            }
        }

        return results;
    }


    public static ArrayList<PayByPhoneParking> getPaidAtStallNumber(String parkings, String parameters) {
        ArrayList<PayByPhoneParking> results = new ArrayList<PayByPhoneParking>();
        Date systemDate = new Date();
        if (parameters != null && !parameters.isEmpty()) {
            String[] values = parameters.split(",");
            if (values.length > 5) {
                systemDate = getDateFromUTCString(getUnescapedString(values[4]));
            }
        }

        StringTokenizer tokens = new StringTokenizer(parkings, "\n", true);
        while (tokens.hasMoreTokens()) {
            PayByPhoneParking result = new PayByPhoneParking();
            String parking = tokens.nextToken();
            String[] parkingTokens = parking.split(",");
            if (parkingTokens.length >= 6) {
                result.setSystemDate(systemDate);
                result.setPlate(getUnescapedString(parkingTokens[0]));
                result.setState(getUnescapedString(parkingTokens[1]));
                result.setStallNumber(getUnescapedString(parkingTokens[2]));
                result.setVendorStallNumber(getUnescapedString(parkingTokens[3]));
                result.setStartDateTime(getDateFromUTCString(getUnescapedString(parkingTokens[4])));
                result.setEndDateTime(getDateFromUTCString(getUnescapedString(parkingTokens[5])));

                results.add(result);
            }
        }

        return results;
    }

    public static ArrayList<PayByPhoneParking> getGenericResults(String parkings, String parameters) {
        ArrayList<PayByPhoneParking> results = new ArrayList<PayByPhoneParking>();
        Date systemDate = new Date();
        if (parameters != null && !parameters.isEmpty()) {
            String[] values = parameters.split(",");
            for (String token : values) {
                if (token.contains(":") && token.contains("-")) {
                    systemDate = getDateFromUTCString(getUnescapedString(token));
                }
            }
        }

        StringTokenizer tokens = new StringTokenizer(parkings, "\n", true);
        while (tokens.hasMoreTokens()) {
            PayByPhoneParking result = new PayByPhoneParking();
            String parking = tokens.nextToken();
            String[] parkingTokens = parking.split(",");
            if (parkingTokens.length >= 6) {
                result.setSystemDate(systemDate);
                result.setPlate(getUnescapedString(parkingTokens[0]));
                result.setState(getUnescapedString(parkingTokens[1]));
                result.setStallNumber(getUnescapedString(parkingTokens[2]));
                result.setVendorStallNumber(getUnescapedString(parkingTokens[3]));
                result.setStartDateTime(getDateFromUTCString(getUnescapedString(parkingTokens[4])));
                result.setEndDateTime(getDateFromUTCString(getUnescapedString(parkingTokens[5])));

                results.add(result);
            }
        }

        return results;
    }

    public static Date getDateFromUTCString(String utcString) {
        if (utcString == null || utcString.equals("null") || utcString.equals("")) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ENGLISH);
        try {
            return sdf.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getUnescapedString(String str) {
        if (str == null) {
            return "";
        }

        return str.replaceAll("\"", "");
    }

    public static ArrayList<PayByPhoneParking> getParkings(List<PlatesResponse> platesResponses) {
        ArrayList<PayByPhoneParking> parkings = new ArrayList<>();
        for (PlatesResponse response : platesResponses) {
            PayByPhoneParking payByPhoneParking = new PayByPhoneParking();
            payByPhoneParking.setPlate(response.getParkingEntitlements().get(0).getPlate());
            payByPhoneParking.setStallNumber(response.getParkingEntitlements().get(0).getStall());
            payByPhoneParking.setState(response.getParkingEntitlements().get(0).getVehicleState());
            payByPhoneParking.setStartDateTime(DateUtil.getDateFromUTCStringPaybyPhone(response.getParkingEntitlements().get(0).getStartDateTime()));
            payByPhoneParking.setEndDateTime(DateUtil.getDateFromUTCStringPaybyPhone(response.getParkingEntitlements().get(0).getEndDateTime()));
            parkings.add(payByPhoneParking);
        }

        return parkings;
    }
}