package com.ticketpro.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import org.apache.log4j.Logger;

public class PowerConnectionReceiver extends BroadcastReceiver {
    public Logger log;

    public PowerConnectionReceiver() {
        log = Logger.getLogger("PowerConnectionReceiver");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        TPConstant.isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        TPConstant.usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        TPConstant.acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        //log.error("isCharging = " + isCharging + "  usbCharge = " + usbCharge + "  acCharge = " + acCharge);

    }
}
