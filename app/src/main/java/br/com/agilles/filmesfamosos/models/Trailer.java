package br.com.agilles.filmesfamosos.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by jille on 22/01/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trailer {

    private final static String COVER_TRAILER_URL_BASE = "http://img.youtube.com/vi/";
    private final static String COVER_TRAILER_IMAGE = "/0.jpg";
    private String id;
    private String key;
    private String name;
    private String coverArtPath;

    public static String getCoverTrailerUrlBase() {
        return COVER_TRAILER_URL_BASE;
    }

    public static String getCoverTrailerImage() {
        return COVER_TRAILER_IMAGE;
    }

    public String getCoverArtPath() {
        return COVER_TRAILER_URL_BASE + key + COVER_TRAILER_IMAGE;
    }

    public void setCoverArtPath(String coverArtPath) {
        this.coverArtPath = coverArtPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
