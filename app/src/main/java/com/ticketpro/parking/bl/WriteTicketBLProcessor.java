package com.ticketpro.parking.bl;

import android.content.Context;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.CustomerInfo;
import com.ticketpro.model.Duty;
import com.ticketpro.model.DutyReport;
import com.ticketpro.model.Location;
import com.ticketpro.model.Meter;
import com.ticketpro.model.MeterHandler;
import com.ticketpro.model.MobileNowLog;
import com.ticketpro.model.Permit;
import com.ticketpro.model.PermitHandler;
import com.ticketpro.model.RepeatOffender;
import com.ticketpro.model.RepeatOffenderParams;
import com.ticketpro.model.RepeatOffendersFromService;
import com.ticketpro.model.SearchVinHistoryHandler;
import com.ticketpro.model.SyncData;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.model.TicketViolationHandler;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WriteTicketBLProcessor extends BLProcessorImpl {

	public WriteTicketBLProcessor(TPApplication TPApp) {
		setLogger(WriteTicketBLProcessor.class.getName());
		setTPApplication(TPApp);
	}

	public boolean syncTicketData(ArrayList<SyncData> syncData, Context context) throws TPException {
		return proxy.syncTicketData(syncData, context);
	}

	public void updateChalkStatus(long chalkId, String status,String reason) throws TPException {
		proxy.updateChalkStatus(chalkId, status,reason);
	}

	public Ticket searchPlateHistory(String plate, String state) throws TPException {
		return proxy.searchPlateHistory(plate, state);
	}

	public Ticket searchVinHistory(String vin, String state) throws TPException {
		return proxy.searchVinHistory(vin, state);
	}

	// This code is changed by mohit 5/04/2023

	public void searchVinHistory1(String vin, String state, SearchVinHistoryHandler searchVinHistoryHandler) throws TPException {
		proxy.searchVinHistory1(vin, state,searchVinHistoryHandler);
	}
	
	public Permit searchPermitVinHistory(String vin, String state) throws TPException {
		return proxy.searchPermitVinHistory(vin, state);
	}

	// This code is changed by mohit 4/04/2023

	public void searchPermitVinHistory1(String vin, String state,PermitHandler permitHandler) throws TPException {
		 proxy.searchPermitVinHistory1(vin, state,permitHandler);
	}
	//End

	public Meter searchMeterHistory(String meter) throws TPException {
		return proxy.searchMeterHistory(meter);
	}

	// This code is changed by mohit 3/04/2023
	public void searchMeterHistory1(String meter, MeterHandler meterHandler) throws TPException {
		 proxy.searchMeterHistory1(meter,meterHandler);
	}

	public ChalkVehicle searchChalkHistory(String plate, String state) throws TPException {
		return proxy.searchChalkHistory(plate, state);
	}

	public Permit searchPermitHistory(String permit) throws TPException {
		return proxy.searchPermitHistory(permit);
	}


	// This code is changed by mohit 4/04/2023
	public void searchPermitHistory1(String permit, PermitHandler permitHandler) throws TPException {
		proxy.searchPermitHistory1(permit,permitHandler);
	}
	//End

	public Permit searchPermitByPlate(int custId, String plate, String state) throws TPException {
		ArrayList<Permit> permits = proxy.searchPermitsByPlate(custId, plate, state);
		if (permits != null && permits.size() > 0) {
			return permits.get(0);
		}

		return null;
	}

	public ArrayList<Permit> searchAllPermitByPlate(int custId, String plate, String state) throws TPException {
		ArrayList<Permit> permits = proxy.searchPermitsByPlate(custId, plate, state);
		if (permits != null && permits.size() > 0) {
			return permits;
		}

		return null;
	}

	/*public ArrayList<Hotlist> searchHotlist(String plate, String state) throws TPException {
		return proxy.searchHotlist(plate, state);
	}*/

	/*public ArrayList<Ticket> searchAllTickets(String plate, String state) throws TPException {
		return proxy.searchPlateHistories(plate, state);
	}
*/
	public ArrayList<TicketViolation> getLiveTicketViolations(long citationNumber) throws TPException {
		return proxy.getLiveTicketViolations(citationNumber);
	}

	public void getLiveTicketViolations1(long citationNumber, TicketViolationHandler ticketViolationHandler) throws TPException {
		getLiveTicketViolations1(citationNumber,ticketViolationHandler);
	}

	public List<String> getSearchList(int searchType) throws TPException {
		List<String> searchList = new ArrayList<>();
		switch (searchType) {
		case TPConstant.STATES_SEARCH_LIST:
			searchList = proxy.getStateTitles();
			break;

		case TPConstant.MAKE_SEARCH_LIST:
			searchList = proxy.getMakeAndModelTitles();
			break;

		case TPConstant.BODY_SEARCH_LIST:
			searchList = proxy.getBodyTitles();
			break;

		case TPConstant.COLOR_SEARCH_LIST:
			searchList = proxy.getColorTitles();
			break;

		case TPConstant.SELECT_LOCATION_LIST:
			Duty activeDuty = TPApp.getActiveDutyInfo();
			try {
				if (activeDuty.isAllLocations()) {
					searchList = proxy.getLocationTitles();
				} else {
					ArrayList<Location> locations = proxy.getLocations(activeDuty.getLocationGroups());
					for (Location location : locations) {
						if (!searchList.contains(location.getLocation())) {
							searchList.add(location.getLocation());
						}
					}
				}
			} catch (TPException e) {
				e.printStackTrace();
			}

			break;
		}

		return searchList;
	}

	public boolean sendEmail(String from, String[] to, String subject, String message) throws TPException {
		return proxy.sendEmail(from, to, subject, message);
	}

	public CustomerInfo getCustomerInfo(int custId) throws TPException {
		return proxy.getCustomerInfo(custId);
	}

	public JSONObject getValidTicket(int custId, long citationNumber) throws TPException {
		return proxy.getValidTicket(custId, citationNumber);
	}

	public boolean sendMobileNowLog(ArrayList<MobileNowLog> logs) throws TPException {
		return proxy.sendMobileNowLog(logs);
	}

	public void closeActiveDuty(boolean isServiceAvailable) {
		try {
			if (TPApp.getActiveDutyReport() == null) {
				TPApp.resetUserSession();
				return;
			}
			TPApp.getActiveDutyReport().setDateOut(new Date());
			TPApp.getActiveDutyReport().setCustId(TPApp.getCustId());
			DutyReport.insertDutyReport(TPApp.getActiveDutyReport());
			/*DatabaseHelper.getInstance().openWritableDatabase();
			DatabaseHelper.getInstance().insertOrReplace(TPApp.getActiveDutyReport().getContentValues(),
					"duty_reports");*/
			boolean result = false;
			if (isServiceAvailable) {
				result = proxy.updateDutyReport(TPApp.getActiveDutyReport());
			}
			if (!result) {
				SyncData syncData = new SyncData();
				syncData.setActivity("INSERT");
				syncData.setPrimaryKey(DutyReport.getLastInsertId() + "");
				syncData.setActivityDate(new Date());
				syncData.setCustId(TPApp.getCustId());
				syncData.setTableName(TPConstant.TABLE_DUTY_REPORTS);
				syncData.setStatus("Pending");

				SyncData.insertSyncData(syncData).subscribe();
				/*DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
				DatabaseHelper.getInstance().closeWritableDb();*/
			}

			// Close User Session
			TPApp.resetUserSession();
			
		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}

	public boolean syncRepeatOffender(String state, String plate, int violId, int custId,String created_date) throws TPException {
		return proxy.syncRepeatOffender(state, plate, violId, custId,created_date);
	}

	//change to retrofit by mohit 10 feb
	public boolean syncRepeatOffender(RepeatOffenderParams repeatOffenderParams) throws TPException {
		return proxy.syncRepeatOffender(repeatOffenderParams);
	}
	
	public RepeatOffender repeatOffenderFromService(int custId, String plate, int violId, String stateCode) throws TPException{
		return proxy.repeatOffenderFromService(custId, plate, violId, stateCode);
	}

	public RepeatOffender repeatOffenderFromService(RepeatOffendersFromService repeatOffendersFromService) throws TPException{
		return proxy.repeatOffenderFromService(repeatOffendersFromService);
	}

	public boolean getPlacard(String agencyCode, String user, String plate, String placard, String source) throws Exception {
		return proxy.getPlacard(agencyCode,user,plate,placard,source);
	}
}
