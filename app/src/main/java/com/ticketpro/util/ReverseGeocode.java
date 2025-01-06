package com.ticketpro.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Address;
import android.util.Log;

import com.ticketpro.model.Feature;

import javax.net.ssl.HttpsURLConnection;

public class ReverseGeocode {

    public static List<Address> getFromLocation(double lat, double lon, int maxResults) {
    		//String urlStr = "http://maps.google.com/maps/geo?q="+lat+","+lon+"&output=json&sensor=false&key=ABQIAAAArkDVdMx7JVlBmKCc9oc4cxRmm8r5uVhRKbje0Xa0Cw3bZePxrBSgzFe3b5hKDyIMrYUQY3fuehnCMA";//google map old api key using ravi sir account
            String urlStr = "https://maps.google.com/maps/geo?q="+lat+","+lon+"&output=json&sensor=false&key=ABQIAAAArkDVdMx7JVlBmKCc9oc4cxRmm8r5uVhRKbje0Xa0Cw3bZePxrBSgzFe3b5hKDyIMrYUQY3fuehnCMA";

        //String urlStr = "http://maps.google.com/maps/geo?q="+lat+","+lon+"&output=json&sensor=false&key="+TPConstant.GOOGLE_MAP_API_KEY_TICKETPROAPP;//ABQIAAAArkDVdMx7JVlBmKCc9oc4cxRmm8r5uVhRKbje0Xa0Cw3bZePxrBSgzFe3b5hKDyIMrYUQY3fuehnCMA";

            String response = "";
            List<Address> results = new ArrayList<Address>();
            /*HttpClient client = new DefaultHttpClient();
            Log.d("ReverseGeocode", urlStr);
            try {
                    HttpResponse hr = client.execute(new HttpGet(urlStr));
                    HttpEntity entity = hr.getEntity();
                    BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String buff = null;
                    while ((buff = br.readLine()) != null)
                            response += buff;
            } catch (IOException e) {
                    e.printStackTrace();
            }*/
            try {
                URL url = new URL(urlStr);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setSSLSocketFactory(new TLSSocketFactory());
                conn.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                conn.setReadTimeout(getNetworkTimeout());
                conn.setConnectTimeout(getNetworkTimeout());
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response += inputLine;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            JSONArray responseArray = null;
            try {
                    JSONObject jsonObject = new JSONObject(response);
                    responseArray = jsonObject.getJSONArray("Placemark");
            } catch (JSONException e) {
                    return results;
            }

            Log.d("ReverseGeocode", "" + responseArray.length() + " result(s)");
            
            for(int i = 0; i < responseArray.length() && i < maxResults-1; i++) {
                    Address addy = new Address(Locale.getDefault());

                    try {
                            JSONObject jsl = responseArray.getJSONObject(i);
                            String addressLine = jsl.getString("address");

                            if(addressLine.contains(","))
                                    addressLine = addressLine.split(",")[0];

                            addy.setAddressLine(0, addressLine);

                            jsl = jsl.getJSONObject("AddressDetails").getJSONObject("Country");
                            addy.setCountryName(jsl.getString("CountryName"));
                            addy.setCountryCode(jsl.getString("CountryNameCode"));
                            
                            Log.i("Address ", jsl.toString());

                            if(!jsl.isNull("AdministrativeArea")){
                            	jsl = jsl.getJSONObject("AdministrativeArea");
                            	addy.setAdminArea(jsl.getString("AdministrativeAreaName"));
                            	
                            	jsl = jsl.getJSONObject("SubAdministrativeArea");
                                addy.setSubAdminArea(jsl.getString("SubAdministrativeAreaName"));
                                
                                jsl = jsl.getJSONObject("Locality");
                                addy.setLocality(jsl.getString("LocalityName"));

                                if(!jsl.isNull("DependentLocality")){
                                	jsl = jsl.getJSONObject("DependentLocality");
                                	addy.setThoroughfare(jsl.getJSONObject("Thoroughfare").getString("ThoroughfareName"));
                                }else{
                                	addy.setThoroughfare(jsl.getJSONObject("Thoroughfare").getString("ThoroughfareName"));
                                }
                            }else{
                            	
                            	jsl = jsl.getJSONObject("SubAdministrativeArea");
                                addy.setSubAdminArea(jsl.getString("SubAdministrativeAreaName"));
                                
                                jsl = jsl.getJSONObject("Locality");
                                addy.setLocality(jsl.getString("LocalityName"));

                                if(!jsl.isNull("DependentLocality")){
                                	jsl = jsl.getJSONObject("DependentLocality");
                                	addy.setThoroughfare(jsl.getJSONObject("Thoroughfare").getString("ThoroughfareName"));
                                }else{
                                	addy.setThoroughfare(jsl.getJSONObject("Thoroughfare").getString("ThoroughfareName"));
                                }
                                
                                if(addy.getThoroughfare()!=null && addy.getThoroughfare().contains(",")){
                                	String[] location=addy.getThoroughfare().split(",");
                                	if(location.length==2){
                                		addy.setSubThoroughfare(location[0]);
                                		addy.setThoroughfare(location[1]);
                                	}else{
                                		addy.setSubThoroughfare("");
                                	}
                                }
                                
                            }
                    } catch (JSONException e) {
                            e.printStackTrace();
                            continue;
                    }

                    results.add(addy);
            }

            return results;
        }

    public static int getNetworkTimeout() {
        int timeout = 5000;
        if (Feature.isFeatureAllowed(Feature.NETWORK_TIMEOUT)) {
            String timeoutString = Feature.getFeatureValue(Feature.NETWORK_TIMEOUT);
            try {
                timeout = Integer.parseInt(timeoutString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return timeout;
    }
}