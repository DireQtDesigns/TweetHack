package edu.saxion.kuiperklaczynski.tweethack.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.apache.commons.io.IOUtils;

import edu.saxion.kuiperklaczynski.tweethack.gui.MainActivity;

/**
 * Created by Robin on 24-5-2016.
 */
public class BearerToken extends AsyncTask<Context,Void,String>{
    private final String API_KEY = "4LiUJZIHjuFT6IVaGBCZooSRw", API_SECRET = "yrxAVjSOd7oyqOKCSwpAVKCsktOlw0rR8ZwjGUOQNnyxiz13QL";

    private final String TAG = "BearerToken";

    private Context c;

    private String generateBearerToken(Context c) {
        this.c = c;

        URL url;
        try {
            url = new URL("https://api.twitter.com/oauth2/token");
        } catch (MalformedURLException mue) {
            Log.e(TAG, "generateBearerToken: ", mue);;
            return null;
        }

        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
        } catch (Exception e) {
            Log.e(TAG, "generateBearerToken: ", e);
            return null;
        }


        String authString;
        try {
            authString = URLEncoder.encode(API_KEY, "UTF-8") + ":" + URLEncoder.encode(API_SECRET, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            Log.e(TAG, "generateBearerToken: ", uee);
            return null;
        }

        String authStringBase64;

        try {
            authStringBase64 = Base64.encodeToString(authString.getBytes("UTF-8"), Base64.NO_WRAP);
        } catch (Exception e) {
            Log.e(TAG, "generateBearerToken: ", e);
            return null;
        }

        conn.setRequestProperty("Authorization", "Basic " + authStringBase64);

        conn.setRequestProperty("Content-Type", "application/x-www-from-urlencoded;charset=UTF-8");

        conn.setDoOutput(true);

        byte[] body;
        try {
            body = "grant_type=client_credentials".getBytes("UTF-8");
        } catch (UnsupportedEncodingException uee) {
            Log.e(TAG, "generateBearerToken: ", uee);
            return null;
        }

        conn.setFixedLengthStreamingMode(body.length);

        BufferedOutputStream os;

        String token = null;

        try {
            os = new BufferedOutputStream(conn.getOutputStream());
            os.write(body);
            os.close();


        if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
            InputStream is = conn.getInputStream();
            String response = IOUtils.toString(is, "UTF-8");
            IOUtils.closeQuietly(is);
            conn.disconnect();

            if (response == null) Log.d(TAG, "generateBearerToken: response is null");

            JSONObject jsonObject = new JSONObject(response);
            token = jsonObject.getString("access_token");

            if (token == null) Log.d(TAG, "generateBearerToken: token is null");
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "generateBearerToken: ", e);
        }
        return token;
    }

//    @Override
//    protected String doInBackground(Context... params) {
//        return generateBearerToken(params[0]);
//    }

    @Override
    protected void onPostExecute(String s) {
        SharedPreferences prefs = c.getSharedPreferences("edu.saxion.kuiperklaczynski.tweethack", c.MODE_PRIVATE);

        prefs.edit().putString("BEARERTOKEN", s).apply();
        MainActivity m = MainActivity.getInstance();
        m.addBearerToken(s);
    }

    @Override
    protected String doInBackground(Context... params) {
        c = params[0];

        // prepare request
        String token = null;
        try {
            URL url = new URL(" https://api.twitter.com/oauth2/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            // encode api key and secret
            String authString = URLEncoder.encode(API_KEY, "UTF-8") + ":" +
                    URLEncoder.encode(API_SECRET, "UTF-8");

            // apply base64 encoding on the encode string
            String authStringBase64 = Base64.encodeToString(authString.getBytes("UTF-8"), Base64.NO_WRAP);

            // set headers
            conn.setRequestProperty("Authorization", "Basic " + authStringBase64);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            // set body
            conn.setDoOutput(true);
            byte[] body = "grant_type=client_credentials".getBytes("UTF-8");
            conn.setFixedLengthStreamingMode(body.length);
            BufferedOutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(body);
            os.close();

            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                InputStream is = conn.getInputStream();
                String response = IOUtils.toString(is, "UTF-8");
                IOUtils.closeQuietly(is);
                conn.disconnect();
                JSONObject jsonObject = new JSONObject(response);
                token = jsonObject.getString("access_token");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return token;
    }

}
