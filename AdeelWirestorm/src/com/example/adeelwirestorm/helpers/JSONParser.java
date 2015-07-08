package com.example.adeelwirestorm.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class JSONParser {

	static InputStream is = null;
	String json = "";
	final Context context;

	// constructor
	public JSONParser(Context context) {
		this.context = context;
	}

	// function get json from url
	// by making HTTP POST or GET mehtod
	public String makeHttpRequest(String url, String method,
			List<NameValuePair> params) {

		// Making HTTP request
		try {

			// check for request method
			if (method == "POST") {
				// request method is POST
				DefaultHttpClient httpClient;
				// defaultHttpClient
				if (url.startsWith("https://"))
					httpClient = new SecureClient(context);
				else
					httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");

				JSONObject jsonObj = new JSONObject();
				int length = params.size();
				for (int a = 0; a < length; a++)
					try {
						jsonObj.put(params.get(a).getName(), params.get(a)
								.getValue());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				httpPost.setEntity(new StringEntity(jsonObj.toString(),
						HTTP.UTF_8));
				Log.d("params", httpPost.getEntity().toString());
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			} else if (method == "GET") {
				// request method is GET
				DefaultHttpClient httpClient = new SecureClient(context);
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// return JSON String
		return json;

	}

}
