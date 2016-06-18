package edu.saxion.kuiperklaczynski.tweethack.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import edu.saxion.kuiperklaczynski.tweethack.gui.MainActivity;

/**
 * Created by Robin on 24-5-2016.
 */
public class AccessTokenTraderTask extends AsyncTask<Context,Void,String>{
    private static final String API_KEY = "4LiUJZIHjuFT6IVaGBCZooSRw", API_SECRET = "yrxAVjSOd7oyqOKCSwpAVKCsktOlw0rR8ZwjGUOQNnyxiz13QL";
    private static String callback = "http://www.4chan.org";
    private final String TAG = "RequestToken";
    private String verifier;
    private OAuth1RequestToken requestToken;
    private OAuth1AccessToken accessToken;
    private Context c;

    final OAuth10aService service = new ServiceBuilder()
                .apiKey(API_KEY)
                .apiSecret(API_SECRET)
                .callback(callback)
                .build(TwitterApi.instance());

    public AccessTokenTraderTask(OAuth1RequestToken requestToken, String verifier, Context c) {
        this.verifier = verifier;
        this.requestToken = requestToken;
        this.c = c;
    }

    @Override
    protected void onPostExecute(String s) {
        SharedPreferences prefs = c.getSharedPreferences("edu.saxion.kuiperklaczynski.tweethack", c.MODE_PRIVATE);
        prefs.edit().putString("access_token", accessToken.getToken()).apply();
        prefs.edit().putString("access_token_secret", accessToken.getTokenSecret()).apply();
        MainActivity.getInstance().fillAccessTokens(accessToken.getToken(), accessToken.getTokenSecret());
        Toast.makeText(c, "Login successful:"+accessToken.getToken(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onPostExecute: " + s);
    }

    @Override
    protected String doInBackground(Context... params) {
        try {
            final OAuth1AccessToken accessToken = service.getAccessToken(requestToken, verifier);
            SharedPreferences prefs = c.getSharedPreferences("edu.saxion.kuiperklaczynski.tweethack", c.MODE_PRIVATE);
            this.accessToken = accessToken;
            prefs.edit().putString("access_token", accessToken.getToken()).apply();
            prefs.edit().putString("access_token_secret", accessToken.getTokenSecret()).apply();
            Log.d(TAG, "doInBackground: Tokens: " + accessToken.getRawResponse());
        } catch(Exception e) {
            Log.e(TAG, "doInBackground: ", e);
        }
        return service.getAuthorizationUrl(requestToken);
    }

}
