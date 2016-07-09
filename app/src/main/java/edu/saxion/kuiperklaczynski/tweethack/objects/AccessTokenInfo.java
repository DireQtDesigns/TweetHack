package edu.saxion.kuiperklaczynski.tweethack.objects;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth10aService;

/**
 * Created by Robin on 9-7-2016.
 */
public abstract class AccessTokenInfo {
    private static final String API_KEY = "4LiUJZIHjuFT6IVaGBCZooSRw", API_SECRET = "yrxAVjSOd7oyqOKCSwpAVKCsktOlw0rR8ZwjGUOQNnyxiz13QL";
    private static final String callback = "4chan.org";

    private static final OAuth10aService service = new ServiceBuilder()
            .apiKey(API_KEY)
            .apiSecret(API_SECRET)
            .callback(callback)
            .build(TwitterApi.instance());

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getApiSecret() {
        return API_SECRET;
    }

    public static String getCallback() {
        return callback;
    }

    public static OAuth10aService getService() {
        return service;
    }
}
