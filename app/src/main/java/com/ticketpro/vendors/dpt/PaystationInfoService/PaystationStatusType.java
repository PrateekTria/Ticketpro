package com.ticketpro.vendors.dpt.PaystationInfoService;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import java.util.Hashtable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import com.ticketpro.vendors.dpt.PaystationInfoService.VectorPaystationStatusEventType;

public class PaystationStatusType implements KvmSerializable {
    
    public String serialNumber;
    public float battery1voltage;
    public float battery2voltage;
    public String alarmState;
    public VectorPaystationStatusEventType paystationStatusEvent;
    public boolean isInServiceMode;
    public boolean isDoorOpen;
    public String lastHeartBeat;
    
    public PaystationStatusType(){}
    
    public PaystationStatusType(SoapObject soapObject)
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
        if (soapObject.hasProperty("battery1voltage"))
        {
            Object obj = soapObject.getProperty("battery1voltage");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                battery1voltage = Float.parseFloat(j.toString());
            }else if (obj!= null && obj instanceof Number){
                battery1voltage = (Integer) obj;
            }
        }
        if (soapObject.hasProperty("battery2voltage"))
        {
            Object obj = soapObject.getProperty("battery2voltage");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                battery2voltage = Float.parseFloat(j.toString());
            }else if (obj!= null && obj instanceof Number){
                battery2voltage = (Integer) obj;
            }
        }
        if (soapObject.hasProperty("alarmState"))
        {
            Object obj = soapObject.getProperty("alarmState");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                alarmState = j.toString();
            }else if (obj!= null && obj instanceof String){
                alarmState = (String) obj;
            }
        }
        if (soapObject.hasProperty("paystationStatusEvent"))
        {
            SoapObject j = (SoapObject)soapObject.getProperty("paystationStatusEvent");
            paystationStatusEvent = new VectorPaystationStatusEventType(j);
        }
        if (soapObject.hasProperty("isInServiceMode"))
        {
            Object obj = soapObject.getProperty("isInServiceMode");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                isInServiceMode = Boolean.parseBoolean(j.toString());
            }else if (obj!= null && obj instanceof Boolean){
                isInServiceMode = (Boolean) obj;
            }
        }
        if (soapObject.hasProperty("isDoorOpen"))
        {
            Object obj = soapObject.getProperty("isDoorOpen");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                isDoorOpen = Boolean.parseBoolean(j.toString());
            }else if (obj!= null && obj instanceof Boolean){
                isDoorOpen = (Boolean) obj;
            }
        }
        if (soapObject.hasProperty("lastHeartBeat"))
        {
            Object obj = soapObject.getProperty("lastHeartBeat");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                lastHeartBeat = j.toString();
            }else if (obj!= null && obj instanceof String){
                lastHeartBeat = (String) obj;
            }
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return serialNumber;
            case 1:
                return battery1voltage;
            case 2:
                return battery2voltage;
            case 3:
                return alarmState;
            case 4:
                return paystationStatusEvent;
            case 5:
                return isInServiceMode;
            case 6:
                return isDoorOpen;
            case 7:
                return lastHeartBeat;
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
                info.name = "serialNumber";
                break;
            case 1:
                info.type = Float.class;
                info.name = "battery1voltage";
                break;
            case 2:
                info.type = Float.class;
                info.name = "battery2voltage";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "alarmState";
                break;
            case 4:
                info.type = PropertyInfo.VECTOR_CLASS;
                info.name = "paystationStatusEvent";
                break;
            case 5:
                info.type = PropertyInfo.BOOLEAN_CLASS;
                info.name = "isInServiceMode";
                break;
            case 6:
                info.type = PropertyInfo.BOOLEAN_CLASS;
                info.name = "isDoorOpen";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "lastHeartBeat";
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
