package edu.saxion.kuiperklaczynski.tweethack.io;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.saxion.kuiperklaczynski.tweethack.gui.Settings;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;
import edu.saxion.kuiperklaczynski.tweethack.objects.User;

/**
 * Created by leonk on 25/04/2016.
 */
public class JSONLoading {

    public static ArrayList<Tweet> tweetsList = new ArrayList<>();
    public static Map<String, Tweet> tweetsMap = new HashMap<>(); //id_str is key
    private static final String TAG = "TweetHax_JSONLoader"; //Log Tag

    /**
     * reads the given JSONArray and gives back the contents in the form of Tweet Objects
     * @param statuses jsonArray
     * @return contents of JSONArray converted to TweetList
     * @throws JSONException when reading the JSONArray goes wrong
     */
    public static ArrayList<Tweet> readJSONArray(JSONArray statuses) throws JSONException {

        ArrayList<Tweet> tempTweets = new ArrayList<>();

        for (int i = 0; i < statuses.length(); i++) {
            //Creates the Tweet
            Tweet tweet = new Tweet(statuses.getJSONObject(i));

            //adds tweet to arraylist & hashmap
            tempTweets.add(tweet);
            tweetsMap.put(tweet.getIdStr(), tweet);
        }

        tweetsList = tempTweets;


        return tempTweets;
    }

    /**
     * Fetches data from jsonCode using readAssetIntoString, into json data, then into object structures.
     * @author Leon
     * @throws JSONException If object or data cannot be found etc.
     */
    public static ArrayList<Tweet> readJSON(String jsonCode) throws JSONException {
        if(Settings.DEBUG == Settings.IO || Settings.DEBUG == Settings.ALL) Log.d("JSONLoader", jsonCode);
        JSONObject jason = new JSONObject(jsonCode);
        JSONArray statuses = jason.getJSONArray("statuses");

        return readJSONArray(statuses);
    }

}
