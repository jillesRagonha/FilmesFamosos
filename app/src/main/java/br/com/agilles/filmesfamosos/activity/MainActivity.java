package br.com.agilles.filmesfamosos.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.agilles.filmesfamosos.DTO.MoviesDTO;
import br.com.agilles.filmesfamosos.R;
import br.com.agilles.filmesfamosos.adapters.MoviesAdapter;
import br.com.agilles.filmesfamosos.models.Movie;
import br.com.agilles.filmesfamosos.retrofit.RetrofitStarter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClicked {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private List<Movie> movies = new ArrayList<>();
    private ProgressBar mLoadingProgressBar;
    private TextView mErrorMessageTextView;
    private final Activity activity = MainActivity.this;
    private MoviesAdapter mMoviesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //call the method to initialize my views
        initViews();
    }

    /**
     * Initialize views
     */
    private void initViews() {

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_lista_filmes);
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);

        //Change layout according device orientation
        GridLayoutManager gridManager;
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridManager = new GridLayoutManager(MainActivity.this, 2);

        } else {
            gridManager = new GridLayoutManager(MainActivity.this, 3);
        }

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(gridManager);


        //Call the method to load popular movies
        loadPopularMovies();
    }

    /**
     * Load Popular Movies Using Retrofit
     */
    private void loadPopularMovies() {
        showProgressBar();

        Call<MoviesDTO> call = new RetrofitStarter().getMovieService().retrievePopularMovies(getString(R.string.the_movie_db_api_key));

        call.enqueue(new Callback<MoviesDTO>() {

            @Override
            public void onResponse(Call<MoviesDTO> call, Response<MoviesDTO> response) {
                addMoviesToList(response);
            }

            @Override
            public void onFailure(Call<MoviesDTO> call, Throwable t) {
                Log.e(TAG, "error loading popular movies: " + t.getMessage());
                showErrorMessage();
            }
        });

    }

    private void showErrorMessage() {
        if (mLoadingProgressBar.isShown()) {
            mLoadingProgressBar.setVisibility(View.INVISIBLE);
        }
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mLoadingProgressBar.setVisibility(View.VISIBLE);
    }


    private void hideProgressBar() {
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
    }


    private void initAdapter(List<Movie> movies) {
        if (!mRecyclerView.isShown()) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        mMoviesAdapter = new MoviesAdapter(movies, MainActivity.this);
        mMoviesAdapter.setOnClick(this);
        mRecyclerView.setAdapter(mMoviesAdapter);
        hideProgressBar();
    }

    private void loadTopRatedMovies() {
        showProgressBar();
        Call<MoviesDTO> call = new RetrofitStarter().getMovieService().retriveTopRatedMovies(getString(R.string.the_movie_db_api_key));

        call.enqueue(new Callback<MoviesDTO>() {
            @Override
            public void onResponse(Call<MoviesDTO> call, Response<MoviesDTO> response) {
                addMoviesToList(response);
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<MoviesDTO> call, Throwable t) {
                Log.e("ERROR", "error loading top rated movies: " + t.getMessage());
                showErrorMessage();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        URL url = null;
        int id = item.getItemId();
        switch (id) {
            case R.id.mi_popular:
                loadPopularMovies();
                break;
            case R.id.mi_melhor_avaliado:
                loadTopRatedMovies();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(int position) {
        Movie movie = movies.get(position);
        Intent intentSelectedMovieDetail = new Intent(this, MovieDetailActivity.class);
        intentSelectedMovieDetail.putExtra("selectedMovie", movie);
        startActivity(intentSelectedMovieDetail);
    }

    private void addMoviesToList(Response<MoviesDTO> response) {
        MoviesDTO moviesDTO = response.body();
        movies = new ArrayList<>();

        movies.addAll(moviesDTO.getMovies());

        initAdapter(movies);
    }

//TODO 1 - Criar activity para trailers --
//TODO 2 - Puxar trailers da api --
//TODO 3 - executar os trailers --
//TODO 4 - Criar parte de criticas--
//TODO 5 - Puxar criticas da api--
//TODO 6 - Exibir criticas--

}
