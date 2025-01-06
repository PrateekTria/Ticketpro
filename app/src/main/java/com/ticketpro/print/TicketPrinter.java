package com.ticketpro.print;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ticketpro.model.DeviceInfo;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.print.model.BluetoothInfo;
import com.ticketpro.print.model.PrintServiceInfo;
import com.ticketpro.print.model.TCPIPInfo;
import com.ticketpro.print.model.TicketPROConstant;
import com.ticketpro.print.model.TicketPrinterSetting;
import com.ticketpro.util.CallbackHandler;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class TicketPrinter extends Activity {
    public static final String LOGTAG = TicketPrinter.class.getSimpleName();
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "deviceName";
    public static final String TOAST = "toast";
    public static final String COMMUNICATION_METHOD_TCPIP = "TCP/IP";
    public static final String COMMUNICATION_METHOD_BLUETOOTH = "BLUETOOTH";
    public static final String COMMUNICATION_METHOD_BLUETOOTH_ZEBRA = "ZEBRA_BLUETOOTH";
    public static final String COMMUNICATION_METHOD_TSC_BLUETOOTH = "TSC BLUETOOTH";
    public static final String COMMUNICATION_METHOD_PRINTSERVICE = "Internal";
    // keys to access extra data for the intend.
    public static final String PRINTER_IPADDRESS_KEY = "PRINTER_IPAddress";
    public static final String PRINTER_TCPIPPORT_KEY = "PRINTER_TCPIPPort";
    public static final String PRINTER_BLUETOOTH_DEVICE_NAME_KEY = "PRINTER_Bluetooth_Device_Name";
    public static final String PRINTER_SERVICE_NAME_KEY = "PRINTER_Service_Device_Name";
    public static final String APPLICATION_COMM_METHOD_NAME_KEY = "APPLICATION_Comm_Method_Name";
    public static final String APPLICATION_SELECTED_FILE_NAME_KEY = "APPLICATION_Selected_File_Name";
    public static final String DEBUG_MODE_KEY = "DEBUG_Mode";
    private static final int CONFIG_TCPIP_REQUEST = 0; // for TCPIP configuration
    private static final int CONFIG_BLUETOOTH_REQUEST = 1; // for Bluetooth
    private static Dialog printTicketDialog;
    private static Logger logger = Logger.getLogger("TicketPrinter");
    private static ProgressDialog progressDialog;
    private final Context appContext = this;
    // use to update the UI information.
    private final Handler handler = new Handler(); // Main thread
    public String mConnectedBTDeviceName = "";
    // display the printing status
    private TextView printStatusTextView;
    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    Log.i(LOGTAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            ShowPrintingStatusMessage("Connected");
                            break;

                        case BluetoothChatService.STATE_CONNECTING:
                        case BluetoothChatService.STATE_LISTEN:

                        case BluetoothChatService.STATE_NONE:
                            ShowPrintingStatusMessage("Not Connected");
                            break;
                    }
                    break;

                case MESSAGE_WRITE:
                    break;

                case MESSAGE_READ:
                    break;

                case MESSAGE_DEVICE_NAME:
                    mConnectedBTDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to " + mConnectedBTDeviceName, Toast.LENGTH_SHORT)
                            .show();
                    break;

                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };
    private String printStatusStr = "";
    private Button printButton;
    private Button saveSettingButton;
    private Spinner fileSpinner;
    private Spinner communicationSpinner;
    // application setting
    private TicketPrinterSetting appSetting = new TicketPrinterSetting("PrintTest1.prn", COMMUNICATION_METHOD_BLUETOOTH);
    // TCPIP Information object
    private TCPIPInfo TCPIPInfo = new TCPIPInfo("10.0.2.2", 5000);
    // Bluetooth information object
    private BluetoothInfo BluetoothInfo = new BluetoothInfo("Unknown");
    // N5 Printer Info
    private PrintServiceInfo PrintServiceInfo = new PrintServiceInfo("N5");
    private SharedPreferences mPreferences;
    private TextView printerNameTextView;
    private CheckBox debugModeChk;
    private CheckBox showDialogChk;

    public static void print(Activity activity, String printString) {
        SharedPreferences mPreferences = activity.getSharedPreferences(activity.getPackageName(), MODE_PRIVATE);
        String methodName = mPreferences.getString(APPLICATION_COMM_METHOD_NAME_KEY, null);

        if (methodName == null) {
            Builder dialog = new AlertDialog.Builder(activity);
            dialog.setCancelable(false);
            dialog.setMessage("Printer is not configured properly.");
            dialog.setTitle("Printing Error");
            dialog.setPositiveButton("OK", (dialog1, which) -> dialog1.cancel());

            dialog.show();
        } else if (methodName.equalsIgnoreCase(COMMUNICATION_METHOD_TCPIP)) {
            new NetworkPrinter(activity, printString);
        } else if (methodName.equalsIgnoreCase(COMMUNICATION_METHOD_BLUETOOTH)) {
            new BluetoothPrinter(activity, printString);
        } else if (methodName.equalsIgnoreCase(COMMUNICATION_METHOD_PRINTSERVICE)) {
            new N5TicketPrinter(activity).print(printString);
        } else if (methodName.equalsIgnoreCase(COMMUNICATION_METHOD_TSC_BLUETOOTH)) {
            new TSCBluetoothPrinter(activity, printString).print(false);
            //logger.error("PRINT STRING" + printString);
            logger.debug("PRINT STRING" + printString);
        }
    }

    public static void print(Activity activity, ArrayList<String> printTickets) {
        SharedPreferences mPreferences = activity.getSharedPreferences(activity.getPackageName(), MODE_PRIVATE);
        String methodName = mPreferences.getString(APPLICATION_COMM_METHOD_NAME_KEY, null);

        StringBuilder tickets = new StringBuilder();
        for (String str : printTickets) {
            tickets.append(str);
        }

        if (methodName == null) {
            Builder dialog = new AlertDialog.Builder(activity);
            dialog.setCancelable(false);
            dialog.setMessage("Printer is not configured properly.");
            dialog.setTitle("Printing Error");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            dialog.show();
        } else if (methodName.equalsIgnoreCase(COMMUNICATION_METHOD_TCPIP)) {
            new NetworkPrinter(activity, tickets.toString());
        } else if (methodName.equalsIgnoreCase(COMMUNICATION_METHOD_BLUETOOTH)) {
            new BluetoothPrinter(activity, tickets.toString());
        } else if (methodName.equalsIgnoreCase(COMMUNICATION_METHOD_PRINTSERVICE)) {
            printTicketDialog(activity, printTickets);
        }
    }

    private static void printN5Ticket(final Activity activity, final ArrayList<String> printTickets,
                                      final int ticketIndex) {
        if (ticketIndex >= printTickets.size()) {
            return;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new N5TicketPrinter(activity).print(printTickets.get(ticketIndex), new CallbackHandler() {
                    @Override
                    public void success(String data) {
                        printN5Ticket(activity, printTickets, ticketIndex + 1);
                    }

                    @Override
                    public void failure(String message) {
                        printN5Ticket(activity, printTickets, ticketIndex + 1);
                    }
                });
            }
        }, 200);
    }

    private static void printTicketDialog(final Activity activity, final ArrayList<String> printTickets) {

        // If showPrintDialog is not enabled , print all the tickets without prompt
        // dialog window
        if (!TPApplication.getInstance().showPrintDialog) {
            Toast.makeText(activity, "Printing all tickets ", Toast.LENGTH_LONG).show();
            printN5Ticket(activity, printTickets, 0);

            return;
        }

        // Print first ticket by default and ask for printing more tickets
        if (printTickets.size() == 1) {
            new N5TicketPrinter(activity).print(printTickets.get(0), new CallbackHandler() {
                @Override
                public void success(String data) {
                }

                @Override
                public void failure(String message) {
                }
            });

            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View headerView = LayoutInflater.from(activity).inflate(R.layout.dialog_header, null);
        TextView titleView = (TextView) headerView.findViewById(R.id.header_title);
        titleView.setText("Print Tickets");

        Button actionButton = new Button(activity);
        actionButton.setText("Adv. Paper");
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TPUtility.printAdvancePaper(activity);
            }
        });

        Button closeButton = new Button(activity);
        closeButton.setText("Close");
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TPConstant.LOCAL_BROADCAST_EVENT);
                intent.putExtra("action", "ClosePrintDialog");
                LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);

                printTicketDialog.dismiss();
            }
        });

        LinearLayout actionLayout = (LinearLayout) headerView.findViewById(R.id.header_actions);
        actionLayout.addView(actionButton);
        actionLayout.addView(closeButton);

        builder.setCustomTitle(headerView);

        try {
            ListView listView = new ListView(activity);
            String[] from = new String[]{"search_title"};
            int[] to = new int[]{R.id.search_textview};

            List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < printTickets.size(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("search_title", "Ticket " + (i + 1));
                fillMaps.add(map);
            }

            SimpleAdapter adapter = new SimpleAdapter(activity, fillMaps, R.layout.search_list_item, from, to);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> viewAdapter, View view, int pos, long arg3) {
                    String printData = printTickets.get(pos);

                    progressDialog = ProgressDialog.show(activity, "", "Printing ticket " + (pos + 1));
                    new N5TicketPrinter(activity).print(printData, new CallbackHandler() {
                        @Override
                        public void success(String data) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void failure(String message) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            });

            builder.setView(listView);

            printTicketDialog = builder.create();
            printTicketDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSelectedMethod(Context context) {
        SharedPreferences mPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
        String methodName = mPreferences.getString(APPLICATION_COMM_METHOD_NAME_KEY, null);

        return methodName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticketprinter);

        mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        printerNameTextView = (TextView) findViewById(R.id.printer_settings_printer_textview);
        debugModeChk = (CheckBox) findViewById(R.id.debug_mode_chk);
        showDialogChk = (CheckBox) findViewById(R.id.show_printdialog_chk);

        // read application setting
        TicketPrinterSetting appsetting = getApplicationSetting();
        if (appsetting != null) {
            appSetting = appsetting;
        }

        // Populate Printer Info
        try {
            TCPIPInfo info = getTCPIPConfigSetting();
            if (info != null) {
                TCPIPInfo = info;
            }
        } catch (Exception e) {
            Log.e(LOGTAG, e.getMessage());
        }

        try {
            BluetoothInfo btinfo = getBluetoothConfigSetting();
            if (btinfo != null) {
                BluetoothInfo = btinfo;
            }
        } catch (Exception e) {
            Log.e(LOGTAG, e.getMessage());
        }

        try {
            PrintServiceInfo info = getPrintServiceConfigSetting();
            if (info != null) {
                PrintServiceInfo = info;
            }
        } catch (Exception e) {
            Log.e(LOGTAG, e.getMessage());
        }

        // populate the list of available file to print
        PopulatePrinterFiles();

        // Populate communication method
        PopulateCommunicationMethod();

        // display the connectivity information
        PopulateCurrSelectedPrinterInfo();

        // text view for displaying printing status.
        printStatusTextView = (TextView) findViewById(R.id.printing_status_textview);

        // handler for the print button click
        printButton = (Button) findViewById(R.id.print_button);
        printButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // disable the button
                try {
                    ToggleControls(false);
                    String communicationMethod = appSetting.getCommunicationMethod();

                    if (communicationMethod.equalsIgnoreCase(COMMUNICATION_METHOD_TCPIP)) {
                        TCPIPPrint();
                    } else if (communicationMethod.equalsIgnoreCase(COMMUNICATION_METHOD_BLUETOOTH)) {
                        BluetoothPrint();
                    } else if (communicationMethod.equalsIgnoreCase(COMMUNICATION_METHOD_PRINTSERVICE)) {
                        N5Print();
                    } else if (communicationMethod.equalsIgnoreCase(COMMUNICATION_METHOD_TSC_BLUETOOTH)) {
                        TSCPrint();
                    }

                    // Enable the UI here
                    EnableControls();

                } catch (Exception e) {
                    logger.error(TPUtility.getPrintStackTrace(e));
                }
            }
        });

        if (TPApplication.getInstance().printDebugMode) {
            debugModeChk.setChecked(true);
        }

        if (TPApplication.getInstance().showPrintDialog) {
            showDialogChk.setChecked(true);
        }

        debugModeChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TPApplication.getInstance().printDebugMode = isChecked;

                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_PRINT_DEBUG, TPApplication.getInstance().printDebugMode);
                editor.commit();
            }
        });

        showDialogChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TPApplication.getInstance().showPrintDialog = isChecked;

                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_SHOW_PRINTDIALOG, TPApplication.getInstance().showPrintDialog);
                editor.commit();
            }
        });

        // ---------------------------------------
        // handler for the Save button click
        // ---------------------------------------
        saveSettingButton = (Button) findViewById(R.id.save_setting_button);
        saveSettingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                do {
                    // save application setting.
                    if (!saveApplicationSetting()) {
                        printStatusTextView.setText("Failed to save Printer Settings");
                        break;
                    }

                    // save TCP/IP setting.
                    if (!saveTCPIPConfigSetting()) {
                        printStatusTextView.setText("Failed to save TCP/IP Printer settings");
                        break;
                    }

                    // save Bluetooth Setting
                    if (!SaveBluetoothConfigSetting()) {
                        printStatusTextView.setText("Failed to save Bluetooth Printer Settings");
                        break;
                    }

                    // save PrintService Setting
                    if (!savePrintServiceConfigSetting()) {
                        printStatusTextView.setText("Failed to save Print Service Settings");
                        break;
                    }

                    printStatusTextView.setText("Saved Settings");

                    finish();
                } while (false);

            }
        });

        // ------------------------------------------------
        // Event handler when user selected a file to print.
        // -------------------------------------------------
        fileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                appSetting.setSelectedFileName(fileSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                appSetting.setSelectedFileName((String) fileSpinner.getItemAtPosition(0));
            }
        });

        // --------------------------------------------
        // Event handler when user selected a communication method
        // ---------------------------------------------
        communicationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                appSetting.setCommunicationMethod(communicationSpinner.getSelectedItem().toString());
                PopulateCurrSelectedPrinterInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                appSetting.setSelectedFileName(COMMUNICATION_METHOD_TCPIP);
                PopulateCurrSelectedPrinterInfo();
            }
        });
    }

    private boolean isBluetoothPrinter() {
        if (appSetting == null) {
            return false;
        }

        return appSetting.getCommunicationMethod().equalsIgnoreCase(COMMUNICATION_METHOD_BLUETOOTH) ||
                appSetting.getCommunicationMethod().equalsIgnoreCase(COMMUNICATION_METHOD_TSC_BLUETOOTH);
    }

    private boolean isNetworkPrinter() {
        return appSetting != null && appSetting.getCommunicationMethod().equalsIgnoreCase(COMMUNICATION_METHOD_TCPIP);
    }

    private boolean isPrintServicePrinter() {
        return appSetting != null
                && appSetting.getCommunicationMethod().equalsIgnoreCase(COMMUNICATION_METHOD_PRINTSERVICE);
    }

    // -------------------------------------------------
    // Read the TCPIP configuration information from a file.
    // -------------------------------------------------
    private TicketPrinterSetting getApplicationSetting() {
        TicketPrinterSetting settings = new TicketPrinterSetting("PrintTest1.prn", COMMUNICATION_METHOD_PRINTSERVICE);
        String methodName = mPreferences.getString(APPLICATION_COMM_METHOD_NAME_KEY, null);
        String selectedFile = mPreferences.getString(APPLICATION_SELECTED_FILE_NAME_KEY, null);

        if (selectedFile != null) {
            settings.setSelectedFileName(selectedFile);
        }

        if (methodName != null) {
            settings.setCommunicationMethod(methodName);
        }

        return settings;
    }

    // ----------------------------------------
    // Save the TCP/IP configuration to a file.
    // ------------------------------------------
    private boolean saveApplicationSetting() {
        boolean bRet = true;

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(APPLICATION_COMM_METHOD_NAME_KEY, appSetting.getCommunicationMethod());
        editor.putString(APPLICATION_SELECTED_FILE_NAME_KEY, appSetting.getSelectedFileName());
        editor.commit();

        return bRet;
    }// SaveApplicationSetting()

    // -------------------------------------------------
    // Read the TCPIP configuration information from a file.
    // -------------------------------------------------
    private TCPIPInfo getTCPIPConfigSetting() {
        TCPIPInfo tcpIPInfo = null;
        String ipAddress = mPreferences.getString(PRINTER_IPADDRESS_KEY, null);
        int port = mPreferences.getInt(PRINTER_TCPIPPORT_KEY, 0);
        if (ipAddress != null && port != 0) {
            tcpIPInfo = new TCPIPInfo(ipAddress, port);
        }

        return tcpIPInfo;
    }

    // ----------------------------------------
    // Save the TCP/IP configuration to a file.
    // ------------------------------------------
    private boolean saveTCPIPConfigSetting() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PRINTER_IPADDRESS_KEY, TCPIPInfo.getIPAddress());
        editor.putInt(PRINTER_TCPIPPORT_KEY, TCPIPInfo.getTCPIPPort());
        editor.commit();

        return true;
    }// SaveTCPIPConfigSetting()

    // -------------------------------------------------
    // Read the BlueTooth configuration information from a file.
    // -------------------------------------------------
    private BluetoothInfo getBluetoothConfigSetting() {
        BluetoothInfo bluetoothInfo = null;
        String deviceName = mPreferences.getString(PRINTER_BLUETOOTH_DEVICE_NAME_KEY, null);

        // Check Device Name from Database
        DeviceInfo device = TPApplication.getInstance().getDeviceInfo();
        if (deviceName == null && device != null) {
            deviceName = device.getDefaultPrinterName();
        }

        if (deviceName != null) {
            bluetoothInfo = new BluetoothInfo(deviceName);
        }

        return bluetoothInfo;
    }

    // -------------------------------------------------
    // Save the BlueTooth configuration information to a file.
    // -------------------------------------------------
    private boolean SaveBluetoothConfigSetting() {
        boolean bRet = true;

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PRINTER_BLUETOOTH_DEVICE_NAME_KEY, BluetoothInfo.getDeviceName());
        editor.commit();

        if (isBluetoothPrinter()) {
            DeviceInfo deviceInfo = TPApplication.getInstance().getDeviceInfo();
            if (deviceInfo != null) {
                try {
                    deviceInfo.setDefaultPrinterName(BluetoothInfo.getDeviceName());
                    DeviceInfo.insertDeviceInfo(deviceInfo);
                    /*DatabaseHelper.getInstance().openWritableDatabase();
                    DatabaseHelper.getInstance().insertOrReplace(deviceInfo.getContentValues(), "devices");
                    DatabaseHelper.getInstance().closeWritableDb();*/
                } catch (Exception e) {
                    Log.e(LOGTAG, TPUtility.getPrintStackTrace(e));
                }
            }
        }

        return bRet;
    }

    private boolean savePrintServiceConfigSetting() {
        boolean result = true;
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PRINTER_SERVICE_NAME_KEY, PrintServiceInfo.getDeviceName());
        editor.commit();

        if (isPrintServicePrinter()) {
            DeviceInfo deviceInfo = TPApplication.getInstance().getDeviceInfo();
            if (deviceInfo != null) {
                try {
                    deviceInfo.setDefaultPrinterName(PrintServiceInfo.getDeviceName());
                    DeviceInfo.insertDeviceInfo(deviceInfo);
                    /*DatabaseHelper.getInstance().openWritableDatabase();
                    DatabaseHelper.getInstance().insertOrReplace(deviceInfo.getContentValues(), "devices");
                    DatabaseHelper.getInstance().closeWritableDb();*/
                } catch (Exception e) {
                    Log.e(LOGTAG, TPUtility.getPrintStackTrace(e));
                }
            }
        }

        return result;
    }

    private PrintServiceInfo getPrintServiceConfigSetting() {
        PrintServiceInfo printerInfo = null;
        String deviceName = null;

        // Check Device Name from Database
        DeviceInfo device = TPApplication.getInstance().getDeviceInfo();
        if (device != null) {
            deviceName = device.getDefaultPrinterName();
        }

        if (deviceName == null) {
            deviceName = mPreferences.getString(PRINTER_SERVICE_NAME_KEY, null);
        }

        if (deviceName != null) {
            printerInfo = new PrintServiceInfo(deviceName);
        }

        return printerInfo;
    }

    // ------------------------------------
    // Enable/Disable Control when user click on Print Button
    // ------------------------------------
    private void ToggleControls(boolean enable) {
        printButton.setEnabled(enable);
        fileSpinner.setEnabled(enable);

        communicationSpinner.setEnabled(enable);
    }

    // ----------------------------------------------------
    // display the current selected printer TCPIP or Bluetooth or PrintService
    // information to
    // the text view.
    // ----------------------------------------------------
    private void PopulateCurrSelectedPrinterInfo() {
        String methodName = appSetting.getCommunicationMethod();

        if (methodName.equalsIgnoreCase(COMMUNICATION_METHOD_TCPIP)) {
            printerNameTextView.setText("Selected printer: " + TCPIPInfo.getIPAddress() + ":" + String.valueOf(TCPIPInfo.getTCPIPPort()));
        } else if (methodName.equalsIgnoreCase(COMMUNICATION_METHOD_BLUETOOTH)) {
            printerNameTextView.setText("Selected printer: " + BluetoothInfo.getDeviceName());
        } else if (methodName.equalsIgnoreCase(COMMUNICATION_METHOD_PRINTSERVICE)) {
            printerNameTextView.setText("Selected printer: " + PrintServiceInfo.getDeviceName());
        } else if (methodName.equalsIgnoreCase(COMMUNICATION_METHOD_TSC_BLUETOOTH)) {
            printerNameTextView.setText("Selected printer: " + BluetoothInfo.getDeviceName());
        }
    }

    private void copyPrintTemplate(String filename) {
        AssetManager assetManager = this.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open("print_test" + File.separator + filename);
            String newFileName = TPUtility.getPrintTemplateFolder() + filename;
            if (new File(newFileName).exists()) {
                return;
            }

            out = new FileOutputStream(newFileName);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    // --------------------------------------------------------------------------
    // populate list of available files to be selected for printing for the
    // spinner
    // ---------------------------------------------------------------------------
    private void PopulatePrinterFiles() {
        fileSpinner = (Spinner) findViewById(R.id.file_to_print_spinner);
        AssetManager assetManager = getAssets();
        try {
            // get the list of available files to select for printing from sub
            // directory demo under Assests folder.
            String[] files = assetManager.list("print_test");

            File printTemplateFolder = new File(TPUtility.getPrintTemplateFolder());
            if (printTemplateFolder.exists()) {
                for (String filename : files) {
                    copyPrintTemplate(filename);
                }

                files = printTemplateFolder.list();
            }

            // create an adapter to bind data from the list of the file to the
            // spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(appContext, android.R.layout.simple_spinner_item,
                    files);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fileSpinner.setAdapter(adapter);

            // set the selected item from the fileSpinner with the value from
            // application setting.
            for (int index = 0; index < files.length; index++) {
                if (files[index].compareTo(appSetting.getSelectedFileName()) == 0) {
                    fileSpinner.setSelection(index);
                    break;
                }
            }
        } catch (IOException e) {
            Log.e(LOGTAG, TPUtility.getPrintStackTrace(e));
        }
    }

    // -------------------------------------------------
    // Populate the option for the communication methods to the spinner
    // -------------------------------------------------
    private void PopulateCommunicationMethod() {
        communicationSpinner = (Spinner) findViewById(R.id.communication_method_spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(appContext, android.R.layout.simple_spinner_item,
                new String[]{"TSC Bluetooth", "Zebra Bluetooth", "Bluetooth", "TCP/IP", "Internal"});

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        communicationSpinner.setAdapter(adapter);

        // set to zero index for default.
        communicationSpinner.setSelection(0);
        String selectedCommunicationMethod;

        for (int nIndex = 0; nIndex < communicationSpinner.getCount(); nIndex++) {
            selectedCommunicationMethod = communicationSpinner.getItemAtPosition(nIndex).toString();
            if (selectedCommunicationMethod.equalsIgnoreCase(appSetting.getCommunicationMethod())) {
                communicationSpinner.setSelection(nIndex);

                break;
            }
        }
    }

    private void ShowPrintingStatusMessage(String msgStr) {
        printStatusTextView.setText(msgStr);
    }

    // --------------------------------------------------------------------------
    // Display printing status message to the Text view in the
    // DOPrinterDemoActivity activity from another thread
    // ----------------------------------------------------------------------------
    private void DisplayPrintingStatusMessage(String msgStr) {
        printStatusStr = msgStr;
        // display the result to the UI here
        handler.post(new Runnable() {
            public void run() {
                printStatusTextView.setText(printStatusStr);
            }// run()
        });
    }

    // --------------------------------
    // Enable controls after printing has completed
    // ---------------------------------
    private void EnableControls() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ToggleControls(true);
            }
        });
    }

    // ------------------------
    // Print a file via TCPIP
    // ------------------------
    private void TCPIPPrint() {
        String msgStr = "";
        InputStream inStream = null;
        DataOutputStream dOutStream = null;

        try {
            // file name to be printed
            Spinner fileSpinner = (Spinner) findViewById(R.id.file_to_print_spinner);
            String fileName = fileSpinner.getSelectedItem().toString();

            // target printer
            String deviceAddr = TCPIPInfo.getIPAddress();
            int TCPIPPort = TCPIPInfo.getTCPIPPort();

            File file = new File(TPUtility.getPrintTemplateFolder() + fileName);
            inStream = new FileInputStream(file);

            // read data from file and save it into Byte Array Output Stream
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            msgStr = "Read data from file " + fileName;
            DisplayPrintingStatusMessage(msgStr);
            int nextByte = inStream.read();
            while (nextByte != -1) {
                bos.write(nextByte);
                nextByte = inStream.read();
            }

            byte[] finalArray = bos.toByteArray();

            // create a socket to communicate with selected print ip address:
            // port number
            msgStr = "Create TCP/IP socket";
            DisplayPrintingStatusMessage(msgStr);
            Socket socket = null;
            socket = new Socket(deviceAddr, TCPIPPort);

            // associate the output stream with the open socket
            msgStr = "Send data to printer";
            DisplayPrintingStatusMessage(msgStr);
            dOutStream = new DataOutputStream(socket.getOutputStream());

            // send the label sample to printer, in turn, printer will print
            // this label.
            dOutStream.write(finalArray, 0, finalArray.length);
            dOutStream.flush();

            msgStr = "Done Printing.";

        } catch (SocketException se) {
            msgStr = se.getMessage();
        } catch (UnknownHostException uhe) {
            msgStr = uhe.getMessage();

        } catch (IOException e) {
            msgStr = e.getMessage();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                    inStream = null;
                } catch (IOException e) { /* ignore */
                }
            }
            if (dOutStream != null) {
                try {
                    dOutStream.close();
                    dOutStream = null;
                } catch (IOException e) { /* ignore */
                }
            }
        }

        // display the last result to the UI here
        DisplayPrintingStatusMessage(msgStr);
    }

    // TCP/IP Print
    private void BluetoothPrint() {
        mConnectedBTDeviceName = "";
        BluetoothAdapter btAdapter = null;
        String msgStr = "";
        InputStream inStream = null;
        BluetoothChatService BTService = null;
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        try {
            // file name to be printed
            String fileName = fileSpinner.getSelectedItem().toString();

            // Get BlueTooth Adapter
            btAdapter = BluetoothAdapter.getDefaultAdapter();
            if (btAdapter == null) {
                msgStr = "Bluetooth not supported";
                DisplayPrintingStatusMessage(msgStr);
                return;
            }

            // get the list of paired printer.
            Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();

            // if there is any paired device, get out
            if (pairedDevices.size() == 0) {
                msgStr = "Bluetooth not paired";
                DisplayPrintingStatusMessage(msgStr);
                return;
            }

            // verify the current selected printer is in the list of the paired printer.
            BluetoothDevice selectedBTDevice = null;
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().compareTo(BluetoothInfo.getDeviceName()) == 0) {
                    selectedBTDevice = device;
                    break;
                }
            }

            // the current selected printer is not in the paired printer list
            if (selectedBTDevice == null) {
                msgStr = "Printer not paired in list";
                DisplayPrintingStatusMessage(msgStr);
                return;
            }

            // at this point we got the a target Bluetooth Device.
            // create a BT Service object
            BTService = new BluetoothChatService(this, mHandler);

            // Start the BT Service
            BTService.start();

            // connecting to device - generate Bluetooth Socket, connecting to the device,
            // and get the output stream for the socket.
            msgStr = "connecting to " + selectedBTDevice.getName() + "(" + selectedBTDevice.getAddress() + ")...";
            DisplayPrintingStatusMessage(msgStr);
            BTService.connect(selectedBTDevice);

            // wait for the connection has established
            // Check that we're actually connected before trying anything
            int nWaitTime = 8;
            while (BTService.getState() != BluetoothChatService.STATE_CONNECTED) {
                Thread.sleep(1000);
                nWaitTime--;
                if (nWaitTime == 0) {
                    DisplayPrintingStatusMessage("Unable to connect!");
                    throw (new Throwable("Unable To connect to " + selectedBTDevice.getName() + "!"));
                }
            }

            // ------------------------------------------
            // read the data to be sent to printer
            File file = new File(TPUtility.getPrintTemplateFolder() + fileName);
            inStream = new FileInputStream(file);

            // read data from file and save it into Byte Array Output Stream
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            msgStr = "Read data from file " + fileName;
            DisplayPrintingStatusMessage(msgStr);
            int nextByte = inStream.read();
            while (nextByte != -1) {
                bos.write(nextByte);
                nextByte = inStream.read();
            }
            byte[] finalArray = bos.toByteArray();

            // Write data to the connected BT device
            msgStr = "Send data to printer";
            DisplayPrintingStatusMessage(msgStr);
            BTService.write(finalArray);
            Thread.sleep(2000);

            msgStr = "Done Printing";

        } catch (SocketException e) {
            msgStr = e.getMessage();
            Log.e(LOGTAG, e.getMessage(), e);
        } catch (UnknownHostException e) {
            msgStr = e.getMessage();
            Log.e(LOGTAG, e.getMessage(), e);
        } catch (IOException e) {
            msgStr = e.getMessage();
            Log.e(LOGTAG, e.getMessage(), e);
        } catch (Throwable e) {
            msgStr = e.getMessage();
            Log.e(LOGTAG, e.getMessage(), e);
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                    inStream = null;
                } catch (IOException e) { /* ignore */
                }
            }

            if (BTService != null)
                BTService.stop();

        }

        // display the last result to the UI here
        DisplayPrintingStatusMessage(msgStr);
        return;
    }

    private void N5Print() {
        String msgStr = "";
        InputStream inStream = null;


        try {
            // file name to be printed
            Spinner fileSpinner = (Spinner) findViewById(R.id.file_to_print_spinner);
            String fileName = fileSpinner.getSelectedItem().toString();

            File file = new File(TPUtility.getPrintTemplateFolder() + fileName);
            inStream = new FileInputStream(file);

            msgStr = "Read data from file " + fileName;

            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            StringBuffer contents = new StringBuffer();
            String line;

            while ((line = reader.readLine()) != null) {
                contents.append(line);
                contents.append('\n');
            }

            reader.close();

            // associate the output stream with the open socket
            msgStr = "Send data to printer";
            DisplayPrintingStatusMessage(msgStr);

            String data = contents.toString();

            // Print Data
            N5TicketPrinter printer = new N5TicketPrinter(TicketPrinter.this);
            printer.print(data);

            msgStr = "Done Printing.";

        } catch (IOException e) {
            msgStr = e.getMessage();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                    inStream = null;
                } catch (IOException e) {
                }
            }
        }

        // display the last result to the UI here
        DisplayPrintingStatusMessage(msgStr);

    }// N5Print

    private void TSCPrint() {
        String msgStr = "";
        InputStream inStream = null;

        try {
            // file name to be printed
            Spinner fileSpinner = (Spinner) findViewById(R.id.file_to_print_spinner);
            String fileName = fileSpinner.getSelectedItem().toString();

            File file = new File(TPUtility.getPrintTemplateFolder() + fileName);
            inStream = new FileInputStream(file);

            msgStr = "Read data from file " + fileName;

            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            StringBuffer contents = new StringBuffer();
            String line;

            while ((line = reader.readLine()) != null) {
                contents.append(line);
                contents.append('\n');
            }

            reader.close();

            // associate the output stream with the open socket
            msgStr = "Send data to printer";
            DisplayPrintingStatusMessage(msgStr);

            String data = contents.toString();
            if (data == null) {
                data = TicketPROConstant.ticketData;
            }
            logger.error("Printable TSC Data \n" + data);
            // Print Data
            new TSCBluetoothPrinter(TicketPrinter.this, data).print(true);

            msgStr = "Done Printing.";

        } catch (IOException e) {
            logger.error(TPUtility.getPrintStackTrace(e));
            msgStr = e.getMessage();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                    inStream = null;
                } catch (IOException e) {
                    logger.error(TPUtility.getPrintStackTrace(e));
                }
            }
        }

        // display the last result to the UI here
        DisplayPrintingStatusMessage(msgStr);

    }// N5Print

    public void TCPIPPrinter(View view) {
        Intent intent = new Intent();
        intent.setClass(this, TCPIPConfig.class);
        intent.putExtra(PRINTER_IPADDRESS_KEY, TCPIPInfo.getIPAddress());
        intent.putExtra(PRINTER_TCPIPPORT_KEY, TCPIPInfo.getTCPIPPort());

        startActivityForResult(intent, CONFIG_TCPIP_REQUEST);
    }

    public void BluetootPrinter(View view) {
        Intent intent = new Intent();
        intent.setClass(this, BluetoothConfig.class);
        String deviceName = null;
        DeviceInfo device = TPApplication.getInstance().getDeviceInfo();

        if (device != null) {
            deviceName = device.getDefaultPrinterName();
        }

        if (deviceName == null) {
            deviceName = BluetoothInfo.getDeviceName();
        }

        intent.putExtra(PRINTER_BLUETOOTH_DEVICE_NAME_KEY, deviceName);
        startActivityForResult(intent, CONFIG_BLUETOOTH_REQUEST);
    }

    // --------------------------------------------------------------------------------------------------
    // handle the data when the TCP/IP Config Activity has finished its works,
    // and return to this activity
    // -----------------------------------------------------------------------------------------------------
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CONFIG_TCPIP_REQUEST: {
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        TCPIPInfo.setIPAddress(extras.getString(PRINTER_IPADDRESS_KEY));
                        TCPIPInfo.setTCPIPPort(extras.getInt(PRINTER_TCPIPPORT_KEY));

                        saveTCPIPConfigSetting();
                        PopulateCurrSelectedPrinterInfo();
                    }
                }

                break;
            }

            case CONFIG_BLUETOOTH_REQUEST: {
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        BluetoothInfo.setDeviceName(extras.getString(PRINTER_BLUETOOTH_DEVICE_NAME_KEY));
                        printerNameTextView.setText("Selected Printer: " + BluetoothInfo.getDeviceName());

                        SaveBluetoothConfigSetting();
                        PopulateCurrSelectedPrinterInfo();
                    }
                }

                break;
            }
        }

    }// onActivityResult()

    public void backAction(View view) {
        finish();
    }
}
