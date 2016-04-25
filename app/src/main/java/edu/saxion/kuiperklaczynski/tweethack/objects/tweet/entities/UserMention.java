package edu.saxion.kuiperklaczynski.tweethack.objects.tweet.entities;

/**
 * Created by leonk on 25/04/2016.
 */
public class UserMention {
    private int id;
    private String id_str;
    private int[] indices;
    private String name;
    private String screen_name;

    public UserMention(int id, String id_str, int[] indices, String name, String screen_name) {
        this.id = id;
        this.id_str = id_str;
        this.indices = indices;
        this.name = name;
        this.screen_name = screen_name;
    }
}
