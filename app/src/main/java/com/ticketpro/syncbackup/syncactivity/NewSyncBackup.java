package com.ticketpro.syncbackup.syncactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.syncbackup.synmodels.CSV_Params;
import com.ticketpro.syncbackup.synmodels.CSVinfo_Json_rpc;
import com.ticketpro.syncbackup.synmodels.Csvinfo;
import com.ticketpro.syncbackup.synmodels.Dbinfo;
import com.ticketpro.syncbackup.synmodels.Dbinfo_Json_rpc;
import com.ticketpro.syncbackup.synmodels.Dbinfo_Param;
import com.ticketpro.syncbackup.synmodels.Debuginfo;
import com.ticketpro.syncbackup.synmodels.Params;
import com.ticketpro.syncbackup.synmodels.SyncBackup_Json_rpc;
import com.ticketpro.syncbackup.synmodels.UploadDebugResponse;
import com.ticketpro.util.TPUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewSyncBackup extends BaseActivityImpl {
    private Button back_btn;
    private Button upload_btn;
    ProgressDialog progressDialog;
    ProgressBar upload_debug_log_progress;
    Button upload_debug_log_sent_Btn;
    ProgressBar upload_dbbackup_progress;
    Button upload_dbbackup_btn;
    ProgressBar upload_CSV_backup_progress;
    Button upload_CSV_backup_btn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sync_backup);
        setLogger(NewSyncBackup.class.getName());
        back_btn=findViewById(R.id.shift_back_btn);
        upload_btn=findViewById(R.id.shift_upload_btn);
        progressDialog= new ProgressDialog(NewSyncBackup.this);
        upload_debug_log_progress=findViewById(R.id.log_sent_pgr);
        upload_debug_log_sent_Btn=findViewById(R.id.log_sent_btn);
        upload_dbbackup_progress=findViewById(R.id.db_sent_pgr);
        upload_dbbackup_btn=findViewById(R.id.database_sent_btn);
        upload_CSV_backup_progress=findViewById(R.id.system_sent_pgr);
        upload_CSV_backup_btn=findViewById(R.id.system_sent_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                upload_CSV_backup_btn.setVisibility(View.GONE);
                upload_debug_log_sent_Btn.setVisibility(View.GONE);
                upload_CSV_backup_btn.setVisibility(View.GONE);
                back_btn.setClickable(false);
               // callBackupCSV();
               // callBackupSqlite();

                callBackupCSV();

            }
        });
    }



    public void callBackupCSV() {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> csvinnerFileList= new ArrayList<String>();
        File folder = new File(Environment.getExternalStorageDirectory() + "/TicketPro/PARKING/CSVBackups");
        if(!folder.exists()) {
            Toast.makeText(getApplicationContext(),"CSVBackups Directory does not exist",
                    Toast.LENGTH_LONG).show();
            back_btn.setClickable(true);
            log.error("CsvBackup Directory not found");
        }
        else {
            File[] filesInFolder = folder.listFiles();
            for (File file : filesInFolder) {
                if (!file.isDirectory()) {
                    result.add(new String(file.getName()));
                }
            }



            if (result.size()>0) {
                for (int i = 0; i < result.size(); i++) {

                    File file = new File(Environment.getExternalStorageDirectory() + "/TicketPro/PARKING/CSVBackups/" + result.get(i));

                    if (!(file.exists())) {
                        back_btn.setClickable(true);
                        Toast.makeText(getApplicationContext(), "Directory does not exist",
                                Toast.LENGTH_LONG).show();
                        log.error("/TicketPro/PARKING/CSVBackups/" + result.get(i)+"not found");
                    } else {
                        try {
                            String base64String1 = fileToBase64(file);
                            csvinnerFileList.add(base64String1);

                        } catch (IOException e) {
                            e.printStackTrace();
                            log.error(e.getMessage().toString());
                        }
                    }
                }
                if (csvinnerFileList.size() >= 1) {
                    uploadCSVBackup(result, csvinnerFileList);
                } else {
                    Toast.makeText(getApplicationContext(), "CsvBackup Directory is empty",
                            Toast.LENGTH_LONG).show();
                    back_btn.setClickable(true);
                    log.error("CsvBackup Directory is empty");

                }
            }

            else {
                Toast.makeText(getApplicationContext(), "CsvBackup Directory is empty",
                        Toast.LENGTH_LONG).show();
                back_btn.setClickable(true);
                log.error("CsvBackup Directory is empty");
            }
        }



    }

    public void callBackupSqlite()
    {
        upload_dbbackup_progress.setVisibility(View.VISIBLE);
        backupDatabase();

    }

    public void calldebugLogBackup()
    {
        File file = new File(Environment.getExternalStorageDirectory()+"/TicketPro/PARKING/debug.log");
        boolean success = true;
        if(!file.exists()) {
            back_btn.setClickable(true);
            Toast.makeText(getApplicationContext(),"Directory does not exist",
                    Toast.LENGTH_LONG).show();
            log.error("TicketPro/PARKING/debug.log  not found");
        }
        else
        {
            try {
                String base64String1 = fileToBase64(file);
                uploadDebugLog(base64String1);

            } catch (IOException e) {
                e.printStackTrace();
                back_btn.setClickable(true);
                log.error(e.getMessage().toString());
            }
        }

    }



    public void uploadCSVBackup(ArrayList<String> Csvname,ArrayList<String> CSVdata ) {
        upload_CSV_backup_progress.setVisibility(View.VISIBLE);
        upload_CSV_backup_btn.setVisibility(View.GONE);
        CSVinfo_Json_rpc jsonRpc = new CSVinfo_Json_rpc();
        CSV_Params param = new CSV_Params();
        ArrayList<Csvinfo> CsvModels = new ArrayList<>();

        for (int i=0;i<Csvname.size();i++)
        {
            Csvinfo csvinfo = new Csvinfo();
            csvinfo.setCsvname(Csvname.get(i));
            csvinfo.setCsvData(CSVdata.get(i));
            CsvModels.add(i,csvinfo);

        }
        param.setCustid(TPApp.custId);
        param.setUserid(TPApp.userId);
        param.setCsvinfo(CsvModels);
        param.setDeviceid(TPApp.deviceId);
        param.setModule("Parking");
        jsonRpc.setJsonrpc("2.o");
        jsonRpc.setMethod("uploadCsvInsideDiagnostics");
        jsonRpc.setId("82F85DB43CBF6");
        jsonRpc.setParams(param);
        System.out.println("RequestSync**" + new Gson().toJson(jsonRpc));
        try {
            if (isNetworkConnected()) {
                try {
                    ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
                    api.uploadCSVBackUp(jsonRpc).enqueue(new Callback<UploadDebugResponse>() {
                        @Override
                        public void onResponse(Call<UploadDebugResponse> call, Response<UploadDebugResponse> response) {
                            if (response.isSuccessful()) {
                                Log.d("ResponseBody", response.body().getResult().toString());
                                if (response.body().getResult() != null) {
                                    if (response.body().getResult().getResult()) {
                                        log.info("CSVBackup uploaded sucessfully");
                                        upload_CSV_backup_progress.setVisibility(View.GONE);
                                        upload_CSV_backup_btn.setVisibility(View.VISIBLE);
                                        callBackupSqlite();
                                    } else {
                                        log.error("CSVBackup uploading failed-1");
                                        upload_CSV_backup_progress.setVisibility(View.GONE);
                                        back_btn.setClickable(true);
                                        Toast.makeText(NewSyncBackup.this,response.body().getResult().getMessage().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    log.error("CSVBackup uploading failed-2");
                                    upload_CSV_backup_progress.setVisibility(View.GONE);
                                    back_btn.setClickable(true);
                                    Toast.makeText(NewSyncBackup.this,"Service not responding",Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                log.error("CSVBackup uploading failed-3");
                                back_btn.setClickable(true);
                                /*if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }*/

                                Toast.makeText(NewSyncBackup.this,"Service not responding",Toast.LENGTH_SHORT).show();
                                upload_CSV_backup_progress.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<UploadDebugResponse> call, Throwable t) {
                            back_btn.setClickable(true);
                            upload_CSV_backup_progress.setVisibility(View.GONE);
                            Toast.makeText(NewSyncBackup.this,"Service not responding",Toast.LENGTH_SHORT).show();
                            log.error("CSVBackup uploading failed-4");
                            /*Log.d("Error", t.getMessage());
                            log.error("Mileage Failure");*/
                        }
                    });
                } catch (Exception e) {
                    back_btn.setClickable(true);
                    upload_CSV_backup_progress.setVisibility(View.GONE);
                    e.printStackTrace();
                    log.error(e.getMessage().toString());
                    Toast.makeText(NewSyncBackup.this,"Service not responding",Toast.LENGTH_SHORT).show();


                }
            } else {
                log.error("No internet");
                back_btn.setClickable(true);
                upload_CSV_backup_progress.setVisibility(View.GONE);
                Toast.makeText(NewSyncBackup.this,"Please check your internet connecticity",Toast.LENGTH_SHORT).show();

            }
        } catch (IOException e) {
            log.error(e.getMessage().toString());
            e.printStackTrace();
            Toast.makeText(NewSyncBackup.this,"Service not responding",Toast.LENGTH_SHORT).show();
            back_btn.setClickable(true);
            upload_CSV_backup_progress.setVisibility(View.GONE);
        }
    }


    public void uploadDatabaseBackup(String dbName,String dbData) {
        upload_dbbackup_progress.setVisibility(View.VISIBLE);
        Dbinfo_Json_rpc jsonRpc = new Dbinfo_Json_rpc();
        Dbinfo_Param param = new Dbinfo_Param();
        ArrayList<Dbinfo> dbModels = new ArrayList<>();
        Dbinfo dbinfo = new Dbinfo();
        dbinfo.setDbData(dbData);
        dbinfo.setDbname(dbName);
        dbModels.add(dbinfo);
        param.setCustid(TPApp.custId);
        param.setUserid(TPApp.userId);
        param.setDbinfo(dbModels);
        param.setDeviceid(TPApp.deviceId);
        param.setModule("Parking");
        jsonRpc.setJsonrpc("2.o");
        jsonRpc.setMethod("uploadDatabase");
        jsonRpc.setId("82F85DB43CBF6");
        jsonRpc.setParams(param);
        System.out.println("RequestSync**" + new Gson().toJson(jsonRpc));
        try {
            if (isNetworkConnected()) {
                try {
                    ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
                    api.uploadDbBackUp(jsonRpc).enqueue(new Callback<UploadDebugResponse>() {
                        @Override
                        public void onResponse(Call<UploadDebugResponse> call, Response<UploadDebugResponse> response) {
                            if (response.isSuccessful()) {

                                Log.d("ResponseBody", response.body().getResult().toString());
                                if (response.body().getResult() != null) {
                                    if (response.body().getResult().getResult()) {
                                        log.info("dbBackup uploaded sucessfully");
                                        upload_dbbackup_progress.setVisibility(View.GONE);
                                        upload_dbbackup_btn.setVisibility(View.VISIBLE);
                                        calldebugLogBackup();

                                    } else {
                                        log.error("dbBackup uploading failed-1");
                                        upload_dbbackup_progress.setVisibility(View.GONE);
                                        back_btn.setClickable(true);
                                        Toast.makeText(NewSyncBackup.this,response.body().getResult().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    log.error("dbBackup uploading failed-2");
                                    upload_dbbackup_progress.setVisibility(View.GONE);
                                    back_btn.setClickable(true);
                                    Toast.makeText(NewSyncBackup.this,"Service not responding",Toast.LENGTH_SHORT).show();

                                }

                            }else {
                                log.error("dbBackup uploading failed-3");
                                upload_dbbackup_progress.setVisibility(View.GONE);
                                back_btn.setClickable(true);
                                Toast.makeText(NewSyncBackup.this,"Service not responding",Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<UploadDebugResponse> call, Throwable t) {
                            back_btn.setClickable(true);
                            log.error("dbBackup uploading failed-4");
                            upload_dbbackup_progress.setVisibility(View.GONE);
                            Toast.makeText(NewSyncBackup.this,"Service not responding",Toast.LENGTH_SHORT).show();

                        }
                    });
                } catch (Exception e) {
                    log.error(e.getMessage().toString());
                    back_btn.setClickable(true);
                    upload_dbbackup_progress.setVisibility(View.GONE);
                    e.printStackTrace();
                    Toast.makeText(NewSyncBackup.this,"Service not responding",Toast.LENGTH_SHORT).show();
                }
            } else {
                log.error("dbBackup uploading failed-5");
                back_btn.setClickable(true);
                upload_dbbackup_progress.setVisibility(View.GONE);
                Toast.makeText(NewSyncBackup.this,"Please check your internet connecticity",Toast.LENGTH_SHORT).show();

            }
        } catch (IOException e) {
            log.error(e.getMessage().toString());
            back_btn.setClickable(true);
            upload_dbbackup_progress.setVisibility(View.GONE);
            e.printStackTrace();
            Toast.makeText(NewSyncBackup.this,"Service not responding",Toast.LENGTH_SHORT).show();

        }
    }


    public void uploadDebugLog(String debugDData) {
        upload_debug_log_progress.setVisibility(View.VISIBLE);
        SyncBackup_Json_rpc jsonRpc = new SyncBackup_Json_rpc();
        Params param = new Params();
        ArrayList<Debuginfo> debugModels = new ArrayList<>();
        Debuginfo debuginfo = new Debuginfo();
        debuginfo.setDebugFileData(debugDData);
        debuginfo.setDebugfilename("debug.log");
        debugModels.add(debuginfo);
        param.setCustid(TPApp.custId);
        param.setUserid(TPApp.userId);
        param.setCsvinfo(debugModels);
        param.setDeviceid(String.valueOf(TPApp.deviceId));
        param.setModule("Parking");
        jsonRpc.setJsonrpc("2.o");
        jsonRpc.setMethod("uploadDebugLog");
        jsonRpc.setId("82F85DB43CBF6");
        jsonRpc.setParams(param);
        System.out.println("RequestSync**" + new Gson().toJson(jsonRpc));
        try {
            if (isNetworkConnected()) {
                try {
                    ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
                    api.uploadDebugLog(jsonRpc).enqueue(new Callback<UploadDebugResponse>() {
                        @Override
                        public void onResponse(Call<UploadDebugResponse> call, Response<UploadDebugResponse> response) {
                            if (response.isSuccessful()) {

                                Log.d("ResponseBody", response.body().getResult().toString());
                                if (response.body().getResult() != null) {
                                    if (response.body().getResult().getResult()) {
                                        log.info("debuglog uploaded sucessfully");
                                        upload_debug_log_progress.setVisibility(View.GONE);
                                        upload_debug_log_sent_Btn.setVisibility(View.VISIBLE);
                                        Toast.makeText(NewSyncBackup.this,"System backup uploaded successfully ",Toast.LENGTH_SHORT).show();
                                        back_btn.setClickable(true);
                                    } else {
                                        log.error("debuglog uploading failed-1");
                                        upload_debug_log_progress.setVisibility(View.GONE);
                                        back_btn.setClickable(true);
                                        Toast.makeText(NewSyncBackup.this,response.body().getResult().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    log.error("debuglog uploading failed-2");
                                    upload_debug_log_progress.setVisibility(View.GONE);
                                    back_btn.setClickable(true);
                                    Toast.makeText(NewSyncBackup.this,"Service not responding",Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                log.error("debuglog uploading failed-3");
                                upload_debug_log_progress.setVisibility(View.GONE);
                                back_btn.setClickable(true);
                                Toast.makeText(NewSyncBackup.this,"Service not responding",Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<UploadDebugResponse> call, Throwable t) {
                            back_btn.setClickable(true);
                            log.error(t.getMessage().toString());
                            upload_debug_log_progress.setVisibility(View.GONE);
                            Toast.makeText(NewSyncBackup.this,"Service not responding",Toast.LENGTH_SHORT).show();

                        }
                    });
                } catch (Exception e) {
                    back_btn.setClickable(true);
                    e.printStackTrace();
                    log.error(e.getMessage().toString());
                    upload_debug_log_progress.setVisibility(View.GONE);
                    Toast.makeText(NewSyncBackup.this,"Service not responding",Toast.LENGTH_SHORT).show();


                }
            } else {
                back_btn.setClickable(true);
                log.error("debuglog uploading failed-6");
                upload_debug_log_progress.setVisibility(View.GONE);
                Toast.makeText(NewSyncBackup.this,"Please check your internet connecticity",Toast.LENGTH_SHORT).show();

            }
        } catch (IOException e) {
            log.error(e.getMessage().toString());
            back_btn.setClickable(true);
            upload_debug_log_progress.setVisibility(View.GONE);
            e.printStackTrace();
            Toast.makeText(NewSyncBackup.this,"Service not responding",Toast.LENGTH_SHORT).show();

        }
    }


    public static String fileToBase64(File file) throws IOException {
        FileInputStream inputStream = null;
        try {
            // Read the file as bytes
            inputStream = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputStream.read(buffer);

            // Encode the bytes to Base64
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            // Close the input stream
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


        @Override
    public void onClick(View view) {

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


    private void backupDatabase() {
        //DatabaseHelper.getInstance().backupDatabase();
        ParkingDatabase.backupDatabase(NewSyncBackup.this);
       // upload_dbbackup_progress.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ArrayList<String> result = new ArrayList<String>();
                File file = new File(Environment.getExternalStorageDirectory()+"/TicketPro/PARKING/Backups");

                if(!file.exists()) {
                    Toast.makeText(getApplicationContext(),"Directory does not exist",
                            Toast.LENGTH_LONG).show();
                    back_btn.setClickable(true);
                    log.error("Backups Directory not found");
                }
                else
                {
                    File[] filesInFolder = file.listFiles();
                    for (File file1 : filesInFolder) {
                        if (!file1.isDirectory()) {
                            result.add(new String(file1.getName()));
                        }
                    }

                    if (result.size()>0) {
                        File file3 = new File(Environment.getExternalStorageDirectory() + "/TicketPro/PARKING/Backups/" + result.get(result.size() - 1));
                        boolean success = true;
                        if (!file3.exists()) {
                            Toast.makeText(getApplicationContext(), "Directory does not exist",
                                    Toast.LENGTH_LONG).show();
                            back_btn.setClickable(true);
                            log.error( "/TicketPro/PARKING/Backups/" + result.get(result.size() - 1)+"not found");

                        } else {
                            try {
                                String base64String1 = fileToBase64(file3);
                                if (result.size() > 0) {
                                    uploadDatabaseBackup(result.get(result.size() - 1).toString(), base64String1);
                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                                back_btn.setClickable(true);
                                log.error(e.getMessage().toString());
                            }
                        }
                    }
                }


                }


        }, 5000);



       // progressDialog.dismiss();
    }


        /*upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //callBackupCSV();
            }
        });*/
    /*public static String base64ToString(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return new String(decodedBytes);
    }*/

     /*  File folder = new File(Environment.getExternalStorageDirectory()+"/TicketPro/PARKING/CSVBackups");
        File[] filesInFolder = folder.listFiles();

        for (File file : filesInFolder) { //For each of the entries do:
            if (!file.isDirectory()) { //check that it's not a dir
                result.add(new String(file.getName())); //push the filename as a string
            }
        }*/

    //  String bv=base64ToString(base64String1);
    //   Toast.makeText(NewSyncBackup.this,base64String1,Toast.LENGTH_SHORT).show();
    // Now, the 'base64String' contains the Base64 encoded representation of the file

}