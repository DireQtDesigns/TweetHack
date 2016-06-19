package edu.saxion.kuiperklaczynski.tweethack.net;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

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
        JSONObject jSONObject = null;
        String searchQuery;

        try {
            searchQuery = URLEncoder.encode(params[3], "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            Log.e(TAG, "doInBackground: ", uee );
            return null;
        }

        //if we're using accessTokens
        if (params[0] != null) {

            OAuth1AccessToken accessToken = new OAuth1AccessToken(params[0], params[1]);

            String url;
            if (params[4] != null) {
                url = "https://api.twitter.com/1.1/search/tweets.json?q=" + searchQuery + params[4];
            } else {
                url = "https://api.twitter.com/1.1/search/tweets.json?q=" + searchQuery;
            }

            OAuth10aService service = RequestTokenTask.service;
            Response response;

            OAuthRequest request = new OAuthRequest(Verb.GET, url, service);
            service.signRequest(accessToken, request);
            response = request.send();

            if (response.getCode() != 200) {
                Log.d(TAG, "doInBackground: code was " + response.getCode());
                return null;
            }

            try {
                jSONObject = new JSONObject(response.getBody());
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: ", e);
            }

        //if we're using bearerTokens
        } else if (params[2] != null) {
            String token = "Bearer " + params[2];

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

            try {
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                //TODO: add user token support
                conn.addRequestProperty("Authorization", token);

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

        } else {
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

        if (!params[4].isEmpty()) {
            Log.d(TAG, "doInBackground: there is an additional modifier present");
            if (params[4].contains("since_id")) {
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
                        m.topUpTweetsList(tweets, ListType.SEARCH);
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
