package com.ticketpro.vendors.dpt.PaystationInfoService;

import java.util.Hashtable;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import com.ticketpro.vendors.dpt.PaystationInfoService.WS_Enums.ByEventType;
import com.ticketpro.vendors.dpt.PaystationInfoService.WS_Enums.VersionType;

public class PaystationEventsRequest extends PaystationSoapRequest {
    
    public String token;
    public String dateFrom;
    public String dateTo;
    public ByEventType eventType;
    public VersionType version;
    public boolean versionSpecified;
    
    public PaystationEventsRequest(){}
    
    public PaystationEventsRequest(SoapObject soapObject)
    {
        if (soapObject == null)
            return;
        if (soapObject.hasProperty("token"))
        {
            Object obj = soapObject.getProperty("token");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                token = j.toString();
            }else if (obj!= null && obj instanceof String){
                token = (String) obj;
            }
        }
        if (soapObject.hasProperty("dateFrom"))
        {
            Object obj = soapObject.getProperty("dateFrom");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                dateFrom = j.toString();
            }else if (obj!= null && obj instanceof String){
                dateFrom = (String) obj;
            }
        }
        if (soapObject.hasProperty("dateTo"))
        {
            Object obj = soapObject.getProperty("dateTo");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                dateTo = j.toString();
            }else if (obj!= null && obj instanceof String){
                dateTo = (String) obj;
            }
        }
        if (soapObject.hasProperty("eventType"))
        {
            Object obj = soapObject.getProperty("eventType");
            if (obj!= null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive)obj;
                eventType = ByEventType.fromString(j.toString());
            }
        }
        if (soapObject.hasProperty("version"))
        {
            Object obj = soapObject.getProperty("version");
            if (obj!= null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive)obj;
                version = VersionType.fromString(j.toString());
            }
        }
        if (soapObject.hasProperty("versionSpecified"))
        {
            Object obj = soapObject.getProperty("versionSpecified");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                versionSpecified = Boolean.parseBoolean(j.toString());
            }else if (obj!= null && obj instanceof Boolean){
                versionSpecified = (Boolean) obj;
            }
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return token;
            case 1:
                return dateFrom;
            case 2:
                return dateTo;
            case 3:
                return eventType.toString();
            case 4:
                return version.toString();
            case 5:
                return versionSpecified;
        }
        return null;
    }
    
    @Override
    public int getPropertyCount() {
        return 6;
    }
    
    @Override
    public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "token";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "dateFrom";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "dateTo";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "eventType";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "version";
                break;
            case 5:
                info.type = PropertyInfo.BOOLEAN_CLASS;
                info.name = "versionSpecified";
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
    
}
