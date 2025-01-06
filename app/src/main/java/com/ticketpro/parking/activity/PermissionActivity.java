package com.ticketpro.parking.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ticketpro.parking.R;
import com.ticketpro.util.Preference;
import com.ticketpro.util.TPMruntimePermission;

public class PermissionActivity extends Activity {

    TPMruntimePermission permission;
    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        preference = Preference.getInstance(this);
        permission = new TPMruntimePermission(true, PermissionActivity.this);

    }
    @Override
    protected void onResume() {
        super.onResume();
        //start a new activity
        if (preference.getBoolean("PERMISSION")) {
            Intent i = new Intent();
            i.setClass(PermissionActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }else {
        }
    }
}
