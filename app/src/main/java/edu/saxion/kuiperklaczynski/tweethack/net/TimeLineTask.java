package edu.saxion.kuiperklaczynski.tweethack.net;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import edu.saxion.kuiperklaczynski.tweethack.gui.ListType;
import edu.saxion.kuiperklaczynski.tweethack.gui.MainActivity;
import edu.saxion.kuiperklaczynski.tweethack.io.JSONLoading;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;

/**
 * Created by Robin on 18-6-2016.
 */
public class TimeLineTask extends AsyncTask<String, Void, ArrayList<Tweet>> {
    private final String TAG = "TimeLineTask";

    @Override
    protected ArrayList<Tweet> doInBackground(String... params) {

        String authToken = params[0];

        URL url;
        try {
            if (params[1] != null) {
                url = new URL("https://api.twitter.com/1.1/statuses/home_timeline.json" + params[1]);
            } else {
                url = new URL("https://api.twitter.com/1.1/statuses/home_timeline.json");
            }
        } catch (MalformedURLException mue) {
            Log.e(TAG, "doInBackground: ", mue);
            return null;
        }

        JSONObject jSONObject = null;

        try {
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.addRequestProperty("Authorization", authToken);
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
                m.topUpTweetsList(tweets, ListType.HOME);
            } else {
                m.addItems(tweets);
            }
        } else {
            m.updateView(tweets);
        }
    }
}
