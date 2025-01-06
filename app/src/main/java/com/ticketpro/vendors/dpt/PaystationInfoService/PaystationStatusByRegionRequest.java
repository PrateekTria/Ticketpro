package com.ticketpro.vendors.dpt.PaystationInfoService;

import java.util.Hashtable;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class PaystationStatusByRegionRequest extends PaystationSoapRequest{
    
    public String token;
    public String regionName;
    
    public PaystationStatusByRegionRequest(){}
    
    public PaystationStatusByRegionRequest(SoapObject soapObject)
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
        if (soapObject.hasProperty("regionName"))
        {
            Object obj = soapObject.getProperty("regionName");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                regionName = j.toString();
            }else if (obj!= null && obj instanceof String){
                regionName = (String) obj;
            }
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return token;
            case 1:
                return regionName;
        }
        return null;
    }
    
    @Override
    public int getPropertyCount() {
        return 2;
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
                info.name = "regionName";
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
