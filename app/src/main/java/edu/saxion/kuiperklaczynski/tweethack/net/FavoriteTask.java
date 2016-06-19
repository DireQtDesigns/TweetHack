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
public class FavoriteTask extends AsyncTask<Context,Void,String>{
    private static final String API_KEY = "4LiUJZIHjuFT6IVaGBCZooSRw", API_SECRET = "yrxAVjSOd7oyqOKCSwpAVKCsktOlw0rR8ZwjGUOQNnyxiz13QL";
    private static String callback = "http://www.4chan.org";
    private final String TAG = "favoriteTask";
    private OAuth1AccessToken accessToken;
    private long id;
    private ImageView favoriteView;
    private String response;

    final OAuth10aService service = new ServiceBuilder()
                .apiKey(API_KEY)
                .apiSecret(API_SECRET)
                .callback(callback)
                .build(TwitterApi.instance());

    public FavoriteTask(ImageView favoriteView, long id, OAuth1AccessToken accessToken) {
        this.favoriteView = favoriteView;
        this.id = id;
        this.accessToken = accessToken;
    }

    @Override
    protected void onPostExecute(String s) {
        favoriteView.setAlpha((float) 1);
    }

    @Override
    protected String doInBackground(Context... params) {
        try {
            String resourceURL = "https://api.twitter.com/1.1/favorites/create.json";
            resourceURL += "?";
            resourceURL += "id="+id;

            final OAuthRequest request = new OAuthRequest(Verb.POST, resourceURL, service);
            service.signRequest(accessToken, request);
            response = request.send().toString();
            Log.d(TAG, "doInBackground: favorite Response: "+response);
        } catch(Exception e) {
            Log.e(TAG, "doInBackground: ", e);
        }
        return response;
    }

}
