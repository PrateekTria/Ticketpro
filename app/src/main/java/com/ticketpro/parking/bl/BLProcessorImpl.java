package com.ticketpro.parking.bl;

import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.proxy.Proxy;
import com.ticketpro.parking.proxy.ProxyImpl;

import org.apache.log4j.Logger;

public abstract class BLProcessorImpl implements BLProcessor {
    protected Proxy proxy;
    protected Logger log;
    protected TPApplication TPApp;

    public BLProcessorImpl() {
        setProxy(new ProxyImpl());
    }

    @Override
    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public void setLogger(String classInstance) {
        this.log = Logger.getLogger(classInstance);
    }

    @Override
    public Proxy getProxy() {
        return this.proxy;
    }

    @Override
    public void setTPApplication(TPApplication TPApp) {
        this.TPApp = TPApp;
    }

    @Override
    public TPApplication getTPApplication() {
        return this.TPApp;
    }
}
