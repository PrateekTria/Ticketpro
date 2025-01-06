package com.ticketpro.parking.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.ticketpro.lpr.LPRRequest;
import com.ticketpro.parking.R;
import com.ticketpro.util.Preview;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;

public class LPRActivityBK extends Activity {

    private static final String NAMESPACE = "LPRService";
    private static final String SOAP_ACTION = "LPRService/ILPRService/ReadLPRDataByXML";
    private static final String URL = "http://lpr.ticketproweb.com/LPRWcfService/LPRService.svc?wsdl";
    private static final String DATA_PATH = TPUtility.getDataFolder();
    private static final String DEFAULT_LANGUAGE = "eng";
    private final String TAG = "LPR";
    private Preview cView;
    private ProgressDialog progressDialog;
    private Handler handler;
    private String recognizedText = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cView = new Preview(this);
        setContentView(cView);

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.lpr_view, null, false);
        addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    Toast.makeText(LPRActivityBK.this, "Failed to get plate details. Please try again", Toast.LENGTH_LONG).show();
                    cView.camera.startPreview();
                }

                if (msg.what == 1) {
                    Toast.makeText(LPRActivityBK.this, "Text: " + recognizedText, Toast.LENGTH_LONG).show();
                    cView.camera.startPreview();
                }
            }
        };
    }

    public void backAction(View view) {
        finish();
    }

    public void processAction(View view) {
        cView.playSoundEffect(SoundEffectConstants.CLICK);
        if (cView.previewBitmapData == null)
            return;

        final byte[] data = cView.previewBitmapData.clone();
        cView.camera.stopPreview();

        progressDialog = ProgressDialog.show(this, "", "Processing LPR...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Camera.Parameters parameters = cView.camera.getParameters();
                Size size = parameters.getPreviewSize();

                YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                ByteArrayOutputStream outstr = new ByteArrayOutputStream();
                Rect rect = new Rect(0, 0, size.width, size.height);
                yuvimage.compressToJpeg(rect, 100, outstr);
		        
		        /*
		        BitmapFactory.Options options = new BitmapFactory.Options();
		        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		        options.inSampleSize = 2;
		        Bitmap bitmap=BitmapFactory.decodeByteArray(outstr.toByteArray(), 0, outstr.size(),options);
		        
		        String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };
		        for (String path : paths) {
		            File dir = new File(path);
		            if (!dir.exists()) {
		                if (!dir.mkdirs()) {
		                    Log.d(TAG, "ERROR: Creation of directory " + path+ " on sdcard failed");
		                    return;
		                } else {
		                    Log.d(TAG, "Created directory " + path + " on sdcard");
		                }
		            }
		        }
		        
		        if (!(new File(DATA_PATH + "tessdata/eng.traineddata")).exists()) {
		            try {
		                AssetManager assetManager = getAssets();
		                InputStream in = assetManager.open("tessdata/eng.traineddata");
		                OutputStream out = new FileOutputStream(DATA_PATH+"tessdata/eng.traineddata");

		                // Transfer bytes from in to out
		                byte[] buf = new byte[1024];
		                int len;
		                while ((len = in.read(buf)) > 0) {
		                    out.write(buf, 0, len);
		                }
		                in.close();
		                out.close();
		                Log.d(TAG, "Copied eng.traineddata");
		            } catch (IOException e) {
		                Log.d(TAG,"Was unable to copy eng.traineddata "+ e.toString());
		            }
		        }
		        Log.d(TAG, "Before baseApi");
		        System.gc();
		        
		        TessBaseAPI baseApi = new TessBaseAPI();
		        baseApi.setDebug(true);
		        baseApi.init(DATA_PATH, DEFAULT_LANGUAGE);
		        baseApi.setImage(bitmap);
		        
		        recognizedText = baseApi.getUTF8Text();
		        baseApi.end();
				
		        if(progressDialog.isShowing())
		    		progressDialog.dismiss();

		        //Send Success Handle
		        handler.sendEmptyMessage(1);
		        */

                String encoded = Base64.encodeToString(outstr.toByteArray(), Base64.DEFAULT);
                String XMLString = "<Image><UploadImage><ImageFile><![CDATA[" + encoded + "]]></ImageFile></UploadImage></Image>";

                LPRRequest lpr = new LPRRequest();
                lpr.strPrecisionMode = "0";
                lpr.strXMLImage = XMLString;
                lpr.strError = "";
                lpr.strPlateNumber = "";
                lpr.strState = "";

                SoapObject request = new SoapObject(NAMESPACE, "ReadLPRDataByXML");
                request.addProperty("strXMLImage", XMLString);
                request.addProperty("strPrecisionMode", lpr.strPrecisionMode);
                PropertyInfo pi = new PropertyInfo();
                pi.setName("ReadLPRDataByXML");
                pi.setValue(lpr);
                pi.setType(LPRRequest.class);
                request.addProperty(pi);

                HttpTransportSE androidHttpTransport = null;
                try {
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);

                    androidHttpTransport = new HttpTransportSE(URL);
                    androidHttpTransport.debug = true;
                    androidHttpTransport.call(SOAP_ACTION, envelope);

                    SoapObject resultObject = (SoapObject) envelope.bodyIn;
                    String xmlResult = resultObject.getPropertyAsString("ReadLPRDataByXMLResult");
                    String plateNumber = resultObject.getPropertyAsString("strPlateNumber");
                    String state = resultObject.getPropertyAsString("strState");
                    Log.e(TPConstant.TAG, resultObject + "");

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (xmlResult.equals("true")) {
                        if (state == null || state.equals("") || state.equals("anyType{}"))
                            state = "";

                        if (plateNumber == null || plateNumber.equals("") || plateNumber.equals("anyType{}"))
                            plateNumber = "";

                        if (state.equals("") && plateNumber.equals(""))
                            handler.sendEmptyMessage(0);
                        else
                            sendLPRDetails(state, plateNumber);
                    } else {
                        handler.sendEmptyMessage(0);
                    }

                } catch (Exception e) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    String response = androidHttpTransport.responseDump;
                    Log.e(TPConstant.TAG, response + "");

                    handler.sendEmptyMessage(0);
                }
            }

        }).start();
    }

    public void sendLPRDetails(String state, String plateNumber) {
        Intent data = new Intent();
        data.putExtra("State", state);
        data.putExtra("PlateNumber", plateNumber);

        if (getParent() == null) {
            setResult(Activity.RESULT_OK, data);
        } else {
            getParent().setResult(Activity.RESULT_OK, data);
        }

        finish();
    }

}
