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

import org.json.JSONObject;

import edu.saxion.kuiperklaczynski.tweethack.objects.AccessTokenInfo;

/**
 * Created by Robin on 24-5-2016.
 */
public class UserBannerTask extends AsyncTask<Context,Void,String>{

    /**
     * Fetches the user banner image by issueing a GET-request
     */
    private final String TAG = "favoriteTask";
    private String imgUrl;
    private OAuth1AccessToken accessToken;
    private String screenName;
    private ImageView bannerView;
    private String response;

    public UserBannerTask(ImageView bannerView, String screenName, OAuth1AccessToken accessToken) {
        this.bannerView = bannerView;
        this.screenName = screenName;
        this.accessToken = accessToken;
    }

    @Override
    protected void onPostExecute(String s) {
        new DownloadImageTask(bannerView).execute(s);
    }

    @Override
    protected String doInBackground(Context... params) {
        try {
            String resourceURL = "https://api.twitter.com/1.1/users/profile_banner.json";
            resourceURL += "?";
            resourceURL += "screen_name="+screenName;

            final OAuthRequest request = new OAuthRequest(Verb.GET, resourceURL, AccessTokenInfo.getService());
            AccessTokenInfo.getService().signRequest(accessToken, request);
            String json = request.send().getBody();
            Log.d(TAG, "doInBackground: Banner Response"+json);
            JSONObject jason = new JSONObject(json);
            JSONObject sizes = jason.getJSONObject("sizes");
            JSONObject xl = sizes.getJSONObject("mobile_retina");
            imgUrl = xl.getString("url");
        } catch(Exception e) {
            Log.e(TAG, "doInBackground: ", e);
        }
        return imgUrl;
    }

}
