package edu.saxion.kuiperklaczynski.tweethack.objects.tweet;


import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.entities.Hashtag;
import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.entities.URL;
import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.entities.UserMention;
import edu.saxion.kuiperklaczynski.tweethack.objects.tweet.entities.media.Media;

/**
 * Created by leonk on 25/04/2016.
 */
public class Entity {

    private Hashtag[] hashtag;
    private Media[] media;
    private URL[] URLs;
    private UserMention[] user_mentions;

    public Entity(Hashtag[] hashtag, Media[] media, URL[] URLs, UserMention[] user_mentions) {
        this.hashtag = hashtag;
        this.media = media;
        this.URLs = URLs;
        this.user_mentions = user_mentions;
    }
}
