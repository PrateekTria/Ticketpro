/***********************************************
 * CONFIDENTIAL AND PROPRIETARY 
 * 
 * The source code and other information contained herein is the confidential and the exclusive property of
 * ZIH Corp. and is subject to the terms and conditions in your end user license agreement.
 * This source code, and any other information contained herein, shall not be copied, reproduced, published, 
 * displayed or distributed, in whole or in part, in any medium, by any means, for any purpose except as
 * expressly permitted under such license agreement.
 * 
 * Copyright ZIH Corp. 2012
 * 
 * ALL RIGHTS RESERVED
 ***********************************************/

package com.ticketpro.zebraPrinter;

import android.os.Bundle;
import android.os.Looper;

import com.ticketpro.util.UIHelper;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.discovery.BluetoothDiscoverer;

import org.apache.log4j.Logger;

public class BluetoothDiscovery extends DiscoveryResultList {
    public Logger log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log = Logger.getLogger(BluetoothDiscovery.class);
        new Thread(() -> {
            Looper.prepare();
            try {
                BluetoothDiscoverer.findPrinters(BluetoothDiscovery.this, BluetoothDiscovery.this);
            } catch (ConnectionException e) {
                log.error(UIHelper.getPrintStackTrace(e));
                new UIHelper(BluetoothDiscovery.this).showErrorDialogOnGuiThread(e.getMessage());
            } finally {
                Looper.myLooper().quit();
            }
        }).start();
    }
}
