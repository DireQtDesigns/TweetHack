package edu.saxion.kuiperklaczynski.tweethack.gui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.scribejava.core.model.OAuth1AccessToken;

import java.math.BigInteger;

import edu.saxion.kuiperklaczynski.tweethack.R;
import edu.saxion.kuiperklaczynski.tweethack.net.SendTweetTask;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;

public class NewTweetActivity extends AppCompatActivity {

    /**
     * Lets the user post a new tweet, hasn't really got anything more than an editText as well as a Button.
     * User also gets redirected to this activity when posting a reply, the body of the reply field will get copied over to tis editText, including the screen-name of the user to reply to.
     *
     * Eventually calls a new SendTweetTask with the Access tokens and obviously the tweet object including the body text.
     */

    private static final String TAG = "NewTweetAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tweet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final EditText bodyEditText = (EditText) findViewById(R.id.newTweetEditText);
        final TextView remainingTextView = (TextView) findViewById(R.id.newTweetCharsRemaining);

        bodyEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                remainingTextView.setText(140 - bodyEditText.getText().length());
                if (bodyEditText.getText().length() > 100) {
                    bodyEditText.setTextColor(Color.RED);
                } else {
                    bodyEditText.setTextColor(Color.rgb(50, 50, 50));
                }
                return false;
            }
        });

        if(getIntent().hasExtra("body")) {
            bodyEditText.setText(getIntent().getStringExtra("body"));
        }

        Button postButton = (Button) findViewById(R.id.newTweetSendButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("edu.saxion.kuiperklaczynski.tweethack", MODE_PRIVATE);
                String accessTokenStr = prefs.getString("access_token", "");
                String accessTokenSecretStr = prefs.getString("access_token_secret", "");
                OAuth1AccessToken accessToken = new OAuth1AccessToken(accessTokenStr, accessTokenSecretStr);
                Tweet tweet = new Tweet();
                tweet.setText(bodyEditText.getText().toString());
                if(getIntent().hasExtra("replyTo")) {
                    tweet.setIn_reply_to_status_id(getIntent().getLongExtra("replyTo", 0));
                    Log.d(TAG, "onClick: Has extra 'replyTo: ' "+getIntent().getIntExtra("replyTo", 0));
                }
                new SendTweetTask(tweet, getApplicationContext(), accessToken).execute();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
