package com.ticketpro.lpr;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class LPRRequest implements KvmSerializable {
    
    public String strXMLImage;
    public String strPrecisionMode;
    public String strPlateNumber;
    public String strState;
    public String strError;
    
    public LPRRequest(){}
    
    public LPRRequest(SoapObject soapObject)
    {
        if (soapObject == null)
            return;
        
        if (soapObject.hasProperty("strXMLImage"))
        {
            Object obj = soapObject.getProperty("strXMLImage");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) soapObject.getProperty("strXMLImage");
                strXMLImage = j.toString();
            }
        }
        
        if (soapObject.hasProperty("strPrecisionMode"))
        {
            Object obj = soapObject.getProperty("strPrecisionMode");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) soapObject.getProperty("strPrecisionMode");
                strPrecisionMode = j.toString();
            }
        }
        
        if (soapObject.hasProperty("strPlateNumber"))
        {
            Object obj = soapObject.getProperty("strPlateNumber");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) soapObject.getProperty("strPlateNumber");
                strPlateNumber = j.toString();
            }
        }
        
        if (soapObject.hasProperty("strState"))
        {
            Object obj = soapObject.getProperty("strState");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) soapObject.getProperty("strState");
                strState = j.toString();
            }
        }
        
        
        if (soapObject.hasProperty("strError"))
        {
            Object obj = soapObject.getProperty("strError");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) soapObject.getProperty("strError");
                strError = j.toString();
            }
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return strXMLImage;
            case 1:
                return strPrecisionMode;
            case 2:
                return strPlateNumber;
            case 3:
                return strState;
            case 4:
                return strError;    
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
                info.name = "strXMLImage";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "strPrecisionMode";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "strPlateNumber";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "strState";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "strError";
                break;    
                
        }
    }

	@Override
	public void setProperty(int arg0, Object arg1) {
		
	}

}
