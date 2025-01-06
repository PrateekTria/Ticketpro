package com.ticketpro.vendors.dpt.StallInfoService;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import java.util.Hashtable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class StallInfoType implements KvmSerializable {
    
    public String expiryDate;
    public String purchaseDate;
    public String stallNumber;
    public String setting;
    public String status;
    
    public StallInfoType(){}
    
    public StallInfoType(SoapObject soapObject)
    {
        if (soapObject == null)
            return;
        if (soapObject.hasProperty("expiryDate"))
        {
            Object obj = soapObject.getProperty("expiryDate");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                expiryDate = j.toString();
            }else if (obj!= null && obj instanceof String){
                expiryDate = (String) obj;
            }
        }
        if (soapObject.hasProperty("purchaseDate"))
        {
            Object obj = soapObject.getProperty("purchaseDate");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                purchaseDate = j.toString();
            }else if (obj!= null && obj instanceof String){
                purchaseDate = (String) obj;
            }
        }
        if (soapObject.hasProperty("stallNumber"))
        {
            Object obj = soapObject.getProperty("stallNumber");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                stallNumber = j.toString();
            }else if (obj!= null && obj instanceof String){
                stallNumber = (String) obj;
            }
        }
        if (soapObject.hasProperty("setting"))
        {
            Object obj = soapObject.getProperty("setting");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                setting = j.toString();
            }else if (obj!= null && obj instanceof String){
                setting = (String) obj;
            }
        }
        if (soapObject.hasProperty("status"))
        {
            Object obj = soapObject.getProperty("status");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                status = j.toString();
            }else if (obj!= null && obj instanceof String){
                status = (String) obj;
            }
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return expiryDate;
            case 1:
                return purchaseDate;
            case 2:
                return stallNumber;
            case 3:
                return setting;
            case 4:
                return status;
        }
        return null;
    }
    
    @Override
    public int getPropertyCount() {
        return 5;
    }
    
    @Override
    public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "expiryDate";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "purchaseDate";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "stallNumber";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "setting";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "status";
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
