package edu.saxion.kuiperklaczynski.tweethack.objects;


import android.support.annotation.Nullable;

import java.lang.reflect.Type;
import java.math.BigInteger;

import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.Entity;

/**
 * Created by leonk on 25/04/2016.
 */
public class User {
    private Boolean contributors_enabled;
    private String created_at;
    private Boolean default_profile;
    private String description;
    private Entity[] entities;
    private Integer favourites_count;
    private Type following;
    private Type follow_request_sent;
    private Integer followers_count;
    private Integer friends_count;
    private Boolean geo_enabled;
    private BigInteger id;
    private String id_str;
    private Boolean is_translator;
    private String lang;
    private Integer listed_count;
    private String location;
    private String name;
    private Boolean notifications;
    private String profile_background_color;
    private String profile_background_image_url;
    private String profile_background_image_url_https;
    private Boolean profile_background_tile;
    private String profile_banner_url;
    private String profile_image_url;
    private String profile_image_url_https;
    private String profile_link_color;
    private String profile_sidebar_border_color;
    private String profile_sidebar_fill_color;
    private String profile_text_color;
    private Boolean show_all_inline_media;
    private Tweet status;
    private Integer statuses_count;
    private @Nullable
    String time_zone;
    private String url;
    private Integer itc_offset;
    private Boolean verified;
    private String withheld_in_countries;
    private String withheld_scope;

    public User(Boolean is_translator, Boolean contributors_enabled, String created_at, Boolean default_profile, String description, Entity[] entities, Integer favourites_count, Type following, Type follow_request_sent, Integer followers_count, Integer friends_count, Boolean geo_enabled, BigInteger id, String id_str, String lang, Integer listed_count, String location, String name, Boolean notifications, String profile_background_color, String profile_background_image_url, String profile_background_image_url_https, Boolean profile_background_tile, String profile_banner_url, String profile_image_url, String profile_image_url_https, String profile_link_color, String profile_sidebar_border_color, String profile_sidebar_fill_color, String profile_text_color, Boolean show_all_inline_media, Tweet status, Integer statuses_count, String time_zone, String url, Integer itc_offset, Boolean verified, String withheld_in_countries, String withheld_scope) {
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

    public Boolean getGeo_enabled() {
        return geo_enabled;
    }

    public void setGeo_enabled(Boolean geo_enabled) {
        this.geo_enabled = geo_enabled;
    }

    public Boolean getContributors_enabled() {
        return contributors_enabled;
    }

    public void setContributors_enabled(Boolean contributors_enabled) {
        this.contributors_enabled = contributors_enabled;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Boolean getDefault_profile() {
        return default_profile;
    }

    public void setDefault_profile(Boolean default_profile) {
        this.default_profile = default_profile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Entity[] getEntities() {
        return entities;
    }

    public void setEntities(Entity[] entities) {
        this.entities = entities;
    }

    public Integer getFavourites_count() {
        return favourites_count;
    }

    public void setFavourites_count(Integer favourites_count) {
        this.favourites_count = favourites_count;
    }

    public Type getFollowing() {
        return following;
    }

    public void setFollowing(Type following) {
        this.following = following;
    }

    public Type getFollow_request_sent() {
        return follow_request_sent;
    }

    public void setFollow_request_sent(Type follow_request_sent) {
        this.follow_request_sent = follow_request_sent;
    }

    public Integer getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(Integer followers_count) {
        this.followers_count = followers_count;
    }

    public Integer getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(Integer friends_count) {
        this.friends_count = friends_count;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public Boolean getIs_translator() {
        return is_translator;
    }

    public void setIs_translator(Boolean is_translator) {
        this.is_translator = is_translator;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Integer getListed_count() {
        return listed_count;
    }

    public void setListed_count(Integer listed_count) {
        this.listed_count = listed_count;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNotifications() {
        return notifications;
    }

    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }

    public String getProfile_background_color() {
        return profile_background_color;
    }

    public void setProfile_background_color(String profile_background_color) {
        this.profile_background_color = profile_background_color;
    }

    public String getProfile_background_image_url() {
        return profile_background_image_url;
    }

    public void setProfile_background_image_url(String profile_background_image_url) {
        this.profile_background_image_url = profile_background_image_url;
    }

    public String getProfile_background_image_url_https() {
        return profile_background_image_url_https;
    }

    public void setProfile_background_image_url_https(String profile_background_image_url_https) {
        this.profile_background_image_url_https = profile_background_image_url_https;
    }

    public Boolean getProfile_background_tile() {
        return profile_background_tile;
    }

    public void setProfile_background_tile(Boolean profile_background_tile) {
        this.profile_background_tile = profile_background_tile;
    }

    public String getProfile_banner_url() {
        return profile_banner_url;
    }

    public void setProfile_banner_url(String profile_banner_url) {
        this.profile_banner_url = profile_banner_url;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getProfile_image_url_https() {
        return profile_image_url_https;
    }

    public void setProfile_image_url_https(String profile_image_url_https) {
        this.profile_image_url_https = profile_image_url_https;
    }

    public String getProfile_link_color() {
        return profile_link_color;
    }

    public void setProfile_link_color(String profile_link_color) {
        this.profile_link_color = profile_link_color;
    }

    public String getProfile_sidebar_border_color() {
        return profile_sidebar_border_color;
    }

    public void setProfile_sidebar_border_color(String profile_sidebar_border_color) {
        this.profile_sidebar_border_color = profile_sidebar_border_color;
    }

    public String getProfile_sidebar_fill_color() {
        return profile_sidebar_fill_color;
    }

    public void setProfile_sidebar_fill_color(String profile_sidebar_fill_color) {
        this.profile_sidebar_fill_color = profile_sidebar_fill_color;
    }

    public String getProfile_text_color() {
        return profile_text_color;
    }

    public void setProfile_text_color(String profile_text_color) {
        this.profile_text_color = profile_text_color;
    }

    public Boolean getShow_all_inline_media() {
        return show_all_inline_media;
    }

    public void setShow_all_inline_media(Boolean show_all_inline_media) {
        this.show_all_inline_media = show_all_inline_media;
    }

    public Tweet getStatus() {
        return status;
    }

    public void setStatus(Tweet status) {
        this.status = status;
    }

    public Integer getStatuses_count() {
        return statuses_count;
    }

    public void setStatuses_count(Integer statuses_count) {
        this.statuses_count = statuses_count;
    }

    @Nullable
    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(@Nullable String time_zone) {
        this.time_zone = time_zone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getItc_offset() {
        return itc_offset;
    }

    public void setItc_offset(Integer itc_offset) {
        this.itc_offset = itc_offset;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getWithheld_in_countries() {
        return withheld_in_countries;
    }

    public void setWithheld_in_countries(String withheld_in_countries) {
        this.withheld_in_countries = withheld_in_countries;
    }

    public String getWithheld_scope() {
        return withheld_scope;
    }

    public void setWithheld_scope(String withheld_scope) {
        this.withheld_scope = withheld_scope;
    }
}
