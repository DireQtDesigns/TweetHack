package edu.saxion.kuiperklaczynski.tweethack.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.channels.FileLockInterruptionException;

/**
 * Created by leonk on 27/04/2016.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    ImageView bmImage;
    String urldisplay;
    public static final String TAG = "TweetHax_TwListAdapter"; //Log Tag
    boolean success = false;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
         urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            success = true;
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if(success) bmImage.setImageBitmap(result);
    }
}