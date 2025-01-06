package com.ticketpro.parking.service;

import org.apache.log4j.Logger;

import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

public class GPSService {
	private Logger log = Logger.getLogger("GPSService");
	private LocationManager mLocationManager;
	private LocationUpdaterListener mLocationListener;
	private GPSResultReceiver resultReceiver;
	private boolean isListening = false;

	public GPSService() {

	}

	public GPSService(GPSResultReceiver receiver, Context context) {
		resultReceiver = receiver;

		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		mLocationListener = new LocationUpdaterListener();

		startListening();
	}

	private void startListening() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);
		criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
		criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

		setListening(true);
		
		mLocationManager.requestSingleUpdate(criteria, mLocationListener, null);
		
		Handler timeoutHandler = new Handler();
		timeoutHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				stopListening();
				
				try {
					resultReceiver.onTimeout();
				} catch (Exception e) {
					log.error(TPUtility.getPrintStackTrace(e));
				}
			}
		}, TPConstant.GPS_STOP_DELAY);
	}

	public Location getLastKnownLocation() {
		Location location;

		if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
			location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null && location.hasAccuracy()) {
				return location;
			}
		}

		if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
			location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location != null && location.hasAccuracy()) {
				return location;
			}
		}

		return null;
	}

	public void stopListening() {
		setListening(false);
		
		if (mLocationManager != null) {
			mLocationManager.removeUpdates(mLocationListener);
		}
	}

	public class LocationUpdaterListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			try {
				//resultReceiver.onReceiveResult(location, new Bundle());
			} catch (Exception e) {
				log.error(TPUtility.getPrintStackTrace(e));
			} finally {
				stopListening();
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			stopListening();
		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}
	}

	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > (2 * 60 * 1000);
		boolean isSignificantlyOlder = timeDelta < -(2 * 60 * 1000);
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}

		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}

		return provider1.equals(provider2);
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public LocationManager getmLocationManager() {
		return mLocationManager;
	}

	public void setmLocationManager(LocationManager mLocationManager) {
		this.mLocationManager = mLocationManager;
	}

	public LocationUpdaterListener getmLocationListener() {
		return mLocationListener;
	}

	public void setmLocationListener(LocationUpdaterListener mLocationListener) {
		this.mLocationListener = mLocationListener;
	}

	public GPSResultReceiver getResultReceiver() {
		return resultReceiver;
	}

	public void setResultReceiver(GPSResultReceiver resultReceiver) {
		this.resultReceiver = resultReceiver;
	}

	public boolean isListening() {
		return isListening;
	}

	public void setListening(boolean isListening) {
		this.isListening = isListening;
	}
}
