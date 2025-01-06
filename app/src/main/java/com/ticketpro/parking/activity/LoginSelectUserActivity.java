package com.ticketpro.parking.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.exception.TPException;
import com.ticketpro.model.Feature;
import com.ticketpro.model.Params;
import com.ticketpro.model.RequestPOJO;
import com.ticketpro.model.User;
import com.ticketpro.model.UserResponse;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.LoginBLProcessor;
import com.ticketpro.parking.service.TPAsyncTask;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPTask;
import com.ticketpro.util.TPUtility;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.RequiresApi;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 *
 */

public class LoginSelectUserActivity extends BaseActivityImpl {
    private ArrayList<User> users;
    private ArrayList<User> filteredUsers = new ArrayList<>();
    private Handler dataLoadHandler;
    private ListView listView;
    private EditText searchEditText;
    private CheckBox keyboardPopupChk;
    private SharedPreferences mPreferences;
    private ProgressDialog progressDialog;

    /**
     * Entry point of the Activity
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.login_user_list);
            setLogger(LoginSelectUserActivity.class.getName());
            setLogger(HomeActivity.class.getName());
            setBLProcessor(new LoginBLProcessor());

            isNetworkInfoRequired = true;
            mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

            listView = findViewById(R.id.login_user_list);
            keyboardPopupChk = findViewById(R.id.keyboard_chk);

            listView.setScrollbarFadingEnabled(false);
            listView.setFastScrollAlwaysVisible(true);
            listView.setFastScrollEnabled(true);
            Button btn = findViewById(R.id.btnRefresh);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshAction();
                }
            });
            keyboardPopupChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        updateKeyboardPrefs(TPConstant.PREFS_KEY_SETTING_HOME_KEYBOARD, isChecked);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            listView.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
                TPApp.setUserInfo(filteredUsers.get(arg2));

                Intent i = new Intent();
                i.setClass(LoginSelectUserActivity.this, LoginPasswordActivity.class);
                i.putExtra("username", filteredUsers.get(arg2).getUsername());
                i.putExtra("password", filteredUsers.get(arg2).getPassword());
                i.putExtra("userid", filteredUsers.get(arg2).getUserId());
                startActivity(i);

            });

            searchEditText = findViewById(R.id.searchText);
            searchEditText.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    // Abstract Method of TextWatcher Interface.
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Abstract Method of TextWatcher Interface.
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String searchText = searchEditText.getText().toString();
                    filteredUsers.clear();
                    searchText = searchText.toLowerCase();
                    if (searchText.length() == 0) {
                        filteredUsers.addAll(users);
                    } else {
                        for (User user : users) {
                            if (!StringUtil.isEmpty(user.getUsername())) {
                                if (user.getUsername().toLowerCase().contains(searchText)) {
                                    filteredUsers.add(user);
                                }
                            }
                        }
                    }
                    updateListItems(filteredUsers);
                }
            });


            dataLoadHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    if (users == null) {
                        return;
                    }
                    filteredUsers = new ArrayList<>();
                    filteredUsers.addAll(users);
                    updateListItems(filteredUsers);
                    if (progressDialog != null) {
                        progressDialog.hide();
                    }

                }
            };
            users = User.getUsers(TPConstant.MODULE_NAME);
            dataLoadHandler.sendEmptyMessage(1);

            //bindDataAtLoadingTime();
            setKeyboardStatus();
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void bindDataAtLoadingTime() throws Exception {
        // Show soft keyboard selection
        try {
            if (TPUtility.isN5ServiceAvailable(this)) {
                InputMethodManager imeManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(INPUT_METHOD_SERVICE);
                imeManager.showInputMethodPicker();
            }

            try {
                if (Feature.isFeatureAllowed(Feature.TRANSACTION_TIMEOUT)) {
                    TPApplication.getInstance().transactionTimeout = Integer
                            .parseInt(Feature.getFeatureValue(Feature.TRANSACTION_TIMEOUT));
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            TPAsyncTask task = new   TPAsyncTask(this, "Loading...", false);
            task.execute(new TPTask() {
                @Override
                public void execute() {
                    try {
                        users = User.getUsers(TPConstant.MODULE_NAME);
                        dataLoadHandler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        if (isServiceAvailable) {
                            try {
                                users = ((LoginBLProcessor) screenBLProcessor).getUsers();
                            } catch (TPException ae) {
                                log.error(ae.getMessage());
                            }
                        }

                        dataLoadHandler.sendEmptyMessage(1);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateListItems(ArrayList<User> users) {
        if (users!=null && users.size()>0) {
            String[] from = new String[]{"username"};
            int[] to = new int[]{R.id.user_name};

            List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < users.size(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("username", users.get(i).getUsername());
                fillMaps.add(map);
            }

            // fill in the grid_item layout
            SimpleAdapter adapter = new SimpleAdapter(LoginSelectUserActivity.this, fillMaps, R.layout.user_list_item, from, to);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void manualEntryAction(View view) {
        Intent intent = new Intent();
        intent.setClass(LoginSelectUserActivity.this, LoginUserPswdActivity.class);
        startActivity(intent);
    }

    public void backAction(View view) {
        finish();
    }

    public void searchAction(View view) {
        if (searchEditText.getVisibility() == View.GONE) {
            searchEditText.setVisibility(View.VISIBLE);
            TPUtility.showSoftKeyboard(this, searchEditText);
        } else {
            searchEditText.setVisibility(View.GONE);
        }
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

    // Saving users prefs in temp sharedPRefs
    private void updateKeyboardPrefs(String checkedType, boolean isChecked) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(checkedType, isChecked);
        editor.commit();
    }

    private void setKeyboardStatus() {
        try {
            boolean isKeyboardVisible = mPreferences.getBoolean(TPConstant.PREFS_KEY_SETTING_HOME_KEYBOARD, false);
            keyboardPopupChk.setChecked(isKeyboardVisible);
            if (isKeyboardVisible) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            } else {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshAction() {
        progressDialog = ProgressDialog.show(LoginSelectUserActivity.this, "", "Loading User List...");
        try {
            if (isServiceAvailable) {
                filteredUsers.clear();
                try {
                    RequestPOJO requestPOJO = new RequestPOJO();
                    Params params = new Params();
                    params.setCustId(TPApp.custId);
                    params.setFullSync(true);
                    requestPOJO.setParams(params);
                    requestPOJO.setMethod("getUsers");
                    ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
                    Call<UserResponse> userList = api.getUsers1(requestPOJO);
                    Response<UserResponse> request = userList.execute();
                    if (request.body() != null) {
                        users = (ArrayList<User>) request.body().getResult();
                        if (users != null) {
                            //User.removeAll();
                            User.insertUser(users);
                            Thread.sleep(50);
                            users = User.getUsers(TPConstant.MODULE_NAME);
                        }
                    }
                    //users = ((LoginBLProcessor) screenBLProcessor).getUsers();
                } catch (Exception ae) {
                    log.error(ae.getMessage());
                }
            }
            dataLoadHandler.sendEmptyMessage(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
