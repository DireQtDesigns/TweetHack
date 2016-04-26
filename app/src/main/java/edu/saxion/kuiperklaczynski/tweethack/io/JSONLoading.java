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

    public static final File INPUT_FILE = new File("C:/Users/leonk/AndroidStudioProjects/JSONTest/tweets.json"); //TODO Make this more....ehh everything-friendly, like android doesn't like a C drive
    public static List<Tweet> tweetsList = new ArrayList<>();
    public static Map<String, Tweet> tweetsMap = new HashMap<>();
    public static final String TAG = "JSONLoader"; //Log Tag

    public static void main(String[] args) {new JSONLoading().run();}

    /**
     * Gets everything up & running
     * @author Leon
     */
    public static void run() {
        try {
            readJSON(INPUT_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads an asset file and returns a string with the full contents.
     *
     * @param file      The file to read.
     * @return          The contents of the file.
     * @throws IOException  If file could not be found or not read.
     */
    private static String readAssetIntoString(File file) throws IOException {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            InputStream is = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(TAG, "readAssetIntoString: "+sb.toString());
        return sb.toString();
    }

    /**
     * Fetches data from a file using readAssetIntoString, into json data, then into object structures. nullnullnullnullnullnullnullnullnullnullnullnull, oh wait...
     * @author Leon
     * @param fileToRead File to fetch data from
     * @throws JSONException If object or data cannot be found etc.
     * @throws IOException If file cannot be found.
     */
    public static void readJSON(File fileToRead) throws JSONException, IOException {
        if(Settings.DEBUG == Settings.IO || Settings.DEBUG == Settings.ALL) Log.d("JSONLoader", readAssetIntoString(fileToRead));
        JSONObject jason = new JSONObject(readAssetIntoString(fileToRead)); //TODO fix java.lang.RuntimeException: Stub! at org.json.JSONObject.<init>(JSONObject.java:7)
        JSONArray statuses = jason.getJSONArray("statuses");
        for (int i = 0; i < statuses.length(); i++) {
            JSONObject jsonStatus = statuses.getJSONObject(i);
            Tweet status = new Tweet(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            User user = new User(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            JSONObject jsonUser = jason.getJSONObject("user");
            //TODO Add all the other crap, repetitive af, i'll handle it.

            if(Settings.DEBUG == Settings.IO || Settings.DEBUG == Settings.ALL) {
                Log.d(TAG, "Loading tweet id:               " + jsonStatus.getString("id_str"));
                Log.d(TAG, "Text:                           " + jsonStatus.getString("text"));
                Log.d(TAG, "Posted at:                      " + jsonStatus.getString("created_at"));
                Log.d(TAG, "--------------------------------");
                Log.d(TAG, "From user id:                   " + jsonUser.getString("id_str"));
                Log.d(TAG, "Name:                           " + jsonStatus.getString("name"));
                Log.d(TAG, "--------------------------------");
                Log.d(TAG, "--------------------------------");

            }
            //Set tweet info
            //status.setId(jsonStatus.getInt("id")); Doesn't seem to be right, let's just leave it alone...
            status.setIdStr(jsonStatus.getString("id_str"));
            status.setText(jsonStatus.getString("text"));
            status.setCreated_at(jsonStatus.getString("created_at")); //TODO formatting currently: "Wed Apr 20 13:01:49 +0000 2016" as String, conv to 'Data' & 'Time' objects.
            //TODO MOAR info

            //Set user info
            user.setName(jsonUser.getString("name"));
            user.setProfile_image_url(jsonUser.getString("profile_image_url"));
            //TODO More eventually

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
                Log.d(TAG, "Name:                           " + jsonStatus.getString("name"));
                Log.d(TAG, "--------------------------------");
                Log.d(TAG, "--------------------------------");
                Log.d(TAG, "Done");
            }
        }
    }

}
