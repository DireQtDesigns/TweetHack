package edu.saxion.kuiperklaczynski.tweethack.gui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.saxion.kuiperklaczynski.tweethack.R;
import edu.saxion.kuiperklaczynski.tweethack.io.JSONLoading;
import edu.saxion.kuiperklaczynski.tweethack.net.BearerToken;
import edu.saxion.kuiperklaczynski.tweethack.net.SearchTask;
import edu.saxion.kuiperklaczynski.tweethack.net.TimeLineTask;
import edu.saxion.kuiperklaczynski.tweethack.net.VerifyCredentialsTask;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;
import edu.saxion.kuiperklaczynski.tweethack.objects.User;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;

    private String bearerToken;

    public void addBearerToken(String s) {
        if (bearerToken == null) {
            bearerToken = s;
        }
    }

    private static String accessToken, accessTokenSecret;

    public static String getAccessToken() {
        return accessToken;
    }

    public static String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public static MainActivity getInstance() {
        if (instance == null) {
            instance = new MainActivity();
        }

        return instance;
    }


    public static List<Tweet> tweetsList = new ArrayList<>();

    public static User getUser(long id) {
        for (Tweet t: tweetsList) {
            if (t.getUser().getId() == id) {
                return t.getUser();
            }
        }
        return null;
    }

    private static User user;

    public void fillUser(User u) {
        if (user == null && u != null) {
            user = u;
            SharedPreferences prefs = this.getSharedPreferences("edu.saxion.kuiperklaczynski.tweethack", this.MODE_PRIVATE);
            prefs.edit().putLong("user_id", u.getId());
        }
    }

    public static User getUser() {
        return user;
    }

    private static final String TAG = "TweetHax_MainActivity"; //Log Tag
    private static ListView listView;

    private ListType type;
    private String seachField = null;
    private boolean flag_loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Getting the bearerToken
        String tempToken;

        SharedPreferences prefs = getSharedPreferences("edu.saxion.kuiperklaczynski.tweethack", MODE_PRIVATE);
        tempToken = prefs.getString("BEARERTOKEN", null);

        if (tempToken == null) {
            Log.d(TAG, "onCreate: generating new BearerToken");
            new BearerToken().execute(this);
        } else {
            bearerToken = tempToken;
            Log.d(TAG, "onCreate: BearerToken Already Existed");
        }

        //getting the authtoken TODO: auth token getting
        if (prefs.getAll().containsKey("access_token") && prefs.getAll().containsKey("access_token_secret")) {
            fillAccessTokens(prefs.getString("access_token", null), prefs.getString("access_token_secret", null));
            Log.d(TAG, "onCreate: User already authenticated, no need to ask for re-authorisation");
            if (user == null) {
                new VerifyCredentialsTask().execute(new String[]{accessToken, accessTokenSecret});
            }
        } else {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
        }

        //decides what the type is based on the content of the Toolbar, since everytime you open tweetDetailActivity it forgets the type

        String toolbarTitle = ((Toolbar) findViewById(R.id.toolbar)).getTitle().toString();
        if (toolbarTitle.contains("TweetHack") || toolbarTitle.contains("Home")) {
            Log.d(TAG, "onCreate: Setting type to Home");
            type = ListType.HOME;
        } else if (toolbarTitle.contains("Search")) {
            Log.d(TAG, "onCreate: Setting type to Search");
            type = ListType.SEARCH;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newTweetIntent = new Intent(view.getContext(), NewTweetActivity.class);
                startActivity(newTweetIntent);
            }
        });

        setListView();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private long time = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //makes sure you can't spam updates so twitter doesn't time your ass out
                if (time + 15 > System.currentTimeMillis() / 1000) {
                    return;
                } else {
                    time = System.currentTimeMillis() / 1000;
                }

                Log.d(TAG, "onScroll: scrolling using " + type);

                switch (type) {
                    case SEARCH:
                        if (firstVisibleItem < 5 && totalItemCount != 0) {
                            Log.d(TAG, "onScroll: scrolling up in Search");
                            if (!flag_loading) {
                                flag_loading = true;
                                long sinceID = tweetsList.get(0).getId();
                                searchTask("&since_id=" + sinceID, null);
                            }
                        } else if (firstVisibleItem + visibleItemCount >= totalItemCount - 5 && totalItemCount != 0) {
                            Log.d(TAG, "onScroll: scrolling down in Search");
                            if (!flag_loading) {
                                flag_loading = true;
                                long nextid = tweetsList.get(tweetsList.size() - 1).getId() - 1;
                                searchTask(null, "&max_id=" + nextid);
                            }
                        }
                        break;

                    case HOME:
                        if (firstVisibleItem < 2 && totalItemCount != 0) {
                            Log.d(TAG, "onScroll: scrolling up in Home");
                            if (flag_loading == false) {
                                flag_loading = true;
                                long sinceID = tweetsList.get(0).getId();
                                timeLineTask("?since_id=" + sinceID, null);
                            }
                        } else if (firstVisibleItem + visibleItemCount >= totalItemCount - 5 && totalItemCount != 0) {
                            Log.d(TAG, "onScroll: scrolling down in Home");
                            if (flag_loading == false) {
                                flag_loading = true;
                                long nextid = tweetsList.get(tweetsList.size() - 1).getId() - 1;
                                timeLineTask(null, "?max_id=" + nextid);
                            }
                        }
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        SharedPreferences prefs = getSharedPreferences("edu.saxion.kuiperklaczynski.tweethack", MODE_PRIVATE);
        if (prefs.getAll().containsKey("access_token") && prefs.getAll().containsKey("access_token_secret")) {
            Log.d(TAG, "onCreate: User already authenticated, no need to ask for re-authorisation");
        } else {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_home:
                type = ListType.HOME;
                ((Toolbar) findViewById(R.id.toolbar)).setTitle("Home");
                tweetsList.clear();

                timeLineTask(null, null);

                break;

            case R.id.action_search:
                listView.setClickable(false);

                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Search");
                alert.setMessage("Please enter your search terms");

                final EditText input = new EditText(this);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        type = ListType.SEARCH;
                        tweetsList.clear();

                        seachField = input.getText().toString();
                        ((Toolbar) findViewById(R.id.toolbar)).setTitle("Search Results: " + seachField);

                        listView.setClickable(true);
                        searchTask(null, null);

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        listView.setClickable(true);
                        // Canceled.
                    }
                });

                alert.show();
                break;

            case R.id.action_profile:
                if (user == null) {
                    Toast.makeText(this, "we don't have any user info on you right now", Toast.LENGTH_SHORT).show();
                    return false;
                }

                tweetsList.clear();
                Intent profileIntent = new Intent(this, UserActivity.class);
                startActivity(profileIntent);
                break;

            case R.id.action_authtest:
                tweetsList.clear();
                Intent authIntent = new Intent(MainActivity.this, AuthActivity.class);
                startActivity(authIntent);
                break;

            case R.id.action_logout:
                tweetsList.clear();
                SharedPreferences prefs = getSharedPreferences("edu.saxion.kuiperklaczynski.tweethack", MODE_PRIVATE);
                prefs.edit().remove("access_token").apply();
                prefs.edit().remove("access_token_secret").apply();
                if (prefs.getAll().containsKey("access_token") && prefs.getAll().containsKey("access_token_secret")) {
                    Log.d(TAG, "onCreate: User already authenticated, no need to ask for re-authorisation");
                } else {
                    Intent reAuthIntent = new Intent(this, AuthActivity.class);
                    startActivity(reAuthIntent);
                }
                break;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fillAccessTokens(String accessToken, String accessTokenSecret) {
        if (this.accessToken == null && this.accessTokenSecret == null && accessToken != null && accessTokenSecret != null) {
            this.accessToken = accessToken;
            this.accessTokenSecret = accessTokenSecret;
        }
    }

    public void updateView(ArrayList<Tweet> list) {
        tweetsList = list;

        setListView();
    }

    public void addItems(ArrayList<Tweet> list) {
        tweetsList.addAll(list);
        ((TweetListAdapter) listView.getAdapter()).notifyDataSetChanged();
        flag_loading = false;

        Toast toast = Toast.makeText(this, "Added older results", Toast.LENGTH_SHORT);
        toast.show();
    }

    private ArrayList<Tweet> tempTweets = new ArrayList<>();

    public void topUpTweetsList(ArrayList<Tweet> list, ListType type) {
        if (list.size() == 0) {
            if (tempTweets.size() != 0) {
                Log.d(TAG, "topUpTweetsList: list is empty but temptweets is not");
                int currentPosition = listView.getLastVisiblePosition() + tempTweets.size();

                topUpHelper(currentPosition);
            } else {
                Log.d(TAG, "topUpTweetsList: list was empty, temptweets was empty");
                Toast toast = Toast.makeText(this, "no new results", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            if (tweetsList.contains(list.get(list.size() - 1))) {
                Log.d(TAG, "topUpTweetsList: list contains item in tweetList");

                tempTweets.addAll(list);

                int currentPosition = listView.getLastVisiblePosition() + tempTweets.size();

                topUpHelper(currentPosition);
            } else {
                Log.d(TAG, "topUpTweetsList: list does not contain item in tweetList");
                tempTweets.addAll(list);

                long sinceID = tweetsList.get(0).getId();
                long maxID = tempTweets.get(tempTweets.size() - 1).getId() - 1;

                if (type == ListType.HOME) {
                    timeLineTask("?since_id=" + sinceID, "&max_id=" + maxID);
                } else if (type == ListType.SEARCH) {
                    searchTask("&since_id="+sinceID, "&max_id="+maxID);
                }
            }
        }
        flag_loading = false;
    }

    private void searchTask(String sinceID, String maxID) {
        if (sinceID == null) {
            sinceID = "";
        }

        if (maxID == null) {
            maxID = "";
        }

        new SearchTask().execute(new String[]{accessToken, accessTokenSecret, bearerToken, seachField, sinceID + maxID});
    }

    private void timeLineTask(String sinceID, String maxID) {
        if (sinceID == null) {
            sinceID = "";
        }

        if (maxID == null) {
            maxID = "";
        }

        new TimeLineTask().execute(new String[]{accessToken, accessTokenSecret, sinceID + maxID});
    }

    private void topUpHelper(int viewPosition) {
        for (int i = tempTweets.size() - 1; i >= 0; i--) {
            if (tweetsList.contains(tempTweets.get(i))) {
                tempTweets.remove(i);
            } else {
                for (int j = tempTweets.size() - 1; j >= 0; j--) {
                    tweetsList.add(0, tempTweets.get(j));
                }
                tempTweets = new ArrayList<>();
                break;
            }
        }

        //makes you come back to your original location when loading new tweets
        listView.smoothScrollToPosition(viewPosition);

        ((TweetListAdapter) listView.getAdapter()).notifyDataSetChanged();

        Toast toast = Toast.makeText(this, "Added new results", Toast.LENGTH_SHORT);
        toast.show();

    }

    private void setListView() {
        TweetListAdapter tweetListAdapter = new TweetListAdapter(this, R.layout.tweet_list_item, tweetsList);
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
}
