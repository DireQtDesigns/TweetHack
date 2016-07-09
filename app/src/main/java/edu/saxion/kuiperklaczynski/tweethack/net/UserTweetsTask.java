package edu.saxion.kuiperklaczynski.tweethack.net;

import android.util.Log;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONArray;

import java.util.ArrayList;

import edu.saxion.kuiperklaczynski.tweethack.gui.MainActivity;
import edu.saxion.kuiperklaczynski.tweethack.gui.UserActivity;
import edu.saxion.kuiperklaczynski.tweethack.io.JSONLoading;
import edu.saxion.kuiperklaczynski.tweethack.objects.AccessTokenInfo;
import edu.saxion.kuiperklaczynski.tweethack.objects.ErrorCarrier;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;

/**
 * Created by Robin on 19-6-2016.
 */
public class UserTweetsTask extends ErrorTask<String, Void, ArrayList<Tweet>>{
    private static final String TAG = "UserTweetsTask";

    @Override
    protected ArrayList<Tweet> doInBackground(String... params) {
        OAuth1AccessToken accessToken = new OAuth1AccessToken(params[0], params[1]);

        String url = ("https://api.twitter.com/1.1/statuses/user_timeline.json?user_id=" + Long.parseLong(params[2]) + params[3]);

        Log.d(TAG, "doInBackground: " + url);


        OAuth10aService service = AccessTokenInfo.getService();
        Response response;

        OAuthRequest request = new OAuthRequest(Verb.GET, url, service);
        service.signRequest(accessToken, request);
        response = request.send();

        if (response.getCode() != 200) {
            Log.d(TAG, "doInBackground: response was " + response.getCode());
            return failureHelper(response.getCode());
        }

        JSONArray jsonArray;

        try {
            jsonArray = new JSONArray(response.getBody());
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return failureHelper(-1);
        }

        ArrayList<Tweet> tweets;

        try {
            tweets = JSONLoading.readJSONArray(jsonArray);
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return failureHelper(-1);
        }

        Log.d(TAG, "doInBackground: " + jsonArray.toString());

        return tweets;
    }

    @Override
    protected void onPostExecute(ArrayList<Tweet> tweets) {
        if (tweets.get(0) instanceof ErrorCarrier) {
            MainActivity.networkFeedback(((ErrorCarrier) tweets.get(0)).getErrorCode());
            return;
        }

        ArrayList<Tweet> twitters = new ArrayList<>();

        for (int i = tweets.size() - 1; i >= 0; i--) {
            twitters.add(tweets.get(i));
        }

        UserActivity.getInstance().addTweetsList(twitters);
    }
}
