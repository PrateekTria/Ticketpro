package com.ticketpro.vendors.dpt.PlateInfoService;


import java.util.Hashtable;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class PlateInfoByRegionRequest extends PlateSoapRequest{
    
    public String token;
    public String region;
    public int gracePeriod;
    
    public PlateInfoByRegionRequest(){}
    
    public PlateInfoByRegionRequest(SoapObject soapObject)
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
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return token;
            case 1:
                return region;
            case 2:
                return gracePeriod;
        }
        
        return null;
    }
    
    @Override
    public int getPropertyCount() {
        return 3;
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
                info.name = "gracePeriod";
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
