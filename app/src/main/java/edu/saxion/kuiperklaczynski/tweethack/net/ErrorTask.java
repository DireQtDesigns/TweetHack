package edu.saxion.kuiperklaczynski.tweethack.net;

import android.os.AsyncTask;

import java.util.ArrayList;

import edu.saxion.kuiperklaczynski.tweethack.objects.ErrorCarrier;
import edu.saxion.kuiperklaczynski.tweethack.objects.Tweet;

/**
 * Created by Robin on 9-7-2016.
 */
public abstract class ErrorTask<E,F,G> extends AsyncTask<E,F,G> {

    /**
     * when something goes wrong, this is called
     * @param code http error code
     * @return Tweetlist
     */
    protected ArrayList<Tweet> failureHelper(int code) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        tweets.add(new ErrorCarrier(code));
        return tweets;
    }
}
