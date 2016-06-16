package edu.saxion.kuiperklaczynski.tweethack.net;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import edu.saxion.kuiperklaczynski.tweethack.gui.MainActivity;
import edu.saxion.kuiperklaczynski.tweethack.io.JSONLoading;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;

/**
 * Created by Robin on 15-6-2016.
 */
public class SearchTask extends AsyncTask<String, Void, Tweet[]> {
    private final String TAG = "SearchTask";


    @Override
    protected Tweet[] doInBackground(String... params) {
        String token;
        String identifier;

        if (params[0] != null) {
            token = params[0];
            identifier = "AuthToken "; //TODO: correct
        } else if (params[1] != null) {
            token = params[1];
            identifier = "Bearer ";
        } else {
            return null;
        }

        String searchQuery = params[2];

        try {
            searchQuery = URLEncoder.encode(searchQuery, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            Log.e(TAG, "doInBackground: ", uee );
            return null;
        }

        URL url;
        try {
            url = new URL("https://api.twitter.com/1.1/search/tweets.json?q=" + searchQuery);
        } catch (MalformedURLException mue) {
            Log.e(TAG, "doInBackground: ", mue);
            return null;
        }

        JSONObject jSONObject = null;

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //TODO: add user token support
            conn.addRequestProperty("Authorization", identifier + token);
            conn.setRequestMethod("GET");
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                InputStream is = conn.getInputStream();
                String response = IOUtils.toString(is, "UTF-8");
                IOUtils.closeQuietly(is);

                jSONObject = new JSONObject(response);
            } else {
                Log.d(TAG, "doInBackground: httpstatus = " + conn.getResponseCode());
            }
            conn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        Tweet[] tweets = null;

        if (jSONObject != null) {
            try {
                tweets = JSONLoading.readJSON(jSONObject.toString());
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: ", e);
                return null;
            }
        } else {
            Log.d(TAG, "doInBackground: jSONObeject was null");
            return null;
        }

        Log.d(TAG, "doInBackground: succesfully loaded search");

        if (tweets == null) {
            Log.d(TAG, "doInBackground: tweets is null");
        }
        return tweets;
    }

    @Override
    protected void onPostExecute(Tweet[] tweets) {
        MainActivity m = MainActivity.getInstance();

        ArrayList<Tweet> list = new ArrayList<>();

        for (Tweet t : tweets) {
            list.add(t);
        }
        m.updateView(list);
    }
}
