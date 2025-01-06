package com.ticketpro.parking.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TPAuthenticatorService extends Service {
	private TPSyncAuthenticator mAuthenticator;
    
    @Override
    public void onCreate() {
        mAuthenticator = new TPSyncAuthenticator(this);
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
