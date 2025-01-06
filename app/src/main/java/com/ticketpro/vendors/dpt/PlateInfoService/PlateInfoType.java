package com.ticketpro.vendors.dpt.PlateInfoService;

import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import com.ticketpro.util.DateUtil;
import com.ticketpro.vendors.ParkingExpireInfo;
import com.ticketpro.vendors.dpt.PlateInfoService.WS_Enums.StatusType;

public class PlateInfoType implements KvmSerializable {

	public String expiryDate;
	public String purchasedDate;
	public String plateNumber;
	public String regionName;
	public StatusType status;

	public PlateInfoType(SoapObject soapObject) {
		if (soapObject == null)
			return;
		if (soapObject.hasProperty("expiryDate")) {
			Object obj = soapObject.getProperty("expiryDate");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j = (SoapPrimitive) obj;
				expiryDate = j.toString();
			} else if (obj instanceof String) {
				expiryDate = (String) obj;
			}
		}
		if (soapObject.hasProperty("purchasedDate")) {
			Object obj = soapObject.getProperty("purchasedDate");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j = (SoapPrimitive) obj;
				purchasedDate = j.toString();
			} else if (obj instanceof String) {
				purchasedDate = (String) obj;
			}
		}
		if (soapObject.hasProperty("plateNumber")) {
			Object obj = soapObject.getProperty("plateNumber");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j = (SoapPrimitive) obj;
				plateNumber = j.toString();
			} else if (obj instanceof String) {
				plateNumber = (String) obj;
			}
		}
		if (soapObject.hasProperty("regionName")) {
			Object obj = soapObject.getProperty("regionName");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j = (SoapPrimitive) obj;
				regionName = j.toString();
			} else if (obj instanceof String) {
				regionName = (String) obj;
			}
		}
		if (soapObject.hasProperty("status")) {
			Object obj = soapObject.getProperty("status");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j = (SoapPrimitive) obj;
				status = StatusType.fromString(j.toString());
			}
		}
	}

	@Override
	public Object getProperty(int arg0) {
		switch (arg0) {
		case 0:
			return expiryDate;
		case 1:
			return purchasedDate;
		case 2:
			return plateNumber;
		case 3:
			return regionName;
		case 4:
			return status.toString();
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 5;
	}

	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		switch (index) {
		case 0:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "expiryDate";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "purchasedDate";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "plateNumber";
			break;
		case 3:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "regionName";
			break;
		case 4:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "status";
			break;
		}
	}

	public String getInnerText() {
		return null;
	}

	public void setInnerText(String s) {
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
	}

	public Date getPurchasedDate(){
		return DateUtil.getDateFromZuluString(purchasedDate);
	}
	
	public Date getExpiryDate(){
		return DateUtil.getDateFromZuluString(expiryDate);
	}
	
	public ParkingExpireInfo getExpireInfo() {
		ParkingExpireInfo parkingExpireInfo = new ParkingExpireInfo();
		Date expireDate = DateUtil.getDateFromZuluString(expiryDate);
		if (expireDate == null) {
			return parkingExpireInfo;
		}

		String expireStr;
		long diffMinutes, diffHours, diffDays;
		long expiredDiff = System.currentTimeMillis() - expireDate.getTime();
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
				expireStr =/* "in " + */diffDays + " d " + diffHours + " h";

			} else if (diffHours >= 1) {
				expireStr =/* "in " + */diffHours + " h " + diffMinutes + " m";

			} else {
				expireStr = /*"in " +*/ diffMinutes + " m";
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

	public static class PlateComparator implements Comparator<PlateInfoType> {
		@Override
		public int compare(PlateInfoType item1, PlateInfoType item2) {
			return item1.plateNumber.compareToIgnoreCase(item2.plateNumber);
		}
	}

	public static class RegionComparator implements Comparator<PlateInfoType> {
		@Override
		public int compare(PlateInfoType item1, PlateInfoType item2) {
			return item1.regionName.compareToIgnoreCase(item2.regionName);
		}
	}
	
	public static class PurchaseDateComparator implements Comparator<PlateInfoType> {
		@Override
		public int compare(PlateInfoType item1, PlateInfoType item2) {
			Date date1 = DateUtil.getDateFromZuluString(item1.purchasedDate);
			Date date2 = DateUtil.getDateFromZuluString(item2.purchasedDate);
			if (date1 == null || date2 == null) {
				return 0;
			}

			return date1.compareTo(date2);
		}
	}

	public static class ExpireComparator implements Comparator<PlateInfoType> {
		@Override
		public int compare(PlateInfoType item1, PlateInfoType item2) {
			Date date1 = DateUtil.getDateFromZuluString(item1.expiryDate);
			Date date2 = DateUtil.getDateFromZuluString(item2.expiryDate);
			if (date1 == null || date2 == null) {
				return 0;
			}

			return date1.compareTo(date2);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder buffer=new StringBuilder();
		String propertyValue;
		PropertyInfo propertyInfo;
		
		buffer.append("{");
		
		for(int index=0; index < getPropertyCount(); index++){
			propertyInfo=new PropertyInfo();
			getPropertyInfo(index, null, propertyInfo);
			propertyValue = (String)getProperty(index);
			
			buffer.append(propertyInfo.name).append(":").append(propertyValue);
			
			if(index < (getPropertyCount() - 1)){
				buffer.append(",");
			}
		}
		
		buffer.append("}");
		
		return buffer.toString(); 
	}
}
