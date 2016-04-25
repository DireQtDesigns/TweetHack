package edu.saxion.kuiperklaczynski.tweethack;

import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;

/**
 * Created by leonk on 25/04/2016.
 */
public class JSONTesting {

    public static final File INPUT_FILE = new File("C:/Users/leonk/AndroidStudioProjects/JSONTest/tweets.json"); //TODO Make this more....ehh everything-friendly, like android doesn't like a C drive
    public static final boolean DEBUG = true;

    public static  void main(String[] args) {new JSONTesting().run();}

    /**
     * Gets everything up & running
     * @author Leon
     */
    public void run() {
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
    private String readAssetIntoString(File file) throws IOException {
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
        return sb.toString();
    }

    /**
     * Fetches data from a file using readAssetIntoString, into json data, then into object structures.
     * @author Leon
     * @param fileToRead File to fetch data from
     * @throws JSONException If object or data cannot be found etc.
     * @throws IOException If file cannot be found.
     */
    public void readJSON(File fileToRead) throws JSONException, IOException {
        if(DEBUG) System.out.println(readAssetIntoString(fileToRead));
        JSONObject jason = new JSONObject(readAssetIntoString(fileToRead)); //TODO fix java.lang.RuntimeException: Stub! at org.json.JSONObject.<init>(JSONObject.java:7)
        JSONArray statuses = jason.getJSONArray("statuses");
        for (int i = 0; i < statuses.length(); i++) {
            JSONObject jsonStatus = statuses.getJSONObject(i);
            Tweet status = new Tweet(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            status.setId(jsonStatus.getInt("id"));
            status.setIdStr(jsonStatus.getString("id_str"));
            status.setText(jsonStatus.getString("text"));
            //TODO Add all the other crap, repetitive af, i'll handle it.
        }
    }

}
