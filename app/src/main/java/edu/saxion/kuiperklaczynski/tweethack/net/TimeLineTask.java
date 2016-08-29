package edu.saxion.kuiperklaczynski.tweethack.net;

import android.util.Log;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONArray;

import java.util.ArrayList;

import edu.saxion.kuiperklaczynski.tweethack.gui.ListType;
import edu.saxion.kuiperklaczynski.tweethack.gui.MainActivity;
import edu.saxion.kuiperklaczynski.tweethack.io.JSONLoading;
import edu.saxion.kuiperklaczynski.tweethack.objects.AccessTokenInfo;
import edu.saxion.kuiperklaczynski.tweethack.objects.ErrorCarrier;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;

/**
 * Created by Robin on 18-6-2016.
 */
public class TimeLineTask extends ErrorTask<String, Void, ArrayList<Tweet>> {

    private final String TAG = "TimeLineTask";

    /**
     * GET timeline of logged in user
     * @param params String 0 and 1 are AuthTokens and 2 is additional parameters
     * @return Tweetlist with Timeline
     */
    @Override
    protected ArrayList<Tweet> doInBackground(String... params) {
        ArrayList<Tweet> tweets;

        OAuth1AccessToken accessToken = new OAuth1AccessToken(params[0], params[1]);

        String url;

        if (params[2] != null) {
            url = ("https://api.twitter.com/1.1/statuses/home_timeline.json" + params[2]);
        } else {
            url = ("https://api.twitter.com/1.1/statuses/home_timeline.json");
        }

        JSONArray jsonArray;

        OAuth10aService service = AccessTokenInfo.getService();
        Response response;

        OAuthRequest request = new OAuthRequest(Verb.GET, url, service);
        service.signRequest(accessToken, request);
        response = request.send();

        if (response.getCode() != 200) {
            Log.d(TAG, "doInBackground: response was " + response.getCode());
            Log.d(TAG, "doInBackground: "+response.toString());
            return failureHelper(response.getCode());
        }

        try {
            jsonArray = new JSONArray(response.getBody());
        } catch (Exception e) {
            return failureHelper(-1);
        }

        if (jsonArray != null) {
            try {
                tweets = JSONLoading.readJSONArray(jsonArray);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: ", e);
                return failureHelper(-1);
            }
        } else {
            Log.d(TAG, "doInBackground: jSONObject was null");
            return failureHelper(-1);
        }

        Log.d(TAG, "doInBackground: succesfully loaded search");

        if (tweets == null) {
            Log.d(TAG, "doInBackground: tweets is null");
            return failureHelper(-1);
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

    /**
     * uses tweetlist with a certain method in the mainactivity, used method is based on given additional parameters in the doInBackground
     * @param tweets tweetList of timeline
     */
    @Override
    protected void onPostExecute(ArrayList<Tweet> tweets) {
        if (tweets.get(0) instanceof ErrorCarrier) {
            MainActivity.networkFeedback(((ErrorCarrier) tweets.get(0)).getErrorCode());
            return;
        }

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
