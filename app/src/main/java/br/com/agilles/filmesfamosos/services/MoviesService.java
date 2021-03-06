package br.com.agilles.filmesfamosos.services;

import br.com.agilles.filmesfamosos.DTO.MoviesDTO;
import br.com.agilles.filmesfamosos.models.Movie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Jilles on 06/12/2017.
 */

public interface MoviesService {
    @GET("popular")
    Call<MoviesDTO> retrievePopularMovies(@Query("api_key") String apiKey);

    @GET("top_rated")
    Call<MoviesDTO> retriveTopRatedMovies(@Query("api_key") String apiKey);


    @GET("{id}")
    Call<Movie> getFavoriteMovieDetail(@Path("id")Long id, @Query("api_key")String apiKey);
}
