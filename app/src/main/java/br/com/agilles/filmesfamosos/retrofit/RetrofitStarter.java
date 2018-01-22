package br.com.agilles.filmesfamosos.retrofit;

import br.com.agilles.filmesfamosos.services.MoviesService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Jilles on 06/12/2017.
 * Criação da classe para inicializar o retrofit
 */


public class RetrofitStarter {

    private final Retrofit retrofit;


    public RetrofitStarter() {

        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/movie/")
                .addConverterFactory(JacksonConverterFactory.create()).build();


    }

    public MoviesService getMovieService() {
        return retrofit.create(MoviesService.class);
    }
}
