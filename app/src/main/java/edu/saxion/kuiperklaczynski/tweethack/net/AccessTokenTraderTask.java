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
import edu.saxion.kuiperklaczynski.tweethack.objects.AccessTokenInfo;

/**
 * Created by Leonk on 18-6-2016.
 */
public class AccessTokenTraderTask extends AsyncTask<Context,Void,String> {

    /**
     * Fetches the Access token en -secret by trading in the request tokens and verifier for the access tokens, which will be saved in the savedPreferences in onPostExecute().
     */

    private final String TAG = "AccessTokenTask";
    private String verifier;
    private OAuth1RequestToken requestToken;
    private OAuth1AccessToken accessToken;
    private Context c;

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

        new VerifyCredentialsTask().execute(new String[]{accessToken.getToken(), accessToken.getTokenSecret()});
    }

    @Override
    protected String doInBackground(Context... params) {
        try {
            final OAuth1AccessToken accessToken = AccessTokenInfo.getService().getAccessToken(requestToken, verifier);
            SharedPreferences prefs = c.getSharedPreferences("edu.saxion.kuiperklaczynski.tweethack", c.MODE_PRIVATE);
            this.accessToken = accessToken;
            prefs.edit().putString("access_token", accessToken.getToken()).apply();
            prefs.edit().putString("access_token_secret", accessToken.getTokenSecret()).apply();
            Log.d(TAG, "doInBackground: Tokens: " + accessToken.getRawResponse());
        } catch(Exception e) {
            Log.e(TAG, "doInBackground: ", e);
        }
        return AccessTokenInfo.getService().getAuthorizationUrl(requestToken);
    }

}
