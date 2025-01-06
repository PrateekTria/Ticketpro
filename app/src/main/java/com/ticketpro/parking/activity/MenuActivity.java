package com.ticketpro.parking.activity;

//import com.crashlytics.android.Crashlytics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ticketpro.parking.R;
import com.ticketpro.util.TPConstant;

//import io.fabric.sdk.android.Fabric;

public class MenuActivity extends FragmentActivity {

    private SlideoutHelper mSlideoutHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSlideoutHelper = new SlideoutHelper(this);
        mSlideoutHelper.activate();
        getSupportFragmentManager().beginTransaction().add(R.id.slideout_placeholder, new MenuFragment(), "menu").commit();
        mSlideoutHelper.open();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mSlideoutHelper.close();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public SlideoutHelper getSlideoutHelper() {
        return mSlideoutHelper;
    }

    public void callAction(String action) {
        Intent intent = new Intent(TPConstant.LOCAL_BROADCAST_EVENT);
        intent.putExtra("action", action);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public Context getContent() {
        return getApplicationContext();
    }

}
