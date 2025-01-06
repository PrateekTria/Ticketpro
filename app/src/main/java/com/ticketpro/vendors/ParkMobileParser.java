package com.ticketpro.vendors;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ticketpro.model.Feature;
import com.ticketpro.util.DateUtil;

public class ParkMobileParser {

	private static Map<String, ArrayList<ParkMobileParkingRight>> previousParkings;
	private static int expiredResultCacheTime = 10;

	public static ArrayList<ParkMobileZoneInfo> getZones(String responseJSON) {
		ArrayList<ParkMobileZoneInfo> zones = new ArrayList<>();
		if (responseJSON == null || responseJSON.isEmpty()) {
			return zones;
		}

		JSONObject response;
		try {
			response = new JSONObject(responseJSON);
			if (response.has("zones")) {
				JSONArray data = response.getJSONArray("zones");
				for (int i = 0; i < data.length(); i++) {
					zones.add(new ParkMobileZoneInfo(data.getJSONObject(i)));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return zones;
	}

	public static ArrayList<ParkMobileParkingRight> getParkingRights(String responseJSON) {
		ArrayList<ParkMobileParkingRight> zones = new ArrayList<>();
		if (responseJSON == null || responseJSON.isEmpty()) {
			return zones;
		}

		JSONObject response;
		Date systemDate = null;

		try {
			response = new JSONObject(responseJSON);
			if (response.has("resultTimeStampLocal")) {
				String systemTimeLocal = response.getString("resultTimeStampLocal");
				systemTimeLocal = systemTimeLocal.replaceAll("\"", "");
				systemDate = DateUtil.getDateFromUTCString(systemTimeLocal);
			}

			if (systemDate == null) {
				systemDate = new Date();
			}

			if (response.has("parkingRights")) {
				JSONArray data = response.getJSONArray("parkingRights");
				for (int i = 0; i < data.length(); i++) {
					ParkMobileParkingRight parking = new ParkMobileParkingRight(data.getJSONObject(i));
					parking.setSystemDate(systemDate);

					zones.add(parking);
				}
			}

			String expiredResultDuration = Feature.getFeatureValue(Feature.EXPIRED_RESULT_CACHE_TIME);
			if (expiredResultDuration != null && !expiredResultDuration.isEmpty()) {
				try {
					expiredResultCacheTime = Integer.parseInt(expiredResultDuration);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Get Previous result
			for (ParkMobileParkingRight parking : previousParkings.get("parkingRights")) {
				if (!hasParking(parking, zones)) {
					if (parking.getCreationDateDiffInSec() < (expiredResultCacheTime * 60)) {
						parking.setSystemDate(new Date());
						zones.add(parking);
					}
				}
			}

			previousParkings.put("parkingRights", zones);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return zones;
	}

	private static boolean hasParking(ParkMobileParkingRight newParking, ArrayList<ParkMobileParkingRight> parkings) {
		boolean result = false;
		
		for (ParkMobileParkingRight parking : parkings) {
			if (parking.getLpn().equalsIgnoreCase(newParking.getLpn())) {
				result = true;
				break;
			}
		}

		return result;
	}
}