package edu.saxion.kuiperklaczynski.tweethack.objects.tweet.entities.media;


import java.math.BigInteger;

import edu.saxion.kuiperklaczynski.tweethack.objects.Sizes;

/**
 * Created by leonk on 25/04/2016.
 */
public class Media {

    private String display_URL;
    private String expanded_URL;
    private BigInteger id;
    private String id_string;
    private int[] indices;
    private String media_URL;
    private String media_URL_HTTPS;
    private Sizes sizes;
    private BigInteger source_status_id;
    private String source_status_id_string;
    private String type;
    private String URL;

    public Media(String display_URL, String expanded_URL, BigInteger id, String id_string, int[] indices, String media_URL, String media_URL_HTTPS, Sizes sizes, BigInteger source_status_id, String source_status_id_string, String type, String URL) {
        this.display_URL = display_URL;
        this.expanded_URL = expanded_URL;
        this.id = id;
        this.id_string = id_string;
        this.indices = indices;
        this.media_URL = media_URL;
        this.media_URL_HTTPS = media_URL_HTTPS;
        this.sizes = sizes;
        this.source_status_id = source_status_id;
        this.source_status_id_string = source_status_id_string;
        this.type = type;
        this.URL = URL;
    }
}
