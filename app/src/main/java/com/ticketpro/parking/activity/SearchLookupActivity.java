package com.ticketpro.parking.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.Body;
import com.ticketpro.model.Color;
import com.ticketpro.model.Location;
import com.ticketpro.model.MakeAndModel;
import com.ticketpro.model.State;
import com.ticketpro.model.UserSetting;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.WriteTicketBLProcessor;
import com.ticketpro.parking.proxy.ProxyImpl;
import com.ticketpro.parking.service.TPAsyncTask;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPTask;
import com.ticketpro.util.TPUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class SearchLookupActivity extends BaseActivityImpl {

    private ListView listView;
    private EditText searchEditText;
    private Button extraButton, savedLocation;
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayList<String> filteredArray = new ArrayList<String>();
    private int textlength = 0;

    private ProgressDialog progressDialog;
    private Handler dataLoadingHandler;
    private Handler errorHandler;
    private int searchType;
    private Dialog dialog;
    private EditText inputText;
    private CheckBox keyboardPopupChk;

    private String stateValue;
    private String expValue;
    private String makeValue;
    private String bodyValue;
    private String colorValue;
    private String plateValue;
    private String vinValue;
    private String tmValue;
    private String locationValue;
    private String streetNumberValue;
    private String streetPrefixValue;
    private String streetSuffixValue;
    private String directionValue;
    private String permitValue;
    private String meterValue;

    private int stateId;
    private int bodyId;
    private int colorId;
    private int makeId;
    private String bodyCode;
    private String colorCode;
    private String makeCode;
    private View view;
    private RelativeLayout add_new;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_ticket_search);
        try {
            setLogger(WriteTicketActivity.class.getName());
            setBLProcessor(new WriteTicketBLProcessor((TPApplication) getApplicationContext()));
            setActiveScreen(this);

            listView = (ListView) findViewById(R.id.common_search_list);
            searchEditText = (EditText) findViewById(R.id.common_search_input);
            searchEditText.requestFocus();
            extraButton = (Button) findViewById(R.id.search_extra_btn);
            keyboardPopupChk = (CheckBox) findViewById(R.id.keyboard_chk);
            savedLocation = (Button) findViewById(R.id.savedLocation);
            add_new = findViewById(R.id.add_new);
            Intent intent = getIntent();
            searchType = intent.getIntExtra("LIST_TYPE", 0);
            stateValue = intent.getStringExtra("STATE");
            expValue = intent.getStringExtra("EXP");
            makeValue = intent.getStringExtra("MAKE");
            bodyValue = intent.getStringExtra("BODY");
            colorValue = intent.getStringExtra("COLOR");
            plateValue = intent.getStringExtra("PLATE");
            vinValue = intent.getStringExtra("VIN");
            tmValue = intent.getStringExtra("TM");
            locationValue = intent.getStringExtra("Location");
            streetNumberValue = intent.getStringExtra("StreetNumber");
            streetPrefixValue = intent.getStringExtra("StreetPrefix");
            streetSuffixValue = intent.getStringExtra("StreetSuffix");
            directionValue = intent.getStringExtra("Direction");
            permitValue = intent.getStringExtra("PERMIT");
            meterValue = intent.getStringExtra("METER");

            stateId = intent.getIntExtra("StateId", 0);
            bodyId = intent.getIntExtra("BodyId", 0);
            colorId = intent.getIntExtra("ColorId", 0);
            makeId = intent.getIntExtra("MakeId", 0);

            colorCode = intent.getStringExtra("ColorCode");
            bodyCode = intent.getStringExtra("BodyCode");
            makeCode = intent.getStringExtra("MakeCode");

            if (searchType == TPConstant.SELECT_LOCATION_LIST) {
                add_new.setVisibility(View.VISIBLE);
                extraButton.setVisibility(View.VISIBLE);
                savedLocation.setVisibility(View.VISIBLE);
                extraButton.setText("Add Location");
                extraButton.setOnClickListener(v -> addNewLocation());

                listView.setOnItemLongClickListener((arg0, arg1, pos, id) -> {
                    try {
                        Map<String, String> selectedItem = (HashMap<String, String>) listView.getItemAtPosition(pos);
                        String selectedTitle = selectedItem.get("search_title");
                        editNewLocation(selectedTitle);
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    return true;
                });

                //
                if (Location.getManualLocations().size() > 0) {
                    savedLocation.setEnabled(true);
                    savedLocation.setBackground(getResources().getDrawable(R.drawable.btn_yellow));
                } else {
                    savedLocation.setBackground(getResources().getDrawable(R.drawable.btn_gray));
                    savedLocation.setEnabled(false);
                }
            }
            // Controls keyboard visibility on defined search type
            if (searchType == TPConstant.MAKE_SEARCH_LIST || searchType == TPConstant.BODY_SEARCH_LIST
                    || searchType == TPConstant.COLOR_SEARCH_LIST) {

                keyboardPopupChk.setVisibility(View.VISIBLE);

                if (TPApp.getUserSettings() != null) {
                    saveKeyboardPrefs(searchType, false, true);
                    keyboardPopupChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            saveKeyboardPrefs(searchType, isChecked, false);
                            view = keyboardPopupChk.getRootView();
                        }
                    });
                }
            }

            listView.setScrollbarFadingEnabled(false);
            listView.setFastScrollAlwaysVisible(true);
            listView.setFastScrollEnabled(true);

            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> viewAdapter, View view, int pos, long arg3) {
                    try {
                        Intent data = new Intent();
                        switch (searchType) {
                            case TPConstant.STATES_SEARCH_LIST: {
                                String name = getValue(viewAdapter.getItemAtPosition(pos).toString());
                                State state = State.getStateByName(name);
                                if (state != null) {
                                    data.putExtra("STATE", state.getCode());
                                    data.putExtra("StateId", state.getId());
                                } else {
                                    data.putExtra("STATE", name);
                                    data.putExtra("StateId", State.getStateIdByName(name));
                                }
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                                break;
                            }

                            case TPConstant.EXP_SEARCH_LIST: {
                                data.putExtra("EXP", getValue(viewAdapter.getItemAtPosition(pos).toString()));
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                break;
                            }

                            case TPConstant.MAKE_SEARCH_LIST: {
                                String make = getValue(viewAdapter.getItemAtPosition(pos).toString());
                                data.putExtra("MakeId", MakeAndModel.getMakeIdByName(make));
                                data.putExtra("MakeCode", MakeAndModel.getMakeCodeByName(make));
                                data.putExtra("MAKE", make);
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                break;
                            }

                            case TPConstant.BODY_SEARCH_LIST: {
                                String body = getValue(viewAdapter.getItemAtPosition(pos).toString());
                                data.putExtra("BODY", body);
                                data.putExtra("BodyId", Body.getBodyIdByName(body));
                                data.putExtra("BodyCode", Body.getBodyCodeByName(body));
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                break;
                            }

                            case TPConstant.COLOR_SEARCH_LIST: {
                                String color = getValue(viewAdapter.getItemAtPosition(pos).toString());
                                data.putExtra("COLOR", color);
                                data.putExtra("ColorId", Color.getColorIdByName(color));
                                data.putExtra("ColorCode", Color.getColorCodeByName(color));
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                break;
                            }

                            case TPConstant.SELECT_LOCATION_LIST: {
                                String location = getValue(viewAdapter.getItemAtPosition(pos).toString());
                                data.putExtra("Location", location);
                                /*
                                 * data.putExtra("StreetNumber", streetNumberValue);
                                 * data.putExtra("StreetPrefix", streetPrefixValue);
                                 * data.putExtra("StreetSuffix", streetSuffixValue);
                                 * data.putExtra("Direction", directionValue);
                                 */
                                data.putExtra("REDIRECT_LOCATION_FORM", true);
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                break;
                            }
                        }

                        if (getParent() == null) {
                            setResult(Activity.RESULT_OK, data);
                        } else {
                            getParent().setResult(Activity.RESULT_OK, data);
                        }

                        finish();

                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            });

            searchEditText.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    // Abstract Method of TextWatcher Interface.
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Abstract Method of TextWatcher Interface.
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String searchText = searchEditText.getText().toString();

                    textlength = searchText.length();
                    filteredArray.clear();

                    searchText = searchText.toLowerCase();

                    if (searchText.length() == 0) {
                        filteredArray.addAll(listItems);
                    } else {
                        for (String str : listItems) {
                            if (TPApp.getUserSettings().isSearchContains()) {
                                if (str.toLowerCase().contains(searchText)) {
                                    filteredArray.add(str);
                                }
                            } else if (str.toLowerCase().startsWith(searchText)) {
                                filteredArray.add(str);
                            }
                        }
                    }

                    updateListItems(filteredArray);
                }
            });

            errorHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            };

            dataLoadingHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    updateListItems(listItems);

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            };

            savedLocation.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //remove location added manually without impacting server locations
                    removeAddedLocation();
                }
            });

            bindDataAtLoadingTime();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void bindDataAtLoadingTime() {
        progressDialog = ProgressDialog.show(this, "", "Loading...");

            new Thread() {
                public void run() {
                    try {
                        List<String> valueList = ((WriteTicketBLProcessor) screenBLProcessor).getSearchList(searchType);
                        listItems = new ArrayList<String>(valueList);

                        dataLoadingHandler.sendEmptyMessage(0);

                    } catch (TPException ae) {
                        log.error(ae.getMessage());
                        errorHandler.sendEmptyMessage(0);
                    }
                }
            }.start();

        switch (searchType) {
            case TPConstant.SELECT_LOCATION_LIST:
                if (!TPApp.getUserSettings().isLocationKeyboard()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }

                //remove newly added location
                savedLocation.setVisibility(View.VISIBLE);
                if (Location.getManualLocations().size() > 0) {
                    savedLocation.setEnabled(true);
                    savedLocation.setBackground(getResources().getDrawable(R.drawable.btn_yellow));
                } else {
                    savedLocation.setBackground(getResources().getDrawable(R.drawable.btn_gray));
                    savedLocation.setEnabled(false);
                }
                break;

            case TPConstant.BODY_SEARCH_LIST:
                if (!TPApp.getUserSettings().isBodyKeyboard()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                } else {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                }
                break;

            case TPConstant.MAKE_SEARCH_LIST:
                if (!TPApp.getUserSettings().isMakeKeyboard()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                } else {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                }
                break;

            case TPConstant.COLOR_SEARCH_LIST:
                if (!TPApp.getUserSettings().isColorKeyboard()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                } else {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                }
                break;
        }

    }

    private void updateListItems(ArrayList<String> items) {
        // create the grid item mapping
        String[] from = new String[]{"search_title"};
        int[] to = new int[]{R.id.search_textview};

        // prepare the list of all records
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < items.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("search_title", items.get(i));
            fillMaps.add(map);
        }

        // fill in the grid_item layout
        SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.search_list_item, from, to);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    private void addNewLocation() {
        try {
            dialog = new Dialog(this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View inputDlgView = layoutInflater.inflate(R.layout.add_location_input_dialog, null, false);
            dialog.setTitle("Add New Location");
            dialog.setContentView(inputDlgView);
            dialog.show();

            Button enterBtn = (Button) inputDlgView.findViewById(R.id.add_location_input_dialog_enter_btn);
            inputText = (EditText) inputDlgView.findViewById(R.id.add_location_input_dialog_text_field);
            inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
            inputText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    inputText.setText("");
                    return true;
                }
            });

            inputText.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        TPUtility.hideSoftKeyboard(SearchLookupActivity.this);
                        return true;
                    }

                    return false;
                }
            });

            inputText.requestFocus();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }, 50);

            enterBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TPUtility.hideSoftKeyboard(SearchLookupActivity.this);

                    addLocation();
                }
            });

            Button cancelBtn = (Button) inputDlgView.findViewById(R.id.add_location_input_dialog_cancel_btn);
            cancelBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TPUtility.hideSoftKeyboard(SearchLookupActivity.this);

                    if (dialog.isShowing())
                        dialog.dismiss();
                }
            });

            return;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void editNewLocation(String locationText) {
        try {
            dialog = new Dialog(this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View inputDlgView = layoutInflater.inflate(R.layout.add_location_input_dialog, null, false);
            dialog.setTitle("Edit Location");
            dialog.setContentView(inputDlgView);
            dialog.show();

            Button enterBtn = (Button) inputDlgView.findViewById(R.id.add_location_input_dialog_enter_btn);
            inputText = (EditText) inputDlgView.findViewById(R.id.add_location_input_dialog_text_field);
            inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
            inputText.setText(locationText);

            inputText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            enterBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TPUtility.hideSoftKeyboard(SearchLookupActivity.this);

                    addLocation();
                }
            });

            Button cancelBtn = (Button) inputDlgView.findViewById(R.id.add_location_input_dialog_cancel_btn);
            cancelBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TPUtility.hideSoftKeyboard(SearchLookupActivity.this);

                    if (dialog.isShowing())
                        dialog.dismiss();
                }
            });

            return;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void addLocation() {

        Intent data = new Intent();
        String location = inputText.getText().toString();
        if (location.equals("") || location.trim().equals("")) {
            displayErrorMessage("Please enter location.");
            return;
        }

        if (dialog != null && dialog.isShowing())
            dialog.dismiss();

        data.putExtra("Location", location);
        data.putExtra("StreetNumber", streetNumberValue);
        data.putExtra("StreetPrefix", streetPrefixValue);
        data.putExtra("StreetSuffix", streetSuffixValue);
        data.putExtra("Direction", directionValue);
        data.putExtra("REDIRECT_LOCATION_FORM", true);

        // Add to new location
        try {
            Location loc = Location.getLocationByText(location);
            if (loc == null) {
                loc = new Location();
                loc.setId(Location.getLastInsertId() + 1);
                loc.setCustId(TPApp.custId);
                loc.setOrderNumber(0);
                loc.setLocation(location);
                loc.setZoneId(-1);
                loc.setIsManual("Y");

                Location.insertLocation(loc);
                /*DatabaseHelper.getInstance().openWritableDatabase();
                DatabaseHelper.getInstance().insertOrReplace(loc.getContentValues(), "locations");
                DatabaseHelper.getInstance().closeWritableDb();*/
                savedLocation.setEnabled(true);
                savedLocation.setBackground(getResources().getDrawable(R.drawable.btn_yellow));
            }
        } catch (Exception e) {
            Log.e("AddNewLocation", TPUtility.getPrintStackTrace(e));
        }

        if (getParent() == null) {
            setResult(Activity.RESULT_OK, data);
        } else {
            getParent().setResult(Activity.RESULT_OK, data);
        }

        finish();
    }


    private String getValue(String value) {
        String output = value.substring(14, value.length() - 1);
        return output;
    }

    @Override
    public void handleVoiceInput(String text) {

        if (text == null)
            return;

        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        if (text.contains("BACK") || text.contains("CLOSE")) {
            finish();

        } else if (text.contains("CLEAR")) {
            searchEditText.setText("");

        } else if (text.contains("GO") || text.contains("DONE") || text.contains("SELECT")) {
            selectFirstItem();
        } else {
            searchEditText.setText(text);
        }

    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {
        // TODO Auto-generated method stub
    }

    private void selectFirstItem() {
        if (listView.getCount() == 0) {
            return;
        }

        try {
            Map<String, String> selectedItem = (HashMap<String, String>) listView.getItemAtPosition(0);
            String firstItem = selectedItem.get("search_title");
            Intent data = new Intent();
            switch (searchType) {
                case TPConstant.STATES_SEARCH_LIST: {
                    String name = firstItem;
                    State state = State.getStateByName(name);
                    if (state != null) {
                        data.putExtra("STATE", state.getCode());
                        data.putExtra("StateId", state.getId());
                    } else {
                        data.putExtra("STATE", name);
                        data.putExtra("StateId", State.getStateIdByName(name));
                    }

                    break;
                }

                case TPConstant.EXP_SEARCH_LIST: {
                    data.putExtra("EXP", firstItem);
                    break;
                }

                case TPConstant.MAKE_SEARCH_LIST: {
                    String make = firstItem;
                    data.putExtra("MakeId", MakeAndModel.getMakeIdByName(make));
                    data.putExtra("MakeCode", MakeAndModel.getMakeCodeByName(make));
                    data.putExtra("MAKE", make);
                    break;
                }

                case TPConstant.BODY_SEARCH_LIST: {
                    String body = firstItem;
                    data.putExtra("BODY", body);
                    data.putExtra("BodyId", Body.getBodyIdByName(body));
                    data.putExtra("BodyCode", Body.getBodyCodeByName(body));
                    break;
                }

                case TPConstant.COLOR_SEARCH_LIST: {
                    String color = firstItem;
                    data.putExtra("COLOR", color);
                    data.putExtra("ColorId", Color.getColorIdByName(color));
                    data.putExtra("ColorCode", Color.getColorCodeByName(color));
                    break;
                }

                case TPConstant.SELECT_LOCATION_LIST: {
                    String location = firstItem;
                    data.putExtra("Location", location);
                    data.putExtra("StreetNumber", streetNumberValue);
                    data.putExtra("StreetPrefix", streetPrefixValue);
                    data.putExtra("StreetSuffix", streetSuffixValue);
                    data.putExtra("Direction", directionValue);
                    data.putExtra("REDIRECT_LOCATION_FORM", true);
                    break;
                }
            }

            if (getParent() == null) {
                setResult(Activity.RESULT_OK, data);
            } else {
                getParent().setResult(Activity.RESULT_OK, data);
            }

            finish();

        } catch (Exception exp) {
            Log.d(TPConstant.TAG, "Error " + exp.getMessage());
        }
    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
        // TODO Auto-generated method stub
    }

    private void saveKeyboardPrefs(int searchType, boolean isChecked, boolean checkPrefs) {
        switch (searchType) {
            case TPConstant.MAKE_SEARCH_LIST:
                if (!checkPrefs) {
                    TPApp.getUserSettings().setMakeKeyboard(isChecked);
                } else {
                    keyboardPopupChk.setChecked(TPApp.getUserSettings().isMakeKeyboard());
                }

                break;

            case TPConstant.BODY_SEARCH_LIST:
                if (!checkPrefs) {
                    TPApp.getUserSettings().setBodyKeyboard(isChecked);
                } else {
                    keyboardPopupChk.setChecked(TPApp.getUserSettings().isBodyKeyboard());
                }

                break;

            case TPConstant.COLOR_SEARCH_LIST:
                if (!checkPrefs) {
                    TPApp.getUserSettings().setColorKeyboard(isChecked);
                } else {
                    keyboardPopupChk.setChecked(TPApp.getUserSettings().isColorKeyboard());
                }

                break;
            default:
                break;
        }

        if (!checkPrefs) {
            TPApp.updateUserSettings();
            try {
                UserSetting userSettings = TPApp.getUserSettings();
                if (userSettings == null) {
                    userSettings = new UserSetting();
                }
                userSettings.setUserPrefs(UserSetting.getUserPrefsString(userSettings));
                syncUserSetting(userSettings);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void syncUserSetting(UserSetting userSettings) {
        try {
            if (TPApp.isServiceAvailable) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This will remove all location added by user manually
    private void removeAddedLocation() {
        try {
            Builder builder = new Builder(this);
            builder.setTitle("Delete Confirmation")
                    .setMessage("Are you sure you want to Delete All Saved Locations?")
                    .setCancelable(true)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Location.removeManuaLocation();
                                bindDataAtLoadingTime();
                                dialog.dismiss();
                                savedLocation.setEnabled(false);
                                savedLocation.setBackground(getResources().getDrawable(R.drawable.btn_gray));
                                Toast.makeText(getApplicationContext(), "Manual locations have been cleared successfully", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            builder.create().show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}