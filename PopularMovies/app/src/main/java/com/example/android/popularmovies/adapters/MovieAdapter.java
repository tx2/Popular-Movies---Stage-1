package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.interfaces.ListItemClickListener;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by jorgemendes on 05/02/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final ListItemClickListener mOnClickListener;

    private Movie[] mMovies;

    /**
     * Constructor for MovieAdapter that accepts a list of movies to display and the specification
     * for the ListItemClickListener.
     *
     * @param movies List of movies to display.
     * @param mOnClickListener Listener for list item clicks.
     */
    public MovieAdapter(Movie[] movies, ListItemClickListener mOnClickListener) {
        this.mMovies = movies;
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(this.mMovies[position]);
    }

    @Override
    public int getItemCount() {
        return this.mMovies.length;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView moviePosterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            this.moviePosterImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);

            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            this.moviePosterImageView.setImageDrawable(null);
            Picasso.with(this.moviePosterImageView.getContext())
                    .load(movie.posterUrl)
                    .into(this.moviePosterImageView);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
