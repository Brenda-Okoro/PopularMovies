package com.example.brenda.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brenda.popularmovies.R;
import com.example.brenda.popularmovies.models.Review;

import java.util.List;

/**
 * Created by brenda on 9/21/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviews;
    private Context context;

    public ReviewAdapter(Context context, java.util.List<Review> reviews){
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);

        return new ReviewAdapter.ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.authorTV.setText(review.getAuthor());
        holder.reviewTV.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final TextView authorTV;
        private final TextView reviewTV;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            authorTV = (TextView) itemView.findViewById(R.id.author_tv);
            reviewTV = (TextView) itemView.findViewById(R.id.review_tv);
        }
    }


}
