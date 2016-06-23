package edu.saxion.kuiperklaczynski.tweethack.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by leonk on 27/04/2016.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    /**
     * Downloads an image and then applies it to an Imageview, defined by the constructor.
     */

    ImageView imageViewToApplyTo;
    String urldisplay;
    public static final String TAG = "TweetHax_TwListAdapter"; //Log Tag
    boolean success = false;

    public DownloadImageTask(ImageView bmImage) {
        this.imageViewToApplyTo = bmImage;
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
        if(success) imageViewToApplyTo.setImageBitmap(result);
    }
}