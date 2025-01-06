package com.ticketpro.parking.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.work.WorkManager;

import com.ticketpro.model.Feature;
import com.ticketpro.model.User;
import com.ticketpro.model.UserSetting;
import com.ticketpro.parking.R;
import com.ticketpro.util.DataUtility;
import com.ticketpro.util.Preference;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class LoginPasswordActivity extends BaseActivityImpl {

    final int ERROR_EMPTY_PASSWORD = 1;
    final int ERROR_INVALID_PASSWORD = 2;
    private final int BACK_BUTTON_TAG = 1;
    private final int LOGIN_BUTTON_TAG = 2;
    private Button backButton;
    private Button loginButton;
    private EditText passwordInputField;
    private String username;
    private String password;
    private int userid;
    private ProgressDialog progressDialog;
    private Handler errorHandler;
    private SharedPreferences mPreferences;
    //private LoginPreference loginPreference;
    private Preference preference;
    private String eulaAccepted;
    private String rmsid;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.login_password);
            setLogger(LoginPasswordActivity.class.getName());
            preference = Preference.getInstance(this);
            eulaAccepted = preference.getString("EULA_ACCEPTED");
            mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            //loginPreference = LoginPreference.getInstance(LoginPasswordActivity.this);
            backButton = (Button) findViewById(R.id.login_password_back_btn);
            backButton.setOnClickListener(this);
            backButton.setTag(BACK_BUTTON_TAG);

            loginButton = (Button) findViewById(R.id.login_password_login_btn);
            loginButton.setOnClickListener(this);
            loginButton.setTag(LOGIN_BUTTON_TAG);

            passwordInputField = (EditText) findViewById(R.id.login_password_pswd_field);
            passwordInputField.requestFocus();
            Intent intent = getIntent();
            username = intent.getStringExtra("username");
            password = intent.getStringExtra("password");
            rmsid = intent.getStringExtra("rmsid");
            userid = intent.getIntExtra("userid", 0);

            if (rmsid!=null && !TextUtils.isEmpty(rmsid)) {
                ((TextView) findViewById(R.id.login_password_label)).setText("Enter password for user ID " + rmsid);
            }else {
                ((TextView) findViewById(R.id.login_password_label)).setText("Enter password for " + username);

            }
            //Check for Numeric Keypads
            if (Feature.isNumericKeypadRequired("password")) {
                passwordInputField.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            }

            passwordInputField.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {

                        doLogin();
                        return true;
                    }
                    ;

                    return false;
                }
            });

            errorHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    Toast toastView = null;
                    if (msg.what == ERROR_INVALID_PASSWORD) {
                        toastView = TPUtility.getErrorToastView(LoginPasswordActivity.this, "Invalid password. Please try again.");

                    } else if (msg.what == ERROR_EMPTY_PASSWORD) {
                        toastView = TPUtility.getErrorToastView(LoginPasswordActivity.this, "Please enter password.");
                    }

                    if (toastView != null) {
                        toastView.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 300);
                        toastView.show();
                    }

                    //bindDataAtLoadingTime();
                }
            };


        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
        bindDataAtLoadingTime();
    }

    public void bindDataAtLoadingTime() {
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                //passwordInputField.requestFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(passwordInputField, InputMethodManager.SHOW_FORCED);


            }
        }, 100);
    }

    @Override
    public void onClick(View v) {
        try {
            int tagId = Integer.valueOf(v.getTag().toString());
            if (tagId == BACK_BUTTON_TAG) {
                finish();

            } else if (tagId == LOGIN_BUTTON_TAG) {
                doLogin();
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }


    private void doLogin() {
        TPUtility.hideSoftKeyboard(this);
        progressDialog = ProgressDialog.show(this, "", "Processing Login...");
        new Thread() {
            public void run() {
                String userPassword = passwordInputField.getText().toString();
                if (userPassword.equals(password)) {
                    try {
                        TPApp.userId = userid;

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                        Date date = new Date();
                        TPApp.setFirstLogin(simpleDateFormat.format(date));
                        /*@SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        if (loginPreference.getFirstLogin().equalsIgnoreCase("")) {
                            TPApp.setFirstLogin(simpleDateFormat.format(date));
                            loginPreference.setFirstLogin(simpleDateFormat.format(date));
                        } else {
                            if (dateFormat.parse(dateFormat.format(date)).compareTo(dateFormat.parse(loginPreference.getFirstLogin())) > 0) {
                                TPApp.setFirstLogin(simpleDateFormat.format(date));
                                loginPreference.setFirstLogin(simpleDateFormat.format(date));
                            }
                        }*/

                        User userInfo = User.getUserInfo(userid);
                        TPApp.setUserInfo(userInfo);

                        UserSetting userSettings = UserSetting.getUserSettings(userid);
                        if (userSettings == null) {
                            userSettings = new UserSetting();
                            TPApp.setUserSettings(userSettings);
                            TPApp.initDefaultUserPrefs(userSettings);
                        } else {
                            TPApp.initUserPrefs(userSettings, mPreferences);
                        }

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.putString(TPConstant.PREFS_KEY_LAST_USERNAME, userInfo.getUsername());
                        editor.putString(TPConstant.PREFS_KEY_LAST_USERPWD, userInfo.getPassword());
                        editor.apply();

                        DataUtility.setDeleteScheduler(LoginPasswordActivity.this);
                        Intent i = new Intent();
                        i.setClass(LoginPasswordActivity.this, EULAActivity.class);
                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                    } catch (Exception e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                } else {
                    if (userPassword.isEmpty()) {
                        errorHandler.sendEmptyMessage(ERROR_EMPTY_PASSWORD);
                    } else {
                        errorHandler.sendEmptyMessage(ERROR_INVALID_PASSWORD);
                    }
                }
            }
        }.start();
    }

    @Override
    public void handleVoiceInput(String text) {
        // TODO Auto-generated method stub
    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
        // TODO Auto-generated method stub
    }
}
