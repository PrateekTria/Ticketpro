package com.ticketpro.vendors.dpt.StallInfoService;


import java.util.List;

import org.apache.log4j.Logger;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import android.os.AsyncTask;
import android.util.Log;

import com.ticketpro.util.FakeX509TrustManager;
import com.ticketpro.util.TPUtility;
import com.ticketpro.vendors.dpt.StallInfoService.WS_Enums.SoapProtocolVersion;

public class StallInfoService {
    
    public String NAMESPACE ="http://ws.digitalpaytech.com/stallInfo";
    public String url="https://developer.digitalpaytech.com/services/StallInfoService?wsdl";
    public int timeOut = 5000;
    public IWsdl2CodeEvents eventHandler;
    public SoapProtocolVersion soapVersion;
    public Element[] headerElements;
    public Logger log=Logger.getLogger("StallInfoService");
   
    public StallInfoService(){
    	FakeX509TrustManager.allowAllSSL();
    }
    
    public StallInfoService(IWsdl2CodeEvents eventHandler){
        this.eventHandler = eventHandler;
        FakeX509TrustManager.allowAllSSL();
    }
    
    public StallInfoService(IWsdl2CodeEvents eventHandler,String url){
        this.eventHandler = eventHandler;
        this.url = url;
    }
    
    public StallInfoService(IWsdl2CodeEvents eventHandler,String url,int timeOutInSeconds){
        this.eventHandler = eventHandler;
        this.url = url;
        this.setTimeOut(timeOutInSeconds);
    }
    
    public void addAuthHeader(String username, String password){
    	headerElements = new Element[1];
    	headerElements[0] = new Element().createElement("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","Security");
    	headerElements[0].setAttribute(null, "mustUnderstand","1");

        Element usernametoken = new Element().createElement("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "UsernameToken");
        usernametoken.setAttribute(null, "Id", "UsernameToken-1");
        headerElements[0].addChild(Node.ELEMENT, usernametoken);

        Element usernameElement = new Element().createElement(null, "n0:Username");
        usernameElement.addChild(Node.IGNORABLE_WHITESPACE, username);
        usernametoken.addChild(Node.ELEMENT, usernameElement);

        Element passwordElement = new Element().createElement(null,"n0:Password");
        passwordElement.setAttribute(null, "Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
        passwordElement.addChild(Node.TEXT, password);
        usernametoken.addChild(Node.ELEMENT, passwordElement);
    }
    
    public void setTimeOut(int seconds){
        this.timeOut = seconds * 1000;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public void getRegionsAsync(RegionRequest regionRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getRegionsAsync(regionRequest, null);
    }
    
    public void getRegionsAsync(final RegionRequest regionRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorRegionType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorRegionType doInBackground(Void... params) {
                return getRegions(regionRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorRegionType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getRegions", result);
                }
            }
        }.execute();
    }
    
    public VectorRegionType getRegions(RegionRequest regionRequest){
        return getRegions(regionRequest, null);
    }
    
    public VectorRegionType getRegions(RegionRequest regionRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        
        soapEnvelope.setOutputSoapObject(regionRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        httpTransport.debug = true;
       
        try{
        	if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/stallInfo/getRegions", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/stallInfo/getRegions", soapEnvelope);
            }
        	
        	Object retObj = soapEnvelope.bodyIn;
        	if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);
                if (eventHandler != null)
                    eventHandler.Wsdl2CodeFinishedWithException(ex);
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result!=null){
                    return new VectorRegionType(result);
                 }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            
            Log.e("StallInfoService", TPUtility.getPrintStackTrace(e));
        }
        
        return null;
    }
    
    public void getSettingsAsync(SettingRequest settingRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        
        getSettingsAsync(settingRequest, null);
    }
    
    public void getSettingsAsync(final SettingRequest settingRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorSettingType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorSettingType doInBackground(Void... params) {
                return getSettings(settingRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorSettingType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getSettings", result);
                }
            }
        }.execute();
    }
    
    public VectorSettingType getSettings(SettingRequest settingRequest){
        return getSettings(settingRequest, null);
    }
    
    public VectorSettingType getSettings(SettingRequest settingRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        
        soapEnvelope.setOutputSoapObject(settingRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/stallInfo/getSettings", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/stallInfo/getSettings", soapEnvelope);
            }
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);
                if (eventHandler != null)
                    eventHandler.Wsdl2CodeFinishedWithException(ex);
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result!=null){
                    return new VectorSettingType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            
            Log.e("StallInfoService", TPUtility.getPrintStackTrace(e));
        }
        return null;
    }
    
    public void getStallInfoByGroupAsync(StallInfoByGroupRequest stallInfoByGroupRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getStallInfoByGroupAsync(stallInfoByGroupRequest, null);
    }
    
    public void getStallInfoByGroupAsync(final StallInfoByGroupRequest stallInfoByGroupRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorStallInfoType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorStallInfoType doInBackground(Void... params) {
                return getStallInfoByGroup(stallInfoByGroupRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorStallInfoType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getStallInfoByGroup", result);
                }
            }
        }.execute();
    }
    
    public VectorStallInfoType getStallInfoByGroup(StallInfoByGroupRequest stallInfoByGroupRequest){
        return getStallInfoByGroup(stallInfoByGroupRequest, null);
    }
    
    public VectorStallInfoType getStallInfoByGroup(StallInfoByGroupRequest stallInfoByGroupRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        
        soapEnvelope.setOutputSoapObject(stallInfoByGroupRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/stallInfo/getStallInfoByGroup", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/stallInfo/getStallInfoByGroup", soapEnvelope);
            }
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);
                if (eventHandler != null)
                    eventHandler.Wsdl2CodeFinishedWithException(ex);
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result!=null){
                    return new VectorStallInfoType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            Log.e("StallInfoService", TPUtility.getPrintStackTrace(e));
        }
        return null;
    }
    
    public void getStallInfoAsync(StallInfoRequest stallInfoRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getStallInfoAsync(stallInfoRequest, null);
    }
    
    public void getStallInfoAsync(final StallInfoRequest stallInfoRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorStallInfoType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorStallInfoType doInBackground(Void... params) {
                return getStallInfo(stallInfoRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorStallInfoType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getStallInfo", result);
                }
            }
        }.execute();
    }
    
    public VectorStallInfoType getStallInfo(StallInfoRequest stallInfoRequest){
        return getStallInfo(stallInfoRequest, null);
    }
    
    public VectorStallInfoType getStallInfo(StallInfoRequest stallInfoRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        
        soapEnvelope.setOutputSoapObject(stallInfoRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/stallInfo/getStallInfo", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/stallInfo/getStallInfo", soapEnvelope);
            }
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);
                if (eventHandler != null)
                    eventHandler.Wsdl2CodeFinishedWithException(ex);
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result!=null){
                    return new VectorStallInfoType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            Log.e("StallInfoService", TPUtility.getPrintStackTrace(e));
        }
        return null;
    }
    
    public void getGroupsAsync(GroupRequest groupRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getGroupsAsync(groupRequest, null);
    }
    
    public void getGroupsAsync(final GroupRequest groupRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorGroupType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorGroupType doInBackground(Void... params) {
                return getGroups(groupRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorGroupType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getGroups", result);
                }
            }
        }.execute();
    }
    
    public VectorGroupType getGroups(GroupRequest groupRequest){
        return getGroups(groupRequest, null);
    }
    
    public VectorGroupType getGroups(GroupRequest groupRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        
        soapEnvelope.setOutputSoapObject(groupRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/stallInfo/getGroups", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/stallInfo/getGroups", soapEnvelope);
            }
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);
                if (eventHandler != null)
                    eventHandler.Wsdl2CodeFinishedWithException(ex);
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result!=null){
                    return new VectorGroupType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            Log.e("StallInfoService", TPUtility.getPrintStackTrace(e));
        }
        return null;
    }
    
    public void getStallInfoBySettingAsync(StallInfoBySettingRequest stallInfoBySettingRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getStallInfoBySettingAsync(stallInfoBySettingRequest, null);
    }
    
    public void getStallInfoBySettingAsync(final StallInfoBySettingRequest stallInfoBySettingRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorStallInfoType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorStallInfoType doInBackground(Void... params) {
                return getStallInfoBySetting(stallInfoBySettingRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorStallInfoType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getStallInfoBySetting", result);
                }
            }
        }.execute();
    }
    
    public VectorStallInfoType getStallInfoBySetting(StallInfoBySettingRequest stallInfoBySettingRequest){
        return getStallInfoBySetting(stallInfoBySettingRequest, null);
    }
    
    public VectorStallInfoType getStallInfoBySetting(StallInfoBySettingRequest stallInfoBySettingRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        
        soapEnvelope.setOutputSoapObject(stallInfoBySettingRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/stallInfo/getStallInfoBySetting", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/stallInfo/getStallInfoBySetting", soapEnvelope);
            }
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);
                if (eventHandler != null)
                    eventHandler.Wsdl2CodeFinishedWithException(ex);
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result!=null){
                    return new VectorStallInfoType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            Log.e("StallInfoService", TPUtility.getPrintStackTrace(e));
        }
        return null;
    }
    
    public void getStallInfoByRegionAsync(StallInfoByRegionRequest stallInfoByRegionRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getStallInfoByRegionAsync(stallInfoByRegionRequest, null);
    }
    
    public void getStallInfoByRegionAsync(final StallInfoByRegionRequest stallInfoByRegionRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorStallInfoType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorStallInfoType doInBackground(Void... params) {
                return getStallInfoByRegion(stallInfoByRegionRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorStallInfoType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getStallInfoByRegion", result);
                }
            }
        }.execute();
    }
    
    public VectorStallInfoType getStallInfoByRegion(StallInfoByRegionRequest stallInfoByRegionRequest){
        return getStallInfoByRegion(stallInfoByRegionRequest, null);
    }
    
    public VectorStallInfoType getStallInfoByRegion(StallInfoByRegionRequest stallInfoByRegionRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        
        soapEnvelope.setOutputSoapObject(stallInfoByRegionRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/stallInfo/getStallInfoByRegion", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/stallInfo/getStallInfoByRegion", soapEnvelope);
            }
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);
                if (eventHandler != null)
                    eventHandler.Wsdl2CodeFinishedWithException(ex);
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result!=null){
                    return new VectorStallInfoType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            Log.e("StallInfoService", TPUtility.getPrintStackTrace(e));
        }
        return null;
    }
    
}
