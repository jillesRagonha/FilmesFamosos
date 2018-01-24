package br.com.agilles.filmesfamosos.services;

import br.com.agilles.filmesfamosos.DTO.ReviewsDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jille on 23/01/2018.
 */

public interface ReviewsService {
    @GET("{id}/reviews")
    Call<ReviewsDTO> getReviews(@Path("id") Long id, @Query("api_key") String apiKey);

}
