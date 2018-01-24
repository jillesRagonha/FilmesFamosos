package br.com.agilles.filmesfamosos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.agilles.filmesfamosos.R;
import br.com.agilles.filmesfamosos.models.Review;

/**
 * Created by jille on 23/01/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private OnItemClicked onClick;

    public interface OnItemClicked {
        void onItemClick(int position);
    }

    private List<Review> reviewsList;
    private Context context;

    public ReviewsAdapter(List<Review> reviewsList, Context context) {
        this.reviewsList = reviewsList;
        this.context = context;
    }


    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForItem = R.layout.reviews_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForItem, parent, false);
        return new ReviewsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        Review review = reviewsList.get(position);
        holder.mReviewAuthorTextView.setText(review.getAutorName());
        holder.mReviewContent.setText(review.getReview());
        holder.mReviewContent.setOnClickListener(view -> onClick.onItemClick(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {

        public final TextView mReviewAuthorTextView;
        public final TextView mReviewContent;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            this.mReviewAuthorTextView = itemView.findViewById(R.id.tv_review_autor_name);
            this.mReviewContent = itemView.findViewById(R.id.tv_review_text);
        }
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }

}

