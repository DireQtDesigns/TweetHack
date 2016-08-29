package edu.saxion.kuiperklaczynski.tweethack.gui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import edu.saxion.kuiperklaczynski.tweethack.net.DownloadImageTask;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;

/**
 * Created by Leonk on 26-4-2016.
 */
public class TweetListAdapter extends ArrayAdapter<Tweet> {
    private final String TAG = "TweetListAdapter";

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

    /**
     * fills the given view wih the relevant data form tweets ArrayList
     * @param position position in the arraylist
     * @param convertView view to fill
     * @param parent parent View
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_list_item, parent, false);
        }
        final Tweet tweet;
        if(tweets.size() > 0 && tweets.get(position) != null) {
            tweet = tweets.get(position);
        } else {
            tweet = null;
        }

        TextView nameView = (TextView) convertView.findViewById(R.id.nameView);
        TextView timeView = (TextView) convertView.findViewById(R.id.timeView);
        TextView bodyView = (TextView) convertView.findViewById(R.id.bodyView);
        ImageView avatarView = (ImageView) convertView.findViewById(R.id.avatarView);

        //Setting values
        if(tweet != null) {
            nameView.setText(tweet.getUser().getName());
            if (tweet.getCreated_at() == null) {
                Log.d(TAG, "getView: getCreated_at() is null");
            } else {
                timeView.setText(StringDateConverter.agoString(System.currentTimeMillis(), StringDateConverter.dateFromJSONString(tweet.getCreated_at())));
            }
            bodyView.setText(tweet.getText());
            if (tweet.getMedia() != null) {
                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.colorAccent3));
            } else {
                convertView.setBackgroundColor(Color.WHITE);
            }


            //Image fetching from URL
            String imgURL = tweet.getUser().getProfile_image_url();
            new DownloadImageTask(avatarView).execute(imgURL);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.getInstance(), TweetDetailActivity.class);
                    intent.putExtra("TweetID", tweets.get(position).getId());
                    MainActivity.getInstance().startActivity(intent);
                }
            });
        }

        return convertView;
    }
}
