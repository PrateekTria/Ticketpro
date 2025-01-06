package com.ticketpro.util;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.ticketpro.model.GPSLocation;
import com.ticketpro.parking.service.GPSResultReceiver;
import com.ticketpro.parking.service.GPSResultReceiver.Receiver;
import com.ticketpro.parking.service.GPSService;
import com.ticketpro.parking.service.ServiceHandler;
import com.ticketpro.parking.service.ServiceHandlerImpl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

public class GPSTracker {
	private Context context;
	private Location lastLocation;
	private GPSLocation lastGPSLocation;
	private Logger log = Logger.getLogger("GPSTracker");
	private GPSResultReceiver resultReceiver;
	updateGPSLocationTask task = null;

	public GPSTracker(Context context) {
		this.context = context;
	}

	public void initService(final Receiver receiver) {
		resultReceiver = new GPSResultReceiver(new Handler());
		resultReceiver.setReceiver(new Receiver() {
			@Override
			public void onReceiveResult(Location location, Bundle resultData) {
				updateLocation(location, receiver);
			}

			@Override
			public void onReceiveResult(GPSLocation location, Bundle resultData) {
				if (receiver != null) {
					updateLocation(location, receiver);
				}
			}

			@Override
			public void onTimeout() {
				if (receiver != null) {
					receiver.onTimeout();
				}

			}
		});

		final GPSService gpsService = new GPSService(resultReceiver, context);
		long delaysInMili = 2000;
		if(!TPUtility.isRugbyDevice()){
			 delaysInMili = 3*1000;
		}
		// Apply last location after max interval
		Handler gpsCheckHandler = new Handler();
		gpsCheckHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (true) {
					Location location = gpsService.getLastKnownLocation();

					if (location != null) {
						updateLocation(location, receiver);
					}
					if(!TPUtility.isRugbyDevice()) {
						gpsService.stopListening();

						if (receiver != null) {
							receiver.onReceiveResult(location, new Bundle());
						}
						stopGPSProcess();
					}
				}
			}
		}, delaysInMili );
	}

	public boolean isGPSAvailable() {
		PackageManager pm = context.getPackageManager();
		if (!pm.hasSystemFeature(PackageManager.FEATURE_LOCATION)) {
			return false;
		}

		boolean result = false;
		try {
			LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			result = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
					|| locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception e) {
			return result;
		}

		return result;
	}

	public void updateLocation(Location location, Receiver receiver) {

		if (location == null) {
			return;
		}

		if(!TPUtility.isRugbyDevice()) {
		setLastLocation(location);

		if (receiver != null) {
			receiver.onReceiveResult(location, new Bundle());
		}
		}

		task = new updateGPSLocationTask(location, receiver);
		task.execute();
	}

	public void updateLocation(GPSLocation location, Receiver receiver) {
		if (location == null) {
			return;
		}
		if (receiver != null) {
			receiver.onReceiveResult(location, new Bundle());
		}

	}


	@SuppressLint("StaticFieldLeak")
	public class updateGPSLocationTask extends AsyncTask<Void, Void, GPSLocation> {
		private Location location;
		private Receiver receiver;
		private boolean running = true;

		public updateGPSLocationTask(Location location, Receiver receiver) {
			this.location = location;
			this.receiver = receiver;
		}

		/*@Override
		protected void onCancelled() {
			running = false;
		}*/

		@Override
		protected GPSLocation doInBackground(Void... arg0) {
			return getGPSLocation(location);
		}

		@Override
		protected void onPostExecute(GPSLocation result) {
			setLastGPSLocation(result);

			if (receiver != null) {
				receiver.onReceiveResult(result, new Bundle());
			}
		}

		public GPSLocation getGPSLocation(Location location) {
			boolean isNetworkConnected = false;
			
			if (!isGPSAvailable()) {
				return null;
			}

			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			
			if (networkInfo != null && networkInfo.getState() == State.CONNECTED) {
				isNetworkConnected = true;
			}

			GPSLocation gpsLocation = new GPSLocation();
			gpsLocation.setLocation("");
			gpsLocation.setStreetNumber("");
			gpsLocation.setProviderLocation(location);

			int latitude = 0;
			int longitude = 0;

			try {
				latitude = (int) (location.getLatitude() * 1E6);
				longitude = (int) (location.getLongitude() * 1E6);
				gpsLocation.setLatitude(location.getLatitude() + "");
				gpsLocation.setLongitude(location.getLongitude() + "");
				//log.error("getLatitude"+location.getLatitude() + ""+"\n"+"getLongitude"+location.getLongitude() );

				if (isNetworkConnected) {
					//gpsLocation = getGPSLocationFromService(location.getLatitude(), location.getLongitude());
					//gpsLocation = getGPSLocationFromGoogleService(location.getLatitude(), location.getLongitude());

					if(TPUtility.isRugbyDevice()) {
						gpsLocation = getGPSLocationFromService(location.getLatitude(), location.getLongitude());
						if (gpsLocation != null)
							return gpsLocation;
					}
					if (gpsLocation != null
							&& (!gpsLocation.getLocation().equals("") || !gpsLocation.getStreetNumber().equals(""))) {
						return gpsLocation;
					}

					if (gpsLocation != null) {
						if (gpsLocation.getLocation().equals("")) {
							gpsLocation = getGPSLocationFromService(location.getLatitude(), location.getLongitude());
							if (gpsLocation != null)
								return gpsLocation;
						}
					}
					//gpsLocation = getGPSLocationFromService(location.getLatitude(), location.getLongitude());
					Geocoder gcd = new Geocoder(context, Locale.getDefault());
					List<Address> addresses = null;
					addresses = gcd.getFromLocation(latitude / 1E6, longitude / 1E6, 1);
					//log.error("GPS Location address object"+addresses.size());
					if (addresses != null) {
					//	log.error("GPS Location address size "+addresses.size());
						if (addresses.size() > 0) {
							for (int i = 0; i < addresses.size(); i++) {
								Address address = addresses.get(i);
								String streetNumber = address.getSubThoroughfare();
								String streetName = address.getThoroughfare();
								// Add Street Number
								if (streetNumber != null && !streetNumber.equals("")) {
									gpsLocation.setStreetNumber(streetNumber);
								}

								// Add Street Name
								if (streetName != null && !streetName.equals("")) {
									gpsLocation.setLocation(streetName);
								} else
									gpsLocation.setStreetNumber(address.getAddressLine(0).split(",")[0]);

								// Update GPS location if street name is empty
								if (gpsLocation.getLocation().equals("") && !gpsLocation.getStreetNumber().equals("")) {
									gpsLocation.setLocation(gpsLocation.getStreetNumber());
									gpsLocation.setStreetNumber("");
								}

								break;
							}
						}
						else{
							try {
								gpsLocation = getGPSLocationFromService(location.getLatitude(), location.getLongitude());
							}catch (Exception e){
								log.error(TPUtility.getPrintStackTrace(e));
								log.error("Exception while fetching gps location from service");
							}
						}
					}else if(addresses.size()==0){
							gpsLocation = getGPSLocationFromService(location.getLatitude(), location.getLongitude());
					}
				}
			} catch (Exception e) {
				if (isNetworkConnected) {
					//gpsLocation = getGPSLocationFromGoogleService(location.getLatitude(), location.getLongitude());
					if (gpsLocation == null
							|| (gpsLocation.getLocation().isEmpty() && gpsLocation.getStreetNumber().isEmpty())) {
						gpsLocation = getGPSLocationFromService(location.getLatitude(), location.getLongitude());
					}
				}

				return gpsLocation;
			}

			return gpsLocation;
		}

		public GPSLocation getGPSLocationFromService(double lat, double lon) {
			GPSLocation gpsLocation = new GPSLocation();
			Location location = new Location("GPS");
			location.setLatitude(lat);
			location.setLongitude(lon);
			gpsLocation.setProviderLocation(location);

			gpsLocation.setLocation("");
			gpsLocation.setStreetNumber("");
			gpsLocation.setLatitude(lat + "");
			gpsLocation.setLongitude(lon + "");

			ServiceHandler service = new ServiceHandlerImpl();
			try {
				JSONObject json = service.getGPSLocation(lat, lon);
				if (json != null) {
					String streetNumber = "";

					if (!json.isNull("StreetNumber")) {
						gpsLocation.setStreetNumber(json.getString("StreetNumber"));
						streetNumber = json.getString("StreetNumber");
					}
					if (!json.isNull("Location")) {
						if(!streetNumber.equalsIgnoreCase("")) {
							try {
							String geoCodeApiResult = json.getString("Location");
							geoCodeApiResult = geoCodeApiResult.replace(streetNumber, "");
							geoCodeApiResult = geoCodeApiResult.substring(0, geoCodeApiResult.indexOf(",")).replaceAll("[^a-zA-Z0-9\\s+]", "");
							gpsLocation.setLocation(geoCodeApiResult);
						} catch (Exception e){
								e.printStackTrace();
								log.error(TPUtility.getPrintStackTrace(e));
								gpsLocation.setLocation(json.getString("Location"));
							}
						} else {
							gpsLocation.setLocation(json.getString("Location"));
							log.error("Street number not fetched from API");
						}
					}

					// Update GPS location if street name is empty
					if (gpsLocation.getLocation().equals("") && !gpsLocation.getStreetNumber().equals("")) {
						gpsLocation.setLocation(gpsLocation.getStreetNumber());
						gpsLocation.setStreetNumber("");
					}
				}
			} catch (Exception e) {
				log.error(TPUtility.getPrintStackTrace(e));
			}

			return gpsLocation;
		}

		private GPSLocation getGPSLocationFromGoogleService(double lat, double lon) {
			GPSLocation gpsLocation = new GPSLocation();
			Location location = new Location("GPS");
			location.setLatitude(lat);
			location.setLongitude(lon);
			gpsLocation.setProviderLocation(location);

			gpsLocation.setLocation("");
			gpsLocation.setStreetNumber("");
			gpsLocation.setLatitude(lat + "");
			gpsLocation.setLongitude(lon + "");
			try {
				gpsLocation = getGPSLocationFromService(location.getLatitude(), location.getLongitude());
				if (gpsLocation != null
						&& (!gpsLocation.getLocation().equals("") || !gpsLocation.getStreetNumber().equals(""))) {
					return gpsLocation;
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			List<Address> addresses = ReverseGeocode.getFromLocation(lat, lon, 3);
			if (addresses != null) {
				if (addresses.size() > 0) {
					for (int i = 0; i < addresses.size(); i++) {
						Address address = addresses.get(i);
						String streetNumber = address.getSubThoroughfare();
						String streetName = address.getThoroughfare();

						// Add Street Number
						if (streetNumber != null && !streetNumber.equals("")) {
							gpsLocation.setStreetNumber(streetNumber);
						}

						// Add Street Name
						if (streetName != null && !streetName.equals("")) {
							gpsLocation.setLocation(streetName.trim());
						}

						// Update GPS location if street name is empty
						if (gpsLocation.getLocation().equals("") && !gpsLocation.getStreetNumber().equals("")) {
							gpsLocation.setLocation(gpsLocation.getStreetNumber());
							gpsLocation.setStreetNumber("");
						}

						break;
					}
				}else{
					try{
					if(addresses.size()==0){
						gpsLocation = getGPSLocationFromService(location.getLatitude(), location.getLongitude());
					}
				}catch (Exception e){
						log.error(TPUtility.getPrintStackTrace(e));
					e.printStackTrace();
				}
				}
			}
			try {
				if(addresses.size()==0){
						gpsLocation = getGPSLocationFromService(location.getLatitude(), location.getLongitude());
				}
			}catch (Exception e){
				e.printStackTrace();
			}

			return gpsLocation;
		}
	}

	public Location getLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
	}

	public GPSLocation getLastGPSLocation() {
		return lastGPSLocation;
	}

	public void setLastGPSLocation(GPSLocation lastGPSLocation) {
		this.lastGPSLocation = lastGPSLocation;
	}

	public void stopGPSProcess(){
		try{
			if(task!=null) {
				task.cancel(true);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}