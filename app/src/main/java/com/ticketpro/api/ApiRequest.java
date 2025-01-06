package com.ticketpro.api;

import com.ticketpro.model.ActivityRequest_Rpc;
import com.ticketpro.model.ArrayOfEnforcementZone;
import com.ticketpro.model.ArrayOfPlaveSearch;
import com.ticketpro.model.ArrayOfValidParkingData;
import com.ticketpro.model.BodyResponse;
import com.ticketpro.model.CallInListResponse;
import com.ticketpro.model.ClientInfoResponse;
import com.ticketpro.model.ColorResponse;
import com.ticketpro.model.CommentGroupCommentResponse;
import com.ticketpro.model.CommentGroupResponse;
import com.ticketpro.model.CommentResponse;
import com.ticketpro.model.ContactResponse;
import com.ticketpro.model.CurveSenseTokenRequest;
import com.ticketpro.model.CurveSenseZoneItemSelectedList;
import com.ticketpro.model.CurveSenseZoneList;
import com.ticketpro.model.CurvesenseLoginTokenResponse;
import com.ticketpro.model.CustomerModuleResponse;
import com.ticketpro.model.CustomerResponse;
import com.ticketpro.model.DeviceGroupResponse;
import com.ticketpro.model.DeviceInfoResponse;
import com.ticketpro.model.DirectionResponse;
import com.ticketpro.model.DurationResponse;
import com.ticketpro.model.DutyResponse;
import com.ticketpro.model.EulaAcceptanceRequest_Rpc;
import com.ticketpro.model.EulaAcceptanceResult;
import com.ticketpro.model.EulaResult;
import com.ticketpro.model.EulaReuest_Rpc;
import com.ticketpro.model.FeatureResponse;
import com.ticketpro.model.FirebaseModel;
import com.ticketpro.model.FirebaseResponse;
import com.ticketpro.model.GPSLocationResponse;

import com.ticketpro.model.GenetecPatrollerActivitiesResponse;
import com.ticketpro.model.GeocodeLocation;
import com.ticketpro.model.HotList_Rpc;
import com.ticketpro.model.HotlistResponse;
import com.ticketpro.model.LocationGroupLocationResponse;
import com.ticketpro.model.LocationGroupResponse;
import com.ticketpro.model.LocationResponse;
import com.ticketpro.model.LotwiseApi;
import com.ticketpro.model.LprBodyResponse;
import com.ticketpro.model.MakeAndModelResponse;
import com.ticketpro.model.MessageResponse;
import com.ticketpro.model.MeterReponse;
import com.ticketpro.model.MeterRequest_Rpc;
import com.ticketpro.model.MeterResponse;
import com.ticketpro.model.ModuleResponse;
//import com.ticketpro.model.PatrollersResponse;
import com.ticketpro.model.PatrollersResponse;
import com.ticketpro.model.PermitRequest_Rpc;
import com.ticketpro.model.PermitResponse;
import com.ticketpro.model.PrintMacroResponse;
import com.ticketpro.model.PrintTemplateResponse;
import com.ticketpro.model.RepeatOffenderResponse;
import com.ticketpro.model.RepeatOffender_Rpc;
import com.ticketpro.model.RepeatOffendersLive_Rpc;
import com.ticketpro.model.RequestPOJO;
import com.ticketpro.model.RequestPOJODeviceFeatures;
import com.ticketpro.model.SearchVinResponse;
import com.ticketpro.model.SpecialActivitiesLocationResponse;
import com.ticketpro.model.SpecialActivityDispositionResponse;
import com.ticketpro.model.SpecialActivityReportResponse;
import com.ticketpro.model.SpecialActivityReport_Rpc;
import com.ticketpro.model.SpecialActivityResponse;
import com.ticketpro.model.StateResponse;
import com.ticketpro.model.StreetPrefixResponse;
import com.ticketpro.model.StreetSuffixResponse;
import com.ticketpro.model.TicketComment_Rpc;
import com.ticketpro.model.TicketHistoryResponse;
import com.ticketpro.model.TicketPicture_Rpc;
import com.ticketpro.model.TicketResponse;
import com.ticketpro.model.TicketViolationResponse;
import com.ticketpro.model.TicketViolation_Rpc;
import com.ticketpro.model.TicketsResponse;
import com.ticketpro.model.TokenBody;
import com.ticketpro.model.TokenResponse;
import com.ticketpro.model.UserResponse;
import com.ticketpro.model.UserSettingResponse;
import com.ticketpro.model.ValidResult;
import com.ticketpro.model.ValidTicketRequest_Rpc;
import com.ticketpro.model.VendorItemResponse;
import com.ticketpro.model.VendorResponse;
import com.ticketpro.model.VendorServiceRegistrationResponse;
import com.ticketpro.model.VendorServiceResponse;
import com.ticketpro.model.VerifyAndSyncTicketsResponse;
import com.ticketpro.model.VersionUpdateResponse;
import com.ticketpro.model.ViolationGroupResponse;
import com.ticketpro.model.ViolationGroupViolationResponse;
import com.ticketpro.model.ViolationResponse;
import com.ticketpro.model.VoidTicketReasonResponse;
import com.ticketpro.model.ZonePoleModel;
import com.ticketpro.model.ZonePoleResponse;
import com.ticketpro.model.ZoneResponse;
import com.ticketpro.model.chalk_response.ChalkResponse;
import com.ticketpro.model.devicefeature.ResponseResult;
import com.ticketpro.model.gps.GpsResponse;
import com.ticketpro.model.gps.Location_Json_rpc;
import com.ticketpro.syncbackup.synmodels.CSVinfo_Json_rpc;
import com.ticketpro.syncbackup.synmodels.Dbinfo_Json_rpc;
import com.ticketpro.syncbackup.synmodels.SyncBackup_Json_rpc;
import com.ticketpro.syncbackup.synmodels.UploadDebugResponse;
import com.ticketpro.vendors.ParkMobileSpace;
import com.ticketpro.vendors.ParkMobileZoneList;
import com.ticketpro.vendors.cubtrack.cbt_model.CubTracZone;
import com.ticketpro.vendors.cubtrack.cbt_model.CubtracRequest;
import com.ticketpro.vendors.cubtrack.cbt_model.CubtracResponse;
import com.ticketpro.vendors.offstreet.OffStreetList;
import com.ticketpro.vendors.offstreet.OffstreetReqest;
import com.ticketpro.vendors.passport2_model.PP2TokenResponse;
import com.ticketpro.vendors.passport2_model.Passport2Array;
import com.ticketpro.vendors.passport2_model.PlateResponse;
import com.ticketpro.vendors.passport2_model.TokenRequest;
import com.ticketpro.vendors.passport2_model.zoneInfo.PP2ZoneInfo;
import com.ticketpro.vendors.passport2_model.zonelist.PP2ZoneList;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by SANJIV on 14-06-2020.
 */
public interface ApiRequest {




    @POST("service/genericv1")
    Call<GpsResponse> getGpsLocation(@Body Location_Json_rpc jbj);


    @GET

    @Multipart
    @POST("/uploadfile")
    Observable<ResponseBody> updateProfile(@Part("custId") RequestBody id,
                                           @Part("full_name") RequestBody fullName,
                                           @Part MultipartBody.Part image);


    @POST()
    Call<List<CubtracResponse>> cubtracPlatelookup(@Url String url, @Header("Authorization") String authToken, @Body CubtracRequest user);

    @POST()
    Call<OffStreetList> offstreetPlatelookup(@Url String url, @Header("Authorization") String authToken, @Body OffstreetReqest reqest);


    @GET()
    Call<List<CubTracZone>> cubtracGetZone(@Url String url, @Header("Authorization") String authToken);


    /**

     * @return
     */

    @POST("/v3/shared/access-tokens")
    Call<PP2TokenResponse> getTokenForPassportParking2(@Body TokenRequest tokenBody);

    @POST()
    Call<CurvesenseLoginTokenResponse> getTokenForCurveSense(@Url String url,@Body CurveSenseTokenRequest curveSenseTokenRequest);

    @GET("Zone/zoneList")
    Call<List<ZonePoleModel>> getZonePoleList();

    @POST("Zone/SaveZoneActivity")  // Replace with your actual API endpoint
    Call<ZonePoleModel> createZone(@Body RequestBody requestBody);



    @GET
    Call<CurveSenseZoneItemSelectedList> getVioEvents(@Header("Authorization") String authToken, @Url String url);

    @GET
    Call<String> getVioEventsImage(@Header("Authorization") String authToken, @Url String url);


    @GET
    Call<Passport2Array> getPP2Space(@Url String url);

    //PP2
    @GET
    Call<PlateResponse> getPP2PlateInfo(@Url String url);

    @GET
    Call<PP2ZoneList> getPP2ZoneList(@Url String url);

    @GET
    Call<List<CurveSenseZoneList>> getCurveSenseZoneList(@Url String url);

    @GET
    Call<PP2ZoneInfo> getPP2ZoneListInfo(@Url String url);


    @GET
    Call<ParkMobileZoneList> getZoneParkMobile(@Url String url);

    /**
     * @param url
     * @return
     */
    @GET
    Call<ParkMobileSpace> getParkMobileSingleSpace(@Url String url);

    @GET
    Call<ArrayOfEnforcementZone> getZone(@Url String url);

    @POST
    Call<ResponseBody> getPhotoAlpr(@Url String url, @Body String image);

    @GET
    Call<ArrayOfPlaveSearch> _serchPlate(@Url String url);

    @GET
    Call<ArrayOfValidParkingData> _serchPlate2(@Url String url);

    //CALE
    @GET
    Observable<ArrayOfValidParkingData> _searchPlateCALEWithoutZero(@Url String url);

    @GET
    Observable<ArrayOfValidParkingData> _searchPlateCALEWithZero(@Url String url);

    @GET
    Call<ArrayOfValidParkingData> _validateParking(@Url String url);

    @GET("/token")
    Call<ResponseBody> getAccessToken();

    @GET
    Call<LotwiseApi> _getDataBylotname(@Url String url);

    @GET("/parkingsummary/{Lotname}")
    Call<LotwiseApi> getDataBylotname(@Header("Authorization") String token, @Path("Lotname") String LotName);

    @GET("parkingsummary/{purchasedate}/{expirydate}")
    Call<LotwiseApi> getdatabydate(@Header("Authorization") String token, @Path("purchasedate") String purchasedate, @Path("expirydate") String expirydate);

    @GET("parkingsummary/{Lotname}/{purchasedate}/{expirydate}")
    Call<LotwiseApi> getdata_by_date_nd_lot(@Header("Authorization") String token, @Path("Lotname") String LotName, @Path("purchasedate") String purchasedate, @Path("expirydate") String expirydate);

    @POST("Tracking/AddDeviceInfo/")
    Call<FirebaseResponse> postDatatoFBDB(@Body FirebaseModel firebaseModel);

    @GET("json")
    Call<GeocodeLocation> getAddressfromLatLng(@Query("latlng") String latlng, @Query("key") String key);

    @POST("Authentication/GetToken/")
    Call<TokenResponse> getToken(@Body TokenBody tokenBody);

    @POST("service/genericv1")
    Observable<CustomerResponse> getCustomers(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<ClientInfoResponse> getClientInfo(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<DeviceInfoResponse> getDeviceInfo(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Call<UserResponse> getUsers(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Call<UserResponse> getUsers1(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Call<GenetecPatrollerActivitiesResponse> updatePatrollerActivities(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Call<ResponseBody> updateGCMRegistrationID(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<UserSettingResponse> getUserSettings(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<MessageResponse> getMessages(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<BodyResponse> getBodies(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Call<PatrollersResponse> getPatrollersId(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<LprBodyResponse> getLPRBodies(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<ContactResponse> getContacts(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<ColorResponse> getColors(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<CommentResponse> getComments(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<DirectionResponse> getDirections(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<DurationResponse> getDurations(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<DutyResponse> getDuties(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<FeatureResponse> getFeatures(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<GPSLocationResponse> getGPSLocations(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<SpecialActivitiesLocationResponse> getSpecialActivitiesLocation(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<HotlistResponse> getHotlist(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<LocationResponse> getLocations(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<RepeatOffenderResponse> getRepeatOffenders(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<MakeAndModelResponse> getMakesAndModels(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<MeterResponse> getMeters(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<PermitResponse> getPermits(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<StateResponse> getStates(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<StreetPrefixResponse> getStreetPrefixes(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<StreetSuffixResponse> getStreetSuffixes(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<ViolationResponse> getViolations(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<ZoneResponse> getZones(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<DeviceGroupResponse> getDeviceGroup(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<VoidTicketReasonResponse> getVoidTicketReasons(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<SpecialActivityResponse> getSpecialActivities(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<SpecialActivityDispositionResponse> getSpecialActivityDispositions(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<PrintMacroResponse> getPrintMacros(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<PrintTemplateResponse> getPrintTemplates(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<CallInListResponse> getCallInList(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<LocationGroupResponse> getLocationGroups(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<LocationGroupLocationResponse> getLocationGroupLocations(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<CommentGroupResponse> getCommentGroups(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<CommentGroupCommentResponse> getCommentGroupComments(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<ViolationGroupResponse> getViolationGroups(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<ViolationGroupViolationResponse> getViolationGroupViolations(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<VendorResponse> getVendors(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<VendorServiceResponse> getVendorServices(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<VendorItemResponse> getVendorItems(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<VendorServiceRegistrationResponse> getVendorServiceRegistrations(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<ModuleResponse> getModules(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Observable<CustomerModuleResponse> getCustomerModules(@Body RequestPOJO jbj);

    @POST("service/genericv1")
    Call<TicketResponse> syncTickets(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<ResponseBody> syncDevices(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<ResponseBody> sendEmail(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<ResponseBody> updateChalkStatus(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<ResponseBody> sendErrorLogs(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<RepeatOffenderResponse> lastUpDateRepeatOffenders(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<RepeatOffenderResponse> getlastRepeatOffenderService(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<ChalkResponse> syncChalks(@Body RequestPOJO requestPOJO);

    @POST("service/genericv1")
    Call<TicketsResponse> getPlateInfo(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<HotlistResponse> searchHotlist(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<PermitResponse> searchAllPermitByPlate(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<VersionUpdateResponse> getVersionUpdates(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<VerifyAndSyncTicketsResponse> verifyAndSyncTickets(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<ResponseResult> saveDeviceFeatures1(@Body RequestPOJODeviceFeatures requestPOJO);

    @POST("service/genericv1")
    Call<ResponseBody> updateMobileNowLog(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<TicketResponse> updatedeviceid(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<ResponseBody> updateDutyReport(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<EulaResult> getEula(@Body EulaReuest_Rpc pojo);

    @POST("service/genericv1")
    Call<EulaAcceptanceResult> getEulaAcceptance(@Body EulaAcceptanceRequest_Rpc pojo);

    @POST("service/genericv1")
    Call<ValidResult> getValidTicket(@Body ValidTicketRequest_Rpc pojo);


    // This code is added by mohit 27/02/2023

    @POST("service/genericv1")
    Call<ResponseBody> updateRepeatOffendersCount(@Body RepeatOffender_Rpc pojo);

    @POST("service/genericv1")
    Call<RepeatOffenderResponse> getRepeatOffendersLive(@Body RepeatOffendersLive_Rpc pojo);

    @POST("service/genericv1")
    Call<ResponseBody> deleteTicketComments(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<ResponseBody> deleteTicketPicture(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<TicketHistoryResponse> getTickeHistory(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<ResponseBody> updateTicketComments(@Body TicketComment_Rpc pojo);


    @POST("service/genericv1")
    Call<ResponseBody> GetLicenseDetail(@Body RequestPOJO pojo);

    @POST("service/genericv1")
    Call<ResponseBody> newHotlist(@Body HotList_Rpc pojo);

    @POST("service/genericv1")
    Call<ResponseBody> updateSpecialActivityReports(@Body ActivityRequest_Rpc pojo);

    @POST("service/genericv1")
    Call<ResponseBody> updateSpecialActivityPictures(@Body ActivityRequest_Rpc pojo);

    @POST("service/genericv1")
    Call<SpecialActivityReportResponse> getSpecialActivityReports(@Body SpecialActivityReport_Rpc pojo);

    @POST("service/genericv1")
    Call<ResponseBody> updateDutyReports(@Body ActivityRequest_Rpc pojo);

    @POST("service/genericv1")
    Call<ResponseBody> updateHotListReports(@Body ActivityRequest_Rpc pojo);

    @POST("service/genericv1")
    Call<MeterReponse> searchMeters(@Body MeterRequest_Rpc pojo);

    @POST("service/genericv1")
    Call<PermitResponse> searchPermits(@Body PermitRequest_Rpc pojo);

    @POST("service/genericv1")
    Call<PermitResponse> searchPermitVins(@Body PermitRequest_Rpc pojo);

    @POST("service/genericv1")
    Call<SearchVinResponse> searchVins(@Body PermitRequest_Rpc pojo);

    @POST("service/genericv1")
    Call<ResponseBody> updateTicketPicture(@Body TicketPicture_Rpc pojo);

    @POST("service/genericv1")
    Call<TicketViolationResponse> getTicketViolations(@Body TicketViolation_Rpc pojo);

    @POST("service/genericv1")
    Call<UploadDebugResponse> uploadDebugLog(@Body SyncBackup_Json_rpc rpc);

    @POST("service/genericv1")
    Call<UploadDebugResponse> uploadDbBackUp(@Body Dbinfo_Json_rpc rpc);

    @POST("service/genericv1")
    Call<UploadDebugResponse> uploadCSVBackUp(@Body CSVinfo_Json_rpc rpc);

}

