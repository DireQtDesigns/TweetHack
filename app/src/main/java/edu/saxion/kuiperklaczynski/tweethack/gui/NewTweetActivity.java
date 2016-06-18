package edu.saxion.kuiperklaczynski.tweethack.gui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.scribejava.core.model.OAuth1AccessToken;

import edu.saxion.kuiperklaczynski.tweethack.R;
import edu.saxion.kuiperklaczynski.tweethack.net.SendTweetTask;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;

public class NewTweetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tweet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText bodyEditText = (EditText) findViewById(R.id.newTweetEditText);
        final TextView remainingTextView = (TextView) findViewById(R.id.newTweetCharsRemaining);

        bodyEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                remainingTextView.setText(140 - bodyEditText.getText().length());
                if(bodyEditText.getText().length() > 100) {
                    bodyEditText.setTextColor(Color.RED);
                } else {
                    bodyEditText.setTextColor(Color.rgb(50, 50, 50));
                }
                return false;
            }
        });
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
                new SendTweetTask(tweet, getApplicationContext(), accessToken).execute();
            }
        });



    }

}
