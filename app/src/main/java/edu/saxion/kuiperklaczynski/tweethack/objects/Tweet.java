package edu.saxion.kuiperklaczynski.tweethack.objects;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;

import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.Contributor;
import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.Coordinates;
import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.Entity;
import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.Place;
import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.entities.media.Media;

/**
 * Created by leonk on 25/04/2016.
 */
public class Tweet implements Serializable {
    private final String TAG = "Tweet";
    private boolean hasGif = false;

    private String createdAt;
    private long id;
    private String idStr;
    private String text;
    //private Annotation annotation cba tho
    private Contributor[] contributor;
    private Coordinates coordinates;
    private String created_at;
    private User current_user_retweet;
    private Entity[] entities;
    private Integer favorite_count;
    private Boolean favorited;
    private String filter_level;
    //private Geo geo Wtf
    private String in_reply_to_screen_name;
    private long in_reply_to_status_id;
    private String in_reply_to_status_id_str;
    private String lang;
    private Place place;
    private Boolean possibly_sensitive;
    private BigInteger quoted_status_id;
    private String quoted_status_id_str;
    private Tweet quoted_status;
    //private Scope scope FUCK ADS THO
    private Integer retweet_count;
    private Boolean retweeted;
    private Tweet retweeted_status;
    private String source;
    private Boolean truncated;
    private User user;
    private Boolean withheld_copyright;
    private String[] withheld_in_countries;
    private String withheld_scope;
    private Media media;

    public Tweet(JSONObject tweet) {
        try {
            user = new User(tweet.getJSONObject("user"));

            idStr = tweet.getString("id_str");
            text = tweet.getString("text").replace("&amp;", "&");
            created_at = tweet.getString("created_at");
            in_reply_to_status_id_str = tweet.getString("in_reply_to_status_id_str");
            id = tweet.getLong("id");
            if(tweet.getJSONObject("entities") != null && tweet.getJSONObject("entities").has("media") && tweet.getJSONObject("entities").getJSONArray("media") != null && tweet.getJSONObject("entities").getJSONArray("media").get(0) != null) {
                media = new Media(tweet.getJSONObject("entities").getJSONArray("media").getJSONObject(0)); // Because consistency
                media.loadUrls(tweet.getJSONObject("entities"));

                String oriText = text;
                if(getMedia().getMedia_URL().endsWith("gif") || getMedia().getMedia_URL().contains("tweet_video_thumb")) {
                    hasGif = true;
                } else {
                    Log.e(TAG, "Tweet: media url: "+ getMedia().getMedia_URL());
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < oriText.length(); i++) {
                        if (i < media.getIndices()[0] || i > media.getIndices()[1])
                            sb.append(oriText.charAt(i));
                    }
                    text = sb.toString();
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Tweet: ", e);
        }
    }

    public Media getMedia() {
        return media;
    }

    public Tweet() {}

    public Boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Contributor[] getContributor() {
        return contributor;
    }

    public void setContributor(Contributor[] contributor) {
        this.contributor = contributor;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public User getCurrent_user_retweet() {
        return current_user_retweet;
    }

    public void setCurrent_user_retweet(User current_user_retweet) {
        this.current_user_retweet = current_user_retweet;
    }

    public Entity[] getEntities() {
        return entities;
    }

    public void setEntities(Entity[] entities) {
        this.entities = entities;
    }

    public Integer getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(Integer favorite_count) {
        this.favorite_count = favorite_count;
    }

    public String getFilter_level() {
        return filter_level;
    }

    public void setFilter_level(String filter_level) {
        this.filter_level = filter_level;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public long getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id(long in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public String getIn_reply_to_status_id_str() {
        return in_reply_to_status_id_str;
    }

    public void setIn_reply_to_status_id_str(String in_reply_to_status_id_str) {
        this.in_reply_to_status_id_str = in_reply_to_status_id_str;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Boolean isPossibly_sensitive() {
        return possibly_sensitive;
    }

    public void setPossibly_sensitive(Boolean possibly_sensitive) {
        this.possibly_sensitive = possibly_sensitive;
    }

    public BigInteger getQuoted_status_id() {
        return quoted_status_id;
    }

    public void setQuoted_status_id(BigInteger quoted_status_id) {
        this.quoted_status_id = quoted_status_id;
    }

    public String getQuoted_status_id_str() {
        return quoted_status_id_str;
    }

    public void setQuoted_status_id_str(String quoted_status_id_str) {
        this.quoted_status_id_str = quoted_status_id_str;
    }

    public Tweet getQuoted_status() {
        return quoted_status;
    }

    public void setQuoted_status(Tweet quoted_status) {
        this.quoted_status = quoted_status;
    }

    public Integer getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(Integer retweet_count) {
        this.retweet_count = retweet_count;
    }

    public Boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(Boolean retweeted) {
        this.retweeted = retweeted;
    }

    public Tweet getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(Tweet retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(Boolean truncated) {
        this.truncated = truncated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean isWithheld_copyright() {
        return withheld_copyright;
    }

    public void setWithheld_copyright(Boolean withheld_copyright) {
        this.withheld_copyright = withheld_copyright;
    }

    public String[] getWithheld_in_countries() {
        return withheld_in_countries;
    }

    public void setWithheld_in_countries(String[] withheld_in_countries) {
        this.withheld_in_countries = withheld_in_countries;
    }

    public String getWithheld_scope() {
        return withheld_scope;
    }

    public void setWithheld_scope(String withheld_scope) {
        this.withheld_scope = withheld_scope;
    }

    public boolean hasGif() {
        return hasGif;
    }
}
