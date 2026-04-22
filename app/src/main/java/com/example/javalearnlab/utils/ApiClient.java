package com.example.javalearnlab.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiClient {
    private static final String TAG = "ApiClient";

    public static String post(String urlStr, JSONObject body) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            Log.d(TAG, "POST URL: " + urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes("UTF-8"));
            os.close();

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Response code: " + responseCode);

            if (responseCode >= 200 && responseCode < 300) {
                Scanner sc = new Scanner(conn.getInputStream(), "UTF-8");
                String response = sc.useDelimiter("\\A").next();
                sc.close();
                Log.d(TAG, "Response: " + response);
                return response;
            } else {
                Scanner sc = new Scanner(conn.getErrorStream(), "UTF-8");
                String error = sc.useDelimiter("\\A").next();
                sc.close();
                Log.e(TAG, "Error response: " + error);
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in POST", e);
            return null;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}