package com.ticketpro.parking.bl;

import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.proxy.Proxy;

public interface BLProcessor {
	void setProxy(Proxy proxy);
	void setLogger(String classInstance);
	Proxy getProxy();
	void setTPApplication(TPApplication TPApp);
	TPApplication getTPApplication();
}
