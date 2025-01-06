package com.ticketpro.vendors.dpt.PaystationInfoService;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import java.util.Hashtable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class PaystationStatusEventType implements KvmSerializable {
    
    public String eventSeverity;
    public String eventDetail;
    public String eventTimeStamp;
    
    public PaystationStatusEventType(){}
    
    public PaystationStatusEventType(SoapObject soapObject)
    {
        if (soapObject == null)
            return;
        if (soapObject.hasProperty("eventSeverity"))
        {
            Object obj = soapObject.getProperty("eventSeverity");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                eventSeverity = j.toString();
            }else if (obj!= null && obj instanceof String){
                eventSeverity = (String) obj;
            }
        }
        if (soapObject.hasProperty("eventDetail"))
        {
            Object obj = soapObject.getProperty("eventDetail");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                eventDetail = j.toString();
            }else if (obj!= null && obj instanceof String){
                eventDetail = (String) obj;
            }
        }
        if (soapObject.hasProperty("eventTimeStamp"))
        {
            Object obj = soapObject.getProperty("eventTimeStamp");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                eventTimeStamp = j.toString();
            }else if (obj!= null && obj instanceof String){
                eventTimeStamp = (String) obj;
            }
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return eventSeverity;
            case 1:
                return eventDetail;
            case 2:
                return eventTimeStamp;
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
                info.name = "eventSeverity";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "eventDetail";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "eventTimeStamp";
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
