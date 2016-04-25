package edu.saxion.kuiperklaczynski.tweethack.twitterobjects;

import android.support.annotation.Nullable;

import java.lang.reflect.Type;
import java.math.BigInteger;

import edu.saxion.kuiperklaczynski.tweethack.twitterobjects.tweet.Entity;
import edu.saxion.kuiperklaczynski.tweethack.twitterobjects.Tweet;

/**
 * Created by leonk on 25/04/2016.
 */
public class User {
    private boolean contributors_enabled;
    private String created_at;
    private boolean default_profile;
    private String description;
    private Entity[] entities;
    private int favourites_count;
    private Type following;
    private Type follow_request_sent;
    private int followers_count;
    private int friends_count;
    private boolean geo_enabled;
    private BigInteger id;
    private String id_str;
    private boolean is_translator;
    private String lang;
    private int listed_count;
    private String location;
    private String name;
    private boolean notifications;
    private String profile_background_color;
    private String profile_background_image_url;
    private String profile_background_image_url_https;
    private boolean profile_background_tile;
    private String profile_banner_url;
    private String profile_image_url;
    private String profile_image_url_https;
    private String profile_link_color;
    private String profile_sidebar_border_color;
    private String profile_sidebar_fill_color;
    private String profile_text_color;
    private boolean show_all_inline_media;
    private Tweet status;
    private int statuses_count;
    private @Nullable String time_zone;
    private String url;
    private int itc_offset;
    private boolean verified;
    private String withheld_in_countries;
    private String withheld_scope;

}
