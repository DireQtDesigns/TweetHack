package edu.saxion.kuiperklaczynski.tweethack.net;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

/**
 * Created by Robin on 24-5-2016.
 */
public class UnRetweetTask extends AsyncTask<Context,Void,String>{

    /**
     * Retweets a tweet by submitting a POST-request to "https://api.twitter.com/1.1/statuses/retweet/" where only the tweet-id is required as argument. Sets the opacity of the retweet imageview in onPostExecute.
     */


    private static final String API_KEY = "4LiUJZIHjuFT6IVaGBCZooSRw", API_SECRET = "yrxAVjSOd7oyqOKCSwpAVKCsktOlw0rR8ZwjGUOQNnyxiz13QL";
    private static String callback = "http://www.4chan.org";
    private final String TAG = "RetweetTask";
    private OAuth1AccessToken accessToken;
    private long id;
    private ImageView retweetView;
    private String response;

    final OAuth10aService service = new ServiceBuilder()
                .apiKey(API_KEY)
                .apiSecret(API_SECRET)
                .callback(callback)
                .build(TwitterApi.instance());

    public UnRetweetTask(ImageView retweetView, long id, OAuth1AccessToken accessToken) {
        this.retweetView = retweetView;
        this.id = id;
        this.accessToken = accessToken;
    }

    @Override
    protected void onPostExecute(String s) {
        retweetView.setAlpha((float) 0.5);
    }

    @Override
    protected String doInBackground(Context... params) {
        try {
            String resourceURL = "https://api.twitter.com/1.1/statuses/unretweet/";
            resourceURL += id;
            resourceURL += ".json";

            final OAuthRequest request = new OAuthRequest(Verb.POST, resourceURL, service);
            service.signRequest(accessToken, request);
            response = request.send().toString();
            Log.d(TAG, "doInBackground: Retweet Response: "+response);
        } catch(Exception e) {
            Log.e(TAG, "doInBackground: ", e);
        }
        return response;
    }

}
