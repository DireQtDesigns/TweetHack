package edu.saxion.kuiperklaczynski.tweethack.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.net.URLEncoder;

import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;

/**
 * Created by Robin on 24-5-2016.
 */
public class SendTweetTask extends AsyncTask<Context,Void,String>{
    private static final String API_KEY = "4LiUJZIHjuFT6IVaGBCZooSRw", API_SECRET = "yrxAVjSOd7oyqOKCSwpAVKCsktOlw0rR8ZwjGUOQNnyxiz13QL";
    private static String callback = "http://www.4chan.org";
    private OAuth1AccessToken accessToken;
    private final String TAG = "SendTweetTask";
    private Tweet tweet;
    private Context c;

    final OAuth10aService service = new ServiceBuilder()
                .apiKey(API_KEY)
                .apiSecret(API_SECRET)
                .callback(callback)
                .build(TwitterApi.instance());

    public SendTweetTask(Tweet tweet, Context c, OAuth1AccessToken accessToken) {
        this.accessToken = accessToken;
        this.tweet = tweet;
        this.c = c;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(c, "Sent tweet successfully", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onPostExecute: " + s);
    }

    @Override
    protected String doInBackground(Context... params) {
        String result = "";
        try {
            String url = "https://api.twitter.com/1.1/statuses/update.json";
            url += "?status=";
            url+= URLEncoder.encode(tweet.getText(), "UTF-8");
            if(tweet.getIn_reply_to_status_id() != 0) {
                url += "&in_reply_to_status_id";
                url += tweet.getIn_reply_to_status_id();
                Log.d(TAG, "doInBackground: Adding replyId to Url: "+tweet.getIn_reply_to_status_id());
            }
            final OAuthRequest request = new OAuthRequest(Verb.POST, url, service);
            service.signRequest(accessToken, request);
            Response res = request.send();
            Log.d(TAG, "doInBackground: "+res.toString());

        } catch(Exception e) {
            Log.e(TAG, "doInBackground: ", e);
        }
        return result;
    }

}
