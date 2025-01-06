package com.ticketpro.util;

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.TPApplication;

public class TPHttpClient extends DefaultHttpClient{
 
	public TPHttpClient() {
    
	}
 
    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", newSslSocketFactory(), 443));
        
        return new ThreadSafeClientConnManager(getParams(), registry);
    }
 
    private SSLSocketFactory newSslSocketFactory() {
        try {
        	KeyStore trusted = KeyStore.getInstance("BKS");
            InputStream in = TPApplication.getInstance().getResources().openRawResource(R.raw.keystore);
            try {
                trusted.load(in,"ticketpro".toCharArray());
            } finally {
                in.close();
            }
            
            SSLSocketFactory sf = new TPSSLSocketFactory(trusted ,false);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            
            return sf;
            
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}