package edu.saxion.kuiperklaczynski.tweethack.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import edu.saxion.kuiperklaczynski.tweethack.gui.AuthActivity;
import edu.saxion.kuiperklaczynski.tweethack.gui.MainActivity;

/**
 * Created by Robin on 24-5-2016.
 */
public class RequestTokenTask extends AsyncTask<Context,Void,String>{
    private static final String API_KEY = "4LiUJZIHjuFT6IVaGBCZooSRw", API_SECRET = "yrxAVjSOd7oyqOKCSwpAVKCsktOlw0rR8ZwjGUOQNnyxiz13QL";
    private static String callback = "http://www.4chan.org";
    private WebView webView;
    private final String TAG = "RequestToken";
    private OAuth1RequestToken requestToken;

    public static final OAuth10aService service = new ServiceBuilder()
                .apiKey(API_KEY)
                .apiSecret(API_SECRET)
                .callback(callback)
                .build(TwitterApi.instance());

    public RequestTokenTask(WebView webView) {
        this.webView = webView;
    }

    @Override
    protected void onPostExecute(String s) {
        webView.loadUrl(s);
        Log.d(TAG, "onPostExecute: " + s);
    }

    @Override
    protected String doInBackground(Context... params) {
        OAuth1RequestToken requestToken = new OAuth1RequestToken("", "");
        try {
            requestToken = service.getRequestToken();
            AuthActivity.requestToken = requestToken;
        } catch(Exception e) {
            Log.e(TAG, "doInBackground: ", e);
        }
        return service.getAuthorizationUrl(requestToken);
    }

}
