package edu.saxion.kuiperklaczynski.tweethack.net;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import edu.saxion.kuiperklaczynski.tweethack.gui.ListType;
import edu.saxion.kuiperklaczynski.tweethack.gui.MainActivity;
import edu.saxion.kuiperklaczynski.tweethack.io.JSONLoading;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;

/**
 * Created by Robin on 15-6-2016.
 */
public class SearchTask extends AsyncTask<String, Void, ArrayList<Tweet>> {
    private final String TAG = "SearchTask";


    @Override
    protected ArrayList<Tweet> doInBackground(String... params) {
        String token;
        String identifier;

        if (params[0] != null) {
            token = params[0];
            identifier = "";
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
            if (params[3] != null) {
                url = new URL("https://api.twitter.com/1.1/search/tweets.json?q=" + searchQuery + params[3]);
                Log.d(TAG, "doInBackground: loading additional search results");
            } else {
                url = new URL("https://api.twitter.com/1.1/search/tweets.json?q=" + searchQuery);
                Log.d(TAG, "doInBackground: loading search results");
            }
        } catch (MalformedURLException mue) {
            Log.e(TAG, "doInBackground: ", mue);
            return null;
        }



        JSONObject jSONObject = null;

        try {
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            //TODO: add user token support
            conn.addRequestProperty("Authorization", identifier + token);

            conn.setRequestMethod("GET");
            if (HttpsURLConnection.HTTP_OK == conn.getResponseCode()) {
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

        ArrayList<Tweet> tweets;

        if (jSONObject != null) {
            try {
                tweets = JSONLoading.readJSON(jSONObject.toString());
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: ", e);
                return null;
            }
        } else {
            Log.d(TAG, "doInBackground: jSONObject was null");
            return null;
        }

        Log.d(TAG, "doInBackground: succesfully loaded search");

        if (tweets == null) {
            Log.d(TAG, "doInBackground: tweets is null");
        }

        if (params[3] != null) {
            Log.d(TAG, "doInBackground: there is an additional modifier present");
            if (params[3].contains("since_id")) {
                Log.d(TAG, "doInBackground: since_id has been used");
                tweets.add(null);
            }
            tweets.add(null);
        }
        return tweets;
    }

    @Override
    protected void onPostExecute(ArrayList<Tweet> tweets) {
        MainActivity m = MainActivity.getInstance();

        if (tweets.get(tweets.size()-1) == null) {
            tweets.remove(tweets.size()-1);
            if (tweets.get(tweets.size()-1) == null) {
                tweets.remove(tweets.size()-1);
                m.topUpTweetsList(tweets, ListType.SEARCH);
            } else {
                m.addItems(tweets);
            }
        } else {
            m.updateView(tweets);
        }
    }
}
