package com.ticketpro.parking.activity;

import static com.ticketpro.util.TPConstant.ASSETS_URL;
import static com.ticketpro.util.TPConstant.DEVELOPMENT;
import static com.ticketpro.util.TPConstant.FIREBASE_DB_URL;
import static com.ticketpro.util.TPConstant.IMAGES_URL;
import static com.ticketpro.util.TPConstant.IS_DEVELOPMENT_BUILD;
import static com.ticketpro.util.TPConstant.IS_STAGING_BUILD;
import static com.ticketpro.util.TPConstant.LPR_URL;
import static com.ticketpro.util.TPConstant.RX_SERVICE_URL;
import static com.ticketpro.util.TPConstant.SERVICE_URL;
import static com.ticketpro.util.TPConstant.FILE_UPLOAD;
import static com.ticketpro.util.TPConstant.SPLASH_TIME;
import static com.ticketpro.util.TPConstant.STAGING;
import static com.ticketpro.util.TPConstant.UPDATE_URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.ticketpro.logger.LoggerConfigurator;
import com.ticketpro.parking.R;
import com.ticketpro.util.CountDownDialog;
import com.ticketpro.util.Preference;
import com.ticketpro.util.TPUtility;

/**
 * Splash Screen.
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class SplashActivity extends Activity implements CountDownDialog.versioncallback {
    public static final String MyPREFERENCES = "MyPrefs";
    ProgressBar progressBar;
    SharedPreferences sharedpreferences;
    private Preference preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
                Log.w("TicketPRO", "Main Activity is not the root. Finishing Main Activity instead of launching.");
                finish();
                return;
            }
        }
        setContentView(R.layout.splash);
        preference = Preference.getInstance(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBar1);

        String redirect = sharedpreferences.getString("firstime", "");
        if (!redirect.equals("Y")) {
            TimerforBuild();
        } else {
            int where = sharedpreferences.getInt("where", 0);
            if (where == 1) {
                FILE_UPLOAD = "https://tpwebservicesdev.ticketproweb.com/public/index.php/service";
                SERVICE_URL = "https://tpwebservicesdev.ticketproweb.com/public/index.php/service/genericv1";
                //  RX_SERVICE_URL = "https://tpwebservicesdev.ticketproweb.com/public/index.php/";
                RX_SERVICE_URL = "https://tpwebservicesdev.ticketproweb.com/public/index.php/";
                ASSETS_URL = "https://tpwebservicesdev.ticketproweb.com/public/assets/customers";
                UPDATE_URL = "https://tpwebservicesdev.ticketproweb.com/public/updates";
                IMAGES_URL = "https://tpwebservicesdev.ticketproweb.com/public/images/customers";
                LPR_URL = "http://lprdev.ticketproweb.com/LPRWcfService/LPRService.svc?wsdl";
                FIREBASE_DB_URL = "http://trackerdev.ticketproweb.com:8081/api/";
                IS_DEVELOPMENT_BUILD = true;
            }
            if (where == 2) {
                FILE_UPLOAD = "https://tpwebservicesstage24.ticketproweb.com/public/index.php/service";
                SERVICE_URL = "https://tpwebservicesstage24.ticketproweb.com/public/index.php/service/genericv1";
                RX_SERVICE_URL = "https://tpwebservicesstage24.ticketproweb.com/public/index.php/";
                ASSETS_URL = "https://tpwebservicesstage24.ticketproweb.com/public/assets/customers";
                UPDATE_URL = "https://tpwebservicesstage24.ticketproweb.com/public/updates";
                IMAGES_URL = "https://tpwebservicesstage24.ticketproweb.com/public/images/customers";
                LPR_URL = "http://lprdev.ticketproweb.com/LPRWcfService/LPRService.svc?wsdl";
                FIREBASE_DB_URL = "http://trackerdev.ticketproweb.com:8081/api/";
                IS_STAGING_BUILD = true;
                IS_DEVELOPMENT_BUILD = false;
            }
            progressBar.setVisibility(View.VISIBLE);
            TPUtility.createTxtFile();

            final SplashActivity splashActivity = this;
            try {
                // Try catch SDCard Issues
                try {
                    String versionString = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                    new LoggerConfigurator().configLogger(versionString);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            synchronized (this) {
                                wait(SPLASH_TIME);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            finish();

                            //start a new activity
                            if (preference.getBoolean("PERMISSION")) {
                                //start a new activity
                                Intent i = new Intent();
                                i.setClass(splashActivity, HomeActivity.class);
                                startActivity(i);
                            } else {
                                Intent i = new Intent();
                                i.setClass(splashActivity, PermissionActivity.class);
                                startActivity(i);
                            }
                        }

                    }
                }.start();

            } catch (Exception e) {
                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Failed to initialize sdcard. Please mount the sdcard and try again.");
                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", (dialog, which) -> {
                    if (alertDialog.isShowing())
                        alertDialog.dismiss();
                });

                alertDialog.show();
            }
        }
    }


    public void TimerforBuild() {
        CountDownDialog countDownDialog = new CountDownDialog();
        countDownDialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
        countDownDialog.show(getFragmentManager(), "fragment_countdownTimer");
    }

    @Override
    public void sendresult(boolean userInput, int BuildType) {


        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (BuildType == DEVELOPMENT) {
            FILE_UPLOAD = "https://tpwebservicesdev.ticketproweb.com/public/index.php/service";
          // zend //= "https://tpwebservicesdev.ticketproweb.com/public/index.php/service";
            // laravel https://tpwebservicesdev24.ticketproweb.com/public/index.php/service
            SERVICE_URL = "https://tpwebservicesdev.ticketproweb.com/public/index.php/service/genericv1";
            RX_SERVICE_URL = "https://tpwebservicesdev.ticketproweb.com/public/index.php/";
            ASSETS_URL = "https://tpwebservicesdev.ticketproweb.com/public/assets/customers";
            UPDATE_URL = "https://tpwebservicesdev.ticketproweb.com/public/updates";
            IMAGES_URL = "https://tpwebservicesdev.ticketproweb.com/public/images/customers";
            LPR_URL = "http://lprdev.ticketproweb.com/LPRWcfService/LPRService.svc?wsdl";
            FIREBASE_DB_URL = "http://trackerdev.ticketproweb.com:8081/api/";
            IS_DEVELOPMENT_BUILD = true;
            editor.putInt("where", 1);
        }

        if (BuildType == STAGING) {
            FILE_UPLOAD = "https://tpwebservicesstage24.ticketproweb.com/public/index.php/service";
            SERVICE_URL = "https://tpwebservicesstage24.ticketproweb.com/public/index.php/service/genericv1";
            RX_SERVICE_URL = "https://tpwebservicesstage24.ticketproweb.com/public/index.php/";
            ASSETS_URL = "https://tpwebservicesstage24.ticketproweb.com/public/assets/customers";
            UPDATE_URL = "https://tpwebservicesstage24.ticketproweb.com/public/updates";
            IMAGES_URL = "https://tpwebservicesstage24.ticketproweb.com/public/images/customers";
            LPR_URL = "http://lprdev.ticketproweb.com/LPRWcfService/LPRService.svc?wsdl";
            FIREBASE_DB_URL = "http://trackerdev.ticketproweb.com:8081/api/";
            IS_STAGING_BUILD = true;
            editor.putInt("where", 2);
        }

        editor.putString("firstime", "Y");
        editor.apply();
        progressBar.setVisibility(View.VISIBLE);
        TPUtility.createTxtFile();

        final SplashActivity splashActivity = this;
        try {
            // Try catch SDCard Issues
            try {
                String versionString = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                new LoggerConfigurator().configLogger(versionString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            new Thread() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            wait(SPLASH_TIME);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        finish();

                        //start a new activity
                        if (preference.getBoolean("PERMISSION")) {
                            //start a new activity
                            Intent i = new Intent();
                            i.setClass(splashActivity, HomeActivity.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent();
                            i.setClass(splashActivity, PermissionActivity.class);
                            startActivity(i);
                        }
                    }

                }
            }.start();

        } catch (Exception e) {
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Failed to initialize sdcard. Please mount the sdcard and try again.");
            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", (dialog, which) -> {
                if (alertDialog.isShowing())
                    alertDialog.dismiss();
            });

            alertDialog.show();
        }
    }


}
