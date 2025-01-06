package com.ticketpro.vendors.dpt.PaystationInfoService;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import java.util.Hashtable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class PaystationEventsType implements KvmSerializable {
    
    public String serialNumber;
    public String type;
    public String action;
    public String severity;
    public String information;
    public String timeStamp;
    
    public PaystationEventsType(){}
    
    public PaystationEventsType(SoapObject soapObject)
    {
        if (soapObject == null)
            return;
        if (soapObject.hasProperty("serialNumber"))
        {
            Object obj = soapObject.getProperty("serialNumber");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                serialNumber = j.toString();
            }else if (obj!= null && obj instanceof String){
                serialNumber = (String) obj;
            }
        }
        if (soapObject.hasProperty("type"))
        {
            Object obj = soapObject.getProperty("type");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                type = j.toString();
            }else if (obj!= null && obj instanceof String){
                type = (String) obj;
            }
        }
        if (soapObject.hasProperty("action"))
        {
            Object obj = soapObject.getProperty("action");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                action = j.toString();
            }else if (obj!= null && obj instanceof String){
                action = (String) obj;
            }
        }
        if (soapObject.hasProperty("severity"))
        {
            Object obj = soapObject.getProperty("severity");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                severity = j.toString();
            }else if (obj!= null && obj instanceof String){
                severity = (String) obj;
            }
        }
        if (soapObject.hasProperty("information"))
        {
            Object obj = soapObject.getProperty("information");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                information = j.toString();
            }else if (obj!= null && obj instanceof String){
                information = (String) obj;
            }
        }
        if (soapObject.hasProperty("timeStamp"))
        {
            Object obj = soapObject.getProperty("timeStamp");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                timeStamp = j.toString();
            }else if (obj!= null && obj instanceof String){
                timeStamp = (String) obj;
            }
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return serialNumber;
            case 1:
                return type;
            case 2:
                return action;
            case 3:
                return severity;
            case 4:
                return information;
            case 5:
                return timeStamp;
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
                info.name = "serialNumber";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "type";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "action";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "severity";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "information";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "timeStamp";
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
