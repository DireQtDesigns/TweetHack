package edu.saxion.kuiperklaczynski.tweethack.gui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import edu.saxion.kuiperklaczynski.tweethack.R;
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
    private SwipeRefreshLayout swipeRefreshLayout;

    private ListType type;
    private String seachField = null;
    private boolean flag_loading = false;

    /**
     * onCreate, sets up the entire activity. logs in, and sets scrollListeners so you have the latest data when you swipe up/down
     * @param savedInstanceState
     */
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

        //Getting the access token and secret saved in SharedPreferences in order to run the VerifyCredentialTask
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
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!canRefresh()) {
                    return;
                }

                Log.d(TAG, "onScroll: scrolling using " + type);

                if (firstVisibleItem + visibleItemCount >= totalItemCount - 5 && totalItemCount != 0) {
                    onScrollDown();
                }
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if (!canRefresh()) {
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }

                Log.d(TAG, "onRefresh: refreshing");

                if (listView.getFirstVisiblePosition() == 0) {
                    onScrollUp();
                }
            }
        });
    }

    private long time = 0;

    /**
     * prevents the user from spamming updates, and getting timed out by twitter
     * @return true if enough time has passed since the last update
     */
    private boolean canRefresh() {
        if (time + 15 > System.currentTimeMillis() / 1000) {
            return false;
        } else {
            time = System.currentTimeMillis() / 1000;
            return true;
        }
    }

    /**
     * called when the user scrolls up far enough to refresh for new tweets
     */
    private void onScrollUp() {
        Log.d(TAG, "onScroll: scrolling up in " + type);

        switch (type) {
            case SEARCH:
                if (!flag_loading) {
                    flag_loading = true;
                    long sinceID = tweetsList.get(0).getId();
                    searchTask("&since_id=" + sinceID, null);
                }
                break;

            case HOME:
                if (flag_loading == false) {
                    flag_loading = true;
                    long sinceID = tweetsList.get(0).getId();
                    timeLineTask("?since_id=" + sinceID, null);
                }
                break;
        }
    }

    /**
     * called when the user scrolls down far enough to load new tweets
     */
    private void onScrollDown() {
        Toast.makeText(this, "getting more tweets for you, hang on..", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "onScroll: scrolling down in " + type);

        switch (type) {
            case SEARCH:
                if (!flag_loading) {
                    flag_loading = true;
                    long nextid = tweetsList.get(tweetsList.size() - 1).getId() - 1;
                    searchTask(null, "&max_id=" + nextid);
                }
                break;

            case HOME:
                if (flag_loading == false) {
                    flag_loading = true;
                    long nextid = tweetsList.get(tweetsList.size() - 1).getId() - 1;
                    timeLineTask(null, "?max_id=" + nextid);
                }
                break;
            }
    }

    /**
     * checks whether the user is still logged in. if no, starts the login activity. if yes, continues as usual
     */
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
        MenuItem itemLogin = menu.findItem(R.id.action_authtest);
        SharedPreferences prefs = getSharedPreferences("edu.saxion.kuiperklaczynski.tweethack", MODE_PRIVATE);
        if(prefs.getAll().containsKey("access_token") && prefs.getAll().containsKey("access_token_secret"))
            itemLogin.setTitle("Logged in");
        else
            itemLogin.setTitle("Log in");
        return true;
    }

    /**
     * sets actions for the options menu like:
     * Home: gets you the home feed
     * Search: asks for input, then searches based on that input
     * Profile: opens a profile activity where you can see your own profile\
     * Login: lets you login
     * Logout: lets you logout
     * @param item which item has been clicked
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        SharedPreferences prefs = getSharedPreferences("edu.saxion.kuiperklaczynski.tweethack", MODE_PRIVATE);
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
                if (prefs.getAll().containsKey("access_token") && prefs.getAll().containsKey("access_token_secret")) {
                    Toast.makeText(getApplicationContext(), "Already logged in!", Toast.LENGTH_LONG).show();
                } else {
                    tweetsList.clear();
                    Intent authIntent = new Intent(MainActivity.this, AuthActivity.class);
                    startActivity(authIntent);
                }
                break;

            case R.id.action_logout:
                tweetsList.clear();
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

        return super.onOptionsItemSelected(item);
    }

    public void fillAccessTokens(String accessToken, String accessTokenSecret) {
        if (this.accessToken == null && this.accessTokenSecret == null && accessToken != null && accessTokenSecret != null) {
            this.accessToken = accessToken;
            this.accessTokenSecret = accessTokenSecret;
        }
    }

    /**
     * helper method for when you switch from Home to Search
     * @param list contains the new tweets
     */
    public void updateView(ArrayList<Tweet> list) {
        tweetsList = list;

        setListView();
    }

    /**
     * helper method for when you scroll down and load more items
     * @param list contains the older tweets, to be added
     */
    public void addItems(ArrayList<Tweet> list) {
        tweetsList.addAll(list);
        ((TweetListAdapter) listView.getAdapter()).notifyDataSetChanged();
        flag_loading = false;

        Toast toast = Toast.makeText(this, "Added older results", Toast.LENGTH_SHORT);
        toast.show();
    }

    private ArrayList<Tweet> tempTweets = new ArrayList<>();

    /**
     * method for refreshing your feed, can be called multiple times by a single load depending on how much new data is coming in
     * @param list list of new tweets
     * @param type type of the current search, needed to know if you should call SearchTask of TimeLineTask
     */
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
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * helper method to easily call SearchTask
     * @param sinceID
     * @param maxID
     */
    private void searchTask(String sinceID, String maxID) {
        if (sinceID == null) {
            sinceID = "";
        }

        if (maxID == null) {
            maxID = "";
        }

        new SearchTask().execute(new String[]{accessToken, accessTokenSecret, bearerToken, seachField, sinceID + maxID});
    }

    /**
     * helper method to easily call TimeLineTask
     * @param sinceID
     * @param maxID
     */
    private void timeLineTask(String sinceID, String maxID) {
        if (sinceID == null) {
            sinceID = "";
        }

        if (maxID == null) {
            maxID = "";
        }

        new TimeLineTask().execute(new String[]{accessToken, accessTokenSecret, sinceID + maxID});
    }

    /**
     * helper method to, when all new tweets have been loaded, add them to the current tweetlist and update views
     * @param viewPosition
     */
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
    }
}
