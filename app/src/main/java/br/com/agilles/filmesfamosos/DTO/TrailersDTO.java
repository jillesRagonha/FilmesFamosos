package br.com.agilles.filmesfamosos.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import br.com.agilles.filmesfamosos.models.Trailer;

/**
 * Created by jille on 22/01/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrailersDTO {

    @JsonProperty("results")
    private List<Trailer> trailers = new ArrayList<Trailer>();


    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }
}
