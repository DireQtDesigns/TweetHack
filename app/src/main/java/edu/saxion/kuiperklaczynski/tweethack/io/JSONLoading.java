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

    //public static final File INPUT_FILE = new File("C:/Users/leonk/AndroidStudioProjects/JSONTest/tweets.json");
    public static ArrayList<Tweet> tweetsList = new ArrayList<>();
    public static Map<String, Tweet> tweetsMap = new HashMap<>(); //id_str is key
    private static final String TAG = "TweetHax_JSONLoader"; //Log Tag

    //public static void main(String[] args) {new JSONLoading().run();}

    /**
     * Gets everything up & running in a non-android environment.
     * @author Leon
     */
    public static void run() {
        try {
            //readJSON(INPUT_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches data from jsonCode using readAssetIntoString, into json data, then into object structures. nullnullnullnullnullnullnullnullnullnullnullnull, oh wait...
     * @author Leon
     * @throws JSONException If object or data cannot be found etc.
     * @throws IOException If file cannot be found.
     */
    public static void readJSON(String jsonCode) throws JSONException, IOException {
        if(Settings.DEBUG == Settings.IO || Settings.DEBUG == Settings.ALL) Log.d("JSONLoader", jsonCode);
        JSONObject jason = new JSONObject(jsonCode);
        JSONArray statuses = jason.getJSONArray("statuses");
        for (int i = 0; i < statuses.length(); i++) {
            JSONObject jsonStatus = statuses.getJSONObject(i);
            Tweet status = new Tweet(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            User user = new User(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            JSONObject jsonUser = jsonStatus.getJSONObject("user");
            //TODO Add all the other crap, repetitive af, i'll handle it.

            if(Settings.DEBUG == Settings.IO || Settings.DEBUG == Settings.ALL) {
                Log.d(TAG, "Loading tweet id:               " + jsonStatus.getString("id_str"));
                Log.d(TAG, "Text:                           " + jsonStatus.getString("text"));
                Log.d(TAG, "Posted at:                      " + jsonStatus.getString("created_at"));
                Log.d(TAG, "--------------------------------");
                Log.d(TAG, "From user id:                   " + jsonUser.getString("id_str"));
                Log.d(TAG, "Name:                           " + jsonUser.getString("name"));
                Log.d(TAG, "--------------------------------");
                Log.d(TAG, "--------------------------------");

            }
            //Set tweet info
            //status.setId(jsonStatus.getInt("id")); Doesn't seem to be right, let's just leave it alone...
            status.setIdStr(jsonStatus.getString("id_str"));
            status.setText(jsonStatus.getString("text"));
            status.setCreated_at(jsonStatus.getString("created_at"));
            status.setIn_reply_to_status_id_str(jsonStatus.getString("in_reply_to_status_id_str"));
            //TODO MOAR info

            //Set user info
            user.setName(jsonUser.getString("name"));
            user.setProfile_image_url(jsonUser.getString("profile_image_url"));
            user.setScreenname(jsonUser.getString("screen_name"));
            user.setProfile_banner_url("profile_banner_url");
            //TODO ALL THE STUFFS

            //Finishing up
            status.setUser(user);
            tweetsList.add(status);
            tweetsMap.put(status.getIdStr(), status);

            if(Settings.DEBUG == Settings.IO || Settings.DEBUG == Settings.ALL) {
                Log.d(TAG, "Loaded tweet id:                " + status.getIdStr());
                Log.d(TAG, "Text:                           " + status.getText());
                Log.d(TAG, "Posted at:                      " + status.getCreated_at());
                Log.d(TAG, "--------------------------------");
                Log.d(TAG, "From user id:                   " + user.getId_str());
                Log.d(TAG, "Name:                           " + user.getName());
                Log.d(TAG, "--------------------------------");
                Log.d(TAG, "--------------------------------");
                Log.d(TAG, "Done");
            }
        }
    }
    public static ArrayList<Tweet> repliesTo(ArrayList<Tweet> from, Tweet to) {
        ArrayList<Tweet> temp = new ArrayList<Tweet>();
        for(Tweet t : from) {
            if(t.getIn_reply_to_status_id_str().equals(to.getIdStr())) temp.add(t);
        }
        Log.d(TAG, "repliesTo: "+temp.toString());
        return temp;
    }

}
