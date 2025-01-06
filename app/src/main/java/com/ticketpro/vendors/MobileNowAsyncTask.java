package com.ticketpro.vendors;

import org.json.JSONObject;

import android.os.AsyncTask;

class MobileNowAsyncTask extends AsyncTask<String, Void, JSONObject>{

	@Override
    protected JSONObject doInBackground(String... params) {
    	JSONObject jObject = null;
        String methodName = params[0];
        
        return jObject;
    }

    protected void onPostExecute(JSONObject data) {
    
    }
}