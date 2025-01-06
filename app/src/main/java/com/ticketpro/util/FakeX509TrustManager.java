package com.ticketpro.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.annotation.SuppressLint;
import android.util.Log;

@SuppressLint("CustomX509TrustManager")
public class FakeX509TrustManager implements X509TrustManager {
        private static TrustManager[] trustManagers;
        private final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return _AcceptedIssuers;
        }

        public static void allowAllSSL() {
        	HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
        	SSLContext context = null;
            if (trustManagers == null) {
                trustManagers = new TrustManager[] { new FakeX509TrustManager() };
            }
            
            try {

                //context = SSLContext.getInstance("TLS");
                //context.init(null, trustManagers, new SecureRandom());

                    context = SSLContext.getInstance("TLSv1.2");
                    context.init(null, trustManagers, new SecureRandom());



            }
            catch (final NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
            }

            assert context != null;
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException {

        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {

        }



    }

class NullHostNameVerifier implements HostnameVerifier {
    @SuppressLint("BadHostnameVerifier")
    @Override
    public boolean verify(String hostname, SSLSession session) {
        Log.i("NullHostNameVerifier", "Approving certificate for " + hostname);
        return true;
    }



}