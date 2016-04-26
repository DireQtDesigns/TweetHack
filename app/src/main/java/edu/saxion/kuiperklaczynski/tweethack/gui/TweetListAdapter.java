package edu.saxion.kuiperklaczynski.tweethack.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.saxion.kuiperklaczynski.tweethack.R;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;

/**
 * Created by Leonk on 26-4-2016.
 */
public class TweetListAdapter extends ArrayAdapter<Tweet> {


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
        avatarView.setImageResource(R.drawable.aliens);

        return super.getView(position, convertView, parent);
    }
}
