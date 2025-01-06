package com.ticketpro.vendors;

import android.util.Log;

import com.ticketpro.util.TLSSocketFactory;
import com.ticketpro.util.TPUtility;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import static com.ticketpro.util.TPUtility.getNetworkTimeout;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class IPSQuery {
	private static Logger log;
	private static String responseString;

	private static final OkHttpClient client = new OkHttpClient.Builder()
			.connectTimeout(getNetworkTimeout(), TimeUnit.MILLISECONDS)
			.readTimeout(getNetworkTimeout(), TimeUnit.MILLISECONDS)
			.build();

	public static String getMeterStatus(String URL, String token, String meterNumber) {
		String meterReqeust = "<Request xmlns=\"http://www2.ipsmetersystems.com/meter\"><MeterNumber>" + meterNumber
				+ "</MeterNumber>" + "</Request>";

		return callService(meterReqeust, token, URL);
	}

	public static String getSpaceStatus(String URL, String token, String SpaceGroup, String spaceNumber) {
		String spaceRequest = "<Request xmlns=\"http://www2.ipsmetersystems.com/meter\"><Space>" + spaceNumber
				+ "</Space><SpaceGroup>" + SpaceGroup + "</SpaceGroup></Request>";
		return callService(spaceRequest, token, URL);
	}

	public static String getMultiSpaceStatus(String URL, String token, String SubAreaName) {
		String spaceRequest = "<Request xmlns=\"http://www2.ipsmetersystems.com/meter\"><SubAreaName>" + SubAreaName
				+ "</SubAreaName></Request>";

		return callServiceWithSSL(spaceRequest, token, URL);
	}

	public static String getSpaceGroupStatus(String URL, String token, String SpaceGroup) {
		String spaceRequest = "<Request xmlns=\"http://www2.ipsmetersystems.com/meter\"><SpaceGroup>" + SpaceGroup
				+ "</SpaceGroup></Request>";

		return callServiceWithSSL(spaceRequest, token, URL);
	}

	public static String getPlatesBySubArea(String URL, String token, String subArea) {
		String spaceRequest = "<Request xmlns=\"http://www2.ipsmetersystems.com/meter\"><SubAreaName>" + subArea
				+ "</SubAreaName></Request>";

		return callServiceWithSSL(spaceRequest, token, URL);
	}

	public static String getPlateStatus(String URL, String token, String plateNumber) {
		String serverRequest = "<Request xmlns=\"http://www2.ipsmetersystems.com/meter\"><LicensePlateNumber>"
				+ plateNumber + "</LicensePlateNumber>" + "</Request> ";

		return callService(serverRequest, token, URL);
	}

	private static String callService(String request, String token, String URL) {
		String response;
		StringEntity se;
		try {
			se = new StringEntity(request, HTTP.UTF_8);
			se.setContentType("text/xml");

			HttpClient httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(URL);
			httpPost.addHeader("IPSToken", token);
			httpPost.setEntity(se);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity resEntity = httpResponse.getEntity();
			response = EntityUtils.toString(resEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return response;
	}

	private static String callServiceWithSSL(String request, String token, String urlString, boolean isRequired) {
		String result = "";
		StringEntity se;
		HttpsURLConnection urlConnection = null;
		try {
			// create connection
			URL urlToRequest = new URL(urlString);
			urlConnection = (HttpsURLConnection) urlToRequest.openConnection();
			urlConnection.setSSLSocketFactory(new TLSSocketFactory());
			urlConnection.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			urlConnection.setReadTimeout(getNetworkTimeout());
			urlConnection.setConnectTimeout(getNetworkTimeout());
			urlConnection.setRequestProperty("IPSToken", token);
			// handle POST parameters
			if (request != null) {
				Log.i("TAG", "POST parameters: " + request);
				urlConnection.setDoOutput(true);
				urlConnection.setRequestMethod("POST");
				urlConnection.setFixedLengthStreamingMode(
						request.getBytes().length);
				urlConnection.setRequestProperty("Content-Type",
						"text/xml");

				//send the POST out
				PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
				out.print(request);
				out.close();
			}

			// handle issues
			int statusCode = urlConnection.getResponseCode();
			if (statusCode != HttpURLConnection.HTTP_OK) {
				// throw some exception
			}

			// read output (only for GET)
			if (request == null) {
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

		return result;
	}

	public static String callServiceWithSSL(String request, String token, String url) {
		String xmlData = request;

		RequestBody body = RequestBody.create(xmlData, MediaType.parse("application/xml"));

// Create the request
		Request requestObj = new Request.Builder()
				.url(url)
				.post(body)
				.addHeader("Content-Type", "application/xml")
				.addHeader("IPSToken", token)
				.build();

		String responseString = null;

		try (Response response = client.newCall(requestObj).execute()) {
			if (!response.isSuccessful()) {
				Log.e("NetworkError", "Unexpected code: " + response.code() + " - " + response.message());
			} else {
				// Handle response
				ResponseBody responseBody = response.body();
				if (responseBody != null) {
					responseString = responseBody.string(); // Read the response body
					System.out.println("Response: " + responseString);
					return responseString;// Print the response
				}
			}
		} catch (IOException e) {
			Log.e("NetworkError", "IOException occurred", e);
			// Handle the exception (e.g., log it, rethrow it, etc.)
		}

		return responseString;

	}
}
