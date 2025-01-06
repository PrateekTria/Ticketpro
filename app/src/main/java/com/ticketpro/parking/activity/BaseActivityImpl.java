package com.ticketpro.parking.activity;

import static com.ticketpro.util.TPConstant.ASSETS_URL;
import static com.ticketpro.util.TPConstant.FIREBASE_DB_URL;
import static com.ticketpro.util.TPConstant.IMAGES_URL;
import static com.ticketpro.util.TPConstant.IS_DEVELOPMENT_BUILD;
import static com.ticketpro.util.TPConstant.IS_STAGING_BUILD;
import static com.ticketpro.util.TPConstant.LPR_URL;
import static com.ticketpro.util.TPConstant.RX_SERVICE_URL;
import static com.ticketpro.util.TPConstant.SERVICE_URL;
import static com.ticketpro.util.TPConstant.UPDATE_URL;

import android.Manifest;
import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.CustomerInfo;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.model.Duty;
import com.ticketpro.model.DutyReport;
import com.ticketpro.model.Feature;
import com.ticketpro.model.SpecialActivityReport;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.User;
import com.ticketpro.model.UserSetting;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.handlers.NotificationHandler;
import com.ticketpro.parking.bl.BLProcessor;
import com.ticketpro.parking.proxy.Proxy;
import com.ticketpro.parking.service.JobIntentServiceSaveChalk;
import com.ticketpro.parking.service.JobIntentServiceSaveTicket;
import com.ticketpro.parking.service.TPSyncAdapter;
import com.ticketpro.util.BitmapDownloaderTask;
import com.ticketpro.util.CallbackHandler;
import com.ticketpro.util.CustomExceptionHandler;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;
import com.twotechnologies.n5library.BarcodeScanner.BarcodeConfiguration;
import com.twotechnologies.n5library.BarcodeScanner.BarcodeManualControl;
import com.twotechnologies.n5library.BarcodeScanner.BarcodeSetting;
import com.twotechnologies.n5library.client.BarcodeDataListener;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//import com.crashlytics.android.Crashlytics;

//import io.fabric.sdk.android.Fabric;

/**
 * Implementer of Base screen. Take cares all the common activities of the
 * screens.
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public abstract class BaseActivityImpl extends Activity
        implements BaseActivity, OnClickListener, TextToSpeech.OnInitListener, LocationListener {

    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 2;
    public static final long MIN_TIME_BW_UPDATES = 200;
    public static final String MyPREFERENCES = "MyPrefs";
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    public static BaseActivityImpl instance;
    String type = "";
    private static String[] systemCommands = {"back", "close", "end", "end shift", "change duty", "go back", "done",
            "go", "okay", "ok", "cancel", "delete", "clear list", "delete all", "clear", "clear all", "select", "gps",
            "gps location", "location", "permit", "plate", "meter", "vin", "space", "body", "color", "make", "model",
            "state", "lpr", "edit location", "edit permit", "edit plate", "edit vin", "edit space", "select body",
            "select color", "select model", "select make", "select state", "print preview", "print", "save",
            "save ticket", "save chalk", "add chalk", "view chalks", "view list", "view photos", "view violations",
            "violations", "view comments", "comments", "add violation", "take photo", "take picture", "photos",
            "show actions", "special", "photo chalk", "new chalk", "write ticket", "new ticket", "void last ticket",
            "warn last ticket", "menu", "check warn", "check driveaway", "uncheck warn", "uncheck driveaway"};
    private static List<String> commands;
    private static TextToSpeech tts;
    public BLProcessor screenBLProcessor;
    public TPApplication TPApp;
    public Logger log;
    public boolean isServiceAvailable = false;
    public boolean isFastConnection = true;
    public boolean isBarcodeBtnPressed = false;
    public LocationManager locationManager;
    public Location gpsLocation;
    public View.OnKeyListener onKeyListener;
    public boolean isNetworkInfoRequired = true;
    protected NetworkInfo mNetworkInfo;
    protected BroadcastReceiver networkStateReceiver;
    LocationManager mLocationManager;
    private String TAG = "TicketPRO";
    private BaseActivity activeScreen;
    private ImageView voiceImage;
    private SpeechRecognizer sr;
    private SharedPreferences mPreferences;
    private SharedPreferences sharedpreferences;
    private DefaultHttpClient httpClient;
    private CallbackHandler barcodeCallback;
    private BarcodeConfiguration mBarcodeConfiguration = new BarcodeConfiguration();
    private boolean success = false;
    boolean internetPingSuccess = false;

    private BarcodeDataListener barcodeDataComplete = new BarcodeDataListener(this) {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("serial");
            if (barcodeCallback != null) {
                barcodeCallback.success(data);
            }
        }
    };

    public BaseActivityImpl getInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TPApp = (TPApplication) getApplicationContext();

        instance = this;
        SpecialActivityReport specialActivityReport = new SpecialActivityReport();
        TPApp.setReport(specialActivityReport);
        // Force to manual mode
        mBarcodeConfiguration.setSetting(BarcodeSetting.AUTOMODE, false);

        // Start barcode data listener
        barcodeDataComplete.startListening();

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                Looper.prepare();
                if (locationManager == null) {
                    return;
                }
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (isNetworkInfoRequired) {
                    initGPSUpdates();
                }
            }
        }).start();

        try {
            canPingInternetConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void initGPSUpdates() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (locationManager == null) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, this);
            //gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            gpsLocation = getLastKnownLocation();
           /* if(gpsLocation==null){
                gpsLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }*/
        }
    }

    //Location myLocation = getLastKnownLocation();
    @RequiresApi(api = Build.VERSION_CODES.M)
    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (isNetworkInfoRequired) {
                if (networkStateReceiver != null) {
                    unregisterReceiver(networkStateReceiver);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopListening();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        gpsLocation = location;
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    private BroadcastReceiver getNetworkStateReceiver() {
        networkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                mNetworkInfo = connectivityManager.getActiveNetworkInfo();

                if (mNetworkInfo == null || mNetworkInfo.isConnectedOrConnecting() == false) {
                    isServiceAvailable = false;
                    isFastConnection = false;

                    TPApp.isServiceAvailable = isServiceAvailable;

                    handleNetworkStatus(false, false);

                } else {

                    new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void run() {
                            boolean isSyncRequired = false;

                            isFastConnection = isFastConnection(mNetworkInfo.getType(), mNetworkInfo.getSubtype());
                            try {
                                //isServiceAvailable = isFastConnection && isNetworkConnected1();
                                isServiceAvailable = isNetworkConnected1();
                                isFastConnection = isNetworkConnected1();
                               /* if (isNetworkConnected1()){
                                    log.info("***********1. Network Status Connected*********");
                                }else {
                                    log.info("***********1. Network Status Not Connected*********");

                                }
                                if (isFastConnection){
                                    log.info("***********2. "+type+ "connection available*********");

                                }else {
                                    log.info("***********2. LTE connection not available*********");

                                }
                                if (isServiceAvailable){
                                    log.info("***********3. Both LTE and Network Stats Connected*********");
                                }else {
                                    log.info("***********3. Network Not Connected*********");

                                }*/

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            TPApp.isServiceAvailable = isServiceAvailable;

                            Date lastNetworkTime = TPApp.getLastNetworkTime();
                            if (lastNetworkTime != null) {
                                long diff = new Date().getTime() - lastNetworkTime.getTime();
                                isSyncRequired = diff >= (2 * 60 * 1000);
                            } else {
                                isSyncRequired = true;
                            }

                            // Request for Sync or verify and send pending
                            // tickets
                            if (isServiceAvailable && TPApp.dbConfigured && isSyncRequired) {
                                if (TPApp.isSyncRequired()) {
                                    Account syncAccount = new Account(TPSyncAdapter.ACCOUNT, TPSyncAdapter.ACCOUNT_TYPE);
                                    ContentResolver.requestSync(syncAccount, TPSyncAdapter.AUTHORITY, new Bundle());
                                }

                                if (screenBLProcessor != null) {
                                    Proxy serviceProxy = screenBLProcessor.getProxy();
                                    if (serviceProxy != null) {
                                       /* try {
                                            if (isNetworkConnected()) {


                                                try {
                                                    ArrayList<ChalkVehicle> pendingChalkedVehicle = ChalkVehicle.getPendingChalkedVehicle();
                                                    if (pendingChalkedVehicle!=null && pendingChalkedVehicle.size()>0){
                                                        Intent serviceIntent = new Intent(BaseActivityImpl.this, JobIntentServiceSaveChalk.class);
                                                        JobIntentServiceSaveChalk.enqueueWork(BaseActivityImpl.this, serviceIntent);

                                                    }else { Log.d("WriteTicketActivity","No pending chalk available.");}
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                ArrayList<Ticket> pendingTickets = Ticket.getPendingTickets();
                                                ArrayList<Ticket> pendingTicketsPI = Ticket.getPendingTicketsPI();
                                                if (pendingTickets.size()>0 ||pendingTicketsPI.size()>0) {
                                                    Intent serviceIntent = new Intent(BaseActivityImpl.this, JobIntentServiceSaveTicket.class);
                                                    JobIntentServiceSaveTicket.enqueueWork(BaseActivityImpl.this, serviceIntent);
                                                }
                                                //serviceProxy.verifyAndUploadTickets(getApplicationContext(), false);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }*/
                                    }
                                }
                            } else {
                                if (screenBLProcessor != null) {
                                    Proxy serviceProxy = screenBLProcessor.getProxy();
                                    if (serviceProxy != null) {
                                       /* try {
                                            if (isNetworkConnected()) {


                                                try {
                                                    ArrayList<ChalkVehicle> pendingChalkedVehicle = ChalkVehicle.getPendingChalkedVehicle();
                                                    if (pendingChalkedVehicle!=null && pendingChalkedVehicle.size()>0){
                                                        Intent serviceIntent = new Intent(BaseActivityImpl.this, JobIntentServiceSaveChalk.class);
                                                        JobIntentServiceSaveChalk.enqueueWork(BaseActivityImpl.this, serviceIntent);

                                                    }else { Log.d("WriteTicketActivity","No pending chalk available.");}
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                ArrayList<Ticket> pendingTickets = Ticket.getPendingTickets();
                                                ArrayList<Ticket> pendingTicketsPI = Ticket.getPendingTicketsPI();
                                                if (pendingTickets.size()>0 ||pendingTicketsPI.size()>0) {
                                                    Intent serviceIntent = new Intent(BaseActivityImpl.this, JobIntentServiceSaveTicket.class);
                                                    JobIntentServiceSaveTicket.enqueueWork(BaseActivityImpl.this, serviceIntent);
                                                }
                                                //serviceProxy.verifyAndUploadTickets(getApplicationContext(), false);
                                            }
                                        }  catch (IOException e) {
                                            e.printStackTrace();
                                        }*/
                                    }
                                }
                            }

                            BaseActivityImpl.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    handleNetworkStatus(isServiceAvailable, isFastConnection);

                                    if (isServiceAvailable) {
                                        TPApp.setLastNetworkTime(new Date());
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        };

        return networkStateReceiver;
    }



    @Override
    protected void onResume() {
        super.onResume();

        new Thread(() -> {
            Looper.prepare();
            if (isNetworkInfoRequired) {
                // Initialize Network State BroadcastReceiver
                if (networkStateReceiver == null) {
                    networkStateReceiver = getNetworkStateReceiver();
                }

                IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                registerReceiver(networkStateReceiver, filter);

                if (ActivityCompat.checkSelfPermission(BaseActivityImpl.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BaseActivityImpl.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (locationManager == null) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, BaseActivityImpl.this);
            }
        }).start();

        mPreferences = getApplicationContext().getSharedPreferences(getPackageName(), MODE_PRIVATE);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (!TPConstant.IS_DEBUG_MODE) {
            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(getBaseContext(), this));
        }

        TPApp.currentAcivity = this;

        checkAndRestoreSession();

        // Check if User is logged in and session is reset due some error or
        // idle time.
        boolean isLoggedIn = mPreferences.getBoolean(TPConstant.PREFS_KEY_IS_LOGGED_IN, false);

        if (isLoggedIn && TPApp.getCurrentUserId() == 0) {
            try {
                restoreSession();
            } catch (Exception e) {
                log.error("Error while restoring session. " + e.getMessage());
            }
        }

        voiceImage = findViewById(R.id.voice_search_icon);

        if (Feature.isFeatureAllowed(Feature.VOICE_COMMANDS)) {
            if (voiceImage != null) {
                voiceImage.setVisibility(View.VISIBLE);
                try {
                    if (tts == null) {
                        tts = new TextToSpeech(this, this);
                    }
                } catch (Exception e) {
                    log.error("Error initializing TTS Service. " + e.getMessage());
                }

                if (TPApp.voiceMode) {
                    voiceImage.setImageResource(R.drawable.voice_search_enabled);
                } else {
                    voiceImage.setImageResource(R.drawable.voice_search_disabled);
                }
            }

            commands = Arrays.asList(systemCommands);
            if (TPApp.voiceMode) {
                startListening();
            } else {
                stopListening();
            }
        }

        if (TPApp.resumeFromNotification) {
            TPApp.resumeFromNotification = false;

            if (TPApp.notificationIntents.size() > 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Notifications");
                ListView listView = new ListView(this);
                String[] stringArray = new String[TPApp.notificationIntents.size()];

                for (int i = 0; i < TPApp.notificationIntents.size(); i++) {
                    Bundle extras = TPApp.notificationIntents.get(i).getExtras();
                    String message = extras.getString("Message");
                    stringArray[i] = message;
                }

                ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
                listView.setAdapter(modeAdapter);

                builder.setView(listView);
                final Dialog dialog = builder.create();
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    NotificationHandler handler = new NotificationHandler(BaseActivityImpl.this, TPApp.notificationIntents.get(position));
                    try {
                        handler.showNotification();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                dialog.show();
            } else if (TPApp.notificationIntents.size() > 0) {
                NotificationHandler handler = new NotificationHandler(this, TPApp.notificationIntents.get(0));
                try {
                    handler.showNotification();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            TPApp.notificationIntents.clear();
        }
    }

    public BLProcessor getScreenBLProcessor() {
        return screenBLProcessor;
    }

    @Override
    public void setBLProcessor(BLProcessor screenBLProcessor) {
        this.screenBLProcessor = screenBLProcessor;

        this.onKeyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    return true;
                }

                return false;
            }
        };
    }

    @Override
    public void setLogger(String classInstance) {
        log = Logger.getLogger(classInstance);
    }

    public void backAction(View view) {
        TPConstant.parkeon = false;
        setResult(RESULT_CANCELED);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isNetworkConnected() throws IOException {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Network currentNetwork = connectivityManager.getActiveNetwork();
        //NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(currentNetwork);
        //LinkProperties linkProperties = connectivityManager.getLinkProperties(currentNetwork);

        return networkInfo != null
                && networkInfo.isAvailable()
                && networkInfo.isConnected();

    }

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



    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    private boolean internetConnectionAvailable(int timeOut) {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    return InetAddress.getByName("google.com");
                } catch (UnknownHostException e) {
                    return null;
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
            return false;
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
            return  false;
        }
        return inetAddress!=null && !inetAddress.equals("");
    }
    public void canPingInternetConnection() throws IOException {

       Observable.fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (InetAddress.getByName("www.google.com").isReachable(3000)) {
                            return true;
                        }
                        return false;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        internetPingSuccess = aBoolean;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });



        /*ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Don't process if network is not available or disconnected
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            return false;
        }
 try {
            ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
            RequestPOJO requestPOJO;
            Params params;
            if (ticketCounter==ticketsArrayDetails.size())
                return;
            Ticket ticket = ticketsArrayDetails.get(ticketCounter);

            final ArrayList<Ticket> _singleTicketArray = new ArrayList<>();
            final ArrayList<String> uploadImages = new ArrayList<>();
            final ArrayList<String> uploadVoiceComments = new ArrayList<>();

            long ticketId = Ticket.getTicketIdByCitationNumber(ticket.getCitationNumber());
            ArrayList<TicketComment> comments = TicketComment.getTicketComments(ticketId, ticket.getCitationNumber());
            for (TicketComment comment : comments) {
                if (comment.isVoiceComment()) {
                    uploadVoiceComments.add(comment.getComment());
                }
            }
            ArrayList<TicketPicture> pictures = TicketPicture.getTicketPictures(ticketId, ticket.getCitationNumber());
            ArrayList<TicketPicture> ticketPictures = new ArrayList<>();
            for (TicketPicture picture : pictures) {
                if ("Y".equalsIgnoreCase(picture.getLprNotification())) {
                    continue;
                }
                uploadImages.add(picture.getImagePath());
                String[] path = picture.getImagePath().split("/");
                picture.setImagePath(path[path.length - 1]);
                ticketPictures.add(picture);
            }
            ticket.setTicketPictures(ticketPictures);
            _singleTicketArray.add(ticket);


            requestPOJO = new RequestPOJO();
            requestPOJO.setMethod("updateTickets");
            params = new Params();
            params.setTickets(_singleTicketArray);
            requestPOJO.setParams(params);

            api.syncTickets(requestPOJO).enqueue(new Callback<TicketResponse>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(@NotNull Call<TicketResponse> call, @NotNull Response<TicketResponse> response) {
                    ticketCounter++;
                    try {
                        if (response.isSuccessful() && response.body().getResult().getResult()) {
                            Log.i(TAG, "onResponse: " + new Gson().toJson(response.body()));
                            syncUploadImages(uploadImages);
                            uploadVoiceComments(uploadVoiceComments);
                            assert response.body() != null;
                            if (response.body().getResult().getSuccess() != null) {
                                updateTicketStatus(response.body().getResult().getSuccess());

                            }
                            saveTicketOnServer(ticketsArrayDetails);
                        }else {
                            saveTicketOnServer(ticketsArrayDetails);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error(e.getMessage());
                        ticketCounter++;
                        saveTicketOnServer(ticketsArrayDetails);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<TicketResponse> call, @NotNull Throwable t) {
                    log.error("Function name is saveTicketTask()  " + t.getMessage());

                    log.error(TPUtility.getPrintStackTrace(t));
                    ticketCounter++;
                    saveTicketOnServer(ticketsArrayDetails);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            ticketCounter++;
            saveTicketOnServer(ticketsArrayDetails);
        }
        if (connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected()) {
            try {

                URL url = new URL("https://google.com/");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("User-Agent", "Test");
                urlConnection.setRequestProperty("Connection", "close");
                urlConnection.setConnectTimeout(3000);
                urlConnection.connect();
                return urlConnection.getResponseCode() == 200;
            } catch (Exception e) {
                Log.e(TPConstant.TAG, "Error " + e.getMessage());
            }
        }
        return false;*/
    }

    public boolean isGpsAvailable() {
        PackageManager pm = this.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
    }

    public void checkAndRestoreSession() {
        try {
            boolean restoreFlag = mPreferences.getBoolean(TPConstant.PREFS_KEY_RESTORE_SESSION, false);
            if (restoreFlag) {
                Toast.makeText(this, "Restoring Session...", Toast.LENGTH_SHORT).show();
                restoreSession();
            }
        } catch (Exception e) {
            log.error("Error while restoring session." + e.getMessage());
        }
    }

    private void restoreSession() throws Exception {
        try {
            int where = sharedpreferences.getInt("where", 0);
            if (where == 1) {
                SERVICE_URL = "https://tpwebservicesdev24.ticketproweb.com/index.php/service/genericv1";
                RX_SERVICE_URL = "https://tpwebservicesdev24.ticketproweb.com/index.php/";
                ASSETS_URL = "hhttps://tpwebservicesdev24.ticketproweb.com/assets/customers";
                UPDATE_URL = "https://tpwebservicesdev24.ticketproweb.com/updates";
                IMAGES_URL = "https://tpwebservicesdev24.ticketproweb.com/images/customers";
                LPR_URL = "http://lprdev.ticketproweb.com/LPRWcfService/LPRService.svc?wsdl";
                FIREBASE_DB_URL = "http://trackerdev.ticketproweb.com:8081/api/";
                IS_DEVELOPMENT_BUILD = true;
            }
            if (where == 2) {
                SERVICE_URL = "https://tpwebservicestage.ticketproweb.com/public/index.php/service";
                RX_SERVICE_URL = "https://tpwebservicestage.ticketproweb.com/public/index.php/";
                ASSETS_URL = "https://tpwebservicestage.ticketproweb.com/public/assets/customers";
                UPDATE_URL = "https://tpwebservicestage.ticketproweb.com/public/updates";
                IMAGES_URL = "https://tpwebservicestage.ticketproweb.com/public/images/customers";
                LPR_URL = "http://lprdev.ticketproweb.com/LPRWcfService/LPRService.svc?wsdl";
                FIREBASE_DB_URL = "http://trackerdev.ticketproweb.com:8081/api/";
                IS_STAGING_BUILD = true;
                IS_DEVELOPMENT_BUILD = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            int version = Integer.parseInt(Build.VERSION.SDK);
            if (version >= 10) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(TPConstant.PREFS_KEY_RESTORE_SESSION, false);
            editor.apply();

            TPApp.userId = mPreferences.getInt(TPConstant.PREFS_KEY_RESTORE_USERID, 0);
            TPApp.deviceId = mPreferences.getInt(TPConstant.PREFS_KEY_RESTORE_DEVICEID, 0);
            TPApp.custId = mPreferences.getInt(TPConstant.PREFS_KEY_RESTORE_CUSTID, 0);

            TPApp.dbConfigured = true;
            if (TPApp.getCurrentUserId() == 0) {
                Intent intent = new Intent();
                intent.putExtra(TPConstant.EXTRA_RESTORE_SESSION, true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(this, HomeActivity.class);
                log.error("se");
                startActivity(intent);
                finish();
                return;
            }

            // Get User Info
            User userInfo = User.getUserInfo(TPApp.getCurrentUserId());
            if (userInfo != null) {
                TPApp.setUserInfo(userInfo);
            }

            // Get Device Info
            DeviceInfo deviceInfo = DeviceInfo.getDeviceInfoById(TPApp.deviceId);
            if (deviceInfo != null) {
                TPApp.setDeviceInfo(deviceInfo);

                // Get Customer Info
                CustomerInfo customerInfo = CustomerInfo.getCustomerInfo(deviceInfo.getCustId());
                TPApp.setCustomerInfo(customerInfo);
            }

            int dutyId = mPreferences.getInt(TPConstant.PREFS_KEY_RESTORE_DUTYID, 0);
            if (dutyId > 0) {
                Duty duty = Duty.getDutyById(dutyId);
                TPApp.setActiveDutyInfo(duty);

                DutyReport dutyReport = new DutyReport();
                dutyReport.setDateIn(new Date());
                dutyReport.setDutyId(dutyId);
                dutyReport.setUserId(TPApp.userId);
                dutyReport.setCustId(TPApp.custId);
                TPApp.setActiveDutyReport(dutyReport);
            }
            try {
                SpecialActivityReport specialActivityReport = new SpecialActivityReport();
                TPApp.setReport(specialActivityReport);
                TPApp.setUserSettings(UserSetting.getUserSettings(TPApp.userId));
            } catch (TPException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isEmptyString(String str) {
        return str != null && !str.equals("");
    }

    public BaseActivity getActiveScreen() {
        return activeScreen;
    }

    public void setActiveScreen(BaseActivity screen) {
        this.activeScreen = screen;
    }

    public void speakText(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TPConstant.TAG, "This Language is not supported");
            }
        } else {
            Log.e(TPConstant.TAG, "Initilization Failed!");
        }
    }

    private void startListening() {
        if (activeScreen == null) {
            return;
        }

        // Check if voice mode is enabled
        if (!TPApp.voiceMode) {
            stopListening();
            return;
        }

        // Check if SpeechRecognizer is not initialized
        if (sr == null) {
            sr = SpeechRecognizer.createSpeechRecognizer(this);
            sr.setRecognitionListener(new VoiceListener());
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        sr.startListening(intent);

    }

    public void enableVoiceMode(View view) {
        ImageView voiceImage = (ImageView) findViewById(R.id.voice_search_icon);
        if (!TPApp.voiceMode) {
            TPApp.voiceMode = true;
            voiceImage.setImageResource(R.drawable.voice_search_enabled);
            speakText("Voice mode enabled.");
            startListening();
        } else {
            TPApp.voiceMode = false;
            voiceImage.setImageResource(R.drawable.voice_search_disabled);
            speakText("Voice mode disabled.");
            stopListening();
        }

        // Invoke Screen's handle voice mode method;
        if (activeScreen != null) {
            activeScreen.handleVoiceMode(TPApp.voiceMode);
        }
    }

    public void displayErrorMessage(final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Builder dialog = new AlertDialog.Builder(BaseActivityImpl.this);
                dialog.setCancelable(false);
                dialog.setMessage(message);
                dialog.setTitle("Alert");
                dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });
    }

    public void showAlertDialog(final String message) {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the message for the dialog (no title)
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the OK button click
                        dialog.dismiss();
                    }
                });


        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void stopListening() {
        if (sr != null) {
            sr.cancel();
            sr.stopListening();
            sr.destroy();
            sr = null;
        }

        if (tts != null) {
            tts.shutdown();
        }
    }

    private boolean isFastConnection(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            int signalStrength = 0;

            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            List<ScanResult> scanResult = wifiManager.getScanResults();
            for (ScanResult result : scanResult) {
                if (result.BSSID.equals(wifiManager.getConnectionInfo().getBSSID())) {
                    int level = WifiManager.calculateSignalLevel(wifiManager.getConnectionInfo().getRssi(),
                            result.level);
                    int difference = level * 100 / result.level;

                    if (difference >= 100) {
                        signalStrength = 5;

                    } else if (difference >= 80) {
                        signalStrength = 4;

                    } else if (difference >= 60) {
                        signalStrength = 3;

                    } else if (difference >= 40) {
                        signalStrength = 2;

                    } else if (difference >= 20) {
                        signalStrength = 1;
                    }
                }
            }
            return true;

        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            /*switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps

                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps

                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps

                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps

                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps

                case TelephonyManager.NETWORK_TYPE_GPRS:
                    type = TelephonyManager.NETWORK_TYPE_GPRS;
                    return false; // ~ 100 kbps

                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    type = TelephonyManager.NETWORK_TYPE_HSDPA;
                    return true; // ~ 2-14 Mbps

                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps

                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    type = TelephonyManager.NETWORK_TYPE_HSUPA;
                    return true; // ~ 1-23 Mbps

                case TelephonyManager.NETWORK_TYPE_UMTS:
                    type = TelephonyManager.NETWORK_TYPE_UMTS;
                    return true; // ~ 400-7000 kbps

                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    type = TelephonyManager.NETWORK_TYPE_EVDO_B;
                    return true; // ~ 1-2 Mbps

                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    type = TelephonyManager.NETWORK_TYPE_EVDO_B;
                    return true; // ~ 5 Mbps

                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return false; // ~25 kbps

                case TelephonyManager.NETWORK_TYPE_LTE:
                    type = TelephonyManager.NETWORK_TYPE_LTE;
                    return true; // ~ 10+ Mbps

                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }*/
        }
        return true;
    }

    public String getBestMatch(ArrayList<String> matches) {
        String result = matches.get(0);
        for (int i = 0; i < matches.size(); i++) {
            if (commands.contains(matches.get(i))) {
                return matches.get(i);
            }
        }
        return result;
    }

    public String getCustomerImagesURL(String filename) {
        CustomerInfo customerInfo = TPApp.getCustomerInfo();
        String contentFolder = customerInfo.getContentFolder();
        if (contentFolder == null || contentFolder.equals("")) {
            contentFolder = customerInfo.getCustId() + "";
        }

        return TPConstant.IMAGES_URL + "/" + contentFolder + "/" + filename;
    }

    public void lazyLoadImage(String url, String photoName, ImageView imageView) {
        File imgFile = new File(TPUtility.getLPRImagesFolder() + photoName);
        if (imgFile.exists()) {
            imageView.setImageURI(Uri.fromFile(imgFile));
            return;
        }

        BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
        task.execute(url, photoName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (barcodeDataComplete != null) {
            barcodeDataComplete.stopListening();
        }
        //DatabaseHelper.getInstance().close();
    }

    public void processBarcode(final CallbackHandler callback) {
        barcodeCallback = callback;

        int result = BarcodeManualControl.startScan();
        if (result == -1) {
            Toast.makeText(this, "Scanner in automatic mode – scan not started", Toast.LENGTH_LONG).show();
        }
    }

    class VoiceListener implements RecognitionListener {

        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "onReadyForSpeech");
        }

        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech");
        }

        public void onRmsChanged(float rmsdB) {
            if (TPApp.voiceMode && voiceImage != null) {
                if (rmsdB > 1) {
                    voiceImage.setImageResource(R.drawable.voice_search);
                } else {
                    voiceImage.setImageResource(R.drawable.voice_search_enabled);
                }
            }
        }

        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "onBufferReceived");
        }

        public void onEndOfSpeech() {
            Log.d(TAG, "onEndofSpeech");
        }

        public void onError(int error) {
            Log.d(TAG, "error " + error);
            switch (error) {
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                case SpeechRecognizer.ERROR_SERVER:
                case SpeechRecognizer.ERROR_NO_MATCH:
                    sr.cancel();
                    startListening();
                    break;

                default:
                    break;
            }
        }

        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (activeScreen != null && matches.size() > 0) {
                String bestMatch = getBestMatch(matches).toUpperCase(Locale.getDefault());
                try {
                    activeScreen.handleVoiceInput(bestMatch);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (TPApp.voiceMode) {
                    sr.cancel();
                    startListening();
                }
            }
        }

        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "onPartialResults");
        }

        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent " + eventType);
        }
    }

    public interface ApiCallback {
        void onResponse(boolean success);
    }

}
