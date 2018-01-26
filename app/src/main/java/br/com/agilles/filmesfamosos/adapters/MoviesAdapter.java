package br.com.agilles.filmesfamosos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.agilles.filmesfamosos.R;
import br.com.agilles.filmesfamosos.models.Movie;

/**
 * Created by Jilles on 05/12/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private OnItemClicked onClick;

    public interface OnItemClicked {
        void onItemClick(int position);
    }

    private final List<Movie> moviesList;
    private final Context context;


    public MoviesAdapter(List<Movie> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }


    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForItem = R.layout.filme_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutIdForItem, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        Movie movie = moviesList.get(position);
        String imagemUri = movie.getPosterPath();
        Picasso
                .with(context)
                .load(imagemUri)
                .into(holder.mPosterMovieImageView);
        holder.mPosterMovieImageView.setOnClickListener(view -> onClick.onItemClick(holder.getAdapterPosition()));

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    public class MoviesViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mPosterMovieImageView;


        public MoviesViewHolder(View view) {
            super(view);
            mPosterMovieImageView = view.findViewById(R.id.iv_movies_poster);
        }
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }
}
