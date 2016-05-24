package edu.saxion.kuiperklaczynski.tweethack.net;

import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Robin on 24-5-2016.
 */
public class BearerToken {
    private final String API_KEY = "4LiUJZIHjuFT6IVaGBCZooSRw", API_SECRET = "yrxAVjSOd7oyqOKCSwpAVKCsktOlw0rR8ZwjGUOQNnyxiz13QL";
    //private final String BEARER_TOKEN;

    public void BearerToken() {

    }

    private JSONObject getBearerToken() {
        URL url;
        try {
            url = new URL("https://api.twitter.com/oauth2/token");
        } catch (MalformedURLException mue) {
            System.out.println("get fucked kiddo");
            return null;
        }

        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }


        String authString;
        try {
            authString = URLEncoder.encode(API_KEY, "UTF-8") + ":" + URLEncoder.encode(API_SECRET, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            System.out.println(uee);
            return null;
        }

        String authStringBase64;

        try {
            authStringBase64 = Base64.encodeToString(authString.getBytes("UTF-8"), Base64.NO_WRAP);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        conn.setRequestProperty("Authorization", "Basic " + authStringBase64);

        conn.setRequestProperty("Content-Type", "application/x-www-from-urlencoded;charset=UTF-8");

        conn.setDoOutput(true);

        byte[] body;
        try {
            body = "grant_type=client_credentials".getBytes("UTF-8");
        } catch (UnsupportedEncodingException uee) {
            System.out.println(uee);
            return null;
        }

        conn.setFixedLengthStreamingMode(body.length);

        BufferedOutputStream os;

        try {
            os = new BufferedOutputStream(conn.getOutputStream());
            os.write(body);
            os.close();
        } catch (IOException ioe) {
            System.out.println(ioe);
            return null;
        }

        for (int i = 0; i < body.length; i++) {
            Log.d(TAG, "getBearerToken: ");
        };
    }

}
