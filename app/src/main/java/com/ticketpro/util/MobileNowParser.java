package com.ticketpro.util;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.util.Log;

import com.ticketpro.model.MobileNowZoneInfo;
import com.ticketpro.model.MobileNowZoneItem;
import com.ticketpro.model.VendorItem;
import com.ticketpro.parking.activity.TPApplication;

public class MobileNowParser {
	public static String getSentence(String input, String word) {
	    Matcher matcher = Pattern.compile(word, Pattern.LITERAL | Pattern.CASE_INSENSITIVE).matcher(input);
	    if(matcher.find()) {
	        BreakIterator br = BreakIterator.getSentenceInstance(Locale.ENGLISH);
	        br.setText(input);
	        int start = br.preceding(matcher.start());
	        int end = br.following(matcher.end());
	        return input.substring(start, end);
	    }
	    
	    return null;
	}
	
	public static String getCarCheckResponseHTML(String response) {
		if (response == null) {
			return "";
		}

		StringBuffer message = new StringBuffer();
		StringTokenizer tokens = new StringTokenizer(response, "\n", true);
		while (tokens.hasMoreTokens()) {
			String line = tokens.nextToken();
			message.append(MobileNowParser.getCarCheckLineHTML(line));
		}

		return message.toString();
	}

	public static String getPlateResponseHTML(String response) {
		if (response == null) {
			return "";
		}

		StringBuffer message = new StringBuffer();
		StringTokenizer tokens = new StringTokenizer(response, ".", true);
		while (tokens.hasMoreTokens()) {
			String line = tokens.nextToken();
			message.append(MobileNowParser.getPlateLineHTML(line));
		}

		return message.toString();
	}

	private static String getCarCheckLineHTML(String responseLine) {
		String message = "";
		if (responseLine == null) {
			return "";
		}

		String resultCode;
		String plate = "";
		String status = "";
		String zone = "";
		Date startTime = null;
		Date endTime = null;
		String info = null;
		
		try {
		    if(responseLine.contains("parking") && responseLine.contains("zone") && responseLine.contains("since")){
				String[] tokens = responseLine.split(":", 4);
				if(tokens.length > 3){
					plate = tokens[1];
					info = tokens[3];
					status = "Parking";
					
					StringTokenizer token = new StringTokenizer(info, " ");
					while(token.hasMoreTokens()){
						String next = token.nextToken();
						if("zone".equalsIgnoreCase(next)){
							zone = token.nextToken();
						}
						
						if("is".equalsIgnoreCase(next)){
							status = token.nextToken();
						}
					}
					
					message = getMessage(plate, status, zone, info, startTime, endTime);
					
				}else{
					message = responseLine;
				}
			}else{
				String[] tokens = responseLine.split(":", 6);
				if (tokens.length > 1) {
					resultCode = tokens[0];
					if (resultCode.equals("0")) {
						return tokens[1];
					}

					plate = tokens[1];
					if (plate.equals("0")) {
						return tokens[2];
					}

					status = tokens[2];

					if (status.equals("0")) {
						status = "No Cars Parking";
					} 
					else if (status.equals("1")) {
						status = "Parking";
					} 
					else if (status.equals("2")) {
						status = "Alert";
					} 
					else if (status.equals("3")) {
						status = "Parking Permit";
					} 
					else if (status.equals("4")) {
						status = "Indeterminate Parking";
					} 
					else if (status.equals("5")) {
						status = "Indeterminate Parking";
					}

					if (status.equalsIgnoreCase("No Cars Parking")) {
						info = tokens[3];
						startTime = null;
						endTime = null;
						zone = null;
					} else {
						zone = tokens[3];
						
						if(tokens.length > 5){
							startTime = DateUtil.getDateFromTimestamp(tokens[4]);
							endTime = DateUtil.getDateFromTimestamp(tokens[5]);
						}
					}

					message = getMessage(plate, status, zone, info, startTime, endTime);
				}
			}
		} catch (Exception e) {
			Log.e("MobileNow", TPUtility.getPrintStackTrace(e));
			return "<p>Error parsing MobileNow response.</p>";
		}
		
		return message;
	}
	
	
	private static String getMessage(String plate, String status, String zone, String info, Date startTime, Date endTime) {
		StringBuffer message = new StringBuffer();
		String expireStr = "";
		long diffMinutes = 0, diffHours, diffDays;

		message.append("<table>");
		message.append("<tr>");
		message.append("<td colspan=3>Result for " + plate + "</td>");
		message.append("</tr>");

		message.append("<tr>");
		message.append("<td valign=top>Status </td><td valign=top> : </td><td valign=top>" + status + "</td>");
		message.append("</tr>");
		
		if(StringUtil.isEmpty(info)){
			if (startTime != null && endTime != null) {
				long expiredDiff = startTime.getTime() - endTime.getTime();
				if (expiredDiff > 0) {
					diffMinutes = expiredDiff / (60 * 1000) % 60;
					diffHours = expiredDiff / (60 * 60 * 1000) % 24;
					diffDays = expiredDiff / (24 * 60 * 60 * 1000);
					if (diffDays >= 1) {
						expireStr = diffDays + "/" + diffHours + " days/hrs ago";
					} else if (diffHours >= 1) {
						expireStr = diffHours + "/" + diffMinutes + " hrs/mins ago";
					} else {
						expireStr = diffMinutes + " mins ago";
					}
				} else {
					expiredDiff = Math.abs(expiredDiff);
					diffMinutes = expiredDiff / (60 * 1000) % 60;
					diffHours = expiredDiff / (60 * 60 * 1000) % 24;
					diffDays = expiredDiff / (24 * 60 * 60 * 1000);
					if (diffDays >= 1) {
						expireStr = "in " + diffDays + "/" + diffHours + " days/hrs";
					} else if (diffHours >= 1) {
						expireStr = "in " + diffHours + "/" + diffMinutes + " hrs/mins";
					} else {
						expireStr = "in " + diffMinutes + " mins";
					}
				}

				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mma");
				info = "Car is parking in zone " + zone + " since " + sdf.format(startTime);
			} else {
				info = "Car is parking in zone " + zone;
			}
		}
		
		message.append("<tr>");
		message.append("<td valign=top>Info </td><td valign=top> : </td><td valign=top>" + info + "</td>");
		message.append("</tr>");

		if (!expireStr.equals("") && endTime!=null) {
			message.append("<tr>");
			if (diffMinutes > 3) {
				message.append("<td valign=top>Exp </td><td valign=top> : </td>");
				message.append("<td valign=top>" + expireStr + " <br/> " + DateUtil.getShortDateTime(endTime) + "</td>");
			} else {
				message.append("<td valign=top>Exp </td><td valign=top> : </td>");
				message.append("<td valign=top color=red>" + expireStr + " <br/> " + DateUtil.getShortDateTime(endTime) + "</td>");
			}

			message.append("</tr>");
		}

		message.append("</table>");
		
		return message.toString();
	}

	@SuppressLint("SimpleDateFormat")
	private static String getPlateLineHTML(String responseLine) {
		if (responseLine == null) {
			return "";
		}

		StringBuffer message = new StringBuffer();
		String resultCode;
		String item;
		String status;
		String info;

		try {
			String[] tokens = responseLine.split(":", 4);
			if (tokens.length > 1) {
				resultCode = tokens[0];
				if (resultCode.equals("0")) {
					return tokens[1];
				}

				item = tokens[1];
				if (item.equals("0")) {
					return tokens[2];
				}

				status = tokens[2];
				info = tokens.length > 3 ? tokens[3] : "NA";

				message.append("<table>");
				message.append("<tr>");
				message.append("<td colspan=3>Result for " + item + "</td>");
				message.append("</tr>");

				if (status.equals("0")) {
					status = "No Cars Parking";

				} else if (status.equals("1")) {
					status = "Parking";

				} else if (status.equals("2")) {
					status = "Alert";

				} else if (status.equals("3")) {
					status = "Parking Permit";

				} else if (status.equals("4")) {
					status = "Indeterminate Parking";

				} else if (status.equals("5")) {
					status = "Indeterminate Parking";
				}

				message.append("<tr>");
				message.append("<td valign=top>Status </td><td valign=top> : </td><td valign=top>" + status + "</td>");
				message.append("</tr>");

				message.append("<tr>");
				message.append("<td valign=top>Info </td><td valign=top> : </td><td valign=top>" + info + "</td>");
				message.append("</tr>");

				try {
					if (info.contains("in zone")) {
						int startIndex = info.indexOf("in zone") + 7;
						int endIndex = info.indexOf("since");

						String zoneString = info.substring(startIndex, endIndex);
						if (zoneString != null) {
							zoneString = zoneString.trim();
						}

						VendorItem vendorItem = VendorItem.getVendorZone(zoneString);

						startIndex = endIndex + 6;
						String dateString = info.substring(startIndex, startIndex + 16);

						Date startDate;
						SimpleDateFormat sdf;
						try {
							sdf = new SimpleDateFormat("MM/dd/yy hh:mma");
							startDate = sdf.parse(dateString);
						} catch (Exception e) {
							sdf = new SimpleDateFormat("hh:mma MM/dd/yy");
							startDate = sdf.parse(dateString);
						}

						if (startDate != null && vendorItem != null) {
							String expireStr = "";
							Date expiryDate = TPUtility.addMinutesToDate(vendorItem.getDuration(), startDate);

							long diffMinutes, diffHours, diffDays;
							long expiredDiff = new Date().getTime() - expiryDate.getTime();
							if (expiredDiff > 0) {
								diffMinutes = expiredDiff / (60 * 1000) % 60;
								diffHours = expiredDiff / (60 * 60 * 1000) % 24;
								diffDays = expiredDiff / (24 * 60 * 60 * 1000);
								if (diffDays >= 1) {
									expireStr = diffDays + "/" + diffHours + " days/hrs ago";

								} else if (diffHours >= 1) {
									expireStr = diffHours + "/" + diffMinutes + " hrs/mins ago";

								} else {
									expireStr = diffMinutes + " mins ago";
								}
							} else {
								expiredDiff = Math.abs(expiredDiff);
								diffMinutes = expiredDiff / (60 * 1000) % 60;
								diffHours = expiredDiff / (60 * 60 * 1000) % 24;
								diffDays = expiredDiff / (24 * 60 * 60 * 1000);

								if (diffDays >= 1) {
									expireStr = "in " + diffDays + "/" + diffHours + " days/hrs";

								} else if (diffHours >= 1) {
									expireStr = "in " + diffHours + "/" + diffMinutes + " hrs/mins";

								} else {
									expireStr = "in " + diffMinutes + " mins";
								}
							}

							if (!expireStr.equals("")) {
								message.append("<tr>");
								if (diffMinutes > 3) {
									message.append("<td valign=top>Exp </td><td valign=top> : </td>");
									message.append("<td valign=top>" + expireStr + " <br/> " + DateUtil.getShortDateTime(expiryDate) + "</td>");
								} else {
									message.append("<td valign=top>Exp </td><td valign=top> : </td>");
									message.append("<td valign=top color=red>" + expireStr + " <br/> " + DateUtil.getShortDateTime(expiryDate) + "</td>");
								}

								message.append("</tr>");
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				message.append("</table>");
			}
		} catch (Exception e) {
			Log.e("MobileNow", TPUtility.getPrintStackTrace(e));
			return "";
		}

		return message.toString();
	}

	public static MobileNowZoneInfo getZoneInfo(String response) {
		if (response == null) {
			return new MobileNowZoneInfo();
		}

		String[] tokens = response.split("\n");
		String[] resultTokens;
		String zoneType;

		MobileNowZoneInfo zoneInfo = new MobileNowZoneInfo();
		try {
			resultTokens = tokens[0].split(":");
			if (resultTokens.length > 1) {
				if (resultTokens[0].equals("0")) {
					zoneInfo.setMessage(resultTokens[1]);
					zoneInfo.setValid(false);
					zoneInfo.setZone(TPApplication.getInstance().getActiveDutyInfo().getCode());

					return zoneInfo;
				}

				zoneType = resultTokens[1];
				if (zoneType.equalsIgnoreCase("L")) {
					zoneType = "Plate";

				} else if (zoneType.equalsIgnoreCase("I")) {
					zoneType = "IDCard";

				} else if (zoneType.equalsIgnoreCase("N")) {
					zoneType = "Space";
				}

				zoneInfo.setZone(TPApplication.getInstance().getActiveDutyInfo().getCode());
				zoneInfo.setZoneType(zoneType);
				zoneInfo.setSysDate(DateUtil.getDateFromTimestamp(resultTokens[2]));
				zoneInfo.setValid(true);

				try {
					zoneInfo.setGracePeriod(Integer.parseInt(resultTokens[3]));
				} catch (Exception e) {
				}

				for (int i = 1; i < tokens.length; i++) {
					MobileNowZoneItem item = MobileNowParser.getZoneItem(zoneType, tokens[i]);
					if (item != null) {
						zoneInfo.getZoneItems().add(item);
					}
				}
			}

		} catch (Exception e) {
			Log.e("MobileNow", TPUtility.getPrintStackTrace(e));
			return zoneInfo;
		}

		return zoneInfo;
	}

	private static MobileNowZoneItem getZoneItem(String parentZoneType, String zoneItemText) {
		MobileNowZoneItem zoneItem = null;

		String item;
		String zoneType;
		String responseType;
		String strStartDate;
		String strEndDate;

		try {
			String[] tokens = zoneItemText.split(":");
			if (tokens.length < 4) {
				return null;
			}

			zoneItem = new MobileNowZoneItem();
			if (tokens.length == 5) {
				zoneType = tokens[0];
				item = tokens[1];
				responseType = tokens[2];
				strStartDate = tokens[3];
				strEndDate = tokens[4];
			} else {
				item = tokens[0];
				responseType = tokens[1];
				strStartDate = tokens[2];
				strEndDate = tokens[3];
				zoneType = parentZoneType;
			}

			if (responseType.equals("0")) {
				responseType = "No Cars Parking";

			} else if (responseType.equals("1")) {
				responseType = "Parking";

			} else if (responseType.equals("2")) {
				responseType = "Alert";

			} else if (responseType.equals("3")) {
				responseType = "Parking Permit";

			} else if (responseType.equals("4")) {
				responseType = "Indeterminate Parking";

			} else if (responseType.equals("5")) {
				responseType = "Indeterminate Parking";
			}

			zoneItem.setItem(item);
			zoneItem.setZoneType(zoneType);
			zoneItem.setResponseType(responseType);
			zoneItem.setStartTime(DateUtil.getDateFromTimestamp(strStartDate));
			zoneItem.setEndTime(DateUtil.getDateFromTimestamp(strEndDate));

		} catch (Exception e) {
			Log.e("MobileNow", TPUtility.getPrintStackTrace(e));
			zoneItem = null;
		}

		return zoneItem;
	}
}