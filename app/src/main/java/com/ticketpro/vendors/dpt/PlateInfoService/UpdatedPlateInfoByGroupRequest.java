package com.ticketpro.vendors.dpt.PlateInfoService;


import java.util.Hashtable;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class UpdatedPlateInfoByGroupRequest extends PlateSoapRequest{
    
    public String token;
    public String groupName;
    public String lastCallDate;
    
    public UpdatedPlateInfoByGroupRequest(){}
    
    public UpdatedPlateInfoByGroupRequest(SoapObject soapObject)
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
        if (soapObject.hasProperty("groupName"))
        {
            Object obj = soapObject.getProperty("groupName");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                groupName = j.toString();
            }else if (obj!= null && obj instanceof String){
                groupName = (String) obj;
            }
        }
        if (soapObject.hasProperty("lastCallDate"))
        {
            Object obj = soapObject.getProperty("lastCallDate");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                lastCallDate = j.toString();
            }else if (obj!= null && obj instanceof String){
                lastCallDate = (String) obj;
            }
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return token;
            case 1:
                return groupName;
            case 2:
                return lastCallDate;
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
                info.name = "groupName";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "lastCallDate";
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
