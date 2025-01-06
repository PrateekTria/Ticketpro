package com.ticketpro.print;

import static com.twotechnologies.n5library.N5Information.isPlatformAvailable;
import static com.twotechnologies.n5library.N5Information.isServiceAvailable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.ticketpro.model.Feature;
import com.ticketpro.model.PrintTemplate;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.util.CallbackHandler;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPUtility;
import com.twotechnologies.n5library.N5Library;
import com.twotechnologies.n5library.N5ReadyListener;
import com.twotechnologies.n5library.printer.Code128CodeSets;
import com.twotechnologies.n5library.printer.Fonts;
import com.twotechnologies.n5library.printer.ImageAlignment;
import com.twotechnologies.n5library.printer.ImageScale;
import com.twotechnologies.n5library.printer.PrtActionRequest;
import com.twotechnologies.n5library.printer.PrtBarcodeRequest;
import com.twotechnologies.n5library.printer.PrtContrastLevel;
import com.twotechnologies.n5library.printer.PrtEOJListener;
import com.twotechnologies.n5library.printer.PrtFormatting;
import com.twotechnologies.n5library.printer.PrtGraphics;
import com.twotechnologies.n5library.printer.PrtSeekListener;
import com.twotechnologies.n5library.printer.PrtSeekRequest;
import com.twotechnologies.n5library.printer.PrtStatus;
import com.twotechnologies.n5library.printer.PrtStatusChangeListener;
import com.twotechnologies.n5library.printer.PrtTextStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.widget.Toast;

public class N5TicketPrinter {
    private Context context;
    private Activity activity;
    private N5ReadyListener srvReady;
    private N5ReadyListener srvNotReady;
    private PrtEOJListener prtEOJListener;
    private PrtSeekListener prtSeekListener;
    private PrtStatusChangeListener prtStatusChangeListener;
    private Logger logger = Logger.getLogger("N5TicketPrinter");
    private CallbackHandler callbackHandler;
    private boolean advancePaperOnly = false;
    //private TimerTask updateTimerTask;
    //private Timer updateTimer;

    enum SeekRequest {
        NOT_STARTED, STARTED, COMPLETED, SEEK_FAILED
    }

    private Handler seekHandler;
    private Runnable seekRunnable;
    private int executionCount;
    private final int MAX_SEEK_REQUESTS = 20;
    private ArrayList<String> printTickets = null;
    private boolean printMarker = false;
    private boolean isInitialized = false;
    private int currentTicketIndex = 0;

    private SeekRequest seekRequest = SeekRequest.NOT_STARTED;

    static Map<String, String> fontMaps = new HashMap<String, String>();

    static {
        fontMaps.put("SS55", "SAN_SERIF_5_5_CPI");
        fontMaps.put("SS102", "SAN_SERIF_10_2_CPI");
        fontMaps.put("SS107", "SAN_SERIF_10_7_CPI");
        fontMaps.put("C127", "COURIER_12_7_CPI");
        fontMaps.put("C135", "COURIER_13_5_CPI");
        fontMaps.put("C145", "COURIER_14_5_CPI");
        fontMaps.put("C156", "COURIER_15_6_CPI");
        fontMaps.put("C169", "COURIER_16_9_CPI");
        fontMaps.put("C185", "COURIER_18_5_CPI");
        fontMaps.put("C203", "COURIER_20_3_CPI");
        fontMaps.put("C226", "COURIER_22_6_CPI");
        fontMaps.put("C254", "COURIER_25_4_CPI");
        fontMaps.put("SS169", "SAN_SERIF_16_9_CPI");
        fontMaps.put("SS185", "SAN_SERIF_18_5_CPI");
        fontMaps.put("SS203", "SAN_SERIF_20_3_CPI");
        fontMaps.put("SS42", "SAN_SERIF_4_2_CPI");
    }

    static Map<String, String> contrastLevels = new HashMap<String, String>();

    static {
        contrastLevels.put("L0", "CONTRAST_LEVEL_0");
        contrastLevels.put("L1", "CONTRAST_LEVEL_1");
        contrastLevels.put("L2", "CONTRAST_LEVEL_2");
        contrastLevels.put("L3", "CONTRAST_LEVEL_3");
        contrastLevels.put("L4", "CONTRAST_LEVEL_4");
        contrastLevels.put("L5", "CONTRAST_LEVEL_5");
        contrastLevels.put("L6", "CONTRAST_LEVEL_6");
        contrastLevels.put("L7", "CONTRAST_LEVEL_7");
        contrastLevels.put("L8", "CONTRAST_LEVEL_8");
        contrastLevels.put("L9", "CONTRAST_LEVEL_9");
    }

    public N5TicketPrinter(Activity activity, boolean advancePaperOnly) {
        this(activity);
        this.advancePaperOnly = advancePaperOnly;
    }

    public N5TicketPrinter(Activity activity) {
        this.activity = activity;
        this.context = TPApplication.getInstance();

        srvReady = new N5ReadyHandler(context, N5ReadyListener.ACTION_N5_READY);
        srvNotReady = new N5ReadyHandler(context, N5ReadyListener.ACTION_N5_NOT_READY);

        prtEOJListener = new PrtEOJListener(context) {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!printMarker) {
                    return;
                }

                printMarker = false;
                onPrintCompletion();
            }
        };

        prtSeekListener = new PrtSeekListener(context) {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean result = getResult(intent);
                if (result) {
                    onSeekSuccess();
                } else {
                    seekRequest = SeekRequest.SEEK_FAILED;
                }
            }
        };

        prtStatusChangeListener = new PrtStatusChangeListener(context) {
            @Override
            public void onReceive(Context context, Intent intent) {
                // logger.debug("Print status changed "+ getResultData());
                // When this listener is invoked, the printer
                // status has changed ...
                // update the status flags
                updateStatusFlags();

                // cancel print job if there is a printer problem
                // NOTE: do not include Paper Out here since Cancel
                // will clear the paper out indicator
                if (!PrtStatus.isCoverOpen()
                        || PrtStatus.isPlatenOpen()
                        || PrtStatus.isOverTemperature()
                        || PrtStatus.isHardwareFault()) {

                    Toast.makeText(activity, "Cancelling print job", Toast.LENGTH_LONG).show();

                    // cancel print operations
                    PrtActionRequest.cancelPrinting();

                    // Stop print services
                    stopPrintServices();
                }
            }
        };


        /*updateTimer = new Timer();
        updateTimerTask = new TimerTask() {
            @Override
            public void run() {
                // Status Flags
                updateStatusFlags();

                //Refresh status
                PrtActionRequest.refreshStatus();
            }
        };*/

        initPrintServices();

        //Update status periodically
        //updateTimer.schedule(updateTimerTask, 1000, 1000);
    }

    public void initPrintServices() {
        try {
            // initialize N5 API
            N5Library.initialize(context);

            if (!isServiceAvailable()) {
                logger.info("N5 Print Service is not available");
                Toast.makeText(activity, "N5 Print Service is not available", Toast.LENGTH_SHORT).show();
                return;
            }

            // Reset current ticket index and print tickets
            currentTicketIndex = 0;
            printTickets = null;

            // start listening for connection events
            srvReady.startListening();
            srvNotReady.startListening();

            // start printer listeners
            prtEOJListener.startListening();
            prtStatusChangeListener.startListening();
            prtSeekListener.startListening();

            isInitialized = true;

            // Reset parameters
            PrtActionRequest.resetPrinter();

            // Refresh printer status
            PrtActionRequest.refreshStatus();

        } catch (Exception e) {
            logger.info(TPUtility.getPrintStackTrace(e));
        }
    }

    public void stopPrintServices() {
        try {
            /*if (updateTimer != null) {
                updateTimer.cancel();
            }*/

            if (!isServiceAvailable()) {
                logger.info("N5 Print Service is not available");
                return;
            }

            if (seekHandler != null && seekRunnable != null) {
                seekHandler.removeCallbacks(seekRunnable);
            }

            // stop printer listeners
            if (isInitialized) {
                prtEOJListener.stopListening();
                prtStatusChangeListener.stopListening();
                prtSeekListener.stopListening();

                // stop listening for connection events
                srvReady.stopListening();
                srvNotReady.stopListening();
            }

            isInitialized = false;

            // unbind library
            N5Library.close();

        } catch (Exception e) {
            logger.info(TPUtility.getPrintStackTrace(e));
        } finally {
            if (callbackHandler != null) {
                callbackHandler.success("Done");
            }
        }
    }

    public void advancePaper() {
        advancePaper(false);
    }

    public void advancePaper(final boolean multiprint) {
        Toast.makeText(activity, "Processing AdvancePaper...", Toast.LENGTH_SHORT).show();

        try {
            if (Feature.isFeatureAllowed(Feature.N5_ADVANCE_PRINT_TEMPLATE)) {
                String templateName = Feature.getFeatureValue(Feature.N5_ADVANCE_PRINT_TEMPLATE);
                if (StringUtil.isEmpty(templateName)) {
                    templateName = "N5AdvancePaper";
                }

                PrintTemplate template = PrintTemplate.getPrintTemplateByName(templateName);
                if (template != null && !StringUtil.isEmpty(template.getTemplateData())) {
                    processAdvancePrintTemplate(template.getTemplateData());

                    if (advancePaperOnly) {
                        stopPrintServices();
                    }

                    return;
                }
            }
        } catch (Exception e) {
            logger.error(TPUtility.getPrintStackTrace(e));
        }

        seekHandler = new Handler();
        seekRunnable = new Runnable() {
            public void run() {
                if (seekRequest == SeekRequest.NOT_STARTED || seekRequest == SeekRequest.SEEK_FAILED) {
                    seekRequest = SeekRequest.STARTED;
                    PrtSeekRequest.forwardSeek(200);
                }

                executionCount++;

                if (executionCount >= MAX_SEEK_REQUESTS || seekRequest == SeekRequest.COMPLETED) {
                    seekHandler.removeCallbacks(seekRunnable);
                } else {
                    seekHandler.postDelayed(this, 300);
                }
            }
        };

        executionCount = 0;
        seekRequest = SeekRequest.NOT_STARTED;
        seekHandler.post(seekRunnable);
    }


    private boolean checkStatusAndRetry(final String printData, CallbackHandler errorCallback) {
        return checkStatusAndRetry(printData, errorCallback,
                "Printer door is closed. Open door and press Retry to print ticket. Press OK to return"
        );
    }

    private boolean checkStatusAndRetry(final String printData, CallbackHandler errorCallback, String closedMsg) {
        if (!isServiceAvailable()) {
            showPrinterErrorMsg("Print service is not available.");
            return false;
        }

        if (PrtStatus.isPaperOut()) {
            showPrinterErrorMsg("Printer is out of paper.", true, errorCallback);
            return false;
        }

        if (!PrtStatus.isCoverOpen()
                || PrtStatus.isHardwareFault()
                || !PrtStatus.isPrinterReady()) {

            showPrinterErrorMsg("Printer not ready. Check door or power level (either change battery or connect to charger)."
                    + "\nPress Retry to print ticket"
                    + "\nPress OK to return", true, errorCallback);

            return false;
        }

        return true;
    }

    private void processAdvancePrintTemplate(final String printData) throws IOException {

        //If printer is not ready return
        boolean printerCheck = checkStatusAndRetry(printData, new CallbackHandler() {
            @Override
            public void success(String data) {
                try {
                    processAdvancePrintTemplate(printData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(String message) {
                //TODO - failure action
            }
        }, "Printer door is closed");

        if (!printerCheck) {
            return;
        }

        Scanner scanner = new Scanner(printData);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            // PrintNew Lines
            if (line.startsWith("@NEWLINES")) {
                String linesCount = StringUtil.substringBetween(line, "(", ")");
                if (linesCount != null && !linesCount.isEmpty()) {
                    try {
                        PrtTextStream.newlines(Integer.parseInt(linesCount));
                    } catch (Exception e) {
                        logger.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            }

            // Forward Feed
            else if (line.startsWith("@FORWARD_FEED")) {
                String maxRows = StringUtil.substringBetween(line, "(", ")");
                PrtActionRequest.forwardFeed(Integer.parseInt(maxRows));
                PrtTextStream.flush();
            }

            // Reverse Feed
            else if (line.startsWith("@REVERSE_FEED")) {
                String maxRows = StringUtil.substringBetween(line, "(", ")");
                PrtActionRequest.reverseFeed(Integer.parseInt(maxRows));
            }

            // Reverse seek
            else if (line.startsWith("@REVERSE_SEEK")) {
                String maxRows = StringUtil.substringBetween(line, "(", ")");
                PrtSeekRequest.reverseSeek(Integer.parseInt(maxRows));
            } else {
                PrtTextStream.newline();
            }
        }

        scanner.close();

        // flush data to printer
        PrtTextStream.flush();
    }

    // On Print Completion, advance the paper and process next ticket if any
    private void onPrintCompletion() {
        // Done Printing
        Toast.makeText(activity, "Printing Done", Toast.LENGTH_SHORT).show();

        // Advance Paper
        if (this.printTickets != null) {
            this.advancePaper(this.printTickets.size() > 1);
        } else {
            this.advancePaper();
        }
    }

    // After Seek Process Form Feed and Start new ticket
    private void onSeekSuccess() {
        // Avoid multiple form feed if seek request is already completed
        if (seekRequest == SeekRequest.COMPLETED) {
            return;
        }

        // Mark seek complete and request form feed
        seekRequest = SeekRequest.COMPLETED;
        try {
            PrtTextStream.formfeed();
            PrtTextStream.flush();
        } catch (IOException e) {
            logger.error(TPUtility.getPrintStackTrace(e));
        }

        if (advancePaperOnly) {
            stopPrintServices();
            return;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (printTickets != null) {
                    currentTicketIndex++;
                    if (currentTicketIndex < printTickets.size()) {
                        print(printTickets.get(currentTicketIndex));
                    } else {
                        stopPrintServices();
                    }
                } else {
                    stopPrintServices();
                }
            }
        }, 200);
    }

    // Print multiple tickets
    public void print(ArrayList<String> printTickets) {
        if (printTickets == null || printTickets.size() == 0) {
            return;
        }

        this.printTickets = printTickets;
        this.currentTicketIndex = 0;

        print(printTickets.get(currentTicketIndex), true);
    }

    // Print single ticket
    public void print(String printData) {
        print(printData, false);
    }

    // Print single ticket
    public void print(String printData, CallbackHandler callback) {
        callbackHandler = callback;
        print(printData, false);
    }

    public void print(final String printData, final boolean multiprint) {
        Fonts currentFont = Fonts.COURIER_13_5_CPI;
        try {
            //Refresh printer status
            PrtActionRequest.refreshStatus();

            boolean printerCheck = checkStatusAndRetry(printData, new CallbackHandler() {
                @Override
                public void success(String data) {
                    print(printData, multiprint);
                }

                @Override
                public void failure(String message) {
                    //TODO - failure action
                }
            });

            if (!printerCheck) {
                return;
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            // Mark seek not started
            this.seekRequest = SeekRequest.NOT_STARTED;

            // Mark print marker false to indicate start on print job
            this.printMarker = false;

            Scanner scanner = new Scanner(printData);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                PrtFormatting.setFont(currentFont);

                // Font Settings
                if (line.startsWith("@FONT")) {
                    String fontName = StringUtil.substringBetween(line, "(", ")");
                    Fonts newFont = getFont(fontName);
                    if (newFont != null) {
                        currentFont = newFont;
                    }
                    continue;
                }

                // Double Height Settings
                if (line.startsWith("@DOUBLE_HEIGHT")) {
                    String flagValue = StringUtil.substringBetween(line, "(", ")");
                    if (flagValue == null || flagValue.isEmpty()) {
                        PrtFormatting.setDoubleHeight(true);
                    } else {
                        PrtFormatting.setDoubleHeight(flagValue.trim().equalsIgnoreCase("TRUE"));
                    }

                    continue;
                }

                // Double Size Settings
                if (line.startsWith("@DOUBLE_SIZE")) {
                    String flagValue = StringUtil.substringBetween(line, "(", ")");
                    if (flagValue == null || flagValue.isEmpty()) {
                        PrtFormatting.setDoubleSize(true);
                    } else {
                        PrtFormatting.setDoubleSize(flagValue.trim().equalsIgnoreCase("TRUE"));
                    }

                    continue;
                }

                // Double Size Settings
                if (line.startsWith("@EMPHASIZE")) {
                    String flagValue = StringUtil.substringBetween(line, "(", ")");
                    if (flagValue == null || flagValue.isEmpty()) {
                        PrtFormatting.setEmphasize(true);
                    } else {
                        PrtFormatting.setEmphasize(flagValue.trim().equalsIgnoreCase("TRUE"));
                    }

                    continue;
                }

                // Line Spacing Settings
                if (line.startsWith("@LINE_SPACING")) {
                    String dotRows = StringUtil.substringBetween(line, "(", ")");
                    if (dotRows != null && !dotRows.isEmpty()) {
                        try {
                            PrtFormatting.setLineSpacing(Integer.parseInt(dotRows));
                        } catch (Exception e) {
                            logger.error(TPUtility.getPrintStackTrace(e));
                        }
                        continue;
                    }
                }

                // PrintNew Lines
                if (line.startsWith("@NEWLINES")) {
                    String linesCount = StringUtil.substringBetween(line, "(", ")");
                    if (linesCount != null && !linesCount.isEmpty()) {
                        try {
                            PrtTextStream.newlines(Integer.parseInt(linesCount));
                        } catch (Exception e) {
                            logger.error(TPUtility.getPrintStackTrace(e));
                        }
                        continue;
                    }
                }

                // PrintNew Lines
                if (line.startsWith("@CONTRAST_LEVEL")) {
                    String contrastLevel = StringUtil.substringBetween(line, "(", ")");
                    PrtActionRequest.setPrintContrast(getContrastLevel(contrastLevel));
                    continue;
                }

                // Forward Feed
                if (line.startsWith("@FORWARD_FEED")) {
                    String maxRows = StringUtil.substringBetween(line, "(", ")");
                    PrtActionRequest.forwardFeed(Integer.parseInt(maxRows));
                    PrtTextStream.flush();
                    continue;
                }

                // Reverse Feed
                if (line.startsWith("@REVERSE_FEED")) {
                    String maxRows = StringUtil.substringBetween(line, "(", ")");
                    PrtActionRequest.reverseFeed(Integer.parseInt(maxRows));
                    continue;
                }

                // Reverse seek
                if (line.startsWith("@REVERSE_SEEK")) {
                    String maxRows = StringUtil.substringBetween(line, "(", ")");
                    PrtSeekRequest.reverseSeek(Integer.parseInt(maxRows));
                    continue;
                }

                // Form feeds
                if (line.startsWith("@FORM_FEED")) {
                    PrtTextStream.formfeed();
                    PrtTextStream.flush();
                    continue;
                }

                // RESET
                if (line.startsWith("@RESET")) {
                    PrtActionRequest.resetPrinter();
                    continue;
                }

                if (line.startsWith("@IMAGE")) {
                    String[] imageTokens = line.split(":");
                    if (imageTokens.length > 1) {
                        String imagePath = imageTokens[1];
                        String imageOptions = StringUtil.substringBetween(imageTokens[0], "(", ")");

                        if (!imagePath.trim().isEmpty()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imagePath.trim(), options);
                            bitmap = TPUtility.removeMargins(bitmap, Color.WHITE);
                            ImageScale imageScale = ImageScale.SCALE_FULL_IMAGE;
                            ImageAlignment imageAlignment = ImageAlignment.IMAGE_CENTER;

                            if (imageOptions != null && !imageOptions.isEmpty()) {
                                String[] optionTokens = imageOptions.split(",");
                                String alignmentOption = optionTokens[0];

                                if (alignmentOption.equalsIgnoreCase("L")) {
                                    imageAlignment = ImageAlignment.IMAGE_LEFT;
                                } else if (alignmentOption.equalsIgnoreCase("C")) {
                                    imageAlignment = ImageAlignment.IMAGE_CENTER;
                                } else if (alignmentOption.equalsIgnoreCase("R")) {
                                    imageAlignment = ImageAlignment.IMAGE_RIGHT;
                                }

                                if (optionTokens.length == 3) {
                                    int width = Integer.parseInt(optionTokens[1]);
                                    int height = Integer.parseInt(optionTokens[2]);
                                    bitmap = TPUtility.scalePreserveRatio(bitmap, width, height);
                                    imageScale = ImageScale.SCALE_ONE_TO_ONE;
                                }
                            }
                            PrtGraphics.printImage(bitmap, imageScale, imageAlignment);
                            PrtTextStream.newline();
                        }
                    }
                } else if (line.startsWith("@LINE")) {
                    int lineWidth = 2;
                    String lineOptions = StringUtil.substringBetween(line, "(", ")");
                    if (lineOptions != null && !lineOptions.isEmpty()) {
                        try {
                            lineWidth = Integer.parseInt(lineOptions);
                        } catch (Exception e) {
                            logger.error(TPUtility.getPrintStackTrace(e));
                        }
                    }

                    addGraphicLine(lineWidth);
                } else if (line.startsWith("@TITLE")) {
                    String[] titleTokens = line.split(":");
                    if (titleTokens.length > 1) {
                        PrtFormatting.setFont(Fonts.SAN_SERIF_10_2_CPI);
                        PrtTextStream.write(titleTokens[1].trim());
                    }
                } else if (line.startsWith("@BARCODE")) {
                    int height = 100;
                    String barcodeType = "code39";
                    String[] barcodeTokens = line.split(":");

                    if (barcodeTokens.length > 1) {
                        String barcodeText = barcodeTokens[1].trim();
                        String barcodeOptions = StringUtil.substringBetween(line, "(", ")");
                        if (barcodeOptions != null && !barcodeOptions.isEmpty()) {
                            if (barcodeOptions.contains(",")) {
                                String[] tokens = barcodeOptions.split(",");
                                if (tokens.length >= 2) {
                                    barcodeType = tokens[0];
                                    height = Integer.parseInt(tokens[1]);
                                }
                            } else {
                                barcodeType = barcodeOptions;
                            }
                        }

                        if (barcodeType.equalsIgnoreCase("code39")) {
                            PrtBarcodeRequest.code39(barcodeText, true, height);
                        } else if (barcodeType.equalsIgnoreCase("code128")) {
                            PrtBarcodeRequest.code128(barcodeText, true, height, Code128CodeSets.CODE128_CODESET_B);

                        } else if (barcodeType.equalsIgnoreCase("code2of5")) {
                            PrtBarcodeRequest.code2of5(barcodeText, true, height);

                        } else if (barcodeType.equalsIgnoreCase("codeUPC_A")) {
                            PrtBarcodeRequest.codeUPC_A(barcodeText, true, height);

                        } else if (barcodeType.equalsIgnoreCase("codeEAN13")) {
                            PrtBarcodeRequest.codeEAN13(barcodeText, true, height);

                        } else if (barcodeType.equalsIgnoreCase("codeEAN8")) {
                            PrtBarcodeRequest.codeEAN8(barcodeText, true, height);

                        } else if (barcodeType.equalsIgnoreCase("codeUPC_E")) {
                            PrtBarcodeRequest.codeUPC_E(barcodeText, true, height);
                        }
                    }
                } else {
                    PrtTextStream.write(line);
                }
                PrtTextStream.newline();
            }
            scanner.close();
            // flush data to printer
            PrtTextStream.flush();
            printMarker = true;
            // mark EOJ
            PrtActionRequest.markEOJ();
        } catch (IOException e) {
            logger.error("Print job failed " + TPUtility.getPrintStackTrace(e));
            stopPrintServices();
        }
    }


    //************************************************************************

    /**
     * Common class to handle N5 Platform Ready
     */
    //************************************************************************
    private class N5ReadyHandler extends N5ReadyListener {
        public N5ReadyHandler(Context context, String s) {
            super(context, s);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // update printer status
            if (isPlatformAvailable()) {
                // show current print status
                updateStatusFlags();

                //Update status
                PrtActionRequest.refreshStatus();
            }
        }
    }

    private void updateStatusFlags() {
		/*logger.info("Printer Status: " +
			" C="+ PrtStatus.isCoverOpen() +
			",H="+ PrtStatus.isHardwareFault() +
			",O="+ PrtStatus.isOverTemperature() +
			",P="+ PrtStatus.isPaperOut() +
			",R="+ PrtStatus.isPrinterReady() 
		);*/
    }

    public static void addGraphicLine(int rows) {
        int cols = 576;
        byte[] line = new byte[rows * cols / 8];

        Arrays.fill(line, (byte) 0xff);
        PrtGraphics.printGraphic(rows, cols, line);
    }

    public static void printTitle(String text) throws IOException {
        PrtFormatting.setFont(Fonts.SAN_SERIF_10_2_CPI);
        PrtTextStream.write(text);
    }

    public Fonts getFont(String fontName) {
        if (fontName == null || fontName.isEmpty()) {
            return Fonts.COURIER_13_5_CPI;
        }

        String fullName = fontMaps.get(fontName);
        if (fullName == null) {
            fullName = fontName;
        }

        return Fonts.valueOf(fullName);
    }

    public PrtContrastLevel getContrastLevel(String constratLevel) {
        if (constratLevel == null || constratLevel.isEmpty()) {
            return PrtContrastLevel.CONTRAST_LEVEL_5;
        }

        String fullName = contrastLevels.get(constratLevel);
        if (fullName == null) {
            fullName = constratLevel;
        }

        return PrtContrastLevel.valueOf(fullName);
    }

    public void showPrinterErrorMsg(final String message) {
        showPrinterErrorMsg(message, false, null);
    }


    public void showPrinterErrorMsg(final String message, final boolean actionRequired, final CallbackHandler callback) {
        logger.error(message);

        if (!actionRequired
                || activity == null
                || activity.isDestroyed()
                || activity.isFinishing()) {

            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            activity.runOnUiThread(() -> {
                final Builder dialog = new Builder(activity);
                dialog.setCancelable(false);
                dialog.setMessage(message);
                dialog.setTitle("Print Error");
                dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        callback.failure("OK");
                        stopPrintServices();
                    }
                });

                dialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        callback.success("Retry");
                    }
                });

                dialog.show();
            });

        } catch (Exception e) {
            logger.info(TPUtility.getPrintStackTrace(e));
        }
    }
}