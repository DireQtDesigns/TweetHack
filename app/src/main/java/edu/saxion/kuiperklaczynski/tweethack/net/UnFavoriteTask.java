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
public class UnFavoriteTask extends AsyncTask<Context,Void,Integer>{

    /**
     * Submits a POST-request to "https://api.twitter.com/1.1/favorites/destroy.json" to favorite a tweet, only the tweet-id is required as an argument.  Sets the opacity of the favorite imageview in onPostExecute.
     */

    private final String TAG = "UnFavoriteTask";
    private OAuth1AccessToken accessToken;
    private long id;
    private ImageView favoriteView;
    private String response;

    public UnFavoriteTask(ImageView favoriteView, long id, OAuth1AccessToken accessToken) {
        this.favoriteView = favoriteView;
        this.id = id;
        this.accessToken = accessToken;
    }

    @Override
    protected void onPostExecute(Integer i) {
        if (i == 200) {
            favoriteView.setAlpha((float) 0.5);
        }
        MainActivity.networkFeedback(i);
    }

    @Override
    protected Integer doInBackground(Context... params) {
        int code;

        try {
            String resourceURL = "https://api.twitter.com/1.1/favorites/destroy.json";
            resourceURL += "?";
            resourceURL += "id="+id;

            final OAuthRequest request = new OAuthRequest(Verb.POST, resourceURL, AccessTokenInfo.getService());
            AccessTokenInfo.getService().signRequest(accessToken, request);

            Response r = request.send();
            response = r.toString();
            code = r.getCode();

            Log.d(TAG, "doInBackground: favorite Response: "+response);
        } catch(Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return -1;
        }
        return code;
    }

}
