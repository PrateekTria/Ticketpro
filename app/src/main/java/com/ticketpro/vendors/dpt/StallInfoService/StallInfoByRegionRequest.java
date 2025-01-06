package com.ticketpro.vendors.dpt.StallInfoService;


import java.util.Hashtable;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import com.ticketpro.vendors.dpt.StallInfoService.WS_Enums.Bystatus;

public class StallInfoByRegionRequest extends StallSoapRequest{
    
    public String token;
    public String region;
    public int stallfrom;
    public int stallto;
    public Bystatus stallstatus;
    public String datetimeStamp;
    public int gracePeriod;
    public boolean gracePeriodSpecified;
    
    public StallInfoByRegionRequest(){}
    
    public StallInfoByRegionRequest(SoapObject soapObject)
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
        if (soapObject.hasProperty("region"))
        {
            Object obj = soapObject.getProperty("region");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                region = j.toString();
            }else if (obj!= null && obj instanceof String){
                region = (String) obj;
            }
        }
        if (soapObject.hasProperty("stallfrom"))
        {
            Object obj = soapObject.getProperty("stallfrom");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                stallfrom = Integer.parseInt(j.toString());
            }else if (obj!= null && obj instanceof Number){
                stallfrom = (Integer) obj;
            }
        }
        if (soapObject.hasProperty("stallto"))
        {
            Object obj = soapObject.getProperty("stallto");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                stallto = Integer.parseInt(j.toString());
            }else if (obj!= null && obj instanceof Number){
                stallto = (Integer) obj;
            }
        }
        if (soapObject.hasProperty("stallstatus"))
        {
            Object obj = soapObject.getProperty("stallstatus");
            if (obj!= null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive)obj;
                stallstatus = Bystatus.fromString(j.toString());
            }
        }
        if (soapObject.hasProperty("datetimeStamp"))
        {
            Object obj = soapObject.getProperty("datetimeStamp");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                datetimeStamp = j.toString();
            }else if (obj!= null && obj instanceof String){
                datetimeStamp = (String) obj;
            }
        }
        if (soapObject.hasProperty("gracePeriod"))
        {
            Object obj = soapObject.getProperty("gracePeriod");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                gracePeriod = Integer.parseInt(j.toString());
            }else if (obj!= null && obj instanceof Number){
                gracePeriod = (Integer) obj;
            }
        }
        if (soapObject.hasProperty("gracePeriodSpecified"))
        {
            Object obj = soapObject.getProperty("gracePeriodSpecified");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                gracePeriodSpecified = Boolean.parseBoolean(j.toString());
            }else if (obj!= null && obj instanceof Boolean){
                gracePeriodSpecified = (Boolean) obj;
            }
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return token;
            case 1:
                return region;
            case 2:
                return stallfrom;
            case 3:
                return stallto;
            case 4:
                return stallstatus.toString();
            case 5:
                return datetimeStamp;
            case 6:
                return gracePeriod;
            case 7:
                return gracePeriodSpecified;
        }
        return null;
    }
    
    @Override
    public int getPropertyCount() {
        return 8;
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
                info.name = "region";
                break;
            case 2:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "stallfrom";
                break;
            case 3:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "stallto";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "stallstatus";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "datetimeStamp";
                break;
            case 6:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "gracePeriod";
                break;
            case 7:
                info.type = PropertyInfo.BOOLEAN_CLASS;
                info.name = "gracePeriodSpecified";
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
