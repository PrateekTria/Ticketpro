package com.ticketpro.util;

import static com.ticketpro.util.TPConstant.TAG;
import static com.twotechnologies.n5library.N5Information.isServiceAvailable;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.ticketpro.model.ALPRChalk;
import com.ticketpro.model.Address;
import com.ticketpro.model.Body;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Color;
import com.ticketpro.model.Contact;
import com.ticketpro.model.CustomerInfo;
import com.ticketpro.model.CustomerModule;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.model.Duration;
import com.ticketpro.model.Duty;
import com.ticketpro.model.Feature;
import com.ticketpro.model.MaintenanceLog;
import com.ticketpro.model.MaintenancePicture;
import com.ticketpro.model.MakeAndModel;
import com.ticketpro.model.Module;
import com.ticketpro.model.PrintMacro;
import com.ticketpro.model.PrintTemplate;
import com.ticketpro.model.SpecialActivityReport;
import com.ticketpro.model.State;
import com.ticketpro.model.SyncData;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.model.User;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.api.ChalkVehicleNetworkCalls;
import com.ticketpro.print.N5TicketPrinter;
import com.ticketpro.print.TicketPrinter;
import com.twotechnologies.n5library.N5Library;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Completable;

public class TPUtility {

    final protected static char[] hexArray = "0123456789abcdef".toCharArray();
    public static String DEVICE_ID = "";
    public static String result;
    private static int retry = 0;

    public static synchronized String getPrintStackTrace(Exception e) {
        FirebaseCrashlytics.getInstance().recordException(e);
        FirebaseCrashlytics.getInstance().log(String.valueOf(TPApplication.getInstance().userId + TPApplication.getInstance().deviceId));
        FirebaseCrashlytics.getInstance().setUserId(String.valueOf(TPApplication.getInstance().userId + TPApplication.getInstance().deviceId));
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return e.getMessage();
    }

    public static void removeImage(String imagePath) {
        if (imagePath != null) {
            try {
                File file = new File(imagePath);
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Dialog showProgressDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(R.layout.progress);
        Dialog dialog = builder.create();
        return dialog;
    }

    public static synchronized String getPrintStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static String escapeSpecialChars(String str) {
        if (str == null || str.equals("")) {
            return "";
        }

        return str.replaceAll("\\$", "\\\\\\$");
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float aspect = (float) width / height;
        float scaleWidth = newWidth;
        float scaleHeight = scaleWidth / aspect;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth / width, scaleHeight / height);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        bm.recycle();

        return resizedBitmap;
    }

    public static boolean isValidPlateNumber(String plateNumber, String state) {
        if (state != null && !state.equals("CA"))
            return true;

        if (plateNumber == null || plateNumber.equals(""))
            return false;
        plateNumber = plateNumber.toUpperCase(Locale.getDefault());
        boolean shouldWarnCAPlate = true;
        int length = plateNumber.length();
        switch (length) {

            case 5: {
                Pattern p = Pattern.compile("^[A-Za-z]{2}[0-9]{3}$");
                Matcher m = p.matcher(plateNumber);
                if (m.find()) {
                    shouldWarnCAPlate = false;
                }
            }
            break;
            case 6:
                if (isNumeric(StringUtil.Left(plateNumber, 5)) && isAlpha(StringUtil.Right(plateNumber, 1), false))
                    shouldWarnCAPlate = false;

                if (isNumeric(StringUtil.Right(plateNumber, 4))) {
                    if (isAlpha(StringUtil.Left(plateNumber, 2), true) || (isNumeric(StringUtil.Left(plateNumber, 1))
                            && isAlpha(StringUtil.Mid(plateNumber, 2, 1), true)))
                        shouldWarnCAPlate = false;
                }

                if (isNumeric(StringUtil.Left(plateNumber, 3)) && isAlpha(StringUtil.Right(plateNumber, 3), false))
                    shouldWarnCAPlate = false;

                if (isNumeric(StringUtil.Right(plateNumber, 3)) && isAlpha(StringUtil.Left(plateNumber, 3), true))
                    shouldWarnCAPlate = false;

                if (isNumeric(StringUtil.Left(plateNumber, 4)) && StringUtil.Right(plateNumber, 2).equals("PH"))
                    shouldWarnCAPlate = false;

                if (StringUtil.Left(plateNumber, 2).equals("HV") && isAlpha(StringUtil.Right(plateNumber, 1), false)
                        && isNumeric(StringUtil.Mid(plateNumber, 3, 3)))
                    shouldWarnCAPlate = false;

                if (isNumeric(StringUtil.Left(plateNumber, 2)) && isAlpha(StringUtil.Mid(plateNumber, 3, 1), false)
                        && isNumeric(StringUtil.Mid(plateNumber, 4, 2)) && isAlpha(StringUtil.Right(plateNumber, 1), false))
                    shouldWarnCAPlate = false;

                if (isAlpha(StringUtil.Right(plateNumber, 1), false) && isNumeric(StringUtil.Mid(plateNumber, 2, 2))
                        && isAlpha(StringUtil.Mid(plateNumber, 4, 1), false) && isNumeric(StringUtil.Right(plateNumber, 2)))
                    shouldWarnCAPlate = false;

                if (isAlpha(StringUtil.Left(plateNumber, 1), false) && !(Character.toString(plateNumber.charAt(0)).equals("I") || Character.toString(plateNumber.charAt(0)).equals("O") || Character.toString(plateNumber.charAt(0)).equals("Q") ||
                        Character.toString(plateNumber.charAt(2)).equals("I") || Character.toString(plateNumber.charAt(2)).equals("O") || Character.toString(plateNumber.charAt(2)).equals("Q")))
                    shouldWarnCAPlate = false;

                if (Character.toString(plateNumber.charAt(0)).equals("I") || Character.toString(plateNumber.charAt(0)).equals("O") || Character.toString(plateNumber.charAt(0)).equals("Q") ||
                        Character.toString(plateNumber.charAt(2)).equals("I") || Character.toString(plateNumber.charAt(2)).equals("O") || Character.toString(plateNumber.charAt(2)).equals("Q"))
                    shouldWarnCAPlate = true;

                break;

            case 7: {
                Pattern p = Pattern.compile("^[A-Za-z]{2}[0-9]{2}[A-Za-z]{1}[0-9]{2}$");
                Matcher m = p.matcher(plateNumber);
                if (m.find()) {
                    shouldWarnCAPlate = false;
                }
            }
            Pattern p = Pattern.compile("^[A-Za-z]{2}[0-9]{5}$");
            Matcher m = p.matcher(plateNumber);
            if (m.find()) {
                shouldWarnCAPlate = false;
            }

            if (isNumeric(StringUtil.Right(plateNumber, 5)) && isNumeric(StringUtil.Left(plateNumber, 1))
                    && isAlpha(StringUtil.Mid(plateNumber, 2, 1), true))
                shouldWarnCAPlate = false;

            if (isNumeric(StringUtil.Left(plateNumber, 5)) && isNumeric(StringUtil.Right(plateNumber, 1))
                    && isAlpha(StringUtil.Mid(plateNumber, 6, 1), true))
                shouldWarnCAPlate = false;

            if (isNumeric(StringUtil.Right(plateNumber, 4)) && isNumeric(StringUtil.Left(plateNumber, 2))
                    && isAlpha(StringUtil.Mid(plateNumber, 3, 1), true))
                shouldWarnCAPlate = false;

            if (isNumeric(StringUtil.Right(plateNumber, 4)) && isNumeric(StringUtil.Left(plateNumber, 2))
                    && isAlpha(StringUtil.Mid(plateNumber, 3, 1), true) && isNumeric(StringUtil.Left(plateNumber, 1))
                    && isAlpha(StringUtil.Mid(plateNumber, 2, 2), true))
                shouldWarnCAPlate = false;

            if (isNumeric(StringUtil.Right(plateNumber, 4)) && isNumeric(StringUtil.Left(plateNumber, 1))
                    && isAlpha(StringUtil.Mid(plateNumber, 2, 2), false) && StringUtil.hasValidChars(plateNumber))
                shouldWarnCAPlate = false;

            if (isNumeric(StringUtil.Right(plateNumber, 3)) && isNumeric(StringUtil.Left(plateNumber, 1))
                    && isAlpha(StringUtil.Mid(plateNumber, 2, 3), false))
                shouldWarnCAPlate = false;

            if (isNumeric(StringUtil.Left(plateNumber, 4)) && isAlpha(StringUtil.Right(plateNumber, 3), false))
                shouldWarnCAPlate = false;

            if (Character.toString(plateNumber.charAt(1)).equals("I") || Character.toString(plateNumber.charAt(1)).equals("O") || Character.toString(plateNumber.charAt(1)).equals("Q") ||
                    Character.toString(plateNumber.charAt(3)).equals("I") || Character.toString(plateNumber.charAt(3)).equals("O") || Character.toString(plateNumber.charAt(3)).equals("Q"))
                shouldWarnCAPlate = true;

            break;
        }

        return !shouldWarnCAPlate;
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.length() == 0)
            return false;

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
    }

    public static boolean isAlpha(String str, boolean spaceChar) {
        if (str == null || str.length() == 0)
            return false;

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)) || (!spaceChar && Character.isSpace(str.charAt(i))))
                return false;
        }

        return true;
    }

    public static String getValidVIN(String vinString) {
        vinString = vinString.replace("I", "1");
        vinString = vinString.replace("O", "0");
        vinString = vinString.replace("Q", "0");
        vinString = vinString.replaceAll("[^\\dA-Za-z]", "").replaceAll(" ", "");
        try {
            vinString = vinString.toUpperCase(Locale.getDefault());
        } catch (Exception ignored) {
        }
        int vinLength = vinString.length();
        if (vinString.equals("COV"))
            return vinString;

        if (Feature.isFeatureAllowed(Feature.SHOW_VNV)
                && vinString.equalsIgnoreCase(Feature.getFeatureValue(Feature.SHOW_VNV))) {

            return vinString;
        }

        if (Feature.isFeatureAllowed(Feature.VIN_SPECIAL_VALIDATE)
                && (vinString.equals("VNV") || vinString.equals("VC"))) {

            return vinString;
        }

        if (vinLength > 17 && StringUtil.Left(vinString, 1).equals("I")) {
            vinString = StringUtil.Mid(vinString, 2);
            vinLength = vinString.length();
        }

        return vinString;
    }

    public static String getDataFromQR(String qrcode, int i) {
        String[] details = qrcode.split(" ");
        if (i == 12) {
            String[] date = details[i].split("-");

            return getMonthfromNo(Integer.parseInt(date[0])) + "/" + date[2];
        }
        return details[i];
    }

    public static String getValidPlate(String plate) {
        plate = plate.replaceAll("[^\\dA-Za-z]", "").replaceAll(" ", "");
        try {
            plate = plate.toUpperCase(Locale.getDefault());
        } catch (Exception ignored) {
        }
        if (plate.length() > 8)
            plate = StringUtil.Left(plate, 8);

        return plate;
    }

    public static String getValidMeter(String meter) {
        meter = meter.replaceAll("[^\\dA-Za-z-]", "").replaceAll(" ", "");
        try {
            meter = meter.toUpperCase(Locale.getDefault());
        } catch (Exception ignored) {
        }
        return meter;
    }

    public static String getValidPermit(String permit) {
        permit = permit.replaceAll("[^\\dA-Za-z]", "").replaceAll(" ", "");
        try {
            permit = permit.toUpperCase(Locale.getDefault());
        } catch (Exception ignored) {
        }
        return permit;
    }

    public static String VINValidationMsg(String vin) {
        String message = "";

        int len = vin.length();
        if (!(len == 4 || len == 17 || len == 0)) {
            if (Feature.isFeatureAllowed(Feature.VIN_SPECIAL_VALIDATE) && (vin.equals("VNV") || vin.equals("VC"))) {
                message = "";
            } else if (Feature.isFeatureAllowed(Feature.SHOW_VNV)
                    && vin.equalsIgnoreCase(Feature.getFeatureValue(Feature.SHOW_VNV))) {
                message = "";
            } else if (len == 11) {
                message = "VIN plates with only 11 characters are rare. Please verify before continuing";
            } else if (len == 9) {
                message = "VIN plates with only 9 characters are rare. Please verify before continuing";
            } else {
                message = "VIN should be either the last 4 or all 17 characters!";
            }
        }

        return message;
    }

    public static boolean PlateVINValidate(String vin, boolean isPlateExists) {
        if (isPlateExists) {
            if (Feature.isFeatureAllowed(Feature.VIN_SPECIAL_VALIDATE)) {
                if (Feature.isFeatureAllowed(Feature.SHOW_VNV)) {
                    return vin.equalsIgnoreCase(Feature.getFeatureValue(Feature.SHOW_VNV)) || vin.length() == 4
                            || vin.length() == 17;
                }
                if (vin.equalsIgnoreCase("VNV") || vin.equalsIgnoreCase("VC")) {
                    return true;
                }
                return vin.length() == 4 || vin.length() == 17;
            } else if (Feature.isFeatureAllowed(Feature.SHOW_VNV)
                    && vin.equalsIgnoreCase(Feature.getFeatureValue(Feature.SHOW_VNV))) {
                return true;
            }
            return vin.length() == 0 || vin.length() == 4 || vin.length() == 17 || vin.length() == 11;
        } else if (Feature.isFeatureAllowed(Feature.VIN_OVERRIDE) && vin.length() >= 4) {
            return true;
        } else return vin.length() == 17 || vin.length() == 11;
    }

    public static boolean ChalkVINValidate(String vin, boolean isPlateExists) {

        if (isPlateExists) {
            if (Feature.isFeatureAllowed(Feature.SHOW_VNV)
                    && vin.equalsIgnoreCase(Feature.getFeatureValue(Feature.SHOW_VNV))) {
                return true;
            }

            if (Feature.isFeatureAllowed(Feature.VIN_SPECIAL_VALIDATE)) {
                if (vin.equalsIgnoreCase("VNV") || vin.equalsIgnoreCase("VC"))
                    return true;
            }

            return vin.length() == 0 || vin.length() == 4 || vin.length() == 17 || vin.length() == 11;

        } else return vin.length() == 17 || vin.length() == 11;
    }

    public static String prefixZeros(long num, int digits) {
        String output = Long.toString(num);
        while (output.length() < digits)
            output = "0" + output;
        return output;
    }

    public static long reverseNumber(long number) {
        long reverse = 0;
        while (number > 0) {
            reverse = reverse * 10;
            reverse = reverse + number % 10;
            number = number / 10;
        }
        return reverse;
    }

    public static String getHrsMinSecs(long milliseconds) {

        // int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)));

        String hrs = (hours < 10) ? ("0" + hours) : hours + "";
        String mins = (minutes < 10) ? ("0" + minutes) : minutes + "";
        // String secs = (seconds < 10) ? ("0" + seconds) : seconds + "";

        return hrs + ":" + mins; // + ":" + secs;
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        camera.setDisplayOrientation(result);
    }

    public static int getRotationDegrees(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        return degrees;
    }

    public static Bitmap rotateBitmap(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                throw ex;
            }
        }
        return b;
    }

    public static void copy(File src, File dst, boolean removeFlag) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        in.close();
        out.close();

        if (removeFlag && src.exists()) {
            src.delete();
        }
    }

    public static boolean uploadFile(String sourceFileName, String uploadServerUri, int custId) {
        try {
            HttpClient client = new TPHttpClient();
            HttpPost httppost = new HttpPost(uploadServerUri);

            MultipartEntity entity = new MultipartEntity();

            File sourceFile = new File(sourceFileName);
            if (!sourceFile.isFile()) {
                Log.e("UploadFile", "File does not exists");
                return false;
            }

            entity.addPart("uploadedFile", new FileBody(sourceFile));
            entity.addPart("custId", new StringBody(custId + ""));

            httppost.setEntity(entity);

            HttpResponse response = client.execute(httppost);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
            String sResponse;

            StringBuilder responseBuffer = new StringBuilder();
            while ((sResponse = reader.readLine()) != null) {
                responseBuffer.append(sResponse);
            }

            Log.i("UploadFile", "File Upload.." + responseBuffer.toString());
            client.getConnectionManager().shutdown();

            if (responseBuffer.toString().equals("true")){
                return true;
            }else {
                return false;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean uploadSelfie(String sourceFileName, String uploadServerUri, int custId) {
        try {
            HttpClient client = new TPHttpClient();
            HttpPost httppost = new HttpPost(uploadServerUri);

            MultipartEntity entity = new MultipartEntity();

            File sourceFile = new File(sourceFileName);
            if (!sourceFile.isFile()) {
                Log.e("UploadFile", "File does not exists");
                return false;
            }

            entity.addPart("uploadedFile", new FileBody(sourceFile));
            entity.addPart("custId", new StringBody(custId + ""));
            entity.addPart("isSelfi", new StringBody("1"));

            httppost.setEntity(entity);

            HttpResponse response = client.execute(httppost);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
            String sResponse;

            StringBuilder responseBuffer = new StringBuilder();
            while ((sResponse = reader.readLine()) != null) {
                responseBuffer.append(sResponse);
            }

            Log.i("UploadFile", "File Upload.." + responseBuffer.toString());
            client.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static ArrayList<TicketComment> getPrintableComments(ArrayList<TicketComment> ticketComments) {
        ArrayList<TicketComment> comments = new ArrayList<TicketComment>();
        for (TicketComment comment : ticketComments) {
            if (comment.isPrivate() || comment.isVoiceComment())
                continue;

            comments.add(comment);
        }

        return comments;
    }



    public static String getExpiration(String expString) {
        if (expString == null || expString.length() <= 2)
            return expString;

        String expiration = expString;
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        List<String> monthsList = Arrays.asList(months);
        try {
            String[] expArray = expString.split("/");
            if (expArray.length == 1) {
                if (monthsList.contains(expArray[0])) {
                    int monthIndex = monthsList.indexOf(expArray[0]);
                    monthIndex += 1;
                    expiration = monthIndex + "/";
                    if (monthIndex < 10)
                        expiration = "0" + monthIndex + "/";
                } else {
                    expiration = "/" + expArray[0];
                }
            } else if (expArray.length == 2) {
                int monthIndex = monthsList.indexOf(expArray[0]);
                if (monthIndex >= 0) {
                    monthIndex += 1;

                    expiration = monthIndex + "/" + expArray[1];
                    if (monthIndex < 10)
                        expiration = "0" + monthIndex + "/" + expArray[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return expiration;
    }

    public static String getMonthfromNo(int number) {
        String month = "";

        switch (number) {
            case 1:
                return "JAN";

            case 2:
                return "FEB";

            case 3:
                return "MAR";

            case 4:
                return "APR";

            case 5:
                return "MAY";

            case 6:
                return "JUN";

            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";

        }

        return month;
    }

    public static String getMonthfromText(String month) {
        String month_no = "";

        switch (month) {
            case "JAN":
                return "01";
            case "FEB":
                return "02";
            case "MAR":
                return "03";
            case "APR":
                return "04";
            case "MAY":
                return "05";
            case "JUN":
                return "06";
            case "JUL":
                return "07";
            case "AUG":
                return "08";
            case "SEP":
                return "09";
            case "OCT":
                return "10";
            case "NOV":
                return "11";
            case "DEC":
                return "12";
        }
        return month_no;
    }

    public static String getImageSize(String imagePath) {
        double KB = 0;
        File file = new File(imagePath);
        if (file.exists()) {
            KB = file.length() / 1024.0;
        }

        String strSize = "";
        DecimalFormat dec = new DecimalFormat("0.00");
        double MB = KB / 1024.0;
        if (MB > 1) {
            strSize = dec.format(MB).concat(" MB");
        } else {
            strSize = dec.format(KB).concat(" KB");
        }

        return strSize;
    }

    public static String getImageSize(byte[] byteArray) {
        try {
            double KB = byteArray.length / 1024.0;
            String strSize = "";
            DecimalFormat dec = new DecimalFormat("0.00");
            double MB = KB / 1024.0;
            if (MB > 1) {
                strSize = dec.format(MB).concat(" MB");
            } else {
                strSize = dec.format(KB).concat(" KB");
            }

            return strSize;

        } catch (Exception e) {
            return "0.00 KB";
        }
    }

    public static int getNetworkTimeout() {
        int timeout = 5;
        if (Feature.isFeatureAllowed(Feature.TIMEOUT)) {
            String timeoutString = Feature.getFeatureValue(Feature.TIMEOUT);
            try {
                timeout = Integer.parseInt(timeoutString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return timeout * 1000;
    }

    public static String getURLResponse(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        SSLHttpClient httpClient = new SSLHttpClient();
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, getNetworkTimeout());
        HttpConnectionParams.setSoTimeout(params, getNetworkTimeout());
        httpClient.setRedirectHandler(new DefaultRedirectHandler());
        httpClient.setKeepAliveStrategy((response, context) -> 0);

        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity);
    }

    public static String getURLResponse(String url, boolean isTLS12Required) throws IOException {
        //String testurl1 = "https://csufresno.verrus.com/ParkEnforce/?method=GetPaidAtWaveNumber&wavenumber=5447&User=Turbo1&Password=7uR9Od474&VendorUID=8481";
        //String testurl1 = "https://ParkEnforce.verrus.com/ParkEnforce/?method=GetPaidAtWaveNumber&wavenumber=5213&User=Ventek13&Password=Canucks17&VendorUID=9061";
        HttpGet request = new HttpGet(url);
        SSLHttpClient httpClient = new SSLHttpClient(isTLS12Required);
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, getNetworkTimeout());
        HttpConnectionParams.setSoTimeout(params, getNetworkTimeout());
        httpClient.setRedirectHandler(new DefaultRedirectHandler());
        httpClient.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                return 0;
            }
        });

        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity);
    }

    public static String getURLResponseWithTLS(String urlString)
            throws IOException, KeyManagementException, NoSuchAlgorithmException {

        URL url = new URL(urlString);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setSSLSocketFactory(new TLSSocketFactory());
        conn.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        conn.setReadTimeout(getNetworkTimeout());
        conn.setConnectTimeout(getNetworkTimeout());
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        String result = "";
        InputStream is = conn.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            result += inputLine;
        }

        return result;
    }

    public static String callService(String urlString) {
        //urlString = "https://parkingpermits.menlopark.org/parkingservice/service1.asmx";
        String result = "";
        try {
		/*String response = "";
		StringEntity se;
		try {
			se = new StringEntity(request, HTTP.UTF_8);
			se.setContentType("text/xml");
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(URL);
			httpPost.setEntity(se);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity resEntity = httpResponse.getEntity();
			response = EntityUtils.toString(resEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return response;*/
            URL url = new URL(urlString);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(new TLSSocketFactory());
            conn.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            conn.setReadTimeout(getNetworkTimeout());
            conn.setConnectTimeout(getNetworkTimeout());
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();

            result = "";
            InputStream is = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result += inputLine;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String requestUrl(String postParameters) {
        String result = "";
        String url = "https://parkingpermits.menlopark.org/parkingservice/service1.asmx";
        HttpsURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(url);
            urlConnection = (HttpsURLConnection) urlToRequest.openConnection();
            urlConnection.setSSLSocketFactory(new TLSSocketFactory());
            urlConnection.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            urlConnection.setReadTimeout(getNetworkTimeout());
            urlConnection.setConnectTimeout(getNetworkTimeout());

            // handle POST parameters
            if (postParameters != null) {
                Log.i("TAG", "POST parameters: " + postParameters);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setFixedLengthStreamingMode(
                        postParameters.getBytes().length);
                urlConnection.setRequestProperty("Content-Type",
                        "text/xml");

                //send the POST out
                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(postParameters);
                out.close();
            }

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                // throw some exception
            }

            // read output (only for GET)
            if (postParameters == null) {
                return null;
            } else {
                InputStream in =
                        new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
                String inputLine;
                while ((inputLine = bfr.readLine()) != null) {
                    result += inputLine;
                }


                return result;
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
            // handle invalid URL
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            // hadle timeout
        } catch (IOException e) {
            e.printStackTrace();
            // handle I/0
        } catch (Exception e) {
            e.printStackTrace();// handle I/0
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    public static String postXMLRequest(String xmlContentToSend) {
        String urlToSendRequest = "https://parkingpermits.menlopark.org/parkingservice/service1.asmx";
        String targetDomain = "parkingpermits.menlopark.org";

        //String targetDomain = "https://parkingpermits.menlopark.org";
        //String xmlContentToSend = "hello this is a test";

        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpHost targetHost = new HttpHost(targetDomain);
        // Using POST here
        HttpPost httpPost = new HttpPost(urlToSendRequest);
        // Make sure the server knows what kind of a response we will accept
        httpPost.addHeader("Accept", "text/xml");
        // Also be sure to tell the server what kind of content we are sending
        httpPost.addHeader("Content-Type", "application/xml");

        try {
            StringEntity entity = new StringEntity(xmlContentToSend, "UTF-8");
            entity.setContentType("application/xml");
            httpPost.setEntity(entity);

            // execute is a blocking call, it's best to call this code in a
            // thread separate from the ui's
            HttpResponse response = httpClient.execute(targetHost, httpPost);

            HttpEntity entity1 = response.getEntity();
            InputStream content = entity1.getContent();
            // Have your way with the response
            String result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(content));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result += inputLine;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String postSOAPURLResponseWithTLS(String body)
            throws IOException, KeyManagementException, NoSuchAlgorithmException {
        String urlString = "https://parkingpermits.menlopark.org/parkingservice/service1.asmx";
        URL url = new URL(urlString);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setSSLSocketFactory(new TLSSocketFactory());
        conn.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        conn.setReadTimeout(getNetworkTimeout());
        conn.setConnectTimeout(getNetworkTimeout());
        conn.setRequestMethod("POST");
        //conn.setCont
        conn.setDoInput(true);
        conn.connect();


        //Writing data (bytes) to the data output stream
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        dos.writeBytes(body);
        //flushes data output stream.
        dos.flush();
        dos.close();

        //Getting HTTP response code
        int response = conn.getResponseCode();

        //if response code is 200 / OK then read Inputstream
        //HttpURLConnection.HTTP_OK is equal to 200
        if (response == HttpURLConnection.HTTP_OK) {
            Log.i("OK", "Connection OK");
            //DataInputStream = conn.getInputStream();
        }

        StringBuilder result = new StringBuilder();
        InputStream is = conn.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            result.append(inputLine);
        }

        return result.toString();
    }

    public static String getURLResponse(String url, String username, String password)
            throws IOException {
        HttpGet request = new HttpGet(url);
        SSLHttpClient httpClient = new SSLHttpClient();
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, getNetworkTimeout());
        HttpConnectionParams.setSoTimeout(params, getNetworkTimeout());
        httpClient.setRedirectHandler(new DefaultRedirectHandler());
        httpClient.setKeepAliveStrategy((response, context) -> 0);

        httpClient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                new UsernamePasswordCredentials(username, password));

        httpClient.addRequestInterceptor((arg0, context) -> {
            AuthState state = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
            if (state.getAuthScheme() == null) {
                BasicScheme scheme = new BasicScheme();
                CredentialsProvider credentialsProvider = (CredentialsProvider) context
                        .getAttribute(ClientContext.CREDS_PROVIDER);
                Credentials credentials = credentialsProvider.getCredentials(AuthScope.ANY);
                if (credentials == null) {
                    throw new HttpException();
                }

                state.setAuthScope(AuthScope.ANY);
                state.setAuthScheme(scheme);
                state.setCredentials(credentials);
            }
        }, 0);


        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity);
    }

    public static String getPMURLResponse(String url, String username, String password)
            throws IOException {
        HttpGet request = new HttpGet(url);
        SSLHttpClient httpClient = new SSLHttpClient();
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, getNetworkTimeout());
        HttpConnectionParams.setSoTimeout(params, getNetworkTimeout());
        httpClient.setRedirectHandler(new DefaultRedirectHandler());
        httpClient.setKeepAliveStrategy((response, context) -> 0);

        httpClient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                new UsernamePasswordCredentials(username, password));

        httpClient.addRequestInterceptor((arg0, context) -> {
            AuthState state = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
            if (state.getAuthScheme() == null) {
                BasicScheme scheme = new BasicScheme();
                CredentialsProvider credentialsProvider = (CredentialsProvider) context
                        .getAttribute(ClientContext.CREDS_PROVIDER);
                Credentials credentials = credentialsProvider.getCredentials(AuthScope.ANY);
                if (credentials == null) {
                    throw new HttpException();
                }

                state.setAuthScope(AuthScope.ANY);
                state.setAuthScheme(scheme);
                state.setCredentials(credentials);
            }
        }, 0);

        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        if (response.getStatusLine().toString().contains("200 OK")) {
            return EntityUtils.toString(entity);
        } else {
            return response.getStatusLine().toString();
        }
    }

    public static double getAngle(double xTouch, double yTouch, int dialerWidth, int dialerHeight) {
        double x = xTouch - (dialerWidth / 2d);
        double y = dialerHeight - yTouch - (dialerHeight / 2d);
        switch (TPUtility.getQuadrant(x, y)) {
            case 1:
                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 2:
                return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 3:
                return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
            case 4:
                return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            default:
                return 0;
        }
    }

    public static int getQuadrant(double x, double y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
            return y >= 0 ? 2 : 3;
        }
    }

    public static void markPendingImage(String imagePath) {
        SyncData syncData = new SyncData();
        syncData.setActivity("IMAGE_UPLOAD");
        syncData.setActivitySource(imagePath);
        syncData.setActivityDate(new Date());
        syncData.setCustId(TPApplication.getInstance().getCustId());
        syncData.setTableName(TPConstant.TABLE_PENDING_IMAGES);
        syncData.setStatus("PendingUpload");

        try {
            /*DatabaseHelper.getInstance().openWritableDatabase();
            DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
            DatabaseHelper.getInstance().closeWritableDb();*/
            SyncData.insertSyncData(syncData).subscribe();
        } catch (Exception e) {
        }
    }

    public static void markPendingVoiceComment(String voiceFile) {
        SyncData syncData = new SyncData();
        syncData.setActivity("VOICE_UPLOAD");
        syncData.setActivitySource(voiceFile);
        syncData.setActivityDate(new Date());
        syncData.setCustId(TPApplication.getInstance().getCustId());
        syncData.setTableName(TPConstant.TABLE_PENDING_VOICES);
        syncData.setStatus("PendingUpload");

        try {
            SyncData.insertSyncData(syncData).subscribe();
            /*DatabaseHelper.getInstance().openWritableDatabase();
            DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
            DatabaseHelper.getInstance().closeWritableDb();*/
        } catch (Exception e) {
            Log.e("TicketPRO", "Error " + e.getMessage());
        }
    }

    public static String getDataFolder() {
        File file = new File(TPUtility.getExternalStorage() + "/TicketPro");
        if (!file.exists()) {
            file.mkdir();
        }

        file = new File(TPUtility.getExternalStorage() + "/TicketPro/" + TPConstant.MODULE_NAME);
        if (!file.exists()) {
            file.mkdir();
        }

        return TPUtility.getExternalStorage() + "/TicketPro/" + TPConstant.MODULE_NAME + "/";
    }

    public static String getLPRImagesFolder() {
        String dataFolder = TPUtility.getDataFolder();
        File file = new File(dataFolder + "LPRImages");
        if (!file.exists()) {
            file.mkdir();
        }

        return dataFolder + "LPRImages/";
    }

    public static String getALPRImagesFolder() {
        String dataFolder = TPUtility.getDataFolder();
        File file = new File(dataFolder + "ALPRImages");
        if (!file.exists()) {
            file.mkdir();
        }

        return dataFolder + "ALPRImages/";
    }

    public static String getTicketsFolder() {
        String dataFolder = TPUtility.getDataFolder();
        File file = new File(dataFolder + "TicketImages");
        if (!file.exists()) {
            file.mkdir();
        }

        return dataFolder + "TicketImages/";
    }

    public static String getChalksFolder() {
        String dataFolder = TPUtility.getDataFolder();
        File file = new File(dataFolder + "ChalkImages");
        if (!file.exists()) {
            file.mkdir();
        }

        return dataFolder + "ChalkImages/";
    }

    public static String getActivityFolder() {
        String dataFolder = TPUtility.getDataFolder();
        File file = new File(dataFolder + "ActivityImages");
        if (!file.exists()) {
            file.mkdir();
        }

        return dataFolder + "ActivityImages/";
    }

    public static String getBackupFolder() {
        String dataFolder = TPUtility.getDataFolder();
        File file = new File(dataFolder + "Backups");
        if (!file.exists()) {
            file.mkdir();
        }

        return dataFolder + "Backups/";
    }

    public static String getCSVBackupFolder() {
        String dataFolder = TPUtility.getDataFolder();
        File file = new File(dataFolder + "CSVBackups");
        if (!file.exists()) {
            file.mkdir();
        }

        return dataFolder + "CSVBackups/";
    }

    public static File getLPRNotifyCSVFile() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
        Calendar cal = Calendar.getInstance();
        String dateStr = sdf.format(cal.getTime());
        File file = new File(TPUtility.getCSVBackupFolder(), dateStr + "_TBL_LPR_NOTIFY.csv");

        return file;
    }

    public static File getCurrentCSVFile(String table) {
        if (table == null)
            return null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
        Calendar cal = Calendar.getInstance();
        String dateStr = sdf.format(cal.getTime());
        File file = new File(TPUtility.getCSVBackupFolder(), dateStr + "_TBL_" + table + ".csv");
        return file;
    }

    public static File getSystemBackupCSVFile() {
        return new File(TPUtility.getCSVBackupFolder(), "SYSTEM_BACKUP.csv");
    }

    public static File getNetworkLogsCSVFile() {
        return new File(TPUtility.getRootFolder(), "NETWORK_LOGS.csv");
    }

    public static File getMobileLogsCSVFile() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
        Calendar cal = Calendar.getInstance();
        String dateStr = sdf.format(cal.getTime());
        File file = new File(TPUtility.getCSVBackupFolder(), dateStr + "_MOBILE_LOGS.csv");

        return file;
    }

    /**
     * @return root folder path of application
     */
    public static String getRootFolder() {
        try {
            File file = new File(TPUtility.getExternalStorage() + "/TicketPro");
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TPUtility.getExternalStorage() + "/TicketPro/";
    }

    public static String getVoiceMemosFolder() {
        String dataFolder = TPUtility.getDataFolder();
        File file = new File(dataFolder + "VoiceMemos");
        if (!file.exists()) {
            file.mkdir();
        }

        return dataFolder + "VoiceMemos/";
    }

    public static String parseTicketTokens(String template, Ticket ticket) {
        TPApplication TPApp = TPApplication.getInstance();

        template = template.replaceAll("\\{CITE\\}", TPUtility.prefixZeros(ticket.getCitationNumber(), 8));
        template = template.replaceAll("\\{DATE\\}", DateUtil.getStringFromDate(ticket.getTicketDate()) + "");
        template = template.replaceAll("\\{CITE_DATE\\}", DateUtil.getDateStringFromDate(ticket.getTicketDate()) + "");
        template = template.replaceAll("\\{CITE_TIME\\}", DateUtil.getTimeStringFromDate(ticket.getTicketDate()) + "");
        template = template.replaceAll("\\{METER\\}", ticket.getMeter() + "");
        template = template.replaceAll("\\{LOCATION\\}", TPUtility.getFullAddress(ticket) + "");
        template = template.replaceAll("\\{PLATE\\}", ticket.getPlate() + "");
        template = template.replaceAll("\\{EXPDATE\\}", ticket.getExpiration() + "");
        template = template.replaceAll("\\{VIN\\}", ticket.getVin() + "");
        template = template.replaceAll("\\{STATE_CODE\\}", ticket.getStateCode() + "");
        template = template.replaceAll("\\{MAKE_CODE\\}", ticket.getMakeCode() + "");
        template = template.replaceAll("\\{BODY_CODE\\}", ticket.getBodyCode() + "");
        template = template.replaceAll("\\{COLOR_CODE\\}", ticket.getColorCode() + "");
        template = template.replaceAll("\\{VIOLATION\\}", ticket.getViolation() + "");
        template = template.replaceAll("\\{VIOLATION_CODE\\}", ticket.getViolationCode() + "");

        if (ticket.getStateCode() != null && !ticket.getStateCode().equals("")) {
            State state = State.getStateByName(ticket.getStateCode());
            if (state != null) {
                template = template.replaceAll("\\{STATE\\}", state.getTitle());
            }
        }

        template = template.replaceAll("\\{STATE\\}", "");

        if (ticket.getMakeCode() != null && !ticket.getMakeCode().equals("")) {
            MakeAndModel make = MakeAndModel.getMakeByCode(ticket.getMakeCode());
            if (make != null) {
                template = template.replaceAll("\\{MAKE\\}", make.getMakeTitle());
            }
        }

        template = template.replaceAll("\\{MAKE\\}", "");

        if (ticket.getBodyCode() != null && !ticket.getBodyCode().equals("")) {
            Body body = Body.getBodyByCode(ticket.getBodyCode());
            if (body != null) {
                template = template.replaceAll("\\{BODY\\}", body.getTitle());
            }
        }

        template = template.replaceAll("\\{BODY\\}", "");

        if (ticket.getColorCode() != null && !ticket.getColorCode().equals("")) {
            Color color = Color.getColorByCode(ticket.getColorCode());
            if (color != null) {
                template = template.replaceAll("\\{COLOR\\}", color.getTitle());
            }
        }

        template = template.replaceAll("\\{COLOR\\}", "");
        template = template.replaceAll("\\{PERMIT\\}", ticket.getPermit() + "");
        template = template.replaceAll("\\{FULL_LOC\\}", TPUtility.getFullAddress(ticket));
        template = template.replaceAll("\\{LOC_BLOCK\\}", ticket.getStreetNumber());
        template = template.replaceAll("\\{LOC_DIRECTION\\}", ticket.getDirection());
        template = template.replaceAll("\\{LOC_PREFIX\\}", ticket.getStreetPrefix());
        template = template.replaceAll("\\{LOC_SUFFIX\\}", ticket.getStreetSuffix());
        template = template.replaceAll("\\{LONG_LAT\\}", ticket.getLongitude() + " - " + ticket.getLatitude());
        template = template.replaceAll("\\{LONG\\}", ticket.getLongitude());
        template = template.replaceAll("\\{LAT\\}", ticket.getLatitude());
        template = template.replaceAll("\\{USERID\\}", ticket.getUserId() + "");
        template = template.replaceAll("\\{MARKED\\}", DateUtil.getStringFromDate(ticket.getTimeMarked()) + "");
        template = template.replaceAll("\\{MARKED_DATE\\}",
                DateUtil.getDateStringFromDate(ticket.getTimeMarked()) + "");
        template = template.replaceAll("\\{MARKED_TIME\\}",
                DateUtil.getTimeStringFromDate(ticket.getTimeMarked()) + "");

        String agencyCode = "";
        String webAddress = "";
        if (TPApp.getCustomerInfo() != null) {
            agencyCode = TPApp.getCustomerInfo().getAgencyCode();
            webAddress = TPApp.getCustomerInfo().getWebAddress();
        }

        template = template.replaceAll("\\{AGENCY_CODE\\}", agencyCode);
        template = template.replaceAll("\\{WEBADDRESS\\}", webAddress);

        if (ticket.isVoided()) {
            template = template.replaceAll("\\{VOIDMSG\\}", PrintMacro.getPrintMacroMessageByName("VOIDMSG"));
        } else {
            template = template.replaceAll("\\{VOIDMSG\\}", "");
        }

        if (ticket.isWarn()) {
            template = template.replaceAll("\\{WARNMSG\\}", PrintMacro.getPrintMacroMessageByName("WARNMSG"));
        } else {
            template = template.replaceAll("\\{WARNMSG\\}", "");
        }

        if (ticket.getTicketPictures().size() > 0) {
            template = template.replaceAll("\\{PHOTOMSG\\}", PrintMacro.getPrintMacroMessageByName("PHOTOMSG"));
        } else {
            template = template.replaceAll("\\{PHOTOMSG\\}", "");
        }

        Duty duty;
        try {
            duty = Duty.getDutyById(ticket.getDutyId());
            if (duty != null)
                template = template.replaceAll("\\{DUTY\\}", duty.getTitle());
            else
                template = template.replaceAll("\\{DUTY\\}", "");

        } catch (Exception e2) {
            e2.printStackTrace();
            template = template.replaceAll("\\{DUTY\\}", "");
        }

        if (ticket.getExpiration() != null && ticket.getExpiration().contains("/")) {
            try {
                String[] expArray = ticket.getExpiration().split("/");
                template = template.replaceAll("\\{EXP_MONTH\\}", expArray[0]);
                template = template.replaceAll("\\{EXP_YEAR\\}", expArray[1]);
            } catch (Exception e) {
            }
        }

        template = template.replaceAll("\\{EXP_MONTH\\}", "");
        template = template.replaceAll("\\{EXP_YEAR\\}", "");

        User userInfo;
        try {
            userInfo = User.getUserInfo(ticket.getUserId());
            if (userInfo != null) {
                template = template.replaceAll("\\{BADGE\\}", userInfo.getBadge() + "");
                template = template.replaceAll("\\{DEPT\\}", userInfo.getDepartment() + "");
                template = template.replaceAll("\\{FIRST_NAME\\}", userInfo.getFirstName() + "");
                template = template.replaceAll("\\{LAST_NAME\\}", userInfo.getLastName() + "");
            } else {
                template = template.replaceAll("\\{BADGE\\}", "");
                template = template.replaceAll("\\{DEPT\\}", "");
                template = template.replaceAll("\\{FIRST_NAME\\}", "");
                template = template.replaceAll("\\{LAST_NAME\\}", "");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (ticket.isChalked() && ticket.getChalkId() > 0) {
            if (ticket.getTimeZone() != null && ticket.getElapsed() != null) {
                template = template.replaceAll("\\{TIME_ZONE\\}", ticket.getTimeZone());
                template = template.replaceAll("\\{ELAPSED\\}", ticket.getElapsed());

            } else {
                try {
                    ChalkVehicle chalk = ChalkVehicle.getChalkVehicleById(ticket.getChalkId());
                    if (chalk != null) {
                        Date dt = ticket.getTicketDate();
                        if (dt == null)
                            dt = new Date();

                        String durationTitle = Duration.getDurationTitleById(chalk.getDurationId());
                        long milliseconds = (ticket.getTicketDate().getTime() - chalk.getChalkDate().getTime());
                        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                        int hours = (int) ((milliseconds / (1000 * 60 * 60)));
                        String hrs = (hours < 10) ? ("0" + hours) : hours + "";
                        String mins = (minutes < 10) ? ("0" + minutes) : minutes + "";
                        template = template.replaceAll("\\{ELAPSED\\}", hrs + ":" + mins + " hrs/min");
                        template = template.replaceAll("\\{TIME_ZONE\\}", durationTitle);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (ticket.getSpace() != null && ticket.getSpace().length() > 0) {
            template = template.replaceAll("\\{SPACE\\}", ticket.getSpace() + "");
        } else {
            template = template.replaceAll("\\{SPACE\\}", "");
        }

        template = template.replaceAll("\\{ELAPSED\\}", "");
        template = template.replaceAll("\\{TIME_ZONE\\}", "");
        template = template.replaceAll("\\{COPY_MSG\\}", "");
        template = template.replaceAll("\\{CUST_MSG1\\}", "");
        template = template.replaceAll("\\{CUST_MSG2\\}", "");
        template = template.replaceAll("\\{CUST_MSG3\\}", "");
        template = template.replaceAll("\\{PERMIT_EXPIRE\\}", "");
        template = template.replaceAll("null", "");

        return template;
    }

    public static String parseSpecialActivity(String templateHTML, ArrayList<SpecialActivityReport> specialActivityReports) {
        TPApplication TPApp = TPApplication.getInstance();
        StringBuffer ticketHTML = new StringBuffer();
        String template;
        final ArrayList<SpecialActivityReport> specialActivityReports1 = specialActivityReports;
        for (SpecialActivityReport report : specialActivityReports) {
            template = templateHTML;
            template = template.replaceAll("\\{REPORT_ID\\}", report.getReportId() + "");
            template = template.replaceAll("\\{ACTIVITY_ID\\}", report.getActivityId() + "");
            template = template.replaceAll("\\{ACTIVITY_NAME\\}", report.getActivityName() + "");
            template = template.replaceAll("\\{CASE_NUMBER\\}", report.getCaseNumber() + "");
            template = template.replaceAll("\\{START_DATE\\}", report.getStartDate() + "");
            template = template.replaceAll("\\{END_DATE\\}", report.getEndDate() + "");
            template = template.replaceAll("\\{ADDRESS\\}", report.getStreetAddress() + "");
            template = template.replaceAll("\\{NOTE\\}", report.getNotes() + "");
            template = template.replaceAll("\\{DATE\\}", report.getCreatedDate() + "");
            template = template.replaceAll("\\{DURATION\\}", report.getDuration() + "");
            template = template.replaceAll("null", "");
            User userInfo = User.getUserInfo(TPApplication.getInstance().userId);
            if (userInfo != null) {
                template = template.replaceAll("\\{BADGE\\}", userInfo.getBadge() + "");
                template = template.replaceAll("\\{DEPT\\}", userInfo.getDepartment() + "");
                template = template.replaceAll("\\{FIRST_NAME\\}", userInfo.getFirstName() + "");
                template = template.replaceAll("\\{LAST_NAME\\}", userInfo.getLastName() + "");
            } else {
                template = template.replaceAll("\\{BADGE\\}", "");
                template = template.replaceAll("\\{DEPT\\}", "");
                template = template.replaceAll("\\{FIRST_NAME\\}", "");
                template = template.replaceAll("\\{LAST_NAME\\}", "");
            }
            ticketHTML.append(template);

        }
        //template = template.replaceAll("\\{TOTAL\\}", report.getTotalDuration() + "");
        return ticketHTML.toString();

    }

    public static String parseTicketViolations(String templateHTML, Ticket ticket) {
        TPApplication TPApp = TPApplication.getInstance();
        DecimalFormat dec = new DecimalFormat("0.00");
        dec.setRoundingMode(RoundingMode.DOWN);

        StringBuffer ticketHTML = new StringBuffer();

        long citationNumber = ticket.getCitationNumber();
        for (TicketViolation violation : ticket.getTicketViolations()) {
            try {
                String template = templateHTML;
                template = template.replaceAll("\\{CITE\\}", TPUtility.prefixZeros(citationNumber, 8));
                template = template.replaceAll("\\{DATE\\}", DateUtil.getStringFromDate(ticket.getTicketDate()) + "");
                template = template.replaceAll("\\{CITE_DATE\\}",
                        DateUtil.getDateStringFromDate(ticket.getTicketDate()) + "");
                template = template.replaceAll("\\{CITE_TIME\\}",
                        DateUtil.getTimeStringFromDate(ticket.getTicketDate()) + "");
                template = template.replaceAll("\\{METER\\}", ticket.getMeter() + "");
                template = template.replaceAll("\\{LOCATION\\}", TPUtility.getFullAddress(ticket) + "");
                template = template.replaceAll("\\{VIOLATION\\}",
                        TPUtility.escapeSpecialChars(violation.getViolationDesc()));
                template = template.replaceAll("\\{PLATE\\}", ticket.getPlate() + "");
                template = template.replaceAll("\\{EXPDATE\\}", ticket.getExpiration() + "");
                template = template.replaceAll("\\{VIN\\}", ticket.getVin() + "");
                template = template.replaceAll("\\{STATE_CODE\\}", ticket.getStateCode() + "");
                template = template.replaceAll("\\{MAKE_CODE\\}", ticket.getMakeCode() + "");
                template = template.replaceAll("\\{BODY_CODE\\}", ticket.getBodyCode() + "");
                template = template.replaceAll("\\{COLOR_CODE\\}", ticket.getColorCode() + "");

                if (ticket.getStateCode() != null && !ticket.getStateCode().equals("")) {
                    State state = State.getStateByName(ticket.getStateCode());
                    if (state != null)
                        template = template.replaceAll("\\{STATE\\}", state.getTitle());
                }
                template = template.replaceAll("\\{STATE\\}", "");

                if (ticket.getMakeCode() != null && !ticket.getMakeCode().equals("")) {
                    MakeAndModel make = MakeAndModel.getMakeByCode(ticket.getMakeCode());
                    if (make != null)
                        template = template.replaceAll("\\{MAKE\\}", make.getMakeTitle());
                }
                template = template.replaceAll("\\{MAKE\\}", "");

                if (ticket.getBodyCode() != null && !ticket.getBodyCode().equals("")) {
                    Body body = Body.getBodyByCode(ticket.getBodyCode());
                    if (body != null)
                        template = template.replaceAll("\\{BODY\\}", body.getTitle());
                }
                template = template.replaceAll("\\{BODY\\}", "");

                if (ticket.getColorCode() != null && !ticket.getColorCode().equals("")) {
                    Color color = Color.getColorByCode(ticket.getColorCode());
                    if (color != null)
                        template = template.replaceAll("\\{COLOR\\}", color.getTitle());
                }
                template = template.replaceAll("\\{COLOR\\}", "");

                template = template.replaceAll("\\{PERMIT\\}", ticket.getPermit() + "");
                template = template.replaceAll("\\{VIOLATION_CODE\\}", violation.getViolationCode() + "");
                template = template.replaceAll("\\{FULL_LOC\\}", TPUtility.getFullAddress(ticket));
                template = template.replaceAll("\\{LOC_BLOCK\\}", ticket.getStreetNumber());
                template = template.replaceAll("\\{LOC_DIRECTION\\}", ticket.getDirection());
                template = template.replaceAll("\\{LOC_PREFIX\\}", ticket.getStreetPrefix());
                template = template.replaceAll("\\{LOC_SUFFIX\\}", ticket.getStreetSuffix());
                template = template.replaceAll("\\{LONG_LAT\\}", ticket.getLongitude() + " - " + ticket.getLatitude());
                template = template.replaceAll("\\{LONG\\}", ticket.getLongitude());
                template = template.replaceAll("\\{LAT\\}", ticket.getLatitude());
                template = template.replaceAll("\\{USERID\\}", ticket.getUserId() + "");
                template = template.replaceAll("\\{MARKED\\}", DateUtil.getStringFromDate(ticket.getTimeMarked()) + "");
                template = template.replaceAll("\\{MARKED_DATE\\}",
                        DateUtil.getDateStringFromDate(ticket.getTimeMarked()) + "");
                template = template.replaceAll("\\{MARKED_TIME\\}",
                        DateUtil.getTimeStringFromDate(ticket.getTimeMarked()) + "");

                String agencyCode = "";
                String webAddress = "";
                CustomerInfo custInfo = CustomerInfo.getCustomerInfo(ticket.getCustId());
                if (custInfo != null) {
                    agencyCode = custInfo.getAgencyCode();
                    webAddress = custInfo.getWebAddress();
                }

                template = template.replaceAll("\\{AGENCY_CODE\\}", agencyCode);
                template = template.replaceAll("\\{WEBADDRESS\\}", webAddress);

                if (violation.isWarning() || ticket.isVoided()) {
                    template = template.replaceAll("\\{FINE\\}", "\\$0");
                } else {
                    template = template.replaceAll("\\{FINE\\}", "\\$" + dec.format(violation.getFine()));
                }

                ArrayList<TicketComment> ticketComments = TPUtility.getPrintableComments(violation.getTicketComments());
                if (ticketComments.size() > 0) {
                    template = template.replaceAll("\\{COMMENTS\\}",
                            TPUtility.escapeSpecialChars(ticketComments.get(0).getComment()));
                    template = template.replaceAll("\\{COMMENT1\\}",
                            TPUtility.escapeSpecialChars(ticketComments.get(0).getComment()));

                    if (ticketComments.size() > 1)
                        template = template.replaceAll("\\{COMMENT2\\}",
                                TPUtility.escapeSpecialChars(ticketComments.get(1).getComment()));
                }

                template = template.replaceAll("\\{COMMENTS\\}", "");
                template = template.replaceAll("\\{COMMENT1\\}", "");
                template = template.replaceAll("\\{COMMENT2\\}", "");

                if (ticket.isVoided()) {
                    String voidMsg = PrintMacro.getPrintMacroMessageByName("VOIDMSG");
                    template = template.replaceAll("\\{VOIDMSG\\}", voidMsg);
                } else {
                    template = template.replaceAll("\\{VOIDMSG\\}", "");
                }

                if (violation.isWarning()) {
                    String warnMsg = PrintMacro.getPrintMacroMessageByName("WARNMSG");
                    template = template.replaceAll("\\{WARNMSG\\}", warnMsg);
                } else {
                    template = template.replaceAll("\\{WARNMSG\\}", "");
                }

                if (ticket.getTicketPictures().size() > 0) {
                    String photoMsg = PrintMacro.getPrintMacroMessageByName("PHOTOMSG");
                    template = template.replaceAll("\\{PHOTOMSG\\}", photoMsg);
                } else {
                    template = template.replaceAll("\\{PHOTOMSG\\}", "");
                }

                Duty duty = Duty.getDutyById(ticket.getDutyId());
                if (duty != null)
                    template = template.replaceAll("\\{DUTY\\}", duty.getTitle());
                else
                    template = template.replaceAll("\\{DUTY\\}", "");

                if (ticket.getExpiration() != null && ticket.getExpiration().contains("/")) {
                    try {
                        String[] expArray = ticket.getExpiration().split("/");
                        template = template.replaceAll("\\{EXP_MONTH\\}", expArray[0]);
                        template = template.replaceAll("\\{EXP_YEAR\\}", expArray[1]);
                    } catch (Exception e) {
                    }
                }

                template = template.replaceAll("\\{EXP_MONTH\\}", "");
                template = template.replaceAll("\\{EXP_YEAR\\}", "");

                User userInfo = User.getUserInfo(ticket.getUserId());
                if (userInfo != null) {
                    template = template.replaceAll("\\{BADGE\\}", userInfo.getBadge() + "");
                    template = template.replaceAll("\\{DEPT\\}", userInfo.getDepartment() + "");
                    template = template.replaceAll("\\{FIRST_NAME\\}", userInfo.getFirstName() + "");
                    template = template.replaceAll("\\{LAST_NAME\\}", userInfo.getLastName() + "");
                } else {
                    template = template.replaceAll("\\{BADGE\\}", "");
                    template = template.replaceAll("\\{DEPT\\}", "");
                    template = template.replaceAll("\\{FIRST_NAME\\}", "");
                    template = template.replaceAll("\\{LAST_NAME\\}", "");
                }

                if (ticket.isChalked() && ticket.getChalkId() > 0) {
                    if (ticket.getTimeZone() != null && ticket.getElapsed() != null) {
                        template = template.replaceAll("\\{TIME_ZONE\\}", ticket.getTimeZone());
                        template = template.replaceAll("\\{ELAPSED\\}", ticket.getElapsed());
                    } else {
                        try {
                            ChalkVehicle chalk = ChalkVehicle.getChalkVehicleById(ticket.getChalkId());
                            if (chalk != null) {
                                Date dt = ticket.getTicketDate();
                                if (dt == null)
                                    dt = new Date();

                                String durationTitle = Duration.getDurationTitleById(chalk.getDurationId());
                                long milliseconds = (ticket.getTicketDate().getTime() - chalk.getChalkDate().getTime());
                                int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                                int hours = (int) ((milliseconds / (1000 * 60 * 60)));
                                String hrs = (hours < 10) ? ("0" + hours) : hours + "";
                                String mins = (minutes < 10) ? ("0" + minutes) : minutes + "";
                                template = template.replaceAll("\\{ELAPSED\\}", hrs + ":" + mins + " hrs/min");
                                template = template.replaceAll("\\{TIME_ZONE\\}", durationTitle);
                            }
                        } catch (Exception e) {
                        }
                    }
                }

                if (ticket.getSpace() != null && ticket.getSpace().length() > 0) {
                    template = template.replaceAll("\\{SPACE\\}", ticket.getSpace() + ""); // Space
                    // From
                    // Chalk
                    // Vehicle
                } else {
                    template = template.replaceAll("\\{SPACE\\}", "");
                }

                template = template.replaceAll("\\{ELAPSED\\}", "");
                template = template.replaceAll("\\{TIME_ZONE\\}", "");

                // Apply Macros
                try {
                    ArrayList<PrintMacro> macros = PrintMacro.getPrintMacros();
                    for (PrintMacro macro : macros) {
                        template = template.replaceAll("\\{" + macro.getMacroName() + "\\}", macro.getMessage());
                    }
                } catch (Exception e) {
                }

                if (ticket.getTicketPictures().size() > 0) {
                    if (ticket.isLPR()) {
                        for (int i = 0; i < ticket.getTicketPictures().size(); i++) {
                            TicketPicture pic = ticket.getTicketPictures().get(i);
                            if (pic.getImagePath().contains("LPR")) {
                                template = template.replaceAll("\\{LPR_IMAGE" + (i + 1) + "\\}",
                                        "<img src=\"file://" + ticket.getTicketPictures().get(i).getImagePath() + "\"/>");
                            }
                        }
                        //"<img src=\"" + Uri.fromFile(new File(ticket.getTicketPictures().get(0).getImagePath())) + "\"/>");
                    }
                    boolean u4524ImageIncluded = false;
                    for (int i = 0; i < ticket.getTicketPictures().size(); i++) {
                        TicketPicture pic = ticket.getTicketPictures().get(i);
                        if (pic.getImagePath().contains("U"+TPApp.userId ) && !u4524ImageIncluded) {
                            // Add the U4524 image only once
                            template = template.replaceAll("\\{TICKET_IMAGE" + (i + 1) + "\\}",
                                    "<img src=\"file://" + pic.getImagePath() + "\"/>");
                            u4524ImageIncluded = true; // Set the flag to true after adding the image
                        } else if (!pic.getImagePath().contains("LPR") && !pic.getImagePath().contains("U4524")) {
                            // Add the other images as normal, except for "LPR" and "U4524"
                            template = template.replaceAll("\\{TICKET_IMAGE" + (i + 1) + "\\}",
                                    "<img src=\"file://" + pic.getImagePath() + "\"/>");
                        }
                    }
                }

                template = template.replaceAll("\\{QRCODE\\}", "<div class=\"QRCODE\" data-agencycode=\"" + agencyCode
                        + "\">" + TPUtility.prefixZeros(citationNumber, 8) + "</div>");
                template = template.replaceAll("\\{BARCODE\\}",
                        "<div class=\"BARCODE\">" + TPUtility.prefixZeros(citationNumber, 8) + "</div>");

                // Empty Tokens
                //template = template.replaceAll("\\{LPR_IMAGE\\}", "");
                //template = template.replaceAll("\\{TICKET_IMAGE1\\}", "");
                // template = template.replaceAll("\\{TICKET_IMAGE2\\}", "");
                // template = template.replaceAll("\\{TICKET_IMAGE3\\}", "");
                // template = template.replaceAll("\\{TICKET_IMAGE4\\}", "");
                //template = template.replaceAll("\\{TICKET_IMAGE5\\}", "");
                // template = template.replaceAll("\\{TICKET_IMAGE6\\}", "");
                // Empty Tokens
                template = template.replaceAll("\\{LPR_IMAGE1\\}", "");
                template = template.replaceAll("\\{LPR_IMAGE2\\}", "");
                template = template.replaceAll("\\{LPR_IMAGE3\\}", "");
                template = template.replaceAll("\\{LPR_IMAGE4\\}", "");
                template = template.replaceAll("\\{TICKET_IMAGE1\\}", "");
                template = template.replaceAll("\\{TICKET_IMAGE2\\}", "");
                template = template.replaceAll("\\{TICKET_IMAGE3\\}", "");
                template = template.replaceAll("\\{TICKET_IMAGE4\\}", "");
                template = template.replaceAll("\\{TICKET_IMAGE5\\}", "");
                template = template.replaceAll("\\{TICKET_IMAGE6\\}", "");
                template = template.replaceAll("\\{TICKET_IMAGE7\\}", "");
                template = template.replaceAll("\\{TICKET_IMAGE8\\}", "");
                template = template.replaceAll("\\{TICKET_IMAGE9\\}", "");
                template = template.replaceAll("\\{TICKET_IMAGE10\\}", "");

                template = template.replaceAll("\\{COPY_MSG\\}", "");
                template = template.replaceAll("\\{CUST_MSG1\\}", "");
                template = template.replaceAll("\\{CUST_MSG2\\}", "");
                template = template.replaceAll("\\{CUST_MSG3\\}", "");
                template = template.replaceAll("\\{PERMIT_EXPIRE\\}", "");

                if (custInfo != null) {
                    template = template.replaceAll("\\{CUSTOMER\\}", custInfo.getName());
                    template = template.replaceAll("\\{CUST_ADDRESS\\}", custInfo.getAddress());
                    template = template.replaceAll("\\{CUST_PHONE\\}", custInfo.getContactNumber());
                } else {
                    template = template.replaceAll("\\{CUSTOMER\\}", "");
                    template = template.replaceAll("\\{CUST_ADDRESS\\}", "");
                    template = template.replaceAll("\\{CUST_PHONE\\}", "");
                }

                template = template.replaceAll("null", "");
                ticketHTML.append(template);

                citationNumber++;
            } catch (Exception e) {
            }
        }

        return ticketHTML.toString();
    }

    public static String parseTicket(String templateHTML, Ticket ticket) {

        TPApplication TPApp = TPApplication.getInstance();
        DecimalFormat dec = new DecimalFormat("0.00");
        dec.setRoundingMode(RoundingMode.DOWN);

        StringBuffer ticketHTML = new StringBuffer();

        try {
            String template = templateHTML;
            template = template.replaceAll("\\{CITE\\}", TPUtility.prefixZeros(ticket.getCitationNumber(), 8));
            template = template.replaceAll("\\{DATE\\}", DateUtil.getStringFromDate(ticket.getTicketDate()) + "");
            template = template.replaceAll("\\{CITE_DATE\\}",
                    DateUtil.getDateStringFromDate(ticket.getTicketDate()) + "");
            template = template.replaceAll("\\{CITE_TIME\\}",
                    DateUtil.getTimeStringFromDate(ticket.getTicketDate()) + "");
            template = template.replaceAll("\\{METER\\}", ticket.getMeter() + "");
            template = template.replaceAll("\\{LOCATION\\}", TPUtility.getFullAddress(ticket) + "");
            template = template.replaceAll("\\{VIOLATION\\}", TPUtility.escapeSpecialChars(ticket.getViolation()));
            template = template.replaceAll("\\{PLATE\\}", ticket.getPlate() + "");
            template = template.replaceAll("\\{EXPDATE\\}", ticket.getExpiration() + "");
            template = template.replaceAll("\\{VIN\\}", ticket.getVin() + "");
            template = template.replaceAll("\\{STATE_CODE\\}", ticket.getStateCode() + "");
            template = template.replaceAll("\\{MAKE_CODE\\}", ticket.getMakeCode() + "");
            template = template.replaceAll("\\{BODY_CODE\\}", ticket.getBodyCode() + "");
            template = template.replaceAll("\\{COLOR_CODE\\}", ticket.getColorCode() + "");

            if (ticket.getStateCode() != null && !ticket.getStateCode().equals("")) {
                State state = State.getStateByName(ticket.getStateCode());
                if (state != null)
                    template = template.replaceAll("\\{STATE\\}", state.getTitle());
            }
            template = template.replaceAll("\\{STATE\\}", "");

            if (ticket.getMakeCode() != null && !ticket.getMakeCode().equals("")) {
                MakeAndModel make = MakeAndModel.getMakeByCode(ticket.getMakeCode());
                if (make != null)
                    template = template.replaceAll("\\{MAKE\\}", make.getMakeTitle());
            }
            template = template.replaceAll("\\{MAKE\\}", "");

            if (ticket.getBodyCode() != null && !ticket.getBodyCode().equals("")) {
                Body body = Body.getBodyByCode(ticket.getBodyCode());
                if (body != null)
                    template = template.replaceAll("\\{BODY\\}", body.getTitle());
            }
            template = template.replaceAll("\\{BODY\\}", "");

            if (ticket.getColorCode() != null && !ticket.getColorCode().equals("")) {
                Color color = Color.getColorByCode(ticket.getColorCode());
                if (color != null)
                    template = template.replaceAll("\\{COLOR\\}", color.getTitle());
            }
            template = template.replaceAll("\\{COLOR\\}", "");

            template = template.replaceAll("\\{PERMIT\\}", ticket.getPermit() + "");
            template = template.replaceAll("\\{VIOLATION_CODE\\}", ticket.getViolationCode() + "");
            template = template.replaceAll("\\{FULL_LOC\\}", TPUtility.getFullAddress(ticket));
            template = template.replaceAll("\\{LOC_BLOCK\\}", ticket.getStreetNumber());
            template = template.replaceAll("\\{LOC_DIRECTION\\}", ticket.getDirection());
            template = template.replaceAll("\\{LOC_PREFIX\\}", ticket.getStreetPrefix());
            template = template.replaceAll("\\{LOC_SUFFIX\\}", ticket.getStreetSuffix());
            template = template.replaceAll("\\{LONG_LAT\\}", ticket.getLongitude() + " - " + ticket.getLatitude());
            template = template.replaceAll("\\{LONG\\}", ticket.getLongitude());
            template = template.replaceAll("\\{LAT\\}", ticket.getLatitude());
            template = template.replaceAll("\\{USERID\\}", ticket.getUserId() + "");
            template = template.replaceAll("\\{MARKED\\}", DateUtil.getStringFromDate(ticket.getTimeMarked()) + "");
            template = template.replaceAll("\\{MARKED_DATE\\}",
                    DateUtil.getDateStringFromDate(ticket.getTimeMarked()) + "");
            template = template.replaceAll("\\{MARKED_TIME\\}",
                    DateUtil.getTimeStringFromDate(ticket.getTimeMarked()) + "");

            String agencyCode = "";
            String webAddress = "";
            CustomerInfo custInfo = CustomerInfo.getCustomerInfo(ticket.getCustId());

            if (custInfo != null) {
                agencyCode = custInfo.getAgencyCode();
                webAddress = custInfo.getWebAddress();
            }

            template = template.replaceAll("\\{AGENCY_CODE\\}", agencyCode);
            template = template.replaceAll("\\{WEBADDRESS\\}", webAddress);

            if (ticket.isWarn() || ticket.isVoided()) {
                template = template.replaceAll("\\{FINE\\}", "\\$0");
            } else {
                template = template.replaceAll("\\{FINE\\}", "\\$" + dec.format(ticket.getFine()));
            }

            ArrayList<TicketComment> ticketComments = TPUtility
                    .getPrintableComments(ticket.getTicketViolations().get(0).getTicketComments());
            if (ticketComments != null && ticketComments.size() > 0) {
                template = template.replaceAll("\\{COMMENTS\\}",
                        TPUtility.escapeSpecialChars(ticketComments.get(0).getComment()));
                template = template.replaceAll("\\{COMMENT1\\}",
                        TPUtility.escapeSpecialChars(ticketComments.get(0).getComment()));

                if (ticketComments.size() > 1)
                    template = template.replaceAll("\\{COMMENT2\\}",
                            TPUtility.escapeSpecialChars(ticketComments.get(1).getComment()));
            }

            template = template.replaceAll("\\{COMMENTS\\}", "");
            template = template.replaceAll("\\{COMMENT1\\}", "");
            template = template.replaceAll("\\{COMMENT2\\}", "");

            ArrayList<PrintMacro> macros1 = PrintMacro.getPrintMacros();
            if (ticket.isVoided() && !macros1.isEmpty()) {
                String voidMsg = PrintMacro.getPrintMacroMessageByName("VOIDMSG");
                template = template.replaceAll("\\{VOIDMSG\\}", voidMsg);
            } else {
                template = template.replaceAll("\\{VOIDMSG\\}", "");
            }

            if (ticket.isWarn() && !macros1.isEmpty()) {
                String warnMsg = PrintMacro.getPrintMacroMessageByName("WARNMSG");

                // Log the value of warnMsg for debugging
                Log.d("TicketParsing", "WARNMSG: " + warnMsg);

                if (warnMsg != null) {
                    template = template.replaceAll("\\{WARNMSG\\}", warnMsg);
                } else {
                    template = template.replaceAll("\\{WARNMSG\\}", "");
                }
            } else {
                template = template.replaceAll("\\{WARNMSG\\}", "");
            }


            if (!ticket.getTicketPictures().isEmpty() && !macros1.isEmpty()) {
                String photoMsg = PrintMacro.getPrintMacroMessageByName("PHOTOMSG");
                template = template.replaceAll("\\{PHOTOMSG\\}", photoMsg);
            } else {
                template = template.replaceAll("\\{PHOTOMSG\\}", "");
            }

            Duty duty = Duty.getDutyById(ticket.getDutyId());
            if (duty != null)
                template = template.replaceAll("\\{DUTY\\}", duty.getTitle());
            else
                template = template.replaceAll("\\{DUTY\\}", "");

            if (ticket.getExpiration() != null && ticket.getExpiration().contains("/")) {
                try {
                    String[] expArray = ticket.getExpiration().split("/");
                    template = template.replaceAll("\\{EXP_MONTH\\}", expArray[0]);
                    template = template.replaceAll("\\{EXP_YEAR\\}", expArray[1]);
                } catch (Exception e) {
                }
            }

            template = template.replaceAll("\\{EXP_MONTH\\}", "");
            template = template.replaceAll("\\{EXP_YEAR\\}", "");

            User userInfo = User.getUserInfo(ticket.getUserId());
            if (userInfo != null) {
                template = template.replaceAll("\\{BADGE\\}", userInfo.getBadge() + "");
                template = template.replaceAll("\\{DEPT\\}", userInfo.getDepartment() + "");
                template = template.replaceAll("\\{FIRST_NAME\\}", userInfo.getFirstName() + "");
                template = template.replaceAll("\\{LAST_NAME\\}", userInfo.getLastName() + "");
            } else {
                template = template.replaceAll("\\{BADGE\\}", "");
                template = template.replaceAll("\\{DEPT\\}", "");
                template = template.replaceAll("\\{FIRST_NAME\\}", "");
                template = template.replaceAll("\\{LAST_NAME\\}", "");
            }

            if (ticket.isChalked() && ticket.getChalkId() > 0) {
                if (ticket.getTimeZone() != null && ticket.getElapsed() != null) {
                    template = template.replaceAll("\\{TIME_ZONE\\}", ticket.getTimeZone());
                    template = template.replaceAll("\\{ELAPSED\\}", ticket.getElapsed());
                } else {
                    try {
                        ChalkVehicle chalk = ChalkVehicle.getChalkVehicleById(ticket.getChalkId());
                        if (chalk != null) {
                            Date dt = ticket.getTicketDate();
                            if (dt == null) {
                                dt = new Date();
                            }

                            String durationTitle = Duration.getDurationTitleById(chalk.getDurationId());
                            long milliseconds = (ticket.getTicketDate().getTime() - chalk.getChalkDate().getTime());
                            int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                            int hours = (int) ((milliseconds / (1000 * 60 * 60)));
                            String hrs = (hours < 10) ? ("0" + hours) : hours + "";
                            String mins = (minutes < 10) ? ("0" + minutes) : minutes + "";
                            template = template.replaceAll("\\{ELAPSED\\}", hrs + ":" + mins + " hrs/min");
                            template = template.replaceAll("\\{TIME_ZONE\\}", durationTitle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (ticket.getSpace() != null && ticket.getSpace().length() > 0) {
                template = template.replaceAll("\\{SPACE\\}", ticket.getSpace() + ""); // Space
                // From
                // Chalk
                // Vehicle
            } else {
                template = template.replaceAll("\\{SPACE\\}", "");
            }

            template = template.replaceAll("\\{ELAPSED\\}", "");
            template = template.replaceAll("\\{TIME_ZONE\\}", "");

            // Apply Macros
            try {
                ArrayList<PrintMacro> macros = PrintMacro.getPrintMacros();
                for (PrintMacro macro : macros) {
                    template = template.replaceAll("\\{" + macro.getMacroName() + "\\}", macro.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!ticket.getTicketPictures().isEmpty()) {
                if (ticket.isLPR()) {
                    for (int i = 0; i < ticket.getTicketPictures().size(); i++) {
                        TicketPicture pic = ticket.getTicketPictures().get(i);
                        if (pic.getImagePath().contains("LPR")) {
                            template = template.replaceAll("\\{LPR_IMAGE" + (i + 1) + "\\}",
                                    "<img src=\"file://" + ticket.getTicketPictures().get(i).getImagePath() + "\"/>");
                        }
                    }
                    //"<img src=\"" + Uri.fromFile(new File(ticket.getTicketPictures().get(0).getImagePath())) + "\"/>");
                }

                for (int i = 0; i < ticket.getTicketPictures().size(); i++) {
                    TicketPicture pic = ticket.getTicketPictures().get(i);
                    if (!pic.getImagePath().contains("LPR")) {
                        template = template.replaceAll("\\{TICKET_IMAGE" + (i + 1) + "\\}",
                                "<img src=\"file://" + pic.getImagePath() + "\"/>");
                    }
                }
            }

            template = template.replaceAll("\\{QRCODE\\}", "<div class=\"QRCODE\" data-agencycode=\"" + agencyCode
                    + "\">" + TPUtility.prefixZeros(ticket.getCitationNumber(), 8) + "</div>");
            template = template.replaceAll("\\{BARCODE\\}",
                    "<div class=\"BARCODE\">" + TPUtility.prefixZeros(ticket.getCitationNumber(), 8) + "</div>");

            // Empty Tokens
            template = template.replaceAll("\\{LPR_IMAGE1\\}", "");
            template = template.replaceAll("\\{LPR_IMAGE2\\}", "");
            template = template.replaceAll("\\{LPR_IMAGE3\\}", "");
            template = template.replaceAll("\\{LPR_IMAGE4\\}", "");
            template = template.replaceAll("\\{TICKET_IMAGE1\\}", "");
            template = template.replaceAll("\\{TICKET_IMAGE2\\}", "");
            template = template.replaceAll("\\{TICKET_IMAGE3\\}", "");
            template = template.replaceAll("\\{TICKET_IMAGE4\\}", "");
            template = template.replaceAll("\\{TICKET_IMAGE5\\}", "");
            template = template.replaceAll("\\{TICKET_IMAGE6\\}", "");
            template = template.replaceAll("\\{TICKET_IMAGE7\\}", "");
            template = template.replaceAll("\\{TICKET_IMAGE8\\}", "");
            template = template.replaceAll("\\{TICKET_IMAGE9\\}", "");
            template = template.replaceAll("\\{TICKET_IMAGE10\\}", "");

            template = template.replaceAll("\\{COPY_MSG\\}", "");
            template = template.replaceAll("\\{CUST_MSG1\\}", "");
            template = template.replaceAll("\\{CUST_MSG2\\}", "");
            template = template.replaceAll("\\{CUST_MSG3\\}", "");
            template = template.replaceAll("\\{PERMIT_EXPIRE\\}", "");

            if (custInfo != null) {
                template = template.replaceAll("\\{CUSTOMER\\}", custInfo.getName());
                template = template.replaceAll("\\{CUST_ADDRESS\\}", custInfo.getAddress());
                template = template.replaceAll("\\{CUST_PHONE\\}", custInfo.getContactNumber());
            } else {
                template = template.replaceAll("\\{CUSTOMER\\}", "");
                template = template.replaceAll("\\{CUST_ADDRESS\\}", "");
                template = template.replaceAll("\\{CUST_PHONE\\}", "");
            }

            template = template.replaceAll("null", "");
            ticketHTML.append(template);

        } catch (Exception e) {
            Log.e("TicketPRO", "Ticket Parsing Error " + TPUtility.getPrintStackTrace(e));
        }

        return ticketHTML.toString();
    }

    public static String getTowNotifyEmail(Context context, String tickets, String amounts) {
        try {
            TPApplication TPApp = (TPApplication) context.getApplicationContext();
            Ticket activeTicket = TPApp.getActiveTicket();
            String emailTemplate = TPUtility.getEmailTemplate(context);
            emailTemplate = TPUtility.parseTicketTokens(emailTemplate, activeTicket);
            emailTemplate = emailTemplate.replaceAll("\\{EXP\\}", activeTicket.getExpiration() + "");
            emailTemplate = emailTemplate.replaceAll("\\{DATE_TIME\\}", DateUtil.getCurrentDateTime() + "");
            emailTemplate = emailTemplate.replaceAll("\\{TICKETCOUNT\\}", tickets + "");
            emailTemplate = emailTemplate.replaceAll("\\{AMTOWED\\}", "\\" + amounts + "");

            if (TPApp.getCustomerInfo() != null) {
                emailTemplate = emailTemplate.replaceAll("\\{CUSTNAME\\}", TPApp.getCustomerInfo().getName() + "");
            } else {
                emailTemplate = emailTemplate.replaceAll("\\{CUSTNAME\\}", "");
            }

            if (activeTicket.getLatitude() != null && activeTicket.getLongitude() != null) {
                emailTemplate = emailTemplate.replaceAll("\\{MAPLINK\\}", "http://maps.google.com/maps?q="
                        + activeTicket.getLatitude() + "," + activeTicket.getLongitude());

                emailTemplate = emailTemplate.replaceAll("\\{MAPIMG\\}",
                        "<img src='http://maps.google.com/maps/api/staticmap?center=" + activeTicket.getLatitude() + ","
                                + activeTicket.getLongitude() + "&zoom=15&size=200x200&sensor=false'/>");

            } else {
                emailTemplate = emailTemplate.replaceAll("\\{MAPLINK\\}", "");
                emailTemplate = emailTemplate.replaceAll("\\{MAPIMG\\}", "");
            }

            if (activeTicket.getTicketPictures().size() > 0 && emailTemplate.contains("PHOTO1")) {
                TicketPicture picture = activeTicket.getTicketPictures().get(0);
                Bitmap bitmap = BitmapFactory.decodeFile(picture.getImagePath());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(CompressFormat.PNG, 0, bos);
                byte[] bitmapdata = bos.toByteArray();
                String dataToUpload = Base64.encodeToString(bitmapdata, Base64.DEFAULT);
                emailTemplate = emailTemplate.replaceAll("\\{PHOTO1\\}", "<img src='" + dataToUpload + "'>");
            } else {
                emailTemplate = emailTemplate.replaceAll("\\{PHOTO1\\}", "");
            }

            return emailTemplate;

        } catch (Exception e) {
            Toast.makeText(context, "Error sending emails.", Toast.LENGTH_LONG).show();
            Log.e(TAG, TPUtility.getPrintStackTrace(e));
        }

        return "";
    }

    public static String getMaintenancePreviewTemplate(Context context) {
        String html = "";
        try {
            PrintTemplate template = PrintTemplate.getPrintTemplateByName("MaintenancePreview");
            if (template != null) {
                String printTemplate = template.getTemplateData();
                return printTemplate;
            }

            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open("maintenanceTemplate.html");
            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            html = new String(buffer);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return html;
    }

    public static String getMaintenanceEmail(Context context, MaintenanceLog maintenanceLog) {
        try {
            String maintenanceTemplate = TPUtility.getMaintenancePreviewTemplate(context);
            String template = maintenanceTemplate;

            try {
                template = template.replaceAll("\\{ITEM_NAME\\}", StringUtil.getDisplayString(maintenanceLog.getItemName()));
                template = template.replaceAll("\\{LOG_DATE\\}", DateUtil.getStringFromDate2(maintenanceLog.getLogDate()));
                template = template.replaceAll("\\{PROBLEM_TYPE\\}", StringUtil.getDisplayString(maintenanceLog.getProblemType()));
                template = template.replaceAll("\\{COMMENTS\\}", StringUtil.getDisplayString(maintenanceLog.getComments()));
                template = template.replaceAll("\\{LOCATION\\}", StringUtil.getDisplayString(maintenanceLog.getLocation()));

                if (!StringUtil.isEmpty(maintenanceLog.getLatitude())
                        && !StringUtil.isEmpty(maintenanceLog.getLongitude())) {

                    template = template.replaceAll("\\{MAPLINK\\}", "http://maps.google.com/maps?q="
                            + maintenanceLog.getLatitude() + "," + maintenanceLog.getLongitude());

                    template = template.replaceAll("\\{MAPIMG\\}",
                            "<img src='http://maps.google.com/maps/api/staticmap?center=" + maintenanceLog.getLatitude() + ","
                                    + maintenanceLog.getLongitude() + "&zoom=15&size=260x260&sensor=false'/>");

                } else {
                    template = template.replaceAll("\\{MAPLINK\\}", "");
                    template = template.replaceAll("\\{MAPIMG\\}", "");
                }

                StringBuffer imageHTML = new StringBuffer();
                for (MaintenancePicture picture : maintenanceLog.getPictures()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(picture.getImagePath());
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(CompressFormat.PNG, 0, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    String dataToUpload = Base64.encodeToString(bitmapdata, Base64.DEFAULT);
                    imageHTML.append("<img src='" + dataToUpload + "'><br/>");
                }

                template = template.replaceAll("\\{IMAGES\\}", imageHTML.toString());

                return template;

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            Toast.makeText(context, "Error sending emails.", Toast.LENGTH_LONG).show();
            Log.e(TAG, TPUtility.getPrintStackTrace(e));
        }

        return "";
    }

    public static String getSupportEmail(Context context, Ticket activeTicket) {
        try {
            TPApplication TPApp = (TPApplication) context.getApplicationContext();
            ArrayList<Contact> contacts = Contact.getSupportContacts();
            if (contacts.size() == 0) {
                return "";
            }

            String[] emails = new String[contacts.size()];
            for (int i = 0; i < contacts.size(); i++) {
                emails[i] = contacts.get(i).getEmailId();
            }

            String emailTemplate = TPUtility.getSupportEmailTemplate(context);
            emailTemplate = TPUtility.parseTicketTokens(emailTemplate, activeTicket);
            emailTemplate = emailTemplate.replaceAll("\\{EXP\\}",
                    StringUtil.getDisplayString(activeTicket.getExpiration()));
            emailTemplate = emailTemplate.replaceAll("\\{DATE_TIME\\}",
                    StringUtil.getDisplayString(DateUtil.getCurrentDateTime()));

            if (TPApp.getCustomerInfo() != null) {
                emailTemplate = emailTemplate.replaceAll("\\{CUSTNAME\\}",
                        StringUtil.getDisplayString(TPApp.getCustomerInfo().getName()));
            } else {
                emailTemplate = emailTemplate.replaceAll("\\{CUSTNAME\\}", "");
            }

            if (activeTicket.getLatitude() != null && activeTicket.getLongitude() != null) {
                emailTemplate = emailTemplate.replaceAll("\\{MAPLINK\\}", "http://maps.google.com/maps?q="
                        + activeTicket.getLatitude() + "," + activeTicket.getLongitude());

                emailTemplate = emailTemplate.replaceAll("\\{MAPIMG\\}",
                        "<img src='http://maps.google.com/maps/api/staticmap?center=" + activeTicket.getLatitude() + ","
                                + activeTicket.getLongitude() + "&zoom=15&size=260x260&sensor=false'/>");

            } else {
                emailTemplate = emailTemplate.replaceAll("\\{MAPLINK\\}", "");
                emailTemplate = emailTemplate.replaceAll("\\{MAPIMG\\}", "");
            }

            if (activeTicket.getTicketPictures().size() > 0 && emailTemplate.contains("PHOTO1")) {
                TicketPicture picture = activeTicket.getTicketPictures().get(0);
                Bitmap bitmap = BitmapFactory.decodeFile(picture.getImagePath());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(CompressFormat.PNG, 0, bos);
                byte[] bitmapdata = bos.toByteArray();
                String dataToUpload = Base64.encodeToString(bitmapdata, Base64.DEFAULT);
                emailTemplate = emailTemplate.replaceAll("\\{PHOTO1\\}", "<img src='" + dataToUpload + "'>");
            } else {
                emailTemplate = emailTemplate.replaceAll("\\{PHOTO1\\}", "");
            }

            return emailTemplate;
        } catch (Exception e) {
            Toast.makeText(context, "Error sending emails.", Toast.LENGTH_LONG).show();
            Log.e(TAG, TPUtility.getPrintStackTrace(e));
        }

        return "";
    }

    public static String getEmailTemplate(Context context) {
        PrintTemplate template;
        try {
            template = PrintTemplate.getPrintTemplateByName("TowAlertEmail");
            if (template != null) {
                return template.getTemplateData();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error " + e.getMessage());
        }

        AssetManager assetManager = context.getAssets();
        InputStream inStream;
        try {
            inStream = assetManager.open("TowAlertTemplate.html");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int nextByte = inStream.read();
            while (nextByte != -1) {
                bos.write(nextByte);
                nextByte = inStream.read();
            }

            return bos.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getSupportEmailTemplate(Context context) {
        PrintTemplate template;
        try {
            template = PrintTemplate.getPrintTemplateByName("Support");
            if (template != null) {
                return template.getTemplateData();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error " + e.getMessage());
        }

        AssetManager assetManager = context.getAssets();
        InputStream inStream;
        try {
            inStream = assetManager.open("Support.html");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int nextByte = inStream.read();
            while (nextByte != -1) {
                bos.write(nextByte);
                nextByte = inStream.read();
            }
            return bos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }

        String externalStorage = TPApplication.getInstance().externalStoragePath;
        if (externalStorage != null && !externalStorage.equals("")) {
            return externalStorage;
        }

        externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        final String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");

        if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
            String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
            if (rawSecondaryStorages.length > 0) {
                try {
                    for (String rawSecondaryStorage : rawSecondaryStorages) {
                        if (rawSecondaryStorage != null && rawSecondaryStorage.toLowerCase().contains("card")) {
                            externalStorage = rawSecondaryStorage;
                            break;
                        }
                    }

                    StatFs stats = new StatFs(externalStorage);
                    int availableBytes = stats.getAvailableBlocks() * stats.getBlockSize();
                    if (availableBytes == 0) {
                        externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
                    }

                } catch (Exception e) {
                    Log.e("TicketPRO", "Error getting external storage information");
                }
            }
        }

        TPApplication.getInstance().externalStoragePath = externalStorage;
        return externalStorage;
    }

    public static String getFullAddress(Address address) {
        if (address == null)
            return "";

        StringBuffer fullAddress = new StringBuffer();
        if (address.getStreetPrefix() != null) {
            fullAddress.append(StringUtil.getDisplayString(address.getStreetPrefix()));
            fullAddress.append(" ");
        }

        if (address.getStreetNumber() != null) {
            fullAddress.append(StringUtil.getDisplayString(address.getStreetNumber()));
            fullAddress.append(" ");
        }

        if (address.getDirection() != null) {
            fullAddress.append(StringUtil.getDisplayString(address.getDirection()));
            fullAddress.append(" ");
        }

        if (address.getLocation() != null) {
            fullAddress.append(StringUtil.getDisplayString(address.getLocation()));
            fullAddress.append(" ");
        }

        if (address.getStreetSuffix() != null) {
            fullAddress.append(StringUtil.getDisplayString(address.getStreetSuffix()));
        }

        return fullAddress.toString().replaceAll("\\s+", " ").trim().toUpperCase();
    }

    public static String getFullAddress(Ticket ticket) {
        if (ticket == null)
            return "";

        Address address = new Address();
        address.setDirection(ticket.getDirection());
        address.setLocation(ticket.getLocation());
        address.setStreetNumber(ticket.getStreetNumber());
        address.setStreetPrefix(ticket.getStreetPrefix());
        address.setStreetSuffix(ticket.getStreetSuffix());

        StringBuilder fullAddress = new StringBuilder();
        if (address.getStreetPrefix() != null) {
            fullAddress.append(StringUtil.getDisplayString(address.getStreetPrefix()));
            fullAddress.append(" ");
        }

        if (address.getStreetNumber() != null) {
            fullAddress.append(StringUtil.getDisplayString(address.getStreetNumber()));
            fullAddress.append(" ");
        }

        if (address.getDirection() != null) {
            fullAddress.append(StringUtil.getDisplayString(address.getDirection()));
            fullAddress.append(" ");
        }

        if (address.getLocation() != null) {
            fullAddress.append(StringUtil.getDisplayString(address.getLocation()));
            fullAddress.append(" ");
        }

        if (address.getStreetSuffix() != null) {
            fullAddress.append(StringUtil.getDisplayString(address.getStreetSuffix()));
        }

        return fullAddress.toString().replaceAll("\\s+", " ").trim().toUpperCase();
    }

    public static String getFullAddress(ChalkVehicle chalk) {
        if (chalk == null)
            return "";

        Address address = new Address();
        address.setDirection(chalk.getDirection());
        address.setLocation(chalk.getLocation());
        address.setStreetNumber(chalk.getStreetNumber());
        address.setStreetPrefix(chalk.getStreetPrefix());
        address.setStreetSuffix(chalk.getStreetSuffix());

        StringBuilder fullAddress = new StringBuilder();
        if (address.getStreetPrefix() != null) {
            fullAddress.append(StringUtil.getDisplayString(address.getStreetPrefix()));
            fullAddress.append(" ");
        }

        if (address.getStreetNumber() != null) {
            fullAddress.append(StringUtil.getDisplayString(address.getStreetNumber()));
            fullAddress.append(" ");
        }

        if (address.getDirection() != null) {
            fullAddress.append(StringUtil.getDisplayString(address.getDirection()));
            fullAddress.append(" ");
        }

        if (address.getLocation() != null) {
            fullAddress.append(StringUtil.getDisplayString(address.getLocation()));
            fullAddress.append(" ");
        }

        if (address.getStreetSuffix() != null) {
            fullAddress.append(StringUtil.getDisplayString(address.getStreetSuffix()));
        }

        return fullAddress.toString().replaceAll("\\s+", " ").trim().toUpperCase();
    }

    public static Address createEmptyAddress() {
        Address address = new Address();
        address.setLocation("");
        address.setStreetNumber("");
        address.setStreetPrefix("");
        address.setStreetSuffix("");
        address.setDirection("");

        return address;
    }

    public static void hideSoftKeyboard(final Activity activity) {
        if (activity==null)
            return;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (imm != null)
                        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100);
    }

    public static void showSoftKeyboard(final Activity activity, EditText inputText) {
        if (inputText == null) {
            return;
        }

        inputText.requestFocus();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }, 100);
    }

    public static void hideSoftKeyboard(final Activity activity, final EditText view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 50);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void removeFile(String filePath) {
        if (filePath == null)
            return;

        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int totalPrivateComments(List<TicketComment> comments) {
        int count = 0;
        for (TicketComment comment : comments) {
            if (comment.isPrivate())
                count++;
        }
        return count;
    }

    public static int totalPublicComments(List<TicketComment> comments) {
        int count = 0;
        for (TicketComment comment : comments) {
            if (!comment.isPrivate())
                count++;
        }

        return count;
    }

    public static void showErrorToast(Activity activity, String message) {
        Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showErrorDialog(Activity activity, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("Alert");
        alert.setCancelable(true);
        alert.setMessage(message);
        alert.setPositiveButton("OK", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        AlertDialog alertdialog = alert.create();
        alertdialog.setCanceledOnTouchOutside(true);
        alertdialog.show();

    }

    public static Toast getErrorToastView(Activity activity, String message) {
        Toast toast = Toast.makeText(activity, message, Toast.LENGTH_LONG);
        toast.show();

        return toast;
    }

    public static String generateSHA1Hash(String str) {
        String hash = null;
        try {
            str = str
                    + "HKDWsJAEQUZ:TGzQTKTSp9wgRpBieORboo8MTUogUYNjlee8A12oNdULy2DkXU4dVUJG11WORHl7j0cQ:6nyyIf567KrWTH9CfDmbGIt2izeupBht7l4AZkgiopa3uCE16JYJhjLKKQ5dhDuA4xKQpdm8OAiLIVoNyBZ1gzjn9bB8m:p:MUP2QneuYsAZ8QT";
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            String hexString = bytesToHex(bytes);
            hash = convertHexToBase64(hexString);

        } catch (Exception e) {
        }

        return hash;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static String convertHexToBase64(String hexString) {
        try {
            byte[] bytes = Hex.decodeHex(hexString.toCharArray());
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (DecoderException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap downloadBitmap(String url) {
        final TPHttpClient client = new TPHttpClient();
        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.e("TPUtility", "Error " + statusCode + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            getRequest.abort();
        }

        return null;
    }

    public static int getBitmapScale(int width, int height, int requiredWidth, int requiredHeight) {
        int inSampleSize = 1;
        if (height > requiredWidth || width > requiredWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= requiredHeight && (halfWidth / inSampleSize) >= requiredWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap resizeBitmap(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }

            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static Date addMinutesToDate(int minutes, Date beforeTime) {
        long curTimeInMs = beforeTime.getTime();
        Date afterAddingMins = new Date(curTimeInMs + (minutes * 60000));

        return afterAddingMins;
    }

    public static boolean isRunningOnEmulator(final Context inContext) throws Exception {
       /* final TelephonyManager telephonyManager = (TelephonyManager) inContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(TPApplication.getInstance(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return false;
        }
        final boolean hasEmulatorImei = "000000000000000".equalsIgnoreCase(telephonyManager.getDeviceId());
        final boolean hasEmulatorModelName = Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK");
        return hasEmulatorImei || hasEmulatorModelName;*/
        return false;
    }

    public static String getSupportEmailSubject(String citationNumber, String plate) {
        String subject = "TPM - {CUSTNAME} - {CITE} - {PLATE}";
        CustomerInfo customer = TPApplication.getInstance().getCustomerInfo();
        subject = subject.replaceAll("\\{CUSTNAME\\}", customer.getName());
        subject = subject.replaceAll("\\{CITE\\}", citationNumber);
        subject = subject.replaceAll("\\{PLATE\\}", plate);
        return subject;
    }

    public static void applyButtonStyles(AlertDialog dialog) {
        Button button;

        button = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (button != null) {
            //button.setMaxWidth(120);
            button.setTextColor(android.graphics.Color.WHITE);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            button.setBackgroundResource(R.drawable.btn_yellow);
        }

        button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (button != null) {
            //button.setMaxWidth(120);
            button.setTextColor(android.graphics.Color.WHITE);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            button.setBackgroundResource(R.drawable.btn_blue);
        }

        button = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        if (button != null) {
            //button.setMaxWidth(120);
            button.setTextColor(android.graphics.Color.WHITE);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            button.setBackgroundResource(R.drawable.btn_yellow);
        }
    }

    public static Bitmap removeMargins(Bitmap bmp, int color) {
        long dtMili = System.currentTimeMillis();
        int MTop = 0, MBot = 0, MLeft = 0, MRight = 0;
        boolean found = false;

        int[] bmpIn = new int[bmp.getWidth() * bmp.getHeight()];
        int[][] bmpInt = new int[bmp.getWidth()][bmp.getHeight()];

        bmp.getPixels(bmpIn, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        for (int ii = 0, contX = 0, contY = 0; ii < bmpIn.length; ii++) {
            bmpInt[contX][contY] = bmpIn[ii];
            contX++;
            if (contX >= bmp.getWidth()) {
                contX = 0;
                contY++;
                if (contY >= bmp.getHeight()) {
                    break;
                }
            }
        }

        for (int hP = 0; hP < bmpInt[0].length && !found; hP++) {
            for (int wP = 0; wP < bmpInt.length && !found; wP++) {
                if (bmpInt[wP][hP] != color) {
                    MTop = hP;
                    found = true;
                    break;
                }
            }
        }
        found = false;

        for (int hP = bmpInt[0].length - 1; hP >= 0 && !found; hP--) {
            for (int wP = 0; wP < bmpInt.length && !found; wP++) {
                if (bmpInt[wP][hP] != color) {
                    MBot = bmp.getHeight() - hP;
                    found = true;
                    break;
                }
            }
        }
        found = false;

        for (int wP = 0; wP < bmpInt.length && !found; wP++) {
            for (int hP = 0; hP < bmpInt[0].length && !found; hP++) {
                if (bmpInt[wP][hP] != color) {
                    MLeft = wP;
                    found = true;
                    break;
                }
            }
        }
        found = false;

        for (int wP = bmpInt.length - 1; wP >= 0 && !found; wP--) {
            for (int hP = 0; hP < bmpInt[0].length && !found; hP++) {
                if (bmpInt[wP][hP] != color) {
                    MRight = bmp.getWidth() - wP;
                    found = true;
                    break;
                }
            }
        }

        int sizeY = bmp.getHeight() - MBot - MTop, sizeX = bmp.getWidth() - MRight - MLeft;
        Bitmap bmp2 = Bitmap.createBitmap(bmp, MLeft, MTop, sizeX, sizeY);
        dtMili = (System.currentTimeMillis() - dtMili);

        return bmp2;
    }

    public static Bitmap scalePreserveRatio(Bitmap bitmapImage, int targetWidth, int targetHeight) {
        if (targetHeight > 0 && targetWidth > 0 && bitmapImage != null) {
            int width = bitmapImage.getWidth();
            int height = bitmapImage.getHeight();

            // Calculate the max changing amount and decide which dimension to
            // use
            float widthRatio = (float) targetWidth / (float) width;
            float heightRatio = (float) targetHeight / (float) height;

            // Use the ratio that will fit the image into the desired sizes
            int finalWidth = (int) Math.floor(width * widthRatio);
            int finalHeight = (int) Math.floor(height * widthRatio);
            if (finalWidth > targetWidth || finalHeight > targetHeight) {
                finalWidth = (int) Math.floor(width * heightRatio);
                finalHeight = (int) Math.floor(height * heightRatio);
            }

            // Scale given bitmap to fit into the desired area
            bitmapImage = Bitmap.createScaledBitmap(bitmapImage, finalWidth, finalHeight, true);

            // Created a bitmap with desired sizes
            Bitmap scaledImage = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(scaledImage);

            // Draw background color
            Paint paint = new Paint();
            paint.setColor(android.graphics.Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

            // Calculate the ratios and decide which part will have empty areas
            // (width or height)
            float ratioBitmap = (float) finalWidth / (float) finalHeight;
            float destinationRatio = (float) targetWidth / (float) targetHeight;
            float left = ratioBitmap >= destinationRatio ? 0 : (float) (targetWidth - finalWidth) / 2;
            float top = ratioBitmap < destinationRatio ? 0 : (float) (targetHeight - finalHeight) / 2;
            canvas.drawBitmap(bitmapImage, left, top, null);

            return scaledImage;
        } else {
            return bitmapImage;
        }
    }

    public static String getPrintTemplateFolder() {
        String dataFolder = TPUtility.getDataFolder();
        File file = new File(dataFolder + "PrintTemplates");
        if (!file.exists()) {
            file.mkdir();
        }
        return dataFolder + "PrintTemplates/";
    }

    public static boolean isN5ServiceAvailable(Context context) {
        try {
            try {
                N5Library.initialize(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return isServiceAvailable();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static int getPhotoCount(String isLPR, int size) {
        int photoCount = 0;
        if (size <= 0) {
            return 0;
        }

        if ("Y".equalsIgnoreCase(isLPR)) {
            photoCount = size - 1;
        } else {
            photoCount = size;
        }

        return photoCount;
    }

    public static void printAdvancePaper(Activity activity) {
        String printData = "_EZ{PRINT,QSTOP,STOP1300:}{LP}";
        String templateName = "AdvancePaper";
        String printer = TicketPrinter.getSelectedMethod(activity);

        // For N5 Printing
        if (printer != null && printer.equalsIgnoreCase(TicketPrinter.COMMUNICATION_METHOD_PRINTSERVICE)) {
            new N5TicketPrinter(activity, true).advancePaper();
        } else {
            if (printer != null && printer.equalsIgnoreCase(TicketPrinter.COMMUNICATION_METHOD_TSC_BLUETOOTH)) {
                templateName = "TSCAdvancePaper";
            }

            // Find advance paper template and send print commands
            try {
                PrintTemplate template = PrintTemplate.getPrintTemplateByName(templateName);
                if (template != null) {
                    printData = template.getTemplateData();
                }
            } catch (Exception e) {
                Log.e("Advance Paper", TPUtility.getPrintStackTrace(e));
            }

            TicketPrinter.print(activity, printData);
        }
    }

    public static void showBrightnessControl(final Context context) {
        final Dialog dialog = new Dialog(context);
        final ContentResolver contentResolver = context.getContentResolver();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.brightness_control);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setDimAmount(0);

        SeekBar seekbarControl = dialog.findViewById(R.id.seekBar);
        seekbarControl.setMax(255);
        try {
            int currentValue = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
            // TextView valueText = (TextView)
            // dialog.findViewById(R.id.brightnessLabel);
            // valueText.setText(((currentValue/255) * 100) + " %");
            seekbarControl.setProgress(currentValue);
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }

        seekbarControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TextView valueText = (TextView)
                // dialog.findViewById(R.id.brightnessLabel);
                // valueText.setText(((progress/255) * 100) + " %");

                setBrightness(context, progress);
            }
        });

        dialog.show();
    }

    public static void setBrightness(Context context, int brightness) {
        if (brightness < 0) {
            brightness = 0;
        } else if (brightness > 255) {
            brightness = 255;
        }

        ContentResolver cResolver = context.getContentResolver();
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
    }

    public static boolean hasMultipleModules(int custId) {
        Module parkingModule;
        Module trafficModule;
        try {
            parkingModule = Module.getModuleByName(Module.TP_MODULE_PARKING);
            trafficModule = Module.getModuleByName(Module.TP_MODULE_TRAFFIC);

            /* Check if modules are not initialized */
            if (parkingModule == null && trafficModule == null) {
                return false;
            }

            if (trafficModule != null) {
                CustomerModule customerModule = CustomerModule.getModuleByName(trafficModule.getModuleId(),
                        TPApplication.getInstance().getCurrentUserId());
                if (customerModule == null || !customerModule.isActive()) {
                    return false;
                }
            }

            if (parkingModule != null) {
                CustomerModule customerModule = CustomerModule.getModuleByName(parkingModule.getModuleId(),
                        TPApplication.getInstance().getCurrentUserId());
                if (customerModule == null || !customerModule.isActive()) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean isAppInstalled(PackageManager pm, String packageName) {
        boolean installed = false;
        try {
            PackageInfo pkgInfo = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            if (pkgInfo != null) {
                installed = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }

        return installed;
    }

    public static boolean isTrafficInstallled(PackageManager pm) {
        return isAppInstalled(pm, "com.ticketpro.traffic");
    }

    public static PrintTemplate getTicketPrintTemplate(SharedPreferences mPreferences) {
        DeviceInfo deviceInfo = TPApplication.getInstance().getDeviceInfo();
        PrintTemplate template = null;

        if (deviceInfo != null) {
            int templateId = deviceInfo.getDefaultTemplateId();
            try {
                template = PrintTemplate.getPrintTemplateById(templateId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            if (mPreferences != null) {
                String printerType = mPreferences.getString(TicketPrinter.APPLICATION_COMM_METHOD_NAME_KEY, null);
                if (printerType != null) {
                    if (printerType.equalsIgnoreCase(TicketPrinter.COMMUNICATION_METHOD_TSC_BLUETOOTH)) {
                        template = PrintTemplate.getPrintTemplateByName("TSCPrintTicket");
                    }
                }
            }

            // Get N5 PrintTicket Template
            if (template == null && isServiceAvailable()) {
                template = PrintTemplate.getPrintTemplateByName("N5PrintTicket");
            }

            // Get ParkingPrintTicket Template
            if (template == null) {
                template = PrintTemplate.getPrintTemplateByName("ParkingPrintTicket");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return template;
    }


    public static PrintTemplate getTicketPrintTemplateForActivity(SharedPreferences mPreferences) {
        DeviceInfo deviceInfo = TPApplication.getInstance().getDeviceInfo();
        PrintTemplate template = null;

        if (deviceInfo != null) {
            int templateId = deviceInfo.getDefaultTemplateId();
            try {
                template = PrintTemplate.getPrintTemplateById(templateId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            if (mPreferences != null) {
                String printerType = mPreferences.getString(TicketPrinter.APPLICATION_COMM_METHOD_NAME_KEY, null);
                if (printerType != null) {
                    if (printerType.equalsIgnoreCase("TSC Bluetooth")) {
                        template = PrintTemplate.getPrintTemplateByName("TSCActivityTemplate");
                    }
                }
            }

            // Get N5 PrintTicket Template
            if (template == null && isServiceAvailable()) {
                template = PrintTemplate.getPrintTemplateByName("N5ActivityTemplate");
            }

            // Get ParkingPrintTicket Template
            if (template == null) {
                template = PrintTemplate.getPrintTemplateByName("ActivityTemplate");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return template;
    }

    public static boolean isRugbyDevice() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        return currentapiVersion < Build.VERSION_CODES.KITKAT;
    }

    public static byte[] generateKey(String password) throws Exception {
        byte[] keyStart = password.getBytes(StandardCharsets.UTF_8);

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(keyStart);
        kgen.init(128, sr);
        SecretKey skey = kgen.generateKey();
        return skey.getEncoded();
    }

    public static byte[] encodeFile(byte[] key, byte[] fileData) throws Exception {

        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(fileData);

        return encrypted;
    }

    public static byte[] decodeFile(byte[] key, byte[] fileData) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] decrypted = cipher.doFinal(fileData);

        return decrypted;
    }

    /**
     * Function will help to create txt file when user logout the device
     */
    public static final void createTxtFile() {
        try {
            File file = new File(getExternalStorage() + "/TicketPro");
            String fileName = TPConstant.PARKING_TXT_FILE;
            File txtFile = new File(file, fileName);
            FileWriter writer = new FileWriter(txtFile, true);
            writer.append("File created");
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeTxtFile() {

        String filePath = Environment.getExternalStorageDirectory() + "/TicketPro/" + TPConstant.PARKING_TXT_FILE;
        if (filePath != null) {
            try {
                File file = new File(filePath);
                if (file != null && file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void fileEncryption() throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, IOException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException {

        // File to encrypt.
        /*public static void createPasswordProtectedZip(){
         */
        /*try {
			ZipParameters zipParameters = new ZipParameters();
			zipParameters.setEncryptFiles(true);
			zipParameters.setEncryptionMethod(EncryptionMethod.AES);
			zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);

			List<File> filesToAdd = Arrays.asList(
					new File(TPUtility.getCSVBackupFolder()+"/20200102_TBL_Tickets.csv"));

			ZipFile zipFile = new ZipFile(TPUtility.getCurrentCSVFile("Tickets"), "pass123".toCharArray());
			zipFile.addFiles(filesToAdd, zipParameters);

		} catch (ZipException e) {
			e.printStackTrace();
		}*/
        /*


		try {
			ZipFile zipFile = new ZipFile(TPUtility.getCSVBackupFolder()+"/AddFilesWithAESZipEncryption.zip");
			ArrayList filesToAdd = new ArrayList();
			filesToAdd.add(new File(TPUtility.getCSVBackupFolder()+"/20200102_TBL_Tickets.csv"));
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			parameters.setEncryptFiles(true);
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
			parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
			parameters.setPassword("test123");
			zipFile.addFiles(filesToAdd, parameters);
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}*/
        String filename = TPUtility.getCSVBackupFolder() + "/20200102_TBL_Tickets.csv";

        // Password must be at least 8 characters (bytes) long
        String password = "12345678";

        FileInputStream inFile = new FileInputStream(filename);

        FileOutputStream outFile = new FileOutputStream(TPUtility.getCSVBackupFolder() + "/20200102_TBL_Tickets.enc");

        // Use PBEKeySpec to create a key based on a password.
        // The password is passed as a character array

        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey passwordKey = keyFactory.generateSecret(keySpec);

        // PBE = hashing + symmetric encryption.  A 64 bit random
        // number (the salt) is added to the password and hashed
        // using a Message Digest Algorithm (MD5 in this example.).
        // The number of times the password is hashed is determined
        // by the interation count.  Adding a random number and
        // hashing multiple times enlarges the key space.

        byte[] salt = new byte[8];
        Random rnd = new Random();
        rnd.nextBytes(salt);
        int iterations = 100;

        //Create the parameter spec for this salt and interation count

        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);

        // Create the cipher and initialize it for encryption.

        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        cipher.init(Cipher.ENCRYPT_MODE, passwordKey, parameterSpec);

        // Need to write the salt to the (encrypted) file.  The
        // salt is needed when reconstructing the key for decryption.

        outFile.write(salt);

        // Read the file and encrypt its bytes.

        byte[] input = new byte[64];
        int bytesRead;
        while ((bytesRead = inFile.read(input)) != -1) {
            byte[] output = cipher.update(input, 0, bytesRead);
            if (output != null) outFile.write(output);
        }

        byte[] output = cipher.doFinal();
        if (output != null) outFile.write(output);

        inFile.close();
        outFile.flush();
        outFile.close();

        result = "File " + filename + " Encrypted";
    }

    public static void fileDecryption(String filename) {
        String password;
        FileInputStream inFile;
        FileOutputStream outFile;
        password = "12345678";

        try {
            //inFile = new FileInputStream(filename);
            inFile = new FileInputStream(TPUtility.getCSVBackupFolder() + "/20200102_TBL_Tickets.enc");

            outFile = new FileOutputStream(TPUtility.getCSVBackupFolder() + "temp.csv");

            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey passwordKey = keyFactory.generateSecret(keySpec);

            // Read in the previouly stored salt and set the iteration count.

            byte[] salt = new byte[8];
            inFile.read(salt);
            int iterations = 100;

            PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);

            // Create the cipher and initialize it for decryption.

            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
            cipher.init(Cipher.DECRYPT_MODE, passwordKey, parameterSpec);

            byte[] input = new byte[64];
            int bytesRead;
            while ((bytesRead = inFile.read(input)) != -1) {
                byte[] output = cipher.update(input, 0, bytesRead);
                if (output != null)
                    outFile.write(output);
            }

            byte[] output = cipher.doFinal();
            if (output != null)
                outFile.write(output);

            inFile.close();
            outFile.flush();
            outFile.close();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IOException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void updateFB(Activity activity, String activityName, Ticket activeTicket, Logger log) {
        try {
            if (Feature.isFeatureAllowed(Feature.FIELD_TRACKER)) {
                log.info("function call-->updateFB");
                LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                if (locationManager != null) {
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 50, new MyLocationListener());
                }
                Location location = locationManager != null ? locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) : null;
                if (location == null || location.getAccuracy() > 30) {
                    location = locationManager != null ? locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) : null;
                }
                if (retry < 3) {
                    if (location != null && location.getAccuracy() > 30) {
                        retry++;
                        updateFB(activity, activityName, activeTicket, log);
                        return;
                    }
                }
                retry = 0;
                FirebaseDBUpdater firebaseDBUpdater = new FirebaseDBUpdater(activity);
                firebaseDBUpdater.updateFB(location, activityName, activeTicket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void populateSQliteFromCSV(String fileName, int duration, String location, String tire, Context context) {
        try {
            //DatabaseHelper.getInstance().openReadableDatabase();
            FileReader file = new FileReader(fileName);
            BufferedReader buffer = new BufferedReader(file);
            String line = "";
            String date;

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            Date date1;
            try {
                while ((line = buffer.readLine()) != null) {
                    long chalkId = ALPRChalk.getNextPrimaryId();
                    String[] colums = line.split(",");
                    ContentValues cv = new ContentValues();
                    ALPRChalk alprChalk = new ALPRChalk();
                    if (colums[0].trim().replace("\"", "").equalsIgnoreCase("Plate")) {
                        continue;
                    }
                    if (checkAndupdateExistingRecords(colums, dateFormat)) {
                        continue;
                    }

                    alprChalk.setPlate(colums[0].trim().replace("\"", ""));
                    alprChalk.setDetails(colums[1].trim().replace("\"", ""));
                    alprChalk.setCustomData1(colums[2].trim().replace("\"", ""));
                    alprChalk.setCustomData2(colums[3].trim().replace("\"", ""));
                    alprChalk.setCustomData3(colums[4].trim().replace("\"", ""));
                    alprChalk.setConfidence(colums[5].trim().replace("\"", ""));
                    date = colums[6].trim().replace("\"", "") + " " + colums[7].trim().replace("\"", "");
                    date1 = dateFormat.parse(date);

                    long expirationTime = date1.getTime() + (Duration.getDurationMinsById(duration, context) * 60 * 1000);
                    alprChalk.setFirstDateTime(date1);
                    alprChalk.setFirstDate(colums[6].trim().replace("\"", ""));
                    alprChalk.setFirstTime(colums[7].trim().replace("\"", ""));
                    alprChalk.setFirstParkingBay(colums[8].trim().replace("\"", ""));
                    alprChalk.setFirstLocLat(colums[9].trim().replace("\"", ""));
                    alprChalk.setFirstLocLong(colums[10].trim().replace("\"", ""));
                    alprChalk.setFirstLocAcc(colums[11].trim().replace("\"", ""));

                    date = colums[12].trim().replace("\"", "") + " " + colums[13].trim().replace("\"", "");
                    if (!date.equalsIgnoreCase(" ")) {
                        date1 = dateFormat.parse(date);
                        alprChalk.setLastDateTime(date1);
                        alprChalk.setLastDate(colums[12].trim().replace("\"", ""));
                        alprChalk.setLastTime(colums[13].trim().replace("\"", ""));
                    }

                    alprChalk.setLastParkingBay(colums[14].trim().replace("\"", ""));
                    alprChalk.setLastLocLat(colums[15].trim().replace("\"", ""));
                    alprChalk.setLastLocLong(colums[16].trim().replace("\"", ""));
                    alprChalk.setLastLocAcc(colums[17].trim().replace("\"", ""));
                    alprChalk.setPermitExpiryDate(colums[18].trim().replace("\"", ""));
                    alprChalk.setPermitExpiryTime(colums[19].trim().replace("\"", ""));
                    alprChalk.setChalkDurationId(duration);
                    alprChalk.setChalkLocation(location);
                    alprChalk.setChalkTire(tire);
                    alprChalk.setChalkDurationCode(Duration.getDurationTitleUsingId(duration));
                    alprChalk.setUserid(TPApplication.getInstance().userId);
                    alprChalk.setDeviceId(TPApplication.getInstance().deviceId);
                    alprChalk.setCustId(TPApplication.getInstance().custId);
                    alprChalk.setChalkId(chalkId);

                    ChalkPicture chalkPicture = updateALPRPhotoChalkPicture(cv.getAsString("Plate"), chalkId, location, cv.getAsString("FirstLocLat"), cv.getAsString("FirstLocLong"));

                    ALPRChalk.insertALPRChalk(alprChalk);
                    // DatabaseHelper.getInstance().insertOrReplace(cv, TPConstant.TABLE_ALPR_CHALK);
                    syncData(chalkId, date1, TPApplication.getInstance().custId, chalkPicture, expirationTime, context);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //          DatabaseHelper.getInstance().closeWritableDb();
            try {
                new File(fileName).delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
//            DatabaseHelper.getInstance().closeWritableDb();
        }
    }

    public static void exportCSV(String zone, String location) {
        ALPRChalk.updateCSV(/*DatabaseHelper.getInstance()*/ zone, location);
    }

    private static void syncData(long chalkId, Date date, int custId, ChalkPicture chalkPicture, long expirationTime, Context context) {
        try {
            ArrayList<SyncData> syncList = new ArrayList<SyncData>();

            // Update Sync Status.....
            SyncData syncData = new SyncData();
            syncData.setActivity("INSERT");
            syncData.setPrimaryKey(String.valueOf(chalkId));
            syncData.setActivityDate(date);
            syncData.setCustId(custId);
            syncData.setTableName(TPConstant.TABLE_CHALKS);
            syncData.setStatus("Pending");
            SyncData.insertSyncData(syncData).subscribe();
            //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
            syncList.add(syncData);

            SyncData syncPicture = new SyncData();
            syncPicture.setActivity("INSERT");
            syncPicture.setPrimaryKey(chalkPicture.getPictureId() + "");
            syncPicture.setActivityDate(new Date());
            syncPicture.setCustId(custId);
            syncPicture.setTableName(TPConstant.TABLE_CHALK_PICTURES);
            syncPicture.setStatus("Pending");
            SyncData.insertSyncData(syncPicture).subscribe();
            //DatabaseHelper.getInstance().insertOrReplace(syncPicture.getContentValues(), "sync_data");
            syncList.add(syncData);

            /*Intent notificationIntent = new Intent(TPApplication.getInstance().getApplicationContext(), AlarmReceiver.class);
            notificationIntent.putExtra("Title", "Chalk Expiration");
            notificationIntent.putExtra("Message", "Photo chalk has expired");
            notificationIntent.putExtra("ChalkId", chalkId);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), TPApplication.notificationId + 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            alarmManager.set(AlarmManager.RTC_WAKEUP, expirationTime, pendingIntent);*/

            if (TPApplication.getInstance().isServiceAvailable) {
                ChalkVehicleNetworkCalls.saveChalk(syncList);
                /*ChalkBLProcessor blProcessor = new ChalkBLProcessor((TPApplication) context.getApplicationContext());
                blProcessor.syncData(syncList, context);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkAndupdateExistingRecords(String[] colums, SimpleDateFormat dateFormat) {
        String date;
        Date date1;
        for (String alprChalk : ALPRChalk.getAllPlates()) {
            ALPRChalk values = new ALPRChalk();
            try {
                if (alprChalk.equalsIgnoreCase(colums[0].trim().replace("\"", ""))) {
                    date = colums[12].trim().replace("\"", "") + " " + colums[13].trim().replace("\"", "");
                    if (!date.equalsIgnoreCase(" ")) {
                        date1 = dateFormat.parse(date);
                        values.setLastDate(DateUtil.getStringFromDate3(date1));
                    }
                    values.setLastParkingBay(colums[14].trim().replace("\"", ""));
                    values.setLastLocLat(colums[15].trim().replace("\"", ""));
                    values.setLastLocLong(colums[16].trim().replace("\"", ""));
                    values.setLastLocAcc(colums[17].trim().replace("\"", ""));
                    ALPRChalk.updateChalk(alprChalk, values);
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static ChalkPicture updateALPRPhotoChalkPicture(String plate, long chalkId, String address, String latitude, String longitude) {
        String filename = TPUtility.getALPRImagesFolder() + "anpr_" + plate + ".jpg";
        File file = new File(filename);
        if (file.exists()) {
            System.out.println(file.getAbsolutePath());
        }

        ChalkPicture chalkPicture = new ChalkPicture();
        //chalkPicture.setPictureId(ChalkPicture.getNextPrimaryId());
        chalkPicture.setChalkDate(new Date());
        chalkPicture.setImagePath(file.getAbsolutePath());
        chalkPicture.setImageResolution("640x320");
        chalkPicture.setChalkId(chalkId);
        chalkPicture.setSyncStatus("Pending");
        chalkPicture.setLocation(address);
        chalkPicture.setLatitude(latitude);
        chalkPicture.setLongitude(longitude);
        chalkPicture.setCustId(TPApplication.getInstance().custId);
        Completable completable = ChalkPicture.insertChalkPicture(chalkPicture);
        completable.subscribe();
        //DatabaseHelper.getInstance().insertOrReplace(chalkPicture.getContentValues(), "chalk_pictures");
        return chalkPicture;
    }

    public static boolean __checkVersion() {
        return true;
    }

    public static boolean isActivityRunning(Activity activity) {
        boolean shown = activity.getWindow().getDecorView().getRootView().isShown();
        Log.e(TAG, "isActivityRunning: " + shown);
        return shown;
    }

    public boolean isAlphaNumeric(String s) {
        String pattern = "^[a-zA-Z0-9]*$";
        return s.matches(pattern);
    }

    public void addImageFileToMediaLibrary(Context ct) {
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Images.Media.TITLE, "image");
        values.put(MediaStore.Images.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/file");
        values.put(MediaStore.Images.Media.DATA, "");
        ContentResolver contentResolver = ct.getContentResolver();

        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);

    }

    private static class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "onLocationChanged: " + location.getLatitude() + location.getAccuracy() + location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    public static String readExistingDeviceName() {
        File file = new File(TPUtility.getDataFolder() + "deviceName.txt");
        StringBuilder st = new StringBuilder();
        try {
            FileReader fr = new FileReader(file);

            int i;
            while ((i = fr.read()) != -1)
                st.append((char) i);
            //System.out.print((char) i);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String s = st.toString().replaceAll("\n", "");
        return s + "-" + TPConstant.MODULE_NAME;
    }

    public static HashMap<Integer,Integer> getVendorCode(String input){

        HashMap<Integer, Integer> map = new HashMap<>();

        String[] pairs = input.split(";");

        for (String pair : pairs) {
            String[] keyValue = pair.split(",");

            int key = Integer.parseInt(keyValue[0].trim());
            int value = Integer.parseInt(keyValue[1].trim());

            map.put(key, value);
        }

        return map;
    }
}
