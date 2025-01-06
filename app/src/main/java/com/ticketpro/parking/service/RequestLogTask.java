package com.ticketpro.parking.service;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.ticketpro.model.MobileNowLog;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.api.WriteTicketNetworkCalls;
import com.ticketpro.util.TPUtility;

import android.os.AsyncTask;

public class RequestLogTask extends AsyncTask<String, Void, String> {
	private String params;
	private String response;
	private String requestMode;
	private Logger log = Logger.getLogger("RequestLogTask");
	private TPApplication TPApp = TPApplication.getInstance();
	private ServiceHandler service;

	public RequestLogTask(String params, String requestMode, String response) {
		this.requestMode = requestMode;
		this.response = response;
		this.params = params;

		this.service = new ServiceHandlerImpl();
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			MobileNowLog log = new MobileNowLog();
			log.setCustId(TPApp.custId);
			log.setDeviceId(TPApp.deviceId);
			log.setUserId(TPApp.userId);
			log.setRequestDate(new Date());
			log.setRequestParams(this.params);
			log.setServiceMode(this.requestMode);
			log.setResponseText(this.response);

			ArrayList<MobileNowLog> logs = new ArrayList<MobileNowLog>();
			logs.add(log);
			WriteTicketNetworkCalls.sendMobileNogLogs(logs);
			/*service.sendMobileNowLog(logs);*/

		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}

		return response;
	}

	@Override
	protected void onPostExecute(String response) {
		if (isCancelled()) {
			response = null;
		}
	}
}