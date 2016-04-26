package edu.saxion.kuiperklaczynski.tweethack.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.saxion.kuiperklaczynski.tweethack.R;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;

/**
 * Created by Leonk on 26-4-2016.
 */
public class TweetListAdapter extends ArrayAdapter<Tweet> {

    public static final String TAG = "TweetHax_TwListAdapter"; //Log Tag
    private List<Tweet> tweets;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public TweetListAdapter(Context context, int resource, List<Tweet> objects) {
        super(context, resource, objects);
        tweets = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_list_item, parent, false);
        }
        Tweet tweet = tweets.get(position);
        TextView nameView = (TextView) convertView.findViewById(R.id.nameView);
        TextView timeView = (TextView) convertView.findViewById(R.id.timeView);
        TextView bodyView = (TextView) convertView.findViewById(R.id.timeView);
        ImageView avatarView = (ImageView) convertView.findViewById(R.id.avatarView); //TODO Fetch image bitmap over http, ouch.

        //Setting values
        nameView.setText(tweet.getUser().getName());
        timeView.setText(tweet.getCreated_at());
        bodyView.setText(tweet.getText());

        //Image
        String imgURL = tweet.getUser().getProfile_image_url(); //TODO The formerly mentioned http bs, for now:
        try {
            avatarView.setImageDrawable(drawableFromUrl(imgURL));
        }catch (Exception e) {
            avatarView.setImageResource(R.drawable.aliens);
            Log.e(TAG, "getView: ", e);
        }

        return convertView;
    }

    public static Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }
}
