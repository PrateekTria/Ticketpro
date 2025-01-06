package com.ticketpro.parking.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.RetrofitServiceGenerator;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.model.EulaAcceptanceParams;
import com.ticketpro.model.EulaAcceptanceRequest_Rpc;
import com.ticketpro.model.EulaAcceptanceResult;
import com.ticketpro.model.EulaModel;
import com.ticketpro.model.EulaParams;
import com.ticketpro.model.EulaResult;
import com.ticketpro.model.EulaReuest_Rpc;
import com.ticketpro.model.Params;
import com.ticketpro.model.RequestPOJO;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.ViolationBLProcessor;
import com.ticketpro.util.Preference;
import com.ticketpro.util.TPUtility;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EULAActivity extends BaseActivityImpl {
    final int FAILED = 0;
    final int SUCCESSFULL = 1;
    final int EULA_ACCEPTED = 2;
    final int MOVE_TO_NEXT_SCREEN = 3;
    private WebView webView;
    private Button btnAccept, btnDeny;
    private Preference preference;
    private ProgressDialog progressDialog;
    private Handler eulaHandler;
    private Handler errorHandler;
    private EulaModel eulaText;
    private RelativeLayout relativeLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_e_u_l_a);
            setLogger(EULAActivity.class.getName());
            System.out.println(BaseActivityImpl.instance.isServiceAvailable);
            preference = Preference.getInstance(this);
            webView = (WebView) findViewById(R.id.webview);
            btnDeny = (Button) findViewById(R.id.btn_deny);
            btnAccept = (Button) findViewById(R.id.btn_accept);
            relativeLayout = (RelativeLayout) findViewById(R.id.layout_hearer);
            btnAccept.setOnClickListener(this);
            btnDeny.setOnClickListener(this);

            setBLProcessor(new ViolationBLProcessor());
            isNetworkInfoRequired = true;
            eulaHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (msg.what == SUCCESSFULL) {
                        initializeData();
                    } else if (msg.what == EULA_ACCEPTED || msg.what == MOVE_TO_NEXT_SCREEN) {
                        Intent i = new Intent();
                        i.setClass(EULAActivity.this, DateConfActivity.class);
                        startActivity(i);
                        finish();
                    } else if (msg.what == FAILED) {
                        btnAccept.setEnabled(false);
                        displayErrorMessage("Failed to login. Please check username/password");
                    }

                }
            };


            errorHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Intent i = new Intent();
                    i.setClass(EULAActivity.this, DateConfActivity.class);
                    startActivity(i);
                    finish();

                }
            };


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (isNetworkConnected()) {
                callApi();
            } else {
                Intent i = new Intent();
                i.setClass(EULAActivity.this, DateConfActivity.class);
                startActivity(i);
                finish();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* new Handler().postDelayed(() -> {

        },1000);*/

    }

    private void initializeData() {
        relativeLayout.setVisibility(View.VISIBLE);
        String loadData = eulaText.getEulaText();
        this.webView.loadData(loadData, "text/html", "UTF-8");
        btnAccept.setClickable(true);
        btnAccept.setAlpha(1f);
    }

    /**
     * Request for getting data from server.
     */
   /* public void callApi() {
        progressDialog = ProgressDialog.show(this, "", "Checking...");
        progressDialog.setCancelable(true);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    eulaText = ((ViolationBLProcessor) screenBLProcessor).getEulaText(TPApp.userId, 1);
                    Log.d("TAG", "USER_ID_OF_THIS_DEVICE====>" + TPApp.userId);
                    if (eulaText != null) {
                        if (eulaText.getMessage() != null && eulaText.getMessage().equals("N")) {
                            eulaHandler.sendEmptyMessage(SUCCESSFULL);
                        } else if (eulaText.getMessage() != null && eulaText.getMessage().equals("Y")) {
                            eulaHandler.sendEmptyMessage(MOVE_TO_NEXT_SCREEN);
                        } else if (eulaText.getMessage() != null && eulaText.getMessage().equals("NA")) {
                            eulaHandler.sendEmptyMessage(MOVE_TO_NEXT_SCREEN);
                        } else {
                            errorHandler.sendEmptyMessage(FAILED);
                           // btnAccept.setEnabled(true);
                        }
                    } else {
                        errorHandler.sendEmptyMessage(FAILED);
                       // btnAccept.setEnabled(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(TPUtility.getPrintStackTrace(e));
                    errorHandler.sendEmptyMessage(FAILED);

                }
            }
        }.start();

    }
*/
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_accept:
                try {
                    if (isNetworkConnected()) {
                        _doUpdateServer();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_deny:

                new iOSDialogBuilder(EULAActivity.this)
                        .setSubtitle("In order to use the ticketPRO app(s), you must review and accept the End-user License Agreement. Select OK to resume or Cancel to exit.")
                        .setBoldPositiveLabel(true)
                        .setCancelable(true)
                        .setNegativeListener(getString(R.string.cancel), new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                Intent intent = new Intent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setClass(EULAActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setPositiveListener(getString(R.string.ok), new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {

                                dialog.dismiss();

                            }
                        }).build().show();


                break;
        }
    }

    /**
     * This function will helps to update data on server,When user will accept EULA
     */
/*
    private void _doUpdateServer() {
        progressDialog = ProgressDialog.show(this, "", "Accepting...");
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    JSONObject result = ((ViolationBLProcessor) screenBLProcessor).getResult(TPApp.userId, eulaText.getResId(), "Y", TPApp.custId);
                    String eulaAcceptedByCust = result.optString("EulaAcceptedByCust");

                    if (eulaAcceptedByCust.equals("Y")) {
                        eulaHandler.sendEmptyMessage(EULA_ACCEPTED);
                        preference.putString("EULA_ACCEPTED", eulaAcceptedByCust);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
*/
    @Override
    public void bindDataAtLoadingTime() throws Exception {

    }

    @Override
    public void handleVoiceInput(String text) {

    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {

    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {

    }

    private void callApi() {
        progressDialog = ProgressDialog.show(this, "", "Checking...");
        progressDialog.setCancelable(true);
        EulaReuest_Rpc requestPOJO = new EulaReuest_Rpc();
        EulaParams params = new EulaParams();

        params.setUserid(String.valueOf(TPApp.userId));
        System.out.println(("*****" + TPApp.userId));
        params.setModuleId(1);

        requestPOJO.setParams(params);
        requestPOJO.setMethod("getEULA");
        requestPOJO.setId("82F85DB43CBF6");
        requestPOJO.setJsonrpc("2.0");

        ApiRequest api = RetrofitServiceGenerator.createRxService(ApiRequest.class);
        api.getEula(requestPOJO).enqueue(new Callback<EulaResult>() {
            @Override
            public void onResponse(Call<EulaResult> call, Response<EulaResult> response) {

                if (response.isSuccessful() && response.code()==200) {
                    progressDialog.dismiss();
                    eulaText = response.body().getEula();

                    if (eulaText != null) {
                        if (eulaText.getMessage() != null && eulaText.getMessage().equals("N")) {
                            eulaHandler.sendEmptyMessage(SUCCESSFULL);
                        } else if (eulaText.getMessage() != null && eulaText.getMessage().equals("Y")) {
                            eulaHandler.sendEmptyMessage(MOVE_TO_NEXT_SCREEN);
                        } else if (eulaText.getMessage() != null && eulaText.getMessage().equals("NA")) {
                            eulaHandler.sendEmptyMessage(MOVE_TO_NEXT_SCREEN);
                        } else {
                            errorHandler.sendEmptyMessage(FAILED);
                            // btnAccept.setEnabled(true);
                        }
                    } else {
                        errorHandler.sendEmptyMessage(FAILED);
                        // btnAccept.setEnabled(false);
                    }
                }else {
                    errorHandler.sendEmptyMessage(MOVE_TO_NEXT_SCREEN);
                }

            }

            @Override
            public void onFailure(Call<EulaResult> call, Throwable t) {
                progressDialog.dismiss();
                eulaHandler.sendEmptyMessage(MOVE_TO_NEXT_SCREEN);
            }
        });


    }

    private void _doUpdateServer() {
        progressDialog = ProgressDialog.show(this, "", "Accepting...");
        EulaAcceptanceRequest_Rpc requestPOJO = new EulaAcceptanceRequest_Rpc();
        EulaAcceptanceParams params = new EulaAcceptanceParams();

        params.setUserid(String.valueOf(TPApp.userId));
        params.setEulaId(eulaText.getResId());  //eulaText.getResId()
        params.setCustId(TPApp.custId);
        params.setIsAccepted("Y");


        requestPOJO.setParams(params);
        requestPOJO.setMethod("eulaAcceptance");
        requestPOJO.setId("82F85DB43CBF6");
        requestPOJO.setJsonrpc("2.0");

        ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
        api.getEulaAcceptance(requestPOJO).enqueue(new Callback<EulaAcceptanceResult>() {
            @Override
            public void onResponse(Call<EulaAcceptanceResult> call, Response<EulaAcceptanceResult> response) {

                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    String eulaAcceptedByCust = response.body().getEulaAcceptedByCust();
                    if (eulaAcceptedByCust.equals("Y")) {
                        eulaHandler.sendEmptyMessage(EULA_ACCEPTED);
                        preference.putString("EULA_ACCEPTED", eulaAcceptedByCust);

                    }

                }

            }

            @Override
            public void onFailure(Call<EulaAcceptanceResult> call, Throwable t) {
                progressDialog.dismiss();

            }
        });

    }

}
