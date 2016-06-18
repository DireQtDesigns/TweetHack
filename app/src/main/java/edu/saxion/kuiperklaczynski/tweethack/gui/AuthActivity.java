package edu.saxion.kuiperklaczynski.tweethack.gui;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import edu.saxion.kuiperklaczynski.tweethack.R;
import edu.saxion.kuiperklaczynski.tweethack.net.AccessTokenTraderTask;
import edu.saxion.kuiperklaczynski.tweethack.net.RequestTokenTask;

public class AuthActivity extends AppCompatActivity {

    private static final String API_KEY = "4LiUJZIHjuFT6IVaGBCZooSRw", API_SECRET = "yrxAVjSOd7oyqOKCSwpAVKCsktOlw0rR8ZwjGUOQNnyxiz13QL";
    private static final String TAG = "AuthActivity";
    private static String callback = "http://www.4chan.org";

    public static OAuth1RequestToken requestToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        final WebView webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        new RequestTokenTask(webView).execute();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if(url.contains("4chan.org") && requestToken != null) {
                        Uri uri = Uri.parse(url);
                        String verifier = uri.getQueryParameter("oauth_verifier");
                        Log.d(TAG, "shouldOverrideUrlLoading: " + requestToken.getToken());
                        new AccessTokenTraderTask(requestToken, verifier, getApplicationContext()).execute();
                        finish();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "shouldOverrideUrlLoading: ", e);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
}
