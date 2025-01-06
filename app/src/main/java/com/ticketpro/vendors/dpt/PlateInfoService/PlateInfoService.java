package com.ticketpro.vendors.dpt.PlateInfoService;


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

import com.ticketpro.util.FakeX509TrustManager;
import com.ticketpro.vendors.dpt.PlateInfoService.WS_Enums.SoapProtocolVersion;

import android.os.AsyncTask;

public class PlateInfoService {
    
    public String NAMESPACE ="http://ws.digitalpaytech.com/plateInfo";
    public String url="https://developer.digitalpaytech.com/services/PlateInfoService?wsdl";
    public int timeOut = 5000;
    public IWsdl2CodeEvents eventHandler;
    public SoapProtocolVersion soapVersion;
    public Element[] headerElements;
    public Logger log=Logger.getLogger("PlateInfoService");
    
    public PlateInfoService(){
    	FakeX509TrustManager.allowAllSSL();
    }
    
    public PlateInfoService(IWsdl2CodeEvents eventHandler)
    {
        this.eventHandler = eventHandler;
        FakeX509TrustManager.allowAllSSL();
    }
    
    public PlateInfoService(IWsdl2CodeEvents eventHandler,String url)
    {
        this.eventHandler = eventHandler;
        this.url = url;
    }
    public PlateInfoService(IWsdl2CodeEvents eventHandler,String url,int timeOutInSeconds)
    {
        this.eventHandler = eventHandler;
        this.url = url;
        this.setTimeOut(timeOutInSeconds);
    }
    public void setTimeOut(int seconds){
        this.timeOut = seconds * 1000;
    }
    public void setUrl(String url){
        this.url = url;
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
    
    public void getUpdatedValidPlatesByRegionAsync(UpdatedPlateInfoByRegionRequest updatedPlateInfoByRegionRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getUpdatedValidPlatesByRegionAsync(updatedPlateInfoByRegionRequest, null);
    }
    
    public void getUpdatedValidPlatesByRegionAsync(final UpdatedPlateInfoByRegionRequest updatedPlateInfoByRegionRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPlateInfoType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPlateInfoType doInBackground(Void... params) {
                return getUpdatedValidPlatesByRegion(updatedPlateInfoByRegionRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPlateInfoType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getUpdatedValidPlatesByRegion", result);
                }
            }
        }.execute();
    }
    
    public VectorPlateInfoType getUpdatedValidPlatesByRegion(UpdatedPlateInfoByRegionRequest updatedPlateInfoByRegionRequest){
        return getUpdatedValidPlatesByRegion(updatedPlateInfoByRegionRequest, null);
    }
    
    public VectorPlateInfoType getUpdatedValidPlatesByRegion(UpdatedPlateInfoByRegionRequest updatedPlateInfoByRegionRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        soapEnvelope.setOutputSoapObject(updatedPlateInfoByRegionRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        httpTransport.debug = true;
        
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getUpdatedValidPlatesByRegion", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getUpdatedValidPlatesByRegion", soapEnvelope);
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
                    return new VectorPlateInfoType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void getUpdatedValidPlatesAsync(UpdatedPlateInfoRequest updatedPlateInfoRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getUpdatedValidPlatesAsync(updatedPlateInfoRequest, null);
    }
    
    public void getUpdatedValidPlatesAsync(final UpdatedPlateInfoRequest updatedPlateInfoRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPlateInfoType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPlateInfoType doInBackground(Void... params) {
                return getUpdatedValidPlates(updatedPlateInfoRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPlateInfoType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getUpdatedValidPlates", result);
                }
            }
        }.execute();
    }
    
    public VectorPlateInfoType getUpdatedValidPlates(UpdatedPlateInfoRequest updatedPlateInfoRequest){
        return getUpdatedValidPlates(updatedPlateInfoRequest, null);
    }
    
    public VectorPlateInfoType getUpdatedValidPlates(UpdatedPlateInfoRequest updatedPlateInfoRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        
        SoapObject soapReq = new SoapObject("http://ws.digitalpaytech.com/plateInfo","getUpdatedValidPlates");
        soapEnvelope.addMapping("http://ws.digitalpaytech.com/plateInfo","UpdatedPlateInfoRequest",new UpdatedPlateInfoRequest().getClass());
        soapReq.addProperty("UpdatedPlateInfoRequest",updatedPlateInfoRequest);
        soapEnvelope.setOutputSoapObject(soapReq);
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        httpTransport.debug=true;
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getUpdatedValidPlates", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getUpdatedValidPlates", soapEnvelope);
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
                    return new VectorPlateInfoType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void getUpdatedValidPlatesByGroupAsync(UpdatedPlateInfoByGroupRequest updatedPlateInfoByGroupRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getUpdatedValidPlatesByGroupAsync(updatedPlateInfoByGroupRequest, null);
    }
    
    public void getUpdatedValidPlatesByGroupAsync(final UpdatedPlateInfoByGroupRequest updatedPlateInfoByGroupRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPlateInfoType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPlateInfoType doInBackground(Void... params) {
                return getUpdatedValidPlatesByGroup(updatedPlateInfoByGroupRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPlateInfoType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getUpdatedValidPlatesByGroup", result);
                }
            }
        }.execute();
    }
    
    public VectorPlateInfoType getUpdatedValidPlatesByGroup(UpdatedPlateInfoByGroupRequest updatedPlateInfoByGroupRequest){
        return getUpdatedValidPlatesByGroup(updatedPlateInfoByGroupRequest, null);
    }
    
    public VectorPlateInfoType getUpdatedValidPlatesByGroup(UpdatedPlateInfoByGroupRequest updatedPlateInfoByGroupRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        soapEnvelope.setOutputSoapObject(updatedPlateInfoByGroupRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getUpdatedValidPlatesByGroup", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getUpdatedValidPlatesByGroup", soapEnvelope);
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
                    return new VectorPlateInfoType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void getValidPlatesByRegionAsync(PlateInfoByRegionRequest plateInfoByRegionRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getValidPlatesByRegionAsync(plateInfoByRegionRequest, null);
    }
    
    public void getValidPlatesByRegionAsync(final PlateInfoByRegionRequest plateInfoByRegionRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPlateInfoType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPlateInfoType doInBackground(Void... params) {
                return getValidPlatesByRegion(plateInfoByRegionRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPlateInfoType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getValidPlatesByRegion", result);
                }
            }
        }.execute();
    }
    
    public VectorPlateInfoType getValidPlatesByRegion(PlateInfoByRegionRequest plateInfoByRegionRequest){
        return getValidPlatesByRegion(plateInfoByRegionRequest, null);
    }
    
    public VectorPlateInfoType getValidPlatesByRegion(PlateInfoByRegionRequest plateInfoByRegionRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        soapEnvelope.setOutputSoapObject(plateInfoByRegionRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        httpTransport.debug=true;
        
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getValidPlatesByRegion", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getValidPlatesByRegion", soapEnvelope);
            }
            
            Object retObj = soapEnvelope.bodyIn;
            
            //Log Network Calls
            //RequestLogTask task=new RequestLogTask("url=" + url +", region="+ plateInfoByRegionRequest.region +", token="+ plateInfoByRegionRequest.token, "DEBUG", retObj.toString());
            //task.execute();
            
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);
                if (eventHandler != null)
                    eventHandler.Wsdl2CodeFinishedWithException(ex);
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result!=null){
                    return new VectorPlateInfoType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void getPlateInfoAsync(PlateInfoByPlateRequest plateInfoByPlateRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getPlateInfoAsync(plateInfoByPlateRequest, null);
    }
    
    public void getPlateInfoAsync(final PlateInfoByPlateRequest plateInfoByPlateRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, PlateInfoByPlateResponse>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected PlateInfoByPlateResponse doInBackground(Void... params) {
                return getPlateInfo(plateInfoByPlateRequest, headers);
            }
            @Override
            protected void onPostExecute(PlateInfoByPlateResponse result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getPlateInfo", result);
                }
            }
        }.execute();
    }
    
    public PlateInfoByPlateResponse getPlateInfo(PlateInfoByPlateRequest plateInfoByPlateRequest){
        return getPlateInfo(plateInfoByPlateRequest, null);
    }
    
    public PlateInfoByPlateResponse getPlateInfo(PlateInfoByPlateRequest plateInfoByPlateRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        soapEnvelope.setOutputSoapObject(plateInfoByPlateRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        httpTransport.debug=true;
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getPlateInfo", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getPlateInfo", soapEnvelope);
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
                    return new PlateInfoByPlateResponse(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void getValidPlatesAsync(PlateInfoRequest plateInfoRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getValidPlatesAsync(plateInfoRequest, null);
    }
    
    public void getValidPlatesAsync(final PlateInfoRequest plateInfoRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPlateInfoType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPlateInfoType doInBackground(Void... params) {
                return getValidPlates(plateInfoRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPlateInfoType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getValidPlates", result);
                }
            }
        }.execute();
    }
    
    public VectorPlateInfoType getValidPlates(PlateInfoRequest plateInfoRequest){
        return getValidPlates(plateInfoRequest, null);
    }
    
    public VectorPlateInfoType getValidPlates(PlateInfoRequest plateInfoRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        
        soapEnvelope.setOutputSoapObject(plateInfoRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getValidPlates", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getValidPlates", soapEnvelope);
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
                    return new VectorPlateInfoType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void getExpiredPlatesByGroupAsync(ExpiredPlateInfoByGroupRequest expiredPlateInfoByGroupRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getExpiredPlatesByGroupAsync(expiredPlateInfoByGroupRequest, null);
    }
    
    public void getExpiredPlatesByGroupAsync(final ExpiredPlateInfoByGroupRequest expiredPlateInfoByGroupRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPlateInfoType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPlateInfoType doInBackground(Void... params) {
                return getExpiredPlatesByGroup(expiredPlateInfoByGroupRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPlateInfoType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getExpiredPlatesByGroup", result);
                }
            }
        }.execute();
    }
    
    public VectorPlateInfoType getExpiredPlatesByGroup(ExpiredPlateInfoByGroupRequest expiredPlateInfoByGroupRequest){
        return getExpiredPlatesByGroup(expiredPlateInfoByGroupRequest, null);
    }
    
    public VectorPlateInfoType getExpiredPlatesByGroup(ExpiredPlateInfoByGroupRequest expiredPlateInfoByGroupRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        
        soapEnvelope.setOutputSoapObject(expiredPlateInfoByGroupRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getExpiredPlatesByGroup", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getExpiredPlatesByGroup", soapEnvelope);
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
                    return new VectorPlateInfoType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
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
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getGroups", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getGroups", soapEnvelope);
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
            e.printStackTrace();
        }
        return null;
    }
    
    public void getExpiredPlatesByRegionAsync(ExpiredPlateInfoByRegionRequest expiredPlateInfoByRegionRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getExpiredPlatesByRegionAsync(expiredPlateInfoByRegionRequest, null);
    }
    
    public void getExpiredPlatesByRegionAsync(final ExpiredPlateInfoByRegionRequest expiredPlateInfoByRegionRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPlateInfoType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPlateInfoType doInBackground(Void... params) {
                return getExpiredPlatesByRegion(expiredPlateInfoByRegionRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPlateInfoType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getExpiredPlatesByRegion", result);
                }
            }
        }.execute();
    }
    
    public VectorPlateInfoType getExpiredPlatesByRegion(ExpiredPlateInfoByRegionRequest expiredPlateInfoByRegionRequest){
        return getExpiredPlatesByRegion(expiredPlateInfoByRegionRequest, null);
    }
    
    public VectorPlateInfoType getExpiredPlatesByRegion(ExpiredPlateInfoByRegionRequest expiredPlateInfoByRegionRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        soapEnvelope.setOutputSoapObject(expiredPlateInfoByRegionRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        httpTransport.debug=true;
        
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getExpiredPlatesByRegion", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getExpiredPlatesByRegion", soapEnvelope);
            }
            
            Object retObj = soapEnvelope.bodyIn;
            
            
            //Log Network Calls
            //RequestLogTask task=new RequestLogTask("url=" + url +", token="+ expiredPlateInfoByRegionRequest.token, "DEBUG", retObj.toString());
            //task.execute();
            
            
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);
                if (eventHandler != null)
                    eventHandler.Wsdl2CodeFinishedWithException(ex);
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result!=null){
                    return new VectorPlateInfoType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void getExpiredPlatesAsync(ExpiredPlateInfoRequest expiredPlateInfoRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getExpiredPlatesAsync(expiredPlateInfoRequest, null);
    }
    
    public void getExpiredPlatesAsync(final ExpiredPlateInfoRequest expiredPlateInfoRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPlateInfoType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPlateInfoType doInBackground(Void... params) {
                return getExpiredPlates(expiredPlateInfoRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPlateInfoType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getExpiredPlates", result);
                }
            }
        }.execute();
    }
    
    public VectorPlateInfoType getExpiredPlates(ExpiredPlateInfoRequest expiredPlateInfoRequest){
        return getExpiredPlates(expiredPlateInfoRequest, null);
    }
    
    public VectorPlateInfoType getExpiredPlates(ExpiredPlateInfoRequest expiredPlateInfoRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        
        soapEnvelope.setOutputSoapObject(expiredPlateInfoRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getExpiredPlates", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getExpiredPlates", soapEnvelope);
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
                    return new VectorPlateInfoType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
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
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getRegions", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getRegions", soapEnvelope);
            }
            
            Object retObj = soapEnvelope.bodyIn;
            
            //Log Network Calls
            //RequestLogTask task=new RequestLogTask("url=" + url +", token="+ regionRequest.token, "DEBUG", retObj.toString());
            //task.execute();
            
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
            e.printStackTrace();
        }
        return null;
    }
    
    public void getValidPlatesByGroupAsync(PlateInfoByGroupRequest plateInfoByGroupRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getValidPlatesByGroupAsync(plateInfoByGroupRequest, null);
    }
    
    public void getValidPlatesByGroupAsync(final PlateInfoByGroupRequest plateInfoByGroupRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPlateInfoType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPlateInfoType doInBackground(Void... params) {
                return getValidPlatesByGroup(plateInfoByGroupRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPlateInfoType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getValidPlatesByGroup", result);
                }
            }
        }.execute();
    }
    
    public VectorPlateInfoType getValidPlatesByGroup(PlateInfoByGroupRequest plateInfoByGroupRequest){
        return getValidPlatesByGroup(plateInfoByGroupRequest, null);
    }
    
    public VectorPlateInfoType getValidPlatesByGroup(PlateInfoByGroupRequest plateInfoByGroupRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        
        soapEnvelope.setOutputSoapObject(plateInfoByGroupRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getValidPlatesByGroup", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/plateInfo/getValidPlatesByGroup", soapEnvelope);
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
                    return new VectorPlateInfoType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
}
