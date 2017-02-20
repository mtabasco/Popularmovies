package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.bean.Review;
import com.example.android.popularmovies.bean.Video;
import com.example.android.popularmovies.listener.OnMovieClickListener;
import com.example.android.popularmovies.listener.OnVideoClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Coco on 16/02/2017.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private List<Video> mVideosList;
    private Context mContext;

    private OnVideoClickListener onVideoClickListener;

    public VideosAdapter(Context context, List<Video> videos) {

        mContext =  context;
        mVideosList = videos;
    }


    public void setOnVideoClickListener(OnVideoClickListener onVideoClickListener) {
        this.onVideoClickListener = onVideoClickListener;
    }

    @Override
    public VideosAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_video, null);

        return new VideosAdapter.VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideosAdapter.VideoViewHolder holder, int position) {

        final Video video = mVideosList.get(position);

        String urlImage = mContext.getString(R.string.tmdb_prefix_preview_videos) + video.getKey() + mContext.getString(R.string.tmdb_sufix_preview_videos);

        //Render image using Picasso library
        if (!TextUtils.isEmpty(video.getKey())) {
            Picasso.with(mContext)
                    .load(urlImage)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_no_image)
                    .into(holder.videoPreview);
        }

        holder.videoName.setText(video.getName());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVideoClickListener.onVideoClick(video);
            }
        };
        holder.videoPreview.setOnClickListener(listener);

    }




    @Override
    public int getItemCount() {
        return (null != mVideosList ? mVideosList.size():0);
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView videoPreview;
        TextView videoName;

        public VideoViewHolder(View view) {
            super(view);
            videoPreview = (ImageView) view.findViewById(R.id.video_preview);
            videoName = (TextView) view.findViewById(R.id.video_name);

        }
    }
}
