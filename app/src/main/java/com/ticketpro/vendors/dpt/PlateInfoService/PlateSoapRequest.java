package com.ticketpro.vendors.dpt.PlateInfoService;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class PlateSoapRequest implements KvmSerializable{
    
	public SoapObject getSoapObject() {
		SoapObject request = new SoapObject("", "plate:"+ this.getClass().getSimpleName());
		request.addAttribute("xmlns:plate", "http://ws.digitalpaytech.com/plateInfo");
		
		for(int i=0;i<getPropertyCount(); i++){
			PropertyInfo info=new PropertyInfo();
			getPropertyInfo(i, null, info);
			request.addProperty(info.getName(), getProperty(i));
		}
		
		return request;
	}
	
	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		// TODO Auto-generated method stub
	}
}
