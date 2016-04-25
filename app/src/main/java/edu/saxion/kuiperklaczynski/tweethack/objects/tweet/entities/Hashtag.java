package edu.saxion.kuiperklaczynski.tweethack.objects.tweet.entities;


import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.Entity;
import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.entities.media.Media;

/**
 * Created by leonk on 25/04/2016.
 */
public class Hashtag extends Entity {
    private String text;
    private int[] indices;

    public Hashtag(Hashtag[] hashtag, Media[] media, URL[] URLs, UserMention[] user_mentions, String text, int[] indices) {
        super(hashtag, media, URLs, user_mentions);
        this.text = text;
        this.indices = indices;
    }
}
