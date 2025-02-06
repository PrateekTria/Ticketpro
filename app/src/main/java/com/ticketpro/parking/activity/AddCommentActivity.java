package com.ticketpro.parking.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.Comment;
import com.ticketpro.model.Duty;
import com.ticketpro.model.Feature;
import com.ticketpro.model.TicketComment;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.ViolationBLProcessor;
import com.ticketpro.util.SpecialCharacterFilter;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.io.IOException;
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

public class AddCommentActivity extends BaseActivityImpl {

    private ListView listView;
    private EditText searchEditText;
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayList<String> filteredArray = new ArrayList<String>();

    private ProgressDialog progressDialog;
    private Handler errorHandler;
    private Handler dataHandler;
    private List<Comment> commentList;
    private int violationId;
    private long citationNumber;
    private Dialog dialog;
    private EditText inputText;
    private CheckBox newCommentPrivateChk;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private String audioFile;
    private TextView statusText;
    private ProgressBar progressBar;
    private boolean privateCommentsOnly;
    private CheckBox privateChk;
    private CheckBox keyboardPopupChk;
    private CheckBox addCommentPrivateChk;
    private TextView searchLabel;
    private LinearLayout commentTopBar;
    private Button voiceMemo;
    private Button addComment;
    private Button backButton;
    private Button clearButton;
    private int commentIds[];
    private ImageView voiceSearchIcon;
    private int commentIndex;
    private int violationIndex;
    private boolean requestEditComment;
    private String editCommentText;

    private SharedPreferences mPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_comment);
            setLogger(AddCommentActivity.class.getName());
            setBLProcessor(new ViolationBLProcessor());
            setActiveScreen(this);

            mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

            Intent intent = getIntent();
            violationId = intent.getIntExtra("ViolationId", 0);
            citationNumber = intent.getLongExtra("CitationNumber", 0);
            privateCommentsOnly = intent.getBooleanExtra("PrivateCommentsOnly", false);
            keyboardPopupChk = (CheckBox) findViewById(R.id.keyboard_chk);
            searchLabel = (TextView) findViewById(R.id.search_label);
            privateChk = (CheckBox) findViewById(R.id.add_comment_private_chk);
            searchEditText = (EditText) findViewById(R.id.add_comment_search_text);
            listView = (ListView) findViewById(R.id.add_comment_list);

            voiceSearchIcon = (ImageView) findViewById(R.id.voice_search_icon);
            commentTopBar = (LinearLayout) findViewById(R.id.commentTopBar);
            backButton = (Button) findViewById(R.id.back_comment_btn);
            clearButton = (Button) findViewById(R.id.clearButton);
            listView.setScrollbarFadingEnabled(false);
            listView.setFastScrollAlwaysVisible(true);
            listView.setFastScrollEnabled(true);

            commentIds = intent.getIntArrayExtra("CommentIds");
            commentIndex = intent.getIntExtra("CommentIndex", -1);
            violationIndex = intent.getIntExtra("ViolationIndex", -1);
            requestEditComment = intent.getBooleanExtra("requestEditComment", false);
            editCommentText = intent.getStringExtra("editCommentText");
            searchEditText.requestFocus();
            if (privateCommentsOnly) {
                privateChk.setChecked(true);
                privateChk.setEnabled(false);
            }

            // Check if voice memo is supported
            voiceMemo = (Button) findViewById(R.id.voice_memo_btn);
            addComment = (Button) findViewById(R.id.add_comment_btn);

            if (!Feature.isFeatureAllowed(Feature.VOICE_MEMOS)) {
                voiceMemo.setVisibility(View.GONE);
            }

            //Hide voice memos to disable it
            voiceMemo.setVisibility(View.GONE);

            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long arg3) {
                    Comment comment = null;
                    try {
                        Map<String, String> selectedItem = (HashMap) listView.getItemAtPosition(pos);
                        String selectedTitle = selectedItem.get("search_title");
                        comment = getComment(selectedTitle);
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    if (comment != null) {
                        Intent data = new Intent();
                        data.putExtra("PrivateComment", privateChk.isChecked());
                        data.putExtra("ViolationId", violationId);
                        data.putExtra("CommentId", comment.getId());
                        data.putExtra("Comment", comment.getTitle());

                        data.putExtra("CommentIndex", commentIndex);
                        data.putExtra("ViolationIndex", violationIndex);
                        if (getParent() == null) {
                            setResult(RESULT_OK, data);
                        } else {
                            getParent().setResult(RESULT_OK, data);
                        }
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        finish();
                    }
                }
            });

            if (commentIds == null) {
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                        try {
                            Map<String, String> selectedItem = (HashMap) listView.getItemAtPosition(pos);
                            String selectedTitle = selectedItem.get("search_title");
                            editCommentAction(selectedTitle);
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }

                        return true;
                    }
                });


                if (TPApp.getUserSettings() != null) {
                    keyboardPopupChk.setChecked(TPApp.getUserSettings().isCommentsKeyboard());
                    keyboardPopupChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            TPApp.getUserSettings().setCommentsKeyboard(isChecked);
                        }
                    });
                }

                searchEditText.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        // Abstract Method of TextWatcher Interface.
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Abstract Method of TextWatcher Interface.
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
            }

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


            dataHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    listItems.clear();

                    for (Comment comment : commentList) {
                        if (comment != null) {
                            if (!listItems.contains(comment.getTitle())) {
                                listItems.add(comment.getTitle());
                            }
                        }
                    }

                    updateListItems(listItems);

                    if (commentIds != null) {
                        optimizeViews();
                    }

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            };

            //bindDataAtLoadingTime();
            if (requestEditComment) {
                try {
                    if (editCommentText != null) {
                        editCommentAction(editCommentText);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void bindDataAtLoadingTime() {
        bindDataAtLoadingTime(0);
    }

    public void bindDataAtLoadingTime(final int reloadCount) {
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        new Thread() {
            public void run() {
                try {
                    if (commentIds == null) {
                        Duty activeDuty = TPApp.getActiveDutyInfo();
                        if (activeDuty.isAllComments()) {
                            commentList = ((ViolationBLProcessor) screenBLProcessor).getComments();
                        } else {
                            commentList = ((ViolationBLProcessor) screenBLProcessor).getComments(activeDuty.getCommentGroups());
                        }
                    } else {
                        commentList = ((ViolationBLProcessor) screenBLProcessor).getCommentsById(commentIds);
                    }

                    dataHandler.sendEmptyMessage(0);

                } catch (TPException e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                    errorHandler.sendEmptyMessage(reloadCount);
                }
            }
        }.start();


        if (TPApp.getUserSettings() != null && !TPApp.getUserSettings().isCommentsKeyboard()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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
        SimpleAdapter adapter = new SimpleAdapter(AddCommentActivity.this, fillMaps, R.layout.search_list_item, from, to);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    public void editCommentAction(String commentText) {
        try {
            dialog = new Dialog(AddCommentActivity.this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View inputDlgView = layoutInflater.inflate(R.layout.add_comment_input_dialog, null, false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(inputDlgView);
            dialog.show();

            TextView titleTextview = (TextView) inputDlgView.findViewById(R.id.add_comment_dialog_title);
            titleTextview.setText("Edit Comment");

            Button enterBtn = (Button) inputDlgView.findViewById(R.id.add_comment_input_dialog_enter_btn);
            inputText = (EditText) inputDlgView.findViewById(R.id.add_comment_input_dialog_text_field);


            inputText.setMovementMethod(ScrollingMovementMethod.getInstance());


            inputText.setText(commentText + " ");

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
                        TPUtility.hideSoftKeyboard(AddCommentActivity.this);
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
                    inputText.setSelection(inputText.getText().length());
                }
            }, 50);

            newCommentPrivateChk = (CheckBox) inputDlgView.findViewById(R.id.add_comment_private_chk);
            if (privateCommentsOnly && requestEditComment) {
                newCommentPrivateChk.setChecked(true);
                newCommentPrivateChk.setEnabled(false);
                inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(250)});
            } else if (!privateCommentsOnly && requestEditComment) {
                newCommentPrivateChk.setChecked(false);
                newCommentPrivateChk.setEnabled(false);
                inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
            } else {
                newCommentPrivateChk.setEnabled(false);
                inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
            }

            try {
                newCommentPrivateChk.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) newCommentPrivateChk).isChecked()) {
                            inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(250)});
                            inputText.setHint("Comment Text (Max - 250 Chars)");
                        } else {
                            inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
                            inputText.setHint("Comment Text (Max - 32 Chars)");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            enterBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TPUtility.hideSoftKeyboard(AddCommentActivity.this);
                    if (inputText.getText().toString().trim().length() > 32 && !newCommentPrivateChk.isChecked()) {
                        TPUtility.showErrorDialog(AddCommentActivity.this, TPConstant.MAX_COMMENT_CHAR_LIMIT);
                    } else {
                        addComment();
                    }
                }
            });

            Button cancelBtn = (Button) inputDlgView.findViewById(R.id.add_comment_input_dialog_cancel_btn);
            cancelBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TPUtility.hideSoftKeyboard(AddCommentActivity.this);

                    if (dialog.isShowing())
                        dialog.dismiss();

                    if (requestEditComment) {
                        finish();
                    }
                }
            });
            if (requestEditComment) {
                dialog.setCanceledOnTouchOutside(false);
            }
            return;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void addCommentAction(View view) {
        try {
            dialog = new Dialog(AddCommentActivity.this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View inputDlgView = layoutInflater.inflate(R.layout.add_comment_input_dialog, null, false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(inputDlgView);
            dialog.show();

            TextView titleTextview = (TextView) inputDlgView.findViewById(R.id.add_comment_dialog_title);
            titleTextview.setText("Add New Comment");

            Button enterBtn = (Button) inputDlgView.findViewById(R.id.add_comment_input_dialog_enter_btn);
            inputText = (EditText) inputDlgView.findViewById(R.id.add_comment_input_dialog_text_field);
            inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
            inputText.setFilters(new InputFilter[]{ new SpecialCharacterFilter() });
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
                        TPUtility.hideSoftKeyboard(AddCommentActivity.this);
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

            newCommentPrivateChk = (CheckBox) inputDlgView.findViewById(R.id.add_comment_private_chk);
            if (privateCommentsOnly) {
                newCommentPrivateChk.setChecked(true);
                newCommentPrivateChk.setEnabled(false);
                inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(250)});
            }

            try {
                newCommentPrivateChk.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) newCommentPrivateChk).isChecked()) {
                            inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(250)});
                            inputText.setHint("Comment Text (Max - 250 Chars)");
                        } else {
                            inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
                            inputText.setHint("Comment Text (Max - 32 Chars)");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            enterBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TPUtility.hideSoftKeyboard(AddCommentActivity.this);
                    if (inputText.getText().toString().length() > 32 && !newCommentPrivateChk.isChecked()) {
                        TPUtility.showErrorDialog(AddCommentActivity.this, TPConstant.MAX_COMMENT_CHAR_LIMIT);
                    } else {
                        addComment();
                    }
                }
            });

            Button cancelBtn = (Button) inputDlgView.findViewById(R.id.add_comment_input_dialog_cancel_btn);
            cancelBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TPUtility.hideSoftKeyboard(AddCommentActivity.this);

                    if (dialog.isShowing())
                        dialog.dismiss();
                }
            });
            return;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void addVoiceAction(View view) {
        try {
            dialog = new Dialog(AddCommentActivity.this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            final View inputDlgView = layoutInflater.inflate(R.layout.voice_memo_dialog, null, false);
            dialog.setTitle("New Voice Memo");
            dialog.setContentView(inputDlgView);
            dialog.show();

            Button doneBtn = (Button) inputDlgView.findViewById(R.id.audio_stop_btn);
            doneBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    addVoiceMemo();
                }
            });
            doneBtn.setEnabled(false);
            doneBtn.setBackgroundResource(R.drawable.btn_disabled);

            progressBar = (ProgressBar) inputDlgView.findViewById(R.id.voice_memo_progressbar);
            progressBar.setVisibility(View.GONE);
            statusText = (TextView) inputDlgView.findViewById(R.id.voice_memo_status_text);

            Button startStopBtn = (Button) inputDlgView.findViewById(R.id.audio_play_btn);
            startStopBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button startStopBtn = (Button) inputDlgView.findViewById(R.id.audio_play_btn);
                    Button doneBtn = (Button) inputDlgView.findViewById(R.id.audio_stop_btn);

                    if (isRecording) {
                        progressBar.setVisibility(View.GONE);
                        statusText.setText("Tap on MIC to play recording...");
                        startStopBtn.setText("Start Rec");
                        doneBtn.setEnabled(true);
                        doneBtn.setBackgroundResource(R.drawable.btn_blue);
                        stopRecording();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        statusText.setText("Recording...");
                        startStopBtn.setText("Stop Rec");
                        doneBtn.setEnabled(false);
                        doneBtn.setBackgroundResource(R.drawable.btn_disabled);
                        startRecording();
                    }
                }
            });

            Button cancelBtn = (Button) inputDlgView.findViewById(R.id.audio_cancel_btn);
            cancelBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopPlaying();
                    stopRecording();

                    if (dialog.isShowing())
                        dialog.dismiss();
                }
            });

            ImageView micIcon = (ImageView) inputDlgView.findViewById(R.id.voice_memo_mic);
            micIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (audioFile == null)
                        return;

                    if (isPlaying) {
                        progressBar.setVisibility(View.GONE);
                        stopPlaying();
                        statusText.setText("Tap on MIC to play recording...");
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        statusText.setText("Playing...");
                        playRecording();
                    }
                }
            });

            return;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void startRecording() {
        if (audioFile == null) {
            int voiceCommentId = (int) (TicketComment.getNextPrimaryId());
            audioFile = TPUtility.prefixZeros(citationNumber, 8) + "-VOICE-" + TPUtility.prefixZeros(voiceCommentId, 2) + ".mp3";
        }

        try {
            isRecording = true;
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(TPUtility.getVoiceMemosFolder() + audioFile);
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            log.error("Error while recording audio. " + e.getMessage());
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.reset();
            recorder.release();
        }

        isRecording = false;
    }

    private void playRecording() {
        if (audioFile == null || isPlaying || isRecording)
            return;

        player = new MediaPlayer();
        try {
            player.setDataSource(TPUtility.getVoiceMemosFolder() + audioFile);
            player.prepare();
            player.start();
            isPlaying = true;
        } catch (IOException e) {
            log.error("Error playing recording.");
        }

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                progressBar.setVisibility(View.GONE);
                statusText.setText("Tap on MIC to play recording...");
            }
        });
    }

    private void stopPlaying() {
        isPlaying = false;
        if (player != null) {
            player.stop();
            player.reset();
            player.release();
        }
    }

    private void addVoiceMemo() {
        if (dialog.isShowing())
            dialog.dismiss();

        Intent data = new Intent();
        data.putExtra("VoiceComment", true);
        data.putExtra("AudioFile", audioFile);
        data.putExtra("ViolationId", violationId);

        if (getParent() == null) {
            setResult(RESULT_OK, data);
        } else {
            getParent().setResult(RESULT_OK, data);
        }

        finish();
    }

    private void addComment() {
        try {
            String newComment = inputText.getText().toString().toUpperCase();
            if (newComment.equals("") || newComment.trim().equals("")) {
                displayErrorMessage("Please enter comment.");
                return;
            }

            Intent data = new Intent();
            data.putExtra("PrivateComment", newCommentPrivateChk.isChecked());
            data.putExtra("NewComment", true);
            data.putExtra("Comment", newComment);
            data.putExtra("ViolationId", violationId);

            data.putExtra("CommentIndex", commentIndex);
            data.putExtra("ViolationIndex", violationIndex);
            //Add to comment table
            try {
                Comment comment = Comment.getCommentByText(newComment);
                if (requestEditComment) {
                    Comment commentObj = Comment.getCommentsByText(newComment);
                    if (comment == null && commentObj != null) {
                        data.putExtra("duplicateComment", true);
                    }
                } else {
                    if (comment == null) {
                        comment = new Comment();
                        comment.setId(Comment.getLastInsertId() + 1);
                        comment.setCustId(TPApp.getCustId());
                        comment.setOrderNumber(100);
                        comment.setTitle(newComment);
                        comment.setCode("NCMMT-" + comment.getId());
                        Comment.insertComment(comment);

                        /*DatabaseHelper.getInstance().openWritableDatabase();
                        DatabaseHelper.getInstance().insertOrReplace(comment.getContentValues(), "comments");
                        DatabaseHelper.getInstance().closeWritableDb();*/
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

            if (dialog.isShowing())
                dialog.dismiss();


            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            finish();
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private Comment getComment(String title) {
        for (Comment comment : commentList) {
            if (comment.getTitle().equals(title)) {
                return comment;
            }
        }

        return null;
    }

    @Override
    public void handleVoiceInput(String text) {
        if (text == null) {
            return;
        }

        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        if (text.contains("BACK") || text.contains("CLOSE")) {
            finish();
        } else if (text.contains("ADD COMMENT") || text.contains("NEW COMMENT")) {
            addComment();

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

        Comment comment = null;
        try {
            Map<String, String> selectedItem = (HashMap) listView.getItemAtPosition(0);
            String selectedTitle = selectedItem.get("search_title");
            comment = getComment(selectedTitle);
        } catch (Exception e) {
            log.error("Error " + e.getMessage());
        }

        if (comment != null) {
            Intent data = new Intent();
            data.putExtra("PrivateComment", privateChk.isChecked());
            data.putExtra("CommentId", comment.getId());
            data.putExtra("Comment", comment.getTitle());
            data.putExtra("ViolationId", violationId);

            if (getParent() == null) {
                setResult(RESULT_OK, data);
            } else {
                getParent().setResult(RESULT_OK, data);
            }

            finish();
        }
    }


    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backAction(null);
    }

    public void backAction(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    //Hide Views for multiple comments
    private void optimizeViews() {
        try {
            backButton.setVisibility(View.VISIBLE);
            keyboardPopupChk.setVisibility(View.GONE);
            addComment.setVisibility(View.GONE);
            searchEditText.setVisibility(View.GONE);
            commentTopBar.setVisibility(View.GONE);
            voiceSearchIcon.setVisibility(View.GONE);
            searchLabel.setVisibility(View.GONE);
            privateChk.setVisibility(View.GONE);
            listView.setVerticalScrollBarEnabled(false);
            clearButton.setVisibility(View.GONE);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            if (voiceMemo.isEnabled()) {
                voiceMemo.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    //Clears added custom comments by user
    //Removes user added comment from comment list
    public void clearAction(View view) {

        startActivity(new Intent(AddCommentActivity.this,DeleteCustomComment.class));
        /*try {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Clear Custom Comments")
                    .setMessage("This will clear your custom comments, proceed?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // To-Do - Remove custom comments having id 0
                            try {
                                Comment.removeAddedComments();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            bindDataAtLoadingTime();

                            dialog.dismiss();
                        }
                    }).show();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindDataAtLoadingTime();

    }
}
