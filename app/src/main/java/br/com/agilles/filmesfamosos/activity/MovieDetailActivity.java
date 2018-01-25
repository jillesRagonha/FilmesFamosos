package br.com.agilles.filmesfamosos.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.agilles.filmesfamosos.DTO.ReviewsDTO;
import br.com.agilles.filmesfamosos.DTO.TrailersDTO;
import br.com.agilles.filmesfamosos.R;
import br.com.agilles.filmesfamosos.adapters.ReviewsAdapter;
import br.com.agilles.filmesfamosos.adapters.TrailersAdapter;
import br.com.agilles.filmesfamosos.data.FavoriteMovieDbHelper;
import br.com.agilles.filmesfamosos.models.Movie;
import br.com.agilles.filmesfamosos.models.Review;
import br.com.agilles.filmesfamosos.models.Trailer;
import br.com.agilles.filmesfamosos.retrofit.RetrofitStarter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements TrailersAdapter.OnTrailerClicked, ReviewsAdapter.OnReviewClicked {

    // Criação dos atributos
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private List<Trailer> trailerList;
    private TrailersAdapter trailersAdapter;
    private RecyclerView mRecyclerViewTrailers;
    private List<Review> reviewsList;
    private ReviewsAdapter reviewsAdapter;
    private Long id;
    private RecyclerView mRecyclerViewReviews;
    ImageView btnTrailer;
    TextView mTitleMovieTextView;
    TextView mMovieOverviewTextView;
    ImageView mMoviePosterDetailImageView;
    RatingBar mVoteAverageRatingBar;
    TextView mVoteAverageTextView;
    TextView mMovieReleaseDateTextView;
    ToggleButton mFavoriteToggleButton;
    private SQLiteDatabase mDb;
    FavoriteMovieDbHelper dbHelper;
    Movie movie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        movie = (Movie) intent.getSerializableExtra("selectedMovie");
        this.id = movie.getId();
        dbHelper = new FavoriteMovieDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        initViews();
        if (dbHelper.exists(movie)) {
            mFavoriteToggleButton.setChecked(true);

        }

        initRecyclerViews(id);
        attribValues(movie);

    }


    /**
     * Método que será acionado ao clicar no botão para favoritar
     *
     * @param view toggle button que chama o método
     */
    public void favoriteMovie(View view) {
        if (mFavoriteToggleButton.isChecked()) {
            Toast.makeText(getApplicationContext(),
                    "Toggle is on", Toast.LENGTH_SHORT).show();
            movie.setFavorite(true);
            dbHelper.insert(movie);

        } else {
            Toast.makeText(getApplicationContext(),
                    "Toggle is off", Toast.LENGTH_SHORT).show();
            movie.setFavorite(false);
            dbHelper.remove(movie);
        }
    }

    /**
     * inicialização dos atributos
     *
     * @param movie
     */
    private void attribValues(Movie movie) {
        mTitleMovieTextView.setText(movie.getTitle());
        mMovieOverviewTextView.setText(movie.getOverview());
        mVoteAverageRatingBar.setRating(movie.getVoteAverage());
        mVoteAverageTextView.setText("Vote Average: (" + movie.getVoteAverage() + " )");
        mMovieReleaseDateTextView.setText(getDateConverted(movie.getReleaseDate()));
        String imagemUri = movie.getPosterPath();
        Picasso
                .with(this)
                .load(imagemUri)
                .into(mMoviePosterDetailImageView);
    }

    /**
     * inicialização dos recyclerviews
     *
     * @param id do filme que será usado para chamar os trailers e reviews
     */
    private void initRecyclerViews(Long id) {
        mRecyclerViewTrailers = (RecyclerView) findViewById(R.id.rv_lista_trailers);
        mRecyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewTrailers.setHasFixedSize(true);
        trailerList = new ArrayList<>();
        initTrailers(id);

        mRecyclerViewReviews = (RecyclerView) findViewById(R.id.rv_reviews_list);
        mRecyclerViewReviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewReviews.setHasFixedSize(true);
        reviewsList = new ArrayList<>();
        initReviews(id);


    }

    /**
     * inicialização dos trailers
     *
     * @param id
     */
    private void initTrailers(Long id) {
        Call<TrailersDTO> call = new RetrofitStarter().getTrailerService().getMovieTrailers(id, getString(R.string.the_movie_db_api_key));
        call.enqueue(new Callback<TrailersDTO>() {
            @Override
            public void onResponse(Call<TrailersDTO> call, Response<TrailersDTO> response) {
                addTrailersToList(response);
            }

            @Override
            public void onFailure(Call<TrailersDTO> call, Throwable t) {
                Log.e(TAG, "error loading trailers: " + t.getMessage());

            }
        });
    }

    /**
     * adiciona os trailers a lista de trailers de acordo com o resultado da api
     *
     * @param response
     */
    private void addTrailersToList(Response<TrailersDTO> response) {
        TrailersDTO trailersDTO = response.body();
        this.trailerList.addAll(trailersDTO.getTrailers());

        trailersAdapter = new TrailersAdapter(trailerList, MovieDetailActivity.this);
        trailersAdapter.setOnClick(this);
        mRecyclerViewTrailers.setAdapter(trailersAdapter);
    }

    /**
     * inicializa as views
     */
    private void initViews() {
        btnTrailer = findViewById(R.id.ib_go_trailers);
        mTitleMovieTextView = findViewById(R.id.tv_title_movie_detail);
        mMovieOverviewTextView = findViewById(R.id.tv_overview);
        mMoviePosterDetailImageView = findViewById(R.id.iv_movies_poster_detail);
        mVoteAverageRatingBar = findViewById(R.id.rb_vote_average);
        mVoteAverageTextView = findViewById(R.id.tv_vote_average);
        mMovieReleaseDateTextView = findViewById(R.id.tv_release_date);
        mFavoriteToggleButton = findViewById(R.id.tb_favorite_movie);
    }

    /**
     * inicializa os reviews do filme selecionado
     *
     * @param id
     */
    private void initReviews(Long id) {
        Call<ReviewsDTO> call = new RetrofitStarter().getReviewsService().getReviews(id, getString(R.string.the_movie_db_api_key));
        call.enqueue(new Callback<ReviewsDTO>() {
            @Override
            public void onResponse(Call<ReviewsDTO> call, Response<ReviewsDTO> response) {

                addReviewsToList(response);
            }

            @Override
            public void onFailure(Call<ReviewsDTO> call, Throwable t) {
                Log.e(TAG, "error loading reviews: " + t.getMessage());

            }
        });
    }

    /**
     * adiciona os reviews a lista de reviews
     *
     * @param response
     */
    private void addReviewsToList(Response<ReviewsDTO> response) {
        ReviewsDTO reviewsDTO = response.body();
        this.reviewsList.addAll(reviewsDTO.getReviews());
        reviewsAdapter = new ReviewsAdapter(reviewsList, MovieDetailActivity.this);
        mRecyclerViewReviews.setAdapter(reviewsAdapter);
    }


    /**
     * converte a data para padrão br
     *
     * @param date
     * @return
     */
    private String getDateConverted(String date) {
        date = date.replaceAll("-", "/");
        return date;
    }


    /**
     * abre o trailer no youtube
     *
     * @param position
     */
    public void onItemClick(int position) {
        Trailer trailer = trailerList.get(position);
        Intent openVideoOnYoutube = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
        startActivity(openVideoOnYoutube);
    }

}


