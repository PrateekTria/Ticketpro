package com.ticketpro.parking.service;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ticketpro.model.ErrorLog;
import com.ticketpro.model.Hotlist;
import com.ticketpro.model.MobileNowLog;
import com.ticketpro.model.RepeatOffender;
import com.ticketpro.model.RepeatOffenderParams;
import com.ticketpro.model.RepeatOffendersFromService;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.User;


public interface ServiceHandler {
	
	public boolean syncDatabase();
	public boolean syncDevices(JSONArray devices);
	public boolean syncUserSettings(JSONArray settings);
	public boolean syncUpdatesTickets(JSONArray tickets);
	public boolean syncTicketComments(int violationId,JSONArray comments);
	public boolean syncTicketViolations(JSONArray violations);
	public boolean syncTicketPictures(JSONArray pictures);
	public boolean syncChalks(JSONArray chalks);
	public boolean syncChalkPictures(JSONArray chalkPictures);
	public boolean syncChalkComments(JSONArray chalkComments);
	public boolean syncDutyReports(JSONArray dutyReports);
	public boolean syncCallInReports(JSONArray callInReports);
	public boolean syncSpecialActivityReports(JSONArray specialActivityReports);
	public boolean syncSpecialActivityPicture(JSONArray specialActivityReports);
	public boolean syncUploadImages(ArrayList<String> images);
	public boolean syncUploadImagesEdit(long citationNumber, int s_no, ArrayList<String> images);
	public boolean syncSelfiImages(ArrayList<String> images,Context ctx);
	public ArrayList<String> verifyAndSyncTickets(int custId, ArrayList<String> citations) throws Exception;
	public JSONArray syncTickets(JSONArray tickets) throws Exception;
	public boolean syncHotListReports(JSONArray hotListReports) throws Exception;
	
	public User doLogin(String username,String password) throws Exception;
	
	public JSONObject getDeviceInfo(String deviceName) throws Exception;
	public JSONObject getClientInfo(int custId) throws Exception;
	public JSONArray getUsers(int custId) throws Exception;
	public JSONArray getGenetecPatrollers(int custId) throws Exception;
	public JSONArray getCustomers(int custId) throws Exception;
	public JSONArray getMakesAndModels(int custId, boolean fullSync) throws Exception;
	public JSONArray getMessages(int custId, boolean fullSync) throws Exception;
	public JSONArray getBodies(int custId, boolean fullSync) throws Exception;
	public JSONArray getDeviceGroup(int custId, boolean fullSync) throws Exception;
	public JSONArray getContacts(int custId, boolean fullSync) throws Exception;
	public JSONArray getColors(int custId, boolean fullSync) throws Exception;
	public JSONArray getFeatures(int custId, boolean fullSync) throws Exception;
	public JSONArray getLocations(int custId, boolean fullSync) throws Exception;
	public JSONArray getDuties(int custId, boolean fullSync) throws Exception;
	public JSONArray getHotlist(int custId, boolean fullSync) throws Exception;
	public JSONArray getMeters(int custId, boolean fullSync) throws Exception;
	public JSONArray getPermits(int custId, boolean fullSync) throws Exception;
	public JSONArray getStates(int custId, boolean fullSync) throws Exception;
	public JSONArray getViolations(int custId, boolean fullSync) throws Exception;
	public JSONArray getComments(int custId, boolean fullSync) throws Exception;
	public JSONArray getDirections(int custId, boolean fullSync) throws Exception;
	public JSONArray getStreetPrefixes(int custId, boolean fullSync) throws Exception;
	public JSONArray getStreetSuffixes(int custId, boolean fullSync) throws Exception;
	public JSONArray getDurations(int custId, boolean fullSync)  throws Exception;
	public JSONArray getUserSettings(int custId) throws Exception;
	public JSONArray getZones(int custId, boolean fullSync) throws Exception;
	public JSONArray getTickets(int userId,int deviceId)  throws Exception;
	public JSONArray getTicketComments(long citationNumber)  throws Exception;
	public JSONArray getTicketPictures(long citationNumber)  throws Exception;
	public JSONArray getTicketViolations(long citationNumber)  throws Exception;
	public JSONArray getChalkVehicles(int userId,int deviceId)  throws Exception;
	public JSONArray getChalkVehicle(int deviceId)  throws Exception;
	
	public JSONArray getChalkPictures(long chalkId) throws Exception;
	public JSONArray getChalkComments(long chalkId) throws Exception;
	public JSONArray getGPSLocations(int custId,boolean fullSync) throws Exception;
	public JSONArray getVoidTicketReasons(int custId, boolean fullSync) throws Exception;
	public JSONArray getSpecialActivities(int custId, boolean fullSync) throws Exception;
	public JSONArray getSpecialActivityDispositions(int custId, boolean fullSync) throws Exception;
	public JSONArray getPrintMacros(int custId, boolean fullSync) throws Exception;
	public JSONArray getPrintTemplates(int custId, boolean fullSync) throws Exception;
	public JSONArray getCallInList(int custId, boolean fullSync) throws Exception;
	public JSONArray getTicketHistory(int custId,Date startDate,Date endDate) throws Exception;
	public JSONArray getRepeatOffenders(int custId, boolean fullSync) throws Exception;
	public JSONArray getRepeatOffendersLive(String stateCode, String plate, int violationId) throws Exception;

	////Edited by mohit//
	public JSONArray getRepeatOffendersLive(RepeatOffendersFromService repeatOffendersFromService) throws Exception;
	//End
	public boolean updateRepeatOffendersCount(String stateCode, String plate, int violationId, int custId,String created_date) throws Exception;
	public boolean updateRepeatOffendersCount(RepeatOffenderParams repeatOffenderParams) throws Exception;
	public JSONArray lastUpDateRepeatOffenders(ArrayList<RepeatOffender> repeatOffenders) throws Exception;

	public JSONArray searchMeters(int custId,String meter) throws Exception;
	public JSONArray searchPermits(int custId,String permit) throws Exception;
	public JSONArray searchPlates(int custId,String plate,String state) throws Exception;
	public JSONArray searchPlatesVin(int custId,String vin,String state) throws Exception;
	public JSONArray searchChalks(int custId,String plate,String state) throws Exception;
	public JSONArray searchVins(int custId,String vin, String state) throws Exception;
	public JSONArray searchPermitVins(int custId,String vin, String state) throws Exception;
	
	public JSONArray searchHotlist(int custId,String plate,String state) throws Exception;
	public JSONArray searchPermitsByPlate(int custId,String plate,String state) throws Exception;
	public JSONArray searchRepeatOffenders(int custId,String plate,String violation) throws Exception;
	
	public JSONObject getGPSLocation(double lat,double lon) throws Exception;
	public JSONArray getLocationGroups(int custId,boolean fullSync) throws Exception;
	public JSONArray getLocationGroupLocations(int custId,boolean fullSync) throws Exception;
	public JSONArray getCommentGroups(int custId,boolean fullSync) throws Exception;
	public JSONArray getCommentGroupComments(int custId,boolean fullSync) throws Exception;
	public JSONArray getViolationGroups(int custId,boolean fullSync) throws Exception;
	public JSONArray getViolationGroupViolations(int custId,boolean fullSync) throws Exception;
	public JSONObject registerDevice(JSONObject device) throws Exception;
	public boolean updateGCMRegistrationId(String deviceName,String registrationId) throws Exception;
	public boolean sendErrorLog(ArrayList<ErrorLog> errors) throws Exception;
	public boolean sendMobileNowLog(ArrayList<MobileNowLog> logs) throws Exception;
	public boolean sendEmail(String from, String[] to, String subject,String message) throws Exception;
	public boolean newHotList(Hotlist hotList) throws Exception;
	
	public JSONArray getUserChalks(int custId,int userId,Date fromDate,Date toDate,int durationId) throws Exception;
	public boolean updateChalkStatus(long chalkId,String status,String reason) throws Exception;
	public boolean updateTicketComments(int violationId, ArrayList<TicketComment> ticketComment) throws Exception;
	public boolean deleteTicketComments(long citationNumber, int violationId, String comment) throws Exception;
	public boolean deleteTicketPicture(long citationNumber, String imageName) throws Exception;
	public boolean updateTicketPicture(long citationNumber, TicketPicture picture) throws Exception;
	
	public JSONObject getValidTicket(int custId, long citationNumber) throws Exception;
	
	public JSONArray getVendors(int custId, boolean fullSync) throws Exception;
	public JSONArray getVendorItems(int custId, boolean fullSync) throws Exception;
	public JSONArray getVendorServices(int custId,boolean fullSync) throws Exception;
	public JSONArray getVendorServiceRegistrations(int custId,int userId, int deviceId,boolean fullSync) throws Exception;
	public JSONArray getModules(int custId, boolean fullSync) throws Exception;
	public JSONArray getCustomerModules(int custId, boolean fullSync) throws Exception;
	public JSONObject getVersionUpdates(int custId, String module) throws JSONException;
	public JSONArray getLastUpdateRepeatOffender(int custId, String module) throws JSONException;

	//Code by Sanjiv

	/**
	 *
	 * @param custId
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public JSONObject getEULA(int custId, int moduleId) throws Exception;

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	public JSONObject updateEULAAcceptance(int userId,int recId, String isAcceptance,int custId) throws Exception;

    JSONArray getActivityList(int custId, String date) throws Exception;

    JSONArray grtSpecialActivitieslocation(int custId,boolean fullSync) throws Exception;

	public boolean getPlacard(String agencyCode, String user, String plate, String placard, String source) throws Exception;

}
