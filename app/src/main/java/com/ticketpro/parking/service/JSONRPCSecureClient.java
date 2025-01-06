package com.ticketpro.parking.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.alexd.jsonrpc.JSONRPCClient;
import org.alexd.jsonrpc.JSONRPCException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPHttpClient;

public class JSONRPCSecureClient extends JSONRPCClient {

	private HttpClient httpClient;
	private String serviceUri;
	
	public JSONRPCSecureClient(String uri) {
		httpClient = new TPHttpClient();
		serviceUri = uri;
	}

	@Override
	protected JSONObject doJSONRequest(JSONObject jsonRequest) throws JSONRPCException {
		Date startTime=new Date();
		
		if(!TPApplication.getInstance().isServiceAvailable){
			throw new JSONRPCException("Network Error");
		}
		
		HttpPost request = new HttpPost(serviceUri);
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params,TPApplication.getInstance().transactionTimeout);
		HttpConnectionParams.setSoTimeout(params, TPApplication.getInstance().transactionTimeout);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(params, true);
        
        request.setParams(params);

        HttpEntity entity;
        String serviceMethod;
		try {
			serviceMethod=jsonRequest.getString("method");
			jsonRequest.put("id", TPConstant.SECRET_KEY);
			if (encoding.length() > 0) {
				entity = new JSONEntity(jsonRequest, encoding);
			} else {
				entity = new JSONEntity(jsonRequest);
			}
		} catch (UnsupportedEncodingException e1) {
			throw new JSONRPCException("Unsupported encoding", e1);
		} catch (Exception e) {
			throw new JSONRPCException("Error parsing JSON response", e);
		}
		
		request.setEntity(entity);
		try{
			HttpResponse response = httpClient.execute(request);
			String responseString = EntityUtils.toString(response.getEntity());
			
			JSONObject jsonResponse = new JSONObject(responseString);
			if(jsonResponse.has("error")) {
				Object jsonError = jsonResponse.get("error");
				logNetworkCall(serviceMethod, startTime, TPApplication.getInstance().lastGPSLocation, jsonResponse.getString("error"));
				
				if (!jsonError.equals(null)){
					throw new JSONRPCException(jsonResponse.get("error"));
				}
				
				return jsonResponse;
			}else{
				logNetworkCall(serviceMethod, startTime, TPApplication.getInstance().lastGPSLocation, "Success");
				return jsonResponse;
			}
		
		}catch (ClientProtocolException e) {
			logNetworkCall(serviceMethod, startTime, TPApplication.getInstance().lastGPSLocation, "IO error "+e.getMessage());
			throw new JSONRPCException("HTTP error "+e.getMessage(), e);
		
		}catch (IOException e) {
			logNetworkCall(serviceMethod, startTime, TPApplication.getInstance().lastGPSLocation, "IO error "+e.getMessage());
			throw new JSONRPCException("IO error "+e.getMessage(), e);
		
		}catch (JSONException e) {
			logNetworkCall(serviceMethod, startTime, TPApplication.getInstance().lastGPSLocation, "Invalid JSON response "+e.getMessage());
			throw new JSONRPCException("Invalid JSON response "+e.getMessage(), e);
		
		}catch (Exception e) {
			logNetworkCall(serviceMethod, startTime, TPApplication.getInstance().lastGPSLocation, "Error parsing JSON response "+e.getMessage());
			throw new JSONRPCException("Error parsing JSON response "+e.getMessage(), e);
		}
	}
	
	private void logNetworkCall(String process, Date startTime, String location, String status){
		//Disable network as not required
		//CSVUtility.writeNetworkLogCSV(process, startTime, new Date(), location, status);
	}
}


class JSONEntity extends StringEntity {
	public JSONEntity(JSONObject jsonObject)
			throws UnsupportedEncodingException {

		super(jsonObject.toString());
	}

	public JSONEntity(JSONObject jsonObject, String encoding)
			throws UnsupportedEncodingException {

		super(jsonObject.toString(), encoding);
		setContentEncoding(encoding);
	}

	@Override
	public Header getContentType() {
		return new BasicHeader(HTTP.CONTENT_TYPE, "application/json");
	}
}
