package com.ticketpro.parking.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.ticketpro.exception.TPException;
import com.ticketpro.model.Comment;
import com.ticketpro.model.Feature;
import com.ticketpro.model.RepeatOffender;
import com.ticketpro.model.RepeatOffenderHandler;
import com.ticketpro.model.RepeatOffendersFromService;
import com.ticketpro.model.SyncData;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketCommentTemp;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.model.TicketViolationTemp;
import com.ticketpro.model.Violation;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.ViolationBLProcessor;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.parking.proxy.ProxyImpl;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */
public class ViolationsActivity extends BaseActivityImpl implements RepeatOffenderHandler {

    private final int REQUEST_COMMENT = 1;
    private final int REQUEST_VIOLATION = 2;
    private Button addViolationBtn;
    private Button backBtn;
    private CheckBox stickyCommentsChk;
    private CheckBox stickyViolationsChk;
    private ExpandListAdapter expandAdapter;
    private ExpandableListView expandListview;
    private Ticket activeTicket;
    private boolean editCommentsOnly;
    private SharedPreferences mPreferences;
    RepeatOffender repeatOffender;
    private Violation violationexp = null;
    private ProgressDialog progressDialog;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.ticket_violations);
            setLogger(ViolationsActivity.class.getName());
            setBLProcessor(new ViolationBLProcessor());
            setActiveScreen(this);
            isNetworkInfoRequired = true;

            mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

            stickyCommentsChk = (CheckBox) findViewById(R.id.chk_sticky_comments);
            stickyCommentsChk.setChecked(TPApplication.getInstance().stickyComments);
            stickyCommentsChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    TPApplication.getInstance().stickyComments = isChecked;
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean(TPConstant.PREFS_KEY_STICKY_COMMENTS, TPApp.stickyComments);
                    editor.commit();
                }
            });

            stickyViolationsChk = (CheckBox) findViewById(R.id.chk_sticky_violations);
            stickyViolationsChk.setChecked(TPApplication.getInstance().stickyViolations);
            stickyViolationsChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    TPApplication.getInstance().stickyViolations = isChecked;
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean(TPConstant.PREFS_KEY_STICKY_VIOLATIONS, TPApp.stickyViolations);
                    editor.apply();

                    try {
                        if (!isChecked) {
                            TPApp.setStickyViolation(null);

                        } else {
                            TPApp.setStickyViolation(activeTicket.getTicketViolations().get(0));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            editCommentsOnly = getIntent().getBooleanExtra("EditCommentsOnly", false);

            if (editCommentsOnly) {
                activeTicket = TPApp.getSharedTicket();
            } else {
                activeTicket = TPApp.getActiveTicket();
            }
            if (activeTicket == null) {
                finish();
                return;
            }

            addViolationBtn = (Button) findViewById(R.id.violation_add_violation_btn);
            addViolationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addViolationAction(view);
                }
            });

            backBtn = (Button) findViewById(R.id.violation_back_btn);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    backAction(view);
                }
            });


            if (TPApp.getStickyViolation() != null) {
                if ((activeTicket.getTicketViolations().size() == 0)) {

                    activeTicket.getTicketViolations().add(0, TPApp.getStickyViolation());

                }

            }

            if (activeTicket.isChalked() && TPApp.getStickyViolation() != null) {
                activeTicket.getTicketViolations().clear();
                activeTicket.getTicketViolations().add(TPApp.getStickyViolation());
            }

            expandListview = (ExpandableListView) findViewById(R.id.violations_listview);
            expandAdapter = new ExpandListAdapter(ViolationsActivity.this, activeTicket.getTicketViolations());
            expandListview.setAdapter(expandAdapter);


            expandListview.setOnGroupClickListener(new OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    return true;
                }
            });


            if (editCommentsOnly) {
                addViolationBtn.setVisibility(View.GONE);
            } else {
                if (activeTicket.getTicketViolations().size() == 0 && TPApp.getStickyViolation() == null) {
                    Intent i = new Intent();
                    i.setClass(ViolationsActivity.this, AddViolationActivity.class);
                    startActivityForResult(i, REQUEST_VIOLATION);
                    return;
                }
            }

            expandAll();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void bindDataAtLoadingTime() {

    }

    @Override
    public void onClick(View v) {

    }

    public void addViolationAction(View view) {
        if (checkViolationLimits()) {
            displayErrorMessage("Exceeded max violations limit.");
            return;
        }

        Intent i = new Intent();
        i.setClass(ViolationsActivity.this, AddViolationActivity.class);
        startActivityForResult(i, REQUEST_VIOLATION);
    }

    @Override
    public void onBackPressed() {
        backAction(null);
        super.onBackPressed();

    }

    public void backAction(View view) {
        setResult(RESULT_OK);
        finish();
    }

    private boolean checkViolationLimits() {
        int maxViolations = 0;

        if (Feature.isFeatureAllowed(Feature.MAX_VIOLATIONS)) {
            String value = Feature.getFeatureValue(Feature.MAX_VIOLATIONS);
            try {
                maxViolations = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                log.error(e.getMessage());
            }
        }

        if (maxViolations > 0 && activeTicket.getTicketViolations().size() >= maxViolations) {
            addViolationBtn.setEnabled(false);
            addViolationBtn.setBackgroundResource(R.drawable.btn_disabled);
            return true;
        }


        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED && activeTicket.getTicketViolations().size() == 0) {
            finish();
            return;
        } else if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case REQUEST_VIOLATION:
                Violation violation = null;
                int violationId = data.getIntExtra("ViolationId", 0);
                double fine = data.getDoubleExtra("Fine", 0);

                checkViolationLimits();

                if (isDuplicateViolation(violationId)) {
                    displayErrorMessage("Selected violation already exists. Please select another.");
                    break;
                }

                try {
                    violation = Violation.getViolationById(violationId);
                } catch (SQLException e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }

                if (violation == null || violationId == 0) {
                    return;
                }

                if (violation.isRepeatOffender()) {
                    fine = getRepeatOffenderFine(violation);

                    log.info("====getRepeatOffenderFine call ====");
                }

                TicketViolation tv = new TicketViolation();
                TicketViolationTemp tvt = new TicketViolationTemp();

                if (TPApp.activeDutyInfo.getAllowTicket().equals("W")) {
                    tv.setWarning(true);
                    tvt.setWarning(true);
                }
                tv.setTicketId(activeTicket.getTicketId());
                tv.setViolationDesc(violation.getTitle());
                tv.setViolationDisplay(violation.getViolationDisplay());
                tv.setViolationCode(violation.getCode());
                tv.setViolationId(violationId);
                tv.setCitationNumber(activeTicket.getCitationNumber() + 1);
                tv.setFine(fine);
                tv.setRequiredComments(violation.getRequiredComments());
                tv.setRequiredPhotos(violation.getRequiredPhotos());
                tv.setChalktimerequired(violation.isChalktimerequired());

                tvt.setTicketId(activeTicket.getTicketId());
                tvt.setViolationDesc(violation.getTitle());
                tvt.setViolationDisplay(violation.getViolationDisplay());
                tvt.setViolationCode(violation.getCode());
                tvt.setViolationId(violationId);
                tvt.setCitationNumber(activeTicket.getCitationNumber() + 1);
                tvt.setFine(fine);
                tvt.setRequiredComments(violation.getRequiredComments());
                tvt.setRequiredPhotos(violation.getRequiredPhotos());
                tvt.setChalktimerequired(violation.isChalktimerequired());
                if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                    TicketViolationTemp.insertTicketViolationTemp(tvt);
                }


                activeTicket.setFine(fine);
                activeTicket.getTicketViolations().add(tv);

                if (activeTicket.getTicketViolations().size() > 0) {
                    stickyViolationsChk.setEnabled(true);
                    stickyCommentsChk.setEnabled(true);
                }

                if (TPApp.stickyViolations) {
                    if (activeTicket.getTicketViolations().size() == 1) {
                        TPApp.setStickyViolation(activeTicket.getTicketViolations().get(0));

                    }
                }

                if (TPApp.stickyComments) {
                    try {
                        TicketComment tc = TPApp.getStickyComment();

                        if (activeTicket.getTicketViolations().size() == 1 && tc != null) {
                            activeTicket.getTicketViolations().get(0).getTicketComments().add(tc);
                        }
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
                if (Feature.isFeatureAllowed(TPConstant.PARK_KEEP_STICKY_VIOLATION)) {
                    if (activeTicket.getTicketViolations().size() > 0) {
                        stickyViolationsChk.setChecked(true);
                        stickyCommentsChk.setChecked(true);

                    }
                }
                addComments(data);
                break;

            case REQUEST_COMMENT:
                if (data.hasExtra("moreComments")) {
                    int comments[] = data.getIntArrayExtra("moreComments");
                    int violId = data.getIntExtra("ViolationId", 0);
                    int commentIndex = data.getIntExtra("CommentIndex", -1);
                    int violationIndex = data.getIntExtra("ViolationIndex", -1);
                    for (int i = 0; i < comments.length; i++) {
                        Intent dataIntent = getUpdatedIntent(violId, commentIndex, violationIndex, comments[i]);
                        addComments(dataIntent);
                    }
                } else {
                    addComments(data);
                }

                break;

        }

        expandAll();
    }

    private double getRepeatOffenderFineUsingVin(Violation violation){
        //  RepeatOffender repeatOffender;
        log.debug("Current Plate: " + activeTicket.getPlate());
        int count = 0;
        double fine = 0;

        try {//&& violation.getRepeatOffender().equalsIgnoreCase("Y")
            if (TPApp.isServiceAvailable && !activeTicket.getVin().equalsIgnoreCase("") && !activeTicket.getStateCode().equalsIgnoreCase("")) {
                RepeatOffendersFromService repeatOffendersFromService = new RepeatOffendersFromService();

                // repeatOffendersFromService.setCustId(TPApp.getCustId());
                repeatOffendersFromService.setPlate(activeTicket.getVin());
                repeatOffendersFromService.setViolationId(violation.getId());
                repeatOffendersFromService.setStateCode(activeTicket.getStateCode());

                if (TPApp.isServiceAvailable) {
                    progressDialog = ProgressDialog.show(this, "", "Searching Repeat Offender...");
                    progressDialog.setCancelable(false);
                    ProxyImpl.repeatOffenderFromService1(violation, TPApp.custId, activeTicket.getPlate(), violation.getId(), activeTicket.getStateCode(), this);
                }                // repeatOffender = ((ViolationBLProcessor) screenBLProcessor).repeatOffenderFromService(repeatOffendersFromService);
                //repeatOffender =  ((ViolationBLProcessor) screenBLProcessor).repeatOffenderFromService(TPApp.custId, activeTicket.getPlate(), violation.getId(), activeTicket.getStateCode());
            } else {
                if (activeTicket.getPlate().equalsIgnoreCase("")) {
                    new iOSDialogBuilder(ViolationsActivity.this)
                            .setSubtitle("Plate is empty, please enter plate first and select violation.")
                            .setPositiveListener(getString(R.string._ok), dialog -> {
                                dialog.dismiss();
                                log.debug("Plate is empty, please enter plate first and select violation.");
                                activeTicket.getTicketViolations().clear();
                                finish();
                            })
                            .build().show();
                } else if (!TPApp.isServiceAvailable) {
                    new iOSDialogBuilder(ViolationsActivity.this)
                            .setSubtitle("This device is currently off-line.Repeat Offender validation has been disabled.")
                            .setPositiveListener(getString(R.string._ok), dialog -> dialog.dismiss())
                            .build().show();
                    log.debug("This device is currently off-line.Repeat Offender validation has been disabled.");

                }
                //repeatOffender = RepeatOffender.searchRepeatOffender(activeTicket.getPlate(), violation.getId(), activeTicket.getStateCode());
            }
            if (repeatOffender != null) {
                count = repeatOffender.getCount();
            }

            if (count >= 2) {
                fine = violation.getFine2();
            } else if (count >= 1) {
                fine = violation.getFine1();
            } else {
                fine = violation.getBaseFine();
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return fine;
    }
    private double getRepeatOffenderFine(Violation violation) {
        //  RepeatOffender repeatOffender;
        log.debug("Current Plate: " + activeTicket.getPlate());
        int count = 0;
        double fine = 0;

        try {//&& violation.getRepeatOffender().equalsIgnoreCase("Y")
            if (TPApp.isServiceAvailable && !activeTicket.getPlate().equalsIgnoreCase("") && !activeTicket.getStateCode().equalsIgnoreCase("")) {
                RepeatOffendersFromService repeatOffendersFromService = new RepeatOffendersFromService();

                // repeatOffendersFromService.setCustId(TPApp.getCustId());
                repeatOffendersFromService.setPlate(activeTicket.getPlate());
                repeatOffendersFromService.setViolationId(violation.getId());
                repeatOffendersFromService.setStateCode(activeTicket.getStateCode());

                if (TPApp.isServiceAvailable) {
                    progressDialog = ProgressDialog.show(this, "", "Searching Repeat Offender...");
                    progressDialog.setCancelable(false);
                    ProxyImpl.repeatOffenderFromService1(violation, TPApp.custId, activeTicket.getPlate(), violation.getId(), activeTicket.getStateCode(), this);
                }                // repeatOffender = ((ViolationBLProcessor) screenBLProcessor).repeatOffenderFromService(repeatOffendersFromService);
                //repeatOffender =  ((ViolationBLProcessor) screenBLProcessor).repeatOffenderFromService(TPApp.custId, activeTicket.getPlate(), violation.getId(), activeTicket.getStateCode());
            } else {
                if (activeTicket.getPlate().equalsIgnoreCase("")) {
                    new iOSDialogBuilder(ViolationsActivity.this)
                            .setSubtitle("Plate is empty, please enter plate first and select violation.")
                            .setPositiveListener(getString(R.string._ok), dialog -> {
                                dialog.dismiss();
                                log.debug("Plate is empty, please enter plate first and select violation.");
                                activeTicket.getTicketViolations().clear();
                                finish();
                            })
                            .build().show();
                } else if (!TPApp.isServiceAvailable) {
                    new iOSDialogBuilder(ViolationsActivity.this)
                            .setSubtitle("This device is currently off-line.Repeat Offender validation has been disabled.")
                            .setPositiveListener(getString(R.string._ok), dialog -> dialog.dismiss())
                            .build().show();
                    log.debug("This device is currently off-line.Repeat Offender validation has been disabled.");

                }
                //repeatOffender = RepeatOffender.searchRepeatOffender(activeTicket.getPlate(), violation.getId(), activeTicket.getStateCode());
            }
            if (repeatOffender != null) {
                count = repeatOffender.getCount();
            }

            if (count >= 2) {
                fine = violation.getFine2();
            } else if (count >= 1) {
                fine = violation.getFine1();
            } else {
                fine = violation.getBaseFine();
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return fine;
    }


    // Add comment for violation - Default comment will be added for each
    // violations
    private void addComments(Intent data) {
        TicketComment tc = new TicketComment();
        TicketCommentTemp tct = new TicketCommentTemp();
        int violationId = data.getIntExtra("ViolationId", 0);
        boolean isVoiceComment = false;
        boolean isPrivateComment = false;

        if (data.hasExtra("CommentIds")) {
            int commentIds[] = data.getIntArrayExtra("CommentIds");
			/*String havePrivateComment = data.getBooleanExtra("PrivateComment", false) ? "Y" : "N" ;
			boolean isPrivate = false;
			if(havePrivateComment.equalsIgnoreCase("Y"))
				isPrivate = true;*/
            requestComments(activeTicket.getTicketId(), violationId, commentIds, -1, -1, false, null, false);
            return;
        }

        String commentText = data.getStringExtra("Comment");
        if (data.hasExtra("DefaultComment")) {
            commentText = data.getStringExtra("DefaultComment");
        }

        if (data.hasExtra("VoiceComment")) {
            isVoiceComment = data.getBooleanExtra("VoiceComment", false);
            commentText = data.getStringExtra("AudioFile");
            tc.setAudioFile(commentText);
            tct.setAudioFile(commentText);
        }

        tc.setVoiceComment(isVoiceComment);
        tct.setVoiceComment(isVoiceComment);
        tc.setIsPrivate(data.getBooleanExtra("PrivateComment", false) ? "Y" : "N");
        tct.setIsPrivate(data.getBooleanExtra("PrivateComment", false) ? "Y" : "N");

        // check if private comment already added
        if (data.getBooleanExtra("PrivateComment", false)) {
            isPrivateComment = true;

            if (!Feature.isFeatureAllowed(Feature.MAX_PRIVATE_COMMENTS)) {
                if (isDuplicatePrivateComment(violationId)) {
                    displayErrorMessage("Private comment for this violation is already exists.");
                    return;
                }
            }
        }

        if (data.hasExtra("NewComment") || data.hasExtra("VoiceComment")) {
            tc.setComment(commentText);
            tct.setComment(commentText);
        } else {
            int commentId = data.getIntExtra("CommentId", 0);
            if (commentId != 0) {
                Comment comment;
                try {
                    comment = Comment.getCommentById(commentId);
                    if (comment != null) {
                        tc.setTicketId(activeTicket.getTicketId());
                        tc.setComment(commentText);
                        tc.setCommentId(commentId);

                        tct.setTicketId(activeTicket.getTicketId());
                        tct.setComment(commentText);
                        tct.setCommentId(commentId);

                    }
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
            } else {
                return;
            }
        }
        TicketCommentTemp.insertTicketCommentTemp(tct);
        if (violationId == 0) {
            return;
        }
        if (data.hasExtra("CommentIndex") || data.hasExtra("ViolationIndex")) {
            int commentIndex = data.getIntExtra("CommentIndex", -1);
            int violationIndex = data.getIntExtra("ViolationIndex", -1);
            boolean isDuplicate = data.getBooleanExtra("duplicateComment", false);
            if (violationIndex != -1 || commentIndex != -1) {
                if (!isDuplicate) {
                    replaceCommentByIndex(data.getIntExtra("ViolationIndex", 0), data.getIntExtra("CommentIndex", 0));
                }
            }
        }

        for (TicketViolation violation : activeTicket.getTicketViolations()) {
            if (violation.getViolationId() == violationId) {
                tc.setCitationNumber(violation.getCitationNumber());
                List<TicketComment> commentList = violation.getTicketComments();

                if (isPrivateComment) {
                    int maxPrivateComments = 0;
                    if (Feature.isFeatureAllowed(Feature.MAX_PRIVATE_COMMENTS)) {
                        String value = Feature.getFeatureValue(Feature.MAX_PRIVATE_COMMENTS);
                        try {
                            maxPrivateComments = Integer.parseInt(value);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }

                    if (maxPrivateComments > 0 && TPUtility.totalPrivateComments(commentList) >= maxPrivateComments) {
                        displayErrorMessage("Exceeded max private comments limit.");
                        break;
                    }
                } else {
                    int maxComments = 0;
                    if (Feature.isFeatureAllowed(Feature.MAX_COMMENTS)) {
                        String value = Feature.getFeatureValue(Feature.MAX_COMMENTS);
                        try {
                            maxComments = Integer.parseInt(value);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }

                    if (maxComments > 0 && TPUtility.totalPublicComments(commentList) >= maxComments) {
                        displayErrorMessage("Exceeded max public comments limit.");
                        break;
                    }
                }

                // check for duplicate comment
                if (isDuplicateNewComment(commentList, tc.getComment())) {
                    if (!data.hasExtra("DefaultComment")) {
                        displayErrorMessage("Comment already exists.");
                    }

                    break;
                }

                if (TPApp.stickyComments) {
                    if (activeTicket.getTicketViolations().size() == 1) {
                        TPApp.setStickyComment(tc);
                    }
                }

                if (data.hasExtra("NewComment") || data.hasExtra("VoiceComment")) {
                    if (!isEmptyComment(tc)) {
                        commentList.add(tc);
                    }
                } else {
                    // check for duplicate comment
                    if (isDuplicateComment(commentList, data.getIntExtra("CommentId", 0))) {
                        displayErrorMessage("Selected comment already exists. Please select another.");
                        break;
                    }

                    if (!isEmptyComment(tc)) {
                        commentList.add(tc);
                    }
                }

                if (editCommentsOnly) {
                    editComment(tc, violationId);
                }
                expandAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    private boolean isEmptyComment(TicketComment comment) {
        return comment == null || StringUtil.isEmpty(comment.getComment());
    }

    private void expandAll() {
        for (int i = 0; i < activeTicket.getTicketViolations().size(); i++) {
            expandListview.expandGroup(i);
        }
    }

    private void editComment(TicketComment tc, int violationId) {
        ArrayList<SyncData> syncDataList = new ArrayList<SyncData>();
        ArrayList<TicketComment> ticketComments = new ArrayList<TicketComment>();

        int ticketCommentId = TicketComment.getNextPrimaryId();
        tc.setTicketId(activeTicket.getTicketId());
        tc.setCitationNumber(activeTicket.getCitationNumber());
        tc.setTicketCommentId(ticketCommentId);
        tc.setCustId(TPApp.custId);

        try {
            TicketComment.insertTicketComment(tc);
			/*DatabaseHelper.getInstance().openWritableDatabase();
			DatabaseHelper.getInstance().insertOrReplace(tc.getContentValues(), "ticket_comments");*/
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        SyncData syncCommentData = new SyncData();
        syncCommentData.setActivity("INSERT");
        syncCommentData.setPrimaryKey(ticketCommentId + "");
        syncCommentData.setActivityDate(new Date());
        syncCommentData.setCustId(TPApp.custId);
        syncCommentData.setTableName(TPConstant.TABLE_TICKET_COMMENTS);
        syncCommentData.setStatus("Pending");

        boolean result = false;
        if (isServiceAvailable) {
            ticketComments.add(tc);
            try {
                result = ((ViolationBLProcessor) screenBLProcessor).updateTicketComments(violationId, ticketComments);
            } catch (TPException e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        }

       /* if (!result) {
            try {
                SyncData.insertSyncData(syncCommentData).subscribe();
                //DatabaseHelper.getInstance().insertOrReplace(syncCommentData.getContentValues(), "sync_data");
            } catch (SQLiteException e) {
                log.error(TPUtility.getPrintStackTrace(e));

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            syncDataList.add(syncCommentData);
        }*/

        //DatabaseHelper.getInstance().closeWritableDb();
    }

    private void collapsedAll() {
        for (int i = 0; i < activeTicket.getTicketViolations().size(); i++) {
            expandListview.collapseGroup(i);
        }
    }

    private boolean isDuplicateViolation(int violation) {
        boolean result = false;
        for (TicketViolation ticketViolation : activeTicket.getTicketViolations()) {
            if (ticketViolation.getViolationId() == violation) {
                return true;
            }
        }

        return result;
    }

    private boolean isDuplicateComment(List<TicketComment> commentList, int commentId) {
        boolean result = false;

        for (TicketComment ticketComment : commentList) {
            if (ticketComment.getCommentId() == commentId) {
                return true;
            }
        }

        return result;
    }

    private boolean isDuplicateNewComment(List<TicketComment> commentList, String newComment) {
        boolean result = false;
        try {
            for (TicketComment ticketComment : commentList) {
                if (ticketComment.getComment().equalsIgnoreCase(newComment)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean isDuplicatePrivateComment(int violationId) {
        boolean result = false;

        for (TicketViolation violation : activeTicket.getTicketViolations()) {
            if (violation.getViolationId() == violationId) {
                List<TicketComment> commentList = violation.getTicketComments();
                for (TicketComment ticketComment : commentList) {
                    if (ticketComment.isPrivate()) {
                        return true;
                    }
                }
            }
        }

        return result;
    }

    @Override
    public void handleVoiceInput(String text) {
        if (text == null)
            return;

        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        if (text.contains("BACK") || text.contains("GO BACK") || text.contains("CLOSE")) {
            backAction(null);

        } else if (text.contains("ADD VIOLATION") || text.contains("NEW VIOLATION")) {
            addViolationAction(null);
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

    // request for Multiple comments , comments replace and long press comments
    private void requestComments(long ticketId, int violationId, int commentIds[], int violationIndex, int commentIndex,
                                 boolean requestEditComment, String commentText, boolean isPrivateComment) {
        Intent i = new Intent();
        if (commentIds == null) {
            i.setClass(ViolationsActivity.this, AddCommentActivity.class);
        } else {
            i.setClass(ViolationsActivity.this, AddCommentAutoActivity.class);
        }

        if (editCommentsOnly) {
            i.putExtra("PrivateCommentsOnly", editCommentsOnly);
        } else if (isPrivateComment) {
            i.putExtra("PrivateCommentsOnly", isPrivateComment);
        } else {
            i.putExtra("PrivateCommentsOnly", editCommentsOnly);
        }

        i.putExtra("TicketId", ticketId);
        i.putExtra("CitationNumber", activeTicket.getCitationNumber());
        i.putExtra("ViolationId", violationId);

        i.putExtra("CommentIds", commentIds);
        i.putExtra("CommentIndex", commentIndex);
        i.putExtra("ViolationIndex", violationIndex);
        i.putExtra("requestEditComment", requestEditComment);
        i.putExtra("editCommentText", commentText);

        startActivityForResult(i, REQUEST_COMMENT);
    }

    // replace comments on index basis
    private void replaceCommentByIndex(int violationIdex, int commentIndex) {

        try {
            TicketViolation ticketViolation = activeTicket.getTicketViolations().get(violationIdex);
            if (ticketViolation != null) {
                ticketViolation.getTicketComments().remove(commentIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        expandAdapter.notifyDataSetChanged();
    }

    private Intent getUpdatedIntent(int violationId, int commentIndex, int violationIndex, int commentId) {
        Intent data = new Intent();
        try {
            Comment comment = null;
            try {
                comment = Comment.getCommentById(commentId);
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            if (comment != null) {
                data.putExtra("PrivateComment", false);
                data.putExtra("ViolationId", violationId);
                data.putExtra("CommentId", comment.getId());
                data.putExtra("Comment", comment.getTitle());

                data.putExtra("CommentIndex", commentIndex);
                data.putExtra("ViolationIndex", violationIndex);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    @Override
    public void successRepeatOffender(Violation violation, RepeatOffender repeatOffender) {
        log.info("SuccessRepeatOffender Call");
        progressDialog.dismiss();
        int count = 0;
        double fine = 0;
        if (repeatOffender != null) {
            count = repeatOffender.getCount();
        }
        if (Feature.isFeatureAllowed(Feature.PARK_REPEATOFFENDER_HIDE) && violation.getRepeatOffenderType() != null && violation.getRepeatOffenderType().equalsIgnoreCase("VER")) {
            log.info("====(Feature enabled)====");
            if (count >= 2) {
                int violation_code_export = Integer.parseInt(violation.getCode_exportt()) + 2;
                log.info("I====violation_code_export ====" + violation_code_export);

                violationexp = Violation.getViolationByCode_Export(String.valueOf(violation_code_export));
                fine = violationexp.getBaseFine();
            } else if (count >= 1) {
                int violation_code_export = Integer.parseInt(violation.getCode_exportt()) + 1;
                violationexp = Violation.getViolationByCode_Export(String.valueOf(violation_code_export));

                fine = violationexp.getBaseFine();

            } else {
                violationexp = violation;//Violation.getViolationByCode_Export(violation.getCode_exportt());
                fine = violationexp.getBaseFine();

            }
        } else {
            if (count >= 2) {
                fine = violation.getFine2();
            } else if (count >= 1) {
                fine = violation.getFine1();
            } else {
                fine = violation.getBaseFine();
            }
        }
        activeTicket.setFine(fine);
        ArrayList<TicketViolation> ticketViolations = activeTicket.getTicketViolations();
        for (int i = 0; i < ticketViolations.size(); i++) {
            TicketViolation ticketViolation = ticketViolations.get(i);
            if (ticketViolations.get(i).getViolationCode().equals(violation.getCode())) {
                if (Feature.isFeatureAllowed(Feature.PARK_REPEATOFFENDER_HIDE) &&violation.getRepeatOffenderType()!=null && violation.getRepeatOffenderType().equalsIgnoreCase("VER")) {
                    ticketViolation.setFine(fine);
                    ticketViolation.setViolationId(violationexp.getId());
                    ticketViolation.setViolationCode(violationexp.getCode());
                    ticketViolation.setViolationDesc(violationexp.getTitle());
                    ticketViolation.setVerticalViolationId(violation.getId());
                    ticketViolation.setRepeatOffenderVertical(true);
                    activeTicket.getTicketViolations().set(i, ticketViolation);
                } else {
                    ticketViolation.setFine(fine);
                    activeTicket.getTicketViolations().set(i, ticketViolation);

                }
                break;
            } else {
                log.info("Ticket violation and selected violation not match ");
            }
        }
        expandAdapter.notifyDataSetChanged();
    }

    @Override
    public void failRepeatOffender(String fail) {
        log.debug("FailRepeatOffender: "+fail);
        progressDialog.dismiss();
        /*log.error(fail);
        //Dialog
        new iOSDialogBuilder(ViolationsActivity.this)

                .setSubtitle("Server not responding Repeat Offender validation has been disabled.")
                .setPositiveListener(getString(R.string._ok), dialog -> dialog.dismiss())

                .build().show();*/
    }


    class ExpandListAdapter extends BaseExpandableListAdapter {

        private Context context;
        private ArrayList<TicketViolation> groups;
        private Dialog playerDialog;
        private MediaPlayer audioPlayer;
        private boolean isPlaying = false;
        private SeekBar seekbar;
        private Button playBtn;
        private Handler handler;
        private Runnable moveSeekBarThread = new Runnable() {
            public void run() {
                if (audioPlayer == null) {
                    return;
                }
                try {
                    if (audioPlayer.isPlaying()) {
                        int mediaPosNew = audioPlayer.getCurrentPosition();
                        int mediaMaxNew = audioPlayer.getDuration();

                        seekbar.setMax(mediaMaxNew);
                        seekbar.setProgress(mediaPosNew);
                        handler.postDelayed(this, 100);
                    } else {
                        int mediaPosNew = audioPlayer.getCurrentPosition();
                        int mediaMaxNew = audioPlayer.getDuration();

                        seekbar.setMax(mediaMaxNew);
                        seekbar.setProgress(mediaPosNew);
                        handler.postDelayed(this, 100);
                    }
                } catch (IllegalStateException e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
            }
        };

        public ExpandListAdapter(Context context, ArrayList<TicketViolation> groups) {
            this.context = context;
            this.groups = groups;
        }

        public void addItem(TicketComment item, TicketViolation group) {
            if (!groups.contains(group)) {
                groups.add(group);
            }

            int index = groups.indexOf(group);
            ArrayList<TicketComment> comments = groups.get(index).getTicketComments();
            comments.add(item);
            groups.get(index).setTicketComments(comments);
        }

        public Object getChild(int groupPosition, int childPosition) {
            ArrayList<TicketComment> comments = groups.get(groupPosition).getTicketComments();
            return comments.get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View view,
                                 ViewGroup parent) {
            TicketComment child = (TicketComment) getChild(groupPosition, childPosition);
            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) context
                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.ticket_comment_row_item, null);
            }

            TextView tv = (TextView) view.findViewById(R.id.comment_row_title_textview);
            tv.setText(child.getComment());
            tv.setTag(child.getTag());

            final int violationIdex = groupPosition;
            final int commentIndex = childPosition;

            ImageView deleteBtn = (ImageView) view.findViewById(R.id.comment_row_trash_imgview);
            deleteBtn.setClickable(true);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViolationsActivity.this);
                    builder.setTitle("Delete Confirmation").setMessage("Are you sure you want to delete?")
                            .setCancelable(true).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TicketViolation ticketViolation = activeTicket.getTicketViolations().get(violationIdex);

                                    if (editCommentsOnly) {
                                        try {
                                            TicketComment comment = ticketViolation.getTicketComments()
                                                    .get(commentIndex);
                                            TicketComment.removeById(comment.getTicketCommentId());
                                            if (isServiceAvailable) {

                                                ((ViolationBLProcessor) screenBLProcessor).deleteTicketComment(
                                                        comment.getCitationNumber(), ticketViolation.getViolationId(),
                                                        comment.getComment());

                                            }

                                        } catch (TPException e) {
                                            log.error(TPUtility.getPrintStackTrace(e));

                                        } catch (Exception e) {
                                            log.error(TPUtility.getPrintStackTrace(e));
                                        }
                                    }

                                    if (ticketViolation != null) {
                                        ticketViolation.getTicketComments().remove(commentIndex);

                                    }

                                    expandAdapter.notifyDataSetChanged();

                                }


                            });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });

            ImageView lockIcon = (ImageView) view.findViewById(R.id.comment_row_lock_imgview);
            if (child.isPrivate()) {
                lockIcon.setVisibility(View.VISIBLE);
                view.setBackgroundColor(Color.RED);
            } else {
                lockIcon.setVisibility(View.GONE);
                view.setBackgroundColor(Color.WHITE);
            }

            ImageView voiceIcon = (ImageView) view.findViewById(R.id.comment_row_voice_imgview);
            if (child.isVoiceComment()) {
                final String audioFile = child.getAudioFile();
                voiceIcon.setVisibility(View.VISIBLE);
                voiceIcon.setClickable(true);
                voiceIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playAudio(audioFile);
                    }
                });
            } else {
                voiceIcon.setVisibility(View.GONE);
            }

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        TicketViolation ticketViolation = activeTicket.getTicketViolations().get(violationIdex);
                        int commentIDs[] = null;
                        //boolean isPrivateComment = ticketViolation.getTicketComments().get(childPosition).isPrivate();
                        requestComments(activeTicket.getTicketId(), ticketViolation.getViolationId(), commentIDs,
                                violationIdex, commentIndex, false, null, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            tv.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                    try {
                        TicketViolation ticketViolation = activeTicket.getTicketViolations().get(violationIdex);
                        int commentIDs[] = null;
                        String commentText = ticketViolation.getTicketComments().get(commentIndex).getComment();
                        boolean isPrivateComment = ticketViolation.getTicketComments().get(childPosition).isPrivate();
                        requestComments(activeTicket.getTicketId(), ticketViolation.getViolationId(), commentIDs,
                                violationIdex, commentIndex, true, commentText, isPrivateComment);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });

            return view;
        }

        public void playAudio(final String audioFile) {
            playerDialog = new Dialog(ViolationsActivity.this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View inputDlgView = layoutInflater.inflate(R.layout.audio_player_dialog, null, false);
            playerDialog.setCancelable(false);
            playerDialog.setTitle("Voice Memo");
            playerDialog.setContentView(inputDlgView);
            playerDialog.show();

            Button cancelBtn = (Button) inputDlgView.findViewById(R.id.audio_cancel_btn);
            Button stopBtn = (Button) inputDlgView.findViewById(R.id.audio_stop_btn);
            playBtn = (Button) inputDlgView.findViewById(R.id.audio_play_btn);
            seekbar = (SeekBar) inputDlgView.findViewById(R.id.audio_seekbar);
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        audioPlayer.seekTo(progress);
                        seekBar.setProgress(progress);
                    }
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (playerDialog.isShowing())
                        playerDialog.dismiss();
                }
            });

            stopBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopPlaying();
                    playBtn.setText("Play");
                    seekbar.setProgress(0);
                }
            });

            playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isPlaying) {
                        isPlaying = false;
                        audioPlayer.pause();
                        playBtn.setText("Play");
                    } else {
                        playRecording(audioFile);
                        playBtn.setText("Pause");
                    }
                }
            });

        }

        private void playRecording(String audioFile) {
            audioPlayer = new MediaPlayer();
            handler = new Handler();
            try {
                audioPlayer.setDataSource(TPUtility.getVoiceMemosFolder() + audioFile);
                audioPlayer.prepare();
                audioPlayer.start();
                isPlaying = true;
            } catch (IOException e) {
                log.error("Error playing recording.");
            }

            int mediaPos = audioPlayer.getCurrentPosition();
            int mediaMax = audioPlayer.getDuration();
            seekbar.setMax(mediaMax);
            seekbar.setProgress(mediaPos);
            handler.removeCallbacks(moveSeekBarThread);
            handler.postDelayed(moveSeekBarThread, 100);

            audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isPlaying = false;
                    if (playerDialog.isShowing())
                        playerDialog.dismiss();

                    audioPlayer.reset();
                    audioPlayer.release();
                }
            });
        }

        private void stopPlaying() {
            isPlaying = false;
            audioPlayer.stop();
            audioPlayer.reset();
            audioPlayer.release();
        }

        public int getChildrenCount(int groupPosition) {
            ArrayList<TicketComment> chList = groups.get(groupPosition).getTicketComments();
            return chList.size();

        }

        public Object getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        public int getGroupCount() {
            return groups.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup parent) {
            final TicketViolation group = (TicketViolation) getGroup(groupPosition);
            if (view == null) {
                LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                view = inf.inflate(R.layout.ticket_violation_row_item, null);
            }

            TextView tvTitle = (TextView) view.findViewById(R.id.violation_row_title_textview);
            tvTitle.setText(group.getViolationDesc());

            TextView tv = (TextView) view.findViewById(R.id.violation_row_code_textview);
            tv.setText(group.getViolationCode());

            final int violationIdex = groupPosition;
            ImageView deleteBtn = (ImageView) view.findViewById(R.id.violation_row_trash_imgview);
            deleteBtn.setClickable(true);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ViolationsActivity.this);
                    builder.setTitle("Delete Confirmation").setMessage("Are you sure you want to delete?")
                            .setCancelable(true).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    try {
                                        activeTicket.getTicketViolations().remove(violationIdex);
                                        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketViolationsDaoTemp().removeById(group.getTicketViolationId());
                                        expandAdapter.notifyDataSetChanged();
                                        if (groups.size() == 0) {

                                            TPApp.setStickyViolation(null);
                                            TPApp.setStickyComment(null);
                                            stickyCommentsChk.setChecked(false);
                                            stickyViolationsChk.setChecked(false);
                                            TPApp.setStickyViolations(false);
                                            stickyCommentsChk.setEnabled(false);
                                            stickyViolationsChk.setEnabled(false);
                                        } else {
                                            stickyCommentsChk.setEnabled(true);
                                            stickyViolationsChk.setEnabled(true);
                                            if (TPApp.stickyViolations) {
                                                TPApp.setStickyViolation(groups.get(0));
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        log.error(e.getMessage());
                                    }

                                    if (groups.size() < 3) {
                                        addViolationBtn.setEnabled(true);
                                        addViolationBtn.setBackgroundResource(R.drawable.btn_blue);
                                    }
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            if (editCommentsOnly) {
                deleteBtn.setVisibility(View.GONE);
            }

            final long ticketId = group.getTicketId();
            final int violationId = group.getViolationId();

            ImageView addComment = (ImageView) view.findViewById(R.id.violation_row_addcomment_imgview);
            addComment.setClickable(true);
            addComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setClass(ViolationsActivity.this, AddCommentActivity.class);
                    i.putExtra("TicketId", ticketId);
                    i.putExtra("CitationNumber", activeTicket.getCitationNumber());
                    i.putExtra("ViolationId", violationId);
                    i.putExtra("PrivateCommentsOnly", editCommentsOnly);

                    startActivityForResult(i, REQUEST_COMMENT);
                }
            });

            return view;
        }

        public boolean hasStableIds() {
            return true;
        }

        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }

}
