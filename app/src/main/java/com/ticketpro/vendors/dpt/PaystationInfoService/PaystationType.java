package com.ticketpro.vendors.dpt.PaystationInfoService;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import java.util.Hashtable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import com.ticketpro.vendors.dpt.PaystationInfoService.VectorString;

public class PaystationType implements KvmSerializable {
    
    public String serialNumber;
    public String paystationName;
    public VectorString paystationGroup;
    public int regionId;
    public String regionName;
    
    public PaystationType(){}
    
    public PaystationType(SoapObject soapObject)
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
        if (soapObject.hasProperty("paystationName"))
        {
            Object obj = soapObject.getProperty("paystationName");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                paystationName = j.toString();
            }else if (obj!= null && obj instanceof String){
                paystationName = (String) obj;
            }
        }
        if (soapObject.hasProperty("paystationGroup"))
        {
            SoapObject j = (SoapObject)soapObject.getProperty("paystationGroup");
            paystationGroup = new VectorString(j);
        }
        if (soapObject.hasProperty("regionId"))
        {
            Object obj = soapObject.getProperty("regionId");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                regionId = Integer.parseInt(j.toString());
            }else if (obj!= null && obj instanceof Number){
                regionId = (Integer) obj;
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
                return serialNumber;
            case 1:
                return paystationName;
            case 2:
                return paystationGroup;
            case 3:
                return regionId;
            case 4:
                return regionName;
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
                info.name = "serialNumber";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "paystationName";
                break;
            case 2:
                info.type = PropertyInfo.VECTOR_CLASS;
                info.name = "paystationGroup";
                break;
            case 3:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "regionId";
                break;
            case 4:
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
