package edu.saxion.kuiperklaczynski.tweethack.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.saxion.kuiperklaczynski.tweethack.R;
import edu.saxion.kuiperklaczynski.tweethack.io.JSONLoading;
import edu.saxion.kuiperklaczynski.tweethack.net.DownloadImageTask;
import edu.saxion.kuiperklaczynski.tweethack.net.URLTesting;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;
import edu.saxion.kuiperklaczynski.tweethack.objects.User;
import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.entities.URL;

public class TweetDetailActivity extends AppCompatActivity {

    public static ArrayList<Tweet> tweetsList = new ArrayList<>(), repliesTo = new ArrayList<>();
    public static Map<String, Tweet> tweetsMap = new HashMap<>(); //id_str is key
    private static final String TAG = "TweetHax_TweetDetail"; //Log Tag
    private String fullName, username, avatarURL, body, timeAgo, id_str;
    TweetListAdapter tweetListAdapter;
    public Tweet detailTweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tweetsMap = JSONLoading.tweetsMap;
        tweetsList = JSONLoading.tweetsList;
        Log.e(TAG, "onCreate: " + JSONLoading.tweetsList.toString());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getStringExtra("TweetID") == null) {
            Log.e(TAG, "onCreate: TweetID = null");
        } else {
            detailTweet = tweetsMap.get(getIntent().getStringExtra("TweetID"));
            fullName = detailTweet.getUser().getName();
            username = detailTweet.getUser().getScreenname();
            avatarURL = detailTweet.getUser().getProfile_image_url();
            body = detailTweet.getText();
            timeAgo = StringDateConverter.agoString(System.currentTimeMillis(), StringDateConverter.dateFromJSONString(detailTweet.getCreated_at()));
            id_str = detailTweet.getIdStr();
        }
        repliesTo = JSONLoading.repliesTo(tweetsList, detailTweet);
        tweetListAdapter = new TweetListAdapter(this, R.layout.tweet_list_item, repliesTo); //TODO Implement actual tweets list, the above only filters out the non-replies to the dummy tweets
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
                intent.putExtra("TweetID", tweet.getIdStr());
                startActivity(intent);
            }
        });
        setTitle(fullName);

        TextView nameView = (TextView) findViewById(R.id.tweetDetailNameView);
        TextView usernameView = (TextView) findViewById(R.id.tweetDetailUsernameView);
        TextView timeAgoView = (TextView) findViewById(R.id.tweetDetailTimeAgo);
        TextView bodyView = (TextView) findViewById(R.id.tweetDetailBodyView);
        ImageView avatarView = (ImageView) findViewById(R.id.tweetDetailAvatarView);
        final EditText replyField = (EditText) findViewById(R.id.tweetDetailReplyField);


        nameView.setText(fullName);
        usernameView.setText("@" + username);
        bodyView.setText(body);
        timeAgoView.setText(timeAgo);
        replyField.setText("@" + username + " ");

                new DownloadImageTask(avatarView).execute(avatarURL);
        replyField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(140)});
        replyField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*char[] messageChar = replyField.getText().toString().toCharArray();
                if(messageChar.length > 139) {
                    Toast.makeText(getApplicationContext(), "Message too long! Size is limited to 140 Characters.", Toast.LENGTH_LONG).show();
                    List<Character> chars = new ArrayList<Character>();
                    String finalString;
                    StringBuilder builder = new StringBuilder();
                    for(int i = 0; i < messageChar.length; i++) {
                        if(chars.size() < 140) chars.add(messageChar[i]);
                    }
                    for(Character c : chars) {
                        builder.append(c);
                    }
                    finalString = builder.toString();
                    replyField.setText(finalString);
                }*/
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    sendTweet(replyField.getText().toString(), replyField);
                    replyField.setText("@" + username + " ");
                }
                return true;
            }
        });
    }

    private void sendTweet(String text, View view) {
        Snackbar.make(view, "Tweet sent to: " + username, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        //TODO Implement sending a tweet
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
