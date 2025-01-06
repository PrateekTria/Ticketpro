package com.ticketpro.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.ticketpro.model.Feature;
import com.ticketpro.parking.service.LocationUpdatesService;

/**
 * Created by Rohit Kumar on 27-09-2019.
 */

public class MyLocationReceiver extends BroadcastReceiver {
    private Context mContext;

    public MyLocationReceiver() {
    }

    public MyLocationReceiver(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("onReceive Method called");
        mContext = context;
        Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);

        try {
            if (Feature.isFeatureAllowed(Feature.FIELD_TRACKER)) {
                FirebaseDBUpdater firebaseDBUpdater = new FirebaseDBUpdater(mContext);
                firebaseDBUpdater.updateFB(location, "", null);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        /* if (intent.getExtras() != null) {
            Bundle mBundle = intent.getBundleExtra("BUNDLE");
            assert mBundle != null;
            lat = mBundle.getDouble("Latitude");
            lng = mBundle.getDouble("Longitude");
        }
        try {
            if (Feature.isFeatureAllowed(Feature.FIELD_TRACKER)) {
                FirebaseDBUpdater firebaseDBUpdater = new FirebaseDBUpdater(mContext);
                firebaseDBUpdater.updateFB(lat, lng, "");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }*/
    }
}
