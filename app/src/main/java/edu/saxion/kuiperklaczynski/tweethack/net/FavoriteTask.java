package edu.saxion.kuiperklaczynski.tweethack.net;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;

import edu.saxion.kuiperklaczynski.tweethack.gui.MainActivity;
import edu.saxion.kuiperklaczynski.tweethack.objects.AccessTokenInfo;

/**
 * Created by leonk on 24-5-2016.
 */
public class FavoriteTask extends AsyncTask<Context,Void,Integer>{

    /**
     * Submits a POST-request to "https://api.twitter.com/1.1/favorites/create.json" to favorite a tweet, only the tweet-id is required as an argument.  Sets the opacity of the favorite imageview in onPostExecute.
     */
    private final String TAG = "favoriteTask";
    private OAuth1AccessToken accessToken;
    private long id;
    private ImageView favoriteView;
    private String response;

    public FavoriteTask(ImageView favoriteView, long id, OAuth1AccessToken accessToken) {
        this.favoriteView = favoriteView;
        this.id = id;
        this.accessToken = accessToken;
    }

    @Override
    protected void onPostExecute(Integer i) {
        if (i == 200) {
            favoriteView.setAlpha((float) 1);
        }
        MainActivity.networkFeedback(i);

    }

    @Override
    protected Integer doInBackground(Context... params) {
        int code;

        try {
            String resourceURL = "https://api.twitter.com/1.1/favorites/create.json";
            resourceURL += "?";
            resourceURL += "id="+id;

            final OAuthRequest request = new OAuthRequest(Verb.POST, resourceURL, AccessTokenInfo.getService());
            AccessTokenInfo.getService().signRequest(accessToken, request);
            Response r = request.send();
            response = r.toString();

            Log.d(TAG, "doInBackground: favorite Response: "+response);

            code = r.getCode();

        } catch(Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return -1;
        }
        return code;
    }

}
