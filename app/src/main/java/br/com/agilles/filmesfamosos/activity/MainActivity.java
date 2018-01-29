package br.com.agilles.filmesfamosos.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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
import br.com.agilles.filmesfamosos.adapters.FavoriteMoviesAdapter;
import br.com.agilles.filmesfamosos.adapters.MoviesAdapter;
import br.com.agilles.filmesfamosos.data.contract.ContentProviderContract;
import br.com.agilles.filmesfamosos.data.contract.DBContract;
import br.com.agilles.filmesfamosos.models.Movie;
import br.com.agilles.filmesfamosos.retrofit.RetrofitStarter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClicked, FavoriteMoviesAdapter.OnItemClicked, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private List<Movie> movies = new ArrayList<>();
    private List<Movie> favoriteMovies = new ArrayList<>();
    private ProgressBar mLoadingProgressBar;
    private TextView mErrorMessageTextView;
    private final Activity activity = MainActivity.this;
    private MoviesAdapter mMoviesAdapter;
    private FavoriteMoviesAdapter mFavoriteMoviesAdapter;
    private static final String MOVIE_LIST_CHOICE = "Movie List Choice";
    private int listMovieId;
    private static final int POP_MOVIES = 1;
    private static final int TOP_MOVIES = 2;
    private static final int FAVORITE_MOVIES = 3;
    private static final int MOVIE_LOADER_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            this.listMovieId = savedInstanceState.getInt(MOVIE_LIST_CHOICE);
        }
        //call the method to initialize my views

        initViews();

        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    /**
     * Inicializa as views
     */
    private void initViews() {
        mFavoriteMoviesAdapter = new FavoriteMoviesAdapter(this, favoriteMovies);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_lista_filmes);
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        mFavoriteMoviesAdapter = new FavoriteMoviesAdapter(this, favoriteMovies);


        //Change layout according device orientation
        GridLayoutManager gridManager;
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridManager = new GridLayoutManager(MainActivity.this, 2);

        } else {
            gridManager = new GridLayoutManager(MainActivity.this, 3);
        }

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(gridManager);


        if (listMovieId != 0) {
            switch (listMovieId) {
                case POP_MOVIES:
                    loadPopularMovies();
                    break;
                case TOP_MOVIES:
                    loadTopRatedMovies();
                    break;
                case FAVORITE_MOVIES:
                    loadFavoriteMovies();
                    break;
                default:
                    loadTopRatedMovies();
            }
        } else {
            loadPopularMovies();
        }

    }

    /**
     * Load Popular Movies Using Retrofit
     */
    private void loadPopularMovies() {
        listMovieId = POP_MOVIES;
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
        listMovieId = TOP_MOVIES;

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
            case R.id.mi_favorite:
                loadFavoriteMovies();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFavoriteMovies() {
        listMovieId = FAVORITE_MOVIES;

        showProgressBar();

        mFavoriteMoviesAdapter.setOnCLick(this);
        mRecyclerView.setAdapter(mFavoriteMoviesAdapter);
        hideProgressBar();
    }


    @Override
    public void onItemClick(int position) {
        Movie movie = movies.get(position);
        Intent intentSelectedMovieDetail = new Intent(this, MovieDetailActivity.class);
        intentSelectedMovieDetail.putExtra("selectedMovie", movie);
        intentSelectedMovieDetail.putExtra("posterPath", movie.getFinalPosterPath());

        startActivity(intentSelectedMovieDetail);
    }

    @Override
    public void onItemClicked(int position) {
        Movie movie = favoriteMovies.get(position);
        Intent intentSelectedMovieDetail = new Intent(this, MovieDetailActivity.class);
        intentSelectedMovieDetail.putExtra("selectedMovie", movie);
        intentSelectedMovieDetail.putExtra("posterPath", movie.getPosterPath());
        startActivity(intentSelectedMovieDetail);

    }

    private void addMoviesToList(Response<MoviesDTO> response) {
        MoviesDTO moviesDTO = response.body();
        movies = new ArrayList<>();
        movies.addAll(moviesDTO.getMovies());

        initAdapter(movies);
    }

    /**
     * salvar estado da activity
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(MOVIE_LIST_CHOICE, listMovieId);
        super.onSaveInstanceState(outState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(ContentProviderContract.CONTENT_URI, null,
                            null, null, DBContract.FavoriteMoviesEntry.COLUMN_NAME_TITLE);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favoriteMovies = new ArrayList<>();

        while (data.moveToNext()) {
            Movie movie = new Movie();
            int favorite = data.getInt(data.getColumnIndex(DBContract.FavoriteMoviesEntry.COLUMN_NAME_FAVORITE));
            movie.setId(data.getLong(data.getColumnIndex(DBContract.FavoriteMoviesEntry.COLUMN_NAME_KEY)));
            movie.setTitle(data.getString(data.getColumnIndex(DBContract.FavoriteMoviesEntry.COLUMN_NAME_TITLE)));
            movie.setOverview(data.getString(data.getColumnIndex(DBContract.FavoriteMoviesEntry.COLUMN_NAME_OVERVIEW)));
            movie.setLanguage(data.getString(data.getColumnIndex(DBContract.FavoriteMoviesEntry.COLUMN_NAME_LANGUAGE)));
            movie.setReleaseDate(data.getString(data.getColumnIndex(DBContract.FavoriteMoviesEntry.COLUMN_NAME_RELEASE_DATE)));
            movie.setPosterPath(data.getString(data.getColumnIndex(DBContract.FavoriteMoviesEntry.COLUMN_NAME_POSTERPATH)));
            movie.setVoteAverage(data.getFloat(data.getColumnIndex(DBContract.FavoriteMoviesEntry.COLUMN_NAME_AVERAGE)));

            if (favorite == 1) {
                movie.setFavorite(true);
            }
            favoriteMovies.add(movie);
        }
        mFavoriteMoviesAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavoriteMoviesAdapter.swapCursor(null);
    }
}
