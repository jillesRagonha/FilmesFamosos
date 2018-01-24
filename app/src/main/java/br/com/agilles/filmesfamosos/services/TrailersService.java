package br.com.agilles.filmesfamosos.services;

import br.com.agilles.filmesfamosos.DTO.TrailersDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jille on 22/01/2018.
 */

public interface TrailersService {

    @GET("{id}/videos")
    Call<TrailersDTO> getMovieTrailers(@Path("id") Long id, @Query("api_key") String apiKey);


}
