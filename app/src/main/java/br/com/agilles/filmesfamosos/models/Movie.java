package br.com.agilles.filmesfamosos.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jilles on 05/12/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie implements Serializable {
    private final static String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/";
    private final static String IMAGE_BASE_PATH_SIZE = "w500";

    @JsonProperty("release_date")
    private String releaseDate;
    private Long id;
    private String title;
    @JsonProperty("original_title")
    private String originalName;
    @JsonProperty("original_language")
    private String language;
    @JsonProperty("poster_path")
    private String posterPath;
    private String overview;

    private List<Trailer> trailers;

    @JsonProperty("vote_average")
    private float voteAverage;

    public float getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return IMAGE_BASE_PATH + IMAGE_BASE_PATH_SIZE + posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public static String getImageBasePath() {
        return IMAGE_BASE_PATH;
    }

    public static String getImageBasePathSize() {
        return IMAGE_BASE_PATH_SIZE;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }
}
