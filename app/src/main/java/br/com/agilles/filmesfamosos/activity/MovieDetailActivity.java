package br.com.agilles.filmesfamosos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.agilles.filmesfamosos.R;
import br.com.agilles.filmesfamosos.models.Movie;

public class MovieDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        TextView mTitleMovieTextView = findViewById(R.id.tv_title_movie_detail);
        TextView mMovieOverviewTextView = findViewById(R.id.tv_overview);
        ImageView mMoviePosterDetailImageView = findViewById(R.id.iv_movies_poster_detail);
        RatingBar mVoteAverageRatingBar = findViewById(R.id.rb_vote_average);
        TextView mVoteAverageTextView = findViewById(R.id.tv_vote_average);
        TextView mMovieReleaseDateTextView = findViewById(R.id.tv_release_date);

        Intent intent = getIntent();
        Movie movie = (Movie) intent.getSerializableExtra("selectedMovie");


        mTitleMovieTextView.setText(movie.getTitle());
        mMovieOverviewTextView.setText(movie.getOverview());
        mVoteAverageRatingBar.setRating(movie.getVoteAverage());
        mVoteAverageTextView.setText("Vote Average: (" + movie.getVoteAverage() + " )");
        mMovieReleaseDateTextView.setText(getDateConverted(movie.getReleaseDate()));

        String imagemUri = movie.getPosterPath();
        Picasso
                .with(this)
                .setIndicatorsEnabled(true);
        Picasso
                .with(this)
                .load(imagemUri)
                .into(mMoviePosterDetailImageView);


    }

    private String getDateConverted(String date)  {
        date = date.replaceAll("-", "/");
        return date;

    }
}
