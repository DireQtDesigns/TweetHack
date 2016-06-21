package edu.saxion.kuiperklaczynski.tweethack.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.scribejava.core.model.OAuth1AccessToken;

import java.util.ArrayList;
import java.util.List;

import edu.saxion.kuiperklaczynski.tweethack.R;
import edu.saxion.kuiperklaczynski.tweethack.net.DownloadImageTask;
import edu.saxion.kuiperklaczynski.tweethack.net.UserBannerTask;
import edu.saxion.kuiperklaczynski.tweethack.net.UserTweetsTask;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;
import edu.saxion.kuiperklaczynski.tweethack.objects.User;

public class UserActivity extends AppCompatActivity {

    private static UserActivity instance;

    public static UserActivity getInstance() {
        if (instance == null) {
            return new UserActivity();
        }

        return instance;
    }

    private User user;

    public static List<Tweet> tweetsList = new ArrayList<>();

    public void addTweetsList(ArrayList<Tweet> list) {
        tweetsList.addAll(list);
        ((TweetListAdapter)tweetsListView.getAdapter()).notifyDataSetChanged();
        flag_loading = false;
    }

    private boolean flag_loading = false;

    private static final String TAG = "TweetHax_MainActivity"; //Log Tag
    private String fullName, username, avatarURL, bannerURL;
    ListView tweetsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getIntent().getLongExtra("TweetID", -1) != -1) {
            Log.e(TAG, "onCreate: has a tweetID");
            user = MainActivity.getUser(getIntent().getLongExtra("TweetID", -1));
        } else {
            Log.d(TAG, "onCreate: getting user from MainActivity");
            user = MainActivity.getUser();
            if (user == null) {
                Log.e(TAG, "onCreate: User was null");
            }
        }

        getUserTweets(-1);

        fullName = user.getName();
        username = user.getScreenname();
        avatarURL = user.getProfile_image_url();
        bannerURL = user.getProfile_banner_url();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newTweetIntent = new Intent(view.getContext(), NewTweetActivity.class);
                startActivity(newTweetIntent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TweetListAdapter tweetListAdapter = new TweetListAdapter(this, R.layout.tweet_list_item, tweetsList);
        tweetsListView = (ListView) findViewById(R.id.userTweetsListView);
        tweetsListView.setAdapter(tweetListAdapter);

        TextView nameView = (TextView) findViewById(R.id.fullNameView);
        TextView usernameView = (TextView) findViewById(R.id.usernameView);
        ImageView avatarView = (ImageView) findViewById(R.id.avatarViewProfile);
        ImageView bannerView = (ImageView) findViewById(R.id.bannerView);


        nameView.setText(fullName);
        usernameView.setText("@" + username);
        new DownloadImageTask(avatarView).execute(avatarURL);
        SharedPreferences prefs = getSharedPreferences("edu.saxion.kuiperklaczynski.tweethack", MODE_PRIVATE);
        OAuth1AccessToken accessToken = new OAuth1AccessToken(prefs.getString("access_token", "0"),prefs.getString("access_token_secret", "0"));
        new UserBannerTask(bannerView, username, accessToken).execute();

        tweetsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            long time = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (time + 15 > System.currentTimeMillis() / 1000) {
                    return;
                } else {
                    time = System.currentTimeMillis() / 1000;
                }

                if (firstVisibleItem + visibleItemCount >= totalItemCount - 5) {
                    if (!flag_loading) {
                        getUserTweets(tweetsList.get(tweetsList.size()-1).getId());
                    }
                }
            }
        });
    }

    private void getUserTweets(long maxID) {
        flag_loading = true;
        String s;
        if (maxID != -1) {
            s = "&max_id=" + maxID;
        } else {
            s = "";
        }
        new UserTweetsTask().execute(MainActivity.getAccessToken(), MainActivity.getAccessTokenSecret(), user.getId() + "", s);
    }

}
