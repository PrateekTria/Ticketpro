package com.ticketpro.parking.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.ticketpro.model.User;
import com.ticketpro.parking.R;
import com.ticketpro.util.TPUtility;

public class IDVerification extends BaseActivityImpl {

    private EditText mRmsid;
    private Button btnBack,btnNext,btnClear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idverification);

        __intiView();
        btnBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnClear.setOnClickListener(this);

    }

    private void __intiView() {
        try {
            mRmsid = findViewById(R.id.idv_et_rmsid);
            btnBack = findViewById(R.id.idv_btnBack);
            btnNext = findViewById(R.id.idv_btnNext);
            btnClear = findViewById(R.id.idv_btnClear);
            this.mRmsid.setOnKeyListener(new C01841());
            mRmsid.setFocusable(true);
            mRmsid.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mRmsid.setText("");
                    return false;
                }
            });
            initializeData();
        } catch (Exception e) {
            e.printStackTrace();
            this.log.error(TPUtility.getPrintStackTrace(e));
        }

    }
    public void initializeData() {
        this.mRmsid.requestFocus();
        new Handler().postDelayed(new C01863(), 100);
    }
    class C01863 implements Runnable {
        C01863() {
        }

        public void run() {
            try {
                InputMethodManager imm = (InputMethodManager) IDVerification.this.getSystemService("input_method");
                if (imm != null) {
                    imm.showSoftInput(IDVerification.this.mRmsid, 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C01841 implements View.OnKeyListener {
        C01841() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() != 0 || keyCode != 66) {
                return false;
            }
            if (!mRmsid.getText().toString().trim().isEmpty()) {
                __checkAndPassToLoginScreen(mRmsid.getText().toString());
            }
            return true;
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.idv_btnBack:
                finish();
                break;
            case R.id.idv_btnNext:

                if (!mRmsid.getText().toString().trim().isEmpty()) {
                    __checkAndPassToLoginScreen(mRmsid.getText().toString());
                }
                break;

            case R.id.idv_btnClear:
                mRmsid.setText("");
                break;
        }
    }

    private void __checkAndPassToLoginScreen(String rmsid) {
        try {
            if (rmsid!=null && !rmsid.isEmpty()){
                User userByRmsid = User.getUserByRmsid(rmsid);
                if (userByRmsid!=null) {
                    if (!TextUtils.isEmpty(userByRmsid.getRmsid())) {
                        Intent i = new Intent();
                        i.setClass(IDVerification.this, LoginPasswordActivity.class);
                        i.putExtra("username", userByRmsid.getUsername());
                        i.putExtra("password", userByRmsid.getPassword());
                        i.putExtra("userid", userByRmsid.getUserId());
                        i.putExtra("rmsid", rmsid);
                        IDVerification.this.startActivity(i);
                    } else {

                    }
                }else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Information")
                            .setMessage(R.string.invalid_userid)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    builder.create();
                    builder.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    public void handleNetworkStatus(boolean z, boolean z2) {

    }

    @Override
    public void bindDataAtLoadingTime() throws Exception {

    }

    @Override
    public void handleVoiceInput(String str) {

    }

    @Override
    public void handleVoiceMode(boolean z) {

    }

}