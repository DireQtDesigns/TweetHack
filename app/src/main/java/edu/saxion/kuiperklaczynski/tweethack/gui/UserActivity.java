package edu.saxion.kuiperklaczynski.tweethack.gui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.saxion.kuiperklaczynski.tweethack.R;
import edu.saxion.kuiperklaczynski.tweethack.io.JSONLoading;
import edu.saxion.kuiperklaczynski.tweethack.net.DownloadImageTask;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;
import edu.saxion.kuiperklaczynski.tweethack.objects.User;

public class UserActivity extends AppCompatActivity {

    public static List<Tweet> tweetsList = new ArrayList<>();
    public static Map<String, Tweet> tweetsMap = new HashMap<>(); //id_str is key
    private static final String TAG = "TweetHax_MainActivity"; //Log Tag
    private String fullName, username, avatarURL, bannerURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        tweetsList = JSONLoading.tweetsList;
        tweetsMap = JSONLoading.tweetsMap;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getIntent().getStringExtra("TweetID") == null) {
            Log.e(TAG, "onCreate: TweetID = null");
        } else {
            User user = tweetsMap.get(getIntent().getStringExtra("TweetID")).getUser();
            fullName = user.getName();
            username = user.getScreenname();
            avatarURL = user.getProfile_image_url();
            bannerURL = user.getProfile_banner_url();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TweetListAdapter tweetListAdapter = new TweetListAdapter(this, R.layout.tweet_list_item, tweetsList);
        ListView tweetsListView = (ListView) findViewById(R.id.userTweetsListView);
        tweetsListView.setAdapter(tweetListAdapter);

        TextView nameView = (TextView) findViewById(R.id.fullNameView);
        TextView usernameView = (TextView) findViewById(R.id.usernameView);
        ImageView avatarView = (ImageView) findViewById(R.id.avatarViewProfile);
        ImageView bannerView = (ImageView) findViewById(R.id.bannerView);


        nameView.setText(fullName);
        usernameView.setText("@" + username);
        new DownloadImageTask(avatarView).execute(avatarURL);
        new DownloadImageTask(bannerView).execute(bannerURL);
    }

}
