package edu.saxion.kuiperklaczynski.tweethack.objects;


import java.math.BigInteger;

import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.Contributor;
import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.Coordinates;
import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.Entity;
import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.Place;

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
    private int retweet_count;
    private boolean retweeted;
    private Tweet retweeted_status;
    private String source;
    private boolean truncated;
    private User user;
    private boolean withheld_copyright;
    private String[] withheld_in_countries;
    private String withheld_scope;


    public Tweet(String in_reply_to_screen_name, String createdAt, int id, String idStr, String text, Contributor[] contributor, Coordinates coordinates, String created_at, User current_user_retweet, Entity[] entities, int favorite_count, boolean favorited, String filter_level, BigInteger in_reply_to_status_id, String in_reply_to_status_id_str, String lang, Place place, boolean possibly_sensitive, BigInteger quoted_status_id, String quoted_status_id_str, Tweet quoted_status, int retweet_count, boolean retweeted, Tweet retweeted_status, String source, boolean truncated, User user, boolean withheld_copyright, String[] withheld_in_countries, String withheld_scope) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
        this.createdAt = createdAt;
        this.id = id;
        this.idStr = idStr;
        this.text = text;
        this.contributor = contributor;
        this.coordinates = coordinates;
        this.created_at = created_at;
        this.current_user_retweet = current_user_retweet;
        this.entities = entities;
        this.favorite_count = favorite_count;
        this.favorited = favorited;
        this.filter_level = filter_level;
        this.in_reply_to_status_id = in_reply_to_status_id;
        this.in_reply_to_status_id_str = in_reply_to_status_id_str;
        this.lang = lang;
        this.place = place;
        this.possibly_sensitive = possibly_sensitive;
        this.quoted_status_id = quoted_status_id;
        this.quoted_status_id_str = quoted_status_id_str;
        this.quoted_status = quoted_status;
        this.retweet_count = retweet_count;
        this.retweeted = retweeted;
        this.retweeted_status = retweeted_status;
        this.source = source;
        this.truncated = truncated;
        this.user = user;
        this.withheld_copyright = withheld_copyright;
        this.withheld_in_countries = withheld_in_countries;
        this.withheld_scope = withheld_scope;
    }
}
