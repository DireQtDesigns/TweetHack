package edu.saxion.kuiperklaczynski.tweethack.objects.tweet;

import java.math.BigInteger;

/**
 * Created by leonk on 25/04/2016.
 */
public class Contributor {
    private BigInteger id;
    private String id_str;
    private String screen_name;

    public Contributor(BigInteger id, String id_str, String screen_name) {
        this.id = id;
        this.id_str = id_str;
        this.screen_name = screen_name;
    }
}
