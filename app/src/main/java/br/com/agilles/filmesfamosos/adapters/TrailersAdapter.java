package br.com.agilles.filmesfamosos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.agilles.filmesfamosos.R;
import br.com.agilles.filmesfamosos.models.Trailer;

/**
 * Created by jille on 22/01/2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {
    private OnTrailerClicked onClick;

    public interface OnTrailerClicked {
        void onItemClick(int position);
    }

    private final List<Trailer> trailerList;
    private final Context context;

    public TrailersAdapter(List<Trailer> trailerList, Context context) {
        this.trailerList = trailerList;
        this.context = context;
    }


    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForItem = R.layout.trailers_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForItem, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        Trailer trailer = trailerList.get(position);
        String nomeTrailer = trailer.getName();
        Picasso.with(context)
                .load(trailer.getCoverArtPath())
                .into(holder.mTrailerCoverImageView);

        holder.mTrailerNameTextView.setText(nomeTrailer);
        holder.mTrailerCoverImageView.setOnClickListener(view -> onClick.onItemClick(holder.getAdapterPosition()));
        holder.mTrailerNameTextView.setOnClickListener(view -> onClick.onItemClick(holder.getAdapterPosition()));


    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTrailerNameTextView;
        public final ImageView mTrailerCoverImageView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            this.mTrailerNameTextView = itemView.findViewById(R.id.tv_trailer_name);
            this.mTrailerCoverImageView = itemView.findViewById(R.id.iv_trailer_cover);
        }
    }
    public void setOnClick(OnTrailerClicked onClick) {
        this.onClick = onClick;
    }

}
