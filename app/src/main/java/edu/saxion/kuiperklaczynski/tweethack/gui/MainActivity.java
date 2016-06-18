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
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;

    private String bearerToken;

    public void addBearerToken(String s) {
        if (bearerToken == null) {
            bearerToken = s;
        }
    }

    private String authToken;

    public static MainActivity getInstance() {
        if (instance == null) {
            instance = new MainActivity();
        }

        return instance;
    }


    public static List<Tweet> tweetsList = new ArrayList<>();

    private static final String TAG = "TweetHax_MainActivity"; //Log Tag
    private String jsonCode;
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
        authToken = null;

        //setting the tweet list type
        if (type == null) {
            //decides what the type is based on the content of the Toolbar, since everytime you open tweetDetailActivity it forgets the type

            String toolbarTitle = ((Toolbar)findViewById(R.id.toolbar)).getTitle().toString();
            if (toolbarTitle.contains("TweetHack") || toolbarTitle.contains("Home")) {
                type = ListType.HOME;
            } else if (toolbarTitle.contains("Search")){
                type = ListType.SEARCH;
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SearchTask().execute(new String[]{bearerToken, "test"});
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
                if (time + 5 > System.currentTimeMillis()/1000) {
                    return;
                } else {
                    time = System.currentTimeMillis()/1000;
                }

                Log.d(TAG, "onScroll: scrolling using " + type);

                switch (type) {
                    case SEARCH:
                        if (firstVisibleItem < 2 && totalItemCount != 0) {
                            Log.d(TAG, "onScroll: scrolling up in Search");
                            if (flag_loading == false) {
                                flag_loading = true;
                                long sinceID = tweetsList.get(0).getId();
                                new SearchTask().execute(new String[]{authToken,bearerToken,seachField, "&since_id=" + sinceID});
                            }
                        } else if (firstVisibleItem + visibleItemCount >= totalItemCount - 5 && totalItemCount != 0) {
                            Log.d(TAG, "onScroll: scrolling down in Search");
                            if (flag_loading == false) {
                                flag_loading = true;
                                long nextid = tweetsList.get(tweetsList.size() - 1).getId() - 1;
                                new SearchTask().execute(new String[]{authToken, bearerToken, seachField, "&max_id=" + nextid});
                            }
                        }
                        break;

                    case HOME:
                        if (firstVisibleItem < 2 && totalItemCount != 0) {
                            Log.d(TAG, "onScroll: scrolling up in Home");
                            if (flag_loading == false) {
                                flag_loading = true;
                                long sinceID = tweetsList.get(0).getId();
                                new TimeLineTask().execute(new String[]{authToken, "&since_id=" + sinceID});
                            }
                        } else if (firstVisibleItem + visibleItemCount >= totalItemCount - 5 && totalItemCount != 0) {
                            Log.d(TAG, "onScroll: scrolling down in Home");
                            if (flag_loading == false) {
                                flag_loading = true;
                                long nextid = tweetsList.get(tweetsList.size() - 1).getId() - 1;
                                new TimeLineTask().execute(new String[]{authToken, "&max_id=" + nextid});
                            }
                        }
                        break;
                }
            }
        });
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

        switch (id) {
            case R.id.action_home:
                type = ListType.HOME;
                ((Toolbar)findViewById(R.id.toolbar)).setTitle("Home");

                new TimeLineTask().execute(authToken);

                break;

            case R.id.action_search:
                type = ListType.SEARCH;

                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Search");
                alert.setMessage("Please enter your search terms");

                final EditText input = new EditText(this);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        seachField = input.getText().toString();
                        ((Toolbar)findViewById(R.id.toolbar)).setTitle("Search Results: " + seachField);

                        new SearchTask().execute(new String[]{authToken, bearerToken, seachField, null});
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
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

    public void addItems(ArrayList<Tweet> list) {
        tweetsList.addAll(list);
        ((TweetListAdapter) listView.getAdapter()).notifyDataSetChanged();
        flag_loading = false;
    }

    private ArrayList<Tweet> tempTweets = new ArrayList<>();

    public void topUpTweetsList(ArrayList<Tweet> list, ListType type) {
        if (list.size() == 0) {
            if (tempTweets.size() != 0) {
                int currentPosition = listView.getLastVisiblePosition() + tempTweets.size();

                topUpHelper(currentPosition);
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
                    new TimeLineTask().execute(new String[]{authToken, "&since_id=" + sinceID + "&max_id=" + maxID});
                } else if (type == ListType.SEARCH) {
                    new SearchTask().execute(new String[]{authToken, bearerToken, seachField, "&since_id=" + sinceID + "&max_id=" + maxID});
                }
            }
        }
        flag_loading = false;
    }

    private void topUpHelper(int viewPosition) {
        for (int i = tempTweets.size() - 1; i >= 0 ; i--) {
            if (tweetsList.contains(tempTweets.get(i))) {
                tempTweets.remove(i);
            } else {
                for (int j = tempTweets.size() - 1; j >= 0; j--) {
                    tweetsList.add(0,tempTweets.get(j));
                }
                tempTweets = new ArrayList<>();
                break;
            }
        }

        //makes you come back to your original location when loading new tweets
        listView.smoothScrollToPosition(viewPosition);

        ((TweetListAdapter) listView.getAdapter()).notifyDataSetChanged();
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
