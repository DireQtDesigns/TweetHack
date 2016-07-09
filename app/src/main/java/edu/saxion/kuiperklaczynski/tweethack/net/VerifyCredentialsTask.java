package edu.saxion.kuiperklaczynski.tweethack.net;

import android.os.AsyncTask;
import android.util.Log;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONException;
import org.json.JSONObject;

import edu.saxion.kuiperklaczynski.tweethack.gui.MainActivity;
import edu.saxion.kuiperklaczynski.tweethack.io.JSONLoading;
import edu.saxion.kuiperklaczynski.tweethack.objects.AccessTokenInfo;
import edu.saxion.kuiperklaczynski.tweethack.objects.User;

/**
 * Created by Robin on 19-6-2016.
 */
public class VerifyCredentialsTask extends AsyncTask<String, Void, User> {
    private static final String TAG = "VerifyCredentialsTask";

    @Override
    protected User doInBackground(String... params) {
        OAuth1AccessToken accessToken = new OAuth1AccessToken(params[0], params[1]);

        String url = ("https://api.twitter.com/1.1/account/verify_credentials.json");

        OAuth10aService service = AccessTokenInfo.getService();
        Response response;

        OAuthRequest request = new OAuthRequest(Verb.GET, url, service);
        service.signRequest(accessToken, request);
        response = request.send();

        if (response.getCode() != 200) {
            Log.d(TAG, "doInBackground: response was " + response.getCode());
            return null;
        }

        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(response.getBody());
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        User user = new User();

        try {
            JSONLoading.fillUser(jsonObject, user);
        } catch (JSONException e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        return user;
    }

    @Override
    protected void onPostExecute(User user) {
        MainActivity.getInstance().fillUser(user);
    }
}
