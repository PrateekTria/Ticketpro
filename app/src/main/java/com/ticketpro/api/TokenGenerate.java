package com.ticketpro.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.ticketpro.util.Preference;
import com.ticketpro.util.TPConstant;

import net.smartam.leeloo.client.OAuthClient;
import net.smartam.leeloo.client.URLConnectionClient;
import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.client.response.OAuthJSONAccessTokenResponse;
import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import net.smartam.leeloo.common.message.types.GrantType;


public class TokenGenerate extends AsyncTask<String, Void, String> {

    private Preference preference;
    private String token;
    private ProgressDialog progress;
    private Context mContext;
    private String URL;
    private String userName;
    private String pass;

    public TokenGenerate(Activity context, String URL, String userName, String pass) {

        this.mContext = context;
        this.userName = userName;
        this.pass = pass;
        this.URL = URL;
        preference = Preference.getInstance(context);
    }
    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(mContext);
        progress.setMessage("Fetching Token");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }
    @Override
    protected String doInBackground(String... params) {

        OAuthClientRequest request = null;
        try {
            request = OAuthClientRequest.tokenLocation(URL)
                    .setGrantType(GrantType.PASSWORD)
                    .setUsername(userName)
                    .setPassword(pass)
                    .buildBodyMessage();
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        }

        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

        OAuthJSONAccessTokenResponse response = null;
        try {
            response = oAuthClient.accessToken(request);
        } catch (OAuthSystemException | OAuthProblemException e) {
            e.printStackTrace();
        }
        assert response != null;

        try {

            token = response.getAccessToken();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return token;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result!=null){

            preference.putString(TPConstant.SAMTRANS_TOKEN,result);
        }
        progress.dismiss();

    }


}