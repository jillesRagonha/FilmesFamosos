package br.com.agilles.filmesfamosos.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jille on 23/01/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {

    @JsonProperty("author")
    private String autorName;
    @JsonProperty("content")
    private String review;


    public String getAutorName() {
        return autorName;
    }

    public void setAutorName(String autorName) {
        this.autorName = autorName;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
