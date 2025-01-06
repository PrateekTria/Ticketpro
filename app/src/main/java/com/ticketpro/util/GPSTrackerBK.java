package com.ticketpro.util;

import java.util.List;

import org.json.JSONObject;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import androidx.core.app.ActivityCompat;

import com.ticketpro.model.GPSLocation;
import com.ticketpro.parking.service.ServiceHandler;
import com.ticketpro.parking.service.ServiceHandlerImpl;

public class GPSTrackerBK extends Service implements LocationListener {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    //if GPS initially not activated, start and stop forcefully
    boolean isGPSForcefully = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude;  // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTrackerBK(Context context) {
        this.mContext = context;
        Location location = getLocation();
        if (location != null) {
            stopUsingGPS();
        }
    }

    public Location getLocation() {
        try {

            // Check for System Feature
            PackageManager pm = mContext.getPackageManager();
            if (!pm.hasSystemFeature(PackageManager.FEATURE_LOCATION))
                return location;

            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //if not enabled forcefully start
            if (!isGPSEnabled) {
                isGPSForcefully = true;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return location;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, GPSTrackerBK.this);
            }

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;

                // First get location from GPS if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return location;
                        }
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                return location;
                            }
                        }
                    }
                }

                //  get location from Network Provider
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return location;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            
                            return location;
                        }
                    }
                }
                
                
            }
 
        } catch (Exception e) {
        	e.printStackTrace();
        }
 
        return location;
    }
 
    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTrackerBK.this);
        }
    }
 
    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
 
        // return latitude
        return latitude;
    }
 
    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
 
        // return longitude
        return longitude;
    }
 
    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
 
    @Override
    public void onLocationChanged(Location location) {
    	this.location=location;
    	if(this.location!=null){
    		this.latitude=location.getLatitude();
    		this.longitude=location.getLongitude();
    	}
    }
 
    @Override
    public void onProviderDisabled(String provider) {
    }
 
    @Override
    public void onProviderEnabled(String provider) {
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
 
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
 
    
    public boolean isGPSAvailable(){
    	
    	// Check for System Feature
    	PackageManager pm = mContext.getPackageManager();
	    if (!pm.hasSystemFeature(PackageManager.FEATURE_LOCATION))
	        return false;
	    
    	boolean result=false;
    	try{
    		locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
	    	result = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    	}catch(Exception e){
    		return result;
    	}
    	
    	return result;
    }
    

    public GPSLocation getLocationDetails(){
    	if(!isGPSAvailable())
    		return null;
    	
    	GPSLocation gpsLocation=new GPSLocation();
    	gpsLocation.setLocation("");
    	gpsLocation.setStreetNumber("");
    	int latitude=0;
    	int longitude=0;
    	
    	//GeoPoint point = new GeoPoint((int) (getLatitude() * 1E6), (int) (getLongitude() * 1E6));
    	try {
            latitude=(int) (getLatitude() * 1E6);
    		longitude=(int) (getLongitude() * 1E6);
    		Geocoder gcd = new Geocoder(this.mContext);
	    	gpsLocation.setLatitude(getLatitude()+"");
    		gpsLocation.setLongitude(getLongitude()+"");
    		
    		List<Address> addresses = null;
            addresses = gcd.getFromLocation(latitude/1E6,longitude/1E6, 1);
            if (addresses != null) {
                if (addresses.size() > 0) {
                	for (int i = 0; i < addresses.size(); i++){
                		
                		Address address=addresses.get(i);
                		String streetNumber=address.getSubThoroughfare();
                		String streetName=address.getThoroughfare();
                		
                		//Add Street Number
                		if(streetNumber!=null && !streetNumber.equals("")){
                			gpsLocation.setStreetNumber(streetNumber);
                		}
                		
                		// Add Street Name
                		if(streetName!=null && !streetName.equals("")){
                			gpsLocation.setLocation(streetName);
                		}
                		
                		gpsLocation.setLatitude(address.getLatitude()+"");
                		gpsLocation.setLongitude(address.getLongitude()+"");
                		
                		break;
                	}
                }
            } 
           
        } catch (Exception e) {
        	e.printStackTrace();
        	
        	try{
        		stopUsingGPS();
        	}catch(Exception exp){}
        	
            if(latitude==0 && longitude==0)
            	return null;
            else{
            	gpsLocation=getGPSLocationFromService(getLatitude(),getLongitude());
            }
        	
        	return gpsLocation;
        
        }finally{
        	try{
        		stopUsingGPS();
        	}catch(Exception e){}
        }
        
        return gpsLocation;
    }
    
    
    
    
    public GPSLocation getGPSLocationFromService(double lat,double lon){
    	GPSLocation gpsLocation=new GPSLocation();
    	gpsLocation.setLocation("");
    	gpsLocation.setStreetNumber("");
    	gpsLocation.setLatitude(lat+"");
    	gpsLocation.setLongitude(lon+"");
    	
    	ServiceHandler service=new ServiceHandlerImpl();
    	try {
			JSONObject json=service.getGPSLocation(lat, lon);
			if(json!=null){
				if(!json.isNull("Location"))
					gpsLocation.setLocation(json.getString("Location"));
				
				if(!json.isNull("StreetNumber"))
					gpsLocation.setStreetNumber(json.getString("StreetNumber"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return gpsLocation;
    }

}