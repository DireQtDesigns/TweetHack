package edu.saxion.kuiperklaczynski.tweethack.objects;


import android.support.annotation.Nullable;

import java.lang.reflect.Type;
import java.math.BigInteger;

import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.Entity;

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
    private @Nullable
    String time_zone;
    private String url;
    private int itc_offset;
    private boolean verified;
    private String withheld_in_countries;
    private String withheld_scope;

    public User(boolean is_translator, boolean contributors_enabled, String created_at, boolean default_profile, String description, Entity[] entities, int favourites_count, Type following, Type follow_request_sent, int followers_count, int friends_count, boolean geo_enabled, BigInteger id, String id_str, String lang, int listed_count, String location, String name, boolean notifications, String profile_background_color, String profile_background_image_url, String profile_background_image_url_https, boolean profile_background_tile, String profile_banner_url, String profile_image_url, String profile_image_url_https, String profile_link_color, String profile_sidebar_border_color, String profile_sidebar_fill_color, String profile_text_color, boolean show_all_inline_media, Tweet status, int statuses_count, String time_zone, String url, int itc_offset, boolean verified, String withheld_in_countries, String withheld_scope) {
        this.is_translator = is_translator;
        this.contributors_enabled = contributors_enabled;
        this.created_at = created_at;
        this.default_profile = default_profile;
        this.description = description;
        this.entities = entities;
        this.favourites_count = favourites_count;
        this.following = following;
        this.follow_request_sent = follow_request_sent;
        this.followers_count = followers_count;
        this.friends_count = friends_count;
        this.geo_enabled = geo_enabled;
        this.id = id;
        this.id_str = id_str;
        this.lang = lang;
        this.listed_count = listed_count;
        this.location = location;
        this.name = name;
        this.notifications = notifications;
        this.profile_background_color = profile_background_color;
        this.profile_background_image_url = profile_background_image_url;
        this.profile_background_image_url_https = profile_background_image_url_https;
        this.profile_background_tile = profile_background_tile;
        this.profile_banner_url = profile_banner_url;
        this.profile_image_url = profile_image_url;
        this.profile_image_url_https = profile_image_url_https;
        this.profile_link_color = profile_link_color;
        this.profile_sidebar_border_color = profile_sidebar_border_color;
        this.profile_sidebar_fill_color = profile_sidebar_fill_color;
        this.profile_text_color = profile_text_color;
        this.show_all_inline_media = show_all_inline_media;
        this.status = status;
        this.statuses_count = statuses_count;
        this.time_zone = time_zone;
        this.url = url;
        this.itc_offset = itc_offset;
        this.verified = verified;
        this.withheld_in_countries = withheld_in_countries;
        this.withheld_scope = withheld_scope;
    }
}
