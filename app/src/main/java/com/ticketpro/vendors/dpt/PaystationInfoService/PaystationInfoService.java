package com.ticketpro.vendors.dpt.PaystationInfoService;

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

import com.ticketpro.util.FakeX509TrustManager;
import com.ticketpro.vendors.dpt.PaystationInfoService.WS_Enums.SoapProtocolVersion;

public class PaystationInfoService {
    
    public String NAMESPACE ="http://ws.digitalpaytech.com/paystationInfo";
    public String url="https://developer.digitalpaytech.com/services/PaystationInfoService?wsdl";
    public int timeOut = 5000;
    public IWsdl2CodeEvents eventHandler;
    public SoapProtocolVersion soapVersion;
    public Element[] headerElements;
    public Logger log=Logger.getLogger("PlateInfoService");
    
    public PaystationInfoService(){
    	FakeX509TrustManager.allowAllSSL();
    }
    
    public PaystationInfoService(IWsdl2CodeEvents eventHandler)
    {
        this.eventHandler = eventHandler;
        FakeX509TrustManager.allowAllSSL();
    }
    
    
    public PaystationInfoService(IWsdl2CodeEvents eventHandler,String url)
    {
        this.eventHandler = eventHandler;
        this.url = url;
    }
    public PaystationInfoService(IWsdl2CodeEvents eventHandler,String url,int timeOutInSeconds)
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
    
    public void getPaystationEventsByGroupAsync(PaystationEventsByGroupRequest paystationEventsByGroupRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getPaystationEventsByGroupAsync(paystationEventsByGroupRequest, null);
    }
    
    public void getPaystationEventsByGroupAsync(final PaystationEventsByGroupRequest paystationEventsByGroupRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPaystationEventsType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPaystationEventsType doInBackground(Void... params) {
                return getPaystationEventsByGroup(paystationEventsByGroupRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPaystationEventsType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getPaystationEventsByGroup", result);
                }
            }
        }.execute();
    }
    
    public VectorPaystationEventsType getPaystationEventsByGroup(PaystationEventsByGroupRequest paystationEventsByGroupRequest){
        return getPaystationEventsByGroup(paystationEventsByGroupRequest, null);
    }
    
    public VectorPaystationEventsType getPaystationEventsByGroup(PaystationEventsByGroupRequest paystationEventsByGroupRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        soapEnvelope.setOutputSoapObject(paystationEventsByGroupRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystationEventsByGroup", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystationEventsByGroup", soapEnvelope);
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
                    return new VectorPaystationEventsType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void getPaystationStatusByGroupAsync(PaystationStatusByGroupRequest paystationStatusByGroupRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getPaystationStatusByGroupAsync(paystationStatusByGroupRequest, null);
    }
    
    public void getPaystationStatusByGroupAsync(final PaystationStatusByGroupRequest paystationStatusByGroupRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPaystationStatusType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPaystationStatusType doInBackground(Void... params) {
                return getPaystationStatusByGroup(paystationStatusByGroupRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPaystationStatusType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getPaystationStatusByGroup", result);
                }
            }
        }.execute();
    }
    
    public VectorPaystationStatusType getPaystationStatusByGroup(PaystationStatusByGroupRequest paystationStatusByGroupRequest){
        return getPaystationStatusByGroup(paystationStatusByGroupRequest, null);
    }
    
    public VectorPaystationStatusType getPaystationStatusByGroup(PaystationStatusByGroupRequest paystationStatusByGroupRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        soapEnvelope.setOutputSoapObject(paystationStatusByGroupRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystationStatusByGroup", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystationStatusByGroup", soapEnvelope);
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
                    return new VectorPaystationStatusType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void getPaystationsAsync(PaystationRequest paystationRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getPaystationsAsync(paystationRequest, null);
    }
    
    public void getPaystationsAsync(final PaystationRequest paystationRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPaystationType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPaystationType doInBackground(Void... params) {
                return getPaystations(paystationRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPaystationType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getPaystations", result);
                }
            }
        }.execute();
    }
    
    public VectorPaystationType getPaystations(PaystationRequest paystationRequest){
        return getPaystations(paystationRequest, null);
    }
    
    public VectorPaystationType getPaystations(PaystationRequest paystationRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        soapEnvelope.setOutputSoapObject(paystationRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystations", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystations", soapEnvelope);
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
                    return new VectorPaystationType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void getPaystationStatusAsync(PaystationStatusRequest paystationStatusRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getPaystationStatusAsync(paystationStatusRequest, null);
    }
    
    public void getPaystationStatusAsync(final PaystationStatusRequest paystationStatusRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPaystationStatusType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPaystationStatusType doInBackground(Void... params) {
                return getPaystationStatus(paystationStatusRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPaystationStatusType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getPaystationStatus", result);
                }
            }
        }.execute();
    }
    
    public VectorPaystationStatusType getPaystationStatus(PaystationStatusRequest paystationStatusRequest){
        return getPaystationStatus(paystationStatusRequest, null);
    }
    
    public VectorPaystationStatusType getPaystationStatus(PaystationStatusRequest paystationStatusRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        soapEnvelope.setOutputSoapObject(paystationStatusRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystationStatus", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystationStatus", soapEnvelope);
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
                    return new VectorPaystationStatusType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void getPaystationStatusBySerialAsync(PaystationStatusBySerialRequest paystationStatusBySerialRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getPaystationStatusBySerialAsync(paystationStatusBySerialRequest, null);
    }
    
    public void getPaystationStatusBySerialAsync(final PaystationStatusBySerialRequest paystationStatusBySerialRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPaystationStatusType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPaystationStatusType doInBackground(Void... params) {
                return getPaystationStatusBySerial(paystationStatusBySerialRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPaystationStatusType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getPaystationStatusBySerial", result);
                }
            }
        }.execute();
    }
    
    public VectorPaystationStatusType getPaystationStatusBySerial(PaystationStatusBySerialRequest paystationStatusBySerialRequest){
        return getPaystationStatusBySerial(paystationStatusBySerialRequest, null);
    }
    
    public VectorPaystationStatusType getPaystationStatusBySerial(PaystationStatusBySerialRequest paystationStatusBySerialRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        soapEnvelope.setOutputSoapObject(paystationStatusBySerialRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystationStatusBySerial", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystationStatusBySerial", soapEnvelope);
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
                    return new VectorPaystationStatusType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void getPaystationEventsByRegionAsync(PaystationEventsByRegionRequest paystationEventsByRegionRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getPaystationEventsByRegionAsync(paystationEventsByRegionRequest, null);
    }
    
    public void getPaystationEventsByRegionAsync(final PaystationEventsByRegionRequest paystationEventsByRegionRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPaystationEventsType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPaystationEventsType doInBackground(Void... params) {
                return getPaystationEventsByRegion(paystationEventsByRegionRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPaystationEventsType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getPaystationEventsByRegion", result);
                }
            }
        }.execute();
    }
    
    public VectorPaystationEventsType getPaystationEventsByRegion(PaystationEventsByRegionRequest paystationEventsByRegionRequest){
        return getPaystationEventsByRegion(paystationEventsByRegionRequest, null);
    }
    
    public VectorPaystationEventsType getPaystationEventsByRegion(PaystationEventsByRegionRequest paystationEventsByRegionRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        soapEnvelope.setOutputSoapObject(paystationEventsByRegionRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystationEventsByRegion", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystationEventsByRegion", soapEnvelope);
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
                    return new VectorPaystationEventsType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void getGroupsAsync(PaystationGroupsRequest paystationGroupsRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getGroupsAsync(paystationGroupsRequest, null);
    }
    
    public void getGroupsAsync(final PaystationGroupsRequest paystationGroupsRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPaystationGroupsType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPaystationGroupsType doInBackground(Void... params) {
                return getGroups(paystationGroupsRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPaystationGroupsType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getGroups", result);
                }
            }
        }.execute();
    }
    
    public VectorPaystationGroupsType getGroups(PaystationGroupsRequest paystationGroupsRequest){
        return getGroups(paystationGroupsRequest, null);
    }
    
    public VectorPaystationGroupsType getGroups(PaystationGroupsRequest paystationGroupsRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        soapEnvelope.setOutputSoapObject(paystationGroupsRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getGroups", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getGroups", soapEnvelope);
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
                    return new VectorPaystationGroupsType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void getPaystationEventsAsync(PaystationEventsRequest paystationEventsRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getPaystationEventsAsync(paystationEventsRequest, null);
    }
    
    public void getPaystationEventsAsync(final PaystationEventsRequest paystationEventsRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPaystationEventsType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPaystationEventsType doInBackground(Void... params) {
                return getPaystationEvents(paystationEventsRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPaystationEventsType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getPaystationEvents", result);
                }
            }
        }.execute();
    }
    
    public VectorPaystationEventsType getPaystationEvents(PaystationEventsRequest paystationEventsRequest){
        return getPaystationEvents(paystationEventsRequest, null);
    }
    
    public VectorPaystationEventsType getPaystationEvents(PaystationEventsRequest paystationEventsRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        soapEnvelope.setOutputSoapObject(paystationEventsRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystationEvents", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystationEvents", soapEnvelope);
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
                    return new VectorPaystationEventsType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void getPaystationStatusByRegionAsync(PaystationStatusByRegionRequest paystationStatusByRegionRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getPaystationStatusByRegionAsync(paystationStatusByRegionRequest, null);
    }
    
    public void getPaystationStatusByRegionAsync(final PaystationStatusByRegionRequest paystationStatusByRegionRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPaystationStatusType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPaystationStatusType doInBackground(Void... params) {
                return getPaystationStatusByRegion(paystationStatusByRegionRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPaystationStatusType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getPaystationStatusByRegion", result);
                }
            }
        }.execute();
    }
    
    public VectorPaystationStatusType getPaystationStatusByRegion(PaystationStatusByRegionRequest paystationStatusByRegionRequest){
        return getPaystationStatusByRegion(paystationStatusByRegionRequest, null);
    }
    
    public VectorPaystationStatusType getPaystationStatusByRegion(PaystationStatusByRegionRequest paystationStatusByRegionRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        soapEnvelope.setOutputSoapObject(paystationStatusByRegionRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystationStatusByRegion", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystationStatusByRegion", soapEnvelope);
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
                    return new VectorPaystationStatusType(result);
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void getPaystationEventsBySerialAsync(PaystationEventsBySerialRequest paystationEventsBySerialRequest) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        getPaystationEventsBySerialAsync(paystationEventsBySerialRequest, null);
    }
    
    public void getPaystationEventsBySerialAsync(final PaystationEventsBySerialRequest paystationEventsBySerialRequest,final List<HeaderProperty> headers) throws Exception{
        
        new AsyncTask<Void, Void, VectorPaystationEventsType>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected VectorPaystationEventsType doInBackground(Void... params) {
                return getPaystationEventsBySerial(paystationEventsBySerialRequest, headers);
            }
            @Override
            protected void onPostExecute(VectorPaystationEventsType result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("getPaystationEventsBySerial", result);
                }
            }
        }.execute();
    }
    
    public VectorPaystationEventsType getPaystationEventsBySerial(PaystationEventsBySerialRequest paystationEventsBySerialRequest){
        return getPaystationEventsBySerial(paystationEventsBySerialRequest, null);
    }
    
    public VectorPaystationEventsType getPaystationEventsBySerial(PaystationEventsBySerialRequest paystationEventsBySerialRequest,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        soapEnvelope.headerOut = headerElements;
        soapEnvelope.setOutputSoapObject(paystationEventsBySerialRequest.getSoapObject());
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystationEventsBySerial", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws.digitalpaytech.com/paystationInfo/getPaystationEventsBySerial", soapEnvelope);
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
                    return new VectorPaystationEventsType(result);
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
