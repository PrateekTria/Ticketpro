package com.ticketpro.parking.proxy;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ticketpro.model.GenetecPatrollers;
import com.ticketpro.model.HotListHandler;
import com.ticketpro.exception.TPException;
import com.ticketpro.model.Body;
import com.ticketpro.model.CallInList;
import com.ticketpro.model.CallInReport;
import com.ticketpro.model.ChalkComment;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Color;
import com.ticketpro.model.Comment;
import com.ticketpro.model.CommentGroup;
import com.ticketpro.model.CommentGroupComment;
import com.ticketpro.model.Contact;
import com.ticketpro.model.CustomerInfo;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.model.Direction;
import com.ticketpro.model.Duration;
import com.ticketpro.model.Duty;
import com.ticketpro.model.DutyReport;
import com.ticketpro.model.ErrorLog;
import com.ticketpro.model.EulaModel;
import com.ticketpro.model.Feature;
import com.ticketpro.model.GPSLocation;
import com.ticketpro.model.Hotlist;
import com.ticketpro.model.Location;
import com.ticketpro.model.LocationGroup;
import com.ticketpro.model.LocationGroupLocation;
import com.ticketpro.model.MaintenanceLog;
import com.ticketpro.model.MakeAndModel;
import com.ticketpro.model.Message;
import com.ticketpro.model.Meter;
import com.ticketpro.model.MeterHandler;
import com.ticketpro.model.MobileNowLog;
import com.ticketpro.model.Permit;
import com.ticketpro.model.PermitHandler;
import com.ticketpro.model.PrintMacro;
import com.ticketpro.model.PrintTemplate;
import com.ticketpro.model.RepeatOffender;
import com.ticketpro.model.RepeatOffenderParams;
import com.ticketpro.model.RepeatOffendersFromService;
import com.ticketpro.model.SearchVinHistoryHandler;
import com.ticketpro.model.ServiceResult;
import com.ticketpro.model.SpecialActivity;
import com.ticketpro.model.SpecialActivityDisposition;
import com.ticketpro.model.SpecialActivityHandler;
import com.ticketpro.model.SpecialActivityReport;
import com.ticketpro.model.State;
import com.ticketpro.model.StreetPrefix;
import com.ticketpro.model.StreetSuffix;
import com.ticketpro.model.SyncData;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.model.TicketViolationHandler;
import com.ticketpro.model.User;
import com.ticketpro.model.Vendor;
import com.ticketpro.model.VendorItem;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceRegistration;
import com.ticketpro.model.VersionUpdate;
import com.ticketpro.model.Violation;
import com.ticketpro.model.ViolationGroup;
import com.ticketpro.model.ViolationGroupViolation;
import com.ticketpro.model.VoidTicketReason;
import com.ticketpro.model.Zone;
import com.ticketpro.parking.service.ServiceHandler;

import android.content.Context;
import android.os.Handler;

public interface Proxy {
	public void setServiceHandler(ServiceHandler service); 
	public User doLogin(String username,String password) throws TPException;
	public String getLastSynchTime() throws TPException;
	public CustomerInfo getCustomerInfo(int custId) throws TPException;
	public void updateDeviceInfo(Context context) throws TPException;
	public void verifyAndUploadTickets(Context context, boolean fullSync) throws TPException;
	public String getVersionNo();
	
	public ArrayList<Color> getColors() throws TPException;
	public ArrayList<Body> getBodies() throws TPException;
	public ArrayList<Contact> getContacts() throws TPException;
	public ArrayList<Comment> getComments() throws TPException;
	public ArrayList<Feature> getFeatures() throws TPException;
	public ArrayList<Direction> getDirections() throws TPException;
	public ArrayList<Duration> getDurations() throws TPException;
	public ArrayList<Duty> getDuties() throws TPException;
	public ArrayList<Location> getLocations() throws TPException;
	public ArrayList<GPSLocation> getGPSLocations() throws TPException;
	public ArrayList<MakeAndModel> getMakeAndModels() throws TPException;
	public ArrayList<Message> getMessages() throws TPException;
	public ArrayList<Message> getServerMessages() throws TPException;
	public ArrayList<Meter> getMeters() throws TPException;
	public ArrayList<Permit> getPermits() throws TPException;
	public ArrayList<State> getStates() throws TPException;
	public ArrayList<Ticket> getTickets(int userId,int deviceId, String userType) throws TPException;
	public ArrayList<StreetPrefix> getStreetPrefixes() throws TPException;
	public ArrayList<StreetSuffix> getStreetSuffixes() throws TPException;
	public ArrayList<User> getUsers() throws TPException;
	public ArrayList<GenetecPatrollers> getGenetecPatrollers() throws TPException;
	public ArrayList<Zone> getZones() throws TPException;
	public ArrayList<Violation> getViolations() throws TPException;
	public ArrayList<Hotlist> getHotlist() throws TPException;
	public ArrayList<TicketComment> getLiveTicketComments(long ticketId) throws TPException;
	public ArrayList<TicketPicture> getLiveTicketPictures(long ticketId) throws TPException;
	public ArrayList<TicketViolation> getLiveTicketViolations(long ticketId) throws TPException;

	public void getLiveTicketViolations1(long ticketId, TicketViolationHandler ticketViolationHandler) throws TPException;
	public ArrayList<ChalkVehicle> getChalkVehicles(int userId,int deviceId) throws TPException;
	public ArrayList<ChalkVehicle> getAllChalkedVehicles(int deviceId) throws TPException;
	public ArrayList<ChalkPicture> getChalkPictures(long chalkId) throws TPException;
	public ArrayList<ChalkComment> getChalkComments(long chalkId) throws TPException;
	public ArrayList<VoidTicketReason> getVoidTicketReasons(int custId) throws TPException;
	public ArrayList<SpecialActivity> getSpecialActivities(int custId) throws Exception;
	public ArrayList<SpecialActivityDisposition> getSpecialActivityDispositions(int custId) throws Exception;
	public ArrayList<PrintMacro> getPrintMacros(int custId) throws Exception;
	public ArrayList<PrintTemplate> getPrintTemplates(int custId) throws Exception;
	public ArrayList<CallInList> getCallInList(int custId) throws Exception;
	public ArrayList<String> getViolationTitles() throws TPException;
	public ArrayList<String> getBodyTitles() throws TPException;
	public ArrayList<String> getColorTitles() throws TPException;
	public ArrayList<String> getCommentTitles() throws TPException;
	public ArrayList<String> getStateTitles() throws TPException;
	public ArrayList<String> getMakeAndModelTitles() throws TPException;
	public ArrayList<String> getZonesTitles() throws TPException;
	public ArrayList<String> getDurationsTitles() throws TPException;
	public ArrayList<String> getDirectionsTitles() throws TPException;
	public ArrayList<String> getStreetPrefixTitles() throws TPException;
	public ArrayList<String> getStreetSuffixTitles() throws TPException;
	public ArrayList<String> getLocationTitles() throws TPException;
	public ArrayList<String> getSpecialActivitiesTitles() throws TPException;
	public ArrayList<String> getSpecialDispositionsTitles() throws TPException;
	
	public ArrayList<SpecialActivityReport> getActivityReport(int custId,String date) throws  TPException;
	public void getActivityReport1(int custId, String date, SpecialActivityHandler specialActivityHandler) throws  TPException;

	public ArrayList<LocationGroup> getLocationGroups(int custId) throws Exception;
	public ArrayList<LocationGroupLocation> getLocationGroupLocations(int custId) throws Exception;
	public ArrayList<CommentGroup> getCommentGroups(int custId) throws Exception;
	public ArrayList<CommentGroupComment> getCommentGroupComments(int custId) throws Exception;
	public ArrayList<ViolationGroup> getViolationGroups(int custId) throws Exception;
	public ArrayList<ViolationGroupViolation> getViolationGroupViolations(int custId) throws Exception;
	
	
	// Device Configuration and InitialSetup
	public boolean syncDatabase(boolean fullSync, Context context, Handler.Callback callback) throws TPException;
	public ServiceResult isRegisteredDevice(String deviceName) throws TPException;
	public DeviceInfo registeredDevice(DeviceInfo device) throws TPException;
	public void updateAllTables(boolean fullSync, Handler.Callback callback) throws TPException;
	public void updateTicketHistory() throws TPException;
	public void uploadAllChanges(Context context, boolean fullSync) throws TPException;
	public void syncData(ArrayList<SyncData> syncData,Context context) throws TPException;
	public Meter  searchMeterHistory(String meter) throws TPException;

	public void  searchMeterHistory1(String meter, MeterHandler meterHandler) throws TPException;
	public Permit searchPermitHistory(String permit) throws TPException;

	// This code is changed by mohit 4/04/2023
	public void searchPermitHistory1(String permit, PermitHandler permitHandler) throws TPException;
	//End
	public Ticket searchVinHistory( String vin,String State) throws TPException;

	// This code is changed by mohit 5/04/2023
	public void searchVinHistory1(String vin, String State, SearchVinHistoryHandler searchVinHistoryHandler) throws TPException;
	//End
	public Permit searchPermitVinHistory(String vin,String State) throws TPException;


	// This code is changed by mohit 4/04/2023
	public void searchPermitVinHistory1(String vin,String State,PermitHandler permitHandler) throws TPException;
	//End
	public Ticket searchPlateHistory(String plate,String state) throws TPException;
	public ArrayList<Permit> searchPermitsByPlate(int custId, String plate,String state) throws TPException;
	public ArrayList<Hotlist> searchHotlist(String plate,String state) throws TPException;
	public RepeatOffender searchRepeatOffenders(String plate,String violation) throws TPException;
	public ChalkVehicle searchChalkHistory(String plate,String state) throws TPException;
	public boolean updateTickets(ArrayList<Ticket> tickets);
	public boolean updateTicketsComments(int violationId, ArrayList<TicketComment> ticketComments);
	public boolean deleteTicketComment(long citationNumber, int violationId, String comment);
	public boolean deleteTicketPicture(long citationNumber, String imageName);
	public boolean updateTicketPicture(long citationNumber, TicketPicture picture);
	// This code is changed by mohit 10/4/2023
	public boolean updateTicketPicture1(long citationNumber, TicketPicture picture);
	public boolean updateCallInReport(CallInReport report);
	public boolean updateDutyReport(DutyReport report);

	public void updateDutyReport1(DutyReport report,int custId);
	public boolean updateGCMRegistrationId(String deviceName,String GCMRegId) throws TPException;
	public boolean sendErrorLog(ArrayList<ErrorLog> errors) throws TPException;
	public boolean sendMobileNowLog(ArrayList<MobileNowLog> logs) throws TPException;
	public boolean sendEmail(String from,String[] to,String subject,String message) throws TPException;
	public boolean newHotlist(Hotlist hotList, HotListHandler hotListHandler) throws TPException;
	public boolean syncRepeatOffender(String stateCode, String plate, int violId, int custId,String created_date) throws TPException;
	public boolean syncRepeatOffender(RepeatOffenderParams repeatOffenderParams) throws TPException;
	public boolean lastUpDateRepeatOffender(ArrayList<RepeatOffender> repeatOffenders) throws TPException;

	// Duty Group Locations, Comments and Violations
	public ArrayList<Location> getLocations(String groups) throws TPException;
	public ArrayList<Violation> getViolations(String groups) throws TPException;
	public ArrayList<Comment> getComments(String groups) throws TPException;
	public ArrayList<ChalkVehicle> getUserChalks(int userId,Date fromDate,Date toDate,int durationId) throws TPException;
	public void syncChalkPictures(final ArrayList<SyncData> syncData,final Context context) throws TPException;
	public boolean updateChalkStatus(long chalkId,String status,String reason) throws TPException;
	public void uploadImages(long citationNumber, int s_no, ArrayList<String> images) throws TPException;
	public boolean uploadSelfi(ArrayList<String> images,Context ctx) throws TPException;
	public JSONObject getValidTicket(int custId,long citationNumber) throws TPException;
	
	public ArrayList<Vendor> getVendors(int custId) throws TPException;
	public ArrayList<VendorItem> getVendorItems(int custId) throws TPException;
	public ArrayList<VendorService> getVendorServices(int custId) throws TPException;
	public ArrayList<VendorServiceRegistration> getVendorServiceRegistrations(int custId,int userId, int deviceId) throws TPException;
	public void updateUserServices(boolean fullSync) throws TPException;
	public boolean syncTicketData(ArrayList<SyncData> syncData, Context context) throws TPException;
	public ArrayList<Ticket> searchPlateHistories(String plate, String state) throws TPException;
	public ArrayList<Ticket> searchPlateHistoriesVin(String vin, String state) throws TPException;
	public ArrayList<Comment> getCommentsById(int Ids[]) throws TPException;
	public ArrayList<MaintenanceLog> getMaintenanceLogs() throws TPException;
	
	public VersionUpdate getVersionUpdates(int custId, String module) throws TPException;
	public RepeatOffender repeatOffenderFromService(int custId, String plate, int id, String stateCode) throws TPException;

	public RepeatOffender repeatOffenderFromService(RepeatOffendersFromService repeatOffendersFromService) throws TPException;

	ArrayList<RepeatOffender> getlastUpDateRepeatOffender(int custid, String timestamp) throws TPException;

	/**
	 * CODE BY SANJIV
	 * @param custId
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public EulaModel getEULAText(int custId, int moduleId) throws Exception;

	/**
	 * CODE BY SANJIV
	 * @param custId
	 * @param recId
	 * @param isAccpted
	 * @return
	 * @throws Exception
	 */
	public JSONObject updateEULAAcceptance(int userId,int recId,String isAccpted,int custId) throws Exception;
	public boolean getPlacard(String agencyCode, String user, String plate, String placard, String source) throws Exception;
	public boolean updateActivity(JSONArray activityReports) throws Exception;
	public boolean updateActivityPicture(JSONArray activityReports, ArrayList<String> image) throws Exception;
}
