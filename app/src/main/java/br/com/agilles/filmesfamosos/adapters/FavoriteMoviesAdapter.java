package br.com.agilles.filmesfamosos.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.agilles.filmesfamosos.R;
import br.com.agilles.filmesfamosos.data.contract.DBContract;
import br.com.agilles.filmesfamosos.models.Movie;

/**
 * Created by jille on 28/01/2018.
 */

public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.FavoriteMoviesViewHolder> {

    private OnItemClicked onItemClicked;
    private final List<Movie> moviesList;

    public interface OnItemClicked {
        void onItemClicked(int position);
    }

    private Cursor mCursor;
    private final Context mContext;


    public FavoriteMoviesAdapter(Context mContext, List<Movie> moviesList) {
        this.mContext = mContext;
        this.moviesList = moviesList;


    }


    @Override
    public FavoriteMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.filme_item, parent, false);
        return new FavoriteMoviesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(FavoriteMoviesViewHolder holder, int position) {

        mCursor.moveToPosition(position);
        Movie movie = new Movie();
        int idIndex = mCursor.getColumnIndex(DBContract.FavoriteMoviesEntry._ID);
        int keyIndex = mCursor.getColumnIndex(DBContract.FavoriteMoviesEntry.COLUMN_NAME_KEY);
        int posterIndex = mCursor.getColumnIndex(DBContract.FavoriteMoviesEntry.COLUMN_NAME_POSTERPATH);
        int overviewIndex = mCursor.getColumnIndex(DBContract.FavoriteMoviesEntry.COLUMN_NAME_OVERVIEW);
        int titleIndex = mCursor.getColumnIndex(DBContract.FavoriteMoviesEntry.COLUMN_NAME_TITLE);
        final int id = mCursor.getInt(idIndex);
        final Long key = mCursor.getLong(keyIndex);
        final String posterPath = mCursor.getString(posterIndex);
        final String title = mCursor.getString(titleIndex);


        Picasso
                .with(mContext)
                .load(posterPath)
                .into(holder.mPosterImage);


        holder.mPosterImage.setOnClickListener(view -> onItemClicked.onItemClicked(holder.getAdapterPosition()));

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public void setOnCLick(OnItemClicked onCLick) {
        this.onItemClicked = onCLick;
    }


    class FavoriteMoviesViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mPosterImage;


        public FavoriteMoviesViewHolder(View itemView) {
            super(itemView);
            mPosterImage = itemView.findViewById(R.id.iv_movies_poster);
        }
    }


}
