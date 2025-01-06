package com.ticketpro.vendors.progressive;

import java.util.List;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.ticketpro.util.FakeX509TrustManager;
import com.ticketpro.vendors.progressive.WS_Enums.SoapProtocolVersion;

import android.os.AsyncTask;

public class Service1 {
	//Testing plate - 8D35673
	
	public String NAMESPACE = "http://tempuri.org/";
	public String url = "https://parkingpermits.menlopark.org/parkingservice/service1.asmx";
	//public String url ="https://parkingpermits.menlopark.org/parkingservice/service1.asmx?wsdl";
	public int timeOut = 30 * 1000;
	public IWsdl2CodeEvents eventHandler;
	public SoapProtocolVersion soapVersion;

	public Service1() {
	}

	public Service1(IWsdl2CodeEvents eventHandler) {
		this.eventHandler = eventHandler;
	}

	public Service1(IWsdl2CodeEvents eventHandler, String url) {
		this.eventHandler = eventHandler;
		this.url = url;
	}

	public Service1(IWsdl2CodeEvents eventHandler, String url, int timeOutInSeconds) {
		this.eventHandler = eventHandler;
		this.url = url;
		this.setTimeOut(timeOutInSeconds);
	}

	public void setTimeOut(int seconds) {
		this.timeOut = seconds * 1000;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void HelloWorldAsync() throws Exception {
		if (this.eventHandler == null)
			throw new Exception("Async Methods Requires IWsdl2CodeEvents");
		HelloWorldAsync(null);
	}

	public void HelloWorldAsync(final List<HeaderProperty> headers) throws Exception {

		new AsyncTask<Void, Void, String>() {
			@Override
			protected void onPreExecute() {
				eventHandler.Wsdl2CodeStartedRequest();
			};

			@Override
			protected String doInBackground(Void... params) {
				return HelloWorld(headers);
			}

			@Override
			protected void onPostExecute(String result) {
				eventHandler.Wsdl2CodeEndedRequest();
				if (result != null) {
					eventHandler.Wsdl2CodeFinished("HelloWorld", result);
				}
			}
		}.execute();
	}

	public String HelloWorld() {
		return HelloWorld(null);
	}

	public String HelloWorld(List<HeaderProperty> headers) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = new SoapObject("http://tempuri.org/", "HelloWorld");
		soapEnvelope.setOutputSoapObject(soapReq);
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		try {
			if (headers != null) {
				httpTransport.call("http://tempuri.org/HelloWorld", soapEnvelope, headers);
			} else {
				httpTransport.call("http://tempuri.org/HelloWorld", soapEnvelope);
			}
			Object retObj = soapEnvelope.bodyIn;
			if (retObj instanceof SoapFault) {
				SoapFault fault = (SoapFault) retObj;
				Exception ex = new Exception(fault.faultstring);
				if (eventHandler != null)
					eventHandler.Wsdl2CodeFinishedWithException(ex);
			} else {
				SoapObject result = (SoapObject) retObj;
				if (result.getPropertyCount() > 0) {
					Object obj = result.getProperty(0);
					if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
						SoapPrimitive j = (SoapPrimitive) obj;
						String resultVariable = j.toString();
						return resultVariable;
					} else if (obj != null && obj instanceof String) {
						String resultVariable = (String) obj;
						return resultVariable;
					}
				}
			}
		} catch (Exception e) {
			if (eventHandler != null)
				eventHandler.Wsdl2CodeFinishedWithException(e);
			e.printStackTrace();
		}
		return "";
	}

	public void ValidPermitAsync(int permitID, String licensePlate) throws Exception {
		if (this.eventHandler == null)
			throw new Exception("Async Methods Requires IWsdl2CodeEvents");
		ValidPermitAsync(permitID, licensePlate, null);
	}

	public void ValidPermitAsync(final int permitID, final String licensePlate, final List<HeaderProperty> headers)
			throws Exception {

		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				eventHandler.Wsdl2CodeStartedRequest();
			};

			@Override
			protected Boolean doInBackground(Void... params) {
				return ValidPermit(permitID, licensePlate, headers);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				eventHandler.Wsdl2CodeEndedRequest();
				if (result != null) {
					eventHandler.Wsdl2CodeFinished("ValidPermit", result);
				}
			}
		}.execute();
	}

	public boolean ValidPermit(int permitID, String licensePlate) {
		return ValidPermit(permitID, licensePlate, null);
	}

	public boolean ValidPermit(int permitID, String licensePlate, List<HeaderProperty> headers) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;

		SoapObject soapReq = new SoapObject("http://tempuri.org/", "ValidPermit");
		soapReq.addProperty("PermitID", permitID);
		soapReq.addProperty("LicensePlate", licensePlate);
		soapEnvelope.setOutputSoapObject(soapReq);
		//FakeX509TrustManager.allowAllSSL();

		//System.setProperty("http.keepAlive", "false");
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		FakeX509TrustManager.allowAllSSL();

		try {
			if (headers != null) {
				httpTransport.call("http://tempuri.org/ValidPermit", soapEnvelope, headers);
			} else {
				httpTransport.call("http://tempuri.org/ValidPermit", soapEnvelope);
			}
			Object retObj = soapEnvelope.bodyIn;
			if (retObj instanceof SoapFault) {
				SoapFault fault = (SoapFault) retObj;
				Exception ex = new Exception(fault.faultstring);
				if (eventHandler != null)
					eventHandler.Wsdl2CodeFinishedWithException(ex);
			} else {
				SoapObject result = (SoapObject) retObj;
				if (result.getPropertyCount() > 0) {
					Object obj = result.getProperty(0);
					if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
						SoapPrimitive j = (SoapPrimitive) obj;
						boolean resultVariable = Boolean.parseBoolean(j.toString());
						return resultVariable;
					} else if (obj != null && obj instanceof Boolean) {
						boolean resultVariable = (Boolean) obj;
						return resultVariable;
					}
				}
			}
		} catch (Exception e) {
			if (eventHandler != null)
				eventHandler.Wsdl2CodeFinishedWithException(e);
			e.printStackTrace();

		}
		return false;
	}

}
