package edu.saxion.kuiperklaczynski.tweethack.objects.tweet;

/**
 * Created by leonk on 25/04/2016.
 */
public class Place {

    private BoundingBox boundingBox;
    private String country;
    private String country_code;
    private String full_name;
    private String id;
    private String name;
    private String place_type;
    private String URL;

    public Place(String name, BoundingBox boundingBox, String country, String country_code, String full_name, String id, String place_type, String URL) {
        this.name = name;
        this.boundingBox = boundingBox;
        this.country = country;
        this.country_code = country_code;
        this.full_name = full_name;
        this.id = id;
        this.place_type = place_type;
        this.URL = URL;
    }
}
