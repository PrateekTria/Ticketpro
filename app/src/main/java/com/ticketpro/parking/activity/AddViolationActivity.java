package com.ticketpro.parking.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.Comment;
import com.ticketpro.model.Duty;
import com.ticketpro.model.Violation;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.ViolationBLProcessor;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

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
public class AddViolationActivity extends BaseActivityImpl {
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayList<String> filteredArray = new ArrayList<String>();
    private EditText searchEditText;

    private ProgressDialog progressDialog;
    private Handler dataHandler;
    private Handler errorHandler;
    private ListView listView;
    private CheckBox keyboardPopupChk;
    private List<Violation> violationList;
    private SharedPreferences mPreferences;
    private List<Comment> commentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_violations);
            setLogger(AddViolationActivity.class.getName());
            setBLProcessor(new ViolationBLProcessor());
            setActiveScreen(this);

            mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

            searchEditText = (EditText) findViewById(R.id.add_violation_search_text);
            keyboardPopupChk = (CheckBox) findViewById(R.id.keyboard_chk);

            listView = (ListView) findViewById(R.id.add_violation_list);
            listView.setScrollbarFadingEnabled(false);
            listView.setFastScrollAlwaysVisible(true);
            listView.setFastScrollEnabled(true);
            searchEditText.requestFocus();
            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long arg3) {
                    Intent data = new Intent();
                    try {
                        Map<String, String> selectedItem = (HashMap) listView.getItemAtPosition(pos);
                        String selectedTitle = selectedItem.get("search_title");
                        Violation violation = getVilationByTitle(selectedTitle);

                        if (violation == null) {
                            violation = getVilationByDisplay(selectedTitle);
                        }

                        if (violation != null) {
                            data.putExtra("Fine", violation.getBaseFine());
                            data.putExtra("Violation", violation.getViolationDisplay());
                            data.putExtra("ViolationId", violation.getId());

                            String comments = violation.getDefaultComment();
                            int commentIds[] = StringUtil.getIntArray(comments);

                            if (commentIds.length == 1) {
                                Comment comment = Comment.getCommentById(commentIds[0]);
                                if (comment != null) {
                                    data.putExtra("CommentId", comment.getId());
                                    data.putExtra("DefaultComment", comment.getTitle());
                                }
                            } else if (commentIds.length > 1) {
                                data.putExtra("CommentIds", commentIds);
                            }
                        }
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    if (getParent() == null) {
                        setResult(RESULT_OK, data);
                    } else {
                        getParent().setResult(RESULT_OK, data);
                    }
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    finish();
                }
            });

            searchEditText.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String searchText = searchEditText.getText().toString();
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

            if (TPApp.getUserSettings() != null) {
                keyboardPopupChk.setChecked(TPApp.getUserSettings().isViolationKeyboard());
                keyboardPopupChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        TPApp.getUserSettings().setViolationKeyboard(isChecked);

                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.putBoolean(TPConstant.PREFS_KEY_SETTING_VIOLATION_KEYBOARD, isChecked);
                        editor.commit();
                    }
                });
            }

            dataHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    listItems.clear();

                    for (Violation violation : violationList) {
                        // This code is Changed by mohit 20/02/2023
                        if (violation.getHide()==null || violation.getHide().equals("")) {
                            if (violation.getViolationDisplay() != null) {
                                listItems.add(violation.getViolationDisplay());
                            } else {
                                listItems.add(violation.getTitle());
                            }
                        }
                        //End
                    }

                    updateListItems(listItems);

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            };

            errorHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (msg.what == 0) {
                        bindDataAtLoadingTime(1);
                    }
                }
            };

            bindDataAtLoadingTime();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }


    public void bindDataAtLoadingTime() {

            bindDataAtLoadingTime(0);
    }

    public void bindDataAtLoadingTime(final int reloadCount) {
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        progressDialog.setCancelable(true);
        new Thread() {
            public void run() {
                try {
                    Duty activeDuty = TPApp.getActiveDutyInfo();
                    if (activeDuty==null)
                        return;
                    if (activeDuty.isAllViolations()) {
                        violationList = ((ViolationBLProcessor) screenBLProcessor).getViolations();
                    } else {
                        violationList = ((ViolationBLProcessor) screenBLProcessor).getViolations(activeDuty.getViolationGroups());
                    }

                    try {
                        if (activeDuty.isAllComments()) {
                            commentList = ((ViolationBLProcessor) screenBLProcessor).getComments();
                        } else {
                            commentList = ((ViolationBLProcessor) screenBLProcessor).getComments(activeDuty.getCommentGroups());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dataHandler.sendEmptyMessage(0);

                } catch (TPException ae) {
                    log.error(ae.getMessage());
                    errorHandler.sendEmptyMessage(reloadCount);
                }
            }
        }.start();

        if (TPApp.getUserSettings() != null && !TPApp.getUserSettings().isViolationKeyboard()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        }
    }


    private void updateListItems(ArrayList<String> items) {
        String[] from = new String[]{"search_title"};
        int[] to = new int[]{R.id.search_textview};

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backAction(null);
    }

    private Violation getVilationByTitle(String title) {
        for (Violation violation : violationList) {
            if (violation.getTitle().equals(title)) {
                return violation;
            }
        }

        return null;
    }

    private Violation getVilationByDisplay(String display) {
        for (Violation violation : violationList) {
            if (violation.getViolationDisplay() != null
                    && violation.getViolationDisplay().equals(display))
                return violation;
        }

        return null;
    }

    @Override
    public void handleVoiceInput(String text) {
        if (text == null) return;

        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        if (text.contains("BACK") || text.contains("CLOSE")) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (text.contains("CLEAR")) {
            searchEditText.setText("");
        } else if (text.contains("GO") || text.contains("SELECT")) {
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

        Intent data = new Intent();
        try {
            Map<String, String> selectedItem = (HashMap) listView.getItemAtPosition(0);
            String selectedTitle = selectedItem.get("search_title");
            Violation violation = getVilationByTitle(selectedTitle);

            if (violation == null) {
                violation = getVilationByDisplay(selectedTitle);
                if (violation != null) {
                    data.putExtra("ViolationId", violation.getId());
                    data.putExtra("Fine", violation.getBaseFine());
                    data.putExtra("Violation", violation.getViolationDisplay());
                }
            } else {
                data.putExtra("ViolationId", violation.getId());
                data.putExtra("Fine", violation.getBaseFine());
                data.putExtra("Violation", violation.getTitle());
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        if (getParent() == null) {
            setResult(RESULT_OK, data);
        } else {
            getParent().setResult(RESULT_OK, data);
        }

        finish();
    }


    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
        // TODO Auto-generated method stub
    }

    public void backAction(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    private Comment getCommentByTitle(String title) {
        try {
            for (Comment comment : commentList) {
                if (comment.getTitle().equals(title)) {
                    return comment;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}