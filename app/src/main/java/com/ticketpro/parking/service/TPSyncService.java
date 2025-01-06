package com.ticketpro.parking.service;

import android.accounts.Account;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TPSyncService extends Service {
	private static TPSyncAdapter sSyncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();
    
    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new TPSyncAdapter(getApplicationContext(), true);
            }
        }
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		return sSyncAdapter.getSyncAdapterBinder();
	}
	
    public static Account getAccount() {
        return new Account(TPSyncAdapter.ACCOUNT, TPSyncAdapter.ACCOUNT_TYPE);
    }
}
