package edu.saxion.kuiperklaczynski.tweethack.net;

import android.os.AsyncTask;
import android.util.Log;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.spec.ECField;
import java.util.ArrayList;

import edu.saxion.kuiperklaczynski.tweethack.gui.UserActivity;
import edu.saxion.kuiperklaczynski.tweethack.io.JSONLoading;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;
import edu.saxion.kuiperklaczynski.tweethack.objects.User;

/**
 * Created by Robin on 19-6-2016.
 */
public class UserTweetsTask extends AsyncTask<String, Void, ArrayList<Tweet>>{
    private static final String TAG = "UserTweetsTask";

    @Override
    protected ArrayList<Tweet> doInBackground(String... params) {
        OAuth1AccessToken accessToken = new OAuth1AccessToken(params[0], params[1]);

        String url = ("https://api.twitter.com/1.1/statuses/user_timeline.json?user_id=" + Long.parseLong(params[2]) + params[3]);

        Log.d(TAG, "doInBackground: " + url);


        OAuth10aService service = RequestTokenTask.service;
        Response response;

        OAuthRequest request = new OAuthRequest(Verb.GET, url, service);
        service.signRequest(accessToken, request);
        response = request.send();

        if (response.getCode() != 200) {
            Log.d(TAG, "doInBackground: response was " + response.getCode());
            return null;
        }

        JSONArray jsonArray;

        try {
            jsonArray = new JSONArray(response.getBody());
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        ArrayList<Tweet> tweets;

        try {
            tweets = JSONLoading.readJSONArray(jsonArray);
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        Log.d(TAG, "doInBackground: " + jsonArray.toString());

        return tweets;
    }

    @Override
    protected void onPostExecute(ArrayList<Tweet> tweets) {
        ArrayList<Tweet> twitters = new ArrayList<>();

        for (int i = tweets.size() - 1; i >= 0; i--) {
            twitters.add(tweets.get(i));
        }

        UserActivity.getInstance().addTweetsList(twitters);
    }
}
