package edu.saxion.kuiperklaczynski.tweethack.net;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import edu.saxion.kuiperklaczynski.tweethack.gui.MainActivity;
import edu.saxion.kuiperklaczynski.tweethack.objects.AccessTokenInfo;

/**
 * Created by Robin on 24-5-2016.
 */
public class UnRetweetTask extends AsyncTask<Context,Void,Integer>{

    /**
     * Retweets a tweet by submitting a POST-request to "https://api.twitter.com/1.1/statuses/retweet/" where only the tweet-id is required as argument. Sets the opacity of the retweet imageview in onPostExecute.
     */

    private final String TAG = "RetweetTask";
    private OAuth1AccessToken accessToken;
    private long id;
    private ImageView retweetView;
    private String response;

    public UnRetweetTask(ImageView retweetView, long id, OAuth1AccessToken accessToken) {
        this.retweetView = retweetView;
        this.id = id;
        this.accessToken = accessToken;
    }

    @Override
    protected void onPostExecute(Integer i) {
        if (i == 200) {
            retweetView.setAlpha((float) 0.5);
        }
        MainActivity.networkFeedback(i);
    }

    @Override
    protected Integer doInBackground(Context... params) {
        int code;

        try {
            String resourceURL = "https://api.twitter.com/1.1/statuses/unretweet/";
            resourceURL += id;
            resourceURL += ".json";

            final OAuthRequest request = new OAuthRequest(Verb.POST, resourceURL, AccessTokenInfo.getService());
            AccessTokenInfo.getService().signRequest(accessToken, request);

            Response r = request.send();
            response = r.toString();
            code = r.getCode();

            Log.d(TAG, "doInBackground: Retweet Response: "+response);
        } catch(Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return -1;
        }
        return code;
    }

}
