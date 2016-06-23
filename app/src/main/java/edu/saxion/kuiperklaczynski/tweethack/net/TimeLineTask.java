package edu.saxion.kuiperklaczynski.tweethack.net;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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

    /**
     * TODO: Robin
     */


    private final String TAG = "TimeLineTask";

    @Override
    protected ArrayList<Tweet> doInBackground(String... params) {

        OAuth1AccessToken accessToken = new OAuth1AccessToken(params[0], params[1]);

        String url;

        if (params[2] != null) {
            url = ("https://api.twitter.com/1.1/statuses/home_timeline.json" + params[2]);
        } else {
            url = ("https://api.twitter.com/1.1/statuses/home_timeline.json");
        }

        JSONArray jsonArray;

        OAuth10aService service = RequestTokenTask.service;
        Response response;

        OAuthRequest request = new OAuthRequest(Verb.GET, url, service);
        service.signRequest(accessToken, request);
        response = request.send();

        if (response.getCode() != 200) {
            Log.d(TAG, "doInBackground: response was " + response.getCode());
            return null;
        }

        try {
            jsonArray = new JSONArray(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ArrayList<Tweet> tweets;

        if (jsonArray != null) {
            try {
                tweets = JSONLoading.readJSONArray(jsonArray);
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

        if (!params[2].isEmpty()) {
            Log.d(TAG, "doInBackground: there is an additional modifier present");
            if (params[2].contains("since_id")) {
                Log.d(TAG, "doInBackground: since_id has been used");
                tweets.add(null);
            }
            tweets.add(null);
        }

        return tweets;
    }

    @Override
    protected void onPostExecute(ArrayList<Tweet> tweets) {
        if (tweets == null) return;

        MainActivity m = MainActivity.getInstance();

        if (tweets.size() != 0) {
            if (tweets.get(tweets.size()-1) == null) {
                tweets.remove(tweets.size() - 1);

                if (tweets.size() != 0) {
                    if (tweets.get(tweets.size() - 1) == null) {
                        tweets.remove(tweets.size() - 1);
                        m.topUpTweetsList(tweets, ListType.HOME);
                        return;
                    }
                }
                m.addItems(tweets);
                return;
            }
        }
        m.updateView(tweets);
    }
}
