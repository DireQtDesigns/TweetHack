package edu.saxion.kuiperklaczynski.tweethack.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.ortiz.touch.TouchImageView;

import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.saxion.kuiperklaczynski.tweethack.R;
import edu.saxion.kuiperklaczynski.tweethack.io.JSONLoading;
import edu.saxion.kuiperklaczynski.tweethack.net.DownloadImageTask;
import edu.saxion.kuiperklaczynski.tweethack.net.FavoriteTask;
import edu.saxion.kuiperklaczynski.tweethack.net.RetweetTask;
import edu.saxion.kuiperklaczynski.tweethack.net.URLTesting;
import edu.saxion.kuiperklaczynski.tweethack.net.UnFavoriteTask;
import edu.saxion.kuiperklaczynski.tweethack.net.UnRetweetTask;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;
import edu.saxion.kuiperklaczynski.tweethack.objects.User;
import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.entities.URL;

public class TweetDetailActivity extends AppCompatActivity {

    public static ArrayList<Tweet> tweetsList = new ArrayList<>(), repliesTo = new ArrayList<>();
    public static Map<Long, Tweet> tweetsMap = new HashMap<>(); //id_str is key
    private static final String TAG = "TweetHax_TweetDetail"; //Log Tag
    private String fullName, username, avatarURL, body, timeAgo, id_str, media_url, url, expanded_url;
    private int[] mediaIndices;
    private long id;
    TweetListAdapter tweetListAdapter;
    public Tweet detailTweet;
    private boolean hasGif = false;


    /**
     * initializes the activity by getting the relevant tweet from the MainActitivity and filling views with the data
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tweetsMap = JSONLoading.tweetsMap;
        tweetsList = JSONLoading.tweetsList;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getLongExtra("TweetID", -1);
        if (id == -1) {
            Log.e(TAG, "onCreate: TweetID = null");
        } else {
            detailTweet = tweetsMap.get(id);
            fullName = detailTweet.getUser().getName();
            username = detailTweet.getUser().getScreenname();
            avatarURL = detailTweet.getUser().getProfile_image_url();
            body = detailTweet.getText();
            timeAgo = StringDateConverter.agoString(System.currentTimeMillis(), StringDateConverter.dateFromJSONString(detailTweet.getCreated_at()));
            id_str = detailTweet.getIdStr();
            if(detailTweet.getMedia() != null) {
                media_url = detailTweet.getMedia().getMedia_URL();
                mediaIndices = detailTweet.getMedia().getIndices();
            }
            if(detailTweet.getMedia() != null) {
                if(detailTweet.getMedia().getURL() != null) {
                    url = detailTweet.getMedia().getURL();
                }
                if(detailTweet.getMedia().getExpandedURL() != null) {
                    expanded_url = detailTweet.getMedia().getExpandedURL();
                }
            }
            if(expanded_url != null && !expanded_url.equals("") && url != null && !url.equals("")) {
                body.replace(url, "");
            }
            if(detailTweet.hasGif()) {
                hasGif = true;
            }
        }


        tweetListAdapter = new TweetListAdapter(this, R.layout.tweet_list_item, repliesTo);
        ListView replyList = (ListView) findViewById(R.id.tweetDetailReplyList);
        if (repliesTo.size() > 0) {
            TextView noReplyView = (TextView) findViewById(R.id.tweetDetailNoRepliesView);
            noReplyView.setVisibility(View.GONE);
        }
        replyList.setAdapter(tweetListAdapter);

        replyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TweetDetailActivity.this, TweetDetailActivity.class);
                Tweet tweet = tweetsList.get(position);
                intent.putExtra("TweetID", tweet.getId());
                startActivity(intent);
            }
        });
        setTitle("@"+fullName+": "+"'"+body+"'");

        TextView nameView = (TextView) findViewById(R.id.tweetDetailNameView);
        TextView usernameView = (TextView) findViewById(R.id.tweetDetailUsernameView);
        TextView timeAgoView = (TextView) findViewById(R.id.tweetDetailTimeAgo);
        TextView bodyView = (TextView) findViewById(R.id.tweetDetailBodyView);
        ImageView avatarView = (ImageView) findViewById(R.id.tweetDetailAvatarView);
        final ImageView retweetView = (ImageView) findViewById(R.id.tweetDetailRetweet);
        final ImageView favoriteView = (ImageView) findViewById(R.id.tweetDetailFavorite);
        final EditText replyField = (EditText) findViewById(R.id.tweetDetailReplyField);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(TweetDetailActivity.this,UserActivity.class);
                profileIntent.putExtra("TweetID", detailTweet.getUser().getId());
                startActivity(profileIntent);
            }
        };

        nameView.setOnClickListener(listener);
        usernameView.setOnClickListener(listener);
        avatarView.setOnClickListener(listener);

        nameView.setText(fullName);
        usernameView.setText("@" + username);

        if(hasGif) {
            bodyView.setText(Html.fromHtml(body + "<font color=red> (NO GIF SUPPORT)"));
        } else bodyView.setText(body);

        timeAgoView.setText(timeAgo);
        replyField.setText("@" + username + " ");

                new DownloadImageTask(avatarView).execute(avatarURL);
        replyField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(140)});
        replyField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    sendTweet(replyField.getText().toString(), replyField);
                    replyField.setText("@" + username + " ");
                }
                return true;
            }
        });

        retweetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OAuth1AccessToken accessToken = getAccessToken();
                if(retweetView.getAlpha() < 0.7) {
                    new RetweetTask(retweetView, detailTweet.getId(), accessToken).execute();
                    Toast.makeText(getApplicationContext(), "Retweeting...", Toast.LENGTH_SHORT);
                } else {
                    new UnRetweetTask(retweetView, detailTweet.getId(), accessToken).execute();
                    Toast.makeText(getApplicationContext(), "Reverting retweet...", Toast.LENGTH_SHORT);
                }
            }
        });

        favoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OAuth1AccessToken accessToken = getAccessToken();
                if(favoriteView.getAlpha() < 0.7) {
                    new FavoriteTask(favoriteView, detailTweet.getId(), accessToken).execute();
                    Toast.makeText(getApplicationContext(), "Favoriting...", Toast.LENGTH_SHORT);
                } else {
                    new UnFavoriteTask(favoriteView, detailTweet.getId(), accessToken).execute();
                    Toast.makeText(getApplicationContext(), "Reverting favorite...", Toast.LENGTH_SHORT);
                }
            }
        });
        if(media_url != null && !media_url.equals("")) {
            TouchImageView tiv = (TouchImageView) findViewById(R.id.tweetDetailTweetImage);
            final ScrollView sv = (ScrollView) findViewById(R.id.tweetDetailScrollView);
            tiv.setVisibility(View.VISIBLE);
            new DownloadImageTask(tiv).execute(media_url);

            tiv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {

                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_POINTER_DOWN:

                            sv.requestDisallowInterceptTouchEvent(true);
                            return true;

                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_POINTER_UP:

                            sv.requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                    return false;
                }
            });
        }/**
        WebView wv = (WebView) findViewById(R.id.tweetDetailWebView);
        if(expanded_url != null && !expanded_url.equals("") && url != null && !url.equals("") && (media_url == null || media_url.equals(""))) {
            wv.loadUrl(expanded_url);
            wv.setVisibility(View.VISIBLE);
        } else if(url != null && !url.equals("")) {
            wv.loadUrl(url);
            wv.setVisibility(View.VISIBLE);
        } else {
            wv.setVisibility(View.GONE);
        }**/ //Does random unexplainable crap.
    }

    /**
     * enables sending a tweet to the sender of the featured tweet
     * @param text
     * @param view
     */
    private void sendTweet(String text, View view) {
        Intent intent = new Intent(this, NewTweetActivity.class);
        intent.putExtra("body", text);
        intent.putExtra("replyTo", detailTweet.getId());
        Log.d(TAG, "sendTweet: fetched id: "+detailTweet.getId());
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        //tweetsList.clear();
        //tweetsMap.clear();
        //tweetListAdapter.notifyDataSetChanged();
        finish();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * gets the OAuthAccessToken
     * @return OAuthAccessToken
     */
    public OAuth1AccessToken getAccessToken() {
        SharedPreferences prefs = getSharedPreferences("edu.saxion.kuiperklaczynski.tweethack", MODE_PRIVATE);
        String accessTokenStr = prefs.getString("access_token", "");
        String accessTokenSecretStr = prefs.getString("access_token_secret", "");
        OAuth1AccessToken accessToken = new OAuth1AccessToken(accessTokenStr, accessTokenSecretStr);
        return accessToken;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
