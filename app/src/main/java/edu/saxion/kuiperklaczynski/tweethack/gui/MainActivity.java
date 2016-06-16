package edu.saxion.kuiperklaczynski.tweethack.gui;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.saxion.kuiperklaczynski.tweethack.R;
import edu.saxion.kuiperklaczynski.tweethack.io.JSONLoading;
import edu.saxion.kuiperklaczynski.tweethack.net.BearerToken;
import edu.saxion.kuiperklaczynski.tweethack.net.SearchTask;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;
import edu.saxion.kuiperklaczynski.tweethack.objects.User;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;

    private final String bearerToken;
    private final String authToken;

    public static MainActivity getInstance() {
        if (instance == null) {
            instance = new MainActivity();
        }

        return instance;
    }

    private MainActivity() {
        super();

        //Getting the bearerToken
        String tempToken;

        SharedPreferences prefs = getSharedPreferences("edu.saxion.kuiperklaczynski.tweethack", MODE_PRIVATE);
        tempToken = prefs.getString("BEARERTOKEN", null);

        if (tempToken == null) {
            Log.d(TAG, "onCreate: generating new BearerToken");
            new BearerToken().execute(this);
            bearerToken = prefs.getString("BEARERTOKEN", null);
        } else {
            bearerToken = tempToken;
            Log.d(TAG, "onCreate: BearerToken Already Existed");
        }

        //getting the authtoken
        authToken = null;
    }


    public static List<Tweet> tweetsList = new ArrayList<>();
    private static final String TAG = "TweetHax_MainActivity"; //Log Tag
    private String jsonCode;
    private static ListView listView;
    private static TweetListAdapter tweetListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            InputStream is = getAssets().open("tweets.json");
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(TAG, "onCreate: " + sb.toString());
        jsonCode = sb.toString();
        try {
            JSONLoading.readJSON(jsonCode);
            tweetsList = JSONLoading.tweetsList;
        }catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
        }

//        SharedPreferences prefs = getSharedPreferences("edu.saxion.kuiperklaczynski.tweethack", MODE_PRIVATE);
//        bearerToken = prefs.getString("BEARERTOKEN", null);

        if (bearerToken == null) {
            Log.d(TAG, "onCreate: generating new BearerToken");
            new BearerToken().execute(this);
        } else {
            Log.d(TAG, "onCreate: BearerToken Already Existed");
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SearchTask().execute(new String[]{bearerToken, "test"});
            }
        });

        setListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.action_authtest:
                Intent authIntent = new Intent(MainActivity.this, AuthActivity.class);
                startActivity(authIntent);
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateView(ArrayList<Tweet> list) {
        tweetsList = list;

        setListView();
    }

    private void setListView() {
        tweetListAdapter = new TweetListAdapter(this, R.layout.tweet_list_item, tweetsList);
        listView = (ListView) findViewById(R.id.tweetsListView);
        listView.setAdapter(tweetListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TweetDetailActivity.class);
                Tweet tweet = tweetsList.get(position);
                intent.putExtra("TweetID", tweet.getIdStr());
                startActivity(intent);
            }
        });
    }

    private void searchTweets(String searchTerm) {
        SearchTask.execute(new String[]{authToken, bearerToken, "test"});
    }
}
