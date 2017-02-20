package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.bean.Review;

import java.util.List;

/**
 * Created by Coco on 16/02/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private List<Review> mReviewsList;
    private Context mContext;

    public ReviewsAdapter(Context context, List<Review> reviews) {

        mContext =  context;
        mReviewsList = reviews;
    }



    @Override
    public ReviewsAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_review, null);

        return new ReviewsAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewViewHolder holder, int position) {

        final Review review = mReviewsList.get(position);

        holder.personPhoto.setText(review.getAuthor().substring(0,1).toUpperCase());
        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());

    }




    @Override
    public int getItemCount() {
        return (null != mReviewsList ? mReviewsList.size():0);
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView personPhoto;
        TextView author;
        TextView content;

        public ReviewViewHolder(View view) {
            super(view);
            personPhoto = (TextView) view.findViewById(R.id.person_photo);
            author = (TextView) view.findViewById(R.id.review_author);
            content = (TextView) view.findViewById(R.id.review_content);

        }
    }
}
