package com.ticketpro.model;

import android.content.ContentValues;
import android.os.AsyncTask;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity(tableName = "vendor_services")
public class VendorService {
    @Ignore
    public static final String FIELD_TRACKER_TICKETPRO = "ticketpro";
    @Ignore
    public static final String MOBILE_NOW_PLATE_CHECK = "MobileNow_Plate_Check";
    @Ignore
    public static final String MOBILE_NOW_ZONE_CHECK = "MobileNow_Zone_Check";
    @Ignore
    public static final String MOBILE_NOW_SPACE_CHECK = "MobileNow_Space_Check";
    @Ignore
    public static final String PAYBYPHONE_PLATEINFO = "PayByPhone_PlateInfo";
    @Ignore
    public static final String PAYBYPHONE_ZONEINFO = "PayByPhone_Location";
    @Ignore
    public static final String PAYBYPHONE_SPACEINFO = "PayByPhone_SpaceInfo";
    @Ignore
    public static final String DIGITALPAYTECH_PLATEINFO = "DigitalPaytech_PlateInfo";
    @Ignore
    public static final String DIGITALPAYTECH_ZONELIST = "DigitalPaytech_ZoneList";
    @Ignore
    public static final String DIGITALPAYTECH_PAYSTATIONLIST = "DigitalPaytech_PaystationList";
    @Ignore
    public static final String DIGITALPAYTECH_ZONEINFO = "DigitalPaytech_ZoneInfo";
    @Ignore
    public static final String DIGITALPAYTECH_SPACEINFO = "DigitalPaytech_SpaceInfo";
    @Ignore
    public static final String IPS_PLATEINFO = "IPS_PlateInfo";
    @Ignore
    public static final String IPS_SPACEINFO = "IPS_SpaceInfo";
    @Ignore
    public static final String IPS_METERINFO = "IPS_MeterInfo";
    @Ignore
    public static final String IPS_MULTISPACE_GROUP = "IPS_MultiSpaceGroup";
    @Ignore
    public static final String IPS_PLATEBYSUBAREA = "IPS_PlateSubArea";
    @Ignore
    public static final String PASSPORT_PARKING_ZONELIST = "PassportParking_ZoneList";
    @Ignore
    public static final String PASSPORT_PARKING_ZONEINFO = "PassportParking_ZoneInfo";
    @Ignore
    public static final String PASSPORT_PARKING_PLATEINFO = "PassportParking_PlateInfo";
    public static final String PP2_PLATEINFO = "PP2_Plate";
    public static final String PP2_ZONE_LIST = "PP2_ZoneList";
    public static final String PP2_ZONE_LIST_INFO = "PP2_ZoneListInfo";
    @Ignore
    public static final String PARK_MOBILE_ZONELIST = "ParkMobile_ZoneList";
    @Ignore
    public static final String PARK_MOBILE_ZONEINFO = "ParkMobile_ZoneInfo";
    @Ignore
    public static final String PARK_MOBILE_PLATEINFO = "ParkMobile_PlateInfo";
    @Ignore
    public static final String PARK_MOBILE_SPACEINFO = "ParkMobile_SpaceInfo";
    @Ignore
    public static final String PROGRASSIVE_VALIDPERMIT = "Progressive_ValidPermit";
    @Ignore
    public static final String CS_LOOKUP = "CS_LOOKUP";
    @Ignore
    public static final String CALE_ZONE_LIST = "CaleZoneList";
    //Code by Sanjiv
    @Ignore
    public static final String CALE_ZONE_LIST_INFO = "CaleZoneListInfo";
    @Ignore
    public static final String CALE_PLATE_SEARCH = "CalePlateSearch";

    @Ignore
    public static final String CUBTRAC_PLATE_SEARCH = "Cubtrac_Plate";
    @Ignore
    public static final String OFFSTREET_PLATE_SEARCH = "OffStreet_plateInfo";
@Ignore
    public static final String CUBTRAC_ZONE_SEARCH = "Cubtrac_ZoneList";

    @Ignore
    public static final String CURBSENSE_LOGIN = "Curbsense_login";

    @Ignore
    public static final String CURBSENSE_ZONE = "Curbsense_zone";

    @Ignore
    public static final String CURBSENSE_PLATEINFO = "Curbsense_plateInfo";


    public static final String CALE2_PLATE_SEARCH = "Cale2PlateSearch";
    //Samtrans
    @Ignore
    public static final String SAMTRANS_TOKEN = "SamtransToken";
    @Ignore
    public static final String SAMTRANS_BASE_URL = "SamtransBaseUrl";
    @Ignore
    public static final String PARKEON = "Parkeon";

    @PrimaryKey
    @ColumnInfo(name = "service_id")
    @SerializedName("service_id")
    @Expose
    private int serviceId;
    @ColumnInfo(name = "vendor_id")
    @SerializedName("vendor_id")
    @Expose
    private int vendorId;
    @ColumnInfo(name = "service_name")
    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @ColumnInfo(name = "test_url")
    @SerializedName("test_url")
    @Expose
    private String testURL;
    @ColumnInfo(name = "prod_url")
    @SerializedName("prod_url")
    @Expose
    private String prodURL;
    @ColumnInfo(name = "params")
    @SerializedName("params")
    @Expose
    private String params;
    @ColumnInfo(name = "security_key")
    @SerializedName("security_key")
    @Expose
    private String securityKey;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public VendorService() {

    }

    public VendorService(JSONObject object) throws Exception {
        this.setVendorId(!object.isNull("vendor_id") ? object.getInt("vendor_id") : 0);
        this.setServiceId(!object.isNull("service_id") ? object.getInt("service_id") : 0);
        this.setServiceName(object.getString("service_name"));
        this.setTestURL(object.getString("test_url"));
        this.setProdURL(object.getString("prod_url"));
        this.setParams(object.getString("params"));
        this.setSecurityKey(object.getString("security_key"));
    }

    public static ArrayList<VendorService> getServices() throws Exception {
        ArrayList<VendorService> list;
        list = (ArrayList<VendorService>) ParkingDatabase.getInstance(TPApplication.getInstance()).vendorServicesDao().getServices();
        return list;
    }

    public static VendorService getServiceByName(String serviceName) {
        List<VendorService> object;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).vendorServicesDao().getServiceByName(serviceName);
        return object.get(object.size() - 1);
    }

    public static VendorServiceConfig getServiceConfig(String serviceName, int deviceId) {
        return VendorService.getServiceConfig(serviceName, deviceId, "&");
    }

    public static VendorServiceConfig getServiceConfigT2(String serviceName, int deviceId) {
        return VendorService.getServiceConfigT2(serviceName, deviceId, "&");
    }

    public static VendorServiceConfig getServiceConfig(String serviceName, int deviceId, String paramChar) {
        VendorServiceConfig config = null;
        VendorService service;
        try {
            service = VendorService.getServiceByName(serviceName);

            if (service != null) {
                config = new VendorServiceConfig();
                config.setVendorId(service.getVendorId());

                VendorServiceRegistration registration = VendorServiceRegistration.getServiceRegistrationByServiceId(service.getServiceId(), deviceId);

                if (registration == null) {
                    return null;
                }

                if (paramChar == null || paramChar.isEmpty()) {
                    paramChar = "&";
                }

                if (isIPSRequest(serviceName) || isPassPortRequest(serviceName)) {
                    if (registration.getIsActive().equalsIgnoreCase("N")) {
                        return null;
                    }
                    //service.setParams(registration.getParamMappings());
                }
                config.setParamsMap(new HashMap<>());

                String serviceMode = registration.getServiceMode();
                if (serviceMode.equalsIgnoreCase("Prod")) {
                    config.setServiceURL(service.getProdURL());
                } else {
                    config.setServiceURL(service.getTestURL());
                }

                config.setRequestMode(serviceMode);
                StringBuilder params = new StringBuilder();

                String serviceParams = service.getParams();
                if (serviceParams != null && !serviceParams.isEmpty()) {
                    if (!serviceParams.equalsIgnoreCase("null"))
                        params.append(serviceParams.replaceAll(",", paramChar));
                }

                String securityKey = service.getSecurityKey();
                if (securityKey != null && !securityKey.isEmpty() && !securityKey.equalsIgnoreCase("null")) {
                    //Parse HTTP Authentication USERNAME and PASSWORD
                    if (securityKey.contains("username")) {
                        String[] authTokens = securityKey.split(",");
                        for (String token : authTokens) {
                            String[] paramTokens = token.split("=");
                            if (paramTokens.length == 2) {
                                if (paramTokens[0].equalsIgnoreCase("username")) {
                                    config.setUsername(paramTokens[1]);
                                    config.getParamsMap().put("username", paramTokens[1]);
                                } else if (paramTokens[0].equalsIgnoreCase("password")) {
                                    config.setPassword(paramTokens[1]);
                                    config.getParamsMap().put("password", paramTokens[1]);
                                } else {
                                    if (params.length() > 0) {
                                        params.append(paramChar);
                                    }
                                    params.append(token);
                                }
                            }
                        }
                    } else {
                        if (params.length() > 0) {
                            params.append(paramChar);
                        }
                        params.append(securityKey.replaceAll(",", paramChar));
                    }
                }

                String userParams = registration.getParamMappings();
                if (userParams != null && !userParams.isEmpty()) {
                    if (userParams.contains("username")) {
                        String[] authTokens = userParams.split(",");
                        for (String token : authTokens) {
                            String[] paramTokens = token.split("=");
                            if (paramTokens.length == 2) {
                                if (paramTokens[0].equalsIgnoreCase("username")) {
                                    config.setUsername(paramTokens[1]);
                                    config.getParamsMap().put("username", paramTokens[1]);
                                } else if (paramTokens[0].equalsIgnoreCase("password")) {
                                    config.setPassword(paramTokens[1]);
                                    config.getParamsMap().put("password", paramTokens[1]);
                                } else {
                                    if (params.length() > 0) {
                                        params.append(paramChar);
                                    }
                                    params.append(token);
                                }
                            }
                        }
                        if (authTokens.length == 3) {
                            params.append(authTokens[2]);
                            //config.setParams(authTokens[2]);
                        }
                    } else {
                        if (params.length() > 0) {
                            params.append(paramChar);
                        }

                        params.append(userParams.replaceAll(",", paramChar));
                    }
                }

                config.setParams(params.toString().replaceAll(";", ","));

                String[] paramTokens = config.getParams().split(paramChar);
                for (String paramToken : paramTokens) {
                    String[] tokens;
                    /*if (isIPSRequest(serviceName) || isPassPortRequest(serviceName)) {

                    } else {*/
                    tokens = paramToken.split("=", 2);
                    //}
                    if (tokens.length == 2) {
                        config.getParamsMap().put(tokens[0], tokens[1]);
                    } else if (tokens.length == 1) {
                        config.getParamsMap().put(tokens[0], "");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return config;
    }

    public static VendorServiceConfig getServiceConfigT2(String serviceName, int deviceId, String paramChar) {
        VendorServiceConfig config = null;
        VendorService service;
        try {
            service = VendorService.getServiceByName(serviceName);

            if (service != null) {
                config = new VendorServiceConfig();
                config.setVendorId(service.getVendorId());

                VendorServiceRegistration registration = VendorServiceRegistration.getServiceRegistrationByServiceId(service.getServiceId(), deviceId);

                if (registration == null) {
                    return null;
                }

                if (paramChar == null || paramChar.isEmpty()) {
                    paramChar = "&";
                }

                if (isIPSRequest(serviceName) || isPassPortRequest(serviceName)) {
                    if (registration.getIsActive().equalsIgnoreCase("N")) {
                        return null;
                    }
                    //service.setParams(registration.getParamMappings());
                }
                config.setParamsMap(new HashMap<>());

                String serviceMode = registration.getServiceMode();
                if (serviceMode.equalsIgnoreCase("Prod")) {
                    config.setServiceURL(service.getProdURL());
                } else {
                    config.setServiceURL(service.getTestURL());
                }

                config.setRequestMode(serviceMode);
                StringBuilder params = new StringBuilder();

                String serviceParams = service.getParams();
                if (serviceParams != null && !serviceParams.isEmpty()) {
                    if (!serviceParams.equalsIgnoreCase("null"))
                        params.append(serviceParams.replaceAll(",", paramChar));
                }

                String securityKey = service.getSecurityKey();
                if (securityKey != null && !securityKey.isEmpty() && !securityKey.equalsIgnoreCase("null")) {
                    //Parse HTTP Authentication USERNAME and PASSWORD
                    if (securityKey.contains("username")) {
                        String[] authTokens = securityKey.split(",");
                        for (String token : authTokens) {
                            String[] paramTokens = token.split("=");
                            if (paramTokens.length == 2) {
                                if (paramTokens[0].equalsIgnoreCase("username")) {
                                    config.setUsername(paramTokens[1]);
                                    config.getParamsMap().put("username", paramTokens[1]);
                                } else if (paramTokens[0].equalsIgnoreCase("password")) {
                                    config.setPassword(paramTokens[1]);
                                    config.getParamsMap().put("password", paramTokens[1]);
                                } else {
                                    if (params.length() > 0) {
                                        params.append(paramChar);
                                    }
                                    params.append(token);
                                }
                            }
                        }
                    } else {
                        if (params.length() > 0) {
                            params.append(paramChar);
                        }
                        params.append(securityKey.replaceAll(",", paramChar));
                    }
                }

                String userParams = registration.getParamMappings();
                if (userParams != null && !userParams.isEmpty()) {
                    if (userParams.contains("username")) {
                        String[] authTokens = userParams.split(",");
                        for (String token : authTokens) {
                            String[] paramTokens = token.split("=");
                            if (paramTokens.length == 2) {
                                if (paramTokens[0].equalsIgnoreCase("username")) {
                                    config.setUsername(paramTokens[1]);
                                    config.getParamsMap().put("username", paramTokens[1]);
                                } else if (paramTokens[0].equalsIgnoreCase("password")) {
                                    config.setPassword(paramTokens[1]);
                                    config.getParamsMap().put("password", paramTokens[1]);
                                } else {
                                    if (params.length() > 0) {
                                        params.append(paramChar);
                                    }
                                    params.append(token);
                                }
                            }
                        }
                        if (authTokens.length == 3) {
                            //params.append(authTokens[2]);
                            //config.setParams(authTokens[2]);
                        }
                    } else {
                        if (params.length() > 0) {
                            params.append(paramChar);
                        }

                        params.append(userParams.replaceAll(",", paramChar));
                    }
                }

                config.setParams(params.toString().replaceAll(";", ","));

                String[] paramTokens = config.getParams().split(paramChar);
                for (String paramToken : paramTokens) {
                    String[] tokens;
                    /*if (isIPSRequest(serviceName) || isPassPortRequest(serviceName)) {

                    } else {*/
                    tokens = paramToken.split("=", 2);
                    //}
                    if (tokens.length == 2) {
                        config.getParamsMap().put(tokens[0], tokens[1]);
                    } else if (tokens.length == 1) {
                        config.getParamsMap().put(tokens[0], "");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return config;
    }


    //Code by Sanjiv for cale service
    public static VendorServiceConfig getServiceConfigCale(String serviceName, int deviceId, String paramChar) {
        VendorServiceConfig config = null;
        VendorService service;
        try {
            service = VendorService.getServiceByName(serviceName);

            if (service != null) {
                config = new VendorServiceConfig();
                config.setVendorId(service.getVendorId());

                VendorServiceRegistration registration = VendorServiceRegistration.getServiceRegistrationByServiceId(service.getServiceId(), deviceId);

                if (registration == null) {
                    return null;
                }

                if (paramChar == null || paramChar.isEmpty()) {
                    paramChar = "&";
                }

                if (isIPSRequest(serviceName) || isPassPortRequest(serviceName)) {
                    if (registration.getIsActive().equalsIgnoreCase("N")) {
                        return null;
                    }
                    //service.setParams(registration.getParamMappings());
                }
                config.setParamsMap(new HashMap<>());

                String serviceMode = registration.getServiceMode();
                if (serviceMode.equalsIgnoreCase("Prod")) {
                    config.setServiceURL(service.getProdURL());
                } else {
                    config.setServiceURL(service.getTestURL());
                }

                config.setRequestMode(serviceMode);
                StringBuilder params = new StringBuilder();

                String serviceParams = service.getParams();
                if (serviceParams != null && !serviceParams.isEmpty()) {
                    if (!serviceParams.equalsIgnoreCase("null"))
                        params.append(serviceParams.replaceAll(",", paramChar));
                }

                String securityKey = service.getSecurityKey();
                if (securityKey != null && !securityKey.isEmpty() && !securityKey.equalsIgnoreCase("null")) {
                    //Parse HTTP Authentication USERNAME and PASSWORD
                    if (securityKey.contains("username")) {
                        String[] authTokens = securityKey.split(",");
                        for (String token : authTokens) {
                            String[] paramTokens = token.split("=");
                            if (paramTokens.length == 2) {
                                if (paramTokens[0].equalsIgnoreCase("username")) {
                                    config.setUsername(paramTokens[1]);
                                    config.getParamsMap().put("username", paramTokens[1]);
                                } else if (paramTokens[0].equalsIgnoreCase("password")) {
                                    config.setPassword(paramTokens[1]);
                                    config.getParamsMap().put("password", paramTokens[1]);
                                } else {
                                    if (params.length() > 0) {
                                        params.append(paramChar);
                                    }

                                    params.append(token);
                                }
                            }
                        }
                    } else {
                        if (params.length() > 0) {
                            params.append(paramChar);
                        }

                        params.append(securityKey.replaceAll(",", paramChar));
                    }
                }

                String userParams = registration.getParamMappings();
                if (userParams != null && !userParams.isEmpty()) {
                    if (userParams.contains("username")) {
                        String[] authTokens = userParams.split(",");
                        for (String token : authTokens) {
                            String[] paramTokens = token.split("=");
                            if (paramTokens.length == 2) {
                                if (paramTokens[0].equalsIgnoreCase("username")) {
                                    config.setUsername(paramTokens[1]);
                                    config.getParamsMap().put("username", paramTokens[1]);
                                } else if (paramTokens[0].equalsIgnoreCase("password")) {
                                    config.setPassword(paramTokens[1]);
                                    config.getParamsMap().put("password", paramTokens[1]);
                                } else {
                                    if (params.length() > 0) {
                                        params.append(paramChar);
                                    }

                                    params.append(token);
                                }
                            }
                        }
                    } else {
                        if (params.length() > 0) {
                            params.append(paramChar);
                        }

                        params.append(userParams.replaceAll(",", paramChar));
                    }
                }

                config.setParams(params.toString().replaceAll(";", ";"));

                String[] paramTokens = config.getParams().split(paramChar);
                for (String paramToken : paramTokens) {
                    String[] tokens;
                    if (isIPSRequest(serviceName) || isPassPortRequest(serviceName)) {
                        tokens = paramToken.split("=", 2);
                    } else {
                        tokens = paramToken.split("=");
                    }
                    if (tokens.length == 2) {
                        config.getParamsMap().put(tokens[0], tokens[1]);
                    } else if (tokens.length == 1) {
                        config.getParamsMap().put(tokens[0], "");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return config;
    }

    //
    //Code by Sanjiv for cale service
    public static ArrayList<VendorServiceConfig> getServiceConfigCale1(String serviceName, int deviceId, String paramChar) {
        VendorServiceConfig config = null;
        VendorService service;
        ArrayList<VendorServiceConfig> configs = new ArrayList<>();
        try {
            service = VendorService.getServiceByName(serviceName);

            if (service != null) {

                //VendorServiceRegistration registration = VendorServiceRegistration.getServiceRegistrationByServiceId(service.getServiceId(), deviceId);

                final List<VendorServiceRegistration> serviceRegistrationByServiceId1 = VendorServiceRegistration
                        .getServiceRegistrationByServiceId1(service.getServiceId(), deviceId);
                for (int i = 0; i < serviceRegistrationByServiceId1.size(); i++) {
                    VendorServiceRegistration registration = serviceRegistrationByServiceId1.get(i);
                    config = new VendorServiceConfig();
                    config.setVendorId(service.getVendorId());

                    if (registration == null) {
                        return null;
                    }

                    if (paramChar == null || paramChar.isEmpty()) {
                        paramChar = "&";
                    }

                    if (isIPSRequest(serviceName) || isPassPortRequest(serviceName)) {
                        if (registration.getIsActive().equalsIgnoreCase("N")) {
                            return null;
                        }
                        //service.setParams(registration.getParamMappings());
                    }
                    config.setParamsMap(new HashMap<>());

                    String serviceMode = registration.getServiceMode();
                    if (serviceMode.equalsIgnoreCase("Prod")) {
                        config.setServiceURL(service.getProdURL());
                    } else {
                        config.setServiceURL(service.getTestURL());
                    }

                    config.setRequestMode(serviceMode);
                    StringBuilder params = new StringBuilder();

                    String serviceParams = service.getParams();
                    if (serviceParams != null && !serviceParams.isEmpty()) {
                        if (!serviceParams.equalsIgnoreCase("null"))
                            params.append(serviceParams.replaceAll(",", paramChar));
                    }

                    String securityKey = service.getSecurityKey();
                    if (securityKey != null && !securityKey.isEmpty() && !securityKey.equalsIgnoreCase("null")) {
                        //Parse HTTP Authentication USERNAME and PASSWORD
                        if (securityKey.contains("username")) {
                            String[] authTokens = securityKey.split(",");
                            for (String token : authTokens) {
                                String[] paramTokens = token.split("=");
                                if (paramTokens.length == 2) {
                                    if (paramTokens[0].equalsIgnoreCase("username")) {
                                        config.setUsername(paramTokens[1]);
                                        config.getParamsMap().put("username", paramTokens[1]);
                                    } else if (paramTokens[0].equalsIgnoreCase("password")) {
                                        config.setPassword(paramTokens[1]);
                                        config.getParamsMap().put("password", paramTokens[1]);
                                    } else {
                                        if (params.length() > 0) {
                                            params.append(paramChar);
                                        }

                                        params.append(token);
                                    }
                                }
                            }
                        } else {
                            if (params.length() > 0) {
                                params.append(paramChar);
                            }

                            params.append(securityKey.replaceAll(",", paramChar));
                        }
                    }

                    String userParams = registration.getParamMappings();
                    if (userParams != null && !userParams.isEmpty()) {
                        if (userParams.contains("username")) {
                            String[] authTokens = userParams.split(",");
                            for (String token : authTokens) {
                                String[] paramTokens = token.split("=");
                                if (paramTokens.length == 2) {
                                    if (paramTokens[0].equalsIgnoreCase("username")) {
                                        config.setUsername(paramTokens[1]);
                                        config.getParamsMap().put("username", paramTokens[1]);
                                    } else if (paramTokens[0].equalsIgnoreCase("password")) {
                                        config.setPassword(paramTokens[1]);
                                        config.getParamsMap().put("password", paramTokens[1]);
                                    } else {
                                        if (params.length() > 0) {
                                            params.append(paramChar);
                                        }

                                        params.append(token);
                                    }
                                }
                            }
                        } else {
                            if (params.length() > 0) {
                                params.append(paramChar);
                            }

                            params.append(userParams.replaceAll(",", paramChar));
                        }
                    }

                    config.setParams(params.toString().replaceAll(";", ";"));

                    String[] paramTokens = config.getParams().split(paramChar);
                    for (String paramToken : paramTokens) {
                        String[] tokens;
                        if (isIPSRequest(serviceName) || isPassPortRequest(serviceName)) {
                            tokens = paramToken.split("=", 2);
                        } else {
                            tokens = paramToken.split("=");
                        }
                        if (tokens.length == 2) {
                            config.getParamsMap().put(tokens[0], tokens[1]);
                        } else if (tokens.length == 1) {
                            config.getParamsMap().put(tokens[0], "");
                        }
                    }
                    configs.add(config);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return configs;
    }


    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).vendorServicesDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).vendorServicesDao().removeById(id);
    }

    public static boolean isIPSRequest(String serviceName) {
        return serviceName.equalsIgnoreCase(VendorService.IPS_SPACEINFO) || serviceName.equalsIgnoreCase(VendorService.IPS_METERINFO) || serviceName.equalsIgnoreCase(VendorService.IPS_PLATEINFO);
    }

    public static boolean isPassPortRequest(String serviceName) {
        return serviceName.equalsIgnoreCase(VendorService.PASSPORT_PARKING_ZONELIST) || serviceName.equalsIgnoreCase(VendorService.PASSPORT_PARKING_ZONEINFO) || serviceName.equalsIgnoreCase(VendorService.PASSPORT_PARKING_PLATEINFO);
    }

    public int getSyncDataId() {
        return syncDataId;
    }

    public void setSyncDataId(int syncDataId) {
        this.syncDataId = syncDataId;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("vendor_id", this.vendorId);
        values.put("service_id", this.serviceId);
        values.put("service_name", this.serviceName);
        values.put("test_url", this.testURL);
        values.put("prod_url", this.prodURL);
        values.put("params", this.params);
        values.put("security_key", this.securityKey);

        return values;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTestURL() {
        return testURL;
    }

    public void setTestURL(String testURL) {
        this.testURL = testURL;
    }

    public String getProdURL() {
        return prodURL;
    }

    public void setProdURL(String prodURL) {
        this.prodURL = prodURL;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public static void insertVendorService(VendorService VendorService) {
        new VendorService.InsertVendorService(VendorService).execute();
    }

    private static class InsertVendorService extends AsyncTask<Void, Void, Void> {
        private final VendorService VendorService;

        public InsertVendorService(VendorService VendorService) {
            this.VendorService = VendorService;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).vendorServicesDao().insertVendorService(VendorService);
            return null;
        }
    }
}
