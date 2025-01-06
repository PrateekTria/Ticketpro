package com.ticketpro.vendors.dpt.PlateInfoService;


import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class RegionType implements KvmSerializable {
    
    public String regionName;
    public String description;
    
    public RegionType(){}
    
    public RegionType(SoapObject soapObject)
    {
        if (soapObject == null)
            return;
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
        if (soapObject.hasProperty("description"))
        {
            Object obj = soapObject.getProperty("description");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                description = j.toString();
            }else if (obj!= null && obj instanceof String){
                description = (String) obj;
            }
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return regionName;
            case 1:
                return description;
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
                info.name = "regionName";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "description";
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
    
    @Override
	public String toString() {
		StringBuffer buffer=new StringBuffer();
		String propertyValue;
		PropertyInfo propertyInfo;
		
		buffer.append("{");
		
		for(int index=0; index < getPropertyCount(); index++){
			propertyInfo=new PropertyInfo();
			getPropertyInfo(index, null, propertyInfo);
			propertyValue = (String)getProperty(index);
			
			buffer.append(propertyInfo.name +":"+ propertyValue);
			
			if(index < (getPropertyCount() - 1)){
				buffer.append(",");
			}
		}
		
		buffer.append("}");
		
		return buffer.toString(); 
	}
    
}
