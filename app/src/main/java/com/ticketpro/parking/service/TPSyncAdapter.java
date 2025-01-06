package com.ticketpro.parking.service;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.proxy.ProxyImpl;
import com.ticketpro.util.TPUtility;

import org.apache.log4j.Logger;

public class TPSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String ACCOUNT = "ticketpro.parking";
    public static final String ACCOUNT_TYPE = "ticketpro.parking.sync.adapter";
    public static final String AUTHORITY = "com.ticketpro.parking.datasync.provider";

    private final Logger log = Logger.getLogger("TPSyncAdapter");
    private ContentResolver mContentResolver;

    public TPSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public TPSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        ProxyImpl proxy = new ProxyImpl();
        try {
            boolean databaseSync = extras.getBoolean("DatabaseSync", false);
            if (databaseSync) {
                boolean fullSync = extras.getBoolean("FullSync", false);
                proxy.syncDatabase(fullSync, getContext(),null);
            } else if (!TPApplication.getInstance().disableSync) {
                proxy.uploadAllChanges(getContext(), false);
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }
}