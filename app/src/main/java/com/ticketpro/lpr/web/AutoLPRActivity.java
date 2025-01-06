package com.ticketpro.lpr.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.model.Body;
import com.ticketpro.model.Color;
import com.ticketpro.model.Feature;
import com.ticketpro.model.LprBodyMap;
import com.ticketpro.model.MakeAndModel;
import com.ticketpro.model.State;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.activity.CaptureImageActivity;
import com.ticketpro.parking.activity.SearchLookupActivity;
import com.ticketpro.parking.service.TPAsyncTask;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPTask;
import com.ticketpro.util.TPUtility;
import com.ticketpro.util.TouchImageView;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AutoLPRActivity extends BaseActivityImpl {
    private final int REQUEST_STATE = 0;
    private final int REQUEST_COLOR = 1;
    private final int REQUEST_BODY = 2;
    private final int REQUEST_MAKE = 3;
    private final int REQUEST_IMAGE = 4;
    private EditText plateNumberEditView;
    private EditText stateCodeEditView;
    private EditText colorEditView;
    private EditText bodyEditView;
    private EditText makeEditView;
    private TouchImageView previewImage;
    private Bitmap previewBitmap;
    private String state;
    private String plate;
    private String color;
    private String body;
    private String make;
    private String resolution;
    private String imageSize;
    private String imagePath;
    private boolean vehicleInfoRequired;
    private boolean nightMode;
    //private CheckBox nightModeCheck;
    Logger log;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.auto_lpr);
        isNetworkInfoRequired = true;
        log = Logger.getLogger("AutoLPR");
        setLogger(AutoLPRActivity.class.getName());
        log.info("Call AutoLPR");
        previewImage = (TouchImageView) findViewById(R.id.preview_image);
        plateNumberEditView = (EditText) findViewById(R.id.plate_number);
        stateCodeEditView = (EditText) findViewById(R.id.state);

        colorEditView = (EditText) findViewById(R.id.color);
        bodyEditView = (EditText) findViewById(R.id.body);
        makeEditView = (EditText) findViewById(R.id.make);

        if (Feature.isFeatureAllowed(Feature.AUTO_LPR_VEHICLE_INFO)) {
            vehicleInfoRequired = true;
        } else {
            vehicleInfoRequired = false;
            colorEditView.setVisibility(View.GONE);
            bodyEditView.setVisibility(View.GONE);
            makeEditView.setVisibility(View.GONE);
        }

        takePicture();
        //throw new RuntimeException("FIREBASE CRASHLYTICS TEST::" + DateFormat.getDateTimeInstance().format(new Date()));


    }

    public void backAction(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == REQUEST_IMAGE) {
                backAction(null);
            }
            return;
        }

        switch (requestCode) {

            case REQUEST_STATE:
                if (data.hasExtra("STATE")) {
                    stateCodeEditView.setText(data.getStringExtra("STATE"));
                    stateCodeEditView.setBackgroundResource(R.drawable.text_field_bkg);
                }

                break;

            case REQUEST_COLOR:
                if (data.hasExtra("COLOR")) {
                    colorEditView.setText(data.getStringExtra("COLOR"));
                    colorEditView.setBackgroundResource(R.drawable.text_field_bkg);
                }

                break;

            case REQUEST_BODY:
                if (data.hasExtra("BODY")) {

                    String body = data.getStringExtra("BODY");
                    assert body != null;
                    bodyEditView.setText(body);
                    bodyEditView.setBackgroundResource(R.drawable.text_field_bkg);
                }

                break;

            case REQUEST_MAKE:
                if (data.hasExtra("MAKE")) {
                    makeEditView.setText(data.getStringExtra("MAKE"));
                    makeEditView.setBackgroundResource(R.drawable.text_field_bkg);
                }

                break;

            case REQUEST_IMAGE:
                if (data.hasExtra("ImagePath")) {
                    String imagePath = data.getStringExtra("ImagePath");
                    nightMode = data.getBooleanExtra("vehicleInfoRequired", false);

                    if (StringUtil.isEmpty(imagePath)) {
                        break;
                    }

                    previewBitmap = BitmapFactory.decodeFile(imagePath);
                    if (previewBitmap != null) {
                        previewImage.setImageBitmap(previewBitmap);
                        //Zoom Image loader

                        stateCodeEditView.setText("");
                        plateNumberEditView.setText("");
                        colorEditView.setText("");
                        bodyEditView.setText("");
                        makeEditView.setText("");

                        stateCodeEditView.setBackgroundResource(R.drawable.text_field_bkg);
                        colorEditView.setBackgroundResource(R.drawable.text_field_bkg);
                        bodyEditView.setBackgroundResource(R.drawable.text_field_bkg);
                        makeEditView.setBackgroundResource(R.drawable.text_field_bkg);

                        try {
                            handlePreviewDecode(previewBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;
        }

    }

    public void selectStateAction(View view) {
        Intent i = new Intent();
        i.setClass(this, SearchLookupActivity.class);
        i.putExtra("LIST_TYPE", TPConstant.STATES_SEARCH_LIST);
        startActivityForResult(i, REQUEST_STATE);
    }

    public void selectColorAction(View view) {
        Intent i = new Intent();
        i.setClass(this, SearchLookupActivity.class);
        i.putExtra("LIST_TYPE", TPConstant.COLOR_SEARCH_LIST);
        startActivityForResult(i, REQUEST_COLOR);
    }

    public void selectBodyAction(View view) {
        Intent i = new Intent();
        i.setClass(this, SearchLookupActivity.class);
        i.putExtra("LIST_TYPE", TPConstant.BODY_SEARCH_LIST);
        startActivityForResult(i, REQUEST_BODY);
    }

    public void selectMakeAction(View view) {
        Intent i = new Intent();
        i.setClass(this, SearchLookupActivity.class);
        i.putExtra("LIST_TYPE", TPConstant.MAKE_SEARCH_LIST);
        startActivityForResult(i, REQUEST_MAKE);
    }

    public void takePicture() {
        imagePath = TPUtility.getLPRImagesFolder() + new Date().getTime() + ".jpg";

        Intent intent = new Intent();
        intent.putExtra("ImagePath", imagePath);
        intent.putExtra("isALPRRequest", true);
        intent.setClass(this, CaptureImageActivity.class);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    public void retryAction(View view) {
        takePicture();
    }

    public void acceptAction(View view) {

        Intent data = new Intent();
        data.putExtra("State", stateCodeEditView.getText().toString().toUpperCase());
        data.putExtra("PlateNumber", plateNumberEditView.getText().toString().toUpperCase());
        data.putExtra("PlateImageFile", imagePath);
        data.putExtra("Resolution", resolution);
        data.putExtra("ImageSize", imageSize);

        //if(vehicleInfoRequired) {
        data.putExtra("Color", colorEditView.getText().toString().toUpperCase());
        data.putExtra("Body", bodyEditView.getText().toString().toUpperCase());
        data.putExtra("Make", makeEditView.getText().toString().toUpperCase());
        //}

        if (getParent() == null) {
            setResult(Activity.RESULT_OK, data);
        } else {
            getParent().setResult(Activity.RESULT_OK, data);
        }
        log.info("<------AcceptAutoLPR------->");
        finish();
    }

    private void handlePreviewDecode(final Bitmap bitmap) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        final byte[] byteArray = stream.toByteArray();
        if (isNetworkConnected1()) {
            TPAsyncTask task = new TPAsyncTask(this, "Processing LPR...");
            task.execute(new TPTask() {
                @Override
                public void execute() {
                    try {
                        String recognizeVehicle = "0";
                        if (vehicleInfoRequired) {
                            recognizeVehicle = "1";
                        }
                        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        java.net.URL url = new java.net.URL("https://api.openalpr.com/v2/recognize_bytes?secret_key=sk_f497ee76ff2f3ea4846a7311&recognize_vehicle=" + recognizeVehicle + "&country=us&return_image=0&topn=10");
                        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setConnectTimeout(10*1000);
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.connect();

                        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                        os.writeBytes(encoded);

                        os.flush();
                        os.close();

                        if (conn.getResponseCode() == 200) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                            StringBuilder sb = new StringBuilder();
                            String output;

                            while ((output = reader.readLine()) != null) {
                                sb.append(output);
                            }

                            JSONObject jsonObject = new JSONObject(sb.toString());
                            if (jsonObject.has("results")) {
                                vehicleInfoRequired = !nightMode;
                                JSONArray results = jsonObject.getJSONArray("results");

                                if (results.length() > 0) {
                                    JSONObject result = (JSONObject) results.get(0);
                                    try {
                                        plate = result.getString("plate");
                                        state = result.getString("region");
                                        runOnUiThread(() -> {
                                            plateNumberEditView.setText(StringUtil.getDisplayString(plate));
                                            state = StringUtil.getDisplayString(state);
                                            stateCodeEditView.setText(state);
                                            try {
                                                int stateId = State.getStateIdByCode(state);
                                                if (stateId == 0) {
                                                    stateCodeEditView.setBackgroundResource(R.drawable.text_field_error);
                                                }
                                            } catch (Exception ignored) {
                                            }
                                        }
                                        );
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (result.has("vehicle")) {
                                        if (vehicleInfoRequired) {
                                            JSONObject vehicle = result.getJSONObject("vehicle");

                                            color = getBestResult(vehicle, "color");
                                            make = getBestResult(vehicle, "make");
                                            body = getBestResult(vehicle, "body_type");
                                        }

                                        resolution = bitmap.getWidth() + "x" + bitmap.getHeight();
                                        imageSize = TPUtility.getImageSize(byteArray);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
											plateNumberEditView.setText(StringUtil.getDisplayString(plate));
											
											state = StringUtil.getDisplayString(state);
											stateCodeEditView.setText(state);
											try {
												int stateId = State.getStateIdByCode(state);
												if(stateId == 0) {
													stateCodeEditView.setBackgroundResource(R.drawable.text_field_error);
												}
											}catch(Exception e) {}

                                                if (vehicleInfoRequired) {
                                                    color = StringUtil.getDisplayString(color);
                                                    body = StringUtil.getDisplayString(body);
                                                    make = StringUtil.getDisplayString(make);

                                                    colorEditView.setText(color);
                                                if (body.equalsIgnoreCase("SEDAN")&&Feature.isFeatureAllowed("PARK_LPR_BODY_TYPE_CHANGE")) {
                                                    bodyEditView.setText("4-DOOR");
                                                }
                                                else if (body.equalsIgnoreCase("TRUCK")&& Feature.isFeatureAllowed("PARK_LPR_BODY_TYPE_CHANGE")) {
                                                    bodyEditView.setText("PICK-UP TRUCK");
                                                }
                                                else if (body.equalsIgnoreCase("MINIVAN")&&Feature.isFeatureAllowed("PARK_LPR_BODY_TYPE_CHANGE")) {
                                                    bodyEditView.setText("VAN");
                                                }else {
                                                    bodyEditView.setText(body);

                                                }
                                                    bodyEditView.setText(body);

                                                    makeEditView.setText(make);

                                                    try {
                                                        //Color colorRecord = Color.getColorByTitle(color);
                                                        String colorRecord = getColorOfVehicleInfoByTitle(color);
                                                        if (colorRecord == null) {
                                                            colorEditView.setBackgroundResource(R.drawable.text_field_error);
                                                        } else {
                                                            colorEditView.setText(StringUtil.getDisplayString(colorRecord));
                                                            if (colorRecord.equalsIgnoreCase("UNK")) {
                                                                colorEditView.setBackgroundResource(R.drawable.text_field_orange);
                                                            }
                                                        }
                                                        String bodyRecord = null;
                                                        //Body bodyRecord = Body.getBodyByTitle(body);
                                                        if (Feature.isFeatureAllowed("PARK_LPR_BODY_TYPE_CHANGE")) {
                                                            bodyRecord = getLprBodyOfVehicleInfoByTitle(body);
                                                        } else {
                                                            bodyRecord = getBodyOfVehicleInfoByTitle(body);
                                                        }
                                                        if (bodyRecord == null) {
                                                            bodyEditView.setBackgroundResource(R.drawable.text_field_error);
                                                        } else {
                                                            bodyEditView.setText(StringUtil.getDisplayString(bodyRecord));
                                                            if (bodyRecord.equalsIgnoreCase("UNK")) {
                                                                bodyEditView.setBackgroundResource(R.drawable.text_field_orange);
                                                            }
                                                        }

                                                        //MakeAndModel makeRecord = MakeAndModel.getMakeByTitle(make);
                                                        String makeRecord = getMakeOfVehicleInfoByTitle(make);
                                                        if (makeRecord == null) {
                                                            makeEditView.setBackgroundResource(R.drawable.text_field_error);
                                                        } else {
                                                            makeEditView.setText(StringUtil.getDisplayString(makeRecord));
                                                            if (makeRecord.equalsIgnoreCase("UNK")) {
                                                                makeEditView.setBackgroundResource(R.drawable.text_field_orange);
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        log.error(e.getMessage());
                                                    }
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    runOnUiThread(() ->
                                            Toast.makeText(getBaseContext(), "Failed to get vehicle details.Please Re-try", Toast.LENGTH_LONG).show());
                                }
                            } else {
                                runOnUiThread(() ->
                                        Toast.makeText(getBaseContext(), "Failed to get vehicle details.Please Re-try", Toast.LENGTH_LONG).show());
                            }
                        }
                        conn.disconnect();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    runOnUiThread(() -> {
                        new iOSDialogBuilder(AutoLPRActivity.this)
                                .setSubtitle("Your request for LPR service is not responding right now. Please try later.")
                                .setPositiveListener("Ok", new iOSDialogClickListener() {
                                    @Override
                                    public void onClick(iOSDialog dialog) {
                                        dialog.dismiss();
                                    }
                                }).build().show();
                        //Toast.makeText(AutoLPRActivity.this, "Failed to get vehicle details.Please Re-try", Toast.LENGTH_LONG).show();
                    });
                    }
                }
            });
        }else {
            Toast.makeText(AutoLPRActivity.this, "Please check internet!", Toast.LENGTH_SHORT).show();
        }
        //__autoALPR(bitmap);
    }
    private void __autoALPR(final Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        final byte[] byteArray = stream.toByteArray();
        String recognizeVehicle = "0";
        if (vehicleInfoRequired) {
            recognizeVehicle = "1";
        }
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        ApiRequest service = ServiceGenerator.createService(ApiRequest.class);

        String url = "https://api.openalpr.com/v2/recognize_bytes?secret_key=sk_f497ee76ff2f3ea4846a7311&recognize_vehicle=" + recognizeVehicle + "&country=us&return_image=0&topn=10";
        service.getPhotoAlpr(url,encoded).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.e("AUTO_ALPR",t.getMessage());
            }
        });
    }

    private String getBestResult(JSONObject root, String property) {
        String bestMatch = "";

        if (root != null && root.has(property)) {
            JSONArray values;
            try {
                values = root.getJSONArray(property);
                if (values.length() > 0) {
                    JSONObject value = (JSONObject) values.get(0);
                    bestMatch = value.getString("name");
                }
            } catch (JSONException e) {
            }
        }

        return bestMatch;
    }


    /**
     * @return - Return body information with translated/manipulated text
     * @Params - Vehicle title required to get information.
     * @Uses - When we request autoLPR to get vehicle details by images
     * it returns vehicle information by scanning image, we further map data
     * as per our available records.
     * @since - Version - 3.1.003
     **/
    private String getBodyOfVehicleInfoByTitle(String bodyVehicle) {
        if (Body.getBodyByTitle(bodyVehicle) == null) {

            return Body.getBodyStandardName(bodyVehicle);
        } else {
            return Body.getBodyByTitle(bodyVehicle).getTitle();
        }
    }

    private String getLprBodyOfVehicleInfoByTitle(String bodyVehicle) {
        if (bodyVehicle != null && !TextUtils.isEmpty(bodyVehicle)) {
            String bodyByCode = LprBodyMap.getBodyCodeByName(bodyVehicle);
            if (bodyByCode != null) {
                return bodyByCode;
            }
        }
        return "UNK";
        /*if (Body.getBodyByTitle(bodyVehicle) == null) {

            return Body.getBodyStandardName(bodyVehicle);
        } else {
            return Body.getBodyByTitle(bodyVehicle).getTitle();
        }*/
    }

    /**
     * @return - Return make information with translated/manipulated text
     * @Params - Vehicle title required to get information.
     * @Uses - When we request autoLPR to get vehicle details by images
     * it returns vehicle information by scanning image, we further map data
     * as per our available records.
     * @since - Version - 3.1.003
     **/
    private String getMakeOfVehicleInfoByTitle(String makeVehicle) {
        if (MakeAndModel.getMakeByTitle(makeVehicle) == null) {
            return MakeAndModel.getMakeStandardName(makeVehicle);
        } else {
            return MakeAndModel.getMakeByTitle(makeVehicle).getMakeTitle();
        }
    }


    /**
     * @return - Return color information with translated/manipulated text
     * @Params - Vehicle title required to get information.
     * @Uses - When we request autoLPR to get vehicle details by images
     * it returns vehicle information by scanning image, we further map data
     * as per our available records.
     * @since - Version - 3.1.003
     **/
    private String getColorOfVehicleInfoByTitle(String colorName) {
        Color.getColorByTitle(color);
        if (Color.getColorByTitle(colorName) == null) {
            Color colorRecord = new Color();
            String color = colorRecord.getColorStandardName(colorName);
            return color;
        } else {
            return Color.getColorByTitle(colorName).getTitle();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void bindDataAtLoadingTime() throws Exception {

    }

    @Override
    public void handleVoiceInput(String text) throws Exception {

    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {

    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {

    }

    /**
     * @return - Return overridden prefs. of user to prevent vehicle request instead of plate.
     * @since - Version - 3.2.013
     * @Params - Nothing Required as param
     * @Required - Application setting required
     * @Uses -  It controls the request point uses and helps to control the cost of request.
     *
     * **/
	/*private boolean isALPRVehicleRequired() {
		boolean isALPRVehicleInfoRequired = false;
		if (TPApplication.getInstance().getUserSettings() != null && TPApplication.getInstance().getUserSettings().isALPRVehicleRequired()) {
			isALPRVehicleInfoRequired = true;
		} else {
			isALPRVehicleInfoRequired = false;
		}

		return isALPRVehicleInfoRequired;
	}*/
	
	/*private boolean isNightModeActive(){
		if(isALPRVehicleRequired()){
			return nightModeCheck.isChecked();
		}else return false;
	}*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isNetworkConnected1() throws IOException {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Network currentNetwork = connectivityManager.getActiveNetwork();
        //NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(currentNetwork);
        //LinkProperties linkProperties = connectivityManager.getLinkProperties(currentNetwork);
        //internetConnectionAvailable()
        //boolean b = internetConnectionAvailable(2000);
        return networkInfo != null
                && networkInfo.isAvailable()
                && networkInfo.isConnected();// && b; //&& internetPingSuccess;

    }

}
