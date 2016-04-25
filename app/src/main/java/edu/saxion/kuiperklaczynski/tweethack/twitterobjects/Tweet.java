package edu.saxion.kuiperklaczynski.tweethack.twitterobjects;

import java.math.BigInteger;

import edu.saxion.kuiperklaczynski.tweethack.twitterobjects.tweet.Contributor;
import edu.saxion.kuiperklaczynski.tweethack.twitterobjects.tweet.Coordinates;
import edu.saxion.kuiperklaczynski.tweethack.twitterobjects.tweet.Entity;

/**
 * Created by leonk on 25/04/2016.
 */
public class Tweet {

    private String createdAt;
    private int id;
    private String idStr;
    private String text;
    //private Annotation anno
    private Contributor[] contributor;
    private Coordinates coordinates;
    private String created_at;
    private User current_user_retweet;
    private Entity[] entities;
    private int favorite_count;
    private boolean favorited;
    private String filter_level;
    //private Geo geo
    private String in_reply_to_screen_name;
    private BigInteger in_reply_to_status_id;
    private String in_reply_to_status_id_str;
    private String lang;
    private Place place;
    private boolean possibly_sensitive;
    private BigInteger quoted_status_id;
    private String quoted_status_id_str;
    private Tweet quoted_status;
    //private Scope scope FUCK ADS THO


}
