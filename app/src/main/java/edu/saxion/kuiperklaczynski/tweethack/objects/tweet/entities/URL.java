package edu.saxion.kuiperklaczynski.tweethack.objects.tweet.entities;

/**
 * Created by leonk on 25/04/2016.
 */
public class URL {
    private String display_URL;
    private String expanded_display_URL;
    private int[] indices;
    private String type;

    public URL(String display_URL, String expanded_display_URL, int[] indices, String type) {
        this.display_URL = display_URL;
        this.expanded_display_URL = expanded_display_URL;
        this.indices = indices;
        this.type = type;
    }
}
