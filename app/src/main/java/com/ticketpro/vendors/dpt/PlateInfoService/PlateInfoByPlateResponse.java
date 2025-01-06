package com.ticketpro.vendors.dpt.PlateInfoService;


import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class PlateInfoByPlateResponse implements KvmSerializable {
    
    public PlateInfoType plateInfo;
    
    public PlateInfoByPlateResponse(){}
    
    public PlateInfoByPlateResponse(SoapObject soapObject)
    {
        if (soapObject == null)
            return;
        if (soapObject.hasProperty("plateInfo"))
        {
            SoapObject j = (SoapObject)soapObject.getProperty("plateInfo");
            plateInfo =  new PlateInfoType (j);
            
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return plateInfo;
        }
        return null;
    }
    
    @Override
    public int getPropertyCount() {
        return 1;
    }
    
    @Override
    public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PlateInfoType.class;
                info.name = "plateInfo";
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
