package com.ticketpro.parking.service;

/*
import java.util.Date;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.ticketpro.parking.activity.TPApplication;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

public class GpsTrackingService extends Service implements LocationListener, 
	GooglePlayServicesClient.ConnectionCallbacks, 
	GooglePlayServicesClient.OnConnectionFailedListener {

	private LocationRequest mLocationRequest;
	private LocationClient mLocationClient;
	
	public GpsTrackingService() {

	}

	@Override
	public void onCreate() {
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setInterval(60 * 1000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setFastestInterval(20 * 1000);
		mLocationClient = new LocationClient(getApplicationContext(), this, this);
		mLocationClient.connect();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		//int start = Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
			//TODO - Handle connection failure
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		if(mLocationClient!=null){
			mLocationClient.requestLocationUpdates(mLocationRequest, this);
		}
	}

	@Override
	public void onDisconnected() {
	
	}

	@Override
	public void onLocationChanged(Location location) {
		//double latitude = location.getLatitude();
		//double longitude = location.getLongitude();
		
		try{
			TPApplication TPApp = TPApplication.getInstance();
			if(TPApp!=null && location!=null){
				TPApp.setGpsLocation(location);
				TPApp.setLastGPSTime(new Date());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private boolean isServicesConnected() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(GpsTrackingService.this);
		if (ConnectionResult.SUCCESS == resultCode) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onDestroy() {
		if(mLocationClient!=null){
			mLocationClient.removeLocationUpdates(this);
		}
		
		super.onDestroy();
	}
}*/
