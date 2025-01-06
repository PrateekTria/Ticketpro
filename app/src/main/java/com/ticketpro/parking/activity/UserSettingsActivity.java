package com.ticketpro.parking.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ticketpro.model.UserSetting;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.UserSettingsBLProcessor;
import com.ticketpro.parking.proxy.ProxyImpl;
import com.ticketpro.parking.service.TPAsyncTask;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPTask;
import com.ticketpro.util.TPUtility;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */
public class UserSettingsActivity extends BaseActivityImpl {

    private Spinner syncSpinner;
    private Spinner drpSpinner;

    private UserSetting userSettings;
    private ToggleButton gpsToggleButton;
    private ToggleButton backupToggleButton;
    private ToggleButton violationToggleButton;
    private ToggleButton commentsToggleButton;
    private ToggleButton locationToggleButton;
    private ToggleButton skipLocationToggleButton;
    private ToggleButton autoLookupToggleButton;
    private ToggleButton stickyMarkersToggleButton;
    private ToggleButton secondLocationToggleButton;
    private ToggleButton lookupToggleButton;
    private ToggleButton searchToggleButton;
    private ToggleButton bodyLocationToggleButton;
    private ToggleButton colorLocationToggleButton;
    private ToggleButton makeLocationToggleButton;
    private EditText cacheExpiryText;
    private ToggleButton autoPromptToggleButton;
    //private ToggleButton alprVehicleToggleButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.user_settings);
            setLogger(UserSettingsActivity.class.getName());
            setBLProcessor(new UserSettingsBLProcessor());
            setActiveScreen(this);

            isNetworkInfoRequired = true;

            syncSpinner = (Spinner) findViewById(R.id.user_settings_sync_interval);
            drpSpinner = (Spinner) findViewById(R.id.user_settings_drp_interval);
            cacheExpiryText = (EditText) findViewById(R.id.user_settings_cached_expiry);

            gpsToggleButton = (ToggleButton) findViewById(R.id.GPS_toggleButton);
            backupToggleButton = (ToggleButton) findViewById(R.id.databackup_toggleButton);
            violationToggleButton = (ToggleButton) findViewById(R.id.violationKeyboard_toggleButton);
            commentsToggleButton = (ToggleButton) findViewById(R.id.commentsKeyboard_toggleButton);
            locationToggleButton = (ToggleButton) findViewById(R.id.locationKeyboard_toggleButton);
            skipLocationToggleButton = (ToggleButton) findViewById(R.id.skipLocation_toggleButton);
            autoLookupToggleButton = (ToggleButton) findViewById(R.id.autolookup_toggleButton);
            stickyMarkersToggleButton = (ToggleButton) findViewById(R.id.stickyMarkers_toggleButton);
            secondLocationToggleButton = (ToggleButton) findViewById(R.id.secondLocation_toggleButton);
            lookupToggleButton = (ToggleButton) findViewById(R.id.lookup_toggleButton);
            searchToggleButton = (ToggleButton) findViewById(R.id.search_toggleButton);

            makeLocationToggleButton = (ToggleButton) findViewById(R.id.makeKeyboard_toggleButton);
            bodyLocationToggleButton = (ToggleButton) findViewById(R.id.bodyKeyboard_toggleButton);
            colorLocationToggleButton = (ToggleButton) findViewById(R.id.colorKeyboard_toggleButton);
            autoPromptToggleButton = (ToggleButton) findViewById(R.id.autoPrompt_toggleButton);
            //alprVehicleToggleButton = (ToggleButton)findViewById(R.id.alprVehicle);
            bindDataAtLoadingTime();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void bindDataAtLoadingTime() {
        try {
            UserSettingsBLProcessor blProcessor = (UserSettingsBLProcessor) screenBLProcessor;

            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, blProcessor.getSynchIntervalList());
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            syncSpinner.setAdapter(dataAdapter1);

            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, blProcessor.getDRPIntervalList());
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            drpSpinner.setAdapter(dataAdapter2);

            userSettings = TPApp.getUserSettings();

            gpsToggleButton.setChecked(userSettings.isGPSEnabled());
            backupToggleButton.setChecked(userSettings.isDataBackupEnabled());

            int spinnerPosition = dataAdapter1.getPosition(userSettings.getAutoSyncInterval() + " Hrs");
            syncSpinner.setSelection(spinnerPosition);

            spinnerPosition = dataAdapter2.getPosition(userSettings.getDataRetentionPeriod() + " Hrs");
            drpSpinner.setSelection(spinnerPosition);


            locationToggleButton.setChecked(userSettings.isLocationKeyboard());
            commentsToggleButton.setChecked(userSettings.isCommentsKeyboard());
            violationToggleButton.setChecked(userSettings.isViolationKeyboard());
            skipLocationToggleButton.setChecked(userSettings.isSkipLocationEntry());
            autoLookupToggleButton.setChecked(userSettings.isAutoLookup());
            stickyMarkersToggleButton.setChecked(userSettings.isStickyMarker());
            secondLocationToggleButton.setChecked(userSettings.isSecondLocation());
            lookupToggleButton.setChecked(userSettings.isAccordionLookUp());
            searchToggleButton.setChecked(userSettings.isSearchContains());
            makeLocationToggleButton.setChecked(userSettings.isMakeKeyboard());
            bodyLocationToggleButton.setChecked(userSettings.isBodyKeyboard());
            colorLocationToggleButton.setChecked(userSettings.isColorKeyboard());

            cacheExpiryText.setText(StringUtil.getDisplayString(userSettings.getCacheExpiry() + ""));
            autoPromptToggleButton.setChecked(userSettings.isAutoPromptVehicle());
            //alprVehicleToggleButton.setChecked(userSettings.isALPRVehicleRequired());

            if (userSettings.getUserPrefs() == null || userSettings.getUserPrefs().isEmpty()) {
                autoPromptToggleButton.setChecked(true);
                autoLookupToggleButton.setChecked(true);
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    public void onClick(View view) {

    }

    public void backAction(View view) {
        finish();
    }

    public void doneAction(View view) {
        try {
            if (userSettings == null) {
                userSettings = new UserSetting();
            }

            userSettings.setAutoSyncInterval(getSpinnerValue((String) syncSpinner.getSelectedItem()));
            userSettings.setDataRetentionPeriod(getSpinnerValue((String) drpSpinner.getSelectedItem()));

            //Parse cache expiration value
            try {
                userSettings.setCacheExpiry(Integer.parseInt(cacheExpiryText.getText().toString()));
            } catch (NumberFormatException e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            userSettings.setDataBackup(backupToggleButton.isChecked() ? "Y" : "N");
            userSettings.setGps(gpsToggleButton.isChecked() ? "Y" : "N");
            userSettings.setUserId(TPApp.userId);
            userSettings.setDeviceId(TPApp.deviceId);
            userSettings.setLocationKeyboard(locationToggleButton.isChecked());
            userSettings.setCommentsKeyboard(commentsToggleButton.isChecked());
            userSettings.setViolationKeyboard(violationToggleButton.isChecked());
            userSettings.setSkipLocationEntry(skipLocationToggleButton.isChecked());
            userSettings.setAutoLookup(autoLookupToggleButton.isChecked());
            userSettings.setSecondLocationEntry(secondLocationToggleButton.isChecked());
            userSettings.setAccordionLookUp(lookupToggleButton.isChecked());
            userSettings.setSearchContains(searchToggleButton.isChecked());
            userSettings.setStickyMarker(stickyMarkersToggleButton.isChecked());
            userSettings.setMakeKeyboard(makeLocationToggleButton.isChecked());
            userSettings.setBodyKeyboard(bodyLocationToggleButton.isChecked());
            userSettings.setColorKeyboard(colorLocationToggleButton.isChecked());
            userSettings.setAutoPromptVehicle(autoPromptToggleButton.isChecked());
            //userSettings.setALPRVehicleRequired(alprVehicleToggleButton.isChecked());
            userSettings.setUserPrefs(UserSetting.getUserPrefsString(userSettings));
            TPApp.setUserSettings(userSettings);
            TPApp.updateUserSettings();

            if (isNetworkConnected()) {
                TPAsyncTask task = new TPAsyncTask(this, false);
                task.execute(new TPTask() {
                    @Override
                    public void execute() {
                        ProxyImpl proxy = new ProxyImpl();
                        JSONArray settingsArray = new JSONArray();
                        UserSetting settings = TPApp.getUserSettings();
                        if (settings != null) {
                            JSONObject settingsJSON = settings.getJSONObject();
                            settingsArray.put(settingsJSON);
                            proxy.getService().syncUserSettings(settingsArray);
                        }
                    }
                });
            }

            finish();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private int getSpinnerValue(String valueStr) {
        int value = 0;
        try {
            if (!StringUtil.isEmpty(valueStr)) {
                value = Integer.parseInt(valueStr.split(" ")[0]);
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return value;
    }

    @Override
    public void handleVoiceInput(String text) {
        if (text == null) {
            return;
        }

        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        if (text.contains("BACK") || text.contains("GO BACK") || text.contains("CLOSE")) {
            backAction(null);
        }
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
