package br.com.agilles.filmesfamosos.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import br.com.agilles.filmesfamosos.models.Review;

/**
 * Created by jille on 23/01/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewsDTO {

    @JsonProperty("results")
    private List<Review> reviews = new ArrayList<>();

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
