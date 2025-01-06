package com.ticketpro.util;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import java.security.KeyStore;

public class SSLHttpClient extends DefaultHttpClient {
    private boolean isTLS12Required = false;

    public SSLHttpClient() {
    }

    public SSLHttpClient(boolean isTLS12Required) {
        this.isTLS12Required = isTLS12Required;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        try {
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", newSslSocketFactory(), 443));
            //registry.register(new Scheme("https", new Tls12SocketFactory(newSslSocketFactory()), 443));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ThreadSafeClientConnManager(getParams(), registry);
    }

    private SSLSocketFactory newSslSocketFactory() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new TPSSLSocketFactory(trustStore, isTLS12Required);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            return sf;

        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}