package edu.saxion.kuiperklaczynski.tweethack.net;


import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by leonk on 02/05/2016.
 */
public class URLTesting {

    private static final String TAG = "TweetHax_URLTesting"; //Log Tag


    public static int getResponseCode(String urlString) throws MalformedURLException, IOException {
        int res;
        final URL url = new URL(urlString);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        res = huc.getResponseCode();
        Log.d(TAG, "getResponseCode: "+res);
        return res;
    }
}
